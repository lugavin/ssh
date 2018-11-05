package com.ssh.sys.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.web.base.AbstractAction;
import com.ssh.sys.api.dto.PermissionDTO;
import com.ssh.sys.api.service.PermissionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
@Scope("prototype")
public class PermissionAction extends AbstractAction {

    @Resource(name = "permissionService")
    private PermissionService permissionService;

    private PermissionDTO permissionDTO = new PermissionDTO();

    private Long id;
    private Long parentId;

    @RequiresPermissions("permission:query")
    public String list() throws Exception {
        return SUCCESS;
    }

    @RequiresPermissions("permission:query")
    public String getList() throws Exception {
        setData(permissionService.getList(new ModelMapDTO()));
        return JSON;
    }

    @RequiresPermissions("permission:create")
    public String add() throws Exception {
        return SUCCESS;
    }

    @RequiresPermissions("permission:create")
    public String addSubmit() throws Exception {
        permissionService.add(permissionDTO);
        return JSON;
    }

    @RequiresPermissions("permission:update")
    public String edit() throws Exception {
        ActionContext.getContext().getValueStack().push(permissionService.getById(id));
        return SUCCESS;
    }

    @RequiresPermissions("permission:update")
    public String editSubmit() throws Exception {
        permissionService.update(permissionDTO);
        return JSON;
    }

    @RequiresPermissions("permission:delete")
    public String deleteSubmit() throws Exception {
        permissionService.delete(id);
        return JSON;
    }
    
    /*==================== Generate Getter And Setter ====================*/

    public PermissionDTO getPermissionDTO() {
        return permissionDTO;
    }

    public void setPermissionDTO(PermissionDTO permissionDTO) {
        this.permissionDTO = permissionDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
