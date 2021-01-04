<%-- 
    Document   : ProcessImportBill
    Created on : 30 ต.ค. 2551, 13:04:52
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
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
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
            c.Close();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Export GL", "นำส่งไฟล์ GL");
            labelMap.add("START_DATE", "Start Date", "วันที่เริ่มต้น");
            labelMap.add("END_DATE", "End Date", "วันที่สิ้นสุด");
            labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Process", "ขบวนการ");
            labelMap.add("COL_2", "Month", "เดือน");
            labelMap.add("COL_3", "Status", "สถานะ");
            labelMap.add("MM", "Month / Year", "เดือน / ปี");
            labelMap.add("YYYY", "Year", "ปี");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
            String startDateStr = JDate.showDate(JDate.getDate());
            String endDateStr = JDate.showDate(JDate.getDate());
            
            String[] arr_label = {"Accrued","GL Account"};
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
            function AJAX_Send_Request(type) {
                var table = document.getElementById("dataTable");
                TYPE = type;
                document.mainForm.runac.disabled = true;
                document.mainForm.rungl.disabled = true;
                if(type=='AC'){
                    table.rows[2].cells[2].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
                }else{
                    table.rows[3].cells[2].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
                }
                var target = "../../InterfaceExportGLWMCSrvl?"
                        + "TYPE=" + type
                        + "&TERM=" + document.mainForm.term.value
                        + "&yyyy=" + document.mainForm.yyyy.value
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
                        //alert("Success : " + xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue);
                        //table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/succeed_icon.gif" alt="" />';
                    }
                    document.mainForm.runac.disabled = false;
                    document.mainForm.rungl.disabled = false;
                    if(TYPE=='AC'){
                        table.rows[2].cells[2].innerHTML = '<img src="../../images/succeed_icon.gif" alt="" />';
                    }else{
                        table.rows[3].cells[2].innerHTML = '<img src="../../images/succeed_icon.gif" alt="" />';
                    }
                    //document.getElementById("PROGRESS").innerHTML = (currentRowID - 1) + " / " + numRow;
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
        <form id="mainForm" name="mainForm" method="post" action="ProcessExportGLWMC.jsp">
        	<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("ProcessExportGLWMC.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
        
            <table class="form">
                <tr>
                    <th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				 	</th>
                </tr>
                <tr align="center">
                    <td class="label">
                        <label>${labelMap.MM}</label>
                    </td>
                    <td class="input"><%=proUtil.selectMonth("mm", b.getMm())%> / <%=proUtil.selectYear("yyyy", b.getYyyy())%>
                    </td>
                	<td class="label" align="right" width="25%">Payment Term</td>
                    <td class="input" valign="middle" width="25%" align="left">
                   	 	<select class="short" name="term" id="term">
                   	 		 <option value="1">Half Month</option>
                   	 		 <option value="2" selected="selected">Month End</option>
                   	 	</select>
                    </td>
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
                    <td class="row<%=1 % 2%> alignLeft"><dd>Accrued</td>
                    <td class="row<%=1 % 2%> alignCenter"><img src="../../images/waiting_icon.gif" alt="" /></td>
                    <td class="row<%=1 % 2%> alignCenter">
                        <input type="button" name="runac" value=" RUN " onclick="AJAX_Send_Request('AC')"/>
                    </td>
                </tr>
                <tr>
                    <td class="row<%=2 % 2%> alignCenter">2</td>
                    <td class="row<%=2 % 2%> alignLeft">
                        <dd>GL Account
                    </td>
                    <td class="row<%=2 % 2%> alignCenter">
                        <img src="../../images/waiting_icon.gif" alt="" />
                    </td>
                    <td class="row<%=2 % 2%> alignCenter">
                        <input type="button" name="rungl" value=" RUN " onclick="AJAX_Send_Request('GL')"/>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
