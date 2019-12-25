package com.hkh.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.hkh.util.ResultObj;

//加了下面的注解就知道它是专门处理异常的类
@ControllerAdvice
public class HandleException {

	public static boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public Object unauthorizedException(Exception e, HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8;");
		try {
			ResultObj resultObj = new ResultObj(-1, "您没有该权限");
			response.getWriter().print(JSON.toJSONString(resultObj));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@ExceptionHandler(Exception.class)
	public Object handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
		if (isAjax(request)) {
			// 返回JSON数据
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8;");
			try {
				ResultObj resultObj = new ResultObj(-1, "系统内部有误,请联系管理员");
				response.getWriter().print(JSON.toJSONString(resultObj));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		} else {
			// 返回页面
			ModelAndView mv = new ModelAndView();
			mv.setViewName("404");
			mv.addObject("message", "有异常错误请你查看--->" + e.toString());
			return mv;
		}

	}

	/**
	 * 400 - Bad Request
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public String handleHttpMessageNotReadableException(Exception e) {
		return "参数解析失败";
	}

	/**
	 * 405 - Method Not Allowed
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public String handleHttpRequestMethodNotSupportedException(Exception e) {
		return "不支持当前请求方法";
	}

	/**
	 * 415 - Unsupported Media Type
	 */
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public String handleHttpMediaTypeNotSupportedException(Exception e) {
		return "不支持当前媒体类型";
	}

}