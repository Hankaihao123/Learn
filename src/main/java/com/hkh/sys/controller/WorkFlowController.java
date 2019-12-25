package com.hkh.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hkh.sys.service.impl.WorkFlowService;
import com.hkh.sys.vo.activiti.DeploymentVo;
import com.hkh.sys.vo.activiti.ProcessDefinitionVo;
import com.hkh.util.ResultObj;

@RequestMapping("WorkFlow")
@Controller
public class WorkFlowController {

	@Autowired
	WorkFlowService workFlowService;

	// 创建新的工作流
	@RequestMapping("createWorkFlow")
	public void createWorkFlow(HttpServletRequest request, HttpServletResponse response) {
		workFlowService.createWorkFlow(request, response);
	}

	// 部署保存的工作流
	@RequestMapping("delopWorkFlow")
	@ResponseBody
	public ResultObj delopWorkFlow(HttpServletRequest request, HttpServletResponse response) {
		return new ResultObj(1, "部署失败");
	}

	// 查询所有的部署的工作流
	@RequestMapping("queryAllWorkFlow")
	@ResponseBody
	public ResultObj queryAllWorkFlow(DeploymentVo deployVo) {
		ResultObj resultObj = workFlowService.queryAllWorkFlow(deployVo);
		return resultObj;
	}

	// 通过zip来部署流程
	@RequestMapping("deployWorkFlowZip")
	@ResponseBody
	public ResultObj deployWorkFlowZip(String name, @RequestParam("file") MultipartFile file) {
		return workFlowService.deployWorkFlowZip(name, file);
	}

	// 删除部署的流程
	@RequestMapping("deleteDeployWorkFlow")
	@ResponseBody
	public ResultObj deleteDeployWorkFlow(@RequestParam("id") String delopid) {
		return workFlowService.deleteDeployWorkFlow(delopid);
	}

	// 下载部署好的流程zip文件
	@RequestMapping("downWorkFlowZip/{filename}")
	@ResponseBody
	public ResultObj downWorkFlowZip(@PathVariable("filename") String name, HttpServletRequest request,
			HttpServletResponse response) {
		return workFlowService.downWorkFlowZip(name, request, response);
	}

	/******************* 流程定义表(act_re_procdef) ******************/
	// 查询所有流程定义
	@RequestMapping("queryAllWorkflowDefine")
	@ResponseBody
	public ResultObj queryAllWorkflowDefine(ProcessDefinitionVo ProdefVo) {
		return workFlowService.queryAllWorkflowDefine(ProdefVo);
	}

	// 查看指定的流程图
	@RequestMapping("viewWorkFlowImage")
	@ResponseBody
	public ResultObj viewWorkFlowImage(ProcessDefinitionVo ProdefVo) {
		return workFlowService.viewWorkFlowImage(ProdefVo);
	}
}
