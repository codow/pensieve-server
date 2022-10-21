package com.codowang.pensieve.error;

import lombok.Getter;

import java.util.Arrays;

/**
 * 安全层错误
 *
 * @author wangyb
 */
@Getter
public enum SecurityErrorEnum implements CommonError {
    // ~ 定义错误信息
    UN_LOGIN("11000", "您还没登录，请登录后再访问"),
    ACCESS_TOKEN_UNKNOWN("11001","您的账号已在其他地方登录"),
    ACCESS_TOKEN_TIMEOUT("11002","访问令牌已超时"),
    USERNAME_PASSWORD_ERROR("11003","账号或密码错误"),
    ACCOUNT_LOCKED("11004","账号被锁定，无法登录，请联系管理员解封"),
    ACCOUNT_DEVICE_LOCKED("11005","账号在当前设备被锁定，无法登录，请联系管理员解封"),
    USER_UNKNOWN("11006", "账号不存在"),
    LOGIN_INFO_UNKNOWN("11007", "登录状态已失效，请重新登录"),
    PASSWORD_ERROR("11008","密码错误"),
    CAPTCHA_ERROR("11009","验证码错误"),
    INTERVAL_TIME_ERROR("11010","请在登录间隔外操作"),
    ACCOUNT_EXPIRED("11011","账户已过期"),
    PASSWORD_EXPIRED("11012","密码已过期"),
    USER_DISABLED("11013", "账号不可用"),
    UN_INSTANTIATION_SECURITY_PROVIDER("11081", "没有初始化安全服务");

    private final String value;
    private final String label;

    SecurityErrorEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static SecurityErrorEnum parse(String value) {
        return Arrays.stream(SecurityErrorEnum.values())
                .filter(type -> type.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getErrorCode() {
        return this.value;
    }

    @Override
    public String getErrorMessage() {
        return this.label;
    }
}
