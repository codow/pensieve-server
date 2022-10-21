package com.codowang.pensieve.entity;

import com.codowang.pensieve.core.utils.NumberUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户实体类
 *
 * @author wangyb
 */
@Getter
@Setter
@ToString
public class User extends AbstractEntity implements UserDetails {

    /**
     * 主键
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 姓
     */
    private String firstName;

    /**
     * 名
     */
    private String lastName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private String sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 默认组织
     */
    private String orgId;

    /**
     * 默认角色
     */
    private String roleId;

    /**
     * 所属部门信息
     */
    private List<UserOrg> orgList;

    /**
     * 所有角色信息
     */
    private List<UserRole> roleList;

    /**
     * 用户登录账户信息
     */
    private UserAccount loginAccount;

    /**
     * 是否锁定
     */
    private Integer isLocked;

    public User () {}

    public User (Map<String, Object> info) {
        this.set(info);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleList;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.loginAccount != null ? this.loginAccount.getAccount() : null;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.loginAccount != null ? this.loginAccount.getPassword() : null;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return this.loginAccount == null || NumberUtils.isFalse(this.loginAccount.getIsLocked());
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return NumberUtils.isFalse(this.isLocked);
    }
}
