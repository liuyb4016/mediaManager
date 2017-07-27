package com.eshore.yxt.media.core.util;


import java.util.ArrayList;


public class Pagination<T> extends ArrayList<T> {
	private int firstPage = 1;
	private int lastPage = 1;
	private int nextPage = 0;
	private int prePage = 0;
	private int recordCount = 0;
	private int pageCount = 0;
	private int currentPage = 0;
	private int pageSize = 0;
	private int firstPerPage = 0;
	private int totalRecord = 0;

	public Pagination(int currentPage, int pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	public Pagination() {
		new ArrayList();
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPrePage() {
		return prePage;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		initPage(recordCount);
		this.recordCount = recordCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setFirstPerPage(int firstPerPage) {
		this.firstPerPage = firstPerPage;
	}

	public void initPage(int total) {
		recordCount = total;
		if (pageSize != 0 && recordCount != 0) {
			pageCount = (int) Math.ceil((float) recordCount / pageSize);
		} else {
			pageCount = 1;
		}
		lastPage = pageCount;
		if ((currentPage - 1) >= firstPage) {
			prePage = currentPage - 1;
		} else {
			prePage = currentPage;
		}
		if ((currentPage + 1) <= lastPage) {
			nextPage = currentPage + 1;
		} else {
			nextPage = currentPage;
		}
		firstPerPage = (currentPage - 1) * pageSize;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		initPage(this.totalRecord);
	}

	public int getFirstPerPage() {
		return firstPerPage;
	}
}

