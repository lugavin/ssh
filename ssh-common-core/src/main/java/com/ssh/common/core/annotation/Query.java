package com.ssh.common.core.annotation;

import com.ssh.common.util.Constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Gavin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Query {

    String value() default Constant.EMPTY;

    Class<?> transformer() default DEFAULT.class;

    final class DEFAULT {}

}
