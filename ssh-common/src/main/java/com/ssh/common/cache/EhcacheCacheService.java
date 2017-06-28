package com.ssh.common.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class EhcacheCacheService extends AbstractCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EhcacheCacheService.class);

    private CacheManager cacheManager;

    private String cacheName;

    @Override
    public boolean set(String key, Object value, int maxAge) {
        Cache cache = cacheManager.getCache(cacheName);
        try {
            cache.put(new Element(key, value));
            return true;
        } catch (Exception e) {
            LOGGER.error(String.format("缓存%s时被中断操作 ", key), e);
            return false;
        }
    }

    @Override
    public Object get(String key) {
        try {
            return cacheManager.getCache(cacheName).get(key).getObjectValue();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean delete(String key) {
        try {
            return cacheManager.getCache(cacheName).remove(key);
        } catch (Exception e) {
            LOGGER.error(String.format("删除缓存%s时被中断操作 ", key), e);
            return false;
        }
    }

    @Override
    public void checkCacheConfig() throws IllegalArgumentException {
        Assert.notNull(cacheManager, "Property 'cacheManager' is required.");
        Assert.hasText(cacheName, "Property 'cacheName' must have text; it must not be null, empty, or blank.");
        Assert.notNull(cacheManager.getCache(cacheName), String.format("Can't find the cache associated with the given name[%s].", cacheName));
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

}
