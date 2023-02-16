package com.design.bs.core.service.impl;

import com.design.bs.core.basemapper.MyMapper;
import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.service.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 服务基础接口实现
 **/
public class BaseServiceImpl<T> implements BaseService<T> {

	protected static final Logger LOG = LoggerFactory.getLogger(BaseServiceImpl.class);

	@Autowired
	@Getter
	private MyMapper<T> mapper;

	@Override
	public List<T> findAll() {
		return mapper.selectAll();
	}

	@Override
	public T get(Serializable id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public void save(T entity) {
		mapper.insert(entity);
	}

	@Override
	public void insertList(List<T> entitys) {
		mapper.insertList(entitys);
	}

	@Override
	public void delete(Serializable id) {
		mapper.deleteByPrimaryKey(id);
	}

	@Override
	public void batchDelete(List<Long> ids, String property, Class<T> clazz) {
		Example example = new Example(clazz);
		example.createCriteria().andIn(property, ids);
		this.mapper.deleteByExample(example);
	}

	@Override
	public void batchUpdate(List<Long> ids, String property, Class<T> clazz, Object object) {
		Example example = new Example(clazz);
		example.createCriteria().andIn(property, ids);
		this.mapper.updateByExampleSelective((T) object, example);
	}

	@Override
	public void update(T entity) {
		mapper.updateByPrimaryKey(entity);
	}

	/**
	 * updateByPrimaryKeySelective()和updateByPrimaryKey()区别：
	 * 前者会进行字段校验再更新，如果字段值为null就不更新
	 *
	 * @param entity
	 */
	@Override
	public void updateNotNull(T entity) {
		mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<T> findAllByExample(Example example) {
		return mapper.selectByExample(example);
	}

	@Override
	public QueryPageResult<T> findAllByPage(QueryPageRequest<T> pageRequest) {
		PageHelper.clearPage();
		PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows());
		PageInfo<T> pageInfo = new PageInfo<T>(find(pageRequest.getData()));

		return new QueryPageResult<T>(pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public List<T> find(T t) {
		return mapper.select(t);
	}

	@Override
	public T selectOneByExample(Example example) {
		return mapper.selectOneByExample(example);
	}

	@Override
	public void deleteByExample(Example example) {
		mapper.deleteByExample(example);
	}

	@Override
	public int selectCount(T t) {
		return mapper.selectCount(t);
	}

	@Override
	public int selectCount(Example example) {
		return mapper.selectCountByExample(example);
	}

}
