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

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_RECEIPT_TYPE)) {
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
            labelMap.add("TITLE_MAIN", "Receive Type", "ประเภทการรับเงิน");
            labelMap.add("CODE", "Code", "รหัส");
            labelMap.add("DESCRIPTION_THAI", "Description (Thai)", "ชื่อ (ไทย)");
            labelMap.add("DESCRIPTION_ENG", "Description (Eng)", "ชื่อ (อังกฤษ)");
            labelMap.add("IS_CHARGE", "Fee Charge", "ค่าธรรมเนียม");
            labelMap.add("IS_CHARGE_0", "No", "ไม่คิด");
            labelMap.add("IS_CHARGE_1", "Yes", "คิด");
            labelMap.add("PERCENT_OF_CHARGE", "Percent of Charge", "เปอร์เซนต์ค่าธรรมเนียม");
            labelMap.add("BANK_CODE", "Bank", "ธนาคาร");
            labelMap.add("RECEIPT_MODE_CODE", "Receive Mode", "หมวดการรับเงิน");
            //labelMap.add("ACTIVE", "Status", "สถานะ");
            //labelMap.add("ACTIVE_0", "Inactive", "ไม่ใช้งาน");
            //labelMap.add("ACTIVE_1", "Active", "ใช้งาน");
            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord receiptTypeRec = null, bankRec = null;
            byte MODE = DBMgr.MODE_INSERT;
			String getcode = "";
			String codescript = "";
            if (request.getParameter("MODE") != null) {

                MODE = Byte.parseByte(request.getParameter("MODE"));

                receiptTypeRec = new DataRecord("RECEIPT_TYPE");

                receiptTypeRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                receiptTypeRec.addField("DESCRIPTION_THAI", Types.VARCHAR, request.getParameter("DESCRIPTION_THAI"));
                receiptTypeRec.addField("DESCRIPTION_ENG", Types.VARCHAR, request.getParameter("DESCRIPTION_ENG"));
                receiptTypeRec.addField("IS_CHARGE", Types.VARCHAR, request.getParameter("IS_CHARGE"));
                receiptTypeRec.addField("PERCENT_OF_CHARGE", Types.VARCHAR, request.getParameter("PERCENT_OF_CHARGE"));
                receiptTypeRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                receiptTypeRec.addField("RECEIPT_MODE_CODE", Types.VARCHAR, request.getParameter("RECEIPT_MODE_CODE"));
                receiptTypeRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                receiptTypeRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                receiptTypeRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                receiptTypeRec.addField("BANK_CODE", Types.VARCHAR, request.getParameter("BANK_CODE"));
                receiptTypeRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                receiptTypeRec.addField("RECEIPT_MODE_CODE", Types.VARCHAR, request.getParameter("RECEIPT_MODE_CODE"));

                if (MODE == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(receiptTypeRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/receipt_type.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(receiptTypeRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/receipt_type.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
            else if (request.getParameter("CODE") != null) {
                receiptTypeRec = DBMgr.getRecord("SELECT * FROM RECEIPT_TYPE WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND CODE = '" + request.getParameter("CODE") + "'");
                if (receiptTypeRec == null) {
                    MODE = DBMgr.MODE_INSERT;
					getcode = request.getParameter("CODE");
					codescript = "<script language=\"javascript\">";
					codescript+= "	alert('Data Not Found');";
					codescript+= "</script>";

                }
                else {
                    MODE = DBMgr.MODE_UPDATE;
                    bankRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " FROM BANK WHERE CODE = '" + DBMgr.getRecordValue(receiptTypeRec, "BANK_CODE") + "'");
                }
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<style type="text/css">
<!--
.style1 {color: #003399}
.style2 {color: #333}
-->
</style>
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">
                                
            function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    AJAX_Refresh_RECEIPT_TYPE();
                    return false;
                }else {
                    return true;
                }
            }

            function AJAX_Refresh_RECEIPT_TYPE() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?CODE=' + document.mainForm.CODE.value;
                }else {
                    window.location = document.location.pathname.substr(0, to) + '?CODE=' + document.mainForm.CODE.value;
                }
            }
            
            function BANK_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.BANK_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_BANK() {
                var target = "../../RetrieveData?TABLE=BANK&COND=CODE='" + document.mainForm.BANK_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_BANK);
            }
            
            function AJAX_Handle_Refresh_BANK() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.BANK_CODE.value = "";
                        document.mainForm.BANK_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.BANK_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=RECEIPT_TYPE&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
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
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_ENG, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    isObjectValidNumber(document.mainForm.PERCENT_OF_CHARGE, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                //alert("st");
                document.mainForm.reset();
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.readOnly = false;
            }
        </script>
    </head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="receipt_type.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= DBMgr.MODE_INSERT %>" />
			<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("receipt_type.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
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
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.CODE} *</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(receiptTypeRec, "CODE") %>" onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=RECEIPT_TYPE&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&TARGET=CODE&HANDLE=AJAX_Refresh_RECEIPT_TYPE'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_ENG"><span class="style1">${labelMap.DESCRIPTION_ENG} *</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_ENG" name="DESCRIPTION_ENG" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(receiptTypeRec, "DESCRIPTION_ENG") %>" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_THAI"><span class="style1">${labelMap.DESCRIPTION_THAI} *</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_THAI" name="DESCRIPTION_THAI" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(receiptTypeRec, "DESCRIPTION_THAI") %>" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="IS_CHARGE_1">${labelMap.IS_CHARGE}</label></td>
                    <td class="input">
                        <input type="radio" id="IS_CHARGE_1" name="IS_CHARGE" value="1"<%= DBMgr.getRecordValue(receiptTypeRec, "IS_CHARGE").equalsIgnoreCase("1") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_CHARGE_1">${labelMap.IS_CHARGE_1}</label>
                        <input type="radio" id="IS_CHARGE_0" name="IS_CHARGE" value="0"<%= DBMgr.getRecordValue(receiptTypeRec, "IS_CHARGE").equalsIgnoreCase("0") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_CHARGE_0">${labelMap.IS_CHARGE_0}</label>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="PERCENT_OF_CHARGE"><span class="style1">${labelMap.PERCENT_OF_CHARGE} *</span></label></td>
                    <td class="input"><input type="text" id="PERCENT_OF_CHARGE" name="PERCENT_OF_CHARGE" class="short alignRight" maxlength="15" value="<%= DBMgr.getRecordValue(receiptTypeRec, "PERCENT_OF_CHARGE") %>" /> %</td>
                </tr>
                <tr>
                    <td class="label"><label for="BANK_CODE">${labelMap.BANK_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="BANK_CODE" name="BANK_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(bankRec, "CODE") %>" onkeypress="return BANK_CODE_KeyPress(event);" onblur="AJAX_Refresh_BANK();" />
                        <input id="SEARCH_BANK_CODE" name="SEARCH_BANK_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=BANK&BEACTIVE=1&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&TARGET=BANK_CODE&HANDLE=AJAX_Refresh_BANK'); return false;" />
                        <input type="text" id="BANK_DESCRIPTION" name="BANK_DESCRIPTION" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(bankRec, "DESCRIPTION_" + labelMap.getFieldLangSuffix()) %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="RECEIPT_MODE_CODE">${labelMap.RECEIPT_MODE_CODE}</label></td>
                    <td colspan="3" class="input">
                        <%=DBMgr.generateDropDownList("RECEIPT_MODE_CODE", "short", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM RECEIPT_MODE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(receiptTypeRec, "RECEIPT_MODE_CODE"))%>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(receiptTypeRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(receiptTypeRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(receiptTypeRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click();" />
                        <input type="reset" value="${labelMap.RESET}"/>
                        <!-- <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" /> -->
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>