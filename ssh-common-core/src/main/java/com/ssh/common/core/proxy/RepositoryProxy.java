package com.ssh.common.core.proxy;

import com.ssh.common.core.repository.DynamicQueryRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Gavin
 * @see org.apache.ibatis.binding.MapperProxy
 * @see com.ssh.common.core.repository.CrudRepository
 * @see com.ssh.common.core.repository.JpaRepository
 * @see com.ssh.common.core.repository.support.SimpleDynamicQueryRepository
 */
public class RepositoryProxy<T> implements InvocationHandler {

    private final Class<T> repositoryInterface;
    private final DynamicQueryRepository dynamicQueryRepository;
    private final Map<Method, RepositoryMethod> methodCache;

    public RepositoryProxy(Class<T> repositoryInterface, DynamicQueryRepository dynamicQueryRepository, Map<Method, RepositoryMethod> methodCache) {
        this.repositoryInterface = repositoryInterface;
        this.dynamicQueryRepository = dynamicQueryRepository;
        this.methodCache = methodCache;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        return cachedRepositoryMethod(method).execute(dynamicQueryRepository, args);
    }

    private RepositoryMethod cachedRepositoryMethod(Method method) {
        RepositoryMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null) {
            mapperMethod = new RepositoryMethod(repositoryInterface, method);
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }

}
