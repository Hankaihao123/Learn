package com.hkh.sys.service;

import java.util.HashMap;
import java.util.List;

import com.hkh.sys.vo.RoleVo;
import com.hkh.util.ResultObj;

public interface SysRoleService {
	// 获取所有的角色
	public ResultObj queryAllRole(RoleVo roleVo);

	// 增加角色
	public ResultObj addRole(RoleVo roleVo);

	// 修改角色
	public ResultObj updateRole(RoleVo roleVo);

	// 修改角色
	public ResultObj deleteRole(RoleVo roleVo);

	// 获取角色对应的权限
	public ResultObj getPermissionByRoleid(RoleVo roleVo);

	// 给角色分配权限
	public ResultObj setPermissionByRoleid(Integer id, List<HashMap<String, String>> list);
}
