package com.design.bs.menu.entity;

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

/**
 * @description: 菜单（按钮）
 **/
@Data
@ToString
@Table(name = "t_menu")
public class Menu implements Serializable {

    public static final String TYPE_MENU = "menu";
    public static final String TYPE_BUTTON = "button";

    @Id
    private Long id;

    private String name;
    
    private String code;

    @Column(name = "parent_id")
    private Long parentId;

    private String url;

    private String perms;

    private String type;

    private String icon;

    private Long priority;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;

    private Integer status;
    
    private String description;
    
    /**
     * 用于区分相同名字的菜单
     */
    @Transient
    private String nameCode;

    public void setName(String name) {
        this.name = name == null ? "" : name.trim();
    }

    public void setUrl(String url) {
        this.url = url == null ? "" : url.trim();
    }

    public void setPerms(String perms) {
        this.perms = perms == null ? "" : perms.trim();
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? "" : icon.trim();
    }
}
