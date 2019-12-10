package com.hkh.sys.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.hkh.sys.bean.SysNotice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NoticeVo extends SysNotice {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@DateTimeFormat(pattern = "yyyy年MM月dd日")
	private Date startTime;
	@DateTimeFormat(pattern = "yyyy年MM月dd日")
	private Date endTime;

	// 接收ueditod的内容
	private String editorValue;

	@Override
	public String toString() {
		return "NoticeVo [page=" + page + ", limit=" + limit + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", editorValue=" + editorValue + ", getId()=" + getId() + ", getTitle()=" + getTitle()
				+ ", getCreatetime()=" + getCreatetime() + ", getOpername()=" + getOpername() + ", getContent()="
				+ getContent() + "]";
	}

}
