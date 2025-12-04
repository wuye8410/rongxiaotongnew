package com.qst.crop.common.exception;

/**
 * 业务异常类，用于处理业务逻辑中的异常情况
 */
public class BusinessException extends RuntimeException {

    // 无参构造函数
    public BusinessException() {
        super();
    }

    // 带错误信息的构造函数
    public BusinessException(String message) {
        super(message);
    }

    // 带错误信息和 cause 的构造函数
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}