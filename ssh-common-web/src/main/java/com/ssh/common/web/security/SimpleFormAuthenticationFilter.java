package com.ssh.common.web.security;

import com.ssh.common.exception.IncorrectCaptchaException;
import com.ssh.common.util.Constant;
import com.ssh.common.util.PropertiesLoader;
import com.ssh.common.web.captcha.CaptchaService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义基于表单认证的过滤器来实现在认证之前对验证码进行校验
 */
public class SimpleFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFormAuthenticationFilter.class);

    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
    public static final String DEFAULT_TOKEN_PARAM = "token";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    private String tokenParam = DEFAULT_TOKEN_PARAM;

    @Autowired
    private CaptchaService captchaService;

    @Override
    protected CaptchaUsernamePasswordToken createToken(ServletRequest request, ServletResponse response) {
        return new CaptchaUsernamePasswordToken(
                getUsername(request),
                getPassword(request),
                isRememberMe(request),
                getHost(request),
                getCaptcha(request),
                getToken(request)
        );
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        CaptchaUsernamePasswordToken token = this.createToken(request, response);
        try {
            // 若验证码校验失败则拒绝访问, 不再校验帐户和密码
            if (!doCaptchaVerify(token)) {
                request.setAttribute(getFailureKeyAttribute(), IncorrectCaptchaException.class.getName());
                return true;
            }
            Subject subject = getSubject(request, response);
            subject.login(token);
            subject.getSession().setAttribute(Constant.USER_SESSION_KEY, subject.getPrincipal());
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    protected boolean doCaptchaVerify(CaptchaUsernamePasswordToken token) {
        try {
            return !PropertiesLoader.getBoolean(PropertiesLoader.Config.USE_CAPTCHA) ||
                    captchaService.doVerify(token.getToken(), token.getCaptcha());
        } catch (Exception e) {
            LOGGER.warn("Verify captcha error: {}", e.getMessage());
            return false;
        }
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected String getToken(ServletRequest request) {
        return WebUtils.getCleanParam(request, getTokenParam());
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }

    public String getTokenParam() {
        return tokenParam;
    }

    public void setTokenParam(String tokenParam) {
        this.tokenParam = tokenParam;
    }
}
