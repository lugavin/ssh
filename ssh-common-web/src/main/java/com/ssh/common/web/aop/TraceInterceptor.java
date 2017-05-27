package com.ssh.common.web.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;

public class TraceInterceptor extends CustomizableTraceInterceptor {

    private static final long serialVersionUID = 20161208L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceInterceptor.class);

    @Override
    protected void writeToLog(Log logger, String message, Throwable ex) {
        if (ex != null) {
            LOGGER.debug(message, ex);
        } else {
            LOGGER.debug(message);
        }
    }

    @Override
    protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {
        return true;
    }

}
