var form, $,areaData;
layui.config({
    base : "/resource/js/user/"
}).extend({
    "address" : "address"
})
layui.use(['form','layer','upload','laydate',"address",'element'],function(){
    form = layui.form;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        laydate = layui.laydate,
        address = layui.address;
    	element=layui.element;
    	
    //将41-4114-411421 分割
    function getaddress(str){
    	if(str==null || str==undefined){
    		return;
    	}
    	var result=str.split('-');
    	return result;
    }
    
    //获取用户地址的索引
    function getindex(datas,value){
    	var i=0;
    	for(var i=0;i<datas.length;i++){
    		if(datas[i].code===value){ 
    			return i;
    		}
    	}
    }
    
    address.provinces();
    
    //获取个人信息进行渲染
    $.post('/User/getUserInfo',null,function(res){
    	if(res.code==0){
    		var user=res.data;
    		var add=getaddress(user.address);
    		//得到当前省的数组下标索引
    		var index1=getindex(datas,add[0]);
    		//得到当前市数组下标索引 
    		var index2=getindex(datas[index1].childs,add[1]);
    		address.citys(datas[index1].childs);
    		address.areas(datas[index1].childs[index2].childs);
    		form.val("UserInfo", { 
    			  "loginname": user.loginname,
    			  "name":user.name,
    			  "sex": user.sex,
    			  "birth":user.hiredate,
    			  "phone":user.phone,
    			  "email":user.email,
    			  "meself":user.meself,
    			  "province":add[0],
    			  "city":add[1],
    			  "area":add[2]
    			});
    		if(user.imgpath==null || user.imgpath==undefined || user.imgpath==''){
    			$('#userFace').attr("src","/resource/images/face.jpg");
    		}else{
    			 $('#userFace').attr("src",user.imgpath);
    		}
    	}else{
    		layer.msg(res.msg, {icon : 5});
    	}
    })
    
    //上传头像
    upload.render({
        elem: '.userFaceBtn',
        url: '/User/upLoadHead',
        method : "post",  //此处是为了演示之用，实际使用中请将此删除，默认用post方式提交
        done: function(res, index, upload){
        	if(res.code==0){
    		  $('#userFace').attr("src",res.data.headimgagesrc);
              //window.sessionStorage.setItem('userFace',res.data.headimgagesrc);
              layer.msg(res.msg, {icon : 6});
              //window.location.reload();
              $('#headerprogress').addClass("layui-hide");
        	}else{
        		 layer.msg(res.msg, {icon : 5});
        		 $('#headerprogress').addClass("layui-hide");
        	}
        },
        progress: function(n){
        	$('#headerprogress').removeClass("layui-hide");
            var percent = n + '%' //获取进度百分比
            element.progress('demo', percent); //可配合 layui 进度条元素使用
       }
    });

    //添加验证规则
    form.verify({
        userBirthday : function(value){
            if(!/^(\d{4})[\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|1[0-2])([\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/.test(value)){
                return "出生日期格式不正确！";
            }
        }
    })
    
    //选择出生日期
    laydate.render({
        elem: '.userBirthday',
        format: 'yyyy年MM月dd日',
        trigger: 'click',
        max : 0,
    });


    //提交个人资料
    form.on("submit(changeUser)",function(data){
    	console.log(data);
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        //将填写的用户信息存到session以便下次调取
        var userInfoHtml = '';
        userInfoHtml = {
            'loginname' : data.field.loginname,
            'sex' : data.field.sex,
            'name' : data.field.name,
            'address': data.field.province+'-'+ data.field.city+'-'+data.field.area,
            'phone':data.field.phone,
            'email':data.field.email,
            'birth':data.field.birth,
            'meself':data.field.meself 
        };
        //window.sessionStorage.setItem("userInfo",JSON.stringify(userInfoHtml));
        console.log(userInfoHtml);
        $.post('/User/updateUser',userInfoHtml,function (res){
        	if(res.code==0){
        		 layer.msg(res.msg, {icon : 6});
        		 window.location.reload();
        	}else{
        		 layer.msg(res.msg, {icon : 5});
        	}
        },'json');
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })

    //修改密码
    form.on("submit(changePwd)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            layer.msg("密码修改成功！");
            $(".pwd").val('');
        },2000);
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })
})