package com.codowang.pensieve.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;

/**
 * 角色
 *
 * @author wangyb
 */
@Getter
@Setter
@ToString
public class UserRole extends AbstractEntity implements GrantedAuthority {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    public UserRole () {}

    public UserRole (Map<String, Object> info) {
        this.set(info);
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return "ROLE" + "_" + this.code;
    }
}