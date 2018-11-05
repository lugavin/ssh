package com.ssh.common.web.action;

import com.ssh.common.exception.IncorrectCaptchaException;
import com.ssh.common.util.Constant;
import com.ssh.common.web.base.AbstractAction;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
@Scope("prototype")
public class SecurityAction extends AbstractAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityAction.class);

    public String login() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String exceptionName = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (exceptionName != null) {
            if (IncorrectCaptchaException.class.getName().equals(exceptionName)) {
                request.setAttribute(Constant.EXCEPTION_ATTRIBUTE, "Incorrect Captcha!");
            } else {
                request.setAttribute(Constant.EXCEPTION_ATTRIBUTE, "Invalid UserId/password!");
            }
        }
        // 认证失败转到登录页面(认证成功由Shiro自动转到上一请求路径)
        return SUCCESS;
    }

}
