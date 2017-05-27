package com.ssh.common.validation;

import com.ssh.common.exception.ValidationException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Constraint;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see org.springframework.core.MethodParameter
 * @see org.springframework.core.annotation.AnnotationUtils
 * @see org.springframework.validation.beanvalidation.BeanValidationPostProcessor
 * @see org.springframework.validation.beanvalidation.MethodValidationInterceptor
 * @see org.springframework.validation.beanvalidation.MethodValidationPostProcessor
 */
public class MethodValidationInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodValidationInterceptor.class);

    private static final Map<Method, Method> methodCache = new ConcurrentHashMap<>();

    private final ValidationProcessor validationProcessor;

    public MethodValidationInterceptor() {
        this(Validation.buildDefaultValidatorFactory());
    }

    public MethodValidationInterceptor(ValidatorFactory validatorFactory) {
        this(validatorFactory.getValidator());
    }

    public MethodValidationInterceptor(Validator validator) {
        this.validationProcessor = new ValidationProcessor(validator);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameterValues = invocation.getArguments();
        String methodName = method.getName();
        Object target = invocation.getThis();

        LOGGER.debug("method " + method + " is called on " + target + " with args " + Arrays.toString(parameterValues));

        Method targetMethod = methodCache.get(method);
        if (targetMethod == null) {
            targetMethod = findTargetInterfaceMethod(target, methodName, parameterTypes);
            if (targetMethod == null) {
                return invocation.proceed();
            }
            methodCache.put(method, targetMethod);
        }

        if (!hasConstraintParameter(targetMethod)) {
            return invocation.proceed();
        }

        // (1)校验方法入参
        List<String> violations = validationProcessor.validateParameters(target, targetMethod, parameterValues);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.toString());
        }

        // (2)校验方法入参内的属性
        if (parameterValues == null || parameterValues.length < 1) {
            return invocation.proceed();
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
        return invocation.proceed();
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
