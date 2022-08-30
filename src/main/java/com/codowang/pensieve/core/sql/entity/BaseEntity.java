package com.codowang.pensieve.core.sql.entity;

import com.codowang.pensieve.core.utils.ObjectUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 业务对象的抽象类，提供map跟BO的转换
 * @author wangyb
 */
public abstract class BaseEntity implements Serializable {

    /**
     * 获取Map数据
     * @return bo数据
     */
    public Map<String, Object> get() {
        return new ObjectMapper()
                .convertValue(this, new TypeReference<LinkedHashMap<String, Object>>() {});
    }

    /**
     * 通过Map设置BO属性
     * @param map map信息
     */
    public void set(Map<String, Object> map) {
        ObjectUtils.setPropertyValue(this, map);
    }

    /**
     * 通过Map设置BO属性
     * @param bo 对象
     */
    public void set(BaseEntity bo) {
        ObjectUtils.setPropertyValue(this, bo.get());
    }
}