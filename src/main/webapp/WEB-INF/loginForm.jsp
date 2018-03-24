<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
	
	<c:url var="url" value="/login/login2.action"/>
	<h1>${message }</h1>
	<form action="${url }" method="post">
		<div>用户:<input name="user.name" type="text"></div>
		<div>密码:<input name="user.pwd" type="password"></div>
		<div><input type="submit" value="提交"></div>
	</form>
	
</body>
</html>