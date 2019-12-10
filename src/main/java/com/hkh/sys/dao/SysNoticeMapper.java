package com.hkh.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hkh.sys.bean.SysNotice;
import com.hkh.sys.vo.NoticeVo;

public interface SysNoticeMapper {

	// 获取所有的公告记录
	List<SysNotice> queryAllNotice(NoticeVo noticeVo);

	// 删除登录日志记录
	int deleteNoticemult(@Param("ids") List<String> ids);

	int deleteByPrimaryKey(Integer id);

	int insert(SysNotice record);

	int insertSelective(SysNotice record);

	SysNotice selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysNotice record);

	int updateByPrimaryKeyWithBLOBs(SysNotice record);

	int updateByPrimaryKey(SysNotice record);
}