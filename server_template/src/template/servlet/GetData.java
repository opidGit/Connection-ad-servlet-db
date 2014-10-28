package template.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import template.dao.TemplateDao;

/**
 * Servlet implementation class GetData
 */
@WebServlet(name = "GetData", urlPatterns = { "/GetData" })
public class GetData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetData() {
		super();
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		/* Get data from Android. */
		String data = request.getParameter("data");
		
		/* Send a data to Android. */ 
		out.println("'" + data + "' success");

		/* DB query */
		TemplateDao db = new TemplateDao();
 		//int getData = db.getData(0);
		
		System.out.println(data);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
