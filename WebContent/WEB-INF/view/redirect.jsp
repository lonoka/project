<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="poly.util.CmmUtil"%>
<%
	String msg = CmmUtil.nvl((String)request.getAttribute("msg"));
	String url = CmmUtil.nvl((String)request.getAttribute("url"));
%>
<!DOCTYPE html>
<html>
<head>
<link href="/css/sb-admin-2.min.css" rel="stylesheet">
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script>
window.onload = function(){
	location.href='<%=url%>';
};
</script>
</head>
<body class="bg-gradient-saddle">

</body>
</html>