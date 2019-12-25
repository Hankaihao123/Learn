package com.hkh.sys.bean.activiti;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activiti_Task {
	private String id; // id
	private String assignee; // 办理人
	private String name; // 当前正在执行的任务名
	@JsonFormat(pattern = "yyyy年MM月dd日HH:mm:ss")
	private Date createTime;// 创建时间
	private String workFlowName;// 工作流名称
	private String exeWorkFlowName;// 执行的名称
}
