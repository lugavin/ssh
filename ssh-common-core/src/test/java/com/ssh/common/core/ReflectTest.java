package com.ssh.common.core;

import com.ssh.common.subject.ActiveUser;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReflectTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectTest.class);

    @Test
    public void test1() throws Exception {
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(ActiveUser.class, "code");
        LOGGER.info("writeMethod --> {}", descriptor.getWriteMethod().getName());
        LOGGER.info("readMethod --> {}", descriptor.getReadMethod().getName());
    }

    @Test
    public void test2() throws Exception {
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(ActiveUser.class, "code");
        Method method = descriptor.getReadMethod();
        ActiveUser user = new ActiveUser();
        user.setCode("King");
        Object result = ReflectionUtils.invokeMethod(method, user);
        LOGGER.info("{}", result);
    }

}
