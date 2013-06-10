<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="edu.umflix.model.Movie,edu.umflix.model.License,com.google.gson.Gson,java.util.List,java.lang.reflect.Type,com.google.gson.reflect.TypeToken,com.google.gson.GsonBuilder,com.google.gson.Gson,reviewweb.adapters.MovieAdapter"%>
<%
	String token = (String) session.getAttribute("userToken");
	String email = (String) session.getAttribute("userEmail");
	if (email == null || token == null)
		response.sendRedirect("index.jsp");

	List<Movie> movies;

	Type listType = new TypeToken<List<Movie>>() {
	}.getType();
	String json = (String) request.getParameter("moviesAwaitingReview");
	if (json != "") {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.registerTypeAdapter(Movie.class,
				new MovieAdapter()).create();
		movies = gson.fromJson(json, listType);
	}
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
	<h2>This is a list of movies and their respective licenses
		awaiting to be reviewed:</h2>
	<%
		if (movies == null || movies.size() == 0)
			out.println("No movies awaiting review!");
		else {
			for (Movie movie : movies) {
				out.println("<br><h3>Movie title: " + movie.getTitle()
						+ "</h3>");
				out.println("<ul>");
				for (License license : movie.getLicenses())
					out.println("<li><form action='licenseDetail.jsp' method='post'> "
							+ license.getName()
							+ " <input type='hidden' name='movieId' value='"
							+ movie.getId()
							+ "'><input type='hidden' name='jsonLicense' value='"
							+ new Gson().toJson(license)
							+ "'><input type='submit' value='see details'></form></li>");
				out.println("</ul>");
			}
		}
	%>
	<br>
	<a href="logout">Logout</a>
</body>
</html>