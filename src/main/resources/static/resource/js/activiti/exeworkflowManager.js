layui.use([ 'jquery', 'form', 'table', 'layer', 'laydate', 'upload' ],
		function() {
			var $ = layui.jquery;
			var form = layui.form;
			var table = layui.table;
			var layer = layui.layer;
			var laydate = layui.laydate;
			var upload = layui.upload;
			var starttime;

			// 日期控件渲染
			laydate.render({
				elem : '#startTime',
				type : 'date',
				format : 'yyyy-MM-dd HH:mm:ss',
				trigger : 'click',
				done : function(value, date, endDate) {
					starttime = date;
				}
			});

			// 日期控件渲染
			laydate.render({
				elem : '#endTime',
				type : 'date',
				format :'yyyy-MM-dd HH:mm:ss',
				trigger : 'click',
				done : function(value, date, endDate) {
					var tf = compareDate(starttime, date);
					if (tf) {
						tips('结束日期要大于开始日期', '#endTime')
						$("#endTime").val('');
					}
				}
			});

			// 渲染表格
			// elem:<table id="ExeWorkFlowTables">
			var ExeWorkFlowTable = table.render({
				elem : '#ExeWorkFlowTables',
				url : '/ExeWorkFlow/queryAllExeWorkFlow',
				toolbar : '#ExeWorkFlowTableToolBar',
				defaultToolbar : null,
				page : true,
				title : '流程数据表',
				height: 'full-228', //高度最大化减去差值
				cols : [ [ {
					field : 'id',
					title : 'ID',
					width : 80,
					fixed : 'left',
					unresize : true
				}, {
					field : 'title',
					title : '执行流程标题',
					align : 'center',
					width : 150,
					unresize : true
				}, {
					field : 'content',
					title : '信息内容',
					align : 'center',
					unresize : true
				}, {
					field : 'datas',
					title : '信息数据',
					width : 100,
					align : 'center',
					unresize : true
				}, {
					field : 'createtime',
					title : '创建时间',
					width : 170,
					align : 'center',
					unresize : true
				}, {
					field : 'state',
					title : '执行状态',
					align : 'center',
					width : 120,
					unresize : true,
					templet : '#statetpl'
				}, {
					fixed : 'right',
					title : '操作',
					align : 'left',
					templet : '#ExeWorkFlowTableLineBar'
				} ] ]
			});

			// 查询表单的提交
			form.on('submit(WorkFlowQuery)', function(data) {
				console.log(data);
				ExeWorkFlowTable.reload({
					where : data.field,
					page : {
						curr : 1
					}
				})
				return false; // 阻止表单跳转
			});

			// 表格头部工具条监听
			// WorkFlowTables是lay-filter='WorkFlowTables'
			table.on('toolbar(ExeWorkFlowTables)', function(obj) {
				switch (obj.event) {
				case 'allrefresh':
					ExeWorkFlowTableReload();
					break;
				case 'add':
					ExeWorkFlowAdd(obj);
					break;
				}
				;
			});

			// 重新加载
			function ExeWorkFlowTableReload() {
				ExeWorkFlowTable.reload({
					where : null,
					page : {
						curr : 1
					}
				})
				return false; // 阻止表单跳转
			}

			// 增加执行的流程
			var Url;
			var exeworkflowindex;
			function ExeWorkFlowAdd(obj) {
				$('#ExeWorkFlowFrom')[0].reset();
				exeworkflowindex = layer.open({
					type : 1,
					title : '增加执行流程',
					content : $('#ExeWorkFlowdiv'),
					zIndex : 891,
					success : function() {
						// 动态渲染select
						LoadSelectWorkFlow();
						Url = '/ExeWorkFlow/insertExeWorkFlow';
						$('#exeTaskid').val('');
					}
				});
			}

			//设置数字
			function setcount(){
				var $jq= $(window.parent.document.getElementById("navBar"));
				$jq= $jq.find('.layui-badge');
				var count=parseInt($jq.text());
				count+=1;
				if(count>0){
					$jq.removeClass("layui-hide");
				}
				$jq.text(count);
				
				$jq= $(window.parent.document.getElementById("top_tabs"));
				$jq= $jq.find('.layui-badge');
				count=parseInt($jq.text());
				count+=1;
				if(count>0){
					$jq.removeClass("layui-hide");
				}
				$jq.text(count);
			}
			
			// 监听公告的提交
			// <button lay-filter="ExeWorkFlowSubmit">
			form.on('submit(ExeWorkFlowSubmit)', function(data) {
				$.post(Url, data.field, function(res) {
					if (res.code == 0) {
						layer.close(exeworkflowindex);
						layer.msg(res.msg, {icon : 6});
						ExeWorkFlowTableReload();
					} else {
						layer.close(exeworkflowindex);
						layer.msg(res.msg, {icon : 5});
					}
				})
				return false; // 阻止表单跳转
			});

			// 点击打开的layer窗口
			$('#btnclose').on('click', function() {
				layer.close(exeworkflowindex);
			})
			
			//渲染要加载的选择框 
			function LoadSelectWorkFlow(id) {
				var $select = $('#workflow');
				$select.html('<option value="">请选择</option>');
				$.post('/WorkFlow/queryAllWorkFlow', function(res) {
					if (res.code == 0) {
						var start = $select.html();
						$.each(res.data, function(index, elem) {
							if(id!=null && id!=undefined && id==elem.id){
								var str = '<option selected value="' + elem.id + '">'
								+ elem.name + '</option>'
							}else{
								var str = '<option value="' + elem.id + '">'
								+ elem.name + '</option>'
							}
							start = start.concat(str);
						})
						$select.html(start);
						form.render('select'); 
					}
				})
			}

			// 监听行内工具条
			table.on('tool(ExeWorkFlowTables)', function(obj) {
				var layEvent = obj.event;
				if (layEvent === 'delete') { // 删除
					layer.confirm('确定要删除该记录嘛?', function(index) {
						ExeWorkFlowDel(obj,index);
					});
				} else if (layEvent === 'edit') {
					ExeWorkFlowEdit(obj,this);
				} else if (layEvent === 'commit') {
					ExeWorkFlowCommit(obj,this);
					setcount();
				} else if (layEvent === 'view') {
					WorkFlowView(obj);
				}
			});

			function ExeWorkFlowCommit(obj,btn) {
				disbtn(btn,true);
				$.post('/ExeWorkFlow/commitExeWorkFlow', obj.data,
						function(res) {
							if(res.code==0){
								layer.msg(res.msg, {icon : 6});
							}else{
								layer.msg(res.msg, {icon : 5});
							}
							ExeWorkFlowTableReload();
						})
				disbtn(btn,false);
			}
			
			function ExeWorkFlowEdit(obj,btn){
				// 动态渲染select
				LoadSelectWorkFlow(obj.data.type);
				exeworkflowindex = layer.open({
					type : 1,
					title : '编辑执行流程',
					content : $('#ExeWorkFlowdiv'),
					zIndex : 891,
					success : function() {
						form.val("ExeWorkFlowFrom",obj.data);
						Url = '/ExeWorkFlow/updateExeWorkFlow';
						console.log(obj.data);
					}
				});
			}
			
			function ExeWorkFlowDel(obj,index){
				$.post('/ExeWorkFlow/deleteExeWorkFlow?id='+obj.data.id,null,function(res) {
					if(res.code==0){
						layer.msg(res.msg, {icon : 6});
					}else{
						layer.msg(res.msg, {icon : 5});
					}
					ExeWorkFlowTableReload();
				})
			}
			
			var workflowviewindex;
			function WorkFlowView(obj){
				$.post('/ExeWorkFlow/getWorkFlowInfo?id='+obj.data.id,null,function(res){
					if(res.code==0){
						result=modTimeLine(res.data);
						workflowviewindex = layer.open({
							type : 1,
							title : '查询审批流程',
							content : result,
							zIndex : 891,
							area: ['600px', '500px'],
							success : function() {
								getattachmentInfo(obj.data.id);
							}
						});
					}else{
						layer.msg(res.msg, {icon : 5});
					}
				})
				
			}
			
			function getactiveindex(data){
				if(data.activer==='EndEvent'){
					return 10000;
				}
				for (var i = 0; i < data.keys.length; i++) {
					if(data.keys[i]===data.activer){
						return i;
					}
				}
			}
			//是否禁用true禁用false不禁用 obj:JQ对象
			function disbtn(obj,tf){
				obj=$(obj);
				if(tf==true){
					obj.attr('disabled','disabled');	
				}else{
					obj.attr('disabled','');
				}
			}
			
			//时间线
			function modTimeLine(data,str){
				var result = "";
				var bak = $('#exeworkflow').text();
				var temp = 0;
				var index = getactiveindex(data);
				for (var i = 0; i < data.keys.length; i++) {
					var str = bak;
					str = str.replace('${content}', data.values[i]);
					if (i >= index) {
						str = str.replace('${ico}', "&#xe60e;")
						str = str.replace('color:green;', "color:red;");
					} else {
						str = str.replace('${ico}', "&#x1005;")
					}
					result += str;
				}
				var start = '<div style="margin: 10px;"> <ul class="layui-timeline">';
				var end = '</ul><table id="CommentTables" lay-filter="CommentTables" ></table></div>';
				return start + result + end;
			}
			
			
			function getattachmentInfo(id){
				// 渲染表格
				var commentTables = table.render({
					elem : '#CommentTables',
					url : '/ExeWorkFlow/getCommentInfo?id=SysExeWorkFlow'+id,
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
			
			// 时间的比较
			function compareDate(date1, date2) {
				var starttime = new Date(date1.year, date1.month, date1.date);
				var endtime = new Date(date2.year, date2.month, date2.date);
				return starttime >= endtime;
			}

			// 使用layer的tips
			function tips(str, dom) {
				layer.open({
					type : 4,
					content : [ str, dom ],
					tips : [ 3, '#FF5722' ],
					time : 2000,
					closeBtn : 0,
					shade : 0
				});
			}
		})