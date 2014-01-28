var rootPath = "/mxkcrawlerservice"; 

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
    			setData(item);
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
   		success : function(item) {
   			$("#infototal").html("为你找到约<span class='text-warning'>"+item.total+"</span>相关记录");
   			createinfo(item.data);
   			createpage(item.page,item.currentPage);
   			$("#loaddivleft").hide();
   		}
	});
}

 
function createpage(page,currentpage){
	var pagediv = "<ul class='pagination'>";
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
		  show = show + "<div class='media'><a onclick='getcontent("+ list[i].id +")' class='pull-left' href='javascript:;'>"
		       +"<img style='width:120px' src='http://www.waileecn.com/mxk/content/img/"+ list[i].img +"'></a>"
		       //+ "<img style='width:100px' src='http://www.waileecn.com/mxk/image/52b815210cf24a645fafe72d_mini.png'></a>"
		       + "<div class='media-body'><span class='media-heading h4'><a onclick='getcontent("+ list[i].id +")' style='color:#1E0FBE;' href='javascript:;'>"+ list[i].title + "</a></span><br />"
			   + "<small class='text-primary'>"+ list[i].subtext + "</small><br />"
			   + "<small class='text-muted'>"+ list[i].info +"</small><br />"
			   + "<small class='text-success'>"+ list[i].url +"</small></div></div>";
		}
		$("#info").html(show);
}