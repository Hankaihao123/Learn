package com.hkh.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.service.SysDeptService;
import com.hkh.sys.vo.DeptVo;
import com.hkh.util.ResultObj;

@RestController
@RequestMapping("Dept")
public class DeptController {

	@Autowired
	SysDeptService sysDeptService;

	// 查询所有部门树形
	@RequestMapping("queryAllDeptbyTree")
	public ResultObj queryAllDeptbyTree(DeptVo deptVo) {
		System.err.println("queryAllDeptbyTree:" + deptVo);
		return sysDeptService.queryAllDeptbyTree(deptVo);
	}

	// 查询所有部门
	@RequestMapping("queryAllDept")
	public ResultObj queryAllDept(DeptVo deptVo) {
		System.err.println("queryAllDept:" + deptVo);
		return sysDeptService.queryAllDept(deptVo);
	}

	// 更新部门开关
	@RequestMapping("updateDeptSwitch")
	public ResultObj updateDeptSwitch(DeptVo deptVo) {
		System.err.println("updateDeptSwitch:" + deptVo);
		return sysDeptService.updateDeptSwitch(deptVo);
	}

	// 删除部门信息
	@RequestMapping("deleteDept")
	public ResultObj deleteDept(DeptVo deptVo) {
		System.err.println("deleteDept:" + deptVo);
		return sysDeptService.deleteDept(deptVo);
	}

	// 查询最大排序码
	@RequestMapping("getDeptMaxnOrderNum")
	public ResultObj getDeptMaxnOrderNum() {
		return sysDeptService.getDeptMaxnOrderNum();
	}

	// 新增部门
	@RequestMapping("addDept")
	public ResultObj addDept(DeptVo deptVo) {
		System.err.println("addDept:" + deptVo);
		return sysDeptService.addDept(deptVo);
	}

	// 修改部门
	@RequestMapping("updateDept")
	public ResultObj updateDept(DeptVo deptVo) {
		System.err.println("updateDept:" + deptVo);
		return sysDeptService.updateDept(deptVo);
	}

}
