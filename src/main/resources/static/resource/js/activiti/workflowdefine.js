layui.use([ 'jquery', 'form', 'table', 'layer', 'upload' ],function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	
	// 渲染表格
	// elem:<table id="WorkFlowTables">
	var WorkFlowDefineTable = table.render({
		elem : '#WorkFlowDefineTables',
		url : '/WorkFlow/queryAllWorkflowDefine',
		toolbar : '#WorkFlowDefineTableToolBar',
		defaultToolbar : null,
		page : true,
		title : '流程定义数据表',
		height : '300', // 高度最大化减去差值
		cols : [ [ {
			field : 'id',
			title : '流程定义ID',
			fixed : 'left',
			unresize : true
		}, {
			field : 'key',
			title : '流程key',
			align : 'center',
			unresize : true
		}, {
			field : 'deploymentId',
			title : '流程部署id',
			align : 'center',
			unresize : true
		},{
			fixed : 'right',
			title : '操作',
			toolbar : '#WorkFlowDefineTableLineBar',
			align : 'center'
		} ] ]
	});
	
	// 表格重新加载
	function WorkFlowDefineTableReload() {
		WorkFlowDefineTable.reload({
			where : null,
			page : {
				curr : 1
			}
		});
	}
	
	// 表格头部工具条监听
	// WorkFlowTables是lay-filter='WorkFlowTables'
	table.on('toolbar(WorkFlowDefineTables)', function(obj) {
		switch (obj.event) {
		case 'allrefresh':
			WorkFlowDefineTableReload();
			break;
		}
	});
	
	// 监听行内工具条
	table.on('tool(WorkFlowDefineTables)', function(obj) {
		var layEvent = obj.event;
		if (layEvent === 'delete') { // 删除
			layer.confirm('确定要删除该记录嘛?', function(index) {
				console.log("delete", index);
			});
		} else if (layEvent === 'show') {
			showWorkFlowImage(obj);
		} else if (layEvent === 'down') {
			WorkFlowDown(obj);
		}
	});
	
	function showWorkFlowImage(obj){
		console.log(obj.data);
		$.post('/WorkFlow/viewWorkFlowImage?deploymentId='+obj.data.deploymentId,null,function(res){
			if(res.code==0){
				layer.open({
					type:1,
					title:'流程图',
					maxmin: true,
					area: ['500px', '500px'],
					content:'<img src="'+res.data+'"></img>'
				});
			}else{
				
			}
		},'json')
		
	}
})