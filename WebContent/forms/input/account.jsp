<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_ACCOUNT)) {
                response.sendRedirect("../message.jsp");
                return;
            }

            //
            // Initial LabelMap
            //

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }else{
                System.out.println(session.getAttribute("LANG_CODE"));
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Account Code", "รหัสการลงบัญชี");
            labelMap.add("CODE", "Code", "รหัสบัญชี");
            labelMap.add("DESCRIPTION", "Description", "ชื่อบัญชี");
            labelMap.add("GL_CODE", "GL CODE", "GL CODE");
            

            labelMap.add("ALERT_REQUIRED_CODE", "Please fill code", "กรุณาป้อนรหัส");
            labelMap.add("ALERT_REQUIRED_DESCRIPTION", "Please fill description", "กรุณาป้อนชื่อ");

            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");

            if (request.getParameter("MODE") != null) {

                //out.print(request.getParameterMap().toString());

                DataRecord record = new DataRecord("ACCOUNT");

                record.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                record.addField("DESCRIPTION", Types.VARCHAR, request.getParameter("DESCRIPTION"));
                record.addField("GL_CODE", Types.VARCHAR, request.getParameter("GL_CODE"));
                

                if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(record)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/account.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(record)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/account.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">

            function AJAX_Refresh_ACCOUNT() {
                var target = "../../RetrieveData?TABLE=ACCOUNT&COND=CODE='" + document.mainForm.CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_ACCOUNT);
            }
            
            function AJAX_Handle_Refresh_ACCOUNT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                        var code = document.mainForm.CODE.value;
                        document.mainForm.reset();
                        document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                        document.mainForm.CODE.value = code;
                        return;
                    }
                    
                    // Data found
                    document.mainForm.MODE.value = "<%=DBMgr.MODE_UPDATE%>";
                    document.mainForm.CODE.readOnly = true;
                    document.mainForm.DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                    document.mainForm.GL_CODE.value = getXMLNodeValue(xmlDoc, "GL_CODE");
                    
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=ACCOUNT&COND=CODE='" + document.mainForm.CODE.value + "'"; 
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
            
            function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    AJAX_Refresh_ACCOUNT();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                document.mainForm.reset();
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.readOnly = false;
            }
        </script>
    </head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="account.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_INSERT%>" />
            <center>
            <table width="800" border="0">
                <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("account.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                </td></tr>
            </table>
            </center>
            <table class="form">
                <tr>
					<th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  	</th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE">${labelMap.CODE} *</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ACCOUNT&DISPLAY_FIELD=DESCRIPTION&TARGET=CODE&HANDLE=AJAX_Refresh_ACCOUNT'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION">${labelMap.DESCRIPTION} *</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DESCRIPTION" name="DESCRIPTION" class="long" maxlength="255" />
                    </td>
                </tr>
                 <tr>
                    <td class="label"><label for="GL_CODE">${labelMap.GL_CODE} *</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="GL_CODE" name="GL_CODE" class="long" maxlength="255" />
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
