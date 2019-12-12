package com.hkh.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("System")
@Controller
public class SystemController {

	@RequestMapping("toLogin")
	public String toLogin() {
		return "sys/index/login";
	}

	// 登录成功跳转到index.html
	@RequestMapping("toIndex")
	public String toIndex() {
		return "sys/index/index";
	}

	// 跳转后台首页
	@RequestMapping("toMain")
	public String toMain() {
		return "sys/index/main";
	}

	// 跳转到日志管理页面
	@RequestMapping("toLogManager")
	public String toLogManager() {
		return "sys/log/LogInfoManager";
	}

	// 跳转到公告管理页面
	@RequestMapping("toNoticeManager")
	public String tonoticeManager() {
		return "sys/notice/NoticeManager";
	}

	// 跳转到部门管理主页
	@RequestMapping("toDeptManager")
	public String toDeptManager() {
		return "sys/dept/deptManager";
	}

	// 跳转到部门管理左页
	@RequestMapping("toDeptLeft")
	public String toDeptLeft() {
		return "sys/dept/deptLeft";
	}

	// 跳转到部门管理右页
	@RequestMapping("toDeptRight")
	public String toDeptRight() {
		return "sys/dept/deptRight";
	}

	// 跳转到菜单管理
	@RequestMapping("toMenuManager")
	public String toMenuManager() {
		return "sys/menu/MenuManager";
	}

	// 跳转到权限管理
	@RequestMapping("toPermissionManager")
	public String toPermissionManager() {
		return "sys/menu/PermissionManager";
	}

	// 跳转到角色管理
	@RequestMapping("toRoleManager")
	public String toRoleManager() {
		return "sys/menu/RoleManager";
	}

	// 跳转到用户管理
	@RequestMapping("toUserManager")
	public String toUserManager() {
		return "sys/user/UserManager";
	}

	// 跳转到任务管理
	@RequestMapping("toTaskManager")
	public String toTaskManager() {
		return "sys/task/TaskManager";
	}

	// 跳转到个人信息页面
	@RequestMapping("toUserInfo")
	public String toUserInfo() {
		return "sys/user/UserInfo";
	}
}
