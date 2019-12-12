package com.hkh.sys.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkh.sys.service.SysNoticeService;
import com.hkh.sys.vo.NoticeVo;
import com.hkh.util.ResultObj;

@RequestMapping("Notice")
@RestController
public class NoticeController {
	@Autowired
	SysNoticeService sysNoticeService;

	// 查询所有的公告信息
	@RequestMapping("queryAllNotice")
	public ResultObj queryAllNotice(NoticeVo noticeVo) {
		System.err.println(noticeVo);
		return sysNoticeService.queryAllNotice(noticeVo);
	}

	// 批量删除公告信息
	@RequiresPermissions(value = { "notice:delete", "notice:batchdelete" }, logical = Logical.OR)
	@RequestMapping("deleteNotice")
	public ResultObj deleteNotice(String ids) {
		System.err.println(ids);
		return sysNoticeService.deletNoticefomult(ids);
	}

	// 新增公告
	@RequiresPermissions("notice:create")
	@RequestMapping("addNotice")
	public ResultObj addNotice(NoticeVo noticeVo) {
		System.err.println(noticeVo);
		return sysNoticeService.addNotice(noticeVo);
	}

	// 修改公告
	@RequiresPermissions("notice:update")
	@RequestMapping("updateNotice")
	public ResultObj updateNotice(NoticeVo noticeVo) {
		System.err.println(noticeVo);
		return sysNoticeService.updateNotice(noticeVo);
	}

}
