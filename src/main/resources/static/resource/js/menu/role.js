layui.extend({
    dtree: '/resource/lib/dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
  }).use([ 'jquery', 'form', 'table', 'layer','dtree' ], function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var dtree = layui.dtree;
	
	


	// 渲染表格
	// elem:<table id="RoleTables">
	var RoleTables=table.render({
	    elem: '#RoleTables',
	    url:'/Role/queryAllRole',
	    toolbar: '#RoleTableToolBar',
	    defaultToolbar: null,
	    page: true,
	    title: '角色信息表',
	    height: 'full-228', // 高度最大化减去差值
	    cols: [[
	      {field:'id', title:'ID', width:80, fixed: 'left', unresize: true},
	      {field:'name', title:'角色名称',	align:'center',unresize: true},
	      {field:'remark', title:'备注',	align:'center',unresize: true,hide:true},
	      {field:'available', title:'显示状态',align:'center',unresize: true  ,templet : function(data) {
				if (data.available === 1) {
					return '<input type="checkbox" name="available" lay-filter="availableswitch" data-id='+ data.id+ ' lay-skin="switch" lay-text="显示|不显示" value='+ data.available+ ' checked>';
				} else {
					return '<input type="checkbox" name="available"  lay-filter="availableswitch" data-id='+ data.id+ ' lay-skin="switch" lay-text="显示|不显示" value='+ data.available	+ '>';
				}}
	      },
	      {field:'createtime', title:'创建时间',	align:'center',unresize: true},
	      {fixed: 'right', title:'操作', toolbar: '#RoleLineToolBar',align:'center'}
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
	
	// 表格头部工具条监听
	// DeptTables是lay-filter='DeptTables'
	table.on('toolbar(RoleTables)', function(obj) {
		switch (obj.event) {
		case 'allrefresh':
			RoleTableReload(null, 1);
			break;
		case 'add':
			RoleAdd(obj);
			break;
		}
		;
	});
	
	var Url;
	var roleInfoIndex
	function RoleAdd(obj){
		roleInfoIndex=layer.open({
			type: 1,
			title:'新增角色',
			content:$('#RoleInfo'),
			//area: ['500px', '300px'],
			success:function(){
				$('#clear').click();
				Url='/Role/addRole'
			}
		})
	}
	// 点击取消关闭按钮
	$('#RoleClose').on('click', function() {
		layer.close(roleInfoIndex);
	})
	
	
	// 监听工具条
	table.on('tool(RoleTables)', function(obj) {
		var layEvent = obj.event;
		if (layEvent === 'delete') { // 删除
			layer.confirm('确定要删除该记录嘛?', function(index) {
				RoleTableSignDel(obj, index);
			});
		} else if (layEvent === 'edit') {
			RoleTableMod(obj);
		} else if(layEvent==='allot'){
			allPermission(obj);
		}
	});
	
	function RoleTableSignDel(obj, index){
		var data = obj.data;
		$.post('/Role/deleteRole?id=' + data.id,null, function(res) {
			if (res.code == 0) {
				obj.del();
				layer.close(index);
				layer.msg(res.msg, {icon : 6});
				RoleTableReload(null, 1);
			} else {
				layer.close(index);
				layer.msg(res.msg, {icon : 5});
			}
		}, 'json');
	}
	
	function RoleTableMod(obj){
		roleInfoIndex=layer.open({
			type: 1,
			title:'修改角色',
			content:$('#RoleInfo'),
			//area: ['500px', '300px'],
			success:function(){
				$('#clear').click();
				form.val("RoleAddModForm", obj.data);
				Url='/Role/updateRole'
			}
		})
	}

	
	// 监听开关按钮
	form.on('switch(availableswitch)', function(data) {
		var id = $(data.elem).data("id");
		var params = {};
		params['id'] = id;
		data.elem.checked === true ? params['available'] = 1
				: params['available'] = 0;
		$.post('/Role/updateRole', params, function(res) {
			if (res.code == 0) {
				RoleTableReload();
				layer.msg(res.msg, {
					icon : 6
				});
			} else {
				layer.close(index);
				layer.msg(res.msg, {
					icon : 5
				});
			}
		}, 'json');
	});
	
	//分配权限
	function allPermission(obj){
		var id=obj.data.id;
		layer.open({
			type: 1,
			title:'权限分配',
			content:$('#PermissionInfo'),
			area: ['500px', '300px'],
			btn: ['确认分配', '取消分配'],
			btn1 : function(index, layero) {
				var permission = dtree.getCheckbarNodesParam("PermissionTree");
				var params={id,permission};
				//console.log(JSON.stringify(params));;
				$.ajax({
					url : '/Role/setPermissionByRoleid',
					type : "post",
					data : JSON.stringify(params),
					dataType : "json",
					contentType : 'application/json;charset:utf-8;',
					success : function(res) {
						if (res.code == 0) {
							layer.close(index);
							layer.msg(res.msg, {icon : 6});
						} else {
							layer.close(index);
							layer.msg(res.msg, {icon : 5});
						}
					}
				})
			},
			btn2: function(index) {
				layer.close(index);
			},
			success:function(){
			dtree.render({
				skin: "laySimple",  // laySimple主题风格
				elem: "#PermissionTree",
				url: "/Role/getPermissionByRoleid?id="+obj.data.id,
				dataStyle: "layuiStyle",  //使用layui风格的数据格式
				dataFormat: "list",  //配置data的风格为list
				response:{message:"msg",statusCode:0},  //修改response中返回数据的定义
				checkbar:true, //开启复选框
				method:'post',
				checkbarType: "all",
				accordion: true  // 开启手风琴
			});
			}
		});
	}
	
	
	// 新增部门和修改部门的提交
	form.on('submit(RoleSubmit)', function(data) {
		$.post(Url, data.field, function(res) {
			if (res.code == 0) {
				layer.close(roleInfoIndex);
				layer.msg(res.msg, {icon : 6});
				RoleTableReload(null, 1);
			} else {
				layer.close(PermissionIndex);
				layer.msg(res.msg, {icon : 5});
			}
		}, 'json');
		return false; // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});
	
	
	// 表格重新加载方法
	function RoleTableReload(data, pageNum) {
		RoleTables.reload({
			where : data,
			page : {
				curr : pageNum
			}
		});
	}
	
  })