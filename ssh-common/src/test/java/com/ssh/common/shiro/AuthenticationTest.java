package com.ssh.common.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 认证测试
 */
public class AuthenticationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTest.class);

    @Test
    public void testEncrypt() throws Exception {
        String source = "111111";   // 明文
        String salt = "10086";      // 盐(一般由随机数生成)
        // String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        LOGGER.info(new SecureRandomNumberGenerator().nextBytes().toHex());
        int hashIterations = 1;     // 散列次数, 如散列两次, 相当于 MD5(MD5(source, salt), salt)
        Md5Hash md5Hash = new Md5Hash(source, salt, hashIterations); // MySQL : SELECT MD5(CONCAT('10086','111111'));
        LOGGER.info(md5Hash.toString());
        SimpleHash simpleHash = new SimpleHash("md5", source, salt, hashIterations);
        LOGGER.info(simpleHash.toString());
    }

    @Test
    public void testAuthenticate() throws Exception {
        String username = "admin";
        String password = "111111";
        // 通过ini配置文件创建SecurityManagerFactory工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
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
            LOGGER.error("执行认证时出错: ", e);
        }
        // 是否认证通过
        boolean isAuthenticated = subject.isAuthenticated();
        LOGGER.info("认证结果: {}", isAuthenticated);
    }

}
