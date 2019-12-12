package com.hkh.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.service.SysRoleService;
import com.hkh.sys.vo.RoleVo;
import com.hkh.util.ResultObj;

@RequestMapping("Role")
@RestController
public class RoleController {
	@Autowired
	SysRoleService sysRoleService;

	// 查询所有的角色信息
	@RequestMapping("queryAllRole")
	public ResultObj queryAllRole(RoleVo roleVo) {
		System.err.println("queryAllRole:" + roleVo);
		return sysRoleService.queryAllRole(roleVo);
	}

	// 删除角色信息
	@RequestMapping("deleteRole")
	public ResultObj deleteRole(RoleVo roleVo) {
		System.err.println("deleteRole:" + roleVo);
		return sysRoleService.deleteRole(roleVo);
	}

	// 新增角色
	@RequestMapping("addRole")
	public ResultObj addRole(RoleVo roleVo) {
		System.err.println("addRole:" + roleVo);
		return sysRoleService.addRole(roleVo);
	}

	// 修改角色
	@RequestMapping("updateRole")
	public ResultObj updateRole(RoleVo roleVo) {
		System.err.println("updateRole:" + roleVo);
		return sysRoleService.updateRole(roleVo);
	}

	// 获取角色的权限
	@RequestMapping("getPermissionByRoleid")
	public ResultObj getPermissionByRoleid(RoleVo roleVo) {
		System.err.println("getPermissionByRoleid:" + roleVo);
		return sysRoleService.getPermissionByRoleid(roleVo);
	}

	// 给角色分配权限
	@RequestMapping("setPermissionByRoleid")
	public ResultObj setPermissionByRoleid(@RequestBody Map<String, Object> params) {
		System.err.println("setPermissionByRoleid:" + params);
		// 1:先删除原角色的所有权限2:给角色加上对应的权限
		Integer id = (Integer) params.get("id");
		List<HashMap<String, String>> list = (List<HashMap<String, String>>) params.get("permission");
		return sysRoleService.setPermissionByRoleid(id, list);
	}

}
