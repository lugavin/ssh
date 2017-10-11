package com.ssh.sys.core.service;

import com.ssh.common.dto.MapDTO;
import com.ssh.common.util.Constant;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.api.service.UserService;
import com.ssh.sys.core.enums.UserStatus;
import org.apache.shiro.crypto.hash.Md5Hash;
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
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

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
public class UserServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    @Inject
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        Assert.notNull(userService);
        LOGGER.info("isAopProxy => {}", AopUtils.isAopProxy(userService));
        LOGGER.info("isCglibProxy => {}", AopUtils.isCglibProxy(userService));
        LOGGER.info("isJdkDynamicProxy => {}", AopUtils.isJdkDynamicProxy(userService));
    }

    @Test
    @Rollback
    public void test1Save() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setCode("King");
        dto.setName("King");
        dto.setSalt("10086");
        dto.setStatus(UserStatus.AVAILABLE.getValue());
        dto.setPass(new Md5Hash("King", dto.getSalt(), 1).toString());
        userService.add(dto);
    }

    @Test
    public void test2GetList() throws Exception {
        List<Map> list = userService.getList(new MapDTO());
        LOGGER.info("=== {} ===", list);
    }

}
