package com.hkh.sys.service;

import java.util.Map;

import com.hkh.sys.vo.SysExeWorkFlowVo;
import com.hkh.sys.vo.activiti.TaskVo;
import com.hkh.util.ResultObj;

public interface ExeWorkFlowService {

	// 获取执行的流程
	public ResultObj queryAllExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo);

	// 增加一个执行的流程
	public ResultObj insertExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo);

	// 提交要执行的流程
	public ResultObj commitExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo);

	// 查找用户的办理任务
	public ResultObj queryAllDoneTask();

	// 查询当前活动者的连线信息
	public ResultObj queryLineInfo(TaskVo taskVo);

	// 完成任务
	public ResultObj completeTask(Map<String, Object> param);

	// 通过任务id获取业务表中的数据
	public ResultObj getSysExeWorkFlowByTaskId(TaskVo taskVo);

	// 通过任务id查询批注信息
	public ResultObj getCommentInfo(TaskVo taskVo);

	// 获取当前活动的流程图
	public ResultObj getImgActivitUser(TaskVo taskVo);

	// 查询流程审批的进度 id代表业务表中的id
	public ResultObj getWorkFlowInfo(String id);

	// 更新执行的任务
	public ResultObj updateExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo);

	// 删除执行完成的任务
	public ResultObj deleteExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo);

	// 查询待办任务数量
	public ResultObj getTaskCount();
}
