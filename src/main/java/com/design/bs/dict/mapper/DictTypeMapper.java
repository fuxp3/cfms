package com.design.bs.dict.mapper;

import com.design.bs.core.basemapper.MyMapper;
import com.design.bs.dict.entity.DictType;

import java.util.List;

public interface DictTypeMapper extends MyMapper<DictType> {
	/**
	 * 查询数据字典类型列表
	 * @param dictType
	 * @return
	 */
	List<DictType> queryList(DictType dictType);
}
