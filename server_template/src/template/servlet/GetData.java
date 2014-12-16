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
 * 
 * @author yeongjun
 * @since 2014.12.16
 *
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
		// 인코딩 처리
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out = response.getWriter();

		// HTTP 방식 확인 메시지
		System.out.println((request.getMethod() == "GET" ? "GET" : "POST")
				+ "방식");

		/******************************************
		 * 클라이언트로부터 받은 파라미터 값 받기 *
		 ******************************************/
		String data = request.getParameter("data");
		System.out.println("Data : " + data);

		/*****************************************
		 * 서버에서 클라이언트로 데이터 전송하기 *
		 *****************************************/
		out.println("'" + data + "' success");

		/*************************
		 * 데이터베이스 접근하기 *
		 *************************/
		TemplateDao db = new TemplateDao();
		// int getData = db.getData(0);) {

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
