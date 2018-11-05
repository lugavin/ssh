package com.ssh.sys.core.repository;

import com.ssh.common.core.annotation.Param;
import com.ssh.common.core.annotation.Query;
import com.ssh.common.core.annotation.RepositoryBean;
import com.ssh.common.core.repository.CrudRepository;
import com.ssh.common.dto.ModelMapDTO;
import com.ssh.sys.api.dto.RoleDTO;
import com.ssh.sys.core.entity.RoleEntity;

import java.util.List;
import java.util.Map;

@RepositoryBean
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    @Query
    List<Long> getPermissionIds(@Param("roleId") Long roleId);

    @Query
    RoleDTO getRoleById(@Param("roleId") Long roleId);

    @Query(transformer = Map.class)
    List<Map> getRoleListSelective(RoleDTO roleDTO);

    @Query(transformer = Map.class)
    List<Map> getRoleListSelective(ModelMapDTO modelMapDTO);

    @Query(transformer = Map.class)
    List<Map> getRoleListByUserId(@Param("userId") Long userId);

    @Query
    List<RoleEntity> getRoleListByRoleIds(@Param("roleIds") Long[] roleIds);

}
