<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String token = (String) session.getAttribute("userToken");
	if (token == null)
		response.sendRedirect("index.jsp");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Success!</title>
</head>
<body>
	<h2>Review action completed successfully</h2>
	<br>
	<a href="reviewer">Return to list of movies awaiting review</a>
</body>
</html>