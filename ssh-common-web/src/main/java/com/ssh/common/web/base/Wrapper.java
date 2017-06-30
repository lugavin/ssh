package com.ssh.common.web.base;

import com.ssh.common.dto.DTO;
import com.ssh.common.web.datatable.DataTableRequest;

public interface Wrapper<T extends DTO> {

    T getDto();

    DataTableRequest getDtArgs();

}
