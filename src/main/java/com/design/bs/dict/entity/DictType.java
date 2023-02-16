package com.design.bs.dict.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Table(name="t_dict_type")
public class DictType {
    /**
     * id;主键
     */
    @Id
    private Long id;

	/** 编码 */
    private String code ;
    /** 类型 */
    private String name ;

    /** 描述 */
    private String description ;

}
