package com.ssh.common.core.proxy;

import com.ssh.common.core.annotation.Param;
import com.ssh.common.core.annotation.Query;
import com.ssh.common.core.annotation.RepositoryBean;
import com.ssh.common.core.entity.Entity;
import com.ssh.common.core.repository.CrudRepository;
import com.ssh.common.core.repository.DynamicQueryRepository;
import com.ssh.common.core.repository.transform.AliasToBeanResultTransformer;
import com.ssh.common.dto.DTO;
import com.ssh.common.page.Page;
import com.ssh.common.page.Pageable;
import com.ssh.common.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RepositoryMethod {

    private final Class<?> repositoryInterface;
    private final Method method;

    public RepositoryMethod(Class<?> repositoryInterface, Method method) {
        this.repositoryInterface = repositoryInterface;
        this.method = method;
    }

    public Object execute(DynamicQueryRepository dynamicQueryRepository, Object[] args) throws Exception {
        final RepositoryBean repositoryBean = repositoryInterface.getAnnotation(RepositoryBean.class);
        if (repositoryBean == null) {
            throw new RuntimeException("The target repository must be marked with a RepositoryBean annotation.");
        }

        final Query query = method.getAnnotation(Query.class);
        if (query == null) {
            return crudProcess(dynamicQueryRepository, method, args);
        }

        String namespace = getValue(repositoryBean.namespace(), method.getDeclaringClass().getName());
        String statementId = getValue(query.value(), method.getName());
        ResultTransformer transformer = getResultTransformer(query.transformer());

        String statement = applyNamespace(namespace, statementId);

        Class<?> returnType = method.getReturnType();
        String[] paramNames = getParamNames(method);

        if (Collection.class.isAssignableFrom(returnType)) {
            return queryForList(dynamicQueryRepository, statement, paramNames, args, transformer);
        }

        if (Page.class.isAssignableFrom(returnType)) {
            return queryForPage(dynamicQueryRepository, statement, args, transformer);
        }

        return queryForSingle(dynamicQueryRepository, statement, paramNames, args, transformer);
    }

    private String getValue(String value, String defaultValue) {
        return StringUtils.isNotEmpty(value) ? value : defaultValue;
    }

    private String applyNamespace(String namespace, String statementId) {
        return namespace + Constant.PERIOD_SEPARATOR + statementId;
    }

    private ResultTransformer getResultTransformer(Class<?> mappedClass) {
        if (Map.class.isAssignableFrom(mappedClass)) {
            return Transformers.ALIAS_TO_ENTITY_MAP;
        }
        if (DTO.class.isAssignableFrom(mappedClass) || Entity.class.isAssignableFrom(mappedClass)) {
            return AliasToBeanResultTransformer.newInstance(mappedClass);
        }
        return null;
    }

    private Class getEntityClass(Class<?> repositoryInterface) {
        return (Class) ((ParameterizedType) repositoryInterface.getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }

    private Method findTargetMethod(String methodName, Class<?>... paramTypes) {
        try {
            return DynamicQueryRepository.class.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private Object crudProcess(DynamicQueryRepository dynamicQueryRepository, Method method, Object[] args) throws Exception {
        if (CrudRepository.class.isAssignableFrom(method.getDeclaringClass())) {
            Method targetMethod = null;
            Object[] arguments = null;
            if (args == null) {
                targetMethod = findTargetMethod(method.getName());
                arguments = new Object[0];
            } else if (args.length == 1) {
                if (args[0] instanceof Entity || args[0] instanceof Iterable) {
                    targetMethod = findTargetMethod(method.getName(), method.getParameterTypes());
                    arguments = args;
                } else if (args[0] instanceof Serializable) {
                    targetMethod = findTargetMethod(method.getName(), Class.class, Serializable.class);
                    arguments = new Object[]{getEntityClass(repositoryInterface), args[0]};
                }
            } else {
                throw new IllegalArgumentException("The CrudRepository does not support multiple parameters.");
            }
            if (targetMethod != null) {
                return targetMethod.invoke(dynamicQueryRepository, arguments);
            } else {
                throw new NoSuchMethodException("Can not find target method from CrudRepository.");
            }
        } else {
            throw new IllegalArgumentException("The Method signature must be inherited from the CrudRepository or marked with a Query annotation.");
        }
    }

    private List queryForList(DynamicQueryRepository dynamicQueryRepository, String statement,
                              String[] paramNames, Object[] args, ResultTransformer transformer) {
        if (paramNames.length > 0) {    // Query use the "name" placeholder
            return dynamicQueryRepository.getList(statement, paramNames, args, transformer);
        } else {
            if (args.length == 1) {
                if (args[0] instanceof DTO) {
                    return dynamicQueryRepository.getList(statement, (DTO) args[0], transformer);
                } else {                // Query use the "?" placeholder
                    return dynamicQueryRepository.getList(statement, args, transformer);
                }
            } else {
                throw new IllegalArgumentException("If there are multiple parameters, you must mark with a Param annotation.");
            }
        }
    }

    private Page queryForPage(DynamicQueryRepository dynamicQueryRepository, String statement, Object[] args, ResultTransformer transformer) {
        if (args.length == 1) {
            if (args[0] instanceof Pageable) {
                return dynamicQueryRepository.getPage(statement, (Pageable) args[0], transformer);
            } else {
                throw new IllegalArgumentException("The parameter must implement the Pageable interface.");
            }
        } else {
            throw new IllegalArgumentException("The paging method does not support multiple parameters.");
        }
    }

    private Object queryForSingle(DynamicQueryRepository dynamicQueryRepository, String statement,
                                  String[] paramNames, Object[] args, ResultTransformer transformer) {
        if (paramNames.length > 0) {    // Query use the "name" placeholder
            return dynamicQueryRepository.getOne(statement, paramNames, args, transformer);
        } else {
            if (args.length == 1) {     // Query use the "?" placeholder
                return dynamicQueryRepository.getOne(statement, args, transformer);
            } else {
                throw new IllegalArgumentException("If there are multiple parameters, you must mark with a Param annotation.");
            }
        }
    }

    private String[] getParamNames(Method method) {
        if (!hasNamedParams(method)) {
            return new String[0];
        }
        List<String> paramNames = new ArrayList<>();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            paramNames.add(getParamNameFromAnnotation(method, i));
        }
        return paramNames.toArray(new String[paramNames.size()]);
    }

    private boolean hasNamedParams(Method method) {
        final Annotation[][] annotations = method.getParameterAnnotations();
        for (Annotation[] annotation : annotations) {
            for (Annotation param : annotation) {
                if (param instanceof Param) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getParamNameFromAnnotation(Method method, int index) {
        String paramName = String.valueOf(index);
        final Annotation[] annotations = method.getParameterAnnotations()[index];
        for (Annotation annotation : annotations) {
            if (annotation instanceof Param) {
                paramName = ((Param) annotation).value();
            }
        }
        return paramName;
    }

}
