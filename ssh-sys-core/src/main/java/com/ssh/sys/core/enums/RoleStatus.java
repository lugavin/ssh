package com.ssh.sys.core.enums;

public enum RoleStatus {

    UNAVAILABLE(0), AVAILABLE(1);

    private final int value;

    RoleStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
