package com.ssh.common.web.datatable;

import com.ssh.common.page.Page;

public abstract class DataTableUtility {

    public static <T> DataTableResponse<T> buildDataTable(DataTableRequest dataTableRequest, Page<T> page) {
        return new DataTableResponse<T>()
                .setDraw(dataTableRequest.getDraw())
                .setData(page.getRecords())
                .setRecordsTotal(page.getTotalRecords())
                .setRecordsFiltered(page.getTotalRecords());
    }

}
