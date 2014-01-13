<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib uri= "http://java.sun.com/jsp/jstl/core" prefix= "c" %> 
<html>
<head>
<title>Insert title here</title>
<%@ include file="../headerinclude.jsp"%>
</head>
<body>
<strong>选中的资源
<font id="webresourcename" color="blue">
</font>
<font id="webresourceid" color="red"></font>
</strong>
<form style="display:none" id="fromd" action="${pageContext.request.contextPath}/catalog/context" method="post">
   <input id="cid" name="id" />
   <input id="resid" name="resid" />
</form>

<table style="width:100%" border="1">
  <tr>
   <td>
   <input id="keys" style="width:300" /><button onclick="find()">ok</button>
   </td>
    <td>
          新建资源:
     <c:forEach items="${type}" var="list">
         <a href="${pageContext.request.contextPath}/type/${list }/resource">新建资源${list  }</a>
      </c:forEach>
   </td>
  </tr>
  <tr>
    <td id="webresoucre" colspan="2"></td>
  </tr>
</table>
<hr />
<table style="width:100%" border="1">
   <c:forEach items="${list}" var="list">
  <tr>
    <td>${list.headline }</td>
    <td>${list.linkurl }</td>
    <td>${list.sitename }</td>
    <td>
    <c:forEach items="${list.images}" var="img">
       <img src="<%=image %>${img }" style="width:80;height:80"/>
    </c:forEach>
    </td>
    <td>
      <a href="#" onclick="catalog('${list.id}')">编辑</a>
    </td>
  </tr>
  </c:forEach>
</table>
<script type="text/javascript">
  
   function catalog(id){
      if(confirm('编目')){
	      $("#resid").val($("#webresourceid").html());
	      $("#cid").val(id);
	      $("#fromd").submit();
       }else{
          alert("取消");
      }
   }
  
   function find(){
	 var key = $("#keys").val();
	 $.ajax({
			url : path + "/find/key/resource",
			type : "POST",
			cache : false,
			async : false,
			dataType : "json",
			data:{"keyword":key},
			success : function(list) {
				show = '';
				for(var i in list){
					
					show = show + "<a href='#' onclick=choose(\""+ list[i].name +"\",\""+ list[i].id+"\")>"+ list[i].name +"</a><br />";
					
				 }
				 $("#webresoucre").html(show);
			  }
		 });   
   }

   function choose(name,id){
	   $("#webresourcename").html(name);
	   $("#webresourceid").html(id);
   }

</script>
</body>
<%@ include file="../footinclude.jsp"%>
</html>