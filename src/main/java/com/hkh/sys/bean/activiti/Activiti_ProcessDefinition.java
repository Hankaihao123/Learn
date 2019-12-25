package com.hkh.sys.bean.activiti;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activiti_ProcessDefinition {
	private String id;
	protected String key;
	protected int version;
	protected String deploymentId;
	protected String diagramResourceName;

}
