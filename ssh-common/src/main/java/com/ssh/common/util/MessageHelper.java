package com.ssh.common.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public abstract class MessageHelper {

    private static final ResourceBundle resource = ResourceBundle.getBundle("errorCode");

    public static String getMessage(String key) {
        return resource.getString(key);
    }

    public static String getMessage(String key, Object... arguments) {
        return MessageFormat.format(resource.getString(key), arguments);
    }

}
