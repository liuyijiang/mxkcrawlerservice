var rootPath = "/mxkcrawlerservice"; 
$(init());

function init(){
	$("#registbutton").removeAttr("disabled");
	$("#registbutton").click(regist);
	showUser();//显示用户信息
	$("#narbar_loginout").bind("click",function(){
		loginOut();
		showUser();
	});
}

function regist(){
	 $("#loaddivleft").show();
	 var username = $("#username").val();
	 var useremail = $("#useremail").val();
	 var userpassword = $("#userpassword").val();
	 if(username == "" || useremail == ""|| userpassword == ""){
		 alert("请完整填写信息！");
		 return;
	 }
	 var data = {"email":useremail,"username":username,"password":userpassword};
	 $("#registbutton").attr("disabled","disabled");
	 $.ajax({
    		url : rootPath + "/user/regist.do",
    		type : "POST",
    		cache : false,
    		async : false,
    		data: data,
    		success : function(item) {
    			cachData(item.data);
    			$("#registbutton").removeAttr("disabled");
    			$("#loaddivleft").hide();
    			window.location.href= rootPath + "/web/user/userindex.html";
    		},
	        error : function(XMLHttpRequest, textStatus, errorThrown){
	        	$("#registbutton").removeAttr("disabled");
	        	$("#loaddivleft").hide();
	        }
	});
}

function cachData(item){
	$.cookie('username', item.userName , { path: '/' });
	$.cookie('id', item.id, { path: '/' });
	$.cookie('userimage', item.userImage, { path: '/' });
	$.cookie('token', item.token, { path: '/' });
}

//function showUser(){
//	var username = $.cookie('username'); 
//	var userimage = $.cookie('userimage');
//	if (typeof(username) != "undefined" && typeof(userimage) != "undefined") { 
//		alert(username + "#####" + userimage);
//	}
//}