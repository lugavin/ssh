package com.ssh.common.page;

import com.ssh.common.dto.DTO;

public class PageRequest implements Pageable {

    private final DTO param;
    private final int offset;
    private final int limit;

    public PageRequest(DTO param, int offset, int limit) {
        this.param = param;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public DTO getParam() {
        return param;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    public static Pageable newInstance(DTO param, int offset, int limit) {
        return new PageRequest(param, offset, limit);
    }

}
