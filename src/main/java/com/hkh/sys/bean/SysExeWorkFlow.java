package com.hkh.sys.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysExeWorkFlow {
	private Integer id;

	private String title;

	private String content;

	private Integer datas;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createtime;

	private String state; // 流程的状态 '0,放弃 1,新建 2,已提交 3,审批中 4,审批完成',

	private Integer userid; // 谁发起了工作流

	private Integer type; // 指定那个工作流

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endtime;

}