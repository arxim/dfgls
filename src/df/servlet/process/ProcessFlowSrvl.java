/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.process;


import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.process.ProcessFlow;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
/**
 *
 * @author USER
 */
public class ProcessFlowSrvl extends HttpServlet {
    private DBConnection conn = null;
    private DBConn cdb = null;   
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    
    public void init() throws ServletException {
        super.init();

        conn = new DBConnection();
        conn.connectToServer();
        cdb = new DBConn(conn.getConnection());
        //DBConnection conn = new DBConnection();
        try {
            cdb.setStatement();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    @Override
    public void destroy(){
        super.destroy();
        conn.freeConnection();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        PrintWriter out = response.getWriter();
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        ProcessFlow process = new ProcessFlow(this.conn,hospital_code);
        String result = "";
        //System.out.println(hospital_code);
        try {
            String idProcess = request.getParameter("idProcess");
            String startDT = request.getParameter("START_DATE");
            String endDT = request.getParameter("END_DATE");
            String mm = request.getParameter("mm");
            String yy = request.getParameter("yy");
            
            String htmlCode = "";

            if("07".equals(idProcess.toString())){
                result = process.ComputeMonthly(mm,yy);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"','');";
                htmlCode+= "</script>";                  
            }else if("08".equals(idProcess.toString())){
                result = process.ComputePaymentMonthlyForDF(mm,yy);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"','');";
                htmlCode+= "</script>";                 
            }else if("09".equals(idProcess.toString())){
                result = process.ExportToBankForDF(mm,yy);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"','');";
                htmlCode+= "</script>";                 
            }else if("10".equals(idProcess.toString())){
                result = process.ComputeCummaryTax(mm,yy);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"','');";
                htmlCode+= "</script>";                  
            }else if("11".equals(idProcess.toString())){
                result = process.ComputePaymentMonthlyForSalary(mm,yy);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"','');";
                htmlCode+= "</script>";                
            }
            
            out.println(htmlCode);
        } finally { 
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
    }// </editor-fold>

}
