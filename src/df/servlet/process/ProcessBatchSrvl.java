/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.process;

import df.bean.obj.util.JDate;
import df.bean.process.ProcessBatchBean;
import df.bean.process.ProcessSummaryMonthlyDF;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
public class ProcessBatchSrvl extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        request.setCharacterEncoding("UTF-8");
        String hos = request.getParameter("HOSPITAL_CODE");
        String user = request.getParameter("USER_ID");
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String payment_term = request.getParameter("PAYMENT_TERM");
        String payment_date = JDate.saveDate(request.getParameter("PAYMENT_DATE"));
        try {
        	ProcessSummaryMonthlyDF nb = new ProcessSummaryMonthlyDF(hos, payment_date, year, month);
            if(payment_term.equals("1")){
            	System.out.println("Test "+hos+" Pay Date : "+payment_date+"<>"+year+month+payment_term);
            	nb.setUserID(user);
            	nb.doBatchClose();
            	out.print("<RESULT><SUCCESS>SUCCESS</SUCCESS></RESULT>");            
            }else{
            	nb.setUserID(user);
            	nb.doBatchClose();
            	out.print("<RESULT><SUCCESS>SUCCESS</SUCCESS></RESULT>");            
            }
            
        } finally {
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
