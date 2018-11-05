package com.ssh.common.web;

import com.ssh.common.util.PropertiesLoader;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Set;

public class PropertiesLoaderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoaderTest.class);

    @Before
    public void setUp() throws Exception {
        PropertiesLoader.init("classpath:app.properties");
    }

    @Test
    public void testGetKeys() throws Exception {
        Set<String> keys = PropertiesLoader.getKeys();
        LOGGER.info("{}", keys);
    }

    @Test
    public void testGetValue() throws Exception {
        String company = PropertiesLoader.getValue(PropertiesLoader.Config.COMPANY_NAME);
        Boolean useCaptcha = PropertiesLoader.getBoolean(PropertiesLoader.Config.USE_CAPTCHA);
        Integer pageSize = PropertiesLoader.getInteger(PropertiesLoader.Config.PAGE_SIZE);
        Assert.hasText(company);
        Assert.isTrue(useCaptcha);
        Assert.isTrue(pageSize == 10);
    }

}
