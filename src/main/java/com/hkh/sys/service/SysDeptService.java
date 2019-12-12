package com.hkh.sys.service;

import com.hkh.sys.vo.DeptVo;
import com.hkh.util.ResultObj;

public interface SysDeptService {

	public ResultObj queryAllDept(DeptVo deptVo);

	public ResultObj queryAllDeptbyTree(DeptVo deptVo);

	public ResultObj updateDept(DeptVo deptVo);

	public ResultObj deleteDept(DeptVo deptVo);

	public ResultObj updateDeptSwitch(DeptVo deptVo);

	public ResultObj getDeptMaxnOrderNum();

	public ResultObj addDept(DeptVo deptVo);

	public ResultObj getDeptById(Integer deptVo);

}
