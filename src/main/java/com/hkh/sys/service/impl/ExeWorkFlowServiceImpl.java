package com.hkh.sys.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hkh.sys.bean.SysExeWorkFlow;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.bean.activiti.Activiti_CommentEntity;
import com.hkh.sys.bean.activiti.Activiti_Task;
import com.hkh.sys.dao.SysExeWorkFlowMapper;
import com.hkh.sys.dao.SysUserMapper;
import com.hkh.sys.service.ExeWorkFlowService;
import com.hkh.sys.vo.SysExeWorkFlowVo;
import com.hkh.sys.vo.activiti.TaskVo;
import com.hkh.util.ResultObj;
import com.hkh.util.WebUtils;

@Service
@ConfigurationProperties(prefix = "project")
public class ExeWorkFlowServiceImpl implements ExeWorkFlowService {

	@Autowired
	SysExeWorkFlowMapper sysExeWorkFlowMapper;

	@Autowired
	SysUserMapper SysUserMapper;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	TaskService taskService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	HistoryService historyService;

	private String WorkFlowDefineImage;

	public String getWorkFlowDefineImage() {
		return WorkFlowDefineImage;
	}

	public void setWorkFlowDefineImage(String workFlowDefineImage) {
		WorkFlowDefineImage = workFlowDefineImage;
	}

	// 获取执行的流程
	@Override
	public ResultObj queryAllExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		if (sysExeWorkFlowVo.getEndtime() == null) {
			sysExeWorkFlowVo.setEndtime(new Date());
		}
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		sysExeWorkFlowVo.setUserid(user.getId());
		Page<Object> startPage = PageHelper.startPage(sysExeWorkFlowVo.getPage(), sysExeWorkFlowVo.getLimit());
		List<SysExeWorkFlow> list = sysExeWorkFlowMapper.queryByCondtionExeWorkFlow(sysExeWorkFlowVo);
		return new ResultObj(0, "查询部门成功", startPage.getTotal(), list);
	}

	// 增加一个执行的流程
	@Override
	public ResultObj insertExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		sysExeWorkFlowVo.setCreatetime(new Date());
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		sysExeWorkFlowVo.setUserid(user.getId());
		sysExeWorkFlowVo.setState("1");
		int count = sysExeWorkFlowMapper.insert(sysExeWorkFlowVo);
		if (count > 0) {
			return new ResultObj(0, "增加执行流程成功");
		} else {
			return new ResultObj(0, "增加执行流程失败");
		}
	}

	// 提交要执行的流程
	@Override
	public ResultObj commitExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		// 业务id与activiti进行绑定 <SysExeWorkFlow:id>
		String businessKey = SysExeWorkFlow.class.getSimpleName() + sysExeWorkFlowVo.getId();
		// 得到部署id
		String deploymentId = String.valueOf(sysExeWorkFlowVo.getType());
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploymentId).singleResult();
		// 获取key来启动流程
		String key = processDefinition.getKey();
		Map<String, Object> variables = new HashMap<String, Object>();
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		variables.put("username", user.getName());
		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(key, businessKey, variables);
		sysExeWorkFlowVo.setState("2");
		sysExeWorkFlowMapper.update(sysExeWorkFlowVo);
		return new ResultObj(0, "申请执行流程成功", processInstance.getId());
	}

	// 通过任务id查询到运行实例中的业务key
	private ExecutionEntity queryBiussKeyInfo(Task task) {
		// 查询关联业务表中要信息
		String processInstanceId = task.getProcessInstanceId();
		ExecutionEntity singleResult = (ExecutionEntity) runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId).singleResult();
		return singleResult;
	}

	// 查询用户要办理的任务
	@Override
	public ResultObj queryAllDoneTask() {
		// 从session中获取用户信息
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		String assignee = user.getName();
		List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).list();
		ArrayList<Activiti_Task> arrlist = null;
		if (list == null || list.size() == 0) {
			return new ResultObj(0, "查询任务成功");
		}
		arrlist = new ArrayList<Activiti_Task>();
		for (Task task : list) {
			// 查询到流程名称
			String processDefinitionId = task.getProcessDefinitionId();
			String deploymentId = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(processDefinitionId).singleResult().getDeploymentId();
			String workFlowName = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult()
					.getName();
			// 查询关联业务表中要信息
			ExecutionEntity executionEntity = queryBiussKeyInfo(task);
			String businessKey = executionEntity.getBusinessKey();
			int lastIndexOf = businessKey.lastIndexOf('w') + 1;
			businessKey = businessKey.substring(lastIndexOf, businessKey.length());
			SysExeWorkFlow sysExeWorkFlow = this.sysExeWorkFlowMapper.getById(Integer.valueOf(businessKey));
			Activiti_Task activiti_Task = new Activiti_Task();
			BeanUtils.copyProperties(task, activiti_Task);
			activiti_Task.setWorkFlowName(workFlowName);
			activiti_Task.setExeWorkFlowName(sysExeWorkFlow.getTitle());
			Integer userid = sysExeWorkFlow.getUserid();
			SysUser sysUser = SysUserMapper.getUserById(userid);
			activiti_Task.setAssignee(sysUser.getName());
			arrlist.add(activiti_Task);
		}
		return new ResultObj(0, "查询任务成功", arrlist);
	}

	// 获取当前活动流程线信息,按钮相关的
	@Override
	public ResultObj queryLineInfo(TaskVo taskVo) {
		String taskId = taskVo.getId();
		// 通过任务id获取任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 从任务对象获取流程定义id
		String processDefinitionId = task.getProcessDefinitionId();
		String processInstanceId = task.getProcessInstanceId();
		// 获取流程实例对象
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		// 从流程实例中获取活动当前流程活动id
		String activityId = processInstance.getActivityId();
		ProcessDefinitionEntity processdefinitionentity = (ProcessDefinitionEntity) this.repositoryService
				.getProcessDefinition(processDefinitionId);
		ActivityImpl findActivity = processdefinitionentity.findActivity(activityId);
		List<PvmTransition> outgoingTransitions = findActivity.getOutgoingTransitions();
		ArrayList<String> list = new ArrayList<String>();
		for (PvmTransition pvmTransition : outgoingTransitions) {
			list.add((String) pvmTransition.getProperty("name"));
		}
		return new ResultObj(0, "查询按钮上信息成功", list);
	}

	// 判断当前任务是否是结束节点
	private Boolean nextTaskIsEnd(Task task) {
		// 从任务对象获取流程定义id
		String processDefinitionId = task.getProcessDefinitionId();
		String processInstanceId = task.getProcessInstanceId();
		// 获取流程实例对象
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		// 从流程实例中获取活动当前流程活动id
		String activityId = processInstance.getActivityId();
		ProcessDefinitionEntity processdefinitionentity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		ActivityImpl findActivity = processdefinitionentity.findActivity(activityId);
		List<PvmTransition> outgoingTransitions = findActivity.getOutgoingTransitions();
		int i = 0;
		for (PvmTransition pvmTransition : outgoingTransitions) {
			// 获取线路的终点节点
			PvmActivity ac = pvmTransition.getDestination();
			// 当其中一条流线的中点不是结束节点时，直接返回 false（不是结束节点）
			if (i == 0 && "endEvent".equals(ac.getProperty("type"))) {
				return true;
			}
			i++;
		}
		return false;
	}

	// 通过任务id获取业务表中的数据
	@Override
	public ResultObj getSysExeWorkFlowByTaskId(TaskVo taskVo) {
		String taskId = taskVo.getId();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		String businessKey = processInstance.getBusinessKey();
		int lastIndexOf = businessKey.lastIndexOf('w') + 1;
		businessKey = businessKey.substring(lastIndexOf, businessKey.length());
		SysExeWorkFlow sysExeWorkFlow = this.sysExeWorkFlowMapper.getById(Integer.valueOf(businessKey));
		return new ResultObj(0, "查询该任务业务信息成功", sysExeWorkFlow);
	}

	// 通过任务id查询批注信息
	@Override
	public ResultObj getCommentInfo(TaskVo taskVo) {
		String taskId = taskVo.getId();
		ArrayList<Activiti_CommentEntity> arrayList = null;
		List<Comment> comments = null;
		// 查询已经完成的批注信息
		if (taskId.indexOf("S") != -1) {
			HistoricProcessInstance historicProcessInstance = this.historyService.createHistoricProcessInstanceQuery()
					.processInstanceBusinessKey(taskId).singleResult();
			comments = taskService.getProcessInstanceComments(historicProcessInstance.getId());
		} else {
			// 查询未完成的批准信息
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			comments = this.taskService.getProcessInstanceComments(task.getProcessInstanceId());
		}
		if (null == comments || comments.size() == 0) {
			return new ResultObj(0, "查询任务批注成功");
		}
		arrayList = new ArrayList<Activiti_CommentEntity>();
		for (Comment attachment : comments) {
			Activiti_CommentEntity temp = new Activiti_CommentEntity();
			BeanUtils.copyProperties(attachment, temp);
			arrayList.add(temp);
		}
		return new ResultObj(0, "查询任务批注成功", arrayList);
	}

	// 办理完成任务
	// 流程的状态 '0,放弃 1,新建 2,已提交 3,审批中 4,审批完成',
	@Override
	public ResultObj completeTask(Map<String, Object> param) {
		Boolean tf = Boolean.valueOf((String) param.get("tf"));
		String taskId = (String) param.get("id");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 获取一个运行实例,就可以得到业务key
		ExecutionEntity executionEntity = queryBiussKeyInfo(task);
		String processInstanceId = task.getProcessInstanceId();
		String businessKey = executionEntity.getBusinessKey();
		int lastIndexOf = businessKey.lastIndexOf('w') + 1;
		businessKey = businessKey.substring(lastIndexOf, businessKey.length());
		SysExeWorkFlow sysExeWorkFlow = sysExeWorkFlowMapper.getById(Integer.valueOf(businessKey));
		if (tf == true) {
			Boolean isEnd = nextTaskIsEnd(task);
			if (isEnd) {
				sysExeWorkFlow.setState("4");
			} else {
				sysExeWorkFlow.setState("3");
			}
		} else {
			if (param.get("message").equals("驳回")) {
				sysExeWorkFlow.setState("2");
			} else {
				sysExeWorkFlow.setState("0");
			}
		}
		// 获取当前session中的用户名,要保证该用户不会修改
		// 设置那个人批注了信息
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		Authentication.setAuthenticatedUserId(user.getName());
		String comment = '[' + (String) param.get("message") + ']';
		comment += (String) param.get("info");
		taskService.addComment(taskId, processInstanceId, comment);
		this.taskService.complete(taskId, param);
		this.sysExeWorkFlowMapper.update(sysExeWorkFlow);
		return new ResultObj(0, "办理任务成功");
	}

	// 获取当前活动者的流程图
	@Override
	public ResultObj getImgActivitUser(TaskVo taskVo) {
		String taskId = taskVo.getId();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		String deploymentId = processInstance.getDeploymentId();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploymentId).singleResult();
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
			return new ResultObj(0, "获取当前活动者的流程图失败");
		}
		Map<String, Object> resultmap = new HashMap<String, Object>();
		// 获取图片的位置
		String imgsrc = "/WorkFlowDefineImage/" + processDefinition.getDiagramResourceName();
		resultmap.put("imgsrc", imgsrc);
		// 当前活动者的坐标
		String activityId = processInstance.getActivityId();
		ProcessDefinitionEntity processdefinitionentity = (ProcessDefinitionEntity) this.repositoryService
				.getProcessDefinition(processInstance.getProcessDefinitionId());
		ActivityImpl currentactivity = processdefinitionentity.findActivity(activityId);
		Map<String, Object> coordinate = new HashMap<String, Object>();
		coordinate.put("x", currentactivity.getX());
		coordinate.put("y", currentactivity.getY());
		coordinate.put("width", currentactivity.getWidth());
		coordinate.put("height", currentactivity.getHeight());
		resultmap.put("coordinate", coordinate);
		return new ResultObj(0, "获取当前活动者的流程图成功", resultmap);
	}

	// 查看审批流程
	@Override
	public ResultObj getWorkFlowInfo(String id) {
		String bussidkey = SysExeWorkFlow.class.getSimpleName() + id;
		HistoricProcessInstance historicProcessInstance = this.historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(bussidkey).singleResult();
		String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
		System.err.println(historicProcessInstance.getEndTime());
		BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
		if (null == model) {
			return new ResultObj(0, "查看审批流程为空");
		}
		Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
		// 获取流程中所有的任务节点
		List<String> keys = new ArrayList<>();
		List<String> values = new ArrayList<>();
		String lastkey = "EndEvent";
		String lastvalue = null;
		for (FlowElement e : flowElements) {
			String string = e.getClass().toString();
			if (string.indexOf("StartEvent") != -1) {
				keys.add("StartEvent");
				values.add(e.getName());
			}
			if (string.indexOf("UserTask") != -1) {
				keys.add(e.getId());
				values.add(e.getName());
			}
			if (string.indexOf("EndEvent") != -1) {
				lastvalue = e.getName();
			}
		}
		keys.add(lastkey);
		values.add(lastvalue);
		Map<String, Object> resultmap = new HashMap<String, Object>();
		resultmap.put("keys", keys);
		resultmap.put("values", values);
		// 获取当前活动到的节点
		// 如果没有结束,获取活动节点
		if (historicProcessInstance.getEndTime() == null) {
			ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery()
					.processInstanceBusinessKey(bussidkey).singleResult();
			String activityId = processInstance.getActivityId();
			ProcessDefinitionEntity processdefinitionentity = (ProcessDefinitionEntity) this.repositoryService
					.getProcessDefinition(processInstance.getProcessDefinitionId());
			ActivityImpl currentactivity = processdefinitionentity.findActivity(activityId);
			resultmap.put("activer", currentactivity.getId());
		} else {
			// 结束了
			resultmap.put("activer", "EndEvent");
		}
		return new ResultObj(0, "查看审批流程成功", resultmap);
	}

	// 更新执行的任务
	@Override
	public ResultObj updateExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		int count = this.sysExeWorkFlowMapper.update(sysExeWorkFlowVo);
		if (count > 0) {
			return new ResultObj(0, "更新执行任务成功");
		} else {
			return new ResultObj(1, "更新执行任务失败");
		}
	}

	// 删除执行完成的任务
	@Override
	public ResultObj deleteExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo) {
		Integer id = sysExeWorkFlowVo.getId();
		SysExeWorkFlow exeWorkFlow = sysExeWorkFlowMapper.getById(id);
		if (exeWorkFlow.getState().equals("0") || exeWorkFlow.getState().equals("1")
				|| exeWorkFlow.getState().equals("4")) {
			int count = this.sysExeWorkFlowMapper.delete(id);
			if (count > 0) {
				return new ResultObj(0, "删除执行任务成功");
			} else {
				return new ResultObj(1, "删除执行任务失败");
			}
		}
		return new ResultObj(1, "执行任务在执行不能删除");
	}

	@Override
	public ResultObj getTaskCount() {
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		List<Task> list = taskService.createTaskQuery().taskAssignee(user.getName()).list();
		return new ResultObj(0, "查询待办任务成功", list.size());
	}

}
