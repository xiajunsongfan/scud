package com.xj.scud.core.exception;

/**
 * Author: baichuan - xiajun
 * Date: 2017/06/27 11:28
 * 序列化异常
 */
public class SerializableException extends RuntimeException {
    public SerializableException() {
    }

    public SerializableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SerializableException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializableException(String message) {
        super(message);
    }

    public SerializableException(Throwable cause) {
        super(cause);
    }
}
