package com.hkh.sys.service;

import com.hkh.sys.vo.PermissionVo;
import com.hkh.util.ResultObj;

public interface SysPermissionService {
	// 菜单相关
	public ResultObj LoginQueryMenus();

	public ResultObj queryAllMenusTree();

	public ResultObj queryAllMenus(PermissionVo menuVo);

	public ResultObj getMenuMaxnOrderNum();

	public ResultObj delMenu(PermissionVo menuVo);

	public ResultObj addMenu(PermissionVo menuVo);

	public ResultObj updateMenu(PermissionVo menuVo);

	// 权限相关
	public ResultObj queryAllPermissionTree();

	public ResultObj queryAllPermission(PermissionVo permissionVo);

	public ResultObj delPermission(PermissionVo permissionVo);

	public ResultObj addPermission(PermissionVo permissionVo);

	public ResultObj updatePermission(PermissionVo permissionVo);

	public ResultObj getPermissionMaxnOrderNum();

}
