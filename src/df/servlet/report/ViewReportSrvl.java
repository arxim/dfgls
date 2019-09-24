/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.report;

import df.bean.db.conn.DBConn;
import df.bean.interfacefile.ExportReportSummaryDailyBean;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;
import df.bean.report.GenerateReportBean;
import df.bean.report.ReportQuery;
import df.bean.report.VerifyAllowViewReportBean;

import java.io.*;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 * @author nopphadon
 */

public class ViewReportSrvl extends HttpServlet {
	final static Logger logger = Logger.getLogger(ViewReportSrvl.class);
	
    String link = "./reports/output/";
    ExportReportSummaryDailyBean ers;
    private String hp_name;
    private boolean test_status = false;
    
    
    private String getHp_name() {
		return hp_name;
	}
	private void setHp_name(String hp_code) {
		DBConn conn = null;
		try{
			if(conn==null){
				conn = new DBConn();
				conn.setStatement();
			}
			String qHp = "SELECT DESCRIPTION_THAI FROM HOSPITAL WHERE CODE='"+ hp_code +"'";
			String[][] arr = conn.query(qHp);
			this.hp_name = arr[0][0];
		}catch(Exception err){
			conn.closeDB("");
			logger.error(err.getMessage());
		}
		conn.closeDB("");
	}
	
	@Override
    public void init() throws ServletException {
        super.init();       
        ers = new ExportReportSummaryDailyBean();

    }
    /*
    @Override
    public void destroy(){
        super.destroy();
        con1.freeConnection();
    }
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
        try {
        	// set hospital name
        	this.setHp_name(session.getAttribute("HOSPITAL_CODE").toString());
        	
            if(request.getParameter("REPORT_MODULE").equals("checklist")){
                reportInterfaceIn(request,response);                
            }else if(request.getParameter("REPORT_MODULE").equals("error_log")){
                reportErrorLog(request,response);
            }else if(request.getParameter("REPORT_MODULE").equals("df_monthly_checklist")){
                reportMonthlyChecklist(request,response);
            }else if(request.getParameter("REPORT_MODULE").equals("df_monthly")){
                reportMonthly(request,response);
            }else if(request.getParameter("REPORT_MODULE").equals("doctor_report")){
                reportDoctor(request,response);
            }else if(request.getParameter("REPORT_MODULE").equals("df_report")){
                reportDf(request,response);
            }else if(request.getParameter("REPORT_MODULE").equals("df_monthly_behind_payment")){
                reportMonthlyBehindPayment(request,response);
            }else if(request.getParameter("REPORT_MODULE").equals("df_center_management")){
            	this.reportCenterManagement(request, response);
            }else if(request.getParameter("REPORT_MODULE").equals("df_guarentee")){
            	this.reportDoctorProfileGuarantee(request, response);
            }else if(request.getParameter("REPORT_MODULE").equals("tax_report")){
            	this.reportTax(request, response);
            }else if(request.getParameter("REPORT_MODULE").equals("transaction")){
            	this.reportTransaction(request, response);	
            }else if(request.getParameter("REPORT_MODULE").equals("tax_doctor_report")){
            	this.reportTaxDoctor(request, response);
            }else if(request.getParameter("REPORT_MODULE").equals("management_revenue")){
            	this.reportRevenue(request, response);	
            }else if(request.getParameter("REPORT_MODULE").equals("expense_deduct")){
            	this.reportExpenseDeduct(request, response);
            }else if(request.getParameter("REPORT_MODULE").equals("doctor_info_report")){
            	this.reportDoctorInfo(request, response);	
            }else{
                reportVerifyNewMaster(request,response);
            }
        } catch (Exception ex) {
            responseError(ex.toString(),response);
        }
    }
    private void reportExpenseDeduct(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String from_doctor = request.getParameter("DOCTOR_CODE_FROM");
        String to_doctor = request.getParameter("DOCTOR_CODE_TO");
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String next_month = request.getParameter("NEXT_MONTH");
        //String expense_account_code = request.getParameter("EXPENSE_ACCOUNT_CODE");
        String expense_code = request.getParameter("EXPENSE_CODE");
        String path_show=getServletConfig().getServletContext().getRealPath("")+"\\reports\\";
        try{
            if( from_doctor.equals("") || from_doctor.equals(null) ){from_doctor = "0";}
        }catch(Exception e){}
        try{
            if( to_doctor.equals("") || to_doctor.equals(null) ){to_doctor = "Z";}
        }catch(Exception e){}
        
        /*try{
            if( expense_account_code.equals("") || expense_account_code.equals(null) ){expense_account_code = "%";}
        }catch(Exception e){}*/
        try{
            if( expense_code.equals("") || expense_code.equals(null) ){expense_code = "%";}
        }catch(Exception e){}      
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}

         logger.info("to_doctor="+to_doctor);
         logger.info("from_doctor="+from_doctor);
        hm.put("hospital_code", hospital_code);
        hm.put("hospital_name", this.getHp_name());
        hm.put("from_doctor", from_doctor);
        hm.put("to_doctor", to_doctor);
        hm.put("month", month);
        hm.put("year", year);
        hm.put("next_month", next_month);
        hm.put("expense_code", expense_code);
        hm.put("path_show", path_show);
        
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            logger.info(reportfilename);
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }
    }
    private void reportTransaction(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String path = request.getRealPath("/") + "reports\\";
        String type_report = request.getParameter("TYPE_REPORT");
        String doctor_code = request.getParameter("DOCTOR_CODE");
        String payor_office_code = request.getParameter("PAYOR_OFFICE_CODE");
        String order_item_code = request.getParameter("ORDER_ITEM_CODE");
        String hn_no = request.getParameter("HN_NO");
        String invoice_no = request.getParameter("INVOICE_NO");
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        try{
            if( doctor_code.equals("") || doctor_code.equals(null) ){doctor_code = "%";}
        }catch(Exception e){}
        try{
            if( payor_office_code.equals("") || payor_office_code.equals(null) ){payor_office_code = "%";}
        }catch(Exception e){}
        try{
            if( order_item_code.equals("") || order_item_code.equals(null) ){order_item_code = "%";}
        }catch(Exception e){}
        try{
            if( hn_no.equals("") || hn_no.equals(null) ){hn_no = "%";}
        }catch(Exception e){}
        try{
            if( invoice_no.equals("") || invoice_no.equals(null) ){invoice_no = "%";}
        }catch(Exception e){}
        
        hm.put("hospital_code", hospital_code);
        hm.put("LogoPd", path);
        hm.put("type_report", type_report); 
        hm.put("month", month);
        hm.put("year", year);
        hm.put("doctor_code", doctor_code);
        hm.put("payor_office_code", payor_office_code);
        hm.put("order_item_code", order_item_code);
        hm.put("hn_no", hn_no);
        hm.put("invoice_no", invoice_no);
        //logger.info("type_report="+type_report+" : "+year+" : "+month+":"+hospital_code+
        //		":"+doctor_code+":"+payor_office_code+":"+order_item_code+":"+hn_no+":"+invoice_no);
        
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }    	
    }
    private void reportTax(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String path = request.getRealPath("/") + "reports\\";
        String path_show = getServletConfig().getServletContext().getRealPath("")+"\\reports\\";
        String pay_date = request.getParameter("PAY_DATE");
        String doctor_code_to = request.getParameter("DOCTOR_CODE_TO");
        String doctor_code = request.getParameter("DOCTOR_CODE_FROM");
        String term = request.getParameter("TERM");
        String term_year = request.getParameter("YEAR");
        String tax_year = request.getParameter("YYYY402");
        String printing_date = request.getParameter("PRINTING_DATE");
        String print_date = request.getParameter("PRINT_DATE");
        String filling_date = request.getParameter("FILLING_DATE");
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        try{
            if( doctor_code.equals("") || doctor_code.equals(null) ){doctor_code = "%";}
        }catch(Exception e){}
        try{
            if( doctor_code_to.equals("") || doctor_code_to.equals(null) ){doctor_code_to = "%";}
        }catch(Exception e){}

        hm.put("hospital_code", hospital_code);
        hm.put("hospital_logo", path);
        hm.put("mm", month);
        hm.put("path_show", path_show);
        hm.put("filling_date", filling_date);
        
        if("TaxLetter406".equalsIgnoreCase(reportfilename)){
        	hm.put("term", term);
        	hm.put("doctor_code", doctor_code); 
        	hm.put("yyyy", term_year);
        	hm.put("print_date", JDate.saveDate(print_date));
            hm.put("signature", path);
        }else if("ReportSummaryFrontPage01".equalsIgnoreCase(reportfilename)||"Tax402SummaryYearlyFrontPage".equalsIgnoreCase(reportfilename)){
        	hm.put("yyyy", Integer.parseInt(year));
        }else if("Tax402SummaryYearly".equalsIgnoreCase(reportfilename)){
        	hm.put("doctor", doctor_code_to);
        	hm.put("year", year);
           	hm.put("month", month);
           	//hm.put("year", year);
           	hm.put("print_date", JDate.saveDate(printing_date));
        }else{
        	hm.put("yyyy", year);
        	hm.put("pay_date", pay_date);
        }

        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }
        logger.info(hospital_code+"<>"+reportfilename+" Year : "+year+" Pay : "+pay_date+" Print : "+JDate.saveDate(printing_date)+" Month : "+month+" Doctor : "+doctor_code_to);
    }
    private void reportDoctorProfileGuarantee(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String doctor_code = request.getParameter("DOCTOR_CODE");
        String doctor_profile_code = request.getParameter("DOCTOR_PROFILE_CODE");
        String doctor_type_code = request.getParameter("DOCTOR_TYPE_CODE");
        String payment_mode_code = request.getParameter("PAYMENT_MODE_CODE");
        String payment_term = request.getParameter("term");
        String path = request.getRealPath("/") + "reports\\";
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        try{        
        	if( doctor_code.equals("") || doctor_code.equals(null) ){doctor_code = "%";}
        }catch(Exception e){}       
        try{
            if( doctor_profile_code.equals("") || doctor_profile_code.equals(null) ){doctor_profile_code = "%";}
        }catch(Exception e){}          
        
        hm.put("hospital_code", hospital_code);
        hm.put("month", month);
        hm.put("year", year);
        hm.put("term", payment_term);
        hm.put("doctor_code", doctor_code);
        hm.put("doctor_profile_code", doctor_profile_code);
        hm.put("doctor_type_code", doctor_type_code);
        hm.put("payment_mode_code", payment_mode_code);
        hm.put("LogoBGH", path);
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }       	
    }
    private void reportCenterManagement(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        
        String toyear = request.getParameter("TO_YYYY");
        String tomonth = request.getParameter("TO_MM");
        String from_doctor = request.getParameter("DOCTOR_CODE_FROM");
        String to_doctor = request.getParameter("DOCTOR_CODE_TO");
        
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        
        try{
            if( from_doctor.equals("") || from_doctor.equals(null) ){from_doctor = "0";}
        }catch(Exception e){}
        try{
            if( to_doctor.equals("") || to_doctor.equals(null) ){to_doctor = "Z";}
        }catch(Exception e){}
        
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        
        hm.put("hospital_code", hospital_code);        
        hm.put("invoice_mm", month);
        hm.put("invoice_yy", year);
        
        //SummaryRevenueByDetailPeriod
        hm.put("year", year);
        hm.put("month", month);
        hm.put("month_end", tomonth);
        hm.put("year_end", toyear);
        hm.put("doctor_profile_code", from_doctor);
        hm.put("doctor_profile_code_end", to_doctor);
        
        
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }    	
    }
    
    private void responseError(String e, HttpServletResponse r){
        PrintWriter out = null;
        try {
            out = r.getWriter();
        } catch (IOException ex) {
        }
        r.setContentType("text/html");
        out.println("<body>");
        out.println("Error : \n"+e);
        out.println("</body>");
    }
    
    private void reportInterfaceIn(HttpServletRequest request, HttpServletResponse response){
        ReportQuery rq = new ReportQuery();
        HashMap<Object,Object> hm = new HashMap<Object,Object>();
        DBConn cdb = new DBConn();
        try {
            cdb.setStatement();
        } catch (Exception ex) {
            logger.error(ex);
        }
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString(); //request.getParameter("HOSPITAL_CODE");
        String reportfilename = request.getParameter("REPORT_FILE_NAME");
        
        String from_date = JDate.saveDate(request.getParameter("FROM_DATE"));
        String to_date = JDate.saveDate(request.getParameter("TO_DATE"));        
        
        String doctor_profile_code = request.getParameter("DOCTOR_PROFILE_CODE");
        String doctor_code = request.getParameter("DOCTOR_CODE");
        String doctor_category = request.getParameter("DOCTOR_CATEGORY_CODE");
        String order_item_category = request.getParameter("ORDER_CATEGORY_CODE");
        String order_item = request.getParameter("ORDER_ITEM_CODE");
        String doctor_department = request.getParameter("DOCTOR_DEPARTMENT_CODE");
        String payor_office_code = request.getParameter("PAYOR_OFFICE_CODE");
        String transaction_type = request.getParameter("TRANSACTION_TYPE");
        String admission_type = request.getParameter("ADMISSION_TYPE_CODE");
        String doc_type = request.getParameter("DOCUMENT_TYPE");
        String save_file = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String transaction_module = request.getParameter("TRANSACTION_MODULE");
        String invoice_no = request.getParameter("INVOICE_NO");
        String linux_path = getServletConfig().getServletContext().getRealPath("")+"/reports/output/"+save_file+".txt";
        String windows_path = getServletConfig().getServletContext().getRealPath("")+"\\reports\\output\\"+save_file+".txt";
        String linux_logo_path = request.getRealPath("/") + "reports/";
        String windows_logo_path = request.getRealPath("/") + "reports\\";
        String logo_path = Variables.IS_WINDOWS ? windows_logo_path : linux_logo_path;
        String path = Variables.IS_WINDOWS ? windows_path : linux_path;
        String is_onward = "%";
        String is_partial = "%";
        String is_discharge="%";
        logger.info("\nPath Logo : "+logo_path+"\\"+hospital_code);

        try{
            if( from_date.equals("") || from_date.equals(null) ){from_date = "00000000";}
        }catch(Exception e){}
        try{
            if( to_date.equals("") || to_date.equals(null) ){to_date = "99999999";}
        }catch(Exception e){}
        try{
            if( doctor_code.equals("") || doctor_code.equals(null) ){doctor_code = "%";}
        }catch(Exception e){}
        try{
            if( doctor_profile_code.equals("") || doctor_profile_code.equals(null) ){doctor_profile_code = "%";}
        }catch(Exception e){}        
        try{
            if( doctor_category.equals("") || doctor_category.equals(null) ){doctor_category = "%";}
        }catch(Exception e){}
        try{
            if( order_item_category.equals("") || order_item_category.equals(null) ){order_item_category = "%";}
        }catch(Exception e){}
        try{
            if( order_item.equals("") || order_item.equals(null) ){order_item = "%";}
        }catch(Exception e){}
        try{
            if( doctor_department.equals("") || doctor_department.equals(null) ){doctor_department = "%";}
        }catch(Exception e){}
        try{
            if( transaction_type.equals("") || transaction_type.equals(null) ){transaction_type = "%";}
        }catch(Exception e){}
        try{
            if( payor_office_code.equals("") || payor_office_code.equals(null) ){payor_office_code = "%";}
        }catch(Exception e){}
        try{
            if( transaction_module.equals("OW") ){ 
            	is_onward = "Y";
            	transaction_module = "TR";
            }else if(transaction_module.equals("PT")){
            	is_partial = "Y";
            	is_onward = "N";
            	transaction_module = "AR";
        		transaction_type = "INV";
        	}else{
            	if(transaction_module.equals("AR")){
            		is_onward = "N";
            		transaction_type = "INV";
            	}else if(transaction_module.equals("TR")){
            		is_onward = "N";
            		//transaction_type = "REV";
            	}else{
                	is_onward = "%";
                	if(transaction_module.equals("DY")){
                		is_discharge="Y";
                	}else if(transaction_module.equals("DH")){
                		is_discharge="N";
            		}else{
            			is_discharge="%";
                	}
                	transaction_module = "%";
            	}
            }
            logger.info(is_discharge+"><"+transaction_module);
        }catch(Exception e){}

        try{
            if( invoice_no.equals("") || invoice_no.equals(null) ){invoice_no = "%";}
            //else{from_date = "00000000"; to_date = "99999999";}
        }catch(Exception e){}
        try{
            if( admission_type.equals("") || admission_type.equals(null) ){admission_type = "%";}
        }catch(Exception e){}
        try{
            if( save_file.equals("") || save_file.equals(null) ){save_file = "temp";}
        }catch(Exception e){}
        try{
            if( doc_type.equals("") || doc_type.equals(null) ){doc_type = "%";}
        }catch(Exception e){}
        
        String year_start = from_date.substring(0,4);
        String month_start = from_date.substring(4,6);
        String year_end = to_date.substring(0,4);
        String month_end =to_date.substring(4,6);

        hm.put("hospital_code", hospital_code);
        rq.setHospitalCode(hospital_code);
        hm.put("from_date", from_date);
        rq.setFromDate(from_date);
        hm.put("to_date", to_date);
        rq.setToDate(to_date);
        hm.put("doctor_code", doctor_code);
        rq.setDoctorCode(doctor_code);
        hm.put("doctor_profile_code", doctor_profile_code);
        rq.setDoctorProfileCode(doctor_profile_code);
        hm.put("doctor_category", doctor_category);
        rq.setDoctorCategory(doctor_category);
        hm.put("order_item", order_item);
        rq.setOrderItem(order_item);
        hm.put("order_category", order_item_category);
        rq.setOrderItemCategory(order_item_category);
        hm.put("doctor_department", doctor_department);
        rq.setDoctorDepartment(doctor_department);
        hm.put("transaction_type", transaction_type);
        rq.setTransactionType(transaction_type);
        hm.put("invoice_no", invoice_no);
        rq.setInvoiceNo(invoice_no);
        hm.put("admission_type", admission_type);
        rq.setAdmissionType(admission_type);
        hm.put("doc_type", doc_type);
        rq.setDocType(doc_type);
        hm.put("transaction_module", transaction_module);
        rq.setTransactionModule(transaction_module);
        hm.put("is_onward", is_onward);
        rq.setIsOnward(is_onward);
        hm.put("is_partial", is_partial);
        rq.setIsPartial(is_partial);
        hm.put("hospital_logo", logo_path);
        rq.setPayorOffice(payor_office_code);
        hm.put("payor_office_code", payor_office_code);
        
        hm.put("is_discharge", is_discharge);
        hm.put("YYYY_START", year_start);
        hm.put("YYYY_END", year_end);
        hm.put("MM_START", month_start);
        hm.put("MM_END", month_end);
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
        	//logger.info(hm);
            //logger.info(hospital_code+""+from_date+":"+to_date);
            //logger.info(transaction_module);
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            if(file_type.equals("txt")){
                this.reportGenerateFile(null, save_file, null, response, request, ""+ers.exportData(path, "", rq.getReport(reportfilename), null, null, cdb, null));
                cdb.closeDB("");
            }else{
                this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
            }
        }
    }
    private void reportMonthlyChecklist(HttpServletRequest request, HttpServletResponse response) {
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String from_doctor = request.getParameter("DOCTOR_CODE_FROM");
        String to_doctor = request.getParameter("DOCTOR_CODE_TO");
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String gl_type = request.getParameter("GL_TYPE");
        String guarantee_department_code = request.getParameter("GUARANTEE_DEPARTMENT_CODE");
        
        logger.info("from "+from_doctor+" to "+to_doctor);
        try{
            if( from_doctor.equals("") || from_doctor.equals(null) ){from_doctor = "0";}
        }catch(Exception e){}
        try{
            if( to_doctor.equals("") || to_doctor.equals(null) ){to_doctor = "Z";}
        }catch(Exception e){}
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        try {
        	if(guarantee_department_code.equals("") || guarantee_department_code.equals(null)) { guarantee_department_code = "%"; } 
        }catch (Exception ex){      	
        }
        
        if(reportfilename.equals("CheckSumGuarantee")&&hospital_code.equals("00009")){
        	reportfilename = reportfilename+hospital_code;
        }
        hm.put("hospital_code", hospital_code);
        hm.put("from_doctor", from_doctor);
        hm.put("to_doctor", to_doctor);
        hm.put("month", month);
        hm.put("year", year);
        hm.put("type",gl_type);
        hm.put("guarantee_department_code" , guarantee_department_code);
        
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }
    }
    private void reportDf(HttpServletRequest request, HttpServletResponse response) {
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String from_doctor = request.getParameter("DOCTOR_CODE_FROM");
        String to_doctor = request.getParameter("DOCTOR_CODE_TO");
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String as_of_date = year+month+JDate.getEndMonthDate(year, month);
        String from_date = "00000000";
        String to_date = year+month+JDate.getEndMonthDate(year, month);
        String term = request.getParameter("term");
        String payment_date = request.getParameter("PAYMENT_DATE") ;
        String batch = request.getParameter("YYYY").toString()+request.getParameter("MM").toString();
        String linux_logo_path = request.getRealPath("/") + "reports/";
        String windows_logo_path = request.getRealPath("/") + "reports\\";
        String logo_path = Variables.IS_WINDOWS ? windows_logo_path : linux_logo_path;
        try{
        	if(reportfilename.equals("SummaryDFUnpaidByDetailForDoctor") && term.equals("1")){
        		to_date = year+month+"15";
        		logger.info("Yes");
        	}
        	if(hospital_code.equals("VCH") && reportfilename.equals("SummaryRevenueByDetailForDoctor")){
        		reportfilename = reportfilename+hospital_code;
        	}
        }catch(Exception e) { }
        try{
        	if(payment_date.equals(null) || payment_date.equals("")){ payment_date = "%"; }
        }catch(Exception e){ payment_date = "%"; }
        try{
            if( from_doctor.equals("") || from_doctor.equals(null) ){from_doctor = "0";}
        }catch(Exception e){}
        try{
            if( to_doctor.equals("") || to_doctor.equals(null) ){to_doctor = "Z";}
        }catch(Exception e){}
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        
        hm.put("hospital_code", hospital_code);
        logger.info("Path Logo : "+logo_path);
        hm.put("hospital_logo", logo_path);
        hm.put("from_doctor", from_doctor);
        hm.put("to_doctor", to_doctor);
        hm.put("month", month);
        hm.put("year", year);
        hm.put("doctor", from_doctor);
        hm.put("from_date",from_date);
        hm.put("to_date",to_date);
        hm.put("as_of_date",as_of_date);
        hm.put("batch", batch);
        hm.put("payment_date", payment_date);
        hm.put("term", term);
        logger.info("Doctor = "+from_doctor+" - "+to_doctor+" Term : "+term+" Payment Date : "+payment_date);
        logger.info("Test : "+year+month+term+"Hospital_code"+hospital_code);
        VerifyAllowViewReportBean v = new VerifyAllowViewReportBean();
        boolean status = false;
        if(reportfilename.equals("TaxLetter406ForDoctor") || reportfilename.equals("Tax402SummaryYearlyForDoctor")) {
        	status = true;
        }
        else {
        	status = v.getReportPermit(hospital_code, term, month, year);
        }
    	if(request.getParameter("REPORT_DISPLAY").equals("view")){
    		if(status){
    			this.reportGenerateView(hm, reportfilename, response);
    		}else{
    			
    			this.reportGenerateView(hm,"notReport" , response);
    		}
            
        }else{
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }
    }
    private void reportMonthly(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        ReportQuery rq = new ReportQuery();
        DBConn cdb = new DBConn();
        try {
            cdb.setStatement();
        } catch (Exception ex) {
            logger.error(ex);
        }

        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String doctor = request.getParameter("DOCTOR_CODE");
        String from_doctor = request.getParameter("DOCTOR_CODE_FROM");
        String to_doctor = request.getParameter("DOCTOR_CODE_TO");
        //String doc_copy = request.getParameter("COPY");
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String doctor_category = request.getParameter("DOCTOR_CATEGORY_CODE");
        String doctor_department = request.getParameter("DOCTOR_DEPARTMENT_CODE");
        String order_item = request.getParameter("ORDER_ITEM_CODE");
        String order_item_category = request.getParameter("ORDER_ITEM_CATEGORY_CODE");
        String paymentDate = JDate.saveDate(request.getParameter("date"));
        //
        String expense_sign = request.getParameter("EXPENSE_SIGN");
        String expense_account_code = request.getParameter("EXPENSE_ACCOUNT_CODE");
        String expense_code = request.getParameter("EXPENSE_CODE");
        //Revenue by Period
        String year_start = request.getParameter("FROM_YYYY");
        String month_start = request.getParameter("FROM_MM");
        String year_end = request.getParameter("TO_YYYY");
        String month_end = request.getParameter("TO_MM");
        //String path = request.getRealPath("/") + "reports\\";
        String subreport_path=getServletConfig().getServletContext().getRealPath("")+"\\reports";
        String linux_logo_path = request.getRealPath("/") + "reports/";
        String windows_logo_path = request.getRealPath("/") + "reports\\";
        String linux_path = getServletConfig().getServletContext().getRealPath("")+"/reports/output/"+file_save+".txt";
        String windows_path = getServletConfig().getServletContext().getRealPath("")+"\\reports\\output\\"+file_save+".txt";
        String logo_path = Variables.IS_WINDOWS ? windows_logo_path : linux_logo_path;
        String path = Variables.IS_WINDOWS ? windows_path : linux_path;

        try{
            if( from_doctor.equals("") || from_doctor.equals(null) ){from_doctor = "0";}
        }catch(Exception e){}
        try{
            if( to_doctor.equals("") || to_doctor.equals(null) ){to_doctor = "Z";}
        }catch(Exception e){}
        try{
            if( doctor_department.equals("") || doctor_department.equals(null) ){doctor_department = "%";}
        }catch(Exception e){}
        try{
            if( order_item.equals("") || order_item.equals(null) ){order_item = "%";}
        }catch(Exception e){}
        try{
            if( order_item_category.equals("") || order_item_category.equals(null) ){order_item_category = "%";}
        }catch(Exception e){}
        try{
            if( doctor_category.equals("") || doctor_category.equals(null) ){doctor_category = "%";}
        }catch(Exception e){}
        try{
            if( doctor.equals("") || doctor.equals(null) ){doctor = "%";}
        }catch(Exception e){doctor = "%";}
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        
        //
        try{
            if( paymentDate.equals("") || paymentDate.equals(null) ){paymentDate = "%";}
        }catch(Exception e){}
        try{
            if( expense_sign.equals("") || expense_sign.equals(null) ){expense_sign = "%";}
        }catch(Exception e){}
        try{
            if( expense_account_code.equals("") || expense_account_code.equals(null) ){expense_account_code = "%";}
        }catch(Exception e){}
        try{
            if( expense_code.equals("") || expense_code.equals(null) ){expense_code = "%";}
        }catch(Exception e){}
        try{
        	if( reportfilename.equals("SummaryRevenueByDetail") && hospital_code.equals("00001")){
        		reportfilename = "SummaryRevenueByDetail00001";
        	}
        }catch(Exception e){}
        //logger.error("Payment Date : "+paymentDate);
        hm.put("hospital_code", hospital_code);
        rq.setHospitalCode(hospital_code);
        hm.put("hospital_name", this.getHp_name());
        hm.put("doctor_code", doctor);
        hm.put("from_doctor", from_doctor);
        hm.put("to_doctor", to_doctor);
        hm.put("doctor_category", doctor_category);
        hm.put("doctor_department", doctor_department);
        hm.put("order_item_category", order_item_category);
        rq.setOrderItemCategory(order_item_category);
        hm.put("order_item", order_item);
        hm.put("month", month);
        rq.setMonth(month);
        hm.put("year", year);
        rq.setYear(year);
        hm.put("payment_date",paymentDate);
        hm.put("term","%");
        //
        hm.put("expense_sign", expense_sign);
        hm.put("expense_account_code", expense_account_code);
        hm.put("expense_code", expense_code);
        //
        hm.put("doctor_profile_code", (from_doctor=="0"?"%":from_doctor));
        hm.put("year_start", year_start);
        hm.put("month_start", month_start);
        hm.put("year_end", year_end);
        hm.put("month_end", month_end);
        hm.put("hospital_logo", logo_path);
        hm.put("SUBREPORT_DIR", subreport_path);
        
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            logger.info("Payment Date = "+paymentDate+" - "+from_doctor+"<>"+to_doctor+":"+doctor+" | "+file_type+" | "+doctor_department);
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            logger.info(reportfilename);
            if(file_type.equals("txt")){
            	logger.info(file_type+"<>"+path);
                logger.info(rq.getReport(reportfilename));
                this.reportGenerateFile(null, file_save, null, response, request, ""+ers.exportData(path, reportfilename, rq.getReport(reportfilename), null, null, cdb, null));
                cdb.closeDB("");
            }else{
                this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
            }
        }
    }
    private void reportTaxDoctor(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String doctor_code = request.getParameter("DOCTOR_CODE");
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String path_show = getServletConfig().getServletContext().getRealPath("")+"\\reports\\";
        
        try{
            if( doctor_code.equals("") || doctor_code.equals(null) ){doctor_code = "%";}
        }catch(Exception e){}
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        
        hm.put("hospital_code", hospital_code);
        hm.put("doctor_code", doctor_code);
        hm.put("year", year);
        hm.put("path_show",path_show);

        logger.info("hospital_code = "+hospital_code);
        logger.info("doctor_code = "+doctor_code);
        logger.info("year = "+year);
        
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            logger.info(reportfilename);
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }
        
    }
    private void reportRevenue(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String month = request.getParameter("MM");
        String year = request.getParameter("YYYY");
        String group_code = request.getParameter("GROUP_CODE");
        String path_show=getServletConfig().getServletContext().getRealPath("")+"\\reports\\";
        
        logger.info("REPORT_FILE_NAME = "+request.getParameter("REPORT_FILE_NAME")+" Report Show Path ="+path_show);
        
        try{
            if( group_code.equals("") || group_code.equals(null) ){group_code = "%";}
        }catch(Exception e){}
        
        hm.put("hospital_code", hospital_code);
        hm.put("group_code", group_code);
        hm.put("year", year);
        hm.put("month", month);
        hm.put("path_show", path_show);
        
        this.reportGenerateView(hm, reportfilename, response);
        
    }
    private void reportDoctor(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");       
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String doctor = request.getParameter("DOCTOR_CODE");
        String from_doctor = request.getParameter("DOCTOR_CODE_FROM");
        String to_doctor = request.getParameter("DOCTOR_CODE_TO");
        //String doc_copy = request.getParameter("COPY");
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String doctor_category = request.getParameter("DOCTOR_CATEGORY_CODE");
        String doctor_department = request.getParameter("DOCTOR_DEPARTMENT_CODE");
        String order_item = request.getParameter("ORDER_ITEM_CODE");
        String term = request.getParameter("term");
        String payment_date = request.getParameter("PAYMENT_DATE");
        try{
            if( from_doctor.equals("") || from_doctor.equals(null) ){from_doctor = "0";}
        }catch(Exception e){}
        try{
            if( to_doctor.equals("") || to_doctor.equals(null) ){to_doctor = "Z";}
        }catch(Exception e){}
        try{
            if( doctor_department.equals("") || doctor_department.equals(null) ){doctor_department = "%";}
        }catch(Exception e){}
        try{
            if( order_item.equals("") || order_item.equals(null) ){order_item = "%";}
        }catch(Exception e){}
        try{
            if( doctor_category.equals("") || doctor_category.equals(null) ){doctor_category = "%";}
        }catch(Exception e){}
        try{
            if( doctor.equals("") || doctor.equals(null) ){doctor = "%";}
        }catch(Exception e){}
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        
        hm.put("hospital_code", hospital_code);
        hm.put("from_doctor", from_doctor);
        hm.put("to_doctor", to_doctor);
        hm.put("doctor_category", doctor_category);
        hm.put("doctor_department", doctor_department);
        hm.put("order_item", order_item);
        hm.put("payment_date", payment_date);
        hm.put("month", month);
        hm.put("year", year);
        hm.put("term", term);
        hm.put("batch_no", year+month);
        logger.info("Doctor = "+from_doctor+" - "+to_doctor+" Term : "+term+" Payment Date : "+payment_date);
        
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            logger.info(reportfilename);
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }
    }
    private void reportDoctorInfo(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");
        String doctor_profile = request.getParameter("DOCTOR_CODE");
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String doctor_category = request.getParameter("DOCTOR_CATEGORY_CODE");
        String doctor_department = request.getParameter("DOCTOR_DEPARTMENT_CODE");
        String order_item = request.getParameter("ORDER_ITEM_CODE");
        String from_date = request.getParameter("FROM_DATE");
        String to_date = request.getParameter("TO_DATE");
        
        if(reportfilename.equals("DoctorInfo")){
            from_date = request.getParameter("FROM_DATE");
            to_date = request.getParameter("TO_DATE");
        }else if(reportfilename.equals("DoctorGuaranteeExpire")){
            from_date = JDate.saveDate(request.getParameter("GUARANTEE_START_DATE"));
            to_date = JDate.saveDate(request.getParameter("GUARANTEE_EXPIRE_DATE"));
        }else{}
        
        try{
            if( from_date.equals("") || from_date.equals(null) ){from_date = "00000000";}
        }catch(Exception e){}
        try{
            if( to_date.equals("") || to_date.equals(null) ){to_date = "99999999";}
        }catch(Exception e){}
        try{
            if( doctor_department.equals("") || doctor_department.equals(null) ){doctor_department = "%";}
        }catch(Exception e){}
        try{
            if( hospital_code.equals("") || hospital_code.equals(null) ){hospital_code = "%";}
        }catch(Exception e){}
        try{
            if( doctor_category.equals("") || doctor_category.equals(null) ){doctor_category = "%";}
        }catch(Exception e){}
        try{
            if( doctor_profile.equals("") || doctor_profile.equals(null) ){doctor_profile = "%";}
        }catch(Exception e){}
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        
        hm.put("hospital_code", hospital_code);
        hm.put("doctor_profile_code", doctor_profile);
        hm.put("doctor_category", doctor_category);
        hm.put("doctor_department", doctor_department);
        hm.put("to_date", to_date);
        hm.put("from_date", from_date);
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
        }
    }

    private void reportMonthlyBehindPayment(HttpServletRequest request, HttpServletResponse response){
        ReportQuery rq = new ReportQuery();
    	//logger.info("Update 2010/03/31");
        HashMap hm = new HashMap();
        DBConn cdb = new DBConn();
        try {
            cdb.setStatement();
        } catch (Exception ex) {
            logger.error(ex);
        }

        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");
        String as_of_date = JDate.saveDate(request.getParameter("AS_OF_DATE"));
        String file_save = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String from_date = JDate.saveDate(request.getParameter("FROM_DATE"));
        String to_date = JDate.saveDate(request.getParameter("TO_DATE"));
        String doctor_code = request.getParameter("DOCTOR_CODE");
        String payor_code = request.getParameter("PAYOR_OFFICE_CODE");
        String department_code = request.getParameter("DOCTOR_DEPARTMENT_CODE");
        String year = request.getParameter("YYYY");
        String month = request.getParameter("MM");
        String batch_no = "";
        String doctor_category = request.getParameter("DOCTOR_CATEGORY_CODE");
        String from_doctor = request.getParameter("DOCTOR_CODE_FORM");
        String to_doctor = request.getParameter("DOCTOR_CODE_TO");
        String linux_path = getServletConfig().getServletContext().getRealPath("")+"/reports/output/"+file_save+".txt";
        String windows_path = getServletConfig().getServletContext().getRealPath("")+"\\reports\\output\\"+file_save+".txt";
        String linux_logo_path = request.getRealPath("/") + "reports/";
        String windows_logo_path = request.getRealPath("/") + "reports\\";
        String logo_path = Variables.IS_WINDOWS ? windows_logo_path : linux_logo_path;
        String path = Variables.IS_WINDOWS ? windows_path : linux_path;

        try{
            if(doctor_code.equals("") || doctor_code.equals(null) ){doctor_code = "%";}
        }catch(Exception e){}
        try{
            if( file_save.equals("") || file_save.equals(null) ){file_save = "temp";}
        }catch(Exception e){}
        try{
            if( from_date.equals("") || from_date.equals(null) ){from_date = "00000000";}
        }catch(Exception e){}
        try{
            if( to_date.equals("") || to_date.equals(null) ){to_date = "99999999";}
        }catch(Exception e){}
        try{
            if( as_of_date.equals("") || as_of_date.equals(null) ){
            	as_of_date = to_date;
            	batch_no = as_of_date.substring(0, 6);
            }else{
            	to_date = as_of_date;
            	reportfilename = "SummaryDFUnpaidByDetailAsOfDate";
            	batch_no = as_of_date.substring(0, 6);
            }
        }catch(Exception e){}
        logger.error("Report file : "+reportfilename);
        try{
            if( department_code.equals("") || department_code.equals(null) ){department_code = "%";}
        }catch(Exception e){}
        try{
            if( doctor_category.equals("") || doctor_category.equals(null) ){doctor_category = "%";}
        }catch(Exception e){}
        try{
            if( payor_code.equals("") || payor_code.equals(null) ){payor_code = "%";}
        }catch(Exception e){}
        
        hm.put("hospital_code", hospital_code);
        rq.setHospitalCode(hospital_code);
        hm.put("doctor", doctor_code);
        rq.setDoctorProfileCode(doctor_code);
        hm.put("from_date",from_date);
        hm.put("to_date",to_date);
        hm.put("as_of_date",as_of_date);
        hm.put("batch", batch_no);
        hm.put("department_code", department_code);
        rq.setDoctorDepartment(department_code);
        hm.put("payor_code", payor_code);
        rq.setPayorOffice(payor_code);
        
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            if(file_type.equals("txt")){
                this.reportGenerateFile(null, file_save, null, response, request, ""+ers.exportData(path, null, rq.getReport(reportfilename), null, null, cdb, null));
                cdb.closeDB("");
            }else{
                this.reportGenerateFile(hm, file_save, reportfilename, response, request, file_type);
            }
        }
    }  
    
    private void reportErrorLog(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();        
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String user_id = request.getParameter("USER_ID");
        String reportfilename = request.getParameter("REPORT_FILE_NAME");
        String from_date = JDate.saveDate(request.getParameter("FROM_DATE"));
        String to_date = JDate.saveDate(request.getParameter("TO_DATE"));
        String to_time = request.getParameter("TO_TIME");
        String from_time = request.getParameter("FROM_TIME");
        String save_file = request.getParameter("SAVE_FILE");
        String process_name = request.getParameter("PROCESS_NAME").replaceAll(" ", "");
        String file_type = request.getParameter("FILE_TYPE");
       
        try{
            if( from_date.equals("") || from_date.equals(null) ){from_date = "00000000";}
        }catch(Exception e){}
        try{
            if( to_date.equals("") || to_date.equals(null) ){to_date = "99999999";}
        }catch(Exception e){}
        try{
            if( from_time.equals("") || from_time.equals(null) ){from_time = "000000";}
        }catch(Exception e){}
        try{
            if( to_time.equals("") || to_time.equals(null) ){to_time = "235959";}
        }catch(Exception e){}
        try{
            if( save_file.equals("") || save_file.equals(null) ){save_file = "temp";}
        }catch(Exception e){}
        try{
            if( user_id.equals("") || user_id.equals(null) ){user_id = "%";}
        }catch(Exception e){user_id = "%";}

        hm.put("process_name", process_name);
        hm.put("hospital_code", hospital_code);
        hm.put("from_date", from_date);
        hm.put("to_date", to_date);
        hm.put("from_time", from_time);
        hm.put("to_time", to_time);
        hm.put("user_id", user_id);
       
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
        }
    }
    
    private void reportVerifyNewMaster(HttpServletRequest request, HttpServletResponse response){
        HashMap hm = new HashMap();
        HttpSession session = request.getSession(true);
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        String reportfilename = request.getParameter("REPORT_FILE_NAME");
        String save_file = request.getParameter("SAVE_FILE");
        String file_type = request.getParameter("FILE_TYPE");
        String date_from = request.getParameter("YYYY").toString()+request.getParameter("MM").toString()+"01";
        String date_to = request.getParameter("YYYY").toString()+request.getParameter("MM").toString()+"31";
        hm.put("hospital_code", hospital_code);
        hm.put("date_from", date_from);
        hm.put("date_to", date_to);
        
        if(request.getParameter("REPORT_DISPLAY").equals("view")){
            this.reportGenerateView(hm, reportfilename, response);
        }else{
            this.reportGenerateFile(hm, save_file, reportfilename, response,request, file_type);
        }
    }
    
    private void reportGenerateFile(HashMap hashM, String report_file_gen, String report_source_file,
        HttpServletResponse response, HttpServletRequest request, String report_type){
        GenerateReportBean grb = new GenerateReportBean();
        PrintWriter out = null;
        //String path = getServletConfig().getServletContext().getRealPath("")+"\\reports\\output\\"+report_file_gen+".txt";
        String upload_path=getServletConfig().getServletContext().getRealPath("")+"\\reports\\output\\";
        String URLpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+""+request.getContextPath()+"/reports/"+report_source_file+".jasper";   // /mywebapp
        DBConn cdb = new DBConn();
        try {
            cdb.setStatement();
        } catch (Exception ex) {
            logger.error(ex);
        }

        try {
            out = response.getWriter();
            logger.info("upload_path : "+upload_path);
            logger.info("URLpath : "+URLpath);

        } catch (IOException ex) {}
        
        if(report_type.equals("all")){
        	grb.setPath(upload_path);
            if(grb.exportReportPDF(report_file_gen, URLpath, hashM) &&
                grb.exportReportExcel(report_file_gen, URLpath, hashM, cdb)){
                response.setContentType("text/html");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>View Report Servlet</title>");
                out.println("</head>");
                out.println("<body bgcolor=\"#dde4e8\">");
                out.println("<h1 align=\"center\">Report Generate File</h1>");
                out.println("<p align=\"center\">" +
                            "<a href=\""+link+report_file_gen+".pdf\">" +
                            "<img src=\"./images/pdf_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p>");
                out.println("<p align=\"center\">" +
                            "<a href=\""+link+report_file_gen+".xls\">" +
                            "<img src=\"./images/xls_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p><br>");
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
                out.println("<h1>Report Result</h1>");
                out.println("<p align=\"center\">Can't write file report</p>");
                out.println("<p align=\"center\">"+link+report_file_gen+"</p>");
                out.println("</body>");
                out.println("</html>");
            }
        }else if(report_type.equals("xls")){
        	grb.setPath(upload_path);
            //if(grb.exportReportExcel(report_file_gen, report_source_file+".jasper", hashM, cdb)){
            if(grb.exportReportExcel(report_file_gen, URLpath, hashM, cdb)){
                response.setContentType("text/html");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Generate Report Servlet</title>");  
                out.println("</head>");
                out.println("<body bgcolor=\"#dde4e8\">");
                out.println("<h1 align=\"center\">Report Generate File</h1>");
                out.println("<p align=\"center\">" +
                            "<a href=\""+link+report_file_gen+".xls\">" +
                            //"<a href=\""+Variables.file_download+report_file_gen+".xls\">" +
                            "<img src=\"./images/xls_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p><br>");
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
                out.println("<h1>Report Result</h1>");
                out.println("<p align=\"center\">Can't write file report</p>");
                out.println("<p align=\"center\">"+link+report_file_gen+":"+grb.getErrMesg()+"</p>");
                out.println("</body>");
                out.println("</html>");
            }
        }else if(report_type.equals("pdf")){
        	grb.setPath(upload_path);
//            if(grb.exportReportPDF(report_file_gen, report_source_file+".jasper", hashM, null)){
            if(grb.exportReportPDF(report_file_gen, URLpath, hashM)){
                response.setContentType("text/html");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>View Report Servlet</title>");  
                out.println("</head>");
                out.println("<body bgcolor=\"#dde4e8\">");
                out.println("<h1 align=\"center\">Report Generate File</h1>");
                out.println("<p align=\"center\">" +
                            "<a href=\""+link+report_file_gen+".pdf\">" +
                            "<img src=\"./images/pdf_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p>");
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
                out.println("<h1>Report Result</h1>");
                out.println("<p align=\"center\">Can't write file report</p>");
                out.println("<p align=\"center\">"+grb.getErrMesg()+"</p>");
                out.println("</body>");
                out.println("</html>");
            }
        }else{
            if(report_type.equals("true")){
                response.setContentType("text/html");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>View Report Servlet</title>");  
                out.println("</head>");
                out.println("<body bgcolor=\"#dde4e8\">");
                out.println("<h1 align=\"center\">Report Generate File</h1>");
                out.println("<p align=\"center\">" +
                            "<a href=\""+link+report_file_gen+".txt\">" +
                            "<img src=\"./images/txt_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p>");
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
                out.println("<h1>Report Result</h1>");
                out.println("<p align=\"center\">Can't write text file report</p>");
                out.println("<p align=\"center\">"+link+report_file_gen+"</p>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }
   
    private void reportGenerateView(HashMap hashM, String report_source_file, HttpServletResponse response){
    	byte[] b = null;
        DBConn cdb = new DBConn();
       
        try {
            cdb.setStatement();
        } catch (Exception ex) {
            logger.error(ex);
        }

        //ServletContext context = this.getServletConfig().getServletContext();
        //logger.info(this.getServletConfig().getServletContext().getRealPath("/reports/"+report_source_file+".jasper"));
        
        File reportFile = new File(this.getServletConfig().getServletContext().getRealPath("/reports/"+report_source_file+".jasper"));
        hashM.put("SUBREPORT_DIR", this.getServletConfig().getServletContext().getRealPath("/reports/"));
        try {
            response.setContentType("application/pdf");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getPath(), hashM, cdb.getConnection());
            //JasperPrintManager.printPage(jasperPrint, 1, false);
            b = JasperRunManager.runReportToPdf(reportFile.getPath(), hashM, cdb.getConnection());
            if (b != null && b.length > 0){
                response.setContentLength(b.length);
                ServletOutputStream ouputStream = response.getOutputStream();
                ouputStream.write(b, 0, b.length);
                ouputStream.flush();
                ouputStream.close();
            }else{
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                out.println("<body>");
                out.println("Report is empty");
                out.println("</body>");
            }
        }catch(Exception e){
            PrintWriter out = null;
            try {
                out = response.getWriter();
                response.setContentType("text/html");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>View Report Servlet</title>");  
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Report Error</h1>");
                out.println("Report Path :" + reportFile.getPath()+"<br>");
                out.println("Cause : "+e);
                out.println("</body>");
                out.println("</html>");
            } catch (IOException ex) {
            } 
        }finally{ 
        	cdb.closeDB("");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
}