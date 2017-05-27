package com.ssh.common.exception;

import java.io.Serializable;

/**
 * 授权出错
 */
public class AuthzException extends AbstractException {

    private static final long serialVersionUID = 1L;

    private static final String ERROR_CODE = "F-0102";

    public AuthzException(Serializable detailMessage) {
        super(ERROR_CODE, detailMessage);
    }

    public AuthzException(Serializable detailMessage, Throwable cause) {
        super(ERROR_CODE, detailMessage, cause);
    }

}
