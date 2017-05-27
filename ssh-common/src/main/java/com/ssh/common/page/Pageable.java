package com.ssh.common.page;

import com.ssh.common.dto.DTO;

public interface Pageable {

    DTO getParam();

    int getOffset();

    int getLimit();

}
