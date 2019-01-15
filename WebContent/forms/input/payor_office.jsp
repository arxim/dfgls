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
            labelMap.add("TITLE_MAIN", "Payor Office", "บริษัทคู่สัญญา");
            labelMap.add("CODE", "Code", "รหัส");
            labelMap.add("NAME_THAI", "Name (Thai)", "ชื่อ (ไทย)");
            labelMap.add("NAME_ENG", "Name (Eng)", "ชื่อ (อังกฤษ)");
            //labelMap.add("ACTIVE", "Status", "สถานะ");
            //labelMap.add("ACTIVE_0", "Inactive", "ไม่ใช้งาน");
            //labelMap.add("ACTIVE_1", "Active", "ใช้งาน");
            labelMap.add("IS_ADVANCE_PAYMENT", "Advance Payment", "สำรองจ่ายก่อน");
            labelMap.add("IS_ADVANCE_PAYMENT_0", "No", "ไม่ใช่");
            labelMap.add("IS_ADVANCE_PAYMENT_1", "Yes", "ใช่");
            labelMap.add("PAYOR_OFFICE_CATEGORY_CODE","Payer Office Category.","กลุ่มบริษัทคู่สัญญา");
            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord record = null, payorOfficeCategoryRec = null;
            DataRecord payorRecLog;
            String remark = "";
            
            if (request.getParameter("MODE") != null) {
                record = new DataRecord("PAYOR_OFFICE");
                payorRecLog = new DataRecord("LOG_PAYOR_OFFICE");
                payorOfficeCategoryRec = DBMgr.getRecord("SELECT CODE, NAME_THAI AS DESCRIPTION FROM PAYOR_OFFICE_CATEGORY WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND CODE = '" + DBMgr.getRecordValue(record, "PAYER_OFFICE_CATEGORY_CODE") + "'");
                
                record.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                record.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                record.addField("PAYOR_OFFICE_CATEGORY_CODE", Types.VARCHAR, request.getParameter("PAYOR_OFFICE_CATEGORY_CODE"));
                record.addField("NAME_THAI", Types.VARCHAR, request.getParameter("NAME_THAI"));
                record.addField("NAME_ENG", Types.VARCHAR, request.getParameter("NAME_ENG"));
                record.addField("IS_ADVANCE_PAYMENT", Types.VARCHAR, request.getParameter("IS_ADVANCE_PAYMENT"));
                record.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                record.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                record.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                record.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                
                payorRecLog.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                payorRecLog.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                payorRecLog.addField("PAYOR_OFFICE_CATEGORY_CODE", Types.VARCHAR, request.getParameter("PAYOR_OFFICE_CATEGORY_CODE"));
                payorRecLog.addField("NAME_THAI", Types.VARCHAR, request.getParameter("NAME_THAI"));
                payorRecLog.addField("NAME_ENG", Types.VARCHAR, request.getParameter("NAME_ENG"));
                payorRecLog.addField("IS_ADVANCE_PAYMENT", Types.VARCHAR, request.getParameter("IS_ADVANCE_PAYMENT"));
                payorRecLog.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                payorRecLog.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate(), true);
                payorRecLog.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime(), true);
                payorRecLog.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());


                if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_INSERT) {
                	payorRecLog.addField("REMARK", Types.VARCHAR, remark);
                	
                    if (DBMgr.insertRecord(record)&& DBMgr.insertRecord(payorRecLog)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/payor_office.jsp"));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } else if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
                	DataRecord payor = DBMgr.getRecord("SELECT HOSPITAL_CODE, CODE, PAYOR_OFFICE_CATEGORY_CODE, NAME_THAI, NAME_ENG, IS_ADVANCE_PAYMENT, ACTIVE, UPDATE_DATE, UPDATE_TIME, USER_ID "+
               				"FROM PAYOR_OFFICE WHERE CODE = '" + request.getParameter("CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' " );
             		
               		remark = "แก้ไข ";
               		for(int i = 0; i < payor.getSize(); i++){
               			if(!payor.getValueOfIndex(i).getValue().equalsIgnoreCase(record.getValueOfIndex(i).getValue())
               					&& !record.getValueOfIndex(i).getName().equals("USER_ID")
               					&& !record.getValueOfIndex(i).getName().equals("UPDATE_DATE")
               					&& !record.getValueOfIndex(i).getName().equals("UPDATE_TIME")){
               				System.out.println("แก้ไข"+record.getValueOfIndex(i).getName());
               				remark += record.getValueOfIndex(i).getName()+", ";
               			}
               		}
               		payorRecLog.addField("REMARK", Types.VARCHAR, remark.substring(0, remark.length()-2));
               		if (DBMgr.updateRecord(record) && DBMgr.insertRecord(payorRecLog)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/payor_office.jsp"));
                    } else {
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
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">
        
	        function AJAX_Refresh_PAYOR_OFFICE() {
	            var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
	            AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE);
	        }
	        
	        function AJAX_Handle_Refresh_PAYOR_OFFICE() {
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
	                document.mainForm.NAME_ENG.value = getXMLNodeValue(xmlDoc, "NAME_ENG");
	                document.mainForm.NAME_THAI.value = getXMLNodeValue(xmlDoc, "NAME_THAI");
	                document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.value = getXMLNodeValue(xmlDoc, "PAYOR_OFFICE_CATEGORY_CODE");
	              /*   document.mainForm.PLAN_CODE.value = getXMLNodeValue(xmlDoc, "PLAN_CODE"); */
	                AJAX_Refresh_PAYOR_OFFICE_CATEGORY();
                    document.mainForm.IS_ADVANCE_PAYMENT[0].checked = getXMLNodeValue(xmlDoc, "IS_ADVANCE_PAYMENT") == 'Y' ? true : false;
                    document.mainForm.IS_ADVANCE_PAYMENT[1].checked = getXMLNodeValue(xmlDoc, "IS_ADVANCE_PAYMENT") == 'N' ? true : false;
                    document.mainForm.ACTIVE[0].checked = getXMLNodeValue(xmlDoc, "ACTIVE") == '1' ? true : false;
                    document.mainForm.ACTIVE[1].checked = getXMLNodeValue(xmlDoc, "ACTIVE") == '0' ? true : false;
	            }
	        }

	        function CODE_KeyPress(e) {
	            var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
	
	            if (key == 13) {
	                AJAX_Refresh_PAYOR_OFFICE();
	                return false;
	            }else {
	                return true;
	            }
	        }

	        function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
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
                            }else {
                                document.mainForm.submit();
                            }
                            break;
                        case "<%=DBMgr.MODE_UPDATE%>" :
                            if (beExist) {
                                document.mainForm.submit();
                            }else {
                                alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                            }
                            break;
                    }
                }
            }
                        
            function PAYOR_OFFICE_CATEGORY_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_PAYOR_OFFICE_CATEGORY() {
                var target = "../../RetrieveData?TABLE=PAYOR_OFFICE_CATEGORY&COND=CODE='" + document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_PAYOR_OFFICE_CATEGORY() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.value = "";
                        document.mainForm.PAYOR_OFFICE_CATEGORY_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PAYOR_OFFICE_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "NAME_THAI");
                }
            }

            
            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.NAME_ENG, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.NAME_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                document.mainForm.reset();
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.readOnly = false;
            }
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
-->
        </style>
</head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="payor_office.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_INSERT%>" />
			<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("payor_office.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">
				  		<div style="float: left;">${labelMap.TITLE_MAIN}</div>				  	</th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="medium" maxlength="20" onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&TARGET=CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE'); return false;" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="NAME_THAI"><span class="style1">${labelMap.NAME_THAI}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="NAME_THAI" name="NAME_THAI" maxlength="255" class="long" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="NAME_ENG"><span class="style1">${labelMap.NAME_ENG}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="NAME_ENG" name="NAME_ENG" maxlength="255" class="long" /></td>
                </tr>
				<tr>
                    <td class="label"><label for="PAYOR_OFFICE_CATEGORY_CODE"><span>${labelMap.PAYOR_OFFICE_CATEGORY_CODE}</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="PAYOR_OFFICE_CATEGORY_CODE" name="PAYOR_OFFICE_CATEGORY_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(record, "PAYOR_OFFICE_CATEGORY_CODE")%>" onkeypress="return PAYOR_OFFICE_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE_CATEGORY();" />
                        <input id="SEARCH_PAYOR_OFFICE_CATEGORY_CODE" name="SEARCH_PAYOR_OFFICE_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE_CATEGORY&DISPLAY_FIELD=NAME_THAI&TARGET=PAYOR_OFFICE_CATEGORY_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE_CATEGORY'); return false;" />
                        <input type="text" id="PAYOR_OFFICE_CATEGORY_DESCRIPTION" name="PAYOR_OFFICE_CATEGORY_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(payorOfficeCategoryRec, "DESCRIPTION")%>" />
                    </td>
                </tr>
                <%-- <tr>
                    <td class="label"><label for="PLAN_CODE"><span class="style1">${labelMap.PLAN_CODE}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="PLAN_CODE" name="PLAN_CODE" maxlength="255" class="long" /></td>
                </tr> --%>
                <tr>
                    <td class="label"><label for="IS_ADVANCE_PAYMENT_1"><span class="style1">${labelMap.IS_ADVANCE_PAYMENT}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="IS_ADVANCE_PAYMENT_1" name="IS_ADVANCE_PAYMENT" value="Y" checked="checked" />
                        <label for="IS_ADVANCE_PAYMENT_1">${labelMap.IS_ADVANCE_PAYMENT_1}</label>
                        <input type="radio" id="IS_ADVANCE_PAYMENT_0" name="IS_ADVANCE_PAYMENT" value="N" />
                        <label for="IS_ADVANCE_PAYMENT_0">${labelMap.IS_ADVANCE_PAYMENT_0}</label>                    </td>
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
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                    <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="RESET_Click()" />
                    <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" /></th>
                </tr>
            </table>
        </form>
    </body>
</html>
