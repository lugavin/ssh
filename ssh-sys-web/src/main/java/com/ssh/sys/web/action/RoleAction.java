package com.ssh.sys.web.action;

import com.ssh.common.web.base.AbstractAction;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class RoleAction extends AbstractAction {

    @RequiresPermissions("role:query")
    public String list() throws Exception {
        return SUCCESS;
    }

}
