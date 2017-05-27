package com.ssh.common.core.repository;

import com.ssh.common.core.repository.support.SimpleDynamicQueryRepository;
import com.ssh.common.core.repository.transform.AliasToBeanResultTransformer;
import com.ssh.common.util.Constant;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-core-context.xml"})
//@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional(transactionManager = "transactionManager")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransformerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerTest.class);

    private static final String HQL = "SELECT id AS id, funcCode AS funcCode FROM AuditLogEntity";
    private static final String SQL = "SELECT id AS id, func_code AS funcCode FROM sys_audit_log";

    @Inject
    private SimpleDynamicQueryRepository dynamicQueryRepository;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(dynamicQueryRepository);
        Assert.assertFalse(AopUtils.isAopProxy(dynamicQueryRepository));
        Assert.assertFalse(AopUtils.isJdkDynamicProxy(dynamicQueryRepository));
        Assert.assertFalse(AopUtils.isCglibProxy(dynamicQueryRepository));
    }

    /**
     * Transformers.TO_LIST: 把查询结果的value按顺序放进List中
     */
    @Test
    @Transactional(readOnly = true)
    public void test1ToList() throws Exception {
        ResultTransformer transformer = Transformers.TO_LIST;
        List list1 = dynamicQueryRepository.createHQLQuery(HQL).setResultTransformer(transformer).list();
        LOGGER.debug("HQL Transformers.TO_LIST: {}", list1);
        List list2 = dynamicQueryRepository.createSQLQuery(SQL).setResultTransformer(transformer).list();
        LOGGER.debug("SQL Transformers.TO_LIST: {}", list2);
    }

    /**
     * Transformers.aliasToBean: 把查询结果组装到JavaBean(通常为VO对象)中
     */
    @Test
    public void test2AliasToBean() throws Exception {
        ResultTransformer transformer = Transformers.aliasToBean(AuditLogDTO.class);
        List list1 = dynamicQueryRepository.createHQLQuery(HQL).setResultTransformer(transformer).list();
        LOGGER.debug("HQL Transformers.aliasToBean: {}", list1);
        List list2 = dynamicQueryRepository.createSQLQuery(SQL)
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("funcCode", StandardBasicTypes.STRING)
                .setResultTransformer(transformer)
                .list();
        LOGGER.debug("SQL Transformers.aliasToBean: {}", list2);
    }

    /**
     * Transformers.ALIAS_TO_ENTITY_MAP: 把查询结果组装到Map中
     */
    @Test
    public void test3AliasToEntityMap() throws Exception {
        ResultTransformer transformer = Transformers.ALIAS_TO_ENTITY_MAP;
        List list1 = dynamicQueryRepository.createHQLQuery(HQL).setResultTransformer(transformer).list();
        LOGGER.debug("HQL Transformers.ALIAS_TO_ENTITY_MAP: {}", list1);
        List list2 = dynamicQueryRepository.createSQLQuery(SQL).setResultTransformer(transformer).list();
        LOGGER.debug("SQL Transformers.ALIAS_TO_ENTITY_MAP: {}", list2);
    }

    /**
     * AliasToBeanResultTransformer: 把带别名的查询结果组装到JavaBean(通常为VO对象)中
     */
    @Test
    public void test4AliasToBeanResultTransformer() throws Exception {
        ResultTransformer transformer = AliasToBeanResultTransformer.newInstance(AuditLogDTO.class);
        List list1 = dynamicQueryRepository.createHQLQuery(HQL).setResultTransformer(transformer).list();
        LOGGER.debug("HQL AliasToBeanResultTransformer: {}", list1);
        List list2 = dynamicQueryRepository.createSQLQuery(SQL).setResultTransformer(transformer).list();
        LOGGER.debug("SQL AliasToBeanResultTransformer: {}", list2);
    }

}
