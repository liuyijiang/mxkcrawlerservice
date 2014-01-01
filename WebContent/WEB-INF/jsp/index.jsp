<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib uri= "http://java.sun.com/jsp/jstl/core" prefix= "c" %>   
<html>
<head>
<title>模型控系统</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/crawlers">爬取器状态</a>
<hr />
<a href="${pageContext.request.contextPath}/contexts">未编目帖子</a>
<form action="${pageContext.request.contextPath}/regex/contexts" method="post">
   <input name="keyword"/><input type="submit" value="查询匹配资源">
</form>

<hr />
<p>添加链接</p>
<form action="${pageContext.request.contextPath}/add/links" method="post">
            链接<br />
   <textarea name="links" rows="3" cols="30"></textarea><br />
           匹配<br />
   <textarea name="match" rows="3" cols="30"></textarea><br />
   <input type="submit">
</form>
<hr />
<p>新建资源</p>
 <c:forEach items="${type}" var="list">
     <a href="${pageContext.request.contextPath}/type/${list }/resource">新建资源${list  }</a>
  </c:forEach>
</body>
<hr />
<p><a href="${pageContext.request.contextPath}/tags">新建标签</a></p>
</html>