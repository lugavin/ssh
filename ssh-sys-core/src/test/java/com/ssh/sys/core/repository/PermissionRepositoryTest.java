package com.ssh.sys.core.repository;

import com.ssh.common.util.Constant;
import com.ssh.sys.api.dto.extension.PermissionExtDTO;
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
@ContextConfiguration({
        "classpath:spring-core-context.xml",
        "classpath:sys-core-context.xml"
})
//@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional(transactionManager = "transactionManager")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PermissionRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionRepositoryTest.class);

    @Inject
    private PermissionRepository permissionRepository;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(permissionRepository);
    }

    @Test
    @Transactional(readOnly = true)
    public void test1GetPermissionById() throws Exception {
        PermissionExtDTO permissionExtDTO = permissionRepository.getPermissionById(11L);
        LOGGER.info("{}", permissionExtDTO);
    }

}
