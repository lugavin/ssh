package com.ssh.sys.web.action;

import com.opensymphony.xwork2.ActionContext;
import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.web.base.AbstractAction;
import com.ssh.common.web.datatable.DataTableUtility;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.api.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@Scope("prototype")
public class UserAction extends AbstractAction {

    @Resource(name = "userService")
    private UserService userService;

    private UserDTO userDTO = new UserDTO();

    private Long id;
    private Long[] ids;

    @RequiresPermissions("user:query")
    public String list() throws Exception {
        return SUCCESS;
    }

    @RequiresPermissions("user:query")
    public String getList() throws Exception {
        Page<Map> page = userService.getPage(new ModelMapDTO(params), dtArgs.getStart(), dtArgs.getLength());
        setData(DataTableUtility.buildDataTable(dtArgs, page));
        return JSON;
    }

    @RequiresPermissions("user:create")
    public String add() throws Exception {
        return SUCCESS;
    }

    @RequiresPermissions("user:create")
    public String addSubmit() throws Exception {
        userService.add(userDTO);
        return JSON;
    }

    @RequiresPermissions("user:update")
    public String edit() throws Exception {
        ActionContext.getContext().getValueStack().push(userService.getById(id));
        return SUCCESS;
    }

    @RequiresPermissions("user:update")
    public String editSubmit() throws Exception {
        userService.update(userDTO);
        return JSON;
    }

    @RequiresPermissions("user:delete")
    public String deleteSubmit() throws Exception {
        userService.delete(ids);
        return JSON;
    }

    /*==================== Generate Getter And Setter ====================*/

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

}
