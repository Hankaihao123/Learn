package com.hkh.sys.bean;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDept {
	private Integer id;

	private Integer pid;

	private String title;

	private Integer open;

	private String remark;

	private String address;

	private Integer available;

	private Integer ordernum;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createtime;

}