package com.ssh.common.web.servlet.interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.ssh.common.subject.ActiveUser;
import com.ssh.common.util.Constant;
import com.ssh.common.util.ResourceUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 认证拦截器
 */
public class AuthcInterceptor extends AbstractInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthcInterceptor.class);

    private final List<String> anonymousURLList = new ArrayList<>();

    @Override
    public void init() {
        anonymousURLList.addAll(ResourceUtils.getKeys("anonymousURL"));
    }

    /**
     * 返回一个字符串作为逻辑视图
     */
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        // 判断是否是匿名访问地址
        HttpServletRequest request = ServletActionContext.getRequest();
        String url = request.getRequestURI();
        for (String anonymousURL : anonymousURLList) {
            if (url.contains(anonymousURL)) {
                return invocation.invoke();
            }
        }
        // 判断用户是否已登录(登录功能中已将用户相关信息保存在Session中)
        ActiveUser activeUser = (ActiveUser) request.getSession().getAttribute(Constant.USER_SESSION_ATTRIBUTE);
        if (activeUser == null) {
            return Action.LOGIN;
        }
        return invocation.invoke();
    }

}
