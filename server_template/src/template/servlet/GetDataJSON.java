package template.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Servlet implementation class GetData
 */
@WebServlet(name = "GetDataJSON", urlPatterns = { "/GetDataJSON" })
public class GetDataJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetDataJSON() {
		super();
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		/*************************************
		 * 클라이언트로부터 JSON 데이터 받음 *
		 *************************************/
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String originalStr = "";
		if (br != null)
			originalStr = br.readLine();
		System.out.println("test");
		System.out.println("jsonstr : " + originalStr);
		// -----------------------------------------------------------------

		/*****************************
		 * 클라이언트 >> 서버 : JSON *
		 *****************************/
		JSONObject jObj = (JSONObject) JSONValue.parse(originalStr);
		String args = (String) jObj.get("key");
		// -----------------------------------------------------------------

		/*****************************
		 * 서버 >> 클라이언트 : JSON *
		 *****************************/
		JSONArray jsonArr = new JSONArray();
		JSONObject tmpObj = null;

		JSONObject resultJSON = new JSONObject();

		tmpObj = new JSONObject();
		tmpObj.put("arg1", "data");
		tmpObj.put("arg2", "data");
		jsonArr.add(tmpObj);

		out.println(jsonArr.toJSONString());
		// -----------------------------------------------------------------

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
