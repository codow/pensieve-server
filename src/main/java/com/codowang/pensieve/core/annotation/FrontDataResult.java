package com.codowang.pensieve.core.annotation;

import java.lang.annotation.*;

/**
 * 提供一个注解，将拦截此注解的返回值，转化为BaseResetData数据返回
 * 使用此注解的方法，返回类型必须是Object，需要同时支持方法的返回结果和BaseResetData类型的返回结果
 * @author wangyb
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FrontDataResult {
}
