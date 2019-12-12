package com.hkh.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hkh.sys.bean.SysPermission;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.dao.SysPermissionMapper;
import com.hkh.sys.service.SysPermissionService;
import com.hkh.sys.vo.PermissionVo;
import com.hkh.util.ResultObj;
import com.hkh.util.TreeNode;
import com.hkh.util.WebUtils;

//权限菜单相关
@Service
public class SysPermissionServiceImpl implements SysPermissionService {
	@Autowired
	SysPermissionMapper sysPermissionMapper;

	// 查询所有的菜单集合,后期用用户id来查询
	public ResultObj LoginQueryMenus() {
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		List<SysPermission> listMenu = null;
		if (user.getType() == ResultObj.USER_HEIGHT) {
			// 如果是超级管理员就不用处理菜单
			PermissionVo params = new PermissionVo();
			params.setType(ResultObj.TYPE_MENU);
			listMenu = sysPermissionMapper.queryAllMenus(params);
		} else {
			// 通过userid查询菜单用户对应的菜单
			listMenu = sysPermissionMapper.queryMenuAndPermission(user.getId(), ResultObj.TYPE_MENU);
		}
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		for (SysPermission sysPermission : listMenu) {
			Integer id = sysPermission.getId();
			Integer pid = sysPermission.getPid();
			String title = sysPermission.getTitle();
			String icon = sysPermission.getIcon();
			String href = sysPermission.getHref();
			Boolean spread = (sysPermission.getOpen() == 1 ? true : false);
			treeNodes.add(new TreeNode(id, pid, title, icon, href, spread));
		}
		List<TreeNode> nodeList = TreeNode.convertTreeNodeList(treeNodes, 1);
		ResultObj resultObj = new ResultObj(200, "查询菜单成功", nodeList);
		return resultObj;
	}

	// 查询树形菜单
	@Override
	public ResultObj queryAllMenusTree() {
		PermissionVo menuVo = new PermissionVo();
		menuVo.setType(ResultObj.TYPE_MENU);
		List<SysPermission> list = sysPermissionMapper.queryAllMenus(menuVo);
		return new ResultObj(0, "查询菜单成功", list);
	}

	// 查询所有的菜单
	@Override
	public ResultObj queryAllMenus(PermissionVo menuVo) {
		Page<Object> startPage = PageHelper.startPage(menuVo.getPage(), menuVo.getLimit());
		menuVo.setType(ResultObj.TYPE_MENU);
		List<SysPermission> list = sysPermissionMapper.queryAllMenus(menuVo);
		return new ResultObj(0, "查询菜单成功", startPage.getTotal(), list);
	}

	// 查询最大的排序码
	@Override
	public ResultObj getMenuMaxnOrderNum() {
		Integer num = sysPermissionMapper.getMenuMaxnOrderNum();
		if (num != null) {
			num++;
		} else {
			num = 1;
		}
		return new ResultObj(0, "查询最大排序码成功", num);
	}

	// 增加菜单
	@Override
	public ResultObj addMenu(PermissionVo menuVo) {
		ResultObj obj = null;
		menuVo.setType(ResultObj.TYPE_MENU);
		int count = sysPermissionMapper.insert(menuVo);
		if (count >= 0) {
			obj = new ResultObj(0, "新增菜单成功");
		} else {
			obj = new ResultObj(1, "新增菜单失败");
		}
		return obj;
	}

	// 删除菜单
	@Override
	public ResultObj delMenu(PermissionVo menuVo) {
		ResultObj obj = null;
		menuVo.setType(ResultObj.TYPE_MENU);
		int count = sysPermissionMapper.deletById(menuVo.getId());
		if (count >= 0) {
			obj = new ResultObj(0, "删除菜单成功");
		} else {
			obj = new ResultObj(1, "删除菜单失败");
		}
		return obj;
	}

	@Override
	public ResultObj updateMenu(PermissionVo menuVo) {
		ResultObj obj = null;
		int count = sysPermissionMapper.updateById(menuVo);
		if (count >= 0) {
			obj = new ResultObj(0, "更新菜单成功");
		} else {
			obj = new ResultObj(1, "更新菜单失败");
		}
		return obj;
	}

	@Override
	public ResultObj delPermission(PermissionVo permissionVo) {
		ResultObj obj = null;
		permissionVo.setType(ResultObj.TYPE_PERMISSION);
		int count = sysPermissionMapper.deletById(permissionVo.getId());
		if (count >= 0) {
			obj = new ResultObj(0, "删除权限成功");
		} else {
			obj = new ResultObj(1, "删除权限失败");
		}
		return obj;
	}

	@Override
	public ResultObj addPermission(PermissionVo permissionVo) {
		ResultObj obj = null;
		permissionVo.setType(ResultObj.TYPE_PERMISSION);
		permissionVo.setOpen(0);
		int count = sysPermissionMapper.insert(permissionVo);
		if (count >= 0) {
			obj = new ResultObj(0, "新增菜权限成功");
		} else {
			obj = new ResultObj(1, "新增权限失败");
		}
		return obj;
	}

	@Override
	public ResultObj updatePermission(PermissionVo permissionVo) {
		ResultObj obj = null;
		int count = sysPermissionMapper.updateById(permissionVo);
		if (count >= 0) {
			obj = new ResultObj(0, "更新权限成功");
		} else {
			obj = new ResultObj(1, "更新权限失败");
		}
		return obj;
	}

	@Override
	public ResultObj queryAllPermissionTree() {
		PermissionVo permissionVo = new PermissionVo();
		permissionVo.setType(ResultObj.TYPE_PERMISSION);
		List<SysPermission> list = sysPermissionMapper.queryAllMenus(permissionVo);
		return new ResultObj(0, "查询权限成功", list);
	}

	@Override
	public ResultObj queryAllPermission(PermissionVo permissionVo) {
		Page<Object> startPage = PageHelper.startPage(permissionVo.getPage(), permissionVo.getLimit());
		permissionVo.setType(ResultObj.TYPE_PERMISSION);
		List<SysPermission> list = sysPermissionMapper.queryAllMenus(permissionVo);
		return new ResultObj(0, "查询权限成功", startPage.getTotal(), list);
	}

	@Override
	public ResultObj getPermissionMaxnOrderNum() {
		Integer num = sysPermissionMapper.getPermissionMaxnOrderNum();
		if (num != null) {
			num++;
		} else {
			num = 1;
		}
		return new ResultObj(0, "查询最大排序码成功", num);
	}

}
