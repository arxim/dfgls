<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@ include file="../../_global.jsp" %>
<%@page import="df.jsp.Guard"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%    

	if (!Guard.checkPermission(session, Guard.PAGE_INPUT_BANK)) {
	    response.sendRedirect("../message.jsp");
	    return;
	}

    if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
			ProcessUtil proUtil = new ProcessUtil(); 
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "DF Unpaid", "รายงานค่าแพทย์ค้างจ่าย");
            labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
            labelMap.add("DOCTOR_CODE", "Doctor Profile Code", "รหัสแพทย์");
			labelMap.add("DOCTOR_CODE_TO", "To Doctor Code", "ถึงแพทย์รหัส");
            labelMap.add("PAYOR_OFFICE_CODE", "Payor Office Code", "รหัสบริษัทคู่สัญญา");
            labelMap.add("DOCTOR_DEPARTMENT_CODE", "Doctor Department", "รหัสแผนกแพทย์");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รหัสการรักษา");
            labelMap.add("AS_OF_DATE", "As of Date", "วันที่เรียกรายงาน");
            labelMap.add("TO_DATE", "To Date", "ถึงวันที่");
			labelMap.add("FROM_DATE", "From Date", "จากวันที่");
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
			labelMap.add("DOCTOR_CATEGORY_CODE", "Doctor Category", "รหัสกลุ่มแพทย์");
			labelMap.add("REPORT_BEHIND_PAYMENT_SUMMARY","Behind in Payment Summary","รายงานสรุปค่าแพทย์ค้างจ่าย");
			labelMap.add("REPORT_BEHIND_PAYMENT_DETAIL","Behind in Payment Detail","รายงานรายการค่าแพทย์ค้างจ่าย");
			labelMap.add("REPORT_BEHIND_PAYMENT_GL","Accru Detail","รายงานบันทึกบัญชีค่าแพทย์ค้างจ่าย");
			labelMap.add("REPORT_GL_ACCURE_DETAIL","Accru Detail (New)","รายงานค่าแพทย์ค้างจ่าย");
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
					document.mainForm.REPORT_DISPLAY.value = "view";
                    document.mainForm.target = "_blank";
                    document.mainForm.submit();
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
					document.mainForm.REPORT_DISPLAY.value = "save";
                    document.mainForm.target = "_blank";
                    document.mainForm.submit();
                }
            }
			/*
			<option value="None">-- Select Doctorfee Monthly Report --</option>
            <option value="DFHold">${labelMap.REPORT_DF_HOLD}</option>
            <option value="DFUnpaidDetail">${labelMap.REPORT_BEHIND_PAYMENT_DETAIL}</option>
            <option value="DFUnpaidSum">${labelMap.REPORT_BEHIND_PAYMENT_SUMMARY}</option>
			*/
            function changeDropDownList(){
                if(document.mainForm.REPORT_FILE_NAME.value == "None" || document.mainForm.REPORT_FILE_NAME.value == "DFHold" ){
                    document.getElementById('AS_OF_DATE_BUTTON').style.display = "none";
                    document.mainForm.DOCTOR_CODE.disabled = true;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "none";
                }else{
					document.getElementById('AS_OF_DATE_BUTTON').style.display = "inline";
                    document.mainForm.DOCTOR_CODE.disabled = false;
                    document.getElementById('SEARCH_DOCTOR_CODE').style.display = "inline";
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
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR_PROFILE&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "not found";
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
 * Retreive order item information : End
 */        
 
        </script>
    </head>
    <body leftmargin="0">
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("df_monthly_behind_payment.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="df_monthly_behind_payment"/>
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
					</th>
                </tr>
                <tr>
                  <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label>                    </td>
                    <td colspan="3" class="input">
					<select class="mediumMax" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" onchange="changeDropDownList();">
                      <option value="None">-- Select Doctorfee Monthly Report --</option>
                      <option value="SummaryDFUnpaidByDetail">${labelMap.REPORT_BEHIND_PAYMENT_DETAIL}</option>
                      <option value="DFUnpaidSum">${labelMap.REPORT_BEHIND_PAYMENT_SUMMARY}</option>			
                      <!-- <option value="GLAccru">${labelMap.REPORT_BEHIND_PAYMENT_GL}</option> -->
                      <option value="AccruDetails">${labelMap.REPORT_GL_ACCURE_DETAIL}</option>
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
                    <td class="label">
                        <label for="FROM_DATE">${labelMap.AS_OF_DATE}</label>
                    </td>
                    <td colspan="3" class="input">
                        <input type="text" id="AS_OF_DATE" name="AS_OF_DATE" class="short" readonly="readonly"/>
						<input type="image" id="AS_OF_DATE_BUTTON" name="AS_OF_DATE_BUTTON" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('AS_OF_DATE'); return false;" />
					</td>
                </tr>
                <tr>
                    <td class="label"><label for="PAYOR_OFFICE_CODE">${labelMap.PAYOR_OFFICE_CODE}</label></td>
                    <td class="input" colspan="3">
                        <input name="PAYOR_OFFICE_CODE" type="text" class="short" id="PAYOR_OFFICE_CODE" maxlength="20" value="" onkeypress="return PAYOR_OFFICE_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE();" />
                        <input id="SEARCH_PAYOR_OFFICE_CODE" name="SEARCH_PAYOR_OFFICE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_THAI&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PAYOR_OFFICE_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE'); return false;" />
                        <input name="PAYOR_OFFICE_DESCRIPTION" type="text" class="mediumMax" id="PAYOR_OFFICE_DESCRIPTION" readonly="readonly" value="" />                    
                    </td>
                </tr>
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
                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
                        <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="mediumMax" readonly="readonly" value="" />
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
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'"/>
					</th>
                </tr>
            </table>
        </form>
    </body>
</html>
