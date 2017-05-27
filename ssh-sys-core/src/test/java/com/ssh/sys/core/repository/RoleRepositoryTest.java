package com.ssh.sys.core.repository;

import com.ssh.common.util.Constant;
import com.ssh.sys.core.entity.RoleEntity;
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

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring-core-context.xml",
        "classpath:sys-core-context.xml"
})
//@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional(transactionManager = "transactionManager")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoleRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepositoryTest.class);

    @Inject
    private RoleRepository roleRepository;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(roleRepository);
    }

    @Test
    @Transactional(readOnly = true)
    public void test1GetRoleListByRoleIds() throws Exception {
        List<RoleEntity> list = roleRepository.getRoleListByRoleIds(new Long[]{1001L, 1002L});
        LOGGER.info("=== {} ===", list);
    }

}
