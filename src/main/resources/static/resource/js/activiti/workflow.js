layui.use([ 'jquery', 'form', 'table', 'layer', 'laydate', 'upload' ],
		function() {
			var $ = layui.jquery;
			var form = layui.form;
			var table = layui.table;
			var layer = layui.layer;
			var laydate = layui.laydate;
			var upload = layui.upload;

			// 日期控件渲染
			laydate.render({
				elem : '#startTime',
				type : 'date',
				format : 'yyyy年MM月dd日',
				trigger : 'click',
			});

			var uploadfile;// 将上传的文件保存到该变量中,随着表单一起提交
			var uploadInst = upload.render({
				elem : '#uploadZip',
				url : '/WorkFlow/deployWorkFlowZip/',
				size : 50,
				accept : 'file',
				exts : 'zip',
				auto : false,
				acceptMime : 'application/zip',
				choose : function(obj) {
					console.log("choose");
					obj.preview(function(index, file, result) {
						uploadfile = file;
						$('#uploadZip').text(file.name);
					});
				}
			});

			// 渲染表格
			// elem:<table id="WorkFlowTables">
			var WorkFlowTable = table.render({
				elem : '#WorkFlowTables',
				url : '/WorkFlow/queryAllWorkFlow',
				toolbar : '#WorkFlowTableToolBar',
				defaultToolbar : null,
				page : true,
				title : '流程数据表',
				height : '300', // 高度最大化减去差值
				cols : [ [ {
					field : 'id',
					title : 'ID',
					width : 80,
					fixed : 'left',
					unresize : true
				}, {
					field : 'name',
					title : '部署名称',
					align : 'center',
					unresize : true
				}, {
					field : 'deploymentTime',
					title : '部署时间',
					align : 'center',
					unresize : true
				}, {
					fixed : 'right',
					title : '操作',
					toolbar : '#WorkFlowTableLineBar',
					align : 'center'
				} ] ]
			});

			// 表格头部工具条监听
			// WorkFlowTables是lay-filter='WorkFlowTables'
			table.on('toolbar(WorkFlowTables)', function(obj) {
				switch (obj.event) {
				case 'allrefresh':
					WorkFlowTableReload();
					break;
				case 'add':
					WorkFlowAdd(obj);
					break;
				}
				;
			});

			// 查询的表单提交,表格重新加载
			// <button lay-filter="WorkFlowTable">
			form.on('submit(WorkFlowTable)', function(data) {
				WorkFlowTable.reload({
					where : data.field,
					page : {
						curr : 1
					}
				})
				return false; // 阻止表单跳转
			});

			// 表格重新加载
			function WorkFlowTableReload() {
				WorkFlowTable.reload({
					where : null,
					page : {
						curr : 1
					}
				});
			}

			var workflowindex;
			var str;
			// 流程部署增加
			function WorkFlowAdd(obj) {
				workflowindex = layer.open({
					type : 1,
					title : '部署流程',
					content : $('#addWorkFlow'),
					zIndex : 891,
					success : function() {
						console.log($('#uploadZip').text());
						console.log(str=$('#uploadZip').html());
					}
				});
			}

			// 点击新增流程部署按钮
			form.on('submit(WorkFlowInfo)', function(data) {
				var myForm = document.getElementById("addWorkFlowForm");
				var form = new FormData(myForm);
				form.append("file", uploadfile);
				$.ajax({
					url : '/WorkFlow/deployWorkFlowZip/',
					type : 'post',
					cache : false,
					processData : false,
					contentType : false,
					data : form,
					success : function(res) {
						if(res.code==0){
							layer.msg(res.msg, {icon : 6});
						}else{
							layer.msg(res.msg, {icon : 5});
						}
						WorkFlowTableReload();
						layer.close(workflowindex);
					}
				})
				return false; // 阻止表单跳转
			});

			// 关闭流程部署窗口
			$('#btnclose').on('click', function() {
				layer.close(workflowindex);
				$('#addWorkFlowForm')[0].reset();
				$('#uploadZip').html(str);
			})

			// 监听行内工具条
			table.on('tool(WorkFlowTables)', function(obj) {
				var layEvent = obj.event;
				if (layEvent === 'delete') { // 删除
					layer.confirm('确定要删除该记录嘛?', function(index) {
						WorkFlowDel(obj,index);
					});
				} else if (layEvent === 'edit') {
					console.log('edit');
				} else if (layEvent === 'down') {
					WorkFlowDown(obj);
				}
			});

			// 下载操作
			function WorkFlowDown(obj) {
				window.open(obj.data.downurl);
			}
			
			function WorkFlowDel(obj,index){
				$.post('/WorkFlow/deleteDeployWorkFlow', obj.data,function(res) {
					if(res.code==0){
						layer.msg(res.msg, {icon : 6});
					}else{
						layer.msg(res.msg, {icon : 5});
					}
					WorkFlowTableReload();
				})
			}
		});
