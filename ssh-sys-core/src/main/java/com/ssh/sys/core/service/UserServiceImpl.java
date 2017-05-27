package com.ssh.sys.core.service;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.exception.BusinessException;
import com.ssh.common.page.Page;
import com.ssh.common.page.PageRequest;
import com.ssh.common.subject.ActiveUser;
import com.ssh.common.util.BeanMapper;
import com.ssh.common.util.SecurityHelper;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.api.service.UserService;
import com.ssh.sys.core.entity.RoleEntity;
import com.ssh.sys.core.entity.UserEntity;
import com.ssh.sys.core.enums.UserStatus;
import com.ssh.sys.core.repository.RoleRepository;
import com.ssh.sys.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Transactional
@Service(UserService.BEAN_NAME)
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_PASS = "111111";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void add(UserDTO userDTO) {
        userRepository.save(map(userDTO));
    }

    @Override
    public void add(Collection<UserDTO> collection) {
        List<UserEntity> list = new ArrayList<>();
        for (UserDTO userDTO : collection) {
            list.add(map(userDTO));
        }
        userRepository.save(list);
    }

    @Override
    public void update(UserDTO userDTO) {
        UserEntity entity = userRepository.findById(userDTO.getId());
        if (entity == null) {
            throw new BusinessException(String.format("The record which id = %d is not found!", userDTO.getId()));
        }
        entity.setName(userDTO.getName());
        entity.setStatus(userDTO.getStatus());
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void delete(Long[] ids) {
        List<UserEntity> list = userRepository.getListByUserIds(ids);
        if (!list.isEmpty()) {
            userRepository.delete(list);
        }
    }

    @Override
    public boolean checkPass(String pass) {
        ActiveUser activeUser = SecurityHelper.getActiveUser();
        return activeUser.getPass().equals(SecurityHelper.generateMd5Hash(pass, activeUser.getSalt()));
    }

    @Override
    public void changePass(String oldPass, String newPass) {
        if (!checkPass(oldPass)) {
            throw new BusinessException("The password is incorrect！");
        }
        UserEntity entity = userRepository.findById(SecurityHelper.getActiveUser().getId());
        String salt = SecurityHelper.generateRandomNumber();
        entity.setSalt(salt);
        entity.setPass(SecurityHelper.generateMd5Hash(newPass, salt));
    }

    @Override
    public void resetPass(Long[] ids) {
        List<UserEntity> list = userRepository.getListByUserIds(ids);
        for (UserEntity entity : list) {
            String salt = SecurityHelper.generateRandomNumber();
            entity.setSalt(salt);
            entity.setPass(SecurityHelper.generateMd5Hash(DEFAULT_PASS, salt));
        }
    }

    @Override
    public void assignRoles(Long id, Long[] roleIds) {
        UserEntity entity = userRepository.findById(id);
        if (entity == null) {
            throw new BusinessException("The record which id = " + id + " is not found!");
        }
        if (roleIds != null && roleIds.length > 0) {
            List<RoleEntity> roleList = roleRepository.getRoleListByRoleIds(roleIds);
            entity.setRoles(new HashSet<>(roleList));
        } else {
            entity.setRoles(null);
        }
    }

    @Override
    public UserDTO getById(Long id) {
        UserEntity entity = userRepository.findById(id);
        return BeanMapper.map(entity, UserDTO.class);
    }

    @Override
    public UserDTO getByCode(String code) {
        return userRepository.getUserByCode(code);
    }

    @Override
    public List<UserDTO> getList(UserDTO userDTO) {
        return userRepository.getUserListSelective(userDTO);
    }

    @Override
    public List<Map> getList(ModelMapDTO modelMapDTO) {
        return userRepository.getUserListSelective(modelMapDTO);
    }

    @Override
    public Page<Map> getPage(ModelMapDTO modelMapDTO, int offset, int limit) {
        return userRepository.getUserPageSelective(PageRequest.newInstance(modelMapDTO, offset, limit));
    }

    private UserEntity map(UserDTO userDTO) {
        UserEntity entity = BeanMapper.map(userDTO, UserEntity.class);
        entity.setStatus(UserStatus.AVAILABLE.getValue());
        String salt = SecurityHelper.generateRandomNumber();
        entity.setSalt(salt);
        entity.setPass(SecurityHelper.generateMd5Hash(DEFAULT_PASS, salt));
        return entity;
    }

}
