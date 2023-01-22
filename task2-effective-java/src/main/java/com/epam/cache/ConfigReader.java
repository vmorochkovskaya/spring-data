package com.epam.cache;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ConfigReader {
    public long getExpireDuration() {
        try (InputStream input = new FileInputStream("src/main/resources/cacheConfig.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return Long.parseLong(prop.getProperty("expireAfterAccess"));
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return 0;
        }
    }
}
