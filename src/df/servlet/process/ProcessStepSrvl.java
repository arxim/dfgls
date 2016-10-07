package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.process.ProcessStepCalculate;

/**
 * Servlet implementation class ProcessStepSrvl
 */
public class ProcessStepSrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * this is process here 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/xml; charset=UTF-8");
		PrintWriter out = response.getWriter();
	    out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	    request.setCharacterEncoding("UTF-8");
	    String userId = request.getParameter("USER");
	    String hospital = request.getParameter("HOSPITAL_CODE");
	    String doctor = request.getParameter("DOCTOR_CODE");
	    String doctorCategory = request.getParameter("DOCTOR_CATEGORY_CODE");
	    String rowId = request.getParameter("REC_NO");
	    String month = request.getParameter("MM");
	    String year = request.getParameter("YYYY");
	    int numAffRec = 1;
	    ProcessStepCalculate psc = new ProcessStepCalculate();
	    System.out.println("userId ="+userId+" hospital ="+hospital+" doctor ="+doctor+" row ="+rowId);

	    try {
		    psc.setDoctorCode(doctor);
		    psc.setDoctorCategory(doctorCategory);
		    psc.setHospitalCode(hospital);
		    psc.setUserId(userId);
		    psc.setMonth(month);
		    psc.setYear(year);
		    if(rowId.equals("2")){
		    	if(psc.doRollback()){
		    		numAffRec = psc.doProcess() ? 1 : 0;
		    	}else{
		    		numAffRec = 0;
		    	}
		    }else{
		    	numAffRec = psc.doProcess() ? 1 : 0;
		    }
		    //if(psc.doProcess()){ numAffRec = 1; }else{ numAffRec = 0; }
	        out.print("<RESULT><SUCCESS>" + numAffRec + "</SUCCESS></RESULT>");
	    }catch (Exception  e) {
	    	e.printStackTrace(out);
	    }finally { 
	        out.close();
	    }
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
		
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		processRequest(request , response);
	}


}
