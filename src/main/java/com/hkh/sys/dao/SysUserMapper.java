package com.hkh.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hkh.sys.bean.SysRole;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.vo.UserVo;

public interface SysUserMapper {
	// 用户登录
	SysUser userLogin(@Param("username") String name);

	int deleteById(Integer id);

	int insert(SysUser record);

	SysUser getUserById(Integer id);

	int update(SysUser record);

	List<SysUser> queryAllUsers(UserVo userVo);

	// 通过用户id获取角色
	List<SysRole> queryRoleByuserid(Integer userid);

	// 获取所有的角色
	List<SysRole> queryAllRole();

	int insertRole(@Param("rid") Integer rid, @Param("uid") Integer uid);

	int deleteRoleByUserid(@Param("rid") Integer rid, @Param("uid") Integer uid);

}