package com.ssh.common.core.datasource;

import com.ssh.common.util.Constant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-core-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataSourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceTest.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(context);
        Assert.assertNotNull(entityManagerFactory);
    }

    @Test
    public void test1DataSource() throws Exception {
        Assert.assertNotNull(context.getBean(DataSource.class));
    }

    @Test
    public void test2EntityManagerFactory() throws Exception {
        Map<String, Object> props = entityManagerFactory.getProperties();
        LOGGER.info("{}", props);
    }

    @Test
    public void test3EntityManager() throws Exception {
        Assert.assertNotNull(entityManagerFactory.createEntityManager());
    }

}
