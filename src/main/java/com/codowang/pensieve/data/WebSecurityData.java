package com.codowang.pensieve.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "elastic.security")
public class WebSecurityData {
    /**
     * 定义哪些用户是超级管理员
     */
    private List<String> administrators;

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
     * 忽略跨站点伪造攻击的接口
     */
    private List<String> csrfExcepts;

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
