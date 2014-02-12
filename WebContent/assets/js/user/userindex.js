$(init());

function init(){
	showUser();//显示用户信息
	$("#narbar_loginout").bind("click",function(){
		alert(1);
		loginOut();
		showUser();
	});
}

//function showUser(){
//	var username = $.cookie('username'); 
//	var userimage = $.cookie('userimage');
//	if (typeof(username) != "undefined" && typeof(userimage) != "undefined") { 
//		alert(username + "#####" + userimage);
//	}
//}