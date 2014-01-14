<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String rootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	//javascript:;
    String image = "http://192.168.1.17/mxk/image/";
%>
<title>模型控</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="description" content="模型控">
<meta name="keyword" content="模型控">
<!-- <link type="image/x-icon" href="assets/mxkimage/Logo.png" rel="icon"> -->
<!-- <link type="image/x-icon" href="assets/mxkimage/Logo.png"  rel="shortcut icon"> -->
<link rel="stylesheet" type="text/css" href="assets/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="assets/css/mxk.css" />
<link rel="stylesheet" type="text/css" href="assets/css/font-awesome.min.css" /> 
<script type="text/javascript">
var path = '<%=rootPath%>';
</script>