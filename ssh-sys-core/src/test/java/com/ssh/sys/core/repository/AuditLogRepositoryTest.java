package com.ssh.sys.core.repository;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.page.PageRequest;
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
import java.util.List;
import java.util.Map;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring-core-context.xml",
        "classpath:sys-core-context.xml"
})
//@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional(transactionManager = "transactionManager")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditLogRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogRepositoryTest.class);

    @Inject
    private AuditLogRepository auditLogRepository;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(auditLogRepository);
    }

    @Test
    public void test1QueryForList() throws Exception {
        List<Map> auditLogList = auditLogRepository.getListSelective(new ModelMapDTO());
        LOGGER.info("=== {} ===", auditLogList);
        List<Map> auditDetailList = auditLogRepository.getDetailListAuditLogId(1L);
        LOGGER.info("=== {} ===", auditDetailList);
    }

    @Test
    public void test2QueryForPage() throws Exception {
        Page<Map> page = auditLogRepository.getPageSelective(new PageRequest(new ModelMapDTO(), 0, 10));
        LOGGER.info("=== {} ===", page.getRecords());
    }

}
