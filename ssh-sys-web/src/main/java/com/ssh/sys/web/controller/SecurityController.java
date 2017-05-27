package com.ssh.sys.web.controller;

import com.ssh.common.exception.IncorrectCaptchaException;
import com.ssh.common.util.Constant;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/security")
public class SecurityController {

    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        String exceptionName = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (exceptionName != null) {
            if (IncorrectCaptchaException.class.getName().equals(exceptionName)) {
                request.setAttribute(Constant.EXCEPTION_ATTRIBUTE, "Incorrect Captcha");
            } else {
                request.setAttribute(Constant.EXCEPTION_ATTRIBUTE, "Invalid Username/Password");
            }
        }
        // 认证失败转到登录页面(认证成功由Shiro自动转到上一请求路径)
        return "security/login";
    }

}
