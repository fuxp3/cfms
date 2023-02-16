package com.design.bs.dict.service.impl;

import com.design.bs.core.service.impl.BaseServiceImpl;
import com.design.bs.dict.entity.Dict;
import com.design.bs.dict.mapper.DictMapper;
import com.design.bs.dict.service.IDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class IDictServiceImpl extends BaseServiceImpl<Dict> implements IDictService {
	@Autowired
	private DictMapper dictMapper;
	/**
	 * 根据字典编码获取字典列表
	 * @param code
	 * @return
	 */
	@Override
	public List<Dict> getDictListByCode(String code) {
		if(!StringUtils.isEmpty(code)) {
			Example example = new Example(Dict.class);
	        Criteria criteria = example.createCriteria();
	        criteria.andEqualTo("typeCode", code);
	        example.setOrderByClause(" priority asc ");
	        return dictMapper.selectByExample(example);
		}
		return new LinkedList<Dict>();
	}
	@Override
	public void update(Dict entity) {
		dictMapper.updateByPrimaryKeySelective(entity);
	}

}
