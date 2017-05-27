package com.ssh.common.web.datatable;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * https://datatables.net/manual/server-side
 */
public class DataTableResponse<T> implements Serializable {

    private String error;
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private List<T> data = Collections.emptyList();

    public String getError() {
        return error;
    }

    public DataTableResponse<T> setError(String error) {
        this.error = error;
        return this;
    }

    public int getDraw() {
        return draw;
    }

    public DataTableResponse<T> setDraw(int draw) {
        this.draw = draw;
        return this;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public DataTableResponse<T> setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
        return this;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public DataTableResponse<T> setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public DataTableResponse<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

}
