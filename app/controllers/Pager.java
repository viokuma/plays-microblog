package controllers;

import java.util.ArrayList;
import java.util.List;

public class Pager {

	/**
	 * 页码
	 */
	public int page;
	
	/**
	 * 每页显示行数
	 */
	public int rows;
	
	/**
	 * 页数
	 */
	public long total;
	
	/**
	 * 记录总数
	 */
	public long records;
	
	/**
	 * 导航栏显示页码开始位置
	 */
	public long pageStart;
	
	/**
	 * 导航栏显示页码结束位置
	 */
	public long pageEnd;
	
	/**
	 * 导航栏显示当前页码的偏移值,默认前后有5个值
	 */
	public long pageOffset = 5;

	/**
	 * 分页查询到的对象
	 */
	public List models = new ArrayList();

	public void completes() {
		total = (long) Math.ceil((double) records / (double) rows);
		pageStart = page - pageOffset;
		pageEnd = page + pageOffset;
		if (pageStart < 1) {
			pageStart = 1;
			pageEnd = pageStart + pageOffset*2;
			if (pageEnd > total) {
				pageEnd = total;
			}
		} else if (pageEnd > total) {
			pageStart = total - pageOffset*2;
			pageEnd = total;
		}
	}
	
	public static String appendPage(String url, long page) {
		int index = url.indexOf("page=")-1;
		if (index > 0) {
			url = url.substring(0, index); // 删除原来的page参数
		}
		// 增加page参数
		url = url + (url.contains("?") ? "&" : "?") + "page=" + page;
		return url;
	}

	@Override
	public String toString() {
		return "Pager [page=" + page + ", rows=" + rows + ", total=" + total
				+ ", records=" + records + ", pageStart=" + pageStart
				+ ", pageEnd=" + pageEnd + ", pageOffset=" + pageOffset
				+ ", models=" + models + "]";
	}

}
