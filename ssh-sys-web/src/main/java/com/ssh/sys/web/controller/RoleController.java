package com.ssh.sys.web.controller;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.web.data.ResponseData;
import com.ssh.sys.api.dto.RoleDTO;
import com.ssh.sys.api.service.PermissionService;
import com.ssh.sys.api.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @RequiresPermissions("role:query")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public String list() {
        return "role/list";
    }

    @ResponseBody
    @RequiresPermissions("role:query")
    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public List<Map> getList(@RequestParam Map<String, String> params) {
        return roleService.getList(new ModelMapDTO(params));
    }

    @ResponseBody
    @RequiresPermissions("role:create")
    @RequestMapping(value = "/addSubmit", method = RequestMethod.POST)
    public ResponseData addSubmit(RoleDTO roleDTO) {
        roleService.add(roleDTO);
        return new ResponseData();
    }

    @ResponseBody
    @RequiresPermissions("role:update")
    @RequestMapping(value = "/editSubmit", method = {RequestMethod.POST})
    public ResponseData editSubmit(RoleDTO roleDTO) {
        roleService.update(roleDTO);
        return new ResponseData();
    }

    @ResponseBody
    @RequiresPermissions("role:delete")
    @RequestMapping(value = "/deleteSubmit", method = {RequestMethod.POST})
    public ResponseData deleteSubmit(@RequestParam Long[] ids) {
        roleService.delete(ids);
        return new ResponseData();
    }

    @ResponseBody
    @RequestMapping(value = "/getListByUserId", method = {RequestMethod.GET, RequestMethod.POST})
    public List<Map> getListByUserId(@RequestParam Long userId) {
        return roleService.getListByUserId(userId);
    }

    @ResponseBody
    @RequestMapping(value = "/getPermissionList", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelMap getPermissionList(@RequestParam Long id) {
        ModelMap modelMap = new ModelMap();
        modelMap.put("permissionIds", roleService.getPermissionIds(id));
        modelMap.put("permissionList", permissionService.getList(new ModelMapDTO()));
        return modelMap;
    }

    @ResponseBody
    @RequestMapping(value = "/authSubmit", method = RequestMethod.POST)
    public ResponseData authSubmit(@RequestParam Long id, @RequestParam Long[] permissionIds) {
        roleService.assignPermissions(id, permissionIds);
        return new ResponseData();
    }

}
