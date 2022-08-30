package com.codowang.pensieve.core.sql.enums;

/**
 * 定义默认值填充策略
 *
 * @author wangyb
 * @since 2022-08-01
 */
public enum FieldFillStrategy {
    // 总是填充，不管什么情况，都将填充
    ALWAYS,
    // 空值填充，原始值为空对象，才填充
    NONE;
}
