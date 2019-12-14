package com.hkh.sys.service;

import com.hkh.sys.bean.SysUser;
import com.hkh.sys.vo.UserVo;
import com.hkh.util.ResultObj;

public interface SysUserService {
	// 用户登录
	public SysUser userLogin(String name);

	// 获取所有的用户
	public ResultObj queryAllUser(UserVo userVo);

	// 增加用户
	public ResultObj addUser(UserVo userVo);

	// 修改用户
	public ResultObj updateUser(UserVo userVo);

	// 修改用户
	public ResultObj deleteUser(UserVo userVo);

	// 通过用户id获取角色
	public ResultObj queryRoleByuserid(Integer userid);

	// 给用户增加某个角色
	public ResultObj insertRole(String rid, Integer uid);

	// 给用户移除某个角色
	public ResultObj deleteRoleByUserid(String rid, Integer uid);

	// 注册用户
	public ResultObj registerUser(String rloginname, String rpwd, String rname);

	// 获取用户的个人信息
	public ResultObj getUserInfo();

	// 登录时获取用户头像
	public ResultObj getUserImgPath(String name);

}
