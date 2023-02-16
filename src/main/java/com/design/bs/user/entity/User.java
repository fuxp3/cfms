package com.design.bs.user.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 用户
 **/
@Data
@ToString
@Table(name = "t_user")
public class User implements Serializable {
    private static final long serialVersionUID = 7433329159070139047L;

    @Id
    private Long id;

    private String usercode;

    private String password;

    private String name;

    private String salt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "create_user")
    private Long createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Column(name = "update_user")
    private Long updateUser;

    @Column(name = "paas_flag")
    private String paasFlag;

    private String avatar;

    private Long phone;
    
    private String email;

    private Integer sex;

    private String description;

    private Integer status;

    @Column(name = "user_type")
    private Integer userType;

    @Transient
    private String tenantName;

    @Transient
    private String tenantAdmin;
    
    @Transient
    private String oldpwd;
    
    @Transient
    private List<String> roleNames;

    //逗号分割所有角色名称
    @Transient
    private String roles;

    @Transient
    private String departmentName;

    @Transient
    private float amount;

    private Long department;

    private String statistic;
}
