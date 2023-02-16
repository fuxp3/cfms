package com.design.bs.menu.service.impl;

import com.design.bs.core.dto.Tree;
import com.design.bs.core.service.impl.BaseServiceImpl;
import com.design.bs.core.utils.TreeUtils;
import com.design.bs.menu.entity.Menu;
import com.design.bs.menu.mapper.MenuMapper;
import com.design.bs.menu.service.IMenuService;
import com.design.bs.role.service.IRoleMenuService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 权限服务实现
 **/
@Service
@Transactional
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    public List<Menu> findUserPermissions(String usercode) {
        return menuMapper.findUserButtons(usercode);
    }

    @Override
    public List<Menu> findUserResources(String username) {
        return menuMapper.findUserMenus(username);
    }

    @Override
    public List<Menu> queryList(Menu menu) {
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        menu = (null == menu) ? new Menu() : menu;
        if (StringUtils.isNotBlank(menu.getName()))
            criteria.andEqualTo("name", menu.getName());

        if (StringUtils.isNotBlank(menu.getType()))
            criteria.andEqualTo("type", menu.getType());
        //criteria.andEqualTo("status",TenantConstant.MenuStatus.VALID);
        example.setOrderByClause(" priority asc ");

        List<Menu> menus = this.findAllByExample(example);
        //nameCode赋值
        for (Menu m : menus) {
            m.setNameCode(m.getName() + "(" + m.getCode() + ")");
        }
        return menus;

    }

    @Override
    public List<Tree<Menu>> getMenuButtonTree() {
        List<Tree<Menu>> trees = new ArrayList<>();
        List<Menu> menus = queryList(new Menu());
        buildMenuTree(trees, menus);
        return TreeUtils.build(trees);
    }

    @Override
    public List<Tree<Menu>> getMenuTree() {
        List<Tree<Menu>> trees = new ArrayList<>();
        Menu menu = new Menu();
        menu.setType("menu");
        List<Menu> menus = queryList(menu);
        buildMenuTree(trees, menus);
        return TreeUtils.build(trees);
    }

    @Override
    public Menu findById(Long id) {
        return this.get(id);
    }

    @Override
    public void add(Menu menu) {
        //校验符合条件后再保存
        if (checkCode(menu.getCode(), menu.getId())) {
            menu.setCreateTime(new Date());
            if (menu.getParentId() == null) {
                menu.setParentId(0L);
            }
            if (Menu.TYPE_BUTTON.equals(menu.getType())) {
                //menu.setIcon(null);
                menu.setUrl(null);
            }
            this.save(menu);
        }
    }

    @Override
    public boolean checkCode(String code, Long id) {
        if (StringUtils.isBlank(code))
            return false;

        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andCondition("lower(code)=", code.toLowerCase());
        if (id != null)
            criteria.andNotEqualTo("id", id);

        return CollectionUtils.isEmpty(this.findAllByExample(example));
    }

    @Override
    public void update(Menu menu) {
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (Menu.TYPE_BUTTON.equals(menu.getType())) {
            //menu.setIcon(null);
            menu.setUrl(null);
        }
        this.updateNotNull(menu);
    }

    @Override
    public void delete(List<Long> ids) {
        //删除菜单本身
        this.batchDelete(ids, "id", Menu.class);

        //删除角色菜单关系
        roleMenuService.deleteRoleMenusByMenuId(ids);

        //将本菜单下的子菜单变更为一级菜单
        menuMapper.changeTopNode(ids);
    }

    /**
     * 构建一个菜单树
     *
     * @param trees
     * @param menus
     */
    private void buildMenuTree(List<Tree<Menu>> trees, List<Menu> menus) {
        menus.forEach(menu -> {
            Tree<Menu> tree = new Tree<>();
            tree.setId(menu.getId());
            tree.setParentId(menu.getParentId());
            tree.setText(menu.getName());
            trees.add(tree);
        });
    }
}
