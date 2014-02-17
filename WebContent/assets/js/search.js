var rootPath = "/mxkcrawlerservice"; 

$(init());

function init(){
	$("#search-btn").bind("click",function(){
		find(1);
	});
	$("#luck-find-btn").bind("click",function(){
		luckfind();
	});
	$("#significance-btn").bind("click",function(){
		updateWebResourceSignificance(true);
	});
	$("#insignificance-btn").bind("click",function(){
		updateWebResourceSignificance(false);
	});
	$("#user-collect-btn").bind("click",function(){
		userCollect();
	});
	
	
	showUser();//显示用户信息
	$("#narbar_loginout").bind("click",function(){
		loginOut();
		showUser();
	});
}

function getcontent(id){
	 $("#loaddivright").show();
	 $.ajax({
    		url : rootPath + "/resource.do",
    		type : "get",
    		cache : false,
    		async : false,
    		data: {"id":id},
    		success : function(item) {
    			$("#simpleread").show();
    			setData(item.data);
    			$("#loaddivright").hide();
    		}
	});
}

function setData(item){
	$("#contenttitle").html(item.title);
	$("#contentsubtitle").html("作者："+item.ownername+" 评论："+ item.posts +" 阅读："+item.hits);
    var imgs = item.images.split(",");
    var imgshtml = "";
    for (var i in imgs ) {
        if(i > 2){
    		break;
    	}
        if(imgs[i] != ""){
          imgshtml = imgshtml + "<img class='img-thumbnail' style='width:400px' src='"+ imgs[i] +"'/><br /><br />";
        }
    }
    $("#contentid").html(item.id);
    $("#contentimages").html(imgshtml);
    $("#contentinfo").html(item.info);
    $("#contentshow").attr("href",item.url);
}

//查询
function find(page){
	$("#loaddivleft").show();
	 var keyword = $("#keyword").val();
	 $.ajax({
   		url : rootPath + "/search.do",
   		type : "POST",
   		cache : false,
   		async : false,
   		data: {"keyword":keyword,"currentPage":page},
   		success : function(model) {
   			var item = model.data;
   			$("#infototal").html("为你找到约<span class='text-warning'>"+item.total+"</span>相关记录");
   			createinfo(item.data);
   			createpage(item.page,item.currentPage);
   			$("#loaddivleft").hide();
   		}
	});
}

function luckfind(){
	 $("#loaddivleft").show();
	 $.ajax({
  		url : rootPath + "/luck/search.do",
  		type : "POST",
  		cache : false,
  		async : false,
  		success : function(model) {
  			var item = model.data;
  			$("#infototal").html("为你找到约<span class='text-warning'>"+item.total+"</span>相关记录");
  			createinfo(item.data);
  			createpage(item.page,item.currentPage);
  			$("#loaddivleft").hide();
  		}
	});
}
 
function createpage(page,currentpage){
	var pagediv = "<ul class='pagination pagination-sm'>";
	if(currentpage >= 10){
		pagediv = pagediv + "<li><a onclick='find(\""+ (currentpage - 10) +"\")' href='javascript:;'>&laquo;</a></li>";
	}
	var num = parseInt(currentpage / 10) * 10 ; //因子
	var startpage = num + 1;
    if((currentpage % 10) == 0){
    	startpage = currentpage;
	}
	var endpage = num + 10;
	if(endpage > page){
		endpage = page;
	}
	for(var i=startpage; i<=endpage; i++){
		if(i==currentpage){
			pagediv = pagediv + "<li class='active'><a href='javascript:;'>"+ i +"</a></li>";
		}else{
			pagediv = pagediv + "<li><a href='javascript:;' onclick='find(\""+ i +"\")'>"+ i +"</a></li>";
		}
	}
	if(page > 1 && endpage < page){
		pagediv = pagediv + " <li><a href='javascript:;' onclick='find(\""+ (endpage + 1) +"\")'>&raquo;</a></li>";
	}
	pagediv = pagediv +  "</ul>";
	$("#pagedivs").html(pagediv);
}


//组装查询结果
 function createinfo(list){
     var show = '';
		for(var i in list)
		{
		  show = show + "<div class='media'><a href='#read' onclick='getcontent("+ list[i].id +")' class='pull-left' href='javascript:;'>"
		       +"<img class='img-thumbnail' style='width:120px' src='http://www.waileecn.com/mxk/content/img/"+ list[i].img +"'></a>"
		       + "<div class='media-body'><span class='media-heading h4'><a href='#read' onclick='getcontent("+ list[i].id +")' style='color:#1E0FBE;' href='javascript:;'>"+ list[i].title + "</a></span><br />"
			   + "<small class='text-primary'>"+ list[i].subtext + "</small><br />"
			   + "<small class='text-muted'>"+ list[i].info +"</small><br />"
			   + "<small class='text-success'>"+ list[i].url +"</small></div></div>";
		}
		$("#info").html(show);
}
 
 function updateWebResourceSignificance(significance){
	 var id = $("#contentid").html();
	 $.ajax({
  		url : rootPath + "/webresources/significance.do",
  		type : "POST",
  		cache : false,
  		async : false,
  		data: {"id":id,"significance":significance},
  		success : function(model) {
  			alert(model.state);
  		}
	});
 }
 
 function userCollect(){
	 var userId = $.cookie('id'); 
	 if(typeof(userId) == "undefined"){
		 alert("请先登录");
		 return;
	 }
     var colletTarget = $("#contentid").html();
     var simpleDesc = $("#contenttitle").html();
	 $.ajax({
	  		url : rootPath + "/user/collect.do",
	  		type : "POST",
	  		cache : false,
	  		async : false,
	  		data: {"userId":userId,"colletTarget":colletTarget,"colletTargetType":1,"simpleDesc":simpleDesc},
	  		success : function(model) {
	  			alert(model.state);
	  		}
		});
 }

