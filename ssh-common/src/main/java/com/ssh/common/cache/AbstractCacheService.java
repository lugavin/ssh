package com.ssh.common.cache;

import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractCacheService implements CacheService, InitializingBean {

    protected static final int DEFAULT_MAX_AGE = 720;

    @Override
    public boolean set(String key, Object value) {
        return set(key, value, DEFAULT_MAX_AGE);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        try {
            return clazz.cast(get(key));
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        checkCacheConfig();
    }

    protected abstract void checkCacheConfig() throws IllegalArgumentException;

}
