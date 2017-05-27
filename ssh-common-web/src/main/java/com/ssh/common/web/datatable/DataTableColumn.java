package com.ssh.common.web.datatable;

import java.io.Serializable;

public class DataTableColumn implements Serializable {

    private String data;
    private String name;
    private Boolean searchable;
    private Boolean orderable;
    private DataTableSearch search;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSearchable() {
        return searchable;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    public Boolean getOrderable() {
        return orderable;
    }

    public void setOrderable(Boolean orderable) {
        this.orderable = orderable;
    }

    public DataTableSearch getSearch() {
        return search;
    }

    public void setSearch(DataTableSearch search) {
        this.search = search;
    }
}
