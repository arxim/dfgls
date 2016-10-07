/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.expense;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.expense.ExpenseSummaryBean;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author nopphadon
 */
public class ProcessSummaryExpenseSrvl extends HttpServlet {
    private DBConnection conn = null;
    private DBConn cdb = null;
    private ExpenseSummaryBean  esb;
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
            esb = new ExpenseSummaryBean(cdb);
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
        response.setContentType("text/xml; charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        
        request.setCharacterEncoding("UTF-8");
        try {
            String hospitalCode = request.getParameter("HOSPITAL_CODE");
            String year = request.getParameter("YYYY");
            String month = request.getParameter("MM");
            String process_type = request.getParameter("TYPE");
            System.out.println(process_type);
            //GuaranteeNewPrepareBean gpb = new GuaranteeNewPrepareBean(cdb);
            //if(process_type.equals("Remove Expense Transaction") || process_type.equals("Summary Expense Transaction")){
                
                try {
                    //System.out.println(esb.getMessage()+esb.summaryProcess(month, year, hospitalCode, process_type));
                    out.print("<RESULT><SUCCESS>" + esb.summaryProcess(month, year, hospitalCode, process_type) + "</SUCCESS></RESULT>");
                }catch (Exception  e) {
                    System.out.println(e);
                }finally { 
                    out.close();
                }
            //}           
        }catch (Exception d){
            System.out.println(d);
        }finally { 
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
