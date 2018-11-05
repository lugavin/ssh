package com.ssh.common.util;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public abstract class ResourceUtils {

    public static ResourceBundle getBundle(String baseName) {
        return ResourceBundle.getBundle(baseName);
    }

    public static List<String> getKeys(String baseName) {
        return Collections.list(getBundle(baseName).getKeys());
    }

    public static String getValue(String baseName, String key) {
        return getBundle(baseName).getString(key);
    }

    public static String getValue(String baseName, String key, Object... arguments) {
        return MessageFormat.format(getValue(baseName, key), arguments);
    }

}
