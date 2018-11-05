package com.ssh.common.web.base;

import com.opensymphony.xwork2.ActionSupport;
import com.ssh.common.util.JsonUtils;
import com.ssh.common.web.data.ResultBean;
import com.ssh.common.web.datatable.DataTableRequest;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAction extends ActionSupport implements ResultType {

    private static final long serialVersionUID = 20160528L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAction.class);

    protected Map<String, String> params = new HashMap<>();

    protected DataTableRequest dtArgs = new DataTableRequest();

    protected ResultBean resultBean = new ResultBean();

    protected ResultBean setCode(String code) {
        return resultBean.setCode(code);
    }

    protected ResultBean setMessage(String message) {
        return resultBean.setMessage(message);
    }

    protected ResultBean setSuccess(boolean success) {
        return resultBean.setSuccess(success);
    }

    protected ResultBean setData(Object data) {
        return resultBean.setData(data);
    }

    protected String writeJSON(Object data) throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(JsonUtils.serialize(data));
        } catch (IOException e) {
            LOGGER.error(e.toString());
            throw e;
        }
        return NONE;
    }

    /*==================== Generate Getter And Setter ====================*/

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public DataTableRequest getDtArgs() {
        return dtArgs;
    }

    public void setDtArgs(DataTableRequest dtArgs) {
        this.dtArgs = dtArgs;
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

    public void setResultBean(ResultBean resultBean) {
        this.resultBean = resultBean;
    }

}
