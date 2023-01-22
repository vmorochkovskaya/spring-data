package com.epam.cache.caching;

import com.epam.cache.Entry;
import com.epam.cache.listeners.MyRemovalListener;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LFUCache implements ICache {
    private volatile static LFUCache instance;
    private static final List<RemovalListener<String, Entry>> removalEvents = new ArrayList<>();
    private final Map<String, Entry> valueMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> countMap = new ConcurrentHashMap<>();
    private final int capacity;
    private int numberOfCacheEvictions;

    private LFUCache(int capacity) {
        this.capacity = capacity;
    }

    public static LFUCache getInstance(int capacity) {
        if (instance == null) {
            synchronized (LFUCache.class) {
                if (instance == null) {
                    instance = new LFUCache(capacity);
                    removalEvents.add(new MyRemovalListener());
                }
            }
        }
        return instance;
    }

    @Override
    public Entry get(String key) {
        if (!valueMap.containsKey(key) || capacity == 0) {
            return null;
        }
        increaseFrequency(key);
        return valueMap.get(key);
    }

    public void put(String key, Entry entry) {
        if (valueMap.containsKey(key)) {
            synchronized (valueMap) {
                valueMap.put(key, entry);
                increaseFrequency(key);
            }
        } else if (capacity > 0) {
            if (valueMap.size() == capacity) {
                synchronized (valueMap) {
                    if (valueMap.size() == capacity) {
                        evict();
                    }
                }
            }
            synchronized (valueMap) {
                valueMap.put(key, entry);
                increaseFrequency(key);
            }
        }
    }

    private void evict() {
        Comparator<Map.Entry<String, Integer>> valueComparator = Map.Entry.comparingByValue();
        Optional<Map.Entry<String, Integer>> minValue = countMap.entrySet().stream().min(valueComparator);
        var keyToDelete = minValue.get().getKey();
        var valueToDelete = valueMap.get(keyToDelete);
        valueMap.remove(keyToDelete);
        countMap.remove(keyToDelete);
        for (var removalEvent : removalEvents) {
            removalEvent.onRemoval(RemovalNotification.create(keyToDelete, valueToDelete, RemovalCause.SIZE));
        }
        numberOfCacheEvictions++;
    }

    private void increaseFrequency(String key) {
        countMap.compute(key, (k, v) -> {
            var freq = v == null ? 0 : v;
            return freq + 1;
        });
    }

    @Override
    public Map<String, Entry> getValueMap() {
        return valueMap;
    }

    public int getNumberOfEvictions() {
        return numberOfCacheEvictions;
    }
}
