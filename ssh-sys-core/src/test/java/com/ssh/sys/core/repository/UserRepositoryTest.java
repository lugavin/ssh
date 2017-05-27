package com.ssh.sys.core.repository;

import com.ssh.common.util.Constant;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.core.entity.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
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
public class UserRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Inject
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(userRepository);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void test1Save() throws Exception {
        List<UserEntity> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setCode("Test_" + i);
            userEntity.setName("Test_" + i);
            userEntity.setPass("Test_" + i);
            list.add(userEntity);
        }
        userRepository.save(list);
    }

    @Test
    @Transactional(readOnly = true)
    public void test2FindById() throws Exception {
        UserEntity entity = userRepository.findById(101L);
        LOGGER.info("=== {} ===", entity);
    }

    @Test
    @Transactional(readOnly = true)
    public void test3GetById() throws Exception {
        UserEntity entity = userRepository.getById(101L);
        LOGGER.info("=== {} ===", entity);
    }

    @Test
    @Transactional(readOnly = true)
    public void test4GetUserByCode() throws Exception {
        UserDTO dto = userRepository.getUserByCode("admin");
        LOGGER.info("=== {} ===", dto);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test5Delete() throws Exception {
        userRepository.delete(116L);
    }

}
