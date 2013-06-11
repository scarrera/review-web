package reviewweb;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.exceptions.RoleNotFoundException;
import edu.umflix.usermanager.exceptions.InvalidUserException;

public class LogoutServletTest {

	reviewweb.LogoutServlet servlet;
	HttpServletRequest request;
	HttpServletResponse response;

	@Before
	public void prepareTestVariables() throws RoleNotFoundException,
			InvalidTokenException, IOException {
		servlet = new LogoutServlet();

		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
	}

	@Test
	public void testSessionInitialized() throws ServletException, IOException,
			InvalidUserException {
		HttpSession session = mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(session);

		servlet.doGet(request, response);
		verify(request, times(1)).getSession(false);
		verify(session, times(1)).invalidate();
	}

	@Test
	public void testSessionNotInitialized() throws ServletException,
			IOException, InvalidUserException {
		when(request.getSession(false)).thenReturn(null);

		servlet.doGet(request, response);
		verify(request, times(1)).getSession(false);
	}

}
