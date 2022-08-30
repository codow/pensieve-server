package com.codowang.pensieve.core.sql.entity;

import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.codowang.pensieve.core.sql.enums.SqlOperatorEnum;
import com.codowang.pensieve.core.utils.CollectionUtils;
import com.codowang.pensieve.core.utils.MapUtils;
import com.codowang.pensieve.core.utils.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.NOT_LIKE;

/**
 * 继承mybatis-plus的queryWrapper，用来转换SqlQueryParams为QueryWrapper
 *
 * @author wangyb
 * @since 2022-07-06
 */
public class QueryWrapper<T> extends com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<T> {
    public QueryWrapper() {
        super();
    }

    public QueryWrapper(T entity) {
        super(entity);
    }

    public QueryWrapper(T entity, String... columns) {
        super(entity, columns);
    }

    /**
     * 使用sqlQueryParams进行构造，自动初始化where条件和sort条件
     *
     * @param sqlQueryParams sql查询参数
     */
    public QueryWrapper(SqlQueryParams sqlQueryParams) {
        this(null, sqlQueryParams, false);
    }

    /**
     * 使用sqlQueryParams进行构造，自动初始化where条件和sort条件
     *
     * @param sqlQueryParams sql查询参数
     * @param ignoreOrder 忽略排序，分页时使用
     */
    public QueryWrapper(SqlQueryParams sqlQueryParams, boolean ignoreOrder) {
        this(null, sqlQueryParams, ignoreOrder);
    }

    /**
     * 使用sqlQueryParams进行构造，自动初始化where条件和sort条件
     *
     * @param entity 对应的实体类
     * @param sqlQueryParams sql查询参数
     */
    public QueryWrapper(T entity, SqlQueryParams sqlQueryParams, boolean ignoreOrder) {
        super(entity);
        if (sqlQueryParams == null) {
            return;
        }
        this.initCondition(sqlQueryParams);
        if (!ignoreOrder) {
            this.initOrder(sqlQueryParams);
        }
    }

    /**
     * 使用sqlQueryParams初始化查询条件
     *
     * @param sqlQueryParams sql查询参数
     */
    private void initCondition(SqlQueryParams sqlQueryParams) {
        // 初始化where条件
        Map<String, Map<String, Object>> params = sqlQueryParams.getParams();
        if (params != null) {
            params.keySet().forEach(key -> {
                Map<String, Object> config = params.get(key);
                if (config == null) {
                    return;
                }
                Object value = config.get("value");
                if (value == null || StringUtils.isBlank(value)) {
                    return;
                }
                // 获取排序类型
                SqlOperatorEnum operator = SqlOperatorEnum.getSqlOperatorEnum(
                        StringUtils.getString(
                                MapUtils.getString(config, "sql_operator", "operator"),
                                "EQUAL"
                        )
                );
                // 计算条件字段名
                String field = StringUtils.getString(
                        MapUtils.getString(config, "field"),
                        key
                );
                String table = MapUtils.getString(config, "table");
                if (StringUtils.isNotBlank(table)) {
                    field = table + "." + field;
                }
                // 根据排序类型初始化条件
                if (SqlOperatorEnum.EQUAL.equals(operator)) {
                    this.eq(field, value);
                } else if (SqlOperatorEnum.UNEQUAL.equals(operator)) {
                    this.ne(field, value);
                } else if (SqlOperatorEnum.LIKE_RIGHT.equals(operator)) {
                    // 开始以
                    this.likeRight(field, value);
                } else if (SqlOperatorEnum.NOT_LIKE_RIGHT.equals(operator)) {
                    // 开始不是以
                    this.notLikeRight(field, value);
                } else if (SqlOperatorEnum.LIKE.equals(operator)) {
                    // 包含
                    this.like(field, value);
                } else if (SqlOperatorEnum.NOT_LIKE.equals(operator)) {
                    // 不包含
                    this.notLike(field, value);
                } else if (SqlOperatorEnum.LIKE_LEFT.equals(operator)) {
                    // 结束以
                    this.likeLeft(field, value);
                } else if (SqlOperatorEnum.NOT_LIKE_LEFT.equals(operator)) {
                    // 结束不是以
                    this.notLikeLeft(field, value);
                } else if (SqlOperatorEnum.EXISTS.equals(operator)) {
                    // 存在
                    this.exists((String)value);
                } else if (SqlOperatorEnum.NOT_EXISTS.equals(operator)) {
                    // 不存在
                    this.notExists((String)value);
                } else if (SqlOperatorEnum.GT.equals(operator)) {
                    // 大于
                    this.gt(field, value);
                } else if (SqlOperatorEnum.LT.equals(operator)) {
                    // 小于
                    this.lt(field, value);
                } else if (SqlOperatorEnum.GT_EQUAL.equals(operator)) {
                    // 大于等于
                    this.ge(field, value);
                } else if (SqlOperatorEnum.LT_EQUAL.equals(operator)) {
                    // 小于等于
                    this.le(field, value);
                } else if (SqlOperatorEnum.IS_NULL.equals(operator)) {
                    // 为空
                    this.isNull(field);
                } else if (SqlOperatorEnum.IS_NOT_NULL.equals(operator)) {
                    // 不为空
                    this.isNotNull(field);
                } else if (SqlOperatorEnum.BETWEEN_AND.equals(operator)) {
                    // 介于
                    List<Object> listValue = CollectionUtils.castObjectList(value);
                    if (CollectionUtils.isEmpty(listValue)) {
                        return;
                    }
                    if (listValue.size() == 1) {
                        this.between(field, listValue.get(0), listValue.get(0));
                    } else {
                        this.between(field, listValue.get(0), listValue.get(1));
                    }
                } else if (SqlOperatorEnum.NOT_BETWEEN_AND.equals(operator)) {
                    // 不介于
                    List<Object> listValue = CollectionUtils.castObjectList(value);
                    if (CollectionUtils.isEmpty(listValue)) {
                        return;
                    }
                    if (listValue.size() == 1) {
                        this.notBetween(field, listValue.get(0), listValue.get(0));
                    } else {
                        this.notBetween(field, listValue.get(0), listValue.get(1));
                    }
                } else if (SqlOperatorEnum.IN.equals(operator)) {
                    // 在列表
                    List<Object> listValue = CollectionUtils.castObjectList(value);
                    if (CollectionUtils.isEmpty(listValue)) {
                        return;
                    }
                    this.in(field, listValue.toArray());
                } else if (SqlOperatorEnum.NOT_IN.equals(operator)) {
                    // 在列表
                    List<Object> listValue = CollectionUtils.castObjectList(value);
                    if (CollectionUtils.isEmpty(listValue)) {
                        return;
                    }
                    this.notIn(field, listValue.toArray());
                } else if (SqlOperatorEnum.FIND_IN_SET.equals(operator)) {
                    // 字符串数组查询
                    String tempSql = field + " " + SqlOperatorEnum.FIND_IN_SET.getOperator() + " ";
                    this.appendSqlSegments(() -> tempSql + formatParam(null, value));
                } else if (SqlOperatorEnum.SQL.equals(operator)) {
                    // 条件SQL
                    this.appendSqlSegments(() -> (String)value);
                }
            });
        }
    }

    /**
     * 使用sqlQueryParams初始化排序条件
     *
     * @param sqlQueryParams sql查询参数
     */
    private void initOrder(SqlQueryParams sqlQueryParams) {
        // 初始化sort条件
        Map<String, Object> sortInfo = sqlQueryParams.getSort();
        if (sortInfo != null) {
            sortInfo.keySet().forEach(sortField -> {
                String sortType = StringUtils.getString(
                        MapUtils.getString(sortInfo, sortField),
                        "ASC"
                ).toUpperCase(Locale.ROOT);
                if ("DESC".equals(sortType)) {
                    this.orderByDesc(sortField);
                } else {
                    this.orderByAsc(sortField);
                }
            });
        }
    }

    public com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<T> notLikeLeft(String column, Object val) {
        return likeValue(true, NOT_LIKE, column, val, SqlLike.LEFT);
    }

    public com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<T> notLikeRight(String column, Object val) {
        return likeValue(true, NOT_LIKE, column, val, SqlLike.RIGHT);
    }
}
