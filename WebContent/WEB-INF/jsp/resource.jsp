<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib uri= "http://java.sun.com/jsp/jstl/core" prefix= "c" %>  
<html>
<head>
<title>资源</title>
</head>
<script type="text/javascript">
//style="display:none"
   function addtag(name){
	 var str =  document.getElementById("tags").innerHTML;
	 document.getElementById("tags").innerHTML = name + "," + str;
   }
   
   function cleanadd(){
	   document.getElementById("tags").innerHTML = "";  
   }
</script>
<body>
<table border="1">
  <tr>
    <td>
      <form action="${pageContext.request.contextPath}/add/resources" method="post">
       <input style="display:none" name="type" value="${type }"/>
   名称<input name="name" /><br />
 keyword<br/><textarea name="keyword" rows="3" cols="30"></textarea><br />
   标签<br/><textarea readonly="readonly" id="tags" name="tags"  rows="3" cols="30"></textarea><br />
 <input type="submit" value="保存">
</form>
    </td>
    <td>
      <button onclick="cleanadd()">清空</button>
    </td>
  </tr>
</table>
<hr />
<table style="width:100%" border="1">
 <c:forEach items="${tags}" var="list">
  <tr>
    <td><font color="red">${list.name  }</font></td>
    <td>
     <c:forEach items="${list.subtags}" var="subtag">
       <a href="#" onclick="addtag('${subtag.desc  }')">${subtag.desc }</a>&nbsp;
     </c:forEach>
    </td>
  </tr>
  </c:forEach>
</table>
</body>
</html>