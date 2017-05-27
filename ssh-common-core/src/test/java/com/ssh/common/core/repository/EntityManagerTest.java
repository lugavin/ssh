package com.ssh.common.core.repository;

import com.ssh.common.core.entity.AuditLogEntity;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-core-context.xml"})
@Transactional(transactionManager = "transactionManager")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntityManagerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityManagerTest.class);

    public static final String COUNT_QUERY_STRING = "SELECT COUNT(x) FROM %s x";

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(entityManager);
    }

    /**
     * entityManager#find => 若查询的记录不存在则返回null
     */
    @Test
    @Transactional(readOnly = true)
    public void test1FindById() throws Exception {
        AuditLogEntity entity = entityManager.find(AuditLogEntity.class, 1L);
        LOGGER.info("AuditLogEntity => {}", entity);
    }

    /**
     * entityManager#getReference => 若查询的记录不存在则会抛出异常
     */
    @Test
    @Transactional(readOnly = true)
    public void test2GetById() throws Exception {
        AuditLogEntity entity = entityManager.getReference(AuditLogEntity.class, 1L);
        LOGGER.info("AuditLogEntity => {}", entity);
    }

    @Test
    @Transactional(readOnly = true)
    public void test3GetCount() throws Exception {
        String countQuery = String.format(COUNT_QUERY_STRING, AuditLogEntity.class.getSimpleName());
        Long count = entityManager.createQuery(countQuery, Long.class).getSingleResult();
        LOGGER.info("Count => {}", count);
    }

}
