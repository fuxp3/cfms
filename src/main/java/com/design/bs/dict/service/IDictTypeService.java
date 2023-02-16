package com.design.bs.dict.service;

import com.design.bs.core.service.BaseService;
import com.design.bs.dict.entity.DictType;

import java.util.List;

public interface IDictTypeService extends BaseService<DictType> {

	/**
     * 根据查询条件查询字典类型信息
     * @param dictType 封装了查询条件
     * @return
     */
    List<DictType> queryList(DictType dictType);
    
    /**
     * 检查字典编码是否重复
     * 新增字典时id为NULL
     * @param code
     * @param id
     * @return
     */
    boolean checkCode(String code, Long id);
}
