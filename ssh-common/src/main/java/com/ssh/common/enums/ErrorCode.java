package com.ssh.common.enums;

public enum ErrorCode {

    // ========== 正常的 ========== //
    ERR_OK("T-0000"),

    // ========== System(从1000下标开始) ========== //
    ERR_SYSTEM("F-1000"),

    // ========== Database(从2000下标开始) ========== //
    ERR_DATABASE("F-2000"),
    ERR_DATABASE_RECORD_NOT_FOUND("F-2001"),
    ERR_DATABASE_MULTIPLE_RECORDS("F-2002"),

    // ========== Authenticate(从2100下标开始) ========== //
    ERR_AUTH("F-2100"),
    ERR_AUTH_INCORRECT_CREDENTIALS("F-2101"),
    ERR_AUTH_INCORRECT_CAPTCHA("F-2102"),

    // ========== Validation(从2200下标开始) ========== //
    ERR_VALIDATION("F-2200"),
    ERR_VALIDATION_PARAM_EMPTY("F-2201"),

    // ========== Business(从2300下标开始) ========== //
    ERR_BUSINESS("F-2300"),

    ERR_UNKNOWN("F-9999");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
