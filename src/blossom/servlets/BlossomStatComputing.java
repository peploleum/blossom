package blossom.servlets;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger LOGGER = Logger.getLogger("blossom.server.servlet");

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlossomStatComputing() {
        super();
        LOGGER.setLevel(Level.FINEST);
        LOGGER.addHandler(new ConsoleHandler());
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

        // old example to read and write json with javax.json
        // final JsonReader jsonReader = Json.createReader(request.getReader());
        // int max = 0;
        // try {
        // final JsonArray jsonArray = jsonReader.readArray();
        // final Map<String, Integer> statMap = new HashMap<String, Integer>();
        // for (final JsonValue jsonValue : jsonArray) {
        // if (jsonValue.getValueType().equals(ValueType.OBJECT)) {
        // final JsonObject object = (JsonObject) jsonValue;
        // final JsonValue nameValue = object.get("name");
        // if (nameValue == null)
        // continue;
        // final Integer computedSum = statMap.get(nameValue.toString());
        // final int statSum = (computedSum == null) ? 1 : computedSum + 1;
        // statMap.put(nameValue.toString(), statSum);
        // max = Math.max(max, statSum);
        // }
        // }
        //
        // final JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
        // final JsonObjectBuilder responseArrayItem =
        // Json.createObjectBuilder();
        // final JsonArrayBuilder responseArray = Json.createArrayBuilder();
        // for (final String name : statMap.keySet()) {
        // responseArrayItem.add("name", name);
        // responseArrayItem.add("stat", statMap.get(name));
        // responseArray.add(responseArrayItem);
        // }
        // responseBuilder.add("max", max);
        // responseBuilder.add("map", responseArray);
        // final JsonObject responseJsonObject = responseBuilder.build();
        //
        // final PrintWriter out = response.getWriter();
        // response.setContentType("application/json");
        // final JsonWriter responseWriter = Json.createWriter(out);
        // try {
        // responseWriter.writeObject(responseJsonObject);
        // } finally {
        // responseWriter.close();
        // out.flush();
        // }
        // } finally {
        // jsonReader.close();
        // }
    }
}
