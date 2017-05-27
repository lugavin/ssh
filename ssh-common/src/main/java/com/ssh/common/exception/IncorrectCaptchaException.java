package com.ssh.common.exception;

import java.io.Serializable;

public class IncorrectCaptchaException extends AuthcException {

    private static final long serialVersionUID = 20160502L;

    public IncorrectCaptchaException(Serializable detailMessage) {
        super(detailMessage);
    }

    public IncorrectCaptchaException(Serializable detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

}
