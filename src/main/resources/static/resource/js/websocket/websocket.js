var stompClient = null;
layui.use([ 'jquery' ], function() {
	var $ = layui.jquery;
	var userid = USER_ID;
	connect(userid);

	// sockjs连接并且获取服务发送的数据
	function connect(id) {
		// 创建websocket连接
		var socket = new SockJS('/websocket');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			//console.log('Connected: ' + frame);
			stompClient.subscribe('/user/' + id + '/donetask', function(greeting) {
				var result=JSON.parse(greeting.body);
				console.log(result);
				var str=$("#noticeDiv").text();
				console.log(str);
				str=str.replace('#{content}',result.info);
				console.log(str);
				layer.open({
					type : 1,
					title : false, // 不显示标题栏
					loseBtn : false,
					area : '300px;',
					shade : 0.8,
					id : 'LAY_layuipro',
					btn : [ '火速围观', '残忍拒绝' ],
					btnAlign : 'c',
					moveType : 1,
					content : str,
					success : function(layero) {
					}
				});
			});
		});
	}

})

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
}

function sendName() {
	stompClient.send("/app/hello", {}, JSON.stringify({
		'name' : $("#name").val()
	}));
}

function showGreeting(message) {
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
}
