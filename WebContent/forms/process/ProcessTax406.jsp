<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
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
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
            c.Close();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Tax 406 Process", "คำนวณภาษีแพทย์ 40(6)");
            labelMap.add("START_DATE", "Payment Term From", "เริ่มต้นงวดเดือน");
            labelMap.add("END_DATE", "Payment Term To", "สิ้นสุดงวดเดือน");
            labelMap.add("FIRST_TAX_TERM", "First Term", "รายได้ครึ่งปีแรก");
            labelMap.add("SECOND_TAX_TERM", "Second Term", "รายได้ครึ่งปีหลัง");
            labelMap.add("ALL_TAX_TERM", "Yearly", "รายได้ทั้งปี");
            labelMap.add("MM", "Tax Term Month", "รายได้ทั้งปี");
            labelMap.add("YYYY", "Tax Term Year", "รายได้ทั้งปี");

            labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Doctor Code", "รหัสแพทย์");
            labelMap.add("COL_2", "Doctor Name", "ชื่อแพทย์");
            labelMap.add("COL_3", "Status", "สถานะ");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
            String startDateStr = request.getParameter("START_DATE");
            String endDateStr = request.getParameter("END_DATE");
            
            //
            // Process request
            //
            String mm_str="";
            String yy_str="";
            
            if(request.getParameter("YYYY") !=null){
            	yy_str=request.getParameter("YYYY");
            }else{
            	yy_str=b.getYyyy();
            }
            if(request.getParameter("MM") !=null)
            {
            	mm_str=request.getParameter("MM");
            }
            
            String cond = "1 <> 1";
            if (request.getParameter("START_DATE") != null && request.getParameter("END_DATE") != null) {
                startDateStr = "01/" + request.getParameter("START_DATE");
                endDateStr = "31/" + request.getParameter("END_DATE");
                
                cond = " D.ACTIVE = '1' " +
                       " AND D.HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "'" +
                       " AND T.PAYMENT_DATE >= '" + JDate.saveDate(startDateStr) + "'" +
                       " AND T.PAYMENT_DATE <= '" + JDate.saveDate(endDateStr) + "'";
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
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript">
        <%-- [ AJAX --%>
            function AJAX_Send_Request() {
                if (currentRowID <= toRowID ) {
                    table.rows[currentRowID].cells[3].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
                    var target = "../../ProcessTax406Srvl?"
                            + "USER=<%=session.getAttribute("USER_ID").toString()%>"
                            + "&PWD=" 
                            + "&HOSPITAL_CODE=<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
                            + "&DOCTOR_CODE=" + table.rows[currentRowID].cells[1].innerHTML 
                            + "&START_DATE=" + document.mainForm.START_DATE.value
                            + "&END_DATE=" + document.mainForm.END_DATE.value
                            + "&REC_NO=" + currentRowID
    	                    + "&MM=" + document.mainForm.MM.value
    	                    + "&YYYY=" + document.mainForm.YYYY.value;
                                
                    AJAX_Request(target, AJAX_Handle_Response);
                }
                else {
                	alert("Process Summary Tax 406 Complete");
                    document.mainForm.SELECT.disabled = false;
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

                document.mainForm.SELECT.disabled = true;
                document.mainForm.RUN.disabled = true;
                document.mainForm.STOP.disabled = false;
                document.mainForm.CLOSE.disabled = true;
                document.getElementById("PROGRESS").innerHTML = (currentRowID - 1) + " / " + numRow;
                
                AJAX_Send_Request();
            }
            
            function STOP_Click() {
                toRowID = 0;
                document.getElementById("PROGRESS").innerHTML = "";
                document.mainForm.SELECT.disabled = false;
                document.mainForm.RUN.disabled = false;
                document.mainForm.STOP.disabled = true;
                document.mainForm.CLOSE.disabled = false;
                
                // Send Roll back message via AJAX here 
            }
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="ProcessTax406.jsp">
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
				
				<tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>
					</td>
                    <td class="input">
						<select class="medium" id="MM" name="MM">
	                      <option value="None" <% if(mm_str.equals("None")) { out.println("selected");}%>>-- Select Tax Term --</option>                     
	                      <option value="01" <% if(mm_str.equals("01")) { out.println("selected");}%>>${labelMap.FIRST_TAX_TERM}</option>
	                      <option value="06" <% if(mm_str.equals("06")) { out.println("selected");}%>>${labelMap.SECOND_TAX_TERM}</option>
	                      <option value="12" <% if(mm_str.equals("12")) { out.println("selected");}%>>${labelMap.ALL_TAX_TERM}</option>
	                    </select>
					</td>
                        
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("YYYY", yy_str)%></td>
                </tr>

		<tr>
                    <td class="label">
                        <label for="START_DATE">${labelMap.START_DATE}</label></td>
                      <%--  <input type="text" value="<%=startDateStr%>" id="START_DATE" name="START_DATE" class="short" value="<%=request.getParameter("START_DATE") == null ? "" : request.getParameter("START_DATE")%>" />  --%>
                      <%--  <td class="input"> --%>
                      <td>
                      <select name="START_DATE" class="short" >
                          <%
                            String temp, selected;
                            for (int y =  Integer.parseInt(JDate.getYear())-2; y <=  Integer.parseInt(JDate.getYear()); y++)
                                for (int m = 1; m <= 12; m++)
                                {
                                    temp = m + "/" + y;
                                    if (m < 10) { temp = "0" + temp; }
                                    selected = request.getParameter("START_DATE") != null && request.getParameter("START_DATE").equalsIgnoreCase(temp) ? " selected=\"selected\"" : "";
                          %>    
                              <option value = "<%=temp%>"<%=selected%>><%=temp%></option>
                          <%
                                }
                          %>
                      </select>
                      </td>
                      
                    <td class="label">
                      <label for="END_DATE">${labelMap.END_DATE}</label>
                      <td>
                      <select name="END_DATE"   class="short">
                          <%
                            //String temp, selected;
                            for (int y =  Integer.parseInt(JDate.getYear())-2; y <=  Integer.parseInt(JDate.getYear()); y++)
                                for (int m = 1; m <= 12; m++)
                                {
                                    temp = m + "/" + y;
                                    if (m < 10) { temp = "0" + temp; }
                                    selected = request.getParameter("END_DATE") != null && request.getParameter("END_DATE").equalsIgnoreCase(temp) ? " selected=\"selected\"" : "";
                          %>    
                              <option value = "<%=temp%>"<%=selected%>><%=temp%></option>
                          <%
                                }
                          %>
                      </select>
                      </td>
              </tr>
                
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="SELECT_Click()" />
                        <input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="RUN_Click()" />  <%-- disabled="disabled" --%>
                        <input type="button" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
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
            //con.connectToServer();
//            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
            
            //String sql = "SELECT CODE, NAME_THAI FROM DOCTOR WHERE " + cond + " ORDER BY CODE";
            String sql = "SELECT DISTINCT D.CODE AS CODE, D.NAME_THAI AS NAME_THAI " + 
                         "FROM SUMMARY_PAYMENT T INNER JOIN DOCTOR D ON T.DOCTOR_CODE=D.CODE AND T.HOSPITAL_CODE=D.HOSPITAL_CODE " +
                         "WHERE " + cond + " "+
                         //"AND D.CODE NOT IN (SELECT DOCTOR_CODE FROM SUMMARY_TAX_406 WHERE HOSPITAL_CODE = '00029' AND YYYY+MM = '201206')"+
                         "ORDER BY D.CODE";
            ResultSet rs = con.executeQuery(sql);
            int i = 1;
            while (rs.next()) {
%>
                <tr>
                    <td class="row<%=i % 2%> alignRight"><%=i%></td>
                    <td class="row<%=i % 2%> alignLeft"><%=rs.getString("CODE")%></td>
                    <td class="row<%=i % 2%> alignLeft"><%=rs.getString("NAME_THAI")%></td> 
                    <td class="row<%=i % 2%> alignCenter"><img src="../../images/waiting_icon.gif" alt="" /></td>
                </tr>
<%
                i ++;
            }
            
            if (rs != null) {
                rs.close();
            }
            //con.freeConnection();
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
