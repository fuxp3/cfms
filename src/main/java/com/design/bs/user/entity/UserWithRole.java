package com.design.bs.user.entity;

import com.design.bs.menu.entity.Menu;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

@Data
public class UserWithRole extends User {

    @Transient
    private Long roleId;

    @Transient
    private List<Long> roleIds;
    
    @Transient
    private List<Menu> menuAndButtons;
}
