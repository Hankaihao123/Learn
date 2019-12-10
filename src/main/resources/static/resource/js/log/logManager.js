layui.use([ 'jquery', 'form', 'table', 'layer', 'laydate' ], function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var laydate = layui.laydate;
	var starttime;
	
	//日期控件渲染
	laydate.render({
		elem : '#startTime',
		type : 'date',
		format : 'yyyy年MM月dd日',
		trigger : 'click',
		done: function(value, date, endDate){
			starttime=date;
		}
	});

	
	//结束时间
	laydate.render({
		elem : '#endTime',
		type : 'date',
		format : 'yyyy年MM月dd日',
		trigger : 'click',
		done: function(value, date, endDate){
			var  tf=compareDate(starttime, date);
			if(tf){
				tips('结束日期要大于开始日期','#endTime')
				$("#endTime").val('');
			}
		}
	});
	
	
	//渲染表格
	var logTables=table.render({
	    elem: '#logTables',
	    url:'/LogInfo/queryAllLog',
	    toolbar: '#logTableToolBar',
	    defaultToolbar: null,
	    page: true,
	    title: '登录日志数据表',
	    height: 'full-228', //高度最大化减去差值
	    cols: [[
	      {type: 'checkbox', fixed: 'left'},
	      {field:'id', title:'ID', width:80, fixed: 'left', unresize: true},
	      {field:'loginname', title:'用户名',	align:'center',unresize: true},
	      {field:'loginip', title:'登录地址',	align:'center',unresize: true},
	      {field:'logintime', title:'登录时间',align:'center',unresize: true},
	      {fixed: 'right', title:'操作', toolbar: '#logTableLineBar',align:'center'}
	    ]]
	});
	
	
	//表单验证
	form.verify({
		loginname: [
			/^[a-zA-Z0-9_-]{6,16}$/,
			'请输入正确的用户名'
		],
		loginip: [
			/((25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3}(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)/,
			'请输入正确的IP地址'
		],
		startTime:[
			/^(^(\d{4}|\d{2})(\-|\/|\.)\d{1,2}\3\d{1,2}$)|(^\d{4}年\d{1,2}月\d{1,2}日$)$/,
			'请输入正确的开始日期'
		]
	});      
	
	
	//查询的表单提交,表格重新加载
	form.on('submit(logTable)', function(data) {
		logTables.reload({
			where:data.field,
			page:{curr:1}
		})
		return false; //阻止表单跳转
	});
	
	//表格重新加载
	function logTableReload(){
		logTables.reload({
    		where:null,
			page:{curr:1}
		});
	}
	
	//表格数据批量删除
	function logTableDelete(obj){
		var checkStatus = table.checkStatus(obj.config.id);
		if(checkStatus.data.length>0){
			var ids=getAllId(checkStatus.data);
			layer.confirm('确定要删除这些记录嘛?', function(index) {
				$.post('/LogInfo/deleteLogInfo?ids='+ids,null,function(res){
		    		if(res.code==0){
		    			logTables.reload({
				    		where:null,
							page:{curr:1}
						});
		    			layer.close(index);
			    		layer.msg(res.msg,{icon: 6});
		    		}else{
		    			layer.close(index);
		    			layer.msg(res.msg,{icon: 5});
		    		}
		    	})
			});
		}else{
			tips('您没选择要删除的id','#alldelete');
		}
	}
	
	//表格头部工具条监听 
	//logTable是lay-filter='logTable'
	table.on('toolbar(logTable)', function(obj){
	  switch(obj.event){
	    case 'allrefresh':
	    	logTableReload();
	    break;
	    case 'alldelete':
    		logTableDelete(obj);
	    break;
	  };
	});
	
	
	//表格数据单个删除操作
	function logTableSignDel(obj,index){
		var data = obj.data; 
		console.log(data);
		$.post('/LogInfo/deleteLogInfo?ids='+data.id,null,function(res){
			if (res.code == 0) {
				obj.del();
				layer.close(index);
				layer.msg(res.msg,{icon: 6});
				logTableReload();
			}else{
				layer.msg(res.msg,{icon: 6});
			}
		})
	}
	
	
	//监听工具条 
	table.on('tool(logTable)', function(obj){ 
	  var layEvent = obj.event;
	  if (layEvent === 'delete') {	  // 删除 
		layer.confirm('确定要删除该记录嘛?', function(index) {
			logTableSignDel(obj,index);
		});
	  }
	 });
	
	
	$("#loginname").on('click',function(){
		if($(this).val()=='istest'){
			$(this).val('');
			$(this).css('color','#000000');
		}
	})
	
	
	$("#loginname").on('blur',function(){
		if($(this).val().length==0){
			$(this).val('istest');
			$(this).css('color','#7575A0');	
		}
	})
	
	
	$("#loginip").on('click',function(){
		if($(this).val()=='0.0.0.0'){
			$(this).val('');
			$(this).css('color','#000000');
		}
	})
	
	
	$("#loginip").on('blur',function(){
		if($(this).val().length==0){
			$(this).val('0.0.0.0');
			$(this).css('color','#7575A0');	
		}
	})
	
	
	//时间的比较
	function compareDate(date1, date2) {
		var starttime = new Date(date1.year,date1.month,date1.date);
		var endtime = new Date(date2.year,date2.month,date2.date);
		return starttime >= endtime;
	}
	

	//使用layer的tips
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

	
	//获取批量id(1,2,3,4)
	function getAllId(data){
		var str="";
		$.each(data,function(index,obj){
			if(index==data.length-1){
				str+=data[index].id;
			}else{
				str+=data[index].id+',';
			}
		})
		return str;
	 }
})