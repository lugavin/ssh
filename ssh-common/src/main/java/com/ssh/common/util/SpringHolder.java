package com.ssh.common.util;

import org.springframework.context.ApplicationContext;

public abstract class SpringHolder {

    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    public static <T> T getBean(String name, Class<T> type) {
        return context.getBean(name, type);
    }

}
