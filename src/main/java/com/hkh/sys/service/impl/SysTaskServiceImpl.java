package com.hkh.sys.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hkh.sys.bean.SysTask;
import com.hkh.sys.dao.SysTaskMapper;
import com.hkh.sys.service.SysTaskService;
import com.hkh.sys.vo.TaskVo;
import com.hkh.util.QuartzService;
import com.hkh.util.ResultObj;

@Service
@Transactional
public class SysTaskServiceImpl implements SysTaskService {

	@Autowired
	SysTaskMapper sysTaskMapper;

	@Autowired
	QuartzService quartzService;

	@Autowired
	Scheduler scheduler;

	// 0没有开启过 1 正在执行 2暂停了
	@PostConstruct
	public void init() throws Exception {
		TaskVo taskVo = new TaskVo();
		// 启动的任务直接启动
		taskVo.setJobStatus(1);
		List<SysTask> myJobs = sysTaskMapper.queryAllTasks(taskVo);
		for (SysTask myJob : myJobs) {
			Class cls = Class.forName(myJob.getBeanClass());
			quartzService.startJob(myJob.getJobGroup(), myJob.getJobName(), cls, myJob.getCronExpression());
			scheduler.start();
		}
		// 暂停的任务创建一下
		taskVo.setJobStatus(2);
		List<SysTask> myJobs1 = sysTaskMapper.queryAllTasks(taskVo);
		for (SysTask myJob : myJobs1) {
			Class cls = Class.forName(myJob.getBeanClass());
			String jobName = myJob.getJobName();
			String group = myJob.getJobGroup();
			quartzService.startJob(jobName, group, cls, myJob.getCronExpression());
			scheduler.start();
			JobKey jobKey = new JobKey(jobName, group);
			TriggerKey triggerKey = new TriggerKey("triggerName" + jobName, "triggerGroup" + group);
			scheduler.pauseJob(jobKey);
			scheduler.pauseTrigger(triggerKey);
		}
	}

	@Override
	public ResultObj queryAllTasks() {
		List<SysTask> list = sysTaskMapper.queryAllTasks(null);
		return new ResultObj(0, "查询成功", Long.valueOf(list.size()), list);
	}

	@Override
	public ResultObj addTask(TaskVo taskVo) {
		taskVo.setJobStatus(0);
		int count = sysTaskMapper.insert(taskVo);
		if (count > 0) {
			return new ResultObj(0, "增加任务成功");
		} else {
			return new ResultObj(0, "增加任务成功");
		}
	}

	@Override
	public ResultObj delTask(TaskVo taskVo) {
		SysTask task = this.sysTaskMapper.getById(taskVo.getId());
		if (task.getJobStatus() == 1) {
			return new ResultObj(1, "任务没有停止无法删除");
		}
		String jobName = task.getJobName();
		String group = task.getJobGroup();
		JobKey jobKey = new JobKey(jobName, group);
		TriggerKey triggerKey = new TriggerKey("triggerName" + jobName, "triggerGroup" + group);
		try {
			// 停止触发器
			scheduler.pauseTrigger(triggerKey);
			// 移除触发器
			scheduler.unscheduleJob(triggerKey);
			// 终止定时任务
			scheduler.deleteJob(jobKey);
			taskVo.setJobStatus(0);
			int count = sysTaskMapper.deleteById(taskVo.getId());
			if (count > 0) {
				return new ResultObj(0, "删除任务成功");
			} else {
				return new ResultObj(0, "删除任务失败");
			}
		} catch (SchedulerException e) {
			return new ResultObj(1, "删除任务失败");
		}
	}

	// 更新任务,只能更新cron要表达式,并且是只能更新停止的任务,暂停的任务无法更新(切记不能更新任务的全类名)
	@Override
	public ResultObj updateTask(TaskVo taskVo) {
		SysTask task = this.sysTaskMapper.getById(taskVo.getId());
		if (task.getJobStatus() == 1) {
			return new ResultObj(-1, "任务正在执行无法更新,请先暂停和停止任务");
		}
		// 更新定时任务频度
		try {
			// 封装triggerKey
			TriggerKey triggerKey = new TriggerKey("triggerName" + taskVo.getJobName(),
					"triggerGroup" + taskVo.getJobGroup());
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(taskVo.getCronExpression());
			// 重新封装获取触发器
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(taskVo.getJobName(), taskVo.getJobGroup())
					.withSchedule(cronScheduleBuilder).build();
			scheduler.rescheduleJob(triggerKey, trigger);
			int count = sysTaskMapper.updateById(taskVo);
			if (count > 0) {
				return new ResultObj(0, "更新任务成功");
			} else {
				return new ResultObj(1, "更新任务失败");
			}
		} catch (SchedulerException e) {
			return new ResultObj(1, "更新任务失败");
		}
	}

	// 开启任务让任务执行
	@Override
	public ResultObj startTask(TaskVo taskVo) {
		Class cls = null;
		SysTask task = sysTaskMapper.getById(taskVo.getId());
		if (task.getJobStatus() != 0) {
			return new ResultObj(0, "任务已经开启过了");
		}
		try {
			cls = Class.forName(taskVo.getBeanClass());
			quartzService.startJob(taskVo.getJobGroup(), taskVo.getJobName(), cls, taskVo.getCronExpression());
			scheduler.start();
			taskVo.setJobStatus(1);
			int count = sysTaskMapper.updateById(taskVo);
			if (count > 0) {
				return new ResultObj(0, "开启任务成功");
			} else {
				return new ResultObj(0, "开启任务失败");
			}
		} catch (Exception e) {
			return new ResultObj(1, "开启任务失败");
		}
	}

	// 开启任务让任务暂停
	@Override
	public ResultObj pauseTask(TaskVo taskVo) {
		SysTask task = this.sysTaskMapper.getById(taskVo.getId());
		if (task.getJobStatus() != 1) {
			return new ResultObj(0, "任务不在执行当中,无法暂停");
		}
		String jobName = task.getJobName();
		String group = task.getJobGroup();
		JobKey jobKey = new JobKey(jobName, group);
		TriggerKey triggerKey = new TriggerKey("triggerName" + jobName, "triggerGroup" + group);
		try {
			scheduler.pauseJob(jobKey);
			scheduler.pauseTrigger(triggerKey);
			task.setJobStatus(2);
			int count = sysTaskMapper.updateById(task);
			if (count > 0) {
				return new ResultObj(0, "暂停任务成功");
			} else {
				return new ResultObj(0, "暂停任务失败");
			}
		} catch (SchedulerException e) {
			return new ResultObj(1, "暂停任务失败");
		}
	}

	// 任务恢复
	@Override
	public ResultObj resumeTask(TaskVo taskVo) {
		SysTask task = this.sysTaskMapper.getById(taskVo.getId());
		if (task.getJobStatus() != 2) {
			return new ResultObj(0, "任务不在暂停当中,无法恢复");
		}
		String jobName = task.getJobName();
		String group = task.getJobGroup();
		JobKey jobKey = new JobKey(jobName, group);
		TriggerKey triggerKey = new TriggerKey("triggerName" + jobName, "triggerGroup" + group);
		try {
			scheduler.resumeJob(jobKey);
			scheduler.resumeTrigger(triggerKey);
			task.setJobStatus(1);
			int count = sysTaskMapper.updateById(task);
			if (count > 0) {
				return new ResultObj(0, "恢复任务成功");
			} else {
				return new ResultObj(1, "恢复任务失败");
			}
		} catch (SchedulerException e) {
			return new ResultObj(0, "恢复任务失败");
		}
	}

	// 停止任务
	@Override
	public ResultObj stopTask(TaskVo taskVo) {
		SysTask task = this.sysTaskMapper.getById(taskVo.getId());
		if (task.getJobStatus() == 0) {
			return new ResultObj(0, "任务都有没有开始,无法停止");
		}
		String jobName = task.getJobName();
		String group = task.getJobGroup();
		JobKey jobKey = new JobKey(jobName, group);
		TriggerKey triggerKey = new TriggerKey("triggerName" + jobName, "triggerGroup" + group);
		try {
			// 停止触发器
			scheduler.pauseTrigger(triggerKey);
			// 移除触发器
			scheduler.unscheduleJob(triggerKey);
			// 终止定时任务
			scheduler.deleteJob(jobKey);
			taskVo.setJobStatus(0);
			int count = sysTaskMapper.updateById(taskVo);
			if (count > 0) {
				return new ResultObj(0, "停止任务成功");
			} else {
				return new ResultObj(1, "停止任务失败");
			}
		} catch (SchedulerException e) {
			return new ResultObj(1, "停止任务失败");
		}
	}

	// 判断任务是否在运行
	@Override
	public ResultObj isTaskExe(TaskVo taskVo) {
		if (taskVo.getJobStatus() != 0) {
			return new ResultObj(-1, "任务正在执行无法更新,请先停止任务");
		} else {
			return new ResultObj(0, "可以修改任务");
		}
	}
}
