$(init());

var subject_tag = "";

function init(){
	$("#create_subject_button").removeAttr("disabled");
	$("#create_subject_button").bind("click",function(){
		createSubject();
	});
	$("#create_part_button").removeAttr("disabled");
	$("#create_part_button").bind("click",function(){
		createPart();
	});
	
	
	
	showUser();//显示用户信息
	$("#narbar_loginout").bind("click",function(){
		loginOut();
		showUser();
	});
}

function chooseTags(tagName){
	subject_tag = tagName;
}

function createSubject() {
	var title = $("#subject_name").val();
	if(subject_tag == ""){
		alert("请选择专题标签");
	}else if(title == ""){
		alert("请输入专题名");
	}else if(typeof($.cookie('id')) == "undefined"){
		alert("请先登录");
	}else{
		var catalog = "";
		if($("#subject_catalog1").attr("checked") == "checked"){
			catalog = $("#subject_catalog1").val();
		}else if($("#subject_catalog2").attr("checked") == "checked"){
			catalog = $("#subject_catalog2").val();
		}
		var data = {"title":title,"tag":subject_tag,"category":catalog};
		$('#create_subject_loaddivleft').show();
		 $("#create_subject_loaddivleft").attr("disabled","disabled");
		 $.ajax({
		   		url : rootPath + "/add/subject.do",
		   		type : "POST",
		   		cache : false,
		   		async : false,
		   		data: data,
		   		success : function(item) {
		   			$("#create_subject_button").removeAttr("disabled");
		        	$('#create_subject_loaddivleft').hide();
		   			if(item.state == 200){
		   				$("#subject_create_div").hide();
		   				$("#part_create_div").show();
		   				$.cookie('create_return_subject_id', item.data.id , { path: '/' });
		   				$.cookie('create_return_subject_tag', item.data.tag , { path: '/' });
		   				$.cookie('create_return_subject_name', item.data.tag , { path: '/' });
		   				$("#subject_return_name").html(title);
		   				$("#subject_return_info").html("专题标签:"+ subject_tag + " 专题类型：" + catalog);
		   			}
		   		},
			    error : function(XMLHttpRequest, textStatus, errorThrown){
		        	$("#create_subject_button").removeAttr("disabled");
		        	$('#create_subject_loaddivleft').hide();
			    }
			});
	   }
}


function createPart()
{   
	var describes = $("#part_desc").val();
	var faceiamge = true;
	if($("#part_user_image").attr("checked") != "checked"){
		faceiamge = false;
	}
	var data = {"subjectId":$.cookie('create_return_subject_id'),
			   "userId":$.cookie('id'),
			   "subjectType":$.cookie('create_return_subject_tag'),
			   "subjectName":$.cookie('create_return_subject_name'),
			   "partInfo":describes,"change":faceiamge};
	
	if(true){
		$.ajaxFileUpload
		(
			{
				url:rootPath + "/add/part.do",
				secureuri:false,
				fileElementId:'fileToUpload',
				dataType: 'json',
				data:data,
				success: function (data, status)
				{  
					alert(data.message);
				},
				error: function (data, status, e)
				{
					alert(2);
				}
			}
		);
	}
}
