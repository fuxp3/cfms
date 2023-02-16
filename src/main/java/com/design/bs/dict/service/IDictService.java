package com.design.bs.dict.service;


import com.design.bs.core.service.BaseService;
import com.design.bs.dict.entity.Dict;

import java.util.List;

public interface IDictService extends BaseService<Dict> {
	
	/**
	 * 根据字典编码获取字典列表
	 * @param code
	 * @return
	 */
	List<Dict> getDictListByCode(String code);
}
