package com.hkh.sys.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.hkh.sys.bean.SysLogInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LogInfoVo extends SysLogInfo {
	// 页码
	private Integer page = 1;
	// 页的大小
	private Integer limit = 10;

	@DateTimeFormat(pattern = "yyyy年MM月dd日")
	private Date startTime;
	@DateTimeFormat(pattern = "yyyy年MM月dd日")
	private Date endTime;

	@Override
	public String toString() {
		return "LogInfoVo [page=" + page + ", limit=" + limit + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", getId()=" + getId() + ", getLoginname()=" + getLoginname() + ", getLoginip()=" + getLoginip()
				+ ", getLogintime()=" + getLogintime() + "]";
	}

}
