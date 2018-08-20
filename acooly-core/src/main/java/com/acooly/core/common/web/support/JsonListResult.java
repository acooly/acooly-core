package com.acooly.core.common.web.support;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制层分页对象
 *
 * @param <T>
 */
public class JsonListResult<T> extends JsonResult {

    private Long total = 0L;
    private List<T> rows = new ArrayList<T>();
    /**
     * 是否有下一页
     */
    private boolean hasNext;
    /**
     * 当前页号
     */
    private int pageNo;
    /**
     * 页大小
     */
    private int pageSize;

    public JsonListResult() {
        super();
    }

    public JsonListResult(Long total, List<T> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
