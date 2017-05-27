package com.ssh.common.core.proxy;

import com.ssh.common.core.repository.DynamicQueryRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import static org.springframework.util.Assert.notNull;

/**
 * Interface to be implemented by objects used within a {@link BeanFactory}
 * which are themselves factories. If a bean implements this interface,
 * it is used as a factory for an object to expose, not directly as a bean
 * instance that will be exposed itself.
 *
 * @author Gavin
 * @see org.mybatis.spring.mapper.MapperFactoryBean
 */
public class RepositoryFactoryBean<T> implements FactoryBean<T>, InitializingBean {

    private Class<T> repositoryInterface;

    private DynamicQueryRepository dynamicQueryRepository;

    public void setRepositoryInterface(Class<T> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }

    public void setDynamicQueryRepository(DynamicQueryRepository dynamicQueryRepository) {
        this.dynamicQueryRepository = dynamicQueryRepository;
    }

    @Override
    public T getObject() throws Exception {
        return new RepositoryProxyFactory<>(repositoryInterface).newInstance(dynamicQueryRepository);
    }

    @Override
    public Class<?> getObjectType() {
        return repositoryInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(repositoryInterface, "Property 'repositoryInterface' is required");
        notNull(dynamicQueryRepository, "Property 'dynamicQueryRepository' is required");
    }

}
