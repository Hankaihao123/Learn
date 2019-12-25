package com.hkh.sys.vo;

import com.hkh.sys.bean.SysExeWorkFlow;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysExeWorkFlowVo extends SysExeWorkFlow {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@Override
	public String toString() {
		return "SysExeWorkFlowVo [page=" + page + ", limit=" + limit + ", getId()=" + getId() + ", getTitle()="
				+ getTitle() + ", getContent()=" + getContent() + ", getDatas()=" + getDatas() + ", getCreatetime()="
				+ getCreatetime() + ", getState()=" + getState() + ", getUserid()=" + getUserid() + ", getType()="
				+ getType() + "]";
	}

}
