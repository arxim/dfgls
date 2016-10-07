<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_BANK_BRANCH)) {
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
            labelMap.add("TITLE_MAIN", "Bank Branch", "สาขาธนาคาร");
            labelMap.add("CODE", "Code", "รหัส");
            labelMap.add("BANK_CODE", "Bank", "ธนาคาร");
            labelMap.add("DESCRIPTION_THAI", "Description (Thai)", "ชื่อ (ไทย)");
            labelMap.add("DESCRIPTION_ENG", "Description (Eng)", "ชื่อ (อังกฤษ)");
            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            byte MODE = DBMgr.MODE_INSERT;
            DataRecord bankRec = null, bankBranchRec = null;

            if (request.getParameter("MODE") != null) {
                MODE = Byte.parseByte(request.getParameter("MODE"));
                
                bankBranchRec = new DataRecord("BANK_BRANCH");

                bankBranchRec.addField("BANK_CODE", Types.VARCHAR, request.getParameter("BANK_CODE"), true);
                bankBranchRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                bankBranchRec.addField("DESCRIPTION_THAI", Types.VARCHAR, request.getParameter("DESCRIPTION_THAI"));
                bankBranchRec.addField("DESCRIPTION_ENG", Types.VARCHAR, request.getParameter("DESCRIPTION_ENG"));
                bankBranchRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                bankBranchRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                bankBranchRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                bankBranchRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(bankBranchRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/bank.jsp?CODE=" + bankBranchRec.getField("BANK_CODE").getValue()));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(bankBranchRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/bank.jsp?CODE=" + bankBranchRec.getField("BANK_CODE").getValue()));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
            else if (request.getParameter("BANK_CODE") != null && request.getParameter("CODE") != null) {
                bankRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION FROM BANK WHERE CODE = '" + request.getParameter("BANK_CODE") + "'");
                if (bankRec != null) {
                    MODE = DBMgr.MODE_UPDATE;
                    bankBranchRec = DBMgr.getRecord("SELECT * FROM BANK_BRANCH WHERE CODE = '" + request.getParameter("CODE") + "' AND BANK_CODE = '" + request.getParameter("BANK_CODE") + "'");
                }
            }
            else if (request.getParameter("BANK_CODE") != null) {
                bankRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION FROM BANK WHERE CODE = '" + request.getParameter("BANK_CODE") + "'");
            }
            else {
                response.sendRedirect("../message.jsp");
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
          
            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=BANK_BRANCH&COND=CODE='" + document.mainForm.CODE.value + "' AND BANK_CODE='" + document.mainForm.BANK_CODE.value + "'";
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
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_ENG, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.BANK_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.value = '';
                document.mainForm.CODE.readOnly = false;
                document.mainForm.DESCRIPTION_ENG.value = '';
                document.mainForm.DESCRIPTION_THAI.value = '';
                document.mainForm.BANK_CODE.value = '';
                document.mainForm.BANK_DESCRIPTION.value = '';
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
        <form id="mainForm" name="mainForm" method="post" action="bank_branch.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />            
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
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.CODE} *</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" maxlength="20" class="short" value="<%= DBMgr.getRecordValue(bankBranchRec, "CODE") %>"<%= MODE == DBMgr.MODE_UPDATE ? " readonly=\"readonly\"" : "" %> />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_THAI"><span class="style1">${labelMap.DESCRIPTION_THAI}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_THAI" name="DESCRIPTION_THAI" maxlength="255" class="long" value="<%= DBMgr.getRecordValue(bankBranchRec, "DESCRIPTION_THAI") %>" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_ENG"><span class="style1">${labelMap.DESCRIPTION_ENG}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_ENG" name="DESCRIPTION_ENG" maxlength="255" class="long" value="<%= DBMgr.getRecordValue(bankBranchRec, "DESCRIPTION_ENG") %>" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="BANK_CODE">${labelMap.BANK_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="BANK_CODE" name="BANK_CODE" class="short" maxlength="20" readonly="readonly" value="<%= DBMgr.getRecordValue(bankRec, "CODE") %>" />
                        <input type="text" id="BANK_DESCRIPTION" name="BANK_DESCRIPTION" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(bankRec, "DESCRIPTION") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(bankBranchRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(bankBranchRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checkec\"" : "" %> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(bankBranchRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checkec\"" : "" %> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.history.back()" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
