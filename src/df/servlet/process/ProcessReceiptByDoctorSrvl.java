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

/**
 *
 * @author admin
 */
public class ProcessReceiptByDoctorSrvl extends ProcessServlet {
    DBConnection conn = null;
    Batch batch = null;
//    DoctorList drList = null;
    ProcessReceipt pr = null;

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
    
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        conn = new DBConnection();
        conn.connectToLocal();
                
        response.setContentType("text/xml; charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        
        request.setCharacterEncoding("UTF-8");
        
        String user = request.getParameter("USER");
        String password = request.getParameter("PWD");
        String hospitalCode = request.getParameter("HOSPITAL_CODE");
        String doctorCode = request.getParameter("DOCTOR_CODE");
        String recNo = request.getParameter("REC_NO");

        batch = new Batch(hospitalCode, conn);
        pr = new ProcessReceipt(conn);

        int numAffRec = 0;
        
        if (conn.getConnection() == null) {
            out.print("<RESULT><SUCCESS>" + 0 + "</SUCCESS></RESULT>");
            return ;
        }
                
        if (pr.CalculateReceiptByDoctor(batch.getYyyy(), batch.getMm(), hospitalCode, doctorCode )) {
            numAffRec = 1;
        }else numAffRec = 0;
        

        try {
            Thread.sleep(20);
            out.print("<RESULT><SUCCESS>" + numAffRec + "</SUCCESS></RESULT>");
        }
        catch (Exception  e) {
            e.printStackTrace(out);
        }
        finally { 
            out.close();
            conn.Close();
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
