package com.design.bs.core.utils;

import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.function.Supplier;

/**
 * @description: 分页查询工具类
 **/
public class PageUtils {

	/**
	 * 分页查询
	 * 
	 * @param queryPageRequest
	 * @param s
	 * @return
	 */
	public static QueryPageResult queryPage(QueryPageRequest queryPageRequest, Supplier<?> s) {
		PageHelper.startPage(queryPageRequest.getPage(), queryPageRequest.getRows());
		PageInfo<?> pageInfo = new PageInfo<>((List<?>) s.get());
		PageHelper.clearPage();

		return new QueryPageResult(pageInfo.getTotal(), pageInfo.getList());
	}

	public static QueryPageResult queryPageByOrder(QuerySeqRequest querySeqRequest, Supplier<?> s) {
		if (querySeqRequest.getOrderName() == null || querySeqRequest.getOrderRule() == null) {
			PageHelper.startPage(querySeqRequest.getPage(), querySeqRequest.getRows());
		} else {
			String orderStr = querySeqRequest.getOrderName() + " " + querySeqRequest.getOrderRule();
			PageHelper.startPage(querySeqRequest.getPage(), querySeqRequest.getRows(), orderStr);

		}
		PageInfo<?> pageInfo = new PageInfo<>((List<?>) s.get());
		return new QueryPageResult(pageInfo.getTotal(), pageInfo.getList());
	}

}
