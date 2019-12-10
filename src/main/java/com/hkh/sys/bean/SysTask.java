package com.hkh.sys.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysTask {
	private Integer id;

	private String jobName;

	private String description;

	private String cronExpression;

	private String beanClass;

	private Integer jobStatus;

	private String jobGroup;

	private String createUser;

	private Date createTime;
}