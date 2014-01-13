<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib uri= "http://java.sun.com/jsp/jstl/core" prefix= "c" %>   
<html>
<head>
<title>标签</title>
<%@ include file="../headerinclude.jsp"%>
</head>
<body>
<table border="1">
<tr><td>
新建标签<br />
<form action="${pageContext.request.contextPath}/add/tag" method="post">
类型
<select name="type">
  <c:forEach items="${type}" var="list">
     <option value="${list }">${list }</option>
  </c:forEach>
</select><br />
名字<input name="name" /><br />
排序<select name="sort">
 <option value="1">1</option>
 <option value="2">2</option>
 <option value="3">3</option>
 <option value="4">4</option>
 <option value="5">5</option>
</select><br />
子标签
<textarea name="subtags" rows="3" cols="30"></textarea>
 <input type="submit" value="提交">
</form>
</td>
<td>
<p>
国籍<br />
中国#1,美国#2,德国#3,英国#3,日本#4,法国#4,意大利#5,俄国#6,其他#9
</p>
<p>
年代<br />
古代#1,一战#1,二战#1,现在#1,其他#9
</p>
<p>
类型<br />
战列舰#1,巡洋舰#1,驱逐舰#1,护卫舰#1,扫雷舰#1,航母#1,潜艇#1,补给船#2,商船#2,两栖攻击舰#3,其他#9
</p>
</td>
</tr>
</table>
<hr />
<p>
选择标签
<select name="type" onchange="findTagsByType(this.value)">
 <option value="">选择</option>
  <c:forEach items="${type}" var="list" >
     <option value="${list }">${list }</option>
  </c:forEach>
</select>
</p>
<form action="${pageContext.request.contextPath}/add/tag" method="post">
<strong>使用标签名：<font id="tganame" color="red"></font></strong>
 添加子标签:<input id="subtags" style="width:500"/>
  排序<select name="sort">
 <option value="1">1</option>
 <option value="2">2</option>
 <option value="3">3</option>
 <option value="4">4</option>
 <option value="5">5</option>
</select>
 <input type="submit" value="提交">
   </form>
<table id="tags" style="width:100%" border="1">
</table>
</body>
<script type="text/javascript">
var tagid='';

function findTagsByType(type){
	  $.ajax({
		url : path + "/"+ type +"/tags/",
		type : "GET",
		cache : false,
		async : false,
		dataType : "json",
		success : function(list) {
			show = '';
			for(var i in list){
			  var subtags = '';
			  show = show + " <tr><td><table style='width:100%' border='1'><tr><td>"+list[i].id+"</td>"
			  +"<td>"+list[i].name+"</td><td>"+list[i].sort+"</td><td><a href='javascript:;' onclick=usetag(\""+ list[i].name +"\",\""+ list[i].id+"\")>使用</a></td></tr>"
			  +"<tr><td colspan='4'>";
			   for(var j in list[i].subtags){
				   subtags = subtags + "<a href='javascript:;'>"+ list[i].subtags[j].desc +"</a>&nbsp;";
			   }  
			   show =  show + subtags  + "</td></tr></table></td></tr>";
			 }
			 $("#tags").html(show);
		  }
	 }); 
}

function usetag(name,id){
	tagid = id;
	$("#tganame").html(name);
}
</script>

<%@ include file="../footinclude.jsp"%>
</html>