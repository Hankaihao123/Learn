package com.hkh.config.quartz;

import java.io.IOException;
import java.util.Properties;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.hkh.util.RedisService;

@Configuration
public class QuartzAutoConfiguration {
	@Autowired
	RedisService redisSrevice;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		try {
			schedulerFactoryBean.setOverwriteExistingJobs(true);
			schedulerFactoryBean.setQuartzProperties(quartzProperties());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return schedulerFactoryBean;
	}

	// 指定quartz.properties，可在配置文件中配置相关属性
	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	// 创建schedule
	@Bean(name = "scheduler")
	public Scheduler scheduler() {
		Scheduler scheduler = schedulerFactoryBean().getScheduler();
		return scheduler;
	}
}
