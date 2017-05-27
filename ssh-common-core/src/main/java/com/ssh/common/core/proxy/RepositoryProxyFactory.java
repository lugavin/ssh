package com.ssh.common.core.proxy;

import com.ssh.common.core.repository.DynamicQueryRepository;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gavin
 * @see org.apache.ibatis.binding.MapperProxyFactory
 */
public class RepositoryProxyFactory<T> {

    private final Class<T> repositoryInterface;
    private Map<Method, RepositoryMethod> methodCache = new ConcurrentHashMap<>();

    public RepositoryProxyFactory(Class<T> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }

    public Class<T> getRepositoryInterface() {
        return repositoryInterface;
    }

    public T newInstance(DynamicQueryRepository dynamicQueryRepository) {
        return newInstance(new RepositoryProxy<>(repositoryInterface, dynamicQueryRepository, methodCache));
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(RepositoryProxy<T> repositoryProxy) {
        /**
         * 第一个参数: 设置代码使用的类加载器, 一般采用与目标类相同的类加载器;
         * 第二个参数: 设置代理类实现的接口, 跟目标类使用相同的接口;
         * 第三个参数: 设置回调对象, 当代理对象的方法被调用时, 会调用该参数指定对象的invoke方法.
         */
        return (T) Proxy.newProxyInstance(repositoryInterface.getClassLoader(), new Class[]{repositoryInterface}, repositoryProxy);
    }

}
