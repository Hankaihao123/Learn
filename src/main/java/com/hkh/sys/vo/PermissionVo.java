package com.hkh.sys.vo;

import com.hkh.sys.bean.SysPermission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionVo extends SysPermission {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@Override
	public String toString() {
		return "MenuVo [page=" + page + ", limit=" + limit + ", getId()=" + getId() + ", getPid()=" + getPid()
				+ ", getType()=" + getType() + ", getTitle()=" + getTitle() + ", getPercode()=" + getPercode()
				+ ", getIcon()=" + getIcon() + ", getHref()=" + getHref() + ", getTarget()=" + getTarget()
				+ ", getOpen()=" + getOpen() + ", getOrdernum()=" + getOrdernum() + ", getAvailable()=" + getAvailable()
				+ "]";
	}

}
