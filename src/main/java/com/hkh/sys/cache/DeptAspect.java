package com.hkh.sys.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.hkh.sys.bean.SysDept;
import com.hkh.sys.vo.DeptVo;
import com.hkh.util.FastJsonUtil;
import com.hkh.util.RedisService;
import com.hkh.util.ResultObj;

@Aspect
@Component
@EnableAspectJAutoProxy
public class DeptAspect {

	// redis 操作对象
	@Autowired
	RedisService redisService;

	// 声明切面表达式
	private static final String POINTCUT_DEPT_GET = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.getDeptById(..))";
	private static final String POINTCUT_DEPT_UPDATE = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.updateDept(..))";
	private static final String POINTCUT_DEPT_DELETE = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.deleteDept(..))";
	private static final String POINTCUT_DEPT_INSERT = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.addDept(..))";

	private static final String CACHE_DEPT_PROFIX = "dept_";

	@Around(value = POINTCUT_DEPT_INSERT)
	public Object insert(ProceedingJoinPoint joinPoint) throws Throwable {
		DeptVo deptvo = (DeptVo) joinPoint.getArgs()[0];
		Integer deptid = deptvo.getId();
		SysDept dept = new SysDept();
		BeanUtils.copyProperties(deptvo, dept);
		ResultObj resultobj = (ResultObj) joinPoint.proceed();
		if (resultobj.getCode() == 0) {
			redisService.set(CACHE_DEPT_PROFIX + deptid, FastJsonUtil.convertObjectToJSON(dept));
		}
		return resultobj;
	}

	@Around(value = POINTCUT_DEPT_GET)
	public Object get(ProceedingJoinPoint joinPoint) throws Throwable {
		ResultObj resultobj = null;
		Integer deptid = (Integer) joinPoint.getArgs()[0];
		String json = (String) redisService.get(CACHE_DEPT_PROFIX + deptid);
		if (json == null) {
			resultobj = (ResultObj) joinPoint.proceed();
			redisService.set(CACHE_DEPT_PROFIX + deptid, FastJsonUtil.convertObjectToJSON(resultobj.getData()));
		} else {
			SysDept bean = FastJsonUtil.toBean(json, SysDept.class);
			resultobj = new ResultObj(0, "查询部门成功", bean);
		}
		return resultobj;
	}

	@Around(value = POINTCUT_DEPT_UPDATE)
	public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
		ResultObj resultobj = null;
		DeptVo deptvo = (DeptVo) joinPoint.getArgs()[0];
		Integer deptid = deptvo.getId();
		resultobj = (ResultObj) joinPoint.proceed();
		if (resultobj.getCode() == 0) {
			SysDept dept = new SysDept();
			BeanUtils.copyProperties(deptvo, dept);
			redisService.set(CACHE_DEPT_PROFIX + deptid, FastJsonUtil.convertObjectToJSON(dept));
		}
		return resultobj;
	}

	@Around(value = POINTCUT_DEPT_DELETE)
	public Object delete(ProceedingJoinPoint joinPoint) throws Throwable {
		ResultObj resultobj = null;
		DeptVo deptvo = (DeptVo) joinPoint.getArgs()[0];
		Integer deptid = deptvo.getId();
		resultobj = (ResultObj) joinPoint.proceed();
		if (resultobj.getCode() == 0) {
			redisService.del(CACHE_DEPT_PROFIX + deptid);
		}
		return resultobj;
	}

}
