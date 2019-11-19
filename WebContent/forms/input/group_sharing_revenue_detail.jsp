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

<%@ include file="../../_global.jsp" %>

<%
//
// Verify permission
//   

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_GROUP_ORDER_ITEM_CATEGORY)) {
                response.sendRedirect("../message.jsp");
                return;
            }

//
//Initial LabelMap
//

		String select_type="",group_code="";

         if (session.getAttribute("LANG_CODE") == null) {
             session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
         }
         LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
         labelMap.add("TITLE_MAIN", "Group Sharing Revenue Detail", "รายละเอียดการกระจายรายได้แพทย์");
         labelMap.add("LABEL_GROUP_CODE", "Group Code", "รหัสกลุ่ม");
         labelMap.add("LABEL_DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
         labelMap.add("LABEL_TYPE", "Type", "ประเภทรายได้");
         
         request.setAttribute("labelMap", labelMap.getHashMap());
         
//
// Process request
//

         request.setCharacterEncoding("UTF-8");
		 ProcessUtil util = new ProcessUtil();
         DataRecord doctorRec = null;
         //String mm = request.getParameter("MM") != null ? request.getParameter("MM") : JDate.getMonth();
         //String yyyy = request.getParameter("YYYY") != null ? request.getParameter("YYYY") : JDate.getYear();
         //System.out.println("group="+request.getParameter("GROUP_CODE"));
         
         if (request.getParameter("DOCTOR_CODE") != null) {
         	doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("DOCTOR_CODE") + "'");
         }
         
         if (request.getParameter("TYPE") != null) {
			select_type=request.getParameter("TYPE");
		 }
         
         DataRecord doctorProfileRec = null, disRec= null;
         String query = "",query_dis="";
         
		 byte MODE = DBMgr.MODE_INSERT;
         if (session.getAttribute("LANG_CODE") == null) {
             session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
         }
         
         if (request.getParameter("MODE") != null){

             MODE = Byte.parseByte(request.getParameter("MODE"));
             String insertData="", updateData="", deleteData="",arrDoctor[][]=null, doctorCode="";
             int num_doctor=0;
             DBConnection con = new DBConnection();
				con.connectToLocal();
				
				DBConn conDoctor = new DBConn();
				conDoctor.setStatement();
				
				String getGroupCode=request.getParameter("GROUP_CODE");
				String getDoctorCode=request.getParameter("DOCTOR_CODE_SEARCH");
				String getType=request.getParameter("TYPE");
				
				insertData="INSERT INTO STP_SHARING_REVENUE (HOSPITAL_CODE,  "
						+" GROUP_CODE, DOCTOR_CODE, TYPE, PERCENT_DF, CREATE_DATE, CREATE_TIME, USER_ID) "
					    +" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"',  "
					    +" '"+getGroupCode+"', '"+getDoctorCode+"', '"+getType+"', "+"'1',"
					    +" '"+JDate.getDate()+"', '"+JDate.getTime()+ "', "
					    +" '"+session.getAttribute("USER_ID").toString()+"')";
				ResultSet rsInsertData = con.executeQuery(insertData);
				System.out.println("insertData = "+insertData);
				session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/group_sharing_revenue.jsp"));
				response.sendRedirect("../message.jsp");
				return;
				
		 } else if (request.getParameter("GROUP_CODE") != "" || request.getParameter("TYPE") != "") {
			 MODE = DBMgr.MODE_UPDATE;
			 System.out.println("GROUP_CODE = "+request.getParameter("GROUP_CODE"));
			 System.out.println("TYPE = "+request.getParameter("TYPE"));
			 
			 	if (request.getParameter("GROUP_CODE") != null) {
	            	group_code = request.getParameter("GROUP_CODE");
	            }
			 	
	            if (request.getParameter("TYPE") != null) {
					select_type=request.getParameter("TYPE");
				}
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

	        function DOCTOR_CODE_KeyPress(e) {
	            var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
	
	            if (key == 13) {
	                document.mainForm.DOCTOR_CODE_SEARCH.blur();
	                return false;
	            }
	            else {
	                return true;
	            }
	        }
	
	        function AJAX_Refresh_DOCTOR() {
	            var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE_SEARCH.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
	            AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
	        }
	        
	        function AJAX_Handle_Refresh_DOCTOR() {
	            if (AJAX_IsComplete()) {
	                var xmlDoc = AJAX.responseXML;
	
	                if (!isXMLNodeExist(xmlDoc, "CODE")) {
	                    document.mainForm.DOCTOR_CODE_SEARCH.value = "";
	                    document.mainForm.DOCTOR_NAME_SEARCH.value = "";
	                    return;
	                }
	                
	                // Data found
	                document.mainForm.DOCTOR_NAME_SEARCH.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
	            }
	        }   
	        
	        function SAVE_CLICK() {
	        	var group_code =  document.mainForm.GROUP_CODE.value ;
	        	var dr_code =  document.mainForm.DOCTOR_CODE_SEARCH.value ;
	        	var type = document.mainForm.TYPE.value ;
	        	
	        	if (group_code == null || dr_code == null || type == null
	        	   || group_code == "" || dr_code == "" || type == "") {
	        		alert ("Please fill all fields.");
					return false;
				} else {
					document.mainForm.submit();
				}
			}
    
        
        </script>
        
	</head>
	<body>
		<form id="mainForm" name="mainForm" method="post" action="group_sharing_revenue_detail.jsp">
		<input type="hidden" name="MODE" id="MODE" value="<%=MODE %>"/>
			<table class="form">
                <tr>
					<th colspan="4">
					  <div style="float: left;">${labelMap.TITLE_MAIN}</div>				
				  	</th>
				</tr>
                <tr>
                    <td class="label"><label for="LABEL_GROUP_CODE">${labelMap.LABEL_GROUP_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="GROUP_CODE" name="GROUP_CODE" class="medium" value="<%= group_code %>" />
                	</td>
                </tr>
                
                <tr>
                  <td class="label"><label for="LABEL_DOCTOR_CODE">${labelMap.LABEL_DOCTOR_CODE}</label></td>
                  <td colspan="3" class="input"><input type="text" id="DOCTOR_CODE_SEARCH" name="DOCTOR_CODE_SEARCH" class="medium" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
                    <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorOnSearch(); return false;" />
                    <input type="text" id="DOCTOR_NAME_SEARCH" name="DOCTOR_NAME_SEARCH" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_" + labelMap.getFieldLangSuffix()) %>" /></td>
                </tr>
                
                <tr>
                    <td class="label"><label for="LABEL_TYPE">${labelMap.LABEL_TYPE}</label></td>
                    <td colspan="3" class="input" ><select name="TYPE" class="medium">
                      <option value="">-- Select Type --</option>
                      <option value="DF" <%= (select_type.equals("DF") ? "selected" : "") %> >DF</option>
                      <option value="ADJUST" <%= (select_type.equals("ADJUST") ? "selected" : "") %>  >Adjust</option>
                      <option value="ALL" <%= (select_type.equals("ALL") ? "selected" : "") %>  >All</option>
                    </select>
                    </td>
                </tr>
                

                
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_CLICK();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='group_sharing_revenue_detail.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='group_sharing_revenue.jsp'" />
                    </th>
                </tr>
            </table>
		</form>
	</body>
</html>

<script language="javascript">
	function doctorOnSearch(){
		openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE_SEARCH&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR');	
	}
</script>