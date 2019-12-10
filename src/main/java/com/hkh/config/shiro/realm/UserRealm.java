package com.hkh.config.shiro.realm;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.hkh.sys.bean.SysPermission;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.dao.SysPermissionMapper;
import com.hkh.sys.dao.SysUserMapper;
import com.hkh.util.ActivierUser;
import com.hkh.util.ResultObj;

public class UserRealm extends AuthorizingRealm {

	@Autowired
	SysUserMapper sysUserMapper;

	@Autowired
	SysPermissionMapper sysPermissionMapper;

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	// 认证方法
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		SysUser user = sysUserMapper.userLogin(username);
		if (null != user) {
			// 查询角色
			List<String> roles = null;
			// 查询权限
			List<SysPermission> queryMenuAndPermission = sysPermissionMapper.queryMenuAndPermission(user.getId(),
					ResultObj.TYPE_PERMISSION);
			List<String> permissions = new ArrayList<String>();
			for (SysPermission sysPermission : queryMenuAndPermission) {
				String percode = sysPermission.getPercode();
				permissions.add(percode);
			}
			// 构造ActiverUser
			ActivierUser activierUser = new ActivierUser(user, roles, permissions);
			// 创建盐
			ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activierUser, user.getPwd(), credentialsSalt,
					this.getName());
			return info;
		} else {
			return null;
		}

	}

	// 授权方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ActivierUser activierUser = (ActivierUser) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if (activierUser.getUser().getType() == ResultObj.USER_HEIGHT) {
			info.addStringPermission("*:*");
		}
		List<String> permissions = activierUser.getPermissions();
		if (null != permissions && permissions.size() > 0) {
			info.addStringPermissions(permissions);
		}
		return info;
	}

}
