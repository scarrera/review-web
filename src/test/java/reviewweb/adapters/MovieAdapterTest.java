package reviewweb.adapters;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

import edu.umflix.model.License;
import edu.umflix.model.Movie;

public class MovieAdapterTest {

	reviewweb.adapters.MovieAdapter movieAdapter;

	@Before
	public void prepareTestVariables() {
		movieAdapter = new MovieAdapter();
	}

	@Test
	public void testSerializationDeserialization() {
		Movie movie = new Movie();
		movie.setId((long) 1);
		movie.setTitle("movie title");

		List<License> licenses = new ArrayList<License>();
		License license1 = new License();
		license1.setId((long) 1);
		license1.setCostPerMinute((long) 2);
		license1.setDescription("description 1");
		Calendar c = Calendar.getInstance();
		c.set(2013, 8, 1);
		license1.setEndDate(c.getTime());
		license1.setMaxViews((long) 1);
		license1.setName("license 1");
		c = Calendar.getInstance();
		c.set(2012, 8, 1);
		license1.setStartDate(c.getTime());

		License license2 = new License();
		license2.setId((long) 2);
		license2.setCostPerMinute((long) 3);
		license2.setDescription("description 2");
		c = Calendar.getInstance();
		c.set(2013, 5, 4);
		license2.setEndDate(c.getTime());
		license2.setMaxViews((long) 10);
		license2.setName("license 2");
		c = Calendar.getInstance();
		c.set(2012, 4, 3);
		license2.setStartDate(c.getTime());

		licenses.add(license1);
		licenses.add(license2);
		movie.setLicenses(licenses);

		JsonElement jsonMovie = movieAdapter.serialize(movie,
				(Type) anyObject(), (JsonSerializationContext) anyObject());
		Movie deserializedMovie = movieAdapter.deserialize(jsonMovie,
				(Type) anyObject(), (JsonDeserializationContext) anyObject());

		assertEquals(movie.getId(), deserializedMovie.getId());
		assertEquals(movie.getTitle(), deserializedMovie.getTitle());
		for (int i = 0; i < licenses.size(); i++) {
			License license = licenses.get(i);
			License deserializedLicense = deserializedMovie.getLicenses()
					.get(i);
			assertEquals(license.getCostPerMinute(),
					deserializedLicense.getCostPerMinute());
			assertEquals(license.getDescription(),
					deserializedLicense.getDescription());
			assertEquals(license.getEndDate().toString(), deserializedLicense
					.getEndDate().toString());
			assertEquals(license.getId(), deserializedLicense.getId());
			assertEquals(license.getMaxViews(),
					deserializedLicense.getMaxViews());
			assertEquals(license.getName(), deserializedLicense.getName());
			assertEquals(license.getStartDate().toString(), deserializedLicense
					.getStartDate().toString());
		}
	}
}
