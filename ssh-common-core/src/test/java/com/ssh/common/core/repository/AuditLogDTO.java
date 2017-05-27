package com.ssh.common.core.repository;

public class AuditLogDTO {

    private Long id;
    private String funcCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    @Override
    public String toString() {
        return "AuditLogDTO{" + "id=" + id + ", funcCode='" + funcCode + '\'' + '}';
    }
}