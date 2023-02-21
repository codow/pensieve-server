package com.codowang.pensieve.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 自定义动态代理类
 *
 * @param <T>
 * @author wangyb
 */
public class DynamicProxy <T> {

    public static <T> T newProxyInstance(ClassLoader classLoader, Class<?>[] interfaces, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }
}
