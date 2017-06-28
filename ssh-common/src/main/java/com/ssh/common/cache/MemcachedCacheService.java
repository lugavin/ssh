package com.ssh.common.cache;

import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutionException;

public class MemcachedCacheService extends AbstractCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedCacheService.class);

    private MemcachedClient memcachedClient;

    @Override
    public boolean set(String key, Object value, int maxAge) {
        try {
            return memcachedClient.set(key, maxAge, value).get();
        } catch (InterruptedException e) {
            LOGGER.error(String.format("缓存%s时被中断操作 ", key), e);
            return false;
        } catch (ExecutionException e) {
            LOGGER.error(String.format("缓存%s时执行异常,请检查缓存服务器", key));
            return false;
        }
    }

    @Override
    public Object get(String key) {
        try {
            return memcachedClient.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean delete(String key) {
        try {
            return memcachedClient.delete(key).get();
        } catch (InterruptedException e) {
            LOGGER.error(String.format("删除缓存%s时被中断操作 ", key), e);
            return false;
        } catch (ExecutionException e) {
            LOGGER.error(String.format("删除缓存%s时执行异常,请检查缓存服务器", key), e);
            return false;
        }
    }

    @Override
    protected void checkCacheConfig() throws IllegalArgumentException {
        Assert.notNull(memcachedClient, "Property 'memcachedClient' is required.");
    }

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

}
