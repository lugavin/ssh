package com.ssh.sys.api.service;

import com.ssh.common.dto.MapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.service.BaseService;
import com.ssh.sys.api.dto.UserDTO;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public interface UserService extends BaseService<UserDTO> {

    String BEAN_NAME = "userService";

    List<UserDTO> getList(@NotNull UserDTO dto);

    List<Map> getList(MapDTO mapDTO);

    Page<Map> getPage(MapDTO mapDTO, int offset, int limit);

    UserDTO getByCode(@NotEmpty String code);

    boolean checkPass(@NotEmpty String pass);

    void changePass(@NotEmpty String oldPass, @NotEmpty String newPass);

    void resetPass(@Size(min = 1) Long[] ids);

    void assignRoles(@NotNull Long id, Long[] roleIds);

}
