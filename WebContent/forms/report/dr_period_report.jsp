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
            labelMap.add("TITLE_MAIN", "Doctor Revenue Period", "รายงานค่าแพทย์ตามช่วงเวลา");
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
			labelMap.add("FROM_MM", "From Month", "เริ่มเดือน");
			labelMap.add("FROM_YYYY", "From Year", "เริ่มปี");
			labelMap.add("TO_MM", "To Month", "สิ้นสุดเดือน");
			labelMap.add("TO_YYYY", "To Year", "สิ้นสุดปี");
			labelMap.add("REPORT_GUARANTEE_TRANSACTION", "Guarantee Setup", "รายการการันตีแพทย์");
			labelMap.add("REPORT_SUM_DF","Summary Doctorfee","รายงานสรุปรายได้แพทย์");
			labelMap.add("REPORT_DETAIL_DF","Summary Doctorfee Detail","รายงานรายละเอียดรายได้แพทย์");
			labelMap.add("REPORT_PAYMENT_VOUCHER","Payment Voucher","เอกสารการจ่ายเงินแพทย์");
			labelMap.add("REPORT_BANK_TRANSFER","Bank Transfer","รายงานการโอนค่าแพทย์ผ่านธนาคาร");
			labelMap.add("REPORT_PAID_CHEQUE","Summary Cheque Paid","รายงานทำจ่ายเช็คค่าแพทย์");
			labelMap.add("REPORT_EXPENSE","Expense Report","รายงานรายได้ค่าใช้จ่ายแพทย์");
			labelMap.add("REPORT_DF_HOLD","DF Hold Report","รายงานรายการระงับจ่ายค่าแพทย์");
			labelMap.add("REPORT_DF_PERIOD","Summary Revenue Period","รายงานรายได้แพทย์(ช่วงเวลา)");
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
    </script>
    </head>
    <body leftmargin="0">
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("df_period_report.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="df_monthly"/>
                <input type="hidden" id="DOCTOR_CODE_FROM" name="DOCTOR_CODE_FROM" value="<%=request.getSession().getAttribute("USER_ID").toString()%>"/>
                <input type="hidden" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" value="<%=request.getSession().getAttribute("USER_GROUP_CODE").toString().equals("5")? "SummaryRevenueByDetailPeriodForDoctor":"SummaryRevenueByDetailPeriod" %>"/>
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>                    </th>
                </tr>
				<tr>
                    <td class="label">
                        <label>${labelMap.FROM_MM}</label>					</td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "FROM_MM",b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.FROM_YYYY}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("FROM_YYYY", b.getYyyy())%></td>
                </tr>
                <tr>
                    <td class="label">
                        <label>${labelMap.TO_MM}</label>					</td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "TO_MM",b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.TO_YYYY}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("TO_YYYY", b.getYyyy())%></td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
					</th>
                </tr>
            </table>
        </form>
    </body>
</html>
