package com.ssh.sys.core.repository;

import com.ssh.common.core.annotation.Param;
import com.ssh.common.core.annotation.Query;
import com.ssh.common.core.annotation.RepositoryBean;
import com.ssh.common.core.repository.CrudRepository;
import com.ssh.common.dto.MapDTO;
import com.ssh.common.subject.Permission;
import com.ssh.sys.api.dto.extension.PermissionExtDTO;
import com.ssh.sys.core.entity.PermissionEntity;

import java.util.List;
import java.util.Map;

@RepositoryBean
public interface PermissionRepository extends CrudRepository<PermissionEntity, Long> {

    @Query(transformer = PermissionExtDTO.class)
    PermissionExtDTO getPermissionById(@Param("permissionId") Long permissionId);

    @Query(transformer = Map.class)
    List<Map> getListSelective(MapDTO mapDTO);

    @Query(transformer = Permission.class)
    List<Permission> getMenuListByUserId(@Param("userId") Long userId);

    @Query(transformer = Permission.class)
    List<Permission> getFunctionListByUserId(@Param("userId") Long userId);

    @Query
    List<PermissionEntity> getListByPermissionIds(@Param("permissionIds") Long[] permissionIds);

}
