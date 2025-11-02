<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta charset="UTF-8">
<html>
<head>
    <title>logout</title>
</head>
<body>
<%
    session.invalidate();
    response.sendRedirect("LoginRestrito.jsp");
%>
</body>
</html>
