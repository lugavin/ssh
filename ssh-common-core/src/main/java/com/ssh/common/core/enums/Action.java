package com.ssh.common.core.enums;

public enum Action {

    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String type;

    // (1)枚举的构造函数为私有的
    // (2)可以把枚举类型当作构造函数为私有的类
    Action(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
