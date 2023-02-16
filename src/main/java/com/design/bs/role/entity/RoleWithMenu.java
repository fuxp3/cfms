package com.design.bs.role.entity;

import lombok.Data;

import java.util.List;

/**
 * 为什么创建这个类？
 */
@Data
public class RoleWithMenu extends Role {

    private Long menuId;

    private List<Long> menuIds;
}
