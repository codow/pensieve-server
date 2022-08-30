package com.codowang.pensieve.core.annotation;

import com.codowang.pensieve.core.sql.provider.ISqlProvider;
import com.codowang.pensieve.core.sql.provider.SqlProviderHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface SelectSql {

    /**
     * 字段查询时使用的sql
     */
    String value() default "";

    /**
     * 字段查询时使用的sql构建者
     */
    Class<? extends ISqlProvider> using() default SqlProviderHelper.None.class;

    /**
     * 实际对应的表名
     * 注解在类上生效
     */
    String table() default "";

    /**
     * 实际对应的列名
     */
    String column() default "";

    /**
     * 字段查询时用的别名
     */
    String alias() default "";

    /**
     * 定义参数，目前只支持字符串
     */
    SelectParamItem[] params() default {};
}
