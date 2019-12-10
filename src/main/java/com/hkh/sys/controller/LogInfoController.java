package com.hkh.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.service.SysLogInfoService;
import com.hkh.sys.vo.LogInfoVo;
import com.hkh.util.ResultObj;

@RequestMapping("LogInfo")
@RestController
public class LogInfoController {
	@Autowired
	SysLogInfoService sysLogInfoService;

	//查询所有的日志登录记录
	@RequestMapping("queryAllLog")
	public ResultObj queryAllLog(LogInfoVo logVo) {
		System.err.println(logVo);
		return sysLogInfoService.queryAllLog(logVo);

	}

	//批量删除日志登录记录
	@RequestMapping("deleteLogInfo")
	public ResultObj deleteLogInfo(String ids) {
		System.err.println(ids);
		return sysLogInfoService.deleteLogInfomult(ids);
	}
}
