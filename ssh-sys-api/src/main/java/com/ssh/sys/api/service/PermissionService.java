package com.ssh.sys.api.service;

import com.ssh.common.dto.MapDTO;
import com.ssh.common.service.BaseService;
import com.ssh.sys.api.dto.PermissionDTO;
import com.ssh.sys.api.dto.extension.PermissionExtDTO;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<PermissionDTO> {

    String BEAN_NAME = "permissionService";

    PermissionExtDTO getById(@NotNull Long id);

    List<Map> getList(MapDTO mapDTO);

}
