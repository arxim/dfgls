package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.tax.ProcessTax402Bean;
import df.bean.tax.ProcessTaxWithHolding;

/**
 * Servlet implementation class ProcessTax402GetDataSrvl
 */
public class ProcessTax402GetDataSrvl extends ProcessServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
                
        response.setContentType("text/xml; charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        
        request.setCharacterEncoding("UTF-8");        
        String month = request.getParameter("MM");
        String year = request.getParameter("YYYY");
        String hospital = request.getSession().getAttribute("HOSPITAL_CODE").toString();
                
        ProcessTax402Bean tax402 = new ProcessTax402Bean(hospital, month, year);
        
        try {
        	//conn.setStatement();
        	
        	String args[][] = tax402.getDoctor(hospital, month, year);
        	String xml="";
        	xml ="<RESULT><ROW>";
        	for(int i=0; i<args.length ; i++){ 
        		xml +="<DOCTOR_CODE>"+ args[i][1] +"</DOCTOR_CODE><TAX_402_METHOD>"+ args[i][4] +"</TAX_402_METHOD>";
        	}
        	xml +="</ROW></RESULT>";
        	out.println(xml);  
            //Thread.sleep(20);   
        	
        }
        catch (Exception  e) {
            e.printStackTrace(out);
        }
        finally { 
            out.close();
        }
        
    } 

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
    * Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
    * Returns a short description of the servlet.
    */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
