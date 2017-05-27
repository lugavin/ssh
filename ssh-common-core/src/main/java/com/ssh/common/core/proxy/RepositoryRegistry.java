package com.ssh.common.core.proxy;

import com.ssh.common.core.repository.DynamicQueryRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gavin
 * @see org.apache.ibatis.binding.MapperRegistry
 */
public class RepositoryRegistry {

    private final Map<Class<?>, RepositoryProxyFactory<?>> repositories = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getRepository(Class<T> type, DynamicQueryRepository dynamicQueryRepository) {
        final RepositoryProxyFactory<T> repositoryProxyFactory = (RepositoryProxyFactory<T>) repositories.get(type);
        if (repositoryProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the RepositoryRegistry.");
        }
        try {
            return repositoryProxyFactory.newInstance(dynamicQueryRepository);
        } catch (Exception e) {
            throw new RuntimeException("Error getting repository instance. Cause: " + e, e);
        }
    }

    public <T> void addRepository(Class<T> type) {
        if (type.isInterface()) {
            if (hasRepository(type)) {
                throw new RuntimeException("Type " + type + " is already known to the RepositoryRegistry.");
            }
            repositories.put(type, new RepositoryProxyFactory<>(type));
        }
    }

    public <T> boolean hasRepository(Class<T> type) {
        return repositories.containsKey(type);
    }

    public Collection<Class<?>> getRepositories() {
        return Collections.unmodifiableCollection(repositories.keySet());
    }

}
