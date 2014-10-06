package us.monoid.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import com.google.gson.*;


/** A resource presentation in JSON format.
 * You can  ask Resty to parse the JSON into a JsonArray or a JsonObject. The JsonObject is similar to org.json.JsonObject 
 * and allows full access to the JSON.
 * <p />
 * @author beders
 * @author RobertFischer
 */
public class JsonResource extends AbstractResource {

	private JsonElement json;
    private Gson gson;
	
	public JsonResource(Option... options) {
		this(null, options);
	}

    public JsonResource(Gson gson, Option... options) {
        super(options);
        this.gson = gson != null ? gson : DEFAULT_GSON;
    }

	/**
	* Parse and return JSON array. Parsing is done only once after which the inputStream is at EOF.
	*/
	public JsonArray array() throws IOException, JsonParseException {
		if (json == null) unmarshal();
        return json.getAsJsonArray();
	}

	/** 
	 * Parse and return JSON object. Parsing is done only once after which the inputStrem is at EOF.
	 * @return the JSON object
	 * @throws IOException
	 * @throws JsonParseException
	 */
	public JsonObject object() throws IOException, JsonParseException {
		if (json == null) unmarshal();
		return json.getAsJsonObject();
	}

    /**
     * Wrapper for Gson.fromJson(JsonElement, Class<T>).
     */
    public <T> T object(Class<T> classOfT) throws IOException, JsonSyntaxException {
        if (json == null) unmarshal();
        return gson.fromJson(json, classOfT);
    }

    /**
     * Wrapper for Gson.fromJson(JsonElement, Type).
     */
    @SuppressWarnings("unchecked")
    public <T> T object(Type typeOfT) throws IOException, JsonSyntaxException {
        if (json == null) unmarshal();
        return gson.fromJson(json, typeOfT);
    }
	
	/** Added for compatibility with Scala. See Issue #2 at github.
	 * 
	 * @return the JsonObject presentation
	 * @throws IOException 
	 * @throws JsonParseException if data was no valid JSON
	 */
	public JsonObject toObject() throws IOException, JsonParseException {
		return object();
	}

	/** Transforming the JSON on the fly */
	protected JsonElement unmarshal() throws IOException, JsonParseException {
        try {
            json = new JsonParser().parse(new InputStreamReader(inputStream, "UTF-8"));
            return json;
        }
        finally {
            inputStream.close();
        }
	}

	@Override
	public String getAcceptedTypes() {
		return "application/json";
	}
}
