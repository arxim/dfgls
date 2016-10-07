<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>

<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_DOCTOR_CATEGORY)) {
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
            labelMap.add("TITLE_MAIN", "Doctor Category", "หมวดหมู่แพทย์");
            labelMap.add("CODE", "Code", "รหัส");
            labelMap.add("DESCRIPTION", "Description", "ชื่อ");
            //labelMap.add("ACTIVE", "Status", "สถานะ");
            //labelMap.add("ACTIVE_0", "Inactive", "ไม่ใช้งาน");
            //labelMap.add("ACTIVE_1", "Active", "ใช้งาน");
            labelMap.add("DOCTOR_CLASS_CODE", "Doctor Type", "ประเภทแพทย์");
            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord doctorCategoryRec = null, doctorClassRec = null;
            byte MODE = DBMgr.MODE_INSERT;
			String getcode = "";
			String codescript = "";
            if (request.getParameter("MODE") != null) {

                MODE = Byte.parseByte(request.getParameter("MODE"));

                doctorCategoryRec = new DataRecord("DOCTOR_CATEGORY");

                doctorCategoryRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                doctorCategoryRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                doctorCategoryRec.addField("DESCRIPTION", Types.VARCHAR, request.getParameter("DESCRIPTION"));
                doctorCategoryRec.addField("DOCTOR_CLASS_CODE", Types.VARCHAR, "CS");
                //doctorCategoryRec.addField("DOCTOR_CLASS_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CLASS_CODE"));
                doctorCategoryRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                doctorCategoryRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                doctorCategoryRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                doctorCategoryRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if (MODE == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(doctorCategoryRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/doctor_category.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(doctorCategoryRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/doctor_category.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
            else if (request.getParameter("CODE") != null) {
                MODE = DBMgr.MODE_UPDATE;
                doctorCategoryRec = DBMgr.getRecord("SELECT * FROM DOCTOR_CATEGORY WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND CODE = '" + request.getParameter("CODE") + "'");
                if (doctorCategoryRec == null) {
                    MODE = DBMgr.MODE_INSERT;
					getcode = request.getParameter("CODE");
					codescript = "<script language=\"javascript\">";
					codescript+= "	alert('Data Not Found');";
					codescript+= "</script>";
                }
                else {
                    MODE = DBMgr.MODE_UPDATE;
                    //doctorClassRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM DOCTOR_CLASS WHERE CODE = '" + DBMgr.getRecordValue(doctorCategoryRec, "DOCTOR_CLASS_CODE") + "'");
					getcode = DBMgr.getRecordValue(doctorCategoryRec, "CODE");
                }
            }
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
                    Refresh_DOCTOR_CATEGORY();
                    return false;
                }
                else {
                    return true;
                }
            }

            function Refresh_DOCTOR_CATEGORY() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?CODE=' + document.mainForm.CODE.value;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?CODE=' + document.mainForm.CODE.value;
                }
            }
            /*
            function DOCTOR_CLASS_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CLASS_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_DOCTOR_CLASS() {
                var target = "../../RetrieveData?TABLE=DOCTOR_CLASS&COND=CODE='" + document.mainForm.DOCTOR_CLASS_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CLASS);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CLASS() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CLASS_CODE.value = "";
                        document.mainForm.DOCTOR_CLASS_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_CLASS_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
			*/
            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=DOCTOR_CATEGORY&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
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
					//!isObjectEmptyString(document.mainForm.DOCTOR_CLASS_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.value = '';
                document.mainForm.CODE.readOnly = false;
                document.mainForm.DESCRIPTION.value = '';
                //document.mainForm.DOCTOR_CLASS_CODE.value = '';
                //document.mainForm.DOCTOR_CLASS_DESCRIPTION.value = '';
                document.mainForm.ACTIVE_1.checked = true;
                return false;
            }
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
-->
        </style>
</head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="doctor_category.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
			<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("doctor_category.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>

            <table class="form">
                <tr>
				  <th colspan="4"> <div style="float: left;">${labelMap.TITLE_MAIN}</div> </th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" value="<%= getcode %>"<%= MODE == DBMgr.MODE_UPDATE ? " readonly=\"readonly\"" : "" %> onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_CATEGORY&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&TARGET=CODE&HANDLE=Refresh_DOCTOR_CATEGORY'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION"><span class="style1">${labelMap.DESCRIPTION}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION" name="DESCRIPTION" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "DESCRIPTION") %>" /></td>
                </tr>
                <!-- 
                <tr>
                    <td class="label"><label for="DOCTOR_CLASS_CODE"><span class="style1">${labelMap.DOCTOR_CLASS_CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CLASS_CODE" name="DOCTOR_CLASS_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(doctorClassRec, "CODE") %>" onkeypress="return DOCTOR_CLASS_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_CLASS();" />
                        <input id="SEARCH_DOCTOR_CLASS_CODE" name="SEARCH_DOCTOR_CLASS_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_CLASS&DISPLAY_FIELD=DESCRIPTION&BEACTIVE=1&TARGET=DOCTOR_CLASS_CODE&HANDLE=AJAX_Refresh_DOCTOR_CLASS'); return false;" />
                        <input type="text" id="DOCTOR_CLASS_DESCRIPTION" name="DOCTOR_CLASS_DESCRIPTION" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorClassRec, "DESCRIPTION") %>" />
                    </td>
                </tr>
                -->
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(doctorCategoryRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(doctorCategoryRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(doctorCategoryRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : "" %> />
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