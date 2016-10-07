package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import df.bean.tax.CalculateTax90Bean;

/**
 * Servlet implementation class ProcessTax402Srvl
 */
public class ProcessTax90Srvl extends ProcessServlet {
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
        String hospitalCode = request.getParameter("HOSPITAL_CODE");
        int numAffRec = 0;
        CalculateTax90Bean tax90 = new CalculateTax90Bean(hospitalCode, year.toString());
        // Tax 90
        if(tax90.processTax(hospitalCode, year)){
            	numAffRec = 1;
        }else numAffRec  = 0;
        try {
            out.print("<RESULT><SUCCESS>" + numAffRec + "</SUCCESS></RESULT>");
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
