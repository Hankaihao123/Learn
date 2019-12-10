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

					//渲染当前页面的下拉树
					var MenuTree = dtree.renderSelect({
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
						url : "/Menu/queryAllMenusTree"
					});

					// 渲染当前页面的下拉树
					var MenuTree1 = dtree.renderSelect({
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
						url : "/Menu/queryAllMenusTree"
					});

					var MenuTables = table
							.render({
								elem : '#MenuTables',
								url : '/Menu/queryAllMenus',
								toolbar : '#MenuTableToolBar',
								defaultToolbar : null,
								page : true,
								title : '菜单信息',
								height : 'full-228', // 高度最大化减去差值
								cols : [ [
										{
											field : 'id',
											title : 'ID',
											width : 100,
											fixed : 'left',
											align : 'center',
											unresize : true
										},
										{
											field : 'parentId',
											title : '父id',
											width : 100,
											align : 'center',
											unresize : true
										},
										{
											field : 'icon',
											title : '菜单图标',
											width : 100,
											align : 'center',
											unresize : true,
											templet : function(data) {
												return '<font class=layui-icon>'
														+ data.icon + '</font>'
											}
										},
										{
											field : 'title',
											title : '菜单名称',
											align : 'center',
											unresize : true
										},
										{
											field : 'href',
											title : '菜单地址',
											align : 'center',
											unresize : true
										},
										{
											field : 'ordernum',
											title : '排序码',
											align : 'center',
											unresize : true,
											width:80
										},
										{
											field : 'open',
											title : '展开',
											align : 'center',
											unresize : true,
											templet : function(data) {
												if (data.open === 1) {
													return '<input type="checkbox" name="open" lay-filter="openswitch" data-id='
															+ data.id
															+ ' lay-skin="switch" lay-text="展开|不展开" value='
															+ data.open
															+ ' checked>';
												} else {
													return '<input type="checkbox" name="open" lay-filter="openswitch" data-id='
															+ data.id
															+ ' lay-skin="switch" lay-text="展开|不展开" value='
															+ data.open + '>';
												}
											}
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
											toolbar : '#MenuLineToolBar',
											align : 'center'
										} ] ],
								done : function(res, curr, count) {
									if (count != 0) {
										if (res.data.length == 0) {
											MenuTables.reload({
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
					// <button lay-filter="MenuTable">
					form.on('submit(MenuTable)', function(data) {
						MenuTables.reload({
							where : data.field,
							page : {
								curr : 1
							}
						})
						return false; // 阻止表单跳转
					});

					// 表格头部工具条监听
					// DeptTables是lay-filter='DeptTables'
					table.on('toolbar(MenuTables)', function(obj) {
						switch (obj.event) {
						case 'allrefresh':
							MenuTableReload(null, 1);
							break;
						case 'add':
							MenuAdd(obj);
							break;
						}
						;
					});

					var Url;
					var menuIndex;
					// 点击新增部门按钮触发的函数
					function MenuAdd(obj) {
						// 清空表表单,因为jquery没有该函数因此必须转换成DOM对象
						$('#MenuAddModForm')[0].reset();
						$('#newpid').val('');
						menuIndex = layer.open({
							type : 1,
							title : '增加菜单',
							content : $('#MenuInfo'),
							area : [ '700px', '500px' ],
							success : function() {
								$.post('/Menu/getMenuMaxnOrderNum', function(
										res) {
									if (res.code == 0) {
										$('#ordernum').val(res.data);
									} else {
										layer.msg(res.msg, {
											icon : 5
										});
									}
								})
								Url = '/Menu/addMenu';

							}
						});
					}

					// 表格重新加载方法
					function MenuTableReload(data, pageNum) {
						MenuTables.reload({
							where : data,
							page : {
								curr : pageNum
							}
						});
					}

					// 点击取消关闭按钮
					$('#menuClose').on('click', function() {
						layer.close(menuIndex);
					})

					// 点击取消关闭按钮
					$('#resets').on('click', function() {
						$('#MenuForm')[0].reset();
						$('#pid').val('');
					})

					// 监听工具条
					table.on('tool(MenuTables)', function(obj) {
						var layEvent = obj.event;
						if (layEvent === 'delete') { // 删除
							layer.confirm('确定要删除该记录嘛?', function(index) {
								MenuTableSignDel(obj, index);
							});
						} else if (layEvent === 'edit') {
							MenuMod(obj);
						}
					});

					// 表格数据单个删除操作
					function MenuTableSignDel(obj, index) {
						var data = obj.data;
						$.post('/Menu/deleteMenu?id=' + data.id, null,
								function(res) {
									if (res.code == 0) {
										obj.del();
										layer.close(index);
										layer.msg(res.msg, {
											icon : 6
										});
										// 重新加载表格
										MenuTableReload();
										// 重新加载下拉树
										MenuTree1.reload();
										MenuTree.reload();
									} else {
										layer.close(index);
										layer.msg(res.msg, {
											icon : 5
										});
									}
								}, 'json');
					}

					// 修改部门信息
					function MenuMod(obj) {
						$('#MenuAddModForm')[0].reset();
						menuIndex = layer.open({
							type : 1,
							title : '修改菜单',
							content : $('#MenuInfo'),
							area : [ '700px', '500px' ],
							success : function() {
								//表单赋值
								form.val("MenuAddModForm", obj.data);
								//初始化下拉树的值
								dtree.dataInit("MenuTree1", obj.data.parentId);
								$('#newpid').val(obj.data.parentId);
								if (obj.data.parentId == 0) {
									dtree.selectVal("MenuTree1", "请选择");
								} else {
									dtree.selectVal("MenuTree1");
								}
								Url = '/Menu/updateMenu';
							}
						});
					}

					//新增部门和修改部门的提交
					form.on('submit(menuSubmit)', function(data) {
						$.post(Url, data.field, function(res) {
							if (res.code == 0) {
								layer.close(menuIndex);
								layer.msg(res.msg, {
									icon : 6
								});
								//重新加载表格
								MenuTableReload();
								//重新加载下拉树
								MenuTree1.reload();
								MenuTree.reload();
							} else {
								layer.close(menuIndex);
								layer.msg(res.msg, {
									icon : 5
								});
							}
						}, 'json');
						return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
					});

					// 监听开关按钮
					form.on('switch(openswitch)', function(data) {
						var id = $(data.elem).data("id");
						var params = {};
						params['id'] = id;
						data.elem.checked === true ? params['open'] = 1
								: params['open'] = 0;
						$.post('/Menu/updateMenu', params, function(res) {
							if (res.code == 0) {
								MenuTableReload();
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
						$.post('/Menu/updateMenu', params, function(res) {
							if (res.code == 0) {
								MenuTableReload();
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