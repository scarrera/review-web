<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="edu.umflix.model.Movie,edu.umflix.model.License,com.google.gson.Gson,java.util.List,java.lang.reflect.Type,com.google.gson.reflect.TypeToken"%>

<%
	String token = (String) session.getAttribute("userToken");
	if (token == null)
		response.sendRedirect("index.jsp");

	License license;
	String json = (String) request.getParameter("jsonLicense");
	if (json != "")
		license = new Gson().fromJson(json, License.class);
	else
		response.sendRedirect("error.jsp");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>License details</title>
</head>
<body>
	<h1>License details</h1>
	<br> Name:
	<%=license.getName()%>
	Start date:
	<%=license.getStartDate()%>
	End date:
	<%=license.getEndDate()%>
	Description:
	<%=license.getDescription()%>
	Max views:
	<%=license.getMaxViews()%>
	Cost per minute:
	<%=license.getCostPerMinute()%>
	<br>
	<form action="reviewMovie" method="post">
		<input type="hidden" name="movieId"
			value="<%=request.getParameter("movieId")%>"> <input
			type="hidden" name="licenseId" value="<%=license.getId()%>">
		<input type="hidden" name="action" value="approve"> <input
			type="submit" value="Approve">
	</form>
	<form action="reviewMovie" method="post">
		<input type="hidden" name="movieId"
			value="<%=request.getParameter("movieId")%>"> <input
			type="hidden" name="licenseId" value="<%=license.getId()%>">
		<input type="hidden" name="action" value="reject"> <input
			type="submit" value="Reject">
	</form>
</body>
</html>