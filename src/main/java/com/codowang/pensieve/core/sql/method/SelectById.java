package com.codowang.pensieve.core.sql.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;

/**
 * 自定义SelectOne方法
 *
 * @author wangyb
 * @since 2022-07-07
 */
public class SelectById extends AbstractMethod {
    public SelectById() {
        this(SqlMethod.SELECT_BY_ID.getMethod());
    }

    public SelectById(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_BY_ID;
        SqlSource sqlSource = new RawSqlSource(configuration, String.format(sqlMethod.getSql(),
                sqlSelectColumns(tableInfo, false),
                // 获取表名，允许别名
                getTableNameSql(tableInfo),
                tableInfo.getKeyColumn(),
                tableInfo.getKeyProperty(),
                tableInfo.getLogicDeleteSql(true, true)), Object.class);
        return this.addSelectMappedStatementForTable(mapperClass, this.methodName, sqlSource, tableInfo);
    }
}
