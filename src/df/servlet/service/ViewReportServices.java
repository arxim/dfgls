package df.servlet.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import df.bean.db.conn.DBConnection;

/**
 * Servlet implementation class ViewReportServices
 */
public class ViewReportServices extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewReportServices() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		procesRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		procesRequest(request, response);
	}

	public void procesRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		
		String hospitalCode = request.getParameter("HOSPITAL_CODE");
		String term = request.getParameter("TERM");
		String month = request.getParameter("MONTH");
		String year = request.getParameter("YEAR");

		DBConnection objConn = new DBConnection();

		PrintWriter out = response.getWriter();

		JSONObject obj = new JSONObject();

		try {

			objConn.connectToLocal();
			ResultSet rs = objConn.executeQuery(getSQLCheck(hospitalCode, term,
					month, year));
			
			System.out.println(getSQLCheck(hospitalCode, term,
					month, year));

			if (rs.next()) {
				
				if (rs.getString("C").equals("0")) {
					obj.put("rs", "YES");
				} else {
					obj.put("rs", "NO");
				}
			}

			out.print(obj.toJSONString());

		} catch (Exception ex) {
			obj.put("rs", " ตรงนี้นะ " +ex.toString());
			out.print(obj.toJSONString());
		}

	}

	private String getSQLCheck(String hospitalCode, String term, String month,
			String year) {
		return "SELECT COUNT(*) AS C FROM SUMMARY_PAYMENT WHERE "
				+ " HOSPITAL_CODE = '" + hospitalCode + "' AND YYYY + MM = '"
				+ year + month + "'" + " AND PAYMENT_TERM = '" + term + "' AND BATCH_NO <> ''";
	}

}
