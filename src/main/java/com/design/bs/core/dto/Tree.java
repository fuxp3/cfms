package com.design.bs.core.dto;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 树形结构
 **/
@Data
@ToString
public class Tree<T> {

    private Long id; //节点ID

    private Long parentId; //父节点ID

    private Boolean hasChildren; //是否有子节点

    private Boolean hasParent; //是否有父节点

    private String text; //节点名称

    private String url; //节点URL

    private String iconCls; //图标

    //指示节点是否被选中
    private boolean checked;

    //给一个节点添加的自定义属性
    private Map<String,Object> attributes;

    private List<Tree<T>> children = new ArrayList<>(); //子节点信息

}
