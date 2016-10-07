<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
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
            labelMap.add("TITLE_MAIN", "Bank Payment", "คำนวณค่าแพทย์ทำจ่ายผ่านธนาคาร");
            labelMap.add("PAY_DATE", "Pay Date", "วันที่จ่ายเงิน");
            labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Year", "ปี");
            labelMap.add("COL_2", "Month", "เดือน");
            labelMap.add("COL_3", "Status", "สถานะ");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
            String startDateStr = JDate.showDate(JDate.getDate());
//            String endDateStr = JDate.showDate(JDate.getDate());
            
            //
            // Process request
            //
            String cond = "1 <> 1";
            cond = " D.ACTIVE = '1' " +
                   " AND D.HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "'";
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
                if (currentRowID <= toRowID ) {
                    table.rows[currentRowID].cells[3].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
                    var target = "../../ProcessBankTMBPaymentMonthlySrvl?"
                            + "USER=<%=session.getAttribute("USER_ID").toString()%>"
                            + "&PWD=" 
                            + "&HOSPITAL_CODE=<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
                            + "&PAY_DATE=" + document.mainForm.PAY_DATE.value
                            + "&REC_NO=" + currentRowID;
                    AJAX_Request(target, AJAX_Handle_Response);
                } else {
                    alert("Prepare Data for Bank Transfer Finish");
                    document.mainForm.RUN.disabled = false;
                    document.mainForm.STOP.disabled = true;
                    document.mainForm.CLOSE.disabled = false;
                }

            }
            
            function AJAX_Handle_Response() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0] == null) {
                        alert("Exception in update process");
                        return;
                    }
                    
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue == "0") {
                        table.rows[currentRowID].cells[3].innerHTML = '<img src="../../images/failed_icon.gif" alt="" />';
/*                        alert("Failed");
                        document.mainForm.RUN.disabled = false;
                        document.mainForm.STOP.disabled = true;
                        document.mainForm.CLOSE.disabled = false;
                        document.getElementById("PROGRESS").innerHTML = "";
                        return;*/
                    }
                    else {
                        table.rows[currentRowID].cells[3].innerHTML = '<img src="../../images/succeed_icon.gif" alt="" />';
                    }

                    document.getElementById("PROGRESS").innerHTML = (currentRowID - 1) + " / " + numRow;
                            
                    currentRowID++;
                    AJAX_Send_Request();
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
                table = document.getElementById("dataTable"); 
                fromRowID = currentRowID = 2;
                toRowID = document.getElementById("dataTable").rows.length - 1;
//                document.mainForm.SELECT.disabled = true;
                document.mainForm.RUN.disabled = true;
                document.mainForm.STOP.disabled = false;
                document.mainForm.CLOSE.disabled = true;
                document.getElementById("PROGRESS").innerHTML = (currentRowID - 1) + " / " + numRow;
                AJAX_Send_Request();
            }
            
            function STOP_Click() {
                toRowID = 0;
                document.getElementById("PROGRESS").innerHTML = "";
//                document.mainForm.SELECT.disabled = false;
                document.mainForm.RUN.disabled = false;
                document.mainForm.STOP.disabled = true;
                document.mainForm.CLOSE.disabled = false;
                
                // Send Roll back message via AJAX here 
            }
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="ProcessBankTMBPaymentMonthly.jsp">
        	<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("ProcessBankTMBPaymentMonthly.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
        
            <table class="form">
                <tr>
                    <th colspan="2">${labelMap.TITLE_MAIN}</th>
                </tr>
		<tr>
                    <td class="label">
                        <label for="PAY_DATE">${labelMap.PAY_DATE}</label></td>
                    <td class="input">
                        <input type="text" value="<%=startDateStr%>" id="PAY_DATE" name="PAY_DATE" class="short" value="<%=request.getParameter("PAY_DATE") == null ? "" : request.getParameter("PAY_DATE")%>" />
                        <input name="image1" type="image" class="image_button" onclick="displayDatePicker('PAY_DATE'); return false;" src="../../images/calendar_button.png" alt="" /></td>

<%--                    
                    <td class="label">
                        <label for="END_DATE">${labelMap.END_DATE}</label></td>
                    <td class="input">
                        <input type="text" value="<%=endDateStr%>" id="END_DATE" name="END_DATE" class="short" value="<%=request.getParameter("END_DATE") == null ? "" : request.getParameter("END_DATE")%>" />
                        <input name="image2" type="image" class="image_button" onclick="displayDatePicker('END_DATE'); return false;" src="../../images/calendar_button.png" alt="" /></td>
 --%>
                </tr>
               
                <tr>
                    <th colspan="3" class="buttonBar">                        
<%--                    <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="SELECT_Click()" />     --%>
                        <input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="RUN_Click()" disabled="disabled" />
                        <input type="button" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
					</th>
                </tr>
            </table>
            <hr />
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="4" class="alignLeft">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                        <div style="float: right;" id="PROGRESS" name="PROGRESS"></div>
                    </th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("COL_0")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_2")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_3")%></td>
                </tr>
                
<%
            DBConnection con = new DBConnection();
            con.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
            
            //String sql = "SELECT CODE, NAME_THAI FROM DOCTOR WHERE " + cond + " ORDER BY CODE";
//            String sql = "SELECT DISTINCT D.CODE AS CODE, D.NAME_THAI AS NAME_THAI" + 
//                            " FROM VW_ALL_SUMMARY_MONTHLY T INNER JOIN DOCTOR D ON T.DOCTOR_CODE=D.CODE AND T.HOSPITAL_CODE=D.HOSPITAL_CODE" +
//                            " WHERE " + cond + " ORDER BY D.CODE";
//            ResultSet rs = con.executeQuery(sql);
            int i = 1;
//            while (rs.next()) {
%>
                <tr>
                    <td class="row<%=i % 2%> alignRight"><%=i%></td>
                    <td class="row<%=i % 2%> alignCenter"><%=b.getYyyy()%></td>
                    <td class="row<%=i % 2%> alignCenter"><%=b.getMm()%></td> 
                    <td class="row<%=i % 2%> alignCenter"><img src="../../images/waiting_icon.gif" alt="" /></td>
                </tr>
<%
                i ++;
//            }
            
//            if (rs != null) {
//                rs.close();
//            }
            con.Close();
%>
<script type="text/javascript">
                    numRow = <%=i - 1%>;
                    document.getElementById("PROGRESS").innerHTML = "0 / " + numRow;
</script>
            </table>
<%
            if (i > 1) {
                out.print("<script type=\"text/javascript\">document.mainForm.RUN.disabled = false;</script>");
            }

%>
        </form>
    </body>
</html>
