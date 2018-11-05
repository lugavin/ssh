package com.ssh.sys.web.action;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.web.base.AbstractAction;
import com.ssh.common.web.datatable.DataTableUtility;
import com.ssh.sys.api.service.AuditService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@Scope("prototype")
public class AuditAction extends AbstractAction {

    @Resource(name = "auditService")
    private AuditService auditService;

    private Long auditLogId;

    @RequiresPermissions("audit:query")
    public String list() throws Exception {
        return SUCCESS;
    }

    @RequiresPermissions("audit:query")
    public String getList() throws Exception {
        Page<Map> page = auditService.getPage(new ModelMapDTO(params), dtArgs.getStart(), dtArgs.getLength());
        setData(DataTableUtility.buildDataTable(dtArgs, page));
        return JSON;
    }

    @RequiresPermissions("audit:query")
    public String detail() throws Exception {
        return SUCCESS;
    }

    @RequiresPermissions("audit:query")
    public String getDetail() throws Exception {
        setData(auditService.getDetailList(auditLogId));
        return JSON;
    }

    /*==================== Generate Getter And Setter ====================*/

    public Long getAuditLogId() {
        return auditLogId;
    }

    public void setAuditLogId(Long auditLogId) {
        this.auditLogId = auditLogId;
    }

}
