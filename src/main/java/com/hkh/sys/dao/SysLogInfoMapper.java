package com.hkh.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hkh.sys.bean.SysLogInfo;
import com.hkh.sys.vo.LogInfoVo;

public interface SysLogInfoMapper {

	// 获取所有的日志记录
	List<SysLogInfo> queryAllLog(LogInfoVo logVo);

	// 删除登录日志记录
	int deleteLogInfomult(@Param("ids") List<String> ids);

	int deleteByPrimaryKey(Integer id);

	int insert(SysLogInfo record);

	int insertSelective(SysLogInfo record);

	SysLogInfo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysLogInfo record);

	int updateByPrimaryKey(SysLogInfo record);

}