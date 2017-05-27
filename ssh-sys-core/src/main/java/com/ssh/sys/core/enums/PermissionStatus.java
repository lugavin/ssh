package com.ssh.sys.core.enums;

public enum PermissionStatus {

    UNAVAILABLE(0), AVAILABLE(1);

    private final int value;

    PermissionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
