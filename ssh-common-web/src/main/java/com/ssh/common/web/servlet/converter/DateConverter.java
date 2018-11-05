package com.ssh.common.web.servlet.converter;

import com.ssh.common.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class DateConverter extends StrutsTypeConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateConverter.class);

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        Date result = null;
        if (toClass == Date.class) {
            if (values != null && values.length > 0) {
                try {
                    String dateStr = values[0];
                    if (!StringUtils.isEmpty(dateStr)) {
                        result = DateUtils.parseDate(dateStr, Constant.DEFAULT_DATE_PATTERN, Constant.DEFAULT_DATETIME_PATTERN);
                    }
                } catch (ParseException e) {
                    LOGGER.error("Can't to parse string to date.", e);
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    @Override
    public String convertToString(Map context, Object obj) {
        if (obj instanceof Date) {
            DateFormatUtils.format((Date) obj, Constant.DEFAULT_DATE_PATTERN);
        }
        return null;
    }

}
