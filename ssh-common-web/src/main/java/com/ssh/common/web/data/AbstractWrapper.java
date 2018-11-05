package com.ssh.common.web.data;

import com.ssh.common.web.datatable.DataTableRequest;

import java.io.Serializable;

public abstract class AbstractWrapper implements Serializable {

    private static final long serialVersionUID = 20161117L;

    protected DataTableRequest dtArgs;

    public DataTableRequest getDtArgs() {
        return dtArgs;
    }

    public void setDtArgs(DataTableRequest dtArgs) {
        this.dtArgs = dtArgs;
    }

}
