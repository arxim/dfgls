<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

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
<%@page import="java.sql.*"%>

<%@ include file="../../_global.jsp"%>

<%

//
//Verify permission
//

       if (!Guard.checkPermission(session, Guard.PAGE_INPUT_GROUP_ORDER_ITEM_CATEGORY)) {
           response.sendRedirect("../message.jsp");
           return;
       }

	//
	// Initial LabelMap
	//

	if (session.getAttribute("LANG_CODE") == null) {
		session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
	}
	String dt = JDate.showDate(JDate.getDate());
	LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "Setup Holiday", "วันหยุดนักขัตฤกษ์");
 	labelMap.add("LABEL_DATE", "Date", "วันหยุดนักขัตฤกษ์");
	labelMap.add("LABEL_DESCRIPTION", "Description", "รายละเอียดวันหยุดนักขัตฤกษ์");
	
	labelMap.add("TITLE_DATA", "Date Details", "วันหยุดนักขัตฤกษ์");
	labelMap.add("LABEL_DELETE", "Delete", "ลบ");

	request.setAttribute("labelMap", labelMap.getHashMap());

	//
	// Process request
	//
	
	byte MODE = DBMgr.MODE_INSERT;
	request.setCharacterEncoding("UTF-8");
	String query = "";

	if (request.getParameter("MODE") != null){
		
		MODE = Byte.parseByte(request.getParameter("MODE"));
		if(MODE == DBMgr.MODE_DELETE) {
			System.out.println("DELETE");
			String getDateTB = request.getParameter("hid_DATE_TB");
			
			String delete_query = "DELETE FROM STP_SPECIAL_HOLIDAY "
					+ "  WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND HOLIDAY = '"+request.getParameter("hid_DATE_TB")+"' ";
			System.out.println(delete_query);
			boolean status = true;
			DBConnection con = new DBConnection();
			con.connectToLocal();
			
			DBConn conDoctor = new DBConn();
			conDoctor.setStatement();
			
			status = con.executeUpdate(delete_query)<0 ? false : true;
			con.Close();
			System.out.println("status : " + status);
			
		} else if(MODE == DBMgr.MODE_INSERT) {
			String insertData="";
			
			String date = request.getParameter("hid_DATE");
			String date_description=request.getParameter("DESCRIPTION");
			
			DBConnection con = new DBConnection();
			con.connectToLocal();
				
			DBConn conDoctor = new DBConn();
			conDoctor.setStatement();
			insertData="INSERT INTO STP_SPECIAL_HOLIDAY (HOSPITAL_CODE,  "
					+" HOLIDAY, HOLIDAY_DESCRIPTION) "
				    +" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"',  "
				    +" '"+date+"', '"+date_description+"') ";
			ResultSet rsInsertData = con.executeQuery(insertData);
			System.out.println("insertData = "+insertData);
			session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/holiday_setup.jsp"));
			response.sendRedirect("../message.jsp");
		}

	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${labelMap.TITLE_MAIN}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />

		<script type="text/javascript" src="../../javascript/ajax.js"></script>
		<script type="text/javascript" src="../../javascript/util.js"></script>
		<script type="text/javascript" src="../../javascript/search_form.js"></script>
		<script type="text/javascript" src="../../javascript/data_table.js"></script>
		<script type="text/javascript" src="../../javascript/calendar.js"></script>
		<link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript">
		function SAVE_CLICK()
		{
			if(document.mainForm.DATE.value == ""){
               	alert("Please input the data");
			}else{
				console.log(toSaveDate(document.mainForm.DATE.value));
				document.mainForm.hid_DATE.value = toSaveDate(document.mainForm.DATE.value);
				document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
				document.mainForm.submit();
				
			}
		}
		function DELETE_HOLIDAY(date) {
			var dl_date = date;
			console.log("value date >> " + dl_date);
			document.mainForm.hid_DATE_TB.value = dl_date;
			if (confirm("Delete date : "+dl_date+" ?")) {
                console.log("Press OK");
                document.mainForm.MODE.value = "<%=DBMgr.MODE_DELETE%>";
                document.mainForm.submit();
                
            }else{
            	console.log("Press Cancle");
                document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
            }
		}

		</script>
</head>
<body>
	<form id="mainForm" name="mainForm" method="post" action="holiday_setup.jsp">
	<input type="hidden" name="MODE" id="MODE" value="<%=MODE%>" />
	
	<center>
		<table width="800" border="0">
			<tr>
				<td align="left"><b><font color='#003399'><%=Utils.getInfoPage("holiday_setup.jsp", labelMap.getFieldLangSuffix(),new DBConnection("" + session.getAttribute("HOSPITAL_CODE")))%></font></b>
				</td>
			</tr>
		</table>
	</center>
	
	<table class="form">
		<tr>
			<th colspan="4">
				<div style="float: left;">${labelMap.TITLE_MAIN}</div>
			</th>
		</tr>
		
		<tr>
			<td class="label">
				<label for="LABEL_DATE">${labelMap.LABEL_DATE}</label>
			</td>
			
			<td colspan="3" class="input"> 
				<input type="text" name="DATE" id="DATE" class="medium" value=""  />
				<input type="hidden" name="hid_DATE" id="hid_DATE" class="medium" value=""  />
				<input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('DATE'); return false;" />
			</td>
		</tr>
		
		<tr>
			<td class="label">
				<label for="LABEL_DESCRIPTION">${labelMap.LABEL_DESCRIPTION}</label>
			</td>
			
			<td colspan="3" class="input"> 
				<input type="text" name="DESCRIPTION" id="DESCRIPTION" class="medium" value=""  />
			</td>
		</tr>


		<tr>
			<th colspan="6" class="buttonBar">
				<input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_CLICK();" />
				<input type="button" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='holiday_setup.jsp'" />
				<input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
			</th>
		</tr>
	</table>
	
	<hr />
	<table class="data">
		<tr>
			<th colspan="6">
				<div style="float: left;">${labelMap.TITLE_DATA}</div>
			</th>
		</tr>
		
		<tr>
			<td class="sub_head">${labelMap.LABEL_DATE}</td>
			<td class="sub_head">${labelMap.LABEL_DESCRIPTION}</td>
			<td class="sub_head">${labelMap.LABEL_DELETE}</td>
		</tr>
		
		<%
				DBConnection con = new DBConnection();
				con.connectToLocal();

				query = " SELECT HOLIDAY, HOLIDAY_DESCRIPTION "
						+ " FROM STP_SPECIAL_HOLIDAY WHERE HOSPITAL_CODE = '"+ session.getAttribute("HOSPITAL_CODE").toString() + "' "
						+ " ORDER BY HOLIDAY";
				
				ResultSet rs = con.executeQuery(query);
				int i = 0;
				String dis_date = "", dis_description = "";

				while (rs.next()) {
					dis_date = rs.getString("HOLIDAY");
					dis_description = rs.getString("HOLIDAY_DESCRIPTION");
			%>
			<tr>
				<td class="row<%=i % 2%>"><%= Util.formatHTMLString(JDate.showDate(rs.getString("HOLIDAY")), true)%></td>
				<td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("HOLIDAY_DESCRIPTION"), true)%></td>
 				<td class="row<%=i % 2%> alignCenter "><input type="button" id="btn_delete" name="btn_delete" class="button" value="${labelMap.LABEL_DELETE}" onclick="DELETE_HOLIDAY('<%=rs.getString("HOLIDAY")%>')"  /></td>
			</tr>
			<%
				i++;
				}
				if (rs != null) {
					rs.close();
				}
				con.Close();
			%>
			<input type="hidden" name="hid_DATE_TB" id="hid_DATE_TB" value="">
	</table>
	
	</form>

</body>
</html>