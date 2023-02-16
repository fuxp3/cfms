package com.design.bs.role.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description: 角色与菜单关系表
 **/
@Data
@ToString
@Table(name = "t_role_menu")
public class RoleMenu implements Serializable {

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "menu_id")
    private Long menuId;
}
