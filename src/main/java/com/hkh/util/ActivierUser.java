package com.hkh.util;


import java.util.List;

import com.hkh.sys.bean.SysUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivierUser {
	private SysUser user;
	private List<String> roles;
	private List<String> permissions;

}
