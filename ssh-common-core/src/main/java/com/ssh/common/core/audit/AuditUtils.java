package com.ssh.common.core.audit;

import com.ssh.common.core.annotation.Audited;
import com.ssh.common.core.annotation.NotAudited;
import com.ssh.common.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public abstract class AuditUtils {

    public static boolean isCollection(Method method) {
        return method != null && Collection.class.isAssignableFrom(method.getReturnType());
    }

    public static boolean isArray(Object obj) {
        return obj != null && ObjectUtils.isArray(obj);
    }

    public static boolean isNotChanged(Object oldValue, Object newValue) {
        if (oldValue == newValue) {
            return true;
        }
        if (oldValue == null) {
            return StringUtils.isBlank(newValue.toString());
        }
        if (newValue == null) {
            return StringUtils.isBlank(oldValue.toString());
        }
        if (oldValue instanceof Date && newValue instanceof Date) {
            return DateUtils.truncatedEquals((Date) oldValue, (Date) newValue, Calendar.SECOND);
        }
        return (StringUtils.isBlank(oldValue.toString()) && StringUtils.isBlank(newValue.toString()))
                || Objects.equals(oldValue, newValue);
    }

    public static String getValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return DateFormatUtils.format((Date) value, Constant.DEFAULT_DATETIME_PATTERN);
        }
        return value.toString();
    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> entityClass, String propertyName) {
        return BeanUtils.getPropertyDescriptor(entityClass, propertyName);
    }

    public static String getTableName(Class<?> entityClass) {
        return entityClass != null ? entityClass.getAnnotation(Table.class).name() : null;
    }

    public static JoinColumn getJoinColumn(Method method) {
        return method != null ? method.getAnnotation(JoinColumn.class) : null;
    }

    public static Column getColumn(Method method) {
        return method != null ? method.getAnnotation(Column.class) : null;
    }

    public static boolean isAudited(Class<?> entityClass) {
        return entityClass != null && entityClass.isAnnotationPresent(Audited.class);
    }

    public static boolean isNotAudited(Method method) {
        return method == null || method.isAnnotationPresent(NotAudited.class);
    }

}
