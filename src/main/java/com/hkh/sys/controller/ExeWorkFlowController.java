package com.hkh.sys.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.service.ExeWorkFlowService;
import com.hkh.sys.vo.SysExeWorkFlowVo;
import com.hkh.sys.vo.activiti.TaskVo;
import com.hkh.util.ResultObj;

@RequestMapping("ExeWorkFlow")
@RestController
public class ExeWorkFlowController {

	@Autowired
	ExeWorkFlowService sysExeWorkFlowService;

	// 查询正在执行的流程
	@RequestMapping("queryAllExeWorkFlow")
	ResultObj queryAllExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		return sysExeWorkFlowService.queryAllExeWorkFlow(sysExeWorkFlowVo);
	}

	// 新增加要执行的流程
	@RequestMapping("insertExeWorkFlow")
	ResultObj insertExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		return sysExeWorkFlowService.insertExeWorkFlow(sysExeWorkFlowVo);
	}

	// 提交要执行的流程
	@RequestMapping("commitExeWorkFlow")
	ResultObj commitExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		return sysExeWorkFlowService.commitExeWorkFlow(sysExeWorkFlowVo);
	}

	// 查询用户需要办理的任务
	@RequestMapping("queryAllDoneTask")
	ResultObj queryAllDoneTask() {
		return sysExeWorkFlowService.queryAllDoneTask();
	}

	// 查询当前活动者的连线信息
	@RequestMapping("queryLineInfo")
	ResultObj queryLineInfo(TaskVo taskVo) {
		return sysExeWorkFlowService.queryLineInfo(taskVo);
	}

	// 完成任务,不管是驳回还是同意都要走
	@RequestMapping("completeTask")
	public ResultObj completeTask(@RequestParam Map<String, Object> param) {
		return sysExeWorkFlowService.completeTask(param);
	}

	// 通过任务id获取业务表中的数据
	@RequestMapping("getSysExeWorkFlowByTaskId")
	public ResultObj getSysExeWorkFlowByTaskId(TaskVo taskVo) {
		return sysExeWorkFlowService.getSysExeWorkFlowByTaskId(taskVo);
	}

	// 通过任务id查询批注信息
	@RequestMapping("getCommentInfo")
	public ResultObj getCommentInfo(TaskVo taskVo) {
		return sysExeWorkFlowService.getCommentInfo(taskVo);
	}

	// 办理任务时查看流程图
	@RequestMapping("getImgActivitUser")
	public ResultObj getImgActivitUser(TaskVo taskVo) {
		return sysExeWorkFlowService.getImgActivitUser(taskVo);
	}

	// 查询流程审批的进度 id代表业务表中的id
	@RequestMapping("getWorkFlowInfo")
	public ResultObj getWorkFlowInfo(String id) {
		return sysExeWorkFlowService.getWorkFlowInfo(id);
	}

	// 更新执行的任务
	@RequestMapping("updateExeWorkFlow")
	public ResultObj updateExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		return sysExeWorkFlowService.updateExeWorkFlow(sysExeWorkFlowVo);
	}

	// 删除执行完成的任务
	@RequestMapping("deleteExeWorkFlow")
	public ResultObj deleteExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		return sysExeWorkFlowService.deleteExeWorkFlow(sysExeWorkFlowVo);
	}

	// 查询待办任务数
	@RequestMapping("getTaskCount")
	public ResultObj getTaskCount() {
		return sysExeWorkFlowService.getTaskCount();
	}
}
