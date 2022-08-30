package com.codowang.pensieve.core.sql.method;

/**
 * 实现盈天自己的sql构建工具，主要提供对自定义字段翻译的支持
 *
 * @author wangyb
 * @since 2022-07-07
 */
public enum SqlMethod {
    /**
     * 修改
     */
    UPDATE_FROM_MAP_BY_ID("updateFromMapById", "根据ID 选择修改数据，支持空值", "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>"),
    UPDATE_FROM_MAP_BY_MULTI_ID("updateFromMapByMultiId", "根据多个ID 选择修改数据，支持空值", "<script>\nUPDATE %s %s WHERE %s %s\n</script>"),
    /**
     * 查询
     */
    SELECT_BY_ID("selectById", "根据ID查询一条数据", "SELECT %s FROM %s WHERE %s=#{%s} %s");


    private final String method;
    private final String desc;
    private final String sql;

    SqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
