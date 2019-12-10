package com.hkh.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.service.SysUserService;
import com.hkh.sys.vo.UserVo;
import com.hkh.util.ResultObj;

@RequestMapping("User")
@RestController
public class UserController {
	@Autowired
	SysUserService sysUserService;

	// 查询所有的用户信息
	@RequestMapping("queryAllUser")
	public ResultObj queryAllUser(UserVo userVo) {
		System.err.println("queryAllUser:" + userVo);
		return sysUserService.queryAllUser(userVo);
	}

	// 删除用户信息
	@RequestMapping("deleteUser")
	public ResultObj deleteUser(UserVo userVo) {
		System.err.println("deleteUser:" + userVo);
		return sysUserService.deleteUser(userVo);
	}

	// 新增用户
	@RequestMapping("addUser")
	public ResultObj addUser(UserVo userVo) {
		System.err.println("addUser:" + userVo);
		return sysUserService.addUser(userVo);
	}

	// 修改用户
	@RequestMapping("updateUser")
	public ResultObj updateUser(UserVo userVo) {
		System.err.println("updateUser:" + userVo);
		return sysUserService.updateUser(userVo);
	}

	// 通过用户id获取角色
	// 1:先获取所有的角色,2:根据用户id查询对应的角色
	@RequestMapping("queryRoleByuserid")
	public ResultObj queryRoleByuserid(Integer userid) {
		return sysUserService.queryRoleByuserid(userid);
	}

	// 给用户增加角色
	@RequestMapping("insertRole")
	public ResultObj insertRole(String roleid, Integer userid) {
		return sysUserService.insertRole(roleid, userid);
	}

	// 给用户移除角色
	@RequestMapping("deleteRoleByUserid")
	public ResultObj deleteRoleByUserid(String roleid, Integer userid) {
		return sysUserService.deleteRoleByUserid(roleid, userid);
	}

}
