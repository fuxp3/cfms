package com.design.bs.core.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description: 分页参数
 **/
@Data
@ToString
public class QueryPageRequest<T> implements Serializable {

    private static final long serialVersionUID = 8119058965188100799L;
    //当前页
    private int page;

    //每页显示的记录数
    private int rows;
    
    //查询条件
    private T data;
}
