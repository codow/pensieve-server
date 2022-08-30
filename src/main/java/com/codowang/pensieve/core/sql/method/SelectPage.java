package com.codowang.pensieve.core.sql.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class SelectPage extends AbstractMethod {

    public SelectPage() {
        super(SqlMethod.SELECT_PAGE.getMethod());
    }

    /**
     * @since 3.5.0
     * @param name 方法名
     */
    public SelectPage(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_PAGE;
        String sql = String.format(sqlMethod.getSql(), sqlFirst(),
                sqlSelectColumns(tableInfo, true),
                // 获取表名，允许别名
                getTableNameSql(tableInfo),
                sqlWhereEntityWrapper(true, tableInfo), sqlOrderBy(tableInfo), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, this.methodName, sqlSource, tableInfo);
    }
}