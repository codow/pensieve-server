package com.codowang.pensieve.config;

import com.codowang.pensieve.core.utils.HttpResponseUtils;
import com.codowang.pensieve.data.WebSecurityData;
import com.codowang.pensieve.error.SecurityErrorEnum;
import com.codowang.pensieve.error.WebAuthenticationException;
import com.codowang.pensieve.filter.WebUsernamePasswordAuthenticationFilter;
import com.codowang.pensieve.helper.WebSecurityHelper;
import com.codowang.pensieve.provider.impl.WebAuthenticationProvider;
import com.codowang.pensieve.provider.impl.WebSecurityProvider;
import com.codowang.pensieve.service.impl.WebSecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统安全配置
 * @author wangyb
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 安全相关的配置
     */
    WebSecurityData webSecurityData;

    /**
     * 提供安全服务
     */
    WebSecurityService webSecurityService;

    /**
     * 提供认证服务
     */
    WebSecurityProvider webSecurityProvider;


    public WebSecurityConfig (WebSecurityData webSecurityData,
                              WebSecurityService webSecurityService,
                              WebSecurityProvider webSecurityProvider) {
        this.webSecurityData = webSecurityData;
        this.webSecurityService = webSecurityService;
        this.webSecurityProvider = webSecurityProvider;
        // 初始化WebSecurityHelper
        WebSecurityHelper.setSecurityProvider(webSecurityProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                .contentTypeOptions()
                .and()
                .and()
                // 开启跨域
                .cors()
                .configurationSource(corsConfigurationSource())
                .and();

        // 配置忽略跨站点伪造攻击的接口
        if (this.webSecurityData.getCsrfEnable()) {
            http.csrf()
                    .ignoringAntMatchers(this.webSecurityData.getCsrfExceptsArray());
        } else {
            // 关闭防止跨域攻击
            http.csrf().disable();
        }

        // 动态添加认证请求
        http
                .authorizeRequests()
                // 添加不需要登录验证的接口
                .antMatchers(this.webSecurityData.getExceptArray())
                .permitAll()
                // 添加需要认证的请求
                .antMatchers(this.webSecurityData.getPatternArray())
                .authenticated()
                .and();

        // 设置用户服务类，提供自定义的查询登录账号功能
        http.userDetailsService(this.webSecurityProvider)
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling()
                .authenticationEntryPoint(
                        // 处理未登录的异常
                        (HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
                                -> {
                            String errorMessage = authException.getMessage();
                            Exception e;
                            if ("Full authentication is required to access this resource".equals(errorMessage)) {
                                e = new WebAuthenticationException(SecurityErrorEnum.UN_LOGIN);
                            } else {
                                e = authException;
                            }
                            HttpResponseUtils.writeError(response, e);
                        }
                )
                .and()
                .logout()
                .logoutUrl(this.webSecurityData.getLogoutUrl())
                .logoutSuccessHandler(this.webSecurityService)
                .invalidateHttpSession(false)
                .permitAll();

        // 使用自定义的用户密码验证拦截器替代默认的处理器
        http.addFilterAt(this.getUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 替换spring security默认的认证provider
     * @return provider
     */
    @Bean
    public WebAuthenticationProvider daoAuthenticationProvider() {
        WebAuthenticationProvider provider = new WebAuthenticationProvider();
        // 加密方式
        provider.setPasswordEncoder(this.webSecurityProvider.getPasswordEncoder());
        // 用户详情
        provider.setUserDetailsService(this.webSecurityProvider);
        // 是否允许抛出异常
        provider.setHideUserNotFoundExceptions(false);
        // 设置登录错误配置
        provider.setLoginErrorNum(this.webSecurityData.getLoginErrorNum());
        provider.setLoginErrorLockNum(this.webSecurityData.getLoginErrorLockNum());
        provider.setLoginErrorIntervalNum(this.webSecurityData.getLoginErrorIntervalNum());
        provider.setLoginErrorIntervalTime(this.webSecurityData.getLoginErrorIntervalTime());
        return provider;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return httpServletRequest -> {
            CorsConfiguration configuration = new CorsConfiguration();
            // 根据引用设置哪些接口允许跨域
            configuration.setAllowedOrigins(this.webSecurityData.getOrigin());
            configuration.addAllowedMethod("*");//修改为添加而不是设置
            configuration.addAllowedHeader("*");//这里很重要，起码需要允许 Access-Control-Allow-Origin
            configuration.setAllowCredentials(true);
            return configuration;
        };
    }

    public Filter getUsernamePasswordAuthenticationFilter () throws Exception {
        WebUsernamePasswordAuthenticationFilter filter = new WebUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(this.authenticationManager());
        filter.setFilterProcessesUrl(this.webSecurityData.getLoginUrl());
        filter.setAuthenticationSuccessHandler(this.webSecurityService);
        filter.setAuthenticationFailureHandler(this.webSecurityService);
        filter.setUsernameParameter(this.webSecurityData.getUsername());
        filter.setPasswordParameter(this.webSecurityData.getPassword());
        return filter;
    }
}
