var DeptTable;
layui.extend({
    dtree: '/resource/lib/dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
  }).use([ 'jquery', 'form', 'table', 'layer','dtree' ], function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var dtree = layui.dtree;
	
	// 渲染表格
	// elem:<table id="DeptTables">
	DeptTable=table.render({
	    elem: '#DeptTables',
	    url:'/Dept/queryAllDept',
	    toolbar: '#DeptTableToolBar',
	    defaultToolbar: null,
	    page: true,
	    title: '部门信息数据表',
	    height: 'full-228', // 高度最大化减去差值
	    cols: [[
	      {field:'id', title:'ID', width:60, fixed: 'left', unresize: true},
	      {field:'pid', title:'父节点', width:80, fixed: 'left', unresize: true},
	      {field:'title', title:'部门名称', width:120, fixed: 'left', unresize: true},
	      {field:'open', title:'是否展开', width:120, fixed: 'left', unresize: true,templet:'#openswitch'},
	      {field:'remark', title:'备注', width:100, fixed: 'left', unresize: true},
	      {field:'address', title:'地址', width:100, fixed: 'left', unresize: true},
	      {field:'available', title:'可见', width:100, fixed: 'left', unresize: true,templet:function(data){
	    	  if(data.available===1){
		    		 return '<input type="checkbox" name="available" lay-filter="availableswitch" data-id='+data.id+' lay-skin="switch" lay-text="显示|不显示" value='+data.available+' checked>';
		    	  }else{
		    		 return '<input type="checkbox" name="available" lay-filter="availableswitch" data-id='+data.id+' lay-skin="switch" lay-text="显示|不显示" value='+data.available+'>';
		    	  }
		   }},
	      {field:'ordernum', title:'排序码', width:80, fixed: 'left', unresize: true},
	      {field:'createtime', title:'创建时间', width:150, fixed: 'left', unresize: true},
	      {fixed: 'right', title:'操作', toolbar: '#DeptTableLineBar',align:'center'}
	    ]],
	    done : function(res, curr, count) {
	    	if(count!=0){
	    		if(res.data.length==0){
					DeptTable.reload({
			    		page:{curr:curr-1}
					});
				}
	    	}
	    }
	});
	
	// 监听开关按钮
	form.on('switch(openswitch)', function(data){
		var id=$(data.elem).data("id");
		var params = {};
		params['id']=id;
		data.elem.checked === true ? params['open'] = 1 : params['open'] = 0;
		$.post('/Dept/updateDeptSwitch',params,function(res){
			if(res.code==0){
				DeptTableReload();
	    		layer.msg(res.msg,{icon: 6});
    		}else{
    			layer.msg(res.msg,{icon: 5});
    		}
		});
	});  
	
	// 监听开关按钮
	form.on('switch(availableswitch)', function(data){
		var id=$(data.elem).data("id");
		var params = {};
		params['id']=id;
		data.elem.checked === true ? params['available'] = 1 : params['available'] = 0;
		console.log(params);
		$.post('/Dept/updateDeptSwitch',params,function(res){
			if(res.code==0){
				DeptTableReload();
	    		layer.msg(res.msg,{icon: 6});
    		}else{
    			layer.close(index);
    			layer.msg(res.msg,{icon: 5});
    		}
		},'json');
	});  
	
	
	// 查询的表单提交,表格重新加载
	// <button lay-filter="DeptTable">
	form.on('submit(DeptTable)', function(data) {
		DeptTable.reload({
    		where:{pid:null,title:data.field.title,address:data.field.address,remark:data.field.remark},
    		page:{curr:1}
		});
		return false; // 阻止表单跳转
	});
	
	// 表格重新加载方法
	function DeptTableReload(data,pageNum){
		DeptTable.reload({
    		where:data,
    		page:{curr:pageNum}
		});
	}
	
	// 表格头部工具条监听
	// DeptTables是lay-filter='DeptTables'
	table.on('toolbar(DeptTables)', function(obj){
	  switch(obj.event){
	    case 'allrefresh':
	    	DeptTableReload(null,1);
	    break;
	    case 'add':
	    	DeptAdd(obj);
	    break;
	  };
	});
	
	
	// 表格数据单个删除操作
	function DeptTableSignDel(obj,index){
		var data = obj.data; 
		console.log(data);
		$.post('/Dept/deleteDept?id='+data.id,null,function(res){
			if (res.code == 0) {
				obj.del();
				layer.close(index);
				layer.msg(res.msg,{icon: 6});
				DeptTableReload();
				//重新加载外部左边树
	    		window.parent.left.DemoTree.reload();
	    		//重新加载下拉树
	    		deptTree.reload();
			}else{
				layer.close(index);
				layer.msg(res.msg,{icon: 5});
			}
		},'json');
	}

	
	// 监听工具条
	table.on('tool(DeptTables)', function(obj) {
		var layEvent = obj.event;
		if (layEvent === 'delete') { // 删除
			layer.confirm('确定要删除该记录嘛?', function(index) {
				DeptTableSignDel(obj, index);
			});
		} else if (layEvent === 'edit') {
			DeptMod(obj);
		}
	});
	

	// 使用layer的tips
	function tips(str,dom){
		layer.open({
		  type:4,
		  content:[str, dom],
		  tips:[3,'#FF5722'],
		  time:2000,
		  closeBtn:0,
		  shade:0
		});
	}
	
	//新增部门和修改部门相关
	var deptIndex; //弹窗id
	var Url;
	
	//渲染下拉树,千万不要有chkeck属性
	var deptTree=dtree.renderSelect({
		skin : "laySimple",
		elem : "#selectDeptTree",
		width: "100%",
		dataStyle: "layuiStyle",
		dataFormat: "list",  
		response:{message:"msg",statusCode:0},  
		method:'post',
		url:'/Dept/queryAllDeptbyTree'
	});
	
	
	//点击节点设置隐藏pid的值用于提交
	dtree.on("node('selectDeptTree')" ,function(obj){
		$('#pid').val(obj.param.nodeId);
	});
	
	
	
	//点击新增部门按钮触发的函数
	function DeptAdd(obj){
		//清空表表单,因为jquery没有该函数因此必须转换成DOM对象
		$('#DeptAddModForm')[0].reset();
		$('#pid').val('');
		deptIndex = layer.open({
			type : 1,
			title:'增加部门',
			content : $('#DeptInfo'),
			area : [ '700px', '420px' ],
			success :function(){
				$.post('/Dept/getDeptMaxnOrderNum',function(res){
					if(res.code==0){
						$('#ordernum').val(res.data);
					}else{
						layer.msg(res.msg,{icon: 5});
					}
				})
				Url='/Dept/addDept';
				
			}
		});
	}
	
	
	//修改部门信息
	function DeptMod(obj){
		$('#DeptAddModForm')[0].reset();
		deptIndex = layer.open({
			type : 1,
			title:'修改部门',
			content : $('#DeptInfo'),
			area : [ '700px', '420px' ],
			success :function(){
				//表单赋值
				form.val("DeptAddModForm",obj.data);
				console.log(obj.data);
				//初始化下拉树的值
				dtree.dataInit("selectDeptTree", obj.data.pid);
				if(obj.data.pid==0){
					dtree.selectVal("selectDeptTree","请选择"); 
				}else{
					dtree.selectVal("selectDeptTree"); 
				}
				Url='/Dept/updateDept';
			}
		});
	}
	
	//点击取消关闭按钮
	$('#deptClose').on('click',function(){
		layer.close(deptIndex);
	})
	
	
	//新增部门和修改部门的提交
	form.on('submit(deptSubmit)', function(data){
		  $.post(Url, data.field, function(res) {
			  if(res.code==0){
					layer.close(deptIndex);
		    		layer.msg(res.msg,{icon: 6});
		    		//重新加载表格
		    		DeptTableReload();
		    		//重新加载外部左边树
		    		window.parent.left.DemoTree.reload();
		    		//重新加载下拉树
		    		deptTree.reload();
	    		}else{
	    			layer.close(deptIndex);
	    			layer.msg(res.msg,{icon: 5});
	    		}
		  },'json');
		  return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});
})

// 这个方法layui方法外,也是在deptLeft.html页面中调用了这个方法同时DeptTable也是全局变量
function getDeptByParentId(id){
	DeptTable.reload({
		url:'/Dept/queryAllDept',
		where:{pid:id,title:null,address:null,remark:null},
		page:{curr:1}
	});
}