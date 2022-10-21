package com.codowang.pensieve.filter;

import com.codowang.pensieve.core.utils.MapUtils;
import com.codowang.pensieve.core.utils.StringUtils;
import com.codowang.pensieve.entity.WebUsernamePasswordAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义账号密码校验规则
 * 实现json数据的解析
 *
 * @author wangyb
 */
public class WebUsernamePasswordAuthenticationFilter extends org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter {

    /**
     * 重写用户密码认证，替换默认的认证逻辑
     * @param request                   请求
     * @param response                  响应
     * @return                          认证结果
     * @throws AuthenticationException  认证异常
     */
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        Authentication auth;
        // 2. 判断是否是json格式请求
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)
                || request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)){
            // 3. 从json格式中获取用户输入的用户名和密码
            Map<String, Object> userInfo = new HashMap<>();
            try {
                userInfo = MapUtils.castMap(
                        new ObjectMapper().readValue(request.getInputStream(), Map.class),
                        String.class,
                        Object.class
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            String username = StringUtils.getString(userInfo.get(getUsernameParameter()), "");
            String password = StringUtils.getString(userInfo.get(getPasswordParameter()), "");
            // 验证码
            String captcha = StringUtils.getString(userInfo.get(getCaptchaParameter()), "");
            // session中的登录验证码
            String sessionCaptcha = StringUtils.getString(request.getSession().getAttribute(getLoginCaptchaParameter()), "");
            // 移除验证码
            request.getSession().removeAttribute(getLoginCaptchaParameter());

            username = username.trim();

            WebUsernamePasswordAuthenticationToken authRequest = new WebUsernamePasswordAuthenticationToken(
                    username, password, sessionCaptcha, captcha);

            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);

            auth = this.getAuthenticationManager().authenticate(authRequest);
        } else {
            auth = super.attemptAuthentication(request, response);
        }
        return auth;
    }

    String getCaptchaParameter () {
        return "captcha";
    }

    String getLoginCaptchaParameter () {
        return "LOGIN-CAPTCHA";
    }
}
