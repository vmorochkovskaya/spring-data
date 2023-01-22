package com.epam.cache.caching;

import com.epam.cache.Entry;

import java.util.Map;

public interface ICache {
    Entry get(String key);

    void put(String key, Entry entry);

    Map<String, Entry> getValueMap();
}
