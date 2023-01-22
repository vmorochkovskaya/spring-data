package com.epam.cache.caching;

import com.epam.cache.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class LFUCacheTest {
    @BeforeEach
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = LRUCache.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void testPutWithWithTwoEqualKeysShouldReplace() {
        ICache lfuCache = LFUCache.getInstance(2);

        lfuCache.put("key-1", new Entry("entry-1"));
        lfuCache.put("key-1", new Entry("entry-2"));

        assertEquals("entry-2", lfuCache.get("key-1").getStr());
    }

    @Test
    public void testPutWithWithTwoEqualKeysShouldIncreaseFrequency() {
        ICache lfuCache = LFUCache.getInstance(2);

        lfuCache.put("key-1", new Entry("entry-1"));
        lfuCache.put("key-1", new Entry("entry-12"));
        lfuCache.put("key-2", new Entry("entry-2"));
        lfuCache.put("key-3", new Entry("entry-3"));

        assertNull(lfuCache.get("key-2"));
    }

    @Test
    public void testPutWithWithReachedMaxSizeShouldEvict() {
        ICache lfuCache = LFUCache.getInstance(2);

        lfuCache.put("key-1", new Entry("entry-1"));
        lfuCache.get("key-1");
        lfuCache.get("key-1");
        lfuCache.put("key-2", new Entry("entry-2"));
        lfuCache.put("key-3", new Entry("entry-3"));

        assertNull(lfuCache.get("key-2"));
    }

    @Test
    public void testGetValueMap() {
        ICache lfuCache = LFUCache.getInstance(2);

        lfuCache.put("key-1", new Entry("entry-1"));
        lfuCache.put("key-2", new Entry("entry-2"));

        assertEquals(2, lfuCache.getValueMap().entrySet().size());
    }

    @Test
    public void testNumberOfEvictions() {
        LFUCache lfuCache = LFUCache.getInstance(2);

        lfuCache.put("key-1", new Entry("entry-1"));
        lfuCache.put("key-2", new Entry("entry-2"));

        assertEquals(2, lfuCache.getNumberOfEvictions());
    }
}