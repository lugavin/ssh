package com.ssh.common.exception;

import java.io.Serializable;

/**
 * 业务异常
 */
public class BusinessException extends AbstractException {

    private static final long serialVersionUID = 1L;

    private static final String ERROR_CODE = "F-0003";

    public BusinessException(Serializable detailMessage) {
        super(ERROR_CODE, detailMessage);
    }

    public BusinessException(Serializable detailMessage, Throwable cause) {
        super(ERROR_CODE, detailMessage, cause);
    }

}
