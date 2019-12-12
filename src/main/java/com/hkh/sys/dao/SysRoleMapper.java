package com.hkh.sys.dao;

import java.util.List;

import com.hkh.sys.bean.SysRole;
import com.hkh.sys.vo.RoleVo;

public interface SysRoleMapper {
	int deleteById(Integer id);

	int insert(SysRole record);

	SysRole getById(Integer id);

	int update(SysRole record);

	List<SysRole> queryAllRoles(RoleVo roleVo);
}