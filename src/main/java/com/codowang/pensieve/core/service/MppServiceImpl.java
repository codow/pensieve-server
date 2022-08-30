package com.codowang.pensieve.core.service;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.codowang.pensieve.core.sql.mapper.IMppBaseMapper;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 提供基础多主键service实现，继承MybatisPlus-Plus的MppServiceImpl
 *
 * @param <M> Mapper类
 * @param <T> 实体类
 * @author wangyb
 * @since 2022-07-19
 */
public class MppServiceImpl<M extends IMppBaseMapper<T>, T> extends com.github.jeffreyning.mybatisplus.service.MppServiceImpl<M, T> implements IMppService<T> {
    @Override
    public boolean saveOnNotExistsByMultiId(T entity) {
        if (entity != null) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!", new Object[0]);

            Map<String, String> idMap = checkIdCol(cls, tableInfo);
            Assert.notEmpty(idMap, "entity {} not contain MppMultiId anno", new Object[]{cls.getName()});

            boolean exists=true;
            for(String attr: idMap.keySet()){
                if(StringUtils.checkValNull(attr)){
                    exists=false;
                    break;
                }
            }
            if(exists){
                Object obj=this.selectByMultiId(entity);
                if(Objects.isNull(obj)){
                    exists=false;
                }
            }
            if (!exists) {
                return this.save(entity);
            }
        }
        return false;
    }

    @Override
    public boolean saveBatchOnNotExistsByMultiId(Collection<T> entityList, int batchSize) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!", this.entityClass.getName());

        Map<String, String> idMap=checkIdCol(this.entityClass, tableInfo);
        Assert.notEmpty(idMap, "entity {} not contain MppMultiId anno", this.entityClass.getName());

        return this.executeBatch(entityList, batchSize, (sqlSession, entity) -> {
            boolean exists = true;
            for(String attr: idMap.keySet()){
                if(StringUtils.checkValNull(attr)){
                    exists=false;
                    break;
                }
            }
            if(exists){
                Object obj=this.selectByMultiId(entity);
                if(Objects.isNull(obj)){
                    exists = false;
                }
            }
            if (!exists) {
                sqlSession.insert(tableInfo.getSqlStatement(SqlMethod.INSERT_ONE.getMethod()), entity);
            }
        });
    }

    private Map<String, String> checkIdCol(Class<?> modelClass, TableInfo tableInfo){
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
        return idMap;
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
