package com.codowang.pensieve.core.sql.entity;

import com.codowang.pensieve.core.sql.enums.SqlOperatorEnum;
import com.codowang.pensieve.core.utils.NumberUtils;
import com.codowang.pensieve.core.utils.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lvhl
 */
public class SqlQueryParams implements Serializable {

    /**
     * 查询条件
     */
    private Map<String, Map<String, Object>> params;

    /**
     * 分页条件
     */
    private SqlPagination pagination;

    /**
     * 排序规则
     */
    private Map<String, Object> sort;

    public Map<String, Map<String, Object>> getParams() {
        return params;
    }

    public Map<String, Object> getParam (String key) {
        if (params == null) {
            return null;
        }
        return params.get(key);
    }

    public Object getParamValue (String key) {
        if (params == null) {
            return null;
        }
        Map<String, Object> paramFieldInfo = params.get(key);
        if (paramFieldInfo == null) {
            return null;
        }
        return paramFieldInfo.get("value");
    }

    public String getStringParamValue (String key) {
       Object value = getParamValue(key);
       if (value == null) {
           return null;
       }
       return value.toString();
    }

    public Integer getIntegerParamValue (String key) {
        Object value = getParamValue(key);
        if (value == null) {
            return null;
        }
        return NumberUtils.toInteger(value);
    }

    public void setParams(Map<String, Map<String, Object>> params) {
        this.params = params;
    }

    public void setParam(String key, Object value) {
        this.setParam(key, value, null);
    }

    public void setParam(String key, Object value, SqlOperatorEnum operator) {
        Map<String, Object> param = this.getParam(key);
        if (param == null) {
            param = new HashMap<>(6);
            param.put("sql_operator", SqlOperatorEnum.EQUAL.name());
        }
        param.put("value", value);
        if (operator != null) {
            param.put("sql_operator", operator.name());
        }
        if (this.params == null) {
            this.params = new HashMap<>(6);
        }
        this.params.put(key, param);
    }

    public void removeParam(String key) {
        if (this.params == null) {
            return;
        }
        this.params.remove(key);
    }

    public void changeParamKey(String oldKey, String newKey) {
        if (StringUtils.isBlank(oldKey) || StringUtils.isBlank(newKey)) {
            return;
        }
        Map<String, Object> param = this.getParam(oldKey);
        if (param == null) {
            return;
        }
        this.removeParam(oldKey);
        if (this.params == null) {
            this.params = new HashMap<>(6);
        }
        this.params.put(newKey, param);
    }

    public void changeSortKey(String oldKey, String newKey) {
        if (this.sort == null) {
            return;
        }
        Object value = this.sort.get(oldKey);
        this.sort.put(newKey, value);
        this.sort.remove(oldKey);
    }

    public SqlPagination getPagination() {
        return pagination;
    }

    public void setPagination(SqlPagination pagination) {
        this.pagination = pagination;
    }

    public Map<String, Object> getSort() {
        return sort;
    }

    public void setSort(Map<String, Object> sort) {
        this.sort = sort;
    }

    public String getSorting() {
        return getSorting("");
    }

    public String getSorting(String defaultSorting) {
        StringBuilder sortBuilder = new StringBuilder();
        if (this.sort == null) {
            return defaultSorting;
        }
        for (String key : this.sort.keySet()) {
            sortBuilder.append(key);
            sortBuilder.append(" ");
            sortBuilder.append(this.sort.get(key) == null ? "" : this.sort.get(key));
            sortBuilder.append(",");
        }

        if (sortBuilder.length() > 0) {
            return " order by " + sortBuilder.substring(0, sortBuilder.length() - 1);
        }else{
            return defaultSorting;
        }
    }
}
