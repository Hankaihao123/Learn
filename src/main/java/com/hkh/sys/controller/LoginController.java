package com.hkh.sys.controller;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.bean.SysLogInfo;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.service.SysLogInfoService;
import com.hkh.util.ActivierUser;
import com.hkh.util.ResultObj;
import com.hkh.util.WebUtils;

@RequestMapping("Login")
@RestController
public class LoginController {

	@Autowired
	SysLogInfoService sysLogInfoService;

	@RequestMapping("login")
	public ResultObj login(SysUser user, String code) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginname(), user.getPwd());
		try {
			subject.login(token);
			ActivierUser activeUser = (ActivierUser) subject.getPrincipal();
			WebUtils.getSession().setAttribute("user", activeUser.getUser());
			String loginname = activeUser.getUser().getName() + '-' + activeUser.getUser().getLoginname();
			String loginip = WebUtils.getRequest().getRemoteAddr();
			SysLogInfo logInfo = new SysLogInfo();
			logInfo.setLoginip(loginip);
			logInfo.setLoginname(loginname);
			logInfo.setLogintime(new Date());
			sysLogInfoService.insert(logInfo);
			return ResultObj.LOGIN_SUCCESS;
		} catch (AuthenticationException e) {
			return ResultObj.LOGIN_ERROR_PASS;
		}
	}

	@RequestMapping("loginOut")
	public ResultObj loginOut() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return new ResultObj(0, "退出登录成功");
	}
}
