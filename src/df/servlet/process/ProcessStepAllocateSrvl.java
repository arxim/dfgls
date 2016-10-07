package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import df.bean.process.ProcessStepCalculate;

/**
 * Servlet implementation class ProcessStepAllocateSrvl
 */
public class ProcessStepAllocateSrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
    	request.setCharacterEncoding("UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("text/JSON");
    	HashMap<String, ProcessStepCalculate> hasMapPSC = new HashMap<String, ProcessStepCalculate>();
		PrintWriter out = response.getWriter();
		JSONObject voObject = new JSONObject();
        String sessionId = request.getParameter("session_id");
        String doctorCategoryCode = request.getParameter("doctor_category_code");
        String hospitalCode = request.getParameter("hospital_code");
        String doctorCode = request.getParameter("doctor_code");
        String month = request.getParameter("month");
        String year = request.getParameter("year");
          
        try {
        	boolean blnExists = hasMapPSC.containsKey(sessionId);
        	if (!blnExists) {
        		ProcessStepCalculate psc = new ProcessStepCalculate();
        		hasMapPSC.put(sessionId, psc);
        	}
        		
        	hasMapPSC.get(sessionId).setDoctorCode(doctorCode);
        	hasMapPSC.get(sessionId).setHospitalCode(hospitalCode);
        	hasMapPSC.get(sessionId).setMonth(month);
        	hasMapPSC.get(sessionId).setYear(year);
        	hasMapPSC.get(sessionId).doProcess();
        	
        	voObject.put("STATUS", "true");
        }catch (Exception e) {
            voObject.put("STATUS", "false");
        }finally { 
           	hasMapPSC.remove(sessionId);
        }
            out.print(voObject.toJSONString());
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}