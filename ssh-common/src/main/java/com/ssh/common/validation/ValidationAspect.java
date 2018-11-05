package com.ssh.common.validation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Service接口方法參數校驗攔截器:
 * 项目中, 常使用较多的是前端的JS校验, 前端校验的目的是为了提高合法用户的体验, 减轻服务器的压力;
 * 服务端校验则是为了防止一些非法用户绕开前端的JS校验对系统进行访问, 服务端校验主要集中在Controller和Service层;
 * Controller层负责校验页面请求参数的合法性(防止非法用户绕开前端的JS校验对系统进行访问),
 * Service层负责校验关键业务参数(仅限于Service接口中使用的参数),
 * 而DAO层一般不对参数进行校验(参数不合法直接抛出数据库异常).
 *
 * @author Gavin
 * @version 1.0
 * @see com.alibaba.dubbo.validation.support.jvalidation.JValidator
 * @see org.springframework.validation.beanvalidation.BeanValidationPostProcessor
 * @see org.springframework.validation.beanvalidation.MethodValidationInterceptor
 * @see org.springframework.validation.beanvalidation.MethodValidationPostProcessor
 */
@Aspect
@Component
public class ValidationAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationAspect.class);

    private final ValidationProcessor validationProcessor;

    public ValidationAspect() {
        this(Validation.buildDefaultValidatorFactory());
    }

    @Inject
    public ValidationAspect(ValidatorFactory validatorFactory) {
        this(validatorFactory.getValidator());
    }

    public ValidationAspect(Validator validator) {
        this.validationProcessor = new ValidationProcessor(validator);
    }

    // @Pointcut(value = "execution(* com.ssh.*.api.service.*Service.*(..)) && @args(..)")
    // @Pointcut(value = "execution(* com.ssh.*.api.service.*Service.*(java.lang.String)) && args(param)", argNames = "param")
    // private void beforePointcut(String param) {}
    //
    // @AfterReturning(value = "beforePointcut(param)", argNames = "joinPoint,param,returnValue", returning = "returnValue")
    // public void invoke(JoinPoint joinPoint, String param, Object returnValue) throws Throwable {}

    @Pointcut("execution(* com.ssh.*.api.service.*Service.*(..))")
    private void validatePointcut() {
    }

    @Before("validatePointcut()")
    public void validate(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameterValues = joinPoint.getArgs();

        // LOGGER.info("Target Object => {}", joinPoint.getTarget().getClass().getName());
        // LOGGER.info("Proxy Object => {}", joinPoint.getThis().getClass().getName());
        LOGGER.debug("Invoke service method => {}.{}()", target.getClass().getName(), method.getName());

        Method targetMethod = findTargetInterfaceMethod(target, methodName, parameterTypes);
        if (targetMethod == null || !hasConstraintParameter(targetMethod)) {
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
    private <T extends Annotation> T getMethodGroupAnnotation(Method method, int paramIndex, Class<T> annotationType) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Annotation[] annotations = parameterAnnotations[paramIndex];
        for (Annotation annotation : annotations) {
            if (annotationType.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }

    public static class ValidationProcessor {

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
