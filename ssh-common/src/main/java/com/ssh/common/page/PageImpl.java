package com.ssh.common.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Basic {@code Page} implementation.
 *
 * @param <T> the type of which the page consists.
 * @author Gavin
 */
public class PageImpl<T> implements Page<T> {

    /**
     * 传递的参数
     * pageNumber: 第几页(默认从0开始)
     * pageSize: 每页显示的记录数
     */
    private final int pageNumber;
    private final int pageSize;

    /**
     * 查询数据库
     * content: 当前页的数据列表
     * total: 总记录数
     */
    private final List<T> records = new ArrayList<>();
    private final int totalRecords;

    /**
     * 计算总页数
     * totalPages: 总页数
     */
    private final int totalPages;

    /**
     * {@code PageImpl}.
     *
     * @param pageNumber   the number of the current {@link Page}.
     * @param pageSize     the size of the {@link Page}.
     * @param records      the content of this page.
     * @param totalRecords the total amount of elements available.
     */
    public PageImpl(int pageNumber, int pageSize, List<T> records, int totalRecords) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.records.addAll(records);
        this.totalRecords = totalRecords;
        this.totalPages = (totalRecords + pageSize - 1) / pageSize;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public List<T> getRecords() {
        return Collections.unmodifiableList(records);
    }

    @Override
    public int getTotalRecords() {
        return totalRecords;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public int getOffset() {
        return pageNumber * pageSize;
    }

    @Override
    public int getNumberOfRecords() {
        return records.size();
    }

    @Override
    public boolean hasContent() {
        return !records.isEmpty();
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return getPageNumber() > 0;
    }

    @Override
    public boolean hasNext() {
        return getPageNumber() + 1 < getTotalPages();
    }

    @Override
    public Iterator<T> iterator() {
        return records.iterator();
    }

    public static <T> Page<T> newInstance(int pageNumber, int pageSize, List<T> records, int totalRecords) {
        return new PageImpl<>(pageNumber, pageSize, records, totalRecords);
    }

}
