package com.ssh.sys.core.repository;

import com.ssh.common.core.annotation.Param;
import com.ssh.common.core.annotation.Query;
import com.ssh.common.core.annotation.RepositoryBean;
import com.ssh.common.core.repository.CrudRepository;
import com.ssh.common.dto.MapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.page.Pageable;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.core.entity.UserEntity;

import java.util.List;
import java.util.Map;

@RepositoryBean
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(transformer = UserDTO.class)
    UserDTO getUserByCode(@Param("code") String code);

    @Query(transformer = UserDTO.class)
    List<UserDTO> getUserListSelective(UserDTO userDTO);

    @Query(transformer = Map.class)
    List<Map> getUserListSelective(MapDTO mapDTO);

    @Query(value = "getUserListSelective", transformer = Map.class)
    Page<Map> getUserPageSelective(Pageable pageable);

    @Query
    List<UserEntity> getListByUserIds(@Param("userIds") Long[] userIds);

}
