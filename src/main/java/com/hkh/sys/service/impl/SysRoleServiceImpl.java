package com.hkh.sys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hkh.sys.bean.SysRole;
import com.hkh.sys.dao.SysPermissionMapper;
import com.hkh.sys.dao.SysRoleMapper;
import com.hkh.sys.service.SysRoleService;
import com.hkh.sys.vo.RoleVo;
import com.hkh.util.ResultObj;
import com.hkh.util.TreeNode;

@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	SysRoleMapper sysRoleMapper;

	@Autowired
	SysPermissionMapper sysPermissionMapper;

	@Override
	public ResultObj queryAllRole(RoleVo roleVo) {
		Page<Object> startPage = PageHelper.startPage(roleVo.getPage(), roleVo.getLimit());
		List<SysRole> list = sysRoleMapper.queryAllRoles(roleVo);
		// layui表格返回code默认要为0
		return new ResultObj(0, "查询角色成功", startPage.getTotal(), list);
	}

	@Override
	public ResultObj addRole(RoleVo roleVo) {
		ResultObj obj = null;
		roleVo.setCreatetime(new Date());
		int count = sysRoleMapper.insert(roleVo);
		if (count >= 0) {
			obj = new ResultObj(0, "新增角色成功");
		} else {
			obj = new ResultObj(1, "新增角色失败");
		}
		return obj;
	}

	@Override
	public ResultObj updateRole(RoleVo roleVo) {
		ResultObj obj = null;
		int count = sysRoleMapper.update(roleVo);
		if (count >= 0) {
			obj = new ResultObj(0, "修改角色成功");
		} else {
			obj = new ResultObj(1, "修改角色失败");
		}
		return obj;
	}

	@Override
	public ResultObj deleteRole(RoleVo roleVo) {
		ResultObj obj = null;
		Integer roleId = roleVo.getId();
		int count = sysRoleMapper.deleteById(roleId);
		sysPermissionMapper.deletRolePermissionByRole(roleId);
		if (count >= 0) {
			obj = new ResultObj(0, "删除角色成功");
		} else {
			obj = new ResultObj(1, "删除角色失败");
		}
		return obj;
	}

	// 获取角色对应的权限
	@Override
	public ResultObj getPermissionByRoleid(RoleVo roleVo) {
		// 1:先查询数据库中所有的权限
		List<TreeNode> list1 = sysPermissionMapper.queryPermission();
		// 2:通过角色id查询有的权限
		List<TreeNode> list2 = sysPermissionMapper.getPermissionByRoleid(roleVo.getId());
		for (TreeNode treeNode1 : list1) {
			treeNode1.setCheckArr("0");
			for (TreeNode treeNode2 : list2) {
				if (treeNode1.getId() == treeNode2.getId()) {
					treeNode1.setCheckArr("1");
					break;
				}
			}
		}
		return new ResultObj(0, "获取角色权限成功", list1);
	}

	// 给角色分配权限
	@Override
	public ResultObj setPermissionByRoleid(Integer rid, List<HashMap<String, String>> list) {
		// 1:删除原先的角色的权限
		sysPermissionMapper.deletRolePermissionByRole(rid);
		// 2:增加权限
		for (HashMap<String, String> map : list) {
			Integer pid = Integer.valueOf(map.get("nodeId"));
			sysPermissionMapper.insertRolePermission(rid, pid);
		}
		return new ResultObj(0, "修改角色权限成功");
	}

}
