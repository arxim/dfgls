<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils "%>

<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_DEPARTMENT)) {
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
            labelMap.add("TITLE_MAIN", "Department", "แผนก");
            labelMap.add("CODE", "Code", "รหัส");
            labelMap.add("DESCRIPTION", "Description", "ชื่อ");
            labelMap.add("HOSPITAL_UNIT_CODE", "Hospital Unit", "Hospital Unit");
            labelMap.add("LOCATION", "Default Location", "สถานที่สังกัด");
            labelMap.add("GL_CODE", "GL CODE", "GL CODE");
            
            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord departmentRec = null, hospitalUnitRec = null, location = null;
            DataRecord departmentRecLog;
            byte MODE = DBMgr.MODE_INSERT;
			String getcode = "";
			String codescript = "";
			String remark = "";
            if (request.getParameter("MODE") != null) {

                //out.print(request.getParameterMap().toString());

                departmentRec = new DataRecord("DEPARTMENT");
                departmentRecLog = new DataRecord("LOG_DEPARTMENT");
                
                departmentRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                departmentRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                departmentRec.addField("DESCRIPTION", Types.VARCHAR, request.getParameter("DESCRIPTION"));
                departmentRec.addField("HOSPITAL_UNIT_CODE", Types.VARCHAR, request.getParameter("HOSPITAL_UNIT_CODE"));
                departmentRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                departmentRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                departmentRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                departmentRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                departmentRec.addField("DEFAULT_LOCATION_CODE", Types.VARCHAR, request.getParameter("DEFAULT_LOCATION_CODE"));
                departmentRec.addField("GL_CODE", Types.VARCHAR, request.getParameter("GL_CODE"));
                
                //for log
                departmentRecLog.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                departmentRecLog.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                departmentRecLog.addField("DESCRIPTION", Types.VARCHAR, request.getParameter("DESCRIPTION"));
                departmentRecLog.addField("HOSPITAL_UNIT_CODE", Types.VARCHAR, request.getParameter("HOSPITAL_UNIT_CODE"));
                departmentRecLog.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                departmentRecLog.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate(), true);
                departmentRecLog.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime(), true);
                departmentRecLog.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                departmentRecLog.addField("DEFAULT_LOCATION_CODE", Types.VARCHAR, request.getParameter("DEFAULT_LOCATION_CODE"));
                departmentRecLog.addField("GL_CODE", Types.VARCHAR, request.getParameter("GL_CODE"));
                
                if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_INSERT) {
                	departmentRecLog.addField("REMARK", Types.VARCHAR, remark);
                	
                    if (DBMgr.insertRecord(departmentRec) && DBMgr.insertRecord(departmentRecLog)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/department.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
					DataRecord dept = DBMgr.getRecord("SELECT HOSPITAL_CODE, CODE, DESCRIPTION, HOSPITAL_UNIT_CODE, ACTIVE, UPDATE_DATE, UPDATE_TIME, USER_ID, DEFAULT_LOCATION_CODE, GL_CODE "+
               				"FROM DEPARTMENT WHERE CODE = '" + request.getParameter("CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' " );
             		
               		remark = "แก้ไข ";
               		for(int i = 0; i < dept.getSize(); i++){
               			if(!dept.getValueOfIndex(i).getValue().equalsIgnoreCase(departmentRec.getValueOfIndex(i).getValue())
               					&& !departmentRec.getValueOfIndex(i).getName().equals("USER_ID")
               					&& !departmentRec.getValueOfIndex(i).getName().equals("UPDATE_DATE")
               					&& !departmentRec.getValueOfIndex(i).getName().equals("UPDATE_TIME")){
               				//System.out.println(dept.getValueOfIndex(i).getValue()+", "+departmentRec.getValueOfIndex(i).getValue());
               				System.out.println("แก้ไข"+departmentRec.getValueOfIndex(i).getName());
               				remark += departmentRec.getValueOfIndex(i).getName()+", ";
               			}
               		}
               		departmentRecLog.addField("REMARK", Types.VARCHAR, remark.substring(0, remark.length()-2));
               		
                    if (DBMgr.updateRecord(departmentRec) && DBMgr.insertRecord(departmentRecLog)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/department.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
            else if (request.getParameter("CODE") != null) {
                departmentRec = DBMgr.getRecord("SELECT * FROM DEPARTMENT WHERE CODE = '" + request.getParameter("CODE") + "' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"'");
                if (departmentRec == null) {
                    MODE = DBMgr.MODE_INSERT;
					getcode = request.getParameter("CODE");
					codescript = "<script language=\"javascript\">";
					codescript+= "	alert('Data Not Found');";
					codescript+= "</script>";
                }
                else {
                    MODE = DBMgr.MODE_UPDATE;
                    hospitalUnitRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM HOSPITAL_UNIT WHERE CODE = '" + DBMgr.getRecordValue(departmentRec, "HOSPITAL_UNIT_CODE") + "' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"'");
                    location = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM LOCATION WHERE CODE = '" + DBMgr.getRecordValue(departmentRec, "DEFAULT_LOCATION_CODE") + "' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"'");
					getcode = DBMgr.getRecordValue(departmentRec, "CODE");
					
                }
            }
            System.out.println(session.getAttribute("HOSPITAL_CODE"));
            DBConnection conn = new DBConnection();
            conn.connectToLocal();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">
            
            function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    Refresh_DEPARTMENT();
                    return false;
                }
                else {
                    return true;
                }
            }

            function Refresh_DEPARTMENT() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?CODE=' + document.mainForm.CODE.value;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?CODE=' + document.mainForm.CODE.value;
                }
            }
            
            function HOSPITAL_UNIT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.HOSPITAL_UNIT_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_HOSPITAL_UNIT() {
                var target = "../../RetrieveData?TABLE=HOSPITAL_UNIT&COND=CODE='" + document.mainForm.HOSPITAL_UNIT_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_HOSPITAL_UNIT);
            }
            
            function AJAX_Handle_Refresh_HOSPITAL_UNIT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.HOSPITAL_UNIT_CODE.value = "";
                        document.mainForm.HOSPITAL_UNIT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.HOSPITAL_UNIT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }

            function DEFAULT_LOCATION_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DEFAULT_LOCATION_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DEFAULT_LOCATION_CODE() {
                var target = "../../RetrieveData?TABLE=LOCATION&COND=CODE='" + document.mainForm.DEFAULT_LOCATION_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DEFAULT_LOCATION_CODE);
            }

            function AJAX_Handle_Refresh_DEFAULT_LOCATION_CODE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DEFAULT_LOCATION_CODE.value = "";
                        document.mainForm.DEFAULT_LOCATION_DESCRIPTION.value = "";
                        document.mainForm.GL_CODE.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.DEFAULT_LOCATION_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                    //document.mainForm.GL_CODE.value = getXMLNodeValue(xmlDoc, "GL_CODE");
                    
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }
            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "CODE");
                    
                    switch (document.mainForm.MODE.value) {
                        case "<%=DBMgr.MODE_INSERT%>" :
                            if (beExist) {
                                if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                                    document.mainForm.MODE.value= "<%=DBMgr.MODE_UPDATE%>";
                                    document.mainForm.submit();
                                }
                            }
                            else {
                                document.mainForm.submit();
                            }
                            break;
                        case "<%=DBMgr.MODE_UPDATE%>" :
                            if (beExist) {
                                document.mainForm.submit();
                            }
                            else {
                                alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                            }
                            break;
                    }
                }
            }
            
            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
					!isObjectEmptyString(document.mainForm.DEFAULT_LOCATION_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.value = '';
                document.mainForm.CODE.readOnly = false;
                document.mainForm.DESCRIPTION.value = '';
                document.mainForm.HOSPITAL_UNIT_CODE.value = '';
                document.mainForm.HOSPITAL_UNIT_DESCRIPTION.value = '';
                document.mainForm.DEFAULT_LOCATION_CODE.value = '';
                document.mainForm.GL_CODE.value = '';
                document.mainForm.ACTIVE_1.checked = true;
                return false;
            }
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
.style2 {color: #333}
-->
        </style>
</head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="department.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                        <b><font color='#003399'><%=Utils.getInfoPage("department.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
		</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
					<!--
				  	<div style="float: right;" id="Language" name="Language">
				  	<a href="../../switch_lang.jsp?lang=<%=LabelMap.LANG_TH%>"><img src="../../images/thai_flag.jpg" width="16" height="11" /></a> | 
				  	<a href="../../switch_lang.jsp?lang=<%=LabelMap.LANG_EN%>"><img src="../../images/eng_flag.jpg" width="16" height="11" /></a></div>
					-->
				  	</th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" value="<%= getcode %>"<%= MODE == DBMgr.MODE_UPDATE ? " readonly=\"readonly\"" : "" %> onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&TARGET=CODE&HANDLE=Refresh_DEPARTMENT'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION"><span class="style1">${labelMap.DESCRIPTION}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DESCRIPTION" name="DESCRIPTION" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(departmentRec, "DESCRIPTION") %>" />
                    </td>
                </tr>
                
                <tr>
                    <td class="label"><label for="GL_CODE"><span class="style2">${labelMap.GL_CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="GL_CODE" name="GL_CODE" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(departmentRec, "GL_CODE") %>" />
                    </td>
                </tr>
                
                <tr>
                    <td class="label"><label for="HOSPITAL_UNIT_CODE"><span class="style2">${labelMap.HOSPITAL_UNIT_CODE}</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="HOSPITAL_UNIT_CODE" name="HOSPITAL_UNIT_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(hospitalUnitRec, "CODE") %>" onkeypress="return HOSPITAL_UNIT_CODE_KeyPress(event);" onblur="AJAX_Refresh_HOSPITAL_UNIT();" />
                        <input id="SEARCH_HOSPITAL_UNIT_CODE" name="SEARCH_HOSPITAL_UNIT_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=HOSPITAL_UNIT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&TARGET=HOSPITAL_UNIT_CODE&HANDLE=AJAX_Refresh_HOSPITAL_UNIT'); return false;" />
                        <input type="text" id="HOSPITAL_UNIT_DESCRIPTION" name="HOSPITAL_UNIT_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(hospitalUnitRec, "DESCRIPTION") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1"><span class="style1">${labelMap.LOCATION}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DEFAULT_LOCATION_CODE" name="DEFAULT_LOCATION_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(location, "CODE") %>" onkeypress="return DEFAULT_LOCATION_CODE_KeyPress(event);" onblur="AJAX_Refresh_DEFAULT_LOCATION_CODE();" />
                        <input id="SEARCH_DEFAULT_LOCATION" name="SEARCH_DEFAULT_LOCATION" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=LOCATION&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&TARGET=DEFAULT_LOCATION_CODE&HANDLE=AJAX_Refresh_DEFAULT_LOCATION_CODE'); return false;" />
                        <input type="text" id="DEFAULT_LOCATION_DESCRIPTION" name="DEFAULT_LOCATION_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(location, "DESCRIPTION") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(departmentRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(departmentRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(departmentRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<%=codescript%>