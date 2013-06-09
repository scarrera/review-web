package reviewweb;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.exceptions.RoleNotFoundException;
import edu.umflix.model.Role;
import edu.umflix.model.User;
import edu.umflix.persistence.RoleDao;
import edu.umflix.usermanager.UserManager;
import edu.umflix.usermanager.exceptions.InvalidUserException;

/**
 * Servlet in charge of handling user authentication and authorization
 * 
 */
public class LoginServlet extends HttpServlet {

	static Logger logger = Logger.getLogger(LoginServlet.class);
	@EJB(beanName = "UserManager")
	UserManager userManager;
	@EJB(beanName = "AuthenticationHandler")
	AuthenticationHandler authenticationHandler;
	@EJB(beanName = "RoleDao")
	RoleDao roleDao;

	/**
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("Request intercepted; proceeding to retrieve email and password variables");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if (email == null || email == "" || password == null || password == "") {
			logger.info("Null or empty email and/or password");
			response.setHeader("Content-Type", "text/html");
			response.getOutputStream().print(
					"invalid combination of email and password");
			return;
		}
		logger.info("Email and password combination successfully retrieved; proceeding to validate user");
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);

		String userToken;
		try {
			userToken = userManager.login(user);
			logger.info("User successfully authenticated; proceeding to check authorization");
			if (authenticationHandler.isUserInRole(userToken,
					roleDao.getRoleById(Role.RoleType.ADMINISTRATOR.getRole()))
					|| authenticationHandler.isUserInRole(userToken, roleDao
							.getRoleById(Role.RoleType.REVIEWER.getRole()))) {
				logger.info("User belongs to allowed roles; proceeding to save token and email in session");
				HttpSession session = request.getSession(true);
				session.setAttribute("userToken", userToken);
				session.setAttribute("userEmail", email);
				logger.info("Redirecting user to movie list display page");
				response.sendRedirect("reviewer");
			} else {
				logger.info("User does not belong to allowed roles");
				response.setHeader("Content-Type", "text/html");
				response.getOutputStream().print(
						"You are not authorized to access this page");
			}
		} catch (InvalidUserException e) {
			logger.info("User entered invalid combination of email and password");
			response.setHeader("Content-Type", "text/html");
			response.getOutputStream().print(
					"invalid combination of email and password");
		} catch (InvalidTokenException e) {
			logger.error("Attempted to check user role with an invalid token");
			response.sendRedirect("error.jsp");
		} catch (RoleNotFoundException e) {
			logger.error("Attempted to check if user was part of an invalid role");
			response.sendRedirect("error.jsp");
		}
	}

}
