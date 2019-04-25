/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
//import df.bean.obj.doctor.DoctorList;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;
import df.bean.process.ProcessImport;
import df.bean.process.ProcessImportBillNotAllowZeroAndLess;
import df.bean.process.ProcessMapDepartment;
import df.bean.process.ProcessTranferRevenue;
import df.bean.process.ProcessUpdateBillNotPrint;
import df.bean.process.ProcessUpdatePreviousOnWard;
import df.bean.process.ProcessJoinBill;
import df.bean.process.ProcessReceipt;
import df.bean.process.WelfareDFPayment;

/**
 *
 * @author admin
 */
public class ProcessImportBillSrvl extends ProcessServlet {
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	DBConnection conn = new DBConnection();
    	conn.connectToLocal();
        
        response.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        request.setCharacterEncoding("UTF-8");
        
        String user = request.getParameter("USER");
        String password = request.getParameter("PWD");
        String hospitalCode = request.getParameter("HOSPITAL_CODE");
        String startDate = JDate.saveDate(request.getParameter("START_DATE"));
        String endDate = JDate.saveDate(request.getParameter("END_DATE"));
        String type = request.getParameter("TYPE");
    	Batch batch = new Batch(hospitalCode, conn);
    	ProcessImport pi = new ProcessImport(conn);
    	ProcessImportBillNotAllowZeroAndLess piz = new ProcessImportBillNotAllowZeroAndLess(conn);
    	ProcessReceipt pr = new ProcessReceipt(conn);
        ProcessJoinBill j = new ProcessJoinBill();
        WelfareDFPayment wf = new WelfareDFPayment();
        //ProcessUpdatePreviousOnWard processImportOnWard = new ProcessUpdatePreviousOnWard();
        ProcessUpdateBillNotPrint processBillNotPrint = new ProcessUpdateBillNotPrint();
        int numAffRec = 0;
        
        if (conn.getConnection() == null) {
            out.print("<RESULT><SUCCESS>" + 0 + "</SUCCESS></RESULT>");
            return ;
        }
        
        conn.beginTrans();
        if (type.equals("Bill Transaction")) {
        	if(j.ProcessJoinBill(hospitalCode, startDate, endDate)){
            	System.out.print(Variables.IS_TEST ? "OK Join Bill\n" : "");
            	pi.setUserId(user);
                if (pi.importBill(hospitalCode, startDate, endDate) && 
                    pr.CalculateReceiptByCash(batch.getYyyy(), batch.getMm(), startDate, endDate, hospitalCode)) {
                numAffRec = 1;
                } else numAffRec = 0;
            }else numAffRec = 0;
        }
		
        if (type.equals("Patho Transaction")) {
            if(true){
                if (pi.importPatho(hospitalCode, startDate, endDate) && 
                    pr.CalculateReceiptByCash(batch.getYyyy(), batch.getMm(), startDate, endDate, hospitalCode)) {
                numAffRec = 1;
                } else numAffRec = 0;
            }else numAffRec = 0;
        }
        
        if (type.equals("Verify Transaction")) {
            if (pi.importVerifyInMonth(hospitalCode, startDate, endDate) && 
                    pi.importVerifyRecOverMonth(hospitalCode, startDate, endDate) &&
                        pi.importVerifyNotRec(hospitalCode, startDate, endDate)
            	&& pi.importVerifyPreviousMonth(hospitalCode, startDate, endDate)
            	){
                numAffRec = 1;
            } else numAffRec = 0;
        }
        
        if(type.equals("Welfare Calulate")){
        	if(wf.doProcess(hospitalCode, startDate, endDate)){
        		 numAffRec = 1;
        	}else{
        		 numAffRec = 0;
        	}
        	System.out.println("IN Welfare Calulate");
        }
        
        if (type.equals("Update Invoice OnWard/Mobile")) {
        	System.out.println("\nOnward/Moblie");
            DBConn dbconn = new DBConn();
            try {
				dbconn.setStatement();
	            if(dbconn.getSingleData("SELECT IS_ONWARD FROM HOSPITAL WHERE CODE = '"+hospitalCode+"'").equals("Y")){
	            	if(processBillNotPrint.doProcess(hospitalCode, startDate, endDate)){
	                    numAffRec = 1;
	                } else numAffRec = 0;
	            	
	            }else{
	            	numAffRec = 1;
	            }
			} catch (SQLException e) {
				System.out.println("Error Process Update Invoice Onward/Moblie : "+e);
			}

        	//processImportOnWard.setUserId(user);
        	//if(processImportOnWard.importOnward(hospitalCode, startDate, endDate)){
        }
        
        if (numAffRec == 1){ 
        	conn.commitTrans();
            //ProcessTranferRevenue objRevenueDf = new  ProcessTranferRevenue(hospitalCode, user , startDate, endDate);
            //objRevenueDf.doProcess();
            ProcessMapDepartment objMapDepartment  = new ProcessMapDepartment(hospitalCode , startDate , endDate);
            objMapDepartment.doProcess();
        }
        
        if (numAffRec == 0) conn.rollBackTrans();
    
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
            conn = null;
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