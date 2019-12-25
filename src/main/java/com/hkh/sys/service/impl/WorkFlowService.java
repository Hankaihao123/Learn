package com.hkh.sys.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hkh.sys.bean.SysExeWorkFlow;
import com.hkh.sys.bean.activiti.Activiti_Deployment;
import com.hkh.sys.bean.activiti.Activiti_ProcessDefinition;
import com.hkh.sys.dao.SysExeWorkFlowMapper;
import com.hkh.sys.vo.SysExeWorkFlowVo;
import com.hkh.sys.vo.activiti.DeploymentVo;
import com.hkh.sys.vo.activiti.ProcessDefinitionVo;
import com.hkh.util.ResultObj;

@Service
@ConfigurationProperties(prefix = "project")
public class WorkFlowService {
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	TaskService taskService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	HistoryService historyService;
	@Autowired
	SysExeWorkFlowMapper sysExeWorkFlowMapper;

	private String workflow;
	private String WorkFlowDefineImage;

	public String getWorkFlowDefineImage() {
		return WorkFlowDefineImage;
	}

	public void setWorkFlowDefineImage(String workFlowDefineImage) {
		WorkFlowDefineImage = workFlowDefineImage;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	// 创建工作流
	@SuppressWarnings("deprecation")
	public void createWorkFlow(HttpServletRequest request, HttpServletResponse response) {
		try {
			String modelName = "modelName";
			String modelKey = "modelKey";
			String description = "description";
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(modelName);
			modelData.setKey(modelKey);
			// 保存模型
			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
			response.sendRedirect(request.getContextPath() + "/activiti/modeler.html?modelId=" + modelData.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查询所有的部署的工作流
	public ResultObj queryAllWorkFlow(DeploymentVo deployVo) {
		List<Deployment> list = null;
		List<Activiti_Deployment> result = null;
		Long count;
		DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
		if (deployVo == null) {
			list = deploymentQuery.list();
			count = deploymentQuery.count();
		} else {
			if (deployVo.getName() != null && !deployVo.getName().equals("")) {
				deploymentQuery = deploymentQuery.deploymentNameLike("%" + deployVo.getName() + "%");
			}
			if (deployVo.getLimit() != null && deployVo.getPage() != null) {
				int maxResults = deployVo.getLimit();
				int firstResult = (deployVo.getPage() - 1) * maxResults;
				deploymentQuery.listPage(firstResult, maxResults);
			}
			list = deploymentQuery.list();
			count = deploymentQuery.count();
		}
		result = new ArrayList<Activiti_Deployment>();
		for (Deployment deployment : list) {
			Activiti_Deployment temp = new Activiti_Deployment();
			BeanUtils.copyProperties(deployment, temp);
			String url = "/WorkFlow/downWorkFlowZip/" + deployment.getName() + ".zip";
			temp.setDownurl(url);
			result.add(temp);
		}
		return new ResultObj(0, "查询部署流程成功", count, result);
	}

	// 通过压缩包部署流程
	public ResultObj deployWorkFlowZip(String name, MultipartFile file) {
		System.out.println(file);
		String orfilename = file.getOriginalFilename();
		String suff = orfilename.substring(orfilename.lastIndexOf('.') + 1, orfilename.length());
		String newfilename = name + "." + suff;
		File uploadfile = new File(this.workflow + newfilename);
		try {
			if (file.getSize() != 0) {
				file.transferTo(uploadfile);
			}
			InputStream inputStream = new FileInputStream(uploadfile);
			ZipInputStream zipInputStream = new ZipInputStream(inputStream);
			repositoryService.createDeployment().name(name).addZipInputStream(zipInputStream).deploy();
		} catch (IOException e) {
			System.out.println(e);
			return new ResultObj(1, "部署流程失败");
		}
		return new ResultObj(0, "部署流程成功");
	}

	public ResultObj deleteDeployWorkFlow(String delopid) {
		repositoryService.deleteDeployment(delopid, true);
		SysExeWorkFlowVo sysExeWorkFlowVo = new SysExeWorkFlowVo();
		sysExeWorkFlowVo.setType(Integer.valueOf(delopid));
		List<SysExeWorkFlow> list = this.sysExeWorkFlowMapper.queryByCondtionExeWorkFlow(sysExeWorkFlowVo);
		for (SysExeWorkFlow sysExeWorkFlow : list) {
			this.sysExeWorkFlowMapper.delete(sysExeWorkFlow.getId());
		}
		return new ResultObj(0, "删除部署的流程成功");
	}

	// 下载部署过的zip文件
	@RequestMapping("/downWorkFlowZip")
	public ResultObj downWorkFlowZip(String name, HttpServletRequest request, HttpServletResponse response) {
		// 获取下载文件的真实路径
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		File file = new File(this.workflow + name);
		try {
			boolean exists = file.exists();
			if (exists == false) {
				return new ResultObj(1, "下载文件不存在");
			}
			in = new BufferedInputStream(new FileInputStream(file));
			out = new BufferedOutputStream(response.getOutputStream());
			// 解决下载中文乱码
			byte[] bytes = file.getName().getBytes("gbk");
			String filename = new String(bytes, "ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + filename);// 设置头部信息
			byte[] buffer = new byte[10240];
			int length = 0;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			out.flush();
		} catch (Exception e) {
			return new ResultObj(1, "下载文件异常");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				return new ResultObj(1, "下载文件异常");
			}
		}
		return null;
	}

	/******************* 流程定义表(act_re_procdef) ******************/
	// 查询所有流程定义
	public ResultObj queryAllWorkflowDefine(ProcessDefinitionVo ProdefVo) {
		List<ProcessDefinition> list = null;
		List<Activiti_ProcessDefinition> result = null;
		Long count;
		ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
		if (ProdefVo == null) {
			list = definitionQuery.list();
			count = definitionQuery.count();
		} else {
			if (ProdefVo.getKey() != null && !ProdefVo.getKey().equals("")) {
				definitionQuery = definitionQuery.processDefinitionKeyLike("%" + ProdefVo.getKey() + "%");
			}
			if (ProdefVo.getLimit() != null && ProdefVo.getPage() != null) {
				int maxResults = ProdefVo.getLimit();
				int firstResult = (ProdefVo.getPage() - 1) * maxResults;
				definitionQuery.listPage(firstResult, maxResults);
			}
			list = definitionQuery.list();
			count = definitionQuery.count();
		}
		result = new ArrayList<Activiti_ProcessDefinition>();
		for (ProcessDefinition processdef : list) {
			Activiti_ProcessDefinition temp = new Activiti_ProcessDefinition();
			BeanUtils.copyProperties(processdef, temp);
			result.add(temp);
		}
		return new ResultObj(0, "查询流程实例成功", count, result);
	}

	// 查看指定的流程图
	public ResultObj viewWorkFlowImage(ProcessDefinitionVo ProdefVo) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(ProdefVo.getDeploymentId()).singleResult();
		InputStream inputStream = repositoryService.getProcessDiagram(processDefinition.getId());
		File file = new File(this.WorkFlowDefineImage + processDefinition.getDiagramResourceName());
		try {
			int len = 0;
			byte[] bs = new byte[1024];
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			while ((len = inputStream.read(bs)) != -1) {
				bufferedOutputStream.write(bs, 0, len);
			}
			bufferedOutputStream.close();
			inputStream.close();
		} catch (Exception e) {
			return new ResultObj(0, "查看流程图失败");
		}
		return new ResultObj(0, "查看流程图成功", "/WorkFlowDefineImage/" + processDefinition.getDiagramResourceName());
	}
}
