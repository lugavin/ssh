package com.ssh.sys.api.dto.extension;

import com.ssh.sys.api.dto.PermissionDTO;

public class PermissionExtDTO extends PermissionDTO {

    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

}
