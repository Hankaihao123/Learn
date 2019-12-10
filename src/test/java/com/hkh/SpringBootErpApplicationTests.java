package com.hkh;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;
import com.hkh.sys.bean.SysDept;
import com.hkh.sys.bean.SysPermission;
import com.hkh.sys.bean.SysUser;
import com.hkh.sys.dao.SysDeptMapper;
import com.hkh.sys.dao.SysLogInfoMapper;
import com.hkh.sys.dao.SysPermissionMapper;
import com.hkh.sys.dao.SysUserMapper;
import com.hkh.sys.service.SysLogInfoService;
import com.hkh.util.RedisService;
import com.hkh.util.ResultObj;
import com.hkh.util.TreeNode;

@SpringBootTest
class SpringBootErpApplicationTests {

	@Autowired
	SysUserMapper mapper0;
	@Autowired
	SysPermissionMapper mapper1;

	@Test
	public void testMd5() {
		SysUser userLogin = mapper0.userLogin("admin");
		String password = userLogin.getPwd();
		String salt = userLogin.getSalt();
		Integer hashIterations = 2;
		// 4.利用SimpleHash来设置md5(上面三种都可以通过这个来设置，这里举例加盐加散列次数的)
		// 第一个参数是算法名称，这里指定md5，第二个是要加密的密码，第三个参数是加盐，第四个是散列次数
		SimpleHash hash = new SimpleHash("md5", password, salt, hashIterations);
		System.out.println(hash.toString());
	}

	@Autowired
	SysLogInfoService sysLogInfoService;
	@Autowired
	SysLogInfoMapper sysLogInfoMapper;

	@Autowired
	SysDeptMapper sysDeptMapper;

	@Test
	void test2() {
		List<SysDept> queryAllDept = sysDeptMapper.queryAllDept(null);
		System.out.println(queryAllDept);
	}

	@Autowired
	RedisService redisService;

	@Test
	void test3() {
		redisService.set("username", "韩凯浩");
	}
}
