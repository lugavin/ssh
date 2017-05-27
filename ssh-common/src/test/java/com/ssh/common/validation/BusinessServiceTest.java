package com.ssh.common.validation;

import com.ssh.common.util.Constant;
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
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-validation-test.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BusinessServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceTest.class);

    @Inject
    private BusinessService businessService;

    @Before
    public void setUp() throws Exception {
        Assert.notNull(businessService);
    }

    @Test
    public void test() {
        try {
            businessService.convertToUpperCase("");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            List<String> list = new ArrayList<>();
            for (ConstraintViolation<?> violation : violations) {
                list.add(violation.getPropertyPath() + ":" + violation.getMessage());
            }
            LOGGER.info("=== {} ===", list);
        }

    }

}
