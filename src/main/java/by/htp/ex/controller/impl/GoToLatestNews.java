package by.htp.ex.controller.impl;

import java.io.IOException;
import java.util.List;

import by.htp.ex.bean.News;
import by.htp.ex.controller.Command;
import by.htp.ex.service.INewsService;
import by.htp.ex.service.ServiceException;
import by.htp.ex.service.ServiceProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToLatestNews implements Command {

	private static final String JSP_ATRIBUTE_NEWS = "news";
	private static final String JSP_ATRIBUTE_PRESENTATION = "presentation";
	private static final String JSP_ATRIBUTE_ERROR = "errorMessage";
	private static final String JSP_ATRIBUTE_LATEST_NEWS = "latestNewsList";
	private static final String BASELAYOUT_PAGE_JSP = "WEB-INF/pages/layouts/baseLayout.jsp";
	private static final String ERROR_PAGE_JSP = "controller?command=go_to_error_page";

	private final INewsService newsService = ServiceProvider.getInstance().getNewsService();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		List<News> latestNews;
		try {
			latestNews = newsService.latestList(5);
			request.setAttribute(JSP_ATRIBUTE_NEWS, latestNews);
			request.setAttribute(JSP_ATRIBUTE_PRESENTATION, JSP_ATRIBUTE_LATEST_NEWS);
			request.getRequestDispatcher(BASELAYOUT_PAGE_JSP).forward(request, response);
		} catch (ServiceException e) {
			session.setAttribute(JSP_ATRIBUTE_ERROR, "can't go to latest news, please try again later");
			response.sendRedirect(ERROR_PAGE_JSP);
		}

	}

}
