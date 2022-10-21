package com.codowang.pensieve.provider;

import com.codowang.pensieve.entity.User;

import java.io.Serializable;

/**
 * 认证服务提供者
 *
 * @author wangyb
 * @since 2022-07-14
 */
public interface IWebSecurityProvider {
    /**
     * 返回当前登录的用户
     *
     * @return 登录用户
     */
    User getLoginUser() ;

    /**
     * 根据用户ID，查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Serializable userId);

    /**
     * 根据当前的策略加密密码
     *
     * @param password 明文
     * @return 密文
     */
    String encodePassword(String password);

    /**
     * 校验密码是否一致
     * @param rawPassword       明文密码
     * @param encodedPassword   加密后的密码
     * @return                  是否一致
     */
    boolean checkPassword(String rawPassword, String encodedPassword);
}
