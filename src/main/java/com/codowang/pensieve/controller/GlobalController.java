package com.codowang.pensieve.controller;

import com.codowang.pensieve.core.utils.ErrorUtils;
import com.codowang.pensieve.entity.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class GlobalController {
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public WebResult globalException(HttpServletResponse response, Throwable ex) {
        ex.printStackTrace();
        return new WebResult(
                WebResult.RESULT_FAIL,
                response.getStatus(),
                ErrorUtils.toCommonExceptionResult(ex)
        );
    }
}
