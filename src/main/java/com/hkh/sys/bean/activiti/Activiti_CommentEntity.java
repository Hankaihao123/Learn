package com.hkh.sys.bean.activiti;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activiti_CommentEntity {
	private String id;
	private String type;
	private String userId;
	private Date time;
	private String taskId;
	private String processInstanceId;
	private String message;
	// private String fullMessage;
}
