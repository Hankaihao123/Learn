package com.hkh.sys.dao;

import java.util.List;

import com.hkh.sys.bean.SysDept;
import com.hkh.sys.vo.DeptVo;

public interface SysDeptMapper {

	// 查询所有的部门信息
	List<SysDept> queryAllDept(DeptVo deptVo);

	// 查询最大的排序码
	Integer getDeptMaxnOrderNum();

	int deleteById(Integer id);

	int insert(SysDept record);

	SysDept getDeptBy(Integer id);

	int updateById(SysDept record);

}