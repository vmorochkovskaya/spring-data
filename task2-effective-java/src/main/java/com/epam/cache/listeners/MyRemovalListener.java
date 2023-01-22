package com.epam.cache.listeners;

import com.epam.cache.Entry;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyRemovalListener implements RemovalListener<String, Entry> {

    @Override
    public void onRemoval(RemovalNotification<String, Entry> entry) {
        log.info("{} was removed from cache. Cause: {}", entry.getValue(), entry.getCause());
    }
}
