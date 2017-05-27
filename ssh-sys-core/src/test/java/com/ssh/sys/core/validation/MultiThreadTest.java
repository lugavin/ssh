package com.ssh.sys.core.validation;

import com.ssh.common.util.Constant;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.api.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadTest {

    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constant.ENV_DEV);
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:spring-validation.xml",
                "classpath:spring-core-context.xml",
                "classpath:sys-core-context.xml"
        );
        final UserService userService = applicationContext.getBean(UserService.class);
        final ExecutorService threadPool = Executors.newCachedThreadPool();
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 5; i++) {
            final int seq = i;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                        UserDTO dto = new UserDTO();
                        dto.setId((long) seq);
                        dto.setCode("King");
                        dto.setName("King");
                        dto.setPass("King");
                        userService.add(dto);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        latch.countDown();
        threadPool.shutdown();
    }
}
