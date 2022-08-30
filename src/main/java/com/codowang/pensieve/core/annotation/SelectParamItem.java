package com.codowang.pensieve.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface SelectParamItem {
    /**
     * 参数值
     */
    String value() default "";

    /**
     * 数据类型，默认字符串
     */
    Class<?> type() default String.class;
}
