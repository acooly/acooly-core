package com.acooly.core.common.web.support;

import java.util.ArrayList;
import java.util.List;

public class JsonListResult<T> extends JsonResult {

	private Long total = 0L;
	private List<T> rows = new ArrayList<T>();

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

}
