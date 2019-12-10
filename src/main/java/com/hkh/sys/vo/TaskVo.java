package com.hkh.sys.vo;

import com.hkh.sys.bean.SysTask;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TaskVo extends SysTask {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@Override
	public String toString() {
		return "TaskVo [page=" + page + ", limit=" + limit + ", getId()=" + getId() + ", getJobName()=" + getJobName()
				+ ", getDescription()=" + getDescription() + ", getCronExpression()=" + getCronExpression()
				+ ", getBeanClass()=" + getBeanClass() + ", getJobStatus()=" + getJobStatus() + ", getJobGroup()="
				+ getJobGroup() + ", getCreateUser()=" + getCreateUser() + ", getCreateTime()=" + getCreateTime() + "]";
	}

}
