package com.ssh.common.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.ssh.common.subject.ActiveUser;
import com.ssh.common.subject.SecurityService;
import com.ssh.common.util.Constant;
import com.ssh.common.util.SecurityHelper;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
@Scope("prototype")
public class LoginAction extends ActionSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAction.class);

    @Autowired
    private SecurityService securityService;

    private String username;
    private String password;
    private String captcha;

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String kaptcha = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (captcha != null && kaptcha != null && !captcha.equalsIgnoreCase(kaptcha)) {
            request.setAttribute(Constant.EXCEPTION_ATTRIBUTE, "Incorrect Captcha!");
            return LOGIN;
        }
        ActiveUser user = securityService.getActiveUser(username);
        if (user == null || !user.getPass().equals(SecurityHelper.generateMd5Hash(password, user.getSalt()))) {
            request.setAttribute(Constant.EXCEPTION_ATTRIBUTE, "Invalid UserId/password!");
            return LOGIN;
        }
        user.setMenus(securityService.getMenuList(user.getId()));
        user.setFunctions(securityService.getFunctionList(user.getId()));
        request.getSession().setAttribute(Constant.USER_SESSION_ATTRIBUTE, user);
        return SUCCESS;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}
