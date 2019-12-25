package com.hkh.sys.bean.activiti;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activiti_Deployment {
	private String id;
	private String name;
	@JsonIgnore
	private String category;
  
	private Date deploymentTime;
	private String downurl;

}
