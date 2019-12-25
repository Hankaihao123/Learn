package com.hkh.config.activiti;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.service.SysUserService;
import com.hkh.util.ResultObj;
import com.hkh.util.WebUtils;

public class WorkFlowTaskLinstner implements TaskListener {

	private static final long serialVersionUID = 1L;

	// 当任务来时会回调到,如果被驳回或者任务完成是不会调用该回调的
	@Override
	public void notify(DelegateTask delegateTask) {
		System.err.println("------------>开始查询下次处理人");
		// 得到当前用户
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		// 取出领导ID
		Integer mgr = user.getMgr();
		// 取出IOC容器
		HttpServletRequest request = WebUtils.getRequest();
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getServletContext());
		// 从IOC容器里面取出UserService
		SysUserService userService = applicationContext.getBean(SysUserService.class);
		// 3查询领导信息
		ResultObj userById = userService.getUserById(mgr);
		SysUser nextUser = (SysUser) userById.getData();
		// 4,设置办理人
		delegateTask.setAssignee(nextUser.getName());
		// 5.WebSocket通知在线下个用户
		WebSocketSendMessage(applicationContext, nextUser.getId());
	}

	private void WebSocketSendMessage(ApplicationContext applicationContext, Integer userid) {
		SimpMessagingTemplate simpMessagingTemplate = applicationContext.getBean(SimpMessagingTemplate.class);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("info", "您有一条新的待办任务");
		Object json = JSONObject.toJSON(map);
		simpMessagingTemplate.convertAndSendToUser(userid + "", "donetask", json);
		return;
	}
}
