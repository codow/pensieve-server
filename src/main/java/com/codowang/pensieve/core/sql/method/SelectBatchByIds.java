package com.codowang.pensieve.core.sql.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class SelectBatchByIds  extends AbstractMethod {

    public SelectBatchByIds() {
        super(SqlMethod.SELECT_BATCH_BY_IDS.getMethod());
    }

    /**
     * @param name 方法名
     * @since 3.5.0
     */
    public SelectBatchByIds(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_BATCH_BY_IDS;
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, String.format(sqlMethod.getSql(),
                sqlSelectColumns(tableInfo, false),
                // 获取表名，允许别名
                getTableNameSql(tableInfo),
                tableInfo.getKeyColumn(),
                SqlScriptUtils.convertForeach("#{item}", COLL, null, "item", COMMA),
                tableInfo.getLogicDeleteSql(true, true)), Object.class);
        return addSelectMappedStatementForTable(mapperClass, this.methodName, sqlSource, tableInfo);
    }
}