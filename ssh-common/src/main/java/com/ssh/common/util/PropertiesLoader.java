package com.ssh.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public abstract class PropertiesLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

    private static final Properties properties = new Properties();

    public enum Config {
        PHASE_ENV, PAGE_SIZE, USE_CAPTCHA,
        DATE_FORMAT, DATETIME_FORMAT,
        COMPANY_NAME, PROJECT_NAME
    }

    public static void init(String... resources) {
        loadProperties(resources);
    }

    private static void loadProperties(String... resources) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        for (String location : resources) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Loading properties file from {}", location);
            }
            try {
                PropertiesLoaderUtils.fillProperties(properties, resourceLoader.getResource(location));
            } catch (IOException e) {
                LOGGER.warn("Could not load properties from {}", location, e);
            }
        }
    }

    private static String getValue(String key) {
        // String value = System.getProperty(key);
        // if (value != null) {
        //     return value;
        // }
        return properties.getProperty(key);
    }

    private static String getValue(String key, String defaultValue) {
        String value = getValue(key);
        return value != null ? value : defaultValue;
    }

    public static String getValue(Config config) {
        return getValue(StringUtils.toCamelName(config.name()));
    }

    public static String getValue(Config config, String defaultValue) {
        String value = getValue(config);
        return value != null ? value : defaultValue;
    }

    public static Integer getInteger(Config config) {
        return Integer.valueOf(getValue(config));
    }

    public static Integer getInteger(Config config, Integer defaultValue) {
        Integer value = getInteger(config);
        return value != null ? value : defaultValue;
    }

    public static Boolean getBoolean(Config config) {
        return Boolean.valueOf(getValue(config));
    }

    public static Boolean getBoolean(Config config, Boolean defaultValue) {
        Boolean value = getBoolean(config);
        return value != null ? value : defaultValue;
    }

    public static Set<String> getKeys() {
        return properties.stringPropertyNames();
    }

    public static Properties getProperties() {
        return properties;
    }

}
