package by.htp.ex.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final CommandProvider provider = new CommandProvider();
	private static final String STRING_COMMAND = "command";
	private static final String ERROR_PAGE_JSP = "WEB-INF/pages/tiles/error.jsp";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		executeCommand(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		executeCommand(request, response);

	}

	private void executeCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String commandName = request.getParameter(STRING_COMMAND);
			Command command = provider.getCommand(commandName);
			command.execute(request, response);

		} catch (Exception e) {
			request.getRequestDispatcher(ERROR_PAGE_JSP).forward(request, response);

		}

	}

}
