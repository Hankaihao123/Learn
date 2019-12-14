layui.use([ 'form', 'layer', 'jquery' ], function() {
	var form = layui.form, layer = parent.layer === undefined ? layui.layer
			: top.layer
	$ = layui.jquery;

	$(".loginBody .seraph").click(function() {
		layer.msg("这只是做个样式，至于功能，你见过哪个后台能这样登录的？还是老老实实的找管理员去注册吧", {
			time : 5000
		});
	})

	// 登录按钮
	form.on("submit(login)", function(data) {
		NProgress.start();
		var btn = $(this);
		// 设置登录按钮 为不可点击
		btn.text("登录中...").attr("disabled", "disabled").addClass(
				"layui-disabled");
		$.post("/Login/login", data.field, function(rs) {
			if (rs.code != 200) {
				NProgress.done();
				layer.msg(rs.msg);
				// 设置登录按钮 恢复可点击 在前端防止 重复点击
				btn.text("登录").attr("disabled", false).removeClass(
						"layui-disabled");
			} else {
				setTimeout(function() {
					// 跳转到templates/system/index/index.html页面
					layer.msg(rs.msg);
					NProgress.done();
					location.href = "/System/toIndex";
				}, 1000);
			}
		},"json");
		return false;
	})

	// 表单输入效果
	$(".loginBody .input-item").click(function(e) {
		e.stopPropagation();
		$(this).addClass("layui-input-focus").find(".layui-input").focus();
	})
	$(".loginBody .layui-form-item .layui-input").focus(function() {
		$(this).parent().addClass("layui-input-focus");
	})
	$(".loginBody .layui-form-item .layui-input").blur(function() {
		$(this).parent().removeClass("layui-input-focus");
		if ($(this).val() != '') {
			$(this).parent().addClass("layui-input-active");
		} else {
			$(this).parent().removeClass("layui-input-active");
		}
	})
	
	$('#registerbtn').on('click',function(){
		$('#register').show();
		$('#login').hide();
	})
	
	$('#backlogin').on('click',function(){
		$('#register').hide();
		$('#login').show();
	})
	
	
	// 注册按钮
	form.on("submit(register)", function(data) {
		console.log(data.field);
		NProgress.start();
		var btn = $(this);
		// 设置登录按钮 为不可点击
		btn.text("注册中...").attr("disabled", "disabled").addClass(
				"layui-disabled");
		$.post("/Login/registerUser", data.field, function(rs) {
			if (rs.code >0 ) {
				NProgress.done();
				layer.msg(rs.msg);
				// 设置登录按钮 恢复可点击 在前端防止 重复点击
				btn.text("注册").attr("disabled", false).removeClass(
						"layui-disabled");
			} else {
				setTimeout(function() {
					// 跳转到登录页面
					layer.msg(rs.msg);
					NProgress.done();
					location.href = "/System/toLogin";
				}, 1000);
			}
		});
		return false;
	})
	
	//点击更换验证码
	$("#codeimg").on('click',function(){
		console.log(1);
		$(this).attr("src","/Login/codeImg?"+Math.random());
	})
	
	//监听用户名的输入框
	$("#userName").bind("input propertychange",function(event){
		if($("#userName").val().length==0){
			$("#userName").prev().show()
		}
	});
	
	$("#userName").blur(function(){
		var name=$("#userName").val();
		console.log(name);
		$.post('/Login/getUserImgpath?name='+name,null,function(res){
			if(res.code==0){
				$('.userAvatar').attr('src',res.data);
			}else{
				$('.userAvatar').attr('src','/resource/images/face.jpg');
			}
		})
	});
	
	//给用户名设置默认值
	if(document.cookie!='' && document.cookie.indexOf('hkh_username')!=-1){
		var i=document.cookie.indexOf('=');
		var username=document.cookie.substr(i+1);
		$("#userName").focus();
		$("#userName").prev().hide();
		$("#userName").val(username);
		$("#password").focus();
	}
	
})
