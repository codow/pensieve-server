package com.codowang.pensieve.error;

/**
 * 通用的错误对象
 *
 * @author wangyb
 */
public interface CommonError {

    /**
     * 获取错误代码
     *
     * @return 错误代码
     */
    String getErrorCode();

    /**
     * 获取错误描述
     *
     * @return 错误描述
     */
    String getErrorMessage();
}
