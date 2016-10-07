package df.servlet.process;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.tax.Tax91Bean;
/**
 * Servlet implementation class for Servlet: ProcessDistributeRevenueSrvl
 *
 */
public class ProcessTax91Srvl extends HttpServlet {
	   private DBConnection conn = null;
	   private DBConn cdb = null;
	   private Tax91Bean gpb;
	    /* (non-Java-doc)
		 * @see javax.servlet.http.HttpServlet#HttpServlet()
		 */
		//public ProcessGroupSrvl() {
		//	super();
		//}   	
		public void init() throws ServletException {
	        super.init();

	        conn = new DBConnection();
	        conn.connectToServer();
	        cdb = new DBConn(conn.getConnection());
	        //DBConnection conn = new DBConnection();
	        try {
	            cdb.setStatement();
	            gpb = new Tax91Bean(cdb);
	            //gr = new Old(cdb);
	           // gs = new GuaranteeNewSummaryBean(cdb);
	            //ga = new GuaranteeAbsorbAccuBean(cdb);
	        } catch (Exception ex) {
	            System.out.println("Exception test: "+ex);
	        }
	    }    

	    @Override
	    public void destroy(){
	        super.destroy();
	        conn.freeConnection();
	    }
	    
	    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	        response.setContentType("text/xml; charset=UTF-8");
	        
	        PrintWriter out = response.getWriter();
	        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	        
	        request.setCharacterEncoding("UTF-8");
	        try 
	        {
	            String hospitalCode = request.getParameter("HOSPITAL_CODE");
	            String year = request.getParameter("YYYY");
	            String process_type = request.getParameter("TYPE");
	            boolean st = true;
	            
	            //GuaranteeNewPrepareBean gpb = new GuaranteeNewPrepareBean(cdb);
	            //if(process_type.equals("Prepare Guarantee Transaction")||process_type.equals("Set Guarantee Transaction")
	           //    ||process_type.equals("Calculate Guarantee Transaction"))
	           // {
	                try 
	                {
	                    st = gpb.prepareProcess(year, hospitalCode, process_type);
	                    out.print("<RESULT><SUCCESS>" + st + "</SUCCESS></RESULT>");
	                }
	                catch (Exception  e) 
	                {
	                    System.out.println("Prepare error : "+e+" / Process : "+year+hospitalCode+process_type);
	                    out.print("<RESULT><SUCCESS>false</SUCCESS></RESULT>");
	                }
	                finally 
	                { 
	                    out.close();
	                }
	          //  }
	         /*  if(process_type.equals("Summary Guarantee Transaction")||process_type.equals("Summary Guarantee Tax")
	                ||process_type.equals("Summary Guarantee Monthly")){
	                System.out.println(process_type);
	                try {
	                    out.print("<RESULT><SUCCESS>" + gs.summaryProcess(month, year,hospitalCode, process_type) + "</SUCCESS></RESULT>");
	                }catch (Exception  e) {
	                    e.printStackTrace(out);
	                }finally { 
	                    out.close();
	                }
	            }
	            
	            if(process_type.equals("Delete Old Records")){
	                System.out.println(process_type);
	                try {
	                    out.print("<RESULT><SUCCESS>" + gr.processRollback(month, year, hospitalCode) + "</SUCCESS></RESULT>");
	                }catch (Exception  e) {
	                    e.printStackTrace(out);
	                }finally { 
	                    out.close();
	                }
	            }
	            if(process_type.equals("Summary Guarantee Accu")){
	                out.print("<RESULT><SUCCESS>true</SUCCESS></RESULT>");
	                /*
	                System.out.println(process_type);
	                try {
	                    out.print("<RESULT><SUCCESS>" + ga.summaryAbsorbAccu(month, year,hospitalCode) + "</SUCCESS></RESULT>");
	                }catch (Exception  e) {
	                    e.printStackTrace(out);
	                }finally { 
	                    out.close();
	                }
	                */
	           // }
	            

	            /* TODO output your page here
	            out.println("<html>");
	            out.println("<head>");
	            out.println("<title>Servlet ProcessImportGuarantee</title>");  
	            out.println("</head>");
	            out.println("<body>");
	            out.println("<h1>Servlet ProcessImportGuarantee at " + request.getContextPath () + "</h1>");
	            out.println("</body>");
	            out.println("</html>");
	            */
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