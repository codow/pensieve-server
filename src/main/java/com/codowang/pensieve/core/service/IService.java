package com.codowang.pensieve.core.service;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.codowang.pensieve.core.sql.mapper.IBaseMapper;
import com.codowang.pensieve.core.utils.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

/**
 * 继承Mybatis-Plus的IService
 * 提供自定义的基本方法
 *
 * @param <T> 实体类
 * @author wangyb
 * @since 2022-07-18
 */
public interface IService<T> extends com.baomidou.mybatisplus.extension.service.IService<T> {

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     * @return 是否成功
     */
    default boolean updateFromMapById(T entity, Map<String, Object> map) {
        // 设置值
        ObjectUtils.setPropertyValue(entity, map);
        return SqlHelper.retBool(getBaseMapper().updateFromMapById(entity, map));
    }

    /**
     * 不存在时插入数据，必须要定义主键字段
     * @param entity 实体类
     * @return 是否成功
     */
    boolean saveOnNotExists(T entity);

    /**
     * 插入不存在的记录
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveBatchOnNotExists(Collection<T> entityList) {
        return saveBatchOnNotExists(entityList, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量插入不存在的记录
     *
     * @param entityList 实体对象集合
     * @param batchSize  每次的数量
     */
    boolean saveBatchOnNotExists(Collection<T> entityList, int batchSize);

    IBaseMapper<T> getBaseMapper();
}
