package com.ssh.sys.core.service;

import com.ssh.common.util.Constant;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.inject.Inject;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring-core-context.xml",
        "classpath:sys-core-context.xml"
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceTest {

    @Inject
    private ApplicationContext context;

    @Before
    public void setUp() throws Exception {
        Assert.notNull(context);
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void test1Service() throws Exception {
        Assert.notNull(context.getBean("userServiceImpl"));
    }

    @Test
    public void test2Service() throws Exception {
        Assert.notNull(context.getBean("userService"));
    }

}
