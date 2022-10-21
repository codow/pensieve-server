package com.codowang.pensieve.error;

import com.codowang.pensieve.entity.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;

/**
 * 规范错误异常信息
 *
 * @author wangyb
 * @since 2022-07-15
 */
@Getter
@Setter
public class WebAuthenticationException extends AuthenticationException {

    /**
     * 登录错误的账户
     */
    private UserAccount account;

    private SecurityErrorEnum errorEnum;

    private WebAuthenticationException(String msg) {
        super(msg);
    }

    private WebAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public WebAuthenticationException(SecurityErrorEnum errorEnum) {
        this(errorEnum, null);
    }

    public WebAuthenticationException(SecurityErrorEnum errorEnum, UserAccount account) {
        super(errorEnum.getLabel());
        this.errorEnum = errorEnum;
        this.account = account;
    }

    public WebAuthenticationException(SecurityErrorEnum errorEnum, UserAccount account, Throwable t) {
        super(errorEnum.getLabel(), t);
        this.errorEnum = errorEnum;
        this.account = account;
    }

    public CommonException toCommonException () {
        return new CommonException(errorEnum, this);
    }

    public CommonException toCommonException (String message) {
        return new CommonException(errorEnum, this, message);
    }
}
