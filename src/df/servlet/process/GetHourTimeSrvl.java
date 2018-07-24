package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;

/**
 * Servlet implementation class for Servlet: GetHourTimeSrvl
 *
 */
 public class GetHourTimeSrvl extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	
	public GetHourTimeSrvl() {
		super();
	}   	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	       response.setContentType("text/xml; charset=UTF-8");
    	       HttpSession session = request.getSession(true);
    	       PrintWriter out = response.getWriter();
    	       out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    	       request.setCharacterEncoding("UTF-8");
    	       String from_date = request.getParameter("start_date");
    	       String to_date = request.getParameter("end_date");
    	       String from_time = request.getParameter("start_time");
    	       String to_time = request.getParameter("end_time");
    	       String amount_of_time = request.getParameter("amount_per_time");
    	       //String amount_of_time = "0";
    	       String ga_type = request.getParameter("ga_type");
    	       String dr_code  = request.getParameter("dr_code");
    	       String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
    	       double AMT = 0.0;
    	       double amount_diff_time = 0.0;
    	       double diff_time_val = 0.0;
    	       try
    	       {
    	    	   DBConn conn = null;
    	   		
    	   			if(conn==null){
    	   				conn = new DBConn();
    	   				conn.setStatement();
    	   			}
    	   			
    	   			if(amount_of_time.equals("0") || amount_of_time.equals("")||amount_of_time.equals("0.00")){
    	   				String ga_type_code =ga_type.equals("GEA")?"EXTRA_PER_HOUR" :"GUARANTEE_PER_HOUR";
    		    		String sql ="SELECT "+ga_type_code+"  FROM DOCTOR WHERE CODE ='"+dr_code+"' AND HOSPITAL_CODE = '"+hospital_code+"' ";
    		    		amount_of_time =conn.getSingleData(sql);
    		    		System.out.println(amount_of_time+"  "+ga_type_code);
        	   			AMT =  Double.parseDouble(amount_of_time);    		
    	        		amount_diff_time = JDate.getDiffTimes(from_date, to_date, from_time, to_time);
    	        		diff_time_val = AMT*amount_diff_time;
    	        		out.print("<RESULT><STATUS>" + diff_time_val + "</STATUS><STATUS1>" + amount_diff_time + "</STATUS1><STATUS2>" + amount_of_time + "</STATUS2></RESULT>");
    	       
    	   			}else{
        	   			AMT =  Double.parseDouble(amount_of_time);    		
    	        		amount_diff_time = JDate.getDiffTimes(from_date, to_date, from_time, to_time);
    	        		diff_time_val = AMT*amount_diff_time;
    	        		out.print("<RESULT><STATUS>" + diff_time_val + "</STATUS><STATUS1>" + amount_diff_time + "</STATUS1><STATUS2>" + amount_of_time + "</STATUS2></RESULT>");
    	   			}
    	   			conn.closeDB("");
    	       }
    	       catch (Exception  e) 
    	       {
    	    	   System.out.println(e);
    	       }
    	        
    	    }
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	} 
}