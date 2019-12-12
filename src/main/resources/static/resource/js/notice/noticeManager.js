layui.use([ 'jquery', 'form', 'table', 'layer', 'laydate' ], function() {
	var $ = layui.jquery;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var laydate = layui.laydate;
	
	
	var starttime;//开始时间的value

	
	
	// 日期控件渲染
	laydate.render({
		elem : '#startTime',
		type : 'date',
		format : 'yyyy年MM月dd日',
		trigger : 'click',
		done: function(value, date, endDate){
			starttime=date;
		}
	});

	
	// 结束时间
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
	
	
	// 渲染表格
	// elem:<table id="NoticeTables">
	var NoticeTable=table.render({
	    elem: '#NoticeTables',
	    url:'/Notice/queryAllNotice',
	    toolbar: '#NoticeTableToolBar',
	    defaultToolbar: null,
	    page: true,
	    title: '公告信息数据表',
	    height: 'full-228', // 高度最大化减去差值
	    cols: [[
	      {type: 'checkbox', fixed: 'left'},
	      {field:'id', title:'ID', width:80, fixed: 'left', unresize: true},
	      {field:'title', title:'公告标题',	align:'center',unresize: true},
	      {field:'content', title:'公告内容',	align:'center',unresize: true,hide:true},
	      {field:'createtime', title:'创建时间',	align:'center',unresize: true},
	      {field:'opername', title:'设置的人员',align:'center',unresize: true},
	      {fixed: 'right', title:'操作', toolbar: '#NoticeTableLineBar',align:'center'}
	    ]]
	});
	
	
	// 查询的表单提交,表格重新加载
	// <button lay-filter="NoticeTable">
	form.on('submit(NoticeTable)', function(data) {
		NoticeTable.reload({
			where:data.field,
			page:{curr:1}
		})
		return false; // 阻止表单跳转
	});
	
	// 表格重新加载
	function NoticeTableReload(){
		NoticeTable.reload({
    		where:null,
			page:{curr:1}
		});
	}
	
	// 表格数据批量删除
	function NoticeTableDelete(obj){
		var checkStatus = table.checkStatus(obj.config.id);
		if(checkStatus.data.length>0){
			var ids=getAllId(checkStatus.data);
			layer.confirm('确定要删除这些记录嘛?', function(index) {
				$.post('/Notice/deleteNotice?ids='+ids,null,function(res){
		    		if(res.code==0){
		    			NoticeTable.reload({
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
	
	
	// 初始化Ueditor富文本编辑器
	function initUeditor(){
		var editor=UE.getEditor('editor', {
			toolbars : [ [ 'fullscreen', 'source', '|', 'undo', 'redo', '|',
					'bold', 'italic', 'underline', 'fontborder',
					'strikethrough', 'superscript', 'subscript',
					'removeformat', 'formatmatch', 'autotypeset', 'blockquote',
					'pasteplain', '|', 'forecolor', 'backcolor',
					'insertorderedlist', 'insertunorderedlist', 'selectall',
					'cleardoc', '|', 'rowspacingtop', 'rowspacingbottom',
					'lineheight', '|', 'customstyle', 'paragraph',
					'fontfamily', 'fontsize', '|', 'directionalityltr',
					'directionalityrtl', 'indent', '|', 'justifyleft',
					'justifycenter', 'justifyright', 'justifyjustify', '|',
					'touppercase', 'tolowercase', '|', 'link', 'unlink',
					 '|', 'imagenone', 'imageleft', 'imageright',
					'imagecenter', '|', 'simpleupload', 'insertimage',
					'emotion', 'insertvideo', 'music', 'attachment',
					'map','insertcode','pagebreak', 'template', '|', 'horizontal',
					'date', 'time', 'spechars', '|','inserttable', 'deletetable', 'insertparagraphbeforetable',
					'insertrow', 'deleterow', 'insertcol', 'deletecol',
					'mergecells', 'mergeright', 'mergedown', 'splittocells',
					'splittorows', 'splittocols', 'charts', '|', 'print',
					'preview', 'searchreplace' ]],
					initialFrameHeight : 300,
					initialFrameWidth : 750,
					elementPathEnabled : false,　　// 是否启用元素路径，默认是true显示
					wordCount:false,        // 是否开启字数统计
					autoHeightEnabled:false　　// 编辑器内容，是否自动长高,默认true
		},{ zIndex: 1000});
		return editor
	}

	// 创建ueditor的实例
	editor =initUeditor();
	var editor;//ueditor对象
	var noticeIndex; //打开公告那个layerid
	var Url;//来对修改和新增的Url地址修改
	
	// 新增公告
	function NoticeTableAdd(obj) {
		noticeIndex = layer.open({
			type : 1,
			title:'新增公告',
			content : $('#NoticeModForm'),
			area : [ '900px', '620px' ],
			zIndex : 891,
			success :function(){
				form.val("NoticeInfoForm",null);
				Url='/Notice/addNotice';
				//清空表表单,因为jquery没有该函数因此必须转换成DOM对象
				$('#NoticeInfoForm')[0].reset();
			}
		});
	}
	
	// 修改公告
	function NoticeTableMod(obj) {
		noticeIndex = layer.open({
			type : 1,
			title:'修改公告',
			content : $('#NoticeModForm'),
			area : [ '900px', '620px' ],
			zIndex : 891,
			success :function(){
				//表单赋值
				form.val("NoticeInfoForm", { 
					"id": obj.data.id,
					"title": obj.data.title
				});
				//在设置ueditor内容时要加上延迟
				setTimeout(function(){
					editor.setContent(obj.data.content);
				},200);
				Url='/Notice/updateNotice';
			}
		});
	}
	
	
	//点击取消关闭新增或者修改公告的弹窗
	$('#btnclose').on('click',function(){
		layer.close(noticeIndex);
	})
	
	
	// 对新增公告时ueditor验证
	form.verify({
		editorValue: function(value, item){
			if(editor.hasContents()===false){
				editor.focus();
				return "请输入公告内容";
			}
	  }
	});

	// 监听公告的提交
	// <button lay-filter="NoticeTable">
	form.on('submit(NoticeInfo)', function(data) {
		$.post(Url,data.field,function(res){
			if(res.code==0){
				layer.close(noticeIndex);
				layer.msg(res.msg,{icon: 6});
				NoticeTableReload();
			}else{
				layer.close(noticeIndex);
				layer.msg(res.msg,{icon: 5});	
			}
		})
		return false; // 阻止表单跳转
	});
	
	
	
	
	// 表格头部工具条监听
	// NoticeTables是lay-filter='NoticeTables'
	table.on('toolbar(NoticeTables)', function(obj){
	  switch(obj.event){
	    case 'allrefresh':
	    	NoticeTableReload();
	    break;
	    case 'alldelete':
	    	NoticeTableDelete(obj);
	    break;
	    case 'add':
	    	NoticeTableAdd(obj);
	    break;
	  };
	});
	
	
	// 表格数据单个删除操作
	function NoticeTableSignDel(obj,index){
		var data = obj.data; 
		console.log(data);
		$.post('/Notice/deleteNotice?ids='+data.id,null,function(res){
			if (res.code == 0) {
				obj.del();
				layer.close(index);
				layer.msg(res.msg,{icon: 6});
				NoticeTableReload();
			}else{
				layer.msg(res.msg,{icon: 6});
			}
		})
	}
	
	
	// 监听工具条
	table.on('tool(NoticeTables)', function(obj) {
	var layEvent = obj.event;
	if (layEvent === 'delete') { // 删除
		layer.confirm('确定要删除该记录嘛?', function(index) {
			NoticeTableSignDel(obj, index);
		});
	} else if (layEvent === 'edit') {
		NoticeTableMod(obj);
	} else if(layEvent==='show'){
		layer.open({
			type: 1,
			title:obj.data.title,
			area : [ '900px', '620px' ],
			content:obj.data.content
		});
	}
	});
	
	
	// 时间的比较
	function compareDate(date1, date2) {
		var starttime = new Date(date1.year,date1.month,date1.date);
		var endtime = new Date(date2.year,date2.month,date2.date);
		return starttime >= endtime;
	}
	

	// 使用layer的tips
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

	
	// 获取批量id(1,2,3,4)
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