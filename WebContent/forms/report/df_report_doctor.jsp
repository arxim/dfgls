<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@ include file="../../_global.jsp" %>
<%@page import="df.jsp.Guard"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.report.VerifyAllowViewReportBean"%>

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
            labelMap.add("TITLE_MAIN", "Monthly Payment", "รายงานค่าแพทย์รายเดือน");
            labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
            labelMap.add("DOCTOR_CODE_FROM", "From Doctor Profile Code", "จากแพทย์รหัส");
            labelMap.add("DOCTOR_CODE_TO", "To Doctor Profile Code", "ถึงแพทย์รหัส");
            labelMap.add("DOCTOR_DEPARTMENT_CODE", "Doctor Department", "รหัสแผนกแพทย์");
			labelMap.add("DOCTOR_CATEGORY_CODE", "Doctor Category", "รหัสกลุ่มแพทย์");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รหัสการรักษา");
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
			labelMap.add("REPORT_GUARANTEE_TRANSACTION", "Guarantee Setup", "รายการการันตีแพทย์");
			labelMap.add("REPORT_SUM_DF","Summary Doctorfee","รายงานสรุปรายได้แพทย์");
			labelMap.add("REPORT_DETAIL_DF","Payment Doctor Fee Details","รายงานรายละเอียดรายได้แพทย์");
			labelMap.add("REPORT_PAYMENT_VOUCHER","Payment Voucher","เอกสารการจ่ายเงินแพทย์");
			labelMap.add("REPORT_BANK_TRANSFER","Bank Transfer","รายงานการโอนค่าแพทย์ผ่านธนาคาร");
			labelMap.add("REPORT_PAID_CHEQUE","Summary Cheque Paid","รายงานทำจ่ายเช็คค่าแพทย์");
			labelMap.add("REPORT_EXPENSE","Adjust Revenue Report","รายงานรายการปรับปรุงค่าแพทย์");
			labelMap.add("REPORT_DF_HOLD","DF Hold Report","รายงานรายการระงับจ่ายค่าแพทย์");
			labelMap.add("REPORT_TAX_406","Tax 40(6) Report","หนังสือรับรองรายได้ 40(6)"); 
			labelMap.add("REPORT_TAX_402","Tax 40(2) Report","ใบรับรองภาษี 50 ทวิ");
            labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
            labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร");
            labelMap.add("VIEW", "View", "แสดงผล");
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
			labelMap.add("REPORT_BEHIND_PAYMENT_DETAIL","Unpaid Doctor Fee Details","รายงานรายการค้างจ่ายแพทย์");
            String report = "";
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            DBConnection con;
            con = new DBConnection();
            con.connectToLocal();
            String report_payment_voucher = "PaymentVoucher"+session.getAttribute("HOSPITAL_CODE").toString();
            String report_payment_detail = "SummaryRevenueByDetailForDoctor"+session.getAttribute("HOSPITAL_CODE").toString();
            String report_unpaid = "SummaryDFUnpaidByDetailForDoctor"+session.getAttribute("HOSPITAL_CODE").toString();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
            con.Close();
            String BATCH_DATE = b.getYyyy() + b.getMm();      
           
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
            	window.location.replace("df_report_doctor.jsp?payTerm="+document.mainForm.term.value+"&MM="+document.mainForm.MM.value+"&YYYY="+document.mainForm.YYYY.value);
		        
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
        </script>
    </head>
    <body leftmargin="0">

        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("df_report_doctor.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="df_report"/>
                <input type="hidden" id="PAYMENT_DATE" name="PAYMENT_DATE" value="%"/>
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                    </th>
                </tr>
                <tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label></td>
                    <td class="input">
						<select class="medium" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME">
	                      <option value="None">-- Select Monthly Report --</option>                     
	                      <option value="<%=report_payment_voucher%>">${labelMap.REPORT_PAYMENT_VOUCHER}</option>
	                      <option value="<%=report_payment_detail%>">${labelMap.REPORT_DETAIL_DF}</option>
	                      <option value="ExpenseDetailForDoctor">${labelMap.REPORT_EXPENSE}</option>
			      		  <option value="<%=report_unpaid%>">${labelMap.REPORT_BEHIND_PAYMENT_DETAIL}</option>
	                      <option value="TaxLetter406ForDoctor">${labelMap.REPORT_TAX_406}</option>
						  <option value="Tax402SummaryYearlyForDoctor">${labelMap.REPORT_TAX_402}</option>
	                    </select>
					</td>
                	<td class="label" align="right" width="25%">Payment Term</td>
                    <td class="input" valign="middle" width="25%" align="left">
                   	 	<select class="short" name="term" id="term">
                   	 	  <option value="2">Month End</option>
                   	 	  <option value="1">Half Month</option>
                   	 	</select>
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
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='df_report_doctor.jsp'" />
					</th>
                </tr>
            </table>
            <input type="hidden" id="DOCTOR_CODE_FROM" name="DOCTOR_CODE_FROM" class="short" value="<%=request.getSession().getAttribute("USER_ID").toString()%>"/>
            <input type="hidden" id="DOCTOR_CODE_TO" name="DOCTOR_CODE_TO" class="short" value="<%=request.getSession().getAttribute("USER_ID").toString()%>"/>
        </form>
    </body>
</html>