package com.hkh.config.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class ShiroLoginFilter extends FormAuthenticationFilter {

	// 当用户访问未登陆资源时，会走的方法。 返回true代表已登陆，不用处理 返回false代表未登陆。提示前端
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		System.out.println("Shiro认证器给拦截了->>>>>>>>>>>>>");
		httpServletResponse.sendRedirect("/System/toLogin");
		return false;
	}

	// 判断当前请求是否为ajax请求
//	private boolean isAjax(ServletRequest request) {
//		String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
//		if ("XMLHttpRequest".equalsIgnoreCase(header)) {
//			return Boolean.TRUE;
//		}
//		return Boolean.FALSE;
//	}
}
