package com.ssh.sys.web.dubbo;

import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.api.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerTest {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sys-web-context.xml", "classpath:sys-web-consumer.xml");
        context.start();
        UserService userService = (UserService) context.getBean("userService");  // 获取远程服务代理
        UserDTO user = userService.getByCode("admin");  // 执行远程方法
        System.err.println(user);  // 显示调用结果
    }

}
