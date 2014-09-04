package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BlossomStatComputing() {
		super();
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
		BufferedReader inputStream = request.getReader();
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[128];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) > 0) {
			sb.append(buffer, 0, bytesRead);
		}
		System.out.println(sb.toString());

		final String jsonObject = "{\"requestlrequesteela\":1,\"nibbler\":2}";
		// we flush a simple example down the drain, like a turd
		response.setContentType("application/json");
		final PrintWriter out = response.getWriter();
		out.print(jsonObject);
		out.flush();
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
		BufferedReader inputStream = request.getReader();
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[128];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) > 0) {
			sb.append(buffer, 0, bytesRead);
		}
		System.out.println(sb.toString());
	}

}
