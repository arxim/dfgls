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
            labelMap.add("TITLE_MAIN", "Expense Deduct Report", "รายงานค่าใช้จ่ายเพิ่มเติม");
            labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
            labelMap.add("DOCTOR_CODE_FROM", "From Doctor Code", "จากแพทย์รหัส");
            labelMap.add("DOCTOR_CODE_TO", "To Doctor Code", "ถึงแพทย์รหัส");
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
			labelMap.add("SAVE_FILE", "Save as filename", "Save as filename");
			labelMap.add("VIEW_REPORT", "View", "แสดง");
			
			labelMap.add("EXPENSE_SIGN","Expense sign","Expense sign");
			labelMap.add("EXPENSE_ACCOUNT_CODE","Expense account code","Expense account code");
			labelMap.add("EXPENSE_CODE","Expense code","Expense code");
			labelMap.add("EXPENSE_SING_LABEL_ALL","ALL","ทั้งหมด");
			labelMap.add("EXPENSE_SING_LABEL_REVENUE","Revenue","รายได้");
			labelMap.add("EXPENSE_SING_LABEL_EXPENSE","Expense","ค่าใช้จ่าย");
			labelMap.add("REPORT1","Expense Deduct","ค่าใช้จ่ายที่ติดลบ");
			labelMap.add("REPORT2","Expense Distribute","สรุปรวมการกระจาย Expense ที่ติดลบ");
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
                	if(document.mainForm.MM.value == ""){
						alert("Please Select Month");
						document.mainForm.MM.focus();
						return false;
						
					}
					if(document.mainForm.YYYY.value == ""){
						alert("Please Select Year");
						document.mainForm.YYYY.focus();
						return false;
					}
					if(true)
						{
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
					if(document.mainForm.YYYY.value == ""){
						alert("Please Select Year");
						document.mainForm.YYYY.focus();
					}else{
						document.mainForm.REPORT_DISPLAY.value = "save";
                    	document.mainForm.target = "_blank";
                    	document.mainForm.submit();
					}
                }
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
    <body leftmargin="0" >
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("expense_deduct_report.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY" />
                <input type="hidden" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" value="ExpenseDeduct">
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="expense_deduct"/>
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>                    </th>
                </tr>
                <tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>					</td>
                    <td class="input"><%=proUtil.selectMMS(session.getAttribute("LANG_CODE").toString(), "MM",b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
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
                    <td class="label"><label for="EXPENSE_CODE">${labelMap.EXPENSE_CODE}</label></td>
                    <td colspan="3" class="input">
						<input type="text" id="EXPENSE_CODE" name="EXPENSE_CODE" class="short" value="" onkeypress="return EXPENSE_CODE_KeyPress(event);" onblur="AJAX_Refresh_EXPENSE_CODE();" />
                        <input id="SEARCH_EXPENSE_CODE" name="SEARCH_EXPENSE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=EXPENSE&DISPLAY_FIELD=DESCRIPTION&TARGET=EXPENSE_CODE&HANDLE=AJAX_Refresh_EXPENSE_CODE'); return false;" />
                        <input type="text" id="EXPENSE_CODE_DESCRIPTION" name="EXPENSE_CODE_DESCRIPTION" class="mediumMax" readonly="readonly" value="" />                    
                    </td>
                </tr>
                <!-- 
                <tr>
                    <td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td class="input" colspan="3"><input type="text" class="short" id="SAVE_FILE" name="SAVE_FILE"/>
                        <select id="FILE_TYPE" name="FILE_TYPE">
                            <option value="all">all</option>
                            <option value="xls">xls</option>
                            <option value="pdf">pdf</option>
                        </select>                    
                    </td>
                </tr>
                 -->
                <tr>
                    <th colspan="4" class="buttonBar">
                        <!-- <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Report_Save();" /> -->
                        <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW_REPORT}" onclick="Report_View();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
					</th>
                </tr>
            </table>
        </form>
    </body>
</html>

