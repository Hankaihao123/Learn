package com.hkh.sys.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	public ResultObj login(SysUser user, String code, HttpServletResponse response) {
		HttpSession session = WebUtils.getSession();
		System.err.println(session.getId());
		String scode = (String) session.getAttribute("code");
		if (scode == null || !scode.equals(code)) {
			return ResultObj.LOGIN_ERROR_CODE;
		}
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginname(), user.getPwd());
		try {
			subject.login(token);
			ActivierUser activeUser = (ActivierUser) subject.getPrincipal();
			SysUser loginUser = activeUser.getUser();
			String loginname = activeUser.getUser().getName() + '-' + activeUser.getUser().getLoginname();
			String loginip = WebUtils.getRequest().getRemoteAddr();
			SysLogInfo logInfo = new SysLogInfo();
			logInfo.setLoginip(loginip);
			logInfo.setLoginname(loginname);
			logInfo.setLogintime(new Date());
			sysLogInfoService.insert(logInfo);
			// token.setRememberMe(true);
			WebUtils.getSession().setAttribute("user", loginUser);
			Cookie cookie1 = new Cookie("hkh_username", loginUser.getLoginname());
			cookie1.setPath("/");
			cookie1.setMaxAge(172800);
			response.addCookie(cookie1);
			// Cookie cookie2 = new Cookie("password", "123456");
			// cookie2.setPath("/");
			// response.addCookie(cookie2);
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
	@ResponseBody
	public ResultObj codeImg(HttpServletResponse response) {
		byte[] captchaChallengeAsJpeg;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		String createText = defaultKaptcha.createText();
		HttpSession session = WebUtils.getSession();
		System.err.println(session.getId() + "->" + createText);
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
			servletOutputStream.write(captchaChallengeAsJpeg);
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (IOException e) {
			return new ResultObj(1, "验证生成失败"); // 返回成功提示信息
		}
		// return new ResultObj(0, "验证生成成功"); // 返回成功提示信息
		return null;
	}

	// 登录时获取用户头像
	@RequestMapping("getUserImgpath")
	public ResultObj getUserImgpath(String name) {
		ResultObj userImgPath = sysUserService.getUserImgPath(name);
		return userImgPath;
	}

	// 退出登录
	@RequestMapping("loginOut")
	public ResultObj loginOut() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return new ResultObj(0, "退出登录成功");
	}

}
