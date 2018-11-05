package com.ssh.sys.web.action;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.web.base.AbstractAction;
import com.ssh.sys.api.service.Select2Service;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
@Scope("prototype")
public class Select2Action extends AbstractAction {

    @Resource(name = "select2Service")
    private Select2Service select2Service;

    private int pageNumber = 1;
    private int pageSize = 10;

    public String queryActor() throws Exception {
        return writeJSON(select2Service.getActorPage(new ModelMapDTO(params), (pageNumber - 1) * pageSize, pageSize));
    }

    public String queryFunc() throws Exception {
        return writeJSON(select2Service.getFuncPage(new ModelMapDTO(params), (pageNumber - 1) * pageSize, pageSize));
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
