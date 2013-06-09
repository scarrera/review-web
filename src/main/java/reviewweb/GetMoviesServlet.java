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

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.um.umflix.reviewmanager.ReviewManager;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.model.Movie;

/**
 * Servlet in charge of retrieving the list of movies in UMFlix system awaiting
 * review.
 * 
 */
public class GetMoviesServlet extends HttpServlet {

	static Logger logger = Logger.getLogger(GetMoviesServlet.class);
	@EJB(beanName = "ReviewManager")
	ReviewManager reviewManager;

	/**
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("Request intercepted; proceeding to retrieve user from session");
		HttpSession session = request.getSession(true);
		String token = (String) session.getAttribute("userToken");
		if (token == null) {
			logger.info("User attempted to get list of movies without first logging in; redirecting to login page");
			response.sendRedirect("index.jsp");
		} else {
			logger.info("User token successfully retrieved from session");
			try {
				logger.info("Proceeding to retrieve list of movies awaiting review");
				List<Movie> movies = reviewManager.getMovieToReview(token);
				logger.info("Movie list retrieved successfully; proceeding to parse it to json");
				Type listType = new TypeToken<List<Movie>>() {
				}.getType();
				String json = "";
				if (movies != null && movies.size() != 0) {
					json = new Gson().toJson(movies, listType);
					request.setAttribute("moviesAwaitingReview", json);
					logger.info("Movie list parsed to json and added to variable in request");
				} else
					logger.info("Movie list is null or empty");
				logger.info("Forwarding request to movie list display page");
				request.getRequestDispatcher("movieList.jsp").forward(request,
						response);

			} catch (InvalidTokenException e) {
				logger.error("User asked for movie list with an invalid token");
				response.sendRedirect("error.jsp");
			}
		}
	}
}
