package com.ssh.common.enums;

public enum StatusCode {

    FAILURE(0, "Failure"),
    SUCCESS(1, "Success");

    private final int code;

    private final String reasonPhrase;

    StatusCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int getCode() {
        return code;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

}
