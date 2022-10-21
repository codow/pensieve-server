package com.codowang.pensieve.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class UserOrg extends AbstractEntity {

    /**
     * 人员ID
     */
    private Long userId;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织层级码
     */
    private String levelCode;

    /**
     * 组织类型
     */
    private String type;

    /**
     * 组织类型名称
     */
    private String typeName;

    /**
     * 在组织机构内的排序号
     */
    private Integer sort;

    public UserOrg () {}

    public UserOrg (Map<String, Object> info) {
        this.set(info);
    }
}