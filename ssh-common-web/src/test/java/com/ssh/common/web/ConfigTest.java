package com.ssh.common.web;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigTest.class);

    private Properties properties;

    @Before
    public void setUp() throws Exception {
        properties = new Properties();
        InputStream is = null;
        try {
            /**
             * 注意:
             * (1)对象ResourceLoader.getResource(String location)方法可以读取在JAR中的资源文件
             * (2)类ResourceUtils.getFile(String location)方法无法读取JAR中的资源文件
             */
            // is = new FileInputStream(ResourceUtils.getFile("classpath:app.properties"));
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties");
            properties.load(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Test
    public void testGetKeys() throws Exception {
        Set<String> keys = properties.stringPropertyNames();
        LOGGER.info("{}", keys);
    }

    @Test
    public void testGetValue() throws Exception {
        String company = properties.getProperty("company");
        Boolean useCaptcha = Boolean.valueOf(properties.getProperty("useCaptcha"));
        Integer pageSize = Integer.valueOf(properties.getProperty("pageSize"));
        Assert.hasText(company);
        Assert.isTrue(useCaptcha);
        Assert.isTrue(pageSize == 10);
    }

}
