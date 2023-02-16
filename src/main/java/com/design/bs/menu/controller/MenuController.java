package com.design.bs.menu.controller;

import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.dto.Tree;
import com.design.bs.core.utils.PageUtils;
import com.design.bs.menu.entity.Menu;
import com.design.bs.menu.service.IMenuService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @PostMapping("/list")
    @RequiresPermissions("menu:list")
    public QueryPageResult queryList(@RequestBody QueryPageRequest<Menu> queryPageRequest) {
        return PageUtils.queryPage(queryPageRequest, () -> menuService.queryList(queryPageRequest.getData()));
    }

    @GetMapping("/urlList")
    @RequiresPermissions("menu:list")
    public List<Menu> getAllUrl() {
        return menuService.queryList(new Menu());
    }

    @RequestMapping("/menuButtonTree")
    public List<Tree<Menu>> getMenuButtonTree() {
        return menuService.getMenuButtonTree();
    }

    @GetMapping("/menuTree")
    public List<Tree<Menu>> getMenuTree() {
        return menuService.getMenuTree();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions(value= {"menu:detail","menu:update"},logical= Logical.OR)
    public Menu findById(@PathVariable Long id) {

        return menuService.findById(id);

    }

    @PostMapping("/add")
    @RequiresPermissions("menu:add")
    //@Log("新增菜单")
    public Boolean add(@RequestBody Menu menu) {
        menuService.add(menu);
        return true;
    }

    @PostMapping("/checkCode")
    public Boolean checkName(@RequestBody Map<String,String> map) {
        if(map.get("id")==null) {
            return menuService.checkCode(map.get("code"), null);
        }else {
            return menuService.checkCode(map.get("code"), Long.valueOf(map.get("id")));
        }
    }

    @PostMapping("/update")
    @RequiresPermissions("menu:update")
    //@Log("修改菜单")
    public Boolean update(@RequestBody Menu menu) {
        menuService.update(menu);
        return true;
    }

    @PostMapping("/delete")
    @RequiresPermissions("menu:delete")
    //@Log("删除菜单")
    public Boolean delete(@RequestBody List<Long> ids) {
        menuService.delete(ids);
        return true;
    }
}
