package com.codowang.pensieve.core.annotation;

import com.codowang.pensieve.core.sql.enums.FieldFillStrategy;
import com.codowang.pensieve.core.sql.fill.FieldFillImpl;
import com.codowang.pensieve.core.sql.fill.IFieldFill;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据插入时需要自动填充的注解
 *
 * @author wangyb
 * @since 2022-07-05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldUpdateFill {
    /**
     * 默认值
     * @return 默认值
     */
    String value() default IFieldFill.NULL;

    /**
     * 使用那个类来生成值
     * @return 类
     */
    Class<? extends IFieldFill<?>> using() default FieldFillImpl.None.class;

    /**
     * 填充策略，默认为总是填充
     * @return 填充策略
     */
    FieldFillStrategy strategy() default FieldFillStrategy.ALWAYS;
}
