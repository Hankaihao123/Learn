package com.hkh.sys.service;

import com.hkh.sys.bean.SysLogInfo;
import com.hkh.sys.vo.LogInfoVo;
import com.hkh.util.ResultObj;

public interface SysLogInfoService {
	// 获取所有的日志记录
	public ResultObj queryAllLog(LogInfoVo logVo);

	// 批量删除日志
	public ResultObj deleteLogInfomult(String ids);
	
	//增加登录日志
	public ResultObj insert(SysLogInfo record);
}
