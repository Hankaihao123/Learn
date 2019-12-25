var dataStr;
layui.use([ 'jquery', 'form', 'table', 'laydate' ], function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	
	// 渲染表格
	var doneTaskTable = table.render({
		elem : '#DoneTaskTables',
		url : '/ExeWorkFlow/queryAllDoneTask',
		toolbar : '#DoneTaskTableToolBar',
		defaultToolbar : null,
		page : true,
		title : '办理任务表',
		height : 'full-20', // 高度最大化减去差值
		cols : [ [ {
			field : 'id',
			title : 'ID',
			width : 80,
			align : 'center',
			unresize : true
		}, {
			field : 'workFlowName',
			title : '工作流名称',
			align : 'center',
			unresize : true
		}, {
			field : 'exeWorkFlowName',
			title : '执行的名称',
			align : 'center',
			unresize : true
		}, {
			field : 'name',
			title : '执行任务名',
			align : 'center',
			unresize : true
		}, {
			field : 'assignee',
			title : '申请人',
			width : 80,
			align : 'center',
			unresize : true
		}, {
			field : 'createTime',
			title : '创建时间',
			align : 'center',
			unresize : true
		}, {
			fixed : 'right',
			title : '操作',
			align : 'center',
			toolbar : '#DoneTaskTableLineBar'
		} ] ]
	});
	
	function getattachmentInfo(id){
		// 渲染表格
		var commentTables = table.render({
			elem : '#CommentTables',
			url : '/ExeWorkFlow/getCommentInfo?id='+id,
			defaultToolbar : null,
			title : '办理任务表',
			cols : [ [ {
				field : 'time',
				title : '批注时间',
				align : 'center',
				unresize : true
			}, {
				field : 'userId',
				title : '批注人',
				align : 'center',
				unresize : true
			}, {
				field : 'message',
				title : '批注内容',
				align : 'center',
				unresize : true
			}] ]
		});
	}
	
	// 表格头部工具条监听
	table.on('toolbar(DoneTaskTables)', function(obj) {
		switch (obj.event) {
		case 'allrefresh':
			DoneTaskTableReload();
			break;
		}
		;
	});
	
	// 重新加载
	function DoneTaskTableReload() {
		doneTaskTable.reload({
			where : null,
			page : {
				curr : 1
			}
		})
		$.post('/ExeWorkFlow/getTaskCount',null,function(res){
			if(res.code==0){
				var $jq = $(window.parent.document.getElementById("navBar"));
				$jq = $jq.find('.layui-badge');
				if(res.data>0){
					$jq.text(res.data);
					if($jq.hasClass('layui-hide')){
						$jq.removeClass('layui-hide')
					}
				}
				
				$jq = $(window.parent.document.getElementById("top_tabs"));
				$jq = $jq.find('.layui-badge');
				if(res.data>0){
					$jq.text(res.data);
					if($jq.hasClass('layui-hide')){
						$jq.removeClass('layui-hide')
					}
				}
			}else{
				layer.msg("getTaskCount失败",{icon : 5});
			}
		})
		return false; // 阻止表单跳转
	}
	

	// 监听行内工具条
	table.on('tool(DoneTaskTables)', function(obj) {
		var layEvent = obj.event;
		if (layEvent === 'doneTask') {
			doneTask(obj);
		}else if (layEvent === 'view') {
			viewImg(obj);
		}
	});

	var Url;
	var donetaskindex;
	function doneTask(obj) {
		$.post('/ExeWorkFlow/getSysExeWorkFlowByTaskId?id=' + obj.data.id, null, function(res) {
			if(res.code==0){
				form.val("doneTaskFrom",res.data);
				getattachmentInfo(obj.data.id);
				$.post('/ExeWorkFlow/queryLineInfo?id=' + obj.data.id, null, function(res) {
					if (res.code == 0) {
						$('#btn1').html($('#btn1').html().replace('同意', res.data[0]));
						$('#btn2').html($('#btn2').html().replace('驳回', res.data[1]));
						donetaskindex = layer.open({
							type : 1,
							title : '办理任务',
							area: ['600px','500px' ],
							content : $('#showDoneTaskDiv'),
							zIndex : 891,
							success : function() {
								Url = '/ExeWorkFlow/completeTask';
								$('input[type=hidden]').val(obj.data.id);
							}
						});
							} else {
								layer.msg("queryLineInfo失败",{icon : 5});
							}
					})
				} else {
					layer.msg("getSysExeWorkFlowByTaskId失败",{icon : 5});
				}
				})
			}

					//完成任务页面中的查询流程图
					function viewImg(obj) {
						console.log(obj);
						$.post(	'/ExeWorkFlow/getImgActivitUser?id='+ obj.data.id,null,function(res) {
							if (res.code == 0) {
									$('img[alt="workflowimg"]')
											.attr('src',res.data.imgsrc);
									var pos = 'border: 1px solid red; position: absolute; width:'
											+ res.data.coordinate.width
											+ 'px; height:'
											+ res.data.coordinate.height
											+ 'px; left: '
											+ res.data.coordinate.x
											+ 'px; top: '
											+ res.data.coordinate.y
											+ 'px;';
									$('#imgpostion').attr('style',pos);
									layer.open({
										type : 1,
										title : '查看当前活动的流程图',
										area : [ '600px','500px' ],
										content : $('#workflowDivImg'),
									});
								} else {
									layer.msg(res.msg, {icon : 5});
								}
							})

					}

					// 同意
					form.on('submit(rightExe)', function(data) {
						data.field.tf = true;
						data.field.message = $('#btn1').text();
						$.post(Url, data.field, function(res) {
							if (res.code == 0) {
								layer.msg(res.msg, {
									icon : 6
								});
								setcount();
							} else {
								layer.msg(res.msg, {
									icon : 5
								});
							}
							layer.close(donetaskindex);
							DoneTaskTableReload();
							$('#doneTaskFrom')[0].reset();
						})
						return false;
					});

					//设置数字
					function setcount() {
						var $jq = $(window.parent.document.getElementById("navBar"));
						$jq = $jq.find('.layui-badge');
						var count = parseInt($jq.text());
						count -= 1
						if (count <= 0) {
							$jq.addClass('layui-hide');
						}
						$jq.text(count);

						$jq = $(window.parent.document.getElementById("top_tabs"));
						$jq = $jq.find('.layui-badge');
						count = parseInt($jq.text());
						count -= 1
						if (count <= 0) {
							$jq.addClass('layui-hide');
						}
						$jq.text(count);
					}
					// 放弃和驳回
					form.on('submit(errorExe)', function(data) {
						data.field.tf = false;
						data.field.message = $('#btn2').text();
						$.post(Url, data.field, function(res) {
							if (res.code == 0) {
								layer.msg(res.msg, {
									icon : 6
								});
								setcount();
							} else {
								layer.msg(res.msg, {
									icon : 5
								});
							}
							layer.close(donetaskindex);
							DoneTaskTableReload();
							$('#doneTaskFrom')[0].reset();
						})
						return false;
					});

				})