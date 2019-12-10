package com.hkh.util;

import static org.quartz.JobBuilder.newJob;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzService {

	@Autowired
	Scheduler scheduler;

	public void startJob(String group, String name, Class clazz, String cron) {

		// 封装信息
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("group", group);
		jobDataMap.put("name", name);

		// 创建jobDetail
		JobDetail jobDetail = newJob(clazz).usingJobData(jobDataMap).build();

		// 创建cronScheduleBuilder
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron)
				.withMisfireHandlingInstructionDoNothing();// 错过任务不处理

		// 构造触发器组名和别名
		String triggerName = "triggerName" + name;
		String triggerGroup = "triggerGroup" + group;

		// 创建trigger
		CronTrigger cronTrigger = (CronTrigger) TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
				.withSchedule(cronScheduleBuilder).build();

		try {
			// 绑定任务和触发器
			scheduler.scheduleJob(jobDetail, cronTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
