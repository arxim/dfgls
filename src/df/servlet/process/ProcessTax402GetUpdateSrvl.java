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
 * Servlet implementation class ProcessTax402GetUpdateSrvl
 */
public class ProcessTax402GetUpdateSrvl extends ProcessServlet {
	ProcessTax402Bean tax402;
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
                
        response.setContentType("text/xml; charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        
        request.setCharacterEncoding("UTF-8");             
        String month = request.getParameter("MM");
        String year = request.getParameter("YYYY");
        String doctor = request.getParameter("DOCTOR_CODE");
        String hospital = request.getSession().getAttribute("HOSPITAL_CODE").toString();
        String taxMethod = request.getParameter("TAX_METHOD");
        int numAffRec = 0;
        String tax ="";
        
        tax402 = new ProcessTax402Bean(hospital, month, year);
        if(taxMethod.equals("SUM") || taxMethod.equals("STP")){
            tax = tax402.processTax(hospital, doctor, month, year); 
        }else{
        	//Tax 402
        	System.out.println("Upadte : "+taxMethod);
        	ProcessTaxWithHolding tax402WithHolding = new ProcessTaxWithHolding();
        	tax = tax402WithHolding.CalculateTaxWithHolding(hospital, doctor, taxMethod, year, month);
          
        }
        
        try {
            //Thread.sleep(20);
            out.print("<RESULT><SUCCESS>" + tax + "</SUCCESS></RESULT>");
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
