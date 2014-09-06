package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BlossomStatComputing
 */
@WebServlet("/BlossomStatComputing")
public class BlossomStatComputing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger("blossom.server.servlet");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BlossomStatComputing() {
		super();
		logger.setLevel(Level.FINEST);
		logger.addHandler(new ConsoleHandler());
		System.out.println("creating StatComputingServlet");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DOGET");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DOPOST");
		final JsonReader jsonReader = Json.createReader(request.getReader());
		int max = 0;
		try {
			final JsonArray jsonArray = jsonReader.readArray();
			final Map<String, Integer> statMap = new HashMap<String, Integer>();
			for (final JsonValue jsonValue : jsonArray) {
				System.out.println(jsonValue.toString());
				if (jsonValue.getValueType().equals(ValueType.OBJECT)) {
					final JsonObject object = (JsonObject) jsonValue;
					final JsonValue nameValue = object.get("name");
					System.out.println(nameValue);
					if (nameValue == null)
						continue;
					final Integer computedSum = statMap.get(nameValue.toString());
					final int statSum = (computedSum == null) ? 1 : computedSum + 1;
					statMap.put(nameValue.toString(), statSum);
					max = Math.max(max, statSum);
				}
			}

			final JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
			final JsonObjectBuilder responseMapBuilder = Json.createObjectBuilder();

			for (final String name : statMap.keySet()) {
				responseMapBuilder.add(name, statMap.get(name));
			}
			responseBuilder.add("max", max);
			responseBuilder.add("map", responseMapBuilder);
			final JsonObject responseJsonObject = responseBuilder.build();

			final PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			final JsonWriter responseWriter = Json.createWriter(out);
			try {
				responseWriter.writeObject(responseJsonObject);
			} finally {
				responseWriter.close();
				out.flush();
			}
		} finally {
			jsonReader.close();
		}
	}
}
