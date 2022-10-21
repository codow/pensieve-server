package com.codowang.pensieve.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 账号类型
 *
 * @author wangyb
 */
@Getter
public enum AccountTypeEnum {
    // 1 - 4 内置的登录方式
    ACCOUNT_PASSWORD(1, "账号密码"),
    MOBILE_PHONE(2, "手机号码"),
    EMAIL(3, "邮箱"),
    ID_CARD_PASSWORD(4, "身份证密码"),
    // 11 开始第三方扩展的登录类型
    WECHAT(11, "微信登录"),
    WECHAT_EE(12, "企业微信登录"),
    WECHAT_OPEN(13, "微信开放平台登录"),
    QQ(14, "QQ登录"),
    ALIPAY(15, "支付宝登录");

    private final int value;

    private final String label;

    AccountTypeEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static AccountTypeEnum parse(int value) {
        return Arrays.stream(AccountTypeEnum.values())
                .filter(type -> type.getValue() == value)
                .findFirst()
                .orElse(null);
    }
}
