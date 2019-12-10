package com.hkh.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.service.SysTaskService;
import com.hkh.sys.vo.TaskVo;
import com.hkh.util.ResultObj;

@RequestMapping("Task")
@RestController
public class TaskController {

	@Autowired
	SysTaskService sysTaskService;

	@RequestMapping("queryAllTasks")
	public ResultObj queryAllTasks() {
		return sysTaskService.queryAllTasks();
	}

	// 创建一个任务
	@RequestMapping("addTask")
	public ResultObj addTask(TaskVo taskVo) throws Exception {
		return sysTaskService.addTask(taskVo);
	}

	// 开启任务
	@RequestMapping("startTask")
	public ResultObj startTask(TaskVo taskVo) throws Exception {
		return sysTaskService.startTask(taskVo);
	}

	// 暂停任务
	@RequestMapping("pauseTask")
	public ResultObj pauseTask(TaskVo taskVo) throws Exception {
		return sysTaskService.pauseTask(taskVo);
	}

	// 恢复任务
	@RequestMapping("resumeTask")
	public ResultObj resumeTask(TaskVo taskVo) throws Exception {
		return sysTaskService.resumeTask(taskVo);
	}

	// 停止任务
	@RequestMapping("stopTask")
	public ResultObj stopTask(TaskVo taskVo) throws Exception {
		return sysTaskService.stopTask(taskVo);
	}

	// 更新任务
	@RequestMapping("updateTask")
	public ResultObj updateTask(TaskVo taskVo) throws Exception {
		return sysTaskService.updateTask(taskVo);
	}

	// 判断任务是否在执行
	@RequestMapping("isTaskExe")
	public ResultObj isTaskExe(TaskVo taskVo) throws Exception {
		return sysTaskService.isTaskExe(taskVo);
	}

	// 删除任务
	@RequestMapping("deleteTask")
	public ResultObj deleteTask(TaskVo taskVo) throws Exception {
		return sysTaskService.delTask(taskVo);
	}
}
