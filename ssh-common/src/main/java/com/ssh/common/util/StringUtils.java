package com.ssh.common.util;

public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String UNDERSCORE = Constant.UNDERSCORE_SEPARATOR;

    public static String toCamelName(String underscoreName) {
        StringBuilder sb = new StringBuilder();
        for (String str : underscoreName.split(UNDERSCORE)) {
            sb.append(capitalize(str.toLowerCase()));
        }
        return uncapitalize(sb.toString());
    }

    public static String toUnderscoreName(String camelCaseName) {
        if (isBlank(camelCaseName)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(camelCaseName.substring(0, 1).toLowerCase());
        for (int i = 1; i < camelCaseName.length(); i++) {
            String s = camelCaseName.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (s.equals(slc)) {
                result.append(s);
            } else {
                result.append(UNDERSCORE).append(slc);
            }
        }
        return result.toString();
    }

}
