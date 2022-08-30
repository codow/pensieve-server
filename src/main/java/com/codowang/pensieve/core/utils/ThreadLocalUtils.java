package com.codowang.pensieve.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyb
 */
public abstract class ThreadLocalUtils {
    /**
     * 全局threadLocal对象
     */
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    private static Map<String, Object> getContext() {
        // 获取当前的上下文
        return THREAD_LOCAL.get();
    }

    public static void addContext() {
        THREAD_LOCAL.set(new HashMap<>(6));
    }

    public static void removeContext() {
        THREAD_LOCAL.remove();
    }

    /**
     * 给当前线程的threadLocal设置值
     * @param key 关键字
     * @param value 值
     */
    public static void put(String key, Object value) {
        Map<String, Object> context = getContext();
        if (context == null) {
            context = new HashMap<>(6);
            THREAD_LOCAL.set(context);
        }
        context.put(key, value);
    }

    /**
     * 获取当前threadLocal中的数据
     * @param key 关键字
     * @return 值
     */
    public static Object get(String key) {
        Map<String, Object> context = getContext();
        if (context == null) {
            return null;
        }
        return context.get(key);
    }
}
