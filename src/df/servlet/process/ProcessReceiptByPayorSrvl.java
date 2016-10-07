/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.Variables;
import df.bean.db.table.Batch;
//import df.bean.obj.doctor.DoctorList;
import df.bean.obj.util.JDate;
import df.bean.process.ProcessReceipt;
import df.bean.process.ProcessReceiptBean;

/**
 *
 * @author admin
 */
public class ProcessReceiptByPayorSrvl extends ProcessServlet {
    ProcessReceiptBean pr = new ProcessReceiptBean();
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    public void destroy() {
    	pr.clearPayor();
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/xml; charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");        
        request.setCharacterEncoding("UTF-8");
        
        String hospitalCode = request.getParameter("HOSPITAL_CODE");
        String payorCode = request.getParameter("PAYOR_CODE");
        String YYYY = request.getParameter("YYYY");
        String MM = request.getParameter("MM");
        String rowNum = request.getParameter("ROW");
        int numAffRec = 0;
        if (pr.CalculateReceiptByPayor(YYYY, MM, hospitalCode, payorCode)) {
            numAffRec = 1;
        }else numAffRec = 0;

        try {
            Thread.sleep(20);
            out.print("<RESULT><SUCCESS>" + numAffRec + "</SUCCESS></RESULT>");
        }catch (Exception  e) {
            e.printStackTrace(out);
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
    * Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
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
