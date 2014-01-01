<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri= "http://java.sun.com/jsp/jstl/core" prefix= "c" %>    
<%
	String rootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	//javascript:;
    String image = "http://192.168.1.13/mxk/image/";
%>
<script type="text/javascript">
var path = '<%=rootPath%>';
</script>