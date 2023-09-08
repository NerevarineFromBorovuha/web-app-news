package by.htp.ex.controller.impl;

import java.io.IOException;

import by.htp.ex.controller.Command;
import by.htp.ex.service.ServiceException;
import by.htp.ex.service.ServiceProvider;
import by.htp.ex.service.IUserService;
import by.htp.ex.util.validation.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DoSIgnIn implements Command {

	private final IUserService service = ServiceProvider.getInstance().getUserService();

	private static final String JSP_ATRIBUTE_USER = "user";
	private static final String JSP_GUEST_USER = "guest";
	private static final String JSP_ATRIBUTE_ERROR = "errorMessage";
	private static final String JSP_ATRIBUTE_ERROR_AUTH = "AuthenticationError";
	private static final String JSP_SESSION_NOT_ACTIVE = "not active";
	private static final String JSP_SESSION_ACTIVE = "active";
	private static final String JSP_SESSION_ROLE = "role";
	private static final String JSP_USER_LOGIN_PARAM = "login";
	private static final String JSP_USER_PASSWORD_PARAM = "password";

	private static final String GO_TO_NEWS_PAGE = "controller?command=go_to_news_list";
	private static final String ERROR_PAGE_JSP = "controller?command=go_to_error_page";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		String login;
		String password;

		login = request.getParameter(JSP_USER_LOGIN_PARAM);
		password = request.getParameter(JSP_USER_PASSWORD_PARAM);

		try {

			String role = service.signIn(login, password);

			if (!role.equals(JSP_GUEST_USER)) {
				request.getSession(true).setAttribute(JSP_ATRIBUTE_USER, JSP_SESSION_ACTIVE);
				request.getSession(true).setAttribute(JSP_SESSION_ROLE, role);
				response.sendRedirect(GO_TO_NEWS_PAGE);
			} else {
				request.getSession(true).setAttribute(JSP_ATRIBUTE_USER, JSP_SESSION_NOT_ACTIVE);
				request.setAttribute(JSP_ATRIBUTE_ERROR_AUTH, "wrong login or password");
				request.getRequestDispatcher(GO_TO_NEWS_PAGE).forward(request, response);
			}

		} catch (ValidationException e) {
			request.getSession(true).setAttribute(JSP_ATRIBUTE_USER, JSP_SESSION_NOT_ACTIVE);
			request.setAttribute(JSP_ATRIBUTE_ERROR_AUTH, "wrong login or password");
			request.getRequestDispatcher(GO_TO_NEWS_PAGE).forward(request, response);
		} catch (ServiceException e) {
			session.setAttribute(JSP_ATRIBUTE_ERROR, "can't sign in, please try again later ");
			response.sendRedirect(ERROR_PAGE_JSP);

		}

	}

}
