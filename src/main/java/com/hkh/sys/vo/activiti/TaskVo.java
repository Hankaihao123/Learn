package com.hkh.sys.vo.activiti;


import com.hkh.sys.bean.activiti.Activiti_Task;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TaskVo extends Activiti_Task {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@Override
	public String toString() {
		return "TaskVo [page=" + page + ", limit=" + limit + ", getId()=" + getId() + ", getAssignee()=" + getAssignee()
				+ ", getName()=" + getName() + ", getCreateTime()=" + getCreateTime() + ", getWorkFlowName()="
				+ getWorkFlowName() + ", getExeWorkFlowName()=" + getExeWorkFlowName() + "]";
	}

}
