package com.hkh.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.service.SysPermissionService;
import com.hkh.sys.vo.PermissionVo;
import com.hkh.util.ResultObj;

@RequestMapping("Menu")
@RestController
public class MenuController {

	@Autowired
	SysPermissionService sysPermissionService;

	// 登录时查询的菜单
	@RequestMapping("LoginQueryMenus")
	public ResultObj LoginQueryMenus() {
		ResultObj resultObj = sysPermissionService.LoginQueryMenus();
		return resultObj;
	}

	// 查询树形菜单集合
	@RequestMapping("queryAllMenusTree")
	public ResultObj queryAllMenusTree() {
		System.err.println("queryAllMenusTree:无参数");
		return sysPermissionService.queryAllMenusTree();
	}

	// 查询菜单集合
	@RequestMapping("queryAllMenus")
	public ResultObj queryAllMenus(PermissionVo menuVo) {
		System.err.println("queryAllMenus:" + menuVo);
		return sysPermissionService.queryAllMenus(menuVo);
	}

	// 查询最大的排序码
	@RequestMapping("getMenuMaxnOrderNum")
	public ResultObj getMenuMaxnOrderNum() {
		return sysPermissionService.getMenuMaxnOrderNum();
	}

	@RequestMapping("addMenu")
	public ResultObj addMenu(PermissionVo menuVo, @RequestParam("newpid") Integer newpid) {
		System.err.println("addMenu:" + menuVo);
		menuVo.setPid(newpid);
		return sysPermissionService.addMenu(menuVo);
	}

	// 删除菜单
	@RequestMapping("deleteMenu")
	public ResultObj deleteMenu(PermissionVo menuVo) {
		System.err.println("deleteMenu:" + menuVo);
		return sysPermissionService.delMenu(menuVo);
	}

	// 修改菜单
	@RequestMapping("updateMenu")
	public ResultObj updateMenu(PermissionVo menuVo) {
		System.err.println("updateMenu:" + menuVo);
		return sysPermissionService.updateMenu(menuVo);
	}

}
