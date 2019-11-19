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
	// Initial LabelMap
	//

	if (session.getAttribute("LANG_CODE") == null) {
		session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
	}
	String dt = JDate.showDate(JDate.getDate());
	LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "Group Sharing Revenue", "กลุ่มการกระจายรายได้แพทย์");
	labelMap.add("LABEL_GROUP_CODE", "Group Code", "รหัสกลุ่ม");
	labelMap.add("LABEL_TYPE", "Type", "ประเภทรายได้");

	labelMap.add("TITLE_DATA", "Group Detail", "รายละเอียดของกลุ่ม");

	labelMap.add("LABEL_DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
	labelMap.add("LABEL_DOCTOR_NAME", "Doctor Name", "ชื่อคณะบุคคล");
	labelMap.add("LABEL_GROUP", "Group", "กลุ่ม");
	labelMap.add("LABEL_PERCENT", "%", "%");
	labelMap.add("LABEL_RATIO", "Ratio", "อัตราส่วน");
	labelMap.add("LABEL_AMOUNT", "Amount", "จำนวนเงิน");
	labelMap.add("LABEL_DELETE", "Delete", "ลบ");

	request.setAttribute("labelMap", labelMap.getHashMap());

/* 	System.out.print("GROUP_CODE = " + request.getParameter("GROUP_CODE"));
	System.out.println(" , TYPE = " + request.getParameter("TYPE")); */

	//
	// Process request
	//
	
	byte MODE = DBMgr.MODE_INSERT;
	request.setCharacterEncoding("UTF-8");
	DataRecord GroupRec = null;
	String query = "", query_dis = "";
	String select_type = "", group_code = "";
	
	// TODO
	if (request.getParameter("MODE") != null) 
	 {
       MODE = Byte.parseByte(request.getParameter("MODE"));
       	 if(MODE == DBMgr.MODE_DELETE) {
    	    System.out.println("DELETE");
    	    String getGroupCode = request.getParameter("GROUP_CODE");
			String getDoctorCode = request.getParameter("hid_DOCTOR_CODE_TB");
			String getType = request.getParameter("TYPE");
			System.out.println("getGroupCode >> " + getGroupCode);
			System.out.println("getDoctorCode >> " + getDoctorCode);
			System.out.println("getType >> " + getType);
			
			String delete_query = "DELETE FROM STP_SHARING_REVENUE "
						+ " WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND DOCTOR_CODE = '"+request.getParameter("hid_DOCTOR_CODE_TB")+"' AND GROUP_CODE = '"+request.getParameter("GROUP_CODE")+"' AND TYPE ='"+request.getParameter("TYPE")+"' ";
			System.out.println(delete_query);
			boolean status = true;
			DBConnection con = new DBConnection();
			con.connectToLocal();
			
			DBConn conDoctor = new DBConn();
			conDoctor.setStatement();
			
			status = con.executeUpdate(delete_query)<0 ? false : true;
			con.Close();
			System.out.println("status : " + status);
		} 
       	if(MODE == DBMgr.MODE_UPDATE) {
       		String getGroupCode = request.getParameter("GROUP_CODE");
			String getType = request.getParameter("TYPE");
			
       		String getDoctorCode[] = request.getParameterValues("DOCTOR_CODE_TB");
			String getPercent[] = request.getParameterValues("DIS_PERCENT_TB");
			String getAmount[] = request.getParameterValues("DIS_AMOUNT_TB");
			System.out.println("------------------------------------------------------" );
			for(int i = 0; i < getDoctorCode.length; i++){
				System.out.println("getDoctorCode : " + getDoctorCode[i]);
				System.out.println("getPercent : " + getPercent[i]);
				System.out.println("getAmount : " + getAmount[i]);
				
				String update_query = " UPDATE STP_SHARING_REVENUE "
						+ " SET PERCENT_DF='"+getPercent[i]+"', AMOUNT='"+getAmount[i]+"' "
						+ " WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' "
						+ " AND GROUP_CODE = '"+getGroupCode+"' AND TYPE='"+getType+"' "
						+ " AND DOCTOR_CODE = '"+getDoctorCode[i]+"'";
				System.out.println("update_query >> " + update_query);
				boolean status = true;
				DBConnection con = new DBConnection();
				con.connectToLocal();
				
				DBConn conDoctor = new DBConn();
				conDoctor.setStatement();
				
				status = con.executeUpdate(update_query) < 0 ? false : true;
				
				con.Close();
				System.out.println("status : " + status);
			}
       	}
	 }
	
	
	if (request.getParameter("GROUP_CODE") != null && request.getParameter("TYPE") != null) 
    {
		query = "SELECT GROUP_CODE, TYPE FROM STP_SHARING_REVENUE WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND GROUP_CODE = '"+request.getParameter("GROUP_CODE")+"' AND TYPE = '"+request.getParameter("TYPE")+"'";
        GroupRec = DBMgr.getRecord(query);
       
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${labelMap.TITLE_MAIN}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />

		<script type="text/javascript" src="../../javascript/ajax.js"></script>
		<script type="text/javascript" src="../../javascript/util.js"></script>
		<script type="text/javascript" src="../../javascript/search_form.js"></script>
		<script type="text/javascript" src="../../javascript/data_table.js"></script>
		<script type="text/javascript">
            
		function getData(){
			document.getElementById("mainForm").submit();
		}
		
		function DELETE_DR_CODE(code) {
			var dr_code = code;
			/* console.log("value dr_code >> " + dr_code); */
			document.mainForm.hid_DOCTOR_CODE_TB.value = dr_code;
			
			if (confirm("Delete code "+dr_code+" ?")) {
                document.mainForm.MODE.value = "<%=DBMgr.MODE_DELETE%>";
                document.mainForm.submit();
                
            }else{
                document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
            }
		}
		
/* 		function check_val(arr_doctor_code){
			console.log("arr_doctor_code.size : "+ arr_doctor_code.size);
		} */
			
		function SAVE_CLICK()
		{	
			/* console.log(" DOCTOR_CODE_TB.length >> " + document.getElementsByName("DOCTOR_CODE_TB").length);
			console.log(" document.mainForm.elements.length >> " + document.mainForm.elements.length); */
			
			for (var i = 0; i < document.getElementsByName("DOCTOR_CODE_TB").length; i++) {
				//document.getElementsByName("DOCTOR_CODE_TB[]")[i].value
				console.log("CODE i : "+i + " ,value : " + document.getElementsByName("DOCTOR_CODE_TB")[i].value);
				console.log("PERCENT i : "+i + " ,value : " + document.getElementsByName("DIS_PERCENT_TB")[i].value);
				console.log("AMOUNT i : "+i + " ,value : " + document.getElementsByName("DIS_AMOUNT_TB")[i].value);
			}
			
			
			if (confirm("Update data?")) {              
                document.mainForm.MODE.value = "<%=DBMgr.MODE_UPDATE%>";
                document.mainForm.submit();
                
            }else{
                document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
            }
		}
			
			
        </script>
</head>
<body>
	<form id="mainForm" name="mainForm" method="post" action="group_sharing_revenue.jsp">
		<input type="hidden" name="MODE" id="MODE" value="<%=MODE%>" />
		
		<center>
			<table width="800" border="0">
				<tr>
					<td align="left"><b><font color='#003399'><%=Utils.getInfoPage("group_sharing_revenue.jsp", labelMap.getFieldLangSuffix(),new DBConnection("" + session.getAttribute("HOSPITAL_CODE")))%></font></b>
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
					<label for="LABEL_GROUP_CODE">${labelMap.LABEL_GROUP_CODE}</label>
				</td>
				
				<td colspan="3" class="input">
					<input type="text" name="GROUP_CODE" id="GROUP_CODE" class="medium" value="<%= DBMgr.getRecordValue(GroupRec, "GROUP_CODE") %>"  />
				</td>
			</tr>

			<tr>
				<td class="label">
					<label for="LABEL_TYPE">${labelMap.LABEL_TYPE}</label>
				</td>
				
				<td colspan="3" class="input">
					<select name="TYPE" id="TYPE" class="medium">
						<option value="">-- Select Type --</option>
						<option value="DF" <%= DBMgr.getRecordValue(GroupRec, "TYPE").equalsIgnoreCase("DF") ? " selected=\"selected\"" : ""%>>DF</option>
						<%-- <option value="ADJUST" <%= DBMgr.getRecordValue(GroupRec, "TYPE").equalsIgnoreCase("ADJUST") ? " selected=\"selected\"" : ""%>>Adjust</option>
						<option value="ALL" <%= DBMgr.getRecordValue(GroupRec, "TYPE").equalsIgnoreCase("ALL") ? " selected=\"selected\"" : ""%>>All</option> --%>
					</select>
				</td>
			</tr>

			<tr>
				<th colspan="6" class="buttonBar">
					<input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="window.location = 'group_sharing_revenue_detail.jsp?GROUP_CODE=' + document.mainForm.GROUP_CODE.value+'&TYPE=' + document.mainForm.TYPE.value; return false;" style="float: left;" /> 
					<input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location.href ='group_sharing_revenue.jsp?MODE='+<%=DBMgr.MODE_QUERY %>+'&GROUP_CODE=' + document.mainForm.GROUP_CODE.value+'&TYPE=' + document.mainForm.TYPE.value;" />
					<input type="button" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='group_sharing_revenue.jsp'" />
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
				<!--  <td class="sub_head"><input type="checkbox" id="allCheckBox" name="allCheckBox"  /></td> -->
				<td class="sub_head">${labelMap.LABEL_GROUP}</td>
				<td class="sub_head">${labelMap.LABEL_DOCTOR_CODE}</td>
				<td class="sub_head">${labelMap.LABEL_DOCTOR_NAME}</td>
				<td class="sub_head">${labelMap.LABEL_RATIO}</td>
				<td class="sub_head">${labelMap.LABEL_AMOUNT}</td>
				<td class="sub_head">${labelMap.LABEL_DELETE}</td>
			</tr>

			<%
			if (request.getParameter("MODE") != null) {
				if (request.getParameter("GROUP_CODE") != null && request.getParameter("TYPE") != null) {

					group_code = request.getParameter("GROUP_CODE");
					select_type = request.getParameter("TYPE");

					DBConnection con = new DBConnection();
					con.connectToLocal();

					String cond = "";
					if (request.getParameter("GROUP_CODE") != null && request.getParameter("TYPE") != null) {
						cond += "WHERE GROUP_CODE LIKE '" + group_code + "' AND TYPE LIKE '" + select_type + "' ORDER BY GROUP_CODE  ";
						//System.out.println("cond = "+cond);	 
					}

					query = " SELECT GROUP_CODE, DOCTOR_CODE, NAME_" + labelMap.getFieldLangSuffix()
							+ " AS DOCTOR_NAME , PERCENT_DF, AMOUNT " 
							+ " FROM STP_SHARING_REVENUE SR left join "
							+ " DOCTOR D on SR.DOCTOR_CODE = D.CODE " 
							+ cond;

					//System.out.println("query = " + query);

					ResultSet rs = con.executeQuery(query);
					int i = 0;
					String dis_ratio = "", dis_amount = "";

					while (rs.next()) {
						dis_ratio = rs.getString("PERCENT_DF");
						dis_amount = rs.getString("AMOUNT");
						
						if(dis_amount == null){
							dis_amount = "0.00";
						}
						
						if(dis_ratio == null){
							dis_ratio = "0.00";
						}
						
			%>
			<tr>
				<%-- <td class="row<%=i % 2%> alignCenter"><input type="checkbox" id="DIS[]" name="DIS[]" value="<%=rs.getString("CODE") %>" <%=show_check %> onclick="checkValue();"/></td> --%>
				<td class="row<%=i % 2%>"><input type="textbox" name="GROUP_CODE_TB" id="GROUP_CODE_TB" size="10" value="<%=rs.getString("GROUP_CODE")%>" readonly></td>
				<td class="row<%=i % 2%>"><input type="textbox" name="DOCTOR_CODE_TB" id="DOCTOR_CODE_TB" size="10" value="<%=rs.getString("DOCTOR_CODE")%>" readonly></td>
				<input type="hidden" name="hid_DOCTOR_CODE_TB<%=i%>" id="hid_DOCTOR_CODE_TB<%=i%>" value="">
				<td class="row<%=i % 2%>"><%=Util.formatHTMLString(rs.getString("DOCTOR_NAME"), true)%></td>
				<td align="center" class="row<%=i % 2%>"><input type="textbox" name="DIS_PERCENT_TB" id="DIS_PERCENT_TB" size="5" maxlength="6" value="<%=dis_ratio%>"></td>
				<input type="hidden" name="hid_PERCENT_TB<%=i%>" id="hid_PERCENT_TB<%=i%>" value="">
				<td align="center" class="row<%=i % 2%>"><input type="textbox" name="DIS_AMOUNT_TB" id="DIS_AMOUNT_TB" size="10" value="<%=dis_amount%>"></td>
				<input type="hidden" name="hid_AMOUNT_CODE_TB<%=i%>" id="hid_AMOUNT_CODE_TB<%=i%>" value="">
<%--  				<td class="row<%=i % 2%>"><input type="button" id="btn_delete" name="btn_delete" class="button" value="Delete" onclick="window.location = 'group_sharing_revenue.jsp?MODE=<%=DBMgr.MODE_DELETE %>&GROUP_CODE=<%=rs.getString("GROUP_CODE")%>&TYPE='+document.mainForm.TYPE.value+'&DOCTOR_CODE=<%=rs.getString("DOCTOR_CODE")%>'"/></td> --%>
 				<td class="row<%=i % 2%>"><input type="button" id="btn_delete" name="btn_delete" class="button" value="Delete" onclick="DELETE_DR_CODE('<%=rs.getString("DOCTOR_CODE")%>')"  /></td>
			</tr>
			<%
					i++;
					}
					if (rs != null) {
						rs.close();
					}
					con.Close();
				}
			}

			%>

			<tr>
				<input type="hidden" name="hid_DOCTOR_CODE_TB" id="hid_DOCTOR_CODE_TB" value="">
				<th colspan="6" class="buttonBar"><input type="button" id="BT_SAVE" name="BT_SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_CLICK();" /></th>
			</tr>
		</table>
	</form>
</body>
</html>


