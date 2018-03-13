<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>

<%    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    ProcessUtil proUtil = new ProcessUtil();
    DBConnection c = new DBConnection();
    c.connectToLocal();
    Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
    c.Close();
    String startDateStr = JDate.showDate(JDate.getDate());
    String endDateStr = JDate.showDate(JDate.getDate());

    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "Interface Export", "ส่งออกไฟล์ข้อมูล");
	labelMap.add("PROCESS_NAME", "Export Module", "จัดทำไฟล์ข้อมูลระบบ");
    labelMap.add("YEAR", "Year", "ปี");
	labelMap.add("MONTH", "Month", "เดือน");
	labelMap.add("EXPORT_RD", "Export Tax Flatfile", "นำส่งรายการนำส่งภาษีสรรพากร");
	labelMap.add("EXPORT_AP", "Export AP Transaction", "นำส่งรายได้แพทย์เพื่อออกเช็ค");
	labelMap.add("EXPORT_BANK", "Export Bank Transaction", "นำส่งรายได้แพทย์โอนเข้าธนาคาร");
	labelMap.add("EXPORT_PAYROLL", "Export Payroll Transaction", "นำส่งข้อมูลเงินเดือนแพทย์");
    labelMap.add("EXPORT_GL", "Export GL Transaction", "นำส่งข้อมูล GL");
    labelMap.add("EXPORT_GLSAP", "Export SAP GL", "นำส่งข้อมูล GL ระบบ SAP");
    labelMap.add("EXPORT_GLSAPR2C", "Export SAP GL (R2C)", "นำส่งข้อมูล GL ระบบ SAP (R2C)");
    labelMap.add("EXPORT_AC", "Export Accu Transaction", "นำส่งข้อมูล Accu");
    labelMap.add("EXPORT_ACSAP", "Export SAP Accrue", "นำส่งข้อมูล Accu ระบบ SAP");
	labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
	labelMap.add("PAY_TYPE", "Revenue Type", "ประเภทรายได้");
	labelMap.add("BANK_TYPE", "Transfer to Bank", "โอนเข้าบัญชีธนาคาร");
	labelMap.add("TMB", "TMB Bank", "ธนาคารทหารไทย");
	labelMap.add("BAY", "BAY Bank", "ธนาคารกรุงศรีอยุธยา");
	labelMap.add("UOB", "UOB Bank", "ธนาคารยูโอบี");
	labelMap.add("CITI", "Citi Bank", "ธนาคารซิตี้แบงก์");
	labelMap.add("SCB_DIRECT", "SCB Bank Direct", "ธนาคารไทยพาณิชย์ (Direct)");
	labelMap.add("SMART", "BAY Smart", "BAY Smart");
	labelMap.add("OTHER", "TMB Bank to Non TMB", "ธนาคารอื่นๆ โอนผ่านทหารไทย");
	labelMap.add("VIEW", "View", "แสดงผล");
	labelMap.add("PAID_DF", "Doctorfee", "ค่าส่วนแบ่งแพทย์");
	labelMap.add("PAID_SALARY", "Salary", "ค่าเงินเดือน");
	labelMap.add("RD1A", "ภ.ง.ด.1ก","ภ.ง.ด.1ก");
	labelMap.add("RD1", "ภ.ง.ด.1","ภ.ง.ด.1");
	labelMap.add("PAYMENT_DATE", "Payment Date", "วันที่ทำจ่าย");
	labelMap.add("TRANSACTION_DATE", "Transaction Date", "วันที่ทำรายการ");
	labelMap.add("FILING_TYPE", "Filing Type", "ประเภทการยื่นแบบ");
	labelMap.add("FILING_TYPE_0", "Regular Filing", "ยื่นปกติ");
	labelMap.add("FILING_TYPE_1", "Additional Filing", "ยื่นเพิ่มเติมครั้งที่");
	String report = "";
    request.setAttribute("labelMap", labelMap.getHashMap());
    
    String BankTransferDate = JDate.showDate(JDate.getDate());
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
			function Report_Save() {
            	if(document.mainForm.PROCESS_NAME.value == ""){
					alert("Please Select Process");
            	}else if((document.mainForm.PROCESS_NAME.value == "ExportBank" || 
                    	document.mainForm.PROCESS_NAME.value == "ExportRD") && 
                    	document.mainForm.PAY_TYPE.value == "%"){
					alert("Please Select Revenue Type");
            	}else if(document.mainForm.target_file.value == ""){
            		alert("Please Enter Filename");
            	}else if(document.mainForm.PROCESS_NAME.value == "ExportRD" && (document.mainForm.PAY_TYPE.value != 'R01' && document.mainForm.PAY_TYPE.value != 'R00')){
                	alert("Revenue Type is Mismatch");
            	}else{
                    document.mainForm.target = "_blank";
        			document.mainForm.submit();
            	}
			}
			function changeDropDownList(){
				var radios = document.mainForm.FILING_TYPE;
				//alert(radios.length);
				if(document.mainForm.PROCESS_NAME.value == "ExportBank"){
					document.mainForm.PAY_TYPE.disabled = false;
					document.mainForm.TRANSACTION_DATE.disabled = false;
					document.mainForm.BANK_TYPE.disabled = false;
					document.mainForm.FILING_TYPE.disabled = true;
					for (var i=0, iLen=radios.length; i<iLen; i++) {
						radios[i].disabled = true;
					} 
				}else if(document.mainForm.PROCESS_NAME.value == "ExportRD"){
					document.mainForm.PAY_TYPE.disabled = false;
					document.mainForm.TRANSACTION_DATE.disabled = true;
					document.mainForm.BANK_TYPE.disabled = true;
					for (var i=0, iLen=radios.length; i<iLen; i++) {
						radios[i].disabled = false;
					} 
				}else{
					document.mainForm.PAY_TYPE.disabled = true;
					document.mainForm.BANK_TYPE.disabled = true;
					document.mainForm.TRANSACTION_DATE.disabled = false;
					document.mainForm.FILING_TYPE.disabled = true;
					for (var i=0, iLen=radios.length; i<iLen; i++) {
						radios[i].disabled = true;
					} 
				}
			}
			function checkText(){ 
				var elem = document.getElementById('target_file').value; 
				if(elem !="" && !elem.match(/^([a-z0-9\_])+$/i)){ 
					alert("ชื่อไฟล์ กรอกได้เฉพาะ a-Z, A-Z, 0-9 และ _ (underscore)"); 
					document.getElementById('target_file').value = ""; 
					document.getElementById('target_file').focus(); 
				}
			}
		</script>
    </head>
	<body>
        <form id="mainForm" name="mainForm" method="get" action="../../ExportFileSrvl">
        <center>
			<table width="800" border="0">
				<tr><td align="left">
				<b><font color='#003399'><%=Utils.getInfoPage("interface_export.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
				</td></tr>
			</table>
        </center>
        
		<table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
				<input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="checklist"/>
                <tr>
                	<th colspan="4"><div style="float: left;">${labelMap.TITLE_MAIN}</div></th>
                </tr>
				<tr>
                    <td class="label"><label for="PROCESS_NAME">${labelMap.PROCESS_NAME}</label>
					</td>
                    <td colspan="" class="input">
					<select class="medium" id="PROCESS_NAME" name="PROCESS_NAME" onChange="changeDropDownList();">
                            <option value="">-- Select Report --</option>
                            <!-- <option value="ExportAP">${labelMap.EXPORT_AP}</option> -->
                            <option value="ExportBank">${labelMap.EXPORT_BANK}</option>
                            <!-- <option value="ExportPayroll">${labelMap.EXPORT_PAYROLL}</option> -->
                            <option value="ExportGL">${labelMap.EXPORT_GL}</option>
                            <option value="ExportAC">${labelMap.EXPORT_AC}</option>
                            <option value="ExportGLSAP">${labelMap.EXPORT_GLSAP}</option>
                            <option value="ExportGLSAPR2C">${labelMap.EXPORT_GLSAPR2C}</option>
                            <option value="ExportACSAP">${labelMap.EXPORT_ACSAP}</option>
                            <option value="ExportRD">${labelMap.EXPORT_RD}</option>
                    </select>
					</td>
					<td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td class="input"><input type="text" class="medium" id="target_file" name="target_file" onblur="checkText();"/></td>
				</tr>
				<tr>
                    <td class="label">
                        <label for="TRANSACTION_DATE">${labelMap.TRANSACTION_DATE}</label></td>
                    <td class="input">
                        <input type="text" value="<%=startDateStr%>" id="TRANSACTION_DATE" name="TRANSACTION_DATE" class="short" value="<%=request.getParameter("TRANSACTION_DATE") == null ? "" : request.getParameter("TRANSACTION_DATE")%>" />
                        <input name="image1" type="image" class="image_button" onclick="displayDatePicker('TRANSACTION_DATE'); return false;" src="../../images/calendar_button.png" alt="" /></td>
                    <td class="label">
                        <label for="PAYMENT_DATE">${labelMap.PAYMENT_DATE}</label></td>
                    <td class="input">
                        <input type="text" value="<%=endDateStr%>" id="PAYMENT_DATE" name="PAYMENT_DATE" class="short" value="<%=request.getParameter("PAYMENT_DATE") == null ? "" : request.getParameter("PAYMENT_DATE")%>" />
                        <input name="image2" type="image" class="image_button" onclick="displayDatePicker('PAYMENT_DATE'); return false;" src="../../images/calendar_button.png" alt="" /></td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="MM">${labelMap.MONTH}</label>
                    </td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
                    <td class="label">
                         <label for="YYYY">${labelMap.YEAR}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
                </tr>                
                <tr>
                    <td class="label">
                        <label for="DOCUMENT_TYPE">${labelMap.PAY_TYPE}</label><br />
					</td>
                    <td class="input">
                        <select class="medium" id="PAY_TYPE" name="PAY_TYPE">
                            <option value="%">-- Select Revenue Type --</option>
                            <option value="01">${labelMap.PAID_SALARY}</option>
                            <option value="04">${labelMap.PAID_DF}</option>
                            <option value="%">-- Select Tax Type --</option>
                            <option value="R00">${labelMap.RD1A}</option>
                            <option value="R01">${labelMap.RD1}</option>
                        </select>
					</td>
					<td class="label">
                        <label for="BANK_TYPE">${labelMap.BANK_TYPE}</label><br />
					</td>
                    <td class="input">
                        <select class="medium" id="BANK_TYPE" name="BANK_TYPE">
                            <option value="011">${labelMap.TMB}</option>
                            <option value="0011">${labelMap.OTHER}</option>
                            <option value="025">${labelMap.BAY}</option>
                            <option value="0025">${labelMap.SMART}</option>
                            <option value="014">${labelMap.SCB_DIRECT}</option>
                            <option value="017">${labelMap.CITI}</option>
                        </select>
					</td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="FILING_TYPE">${labelMap.FILING_TYPE}</label><br />
					</td>
                    <td colspan="3" class="input" >
                        <input onclick="document.getElementById('filing_add_no').disabled = true;" type="radio" id="1" name="FILING_TYPE" value="0" checked="checked"/>
                        <label for="FILING_TYPE_0">${labelMap.FILING_TYPE_0}</label>
                        <input onclick="document.getElementById('filing_add_no').disabled = false;" type="radio" id="2" name="FILING_TYPE" value="1" />
                        <label for="FILING_TYPE_1">${labelMap.FILING_TYPE_1}</label>
                        <input type="text"  style="width: 20px;" id="filing_add_no" name="filing_add_no" disabled="disabled"/>
                    </td>
                </tr>
                <!-- 
                <tbody id="idFormDate">
	                <tr>
	                    <td class="label">
	                        <label for="PAYMENT_DATE">${labelMap.FROM_DATE}</label>
	                    </td>
	                    <td class="input" colspan='3'>
	                        <input type="text" id="PAYMENT_DATE" name="PAYMENT_DATE" value="<%=BankTransferDate %>" class="short" readonly="readonly"/>
	                    	<input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('PAYMENT_DATE'); return false;" />                    
	                    </td>
	                </tr>
                </tbody>
                 -->              
                <tr>
                    <th colspan="4" class="buttonBar">
					<input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Report_Save();" />
                    <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                  <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />				  </th>
                </tr>
          </table>
    </form>
	</body>
</html>