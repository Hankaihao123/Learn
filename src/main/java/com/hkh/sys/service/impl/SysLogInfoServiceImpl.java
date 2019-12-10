package com.hkh.sys.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hkh.sys.bean.SysLogInfo;
import com.hkh.sys.dao.SysLogInfoMapper;
import com.hkh.sys.service.SysLogInfoService;
import com.hkh.sys.vo.LogInfoVo;
import com.hkh.util.ResultObj;

@Service
public class SysLogInfoServiceImpl implements SysLogInfoService {

	@Autowired
	SysLogInfoMapper sysLogInfoMapper;

	@Override
	public ResultObj queryAllLog(LogInfoVo logVo) {
		if (null != logVo.getStartTime()) {
			if (logVo.getLoginip().equals("0.0.0.0")) {
				logVo.setLoginip(null);
			}
			if (logVo.getLoginname().equals("istest")) {
				logVo.setLoginname(null);
			}
			if (logVo.getEndTime() == null) {
				logVo.setEndTime(new Date());
			}
		}
		Page<Object> startPage = PageHelper.startPage(logVo.getPage(), logVo.getLimit());
		List<SysLogInfo> list = sysLogInfoMapper.queryAllLog(logVo);
		// layui表格返回code默认要为0
		return new ResultObj(0, "查询登录日志成功", startPage.getTotal(), list);
	}

	@Override
	public ResultObj deleteLogInfomult(String ids) {
		String temps[] = ids.split(",");
		ResultObj obj = null;
		List<String> list = Arrays.asList(temps);
		int count = sysLogInfoMapper.deleteLogInfomult(list);
		if (count >= 0) {
			obj = new ResultObj(0, "删除登录日志成功");
		} else {
			obj = new ResultObj(1, "删除登录日志失败");
		}
		return obj;
	}

	@Override
	public ResultObj insert(SysLogInfo record) {
		int count = sysLogInfoMapper.insert(record);
		ResultObj obj = null;
		if (count >= 0) {
			obj = new ResultObj(0, "增加登录日志成功");
		} else {
			obj = new ResultObj(1, "增加登录日志失败");
		}
		return obj;
	}

}
