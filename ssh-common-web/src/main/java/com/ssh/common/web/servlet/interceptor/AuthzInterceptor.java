package com.ssh.common.web.servlet.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.ssh.common.subject.ActiveUser;
import com.ssh.common.util.Constant;
import com.ssh.common.util.ResourceUtils;
import com.ssh.common.web.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 授权拦截器
 */
public class AuthzInterceptor extends MethodFilterInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthzInterceptor.class);

    public static final String DEFAULT_AUTHZ_REFUSE = "refuse";

    private String authzRefuse = DEFAULT_AUTHZ_REFUSE;

    private final List<String> anonymousURLList = new ArrayList<>();
    private final List<String> commonURLList = new ArrayList<>();

    @Override
    public void init() {
        anonymousURLList.addAll(ResourceUtils.getKeys("anonymousURL"));
        commonURLList.addAll(ResourceUtils.getKeys("commonURL"));
    }

    /**
     * 返回一个字符串作为逻辑视图
     */
    @Override
    public String doIntercept(ActionInvocation invocation) throws Exception {
        // 判断是否是匿名访问地址
        HttpServletRequest request = ServletActionContext.getRequest();
        String url = request.getRequestURI();
        for (String anonymousURL : anonymousURLList) {
            if (url.contains(anonymousURL)) {
                return invocation.invoke();
            }
        }
        // 判断是否为公共访问地址(认证通过就可以访问的地址)
        for (String commonURL : commonURLList) {
            if (url.contains(commonURL)) {
                return invocation.invoke();
            }
        }
        // 权限校验
        String methodName = invocation.getProxy().getMethod();
        Method method = invocation.getAction().getClass().getMethod(methodName);
        RequiresPermissions annotation = AnnotationUtils.findAnnotation(method, RequiresPermissions.class);
        if (annotation != null) {
            ActiveUser activeUser = (ActiveUser) request.getSession().getAttribute(Constant.USER_SESSION_ATTRIBUTE);
            String code = annotation.value();
            if (!activeUser.isPermitted(code)) {
                return authzRefuse;
            }
        }
        return invocation.invoke();
    }

    public String getAuthzRefuse() {
        return authzRefuse;
    }

    public void setAuthzRefuse(String authzRefuse) {
        this.authzRefuse = authzRefuse;
    }
}
