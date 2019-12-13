layui.use([ 'jquery', 'form', 'table', 'layer','transfer'], function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var transfer= layui.transfer;
	
	// 渲染表格
	// elem:<table id="UserTables">
	var UserTables=table.render({
	    elem: '#UserTables',
	    url:'/User/queryAllUser',
	    toolbar: '#UserTableToolBar',
	    defaultToolbar: null,
	    page: true,
	    title: '角色信息表',
	    height: 'full-228', // 高度最大化减去差值
	    cols: [[
	      {field:'id', title:'ID', width:80, fixed: 'left', unresize: true},
	      {field:'name', title:'姓名',width:100,align:'center',unresize: true},
	      {field:'address', title:'地址',width:100,align:'center',unresize: true},
	      {field:'sex', title:'性别',width:60,align:'center',unresize: true},
	      {field:'deptname', title:'部门名称',width:100,align:'center',unresize: true,},
	      {field:'hiredate', title:'生日',width:160,align:'center',unresize: true},
	      {field:'leadername', title:'上级领导',width:100,	align:'center',unresize: true},
	      {field:'available', title:'显示状态',width:100,align:'center',unresize: true,templet : function(data) {
				if (data.available === 1) {
					return '<input type="checkbox" name="available" lay-filter="availableswitch" data-id='+ data.id+ ' lay-skin="switch" lay-text="显示|不显示" value='+ data.available+ ' checked>';
				} else {
					return '<input type="checkbox" name="available"  lay-filter="availableswitch" data-id='+ data.id+ ' lay-skin="switch" lay-text="显示|不显示" value='+ data.available	+ '>';
				}}
	      },
	     {field:'ordernum', title:'排序码',width:100,align:'center',unresize: true},
	     {field:'type', title:'用户类型',	width:100,align:'center',unresize: true,templet:function(data){
	    	 if(data.type==0){
	    		 return '普通用户';
	    	 }else{
	    		 return '管理员';
	    	 }
	     }},
	      {fixed: 'right', title:'操作', toolbar: '#UserLineToolBar',align:'center'}
	    ]],
	    done : function(res, curr, count) {
			if (count != 0) {
				if (res.data.length == 0) {
					DeptTable.reload({
						page : {
							curr : curr - 1
						}
					});
				}
			}
		}
	});
	
	
	//监听行工具条
	table.on('tool(UserTables)', function(obj) {
		var layEvent = obj.event;
		if (layEvent === 'delete') { // 删除
			layer.confirm('确定要删除该记录嘛?', function(index) {
				UserTableSignDel(obj, index);
			});
		} else if (layEvent === 'edit') {
			UserTableMod(obj);
		} else if(layEvent==='allot'){
			allotRole(obj);
		}
	});
	
	//让穿梭框选中拥有的角色
	function getcheck(data){
		var arr=new Array();
		var i=0;
		$.each(data,function(index,elem){
			if(elem.tf===true){
				arr[i++]=elem.id+'';
			}
		})
		return arr;
	}
	
	// 获取批量id(1,2,3,4)
	function getAllId(data){
		var str="";
		$.each(data,function(index,obj){
			if(index==data.length-1){
				str+=data[index].value;
			}else{
				str+=data[index].value+',';
			}
		})
		return str;
	 }
	
	function transfers(data,userid){
		var arr=getcheck(data);
		transfer.render({
			elem: '#RoleTransfer',
			title:['可以拥有的角色', '拥有的角色'],
			data:data,
			value:arr,
			onchange:function(data, index){
				console.log(data);
				var str=getAllId(data);
				console.log(str);
				if(index==0){
				//右移到左->增角色
					$.post('/User/insertRole?roleid='+str+'&userid='+userid, null, function(res){
						if(res.code==0){
							layer.msg(res.msg, {icon : 6});
						}else{
							layer.close(allotindex);
							layer.msg(res.msg, {icon : 5});
						}
					}, "json");
				}else{
				//左移到右->删角色
					$.post('/User/deleteRoleByUserid?roleid='+str+'&userid='+userid, null, function(res){
						if(res.code==0){
							layer.msg(res.msg, {icon : 6});
						}else{
							layer.close(allotindex);
							layer.msg(res.msg, {icon : 5});
						}
					}, "json");
				}
			},
			parseData: function(res){
			    return {
			      "value": res.id, 
			      "title": res.name, 
			      "disabled": res.disabled,  
			      "checked": res.checked
			    }
			}
		})
	}
	
	var allotindex;
	function allotRole(obj){
		allotindex=layer.open({
			type: 1,
			title:'角色分配',
			area: ['500px', '430px'],
			content:$('#RoleInfo'),
			offset: 'auto',
			success:function(){
				$.post('/User/queryRoleByuserid?userid='+obj.data.id,null,function(res){
					if(res.code==0){
						//渲染穿梭框
						transfers(res.data,obj.data.id);
					}else{
						layer.close(allotindex);
						layer.msg(res.msg, {icon : 5});
					}
				})
				
			}
		})
	}
	
	
});