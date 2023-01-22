package com.epam.cache.caching;

import com.epam.cache.ConfigReader;
import com.epam.cache.Entry;
import com.epam.cache.listeners.MyRemovalListener;
import com.google.common.base.Ticker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;

import java.time.Duration;
import java.util.Map;

public class LRUCache implements ICache {
    private volatile static LRUCache instance;
    private int capacity;
    private static Cache<String, Entry> cache;

    public static LRUCache getInstance(int capacity, ConfigReader configReader) {
        if (instance == null) {
            synchronized (LRUCache.class) {
                if (instance == null) {
                    instance = new LRUCache();
                    cache = CacheBuilder.newBuilder()
                            .removalListener(new MyRemovalListener())
                            .expireAfterAccess(Duration.ofMillis(configReader.getExpireDuration()))
                            .ticker(Ticker.systemTicker())
                            .recordStats()
                            .maximumSize(capacity).build();
                }
            }
        }
        return instance;
    }

    @Override
    public Entry get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(String key, Entry entry) {
        cache.put(key, entry);
    }

    public Map<String, Entry> getValueMap() {
        return cache.asMap();
    }

    public CacheStats getStats() {
        return cache.stats();
    }
}
