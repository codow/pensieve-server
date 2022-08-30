package com.codowang.pensieve.core.sql.enums;

/**
 * sql操作符
 * @author lvhl
 */
public enum SqlOperatorEnum {
    /**
     * 等于
     */
    EQUAL(" = ? "),
    /**
     * 开始以
     */
    LIKE_RIGHT(" LIKE CONCAT(?$$'%') "),
    /**
     * 开始不是以
     */
    NOT_LIKE_RIGHT(" NOT LIKE CONCAT(?$$'%') "),
    /**
     * 包含
     */
    LIKE(" LIKE CONCAT('%'$$?$$'%') "),
    /**
     * 不包含
     */
    NOT_LIKE(" NOT LIKE CONCAT('%'$$?$$'%') "),
    /**
     * 结束以
     */
    LIKE_LEFT(" LIKE CONCAT('%'$$?) "),
    /**
     * 结束不是以
     */
    NOT_LIKE_LEFT(" NOT LIKE CONCAT('%'$$?) "),
    /**
     * 不等于
     */
    UNEQUAL(" <> ? "),
    /**
     * 存在
     */
    EXISTS(" EXISTS(?) "),
    /**
     * 不存在
     */
    NOT_EXISTS(" NOT EXISTS(?) "),
    /**
     * 大于
     */
    GT(" > ? "),
    /**
     * 小于
     */
    LT(" < ? "),
    /**
     * 大于等于
     */
    GT_EQUAL(" >= ? "),
    /**
     * 小于等于
     */
    LT_EQUAL(" <= ? "),
    /**
     * 为空
     */
    IS_NULL(" IS NULL "),
    /**
     * 不为空
     */
    IS_NOT_NULL(" IS NOT NULL "),
    /**
     * 介于
     */
    BETWEEN_AND(" BETWEEN ? AND ? "),
    /**
     * 不介于
     */
    NOT_BETWEEN_AND(" NOT BETWEEN ? AND ? "),
    /**
     * 在列表
     */
    IN(" IN "),
    /**
     *  不再列表
     */
    NOT_IN (" NOT IN "),

    /**
     * 字符串数组查询
     */
    FIND_IN_SET (" FIND_IN_SET "),

    /**
     * 条件SQL
     */
    SQL(" ");

    private String operator;

    SqlOperatorEnum(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public boolean equals(String name) {
        return this.operator.equalsIgnoreCase(name);
    }

    public static SqlOperatorEnum getSqlOperatorEnum(String name) {
        if ("LIKE_LEFT".equalsIgnoreCase(name) || SqlOperatorEnum.LIKE_LEFT.equals(name)) {
            return SqlOperatorEnum.LIKE_LEFT;
        }if ("NOT_LIKE_LEFT".equalsIgnoreCase(name) || SqlOperatorEnum.NOT_LIKE_LEFT.equals(name)) {
            return SqlOperatorEnum.NOT_LIKE_LEFT;
        } else if ("LIKE_RIGHT".equalsIgnoreCase(name) || SqlOperatorEnum.LIKE_RIGHT.equals(name)) {
            return SqlOperatorEnum.LIKE_RIGHT;
        } else if ("NOT_LIKE_RIGHT".equalsIgnoreCase(name) || SqlOperatorEnum.NOT_LIKE_RIGHT.equals(name)) {
            return SqlOperatorEnum.NOT_LIKE_RIGHT;
        } else if ("LIKE".equalsIgnoreCase(name) || SqlOperatorEnum.LIKE.equals(name)) {
            return SqlOperatorEnum.LIKE;
        } else if ("NOT_LIKE".equalsIgnoreCase(name) || SqlOperatorEnum.NOT_LIKE.equals(name)) {
            return SqlOperatorEnum.NOT_LIKE;
        } else if ("UNEQUAL".equalsIgnoreCase(name) || SqlOperatorEnum.UNEQUAL.equals(name)) {
            return SqlOperatorEnum.UNEQUAL;
        } else if ("EXISTS".equalsIgnoreCase(name) || SqlOperatorEnum.EXISTS.equals(name)) {
            return SqlOperatorEnum.EXISTS;
        } else if ("NOT_EXISTS".equalsIgnoreCase(name) || SqlOperatorEnum.NOT_EXISTS.equals(name)) {
            return SqlOperatorEnum.NOT_EXISTS;
        } else if ("GT".equalsIgnoreCase(name) || SqlOperatorEnum.GT.equals(name)) {
            return SqlOperatorEnum.GT;
        } else if ("LT".equalsIgnoreCase(name) || SqlOperatorEnum.LT.equals(name)) {
            return SqlOperatorEnum.LT;
        } else if ("GT_EQUAL".equalsIgnoreCase(name) || SqlOperatorEnum.GT_EQUAL.equals(name)) {
            return SqlOperatorEnum.GT_EQUAL;
        } else if ("LT_EQUAL".equalsIgnoreCase(name) || SqlOperatorEnum.LT_EQUAL.equals(name)) {
            return SqlOperatorEnum.LT_EQUAL;
        } else if ("BETWEEN_AND".equalsIgnoreCase(name) || SqlOperatorEnum.BETWEEN_AND.equals(name)) {
            return SqlOperatorEnum.BETWEEN_AND;
        } else if ("NOT_BETWEEN_AND".equalsIgnoreCase(name) || SqlOperatorEnum.NOT_BETWEEN_AND.equals(name)) {
            return SqlOperatorEnum.NOT_BETWEEN_AND;
        } else if ("IN".equalsIgnoreCase(name) || SqlOperatorEnum.IN.equals(name)) {
            return SqlOperatorEnum.IN;
        } else if ("NOT_IN".equalsIgnoreCase(name) || SqlOperatorEnum.NOT_IN.equals(name)) {
            return SqlOperatorEnum.NOT_IN;
        } else if ("IS_NULL".equalsIgnoreCase(name) || SqlOperatorEnum.IS_NULL.equals(name)) {
            return SqlOperatorEnum.IS_NULL;
        } else if ("IS_NOT_NULL".equalsIgnoreCase(name) || SqlOperatorEnum.IS_NOT_NULL.equals(name)) {
            return SqlOperatorEnum.IS_NOT_NULL;
        } else if ("SQL".equalsIgnoreCase(name) || SqlOperatorEnum.SQL.equals(name)) {
            return SqlOperatorEnum.SQL;
        } else {
            return SqlOperatorEnum.EQUAL;
        }
    }
}
