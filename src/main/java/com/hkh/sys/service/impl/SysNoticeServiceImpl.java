package com.hkh.sys.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hkh.sys.bean.SysNotice;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.dao.SysNoticeMapper;
import com.hkh.sys.service.SysNoticeService;
import com.hkh.sys.vo.NoticeVo;
import com.hkh.util.ResultObj;
import com.hkh.util.WebUtils;

@Service
public class SysNoticeServiceImpl implements SysNoticeService {

	@Autowired
	SysNoticeMapper sysNoticeMapper;

	@Override
	public ResultObj queryAllNotice(NoticeVo noticeVo) {
		if (noticeVo.getEndTime() == null) {
			noticeVo.setEndTime(new Date());
		}
		Page<Object> startPage = PageHelper.startPage(noticeVo.getPage(), noticeVo.getLimit());
		List<SysNotice> list = sysNoticeMapper.queryAllNotice(noticeVo);
		// layui表格返回code默认要为0
		return new ResultObj(0, "查询公告成功", startPage.getTotal(), list);
	}

	@Override
	public ResultObj deletNoticefomult(String ids) {
		String temps[] = ids.split(",");
		ResultObj obj = null;
		List<String> list = Arrays.asList(temps);
		int count = sysNoticeMapper.deleteNoticemult(list);
		if (count >= 0) {
			obj = new ResultObj(0, "删除公告成功");
		} else {
			obj = new ResultObj(1, "删除公告失败");
		}
		return obj;
	}

	@Override
	public ResultObj addNotice(NoticeVo noticeVo) {
		ResultObj obj = null;
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		SysNotice record = new SysNotice(null, noticeVo.getTitle(), new Date(), user.getName(),
				noticeVo.getEditorValue());
		int count = sysNoticeMapper.insert(record);
		if (count >= 0) {
			obj = new ResultObj(0, "新增公告成功");
		} else {
			obj = new ResultObj(1, "新增公告失败");
		}
		return obj;
	}

	@Override
	public ResultObj updateNotice(NoticeVo noticeVo) {
		ResultObj obj = null;
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		SysNotice record = new SysNotice(noticeVo.getId(), noticeVo.getTitle(), null, user.getName(),
				noticeVo.getEditorValue());
		int count = sysNoticeMapper.updateByPrimaryKeySelective(record);
		if (count >= 0) {
			obj = new ResultObj(0, "修改公告成功");
		} else {
			obj = new ResultObj(1, "修改公告失败");
		}
		return obj;
	}

}
