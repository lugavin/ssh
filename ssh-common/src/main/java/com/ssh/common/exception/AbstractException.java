package com.ssh.common.exception;

import com.ssh.common.util.MessageHelper;

import java.io.Serializable;

public abstract class AbstractException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorCode;
    private Serializable detailMessage;

    public AbstractException(String errorCode, Serializable detailMessage) {
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public AbstractException(String errorCode, Serializable detailMessage, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Serializable getDetailMessage() {
        return detailMessage;
    }

    @Override
    public String getMessage() {
        return MessageHelper.getMessage(errorCode) + " => " + detailMessage;
    }

}
