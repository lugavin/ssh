package com.ssh.common.exception;

import java.io.Serializable;

/**
 * 应用异常
 */
public class AppException extends AbstractException {

    private static final long serialVersionUID = 1L;

    private static final String ERROR_CODE = "F-0001";

    public AppException(Serializable detailMessage) {
        super(ERROR_CODE, detailMessage);
    }

    public AppException(Serializable detailMessage, Throwable cause) {
        super(ERROR_CODE, detailMessage, cause);
    }

}
