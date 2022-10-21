package com.codowang.pensieve.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

/**
 * 账号实体类，账号的基本信息
 *
 * @author codowang
 */
@Getter
@Setter
@ToString
public class UserAccount extends AbstractEntity {

    /**
     * 主键
     */
    private Long accountId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码, 返回给前端，也不序列化到缓存中
     */
    private String password;

    /**
     * 首次登陆强制修改密码，1、必须修改，0、已修改或不启用
     */
    private Integer forceSecretChange;

    /**
     * 账号类型(1-账号密码 2-手机号密码 3-身份证密码)
     */
    private Integer type;

    /**
     * 登录错误次数
     */
    private Integer errorNum;

    /**
     * 上次错误登录时间
     */
    private Date errorTime;

    /**
     * 登录错误的间隔时间
     */
    private Date errorIntervalTime;

    /**
     * 是否锁定
     */
    private Integer isLocked;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 扩展信息(JSON格式)
     */
    private String extend;

    public UserAccount () {}

    public UserAccount (Map<String, Object> info) {
        this.set(info);
    }

    public Integer getErrorNum() {
        if (this.errorNum == null) {
            this.errorNum = 0;
        }
        return this.errorNum;
    }
}