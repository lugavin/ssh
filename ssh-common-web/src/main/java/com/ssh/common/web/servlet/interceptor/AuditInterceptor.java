package com.ssh.common.web.servlet.interceptor;

import com.ssh.common.context.AuditContext;
import com.ssh.common.subject.ActiveUser;
import com.ssh.common.util.SecurityHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class AuditInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditInterceptor.class);

    /**
     * 进入handler方法之前执行
     * 应用场景：用户身份认证
     * 返回值: true(放行) false(拦截)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequiresPermissions annotation = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
            if (annotation != null) {
                ActiveUser activeUser = SecurityHelper.getActiveUser();
                AuditContext auditContext = AuditContext.getThreadInstance();
                auditContext.setUserId(activeUser.getId());
                auditContext.setHostIP(request.getRemoteAddr());
                auditContext.setHostName(request.getRemoteHost());
                auditContext.setRequestURL(request.getRequestURI().substring(request.getContextPath().length()));
                auditContext.setFuncCode(Arrays.toString(annotation.value()));
                auditContext.setIsAudited(true);
            }
        }
        return true;
    }

    /**
     * 进入handler方法之后返回modelAndView之前执行
     * 应用场景：统一指定公用的模型视图数据(如菜单导航)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * handler方法执行完成之后执行
     * 应用场景：统一异常处理和日志处理
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        /**
         * 当Servlet容器启动时, 会先初始化一个线程池, 当容器接收到一个用户请求(一个请求就是一个线程)后, 会直接从线程池中取出一个线程来处理当前请求;
         * 当请求被处理完成后, 这个线程本身不会结束, 而是被放回线程池中, 这样可以减少创建线程和启动线程的系统开销.
         * 由于线程池的原因, 我们使用 ThreadLocal 来保存当前线程的数据, 在下一次请求时可能会依然存在(同一个线程处理),
         * 因此, 在请求处理完成后, 应将 ThreadLocal 绑定的线程范围内的数据清空.
         */
        AuditContext.getThreadInstance().remove();
    }

}
