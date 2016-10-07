package df.servlet.process;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.process.ProcessExpenseBean;
/**
 * Servlet implementation class for Servlet: ProcessDistributeRevenueSrvl
 *
 */
public class ProcessExpenseSrvl extends HttpServlet {	    
	    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	 	    DBConnection conn = null;
		    DBConn cdb = null;
		    ProcessExpenseBean gpb = null;
	        conn = new DBConnection();
	        conn.connectToLocal();
	        cdb = new DBConn(conn.getConnection());
	        try {
	            cdb.setStatement();
	            gpb = new ProcessExpenseBean(cdb);
	        } catch (Exception ex) {
	            System.out.println("Exception test: "+ex);
	        }
	        response.setContentType("text/xml; charset=UTF-8");
	        
	        PrintWriter out = response.getWriter();
	        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	        
	        request.setCharacterEncoding("UTF-8");
	        try 
	        {
	            String hospitalCode = request.getParameter("HOSPITAL_CODE");
	            String year = request.getParameter("YYYY");
	            String month = request.getParameter("MM");
	            String process_type = request.getParameter("TYPE");
	            boolean st = true;
                try{
                    st = gpb.prepareProcess(month, year, hospitalCode, process_type);
                    out.print("<RESULT><SUCCESS>" + st + "</SUCCESS></RESULT>");
                }catch (Exception  e){
                    System.out.println("Prepare error : "+e+" / Process : "+month+year+hospitalCode+process_type);
                    out.print("<RESULT><SUCCESS>false</SUCCESS></RESULT>");
                }finally{ 
                    out.close();
                }
	        } 
	    	finally 
	    	{ 
	            out.close();
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
			//System.out.println("request="+request);
	    	//System.out.println("response="+response);
	        processRequest(request, response);
		}   	
		/** 
		 * Returns a short description of the servlet.
		*/
		public String getServletInfo() {
		  return "Short description";
		}
		    // </editor-fold>
}