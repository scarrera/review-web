package reviewweb;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.um.umflix.reviewmanager.ReviewManager;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.exceptions.RoleNotFoundException;

public class ReviewMovieServletTest {

	reviewweb.ReviewMovieServlet servlet;
	HttpServletRequest request;
	HttpServletResponse response;
	ReviewManager reviewManager;
	HttpSession session;
	RequestDispatcher requestDispatcher;

	@Before
	public void prepareTestVariables() throws RoleNotFoundException,
			InvalidTokenException, IOException {
		servlet = new ReviewMovieServlet();
		reviewManager = mock(ReviewManager.class);

		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		ServletOutputStream stream = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(stream);

		session = mock(HttpSession.class);
		when(request.getSession(anyBoolean())).thenReturn(session);
		when(request.getSession()).thenReturn(session);

		requestDispatcher = mock(RequestDispatcher.class);
		when(request.getRequestDispatcher(anyString())).thenReturn(
				requestDispatcher);

		servlet.setReviewManager(reviewManager);
	}

	@Test
	public void testNoTokenInSession() throws ServletException, IOException,
			InvalidTokenException {
		when(session.getAttribute("userToken")).thenReturn(null);

		servlet.doPost(request, response);
		verify(request, times(1)).getSession(true);
		verify(response, times(1)).sendRedirect(anyString());
		verify(request, never()).getAttribute(anyString());
	}

	@Test
	public void testNullAttributesInRequest() throws ServletException,
			IOException, InvalidTokenException {
		when(session.getAttribute("userToken")).thenReturn("validToken");
		when(request.getParameter(anyString())).thenReturn(null);
		servlet.doPost(request, response);
		verify(request, times(1)).getSession(true);
		verify(response, times(1)).sendRedirect(anyString());
		verify(reviewManager, never())
				.accept(anyString(), anyLong(), anyLong());
		verify(reviewManager, never())
				.reject(anyString(), anyLong(), anyLong());
	}

	@Test
	public void testSuccessfulApproveLicenseForMovie() throws ServletException,
			IOException, InvalidTokenException {
		when(session.getAttribute("userToken")).thenReturn("validToken");
		when(request.getParameter("movieId")).thenReturn("1");
		when(request.getParameter("licenseId")).thenReturn("1");
		when(request.getParameter("action")).thenReturn("approve");
		servlet.doPost(request, response);
		verify(request, times(1)).getSession(true);
		verify(response, times(1)).sendRedirect(anyString());
		verify(reviewManager, times(1)).accept(anyString(), anyLong(),
				anyLong());
		verify(reviewManager, never())
				.reject(anyString(), anyLong(), anyLong());
	}

	@Test
	public void testSuccessfulRejectLicenseForMovie() throws ServletException,
			IOException, InvalidTokenException {
		when(session.getAttribute("userToken")).thenReturn("validToken");
		when(request.getParameter("movieId")).thenReturn("1");
		when(request.getParameter("licenseId")).thenReturn("1");
		when(request.getParameter("action")).thenReturn("reject");
		servlet.doPost(request, response);
		verify(request, times(1)).getSession(true);
		verify(response, times(1)).sendRedirect(anyString());
		verify(reviewManager, never())
				.accept(anyString(), anyLong(), anyLong());
		verify(reviewManager, times(1)).reject(anyString(), anyLong(),
				anyLong());
	}

}
