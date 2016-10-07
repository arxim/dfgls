<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@ include file="../../_global.jsp" %>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>

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
            labelMap.add("TITLE_MAIN", "Monthly Checklist", "รายงานตรวจสอบรายเดือน");
            labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
            labelMap.add("DOCTOR_CODE_FROM", "From Doctor Profile Code", "จากแพทย์รหัส");
            labelMap.add("DOCTOR_CODE_TO", "To Doctor Profile Code", "ถึงแพทย์รหัส");
			labelMap.add("MM", "Month", "เดือน");
			labelMap.add("YYYY", "Year", "ปี");
			labelMap.add("REPORT_MONTHLY_CHECKLIST", "Summary DF Monthly Detail", "รายงานรายละเอียดทำจ่ายเฉพาะค่าแพทย์");
			labelMap.add("REPORT_MONTHLY_RECEIPT", "Summary DF Monthly Receipt", "รายงานรายละเอียดปลดจ่ายค่าแพทย์");
			labelMap.add("REPORT_MONTHLY_PATAIL", "Patail Detail Check List", "รายงานชำระบางส่วน");
			labelMap.add("REPORT_GUARANTEE_TRANSACTION", "Guarantee Setup", "รายการกำหนดการันตีแพทย์");
			labelMap.add("REPORT_GUARANTEE_CHECKLIST","Guarantee Checklist","รายงานตรวจสอบการการันตี");
			labelMap.add("REPORT_NO_INCOME","No Income Checklist","รายงานตรวจสอบรายได้ไม่พอหัก");
			labelMap.add("REPORT_GL_CHECKLIST","GL Checklist","รายงานยอดรวมส่ง GL");
			labelMap.add("REPORT_DOCTOR_REVENUE","Summary Doctor Revenue","รายงานสรุปรายได้แพทย์");
			labelMap.add("REPORT_DOCTOR_REVENUE_GUARANTEE","Summary Doctor Revenue Guarantee","รายงานสรุปรายได้แพทย์การันตี");
			labelMap.add("REPORT_DOCTORPROFILE_REVENUE_GUARANTEE","Summary Doctor Profile Revenue Guarantee","รายงานสรุปรายได้แพทย์การันตีรายแพทย์");
			labelMap.add("REPORT_BANK_TRANSFER","Bank Transfer","รายงานการโอนค่าแพทย์ผ่านธนาคาร");
			labelMap.add("REPORT_MONTHLY_YEARLY","Monthly to Yearly","รายงานตรวจสอบการันตีรายเดือนคิดเป็นรายปี");
			labelMap.add("GL_TYPE", "GL Type", "ประเภทการลงบัญชี");
			labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
            labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร");
            labelMap.add("VIEW", "View", "แสดงผล");
            labelMap.add("MESSAGE", "Please Select Report Module", "กรุณาเลือกรายงาน");
            labelMap.add("EXPENSE_PERIOD","Setup Advance Adjustment","บันทึกค่าใช้จ่ายล่วงหน้า");
			labelMap.add("REPORT_EXPENSE_DEDUCT","Expense Distribute","รายงานการกระจายค่าใช้จ่าย");
			labelMap.add("DEPARTMENT" , "Department" , "Department");
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
            function changeDropDownList(){
                if(document.mainForm.REPORT_FILE_NAME.value == "None" || document.mainForm.REPORT_FILE_NAME.value == "PaymentVoucher" || 
				document.mainForm.REPORT_FILE_NAME.value == "DetailDoctorfeeRevenue" || document.mainForm.REPORT_FILE_NAME.value == "ExpensePeriod"){
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
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE_FROM.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_FROM);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_FROM() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CODE_FROM.value = "";
                        document.mainForm.DOCTOR_NAME_FROM.value = "";
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
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE_TO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_TO);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_TO() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CODE_TO.value = "";
                        document.mainForm.DOCTOR_NAME_TO.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME_TO.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
            
        //  ----------------------------------------------------------- GUARANTEE DEPARTMENT --------------------------------------------------
            function GUARANTEE_DEPARTMENT_CODE_KeyPress(e) {
             var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

             if (key == 13) {
                 document.mainForm.GUARANTEE_DEPARTMENT_CODE.blur();
                 return false;
             } else {
                 return true;
             }
         }

         function AJAX_Refresh_GUARANTEE_DEPARTMENT() {
             var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.GUARANTEE_DEPARTMENT_CODE.value +"'"
             + " AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
             
             AJAX_Request(target, AJAX_Handle_Refresh_GUARANTEE_DEPARTMENT);
         }
         
         function AJAX_Handle_Refresh_GUARANTEE_DEPARTMENT() {
             if (AJAX_IsComplete()) {
                 var xmlDoc = AJAX.responseXML;
                 
                 if (!isXMLNodeExist(xmlDoc, "CODE")) {
                     //alert("AJAX_Handle_Refresh_GUARANTEE_LOCATION");
                     document.mainForm.GUARANTEE_DEPARTMENT_CODE.value = "";
                     document.mainForm.GUARANTEE_DEPARTMENT_DESCRIPTION.value = "";
                     return;
                 }
                 
                 // Data found
                 document.mainForm.GUARANTEE_DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
             }
         }    
                        
/********************************************************************************************************************
 * Retreive doctor information : End
 */
        </script>
    </head>
    <body leftmargin="0">
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("df_monthly_checklist_report.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="df_monthly_checklist"/>
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>                    </th>
                </tr>
                <tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label>                    </td>
                    <td colspan="3" class="input">
					<select class="mediumMax" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" onchange="changeDropDownList();">
                        <option value="None">-- Select Monthly Report --</option>
                        <option value="SummaryMonthlyDetailChecklist">${labelMap.REPORT_MONTHLY_CHECKLIST}</option>
                        <option value="SummaryMonthlyDetailReceipt">${labelMap.REPORT_MONTHLY_RECEIPT}</option>
                        <option value="PatailDetailsCheckList">${labelMap.REPORT_MONTHLY_PATAIL}</option>
                        <option value="GuaranteeSetup">${labelMap.REPORT_GUARANTEE_TRANSACTION}</option>
						<option value="CheckSumGuarantee">${labelMap.REPORT_GUARANTEE_CHECKLIST}</option>
						<option value="SummaryGL">${labelMap.REPORT_GL_CHECKLIST}</option>
                      	<option value="ExpensePeriod">${labelMap.EXPENSE_PERIOD}</option>
                        <option value="BankPayment">${labelMap.REPORT_BANK_TRANSFER}</option>
                        <option value="ExpenseDeductMain">${labelMap.REPORT_EXPENSE_DEDUCT}</option>
                        <option value="NoIncomeChecklist">${labelMap.REPORT_NO_INCOME}</option>
                        <option value="MonthlyToYearly">${labelMap.REPORT_MONTHLY_YEARLY}</option>
                    </select>
					</td>
                </tr>
				<tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>
                    </td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
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
                    <td class="label">
                        <label for="GUARANTEE_DEPARTMENT">${labelMap.DEPARTMENT}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="GUARANTEE_DEPARTMENT_CODE" type="text" class="short" id="GUARANTEE_DEPARTMENT_CODE" onblur="AJAX_Refresh_GUARANTEE_DEPARTMENT();" onkeypress="return GUARANTEE_DEPARTMENT_CODE_KeyPress(event);" value="" maxlength="20" />
                        <input id="SEARCH_GUARANTEE_DEPARTMENT_CODE" name="SEARCH_GUARANTEE_DEPARTMENT_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&TARGET=GUARANTEE_DEPARTMENT_CODE&DISPLAY_FIELD=DESCRIPTION&BEACTIVE=1&HANDLE=AJAX_Refresh_GUARANTEE_DEPARTMENT'); return false;" />
                        <input type="text" id="GUARANTEE_DEPARTMENT_DESCRIPTION" name="GUARANTEE_DEPARTMENT_DESCRIPTION" class="mediumMax" readonly="readonly" value="" />
                   </td>
                </tr>
                
                <tr>
                    <td class="label"><label for="GL_TYPE">${labelMap.GL_TYPE}</label></td>
                    <td class="input" colspan="3">
                        <select id="GL_TYPE" name="GL_TYPE">
                            <option value="GL">GL</option>
                            <option value="AC">ACCU</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td class="input" colspan="3"><input type="text" class="short" id="SAVE_FILE" name="SAVE_FILE"/>
                        <select id="FILE_TYPE" name="FILE_TYPE">
                            <option value="">Select</option>
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
