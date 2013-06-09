package reviewweb;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.um.umflix.reviewmanager.ReviewManager;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.model.Movie;

public class GetMoviesServlet extends HttpServlet {

	@EJB(beanName = "ReviewManager")
	ReviewManager reviewManager;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String token = (String) session.getAttribute("userToken");
		if (token == null)
			response.sendRedirect("index.jsp");
		else {
			try {
				List<Movie> movies = reviewManager.getMovieToReview(token);
				Type listType = new TypeToken<List<Movie>>() {
				}.getType();
				String json = "";
				if (movies != null && movies.size() != 0) {
					json = new Gson().toJson(movies, listType);
					request.setAttribute("moviesAwaitingReview", json);
					request.getRequestDispatcher("movieList.jsp").forward(
							request, response);
				}
			} catch (InvalidTokenException e) {
				response.sendRedirect("index.jsp");
			}
		}
	}
}
