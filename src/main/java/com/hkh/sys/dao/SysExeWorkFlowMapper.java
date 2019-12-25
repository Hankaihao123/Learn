package com.hkh.sys.dao;


import java.util.List;

import com.hkh.sys.bean.SysExeWorkFlow;
import com.hkh.sys.vo.SysExeWorkFlowVo;

public interface SysExeWorkFlowMapper {
	int delete(Integer id);

	int insert(SysExeWorkFlow record);

	SysExeWorkFlow getById(Integer id);

	int update(SysExeWorkFlow record);

	List<SysExeWorkFlow> queryByCondtionExeWorkFlow(SysExeWorkFlowVo sysExeWorkFlowVo);

}