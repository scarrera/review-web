package reviewweb;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet in charge of user logout
 * 
 */
public class LogoutServlet extends HttpServlet {

	static Logger logger = Logger.getLogger(LogoutServlet.class);

	/**
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("Request intercepted; proceeding to check if session exists");
		HttpSession session = request.getSession(false);
		if (session != null) {
			logger.info("Session exists; proceeding to destroy it");
			session.invalidate();
		}
		logger.info("Successfully logged out; redirecting to index page");
		response.sendRedirect("index.jsp");
	}

}
