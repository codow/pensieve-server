package com.codowang.pensieve.core.sql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.codowang.pensieve.core.sql.method.AbstractMethod;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 继承Mybatis-Plus的BaseMapper
 * 提供特有的mapper方法
 *
 * @param <T> 实体类
 * @author wangyb
 * @since 2022-07-18
 */
public interface IBaseMapper<T> extends BaseMapper<T> {

    /**
     * 通过Map传入更新的数据，从而支持设置字段为空值
     *
     * @param entity    实体类
     * @param map       参数
     * @return          更新条数
     */
    int updateFromMapById(@Param(Constants.ENTITY) T entity, @Param(AbstractMethod.MAP) Map<String, Object> map);
}
