package com.codowang.pensieve.core.sql.method;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectByMultiIdMethod extends AbstractMethod {

    private static final Logger logger = LoggerFactory.getLogger(SelectByMultiIdMethod.class);

    public SelectByMultiIdMethod () {
        this("selectByMultiId");
    }

    public SelectByMultiIdMethod (String methodName) {
        super(methodName);
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
    private String createWhere(Class<?> modelClass, TableInfo tableInfo){
        List<TableFieldInfo> fieldList=tableInfo.getFieldList();
        Field[] fieldArray= modelClass.getDeclaredFields();
        Map<String, String> idMap=new HashMap<>();
        for(Field field: fieldArray){
            MppMultiId mppMultiId= field.getAnnotation(MppMultiId.class);
            if(mppMultiId!=null){
                String attrName=field.getName();
                String colName=getCol(fieldList, attrName);
                idMap.put(attrName, colName);
            }
        }
        if(idMap.isEmpty()){
            logger.info("entity {} not contain MppMultiId anno", modelClass.getName());
            return null;
        }
        StringBuilder sb=new StringBuilder("");
        idMap.forEach((attrName, colName)->{
            if(sb.length() <=0){

            }else{
                sb.append(" and ");
            }
            sb.append(colName).append("=").append("#{").append(attrName).append("}");
        });
        return sb.toString();
    }
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {

        String cWhere=createWhere(modelClass, tableInfo);
        if(cWhere==null){
            return null;
        }
        //
        String sql = "SELECT %s FROM %s WHERE "+cWhere+" %s";

        SqlSource sqlSource = new RawSqlSource(this.configuration,
                String.format(
                        sql, this.sqlSelectColumns(tableInfo, false),
                        // 获取表名，允许别名
                        getTableNameSql(tableInfo),
                        tableInfo.getLogicDeleteSql(true, true)
                ),
                Object.class);
        return this.addSelectMappedStatementForTable(mapperClass, this.methodName, sqlSource, tableInfo);
    }

}