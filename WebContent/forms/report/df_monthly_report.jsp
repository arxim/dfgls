<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@ include file="../../_global.jsp" %>
<%@page import="df.jsp.Guard"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>

<%    
	if (!Guard.checkPermission(session, Guard.PAGE_INPUT_BANK)) {
	    response.sendRedirect("../message.jsp");
	    return;
	}

    if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            ProcessUtil proUtil = new ProcessUtil();
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
            c.Close();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "DF Monthly Report", "รายงานค่าแพทย์รายเดือน");
            labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
            labelMap.add("DOCTOR_CODE_FROM", "From Doctor Profile Code", "จากแพทย์รหัส");
            labelMap.add("DOCTOR_CODE_TO", "To Doctor Profile Code", "ถึงแพทย์รหัส");
            labelMap.add("DOCTOR_DEPARTMENT_CODE", "Doctor Department", "รหัสแผนกแพทย์");
			labelMap.add("DOCTOR_CATEGORY_CODE", "Doctor Category", "รหัสกลุ่มแพทย์");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รหัสการรักษา");
            labelMap.add("ORDER_ITEM_CATEGORY_CODE", "Order Item Category Code", "รหัสกลุ่มการรักษา");
            labelMap.add("FROM_DATE", "From Date", "ตั้งแต่วันที่");
            labelMap.add("TO_DATE", "To Date", "ถึงวันที่");
            labelMap.add("TRANSACTION_TYPE", "Transaction Type", "ประเภทใบแจ้งหนี้");
            labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "แผนกผู้ป่วย");
            labelMap.add("STATUS", "Active Status", "สถานะใช้งาน");
            labelMap.add("STATUS_1", "Active", "ใช้งาน");
            labelMap.add("STATUS_0", "In Active", "ยกเลิก");
            labelMap.add("CALCULATE_DF", "Calculate DF ???", "CALCULATE_DF ???");
            labelMap.add("CALCULATE_DF_0", "No", "ไม่ใช่");
            labelMap.add("CALCULATE_DF_1", "Yes", "ใช่");
			labelMap.add("MM", "Month", "เดือน");
			labelMap.add("YYYY", "Year", "ปี");
			labelMap.add("EXPORT_ACCRUE", "Accrue Export", "รายระเอียดการส่ง Accrue");
			labelMap.add("EXPORT_GL", "GL Payment Export", "รายระเอียดการส่ง GL Payment");
			labelMap.add("EXPORT_XRAY_MANAGEMENT", "X-RAY Management Export", "สรุปรายได้แพทย์กลุ่ม X-ray");
			
			labelMap.add("EXPORT_GUARANTEE_MONTHLY", "Monthly Guarantee Export", "สรุปรายได้แพทย์การันตีรายเดือน");
			labelMap.add("EXPORT_GUARANTEE_DAILY", "Daily Guarantee Export", "สรุปรายได้แพทย์การันตีรายวัน");
			
			labelMap.add("EXPORT_GUARANTEE_MONTHLY_ALL_YEAR", "Monthly Guarantee Export All Year", "สรุปรายได้แพทย์การันตีรายเดือนทั้งปี");
			labelMap.add("EXPORT_GUARANTEE_DAILY_ALL_YEAR", "Daily Guarantee Export All Year", "สรุปรายได้แพทย์การันตีรายวันทั้งปี");
			
			labelMap.add("REPORT_GUARANTEE_TRANSACTION", "Guarantee Setup", "รายการการันตีแพทย์");
			labelMap.add("REPORT_SUM_DF","Summary Doctorfee","รายงานสรุปรายได้แพทย์");
			labelMap.add("REPORT_DETAIL_DF","Revenue Detail Payment","รายงานรายละเอียดรายได้แพทย์ทำจ่าย");
			labelMap.add("REPORT_ALL_DETAIL_DF","Revenue Detail All Payment","รายงานรายละเอียดรายได้แพทย์ในเดือน");
			labelMap.add("REPORT_PAYMENT_VOUCHER","Payment Voucher","เอกสารการจ่ายเงินแพทย์");
			labelMap.add("REPORT_BANK_TRANSFER","Bank Transfer","รายงานการโอนค่าแพทย์ผ่านธนาคาร");
			labelMap.add("REPORT_PAID_CHEQUE","Summary Cheque Paid","รายงานทำจ่ายเช็คค่าแพทย์");
			labelMap.add("REPORT_EXPENSE","Adjust Revenue","รายงานรายได้ค่าใช้จ่ายแพทย์");
			labelMap.add("REPORT_DF_HOLD","DF Hold Payment","รายงานรายการระงับจ่ายค่าแพทย์");
            labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
            labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร");
            labelMap.add("VIEW", "View", "แสดงผล");
            labelMap.add("PAYMENT_TERM", "Payment Term", "งวดทำจ่าย");
            labelMap.add("PAYMENT_DATE", "Payment Date", "วันที่ทำจ่าย");
            labelMap.add("MESSAGE", "Please Select Report Module", "กรุณาเลือกรายงาน");
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
            String report = "";
            request.setAttribute("labelMap", labelMap.getHashMap());
            String startDateStr = JDate.showDate(JDate.getDate()); 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript">
            function Report_View() {
                //alert(document.mainForm.REPORT_FILE_NAME.value);
                if(document.mainForm.REPORT_FILE_NAME.value == "None"){
                    alert("Please Select Report");
                    document.mainForm.REPORT_FILE_NAME.focus();
                }else{
					if(document.mainForm.MM.value == "" || document.mainForm.YYYY.value == ""){
						alert("Please Select Month/Year");
						if(document.mainForm.MM.value == ""){
							document.mainForm.MM.focus();
						}else{
							document.mainForm.YYYY.focus();
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
						alert("Please Select Month/Year");
						if(document.mainForm.MM.value == ""){
							document.mainForm.MM.focus();
						}else{
							document.mainForm.YYYY.focus();
						}
					}else{
						document.mainForm.REPORT_DISPLAY.value = "save";
                    	document.mainForm.target = "_blank";
                    	document.mainForm.submit();
					}
                }
            }
			/*
			<option value="None">-- Select Doctorfee Monthly Report --</option>
            <option value="SumDoctorfeeRevenue">${labelMap.REPORT_SUM_DF}</option>
			<option value="DetailDoctorfeeRevenue">${labelMap.REPORT_DETAIL_DF}</option>
            <option value="PaymentVoucher">${labelMap.REPORT_PAYMENT_VOUCHER}</option>
            <option value="DoctorfeeBankTransfer">${labelMap.REPORT_BANK_TRANSFER}</option>
			*/
            function check_expense_report(){
				if(document.mainForm.REPORT_FILE_NAME.value=="ExpenseDetail"){
					document.mainForm.EXPENSE_SIGN.disabled = false;
					document.mainForm.EXPENSE_ACCOUNT_CODE.disabled = false;
					document.mainForm.EXPENSE_CODE.disabled = false;
					document.getElementById("SEARCH_EXPENSE_ACCOUNT_CODE").disabled = false;
					document.getElementById("SEARCH_EXPENSE_CODE").disabled = false;
				}else{
					document.mainForm.EXPENSE_SIGN.disabled = true;
					document.mainForm.EXPENSE_ACCOUNT_CODE.disabled = true;
					document.mainForm.EXPENSE_CODE.disabled = true;
					document.getElementById("SEARCH_EXPENSE_ACCOUNT_CODE").disabled = true;
					document.getElementById("SEARCH_EXPENSE_CODE").disabled = true;					
				}
            } 			
            function changeDropDownList(){
                if(document.mainForm.REPORT_FILE_NAME.value == "None" || document.mainForm.REPORT_FILE_NAME.value == "PaymentVoucher" || 
					document.mainForm.REPORT_FILE_NAME.value == "DetailDoctorfeeRevenue"){
					document.mainForm.MM.disabled = false;
					document.mainForm.YYYY.disabled = false;
                    document.mainForm.DOCTOR_CODE_FROM.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CODE_FROM').style.display = "inline";
                    document.mainForm.DOCTOR_CODE_TO.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CODE_TO').style.display = "inline";
                }
                if(document.mainForm.REPORT_FILE_NAME.value == "SumDoctorfeeRevenue" || document.mainForm.REPORT_FILE_NAME.value == "DoctorfeeBankTransfer" ||
					document.mainForm.REPORT_FILE_NAME.value == "Guarantee_Checklist"){
                    document.mainForm.MM.disabled = false;
					document.mainForm.YYYY.disabled = false;
                    document.mainForm.DOCTOR_CODE_FROM.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_CODE_FROM').style.display = "none";
                    document.mainForm.DOCTOR_CODE_TO.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_CODE_TO').style.display = "none";
                }
                check_expense_report();
            }
/********************************************************************************************************************
 * Retreive doctor information : Begin
 */            
            function DOCTOR_CODE_FROM_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CODE_FROM.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_FROM() {
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR_PROFILE&COND=CODE='" + document.mainForm.DOCTOR_CODE_FROM.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_FROM);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_FROM() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_CODE_FROM.value = "";
                        document.mainForm.DOCTOR_NAME_FROM.value = "not found";
                        return;
                    }

                    // Data found
                    document.mainForm.DOCTOR_NAME_FROM.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                    document.mainForm.DOCTOR_CODE_TO.value = document.mainForm.DOCTOR_CODE_FROM.value;
                    document.mainForm.DOCTOR_NAME_TO.value = document.mainForm.DOCTOR_NAME_FROM.value;
                }
            }
                        
/********************************************************************************************************************
 * Retreive doctor information : End
 */
 
 /********************************************************************************************************************
 * Retreive doctor information : Begin
 */            
            function DOCTOR_CODE_TO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CODE_TO.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_TO() {
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR_PROFILE&COND=CODE='" + document.mainForm.DOCTOR_CODE_TO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_TO);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_TO() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_CODE_TO.value = "";
                        document.mainForm.DOCTOR_NAME_TO.value = "not found";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME_TO.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
                        
/********************************************************************************************************************
 * Retreive doctor information : End
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
                        document.mainForm.DOCTOR_DEPARTMENT_DESCRIPTION.value = "not found";
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
                        document.mainForm.ORDER_ITEM_DESCRIPTION.value = "not found";
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
 * Retreive order item category information : Begin
 */            
            function ORDER_ITEM_CATEGORY_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ORDER_ITEM_CATEGORY_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_ORDER_ITEM_CATEGORY() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" + document.mainForm.ORDER_ITEM_CATEGORY_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY() {
                if (AJAX_IsComplete()) {
                                        
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.ORDER_ITEM_CODE.value = "";
                        document.mainForm.ORDER_ITEM_CATEGORY_DESCRIPTION.value = "not found";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.ORDER_ITEM_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_THAI");
                }
            }
/********************************************************************************************************************
 * Retreive order item category information : End
 */
      
  /********************************************************************************************************************
 * Retreive doctor category information : Begin
 */            
            function DOCTOR_CATEGORY_KeyPress(e) {
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
 * Retreive EXPENSE_ACCOUNT_CODE information : Begin
 */
		 function EXPENSE_ACCOUNT_CODE_KeyPress(e) {
		     var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
		
		     if (key == 13) {
		         document.mainForm.EXPENSE_ACCOUNT_CODE.blur();
		         return false;
		     }
		     else {
		         return true;
		     }
		 }
		
		 function AJAX_Refresh_EXPENSE_ACCOUNT_CODE() {
		     var target = "../../RetrieveData?TABLE=ACCOUNT&COND=CODE='" + document.mainForm.EXPENSE_ACCOUNT_CODE.value + "'";
		     AJAX_Request(target, AJAX_Handle_Refresh_EXPENSE_ACCOUNT_CODE);
		 }
		
		 function AJAX_Handle_Refresh_EXPENSE_ACCOUNT_CODE() {
		     if (AJAX_IsComplete()) {
		
		         var xmlDoc = AJAX.responseXML;
		
		         // Data not found
		         if (!isXMLNodeExist(xmlDoc, "DESCRIPTION")) {			         
		             //document.mainForm.ORDER_ITEM_CODE.value = "";
		             document.mainForm.EXPENSE_ACCOUNT_CODE_DESCRIPTION.value = "";
		             return;
		         }

		         // Data found
		         document.mainForm.EXPENSE_ACCOUNT_CODE_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
		     }
		 }
 
/********************************************************************************************************************
 * Retreive EXPENSE_ACCOUNT_CODE information : End
 */  
 /********************************************************************************************************************
  * Retreive EXPENSE_CODE information : Begin
  */
 		 function EXPENSE_CODE_KeyPress(e) {
 		     var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
 		
 		     if (key == 13) {
 		         document.mainForm.EXPENSE_CODE.blur();
 		         return false;
 		     }
 		     else {
 		         return true;
 		     }
 		 }
 		
 		 function AJAX_Refresh_EXPENSE_CODE() {
 		     var target = "../../RetrieveData?TABLE=EXPENSE&COND=CODE='" + document.mainForm.EXPENSE_CODE.value + "'";
 		     AJAX_Request(target, AJAX_Handle_Refresh_EXPENSE_CODE);
 		 }
 		
 		 function AJAX_Handle_Refresh_EXPENSE_CODE() {
 		     if (AJAX_IsComplete()) {
 		
 		         var xmlDoc = AJAX.responseXML;
 		
 		         // Data not found
 		         if (!isXMLNodeExist(xmlDoc, "DESCRIPTION")) {			         
 		             //document.mainForm.ORDER_ITEM_CODE.value = "";
 		             document.mainForm.EXPENSE_CODE_DESCRIPTION.value = "";
 		             return;
 		         }

 		         // Data found
 		         document.mainForm.EXPENSE_CODE_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
 		     }
 		 }
  
 /********************************************************************************************************************
  * Retreive EXPENSE_CODE information : End
  */  
        </script>
    </head>
    <body leftmargin="0">
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("df_monthly_report.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="df_monthly"/>
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                    </th>
                </tr>
                <tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label></td>
                    <td class="input">
					<select class="medium" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" onchange="changeDropDownList();">
                      <option value="None">-- Select Monthly Report --</option>
                      <option value="SummaryRevenueAllDetail">${labelMap.REPORT_ALL_DETAIL_DF}</option>
                      <option value="SummaryRevenueByDetail">${labelMap.REPORT_DETAIL_DF}</option>
                      <option value="SummaryRevenueByDetailStep">Revenue Detail Payment (STEP)</option>
                      <option value="SummaryRevenueByDetailForDoctorVCH">Revenue Detail Payment (I/O)</option>
                      <option value="PaymentVoucher<%= session.getAttribute("HOSPITAL_CODE").toString() %>">${labelMap.REPORT_PAYMENT_VOUCHER}</option>
                      <option value="DFPaidCheque">${labelMap.REPORT_PAID_CHEQUE}</option>
                      <option value="ExpenseDetail">${labelMap.REPORT_EXPENSE}</option>
		      		  <option value="DFHold">${labelMap.REPORT_DF_HOLD}</option>
                      
                      <option value="DailyGuarantee">${labelMap.EXPORT_GUARANTEE_DAILY}</option>
                      <option value="MonthlyGuarantee">${labelMap.EXPORT_GUARANTEE_MONTHLY}</option>
                     
                      <option value="DailyGuaranteeAllYear">${labelMap.EXPORT_GUARANTEE_DAILY_ALL_YEAR}</option>
                      <option value="MonthlyGuaranteeAllYear">${labelMap.EXPORT_GUARANTEE_MONTHLY_ALL_YEAR}</option>

                      <option value="XrayManagement">${labelMap.EXPORT_XRAY_MANAGEMENT}</option>
                      <option value="Accrue">${labelMap.EXPORT_ACCRUE}</option>
                      <option value="GL">${labelMap.EXPORT_GL}</option>
                    </select>
					</td>
					<td class="label" align="right" width="25%"><label for="date">${labelMap.PAYMENT_DATE}</label></td>
                    <td class="input" valign="middle" width="25%" align="left">
                    	<input type="text" value="<%=startDateStr%>" id="date" name="date" class="short" value="<%=request.getParameter("START_DATE") == null ? "" : request.getParameter("START_DATE")%>" />
                        <input name="image2" type="image" class="image_button" onclick="displayDatePicker('date'); return false;" src="../../images/calendar_button.png" alt="" />
                    </td>
					
                </tr>
				<tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>					</td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM",b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
                </tr>
                <!-- 
                <tr align="center">
                	<td class="label" align="right" width="25%"><label for="term">${labelMap.PAYMENT_TERM}</label></td>
                    <td class="input" valign="middle" width="25%" align="left">
                   	 	<select class="short" name="term" id="term">
                   	 		 <option value="2">Month End</option>
                   	 		 <option value="1">Half Month</option>
                   	 	</select>
                    </td>
                    
            		<td class="label" align="right" width="25%"><label for="date">${labelMap.PAYMENT_DATE}</label></td>
                    <td class="input" valign="middle" width="25%" align="left">
                    	<input type="text" value="<%=startDateStr%>" id="date" name="date" class="short" value="<%=request.getParameter("START_DATE") == null ? "" : request.getParameter("START_DATE")%>" />
                        <input name="image2" type="image" class="image_button" onclick="displayDatePicker('date'); return false;" src="../../images/calendar_button.png" alt="" />
                    </td>
                </tr>
                 -->           
				<tr>
                    <td class="label"><label for="DOCTOR_CATEGORY_CODE">${labelMap.DOCTOR_CATEGORY_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CATEGORY_CODE" name="DOCTOR_CATEGORY_CODE" class="short" value="" onkeypress="return DOCTOR_CATEGORY_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_CATEGORY();" />
                        <input id="SEARCH_DOCTOR_CATEGORY_CODE" name="SEARCH_DOCTOR_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_CATEGORY&DISPLAY_FIELD=DESCRIPTION&TARGET=DOCTOR_CATEGORY_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_CATEGORY'); return false;" />
                        <input type="text" id="DOCTOR_CATEGORY_DESCRIPTION" name="DOCTOR_CATEGORY_DESCRIPTION" class="mediumMax" readonly="readonly" value="" />                    </td>
                </tr>
				<tr>
                    <td class="label"><label for="DOCTOR_DEPARTMENT_CODE">${labelMap.DOCTOR_DEPARTMENT_CODE}</label></td>
                    <td class="input" colspan="3">
                        <input name="DOCTOR_DEPARTMENT_CODE" type="text" class="short" id="DOCTOR_DEPARTMENT_CODE" maxlength="20" value="" onkeypress="return DOCTOR_DEPARTMENT_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_DEPARTMENT();" />
                        <input id="SEARCH_DOCTOR_DEPARTMENT_CODE" name="SEARCH_DOCTOR_DEPARTMENT_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DOCTOR_DEPARTMENT_CODE&HANDLE=AJAX_Refresh_DOCTOR_DEPARTMENT'); return false;" />
                        <input name="DOCTOR_DEPARTMENT_DESCRIPTION" type="text" class="mediumMax" id="DOCTOR_DEPARTMENT_DESCRIPTION" readonly="readonly" value="" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input name="ORDER_ITEM_CODE" type="text" class="short" id="ORDER_ITEM_CODE" maxlength="20" value="" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM();" />
                        <input type="image" name="ORDER_ITEM_BUTTON" id="ORDER_ITEM_BUTTON" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_THAI&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=ORDER_ITEM_CODE&HANDLE=AJAX_Refresh_ORDER_ITEM'); return false;" />
                        <input type="text" id="ORDER_ITEM_DESCRIPTION" name="ORDER_ITEM_DESCRIPTION" class="mediumMax" readonly="readonly"/>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ORDER_ITEM_CATEGORY_CODE">${labelMap.ORDER_ITEM_CATEGORY_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input name="ORDER_ITEM_CATEGORY_CODE" type="text" class="short" id="ORDER_ITEM_CATEGORY_CODE" maxlength="20" value="" onkeypress="return ORDER_ITEM_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM_CATEGORY();" />
                        <input type="image" name="ORDER_ITEM_CATEGORY_BUTTON" id="ORDER_ITEM_CATEGORY_BUTTON" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM_CATEGORY&DISPLAY_FIELD=DESCRIPTION_THAI&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=ORDER_ITEM_CATEGORY_CODE&HANDLE=AJAX_Refresh_ORDER_ITEM_CATEGORY'); return false;" />
                        <input type="text" id="ORDER_ITEM_CATEGORY_DESCRIPTION" name="ORDER_ITEM_CATEGORY_DESCRIPTION" class="mediumMax" readonly="readonly"/>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_CODE_FROM">${labelMap.DOCTOR_CODE_FROM}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE_FROM" name="DOCTOR_CODE_FROM" class="short" value="" onkeypress="return DOCTOR_CODE_FROM_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_FROM();" />
                        <input id="SEARCH_DOCTOR_CODE_FROM" name="SEARCH_DOCTOR_CODE_FROM" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE_FROM&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_FROM'); return false;" />
                        <input type="text" id="DOCTOR_NAME_FROM" name="DOCTOR_NAME_FROM" class="mediumMax" readonly="readonly" value="" />                    </td>
                </tr>
				<tr>
                    <td class="label"><label for="DOCTOR_CODE_TO">${labelMap.DOCTOR_CODE_TO}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE_TO" name="DOCTOR_CODE_TO" class="short" value="" onkeypress="return DOCTOR_CODE_TO_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_TO();" />
                        <input id="SEARCH_DOCTOR_CODE_TO" name="SEARCH_DOCTOR_CODE_TO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE_TO&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_TO'); return false;" />
                        <input type="text" id="DOCTOR_NAME_TO" name="DOCTOR_NAME_TO" class="mediumMax" readonly="readonly" value="" />                    </td>
                </tr>
				<tr>
                    <td class="label"><label for="EXPENSE_SIGN">${labelMap.EXPENSE_SIGN}</label></td>
                    <td colspan="3" class="input">
                    	<select name="EXPENSE_SIGN" class="medium">
                    		<option value="">${labelMap.EXPENSE_SING_LABEL_ALL}</option>
                    		<option value="1">${labelMap.EXPENSE_SING_LABEL_REVENUE}</option>
                    		<option value="-1">${labelMap.EXPENSE_SING_LABEL_EXPENSE}</option>
                    	</select>
                    </td>
                </tr>
               <tr>
                    <td class="label"><label for="EXPENSE_ACCOUNT_CODE">${labelMap.EXPENSE_ACCOUNT_CODE}</label></td>
                    <td colspan="3" class="input">
						<input type="text" id="EXPENSE_ACCOUNT_CODE" name="EXPENSE_ACCOUNT_CODE" class="short" value="" onkeypress="return EXPENSE_ACCOUNT_CODE_KeyPress(event);" onblur="AJAX_Refresh_EXPENSE_ACCOUNT_CODE();" />
                        <input id="SEARCH_EXPENSE_ACCOUNT_CODE" name="SEARCH_EXPENSE_ACCOUNT_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ACCOUNT&DISPLAY_FIELD=DESCRIPTION&TARGET=EXPENSE_ACCOUNT_CODE&COND=AND CODE IN (SELECT ACCOUNT_CODE FROM EXPENSE)&HANDLE=AJAX_Refresh_EXPENSE_ACCOUNT_CODE'); return false;" />
                        <input type="text" id="EXPENSE_ACCOUNT_CODE_DESCRIPTION" name="EXPENSE_ACCOUNT_CODE_DESCRIPTION" class="mediumMax" readonly="readonly" value="" />
                    </td>
                </tr>
				<tr>
                    <td class="label"><label for="EXPENSE_CODE">${labelMap.EXPENSE_CODE}</label></td>
                    <td colspan="3" class="input">
						<input type="text" id="EXPENSE_CODE" name="EXPENSE_CODE" class="short" value="" onkeypress="return EXPENSE_CODE_KeyPress(event);" onblur="AJAX_Refresh_EXPENSE_CODE();" />
                        <input id="SEARCH_EXPENSE_CODE" name="SEARCH_EXPENSE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=EXPENSE&DISPLAY_FIELD=DESCRIPTION&TARGET=EXPENSE_CODE&HANDLE=AJAX_Refresh_EXPENSE_CODE'); return false;" />
                        <input type="text" id="EXPENSE_CODE_DESCRIPTION" name="EXPENSE_CODE_DESCRIPTION" class="mediumMax" readonly="readonly" value="" />                    
                    </td>
                </tr>                
                <tr>
                    <td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td class="input" colspan="3"><input type="text" class="short" id="SAVE_FILE" name="SAVE_FILE"/>
                        <select id="FILE_TYPE" name="FILE_TYPE">
                            <option value="all">all</option>
                            <option value="txt">text</option>
                            <option value="xls">xls</option>
                            <option value="pdf">pdf</option>
                        </select>                    
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Report_Save();" />
                        <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
					</th>
                </tr>
            </table>
        </form>
    </body>
</html>
<script language="javascript">
	check_expense_report();
</script>
