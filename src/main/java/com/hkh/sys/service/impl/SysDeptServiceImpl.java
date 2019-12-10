package com.hkh.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hkh.sys.bean.SysDept;
import com.hkh.sys.dao.SysDeptMapper;
import com.hkh.sys.service.SysDeptService;
import com.hkh.sys.vo.DeptVo;
import com.hkh.util.ResultObj;
import com.hkh.util.TreeNode;

@Service
public class SysDeptServiceImpl implements SysDeptService {

	@Autowired
	SysDeptMapper sysDeptMapper;

	// 查询所有部门信息树形
	@Override
	public ResultObj queryAllDeptbyTree(DeptVo deptVo) {
		List<SysDept> list = sysDeptMapper.queryAllDept(deptVo);
		List<TreeNode> temp = new ArrayList<TreeNode>();
		for (SysDept sysDept : list) {
			Integer id = sysDept.getId();
			Integer pid = sysDept.getPid();
			String title = sysDept.getTitle();
			Boolean spread = (sysDept.getOpen() == 1 ? true : false);
			TreeNode treeNode = new com.hkh.util.TreeNode(id, pid, title, spread);
			temp.add(treeNode);
		}
		return new ResultObj(0, "查询树形部门成功", temp);
	}

	// 查询所有部门信息
	@Override
	public ResultObj queryAllDept(DeptVo deptVo) {
		Page<Object> startPage = PageHelper.startPage(deptVo.getPage(), deptVo.getLimit());
		List<SysDept> list = sysDeptMapper.queryAllDept(deptVo);
		return new ResultObj(0, "查询部门成功", startPage.getTotal(), list);
	}

	// 更新部门展开和显示开关
	@Override
	public ResultObj updateDeptSwitch(DeptVo deptVo) {
		ResultObj obj = null;
		SysDept record = new SysDept();
		String msg = null;
		record.setId(deptVo.getId());
		if (deptVo.getOpen() == null) {
			// 如果展开是null说明可以是修改显示
			record.setAvailable(deptVo.getAvailable());
			msg = (deptVo.getAvailable() == 1 ? "显示成功开启" : "显示成功关闭");
		} else if (deptVo.getAvailable() == null) {
			// 如果显示是null说明可以是修改展开
			record.setOpen(deptVo.getOpen());
			msg = (deptVo.getOpen() == 1 ? "展开成功开启" : "展开成功关闭");
		}
		int count = sysDeptMapper.updateById(record);
		if (count >= 0) {
			obj = new ResultObj(0, msg);
		} else {
			obj = new ResultObj(1, "修改开关失败");
		}
		return obj;
	}

	// 删除部门信息
	@Override
	public ResultObj deleteDept(DeptVo deptVo) {
		ResultObj obj = null;
		int count = sysDeptMapper.deleteById(deptVo.getId());
		if (count >= 0) {
			obj = new ResultObj(0, "删除部门成功");
		} else {
			obj = new ResultObj(1, "删除部门失败");
		}
		return obj;
	}

	// 更新部门
	@Override
	public ResultObj updateDept(DeptVo deptVo) {
		ResultObj obj = null;
		int count = sysDeptMapper.updateById(deptVo);
		if (count >= 0) {
			obj = new ResultObj(0, "更新部门成功");
		} else {
			obj = new ResultObj(1, "更新部门失败");
		}
		return obj;
	}

	// 查询最大的排序码
	@Override
	public ResultObj getDeptMaxnOrderNum() {
		Integer num = sysDeptMapper.getDeptMaxnOrderNum();
		if (num != null) {
			num++;
		} else {
			num = 1;
		}
		return new ResultObj(0, "查询最大排序码成功", num);
	}

	// 新增部门
	@Override
	public ResultObj addDept(DeptVo deptVo) {
		ResultObj obj = null;
		int count = sysDeptMapper.insert(deptVo);
		if (count >= 0) {
			obj = new ResultObj(0, "增加部门成功");
		} else {
			obj = new ResultObj(1, "增加部门失败");
		}
		return obj;
	}

	@Override
	public ResultObj getDeptById(Integer id) {
		SysDept dept = sysDeptMapper.getDeptBy(id);
		return new ResultObj(1, "查询部门成功", dept);
	}

}
