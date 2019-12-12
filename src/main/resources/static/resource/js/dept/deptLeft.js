var DemoTree;

layui.extend({
    dtree: '/resource/lib/dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
  }).use(['dtree','layer','jquery'], function(){
    var dtree = layui.dtree;
    layer = layui.layer;
    $ = layui.jquery;
   
	// 初始化树
    DemoTree= dtree.render({
		skin : "laySimple",
		elem : "#deptTree",
		dataStyle: "layuiStyle",
		dataFormat: "list",  
		response:{message:"msg",statusCode:0},  
		method:'post',
		url:'/Dept/queryAllDeptbyTree'
	});
    
	
	//节点点击,在另个页面中查询出结果
	dtree.on("node('deptTree')" ,function(obj){
	  //这里调用了另个right页面的函数
	  window.parent.right.getDeptByParentId(obj.param.nodeId);
	});
	
	
  });