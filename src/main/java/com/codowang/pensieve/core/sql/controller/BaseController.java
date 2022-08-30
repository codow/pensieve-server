package com.codowang.pensieve.core.sql.controller;

import com.codowang.pensieve.core.sql.entity.BaseResetData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BaseController {

    // 异常的统一拦截处理方法
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(Exception ex) {

        Map<String, Object> responseData = new HashMap<>(6);
        responseData.put("errorCode", "500");
        responseData.put("errorMessage", ex.getMessage());
        responseData.put("errorDetail", ex.getStackTrace());

        return BaseResetData.instance(responseData, "fail");
    }
}
