package com.design.bs.role.controller;

import com.design.bs.menu.service.IMenuService;
import com.design.bs.role.entity.Role;
import com.design.bs.role.entity.RoleWithMenu;
import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.dto.Tree;
import com.design.bs.core.utils.PageUtils;
import com.design.bs.core.utils.TreeUtils;
import com.design.bs.core.utils.UserUtils;
import com.design.bs.menu.entity.Menu;
import com.design.bs.role.service.IRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 角色服务
 **/
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @PostMapping("/list")
    //@RequiresPermissions("role:list")
    public QueryPageResult queryList(int page,int rows,String name) {
        QueryPageRequest<Role> queryPageRequest = new QueryPageRequest();
        Role role = new Role();
        role.setName(name);
        queryPageRequest.setPage(page);
        queryPageRequest.setRows(rows);
        queryPageRequest.setData(role);
        return PageUtils.queryPage(queryPageRequest, () -> roleService.queryList(queryPageRequest.getData()));
    }

    @PostMapping("/listAll")
    public Map<String,Object> queryList() {
        Map<String,Object> map = new HashMap();
        Example example = new Example(Role.class);
        example.createCriteria().andNotEqualTo("id",1);
        map.put("rows",roleService.findAllByExample(example));
        return map;
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions(value= {"role:detail","role:update"},logical=Logical.OR)
    public Role detail(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @PostMapping("/add")
    //@RequiresPermissions("role:add")
    //@Log("新增角色绑定菜单")
    public Boolean add(@RequestBody RoleWithMenu role) {
        roleService.add(role);
        return true;
    }

    @PostMapping("/simpleAdd")
    //@RequiresPermissions("role:add")
    public String add(Role role) {
        synchronized (this){
            //校验符合条件后再保存
            if (roleService.checkName(role.getName(), role.getId())) {
                //保存角色信息
                role.setStatus(1);
                role.setCreateTime(new Date());
                role.setCreateUser(UserUtils.getCurrentUser().getId());
                roleService.save(role);
                return "0";
            }else{
                return "角色名称重复";
            }
        }
    }

    @PostMapping("/checkName")
    public Boolean checkName(@RequestBody Map<String,String> map) {
    	if(null==map.get("id")) {
    		return roleService.checkName(map.get("name"), null);
    	}else {
    		return roleService.checkName(map.get("name"), Long.valueOf(map.get("id")));
    	}
    }

    @PostMapping("simpleUpdate")
    //@RequiresPermissions("role:update")
    //@Log("修改角色绑定菜单")
    public String update(@RequestParam("id")Long id, Role role) {
        synchronized (this){
            role.setId(id);
            if (roleService.checkName(role.getName(), id)){
                roleService.updateNotNull(role);
                return "0";
            }else{
                return "角色名称重复";
            }
        }
    }

    @PostMapping("update")
    //@RequiresPermissions("role:update")
    //@Log("修改角色绑定菜单")
    public String update(String menuIds,Long id) {
        synchronized (this){
            RoleWithMenu role = new RoleWithMenu();
            role.setId(id);
            if (StringUtils.isNotBlank(menuIds)){
                String[] split = menuIds.split(",");
                List<Long> idList = Arrays.stream(split)
                        .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                role.setMenuIds(idList);
            }else{
                role.setMenuIds(new ArrayList<>());
            }
            roleService.update(role);
            return "0";
        }
    }

    @PostMapping("/delete")
    //@RequiresPermissions("role:delete")
    //@Log("删除角色绑定菜单")
    public String delete(Long id) {
        synchronized (this){
            roleService.delete(id);
            return "0";
        }
    }
    
    @PostMapping("/roleMenus")
    //@RequiresPermissions("role:list")
    public List<Menu> getRoleMenuAndButtons(@RequestBody List<Long> roleIds){
    	if(!CollectionUtils.isEmpty(roleIds)) {
    		return roleService.getRoleMenuAndButtons(roleIds);
    	}
    	return null;
    }

    @RequestMapping("/menuButtonTree")
    public List<Tree<Menu>> getMenuButtonTree(Long roleId) {
        List<Tree<Menu>> trees = new ArrayList<>();
        List<Menu> menus = menuService.queryList(new Menu());
        List<Menu> roleMenuAndButtons = roleService.getRoleMenuAndButtons(Arrays.asList(roleId));
        List<Long> roleMenuIds = roleMenuAndButtons.stream().map(Menu::getId).collect(Collectors.toList());
        menus.forEach(menu -> {
            Tree<Menu> tree = new Tree<>();
            tree.setId(menu.getId());
            tree.setParentId(menu.getParentId());
            tree.setText(menu.getName());
            tree.setIconCls(menu.getIcon());
            if (roleMenuIds.contains(tree.getId())){
                tree.setChecked(true);
            }
            trees.add(tree);
        });
        return TreeUtils.build(trees);
    }
}

