<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>

<%    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "Error Report", "รายงานรายการผิดพลาด");
	labelMap.add("REPORT_NAME", "Process Name", "ชั้นตอนการทำงาน");
    labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
	labelMap.add("DOCTOR_DEPARTMENT_CODE", "Doctor Department", "รหัสแผนกแพทย์");
	labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รหัสการรักษา");
	labelMap.add("FROM_DATE", "Date Error From", "วันที่ทำงานผิดพลาด");
    labelMap.add("TO_DATE", "Date Error To", "ถึงวันที่ทำงานผิดพลาด");
	labelMap.add("FROM_TIME", "Time Error From", "เวลาแจ้งการทำงานผิดพลาด");
    labelMap.add("TO_TIME", "Time Error To", "เวลาแจ้งการทำงานผิดพลาด");
    labelMap.add("TRANSACTION_TYPE", "Transaction Type", "ประเภทใบแจ้งหนี้");
	labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "แผนกผู้ป่วย");
    labelMap.add("STATUS", "Active Status", "สถานะใช้งาน");
    labelMap.add("STATUS_1", "Active", "ใช้งาน");
    labelMap.add("STATUS_0", "In Active", "ยกเลิก");
    labelMap.add("CALCULATE_DF", "Calculate DF ???", "CALCULATE_DF ???");
    labelMap.add("CALCULATE_DF_0", "No", "ไม่ใช่");
    labelMap.add("CALCULATE_DF_1", "Yes", "ใช่");
	/*
	labelMap.add("REPORT_TEXTFILE_TRANSACTION", "Import DF Textfile", "นำเข้าไฟล์รายการค่าแพทย์");
	labelMap.add("REPORT_TRANSACTION", "Import Bill", "นำเข้ารายการค่าแพทย์");
	labelMap.add("REPORT_DAILY_CALCULATE", "Daily Calculate", "การคำนวณรายวัน");
	labelMap.add("REPORT_AR_RECEIPT", "Import Receipt", "รายการรับชำระ");
	labelMap.add("REPORT_DOCTOR_RECEIPT", "Doctor Receipt", "รายการรับชำระเงื่อนไขแพทย์");
	labelMap.add("REPORT_SUMMARY_MONTHLY", "Monthly Calculate", "การคำนวณรายเดือน");
	labelMap.add("REPORT_ROLLBACK", "Rollback", "ถอนรายการ");
	*/
	labelMap.add("REPORT_PROCESS_IMPORT_ONWARD", "Interface DF Onward","นำเข้ารายการค่าแพทย์ onward");
	labelMap.add("REPORT_PROCESS_IMPORT_BILL", "Interface DF Transaction","นำเข้ารายการรักษา");	
	labelMap.add("REPORT_PROCESS_INTERFACE_VERIFY","Interface DF Result","นำเข้ารายการอ่านผล");
	labelMap.add("REPORT_PROCESS_INTERFACE_RECEIPT","Interface AR Receipt","นำเข้ารายการรับชำระหนี้");
	labelMap.add("REPORT_PROCESS_INTERFACE_TIME_TABLE","Interface Time Table","นำเข้ารายการตารางเวลาการันตี");
	labelMap.add("REPORT_PROCESS_INTERFACE_TIME","Guarantee Process","การคำนวณการันตี");
	labelMap.add("REPORT_PROCESS_INTERFACE_CO","Import C/O","นำเข้ารายการค่าใช้จ่าย");
	labelMap.add("REPORT_PROCESS_ROLLBACK","Rollback","ถอนรายการ");
    labelMap.add("REPORT_PROCESS_DAILY", "Daily Calculate","คำนวณรายวัน");
    labelMap.add("REPORT_PROCESS_IMPORT_EXCEL", "Import Other Expense", "นำเข้าค่าใช้จ่ายอื่นๆ");
    
    labelMap.add("REPORT_PROCESS_BANK_TMB_PAYMENT_MONTHLY","Export to bank","");
    labelMap.add("REPORT_PROCESS_SUMMARY_MONTHLY","Monthly Calculate","");
    labelMap.add("REPORT_PROCESS_PAYMENT_MONTHLY","DF Payment","");
    labelMap.add("REPORT_PROCESS_SALARY_PAYMENT","Salary Payment","");
    labelMap.add("REPORT_PROCESS_RECEIPT_BY_AR","AR Receipt Process","รับชำระค่าแพทย์ค้างจ่าย");
    labelMap.add("REPORT_PROCESS_RECEIPT_BY_DOCTOR","Receipt By Doctor","");
    labelMap.add("REPORT_PROCESS_RECEIPT_BY_PAYOR","Receipt By Payor","");
    labelMap.add("REPORT_PROCESS_BANK_PAYMENT_MONTHLY_402","Bank Payment Monthly 4(2)","");
    labelMap.add("REPORT_PROCESS_COMPUTE_TAX_406","Compute tax 40(6)","");

    //static public String PROCESS_BANK_TMB_PAYMENT_MONTHLY = "Export to bank";
    //static public String PROCESS_PAYMENT_MONTHLY = "DF Payment";
    //static public String PROCESS_SALARY_PAYMENT = "Salary Payment";
    //static public String PROCESS_RECEIPT_BY_PAYOR = "Receipt By Payor";
    //static public String PROCESS_BANK_PAYMENT_MONTHLY_402 = "Bank Payment Monthly 402";
	
	labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
	labelMap.add("USER_ID", "User ID", "รหัสผู้ใช้งาน");
	labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร");
	labelMap.add("VIEW", "View", "แสดงผล");
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
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
		<script type="text/javascript">
            function Report_View() {
				if(document.mainForm.PROCESS_NAME.value == "None"){
                    alert("Please Select Process");
                    document.mainForm.PROCESS_NAME.focus();
                }else{
					if(document.mainForm.FROM_DATE.value == "" || document.mainForm.TO_DATE.value == ""){
						alert("Please Select From Date/To Date");
					}else{
						document.mainForm.REPORT_DISPLAY.value = "view";
                    	document.mainForm.target = "_blank";
                    	document.mainForm.submit();
					}
                }
            }
			function Report_Save() {
			document.mainForm.REPORT_DISPLAY.value = "save";
			document.mainForm.submit();
			}
		</script>
    </head>
	<body>
        
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
        <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("error_report.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
        </center>
        
		<table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
				<input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="error_log"/>
				<input type="hidden" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" value="ErrorLog" />
                <tr>
				<th colspan="4">
				  <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  </th>
                </tr>
				<tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label>					</td>
                    <td colspan="3" class="input">
					<select class="long" id="PROCESS_NAME" name="PROCESS_NAME" onChange="changeDropDownList();">
                            <option value="None">-- Select Report --</option>
							<option value="InterfaceTransaction">${labelMap.REPORT_PROCESS_IMPORT_BILL}</option>
							<option value="InterfaceVerifyResult">${labelMap.REPORT_PROCESS_INTERFACE_VERIFY}</option>
							<option value="InterfaceArReceipt">${labelMap.REPORT_PROCESS_INTERFACE_RECEIPT}</option>
							<option value="onward">${labelMap.REPORT_PROCESS_IMPORT_ONWARD}</option>
							<option value="ImportTimeTable">${labelMap.REPORT_PROCESS_INTERFACE_TIME_TABLE}</option>
							<option value="ImportReceipt">${labelMap.REPORT_PROCESS_RECEIPT_BY_AR}</option>
							<option value="ImportExcelExpense">${labelMap.REPORT_PROCESS_IMPORT_EXCEL}</option>
							<option value="GuaranteeProcess">${labelMap.REPORT_PROCESS_INTERFACE_TIME}</option>
							<!-- 
							<option value="Import Guarantee">${labelMap.REPORT_PROCESS_INTERFACE_TIME}</option>
							<option value="Import Expense">${labelMap.REPORT_PROCESS_INTERFACE_CO}</option>
							<option value="Rollback">${labelMap.REPORT_PROCESS_ROLLBACK}</option>
							<option value="Daily Calculate">${labelMap.REPORT_PROCESS_DAILY}</option>
							<option value="Export to bank">${labelMap.REPORT_PROCESS_BANK_TMB_PAYMENT_MONTHLY}</option>
							<option value="Monthly Calculate">${labelMap.REPORT_PROCESS_SUMMARY_MONTHLY}</option>
							<option value="DF Payment">${labelMap.REPORT_PROCESS_PAYMENT_MONTHLY}</option>
							<option value="Salary Payment">${labelMap.REPORT_PROCESS_SALARY_PAYMENT}</option>
							<option value="Import Receipt">${labelMap.REPORT_PROCESS_RECEIPT_BY_AR}</option>
							<option value="Receipt By Doctor">${labelMap.REPORT_PROCESS_RECEIPT_BY_DOCTOR}</option>
							<option value="Receipt By Payor">${labelMap.REPORT_PROCESS_RECEIPT_BY_PAYOR}</option>
							<option value="Bank Payment Monthly 4(2)">${labelMap.REPORT_PROCESS_BANK_PAYMENT_MONTHLY_402}</option>
							<option value="Compute tax 40(6)">${labelMap.REPORT_PROCESS_COMPUTE_TAX_406}</option>
							 -->
                    </select>
					</td>
				</tr>
				<tr>
                    <td class="label">
                        <label for="FROM_DATE">${labelMap.FROM_DATE}</label>					</td>
                    <td class="input">
                        <input type="text" id="FROM_DATE" name="FROM_DATE" class="short" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('FROM_DATE'); return false;" />                    </td>
                    <td class="label"><label for="TO_DATE">${labelMap.TO_DATE}</label>					</td>
                    <td class="input">
                        <input type="text" id="TO_DATE" name="TO_DATE" class="short" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('TO_DATE'); return false;" />                    </td>
                </tr>
				<tr>
                    <td class="label"><label for="FROM_TIME">${labelMap.FROM_TIME}</label></td>
                    <td class="input"><input type="text" id="FROM_TIME" name="FROM_TIME" class="short" /> 
                    hhmmss </td>
                    <td class="label"><label for="TO_TIME">${labelMap.TO_TIME}</label></td>
                    <td class="input"><input type="text" id="TO_TIME" name="TO_TIME" class="short" /> hhmmss</td>
                </tr>
                <tr>
                    <td class="label"><label for="USER_ID">${labelMap.USER_ID}</label></td>
                    <td colspan="3" class="input"><input type="text" class="short" id="USER_ID" name="USER_ID"/>
                    </td>
                </tr>
                <!-- 
                <tr>
                    <td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td class="input"><input type="text" class="medium" id="SAVE_FILE" name="SAVE_FILE"/>
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
					<input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Report_Save();" />
                    <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" />
                    <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                  <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />				  </th>
                </tr>
          </table>
    </form>
	</body>
</html>