package com.ssh.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * (1)Annotation类型使用关键字@interface而不是interface.
 * (2)Annotation类型的方法定义是受限制的.
 * |-- Annotation类型的方法必须声明为无参数且无异常抛出
 * (3)元注解: 元注解用来提供对其它Annotation类型做说明.
 * |-- Java1.5定义了4个标准的meta-annotation类型
 * |-- @Retention(用于描述注解的生命周期)
 * |-- SOURCE(java)
 * |-- CLASS(java+class)
 * |-- RUNTIME(java+class+jvm)
 * |-- @Target(用于描述注解的使用范围)
 * |-- @Documented(用于描述在生成JavaDoc文档时将该Annotation写入到文档中)
 * |-- @Inherited(实现注解继承)
 * (4)Annotation与程序代码的隔离性: Annotation是不会影响程序代码的执行, 无论Annotation怎么变化, 代码都始终如一地执行.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Audited {
}
