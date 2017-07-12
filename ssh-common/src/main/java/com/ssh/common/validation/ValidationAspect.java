package com.ssh.common.validation;

import com.ssh.common.exception.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.validation.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目中, 常使用较多的是前端的JS校验, 前端校验的目的是为了提高合法用户的体验, 减轻服务器的压力;
 * 服务端校验则是为了防止一些非法用户绕开前端的JS校验对系统进行访问, 服务端校验主要集中在Controller和Service层;
 * Controller层负责校验页面请求参数的合法性(防止非法用户绕开前端的JS校验对系统进行访问),
 * Service层负责校验关键业务参数(仅限于Service接口中使用的参数),
 * DAO层一般不对参数进行校验(参数不合法直接抛出数据库异常).
 * <p/>
 * Description: Service接口方法參數校驗攔截器
 * Author: Gavin
 * Date: Nov 19, 2016 9:30:00 PM
 * Version: 1.0
 *
 * @see org.springframework.core.MethodParameter
 * @see org.springframework.core.annotation.AnnotationUtils
 * @see org.springframework.validation.beanvalidation.BeanValidationPostProcessor
 * @see org.springframework.validation.beanvalidation.MethodValidationInterceptor
 * @see org.springframework.validation.beanvalidation.MethodValidationPostProcessor
 * @see org.springframework.core.LocalVariableTableParameterNameDiscoverer
 */
public class ValidationAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationAspect.class);

    private final Map<Method, Method> methodCache = new ConcurrentHashMap<>();

    private final ParameterNameDiscoverer parameterNameDiscoverer;
    private final ValidationProcessor validationProcessor;

    public ValidationAspect() {
        this(Validation.buildDefaultValidatorFactory());
    }

    public ValidationAspect(ValidatorFactory validatorFactory) {
        this(validatorFactory.getValidator());
    }

    public ValidationAspect(Validator validator) {
        this.validationProcessor = new ValidationProcessor(validator);
        this.parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    public void validate(JoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();

        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Object[] parameterValues = joinPoint.getArgs();

        LOGGER.info("invoke method [{}] with args {}", method, Arrays.toString(parameterValues));

        Method targetMethod = methodCache.get(method);
        if (targetMethod == null) {
            targetMethod = findTargetInterfaceMethod(target, methodName, parameterTypes);
            if (targetMethod == null) {
                return;
            }
            methodCache.put(method, targetMethod);
        }

        if (!hasConstraintParameter(targetMethod)) {
            return;
        }

        // (1)校验方法入参
        List<String> violations = validationProcessor.validateParameters(target, targetMethod, parameterValues);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.toString());
        }

        // (2)校验方法入参内的属性
        if (parameterValues == null || parameterValues.length < 1) {
            return;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            Object parameterValue = parameterValues[i];
            if (parameterValue == null) {
                continue;
            }
            Validated validated = getMethodGroupAnnotation(targetMethod, i, Validated.class);
            if (validated == null) {
                continue;
            }
            if (parameterValue instanceof Iterable) {
                violations = validationProcessor.validate((Iterable<?>) parameterValue, validated.value());
            } else {
                violations = validationProcessor.validate(parameterValue, validated.value());
            }
            if (!violations.isEmpty()) {
                throw new ValidationException(violations.toString());
            }
        }
    }

    /**
     * @param target         the target object
     * @param methodName     the name of the method
     * @param parameterTypes the list of parameters
     * @return the method from the interface, or {@code null} if not found
     */
    private Method findTargetInterfaceMethod(Object target, String methodName, Class<?>[] parameterTypes) {
        Class<?>[] interfaces = target.getClass().getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            Method[] methods = interfaceClass.getMethods();
            for (Method method : methods) {
                // if (Object.class.equals(method.getDeclaringClass())) {
                //     continue;
                // }
                if (Objects.equals(method.getName(), methodName) && equals(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * @see java.util.Arrays#equals(Object[], Object[])
     */
    private boolean equals(Class<?>[] a1, Class<?>[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }
        if (a2.length != a1.length) {
            return false;
        }
        for (int i = 0; i < a1.length; i++) {
            Class<?> c1 = a1[i];
            Class<?> c2 = a2[i];
            if (!(c1 == null ? c2 == null : c1.isAssignableFrom(c2))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否需要校验方法入参
     */
    private boolean hasConstraintParameter(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations != null && parameterAnnotations.length > 0) {
            for (Annotation[] annotations : parameterAnnotations) {
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().isAnnotationPresent(Constraint.class)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> T getMethodGroupAnnotation(Method method, int parameterIndex, Class<T> annotationType) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Annotation[] annotations = parameterAnnotations[parameterIndex];
        for (Annotation annotation : annotations) {
            if (annotationType.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }

    static class ValidationProcessor {

        private final Validator validator;

        public ValidationProcessor(Validator validator) {
            this.validator = validator;
        }

        public <T> List<String> validate(T object, Class<?>... groups) {
            Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
            return getErrorMessage(violations);
        }

        public <T> List<String> validate(Iterable<T> iterable, Class<?>... groups) {
            for (T object : iterable) {
                List<String> errors = validate(object, groups);
                if (!errors.isEmpty()) {
                    return errors;
                }
            }
            return Collections.emptyList();
        }

        public <T> List<String> validateProperty(T object, String propertyName, Class<?>... groups) {
            Set<ConstraintViolation<T>> violations = validator.validateProperty(object, propertyName, groups);
            return getErrorMessage(violations);
        }

        public <T> List<String> validateParameters(T object, Method method, Object[] parameterValues, Class<?>... groups) {
            Set<ConstraintViolation<T>> violations = validator.forExecutables().validateParameters(object, method, parameterValues, groups);
            return getErrorMessage(violations);
        }

        public <T> List<String> validateReturnValue(T object, Method method, Object returnValue, Class<?>... groups) {
            Set<ConstraintViolation<T>> violations = validator.forExecutables().validateReturnValue(object, method, returnValue, groups);
            return getErrorMessage(violations);
        }

        public <T> List<String> getErrorMessage(Set<ConstraintViolation<T>> violations) {
            if (violations == null || violations.isEmpty()) {
                return Collections.emptyList();
            }
            List<String> list = new ArrayList<>();
            for (ConstraintViolation<T> violation : violations) {
                list.add(violation.getPropertyPath() + ":" + violation.getMessage());
            }
            return list;
        }

    }

}
