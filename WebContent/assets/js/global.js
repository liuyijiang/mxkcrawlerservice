var rootPath = "/mxkcrawlerservice"; 

function showUser(){
	var username = $.cookie('username'); 
	if (typeof(username) != "undefined") { 
		$("#loginout_nar").hide();
		$("#loginin_nar").show();
		$("#narbar_username").html(username);
	}else{
		$("#narbar_username").html("");
		$("#loginout_nar").show();
		$("#loginin_nar").hide();
	}
}

 function loginOut(){
    $.removeCookie('username' , { path: '/' });
	$.removeCookie('id', { path: '/' });
	$.removeCookie('userimage', { path: '/' });
	$.removeCookie('token', { path: '/' });
 }