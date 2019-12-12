package com.hkh.sys.vo;

import com.hkh.sys.bean.SysUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserVo extends SysUser {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@Override
	public String toString() {
		return "UserVo [page=" + page + ", limit=" + limit + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getLoginname()=" + getLoginname() + ", getAddress()=" + getAddress() + ", getSex()=" + getSex()
				+ ", getRemark()=" + getRemark() + ", getPwd()=" + getPwd() + ", getDeptid()=" + getDeptid()
				+ ", getHiredate()=" + getHiredate() + ", getMgr()=" + getMgr() + ", getAvailable()=" + getAvailable()
				+ ", getOrdernum()=" + getOrdernum() + ", getType()=" + getType() + ", getImgpath()=" + getImgpath()
				+ ", getSalt()=" + getSalt() + "]";
	}

}
