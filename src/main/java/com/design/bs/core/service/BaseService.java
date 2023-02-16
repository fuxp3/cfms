package com.design.bs.core.service;

import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 服务基础类
 **/
public interface BaseService<T> {

	/**
	 * 查询所有 T 对象
	 * 
	 * @return
	 */
	List<T> findAll();

	/**
	 * 根据主键查询一个 T 对象
	 * 
	 * @param id
	 * @return
	 */
	T get(Serializable id);

	/**
	 * 保存一个 T 对象
	 * 
	 * @param entity
	 */
	void save(T entity);

	/**
	 * 批量保存 T 对象
	 * 
	 * @param entitys
	 */
	void insertList(List<T> entitys);

	/**
	 * 根据主键删除一个 T 对象
	 * 
	 * @param id
	 */
	void delete(Serializable id);

	/**
	 * 批量删除 T 对象
	 * 
	 * @param ids
	 * @param property
	 * @param clazz
	 */
	void batchDelete(List<Long> ids, String property, Class<T> clazz);

	/**
	 * 批量更新 T 对象<br/>
	 * <font color="red">ids id集合</font><br/>
	 * <font color="red">property sql根据那个属性in 一般都是id</font><br/>
	 * <font color="red">clazz 实体类class</font><br/>
	 * <font color="red">object 批量更新层那个实体类的值</font><br/>
	 * 
	 * @param ids
	 * @param property
	 * @param clazz
	 */
	void batchUpdate(List<Long> ids, String property, Class<T> clazz, Object object);

	/**
	 * 根据 T 对象中的所有属性 完全以参数 entity 中的属性为准，也就是说更新后数据库中表的每个column值与entity属性一致
	 * 
	 * @param entity
	 */
	void update(T entity);

	/**
	 * 更新 T 对象中不为NULL的属性
	 * 
	 * @param entity
	 */
	void updateNotNull(T entity);

	/**
	 * 根据条件查询
	 * 
	 * @param example
	 * @return
	 */
	List<T> findAllByExample(Example example);

	QueryPageResult<T> findAllByPage(QueryPageRequest<T> pageRequest);

	List<T> find(T t);

	T selectOneByExample(Example example);

	void deleteByExample(Example example);

	int selectCount(T t);

	int selectCount(Example example);
}
