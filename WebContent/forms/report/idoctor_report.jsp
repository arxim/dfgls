<%@page contentType="text/html" pageEncoding="UTF-8"
	import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.DBMgr"%>
<%@ include file="../../_global.jsp"%>

<%
	if (session.getAttribute("LANG_CODE") == null) {
		session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
	}
	ProcessUtil proUtil = new ProcessUtil();
	DBConnection c = new DBConnection();
	c.connectToLocal();
	Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
	c.Close();
	LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "iDoctor Report", "รายงาน");
	labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
	labelMap.add("INVOICE_NO", "Invoice No.", "เลขที่ใบแจ้งหนี้");
	labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
	labelMap.add("DOCTOR_PROFILE_CODE", "Doctor Profile Code", "รหัสแพทย์");
	labelMap.add("DOCTOR_CATEGORY_CODE", "Doctor Category", "รหัสกลุ่มแพทย์");
	labelMap.add("DOCTOR_DEPARTMENT_CODE", "Doctor Department", "รหัสแผนกแพทย์");
	labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รหัสการรักษา");
	labelMap.add("ORDER_CATEGORY_CODE", "Order Item Category", "รหัสกลุ่มการรักษา");
	labelMap.add("FROM_DATE", "From Date", "ตั้งแต่วันที่");
	labelMap.add("TO_DATE", "To Date", "ถึงวันที่");
	labelMap.add("TRANSACTION_TYPE", "Transaction Type", "ประเภทใบแจ้งหนี้");
	labelMap.add("TRANSACTION_MODULE", "Module", "ประเภทรายการ");
	labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "แผนกผู้ป่วย");
	labelMap.add("PAYOR_OFFICE_CODE", "Payor Office Code", "รหัสบริษัทคู่สัญญา");
	labelMap.add("REPORT_TRANSACTION", "Interface DF Transaction", "นำเข้ารายการค่าแพทย์ชั่วคราว");
	labelMap.add("REPORT_IMPORT_CHECKLIST", "Import DF Transaction", "นำเข้ารายการค่าแพทย์เข้าระบบ");
	labelMap.add("REPORT_TRANSACTION_RESULT", "Interface Result Transaction", "นำเข้ารายการแพทย์อ่านผลชั่วคราว");
	labelMap.add("REPORT_GUARANTEE_TRANSACTION", "Guarantee Setup", "รายการกำหนดการันตีแพทย์");
	labelMap.add("REPORT_DETAIL_DF", "Revenue Detail Payment", "รายงานรายละเอียดรายได้แพทย์ทำจ่าย");
	labelMap.add("REPORT_DETAIL_IN_MONTH", "Revenue Detail In Month", "รายงานรายละเอียดรายได้แพทย์ในเดือน");
	labelMap.add("REPORT_PAYMENT_VOUCHER", "Payment Voucher", "เอกสารการจ่ายเงินแพทย์");
	labelMap.add("REPORT_EXPENSE", "Adjust Revenue", "รายงานรายได้ค่าใช้จ่ายแพทย์");
	labelMap.add("REPORT_SUMMARY_REVENUE", "Summary Revenue Payment", "รายงานสรุปรายได้แพทย์ทำจ่าย");
	labelMap.add("REPORT_BEHIND_PAYMENT_SUMMARY","Behind in Payment Summary","รายงานสรุปค่าแพทย์ค้างจ่าย");
	labelMap.add("REPORT_BEHIND_PAYMENT_DETAIL","Behind in Payment Detail","รายงานรายการค่าแพทย์ค้างจ่าย");

	labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
	labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร");
	labelMap.add("VIEW", "View", "แสดงผล");
	labelMap.add("MESSAGE", "Please Select Report Module", "กรุณาเลือกรายงาน");
	
	labelMap.add("DOCTOR_CODE_FROM", "From Doctor Profile Code", "จากแพทย์รหัส");
    labelMap.add("DOCTOR_CODE_TO", "To Doctor Profile Code", "ถึงแพทย์รหัส");
	labelMap.add("MM", "Month", "เดือน");
	labelMap.add("YYYY", "Year", "ปี");
	labelMap.add("GL_TYPE", "GL Type", "ประเภทการลงบัญชี");
	labelMap.add("DEPARTMENT" , "Department" , "Department");
	
    labelMap.add("PAYMENT_TERM", "Payment Term", "งวดทำจ่าย");
    labelMap.add("PAYMENT_DATE", "Payment Date", "วันที่ทำจ่าย");
	labelMap.add("JAN","January","มกราคม");
	labelMap.add("FEB","Febuary","กุมภาพันธ์");
	labelMap.add("MAR","March","มีนาคม");
	labelMap.add("APR","April","เมษายน");
	labelMap.add("MAY","May","พฤษภาคม");
	labelMap.add("JUN","June","มิถุนายน");
	labelMap.add("JUL","July","กรกฏาคม");
	labelMap.add("AUG","August","สิงหาคม");
	labelMap.add("SEP","September","กันยายน");
	labelMap.add("OCT","October","ตุลาคม");
	labelMap.add("NOV","November","พฤศจิกายน");
	labelMap.add("DEC","December","ธันวาคม");
	labelMap.add("EXPENSE_SIGN","Expense sign","Expense sign");
	labelMap.add("EXPENSE_ACCOUNT_CODE","Expense account code","Expense account code");
	labelMap.add("EXPENSE_CODE","Expense code","Expense code");
	labelMap.add("EXPENSE_SING_LABEL_ALL","ALL","ทั้งหมด");
	labelMap.add("EXPENSE_SING_LABEL_REVENUE","Revenue","รายได้");
	labelMap.add("EXPENSE_SING_LABEL_EXPENSE","Expense","ค่าใช้จ่าย");
	
	labelMap.add("PAYMENT_MODE_CODE","Payment Mode Code","ประเภทของการจ่าย");
	labelMap.add("DOCTOR_TYPE_CODE", "Doctor Type", "ประเภทแพทย์");
    labelMap.add("CLEAR_DATA","Clear data","ล้างข้อมูล");
    labelMap.add("PAYMENT_TERM" , "Payment term" , "Payment term");
	
	String report = "";
	request.setAttribute("labelMap", labelMap.getHashMap());
	String startDateStr = JDate.showDate(JDate.getDate()); 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${labelMap.TITLE_MAIN}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../../css/share.css"
	media="all" />
<link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
<script type="text/javascript" src="../../javascript/ajax.js"></script>
<script type="text/javascript" src="../../javascript/calendar.js"></script>
<script type="text/javascript" src="../../javascript/search_form.js"></script>
<script type="text/javascript" src="../../javascript/util.js"></script>
<script type="text/javascript">
            function Report_View() {
                if(document.mainForm.REPORT_FILE_NAME.value == "None"){
                    alert("Please Select Report");
                    document.mainForm.REPORT_FILE_NAME.focus();
                }else{
					if((document.mainForm.FROM_DATE.value == "" || document.mainForm.TO_DATE.value == "")){
						if (document.mainForm.REPORT_FILE_NAME.value == "InterfaceTransaction" 
							//|| document.mainForm.REPORT_FILE_NAME.value == "ImportChecklist" 
							|| document.mainForm.REPORT_FILE_NAME.value == "ImportVerifyTransaction") {
							alert("Please Select From Date/To Date");							
						} else {
							document.mainForm.REPORT_DISPLAY.value = "view";
	                    	document.mainForm.target = "_blank";
	                    	document.mainForm.submit();
						}
					}else{
						document.mainForm.REPORT_DISPLAY.value = "view";
                    	document.mainForm.target = "_blank";
                    	document.mainForm.submit();		
					}
                }
            }
            function Report_Save() {
                if(document.mainForm.REPORT_FILE_NAME.value == "None" || document.mainForm.SAVE_FILE.value == ""){
                    if(document.mainForm.REPORT_FILE_NAME.value == "None"){
						alert("Please Select Report");
                        document.mainForm.REPORT_FILE_NAME.focus();
                    }else{
						alert("Please Enter File Name");
                        document.mainForm.SAVE_FILE.focus();
                    }					
                }else{
                	if(document.mainForm.MM.value == "" || document.mainForm.YYYY.value == ""){
						alert("Please Select From Date/To Date");
					}else{
						if(document.mainForm.REPORT_FILE_NAME.value == "GuaranteeSetup" 
							|| document.mainForm.REPORT_FILE_NAME.value == "SummaryRevenueByDetail" 
							|| document.mainForm.REPORT_FILE_NAME.value == "SummaryDFUnpaidByDetail<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
			            	|| document.mainForm.REPORT_FILE_NAME.value == "DFUnpaidSum<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
							|| document.mainForm.REPORT_FILE_NAME.value == "PaymentVoucher<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
							|| document.mainForm.REPORT_FILE_NAME.value == "SummaryRevenuePayment"
							|| document.mainForm.REPORT_FILE_NAME.value == "SummaryRevenueByDetailInMonthVCH"
							|| document.mainForm.REPORT_FILE_NAME.value == "ExpenseDetail"){
							document.mainForm.REPORT_DISPLAY.value = "save";
	                    	document.mainForm.target = "_blank";
	                    	document.mainForm.submit();
						}else{
							document.mainForm.REPORT_DISPLAY.value = "save";
	                    	document.mainForm.target = "_blank";
	                    	document.mainForm.submit();				
						}
					}
                }
            }
            
			function changeDropDownType(){
				if(document.mainForm.REPORT_FILE_NAME.value == "None" || document.mainForm.REPORT_FILE_NAME.value == "SummaryDailyOrderCate" || document.mainForm.REPORT_FILE_NAME.value == "DailyChecklist" || document.mainForm.REPORT_FILE_NAME.value == "NoVerifyTransaction" || document.mainForm.REPORT_FILE_NAME.value == "InterfaceTransaction"){
                }else{
                	if(document.mainForm.FILE_TYPE.value == "txt"){
						alert("Report doesn't Support Text File");
                    }
                }
			}
			
            function changeDropDownList(){
                
                if(document.mainForm.REPORT_FILE_NAME.value == "InterfaceTransaction" 
                    || document.mainForm.REPORT_FILE_NAME.value == "ImportVerifyTransaction"
                    || document.mainForm.REPORT_FILE_NAME.value == "ImportChecklist"){
                    document.getElementById('block_from_to_date').style.display = '';
                	document.getElementById('block_payor_office_code').style.display = 'none';
                	document.getElementById('block_doctor_code').style.display = '';
                	document.getElementById('block_doctor_profile_code').style.display = 'none';
                	document.getElementById('block_doctor_department').style.display = 'none';
                	document.getElementById('block_order_category_code').style.display = 'none';
                	document.getElementById('block_doctor_category_code').style.display = 'none';
                	document.getElementById('block_order_item_code').style.display = 'none';
                	document.getElementById('block_invoice_no').style.display = 'none';
                 	if(document.mainForm.REPORT_FILE_NAME.value == "ImportChecklist"){
                    	document.getElementById('block_module').style.display = 'none';
                 	}else{
                    	document.getElementById('block_module').style.display = 'none';
                 	}
                	document.getElementById('block_adminssion_type').style.display = '';
                	document.getElementById('block_document_type').style.display = 'none';
                	document.getElementById('block_month_year').style.display = 'none';
                	document.getElementById('block_doctor_code_from').style.display = 'none';
                	document.getElementById('block_doctor_code_to').style.display = 'none';
                	document.getElementById('block_guarantee_department').style.display = 'none';
                	document.getElementById('block_gl_type').style.display = 'none';
                	document.getElementById('block_expense_sign').style.display = 'none';
                	document.getElementById('block_expense_sign').style.display = 'none';
                	document.getElementById('block_expense_account_code').style.display = 'none';
                	document.getElementById('block_expense_code').style.display = 'none';
                	document.getElementById('block_payment_term').style.display = 'none';
                	document.getElementById('block_doctor_type').style.display = 'none';
                	document.getElementById('block_payment_mode').style.display = 'none';
                 	document.getElementById('block_save_file').style.display = 'none';
                } else if (document.mainForm.REPORT_FILE_NAME.value == "GuaranteeSetup" || document.mainForm.REPORT_FILE_NAME.value == "SummaryRevenueByDetail" 
            		|| document.mainForm.REPORT_FILE_NAME.value == "PaymentVoucher<%=session.getAttribute("HOSPITAL_CODE").toString()%>" 
            		|| document.mainForm.REPORT_FILE_NAME.value == "SummaryDFUnpaidByDetail<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
            		|| document.mainForm.REPORT_FILE_NAME.value == "DFUnpaidSum<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
            		|| document.mainForm.REPORT_FILE_NAME.value == "SummaryRevenuePayment" || document.mainForm.REPORT_FILE_NAME.value == "SummaryRevenueByDetailInMonthVCH"){
                	document.getElementById('block_from_to_date').style.display = 'none';
                 	document.getElementById('block_payor_office_code').style.display = 'none';
                 	document.getElementById('block_doctor_code').style.display = '';
                 	document.getElementById('block_doctor_profile_code').style.display = 'none';
                 	document.getElementById('block_doctor_department').style.display = 'none';
                 	document.getElementById('block_order_category_code').style.display = 'none';
                 	document.getElementById('block_doctor_category_code').style.display = 'none';
                 	document.getElementById('block_order_item_code').style.display = 'none';
                 	document.getElementById('block_invoice_no').style.display = 'none';
                 	document.getElementById('block_module').style.display = 'none';
                 	document.getElementById('block_adminssion_type').style.display = 'none';
                 	document.getElementById('block_document_type').style.display = 'none';
                 	document.getElementById('block_month_year').style.display = '';
                 	document.getElementById('block_doctor_code_from').style.display = 'none';
                 	document.getElementById('block_doctor_code_to').style.display = 'none';
                 	document.getElementById('block_guarantee_department').style.display = 'none';
                 	document.getElementById('block_gl_type').style.display = 'none';
                 	document.getElementById('block_expense_sign').style.display = 'none';
                 	document.getElementById('block_expense_sign').style.display = 'none';
                 	document.getElementById('block_expense_account_code').style.display = 'none';
                 	document.getElementById('block_expense_code').style.display = 'none';
                 	document.getElementById('block_payment_term').style.display = 'none';
                 	document.getElementById('block_doctor_type').style.display = '';
                 	if(document.mainForm.REPORT_FILE_NAME.value == "SummaryRevenuePayment"){
                 		document.getElementById('block_doctor_type').style.display = '';
                 	}else{
                 		document.getElementById('block_doctor_type').style.display = 'none';
                 	}
                 	document.getElementById('block_payment_mode').style.display = 'none';
                 	document.getElementById('block_save_file').style.display = 'none';
                } else if (document.mainForm.REPORT_FILE_NAME.value == "ExpenseDetail"){
                	document.getElementById('block_from_to_date').style.display = 'none';
                 	document.getElementById('block_payor_office_code').style.display = 'none';
                 	document.getElementById('block_doctor_code').style.display = '';
                 	document.getElementById('block_doctor_type').style.display = '';
                 	document.getElementById('block_doctor_profile_code').style.display = 'none';
                 	document.getElementById('block_doctor_department').style.display = 'none';
                 	document.getElementById('block_order_category_code').style.display = 'none';
                 	document.getElementById('block_doctor_category_code').style.display = 'none';
                 	document.getElementById('block_order_item_code').style.display = 'none';
                 	document.getElementById('block_invoice_no').style.display = 'none';
                 	document.getElementById('block_module').style.display = 'none';
                 	document.getElementById('block_adminssion_type').style.display = 'none';
                 	document.getElementById('block_document_type').style.display = 'none';
                 	document.getElementById('block_month_year').style.display = '';
                 	document.getElementById('block_doctor_code_from').style.display = 'none';
                 	document.getElementById('block_doctor_code_to').style.display = 'none';
                 	document.getElementById('block_guarantee_department').style.display = 'none';
                 	document.getElementById('block_gl_type').style.display = 'none';
                 	document.getElementById('block_expense_sign').style.display = 'none';
                 	document.getElementById('block_expense_sign').style.display = '';
                 	document.getElementById('block_expense_account_code').style.display = '';
                 	document.getElementById('block_expense_code').style.display = '';
                 	document.getElementById('block_payment_term').style.display = 'none';
                 	document.getElementById('block_doctor_type').style.display = 'none';
                 	document.getElementById('block_payment_mode').style.display = 'none';
                 	document.getElementById('block_save_file').style.display = 'none';
                } else {
                	document.getElementById('block_from_to_date').style.display = 'none';
                	document.getElementById('block_payor_office_code').style.display = 'none';
                	document.getElementById('block_doctor_code').style.display = 'none';
                	document.getElementById('block_doctor_profile_code').style.display = 'none';
                	document.getElementById('block_doctor_department').style.display = 'none';
                	document.getElementById('block_order_category_code').style.display = 'none';
                	document.getElementById('block_doctor_category_code').style.display = 'none';
                	document.getElementById('block_order_item_code').style.display = 'none';
                	document.getElementById('block_invoice_no').style.display = 'none';
                	document.getElementById('block_module').style.display = 'none';
                	document.getElementById('block_adminssion_type').style.display = 'none';
                	document.getElementById('block_document_type').style.display = 'none';
                	document.getElementById('block_month_year').style.display = 'none';
                	document.getElementById('block_doctor_code_from').style.display = 'none';
                	document.getElementById('block_doctor_code_to').style.display = 'none';
                	document.getElementById('block_guarantee_department').style.display = 'none';
                	document.getElementById('block_gl_type').style.display = 'none';
                	document.getElementById('block_expense_sign').style.display = 'none';
                	document.getElementById('block_expense_sign').style.display = 'none';
                	document.getElementById('block_expense_account_code').style.display = 'none';
                	document.getElementById('block_expense_code').style.display = 'none';
                	document.getElementById('block_payment_term').style.display = 'none';
                	document.getElementById('block_doctor_type').style.display = 'none';
                	document.getElementById('block_payment_mode').style.display = 'none';
                	document.getElementById('block_save_file').style.display = 'none';
                	document.getElementById('block_doctor_type').style.display = 'none';
                }
            }
                        
/********************************************************************************************************************
 * Retreive doctor information : Begin
 */            
            function DOCTOR_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR() {
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%=labelMap.getFieldLangSuffix()%>");
                }
            }
                        
/********************************************************************************************************************
 * Retreive doctor information : End
 */            

 /********************************************************************************************************************
  * Retreive doctor profile information : begin
  */ 
             function DOCTOR_PROFILE_CODE_KeyPress(e) {
                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                 if (key == 13) {
                     document.mainForm.DOCTORE_PROFILE_CODE.blur();
                     return false;
                 }
                 else {
                     return true;
                 }
             }
             
             function AJAX_Refresh_DOCTOR_PROFILE_CODE() {
                 var target = "../../RetrieveData?TABLE=DOCTOR_PROFILE&COND=CODE='" + document.mainForm.DOCTOR_PROFILE_CODE.value + "'";
                 AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_PROFILE_CODE);
             }
             
             function AJAX_Handle_Refresh_DOCTOR_PROFILE_CODE() {
                 if (AJAX_IsComplete()) {
                                         
                     var xmlDoc = AJAX.responseXML;

                     // Data not found
                     if (!isXMLNodeExist(xmlDoc, "CODE")) {
                         //document.mainForm.DOCTOR_DEPARTMENT_CODE.value = "";
                         document.mainForm.DOCTOR_PROFILE_NAME.value = "";
                         return;
                     }
                     
                     // Data found
                     document.mainForm.DOCTOR_PROFILE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_THAI");
                 }
             }
 /********************************************************************************************************************
  * Retreive doctor profile information : End
  */   
/********************************************************************************************************************
 * Retreive doctor department information : Begin
 */            
            function DOCTOR_DEPARTMENT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_DOCTOR_DEPARTMENT() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.DOCTOR_DEPARTMENT_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_DEPARTMENT);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_DEPARTMENT() {
                if (AJAX_IsComplete()) {
                                        
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_DEPARTMENT_CODE.value = "";
                        document.mainForm.DOCTOR_DEPARTMENT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
/********************************************************************************************************************
 * Retreive doctor department information : End
 */      

/********************************************************************************************************************
 * Retreive doctor department information : Begin
 */            
            function DOCTOR_DEPARTMENT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_DOCTOR_DEPARTMENT() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.DOCTOR_DEPARTMENT_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_DEPARTMENT);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_DEPARTMENT() {
                if (AJAX_IsComplete()) {
                                        
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_DEPARTMENT_CODE.value = "";
                        document.mainForm.DOCTOR_DEPARTMENT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
/********************************************************************************************************************
 * Retreive DOCTOR PROFILE information : End
 */      

 /********************************************************************************************************************
 * Retreive doctor category information : Begin
 */            
            function DOCTOR_CATEGORY_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CATEGORY_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_DOCTOR_CATEGORY() {
                var target = "../../RetrieveData?TABLE=DOCTOR_CATEGORY&COND=CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CATEGORY() {
                if (AJAX_IsComplete()) {
                                        
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.ORDER_ITEM_CODE.value = "";
                        document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
/********************************************************************************************************************
 * Retreive doctor category information : End
 
 /********************************************************************************************************************
 * Retreive order item category information : Begin
 */            
            function ORDER_CATEGORY_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ORDER_CATEGORY_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_ORDER_CATEGORY_CODE() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" + document.mainForm.ORDER_CATEGORY_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_ORDER_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_ORDER_CATEGORY() {
                if (AJAX_IsComplete()) {
                                        
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.ORDER_ITEM_CODE.value = "";
                        document.mainForm.ORDER_CATEGORY_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.ORDER_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_THAI");
                }
            }
/********************************************************************************************************************
 * Retreive order item category information : End
 */        

 /********************************************************************************************************************
 * Retreive order item information : Begin
 */
            function ORDER_ITEM_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ORDER_ITEM_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_ORDER_ITEM() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" + document.mainForm.ORDER_ITEM_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM);
            }

            function AJAX_Handle_Refresh_ORDER_ITEM() {
                if (AJAX_IsComplete()) {

                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.ORDER_ITEM_CODE.value = "";
                        document.mainForm.ORDER_ITEM_DESCRIPTION.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.ORDER_ITEM_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_THAI");
                }
            }
/********************************************************************************************************************
 * Retreive order item information : End
 */

 /********************************************************************************************************************
  * Retreive payor office information : Begin
  */            
             function PAYOR_OFFICE_CODE_KeyPress(e) {
                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                 if (key == 13) {
                     document.mainForm.PAYOR_OFFICE_CODE.blur();
                     return false;
                 }
                 else {
                     return true;
                 }
             }
             
             function AJAX_Refresh_PAYOR_OFFICE() {
                 var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.PAYOR_OFFICE_CODE.value + "'";
                 AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE);
             }
             
             function AJAX_Handle_Refresh_PAYOR_OFFICE() {
                 if (AJAX_IsComplete()) {
                                         
                     var xmlDoc = AJAX.responseXML;

                     // Data not found
                     if (!isXMLNodeExist(xmlDoc, "CODE")) {
                         //document.mainForm.DOCTOR_DEPARTMENT_CODE.value = "";
                         document.mainForm.PAYOR_OFFICE_DESCRIPTION.value = "not found";
                         return;
                     }
                     
                     // Data found
                     document.mainForm.PAYOR_OFFICE_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "NAME_THAI");
                 }
             }
 /********************************************************************************************************************
  * Retreive payor office information : End
  */ 

 /********************************************************************************************************************
 * Retreive Invoice no information : Begin
 */        
 function INVOICE_NO_KeyPress(e) {
     var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
     if (key == 13) {
         AJAX_Refresh_INVOICE();
         return false;
     }else {
         return true;
     }
 }

 function AJAX_Refresh_INVOICE() {
     var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=INVOICE_NO='" + document.mainForm.INVOICE_NO.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>' ";
		AJAX_Request(target, AJAX_Handle_Refresh_INVOICE);
	}

	function AJAX_Handle_Refresh_INVOICE() {
		if (AJAX_IsComplete()) {
			var xmlDoc = AJAX.responseXML;

			// Data not found
			if (!isXMLNodeExist(xmlDoc, "INVOICE_NO")) {
				document.mainForm.INVOICE_NO.value = "";
				document.mainForm.INVOICE_DESCRIPTION.value = "";
				return;
			}

			// Data found
			//document.mainForm.INVOICE_DESCRIPTION.value = toShowDate(getXMLNodeValue(xmlDoc, "INVOICE_DATE"));
		}
	}
</script>
</head>
<body leftmargin="0"  onload='changeDropDownList()'>
	<form id="mainForm" name="mainForm" method="post" action="../../ViewDFReportSrvl">
		<center>
			<table width="800" border="0">
				<tr>
					<td align="left"><b><font color='#003399'><%=Utils.getInfoPage("idoctor_report.jsp", labelMap.getFieldLangSuffix(),
					new DBConnection("" + session.getAttribute("HOSPITAL_CODE")))%></font></b>
					</td>
				</tr>
			</table>
		</center>
		<table class="form">
			<input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY" />
			<input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="iDoctor" />
			<tr>
				<th colspan="4">
					<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				</th>
			</tr>
			<tr>
				<td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label>
				</td>
				<td colspan="3" class="input">
				<select class="mediumMax" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" onchange="changeDropDownList()">
						<option value="None"> Select Report </option>
						<option value="None">--------- Daily Checklist ---------</option>
						<option value="InterfaceTransaction">${labelMap.REPORT_TRANSACTION}</option>
						<option value="ImportVerifyTransaction">${labelMap.REPORT_TRANSACTION_RESULT}</option>
						<option value="ImportChecklist">${labelMap.REPORT_IMPORT_CHECKLIST}</option>
						<option value="None">--------- Monthly Checklist ---------</option>
						<option value="GuaranteeSetup">${labelMap.REPORT_GUARANTEE_TRANSACTION}</option>
						<option value="SummaryRevenueByDetailInMonthVCH">${labelMap.REPORT_DETAIL_IN_MONTH}</option>
						<option value="None">--------- Monthly Payment ---------</option>
						<option value="SummaryRevenuePayment">${labelMap.REPORT_SUMMARY_REVENUE}</option>
						<option value="PaymentVoucher<%=session.getAttribute("HOSPITAL_CODE").toString()%>">${labelMap.REPORT_PAYMENT_VOUCHER}</option>
						<option value="SummaryRevenueByDetail">${labelMap.REPORT_DETAIL_DF}</option>
						<option value="ExpenseDetail">${labelMap.REPORT_EXPENSE}</option>
						<option value="None">--------- DF Unpaid ---------</option>
						<option value="SummaryDFUnpaidByDetail<%=session.getAttribute("HOSPITAL_CODE").toString()%>">${labelMap.REPORT_BEHIND_PAYMENT_DETAIL}</option>
                     	<option value="DFUnpaidSum<%=session.getAttribute("HOSPITAL_CODE").toString()%>">${labelMap.REPORT_BEHIND_PAYMENT_SUMMARY}</option>	
				</select></td>
			</tr>
			<tr id="block_from_to_date">
				<td class="label"><label for="FROM_DATE">${labelMap.FROM_DATE}</label>
				</td>
				<td class="input"><input type="text" id="FROM_DATE"
					name="FROM_DATE" class="short" /> <input type="image"
					class="image_button" src="../../images/calendar_button.png" alt=""
					onclick="displayDatePicker('FROM_DATE'); return false;" /></td>
				<td class="label"><label for="TO_DATE">${labelMap.TO_DATE}</label>
				</td>
				<td class="input"><input type="text" id="TO_DATE"
					name="TO_DATE" class="short" /> <input type="image"
					class="image_button" src="../../images/calendar_button.png" alt=""
					onclick="displayDatePicker('TO_DATE'); return false;" /></td>
			</tr>
			<tr id="block_payor_office_code">
				<td class="label"><label for="PAYOR_OFFICE_CODE">${labelMap.PAYOR_OFFICE_CODE}</label></td>
				<td class="input" colspan="3"><input name="PAYOR_OFFICE_CODE"
					type="text" class="short" id="PAYOR_OFFICE_CODE" maxlength="20"
					value="" onkeypress="return PAYOR_OFFICE_CODE_KeyPress(event);"
					onblur="AJAX_Refresh_PAYOR_OFFICE();" /> <input
					id="SEARCH_PAYOR_OFFICE_CODE" name="SEARCH_PAYOR_OFFICE_CODE"
					type="image" class="image_button"
					src="../../images/search_button.png" alt=""
					onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_THAI&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PAYOR_OFFICE_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE'); return false;" />
					<input name="PAYOR_OFFICE_DESCRIPTION" type="text"
					class="mediumMax" id="PAYOR_OFFICE_DESCRIPTION" readonly="readonly"
					value="" /></td>
			</tr>
			<tr id="block_doctor_profile_code">
				<td class="label"><label for="DOCTOR_PROFILE_CODE">${labelMap.DOCTOR_PROFILE_CODE}</label></td>
				<td colspan="3" class="input"><input type="text"
					id="DOCTOR_PROFILE_CODE" name="DOCTOR_PROFILE_CODE" class="short"
					value="" onkeypress="return DOCTOR_PROFILE_CODE_KeyPress(event);"
					onblur="AJAX_Refresh_DOCTOR_PROFILE_CODE();" /> <input
					id="SEARCH_DOCTOR_PROFILE_CODE" name="SEARCH_DOCTOR_PROFILE_CODE"
					type="image" class="image_button"
					src="../../images/search_button.png" alt="Search"
					onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&TARGET=DOCTOR_PROFILE_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_PROFILE_CODE'); return false;" />
					<input type="text" id="DOCTOR_PROFILE_NAME"
					name="DOCTOR_PROFILE_NAME" class="mediumMax" readonly="readonly"
					value="" /></td>
			</tr>
			
			<tr id="block_doctor_code">
				<td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
				<td colspan="3" class="input"><input type="text"
					id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value=""
					onkeypress="return DOCTOR_CODE_KeyPress(event);"
					onblur="AJAX_Refresh_DOCTOR();" /> <input id="SEARCH_DOCTOR_CODE"
					name="SEARCH_DOCTOR_CODE" type="image" class="image_button"
					src="../../images/search_button.png" alt="Search"
					onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
					<input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME"
					class="mediumMax" readonly="readonly" value="" /></td>
			</tr>
			
			<tr id="block_doctor_department">
				<td class="label"><label for="DOCTOR_DEPARTMENT_CODE">${labelMap.DOCTOR_DEPARTMENT_CODE}</label></td>
				<td class="input" colspan="3"><input
					name="DOCTOR_DEPARTMENT_CODE" type="text" class="short"
					id="DOCTOR_DEPARTMENT_CODE" maxlength="20" value=""
					onkeypress="return DOCTOR_DEPARTMENT_CODE_KeyPress(event);"
					onblur="AJAX_Refresh_DOCTOR_DEPARTMENT();" /> <input
					id="SEARCH_DOCTOR_DEPARTMENT_CODE"
					name="SEARCH_DOCTOR_DEPARTMENT_CODE" type="image"
					class="image_button" src="../../images/search_button.png" alt=""
					onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DOCTOR_DEPARTMENT_CODE&HANDLE=AJAX_Refresh_DOCTOR_DEPARTMENT'); return false;" />
					<input name="DOCTOR_DEPARTMENT_DESCRIPTION" type="text"
					class="mediumMax" id="DOCTOR_DEPARTMENT_DESCRIPTION"
					readonly="readonly" value="" /></td>
			</tr>
			
			<tr id="block_doctor_type">
               <td class="label">
                    <label for="DOCTOR_TYPE_CODE"><span class="style1">Doctor type</span></label>                    </td>
                    <td class="input" colspan="3">
                        <%=DBMgr.generateDropDownList("DOCTOR_TYPE_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM DOCTOR_TYPE WHERE HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' ORDER BY DESCRIPTION", "DESCRIPTION", "CODE","")%>                   
               </td>
			</tr>
			
			<tr id="block_order_category_code">
				<td class="label"><label for="ORDER_CATEGORY_CODE">${labelMap.ORDER_CATEGORY_CODE}</label></td>
				<td colspan="3" class="input"><input name="ORDER_CATEGORY_CODE"
					type="text" class="short" id="ORDER_CATEGORY_CODE" maxlength="20"
					value="" onkeypress="return ORDER_CATEGORY_CODE_KeyPress(event);"
					onblur="AJAX_Refresh_ORDER_CATEGORY_CODE();" /> <input
					type="image" name="ORDER_CATEGORY_BUTTON"
					id="ORDER_CATEGORY_BUTTON" class="image_button"
					src="../../images/search_button.png" alt=""
					onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM_CATEGORY&DISPLAY_FIELD=DESCRIPTION_THAI&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=ORDER_CATEGORY_CODE&HANDLE=AJAX_Refresh_ORDER_CATEGORY_CODE'); return false;" />
					<input type="text" id="ORDER_CATEGORY_DESCRIPTION"
					name="ORDER_CATEGORY_DESCRIPTION" class="mediumMax"
					readonly="readonly" /></td>
			</tr>

			<tr id="block_doctor_category_code">
				<td class="label"><label for="DOCTOR_CATEGORY_CODE">${labelMap.DOCTOR_CATEGORY_CODE}</label></td>
				<td class="input" colspan="3"><input
					name="DOCTOR_CATEGORY_CODE" type="text" class="short"
					id="DOCTOR_CATEGORY_CODE" maxlength="20" value=""
					onkeypress="return DOCTOR_CATEGORY_CODE_KeyPress(event);"
					onblur="AJAX_Refresh_DOCTOR_CATEGORY();" /> <input
					id="SEARCH_DOCTOR_CATEGORY_CODE" name="SEARCH_DOCTOR_CATEGORY_CODE"
					type="image" class="image_button"
					src="../../images/search_button.png" alt=""
					onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_CATEGORY&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DOCTOR_CATEGORY_CODE&HANDLE=AJAX_Refresh_DOCTOR_CATEGORY'); return false;" />
					<input name="DOCTOR_CATEGORY_DESCRIPTION" type="text"
					class="mediumMax" id="DOCTOR_CATEGORY_DESCRIPTION"
					readonly="readonly" value="" /></td>
			</tr>

			<tr id="block_order_item_code">
				<td class="label"><label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label></td>
				<td colspan="3" class="input"><input name="ORDER_ITEM_CODE"
					type="text" class="short" id="ORDER_ITEM_CODE" maxlength="20"
					value="" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);"
					onblur="AJAX_Refresh_ORDER_ITEM();" /> <input type="image"
					name="ORDER_ITEM_BUTTON" id="ORDER_ITEM_BUTTON"
					class="image_button" src="../../images/search_button.png" alt=""
					onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_THAI&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=ORDER_ITEM_CODE&HANDLE=AJAX_Refresh_ORDER_ITEM'); return false;" />
					<input type="text" id="ORDER_ITEM_DESCRIPTION"
					name="ORDER_ITEM_DESCRIPTION" class="mediumMax" readonly="readonly" />
				</td>
			</tr>
			<tr id="block_invoice_no">
				<td class="label"><label for="INVOICE_NO">${labelMap.INVOICE_NO}</label></td>
				<td colspan="3" class="input">
					<input type="text" id="INVOICE_NO" name="INVOICE_NO" class="short" maxlength="20" onkeypress="return INVOICE_NO_KeyPress(event);" /> 
					<input id="SEARCH_INVOICE_NO" name="SEARCH_INVOICE_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search"
					onclick="openSearchForm('../search_invoice.jsp?TABLE=TRN_DAILY&RETURN_FIELD=INVOICE_NO&DISPLAY_FIELD=INVOICE_DATE&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=INVOICE_NO&HANDLE=AJAX_Refresh_INVOICE'); return false;" />
				</td>
			</tr>
			
			<tr id="block_module">
                   <td class="label"><label for="aText">${labelMap.TRANSACTION_MODULE}</label></td>
                   <td class="input" colspan="3">
                       <select class="short" id="TRANSACTION_MODULE" name="TRANSACTION_MODULE">
                           <option value="%">ALL</option>
						<option value="TR">DF</option>
						<option value="AR">AR</option>
						<!-- <option value="PT">PARTIAL RECEIPT</option>
						<option value="OW">ONWARD TRANSACTION</option>
						<option value="DY">DISCHARGE PAYMENT</option>
						<option value="DH">DISCHARGE HOLD</option> -->
                   	</select>
				</td>
            </tr>                
			
			<tr id="block_adminssion_type">
				<td class="label"><label for="ADMISSION_TYPE_CODE">${labelMap.ADMISSION_TYPE_CODE}</label></td>
					<td class="input"><select class="short" id="ADMISSION_TYPE_CODE" name="ADMISSION_TYPE_CODE">
							<option value="%">-- Select --</option>
							<option value="%">ALL</option>
							<option value="I">IPD</option>
							<option value="O">OPD</option>
					</select></td>
				<td class="label"><label for="aText">${labelMap.TRANSACTION_TYPE}</label></td>
                   <td class="input">
                       <select class="short" id="TRANSACTION_TYPE" name="TRANSACTION_TYPE">
                           <option value="%">-- Select --</option>
                           <option value="%">ALL</option>
                           <option value="INV">Credit</option>
                           <option value="REV">Cash</option>
                   		</select>                    
                   </td>
			</tr>
			
			<tr id="block_document_type">
				<td class="label"><label for="DOCUMENT_TYPE">${labelMap.DOCUMENT_TYPE}</label><br />
				</td>
				<td class="input"><select class="short" id="DOCUMENT_TYPE"
					name="DOCUMENT_TYPE">
						<option value="%">-- Select --</option>
						<option value="N">INVOICE</option>
						<option value="Y">WRITE OFF</option>
				</select></td>
			</tr>

			<!-- Monthly Checklist -->
			
			<tr id="block_month_year">
				<td class="label"><label>${labelMap.MM}</label></td>
				<td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
				<td class="label"><label>${labelMap.YYYY}</label></td>
				<td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
			</tr>
			<tr id="block_doctor_code_from">
				<td class="label"><label for="DOCTOR_CODE_FROM">${labelMap.DOCTOR_CODE_FROM}</label></td>
				<td colspan="3" class="input"><input type="text"
					id="DOCTOR_CODE_FROM" name="DOCTOR_CODE_FROM" class="short"
					value="" onkeypress="return DOCTOR_CODE_FROM_KeyPress(event);"
					onblur="AJAX_Refresh_DOCTOR_FROM();" /> <input
					id="SEARCH_DOCTOR_CODE_FROM" name="SEARCH_DOCTOR_CODE_FROM"
					type="image" class="image_button"
					src="../../images/search_button.png" alt="Search"
					onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&TARGET=DOCTOR_CODE_FROM&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_FROM'); return false;" />
					<input type="text" id="DOCTOR_NAME_FROM" name="DOCTOR_NAME_FROM"
					class="mediumMax" readonly="readonly" value="" /></td>
			</tr>
			<tr id="block_doctor_code_to">
				<td class="label"><label for="DOCTOR_CODE_TO">${labelMap.DOCTOR_CODE_TO}</label></td>
				<td colspan="3" class="input"><input type="text"
					id="DOCTOR_CODE_TO" name="DOCTOR_CODE_TO" class="short" value=""
					onkeypress="return DOCTOR_CODE_TO_KeyPress(event);"
					onblur="AJAX_Refresh_DOCTOR_TO();" /> <input
					id="SEARCH_DOCTOR_CODE_TO" name="SEARCH_DOCTOR_CODE_TO"
					type="image" class="image_button"
					src="../../images/search_button.png" alt="Search"
					onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&TARGET=DOCTOR_CODE_TO&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_TO'); return false;" />
					<input type="text" id="DOCTOR_NAME_TO" name="DOCTOR_NAME_TO"
					class="mediumMax" readonly="readonly" value="" /></td>
			</tr>
			<tr id="block_guarantee_department">
				<td class="label"><label for="GUARANTEE_DEPARTMENT">${labelMap.DEPARTMENT}</label>
				</td>
				<td class="input" colspan="3"><input
					name="GUARANTEE_DEPARTMENT_CODE" type="text" class="short"
					id="GUARANTEE_DEPARTMENT_CODE"
					onblur="AJAX_Refresh_GUARANTEE_DEPARTMENT();"
					onkeypress="return GUARANTEE_DEPARTMENT_CODE_KeyPress(event);"
					value="" maxlength="20" /> <input
					id="SEARCH_GUARANTEE_DEPARTMENT_CODE"
					name="SEARCH_GUARANTEE_DEPARTMENT_CODE" type="image"
					class="image_button" src="../../images/search_button.png"
					alt="Search"
					onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&TARGET=GUARANTEE_DEPARTMENT_CODE&DISPLAY_FIELD=DESCRIPTION&BEACTIVE=1&HANDLE=AJAX_Refresh_GUARANTEE_DEPARTMENT'); return false;" />
					<input type="text" id="GUARANTEE_DEPARTMENT_DESCRIPTION"
					name="GUARANTEE_DEPARTMENT_DESCRIPTION" class="mediumMax"
					readonly="readonly" value="" /></td>
			</tr>
			<tr id="block_gl_type">
				<td class="label"><label for="GL_TYPE">${labelMap.GL_TYPE}</label></td>
				<td class="input" colspan="3"><select id="GL_TYPE"
					name="GL_TYPE">
						<option value="GL">GL</option>
						<option value="AC">ACCU</option>
				</select></td>
			</tr>
			<tr id="block_expense_sign">
                <td class="label"><label for="EXPENSE_SIGN">${labelMap.EXPENSE_SIGN}</label></td>
               	<td colspan="3" class="input">
               	<select name="EXPENSE_SIGN" class="medium">
               		<option value="">${labelMap.EXPENSE_SING_LABEL_ALL}</option>
               		<option value="1">${labelMap.EXPENSE_SING_LABEL_REVENUE}</option>
               		<option value="-1">${labelMap.EXPENSE_SING_LABEL_EXPENSE}</option>
               	</select>
              	</td>
           </tr>
           <tr id="block_expense_account_code">
                <td class="label"><label for="EXPENSE_ACCOUNT_CODE">${labelMap.EXPENSE_ACCOUNT_CODE}</label></td>
                <td colspan="3" class="input">
					<input type="text" id="EXPENSE_ACCOUNT_CODE" name="EXPENSE_ACCOUNT_CODE" class="short" value="" onkeypress="return EXPENSE_ACCOUNT_CODE_KeyPress(event);" onblur="AJAX_Refresh_EXPENSE_ACCOUNT_CODE();" />
                    <input id="SEARCH_EXPENSE_ACCOUNT_CODE" name="SEARCH_EXPENSE_ACCOUNT_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ACCOUNT&DISPLAY_FIELD=DESCRIPTION&TARGET=EXPENSE_ACCOUNT_CODE&COND=AND CODE IN (SELECT ACCOUNT_CODE FROM EXPENSE)&HANDLE=AJAX_Refresh_EXPENSE_ACCOUNT_CODE'); return false;" />
                    <input type="text" id="EXPENSE_ACCOUNT_CODE_DESCRIPTION" name="EXPENSE_ACCOUNT_CODE_DESCRIPTION" class="mediumMax" readonly="readonly" value="" />
                </td>
            </tr>
			<tr id="block_expense_code">
                 <td class="label"><label for="EXPENSE_CODE">${labelMap.EXPENSE_CODE}</label></td>
                 <td colspan="3" class="input">
					<input type="text" id="EXPENSE_CODE" name="EXPENSE_CODE" class="short" value="" onkeypress="return EXPENSE_CODE_KeyPress(event);" onblur="AJAX_Refresh_EXPENSE_CODE();" />
					<input id="SEARCH_EXPENSE_CODE" name="SEARCH_EXPENSE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=EXPENSE&DISPLAY_FIELD=DESCRIPTION&TARGET=EXPENSE_CODE&HANDLE=AJAX_Refresh_EXPENSE_CODE'); return false;" />
					<input type="text" id="EXPENSE_CODE_DESCRIPTION" name="EXPENSE_CODE_DESCRIPTION" class="mediumMax" readonly="readonly" value="" />                    
                 </td>
            </tr>                
            <tr id="block_payment_term">
            	<td class="label">
            		<label>${labelMap.PAYMENT_TERM}</label>
            	</td>
            	<td class="input" colspan="3">
            		<select id="term" name="term" class="short">
               	 		 <option value="1">Half Month</option>
               	 		 <option value="2" selected="selected">Month End</option>
               	 	</select>
            	</td>
            </tr>
            <tr id="block_payment_mode">
                <td class="label">
                    <label for="PAYMENT_MODE_CODE"><span class="style1">${labelMap.PAYMENT_MODE_CODE}</span></label>                    </td>
                <td class="input" colspan="3">
				<select id="PAYMENT_MODE_CODE" name="PAYMENT_MODE_CODE" class="mediumMax">
					<option value="%">--SELECT ALL--</option>
					<option value="B">Bank Transfer</option>
					<option value="C">Cash</option>
					<option value="CQ">Cheque</option>
					<option value="PR">Payroll</option>
					<option value="U">Unpaid</option>
				</select>
                </td>
            </tr>                
			<tr id="block_save_file">
				<td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
				<td class="input" colspan="3"><input type="text" class="mediumMax"
					id="SAVE_FILE" name="SAVE_FILE" /> <select id="FILE_TYPE"
					name="FILE_TYPE" onChange="changeDropDownType();">
						<!--
						<option value="">Select</option>
						<option value="pdf">pdf</option>
						<option value="xls">xls</option>
						 -->
						<option value="txt">text</option>
				</select></td>
			</tr>
			<tr>
				<th colspan="4" class="buttonBar">
					<input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Report_Save();" /> 
					<input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" /> 
					<input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" /> 
					<input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" /></th>
			</tr>
		</table>
	</form>
</body>
</html>