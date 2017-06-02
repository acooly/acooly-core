package com.acooly.module.mybatis.page;

import java.util.ArrayList;
import java.util.List;

public class MyBatisPage<E> extends ArrayList<E> {

  /** UID */
  private static final long serialVersionUID = -1426723793570627510L;

  private long totalCount;
  private long totalPage;
  private int currentPage;
  private int countOfCurrentPage;

  public MyBatisPage() {
    super();
  }

  public MyBatisPage(List<E> list, long totalCount, long totalPage) {
    super();
    addAll(list);
    this.totalCount = totalCount;
    this.totalPage = totalPage;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  public long getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(long totalPage) {
    this.totalPage = totalPage;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getCountOfCurrentPage() {
    return countOfCurrentPage;
  }

  public void setCountOfCurrentPage(int countOfCurrentPage) {
    this.countOfCurrentPage = countOfCurrentPage;
  }
}
