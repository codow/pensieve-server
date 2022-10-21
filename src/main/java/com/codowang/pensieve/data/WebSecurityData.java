package com.codowang.pensieve.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义安全配置
 *
 * @author wangyb
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "system.security")
public class WebSecurityData {
    /**
     * 定义哪些用户是超级管理员
     */
    private List<String> administrators;

    /**
     * 登录参数，用户名
     */
    private String username = "account";

    /**
     * 登录参数，密码
     */
    private String password = "password";

    /**
     * 默认加密类型，支持bcrypt / ldap / MD4 / MD5 / noop / pbkdf2 / scrypt / SHA-1 / SHA-256 / sha256 / argon2
     */
    private String passwordEncoding = "bcrypt";

    /**
     * 需要访问权限验证的接口
     */
    private List<String> patterns;

    /**
     * 不需要访问权限验证的接口
     */
    private List<String> excepts;

    /**
     * 跨域配置，允许访问的域
     */
    private List<String> origin;

    /**
     * 是否开启防跨站伪造攻击
     */
    private Boolean csrfEnable = true;

    /**
     * 忽略跨站点伪造攻击的接口
     */
    private List<String> csrfExcepts;

    /**
     * 登录接口，默认为/security/login/authorize
     */
    private String loginUrl = "/security/login/authorize";

    /**
     * 退出接口，默认为/security/logout
     */
    private String logoutUrl = "/security/logout";

    /**
     * 允许登录错误次数
     */
    private int loginErrorNum = 2;

    /**
     * 错误后增加登录间隔
     */
    private int loginErrorIntervalNum = 5;

    /**
     * 错误后自动锁定的次数
     */
    private int loginErrorLockNum = 8;

    /**
     * 登录错误间隔时间（秒）
     */
    private int loginErrorIntervalTime = 120;

    /**
     * 自定义登录信息处理的filter序号
     */
    private int order = SecurityWebFiltersOrder.AUTHORIZATION.getOrder() + 100;

    public String[] getPatternArray () {
        List<String> temp = this.patterns;
        if (temp == null) {
            temp = new ArrayList<>();
        }
        return temp.toArray(new String[]{});
    }

    public String[] getExceptArray () {
        List<String> temp = this.excepts;
        if (temp == null) {
            temp = new ArrayList<>();
        }
        return temp.toArray(new String[]{});
    }

    public String[] getCsrfExceptsArray () {
        List<String> temp = this.csrfExcepts;
        if (temp == null) {
            temp = new ArrayList<>();
        }
        return temp.toArray(new String[]{});
    }
}
