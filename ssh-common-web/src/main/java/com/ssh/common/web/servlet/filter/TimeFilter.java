package com.ssh.common.web.servlet.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TimeFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long t1 = System.currentTimeMillis();
        chain.doFilter(request, response);
        long t2 = System.currentTimeMillis();
        LOGGER.info("Request {} completed in {} ms", ((HttpServletRequest) request).getRequestURI(), t2 - t1);
    }

    @Override
    public void destroy() {

    }

}
