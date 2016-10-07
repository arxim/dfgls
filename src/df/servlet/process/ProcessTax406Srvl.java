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
import df.bean.obj.doctor.DoctorList;
import df.bean.obj.util.JDate;
import df.bean.process.ProcessTax406;

/**
 *
 * @author admin
 */
public class ProcessTax406Srvl extends ProcessServlet {
    DBConnection conn = null;
    Batch batch = null;
    DoctorList drList = null;
    ProcessTax406 pm = null;

    @Override
    public void destroy() {
        super.destroy();
        conn.freeConnection();
        conn = null;
        batch = null;
        drList = null;
        pm = null;
        System.out.println("------------------ destroy-------------------");
        
        System.out.println("Stop time : " + JDate.getTime());
    }

    @Override
    public void init() throws ServletException {
        super.init();

        conn = new DBConnection();
        conn.connectToServer();
        
        System.out.println("------------------ initial ------------------");
        System.out.println("Start time : " + JDate.getTime());
    }
    
    private void newObject(String hospital_code) {
        batch = new Batch(hospital_code, conn);
        drList = new DoctorList(hospital_code, conn);
        pm = new ProcessTax406(conn);
        System.out.println ("========  new object =======");
    }

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
        String user = request.getParameter("USER");
        String password = request.getParameter("PWD");
        String hospitalCode = request.getParameter("HOSPITAL_CODE");
        String doctorCode = request.getParameter("DOCTOR_CODE");
        String startDate = request.getParameter("START_DATE");
        String endDate = request.getParameter("END_DATE");
        String recNo = request.getParameter("REC_NO");
        String MM = request.getParameter("MM");
        String YYYY = request.getParameter("YYYY");

        int numAffRec = 0;
        System.out.println("Start Date : "+startDate+" End Date : "+endDate+" Hospital : "+hospitalCode+" Month : "+MM+" Year : "+YYYY);
        
        if (conn.getConnection() == null) {
            out.print("<RESULT><SUCCESS>" + 0 + "</SUCCESS></RESULT>");
            return ;
        }
        
        if (recNo.equalsIgnoreCase("2")) {       
            newObject(hospitalCode);
            drList.newAllDoctor(hospitalCode);
        }
                
        // Tax 406
        pm.setDoctorList(drList);
        System.out.println("Start Date : "+startDate+" End Date : "+endDate+" Hospital : "+hospitalCode+" Dr : "+doctorCode+" Month : "+MM+" Year : "+YYYY);
        if (pm.Calculate(startDate, endDate, hospitalCode, doctorCode, MM, YYYY)) {
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
            //conn.freeConnection();
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