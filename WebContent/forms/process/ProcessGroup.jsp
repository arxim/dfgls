<%-- 
    Document   : ProcessImportBill
    Created on : 30 ต.ค. 2551, 13:04:52
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.table.Batch"%>

<%@include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_PREPARE_GUARANTEE)) {
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
            labelMap.add("TITLE_MAIN", "Revenue Share", "คำนวณส่วนแบ่งของแพทย์ตามกลุ่มเฉพาะ");
            labelMap.add("LABEL_SHOWDATE", "Month/Year", "เดือน/ปี");
            labelMap.add("LABEL_GROUP", "Group Code", "รหัสกลุ่ม");
            labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Process", "ขบวนการ");
            labelMap.add("COL_2", "Month", "เดือน");
            labelMap.add("COL_3", "Status", "สถานะ");
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            DBConnection con;
            con = new DBConnection();
            con.connectToLocal();
            //con.connectToServer();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
            con.Close();
            //con.freeConnection();
            String BATCH_DATE = b.getYyyy() + b.getMm() + "00";
            
            String startDateStr = JDate.showDate(JDate.getDate());
            String endDateStr = JDate.showDate(JDate.getDate());
			String currentMonth = JDate.getDate().substring(0, 4);
			
			String defaultMM=JDate.getMonth();
			String defaultYYYY=JDate.getYear();
			String showDate=b.getMm()+"/"+b.getYyyy();
			
			request.setCharacterEncoding("UTF-8");
			ProcessUtil util = new ProcessUtil();
            DataRecord groupRec = null;
            //String mm = request.getParameter("MM") != null ? request.getParameter("MM") : JDate.getMonth();
            //String yyyy = request.getParameter("YYYY") != null ? request.getParameter("YYYY") : JDate.getYear();
            
            if (request.getParameter("GROUP_CODE") != null) {
            	groupRec = DBMgr.getRecord("SELECT * FROM STP_GROUP WHERE CODE = '" + request.getParameter("GROUP_CODE") + "'");
            }
            

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
        <%-- [ AJAX --%>
            function AJAX_Send_Request() {
                if (currentRowID <= toRowID ) {
                    table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
                    var target = "../../ProcessGroupSrvl?"
                            + "&HOSPITAL_CODE="+"<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
                            + "&MM=" + document.mainForm.MM.value
                            + "&YYYY=" + document.mainForm.YYYY.value
                            + "&GROUP_CODE=" + document.mainForm.GROUP_CODE.value
                            + "&TYPE=" + table.rows[currentRowID].cells[1].innerHTML;
                    //alert(currentRowID+" : "+toRowID+" : "+target);
                    AJAX_Request(target, AJAX_Handle_Response);
                }
                else {
                    alert("Calculate Revenue Share Finish");
//                    document.mainForm.SELECT.disabled = false;
                    document.mainForm.RUN.disabled = false;
                    document.mainForm.STOP.disabled = true;
                    document.mainForm.CLOSE.disabled = false;
                }

            }
            
            function AJAX_Handle_Response() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0] == null) {
                        //alert("Exception in update process");
                        return;
                    }
                    
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue == "false") {
                        table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/failed_icon.gif" alt="" />';
                    }
                    else {
                        table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/succeed_icon.gif" alt="" />';
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
            
			//RUN_Click
            function RUN_Click() {
                //table high = 4 : first table(top index) = 0
                table = document.getElementById("dataTable"); 
                fromRowID = currentRowID = 2;
                toRowID = document.getElementById("dataTable").rows.length - 1;
		//alert(table+":"+document.getElementById("dataTable").rows.length);
                //document.mainForm.SELECT.disabled = true;
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
			 function GROUP_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.GROUP_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_GROUP() {
                var target = "../../RetrieveData?TABLE=STP_GROUP&COND=CODE='" + document.mainForm.GROUP_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_GROUP);
            }
            
            function AJAX_Handle_Refresh_GROUP() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.GROUP_CODE.value = "";
                        document.mainForm.GROUP_NAME.value = "";
                        document.mainForm.RUN.disabled=true;
                        return;
                    }
                    
                    // Data found
                    document.mainForm.GROUP_NAME.value = getXMLNodeValue(xmlDoc, "GROUP_NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                    document.mainForm.RUN.disabled=false;
                }
            }     
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="">
        <input type="hidden" name="MM" id="MM" value="<%=b.getMm() %>">
        <input type="hidden" name="YYYY" id="YYYY" value="<%=b.getYyyy() %>">
        
            <table class="form">
                <tr>
                    <th colspan="2">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>				 	</th>
                </tr>
				<tr>
				  <td class="label"><label>${labelMap.LABEL_SHOWDATE}</label></td>
				  <td class="input"><%=showDate%></td>
			  </tr>
				<tr>
                    <td class="label">
                        <label>${labelMap.LABEL_GROUP}</label>					</td>
                    <td class="input"><input type="text" id="GROUP_CODE" name="GROUP_CODE" class="short" value="<%= DBMgr.getRecordValue(groupRec, "CODE") %>" onkeypress="return GROUP_CODE_KeyPress(event);" onblur="AJAX_Refresh_GROUP();" />
                      <input id="SEARCH_GROUP_CODE" name="SEARCH_GROUP_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=STP_GROUP&TARGET=GROUP_CODE&DISPLAY_FIELD=GROUP_NAME_ENG&BEACTIVE=1&HANDLE=AJAX_Refresh_GROUP');	 return false;" />
                      <input type="text" id="GROUP_NAME" name="GROUP_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(groupRec, "GROUP_NAME_" + labelMap.getFieldLangSuffix()) %>" /></td>
                </tr>
                <tr>
                    <th colspan="2" class="buttonBar">                        
                        <input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="RUN_Click();" disabled="disabled" />
                        <input type="button" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />					</th>
                </tr>
            </table>
            <hr />
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="3" class="alignLeft">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                        <div style="float: right;" id="PROGRESS" name="PROGRESS"></div>                    </th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("COL_0")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
<%--                    <td class="sub_head"><%=labelMap.get("COL_2")%></td>    --%>
                    <td class="sub_head"><%=labelMap.get("COL_3")%></td>
                </tr>
                
<%
            int i = 3; //SHOW IN TITLE TABLE SUCH AS 0/3
%>
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><%="1"%></td>
                    <td class="row<%=i % 2%> alignCenter"><%="List Doctor Category"%></td>
                    <td class="row<%=i % 2%> alignCenter"><img src="../../images/waiting_icon.gif" alt="" /></td>
                </tr>
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><%="2"%></td>
                    <td class="row<%=i % 2%> alignCenter"><%="Calculate Revenue"%></td>
                    <td class="row<%=i % 2%> alignCenter"><img src="../../images/waiting_icon.gif" alt="" /></td>
                </tr>
				<tr>
                    <td class="row<%=i % 2%> alignCenter"><%="3"%></td>
                    <td class="row<%=i % 2%> alignCenter"><%="Revenue Share Prorate Doctor"%></td>
                    <td class="row<%=i % 2%> alignCenter"><img src="../../images/waiting_icon.gif" alt="" /></td>
                </tr>
<%
                i ++;
%>
<script type="text/javascript">
                    numRow = <%=i - 1%>;
                    document.getElementById("PROGRESS").innerHTML = "0 / " + numRow;
</script>
            </table>
<%
            if (i > 1) {
                //out.print("<script type=\"text/javascript\">document.mainForm.RUN.disabled = false;</script>");
            }

%>
        </form>
    </body>
</html>
