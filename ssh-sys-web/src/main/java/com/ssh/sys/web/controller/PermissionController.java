package com.ssh.sys.web.controller;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.web.data.ResponseData;
import com.ssh.sys.api.dto.PermissionDTO;
import com.ssh.sys.api.service.PermissionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequiresPermissions("permission:query")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public String list() {
        return "permission/list";
    }

    @ResponseBody
    @RequiresPermissions("permission:query")
    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public List<Map> getList(Map<String, String> params) {
        return permissionService.getList(new ModelMapDTO(params));
    }

    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/add", method = {RequestMethod.GET})
    public String add() {
        return "permission/add";
    }

    @ResponseBody
    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/addSubmit", method = RequestMethod.POST)
    public ResponseData addSubmit(@RequestBody PermissionDTO permissionDTO) {
        permissionService.add(permissionDTO);
        return new ResponseData();
    }

    @RequiresPermissions("permission:update")
    @RequestMapping(value = "/edit", method = {RequestMethod.GET})
    public ModelAndView edit(@RequestParam Long id) {
        // 将数据填充到Request域并返回指定的逻辑视图
        return new ModelAndView("permission/edit", "permission", permissionService.getById(id));
    }

    @ResponseBody
    @RequiresPermissions("permission:update")
    @RequestMapping(value = "/editSubmit", method = {RequestMethod.POST})
    public ResponseData editSubmit(@RequestBody PermissionDTO permissionDTO) {
        permissionService.update(permissionDTO);
        return new ResponseData();
    }

    @ResponseBody
    @RequiresPermissions("permission:delete")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ResponseData delete(@RequestParam Long id) {
        permissionService.delete(id);
        return new ResponseData();
    }

    @ResponseBody
    @RequiresPermissions("permission:delete")
    @RequestMapping(value = "/deleteSubmit", method = {RequestMethod.POST})
    public ResponseData deleteSubmit(@RequestParam Long[] ids) {
        permissionService.delete(ids);
        return new ResponseData();
    }

}
