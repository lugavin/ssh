package com.ssh.sys.core.service;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.exception.BusinessException;
import com.ssh.common.util.BeanMapper;
import com.ssh.common.util.Constant;
import com.ssh.sys.api.dto.PermissionDTO;
import com.ssh.sys.api.dto.extension.PermissionExtDTO;
import com.ssh.sys.api.service.PermissionService;
import com.ssh.sys.core.entity.PermissionEntity;
import com.ssh.sys.core.enums.PermissionStatus;
import com.ssh.sys.core.enums.ResourceType;
import com.ssh.sys.core.repository.PermissionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Transactional
@Service(PermissionService.BEAN_NAME)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void add(PermissionDTO permissionDTO) {
        PermissionEntity entity = BeanMapper.map(permissionDTO, PermissionEntity.class);
        Long parentId = permissionDTO.getParentId();
        if (parentId != null) {
            PermissionEntity parent = permissionRepository.findById(parentId);
            if (parent == null) {
                throw new BusinessException("The record which id = " + parentId + " is not found!");
            }
            if (parent.getType() != ResourceType.MENU) {
                throw new BusinessException("The node which you select is not a menu node.");
            }
            entity.setParent(parent);
            if (StringUtils.isNotBlank(parent.getParentIds())) {
                entity.setParentIds(parent.getParentIds() + Long.toString(parentId) + Constant.SLASHES_SEPARATOR);
            } else {
                entity.setParentIds(Long.toString(parentId) + Constant.SLASHES_SEPARATOR);
            }
        } else {
            entity.setParent(null);
            entity.setParentIds(null);
        }
        entity.setStatus(PermissionStatus.AVAILABLE.getValue());
        entity.setType(ResourceType.valueOf(permissionDTO.getType()));
        permissionRepository.save(entity);
    }

    @Override
    public void add(Collection<PermissionDTO> collection) {
        for (PermissionDTO permissionDTO : collection) {
            add(permissionDTO);
        }
    }

    @Override
    public void update(PermissionDTO permissionDTO) {
        PermissionEntity entity = permissionRepository.findById(permissionDTO.getId());
        if (entity == null) {
            throw new BusinessException("The record which id = " + permissionDTO.getId() + " is not found!");
        }
        Long parentId = permissionDTO.getParentId();
        if (parentId != null) {
            PermissionEntity parent = permissionRepository.findById(parentId);
            if (parent == null) {
                throw new BusinessException("The record which id = " + parentId + " is not found!");
            }
            if (parent.getType() != ResourceType.MENU) {
                throw new BusinessException("The node which you select is not a menu node");
            }
            entity.setParent(parent);
            entity.setParentIds(StringUtils.trimToEmpty(parent.getParentIds()) + Long.toString(parentId) + Constant.SLASHES_SEPARATOR);
        } else {
            entity.setParent(null);
            entity.setParentIds(null);
        }
        // entity.setCode(permissionDTO.getCode());
        entity.setName(permissionDTO.getName());
        entity.setType(ResourceType.valueOf(permissionDTO.getType()));
        entity.setUrl(permissionDTO.getUrl());
        entity.setStatus(permissionDTO.getStatus());
        entity.setSeq(permissionDTO.getSeq());
        entity.setIcon(permissionDTO.getIcon());
        updateSubParentIds(entity);
    }

    /**
     * 树形菜单的递归更新ParentIds值
     */
    private void updateSubParentIds(PermissionEntity parent) {
        Set<PermissionEntity> children = parent.getChildren();
        if (children != null && !children.isEmpty()) {
            for (PermissionEntity child : children) {
                child.setParentIds(StringUtils.trimToEmpty(parent.getParentIds()) + Long.toString(parent.getId()) + Constant.SLASHES_SEPARATOR);
                updateSubParentIds(child);
            }
        }
    }

    @Override
    public void delete(Long id) {
        PermissionEntity entity = permissionRepository.findById(id);
        if (entity == null) {
            throw new BusinessException("The record which id = " + id + " is not found!");
        }
        delete(entity);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    /**
     * 树形菜单的递归删除
     */
    private void delete(PermissionEntity parent) {
        Set<PermissionEntity> children = parent.getChildren();
        if (children != null && !children.isEmpty()) {
            for (PermissionEntity child : children) {
                delete(child);
            }
        }
        permissionRepository.delete(parent);
    }

    @Override
    public PermissionExtDTO getById(Long id) {
        return permissionRepository.getPermissionById(id);
    }

    @Override
    public List<Map> getList(ModelMapDTO modelMapDTO) {
        return permissionRepository.getListSelective(modelMapDTO);
    }

}
