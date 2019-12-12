package com.hkh.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.service.SysPermissionService;
import com.hkh.sys.vo.PermissionVo;
import com.hkh.util.ResultObj;

@RequestMapping("Permission")
@RestController
public class PermissionController {

	@Autowired
	SysPermissionService sysPermissionService;

	// 查询树形权限集合
	@RequestMapping("queryAllPermissionTree")
	public ResultObj queryAllPermissionTree() {
		System.err.println("queryAllPermissionTree:无参数");
		return sysPermissionService.queryAllPermissionTree();
	}

	// 查询权限集合
	@RequestMapping("queryAllPermission")
	public ResultObj queryAllPermission(PermissionVo permissionVo) {
		System.err.println("queryAllPermission:" + permissionVo);
		return sysPermissionService.queryAllPermission(permissionVo);
	}

	// 增加权限
	@RequestMapping("addPermission")
	public ResultObj addPermission(PermissionVo permissionVo, @RequestParam("newpid") Integer newpid) {
		System.err.println("addPermission:" + permissionVo);
		permissionVo.setPid(newpid);
		return sysPermissionService.addPermission(permissionVo);
	}

	// 删除权限
	@RequestMapping("deletePermission")
	public ResultObj deletePermission(PermissionVo permissionVo) {
		System.err.println("deletePermission:" + permissionVo);
		return sysPermissionService.delPermission(permissionVo);
	}

	// 修改权限
	@RequestMapping("updatePermission")
	public ResultObj updatePermission(PermissionVo permissionVo) {
		System.err.println("updatePermission:" + permissionVo);
		return sysPermissionService.updatePermission(permissionVo);
	}

	// 查询最大的排序码
	@RequestMapping("getPermissionMaxnOrderNum")
	public ResultObj getPermissionMaxnOrderNum() {
		return sysPermissionService.getPermissionMaxnOrderNum();
	}

}
