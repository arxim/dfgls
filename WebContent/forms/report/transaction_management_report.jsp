<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@ include file="../../_global.jsp" %>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>

<%    
	ProcessUtil proUtil = new ProcessUtil();
	DBConnection c = new DBConnection();
	c.connectToLocal();
	Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
	c.Close();
    if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Management Transaction", "รายงานจัดการข้อมูลใบแจ้งหนี้");
            labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
            labelMap.add("INVOICE_NO", "Invoice No.", "เลขที่ใบแจ้งหนี้");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("YYYY", "Year", "ปี");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รหัสการรักษา");
            labelMap.add("PAYOR_OFFICE_CODE", "Payor Office Code", "รหัสบริษัทคู่สัญญา");
            
            labelMap.add("CHPROCESS_INACTIVE", "Inactive", "การ Inactive");
            labelMap.add("CHPROCESS_DISCOUNT", "Discount", "การทำส่วนลด");
            labelMap.add("CHPROCESS_CHANGE", "Change DR.Code", "การเปลี่ยนแปลงรหัสแพทย์");
			labelMap.add("CHPROCESS_DIS_CHG", "Discount & Change DR.Code", "การทำส่วนลดและการเปลี่ยนแปลงรหัสแพทย์");
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
					if(document.mainForm.TYPE_REPORT.value == ""){
						alert("Please Select Month / Year / Type Report");
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
						document.mainForm.REPORT_DISPLAY.value = "save";
                    	document.mainForm.target = "_blank";
                    	document.mainForm.submit();
					}
                }
            }
			function changeDropDownType(){
				if(document.mainForm.REPORT_FILE_NAME.value == "None" || document.mainForm.REPORT_FILE_NAME.value == "SummaryDaily" || document.mainForm.REPORT_FILE_NAME.value == "ImportTransaction"){
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
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display = "inline";
                    document.mainForm.ORDER_ITEM_CODE.disabled = false;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "inline";
                    document.mainForm.TRANSACTION_TYPE.disabled = false;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = false;
                    document.mainForm.DOCUMENT_TYPE.disabled = false;
                }
                if(document.mainForm.REPORT_FILE_NAME.value == "ImportTransaction"){
					document.mainForm.INVOICE_NO.value = "";
					document.mainForm.INVOICE_NO.disabled = true;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "none";
                    document.mainForm.DOCTOR_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display = "inline";
                    document.mainForm.ORDER_ITEM_CODE.disabled = false;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "inline";
                    document.mainForm.TRANSACTION_TYPE.disabled = false;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = false;
                    document.mainForm.DOCUMENT_TYPE.disabled = true;
                }
                if(document.mainForm.REPORT_FILE_NAME.value == "ImportVerifyTransaction"){
					document.mainForm.INVOICE_NO.value = "";
					document.mainForm.INVOICE_NO.disabled = true;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "none";
                    document.mainForm.DOCTOR_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "inline";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display="none";
                    document.mainForm.ORDER_ITEM_CODE.disabled = true;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "none";
                    document.mainForm.TRANSACTION_TYPE.disabled = true;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = true;
                    document.mainForm.DOCUMENT_TYPE.disabled = true;
                }
                if(document.mainForm.REPORT_FILE_NAME.value == "ImportARTransaction"){
					document.mainForm.INVOICE_NO.value = "";
					document.mainForm.INVOICE_NO.disabled = true;
					document.getElementById('SEARCH_INVOICE_NO').style.display = "none";
                    document.mainForm.DOCTOR_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display="none";
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_DEPARTMENT_CODE').style.display="none";
                    document.mainForm.ORDER_ITEM_CODE.disabled = true;
                    document.getElementById('ORDER_ITEM_BUTTON').style.display = "none";
                    document.mainForm.TRANSACTION_TYPE.disabled = true;
                    document.mainForm.ADMISSION_TYPE_CODE.disabled = true;
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
                var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.PAYOR_OFFICE_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE);
            }
            
            function AJAX_Handle_Refresh_PAYOR_OFFICE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.PAYOR_OFFICE_CODE.value = "";
                        document.mainForm.PAYOR_OFFICE_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PAYOR_OFFICE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            function HN_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.HN_NO.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_HN_NO() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=HN_NO='" + document.mainForm.HN_NO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_HN_NO);
            }
            
            function AJAX_Handle_Refresh_HN_NO() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "HN_NO")) {
                        document.mainForm.HN_NO.value = "";
                        document.mainForm.PATIENT_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PATIENT_NAME.value = getXMLNodeValue(xmlDoc, "PATIENT_NAME");
                }
            }
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
		<b><font color='#003399'><%=Utils.getInfoPage("transaction_management_report.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="transaction"/>
                <input type="hidden" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" value="TransactionEdit">
                
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>                    </th>
                </tr>
                <tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label>                    </td>
                    <td colspan="3" class="input">
                        <select class="mediumMax" id="TYPE_REPORT" name="TYPE_REPORT" >
                            <option value="None">-- Select Type Report --</option>
                            <option value="100">${labelMap.CHPROCESS_INACTIVE}</option>
                            <option value="010">${labelMap.CHPROCESS_DISCOUNT}</option>
                            <option value="001">${labelMap.CHPROCESS_CHANGE}</option>
							<option value="011">${labelMap.CHPROCESS_DIS_CHG}</option>
                       </select>                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>					</td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>					</td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
                </tr>    
                <tr>
                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
                        <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="mediumMax" readonly="readonly" value="" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="PAYOR_OFFICE_CODE">${labelMap.PAYOR_OFFICE_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="PAYOR_OFFICE_CODE" name="PAYOR_OFFICE_CODE" class="short" value="" onkeypress="return PAYOR_OFFICE_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE_CODE();" />
                        <input id="SEARCH_PAYOR_OFFICE_CODE" name="SEARCH_PAYOR_OFFICE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=PAYOR_OFFICE_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_PAYOR_OFFICE_CODE'); return false;" />
                        <input type="text" id="PAYOR_OFFICE_NAME" name="PAYOR_OFFICE_NAME" class="mediumMax" readonly="readonly" value="" />                    </td>
                </tr>
               <tr>
                    <td class="label"><label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input name="ORDER_ITEM_CODE" type="text" class="short" id="ORDER_ITEM_CODE" maxlength="20" value="" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM();" />
                        <input type="image" name="ORDER_ITEM_BUTTON" id="ORDER_ITEM_BUTTON" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_THAI&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=ORDER_ITEM_CODE&HANDLE=AJAX_Refresh_ORDER_ITEM'); return false;" />
                        <input type="text" id="ORDER_ITEM_DESCRIPTION" name="ORDER_ITEM_DESCRIPTION" class="mediumMax" readonly="readonly"/>                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="HN_NO">${labelMap.HN_NO}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="HN_NO" name="HN_NO" class="short" onkeypress="return HN_NO_KeyPress(event);" onblur="AJAX_Refresh_HN_NO();" />
                        <input id="SEARCH_HN_NO" name="SEARCH_HN_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=TRN_DAILY&RETURN_FIELD=HN_NO&DISPLAY_FIELD=PATIENT_NAME&BEINSIDEHOSPITAL=1&TARGET=HN_NO&HANDLE=AJAX_Refresh_HN_NO'); return false;" />
                        <input type="text" id="PATIENT_NAME" name="PATIENT_NAME" class="long" readonly="readonly" />                    </td>
                </tr>
                <tr>
                    <td class="label">
                    	<label for="ORDER_ITEM_CODE">${labelMap.INVOICE_NO}</label>                    </td>
                    <td colspan="3" class="input">
                        <input type="text" id="INVOICE_NO" name="INVOICE_NO" class="medium" maxlength="20" onkeypress="return INVOICE_NO_KeyPress(event);" />
                        <input id="SEARCH_INVOICE_NO" name="SEARCH_INVOICE_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search_invoice.jsp?TABLE=TRN_DAILY&RETURN_FIELD=INVOICE_NO&DISPLAY_FIELD=INVOICE_DATE&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=INVOICE_NO&HANDLE=AJAX_Refresh_INVOICE'); return false;" />                    </td>
                </tr> 
                               
				 <tr>
                    <td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td colspan="3" class="input"><input type="text" class="short" id="SAVE_FILE" name="SAVE_FILE"/>
                        <select id="FILE_TYPE" name="FILE_TYPE" onChange="changeDropDownType();">
                            <option value="">Select</option>
                            <option value="pdf">pdf</option>
							<option value="txt">text</option>
							<option value="xls">xls</option>
                    </select>                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Report_Save();" />
                        <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>