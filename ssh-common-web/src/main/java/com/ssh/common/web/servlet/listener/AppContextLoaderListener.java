package com.ssh.common.web.servlet.listener;

import com.ssh.common.util.PropertiesLoader;
import com.ssh.common.util.SpringHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class AppContextLoaderListener extends ContextLoaderListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppContextLoaderListener.class);

    private static final String APP_CONFIG_LOCATION_PARAM = "appConfigLocation";
    private static final String ROOT_WEB_APP_CONTEXT_ATTRIBUTE = "app";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        String resources = servletContext.getInitParameter(APP_CONFIG_LOCATION_PARAM);
        LOGGER.debug("Start loading app config from {}", resources);
        PropertiesLoader.init(resources);
        servletContext.setAttribute(ROOT_WEB_APP_CONTEXT_ATTRIBUTE, PropertiesLoader.getProperties());
        LOGGER.debug("Complete loading app config, all parameters {} have been bound to [{}] attribute name in this ServletContext.", PropertiesLoader.getProperties(), ROOT_WEB_APP_CONTEXT_ATTRIBUTE);
        String env = PropertiesLoader.getValue(PropertiesLoader.Config.PHASE_ENV);
        servletContext.setInitParameter(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, env);
        LOGGER.debug("Active bean definition profiles [{}]", env);
        super.contextInitialized(event);
        SpringHolder.setApplicationContext(getCurrentWebApplicationContext());
        // SpringHolder.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(servletContext));
    }

}
