package com.ssh.common.web.servlet.converter;

import com.ssh.common.util.Constant;
import com.ssh.common.util.PropertiesLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DateConverter implements Converter<String, Date> {

    private final Set<String> patterns = new HashSet<>();

    public void setPatterns(Set<String> patterns) {
        this.patterns.addAll(patterns);
    }

    @Override
    public Date convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        if (patterns.isEmpty()) {
            patterns.addAll(Arrays.asList(
                    PropertiesLoader.getValue(PropertiesLoader.Config.DATE_FORMAT, Constant.DEFAULT_DATE_PATTERN),
                    PropertiesLoader.getValue(PropertiesLoader.Config.DATETIME_FORMAT, Constant.DEFAULT_DATETIME_PATTERN)
            ));
        }
        try {
            return DateUtils.parseDate(source, patterns.toArray(new String[patterns.size()]));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
