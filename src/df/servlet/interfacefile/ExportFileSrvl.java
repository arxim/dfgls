/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.interfacefile;
import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.interfacefile.ExportDFToAPBean;
import df.bean.interfacefile.ExportDFToBankBean;
import df.bean.interfacefile.ExportDFToGLBean;
import df.bean.interfacefile.ExportSAPGLBean;
import df.bean.interfacefile.ExportSAPGLPaymentBean;
import df.bean.interfacefile.ExportDFToPayrollBean;
import df.bean.interfacefile.ExportRDTaxBean;
import df.bean.obj.util.JDate;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;

import df.bean.obj.util.Variables;
import javax.servlet.*;
import javax.servlet.http.*;
import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadException;

/**
 *
 * @author nopphadon
 */
public class ExportFileSrvl extends HttpServlet {
    //DBConnection con1;
    //DBConn cdb;
    HttpSession session;
    ExportDFToBankBean edtb = new ExportDFToBankBean();
    ExportDFToAPBean edfap = new ExportDFToAPBean();
    ExportDFToPayrollBean edfp = new ExportDFToPayrollBean();
    ExportDFToGLBean edgl = new ExportDFToGLBean();
    ExportRDTaxBean erdt = new ExportRDTaxBean();
    ExportSAPGLBean esap = new ExportSAPGLBean();
    ExportSAPGLPaymentBean esapr = new ExportSAPGLPaymentBean();
        
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        DBConnection con2;
        DBConn cdb2;
        con2 = new DBConnection();
        con2.connectToLocal();
        cdb2 = new DBConn(con2.getConnection());
        try {
            cdb2.setStatement();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String save_file = request.getParameter("target_file");
        String process_name = request.getParameter("PROCESS_NAME");
        String pay_type = request.getParameter("PAY_TYPE");
        String month = request.getParameter("MM");
        String year = request.getParameter("YYYY");
        String bank_type = request.getParameter("BANK_TYPE");
        String payment_date = request.getParameter("PAYMENT_DATE");
        String transaction_date = request.getParameter("TRANSACTION_DATE");
        //path variable for upload or writefile to..
        String linux_path = getServletConfig().getServletContext().getRealPath("")+"/reports/output/";
        String windows_path = getServletConfig().getServletContext().getRealPath("")+"\\reports\\output\\";
        String path = "";
        //link variable for show webpage link
        String link = "./reports/output/";
        //System.out.println("Pay Type : "+pay_type);
        try{
            if(pay_type != null && pay_type.length()>2){
            	pay_type = pay_type.substring(1);
            }
        }catch(Exception e){
        	System.out.println("Pay Type Exception : "+e);
        }
        path = Variables.IS_WINDOWS ? windows_path : linux_path;
        String upload_path = path+save_file+".txt";
        String bank_upload_path = getIPBankEncrypt(hospital_code)+save_file+".txt";
        String encrypt_target_path = getIPBankEncryptFile(hospital_code)+save_file+"-hash.txt";

        try {
            if(process_name.equals("ExportAP") ){
                reportGenerateFile(link + save_file+".txt", response, edfap.exportData(save_file, hospital_code, pay_type, year, month, cdb2, upload_path));
            }
            if(process_name.equals("ExportBank") ){
            	edtb.setOwnerBank(bank_type);
            	edtb.setPaymentDate(payment_date);
            	if(bank_type.equals("017")){ //citi bank
            		reportGenerateFileBank(link + save_file+".txt", response, edtb.exportData(save_file, hospital_code, pay_type, year, month, cdb2, bank_upload_path),edtb.getMessage());            		
            	}else if(bank_type.equals("014")){
            		reportGenerateFileBank(link + save_file+".txt", response, edtb.exportData(encrypt_target_path, hospital_code, pay_type, year, month, cdb2, bank_upload_path),edtb.getMessage()); 
            	}else{ //other bank
            		reportGenerateFileBank(save_file+".txt", response, edtb.exportData(save_file, hospital_code, pay_type, year, month, cdb2, bank_upload_path),edtb.getMessage());            		
            	}
            }
            
            if(process_name.equals("ExportPayroll") ){
                reportGenerateFile(link + save_file+".txt", response, edfp.exportData(save_file, hospital_code, pay_type, year, month, cdb2, upload_path));
            }
            
            if(process_name.equals("ExportGL")){
                reportGenerateFile(link + save_file+".txt", response, edgl.exportData(save_file, hospital_code, "GL", year, month, cdb2, upload_path));
            }
            
            if(process_name.equals("ExportAC")){
                reportGenerateFile(link + save_file+".txt", response, edgl.exportData(save_file, hospital_code, "AC", year, month, cdb2, upload_path));
            }
            
            if(process_name.equals("ExportGLSAP")){
            	esap.setPayDate(payment_date);
            	save_file = "DF_AP_"+this.getHospitalCode(hospital_code)+ "" +year+ "" + month + "_" + JDate.getDate() +  "" + JDate.getTime();
            	upload_path = path+save_file+".TXT";
            	reportGenerateFile(link + save_file+ ".TXT", response, esap.exportData(save_file, hospital_code, "GL", year, month, cdb2, upload_path));
            }
            
            if(process_name.equals("ExportGLSAPR2C")){
            	esapr.setPayDate(payment_date);
            	save_file = "DF_AP_"+this.getHospitalCode(hospital_code)+ "" +year+ "" + month + "_" + JDate.getDate() +  "" + JDate.getTime();
            	upload_path = path+save_file+".TXT";
            	reportGenerateFile(link + save_file+ ".TXT", response, esapr.exportData(save_file, hospital_code, "GL", year, month, cdb2, upload_path));
            }
            
            if(process_name.equals("ExportACSAP")){
            	save_file = "DF_ACC_"+this.getHospitalCode(hospital_code) + "" +year+ "" + month + "_" + JDate.getDate() +  "" + JDate.getTime();
                upload_path = path+save_file+".TXT";
            	reportGenerateFile(link + save_file+ ".TXT", response, esap.exportData(save_file, hospital_code, "AC", year, month, cdb2, upload_path));
            }
            
            if(process_name.equals("ExportRD")){
            	//erdt.exportData(save_file, hospital_code, "", year, month, cdb2, path);
            	erdt.setPaymentDate(payment_date.replaceAll("/", ""));
                reportGenerateFile(link + save_file+".txt", response, erdt.exportData(save_file, hospital_code, pay_type, year, month, cdb2, upload_path),erdt.getMessage());            	
            }
        } finally { 
            //out.close();
        }
    } 
    private String getIPBankEncrypt(String hp_code){
    	String IP = "";
    	String query = "SELECT VALUE FROM CONFIG WHERE NAME='IP_INTERFACE_CITI_BANK' AND HOSPITAL_CODE = '"+hp_code+"'";
        ResultSet rs = null;
        DBConnection con = new DBConnection();
        con.connectToLocal();

        rs = con.executeQuery(query);
        try {
            while (rs != null && rs.next()) {
                //IP = "\\\\" + rs.getString("VALUE").toString(); //for IP
                IP = "" + rs.getString("VALUE").toString(); //for Path
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception err) {
        }
        con.Close();
        System.out.println("IP = "+IP);
        return IP;
    }
    
    private String getIPBankEncryptFile(String hp_code){
    	String IP = "";
    	String query = "SELECT VALUE FROM CONFIG WHERE NAME='IP_SCB_ENCRYPTION' AND HOSPITAL_CODE = '"+hp_code+"'";
        ResultSet rs = null;
        DBConnection con = new DBConnection();
        con.connectToLocal();

        rs = con.executeQuery(query);
        try {
            while (rs != null && rs.next()) {
                //IP = "\\\\" + rs.getString("VALUE").toString(); //for IP
                IP = "" + rs.getString("VALUE").toString(); //for Path
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception err) {
        }
        con.Close();
        System.out.println("IP = "+IP);
        return IP;
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
    
    private void reportGenerateFile(String filename, HttpServletResponse response, boolean status){
    	reportGenerateFile(filename, response, status, "");
    }

    private void reportGenerateFile(String filename, HttpServletResponse response, boolean status, String message){
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (Exception ex) {System.out.println("PrintWriter Ex : "+ex);}
        //System.out.println(filename);
        if(status){
            response.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Generate Report Servlet</title>");  
            out.println("</head>");
            out.println("<body bgcolor=\"#dde4e8\">");
            out.println("<h1 align=\"center\">Report Generate File</h1>");
            out.println("<p align=\"center\">" +
                        "<a href=\""+filename+"\">" +
                        "<img src=\"./images/txt_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p><br>");
            out.println("<p align=\"center\">Write File Report Complete</p><br>");
            out.println("</body>");
            out.println("</html>");
        }else{
            response.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>View Report Servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1 align=\"center\">Report Result</h1>");
            out.println("<p align=\"center\">Can't write interface file.</p>");
            out.println("<p align=\"center\">"+message+"</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    private void reportGenerateFileBank(String filename, HttpServletResponse response, boolean status, String message){
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {System.out.println("PrintWriter Ex : "+ex);}
        System.out.println(filename);
        System.out.println("Info : " + status);
        if(status){
        	 response.setContentType("text/html");
             out.println("<html>");
             out.println("<head>");
             out.println("<title>Generate Report Servlet</title>");  
             out.println("</head>");
             out.println("<body bgcolor=\"#dde4e8\">");
             out.println("<h1 align=\"center\">Report Generate File</h1>");
             /*
             out.println("<p align=\"center\">" +
                         "<a href=\""+filename+"\">" +
                         "<img src=\"./images/txt_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p><br>");
             */
             out.println("<p align=\"center\">Write File Report Complete</p><br>");
             out.println("</body>");
             out.println("</html>");
        }else{
            response.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>View Report Servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1 align=\"center\">Report Result</h1>");
            out.println("<p align=\"center\">Can't write interface file BANK.</p>");
            out.println("<p align=\"center\">"+message+"</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    /**
     * @TODO for hospital code 3 
     * @param hospital_code
     * @return
     */
    private String  getHospitalCode(String hospital_code){
    	 if(hospital_code.length() == 5 &&  hospital_code != null) 
    		 	return hospital_code.substring(2, 5);
    	 return hospital_code;
    }
}