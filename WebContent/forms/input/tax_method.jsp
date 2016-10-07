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

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_TAX_METHOD)) {
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
            labelMap.add("TITLE_MAIN", "Tax Method Calculation", "วิธีการคำนวณภาษี");
            labelMap.add("TAX_CALCULATION_MEHTOD","Calculation Tax Method","วิธีการคำนวณภาษี");
            labelMap.add("SUMMARY","Summary Revenue","คำนวณสรุปรวมรายได้");
            labelMap.add("STEP_SHARING","Step Sharing","คำนวณอัตราก้าวหน้า");
            labelMap.add("WITH_HOLDING_TAX3","With Holding Tax 3%","ภาษีหัก ณ ที่จ่าย 3%");
            labelMap.add("WITH_HOLDING_TAX5","With Holding Tax 5%","ภาษีหัก ณ ที่จ่าย 5%");
            labelMap.add("WITH_HOLDING_TAX14","With Holding Tax 14%","ภาษีหัก ณ ที่จ่าย 14%");
            labelMap.add("WITH_HOLDING_TAX15","With Holding Tax 15%","ภาษีหัก ณ ที่จ่าย 15%");
            labelMap.add("TAX_TYPE_CODE", "Tax Type", "ประเภทภาษี");
            
            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            byte MODE = DBMgr.MODE_INSERT;
            DataRecord record = null;
            if (request.getParameter("MODE") != null) {
            	MODE = Byte.parseByte(request.getParameter("MODE"));
                //out.print(request.getParameterMap().toString());
				
                record = new DataRecord("STP_TAX_CALCULATION");
                record.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(),true);
                record.addField("TAX_TYPE_CODE", Types.VARCHAR, request.getParameter("TAX_TYPE_CODE"));
                record.addField("CALCULATION_METHOD_CODE", Types.VARCHAR, request.getParameter("TAX_CALCULATION_MEHTOD").toString());
                record.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                record.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                record.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                record.addField("UPDATE_USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(record)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/tax_method.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }else if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(record)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/tax_method.jsp"));
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
<style type="text/css">
<!--
.style1 {color: #003399}
.style2 {color: #333}
-->
</style>
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">

            function AJAX_Refresh_TAX_CALCULATION() {
                var target = "../../RetrieveData?TABLE=STP_TAX_CALCULATION&COND=TAX_TYPE_CODE='" + document.mainForm.TAX_TYPE_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_TAX_CALCULATION);
            }
            
            function AJAX_Handle_Refresh_TAX_CALCULATION() {
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
                    document.mainForm.TAX_TYPE_CODE.value = getXMLNodeValue(xmlDoc, "TAX_TYPE_CODE");
                    document.mainForm.ACTIVE[0].checked = getXMLNodeValue(xmlDoc, "ACTIVE") == '1' ? true : false;
                    document.mainForm.ACTIVE[1].checked = getXMLNodeValue(xmlDoc, "ACTIVE") == '0' ? true : false;
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=STP_TAX_CALCULATION&COND=TAX_TYPE_CODE='" + document.mainForm.TAX_TYPE_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                alert(target);
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
            }function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    AJAX_Refresh_EXPENSE();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function SAVE_Click() {
            	AJAX_VerifyData();
            }
            
            function RESET_Click() {
                document.mainForm.reset();
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.readOnly = false;
            }
        </script>
    </head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="tax_method.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_INSERT%>" />
			<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("tax_method.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">
				  		<div style="float: left;">${labelMap.TITLE_MAIN}</div>					</th>
                </tr>
                <tr>
                    <td class="label"><label for="aText"><span class="style1">${labelMap.TAX_TYPE_CODE}*</span></label></td>
                    <td colspan="3"class="input">
                        <%=DBMgr.generateDropDownList("TAX_TYPE_CODE", "short", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM TAX_TYPE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(record, "TAX_TYPE_CODE"))%>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="TAX_CALCULATION_MEHTOD">${labelMap.TAX_CALCULATION_MEHTOD}</label></td>
                    <td colspan="" class="input">
						<select class="mediumMax" id="TAX_CALCULATION_MEHTOD" name="TAX_CALCULATION_MEHTOD">						
	                        <option value=""<%= DBMgr.getRecordValue(record, "CALCULATION_METHOD_CODE").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>-- Undefine --</option>
							<option value="SUM"<%= DBMgr.getRecordValue(record, "CALCULATION_METHOD_CODE").equalsIgnoreCase("SUM") ? " selected=\"selected\"" : "" %>>${labelMap.SUMMARY}</option>
							<option value="STP"<%= DBMgr.getRecordValue(record, "CALCULATION_METHOD_CODE").equalsIgnoreCase("STP") ? " selected=\"selected\"" : "" %>>${labelMap.STEP_SHARING}</option>
							<option value="3"<%= DBMgr.getRecordValue(record, "CALCULATION_METHOD_CODE").equalsIgnoreCase("3") ? " selected=\"selected\"" : "" %>>${labelMap.WITH_HOLDING_TAX3}</option>
							<option value="5"<%= DBMgr.getRecordValue(record, "CALCULATION_METHOD_CODE").equalsIgnoreCase("5") ? " selected=\"selected\"" : "" %>>${labelMap.WITH_HOLDING_TAX5}</option>
							<option value="14"<%= DBMgr.getRecordValue(record, "CALCULATION_METHOD_CODE").equalsIgnoreCase("14") ? " selected=\"selected\"" : "" %>>${labelMap.WITH_HOLDING_TAX14}</option>
							<option value="15"<%= DBMgr.getRecordValue(record, "CALCULATION_METHOD_CODE").equalsIgnoreCase("15") ? " selected=\"selected\"" : "" %>>${labelMap.WITH_HOLDING_TAX15}</option>
		                </select>
                    </td>
                </tr>
                
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1" checked="checked" />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0" />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>                    </td>
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