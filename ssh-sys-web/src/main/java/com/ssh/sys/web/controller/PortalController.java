package com.ssh.sys.web.controller;

import com.ssh.common.subject.Permission;
import com.ssh.common.util.SecurityHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PortalController {

    @RequestMapping("/portal")
    public String index() {
        return "portal/index";
    }

    @RequestMapping("/portal/dashboard")
    public String dashboard() {
        return "portal/dashboard";
    }

    @ResponseBody
    @RequestMapping("/portal/getMenus")
    public List<Permission> getMenus() {
        return SecurityHelper.getActiveUser().getMenus();
    }

}
