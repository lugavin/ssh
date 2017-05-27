package com.ssh.sys.core.validation;

import com.ssh.common.util.Constant;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.api.dto.extension.PermissionExtDTO;
import com.ssh.sys.api.service.PermissionService;
import com.ssh.sys.api.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring-validation.xml",
        "classpath:spring-core-context.xml",
        "classpath:sys-core-context.xml"
})
//@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional(transactionManager = "transactionManager")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationTest.class);

    @Inject
    private UserService userService;

    @Inject
    private PermissionService permissionService;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(userService);
        // 注意: 引进和不引进spring-validation.xml配置文件一下结果的区别
        LOGGER.info("isAopProxy => {}", AopUtils.isAopProxy(userService));
        LOGGER.info("isJdkDynamicProxy => {}", AopUtils.isJdkDynamicProxy(userService));
        LOGGER.info("isCglibProxy => {}", AopUtils.isCglibProxy(userService));
    }

    @Test
    @Rollback
    public void test1Save() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setCode("King");
        dto.setName("King");
        dto.setPass("King");
        userService.add(dto);
    }

    @Test
    public void test2GetList() throws Exception {
        List<UserDTO> list = userService.getList(new UserDTO());
        LOGGER.info("list => {}", list);
    }

    @Test
    public void test3GetById() throws Exception {
        PermissionExtDTO dto = permissionService.getById(11L);
        LOGGER.info("=== {} ===", dto);
    }

}
