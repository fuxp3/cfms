package com.design.bs.core.utils;

import com.design.bs.core.dto.QueryPageRequest;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QuerySeqRequest<T> extends QueryPageRequest<T> {
	private static final long serialVersionUID = 1L;
	private String orderName;
	private String orderRule;
}