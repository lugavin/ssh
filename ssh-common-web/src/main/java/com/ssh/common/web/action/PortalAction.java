package com.ssh.common.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.ssh.common.subject.ActiveUser;
import com.ssh.common.util.Constant;
import com.ssh.common.web.base.AbstractAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class PortalAction extends AbstractAction {

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public String welcome() throws Exception {
        return SUCCESS;
    }

    public String getMenus() throws Exception {
        // ActiveUser activeUser = SecurityHelper.getActiveUser();
        ActiveUser activeUser = (ActiveUser) ActionContext.getContext().getSession().get(Constant.USER_SESSION_ATTRIBUTE);
        setData(activeUser.getMenus());
        return JSON;
    }

}
