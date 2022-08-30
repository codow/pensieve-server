package com.codowang.pensieve.core.service;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.codowang.pensieve.core.sql.mapper.IMppBaseMapper;
import com.codowang.pensieve.core.utils.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

/**
 * 继承MybatisPlus-Plus的IMppService
 * 提供自定义的基本方法
 *
 * @param <T> 实体类
 * @author wangyb
 * @since 2022-07-18
 */
public interface IMppService<T> extends com.github.jeffreyning.mybatisplus.service.IMppService<T> {

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    default boolean updateFromMapByMultiId(T entity, Map<String, Object> map) {
        // 设置值
        ObjectUtils.setPropertyValue(entity, map);
        return SqlHelper.retBool(getBaseMapper().updateFromMapByMultiId(entity, map));
    }

    /**
     * 不存在时插入数据, 多主键
     *
     * @param entity 实体对象
     */
    boolean saveOnNotExistsByMultiId(T entity);

    /**
     * 不存在时插入数据，必须要定义主键字段，多主键
     * @param entityList 实体类集合
     * @return 是否成功
     */
    @Transactional(
            rollbackFor = {Exception.class}
    )
    default boolean saveBatchOnNotExistsByMultiId(Collection<T> entityList) {
        return this.saveBatchOnNotExistsByMultiId(entityList, 1000);
    }

    /**
     * 不存在时插入数据，必须要定义主键字段，多主键
     * @param entityList 实体类集合
     * @param batchSize 一次插入的数量
     * @return 是否成功
     */
    boolean saveBatchOnNotExistsByMultiId(Collection<T> entityList, int batchSize);

    IMppBaseMapper<T> getBaseMapper();
}
