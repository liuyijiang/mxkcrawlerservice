<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
  <head>
   <%@ include file="../../headerinclude.jsp"%>
  </head>
  
  <body>
     <%@ include file="../public/nav.jsp"%>
     <div class="container" >
       <div class="form-inline">
	     <input id="keyword" type="text" class="form-control" style="width:500px" />
	     <button class="btn btn-default" type="button" onclick="search()">搜索!</button>
	     <button type="button" class="btn btn-link">试试手气</button> 
      </div> <!--end 搜索 -->
	     <hr />
     </div>
     <div class="container" >
          <div class="row">
             <div class="col-md-8">
			  <div id="info">
			    
<!-- 				<div class='media'> -->
<!-- 				  <a class='pull-left' href="#"> -->
<!-- 				     <img style="width:120px" src="http://www.waileecn.com/mxk/image/52b815150cf24a645fafe728_mini.png"> -->
<!-- 				  </a> -->
<!-- 				  <div class="media-body"> -->
<!-- 				    <span class="media-heading h3"><a href="#">水景<font color="red">舰队</font> </a></span> -->
<!-- 				    <small class="text-primary">寂寞4角钱 |评论 21| 阅读 21</small> -->
				    
<!-- 				    <span class="pull-right"> -->
<!-- 				        <small> -->
<!-- 					      <a href="#">有用</a> -->
<!-- 					      <a href="#">垃圾</a> -->
<!-- 				        </small> -->
<!-- 				    </span> -->
<!-- 				    <br /> -->
<!-- 				      <small class="text-muted">是一位资深全文索引/检索专家，曾经是V-Twin搜索引擎(Apple的Copland操作系统的成就之一)的主要开发者，后在Excite担任高级系统架构设计师，目前从事于一些INTERNET底层架构的研究。他贡献出的Lucene的目标是为各种中小型应用程序加入全文检索功能。</small><br /> -->
<!-- 				       <small class="text-success">http://www.waileecn.com/vistiorShowSubjectDatail?target=52b814e90cf24a645fafe717</small>               -->
<!-- 				  </div> -->
<!-- 				</div> -->
				
			   </div>	
			</div>
             <div class="col-md-4">
               
             </div>
          </div>
     </div>
  
  
      
  </body>
  <script type="text/javascript">
     function search(){
        var keyword = $("#keyword").val();
         $.ajax({
	       		url : path + "/search",
	       		type : "POST",
	       		cache : false,
	       		async : false,
	       		data: {"keyword":keyword},
	       		success : function(item) {
	       			createinfo(item);
	       		}
       	});
     }
     
     function createinfo(list){
            var show = '';
			for(var i in list)//照片墙第一个位置
			{
			  show = show + "<div class='media'><a class='pull-left' href='"+ list[i].url +"'>"
 			       +  "<img style='width:120px' src='http://localhost/img/"+ list[i].img +"'></a>"
 			       + "<div class='media-body'><span class='media-heading h3'><a href='"+ list[i].url +"'>"+ list[i].title + "</a></span>"
 				   + "<small class='text-primary'>"+ list[i].subtext + "</small><span class='pull-right'><small><a href='#'>有用</a><a href='#'>垃圾</a></small></span><br />"
 				   + "<small class='text-muted'>"+ list[i].info +"</small><br />"
 				   + "<small class='text-success'>"+ list[i].url +"</small></div></div>";
			}
			$("#info").html(show);
     }
     
  </script>
  <%@ include file="../../footinclude.jsp"%>
</html>
