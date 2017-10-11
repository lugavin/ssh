package com.ssh.sys.core.service;

import com.ssh.common.dto.MapDTO;
import com.ssh.common.exception.BusinessException;
import com.ssh.common.util.BeanMapper;
import com.ssh.sys.api.dto.RoleDTO;
import com.ssh.sys.api.service.RoleService;
import com.ssh.sys.core.entity.PermissionEntity;
import com.ssh.sys.core.entity.RoleEntity;
import com.ssh.sys.core.enums.RoleStatus;
import com.ssh.sys.core.repository.PermissionRepository;
import com.ssh.sys.core.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Transactional
@Service(RoleService.BEAN_NAME)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void add(RoleDTO roleDTO) {
        roleRepository.save(map(roleDTO));
    }

    @Override
    public void add(Collection<RoleDTO> collection) {
        List<RoleEntity> list = new ArrayList<>();
        for (RoleDTO roleDTO : collection) {
            list.add(map(roleDTO));
        }
        roleRepository.save(list);
    }

    @Override
    public void update(RoleDTO roleDTO) {
        RoleEntity entity = roleRepository.findById(roleDTO.getId());
        if (entity == null) {
            throw new BusinessException("The record which id = " + roleDTO.getId() + " is not found!");
        }
        entity.setName(roleDTO.getName());
        entity.setRemark(roleDTO.getRemark());
        entity.setStatus(roleDTO.getStatus());
    }

    @Override
    public void delete(Long id) {
        roleRepository.delete(id);
    }

    @Override
    public void delete(Long[] ids) {
        List<RoleEntity> list = new ArrayList<>();
        for (Long id : ids) {
            RoleEntity entity = roleRepository.findById(id);
            if (entity != null) {
                list.add(entity);
            }
        }
        if (!list.isEmpty()) {
            roleRepository.delete(list);
        }
    }

    @Override
    public void assignPermissions(Long id, Long[] permissionIds) {
        RoleEntity entity = roleRepository.findById(id);
        if (entity == null) {
            throw new BusinessException("The record which id = " + id + " is not found!");
        }
        if (permissionIds != null && permissionIds.length > 0) {
            List<PermissionEntity> permissionList = permissionRepository.getListByPermissionIds(permissionIds);
            entity.setPermissions(new HashSet<>(permissionList));
        } else {
            entity.setPermissions(null);
        }
    }

    @Override
    public List<Long> getPermissionIds(Long id) {
        return roleRepository.getPermissionIds(id);
    }

    @Override
    public RoleDTO getById(Long id) {
        return roleRepository.getRoleById(id);
    }

    @Override
    public List<Map> getList(RoleDTO roleDTO) {
        return roleRepository.getRoleListSelective(roleDTO);
    }

    @Override
    public List<Map> getList(MapDTO mapDTO) {
        return roleRepository.getRoleListSelective(mapDTO);
    }

    @Override
    public List<Map> getListByUserId(Long userId) {
        return roleRepository.getRoleListByUserId(userId);
    }

    private RoleEntity map(RoleDTO roleDTO) {
        RoleEntity entity = BeanMapper.map(roleDTO, RoleEntity.class);
        entity.setStatus(RoleStatus.AVAILABLE.getValue());
        return entity;
    }

}
