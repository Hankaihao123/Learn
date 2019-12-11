package com.hkh.sys.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.hkh.sys.bean.SysLogInfo;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.service.SysLogInfoService;
import com.hkh.sys.service.SysUserService;
import com.hkh.util.ActivierUser;
import com.hkh.util.ResultObj;
import com.hkh.util.WebUtils;

@RequestMapping("Login")
@RestController
public class LoginController {

	@Autowired
	private DefaultKaptcha defaultKaptcha;

	@Autowired
	SysLogInfoService sysLogInfoService;

	@Autowired
	SysUserService sysUserService;

	@RequestMapping("login")
	public ResultObj login(SysUser user, String code, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String scode = (String) session.getAttribute("code");
		if (scode == null || !scode.equals(code)) {
			return ResultObj.LOGIN_ERROR_CODE;
		}
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

	// 用户注册
	@RequestMapping("registerUser")
	public ResultObj registerUser(@RequestParam("rloginname") String rloginname, @RequestParam("rpwd") String rpwd,
			@RequestParam("rname") String rname) {
		return sysUserService.registerUser(rloginname, rpwd, rname);
	}

	// 验证码
	@RequestMapping("codeImg")
	public ResultObj codeImg(HttpServletRequest request, HttpServletResponse response) {
		byte[] captchaChallengeAsJpeg;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		String createText = defaultKaptcha.createText();
		HttpSession session = request.getSession();
		session.setAttribute("code", createText);
		BufferedImage challenge = defaultKaptcha.createImage(createText);
		try {
			ImageIO.write(challenge, "jpg", jpegOutputStream);
		} catch (IOException e) {
			return new ResultObj(1, "验证生成失败"); // 返回成功提示信息
		}
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		try {
			ServletOutputStream servletOutputStream = response.getOutputStream();
			response.reset();
			servletOutputStream.write(captchaChallengeAsJpeg);
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (IOException e) {
			return new ResultObj(1, "验证生成失败"); // 返回成功提示信息
		}
		return new ResultObj(0, "验证生成成功"); // 返回成功提示信息
	}

	// 退出登录
	@RequestMapping("loginOut")
	public ResultObj loginOut() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return new ResultObj(0, "退出登录成功");
	}

}
