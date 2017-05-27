package com.ssh.common.web.security;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 扩展UsernamePasswordToken类用来添加额外的验证码参数
 */
public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {

    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public CaptchaUsernamePasswordToken(String username, String password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }

}
