package reviewweb;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.umflix.model.User;
import edu.umflix.usermanager.UserManager;
import edu.umflix.usermanager.exceptions.InvalidUserException;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
	@EJB(beanName = "UserManager")
	UserManager userManager;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		User user = new User();
		user.setEmail(email);
		user.setPassword(password);

		String userToken;
		try {
			userToken = userManager.login(user);
			HttpSession session = request.getSession(true);
			session.setAttribute("userToken", userToken);
			session.setAttribute("userEmail", email);
			response.sendRedirect("reviewer");
		} catch (InvalidUserException e) {
			response.setHeader("Content-Type", "text/html");
			response.getOutputStream().print(
					"invalid combination of email and password");
		}
	}

}
