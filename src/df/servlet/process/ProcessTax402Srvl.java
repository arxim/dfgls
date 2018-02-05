package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import df.bean.tax.ProcessTax402Bean;

/**
 * Servlet implementation class ProcessTax402Srvl
 */
public class ProcessTax402Srvl extends ProcessServlet {
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
        HttpSession session = request.getSession(true);
        
        int numAffRec = 0;
        ProcessTax402Bean tax402 = new ProcessTax402Bean(hospitalCode, month.toString(), year.toString());
        tax402.setUserId(session.getAttribute("USER_ID").toString());
        // Tax 402
        if(tax402.provideProcess()){
        	numAffRec = 1;
        }else numAffRec  = 0;
        try {
            //Thread.sleep(20);
            out.print("<RESULT><SUCCESS>" + numAffRec + "</SUCCESS></RESULT>");
        }catch (Exception  e) {
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
