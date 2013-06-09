package reviewweb.adapters;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.umflix.model.License;
import edu.umflix.model.Movie;

/**
 * Class in charge of serializing and deserializing Movie objects to json
 * 
 * @see Movie
 * 
 */
public class MovieAdapter implements JsonSerializer<Movie>,
		JsonDeserializer<Movie> {
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	/**
	 * Serializes movie object to json representation
	 * 
	 * @see Movie
	 * @see JsonElement
	 */
	@Override
	public JsonElement serialize(Movie movie, Type type,
			JsonSerializationContext jsc) {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("movie_id", movie.getId());
		jsonObject.addProperty("movie_title", movie.getTitle());

		JsonArray licenses = new JsonArray();
		for (License license : movie.getLicenses()) {
			JsonObject licenseJson = new JsonObject();
			licenseJson.addProperty("license_id", license.getId());
			licenseJson.addProperty("license_name", license.getName());
			licenseJson.addProperty("license_description",
					license.getDescription());
			licenseJson.addProperty("license_startDate",
					df.format(license.getStartDate()));
			licenseJson.addProperty("license_endDate",
					df.format(license.getEndDate()));
			licenseJson.addProperty("license_costPerMinute",
					license.getCostPerMinute());
			licenseJson.addProperty("license_maxViews", license.getMaxViews());

			licenses.add(licenseJson);
		}
		jsonObject.add("movie_licenses", licenses);
		return jsonObject;
	}

	/**
	 * Deserializes a json element to a Movie object
	 * 
	 * @see Movie
	 * @see JsonElement
	 */
	@Override
	public Movie deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		Movie movie = new Movie();

		JsonObject jsonObject = json.getAsJsonObject();
		movie.setId(jsonObject.get("movie_id").getAsLong());
		movie.setTitle(jsonObject.get("movie_title").getAsString());
		JsonArray licensesJsonArray = jsonObject
				.getAsJsonArray("movie_licenses");
		List<License> licenses = new ArrayList<License>();
		for (JsonElement element : licensesJsonArray) {
			JsonObject licenseJson = element.getAsJsonObject();
			License license = new License();
			license.setId(licenseJson.get("license_id").getAsLong());
			license.setName(licenseJson.get("license_name").getAsString());
			license.setDescription(licenseJson.get("license_description")
					.getAsString());
			try {
				license.setStartDate(df.parse(licenseJson.get(
						"license_startDate").getAsString()));
				license.setEndDate(df.parse(licenseJson.get("license_endDate")
						.getAsString()));
			} catch (ParseException e) {

			}
			license.setCostPerMinute(licenseJson.get("license_costPerMinute")
					.getAsLong());
			license.setMaxViews(licenseJson.get("license_maxViews").getAsLong());
			licenses.add(license);
		}
		movie.setLicenses(licenses);
		return movie;
	}

}
