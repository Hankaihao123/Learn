package com.hkh.sys.service;

import com.hkh.sys.vo.TaskVo;
import com.hkh.util.ResultObj;

public interface SysTaskService {
	ResultObj queryAllTasks();

	ResultObj addTask(TaskVo taskVo);

	ResultObj delTask(TaskVo taskVo);

	ResultObj updateTask(TaskVo taskVo);

	ResultObj startTask(TaskVo taskVo);

	ResultObj pauseTask(TaskVo taskVo);

	ResultObj resumeTask(TaskVo taskVo);

	ResultObj stopTask(TaskVo taskVo);

	ResultObj isTaskExe(TaskVo taskVo);
}
