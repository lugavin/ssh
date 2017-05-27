package com.ssh.common.exception;

import java.io.Serializable;

/**
 * 认证出错
 */
public class AuthcException extends AbstractException {

    private static final long serialVersionUID = 1L;

    private static final String ERROR_CODE = "F-0101";

    public AuthcException(Serializable detailMessage) {
        super(ERROR_CODE, detailMessage);
    }

    public AuthcException(Serializable detailMessage, Throwable cause) {
        super(ERROR_CODE, detailMessage, cause);
    }

}
