package com.design.bs.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 分页结果
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryPageResult<T> implements Serializable {
    private static final long serialVersionUID = 4430715925064311320L;

    private long total;

    private int pageNum;

    private int pageSize;

    private List<T> rows;

    public QueryPageResult(long total,List<T> rows){
        this.total = total;
        this.rows = rows;
    }
}
