package reviewweb;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.exceptions.RoleNotFoundException;
import edu.umflix.model.Role;
import edu.umflix.model.User;
import edu.umflix.persistence.RoleDao;
import edu.umflix.usermanager.UserManager;
import edu.umflix.usermanager.exceptions.InvalidUserException;

public class LoginServletTest {

	LoginServlet servlet;
	HttpServletRequest request;
	HttpServletResponse response;
	UserManager userManager;
	AuthenticationHandler authenticationHandler;
	RoleDao roleDao;
	HttpSession session;

	@Before
	public void prepareTestVariables() throws RoleNotFoundException,
			InvalidTokenException, IOException {
		servlet = new LoginServlet();

		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		userManager = mock(UserManager.class);
		authenticationHandler = mock(AuthenticationHandler.class);
		roleDao = mock(RoleDao.class);

		ServletOutputStream stream = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(stream);

		session = mock(HttpSession.class);
		when(request.getSession(anyBoolean())).thenReturn(session);
		when(request.getSession()).thenReturn(session);

		// prepare mock roleDao
		Role adminRole = mock(Role.class);
		when(adminRole.getId()).thenReturn((long) 1);
		Role userRole = mock(Role.class);
		when(userRole.getId()).thenReturn((long) 2);
		Role movieProvider = mock(Role.class);
		when(movieProvider.getId()).thenReturn((long) 3);
		Role adProvider = mock(Role.class);
		when(adProvider.getId()).thenReturn((long) 4);
		Role reviewer = mock(Role.class);
		when(reviewer.getId()).thenReturn((long) 5);
		when(roleDao.getRoleById(Role.RoleType.ADMINISTRATOR.getRole()))
				.thenReturn(adminRole);
		when(roleDao.getRoleById(Role.RoleType.USER.getRole())).thenReturn(
				userRole);
		when(roleDao.getRoleById(Role.RoleType.MOVIE_PROVIDER.getRole()))
				.thenReturn(movieProvider);
		when(roleDao.getRoleById(Role.RoleType.REVIEWER.getRole())).thenReturn(
				reviewer);
		when(roleDao.getRoleById(Role.RoleType.AD_PROVIDER.getRole()))
				.thenReturn(adProvider);

		servlet.setAuthenticationHandler(authenticationHandler);
		servlet.setRoleDao(roleDao);
		servlet.setUserManager(userManager);
	}

	@Test
	public void testNullAttributesInRequest() throws ServletException,
			IOException, InvalidUserException {
		servlet.doPost(request, response);
		verify(response, times(1)).getOutputStream();
		verify(userManager, never()).login((User) anyObject());
	}

	@Test
	public void testWrongEmailPasswordCombination() throws ServletException,
			IOException, InvalidUserException, InvalidTokenException {
		when(request.getParameter("email")).thenReturn("wrongEmail");
		when(request.getParameter("password")).thenReturn("wrongPassword");

		when(userManager.login((User) anyObject())).thenThrow(
				InvalidUserException.class);

		servlet.doPost(request, response);
		verify(userManager, times(1)).login((User) anyObject());
		verify(authenticationHandler, never()).isUserInRole(anyString(),
				(Role) anyObject());
	}

	@Test
	public void testUserNotAllowedRole() throws ServletException, IOException,
			InvalidUserException, InvalidTokenException {
		when(request.getParameter("email")).thenReturn("email");
		when(request.getParameter("password")).thenReturn("password");

		when(userManager.login((User) anyObject()))
				.thenReturn("validTokenUser");

		when(
				authenticationHandler.isUserInRole(anyString(),
						(Role) anyObject())).thenReturn(false);

		servlet.doPost(request, response);
		verify(userManager, times(1)).login((User) anyObject());
		verify(authenticationHandler, times(2)).isUserInRole(anyString(),
				(Role) anyObject());
		verify(request, times(0)).getSession();
	}

	@Test
	public void testSuccessfulLogin() throws ServletException, IOException,
			InvalidUserException, InvalidTokenException {
		when(request.getParameter("email")).thenReturn("email");
		when(request.getParameter("password")).thenReturn("password");

		when(userManager.login((User) anyObject()))
				.thenReturn("validTokenUser");

		when(
				authenticationHandler.isUserInRole(anyString(),
						(Role) anyObject())).thenReturn(true);

		servlet.doPost(request, response);
		verify(userManager, times(1)).login((User) anyObject());
		verify(authenticationHandler, times(1)).isUserInRole(anyString(),
				(Role) anyObject());
		verify(request, times(1)).getSession(true);
		verify(session, times(2)).setAttribute(anyString(), anyObject());
	}
}
