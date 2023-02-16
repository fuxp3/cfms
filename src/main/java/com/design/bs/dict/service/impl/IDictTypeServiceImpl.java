package com.design.bs.dict.service.impl;

import com.design.bs.core.service.impl.BaseServiceImpl;
import com.design.bs.dict.entity.DictType;
import com.design.bs.dict.mapper.DictTypeMapper;
import com.design.bs.dict.service.IDictTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class IDictTypeServiceImpl extends BaseServiceImpl<DictType> implements IDictTypeService {
	@Autowired
	private DictTypeMapper dictTypeMapper;
	@Override
	public List<DictType> queryList(DictType dictType) {
		return dictTypeMapper.queryList(dictType);
	}
	
	@Override
	public boolean checkCode(String code, Long id) {
		if (StringUtils.isBlank(code))
            return false;
		Example example = new Example(DictType.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andCondition("lower(code)=", code.toLowerCase());
		if (id != null)
            criteria.andNotEqualTo("id", id);
		return CollectionUtils.isEmpty(this.findAllByExample(example));
	}

}
