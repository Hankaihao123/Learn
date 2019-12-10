package com.hkh.sys.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysLogInfo {
	private Integer id;

	private String loginname;

	private String loginip;

	private Date logintime;

}