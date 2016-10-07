<%-- 
    Document   : ProcessSummaryRevenue
    Created on : 16 มี.ค. 2553, 12:34:52
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
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
                  
            ProcessUtil proUtil = new ProcessUtil();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Revenue Calculate", "คำนวณสรุปรายได้แพทย์");
            labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Process", "ขบวนการ");
            labelMap.add("COL_2", "Month", "เดือน");
            labelMap.add("COL_3", "Status", "สถานะ");
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("YYYY", "Year", "ปี");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
            String startDateStr = JDate.showDate(JDate.getDate());
            String endDateStr = JDate.showDate(JDate.getDate());
            
            String[] arr_label = {"Calulate Summary","Summary"};
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
            var TYPE = "";
            function AJAX_Send_Request() {
                var table = document.getElementById("dataTable");
                table.rows[2].cells[2].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
                var target = "../../InterfaceExportGLSrvl?"
                        + "yyyy=" + document.mainForm.yyyy.value
                        + "&mm=" + document.mainForm.mm.value;
                AJAX_Request(target, AJAX_Handle_Response);
            }
            
            function AJAX_Handle_Response() {
                var table = document.getElementById("dataTable");
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0] == null) {
                        alert("Exception in update process");
                        return;
                    }
                    
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue == "Error") {
                        //table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/failed_icon.gif" alt="" />';
                        alert("Failed");
                    }
                    else {
                    	alert("Process Complete");
                    }
                    document.mainForm.runac.disabled = false;
                    table.rows[2].cells[2].innerHTML = '<img src="../../images/succeed_icon.gif" alt="" />';
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
                document.mainForm.RUN.disabled = true;
                document.mainForm.STOP.disabled = false;
                document.mainForm.CLOSE.disabled = true;
                document.getElementById("PROGRESS").innerHTML = (currentRowID - 1) + " / " + numRow;
                AJAX_Send_Request();
            }
            
            function STOP_Click() {
                toRowID = 0;
                document.getElementById("PROGRESS").innerHTML = "";
				//document.mainForm.SELECT.disabled = false;
                document.mainForm.RUN.disabled = false;
                document.mainForm.STOP.disabled = true;
                document.mainForm.CLOSE.disabled = false;                
                // Send Roll back message via AJAX here 
            }
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="ProcessImportBill.jsp">
            <table class="form">
                <tr>
                    <th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				 	</th>
                </tr>
				<tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>					</td>
                    <td class="input"><%=proUtil.selectMM("", "mm",JDate.getMonth())%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("yyyy", JDate.getYear() )%></td>
                </tr>
            </table>
            <hr />
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="7" class="alignLeft">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                        <div style="float: right;" id="PROGRESS" name="PROGRESS"></div>
                    </th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("COL_0")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
                    <td class="sub_head" colspan="2"><%=labelMap.get("COL_3")%></td>
                </tr>
                <tr>
                    <td class="row<%=1 % 2%> alignCenter">1</td>
                    <td class="row<%=1 % 2%> alignLeft"><dd>Accrued</dd></td>
                    <td class="row<%=1 % 2%> alignCenter"><img src="../../images/waiting_icon.gif" alt="" /></td>
                    <td class="row<%=1 % 2%> alignCenter">
                        <input type="button" name="runac" value=" RUN " onclick="AJAX_Send_Request('AC')"/>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
