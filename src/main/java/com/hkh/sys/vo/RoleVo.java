package com.hkh.sys.vo;

import com.hkh.sys.bean.SysRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoleVo extends SysRole {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@Override
	public String toString() {
		return "RoleVo [page=" + page + ", limit=" + limit + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getRemark()=" + getRemark() + ", getAvailable()=" + getAvailable() + ", getCreatetime()="
				+ getCreatetime() + "]";
	}

}
