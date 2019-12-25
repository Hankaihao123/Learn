package com.hkh.sys.vo.activiti;


import com.hkh.sys.bean.activiti.Activiti_ProcessDefinition;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessDefinitionVo extends Activiti_ProcessDefinition {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@Override
	public String toString() {
		return "ProcessDefinitionVo [page=" + page + ", limit=" + limit + ", getId()=" + getId() + ", getKey()="
				+ getKey() + ", getVersion()=" + getVersion() + ", getDeploymentId()=" + getDeploymentId()
				+ ", getDiagramResourceName()=" + getDiagramResourceName() + "]";
	}

}
