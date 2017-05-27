package com.ssh.common.core.repository;

import com.ssh.common.util.Constant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-core-context.xml"})
@Transactional(transactionManager = "transactionManager")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DynamicQueryRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicQueryRepositoryTest.class);

    @Inject
    private DynamicQueryRepository dynamicQueryRepository;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(dynamicQueryRepository);
    }

    @Test
    @Transactional(readOnly = true)
    public void test1GetOne() throws Exception {

    }

}
