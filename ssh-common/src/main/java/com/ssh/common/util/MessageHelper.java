package com.ssh.common.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public abstract class MessageHelper {

    private static ResourceBundle resource = ResourceBundle.getBundle("errorCode");

    public static String getMessage(String key) {
        return resource.getString(key);
    }

    public static String getMessage(String key, String defaultMessage) {
        String message = resource.getString(key);
        return StringUtils.isBlank(message) ? defaultMessage : message;
    }

    public static String getMessage(String key, String defaultMessage, Object... arguments) {
        String message = resource.getString(key);
        return MessageFormat.format(StringUtils.isBlank(message) ? defaultMessage : message, arguments);
    }

}
