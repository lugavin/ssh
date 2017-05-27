package com.ssh.common.web.wrapper;

import com.ssh.common.dto.DTO;
import com.ssh.common.web.datatable.DataTableRequest;

public class BaseWrapper<T extends DTO> implements Wrapper<T> {

    protected T dto;

    protected DataTableRequest dtArgs;

    @Override
    public T getDto() {
        return dto;
    }

    public void setDto(T dto) {
        this.dto = dto;
    }

    @Override
    public DataTableRequest getDtArgs() {
        return dtArgs;
    }

    public void setDtArgs(DataTableRequest dtArgs) {
        this.dtArgs = dtArgs;
    }

}
