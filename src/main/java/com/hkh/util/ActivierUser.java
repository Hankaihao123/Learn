package com.hkh.util;

import java.io.Serializable;
import java.util.List;

import com.hkh.sys.bean.SysUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivierUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private SysUser user;
	private List<String> roles;
	private List<String> permissions;

}
