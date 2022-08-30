package com.codowang.pensieve.core.sql.provider;

import com.codowang.pensieve.core.annotation.SelectParamItem;

/**
 * sql提供者
 *
 * @author wangyb
 * @since 2022-07-07
 */
public interface ISqlProvider {
    /**
     * 返回sql
     * @param fieldName 字段名
     * @param params 使用到的参数
     * @return sql语句
     */
    String getSql(String fieldName, SelectParamItem... params);
}
