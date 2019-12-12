package com.hkh.sys.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole {
	private Integer id;

	private String name;

	private String remark;

	private Integer available;

	private Date createtime;

	// 下面是layui穿梭框所需要的
	private String checked;
	private String disabled;
	private Boolean tf;

}