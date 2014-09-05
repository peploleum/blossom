package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.json.JsonValue;
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

	private static final Logger logger = Logger
			.getLogger("blossom.server.servlet");
	private static String dataSet = "";

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
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		System.out.println("DOGET");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		System.out.println("DOPOST");
		// final BufferedReader inputStream = request.getReader();
		// final StringBuilder sb = new StringBuilder();
		// final char[] buffer = new char[128];
		// int bytesRead = -1;
		// while ((bytesRead = inputStream.read(buffer)) > 0) {
		// sb.append(buffer, 0, bytesRead);
		// }
		// final String string = sb.toString();
		// final String msg = "received " + string;
		// logger.fine(msg);
		// System.out.println(msg);
		JsonReader createReader = Json.createReader(request.getReader());
		JsonArray jsonArray = createReader.readArray();
		for (JsonValue jsonValue : jsonArray) {
			System.out.println(jsonValue.toString());
		}
		// JsonArray jsonObject2 = jsonArray.getJsonArray(1);

		dataSet = "";
		final String jsonObject = (dataSet == null || dataSet.isEmpty()) ? "{}"
				: dataSet;
		// we flush a simple example down the drain, like a turd
		response.setContentType("application/json");
		final PrintWriter out = response.getWriter();
		out.print(jsonObject);
		out.flush();
	}

}
