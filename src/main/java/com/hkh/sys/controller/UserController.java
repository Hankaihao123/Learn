package com.hkh.sys.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hkh.sys.bean.SysUser;
import com.hkh.sys.service.SysUserService;
import com.hkh.sys.vo.UserVo;
import com.hkh.util.ResultObj;
import com.hkh.util.WebUtils;

@RequestMapping("User")
@RestController
public class UserController {

	@Value("${project.headimageurl}")
	private String headimageurl;

	@Autowired
	SysUserService sysUserService;

	// 查询所有的用户信息
	@RequestMapping("queryAllUser")
	public ResultObj queryAllUser(UserVo userVo) {
		System.err.println("queryAllUser:" + userVo);
		return sysUserService.queryAllUser(userVo);
	}

	// 删除用户信息
	@RequestMapping("deleteUser")
	public ResultObj deleteUser(UserVo userVo) {
		System.err.println("deleteUser:" + userVo);
		return sysUserService.deleteUser(userVo);
	}

	// 新增用户
	@RequestMapping("addUser")
	public ResultObj addUser(UserVo userVo) {
		System.err.println("addUser:" + userVo);
		return sysUserService.addUser(userVo);
	}

	// 修改用户
	@RequestMapping("updateUser")
	public ResultObj updateUser(UserVo userVo) {
		System.err.println("updateUser:" + userVo);
		return sysUserService.updateUser(userVo);
	}

	// 通过用户id获取角色
	// 1:先获取所有的角色,2:根据用户id查询对应的角色
	@RequestMapping("queryRoleByuserid")
	public ResultObj queryRoleByuserid(Integer userid) {
		return sysUserService.queryRoleByuserid(userid);
	}

	// 给用户增加角色
	@RequestMapping("insertRole")
	public ResultObj insertRole(String roleid, Integer userid) {
		return sysUserService.insertRole(roleid, userid);
	}

	// 给用户移除角色
	@RequestMapping("deleteRoleByUserid")
	public ResultObj deleteRoleByUserid(String roleid, Integer userid) {
		return sysUserService.deleteRoleByUserid(roleid, userid);
	}

	// 获取个人信息
	@RequestMapping("getUserInfo")
	public ResultObj getUserInfo() {
		return sysUserService.getUserInfo();
	}

	// 上传头像
	@RequestMapping("upLoadHead")
	public ResultObj upLoadHead(@RequestParam("file") MultipartFile file) {
		SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
		System.out.println(file);
		String orfilename = file.getOriginalFilename();
		String suff = orfilename.substring(orfilename.lastIndexOf('.') + 1, orfilename.length());
		String newfilename = user.getLoginname() + "." + suff;
		File uploadfile = new File(this.headimageurl + newfilename);
		try {
			if (file.getSize() != 0) {
				file.transferTo(uploadfile);
			}
		} catch (IOException e) {
			System.out.println(e);
			return new ResultObj(1, "上传头像失败");
		}
		Map<String, Object> result = new HashMap<String, Object>();
		String headurl = "/headimage/" + newfilename;
		result.put("headimgagesrc", headurl);
		UserVo sysUser = new UserVo();
		sysUser.setId(user.getId());
		sysUser.setImgpath(headurl);
		sysUserService.updateUser(sysUser);
		user.setImgpath(headurl);
		WebUtils.getSession().setAttribute("user", user);
		return new ResultObj(0, "上传头像成功", result);
	}
}
