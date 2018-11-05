package com.ssh.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    String value() default "";                  // 導出字段名(默認調用當前字段的getter方法,如指定導出字段為對象需填寫"對象名.屬性名")

    String title();                             // 導出字段標題(需要批註請用"**"分隔,標題**批註僅對導出模板有效)

    int type() default 0;                       // 字段類型(0導入導出 1 僅導出 2 僅導入)

    int style() default 0;                      // 導出字段樣式(0 自動 1 左對齊 2 居中對齊 3 右對齊 4 左對齊紅色字體 5 居中對齊紅色字體 6 右對齊紅色字體)

    int sort() default 0;                       // 導出字段排序(升序)

    String dictType() default "";               // 如果是字典類型需設置字典的Type值

    Class<?> fieldType() default Class.class;   // 反射類型

    int[] groups() default {};                  // 字段歸屬組(根據分組導入導出)

}
