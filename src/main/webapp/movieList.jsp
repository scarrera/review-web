<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="edu.umflix.model.Movie,com.google.gson.Gson,java.util.List,java.lang.reflect.Type,com.google.gson.reflect.TypeToken"%>
<%
	String token = (String) session.getAttribute("userToken");
	String email = (String) session.getAttribute("userEmail");
	if (email == null || token == null)
		response.sendRedirect("/index.jsp");

	List<Movie> movies;
	
	Type listType = new TypeToken<List<Movie>>() {
	}.getType();
	String json = (String) request.getAttribute("moviesAwaitingReview");
	if(json != "")
		movies = new Gson().fromJson(json, listType);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Movie list</title>
</head>
<body>
	<h1>
		Welcome:
		<%=email%>
	</h1>
	<h2>This is a list of movies awaiting to be reviewed:</h2>
	<table>
		<%
			if(movies == null || movies.size() == 0)
				out.println("<td><tr>No movies awaiting review!</tr></td>");
			else{
				for(Movie movie: movies)
					out.println("<tr><td>" + movie.getTitle() + "</td><td><a href=''>Approve</a></td><td><a href=''>Reject</a></td></tr>");
			}
		%>
	</table>
</body>
</html>