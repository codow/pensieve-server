package com.codowang.pensieve.pkgtest;

public class MyAbstract implements IMyInterface {

    private String a;
    private String b;
    private String c;

    @Override
    public MyAbstract setA() {
        return this;
    }

    @Override
    public MyAbstract setB() {
        return null;
    }

    @Override
    public MyAbstract setC() {
        return null;
    }

    @Override
    public void println() {

    }
}
