package com.ssh.common.context;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * (1)一个请求就是一个线程
 * (2)每个线程维护各自实例对象的数据
 */
public final class AuditContext {

    private static ThreadLocal<AuditContext> map = new ThreadLocal<>();

    private AuditContext() {
    }

    /**
     * 方法上不用加 synchronized 关键字;
     * 因为当线程A进来时, 是从线程A的MAP中获取实例对象; 即使此时线程B进来, 也是从当前线程B的MAP中获取实例对象;
     * 即多个线程并没有操作共享数据, 所以不存在线程安全问题.
     * 说明：Synchronized用于线程间数据的共享，而ThreadLocal用于线程间数据的隔离。
     */
    public static AuditContext getThreadInstance() {
        AuditContext context = map.get();
        if (context == null) {
            context = new AuditContext();
            map.set(context);
        }
        return context;
    }

    public void remove() {
        map.remove();
    }

    private Long logId;
    private Long userId;
    private String funcCode;
    private String hostIP;
    private String hostName;
    private String requestURL;
    private boolean isAudited = false;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public boolean isAudited() {
        return isAudited;
    }

    public void setIsAudited(boolean isAudited) {
        this.isAudited = isAudited;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
