package com.codowang.pensieve.core.sql.method;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.codowang.pensieve.core.annotation.FieldUpdateFill;
import com.codowang.pensieve.core.annotation.SelectSql;
import com.codowang.pensieve.core.sql.enums.FieldFillStrategy;
import com.codowang.pensieve.core.sql.provider.ISqlProvider;
import com.codowang.pensieve.core.sql.provider.SqlProviderHelper;
import com.codowang.pensieve.core.utils.StringUtils;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public abstract class AbstractMethod extends com.baomidou.mybatisplus.core.injector.AbstractMethod {

    /**
     * sql参数中，map参数的默认名字
     */
    public static final String MAP = "map";

    public static final String MAP_DOT = "map.";

    @Deprecated
    public AbstractMethod() {
        super();
    }

    public AbstractMethod(String name) {
        super(name);
    }

    /**
     * SQL 查询所有表字段
     *
     * @param table        表信息
     * @param queryWrapper 是否为使用 queryWrapper 查询
     * @return sql 脚本
     */
    @Override
    protected String sqlSelectColumns(TableInfo table,
                                      boolean queryWrapper) {
        /* 假设存在用户自定义的 resultMap 映射返回 */
        String selectColumns = ASTERISK;
        if (table.getResultMap() == null || table.isAutoInitResultMap()) {
            /* 未设置 resultMap 或者 resultMap 是自动构建的,视为属于mp的规则范围内 */
            // 表字段列表，增加虚拟字段
            List<Field> tableColumnFieldList = getSelectFields(table);
            // 构建查询结果
            selectColumns = sqlSelectColumnsByFields(getActualTableName(table), tableColumnFieldList);
        }
        if (!queryWrapper) {
            return selectColumns;
        }
        return convertChooseEwSelect(selectColumns);
    }

    private String getTableAlias (TableInfo tableInfo) {
        SelectSql sqlAnno = tableInfo.getEntityType().getAnnotation(SelectSql.class);
        if (sqlAnno != null && StringUtils.isNotBlank(sqlAnno.alias())) {
            return sqlAnno.alias();
        }
        return "";
    }

    private String getActualTableName (TableInfo tableInfo) {
        String tableAlias = getTableAlias(tableInfo);
        return StringUtils.isNotBlank(tableAlias) ? tableAlias : tableInfo.getTableName();
    }

    protected String getTableNameSql (TableInfo tableInfo) {
        String tableAlias = getTableAlias(tableInfo);
        String tableName = tableInfo.getTableName();
        if (StringUtils.isNotBlank(tableAlias)) {
            return tableName + " " + tableAlias;
        }
        return tableName;
    }

    private String sqlSelectColumnsByFields (String tableName, List<Field> fields) {
        StringBuilder columnsBuilder = new StringBuilder();
        fields.forEach(field -> {
            // 获取TableField注解, 或者TableId注解，TableField优先
            String columnName = this.getColumnName(field);
            // 查询注解
            SelectSql anno = field.getAnnotation(SelectSql.class);
            // 获取查询sql
            String columnSql = getColumnSql(tableName, columnName, anno);
            // 获取查询的名称，可以是别名
            String alias = getColumnAlias(columnName, field.getName(), anno);
            // 别名和查询sql一致时，不设置别名
            if (!alias.equals(columnSql)) {
                columnSql += " AS " + alias;
            }
            // 获取虚拟字段
            columnsBuilder.append(columnSql).append(",");
        });
        return columnsBuilder.substring(0, columnsBuilder.length() - 1);
    }

    private String getColumnName (Field field) {
        TableField tableFieldAnno = field.getAnnotation(TableField.class);
        if (tableFieldAnno != null) {
            if (!tableFieldAnno.exist()) {
                return "";
            }
            if (StringUtils.isNotBlank(tableFieldAnno.value())) {
                return tableFieldAnno.value();
            }
        }
        TableId tableIdAnno = field.getAnnotation(TableId.class);
        if (tableIdAnno != null) {
            if (StringUtils.isNotBlank(tableIdAnno.value())) {
                return tableIdAnno.value();
            }
        }
        return StringUtils.snakeCase(field.getName());
    }

    private String getColumnSql (String tableName, String columnName, SelectSql anno) {
        if (anno == null) {
            return columnName;
        }
        if (StringUtils.isBlank(columnName)) {
            columnName = anno.column();
        }
        // 如果是虚拟字段，那么必须要指定实际字段名
        if (StringUtils.isBlank(columnName)) {
            throw new RuntimeException("虚拟字段必须指定对应的列名，可以通过ElasticFieldSelectSql的column属性设置。");
        }
        //
        String tableColumnName = getTableColumn(tableName, columnName);
        // 获取参数
        String sqlValue = anno.value();
        if (StringUtils.isNotBlank(sqlValue)) {
            return sqlValue.replaceAll("#\\{field}", tableColumnName);
        }
        // 使用sql构建者构建sql
        Class<? extends ISqlProvider> sqlProvider = anno.using();
        if (SqlProviderHelper.None.class.equals(sqlProvider)) {
            return columnName;
        }
        try {
            return "(" +
                    sqlProvider.newInstance().getSql(tableColumnName, anno.params())
                    + ")";
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("create sql error", e);
        }
    }

    private String getColumnAlias (String columnName, String fieldName, SelectSql anno) {
        if (anno != null && StringUtils.isNotBlank(anno.alias())) {
            return anno.alias();
        }
        return StringUtils.isNotBlank(columnName) ? columnName : StringUtils.snakeCase(fieldName);
    }

    private String getTableColumn(String table, String column) {
        return StringUtils.isNotBlank(table) ? table + "." + column : column;
    }

    private List<Field> getSelectFields (TableInfo table) {
        return Arrays.stream(table.getEntityType().getDeclaredFields()).filter(field -> {
            TableField tableFieldAnno = field.getAnnotation(TableField.class);
            // 只查询虚拟字段，没有tableField注解或者exist为false的就是虚拟字段
            if (tableFieldAnno != null && tableFieldAnno.exist()) {
                return true;
            }
            TableId tableIdAnno = field.getAnnotation(TableId.class);
            if (tableIdAnno != null) {
                return true;
            }
            SelectSql sqlAnno = field.getAnnotation(SelectSql.class);
            // 过滤没有自定查询条件sql
            return sqlAnno != null;
        }).collect(Collectors.toList());
    }

    /**
     * SQL 更新 set 语句
     *
     * @param logic  是否逻辑删除注入器
     * @param ew     是否存在 UpdateWrapper 条件
     * @param table  表信息
     * @param alias  别名
     * @param prefix 前缀
     * @return sql
     */
    protected String sqlMapSet(boolean logic,
                               boolean ew,
                               TableInfo table,
                               boolean judgeAliasNull,
                               final String alias,
                               final String prefix,
                               final String etAlias,
                               final String etPrefix) {
        String sqlScript = this.getAllSqlMapSet(table, logic, alias, prefix, etAlias, etPrefix);
        if (judgeAliasNull) {
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", alias), true);
        }
        if (ew) {
            sqlScript += NEWLINE;
            sqlScript += convertIfEwParam(U_WRAPPER_SQL_SET, false);
        }
        sqlScript = SqlScriptUtils.convertSet(sqlScript);
        return sqlScript;
    }

    /**
     * 获取所有的 sql set 片段
     *
     * @param ignoreLogicDelFiled 是否过滤掉逻辑删除字段
     * @param prefix              前缀
     * @return sql 脚本片段
     */
    public String getAllSqlMapSet(TableInfo tableInfo,
                                  boolean ignoreLogicDelFiled,
                                  final String alias,
                                  final String prefix,
                                  final String etAlias,
                                  final String etPrefix) {
        final String newPrefix = prefix == null ? MAP : prefix;
        boolean isWithLogicDelete = tableInfo.isWithLogicDelete();
        return tableInfo.getFieldList().stream()
                .filter(i -> {
                    if (ignoreLogicDelFiled) {
                        return !(isWithLogicDelete && i.isLogicDelete());
                    }
                    return true;
                }).map(i -> {
                    if (i.isWithUpdateFill()) {
                        FieldUpdateFill anno = i.getField().getAnnotation(FieldUpdateFill.class);
                        if (anno == null || FieldFillStrategy.ALWAYS.equals(anno.strategy())) {
                            return i.getSqlSet(etPrefix);
                        }
                        return this.getSqlMapSet(i, false, true, alias, newPrefix, etAlias, etPrefix);
                    } else {
                        return this.getSqlMapSet(i, false, false, alias, newPrefix, etAlias, etPrefix);
                    }
                }).filter(Objects::nonNull).collect(joining(NEWLINE));
    }

    /**
     * 获取 set sql 片段
     *
     * @param ignoreIf 忽略 IF 包裹
     * @param prefix   前缀
     * @return sql 脚本片段
     */
    public String getSqlMapSet(TableFieldInfo field,
                               final boolean ignoreIf,
                               final boolean useEntity,
                               final String alias,
                               final String prefix,
                               final String etAlias,
                               final String etPrefix) {
        final String newPrefix = prefix == null ? MAP_DOT : prefix;
        // 默认: column=
        String column = field.getColumn();
        String sqlSet = column + EQUALS;
        String update = field.getUpdate();
        if (StringUtils.isNotBlank(update)) {
            sqlSet += String.format(update, column);
        } else if (useEntity) {
            sqlSet += SqlScriptUtils.safeParam(etPrefix + field.getProperty());
        } else {
            sqlSet += SqlScriptUtils.safeParam(newPrefix + column);
        }
        sqlSet += COMMA;
        if (ignoreIf) {
            return sqlSet;
        }
        return convertMapIf(field, sqlSet, alias, convertIfProperty(newPrefix, field.getProperty()), field.getUpdateStrategy());
    }

    /**
     * 转换成 if 标签的脚本片段
     *
     * @param field         字段信息
     * @param sqlScript     sql 脚本片段
     * @param property      字段名
     * @param fieldStrategy 验证策略
     * @return if 脚本片段
     */
    private String convertMapIf(TableFieldInfo field, final String sqlScript, final String alias, final String property, final FieldStrategy fieldStrategy) {
        if (fieldStrategy == FieldStrategy.NEVER) {
            return null;
        }
        if (field.isPrimitive() || fieldStrategy == FieldStrategy.IGNORED) {
            return sqlScript;
        }
        if (fieldStrategy == FieldStrategy.NOT_EMPTY && field.isCharSequence()) {
            return SqlScriptUtils.convertIf(
                    sqlScript,
                    String.format("%s.containsKey('%s') and %s != null and %s != ''",
                            alias, field.getColumn(), property, property),
                    false);
        }
        return SqlScriptUtils.convertIf(sqlScript, String.format("%s.containsKey('%s')", alias, field.getColumn()), false);
    }

    private String convertIfProperty(String prefix, String property) {
        return StringUtils.isNotBlank(prefix) ? prefix.substring(0, prefix.length() - 1) + "['" + property + "']" : property;
    }

    protected String createMultiIdWhere(Class<?> modelClass, TableInfo tableInfo){
        List<TableFieldInfo> fieldList=tableInfo.getFieldList();
        Field[] fieldArray= modelClass.getDeclaredFields();
        Map<String, String> idMap=new HashMap();
        for(Field field: fieldArray){
            MppMultiId mppMultiId= field.getAnnotation(MppMultiId.class);
            if(mppMultiId!=null){
                String attrName=field.getName();
                String colName=getCol(fieldList, attrName);
                idMap.put(attrName, colName);
            }
        }
        if(idMap.isEmpty()){
            logger.debug("entity {} not contain MppMultiId anno, model " + modelClass.getName());
            return null;
        }
        StringBuilder sb=new StringBuilder("");
        idMap.forEach((attrName, colName)->{
            if(sb.length() > 0){
                sb.append(" and ");
            }
            sb.append(colName).append("=").append("#{et.").append(attrName).append("}");
        });
        return sb.toString();
    }

    private String getCol(List<TableFieldInfo> fieldList, String attrName){
        for(TableFieldInfo tableFieldInfo: fieldList){
            String prop=tableFieldInfo.getProperty();
            if(prop.equals(attrName)){
                return tableFieldInfo.getColumn();
            }
        }
        throw new RuntimeException("not found column for "+attrName);
    }
}
