package com.hkh.sys.vo;

import com.hkh.sys.bean.SysDept;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeptVo extends SysDept {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@Override
	public String toString() {
		return "DeptVo [page=" + page + ", limit=" + limit + ", getId()=" + getId() + ", getPid()=" + getPid()
				+ ", getTitle()=" + getTitle() + ", getOpen()=" + getOpen() + ", getRemark()=" + getRemark()
				+ ", getAddress()=" + getAddress() + ", getAvailable()=" + getAvailable() + ", getOrdernum()="
				+ getOrdernum() + ", getCreatetime()=" + getCreatetime() + "]";
	}

}
