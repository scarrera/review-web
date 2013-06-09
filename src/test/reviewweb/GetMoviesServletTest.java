package reviewweb;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import edu.umflix.model.Movie;

public class GetMoviesServletTest {

	GetMoviesServlet servlet;
	HttpServletRequest request;
	HttpServletResponse response;
	ReviewManager reviewManager;
	HttpSession session;
	RequestDispatcher requestDispatcher;

	@Before
	public void prepareTestVariables() throws RoleNotFoundException,
			InvalidTokenException, IOException {
		servlet = new GetMoviesServlet();
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

		servlet.doGet(request, response);
		verify(request, times(1)).getSession(true);
		verify(reviewManager, never()).getMovieToReview(anyString());
	}

	@Test
	public void testSuccessfulMovieRetrievalEmptyList()
			throws ServletException, IOException, InvalidTokenException {
		when(session.getAttribute("userToken")).thenReturn("validToken");
		List<Movie> movies = new ArrayList<Movie>();
		when(reviewManager.getMovieToReview("validToken")).thenReturn(movies);

		servlet.doGet(request, response);
		verify(request, times(1)).getSession(true);
		verify(reviewManager, times(1)).getMovieToReview(anyString());
		verify(requestDispatcher, times(1)).forward(request, response);
	}

	@Test
	public void testSuccessfulMovieRetrievalNonEmptyList()
			throws ServletException, IOException, InvalidTokenException {
		when(session.getAttribute("userToken")).thenReturn("validToken");
		List<Movie> movies = new ArrayList<Movie>();
		Movie movie1 = mock(Movie.class);
		Movie movie2 = mock(Movie.class);
		movies.add(movie1);
		movies.add(movie2);
		when(reviewManager.getMovieToReview("validToken")).thenReturn(movies);

		servlet.doGet(request, response);
		verify(request, times(1)).getSession(true);
		verify(reviewManager, times(1)).getMovieToReview(anyString());
		verify(requestDispatcher, times(1)).forward(request, response);
	}

}
