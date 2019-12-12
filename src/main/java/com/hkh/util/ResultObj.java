package com.hkh.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultObj {
	public static final Integer OK = 200;
	public static final Integer ERROR = -1;
	public static final String TYPE_MENU = "menu"; // 代表菜单
	public static final String TYPE_PERMISSION = "permission";// 代表权限
	public static final Integer AVAILABLE_TRUE = 1;// 显示
	public static final Integer AVAILABLE_FALSE = 0;// 不显示
	public static final Integer USER_NORMAL = 1;// 用户
	public static final Integer USER_HEIGHT = 0;// 系统管理员
	public static final ResultObj LOGIN_SUCCESS = new ResultObj(OK, "登陆成功");
	public static final ResultObj LOGIN_ERROR_PASS = new ResultObj(ERROR, "登陆失败,用户名或密码不正确");
	public static final ResultObj LOGIN_ERROR_CODE = new ResultObj(ERROR, "登陆失败,验证码错误,点击更换");

	private Integer code;
	private String msg;
	private Long count;
	private Object data;

	public ResultObj(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public ResultObj(Integer code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

}
