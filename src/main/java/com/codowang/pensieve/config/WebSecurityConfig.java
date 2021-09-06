package com.codowang.pensieve.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 系统安全配置
 * @author wangyb
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 开启跨域
                .cors().and()
                // 关闭防止跨域攻击
                // .csrf().disable()
                .csrf().ignoringAntMatchers("/druid/**/*.json").and()
                .authorizeRequests()
                    .antMatchers("/", "/home")
                        .permitAll()
                    .antMatchers("/druid/**")
                        .hasRole("ROOT")
                    .anyRequest()
                        .authenticated()
                    .and()
                    .formLogin()
                        .loginPage("/login")
                        .permitAll()
                    .and()
                    .logout()
//                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .permitAll();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("111111").roles("USER").and()
//                .withUser("root").password("111111").roles("USER", "ROOT");
//    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails rootUser = User.withDefaultPasswordEncoder()
                        .username("root")
                        .password("111111")
                        .roles("ROOT", "USER")
                        .build();

        UserDetails commonUser = User.withDefaultPasswordEncoder()
                .username("common")
                .password("111111")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(rootUser, commonUser);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return httpServletRequest -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.addAllowedOrigin("*");//修改为添加而不是设置，* 最好改为实际的需要，我这是非生产配置，所以粗暴了一点
            configuration.addAllowedMethod("*");//修改为添加而不是设置
            configuration.addAllowedHeader("*");//这里很重要，起码需要允许 Access-Control-Allow-Origin
            configuration.setAllowCredentials(true);
            return configuration;
        };
    }
}
