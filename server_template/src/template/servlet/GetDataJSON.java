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
		System.out.println("get json : " + originalStr);
		// -----------------------------------------------------------------

		/*****************************
		 * 클라이언트 >> 서버 : JSON *
		 *****************************/
		JSONObject jObj = (JSONObject) JSONValue.parse(originalStr);
		String args = (String) jObj.get("data");
		// -----------------------------------------------------------------

		/*****************************
		 * 서버 >> 클라이언트 : JSON *
		 *****************************/
		JSONArray jsonArr = new JSONArray();
		JSONObject returnJsonObj = null;

		returnJsonObj = new JSONObject();
		returnJsonObj.put("data", args);
		jsonArr.add(returnJsonObj);
		returnJsonObj = new JSONObject();
		returnJsonObj.put("test", args);
		returnJsonObj.put("test2", args);
		jsonArr.add(returnJsonObj);
		
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
