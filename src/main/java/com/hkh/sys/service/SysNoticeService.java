package com.hkh.sys.service;

import com.hkh.sys.vo.NoticeVo;
import com.hkh.util.ResultObj;

public interface SysNoticeService {
	// 获取所有的公告
	public ResultObj queryAllNotice(NoticeVo noticeVo);

	// 批量删除公告
	public ResultObj deletNoticefomult(String ids);
	
	//增加公告
	public ResultObj addNotice(NoticeVo noticeVo);
	
	//修改公告
	public ResultObj updateNotice(NoticeVo noticeVo);

}
