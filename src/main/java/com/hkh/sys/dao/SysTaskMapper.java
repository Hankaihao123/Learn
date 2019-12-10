package com.hkh.sys.dao;

import java.util.List;

import com.hkh.sys.bean.SysTask;
import com.hkh.sys.vo.TaskVo;

public interface SysTaskMapper {
	int deleteById(Integer id);

	int insert(SysTask record);

	SysTask getById(Integer id);

	int updateById(SysTask record);

	List<SysTask> queryAllTasks(TaskVo taskVo);

}