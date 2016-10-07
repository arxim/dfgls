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
import df.bean.obj.util.JDate;
import df.bean.process.ProcessPartialPayment;
import df.bean.process.ProcessReceipt;

/**
 *
 * @author admin
 */
public class ProcessReceiptByARSrvl extends ProcessServlet {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
                
        response.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");        
        request.setCharacterEncoding("UTF-8");
        
        String user = request.getParameter("USER");
        String password = request.getParameter("PWD");
        String hospitalCode = request.getParameter("HOSPITAL_CODE");
        String startDate = JDate.saveDate(request.getParameter("START_DATE"));
        String endDate = JDate.saveDate(request.getParameter("END_DATE"));
        String recNo = request.getParameter("REC_NO");
        int numAffRec = 0;
        DBConnection conn = null;
        Batch batch = null;
        ProcessReceipt pr = null;
        ProcessPartialPayment pt = null;
        
        /*new code*/
        if (recNo.equalsIgnoreCase("2")) {
        	//System.out.println("\n2");
        	numAffRec = 1;
            conn = new DBConnection();
            conn.connectToLocal();
            conn.setUserID(user);
            batch = new Batch(hospitalCode, conn);
            pr = new ProcessReceipt(conn);
            if (pr.CalculateReceiptByAR(batch.getYyyy(), batch.getMm(), startDate, endDate, hospitalCode )) {
                numAffRec = 1;
            }else numAffRec = 0;
        }else if(recNo.equalsIgnoreCase("3")){
        	//System.out.println("3");
        	numAffRec = 1;
            conn = new DBConnection();
            conn.connectToLocal();
            conn.setUserID(user);
            batch = new Batch(hospitalCode, conn);
            pt = new ProcessPartialPayment();
            if (pt.processPartial(hospitalCode, batch.getMm(), batch.getYyyy(), startDate, endDate)){
                numAffRec = 1;
            }else numAffRec = 0;
        }
        try {
            Thread.sleep(20);
            out.print("<RESULT><SUCCESS>" + numAffRec + "</SUCCESS></RESULT>");
        }catch (Exception e) {        	
            e.printStackTrace(out);
        }finally { 
            conn.Close();
            out.close();
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
