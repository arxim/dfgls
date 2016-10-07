<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@ include file="../../_global.jsp" %>

<%    
    if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Daily Checklist", "รายงานประจำวัน");
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
            labelMap.add("IS_ONWARD", "On Ward Transaction", "รายการยังไม่ออกบิล");
            labelMap.add("TRANSACTION_TYPE", "Transaction Type", "ประเภทใบแจ้งหนี้");
			labelMap.add("TRANSACTION_MODULE", "Module", "ประเภทรายการ");
            labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "แผนกผู้ป่วย");
            labelMap.add("STATUS", "Active Status", "สถานะใช้งาน");
            labelMap.add("STATUS_1", "Active", "ใช้งาน");
            labelMap.add("STATUS_0", "In Active", "ยกเลิก");
            labelMap.add("CALCULATE_DF", "Calculate DF ???", "CALCULATE_DF ???");
            labelMap.add("CALCULATE_DF_0", "No", "ไม่ใช่");
            labelMap.add("CALCULATE_DF_1", "Yes", "ใช่");
            labelMap.add("REPORT_TRANSACTION", "Interface DF Transaction", "นำเข้ารายการค่าแพทย์ชั่วคราว");
            labelMap.add("REPORT_TRANSACTION_RESULT", "Interface Result Transaction", "นำเข้ารายการแพทย์อ่านผลชั่วคราว");
            labelMap.add("REPORT_AR_TRANSACTION", "Account Receipt No DF ", "รายการรับชำระหนี้ไม่มีค่าแพทย์");
            labelMap.add("REPORT_AR_IN_TRANSACTION", "Account Receipt DF", "รายการรับชำระหนี้มีค่าแพทย์");
            labelMap.add("NO_VERIFY_TRANSACTION", "No Verify Transaction","ค่าแพทย์ยังไม่มีการอ่านผล");
            labelMap.add("NOT_VERIFY_TRANSACTION", "Not Verify Transaction","ค่าแพทย์ไม่สามารถอ่านผล");
			labelMap.add("MINUS_TRANSACTION", "Cancel Invoice", "รายการยกเลิกค่าแพทย์");
			labelMap.add("NOT_CALCULATE_TRANSACTION", "Not Calculate Transaction", "รายการที่ไม่ทำการคำนวณ");
            labelMap.add("REPORT_SUMMARY_DAILY", "DF Transaction Checklist", "รายงานตรวจสอบค่าแพทย์ในระบบ");
            labelMap.add("TRANSACTION_EDIT", "Transaction Edit", "แก้ไขรายการค่าแพทย์");
            labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
            labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร");
            labelMap.add("VIEW", "View", "แสดงผล");
            labelMap.add("MESSAGE", "Please Select Report Module", "กรุณาเลือกรายงาน");
            String report = "";
            request.setAttribute("labelMap", labelMap.getHashMap());
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
                if(document.mainForm.REPORT_FILE_NAME.value == "None"){
                    alert("Please Select Report");
                    document.mainForm.REPORT_FILE_NAME.focus();
                }else{
					if((document.mainForm.FROM_DATE.value == "" || document.mainForm.TO_DATE.value == "") && document.mainForm.INVOICE_NO.value == ""){
						alert("Please Select From Date/To Date");
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
					if((document.mainForm.FROM_DATE.value == "" || document.mainForm.TO_DATE.value == "") && document.mainForm.INVOICE_NO.value == ""){
						alert("Please Select From Date/To Date");
					}else{
						if(document.mainForm.REPORT_FILE_NAME.value == "SummaryDaily" || document.mainForm.REPORT_FILE_NAME.value == "NoVerifyTransaction" || document.mainForm.REPORT_FILE_NAME.value == "ImportTransaction"){
							document.mainForm.REPORT_DISPLAY.value = "save";
	                    	document.mainForm.target = "_blank";
	                    	document.mainForm.submit();
						}else{
							alert("Report '"+document.mainForm.REPORT_FILE_NAME.value+"' doesn't Support Text File");						
						}
					}
                }
            }
            
			function changeDropDownType(){
				if(document.mainForm.REPORT_FILE_NAME.value == "None" || document.mainForm.REPORT_FILE_NAME.value == "SummaryDaily" || document.mainForm.REPORT_FILE_NAME.value == "NoVerifyTransaction" || document.mainForm.REPORT_FILE_NAME.value == "ImportTransaction"){
                }else{
                	if(document.mainForm.FILE_TYPE.value == "txt"){
						alert("Report doesn't Support Text File");
                    }
                }
			}
			
            function changeDropDownList(){
                if(document.mainForm.REPORT_FILE_NAME.value == "None" || document.mainForm.REPORT_FILE_NAME.value == "SummaryDaily"){
					document.mainForm.INVOICE_NO.disabled = false;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "inline";
                    document.mainForm.DOCTOR_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_PROFILE_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_PROFILE_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_CATEGORY_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display = "inline";
                    document.mainForm.ORDER_CATEGORY_CODE.disabled = false;
                    document.getElementById('ORDER_CATEGORY_BUTTON').style.display = "inline";
                    document.mainForm.ORDER_ITEM_CODE.disabled = false;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "inline";
                    document.mainForm.TRANSACTION_MODULE.disabled = false;
                    document.mainForm.TRANSACTION_TYPE.disabled = false;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = false;
                    document.mainForm.DOCUMENT_TYPE.disabled = false;
                }else
                if(document.mainForm.REPORT_FILE_NAME.value == "ImportTransaction"){
					document.mainForm.INVOICE_NO.value = "";
					document.mainForm.INVOICE_NO.disabled = true;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "none";
                    document.mainForm.DOCTOR_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_PROFILE_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_PROFILE_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_CATEGORY_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display = "inline";
                    document.mainForm.ORDER_CATEGORY_CODE.disabled = false;
                    document.getElementById('ORDER_CATEGORY_BUTTON').style.display = "inline";
                    document.mainForm.ORDER_ITEM_CODE.disabled = false;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "inline";
                    document.mainForm.TRANSACTION_MODULE.disabled = false;
                    document.mainForm.TRANSACTION_TYPE.disabled = false;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = false;
                    document.mainForm.DOCUMENT_TYPE.disabled = true;
                }else
                if(document.mainForm.REPORT_FILE_NAME.value == "ImportVerifyTransaction"){
					document.mainForm.INVOICE_NO.value = "";
					document.mainForm.INVOICE_NO.disabled = true;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "none";
                    document.mainForm.DOCTOR_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_PROFILE_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_PROFILE_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_CATEGORY_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display="none";
                    document.mainForm.ORDER_CATEGORY_CODE.disabled = false;
                    document.getElementById('ORDER_CATEGORY_BUTTON').style.display = "inline";
                    document.mainForm.ORDER_ITEM_CODE.disabled = true;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "none";
                    document.mainForm.TRANSACTION_MODULE.disabled = false;
                    document.mainForm.TRANSACTION_TYPE.disabled = true;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = true;
                    document.mainForm.DOCUMENT_TYPE.disabled = true;
                }else
                if(document.mainForm.REPORT_FILE_NAME.value == "ImportARTransaction"){
					document.mainForm.INVOICE_NO.value = "";
					document.mainForm.INVOICE_NO.disabled = true;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "none";
                    document.mainForm.DOCTOR_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display="none";
                    document.mainForm.DOCTOR_PROFILE_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_PROFILE_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_CATEGORY_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = true;                    
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display="none";
                    document.mainForm.ORDER_CATEGORY_CODE.disabled = false;
                    document.getElementById('ORDER_CATEGORY_BUTTON').style.display = "inline";
                    document.mainForm.ORDER_ITEM_CODE.disabled = true;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "none";
                    document.mainForm.TRANSACTION_MODULE.disabled = false;
                    document.mainForm.TRANSACTION_TYPE.disabled = true;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = true;
                    document.mainForm.DOCUMENT_TYPE.disabled = false;
                }else
                if(document.mainForm.REPORT_FILE_NAME.value == "NoVerifyTransaction"){
					document.mainForm.INVOICE_NO.value = "";
					document.mainForm.INVOICE_NO.disabled = true;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "none";
                    document.mainForm.DOCTOR_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "none";
                    document.mainForm.DOCTOR_PROFILE_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_PROFILE_CODE').style.display = "none";
                    document.mainForm.DOCTOR_CATEGORY_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').style.display = "none";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display = "inline";
                    document.mainForm.ORDER_CATEGORY_CODE.disabled = false;
                    document.getElementById('ORDER_CATEGORY_BUTTON').style.display = "inline";
                    document.mainForm.ORDER_ITEM_CODE.disabled = false;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "inline";
                    document.mainForm.TRANSACTION_MODULE.disabled = true;
                    document.mainForm.TRANSACTION_TYPE.disabled = false;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = false;
                    document.mainForm.DOCUMENT_TYPE.disabled = true;
                }else
                if(document.mainForm.REPORT_FILE_NAME.value == "NotVerifyTransaction"){
					document.mainForm.INVOICE_NO.value = "";
					document.mainForm.INVOICE_NO.disabled = true;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "none";
                    document.mainForm.DOCTOR_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "none";
                    document.mainForm.DOCTOR_PROFILE_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_PROFILE_CODE').style.display = "none";
                    document.mainForm.DOCTOR_CATEGORY_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').style.display = "none";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display = "none";
                    document.mainForm.ORDER_CATEGORY_CODE.disabled = true;
                    document.getElementById('ORDER_CATEGORY_BUTTON').style.display = "none";
                    document.mainForm.ORDER_ITEM_CODE.disabled = true;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "none";
                    document.mainForm.TRANSACTION_MODULE.disabled = true;
                    document.mainForm.TRANSACTION_TYPE.disabled = true;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = true;
                    document.mainForm.DOCUMENT_TYPE.disabled = true;
                }else{
					document.mainForm.INVOICE_NO.disabled = false;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "inline";
                    document.mainForm.DOCTOR_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_PROFILE_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_PROFILE_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_CATEGORY_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display = "inline";
                    document.mainForm.ORDER_CATEGORY_CODE.disabled = false;
                    document.getElementById('ORDER_CATEGORY_BUTTON').style.display = "inline";
                    document.mainForm.ORDER_ITEM_CODE.disabled = false;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "inline";
                    document.mainForm.TRANSACTION_MODULE.disabled = false;
                    document.mainForm.TRANSACTION_TYPE.disabled = false;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = false;
                    document.mainForm.DOCUMENT_TYPE.disabled = false;
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
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
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
    <body leftmargin="0">
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("checklist_daily_report.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="checklist"/>
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                    </th>
                </tr>
                <tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label>
                    </td>
                    <td colspan="3" class="input">
                        <select class="mediumMax" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" onchange="changeDropDownList();">
                            <option value="None">-- Select Report --</option>
                            <option value="ImportTransaction">${labelMap.REPORT_TRANSACTION}</option>
                            <option value="ImportVerifyTransaction">${labelMap.REPORT_TRANSACTION_RESULT}</option>
                            <option value="SummaryDaily">${labelMap.REPORT_SUMMARY_DAILY}</option>
                            <option value="TransactionNotCalculate">${labelMap.NOT_CALCULATE_TRANSACTION}</option>
                            <option value="NoVerifyTransaction">${labelMap.NO_VERIFY_TRANSACTION}</option>
                            <option value="NotVerifyTransaction">${labelMap.NOT_VERIFY_TRANSACTION}</option>
                            <option value="ImportARTransaction">${labelMap.REPORT_AR_TRANSACTION}</option>
                            <option value="ImportARInTransaction">${labelMap.REPORT_AR_IN_TRANSACTION}</option>
                            <option value="NegativeTransaction">${labelMap.MINUS_TRANSACTION}</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="FROM_DATE">${labelMap.FROM_DATE}</label>
                    </td>
                    <td class="input">
                        <input type="text" id="FROM_DATE" name="FROM_DATE" class="short" readonly="readonly"/>
                    <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('FROM_DATE'); return false;" />                    </td>
                    <td class="label"><label for="TO_DATE">${labelMap.TO_DATE}</label>
                    </td>
                    <td class="input">
                        <input type="text" id="TO_DATE" name="TO_DATE" class="short" readonly="readonly"/>
                    <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('TO_DATE'); return false;" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
                        <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="mediumMax" readonly="readonly" value="" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_PROFILE_CODE">${labelMap.DOCTOR_PROFILE_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_PROFILE_CODE" name="DOCTOR_PROFILE_CODE" class="short" value="" onkeypress="return DOCTOR_PROFILE_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_PROFILE_CODE();" />
                        <input id="SEARCH_DOCTOR_PROFILE_CODE" name="SEARCH_DOCTOR_PROFILE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_PROFILE_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_PROFILE_CODE'); return false;" />
                        <input type="text" id="DOCTOR_PROFILE_NAME" name="DOCTOR_PROFILE_NAME" class="mediumMax" readonly="readonly" value="" />
                    </td>
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
                    <td class="label"><label for="ORDER_CATEGORY_CODE">${labelMap.ORDER_CATEGORY_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input name="ORDER_CATEGORY_CODE" type="text" class="short" id="ORDER_CATEGORY_CODE" maxlength="20" value="" onkeypress="return ORDER_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_CATEGORY_CODE();" />
                        <input type="image" name="ORDER_CATEGORY_BUTTON" id="ORDER_CATEGORY_BUTTON" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM_CATEGORY&DISPLAY_FIELD=DESCRIPTION_THAI&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=ORDER_CATEGORY_CODE&HANDLE=AJAX_Refresh_ORDER_CATEGORY_CODE'); return false;" />
                        <input type="text" id="ORDER_CATEGORY_DESCRIPTION" name="ORDER_CATEGORY_DESCRIPTION" class="mediumMax" readonly="readonly"/>
                    </td>
                </tr>
                
                <tr>
                    <td class="label"><label for="DOCTOR_CATEGORY_CODE">${labelMap.DOCTOR_CATEGORY_CODE}</label></td>
                    <td class="input" colspan="3">
                        <input name="DOCTOR_CATEGORY_CODE" type="text" class="short" id="DOCTOR_CATEGORY_CODE" maxlength="20" value="" onkeypress="return DOCTOR_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_CATEGORY();" />
                        <input id="SEARCH_DOCTOR_CATEGORY_CODE" name="SEARCH_DOCTOR_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_CATEGORY&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DOCTOR_CATEGORY_CODE&HANDLE=AJAX_Refresh_DOCTOR_CATEGORY'); return false;" />
                        <input name="DOCTOR_CATEGORY_DESCRIPTION" type="text" class="mediumMax" id="DOCTOR_CATEGORY_DESCRIPTION" readonly="readonly" value="" />                    
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
                    <td class="label">
                    	<label for="ORDER_ITEM_CODE">${labelMap.INVOICE_NO}</label>
                    </td>
                    <td colspan="3" class="input">
                        <input type="text" id="INVOICE_NO" name="INVOICE_NO" class="medium" maxlength="20" onkeypress="return INVOICE_NO_KeyPress(event);" />
                        <input id="SEARCH_INVOICE_NO" name="SEARCH_INVOICE_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search_invoice.jsp?TABLE=TRN_DAILY&RETURN_FIELD=INVOICE_NO&DISPLAY_FIELD=INVOICE_DATE&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=INVOICE_NO&HANDLE=AJAX_Refresh_INVOICE'); return false;" />                        
                    </td>
                </tr>                
				<tr>
                    <td class="label"><label for="aText">${labelMap.TRANSACTION_MODULE}</label></td>
                    <td colspan="3" class="input">
                        <select class="medium" id="TRANSACTION_MODULE" name="TRANSACTION_MODULE">
                            <option value="%">ALL</option>
                            <option value="TR">HIS. TRANSACTION</option>
                            <option value="AR">ACCOUNT RECEIPT</option>
                            <option value="OW">ONWARD TRANSACTION</option>
                    	</select>
					</td>
                </tr>
                <tr>
                    <td class="label">
                    <label for="aText">${labelMap.TRANSACTION_TYPE}</label>                    </td>
                    <td class="input">
                        <select class="short" id="TRANSACTION_TYPE" name="TRANSACTION_TYPE">
                            <option value="%">-- Select --</option>
                            <option value="%">ALL</option>
                            <option value="INV">INVOICE</option>
                            <option value="REV">RECEIPT</option>
                    </select>                    </td>
                    <td class="label"><label for="ADMISSION_TYPE_CODE">${labelMap.ADMISSION_TYPE_CODE}</label></td>
                    <td class="input">
                        <select class="short" id="ADMISSION_TYPE_CODE" name="ADMISSION_TYPE_CODE">
                            <option value="%">-- Select --</option>
                            <option value="%">ALL</option>
                            <option value="I">IPD</option>
                            <option value="O">OPD</option>
                    </select>					</td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="DOCUMENT_TYPE">${labelMap.DOCUMENT_TYPE}</label><br />
                    </td>
                    <td class="input">
                        <select class="short" id="DOCUMENT_TYPE" name="DOCUMENT_TYPE">
                            <option value="%">-- Select --</option>
                            <option value="N">INVOICE</option>
                            <option value="Y">WRITE OFF</option>
                    </select>					</td>
                    <td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td class="input"><input type="text" class="short" id="SAVE_FILE" name="SAVE_FILE"/>
                        <select id="FILE_TYPE" name="FILE_TYPE" onChange="changeDropDownType();">
                            <option value="">Select</option>
                            <option value="pdf">pdf</option>
							<option value="txt">text</option>
							<option value="xls">xls</option>
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