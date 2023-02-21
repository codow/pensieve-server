package com.codowang.pensieve.proxy;

import java.lang.reflect.InvocationHandler;

public class DynamicProxyDemo implements IDynamicProxySubject {

    public void doSomething() {
        System.out.println("执行一些任务");
    }

    public static void main(String[] args) {

        DynamicProxyDemo demo = new DynamicProxyDemo();

        InvocationHandler invocationHandler = new MyInvocationHandler(demo);

        // 被代理类必须继承接口，否则会报错
        // 必须传入接口数组，否则会报错
        // 必须以接口作为接收对象的类型，否则会报错，动态代理对象实现的是接口不是实际类型
        IDynamicProxySubject proxy = DynamicProxy.newProxyInstance(demo.getClass().getClassLoader(), demo.getClass().getInterfaces(), invocationHandler);

        proxy.doSomething();
    }
}
