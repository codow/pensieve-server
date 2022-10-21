package com.codowang.pensieve.entity;

import com.codowang.pensieve.core.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 定制化的认证令牌
 * 增加对验证码的支持
 *
 * @author wangyb
 * @since 2022-07-14
 */
@Getter
@Setter
public class WebUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String correctCaptcha;

    private String captcha;

    public WebUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public WebUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public WebUsernamePasswordAuthenticationToken(Object principal, Object credentials, String correctCaptcha, String captcha) {
        super(principal, credentials);
        this.correctCaptcha = correctCaptcha;
        this.captcha = captcha;
    }

    public boolean hasCaptcha () {
        return StringUtils.isNotBlank(this.correctCaptcha);
    }

    public boolean isCaptchaCorrect () {
        return hasCaptcha() && this.correctCaptcha.equalsIgnoreCase(this.captcha);
    }
}
