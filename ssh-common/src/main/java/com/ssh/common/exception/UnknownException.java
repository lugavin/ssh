package com.ssh.common.exception;

import java.io.Serializable;

/**
 * 未知异常
 */
public class UnknownException extends AbstractException {

    private static final long serialVersionUID = 20160320L;

    private static final String ERROR_CODE = "F-9999";

    public UnknownException(Serializable detailMessage) {
        super(ERROR_CODE, detailMessage);
    }

    public UnknownException(Serializable detailMessage, Throwable cause) {
        super(ERROR_CODE, detailMessage, cause);
    }

}
