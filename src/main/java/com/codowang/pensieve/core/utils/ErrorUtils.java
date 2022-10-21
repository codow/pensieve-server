package com.codowang.pensieve.core.utils;

import com.codowang.pensieve.error.CommonException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 错误工具类
 * @author wangyb
 */
public class ErrorUtils {

    private ErrorUtils () {

    }

    /**
     * 获取异常的原始异常
     * @param e 异常
     * @return 原始异常
     */
    public static Throwable getRootException(Exception e) {
        Throwable current = e;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current;
    }

    /**
     * 错误信息
     * xxxx
     * com.xx.xx.x.x(12)
     * com.xx.xx.x.x(129)
     * ...
     *
     * @param throwable 抛出的异常
     * @return 错误信息
     */
    public static String getWholeExceptionMessage(Throwable throwable) {
        return "Exception Message: " + throwable.getMessage() + "\n"
                +"Exception Stack Trace: \n"
                + getExceptionStack(throwable);
    }

    /**
     * 获取异常的堆栈信息
     *
     * @param throwable 抛出的异常
     * @return 堆栈信息
     */
    public static String getExceptionStack(Throwable throwable) {
        // 计算堆栈信息，设置再错误详情中
        // 将堆栈信息转换为字符串
        Writer str = new StringWriter();
        PrintWriter printWriter = new PrintWriter(str);

        //获取堆栈信息
        Throwable[] throwableArray = throwable.getSuppressed();

        if (throwableArray.length > 0) {
            throwableArray[throwableArray.length - 1].printStackTrace(printWriter);
        } else {
            throwable.printStackTrace(printWriter);
        }

        return str.toString();
    }

    /**
     * 获取错误信息的结果
     *
     * @param e 抛出的异常
     * @return {"error_code": xxx, "error_message": xxx}
     */
    public static Map<String, Object> toCommonExceptionResult(Throwable e) {
        CommonException commonException;
        if (e instanceof CommonException) {
            commonException = (CommonException) e;
        } else {
            commonException = new CommonException(e);
        }
        return new HashMap<String, Object>(){{
            put("error_code", commonException.getErrorCode());
            put("error_message", commonException.getErrorMessage());
        }};
    }
}
