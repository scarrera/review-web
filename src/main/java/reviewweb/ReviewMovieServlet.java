package reviewweb;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.um.umflix.reviewmanager.ReviewManager;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;

public class ReviewMovieServlet extends HttpServlet {

	@EJB(beanName = "ReviewManager")
	ReviewManager reviewManager;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String token = (String) session.getAttribute("userToken");
		if (token == null)
			response.sendRedirect("index.jsp");
		else {
			long movieId = Long.parseLong((String) request
					.getAttribute("movieId"));
			long licenseId = Long.parseLong((String) request
					.getAttribute("licenseId"));
			String action = (String) request.getAttribute("action");
			if (action == "approve") {
				try {
					reviewManager.accept(token, movieId, licenseId);
					response.sendRedirect("reviewSuccessful.jsp");
				} catch (InvalidTokenException e) {
					response.sendRedirect("index.jsp");
				}
			}
			if (action == "reject") {
				try {
					reviewManager.reject(token, movieId, licenseId);
					response.sendRedirect("reviewSuccessful.jsp");
				} catch (InvalidTokenException e) {
					response.sendRedirect("index.jsp");
				}
			}
		}
	}

}
