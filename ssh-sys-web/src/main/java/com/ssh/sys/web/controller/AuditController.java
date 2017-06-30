package com.ssh.sys.web.controller;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.web.datatable.DataTableRequest;
import com.ssh.common.web.datatable.DataTableResponse;
import com.ssh.common.web.datatable.DataTableUtility;
import com.ssh.common.web.base.BaseWrapper;
import com.ssh.sys.api.service.AuditService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @RequiresPermissions("audit:query")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public String list() {
        return "audit/list";
    }

    @ResponseBody
    @RequiresPermissions("audit:query")
    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public DataTableResponse<Map> getList(@RequestBody BaseWrapper<ModelMapDTO> wrapper) {
        DataTableRequest dtArgs = wrapper.getDtArgs();
        Page<Map> page = auditService.getPage(wrapper.getDto(), dtArgs.getStart(), dtArgs.getLength());
        return DataTableUtility.buildDataTable(dtArgs, page);
    }

    @ResponseBody
    @RequiresPermissions("audit:query")
    @RequestMapping(value = "/getDetailList", method = {RequestMethod.GET, RequestMethod.POST})
    public List<Map> getDetailList(@RequestParam Long auditLogId) {
        return auditService.getDetailList(auditLogId);
    }

}
