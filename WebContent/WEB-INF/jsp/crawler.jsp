<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib uri= "http://java.sun.com/jsp/jstl/core" prefix= "c" %>   
<html>
<head>
<title>模型控系统</title>
<%@ include file="../headerinclude.jsp"%>
</head>
<body>

  <c:forEach items="${list}" var="list">
     <p>${list.info}</p>
     <p>
	     <button onclick="run(true,'${list.crawlerName}')">启动</button>&nbsp;
	     <button onclick="run(false,'${list.crawlerName}')">挂起</button>
     </p>
     <hr />
  </c:forEach>
</body>
<script type="text/javascript">
   function run(runable,crawlername){
	   $.ajax({
			url : path + "/crawle",
			type : "POST",
			cache : false,
			async : false,
			dataType : "json",
			data: {"crawlerName":crawlername,"runable":runable},
			success : function(item) {
				 alert(item);
			  }
		 });  
   }
</script>
<%@ include file="../footinclude.jsp"%>
</html>