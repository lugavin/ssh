package com.ssh.common.subject;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface SecurityService {

    String BEAN_NAME = "securityService";

    ActiveUser getActiveUser(@NotEmpty String userCode);

    List<Permission> getMenuList(@NotNull Long userId);

    List<Permission> getFunctionList(@NotNull Long userId);

}
