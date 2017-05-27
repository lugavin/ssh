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
@Target(ElementType.TYPE)
public @interface RepositoryBean {

    String namespace() default Constant.EMPTY;

}
