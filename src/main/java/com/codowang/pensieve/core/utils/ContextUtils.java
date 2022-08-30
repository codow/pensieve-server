package com.codowang.pensieve.core.utils;

import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author wangyubo
 */
public abstract class ContextUtils {

    private volatile static ApplicationContext context;

    public static ApplicationContext getContext () {
        return context;
    }

    public static void setContext (ApplicationContext context) {
        ContextUtils.context = context;
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    public static Object getBean(String name, Object... objects) {
        return context.getBean(name, objects);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, Object... clazzes) {
        return context.getBean(clazz, clazzes);
    }

    public static Map<String, Object> getLoginUser() {
        return MapUtils.castMap(ThreadLocalUtils.get("loginUser"), String.class, Object.class);
    }

    public static String getLoginUserId() {
        Map<String, Object> loginUser = getLoginUser();
        if (loginUser == null ) {
            return null;
        }
        return MapUtils.getString(loginUser, "user_id");
    }

    public static String getLoginUserName() {
        Map<String, Object> loginUser = getLoginUser();
        if (loginUser == null ) {
            return null;
        }
        return MapUtils.getString(loginUser, "user_name");
    }
}
