layui.use([ 'jquery', 'form', 'table', 'layer' ], function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;

	// 渲染表格
	// elem:<table id="TaskTables">
	var TaskTable = table.render({
		elem : '#TaskTables',
		url : '/Task/queryAllTasks',
		toolbar : '#TaskTableToolBar',
		defaultToolbar : null,
		page : true,
		title : '任务信息数据表',
		height : 'full-228', // 高度最大化减去差值
		cols : [ [ {
			field : 'id',
			title : 'ID',
			width : 80,
			fixed : 'left',
			unresize : true
		}, {
			field : 'jobName',
			title : '任务名称',
			width : 120,
			align : 'center',
			unresize : true
		}, {
			field : 'cronExpression',
			title : 'Cron表达式',
			align : 'center',
			unresize : true,
			hide : true
		}, {
			field : 'beanClass',
			title : '全类名',
			align : 'center',
			unresize : true
		}, {
			field : 'jobStatus',
			title : '任务状态',
			align : 'center',
			unresize : true,
			templet: function(param){
				if( param.jobStatus==0){
					return "<font color='red'>未启动</font>"
				}else if(param.jobStatus==1){
					 return '<input type="checkbox" lay-skin="switch" lay-filter="jobStatus" data-id='+param.id+' lay-text="恢复|暂停" checked="">'
				}else{
					 return '<input type="checkbox" lay-skin="switch" lay-filter="jobStatus" data-id='+param.id+' lay-text="恢复|暂停">'
				}
				
			}
		}, {
			field : 'jobGroup',
			title : '任务组名',
			align : 'center',
			unresize : true
		}, {
			fixed : 'right',
			title : '操作',
			align : 'center',
			templet: '#TaskTableLineBar'
		} ] ],
		done : function(res, curr, count) {
			if (count != 0) {
				if (res.data.length == 0) {
					TaskTables.reload({
						page : {
							curr : curr - 1
						}
					});
				}
			}
		}
	});

	//删除任务
	function TaskTableSignDel(obj, index) {
		console.log(obj, index);
		var data = obj.data;
		$.post('/Task/deleteTask', data,function(res) {
					if (res.code == 0) {
						obj.del();
						layer.close(index);
						layer.msg(res.msg, {icon : 6});
						TaskTableReload();
					} else {
						layer.close(index);
						layer.msg(res.msg, {icon : 5});
					}
				}, 'json');
	}

	//修改任务
	function TaskTableMod(obj) {
		$.post('/Task/isTaskExe',obj.data,function(res){
			if(res.code==-1){
				layer.msg(res.msg, {icon : 5});
				return;
			}else{
				taskIndex = layer.open({
					type : 1,
					title:'修改任务',
					content : $('#TaskModForm'),
					zIndex : 891,
					success :function(){
						Url='/Task/updateTask';
						//表单赋值
						form.val("TaskInfoForm", obj.data);
						$('#beanClass').attr('disabled','');
					}
				});
			}
		})
	}
	
	//执行任务
	function TaskTableExec(obj,btn){
		$.post('/Task/startTask', obj.data, function(res) {
			if(res.code==0){
				if(btn.text() === '开始执行'){
					btn.html('<i class="layui-icon layui-icon-pause"></i>暂停执行');
					layer.msg(res.msg, {icon : 6});
					TaskTableReload();
				}
			}else{
				layer.msg(res.msg, {icon : 5});
				TaskTableReload();
			}
		})
	}
	
	//停止任务
	function TaskTableStop(obj,btn){
		$.post('/Task/stopTask', obj.data, function(res) {
			console.log(res);
			if(res.code==0){
				if(btn.text() === '停止执行'){
					btn.html('<i class="layui-icon layui-icon-play"></i>开始执行');
					layer.msg(res.msg, {icon : 6});
					TaskTableReload();
				}
			}else{
				layer.msg(res.msg, {icon : 5});
				TaskTableReload();
			}
		})
	}
	
	// 监听工具条
	table.on('tool(TaskTables)', function(obj) {
		var layEvent = obj.event;
		if (layEvent === 'delete') { // 删除
			layer.confirm('确定要删除该记录嘛?', function(index) {
				TaskTableSignDel(obj, index);
			});
		} else if (layEvent === 'edit') {
			TaskTableMod(obj);
		} else if (layEvent === 'exec') {
			var btn=$(this);
			TaskTableExec(obj,btn);
		}else if (layEvent === 'stop') {
			var btn=$(this);
			TaskTableStop(obj,btn);
		}
	})

	
	
	//监听暂停和恢复
	form.on('switch(jobStatus)', function(data){
		var url;
		var id = $(data.elem).data("id");
		if(data.elem.checked==true){
			url="resumeTask";
		}else{
			url="pauseTask";
		}
		$.post('/Task/'+url+'?id='+id,null, function(res) {
			console.log(res);
			if(res.code==0){
				layer.msg(res.msg, {icon : 6});
				TaskTableReload();
			}else{
				layer.msg(res.msg, {icon : 5});
				TaskTableReload();
			}
		})
	});
	
	$('#btnclose').on('click',function(){
		layer.close(taskIndex);
	})
	// 表格重新加载方法
	function TaskTableReload(data, pageNum) {
		TaskTable.reload({
			where : data,
			page : {
				curr : pageNum
			}
		});
	}
	
	var Url;
	var taskIndex;
	function TaskAdd(){
		// 清空表表单,因为jquery没有该函数因此必须转换成DOM对象
		$('#TaskInfoForm')[0].reset();
		taskIndex = layer.open({
			type : 1,
			title : '增加任务',
			content : $('#TaskModForm'),
			success : function() {
				Url = '/Task/addTask';
				$('#beanClass').removeAttr('disabled');
			}
		});
	}
	
	// 增加任务修改任务的事件监听
	form.on('submit(TaskInfo)', function(data) {
		$.post(Url, data.field, function(res) {
			if(res.code==0){
				layer.msg(res.msg, {icon : 6});
				$(this).removeClass("layui-hide");
				$(this).next().removeClass("layui-hide");
				layer.close(taskIndex);
				TaskTableReload();
			}else{
				layer.msg(res.msg, {icon : 5});
				TaskTableReload();
			}
		})
		return false;
	});
	
	table.on('toolbar(TaskTables)', function(obj){
	  var checkStatus = table.checkStatus(obj.config.id);
	  switch(obj.event){
	    case 'add':
	      TaskAdd()
	    break;
	    case 'allrefresh':
	    	TaskTableReload();
	    break;
	  };
	});
	
})