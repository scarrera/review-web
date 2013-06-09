<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.umflix.model.Movie,com.google.gson.Gson,java.util.ArrayList,java.lang.reflect.Type,com.google.gson.reflect.TypeToken" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error page</title>
</head>
<body>
<h1>Sorry! An unexpected error occurred</h1>
<% 
	Movie movie = new Movie();
	movie.setId((long) 1);
	Movie mov = new Movie();
	mov.setId((long) 2);
	ArrayList<Movie> movies = new ArrayList<Movie>();
	movies.add(movie);
	movies.add(mov);
	out.println("added movies");
	
	Type listType = new TypeToken<ArrayList<Movie>>() {}.getType();
	
	String json = new Gson().toJson(movies, listType);
	out.println(json);
	
	ArrayList<Movie> array = new Gson().fromJson(json, listType);
	for(Movie movi: array){
		out.println("movieId: " + movi.getId() + "; movieEnabled:" + movi.isEnabled());
	}
%>
</body>
</html>