package df.servlet.interfacefile;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.interfacefile.ImportDischargeExcelBean;
import df.bean.interfacefile.ImportExpenseBean;
import df.bean.interfacefile.ImportGuaranteeBean;
import df.bean.interfacefile.ImportNewMasterBean;
import df.bean.interfacefile.ImportTransactionOnWardBean;
import df.bean.interfacefile.ImportTimeTableBean;
import df.bean.interfacefile.ImportTransactionArReceiptBean;
import df.bean.interfacefile.ImportTransactionBean;
import df.bean.interfacefile.ImportTransactionResultBean;
import df.bean.interfacefile.InterfaceOPDCheckup;
import df.bean.interfacefile.ImportExpenseExcelBean;
import df.bean.obj.util.FileConn;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;
import java.io.*;
import java.sql.ResultSet;
import javax.servlet.*;
import javax.servlet.http.*;
import javazoom.upload.*;

/**
 *
 * @author nopphadon
 */
public class ImportFileSrvl extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpSession session;    
    private String getIP(String cond, String hospital) {
        String IP = null;
        DBConnection con = new DBConnection();
        con.connectToLocal();
        String query = "";
        query = "SELECT VALUE FROM CONFIG WHERE NAME='" + cond + "' AND HOSPITAL_CODE = '"+hospital+"'";
        ResultSet rs = null;
        System.out.println(query);
        rs = con.executeQuery(query);
        try {
            while (rs != null && rs.next()) {
                IP = "" + rs.getString("VALUE").toString(); //for Path
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception err) {
        }
        con.Close();
        return IP;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ImportTransactionBean it = null;
        ImportTransactionResultBean idr = null;
        ImportGuaranteeBean igu = null;
        ImportExpenseBean iex = null;
        ImportExpenseExcelBean iee = null;
        ImportTimeTableBean itt = null;
        ImportTransactionOnWardBean iow = null;
        InterfaceOPDCheckup ioc = null;
        ImportNewMasterBean inmb = null;
        ImportTransactionArReceiptBean iar = null;
    	ImportDischargeExcelBean dis = null;

    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        session = request.getSession(true);
        MultipartFormDataRequest mrequest = null;
        String filename = "";
        String source_file = "";
        String ip = "";
        String hospital_code = "";
        String dt = "";
        String user_id = "";
        String upload_path = "";//getServletConfig().getServletContext().getRealPath("")+"\\reports\\output\\";
        DBConnection con1 = new DBConnection();
        con1.connectToLocal();
        DBConn cdb = new DBConn(con1.getConnection());
        try {
            cdb.setStatement();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        //out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        try {
            mrequest = new MultipartFormDataRequest(request);
            hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
            user_id = session.getAttribute("USER_ID").toString();
            upload_path = getIP("IP_UPLOAD_FILE",hospital_code);
            if ("date".equalsIgnoreCase(mrequest.getParameter("selectFileType").toString())) {
            	//select from date
                dt = mrequest.getParameter("INTERFACE_DATE");
                if (mrequest.getParameter("INTERFACE_PROCESS").equals("ImportTransaction")) {
                    ip = getIP("IP_INTERFACE_TRANSACTION",hospital_code);
                    source_file = ip + "DF" + hospital_code + "." + JDate.saveDate(dt);
                } else if (mrequest.getParameter("INTERFACE_PROCESS").equals("ImportVerifyTransaction")) {
                    ip = getIP("IP_INTERFACE_TRANSACTION_RESULT",hospital_code);
                    source_file = ip + "DFVerify" + hospital_code + "." + JDate.saveDate(dt);
                } else if (mrequest.getParameter("INTERFACE_PROCESS").equals("ImportARTransaction")) {
                    ip = getIP("IP_INTERFACE_AR_TRANSACTION",hospital_code);
                    source_file = ip + "ARDF"+ hospital_code +"." + JDate.saveDate(dt);
                } else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportExpense")){
                    ip = getIP("IP_INTERFACE_CO",hospital_code);
                    source_file = ip + "DFCO" + hospital_code + "." + JDate.saveDate(dt);
                } else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportOnWard")){
                    ip = getIP("IP_INTERFACE_TRANSACTION_ONWARD",hospital_code);
                    source_file = ip + "DFOnward" + hospital_code + "." + JDate.saveDate(dt);
                } else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportOPDCheckup")){
                    ip = getIP("IP_INTERFACE_OPD_CHECKUP",hospital_code);
                    source_file = ip + "DFCHK" + hospital_code + "." + JDate.saveDate(dt);
                } else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportDischargeSummary")){
                    ip = getIP("IP_INTERFACE_DISCHARGE_SUMMARY",hospital_code);
                    source_file = ip + "DFDischarge" + hospital_code + "." + JDate.saveDate(dt);
                } else { /*developer not implement code here*/ }
                System.out.print( Variables.IS_TEST ? "Interface source file : "+source_file+"\n" : "" );
            } else {
                //select from file
                filename = mrequest.getParameter("SOURCE_FILE");
                filename = filename.replace("\\", "|");
                String[] fname = filename.split("[|]");

                if(Variables.IS_WINDOWS){
                    source_file = upload_path+fname[fname.length - 1];
                    source_file = source_file.replace("/", "\\");
                }else{ 
                	upload_path = getServletConfig().getServletContext().getRealPath("")+"/reports/output/";
                    source_file = upload_path+fname[fname.length - 1];
                }
                
                System.out.print( Variables.IS_TEST ? "Interface source browse file : "+source_file+"\n" : "" );
                String[]fileDate = source_file.split("[.]");
                if(fileDate.length>1){
                	dt = fileDate[1];
                }
                File f = new File(source_file);
                
                try{
                    if (f.exists()) {
                        f.delete();
                    }                	
                }catch(Exception e){
                	System.out.println("File Exists Exception : "+e);
                }
                
                UploadBean up = new UploadBean();
                up.setFolderstore(upload_path);
                up.store(mrequest, "FILE_INTERFACE");
            }
            out.println(source_file);
        } catch (UploadException ex) {
        	System.out.println("Upload Exception : "+ex);
        }
        
        if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportTransaction")){
        	inmb = new ImportNewMasterBean();
        	it =  new ImportTransactionBean();
        	it.setDate(dt);
        	it.setUserName(user_id);
        	it.setHospitalCode(hospital_code);
        	
            System.out.print( Variables.IS_TEST ? "Start Interface Transaction : "+JDate.getTime()+"\n" : "" );
            System.out.println("Info source_file : " + source_file);
            
            String sqlHospital = "SELECT DOCTOR_PRIVATE FROM HOSPITAL WHERE CODE = '"  + hospital_code + "' AND ACTIVE = '1'";
            
            System.out.println("Infor : " + sqlHospital);
            
            ResultSet rsHospital =  con1.executeQuery(sqlHospital);
            
            try { 
            	rsHospital.next();
                if( rsHospital.getString("DOCTOR_PRIVATE").equals("Y") ? it.insertData(source_file,con1) : it.insertDataNoDoctorPrivate(source_file,con1)){
	            	System.out.print( Variables.IS_TEST ? "Finish Interface Transaction : "+JDate.getTime()+"\n"+rsHospital.getString("DOCTOR_PRIVATE") : "" );
	                if(inmb.ImportNewMaster(it.getBillDate(), hospital_code, cdb)){
	                    out.println("<body bgcolor=\"#dde4e8\"><p><p>");
	                    out.println("<script language='javascript'>");
	                    out.println("alert('Import Transaction "+it.getMessage()+"');");
	                    out.println("</script>");
	                    out.println("</body>");
	                    out.close();  
	                }else{
	                    out.println("<body bgcolor=\"#dde4e8\"><p><p>");
	                    out.println("<script language='javascript'>");
	                    out.println("alert('Cannot Auto Insert Master Data'"+inmb.getMessage()+");");
	                    out.println("</script>");
	                    out.println("</body>");
	                    out.close(); 
	                }
	                              
	            }else{
	                out.println("<body>");
	                out.println("<script language='javascript'>");
	                out.println("alert('Import Transaction Error : "+it.getMessage()+" / Source : "+source_file+"');");
	                out.println("</script>");                
	                out.println("</body>");
	                out.close();
	            }
            }catch (Exception ex) {
            	ex.printStackTrace();
            }
            
            
        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportVerifyTransaction")){
        	
        	idr = new ImportTransactionResultBean();
        	idr.setUserName(user_id);
        	idr.setHospitalCode(hospital_code);
        	
            if(idr.insertData(source_file,con1)){
                out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Verify " + idr.getMessage()+"');");
                out.println("</script>");
                out.println("</body>");
                out.close();
            }else{
                out.println("<body>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Verify Error : "+idr.getMessage()+" / Source : "+source_file+"');");
                out.println("</script>");                
                out.println("</body>");
                out.close();
            }
        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportARTransaction")){
        	iar = new ImportTransactionArReceiptBean();
        	iar.setUserName(user_id);
        	iar.setHospitalCode(hospital_code);
            
        	if(iar.insertData(source_file,con1)){
                out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                out.println("<script language='javascript'>");
                out.println("alert('Import ERP Recepit "+iar.getMessage()+"');");
                out.println("</script>");
                out.println("</body>");
                out.close();
            }else{
                out.println("<body>");
                out.println("<script language='javascript'>");
                out.println("alert('Import ERP Recepit Error "+iar.getMessage()+" / Source : "+source_file+"');");
                out.println("</script>");                
                out.println("</body>");
                out.close();
            }
        	
        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportGuarantee")){
        	
        	igu = new ImportGuaranteeBean();
        	
            if(igu.insertData(source_file,con1)){
                out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Guarantee "+igu.getMessage()+"');");
                out.println("</script>");
                out.println("</body>");
                out.close();
            }else{
                out.println("<body>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Guarantee Error "+igu.getMessage()+" / Source : "+source_file+"');");
                out.println("</script>");                
                out.println("</body>");
                out.close();
            }
        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportExpense")){
        	
        	iex = new ImportExpenseBean();
        	iex.setHospital(hospital_code);
        	
            if(iex.insertData(source_file,con1)){
                out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                out.println("<script language='javascript'>");
                out.println("alert('Import C/O "+iex.getMessage()+"');");
                out.println("</script>");
                out.println("</body>");
                out.close();
            }else{
                out.println("<body>");
                out.println("<script language='javascript'>");
                out.println("alert('Import C/O Error : "+iex.getMessage()+" / Source : "+source_file+"');");
                out.println("</script>");
                out.println("</body>");
                out.close();
            }
            
        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportPatho")){
            if(it.insertData(source_file,con1)){
                if(inmb.ImportNewMaster(it.getBillDate(), hospital_code, cdb)){
                    out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                    out.println("<script language='javascript'>");
                    out.println("alert('Import Patho "+it.getMessage()+"');");
                    out.println("</script>");
                    out.println("</body>");
                    out.close();  
                }else{
                    out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                    out.println("<script language='javascript'>");
                    out.println("alert('Cannot Auto Insert Master Data'"+inmb.getMessage()+" / Source : "+source_file+"');");
                    out.println("</script>");
                    out.println("</body>");
                    out.close(); 
                }
            }else{
                out.println("<body>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Patho Error : "+it.getMessage()+" / Source : "+source_file+"');");
                out.println("</script>");                
                out.println("</body>");
                out.close();
            }
            
        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportExpenseExcel")){
        	iee  = new ImportExpenseExcelBean();
        	iee.setUserName(user_id);
        	iee.setHospital(hospital_code);
        	
            if(iee.insertData(source_file,con1)){
                out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Expense "+iee.getMessage()+"');");
                out.println("</script>");
                out.println("</body>");
                out.close();
            }else{
                out.println("<body>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Expense Error : "+iee.getMessage()+" / Source : "+source_file+"');");
                out.println("</script>");
                out.println("</body>");
                out.close();
            }
        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportTimeTable")){
        	
        	itt = new ImportTimeTableBean();
        	itt.setUserName(session.getAttribute("USER_ID").toString());
        	itt.setHospital(session.getAttribute("HOSPITAL_CODE").toString());
            
        	if(itt.insertData(source_file,con1)){
                out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Time Table "+itt.getMessage()+"');");
                out.println("</script>");
                out.println("</body>");
                out.close();
            }else{
                out.println("<body>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Time Table Error : " + itt.getMessage() + " / Source : " + source_file + "');");
                out.println("</script>");
                out.println("</body>");
                out.close();
            }
        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportOnWard")){
        	inmb = new ImportNewMasterBean();
        	iow= new ImportTransactionOnWardBean();
        	iow.setDate(dt);
        	iow.setUserName(user_id);
        	iow.setHospitalCode(hospital_code);
            
        	System.out.print( Variables.IS_TEST ? "Start Interface Onward : "+JDate.getTime()+": "+source_file+"\n" : "" );
           
            if(iow.insertData(source_file,con1)){
                System.out.print( Variables.IS_TEST ? "Finish Interface Onward : "+JDate.getTime()+"\n" : "" );
               
                if(inmb.ImportNewMaster(iow.getBillDate(), hospital_code, cdb)){
                    out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                    out.println("<script language='javascript'>");
                    out.println("alert('Import Transaction "+iow.getMessage()+"');");
                    out.println("</script>");
                    out.println("</body>");
                    out.close();  
                }else{
                    out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                    out.println("<script language='javascript'>");
                    out.println("alert('Cannot Auto Insert Master Data'"+inmb.getMessage()+");");
                    out.println("</script>");
                    out.println("</body>");
                    out.close(); 
                }
                              
            }else{
                out.println("<body>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Transaction Error : "+iow.getMessage()+" / Source : "+source_file+"');");
                out.println("</script>");                
                out.println("</body>");
                out.close();
            }
        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportOPDCheckup")){
    		FileConn fc = new FileConn();
        	inmb = new ImportNewMasterBean();
        	ioc= new InterfaceOPDCheckup();
        	ioc.setTransactionDate(dt);
        	ioc.setUserID(user_id);
        	ioc.setHospitalCode(hospital_code);
            
        	System.out.print( Variables.IS_TEST ? "Start Interface OPD Checkup : "+JDate.getTime()+": "+source_file+"\n" : "" );
        	System.out.println(ioc.getTransactionDate()+"//"+ioc.getUserID());
        	
            if(ioc.doProcess(fc.getDataFromFile(source_file))){
                System.out.print( Variables.IS_TEST ? "Finish Interface OPD Checkup : "+JDate.getTime()+"\n" : "" );
               
                if(inmb.ImportNewMaster(ioc.getTransactionDate(), hospital_code, cdb)){
                    out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                    out.println("<script language='javascript'>");
                    out.println("alert('Interface OPD Checkup Transaction "+ioc.getMessage()+"');");
                    out.println("</script>");
                    out.println("</body>");
                    out.close();  
                }else{
                    out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                    out.println("<script language='javascript'>");
                    out.println("alert('Cannot Auto Insert Master Data'"+inmb.getMessage()+");");
                    out.println("</script>");
                    out.println("</body>");
                    out.close(); 
                }
                              
            }else{
                out.println("<body>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Transaction Error : "+ioc.getMessage()+" / Source : "+source_file+"');");
                out.println("</script>");                
                out.println("</body>");
                out.close();
            }

        }else if(mrequest.getParameter("INTERFACE_PROCESS").equals("ImportDischargeSummary")){
        	//> Tom[My] 06122012 1143 AM
        	dis = new ImportDischargeExcelBean();
        	dis.setUserName(session.getAttribute("USER_ID").toString());
        	dis.setHospital(session.getAttribute("HOSPITAL_CODE").toString());
            System.out.print( Variables.IS_TEST ? "1.Start Interface ImportDischargeSummary : "+JDate.getTime()+"<>"+source_file+"\n" : "" );
        	if(dis.insertData(source_file,con1)){
        		out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                out.println("<script language='javascript'>");
                out.println("alert('Import Discharge Summary Complete');");
                out.println("</script>");
                out.println("</body>");
                out.close();
        	}else{
        		System.out.println("Alert : "+dis.getMessage());
        		out.println("<body bgcolor=\"#dde4e8\"><p><p>");
                out.println("<script language='javascript'>");
                //out.println("alert('Import Discharge Summary Error "+dis.getMessage()+"');");
                out.println("alert('Import Discharge Summary InComplete');");
                out.println("</script>");
                out.println("</body>");
                out.close();
        	}
        }
        con1.Close();
        cdb.closeDB("");
    } 

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}