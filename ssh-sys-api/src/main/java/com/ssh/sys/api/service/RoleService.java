package com.ssh.sys.api.service;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.service.BaseService;
import com.ssh.sys.api.dto.RoleDTO;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<RoleDTO> {

    String BEAN_NAME = "roleService";

    void assignPermissions(@NotNull Long id, Long[] permissionIds);

    List<Long> getPermissionIds(@NotNull Long id);

    List<Map> getList(RoleDTO roleDTO);

    List<Map> getList(ModelMapDTO modelMapDTO);

    List<Map> getListByUserId(Long userId);

}
