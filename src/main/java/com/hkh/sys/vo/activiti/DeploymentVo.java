package com.hkh.sys.vo.activiti;


import com.hkh.sys.bean.activiti.Activiti_Deployment;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeploymentVo extends Activiti_Deployment {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@Override
	public String toString() {
		return "DeploymentVo [page=" + page + ", limit=" + limit + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getCategory()=" + getCategory() + ", getDeploymentTime()=" + getDeploymentTime() + "]";
	}

}
