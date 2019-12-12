layui
		.extend({
			dtree : '/resource/lib/dtree/dtree' // {/}的意思即代表采用自有路径，即不跟随 base 路径
		})
		.use(
				[ 'dtree', 'layer', 'jquery', 'table', 'form' ],
				function() {
					var dtree = layui.dtree;
					layer = layui.layer;
					$ = layui.jquery;
					var dtree = layui.dtree;
					var table = layui.table;
					var form = layui.form;

					// 渲染当前页面的下拉树
					var PermissionTree = dtree.renderSelect({
						skin : "laySimple",
						elem : "#MenuTree",
						width : "100%",
						dataStyle : "layuiStyle",
						dataFormat : "list",
						response : {
							message : "msg",
							statusCode : 0
						},
						method : 'post',
						accordion: true,  // 开启手风琴
						url : "/Menu/queryAllMenusTree"
					});

					// 修改页面中
					var PermissionTree1 = dtree.renderSelect({
						skin : "laySimple",
						elem : "#MenuTree1",
						width : "100%",
						dataStyle : "layuiStyle",
						dataFormat : "list",
						response : {
							message : "msg",
							statusCode : 0
						},
						method : 'post',
						accordion: true,  // 开启手风琴
						url : "/Menu/queryAllMenusTree"
					});

					var PermissionTables = table
							.render({
								elem : '#PermissionTables',
								url : '/Permission/queryAllPermission',
								toolbar : '#PermissionTableToolBar',
								defaultToolbar : null,
								page : true,
								title : '菜单信息',
								height : 'full-228', // 高度最大化减去差值
								cols : [ [
										{
											field : 'id',
											title : 'ID',
											fixed : 'left',
											align : 'center',
											unresize : true
										},
										{
											field : 'parentId',
											title : 'Pid',
											align : 'center',
											unresize : true
										},
										{
											field : 'percode',
											title : '权限编码',
											align : 'center',
											unresize : true,
										},
										{
											field : 'ordernum',
											title : '排序码',
											align : 'center',
											unresize : true
										},
										{
											field : 'available',
											title : '显示',
											align : 'center',
											unresize : true,
											templet : function(data) {
												if (data.available === 1) {
													return '<input type="checkbox" name="available" lay-filter="availableswitch" data-id='
															+ data.id
															+ ' lay-skin="switch" lay-text="显示|不显示" value='
															+ data.available
															+ ' checked>';
												} else {
													return '<input type="checkbox" name="available"  lay-filter="availableswitch" data-id='
															+ data.id
															+ ' lay-skin="switch" lay-text="显示|不显示" value='
															+ data.available
															+ '>';
												}
											}
										}, {
											fixed : 'right',
											title : '操作',
											toolbar : '#PermissionLineToolBar',
											align : 'center'
										} ] ],
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

					dtree.on("node('MenuTree')", function(obj) {
						$('#pid').val(obj.param.nodeId);
					});

					dtree.on("node('MenuTree1')", function(obj) {
						$('#newpid').val(obj.param.nodeId);
					});

					// 查询菜单
					// <button lay-filter="PermissionTable">
					form.on('submit(PermissionTable)', function(data) {
						PermissionTables.reload({
							where : data.field,
							page : {
								curr : 1
							}
						})
						return false; // 阻止表单跳转
					});

					// 表格头部工具条监听
					// DeptTables是lay-filter='DeptTables'
					table.on('toolbar(PermissionTables)', function(obj) {
						switch (obj.event) {
						case 'allrefresh':
							PermissionTableReload(null, 1);
							break;
						case 'add':
							PermissionAdd(obj);
							break;
						}
						;
					});

					var Url;
					var PermissionIndex;
					// 点击新增部门按钮触发的函数
					function PermissionAdd(obj) {
						// 清空表表单,因为jquery没有该函数因此必须转换成DOM对象
						$('#PermissionAddModForm')[0].reset();
						$('#newpid').val('');
						PermissionIndex = layer.open({
							type : 1,
							title : '增加菜单',
							content : $('#PermissionInfo'),
							success : function() {
								$.post('/Permission/getPermissionMaxnOrderNum', function(
										res) {
									if (res.code == 0) {
										$('#ordernum').val(res.data);
									} else {
										layer.msg(res.msg, {
											icon : 5
										});
									}
								})
								Url = '/Permission/addPermission';

							}
						});
					}

					// 表格重新加载方法
					function PermissionTableReload(data, pageNum) {
						PermissionTables.reload({
							where : data,
							page : {
								curr : pageNum
							}
						});
					}

					// 点击取消关闭按钮
					$('#PermissionClose').on('click', function() {
						layer.close(PermissionIndex);
					})

					// 点击取消关闭按钮
					$('#resets').on('click', function() {
						$('#PermissionForm')[0].reset();
						$('#pid').val('');
					})

					// 监听工具条
					table.on('tool(PermissionTables)', function(obj) {
						var layEvent = obj.event;
						if (layEvent === 'delete') { // 删除
							layer.confirm('确定要删除该记录嘛?', function(index) {
								PermissionTableSignDel(obj, index);
							});
						} else if (layEvent === 'edit') {
							PermissionMod(obj);
						}
					});

					// 表格数据单个删除操作
					function PermissionTableSignDel(obj, index) {
						var data = obj.data;
						$.post('/Permission/deletePermission?id=' + data.id,
								null, function(res) {
									if (res.code == 0) {
										obj.del();
										layer.close(index);
										layer.msg(res.msg, {
											icon : 6
										});
										// 重新加载表格
										PermissionTableReload();
										// 重新加载下拉树
										PermissionTree1.reload();
										PermissionTree.reload();
									} else {
										layer.close(index);
										layer.msg(res.msg, {
											icon : 5
										});
									}
								}, 'json');
					}

					// 修改部门信息
					function PermissionMod(obj) {
						$('#PermissionAddModForm')[0].reset();
						PermissionIndex = layer.open({
							type : 1,
							title : '修改菜单',
							content : $('#PermissionInfo'),
							area : [ '700px', '500px' ],
							success : function() {
								// 表单赋值
								form.val("PermissionAddModForm", obj.data);
								// 初始化下拉树的值
								dtree.dataInit("PermissionTree1",
										obj.data.parentId);
								$('#newpid').val(obj.data.parentId);
								if (obj.data.parentId == 0) {
									dtree.selectVal("PermissionTree1", "请选择");
								} else {
									dtree.selectVal("PermissionTree1");
								}
								Url = '/Permission/updatePermission';
							}
						});
					}

					// 新增部门和修改部门的提交
					form.on('submit(PermissionSubmit)', function(data) {
						$.post(Url, data.field, function(res) {
							if (res.code == 0) {
								layer.close(PermissionIndex);
								layer.msg(res.msg, {
									icon : 6
								});
								// 重新加载表格
								PermissionTableReload();
								// 重新加载下拉树
								PermissionTree1.reload();
								PermissionTree.reload();
							} else {
								layer.close(PermissionIndex);
								layer.msg(res.msg, {
									icon : 5
								});
							}
						}, 'json');
						return false; // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
					});

					// 监听开关按钮
					form.on('switch(openswitch)', function(data) {
						var id = $(data.elem).data("id");
						var params = {};
						params['id'] = id;
						data.elem.checked === true ? params['open'] = 1
								: params['open'] = 0;
						$.post('/Permission/updatePermission', params,
								function(res) {
									if (res.code == 0) {
										PermissionTableReload();
										layer.msg(res.msg, {
											icon : 6
										});
									} else {
										layer.msg(res.msg, {
											icon : 5
										});
									}
								});
					});

					// 监听开关按钮
					form.on('switch(availableswitch)', function(data) {
						var id = $(data.elem).data("id");
						var params = {};
						params['id'] = id;
						data.elem.checked === true ? params['available'] = 1
								: params['available'] = 0;
						$.post('/Permission/updatePermission', params,
								function(res) {
									if (res.code == 0) {
										PermissionTableReload();
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

				})