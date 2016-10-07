<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "Export File", "ส่งออกไฟล์ข้อมูล");
	labelMap.add("REPORT_NAME", "Process Name", "ชื่อรายงาน");
    labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
	labelMap.add("DOCTOR_DEPARTMENT_CODE", "Doctor Department", "รหัสแผนกแพทย์");
	labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รหัสการรักษา");
	labelMap.add("FROM_DATE", "From Date", "ตั้งแต่วันที่");
    labelMap.add("TO_DATE", "To Date", "ถึงวันที่");
    labelMap.add("YEAR", "YEAR", "ปี");
	labelMap.add("MONTH", "MONTH", "เดือน");
    labelMap.add("STATUS", "Active Status", "สถานะใช้งาน");
    labelMap.add("STATUS_1", "Active", "ใช้งาน");
    labelMap.add("STATUS_0", "In Active", "ยกเลิก");
    labelMap.add("CALCULATE_DF", "Calculate DF ???", "CALCULATE_DF ???");
    labelMap.add("CALCULATE_DF_0", "No", "ไม่ใช่");
    labelMap.add("CALCULATE_DF_1", "Yes", "ใช่");
	labelMap.add("REPORT_TRANSACTION", "Import DF Transaction", "นำเข้ารายการค่าแพทย์");
	labelMap.add("REPORT_TRANSACTION_RESULT", "Import Result Transaction", "นำเข้ารายการแพทย์อ่านผล");
	labelMap.add("REPORT_AR_TRANSACTION", "Import AR Transaction", "นำเข้ารายการรับชำระหนี้");
	labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
	labelMap.add("PAY_TYPE", "Revenue Type", "ประเภทรายได้");
	labelMap.add("VIEW", "View", "แสดงผล");
	labelMap.add("JAN", "January", "มกราคม");
	labelMap.add("FEB", "February", "กุมภาพันธ์");
	labelMap.add("MAR", "March", "มีนาคม");
	labelMap.add("APR", "April", "เมษายน");
	labelMap.add("MAY", "May", "พฤษภาคม");
	labelMap.add("JUN", "June", "มิถุนายน");
	labelMap.add("JUL", "July", "กรกฏาคม");
	labelMap.add("AUG", "August", "สิงหาคม");
	labelMap.add("SEP", "September", "กันยายน");
	labelMap.add("OCT", "October", "ตุลาคม");
	labelMap.add("NOV", "November", "พฤศจิกายน");
	labelMap.add("DEC", "December", "ธันวาคม");
	labelMap.add("PAID_DF", "Doctorfee", "ค่าส่วนแบ่งแพทย์");
	labelMap.add("PAID_SALARY", "Salary", "ค่าเงินเดือน");
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
			document.mainForm.REPORT_DISPLAY.value = "view";
        	document.mainForm.submit();
            }
			function Report_Save() {
			document.mainForm.REPORT_DISPLAY.value = "save";
			document.mainForm.submit();
			}
			function changeDropDownList(){
				if(document.mainForm.REPORT_FILE_NAME.value == "%"){
					document.mainForm.DOCTOR_CODE.disabled = false;
					document.getElementById('DOCTOR_BUTTON').style.display = "inline";
					document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = false;
					document.getElementById('DOCTOR_DEPARTMENT_BUTTON').style.display = "inline";
					document.mainForm.ORDER_ITEM_CODE.disabled = false;
					document.getElementById('ORDER_ITEM_BUTTON').style.display = "inline";
					document.mainForm.TRANSACTION_TYPE.disabled = false;
					document.mainForm.ADMISSION_TYPE_CODE.disabled = false;
					document.mainForm.DOCUMENT_TYPE.disabled = false;
				}
				if(document.mainForm.REPORT_FILE_NAME.value == "ImportTransaction"){
					document.mainForm.DOCTOR_CODE.disabled = false;
					document.getElementById('DOCTOR_BUTTON').style.display = "inline";
					document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = false;
					document.getElementById('DOCTOR_DEPARTMENT_BUTTON').style.display = "inline";
					document.mainForm.ORDER_ITEM_CODE.disabled = false;
					document.getElementById('ORDER_ITEM_BUTTON').style.display = "inline";
					document.mainForm.TRANSACTION_TYPE.disabled = false;
					document.mainForm.ADMISSION_TYPE_CODE.disabled = false;
					document.mainForm.DOCUMENT_TYPE.disabled = true;
				}
				if(document.mainForm.REPORT_FILE_NAME.value == "ImportVerifyTransaction"){
					document.mainForm.DOCTOR_CODE.disabled = false;
					document.getElementById('DOCTOR_BUTTON').style.display = "inline";
					document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = true;
                    document.getElementById('DOCTOR_DEPARTMENT_BUTTON').style.display="none";
					document.mainForm.ORDER_ITEM_CODE.disabled = true;
					document.getElementById('ORDER_ITEM_BUTTON').style.display = "none";
					document.mainForm.TRANSACTION_TYPE.disabled = true;
					document.mainForm.ADMISSION_TYPE_CODE.disabled = true;
					document.mainForm.DOCUMENT_TYPE.disabled = true;
				}
				if(document.mainForm.REPORT_FILE_NAME.value == "ImportARTransaction"){
					document.mainForm.DOCTOR_CODE.disabled = true;
                    document.getElementById('DOCTOR_BUTTON').style.display="none";
					document.mainForm.DOCTOR_DEPARTMENT_CODE.disabled = true;
                    document.getElementById('DOCTOR_DEPARTMENT_BUTTON').style.display="none";
					document.mainForm.ORDER_ITEM_CODE.disabled = true;
					document.getElementById('ORDER_ITEM_BUTTON').style.display = "none";
					document.mainForm.TRANSACTION_TYPE.disabled = true;
					document.mainForm.ADMISSION_TYPE_CODE.disabled = true;
					document.mainForm.DOCUMENT_TYPE.disabled = false;
				}
			}
		</script>
    </head>
	<body>
        <form id="mainForm" name="mainForm" method="get" action="../../ExportFileSrvl">
		<table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
				<input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="checklist"/>
                <tr>
                	<th colspan="4">
				  		<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  		<div style="float: right;" id="Language" name="Language">
				  		<a href="../../switch_lang.jsp?lang=<%=LabelMap.LANG_TH%>"><img src="../../images/thai_flag.jpg" width="16" height="11" /></a> | 
				  		<a href="../../switch_lang.jsp?lang=<%=LabelMap.LANG_EN%>"><img src="../../images/eng_flag.jpg" width="16" height="11" /></a></div>
					</th>
                </tr>
				<tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label>
					</td>
                    <td colspan="3" class="input">
					<select class="medium" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" onChange="changeDropDownList();">
                            <option value="%">-- Select Report --</option>
                            <option value="ImportTransaction">${labelMap.REPORT_TRANSACTION}</option>
                            <option value="ImportVerifyTransaction">${labelMap.REPORT_TRANSACTION_RESULT}</option>
                            <option value="ImportARTransaction">${labelMap.REPORT_AR_TRANSACTION}</option>
                    </select>					</td>
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
                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
                    <td colspan="3" class="input"><input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" />
                    <input type="image" name="DOCTOR_BUTTON" id="DOCTOR_BUTTON" class="image_button" src="../../images/search_button.png" alt="" onclick="alert('Under construction')" /></td>
                </tr>
				<tr>
                    <td class="label"><label for="DOCTOR_DEPARTMENT_CODE">${labelMap.DOCTOR_DEPARTMENT_CODE}</label></td>
                    <td colspan="3" class="input"><input type="text" id="DOCTOR_DEPARTMENT_CODE" name="DOCTOR_DEPARTMENT_CODE" class="short" />
                    <input type="image" name="DOCTOR_DEPARTMENT_BUTTON" id="DOCTOR_DEPARTMENT_BUTTON" class="image_button" src="../../images/search_button.png" alt="" onclick="alert('Under construction')" /></td>
                </tr>
				<tr>
                    <td class="label"><label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label></td>
                    <td colspan="3" class="input"><input type="text" id="ORDER_ITEM_CODE" name="ORDER_ITEM_CODE" class="short" />
                    <input type="image" name="ORDER_ITEM_BUTTON" id="ORDER_ITEM_BUTTON" class="image_button" src="../../images/search_button.png" alt="" onclick="alert('Under construction')" /></td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="aText">${labelMap.YEAR}</label>                    </td>
                    <td class="input"><select class="short" id="YYYY" name="YYYY">
                      <option value="%">-- Select --</option>
                      <option value="2005">2005</option>
                      <option value="2006">2006</option>
                      <option value="2007">2007</option>
					  <option value="2008">2008</option>
					  <option value="2009">2009</option>
					  <option value="2010">2010</option>
					  <option value="2011">2011</option>
					  <option value="2012">2012</option>
                    </select></td>
                    <td class="label"><label for="ADMISSION_TYPE_CODE">${labelMap.MONTH}</label></td>
                    <td class="input">
					<select class="short" id="MM" name="MM">
                            <option value="%">-- Select --</option>
                            <option value="01">${labelMap.JAN}</option>
                            <option value="02">${labelMap.FEB}</option>
                            <option value="03">${labelMap.MAR}</option>
							<option value="04">${labelMap.APR}</option>
							<option value="05">${labelMap.MAY}</option>
							<option value="06">${labelMap.JUN}</option>
							<option value="07">${labelMap.JUL}</option>
							<option value="08">${labelMap.AUG}</option>
							<option value="09">${labelMap.SEP}</option>
							<option value="10">${labelMap.OCT}</option>
							<option value="11">${labelMap.NOV}</option>
							<option value="12">${labelMap.DEC}</option>
                    </select>
					</td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="DOCUMENT_TYPE">${labelMap.PAY_TYPE}</label><br />
					</td>
                    <td class="input">
                        <select class="short" id="PAY_TYPE" name="PAY_TYPE">
                            <option value="%">-- Select --</option>
                            <option value="01">${labelMap.PAID_SALARY}</option>
                            <option value="04">${labelMap.PAID_DF}</option>
                        </select>
					</td>
                    <td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td class="input"><input type="text" class="short" id="target_file" name="target_file"/></td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
					<input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Report_Save();" />
                    <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" />
                    <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                  <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location = '../../process_flow_page.jsp'" />				  </th>
                </tr>
          </table>
    </form>
	</body>
</html>
