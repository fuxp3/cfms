package com.design.bs.dict.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Table(name="t_dict")
public class Dict{
    /**
     * id;主键
     */
    @Id
    private Long id;

	/** 类型(外键) */
    private String typeCode ;

    /** 字典文本 */
    private String name ;

    /** 字典数值 */
    private String value ;

    /** 排序 */
    private Long priority ;

}
