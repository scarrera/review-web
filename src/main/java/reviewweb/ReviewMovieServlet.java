package reviewweb;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.um.umflix.reviewmanager.ReviewManager;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;

/**
 * Servlet in charge of handling reviewer actions (approve or reject) for a
 * license and a given movie
 * 
 */
public class ReviewMovieServlet extends HttpServlet {

	static Logger logger = Logger.getLogger(ReviewMovieServlet.class);
	@EJB(beanName = "ReviewManager")
	ReviewManager reviewManager;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("Request intercepted; proceeding to retrieve user from session");
		HttpSession session = request.getSession(true);
		String token = (String) session.getAttribute("userToken");
		if (token == null) {
			logger.info("User attempted to review a license for a movie without first logging in; redirecting to login page");
			response.sendRedirect("index.jsp");
		} else {
			logger.info("User token successfully retrieved from session; proceeding to retrieve movie id, license id and reviewer action from request");
			if (request.getAttribute("movieId") == null
					|| request.getAttribute("licenseId") == null
					|| request.getAttribute("licenseId") == null) {
				logger.error("Null values retrieved from request");
				response.sendRedirect("error.jsp");
			} else {
				logger.info("Values successfully retrieved from request; proceeding to parse ids to long");
				try {
					long movieId = Long.parseLong((String) request
							.getAttribute("movieId"));
					long licenseId = Long.parseLong((String) request
							.getAttribute("licenseId"));
					logger.info("Successfully parsed ids to long; proceeding to check type of review action");
					String action = (String) request.getAttribute("action");
					if (action == "approve") {
						logger.info("Proceeding to approve license for movie");
						try {
							reviewManager.accept(token, movieId, licenseId);
							logger.info("Successfully approved license for movie");
							response.sendRedirect("reviewSuccessful.jsp");
						} catch (InvalidTokenException e) {
							logger.error("Attempted to approve license for movie with an invalid token");
							response.sendRedirect("index.jsp");
						}
					}
					if (action == "reject") {
						logger.info("Proceeding to reject license for movie");
						try {
							reviewManager.reject(token, movieId, licenseId);
							logger.info("Successfully rejected license for movie");
							response.sendRedirect("reviewSuccessful.jsp");
						} catch (InvalidTokenException e) {
							logger.error("Attempted to reject license for movie with an invalid token");
							response.sendRedirect("index.jsp");
						}
					}
				} catch (NumberFormatException e) {
					logger.error("Could not parse id values in request  to long");
					response.sendRedirect("error.jsp");
				}
			}
		}
	}

}
