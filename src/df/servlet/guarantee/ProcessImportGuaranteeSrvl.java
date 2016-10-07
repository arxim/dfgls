/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.guarantee;

import df.bean.db.conn.DBConn;
import df.bean.guarantee.ProcessGuaranteeBeanNew;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author nopphadon
 */
public class ProcessImportGuaranteeSrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private GuaranteeNewSummaryBean gs;
    //private Old gr;
    //private GuaranteeAbsorbAccuBean ga;
    HttpSession session;

    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/xml; charset=UTF-8");
        session = request.getSession(true);
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        request.setCharacterEncoding("UTF-8");
        DBConn cdb = null;
        ProcessGuaranteeBeanNew gpb = null;

        try {
            String hospitalCode = request.getParameter("HOSPITAL_CODE");
            String user_id = session.getAttribute("USER_ID").toString();
            String year = request.getParameter("YYYY");
            String month = request.getParameter("MM");
            String process_type = request.getParameter("TYPE");
            boolean st = true;
            try {
                cdb = new DBConn();
                cdb.setStatement();
                gpb = new ProcessGuaranteeBeanNew(cdb);            
            } catch (Exception ex) {
                System.out.println("Exception test: "+ex);
            }
            try {
                st = gpb.prepareProcess(month, year, hospitalCode, process_type, user_id);
                System.out.println("Servlet Report Guarantee Process Complete Task : "+process_type+"; Month/Year : "+month+year+"; Hospital : "+hospitalCode);
                out.print("<RESULT><SUCCESS>" + st + "</SUCCESS></RESULT>");
            }catch (Exception  e) {
            	st = false;
                out.print("<RESULT><SUCCESS>" + st + "</SUCCESS></RESULT>");
                System.out.println("Prepare error : "+e+" / Process : "+month+year+hospitalCode+process_type);
            }finally { 
                out.close();
            }
        } finally {
        	cdb.closeDB("");
        	gpb = null;
            out.close();
        }
    } 


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
    * Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
    * Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
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
