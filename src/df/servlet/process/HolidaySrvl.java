package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import df.bean.process.ProcessHolidayCalculate;

/**
 * Servlet implementation class HolidaySrvl
 */
public class HolidaySrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HolidaySrvl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}
	public void doProcess(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		System.out.println("e");
		PrintWriter out = response.getWriter();
		ProcessHolidayCalculate test_holiday = new ProcessHolidayCalculate();
		test_holiday.processHoliday(request.getParameter("HOSPITAL_CODE"), request.getParameter("YYYY"), request.getParameter("MM"));
		//test_holiday.dataRollBack(request.getParameter("HOSPITAL_CODE"), request.getParameter("YYYY"), request.getParameter("MM"));
		out.print(test_holiday.getMsg());
	}

}
