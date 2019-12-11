package com.hkh.sys.service.impl;

import java.util.List;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hkh.sys.bean.SysDept;
import com.hkh.sys.bean.SysRole;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.dao.SysUserMapper;
import com.hkh.sys.service.SysDeptService;
import com.hkh.sys.service.SysUserService;
import com.hkh.sys.vo.UserVo;
import com.hkh.util.ActivierUser;
import com.hkh.util.ResultObj;
import com.hkh.util.WebUtils;

//用户服务相关
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	SysUserMapper sysUserMapper;

	@Autowired
	SysDeptService sysDeptService;

	// 用户登录
	@Override
	public SysUser userLogin(String name) {
		SysUser userLogin = sysUserMapper.userLogin(name);
		return userLogin;
	}

	// 查询所有的用户
	@Override
	public ResultObj queryAllUser(UserVo userVo) {
		Page<Object> startPage = PageHelper.startPage(userVo.getPage(), userVo.getLimit());
		List<SysUser> list = sysUserMapper.queryAllUsers(userVo);
		for (SysUser sysUser : list) {
			SysDept sysdept = (SysDept) sysDeptService.getDeptById(sysUser.getDeptid()).getData();
			sysUser.setDeptname(sysdept.getTitle());
		}
		// layui表格返回code默认要为0
		return new ResultObj(0, "查询用户成功", startPage.getTotal(), list);
	}

	// 新增用户(未使用)
	@Override
	public ResultObj addUser(UserVo userVo) {
		ResultObj obj = null;
		
		int count = sysUserMapper.insert(userVo);
		if (count >= 0) {
			obj = new ResultObj(0, "新增用户成功");
		} else {
			obj = new ResultObj(1, "新增用户失败");
		}
		return obj;
	}

	// 更新用户
	@Override
	public ResultObj updateUser(UserVo userVo) {
		ResultObj obj = null;
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		userVo.setId(user.getId());
		int count = sysUserMapper.update(userVo);
		if (count >= 0) {
			obj = new ResultObj(0, "修改用户成功");
		} else {
			obj = new ResultObj(1, "修改用户失败");
		}
		return obj;
	}

	@Override
	public ResultObj deleteUser(UserVo userVo) {
		ResultObj obj = null;
		Integer roleId = userVo.getId();
		int count = sysUserMapper.deleteById(roleId);
		if (count >= 0) {
			obj = new ResultObj(0, "删除用户成功");
		} else {
			obj = new ResultObj(1, "删除用户失败");
		}
		return obj;
	}

	// 通过用户id获取角色
	@Override
	public ResultObj queryRoleByuserid(Integer userid) {
		// 1:查询数据库中所有角色
		List<SysRole> list1 = sysUserMapper.queryAllRole();
		// 2:查询用户的角色
		List<SysRole> list2 = sysUserMapper.queryRoleByuserid(userid);
		for (SysRole role1 : list1) {
			role1.setDisabled("");
			role1.setChecked("");
			role1.setTf(false);
			for (SysRole role2 : list2) {
				if (role1.getId() == role2.getId()) {
					role1.setTf(true);
					break;
				}
			}
		}
		return new ResultObj(0, "查询角色成功", list1);
	}

	@Override
	public ResultObj insertRole(String rid, Integer uid) {
		String[] split = rid.split(",");
		for (String string : split) {
			int count = sysUserMapper.insertRole(Integer.valueOf(string), uid);
			if (count < 0) {
				return new ResultObj(0, "用户增加角色失败");
			}
		}
		return new ResultObj(0, "用户增加角色成功");
	}

	@Override
	public ResultObj deleteRoleByUserid(String rid, Integer uid) {
		String[] split = rid.split(",");
		for (String string : split) {
			int count = sysUserMapper.deleteRoleByUserid(Integer.valueOf(string), uid);
			if (count < 0) {
				return new ResultObj(0, "用户移除角色失败");
			}
		}
		return new ResultObj(0, "用户移除角色成功");
	}

	@Override
	public ResultObj registerUser(String rloginname, String rpwd, String rname) {
		SysUser user = sysUserMapper.userLogin(rloginname);
		if (user != null) {
			return new ResultObj(1, "该用户已经存在");
		}
		String hashAlgorithmName = "MD5";
		Object salt = ByteSource.Util.bytes("04A93C74C8294AA09A8B974FD1F4ECBB");
		int hashIterations = 2;
		Object result = new SimpleHash(hashAlgorithmName, rpwd, salt, hashIterations);
		UserVo userVo = new UserVo();
		userVo.setLoginname(rloginname);
		userVo.setName(rname);
		userVo.setPwd(result.toString());
		userVo.setSalt("04A93C74C8294AA09A8B974FD1F4ECBB");
		userVo.setAddress("41-4101-410183");
		int count = sysUserMapper.insert(userVo);
		if (count > 0) {
			return new ResultObj(0, "该用户注册成功");
		} else {
			return new ResultObj(1, "该用户注册失败");
		}
	}

	// 获取用户个人信息
	@Override
	public ResultObj getUserInfo() {
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		Integer userid = user.getId();
		SysUser user2 = sysUserMapper.getUserById(userid);
		if (user2 == null) {
			return new ResultObj(1, "未查找到该用户");
		}
		return new ResultObj(0, "查找用户成功", user2);
	}

}
