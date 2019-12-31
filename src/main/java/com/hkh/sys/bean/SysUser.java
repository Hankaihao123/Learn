package com.hkh.sys.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;

	private String name;

	private String loginname;

	private String address;

	private Integer sex;

	private String remark;
	@JsonIgnore
	private String pwd;

	private Integer deptid;
	@JsonFormat(pattern = "yyyy年MM月dd日")
	private Date hiredate;

	private Integer mgr;

	private Integer available;

	private Integer ordernum;

	private Integer type;

	private String imgpath;
	@JsonIgnore
	private String salt;

	private String leadername;

	private String deptname;

	private String phone;

	private String email;

	private String meself;

}