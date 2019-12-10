package com.hkh.sys.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {
	private Integer id;

	private String name;

	private String loginname;

	private String address;

	private Integer sex;

	private String remark;

	private String pwd;

	private Integer deptid;

	private Date hiredate;

	private Integer mgr;

	private Integer available;

	private Integer ordernum;

	private Integer type;

	private String imgpath;

	private String salt;

	private String leadername;

	private String deptname;

}