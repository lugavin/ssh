package com.ssh.common.web.security;

import com.ssh.common.exception.IncorrectCaptchaException;
import com.ssh.common.util.Constant;
import com.ssh.common.util.PropertiesLoader;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义基于表单认证的过滤器来实现在认证之前对验证码进行校验
 */
public class SimpleFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFormAuthenticationFilter.class);

    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    @Override
    protected CaptchaUsernamePasswordToken createToken(ServletRequest request, ServletResponse response) {
        return new CaptchaUsernamePasswordToken(
                getUsername(request),
                getPassword(request),
                isRememberMe(request),
                getHost(request),
                getCaptcha(request)
        );
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        CaptchaUsernamePasswordToken token = createToken(request, response);
        try {
            // 若验证码校验失败则拒绝访问, 不再校验帐户和密码
            if (!doCaptchaVerify(request, token)) {
                request.setAttribute(getFailureKeyAttribute(), IncorrectCaptchaException.class.getName());
                return true;
            }
            Subject subject = getSubject(request, response);
            subject.login(token);
            subject.getSession().setAttribute(Constant.USER_SESSION_KEY, subject.getPrincipal());
            return this.onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return this.onLoginFailure(token, e, request, response);
        }
    }

    protected boolean doCaptchaVerify(ServletRequest request, CaptchaUsernamePasswordToken token) {
        if (!PropertiesLoader.getBoolean(PropertiesLoader.Config.USE_CAPTCHA)) {
            return true;
        }
        String captcha = (String) WebUtils.toHttp(request).getSession().getAttribute(Constant.CAPTCHA_SESSION_KEY);
        if (captcha == null) {
            LOGGER.info("=== {} ===", "The captcha has expired");
            return false;
        }
        return captcha.equalsIgnoreCase(token.getCaptcha());
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, this.getCaptchaParam());
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }

}
