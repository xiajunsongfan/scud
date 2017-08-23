package com.xj.scud.core.exception;

/**
 * Author: baichuan - xiajun
 * Date: 2017/08/23 17:37
 */
public class ScudExecption extends RuntimeException {
    public ScudExecption() {
    }

    public ScudExecption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ScudExecption(String message, Throwable cause) {
        super(message, cause);
    }

    public ScudExecption(String message) {
        super(message);
    }

    public ScudExecption(Throwable cause) {
        super(cause);
    }
}
