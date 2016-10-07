<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_DEMO)) {
                response.sendRedirect("../message.jsp");
                return;
            }

            //
            // Initial LabelMap
            //

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }

            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            String[] mthTh = {"","มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};
            labelMap.add("TITLE_MAIN", "Monthly Close", "ปิดงานรายเดือน");
            labelMap.add("BATCH_MM", "Batch Month", "เดือนที่จะปิดรายการ");
            labelMap.add("BATCH_YYYY", "Batch Year", "ปีที่จะปิดรายการ");
            labelMap.add("BATCH_CLOSE", "Batch Close", "ปิดรายการประจำเดือน");
            DBConnection conn = new DBConnection();
            conn.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString() , conn);
			conn.Close();
            request.setAttribute("labelMap", labelMap.getHashMap());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript">
        <%-- [ AJAX --%>
            function AJAX_Send_Request() {
                var target = "../../ProcessBatchSrvl?"
                    + "&HOSPITAL_CODE=<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
					+ "&USER_ID=<%=session.getAttribute("USER_ID").toString()%>"
					+ "&PAYMENT_DATE=" + document.mainForm.date.value
                    + "&PAYMENT_TERM=" + document.mainForm.term.value
					+ "&MM=<%=b.getBatchNo().substring(4, 6)%>"
                    + "&YYYY=<%=b.getBatchNo().substring(0, 4) %>";
                AJAX_Request(target, AJAX_Handle_Response);
            }

            function AJAX_Handle_Response() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0] == null) {
                        alert("Exception in update process");
                        return;
                    }

                    if (xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue == "SUCCESS") {
                        alert('BACTH CLOSE COMPLETE');
                    }else {
                        alert('BATCH CLOSE FAIL');
                    }
                    //document.getElementById("PROGRESS").innerHTML = (currentRowID - 1) + " / " + numRow;
                    //currentRowID++;
                    //AJAX_Send_Request();
                    document.mainForm.RUN.disabled = false;
                    document.getElementById("divWait").style.display = "none";
                    return;
                }
            }
        <%-- AJAX ] --%>

            function SELECT_Click() {
                document.mainForm.submit();
            }

            var fromRowID = 0;
            var toRowID = 0;
            var numRow = 0;
            var currentRowID = 0;
            var table = document.getElementById("dataTable");

            function RUN_Click() {
                document.mainForm.RUN.disabled = true;
                document.getElementById("divWait").style.display = "block";
                AJAX_Send_Request();
            }

            function STOP_Click() {
                toRowID = 0;
                document.getElementById("PROGRESS").innerHTML = "";
                document.mainForm.RUN.disabled = false;
                document.mainForm.STOP.disabled = true;
                document.mainForm.CLOSE.disabled = false;
            }
            function fncon(){
                document.getElementById("divWait").style.display = "none";
            }
        </script>
    </head>
    <body onload="fncon()">
        <form id="mainForm" name="mainForm" method="post" action="ProcessBatchSrvl">
            <center>
				<table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("ProcessBatch.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                <tr>
                    <td class="label">
                        <label for="PAY_DATE">${labelMap.BATCH_CLOSE}</label></td>
                    <td class="input" colspan="3">
                        <input type="text" class="short" readonly value="<%=mthTh[Integer.parseInt(b.getBatchNo().substring(4, 6))] %>" name="MM"/>
                        <input type="text" class="short" readonly value="<%=b.getBatchNo().substring(0, 4) %>" name="YYYY"/>
                    </td>
                </tr>
                <tr>
                	<td class="label" align="right" width="25%">Payment Term</td>
                    <td class="input" valign="middle" width="25%" align="left">
                   	 	<select class="short" name="term" id="term">
                   	 		 <option value="1">Half Month</option>
                   	 		 <option value="2" selected="selected">Month End</option>
                   	 	</select>
                    </td>
                    <td class="label" align="right" width="25%">Payment Date</td>
                    <td class="input" valign="middle" width="25%" align="left">
                    	<input type="text" id="date" name="date" class="short" value="<%=request.getParameter("START_DATE") == null ? "" : request.getParameter("START_DATE")%>" />
                        <input name="image2" type="image" class="image_button" onclick="displayDatePicker('date'); return false;" src="../../images/calendar_button.png" alt="" />
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="RUN_Click()"/>
                        <input type="hidden" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
            <div id="divWait">
            <table class="form">
                <tr>
                    <td bgcolor="#FFFFFF" align="center"><img src="../../images/wait30trans.gif"></td>
                </tr>
            </table>
            </div>
        </form>
    </body>
</html>
