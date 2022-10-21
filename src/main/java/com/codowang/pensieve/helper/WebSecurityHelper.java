package com.codowang.pensieve.helper;

import com.codowang.pensieve.entity.User;
import com.codowang.pensieve.error.SecurityErrorEnum;
import com.codowang.pensieve.provider.IWebSecurityProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

/**
 * 提供安全功能的静态调用，如密码加密，获取当前登录用户等
 *
 * @author wangyb
 * @since 2022-07-14
 */
public class WebSecurityHelper {

    private static final Log logger = LogFactory.getLog(WebSecurityHelper.class);

    // ~ 保存provider实体
    private static IWebSecurityProvider webSecurityProvider = null;

    // ~ methods

    /**
     * 设置安全功能提供者，由此来提供获取用户和密码加密实现
     * @param webSecurityProvider provider
     */
    public static void setSecurityProvider(IWebSecurityProvider webSecurityProvider) {
        WebSecurityHelper.webSecurityProvider = webSecurityProvider;
    }

    public static User getLoginUser () {
        checkSecurityProvider();
        return webSecurityProvider.getLoginUser();
    }

    public static User getUserById (Serializable userId) {
        checkSecurityProvider();
        return webSecurityProvider.getUserById(userId);
    }

    public static String encodePassword (String password) {
        checkSecurityProvider();
        return webSecurityProvider.encodePassword(password);
    }

    public static boolean checkPassword (String rawPassword, String encodedPassword) {
        checkSecurityProvider();
        return webSecurityProvider.checkPassword(rawPassword, encodedPassword);
    }

    private static void checkSecurityProvider() {
        if (webSecurityProvider == null) {
            logger.error(SecurityErrorEnum.UN_INSTANTIATION_SECURITY_PROVIDER.getLabel());
            throw new RuntimeException(SecurityErrorEnum.UN_INSTANTIATION_SECURITY_PROVIDER.getLabel());
        }
    }
}
