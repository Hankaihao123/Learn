package com.hkh.sys.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hkh.sys.bean.SysDept;
import com.hkh.sys.vo.DeptVo;
import com.hkh.util.FastJsonUtil;
import com.hkh.util.RedisService;
import com.hkh.util.ResultObj;
import com.hkh.util.TreeNode;

//@Aspect
//@Component
//@EnableAspectJAutoProxy
@SuppressWarnings("unchecked")
public class DeptAspect1 {

	// redis 操作对象
	@Autowired
	RedisService redisService;

	// 声明切面表达式
	private static final String POINTCUT_DEPT_QUERYDEPTTREE = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.queryAllDeptbyTree(..))";
	private static final String POINTCUT_DEPT_QUERYALL = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.queryAllDept(..))";
	private static final String POINTCUT_DEPT_UPDATE = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.updateDept(..))";
	private static final String POINTCUT_DEPT_UPDATE_SWITCH = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.updateDeptSwitch(..))";
	private static final String POINTCUT_DEPT_DELETE = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.deleteDept(..))";
	private static final String POINTCUT_DEPT_INSERT = "execution(* com.hkh.sys.service.impl.SysDeptServiceImpl.addDept(..))";

	private static final String CACHE_DEPT_PROFIX = "dept_";

	// 将redsi数据库中的json字符串转换成对象
	public static List<SysDept> getData(List<Object> lGet) {
		List<SysDept> data = new ArrayList<SysDept>();
		for (Object object : lGet) {
			String json = (String) object;
			SysDept e = FastJsonUtil.toBean(json, SysDept.class);
			data.add(e);
		}
		return data;
	}

	// 查询从redis中获取所有的树部门信息
	@Around(value = POINTCUT_DEPT_QUERYDEPTTREE)
	public Object cacheQueryDeptTree(ProceedingJoinPoint joinPoint) throws Throwable {
		// Object args = joinPoint.getArgs()[0];
		String json = (String) redisService.get(CACHE_DEPT_PROFIX + "treedept");
		if (json == null) {
			System.out.println("查询了mysql数据库");
			ResultObj resultobj = (ResultObj) joinPoint.proceed();
			List<TreeNode> data = (List<TreeNode>) resultobj.getData();
			redisService.set(CACHE_DEPT_PROFIX + "treedept", FastJsonUtil.convertObjectToJSON(data));
			return resultobj;
		} else {
			System.err.println("查询了redis数据库");
			List<TreeNode> data = FastJsonUtil.toList(json, TreeNode.class);
			return new ResultObj(0, "查询树形部门成功", data);
		}
	}

	// 从redis数据库中获取所有的部门信息
	@Around(value = POINTCUT_DEPT_QUERYALL)
	public Object cacheQueryDeptAll(ProceedingJoinPoint joinPoint) throws Throwable {
		DeptVo args = (DeptVo) joinPoint.getArgs()[0];
		boolean hasKey = redisService.hasKey(CACHE_DEPT_PROFIX + "AllInfo");
		if (hasKey == false) {
			ResultObj resultObj = (ResultObj) joinPoint.proceed();
			List<SysDept> data = (List<SysDept>) resultObj.getData();
			for (SysDept sysDept : data) {
				redisService.lrSet(CACHE_DEPT_PROFIX + "AllInfo", FastJsonUtil.convertObjectToJSON(sysDept));
			}
			return resultObj;
		} else {
			List<Object> lGet = redisService.lGet(CACHE_DEPT_PROFIX + "AllInfo", (args.getPage() - 1) * args.getLimit(),
					args.getPage() * args.getLimit());
			List<SysDept> data = getData(lGet);
			return new ResultObj(0, "查询部门信息成功", Long.valueOf(redisService.lGetListSize(CACHE_DEPT_PROFIX + "AllInfo")),
					data);
		}
	}

	// 修改时同时修改redis数据库中的对应的数据值
	@Around(value = POINTCUT_DEPT_UPDATE)
	public Object cacheUpdateDept(ProceedingJoinPoint joinPoint) throws Throwable {
		DeptVo args = (DeptVo) joinPoint.getArgs()[0];
		ResultObj resultobj = (ResultObj) joinPoint.proceed();
		if (resultobj.getCode() == 0) {
			List<Object> lGet = redisService.lGet(CACHE_DEPT_PROFIX + "AllInfo", 0, -1);
			List<SysDept> data1 = getData(lGet);
			String json = (String) redisService.get(CACHE_DEPT_PROFIX + "treedept");
			List<TreeNode> data2 = FastJsonUtil.toList(json, TreeNode.class);
			int index = 0;
			for (SysDept sysDept : data1) {
				if (sysDept.getId() == args.getId()) {
					Date date = sysDept.getCreatetime();
					BeanUtils.copyProperties(args, sysDept);
					sysDept.setCreatetime(date);
					redisService.lUpdateIndex(CACHE_DEPT_PROFIX + "AllInfo", index,
							FastJsonUtil.convertObjectToJSON(sysDept));
					for (TreeNode treeNode : data2) {
						if (treeNode.getId() == args.getId()) {
							treeNode.setPid(sysDept.getPid());
							treeNode.setSpread(sysDept.getOpen() == 0 ? false : true);
							treeNode.setTitle(sysDept.getTitle());
							redisService.set(CACHE_DEPT_PROFIX + "treedept", FastJsonUtil.convertObjectToJSON(data2));
						}
					}
					break;
				}
				index++;
			}
		}
		return resultobj;
	}

	// 点击开关时修改redis中的数据
	@Around(value = POINTCUT_DEPT_UPDATE_SWITCH)
	public Object cacheUpdateSwitch(ProceedingJoinPoint joinPoint) throws Throwable {
		DeptVo args = (DeptVo) joinPoint.getArgs()[0];
		ResultObj resultobj = (ResultObj) joinPoint.proceed();
		if (resultobj.getCode() == 0) {
			List<Object> lGet = redisService.lGet(CACHE_DEPT_PROFIX + "AllInfo", 0, -1);
			List<SysDept> data1 = getData(lGet);
			String json = (String) redisService.get(CACHE_DEPT_PROFIX + "treedept");
			List<TreeNode> data2 = FastJsonUtil.toList(json, TreeNode.class);
			// redis下标从0开始
			int index = 0;
			for (SysDept sysDept : data1) {
				if (sysDept.getId() == args.getId()) {
					if (args.getOpen() == null) {
						sysDept.setAvailable(args.getAvailable());
					} else {
						for (TreeNode treeNode : data2) {
							if (treeNode.getId() == args.getId()) {
								treeNode.setSpread(args.getOpen() > 0 ? true : false);
								// 当前String类型,可以直接将原先替换掉
								redisService.set(CACHE_DEPT_PROFIX + "treedept",
										FastJsonUtil.convertObjectToJSON(data2));
								break;
							}
						}
						sysDept.setOpen(args.getOpen());
					}
					// 当前redis的类型list类型,通过索引数据进行修改
					redisService.lUpdateIndex(CACHE_DEPT_PROFIX + "AllInfo", index,
							FastJsonUtil.convertObjectToJSON(sysDept));
					break;
				}
				index++;
			}
		}
		return resultobj;
	}

	// 删除reids中的数据
	@Around(value = POINTCUT_DEPT_DELETE)
	public Object cacheDeleteDept(ProceedingJoinPoint joinPoint) throws Throwable {
		DeptVo args = (DeptVo) joinPoint.getArgs()[0];
		ResultObj resultobj = (ResultObj) joinPoint.proceed();
		if (resultobj.getCode() == 0) {
			List<Object> lGet = redisService.lGet(CACHE_DEPT_PROFIX + "AllInfo", 0, -1);
			List<SysDept> data1 = getData(lGet);
			String json = (String) redisService.get(CACHE_DEPT_PROFIX + "treedept");
			List<TreeNode> data2 = FastJsonUtil.toList(json, TreeNode.class);
			for (SysDept sysDept : data1) {
				if (sysDept.getId() == args.getId()) {
					// 从Redis中的list类型删除数据
					redisService.lRemove(CACHE_DEPT_PROFIX + "AllInfo", 1, FastJsonUtil.convertObjectToJSON(sysDept));
					for (TreeNode treeNode : data2) {
						if (treeNode.getId() == args.getId()) {
							data2.remove(treeNode);
							// Redis的String类型直接替换
							redisService.set(CACHE_DEPT_PROFIX + "treedept", FastJsonUtil.convertObjectToJSON(data2));
							break;
						}
					}
					break;
				}
			}
		}
		return resultobj;
	}

	// 当前数据插入进对redis中的数据进行时时更新
	@Around(value = POINTCUT_DEPT_INSERT)
	public Object cacheInsertDept(ProceedingJoinPoint joinPoint) throws Throwable {
		//获取参数信息
		DeptVo deptVo = (DeptVo) joinPoint.getArgs()[0];
		deptVo.setCreatetime(new Date());
		//执行方法,默认也是会将参数传递过去的
		ResultObj resultobj = (ResultObj) joinPoint.proceed();
		if (resultobj.getCode() == 0) {
			SysDept data = new SysDept();
			BeanUtils.copyProperties(deptVo, data);
			redisService.lrSet(CACHE_DEPT_PROFIX + "AllInfo", FastJsonUtil.convertObjectToJSON(data));
			String json = (String) redisService.get(CACHE_DEPT_PROFIX + "treedept");
			List<TreeNode> data2 = FastJsonUtil.toList(json, TreeNode.class);
			data2.add(new TreeNode(data.getId(), data.getPid(), data.getTitle(), data.getOpen() == 0 ? false : true));
			redisService.set(CACHE_DEPT_PROFIX + "treedept", FastJsonUtil.convertObjectToJSON(data2));
		}
		return resultobj;
	}
}
