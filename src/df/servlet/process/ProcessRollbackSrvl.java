/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.process;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.process.ProcessDischargeSummary;
import df.bean.process.ProcessHolidayCalculate;
import df.bean.process.ProcessRollBack;
import df.bean.process.ProcessSummaryMonthlyDF;
import df.bean.guarantee.GuaranteeRollbackBeanNew;

//import df.bean.process.ProcessHolidayCalculate;
import df.bean.obj.util.JDate;
import df.bean.process.ProcessBankTMBPaymentMonthly;
import df.bean.tax.ProcessTax402Bean;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author USER
 */
public class ProcessRollbackSrvl extends HttpServlet {
    private DBConnection conn = null;
    private DBConn cdb = null;   
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    
    public void init() throws ServletException {
        super.init();
        try {
            /*
            conn = new DBConnection();
            conn.connectToLocal();
            cdb = new DBConn(conn.getConnection());
            */
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    @Override
    public void destroy() {
        super.destroy();
        //conn.Close();
        //cdb.closeDB("From Rollback Process");
        //conn.freeConnection();   
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        conn = new DBConnection();
        conn.connectToLocal();

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String bkkCode = session.getAttribute("HOSPITAL_CODE").toString();
        String mm = request.getParameter("mm");
        String yy = request.getParameter("yy");        
        PrintWriter out = response.getWriter();
        ProcessRollBack rb = new ProcessRollBack(conn);
        ProcessTax402Bean txb = null;
        GuaranteeRollbackBeanNew grb = null;
        Boolean result = false;
        String idProcess = request.getParameter("idProcess");
        
        String startDT = null;
        String endDT = null;        
        if(request.getParameter("START_DATE")!=null && request.getParameter("END_DATE")!=null ){
            startDT = JDate.saveDate(request.getParameter("START_DATE"));
            endDT = JDate.saveDate(request.getParameter("END_DATE"));               
        }else{
        	System.out.println("start-end");
        }
    	System.out.println("Process "+idProcess+" Start : "+startDT);

        try {

            String htmlCode = "";
            if("00".equals(idProcess.toString())) { 
            	 /**
            	  * Mr.sarunyoo Keawsopa
            	  * @date 26-Dec-2012
            	  * RollBack Discharge
            	  */
            	  result = rb.rollBackDischargeSummary(bkkCode, mm, yy);
            	  htmlCode = "<script language='javascript'>";
                  htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                  htmlCode+= "</script>";
            }else if("01".equals(idProcess.toString())){
                //result = process.Process01(startDT);
                result = rb.rollBackInterfaceHisBill(bkkCode, startDT, endDT);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";
            }else if("02".equals(idProcess.toString())){
                result = rb.rollBackInterfaceHisVerify(bkkCode, startDT, endDT);
                htmlCode = "<script language='javascript'>";
                //htmlCode+= "    alert('"+ startDT +"');";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";                
            }else if("03".equals(idProcess.toString())){
                result = rb.rollBackInterfaceErpArReceipt(bkkCode, startDT, endDT);
                htmlCode = "<script language='javascript'>";
                //htmlCode+= "    alert('"+ startDT +"');";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";                
            }else if("04".equals(idProcess.toString())){
                result = rb.rollBackImportBill(bkkCode, startDT, endDT);
                htmlCode = "<script language='javascript'>";
                //htmlCode+= "    alert('"+ startDT +"');";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";                
            }else if("05".equals(idProcess.toString())){
                result = rb.rollBackTrnDaily(bkkCode, startDT, endDT);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";                
            }else if("06_01".equals(idProcess.toString())){
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    alert('"+ idProcess +"');";
                //htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";                
            }else if("06_02".equals(idProcess.toString())){
                //result = process.Process06_02(startDT);
                //result = rb.rollBackReceiptByAR(bkkCode, yy, mm);
            	result = rb.rollBackReceiptByAR(bkkCode, startDT, endDT);
                htmlCode = "<script language='javascript'>";
                //htmlCode+= "    alert('"+ idProcess +"');";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";                
            }else if("06_03".equals(idProcess.toString())){
                //result = process.Process06_03(startDT);
                result = rb.rollBackReceiptByPayor(bkkCode, yy, mm);
                htmlCode = "<script language='javascript'>";
                //htmlCode+= "    alert('"+ idProcess +"');";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";                
            }else if("06_04".equals(idProcess.toString())){
                result = rb.rollBackReceiptByDoctor(bkkCode, yy, mm);
                htmlCode = "<script language='javascript'>";
                //htmlCode+= "    alert('"+ idProcess +"');";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";
            /*
            }else if("07".equals(idProcess.toString())){
                result = rb.rollBackSummaryMonthly(bkkCode, yy, mm);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";
            */            
            }else if("07".equals(idProcess.toString())){
            	ProcessSummaryMonthlyDF pdf = new ProcessSummaryMonthlyDF(bkkCode, startDT,"","");
                //ProcessDischargeBasis pdb = null;
            	result = pdf.doRollback();
            	System.out.println("Rollback Monthly : "+result);
            	//if(result){
            	//	pdb = new ProcessDischargeBasis(bkkCode);
            	//	result = pdb.doRollback();
                //	System.out.println("Rollback Discharge Basis : "+result);
            	//}
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";                  
            }else if("08".equals(idProcess.toString())){
                //result = rb.rollBackPaymentMonthly(bkkCode, yy, mm, PaymentMonthly.PAYMENT_TYPE_DF);
                //(rb.rollBackPaymentMonthly(Variables.getHospitalCode(), "2008", "02", PaymentMonthly.PAYMENT_TYPE_DF))
                //htmlCode = "<script language='javascript'>";
                //htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                //htmlCode+= "</script>";                 
            }else if("09".equals(idProcess.toString())){
                //rb.rollBackMediaClearing(Variables.getHospitalCode(), "2008", "02", BankTMBMediaClearing.SERVICE_TYPE_PAYMENT)
                //result = rb.rollBackMediaClearing(bkkCode, yy, mm, "04");
                //htmlCode = "<script language='javascript'>";
                //htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                //htmlCode+= "</script>";                 
            }else if("10".equals(idProcess.toString())){
                //rb.rollBackSummaryTax406(Variables.getHospitalCode(), "2008", "06")
                result = rb.rollBackSummaryTax406(bkkCode, yy, mm);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";                  
            }else if("11".equals(idProcess.toString())){
                //rb.rollBackPaymentMonthly(Variables.getHospitalCode(), "2008", "02", PaymentMonthly.PAYMENT_TYPE_SALARY)
                //result = rb.rollBackPaymentMonthly(bkkCode, yy, mm, PaymentMonthly.PAYMENT_TYPE_SALARY);
                //htmlCode = "<script language='javascript'>";
                //htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                //htmlCode+= "</script>";                
            }else if("06_05".equals(idProcess.toString())){
            	grb = new GuaranteeRollbackBeanNew();
                //rollback Guarantee
            	result = false;
                if(grb.rollBackTransaction(bkkCode, yy, mm)){
                    result = grb.rollBackSetup(bkkCode, yy, mm);                    
                }
                //result = rb.rollBackPaymentMonthly(bkkCode, yy, mm, PaymentMonthly.PAYMENT_TYPE_SALARY);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";
            }else if("06_06".equals(idProcess.toString())){
                //Roll back Tax402 Calculate
            	txb = new ProcessTax402Bean(bkkCode,mm,yy);
            	result = false;
                if(txb.rollbackTax402(bkkCode, yy, mm)){
                    result = true;                    
                }
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";

            }else if("15".equals(idProcess.toString())){
                // Process Write Off 
                //result = rb.rollBackPaymentMonthly(bkkCode, yy, mm, PaymentMonthly.PAYMENT_TYPE_SALARY);
            	result = rb.rollBackWriteOff(bkkCode, yy+mm+00, yy+mm+31);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";    
            
            }else if("16".equals(idProcess.toString())){
            	ProcessHolidayCalculate test_holiday=new ProcessHolidayCalculate();
            	result = test_holiday.dataRollBack(bkkCode, yy, mm);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','"+ result +"');";
                htmlCode+= "</script>";
              
            }else if("17".equals(idProcess.toString())){
            	ProcessDischargeSummary pd=new ProcessDischargeSummary();
            	pd.dataRollBack(bkkCode, yy, mm);
                htmlCode = "<script language='javascript'>";
                htmlCode+= "    parent.status('" + idProcess + "','true');";
                htmlCode+= "</script>";    
            }
            out.println(htmlCode);
        }catch(Exception err){
                out.println(err.getMessage());
        } finally { 
            conn.Close();
            //cdb.closeDB("From Rollback Process");
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

    /*
    public static void main(String[] arg){
        Boolean result = false;
        
        DBConnection conn = new DBConnection();
        conn.connectToServer();
        
        ProcessRollBack rb = new ProcessRollBack(conn);
        result = rb.rollBackSummaryTax406("201", "2008", "11");
        System.out.println(result);
    }
     * */
}
