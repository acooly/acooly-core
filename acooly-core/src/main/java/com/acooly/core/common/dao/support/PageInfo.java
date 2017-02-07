package com.acooly.core.common.dao.support;

import com.acooly.core.utils.mapper.BeanCopier;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 * 
 * @author zhangpu
 * 
 */
public class PageInfo<T> implements Serializable {
	/** UID */
	private static final long serialVersionUID = -168321987075635774L;
	
	/** 查询分页结果 */
	private List<T> pageResults;
	
	/** 当页记录数 默认为10条记录 */
	private int countOfCurrentPage = 10;
	
	/** 当前页号 */
	private int currentPage;
	
	/** 总共记录数 */
	private long totalCount;
	
	/** 总共页数 */
	private long totalPage;
	
	/** 构造函数 */
	public PageInfo() {
		this.currentPage = 1;
		this.countOfCurrentPage = 10;
		this.totalCount = 0;
		this.totalPage = 0;
	}
	
	public PageInfo(int countOfCurrentPage, int currentPage) {
		super();
		this.countOfCurrentPage = countOfCurrentPage;
		this.currentPage = currentPage;
	}
	
	/** 是否有下页 */
	public boolean hasNext() {
		return currentPage != totalPage;
	}
	
	public boolean isNext() {
		return hasNext();
	}
	
	/** 是否有上页 */
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
	
	/** 装配pageResults */
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
		return String.format("{pageResults:%s, countOfCurrentPage:%s, totalCount:%s}", pageResults, countOfCurrentPage,
			totalCount);
	}
	
	public <E> PageInfo<E> to(Class<E> clazz) {
		PageInfo<E> info = new PageInfo<>();
		info.setTotalPage(this.totalPage);
		info.setTotalCount(this.totalCount);
		info.setCurrentPage(this.currentPage);
		info.setCountOfCurrentPage(this.countOfCurrentPage);
		if (pageResults != null && !pageResults.isEmpty()) {
			List<E> list = Lists.newArrayListWithCapacity(this.pageResults.size());
			for (T pageResult : pageResults) {
				list.add(BeanCopier.copy(pageResult, clazz));
			}
			info.setPageResults(list);
		}
		return info;
	}
	
}
