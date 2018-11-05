package com.ssh.sys.web.action;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:sys-web-context.xml"})
public class ActionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionTest.class);

    @Autowired
    private ApplicationContext context;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(context);
    }

    @Test
    public void testActionScope() throws Exception {
        Assert.assertTrue(context.getBean("securityAction") != context.getBean("securityAction"));
        Object action = context.getBean("securityAction");
        LOGGER.info("isAopProxy => {}", AopUtils.isAopProxy(action));
        LOGGER.info("isJdkDynamicProxy => {}", AopUtils.isJdkDynamicProxy(action));
        LOGGER.info("isCglibProxy => {}", AopUtils.isCglibProxy(action));
    }

}
