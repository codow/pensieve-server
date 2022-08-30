package com.codowang.pensieve.core.sql.mapper;

import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import com.codowang.pensieve.core.sql.method.AbstractMethod;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 继承MybatisPlus-Plus的MppBaseMapper
 * 提供特有的mapper方法
 *
 * @param <T> 实体类
 * @author wangyb
 * @since 2022-07-18
 */
public interface IMppBaseMapper<T> extends MppBaseMapper<T> {

    /**
     * 通过Map传入更新的数据，从而支持设置字段为空值
     *
     * @param entity    实体类
     * @param map       参数
     * @return          更新条数
     */
    int updateFromMapByMultiId(@Param("et") T entity, @Param(AbstractMethod.MAP)Map<String, Object> map);
}
