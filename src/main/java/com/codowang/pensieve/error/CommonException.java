package com.codowang.pensieve.error;

/**
 * 通用系统错误，运行时异常
 *
 * @author wangyb
 */
public class CommonException extends RuntimeException {

    private String errorCode;

    public CommonException() {
        super();
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException(CommonError error) {
        super(error.getErrorMessage());
        this.errorCode = error.getErrorCode();
    }

    public CommonException(CommonError error, Throwable cause) {
        super(error.getErrorMessage(), cause);
        this.errorCode = error.getErrorCode();
    }

    public CommonException(CommonError error, Throwable cause, String message) {
        super(message, cause);
        this.errorCode = error.getErrorCode();
    }

    protected CommonException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getErrorCode() {
        return this.errorCode != null ? this.errorCode : "500";
    }

    public String getErrorMessage() {
        return this.getMessage();
    }
}
