package com.ssh.common.cache;

public interface CacheService {

    boolean set(String key, Object value);

    boolean set(String key, Object value, int maxAge);

    Object get(String key);

    <T> T get(String key, Class<T> clazz);

    boolean delete(String key);

}
