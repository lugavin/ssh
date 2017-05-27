package com.ssh.common.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * 授权测试(先认证再授权)
 */
public class AuthorizationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationTest.class);

    @Test
    public void testAuthorize() throws Exception {
        String username = "admin";
        String password = "111111";
        // 通过ini配置文件创建SecurityManagerFactory工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-permission.ini");
        // 通过SecurityManagerFactory工厂创建SecurityManager实例
        SecurityManager securityManager = factory.getInstance();
        // 将SecurityManager设置到当前的运行环境中
        SecurityUtils.setSecurityManager(securityManager);
        // 通过SecurityUtils创建Subject
        Subject subject = SecurityUtils.getSubject();
        // 创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            // 执行认证
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        // 是否认证通过
        boolean isAuthenticated = subject.isAuthenticated();
        LOGGER.info("认证结果: {}", isAuthenticated);
        // 测试角色
        boolean hasRole = subject.hasRole("role1");
        LOGGER.info("单个角色判断 => {} 用户是否拥有 [role1] 角色: {}", username, hasRole);
        boolean hasAllRoles = subject.hasAllRoles(Arrays.asList("role1", "role2"));
        LOGGER.info("多个角色判断 => {}用户是否拥有 [role1, role2] 角色: {}", username, hasAllRoles);
        // 测试权限
        boolean isPermitted = subject.isPermitted("user:create");
        LOGGER.info("单个权限判断 => {} 用户是否拥有 [user:create] 权限: {}", username, isPermitted);
        boolean isPermittedAll = subject.isPermittedAll("user:update", "user:delete");
        LOGGER.info("多个权限判断 => {} 用户是否拥有 [user:update, user:delete] 权限: {}", username, isPermittedAll);
    }

}
