package com.hkh.config.activiti;

import javax.sql.DataSource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ActivitiConfiguration {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Bean
	public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
		SpringProcessEngineConfiguration spec = new SpringProcessEngineConfiguration();
		spec.setDataSource(dataSource);
		spec.setTransactionManager(platformTransactionManager);
		spec.setDatabaseSchemaUpdate("true");
		return spec;
	}

	@Bean
	public ProcessEngineFactoryBean processEngine() {
		ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
		processEngineFactoryBean.setProcessEngineConfiguration(springProcessEngineConfiguration());
		return processEngineFactoryBean;
	}

	@Bean
	public RepositoryService repositoryService() throws Exception {
		return processEngine().getObject().getRepositoryService();
	}

	@Bean
	public RuntimeService runtimeService() throws Exception {
		return processEngine().getObject().getRuntimeService();
	}

	@Bean
	public TaskService taskService() throws Exception {
		return processEngine().getObject().getTaskService();
	}

	@Bean
	public HistoryService historyService() throws Exception {
		return processEngine().getObject().getHistoryService();
	}
}
