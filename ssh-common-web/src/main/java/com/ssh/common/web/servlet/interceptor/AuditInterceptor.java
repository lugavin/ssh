package com.ssh.common.web.servlet.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.ssh.common.context.AuditContext;
import com.ssh.common.subject.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AuditInterceptor extends MethodFilterInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditInterceptor.class);

    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        String methodName = invocation.getProxy().getMethod();
        Method method = invocation.getAction().getClass().getMethod(methodName);
        // 注意: 此处通过 method.getAnnotation(RequiresPermissions.class) 无法获取对应的 Annotation
        RequiresPermissions annotation = AnnotationUtils.findAnnotation(method, RequiresPermissions.class);
        if (annotation != null) {
            ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
            HttpServletRequest request = ServletActionContext.getRequest();
            AuditContext auditContext = AuditContext.getThreadInstance();
            auditContext.setUserId(activeUser.getId());
            auditContext.setHostIP(request.getRemoteAddr());
            auditContext.setHostName(request.getRemoteHost());
            auditContext.setRequestURL(request.getRequestURI());
            auditContext.setFuncCode(Arrays.toString(annotation.value()));
            auditContext.setIsAudited(true);
            try {
                return invocation.invoke();
            } finally {
                /**
                 * 当Servlet容器启动时, 会先初始化一个线程池, 当容器接收到一个用户请求(一个请求就是一个线程)后, 会直接从线程池中取出一个线程来处理当前请求;
                 * 当请求被处理完成后, 这个线程本身不会结束, 而是被放回线程池中, 这样可以减少创建线程和启动线程的系统开销.
                 * 由于线程池的原因, 我们使用 ThreadLocal 来保存当前线程的数据, 在下一次请求时可能会依然存在(同一个线程处理),
                 * 因此, 在请求处理完成后, 应将 ThreadLocal 绑定的线程范围内的数据清空.
                 */
                auditContext.remove();
            }
        }
        return invocation.invoke();
    }

}
