package com.ssh.sys.core.enums;

public enum UserStatus {

    UNAVAILABLE(0),
    AVAILABLE(1);

    private final int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
