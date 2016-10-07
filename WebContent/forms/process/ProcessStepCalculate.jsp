<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>
<%@page import="java.sql.*"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@ include file="../../_global.jsp"%>

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
	labelMap.add("TITLE_MAIN", "Calculate Step Process","Calculate Step Process");
	labelMap.add("MM", "Month", "Month");
	labelMap.add("YYYY", "Year", "Year");
	labelMap.add("COL_0", "No.", "ลำดับ");
	labelMap.add("COL_1", "Doctor Code", "รหัสแพทย์");
	labelMap.add("COL_2", "Doctor Name", "ชื่อแพทย์");
	labelMap.add("COL_4", "Category", "กลุ่ม");
	labelMap.add("COL_3", "Status", "สถานะ");

	request.setAttribute("labelMap", labelMap.getHashMap());

	String startDateStr = request.getParameter("START_DATE");
	String endDateStr = request.getParameter("END_DATE");

	//
	// Process request
	//
	String mm_str = "";
	String yy_str = "";

	if (request.getParameter("YYYY") != null) {
		yy_str = request.getParameter("YYYY");
	} else {
		yy_str = b.getYyyy();
	}
	if (request.getParameter("MM") != null) {
		mm_str = request.getParameter("MM");
	}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${labelMap.TITLE_MAIN}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
<script type="text/javascript" src="../../javascript/util.js"></script>
<script type="text/javascript" src="../../javascript/ajax.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
<script type="text/javascript" src="../../javascript/calendar.js"></script>
<script type="text/javascript">
        <%-- [ AJAX --%>
            function AJAX_Send_Request() {
                if (currentRowID <= toRowID ) {
                    table.rows[currentRowID].cells[4].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
                    var target = "../../ProcessStepSrvl?"
                    + "USER=<%=session.getAttribute("USER_ID").toString()%>"
                    + "&PWD=" 
                    + "&HOSPITAL_CODE=<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
					+ "&DOCTOR_CODE="
					+ table.rows[currentRowID].cells[1].innerHTML
					+ "&DOCTOR_CATEGORY_CODE="
					+ table.rows[currentRowID].cells[3].innerHTML
					+ "&REC_NO=" + currentRowID + "&MM="
					+ document.mainForm.MM.value + "&YYYY="
					+ document.mainForm.YYYY.value;
                            
                   console.log(" URL :" + target);

			AJAX_Request(target, AJAX_Handle_Response);
		} else {
			alert("Process Calculate Step Complete");
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
				table.rows[currentRowID].cells[4].innerHTML = '<img src="../../images/failed_icon.gif" alt="" />';
			} else {
				table.rows[currentRowID].cells[4].innerHTML = '<img src="../../images/succeed_icon.gif" alt="" />';
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
	}
</script>
</head>
<body>
	<form id="mainForm" name="mainForm" method="post" action="ProcessStepCalculate.jsp">
		<table class="form">
			<tr>
				<th colspan="4">${labelMap.TITLE_MAIN}</th>
			</tr>

			<tr>
				<td class="label"><label>${labelMap.MM}</label></td>
				<td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>

				<td class="label"><label>${labelMap.YYYY}</label></td>
				<td class="input"><%=proUtil.selectYY("YYYY", yy_str)%></td>
			</tr>

			<tr>
				<th colspan="4" class="buttonBar">
					<input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="SELECT_Click()" /> 
					<input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="RUN_Click()" /> 
					<input type="button" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" /> 
					<input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
				</th>
			</tr>
		</table>
		<hr />
		<table class="data" id="dataTable" name="dataTable">
			<tr>
				<th colspan="5" class="alignLeft">
					<div style="float: left;">${labelMap.TITLE_MAIN}</div>
					<div style="float: right;" id="PROGRESS" name="PROGRESS"></div>
				</th>
			</tr>
			<tr>
				<td class="sub_head"><%=labelMap.get("COL_0")%></td>
				<td class="sub_head"><%=labelMap.get("COL_1")%></td>
				<td class="sub_head"><%=labelMap.get("COL_2")%></td>
				<td class="sub_head"><%=labelMap.get("COL_4")%></td>
				<td class="sub_head"><%=labelMap.get("COL_3")%></td>
			</tr>

			<%
				DBConnection con = new DBConnection();
					con.connectToLocal();

					//String sql = "SELECT CODE, NAME_THAI FROM DOCTOR WHERE HOSPITAL_CODE = '"+ session.getAttribute("HOSPITAL_CODE").toString()+"' AND DOCTOR_CATEGORY_CODE IN ( "
					//		 + "SELECT DISTINCT STEP_ID FROM STP_METHOD_ALLOC_STEP WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString()+ "')";

					String sql = 
					"SELECT DISTINCT DOCTOR_CATEGORY_CODE, DOCTOR_PROFILE_CODE AS CODE, DOCTOR_PROFILE.NAME_THAI "+
					"FROM DOCTOR LEFT JOIN DOCTOR_PROFILE ON DOCTOR.HOSPITAL_CODE = DOCTOR_PROFILE.HOSPITAL_CODE AND "+
					"DOCTOR.DOCTOR_PROFILE_CODE = DOCTOR_PROFILE.CODE "+
					"WHERE DOCTOR_PROFILE.HOSPITAL_CODE = '"+ session.getAttribute("HOSPITAL_CODE").toString()+"' "+ 
					"AND DOCTOR_CATEGORY_CODE IN ( "+
					"SELECT DISTINCT STEP_ID FROM STP_METHOD_ALLOC_STEP WHERE HOSPITAL_CODE = '"+ session.getAttribute("HOSPITAL_CODE").toString()+"')";
					
					ResultSet rs = con.executeQuery(sql);
					int i = 1;
					while (rs.next()) {
			%>
			<tr>
				<td class="row<%=i % 2%> alignRight"><%=i%></td>
				<td class="row<%=i % 2%> alignLeft"><%=rs.getString("CODE")%></td>
				<td class="row<%=i % 2%> alignLeft"><%=rs.getString("NAME_THAI")%></td>
				<td class="row<%=i % 2%> alignLeft"><%=rs.getString("DOCTOR_CATEGORY_CODE")%></td>
				<td class="row<%=i % 2%> alignCenter"><img
					src="../../images/waiting_icon.gif" alt="" /></td>
			</tr>
			<%
				i++;
					}

					if (rs != null) {
						rs.close();
					}
					con.Close();
			%>
			<script type="text/javascript">
				var numRow = <%=i - 1%>;
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