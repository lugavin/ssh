package com.ssh.sys.core.service;

import com.ssh.common.subject.ActiveUser;
import com.ssh.common.subject.Permission;
import com.ssh.common.subject.SecurityService;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.core.repository.PermissionRepository;
import com.ssh.sys.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service(SecurityService.BEAN_NAME)
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public ActiveUser getActiveUser(String userCode) {
        UserDTO dto = userRepository.getUserByCode(userCode);
        if (dto == null) {
            return null;
        }
        ActiveUser activeUser = new ActiveUser();
        activeUser.setId(dto.getId());
        activeUser.setCode(dto.getCode());
        activeUser.setName(dto.getName());
        activeUser.setPass(dto.getPass());
        activeUser.setSalt(dto.getSalt());
        activeUser.setStatus(dto.getStatus());
        return activeUser;
    }

    @Override
    public List<Permission> getMenuList(Long userId) {
        return permissionRepository.getMenuListByUserId(userId);
    }

    @Override
    public List<Permission> getFunctionList(Long userId) {
        return permissionRepository.getFunctionListByUserId(userId);
    }

}
