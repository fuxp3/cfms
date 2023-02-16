package com.design.bs.user.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description: 用户与角色关系表
 **/
@Data
@ToString
@Table(name = "t_user_role")
public class UserRole implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;
}
