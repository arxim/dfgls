package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.StpTranferDfModel;
import df.bean.obj.util.JDate;
import df.bean.process.ProcessTranferRevenue;

/**
 * Servlet implementation class ProcessDfTranferRevenueController
 */
public class ProcessDfTranferRevenueController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessDfTranferRevenueController() {
        super();
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
	
	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String  controllerAction  =  request.getParameter("ControllerAction");
		
		if(controllerAction.equals("actionAdd")){ 
			  actionAdd( request, response);
		} else if(controllerAction.equals("actionDelete")) { 
			  actionDelete( request,  response);
		} else if(controllerAction.equals("actionEdite")){ 
			  actionEdite( request,  response);
		} else if(controllerAction.equals("actionUpdate")){ 
			  actionUpdate( request,  response);
		} else if(controllerAction.equals("actionProcess")) {
			  actionProcess( request,  response );
		} else if(controllerAction.equals("actionView")){ 
			  actionView( request,  response );
		}
	}

	private void actionView(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException  {
		
		
		PrintWriter out = response.getWriter();
		
		String hospitalCode = request.getSession().getAttribute("HOSPITAL_CODE").toString();
		
		List<StpTranferDfModel> models = null;
		
		DBConnection conn = new DBConnection();
		conn.connectToLocal();
		
		String  sqlCommand  =  "SELECT * FROM  STP_TRANFER_DF WHERE HOSPITAL_CODE = '" + hospitalCode + "'";
		
		ResultSet rs =  conn.executeQuery(sqlCommand);
		
		 try {
			
			 while(rs.next()){
				 	
				 	// Model  StpTranferDfModel
					StpTranferDfModel  model =  new StpTranferDfModel();
					
					model.setDoctorFrom(rs.getString("DOCTOR_FROM"));
					model.setDoctorFromName(rs.getString("DOCTOR_FROM_NAME"));
					model.setDoctorTo(rs.getString("DOCTOR_TO"));
					model.setDoctorToName(rs.getString("DOCTOR_TO_NAME"));
					model.setAdmissionType(rs.getString("ADMISSION_TYPE"));
					
					models.add(model);
			 }
			 
			
			 
		} catch (Exception e) {
		
		}
		 
		 request.setAttribute("models", models);
		 RequestDispatcher requestDispatcher  =  request.getRequestDispatcher("/forms/input/stp_df_tranfer.jsp");
		 requestDispatcher.forward(request, response);

	}

	/**
	 * This is actonProcess Controller data.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void actionProcess(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		String hospitalCode =  request.getSession().getAttribute("HOSPITAL_CODE").toString();
		
		String userId  =  request.getSession().getAttribute("USER_ID").toString();
		
		String startDate =  JDate.saveDate(request.getParameter("startDate"));

		String endDate  = JDate.saveDate(request.getParameter("endDate"));
		
		JSONObject objJson = new JSONObject();
		PrintWriter out = response.getWriter();
		
		/**
         * @TODO this process tranfer df
         */
        ProcessTranferRevenue objRevenueDf = new  ProcessTranferRevenue(hospitalCode, userId , startDate, endDate);
        
	     /**
	      * @TODO do Process transaction df tranfer.
	      */
       
		 if( objRevenueDf.doProcess()){ 
			 objJson.put("status", "SUCCESS");
		 } else { 
			 objJson.put("status", "FAIL");
		 }
			
		out.print(objJson.toJSONString());
        
	}

	private void actionUpdate(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		
		//response.setContentType("text/html;charset=UTF-8");
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		 
		
		JSONObject objJson = new JSONObject();
		PrintWriter out = response.getWriter();
		
		String hospitalCode = request.getSession().getAttribute("HOSPITAL_CODE").toString();
		
		String doctorFrom = request.getParameter("doctorFrom");
		String doctorFromName  = new String(request.getParameter("doctorFromName").getBytes("ISO8859_1"), "UTF-8"); 
		String doctorTo = request.getParameter("doctorTo");
		String doctorToName  =  new String(request.getParameter("doctorToName").getBytes("ISO8859_1"), "UTF-8");
		String admissionType =  request.getParameter("admissionType");
		
		String doctorFromOld  = request.getParameter("doctorFromOld");   
		String doctorToOld = request.getParameter("doctorToOld");
		String admissionTypeOld = request.getParameter("admissionTypeOld");
		
		String  sqlCommand = "UPDATE STP_TRANFER_DF SET  " + 
							 " DOCTOR_FROM = '" + doctorFrom + "' , " + 
							 " DOCTOR_FROM_NAME = '" + doctorFromName + "', " + 
							 " DOCTOR_TO = '" + doctorTo + "' , "  +
							 " DOCTOR_TO_NAME = '" + doctorToName + "' ," + 
							 " ADMISSION_TYPE  = '"+ admissionType + "'" + 
							 " WHERE DOCTOR_FROM  = '" + doctorFromOld+ "'" + 
							 " AND DOCTOR_TO = '"+ doctorToOld +"'" + 
							 " AND ADMISSION_TYPE = '" + admissionTypeOld + "' " + 
							 " AND HOSPITAL_CODE = '" + hospitalCode + "'";
			
		System.out.println(sqlCommand);
		    
		DBConnection objConn = new DBConnection();
		objConn.connectToLocal();
			
			 if(objConn.executeUpdate(sqlCommand) != -1){ 
				 objJson.put("status", "SUCCESS");
			 } else { 
				 objJson.put("status", "FAIL");
			 }
			
		out.print(objJson.toJSONString());
		
	}

	private void actionEdite(HttpServletRequest request, HttpServletResponse response)   throws ServletException, IOException {
			
		// response.setContentType("application/json");
		
		response.setCharacterEncoding("UTF-8");
		
		String hospitalCode = request.getSession().getAttribute("HOSPITAL_CODE").toString();
		String doctorFrom = request.getParameter("doctorFrom");
		String doctorTo = request.getParameter("doctorTo");
		String admissionType =  request.getParameter("admissionType");
			
		String  sqlCommand = "SELECT * FROM STP_TRANFER_DF WHERE HOSPITAL_CODE = '" + hospitalCode + "' AND " + 
							 " DOCTOR_FROM  = '" + doctorFrom + "' AND DOCTOR_TO  = '" + doctorTo + "'  AND ADMISSION_TYPE = '" + admissionType + "' ";
			
		    
		DBConnection objConn = new DBConnection();
		objConn.connectToLocal();
		ResultSet  rs = objConn.executeQuery(sqlCommand);
		
		try {
			
			// Model  StpTranferDfModel
			StpTranferDfModel  model = new StpTranferDfModel();
			
			if(rs.next()){
					model.setHospitalCode(rs.getString("HOSPITAL_CODE").trim());
					model.setDoctorFrom(rs.getString("DOCTOR_FROM").trim());
					model.setDoctorFromName(rs.getString("DOCTOR_FROM_NAME"));
					model.setDoctorTo(rs.getString("DOCTOR_TO").trim());
					model.setDoctorToName(rs.getString("DOCTOR_TO_NAME"));
					model.setAdmissionType(rs.getString("ADMISSION_TYPE").trim());
			}
		
			request.setAttribute("model", model);
		
			RequestDispatcher rqd = request.getRequestDispatcher("/forms/input/stp_df_tranfer_edit.jsp");
			rqd.forward(request, response);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		JSONObject objJson = new JSONObject();
		PrintWriter out = response.getWriter();
		
		String hospitalCode = request.getSession().getAttribute("HOSPITAL_CODE").toString();
		String doctorFrom = request.getParameter("doctorFrom");
		String doctorTo = request.getParameter("doctorTo");
		String admissionType =  request.getParameter("admissionType");
			
		String  sqlCommand = "DELETE FROM STP_TRANFER_DF WHERE HOSPITAL_CODE = '" + hospitalCode + "' AND " + 
							 " DOCTOR_FROM  = '" + doctorFrom + "' AND DOCTOR_TO  = '" + doctorTo + "'  AND ADMISSION_TYPE = '" + admissionType + "' ";
			
		    
		DBConnection objConn = new DBConnection();
		objConn.connectToLocal();
			
			 if(objConn.executeUpdate(sqlCommand) != -1){ 
				 objJson.put("status", "SUCCESS");
			 } else { 
				 objJson.put("status", "FAIL");
			 }
			
		out.print(objJson.toJSONString());
		
	}

	private void actionAdd(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException  {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		JSONObject objJson = new JSONObject();
		PrintWriter out = response.getWriter();
		
		String hospitalCode = request.getSession().getAttribute("HOSPITAL_CODE").toString();
		String doctorFrom = request.getParameter("doctorFrom");
		String doctorTo = request.getParameter("doctorTo");
		String doctorFromName = new String(request.getParameter("doctorFromName").getBytes("ISO8859_1"), "UTF-8");
		String doctorToName   =   new String(request.getParameter("doctorToName").getBytes("ISO8859_1"), "UTF-8");
		String admissionType =  request.getParameter("admissionType");
			
		String  sqlCommand = "INSERT INTO STP_TRANFER_DF" +
								 "(HOSPITAL_CODE" + 
						         ",DOCTOR_FROM" + 
						         ",DOCTOR_TO" +
						         ",ADMISSION_TYPE" + 
						         ",PERCENT_ALLOCATE , DOCTOR_FROM_NAME  , DOCTOR_TO_NAME)" +
						         "  VALUES  " + 
						         "( '" + hospitalCode +"' , '" + doctorFrom + "' , '" + doctorTo + "' , '" + admissionType + "' , 0.0  ,  '" + doctorFromName + "' , '" + doctorToName + "' ) ";
			
		System.out.println("sql : " + sqlCommand);
		    
		DBConnection objConn = new DBConnection();
		objConn.connectToLocal();
			
			 if(objConn.executeUpdate(sqlCommand) != -1){ 
				 objJson.put("status", "SUCCESS");
			 } else { 
				 objJson.put("status", "FAIL");
			 }
			
		out.print(objJson.toJSONString());
			
	}

}
