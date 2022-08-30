package com.codowang.pensieve.core.sql.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 从map中获取数据，支持空值
 *
 * @author wangyb
 * @since 2022-07-18
 */
public class UpdateFromMapByMultiId extends AbstractMethod {

    public UpdateFromMapByMultiId() {
        this(SqlMethod.UPDATE_FROM_MAP_BY_MULTI_ID.getMethod());
    }

    public UpdateFromMapByMultiId(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.UPDATE_FROM_MAP_BY_MULTI_ID;
        final String additional = optlockVersion(tableInfo) + tableInfo.getLogicDeleteSql(true, true);
        String sql = String.format(sqlMethod.getSql(),
                tableInfo.getTableName(),
                sqlMapSet(
                        tableInfo.isWithLogicDelete(),
                        false,
                        tableInfo,
                        false,
                        MAP,
                        MAP_DOT,
                        ENTITY,
                        ENTITY_DOT
                ),
                // 创建多组件where条件
                createMultiIdWhere(modelClass, tableInfo),
                additional);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, this.methodName, sqlSource);
    }
}
