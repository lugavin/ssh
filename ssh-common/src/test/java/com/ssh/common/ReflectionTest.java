package com.ssh.common;

import com.ssh.common.dto.DTO;
import com.ssh.common.service.BaseService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReflectionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void test1GetMethods() {
        Method[] methods = BaseService.class.getMethods();
        for (Method method : methods) {
            LOGGER.info("=== {} ===", method.getName());
        }
    }

    @Test
    public void test2FindMethod() {
        Method method = ReflectionUtils.findMethod(BaseService.class, "add", DTO.class);
        LOGGER.info("=== {} ===", method);
    }

    @Test
    public void test3Annotation() {
        Method method = ReflectionUtils.findMethod(BaseService.class, "add", DTO.class);
        LOGGER.info("=== {} ===", method);
        Override annotation = AnnotationUtils.getAnnotation(method, Override.class);
        LOGGER.info("=== {} ===", annotation);
    }

}
