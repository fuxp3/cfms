package com.design.bs.menu.mapper;


import com.design.bs.core.basemapper.MyMapper;
import com.design.bs.menu.entity.Menu;

import java.util.List;

/**
 * @description: 菜单mapper
 **/
public interface MenuMapper extends MyMapper<Menu> {

    List<Menu> getMenuButton(String usercode);

    List<Menu> findUserMenus(String usercode);

    List<Menu> findUserButtons(String usercode);

    void changeTopNode(List<Long> ids);
}
