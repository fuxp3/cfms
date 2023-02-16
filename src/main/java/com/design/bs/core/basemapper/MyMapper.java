package com.design.bs.core.basemapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @description: 通用mapper
 **/
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
