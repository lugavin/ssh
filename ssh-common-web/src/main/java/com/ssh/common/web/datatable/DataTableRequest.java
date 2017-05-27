package com.ssh.common.web.datatable;

import java.io.Serializable;
import java.util.List;

/**
 * ======================================================= *
 * draw:1                                                  *
 * start:0                                                 *
 * length:10                                               *
 * order[0][column]:0                                      *
 * order[0][dir]:asc                                       *
 * search[value]:                                          *
 * search[regex]:false                                     *
 * columns[0][data]:username                               *
 * columns[0][name]:username                               *
 * columns[0][searchable]:true                             *
 * columns[0][orderable]:true                              *
 * columns[0][search][value]:                              *
 * columns[0][search][regex]:false                         *
 * ======================================================= *
 * https://datatables.net/manual/server-side
 */
public class DataTableRequest implements Serializable {

    private int draw;
    private int start;
    private int length;
    private DataTableSearch search;
    private List<DataTableOrder> order;
    private List<DataTableColumn> columns;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public DataTableSearch getSearch() {
        return search;
    }

    public void setSearch(DataTableSearch search) {
        this.search = search;
    }

    public List<DataTableOrder> getOrder() {
        return order;
    }

    public void setOrder(List<DataTableOrder> order) {
        this.order = order;
    }

    public List<DataTableColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DataTableColumn> columns) {
        this.columns = columns;
    }
}
