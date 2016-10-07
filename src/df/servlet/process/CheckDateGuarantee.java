package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.obj.util.JDate;

/**
 * Servlet implementation class CheckDateGuarantee
 */
public class CheckDateGuarantee extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckDateGuarantee() {
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
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml; charset=UTF-8");

		PrintWriter out = response.getWriter();
		out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		request.setCharacterEncoding("UTF-8");
		
		String date_start = JDate.saveDate(request.getParameter("start_date"))+JDate.saveTime(request.getParameter("start_time"));
		String date_end =  JDate.saveDate(request.getParameter("end_date"))+JDate.saveTime(request.getParameter("end_time"));
		
		try {
			if(Double.parseDouble(date_start) <= Double.parseDouble(date_end)){
				out.print("<RESULT><STATUS>pass</STATUS></RESULT>");
			} else {
				out.print("<RESULT><STATUS>fail</STATUS></RESULT>");
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
