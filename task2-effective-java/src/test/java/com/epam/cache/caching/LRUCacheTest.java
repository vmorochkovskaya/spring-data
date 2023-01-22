package com.epam.cache.caching;

import com.epam.cache.ConfigReader;
import com.epam.cache.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LRUCacheTest {
    private ConfigReader configReader;

    @BeforeEach
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = LRUCache.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        configReader = mock(ConfigReader.class);
        when(configReader.getExpireDuration()).thenReturn(10000L);
    }

    @Test
    public void testPutWithWithTwoEqualKeysShouldReplace() {
        ICache lruCache = LRUCache.getInstance(2, configReader);

        lruCache.put("key-1", new Entry("entry-1"));
        lruCache.put("key-1", new Entry("entry-2"));

        assertEquals("entry-2", lruCache.get("key-1").getStr());
    }

    @Test
    public void testEvictOldValue() {
        ICache lruCache = LRUCache.getInstance(2, configReader);

        lruCache.put("key-1", new Entry("entry-1"));
        lruCache.put("key-1", new Entry("entry-12"));
        lruCache.put("key-2", new Entry("entry-2"));
        lruCache.put("key-3", new Entry("entry-3"));

        assertNull(lruCache.get("key-1"));
    }

    @Test
    public void testGetValueMap() {
        ICache lruCache = LRUCache.getInstance(2, configReader);

        lruCache.put("key-1", new Entry("entry-1"));
        lruCache.put("key-2", new Entry("entry-2"));

        assertEquals(2, lruCache.getValueMap().entrySet().size());
    }

    @Test
    public void testStats() {
        LRUCache lruCache = LRUCache.getInstance(1, configReader);

        lruCache.put("key-1", new Entry("entry-1"));
        lruCache.get("key-1");
        lruCache.get("key-2");
        lruCache.put("key-2", new Entry("entry-2"));

        assertNotNull(lruCache.getStats());
        assertEquals(1L, lruCache.getStats().hitCount());
        assertEquals(1L, lruCache.getStats().missCount());
        assertEquals(1L, lruCache.getStats().evictionCount());
    }
}
