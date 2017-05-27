package com.ssh.sys.web.dubbo;

import com.alibaba.dubbo.rpc.RpcException;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

public class ConsumerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerTest.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sys-web-consumer.xml");
            context.start();
            UserService userService = context.getBean(UserService.class);   // 获取远程服务代理
            UserDTO user = userService.getByCode("admin");                  // 执行远程方法
            LOGGER.info("=== {} ===", user);                                // 显示调用结果
        } catch (RpcException e) {
            ConstraintViolationException violation = (ConstraintViolationException) e.getCause();
            Set<ConstraintViolation<?>> violations = violation.getConstraintViolations();
            System.err.println(violations);
        }
    }

}
