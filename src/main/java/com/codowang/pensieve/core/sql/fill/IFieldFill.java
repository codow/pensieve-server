package com.codowang.pensieve.core.sql.fill;

/**
 * 定义生成字段填充数据的接口
 *
 * @author wangyb
 * @since 2022-07-05
 */
public interface IFieldFill<T> {

    /**
     * 表示空对象
     */
    String NULL = "ELASTIC_FIELD_FILL_NULL";

    /**
     * 生成值
     * @return 默认为空
     */
    T generateValue();
}
