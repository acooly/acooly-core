package com.acooly.core.common.dao.support;

import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.mapper.BeanCopier;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 分页对象
 *
 * @author zhangpu
 */
public class PageInfo<T> implements Serializable {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NO = 1;
    /**
     * UID
     */
    private static final long serialVersionUID = -168321987075635774L;

    /**
     * 查询分页结果
     */
    private List<T> pageResults;

    /**
     * 当页记录数 默认为10条记录
     */
    private int countOfCurrentPage;

    /**
     * 当前页号
     */
    private int currentPage;

    /**
     * 总共记录数
     */
    private long totalCount;

    /**
     * 总共页数
     */
    private long totalPage;

    /**
     * 构造函数
     */
    public PageInfo() {
        this.currentPage = DEFAULT_PAGE_NO;
        this.countOfCurrentPage = DEFAULT_PAGE_SIZE;
        this.totalCount = 0;
        this.totalPage = 0;
    }

    public PageInfo(int countOfCurrentPage, int currentPage) {
        super();
        this.countOfCurrentPage = countOfCurrentPage;
        this.currentPage = currentPage;
    }

    /**
     * 是否有下页
     */
    public boolean hasNext() {
        return currentPage < totalPage;
    }

    public boolean isNext() {
        return hasNext();
    }

    /**
     * 是否有上页
     */
    public boolean hasPrevious() {
        return currentPage != 1;
    }

    public boolean isPrevious() {
        return hasPrevious();
    }

    public int getCountOfCurrentPage() {
        return countOfCurrentPage;
    }

    public void setCountOfCurrentPage(int countOfCurrentPage) {
        this.countOfCurrentPage = countOfCurrentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getPageResults() {
        return pageResults;
    }

    public void setPageResults(List<T> pageResults) {
        this.pageResults = pageResults;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalPage() {
        return ((totalCount + countOfCurrentPage) - 1) / countOfCurrentPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 装配pageResults
     */
    public void calPageResults(List<T> results) {
        int iOffset;
        if (results == null) {
            throw new IllegalArgumentException("null argument!");
        }
        int iDatasSize = results.size();
        if (iDatasSize >= countOfCurrentPage * currentPage) {
            iOffset = countOfCurrentPage;
        } else {
            iOffset = iDatasSize - countOfCurrentPage * (currentPage - 1);
        }
        int iStart = countOfCurrentPage * (currentPage - 1);

        pageResults = results.subList(iStart, iStart + iOffset);
    }

    @Override
    public String toString() {
        return String.format(
                "{pageResults:%s, countOfCurrentPage:%s, totalCount:%s}",
                pageResults, countOfCurrentPage, totalCount);
    }

    /**
     * 转换PageInfo<T>为PageInfo<E>
     * <p>
     * （完整实现）
     * 一般使用场景为：从数据库查询出entity的PageInfo<T>转换为facade或接口层的DTO类型PageInfo<E>
     * <li>内部自动BeanCopies</li>
     * <li>可后置处理已经完成Bean Copy的DTO</li>
     *
     * @param clazz      DTO类型
     * @param biFunction 转换后置处理函数
     * @param <E>        DTO泛型
     * @return DTO分页对象
     */
    @SuppressWarnings("unchecked")
    public <E> PageInfo<E> to(final Class<E> clazz, BiFunction<T, E, E> biFunction) {
        PageInfo<E> info = new PageInfo<>();
        info.setTotalPage(this.totalPage);
        info.setTotalCount(this.totalCount);
        info.setCurrentPage(this.currentPage);
        info.setCountOfCurrentPage(this.countOfCurrentPage);
        if (pageResults != null && !pageResults.isEmpty()) {
            boolean needConvert = !Collections3.getFirst(pageResults).getClass().equals(clazz);
            List<E> list = Lists.newArrayListWithCapacity(this.pageResults.size());
            for (T pageResult : pageResults) {
                E e = null;
                if (needConvert) {
                    e = BeanCopier.copy(pageResult, clazz, BeanCopier.CopyStrategy.IGNORE_NULL);
                } else {
                    e = (E) pageResult;
                }
                if (biFunction != null) {
                    e = biFunction.apply(pageResult, e);
                }
                list.add(e);
            }
            info.setPageResults(list);
        } else {
            info.setPageResults(Lists.<E>newArrayList());
        }
        return info;
    }


    /**
     * 转换PageInfo<T>为PageInfo<E>,默认空后置处理
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> PageInfo<E> to(final Class<E> clazz) {
        return to(clazz, (T t, E e) -> {
            return e;
        });
    }


    /**
     * 自定义人工方式转换PageInfo<T>为PageInfo<E>
     * 代替1.7的同参数重载方法
     *
     * @param function 转换函数
     * @param <E>      DTO泛型
     * @return DTO分页对象
     */
    public <E> PageInfo<E> to(Function<T, E> function) {
        PageInfo<E> info = new PageInfo<>();
        info.setTotalPage(this.totalPage);
        info.setTotalCount(this.totalCount);
        info.setCurrentPage(this.currentPage);
        info.setCountOfCurrentPage(this.countOfCurrentPage);
        if (pageResults != null && !pageResults.isEmpty()) {
            List<E> list = Lists.newArrayListWithCapacity(this.pageResults.size());
            for (T pageResult : pageResults) {
                list.add(function.apply(pageResult));
            }
            info.setPageResults(list);
        } else {
            info.setPageResults(Lists.<E>newArrayList());
        }
        return info;
    }


}
