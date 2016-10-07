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

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_ORDER_ITEM)) {
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
            labelMap.add("TITLE_MAIN", "Order Item Category", "Order Item Category");
            labelMap.add("CODE", "Code", "รหัส");
            labelMap.add("DESCRIPTION_THAI", "Description (Thai)", "ชื่อ (ไทย)");
            labelMap.add("DESCRIPTION_ENG", "Description (Eng)", "ชื่อ (อังกฤษ)");
			labelMap.add("ACCOUNT_CODE", "Account Code", "รหัสลงบัญชี");
            labelMap.add("ACCOUNT_NO", "Account No.", "Account No.");
            request.setAttribute("labelMap", labelMap.getHashMap());

			//
			// Process request
			//

            request.setCharacterEncoding("UTF-8");
            DataRecord orderItemCategoryRec = null, accountRec = null , orderItemCategoryLog  = null  , orderItemCategoryOld = null ;
            byte MODE = DBMgr.MODE_INSERT;

            if (request.getParameter("MODE") != null) {

                MODE = Byte.parseByte(request.getParameter("MODE"));

                orderItemCategoryRec = new DataRecord("ORDER_ITEM_CATEGORY");

                orderItemCategoryRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                orderItemCategoryRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                orderItemCategoryRec.addField("DESCRIPTION_THAI", Types.VARCHAR, request.getParameter("DESCRIPTION_THAI"));
                orderItemCategoryRec.addField("DESCRIPTION_ENG", Types.VARCHAR, request.getParameter("DESCRIPTION_ENG"));
                orderItemCategoryRec.addField("ACCOUNT_NO", Types.VARCHAR, request.getParameter("ACCOUNT_NO"));
                orderItemCategoryRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                orderItemCategoryRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                orderItemCategoryRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                orderItemCategoryRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if (MODE == DBMgr.MODE_INSERT) {
                    if (DBMgr.insertRecord(orderItemCategoryRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/order_item_category.jsp"));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(orderItemCategoryRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/order_item_category.jsp"));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            } else if (request.getParameter("CODE") != null) {
            	
                orderItemCategoryRec = DBMgr.getRecord("SELECT * FROM ORDER_ITEM_CATEGORY WHERE CODE = '" + request.getParameter("CODE") + "'");
                
                if (orderItemCategoryRec == null) {
                    MODE = DBMgr.MODE_INSERT;
                } else {
                    MODE = DBMgr.MODE_UPDATE;
                    accountRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM ACCOUNT WHERE CODE = '" + DBMgr.getRecordValue(orderItemCategoryRec, "ACCOUNT_NO") + "'");
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
                    AJAX_Refresh_ORDER_ITEM_CATEGORY();
                    return false;
                }
                else {
                    return true;
                }
            }
			
			function AJAX_Refresh_ORDER_ITEM_CATEGORY(){
				var target = "../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY);
			}
			
			function AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY() {
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
                    document.mainForm.DESCRIPTION_ENG.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_ENG");
                    document.mainForm.DESCRIPTION_THAI.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_THAI");
                    document.mainForm.ACCOUNT_NO.value = getXMLNodeValue(xmlDoc, "ACCOUNT_NO");
                    document.mainForm.ACTIVE[0].checked = getXMLNodeValue(xmlDoc, "ACTIVE") == '1' ? true : false;
                    document.mainForm.ACTIVE[1].checked = getXMLNodeValue(xmlDoc, "ACTIVE") == '0' ? true : false;
                }
            }

            function Refresh_ORDER_ITEM_CATEGORY() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?CODE=' + document.mainForm.CODE.value;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?CODE=' + document.mainForm.CODE.value;
                }
            }
            
            function ACCOUNT_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ACCOUNT_NO.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_ACCOUNT() {
                var target = "../../RetrieveData?TABLE=ACCOUNT&COND=CODE='" + document.mainForm.ACCOUNT_NO.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_ACCOUNT);
            }
            
            function AJAX_Handle_Refresh_ACCOUNT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.ACCOUNT_NO.value = "";
                        document.mainForm.ACCOUNT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.ACCOUNT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
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
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                document.mainForm.reset();
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.value = '';
                document.mainForm.CODE.readOnly = false;
                document.mainForm.DESCRIPTION_THAI.value = '';
                document.mainForm.DESCRIPTION_ENG.value = '';
                document.mainForm.ACCOUNT_NO.value = '';
                document.mainForm.ACCOUNT_DESCRIPTION.value = '';
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
        <form id="mainForm" name="mainForm" method="post" action="order_item_category.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
			<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("order_item_category.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>                    </th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(orderItemCategoryRec, "CODE")%>"<%= MODE == DBMgr.MODE_UPDATE ? " readonly=\"readonly\"" : ""%> onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM_CATEGORY&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&TARGET=CODE&HANDLE=Refresh_ORDER_ITEM_CATEGORY'); return false;" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_ENG"><span class="style1">${labelMap.DESCRIPTION_ENG}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_ENG" name="DESCRIPTION_ENG" maxlength="255" class="long" value="<%= DBMgr.getRecordValue(orderItemCategoryRec, "DESCRIPTION_ENG")%>" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_THAI"><span class="style1">${labelMap.DESCRIPTION_THAI}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_THAI" name="DESCRIPTION_THAI" maxlength="255" class="long" value="<%= DBMgr.getRecordValue(orderItemCategoryRec, "DESCRIPTION_THAI")%>" /></td>
                </tr>
				<tr>
                    <td class="label"><label for="ACCOUNT_CODE"><span class="style2">${labelMap.ACCOUNT_CODE}</span></label></td>
                    <td colspan="3" class="input">
					<%=DBMgr.generateDropDownList("ACCOUNT_NO", "long", "SELECT CODE, CODE + ' : ' + DESCRIPTION AS DESCRIPTION FROM ACCOUNT ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", ( DBMgr.getRecordValue(orderItemCategoryRec, "ACCOUNT_NO")=="" ? "602101" : DBMgr.getRecordValue(orderItemCategoryRec, "ACCOUNT_NO") ))%>					</td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(orderItemCategoryRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(orderItemCategoryRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                               <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(orderItemCategoryRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                               <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>