package com.hkh.sys.dao;

import java.util.List;

import com.hkh.sys.bean.SysPermission;
import com.hkh.sys.vo.PermissionVo;
import com.hkh.util.TreeNode;

import io.lettuce.core.dynamic.annotation.Param;

public interface SysPermissionMapper {
	List<SysPermission> queryAllMenus(PermissionVo menuVo);

	Integer getMenuMaxnOrderNum();

	Integer getPermissionMaxnOrderNum();

	int deletById(Integer id);

	int insert(SysPermission record);

	SysPermission getById(Integer id);

	int updateById(SysPermission record);

	// 获取所有的权限
	List<TreeNode> queryPermission();

	// 在SysRoleServiceImpl中使用了通过角色查询权限
	List<TreeNode> getPermissionByRoleid(@Param("roleid") Integer roleid);

	// 删除角色对应的权限
	int deletRolePermissionByRole(@Param("roleid") Integer id);

	// 增加角色的权限
	int insertRolePermission(@Param("rid") Integer rid, @Param("pid") Integer pid);

	// 通过用户的id查询权限的菜单
	List<SysPermission> queryMenuAndPermission(@Param("userid") Integer userid, @Param("type") String type);

}