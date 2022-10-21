package com.codowang.pensieve.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 统一的web请求结果，status为请求响应状态
 *
 * @author wangyb
 */
@Getter
@Setter
@ToString
public class WebResult {

    public static final String RESULT_SUCCESS = "success";

    public static final String RESULT_FAIL = "fail";

    private String result;

    private int status;

    private Object data;

    public WebResult (String result, int status, Object data) {
        this.result = result;
        this.status = status;
        this.data = data;
    }

    public static WebResult success(Object data){
        return new WebResult(RESULT_SUCCESS, 200, data);
    }

    public static WebResult fail(Object data){
        return new WebResult(RESULT_FAIL, 500, data);
    }
}
