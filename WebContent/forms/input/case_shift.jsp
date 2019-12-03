<%@page import="df.bean.obj.util.JDate"%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.conn.DBConnection"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_MASTER_DETAIL)) {
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
            labelMap.add("TITLE_MAIN", "Setup Shift Case", "Setup Shift Case");
            labelMap.add("CASE_CODE", "Case Code", "รหัสเคส");
            labelMap.add("CASE_DESCRIPTION", "Description", "ชื่อเคส");
            labelMap.add("START_TIME", "Start Time", "เวลาเริ่มต้น");
            labelMap.add("END_TIME", "End Time", "เวลาสิ้นสุด");
            labelMap.add("CASE_TYPE", "Case Type", "ประเภท");
            labelMap.add("CAL_TYPE", "Calculate Type", "วิธีการคำนวณ");
            labelMap.add("PAYOR_OFFICE_CATEGORY_CODE", "Payor Cate. Code", "Payor Cate. Code");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "Order Item Code");
            labelMap.add("MAX_CASE", "Max Case", "เคสสูงสุด");
            labelMap.add("AMOUNT_PER_CASE", "Amount Per Case", "จำนวนเงินต่อเคสที่เกิน");
            labelMap.add("AMOUNT", "Amount", "จำนวนเงิน");
            labelMap.add("HOUR", "Hour", "จำนวนชั่วโมง");
            labelMap.add("EPISODE", "Episode", "Episode");
            labelMap.add("TRANSACTION", "Transaction", "Transaction");
            labelMap.add("OVER", "Over Case", "Over Case");
            labelMap.add("COMPARE", "Compare Case", "Compare Case");
            labelMap.add("CASE_MAPPING_CODE", "Case Mapping Code", "Case Mapping Code");
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            
            byte MODE = DBMgr.MODE_INSERT;
            
            DataRecord methodRec = null, mappingCaseRec = null , caseTypeRec = null , payorCateRec = null , orderItemRec = null ;
            
            if (request.getParameter("MODE") != null) {
            	// Insert or update
                MODE = Byte.parseByte(request.getParameter("MODE"));

                methodRec = new DataRecord("MST_SHIFT_CASE");

                methodRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                methodRec.addField("CASE_CODE", Types.VARCHAR, request.getParameter("CASE_CODE"), true);
                methodRec.addField("CASE_TYPE", Types.VARCHAR, request.getParameter("CASE_TYPE"));
                methodRec.addField("CAL_TYPE", Types.VARCHAR, request.getParameter("CAL_TYPE"));
                methodRec.addField("CASE_MAPPING_CODE", Types.VARCHAR, request.getParameter("CASE_MAPPING_CODE"));
                methodRec.addField("START_TIME", Types.VARCHAR, JDate.saveTime(request.getParameter("START_TIME")));
                methodRec.addField("END_TIME", Types.VARCHAR, JDate.saveTime(request.getParameter("END_TIME")));
                methodRec.addField("MAX_CASE", Types.NUMERIC, request.getParameter("MAX_CASE"));
                methodRec.addField("AMOUNT_PER_CASE", Types.NUMERIC, request.getParameter("AMOUNT_PER_CASE"));
                methodRec.addField("AMOUNT", Types.NUMERIC, request.getParameter("AMOUNT"));
                methodRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                methodRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                methodRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                methodRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
				
                if (MODE == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(methodRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/case_shift.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (MODE == DBMgr.MODE_UPDATE) {
                	
                    if (DBMgr.updateRecord(methodRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/case_shift.jsp"));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
            else if (request.getParameter("CASE_CODE") != null) {
                // Edit
                MODE = DBMgr.MODE_UPDATE;

                methodRec = DBMgr.getRecord(String.format("SELECT * FROM MST_SHIFT_CASE WHERE HOSPITAL_CODE = '%1$s' AND CASE_CODE = '%2$s'", session.getAttribute("HOSPITAL_CODE"), request.getParameter("CASE_CODE")));
                
                payorCateRec = DBMgr.getRecord(String.format("SELECT CODE, NAME_ENG FROM PAYOR_OFFICE_CATEGORY WHERE CODE = '%1$s'", DBMgr.getRecordValue(methodRec, "PAYOR_OFFICE_CATEGORY_CODE")));

                orderItemRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION_%1$s FROM ORDER_ITEM WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(), DBMgr.getRecordValue(methodRec, "ORDER_ITEM_CODE")));
            }
            else if (request.getParameter("CASE_CODE") == null) {
                // New
                MODE = DBMgr.MODE_INSERT;
                
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-ui-1.8.16.custom.min.js"></script>
        <script type="text/javascript">
            function CASE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                	Refresh_MAPPING_CASE();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function Refresh_MAPPING_CASE() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?CASE_CODE=' + document.mainForm.CASE_CODE.value;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?CASE_CODE=' + document.mainForm.CASE_CODE.value;
                }
            }

            function AJAX_Refresh_MAPPING_CASE() {
                var target = "../../RetrieveData?TABLE=MST_SHIFT_CASE&COND=CASE_CODE='" + document.mainForm.CASE_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_MAPPING_CASE);
            }
            
            function AJAX_Handle_Refresh_MAPPING_CASE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CASE_CODE")) {
                        document.mainForm.CASE_CODE.value = "";
                        document.mainForm.CASE_DESCRIPTION.value = "";
                        document.mainForm.CASE_DESCRIPTION.disabled = false;
                        return;
                    }else{
                    	// Data found

                        document.mainForm.CASE_TYPE.value = getXMLNodeValue(xmlDoc, "CASE_TYPE");
                        document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.value = getXMLNodeValue(xmlDoc, "PAYOR_OFFICE_CATEGORY_CODE");
                        document.mainForm.ORDER_ITEM_CODE.value = getXMLNodeValue(xmlDoc, "ORDER_ITEM_CODE");
                        document.mainForm.START_TIME.value = JDate.showTime(getXMLNodeValue(xmlDoc, "START_TIME"));
                        document.mainForm.END_TIME.value = JDate.showTime(getXMLNodeValue(xmlDoc, "END_TIME"));
                        document.mainForm.MAX_CASE.value = getXMLNodeValue(xmlDoc, "MAX_CASE");
                        document.mainForm.AMOUNT_PER_CASE.value = getXMLNodeValue(xmlDoc, "AMOUNT_PER_CASE");
                        document.mainForm.AMOUNT_PER_HOUR.value = getXMLNodeValue(xmlDoc, "AMOUNT_PER_HOUR");
                        if(getXMLNodeValue(xmlDoc, "ACTIVE") == "1"){
                        	document.mainForm.ACTIVE_1.value = "1";
                        	document.mainForm.ACTIVE_1.checked = "checked";
                        }else{
                        	document.mainForm.ACTIVE_0.value = "0";
                        	document.mainForm.ACTIVE_0.checked = "checked";
                        }

                        AJAX_Refresh_ORDER_ITEM();
                        AJAX_Refresh_PAYOR_OFFICE_CATEGORY();
                        
                        if(isXMLNodeExist(xmlDoc, "ORDER_ITEM_CODE")){
                        	document.mainForm.ORDER_ITEM_CODE.disabled = false;
                        }
                        
                    }
                }
            }
            
            function AJAX_Refresh_CASE_MAPPING() {
                var target = "../../RetrieveData?TABLE=STP_MAPPING_CASE&COND=CASE_MAPPING_CODE='" + document.mainForm.CASE_MAPPING_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_CASE_MAPPING);
            }
            
            function AJAX_Handle_Refresh_CASE_MAPPING() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CASE_MAPPING_CODE")) {
                        document.mainForm.CASE_MAPPING_CODE.value = "";
                        return;
                    }else{
                    	// Data found

                        document.mainForm.CASE_MAPPING_CODE.value = getXMLNodeValue(xmlDoc, "CASE_MAPPING_CODE");

                    }
                }
            }
            
            function ORDER_ITEM_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ORDER_ITEM_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_ORDER_ITEM() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" + document.mainForm.ORDER_ITEM_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_ORDER_ITEM);
            }
            
            function AJAX_Handle_ORDER_ITEM() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        document.mainForm.ORDER_ITEM_CODE.value = "";
                        document.mainForm.ORDER_ITEM_DESCRIPTION.value = "";
                        document.mainForm.ORDER_ITEM_CODE.disabled = false;
                    }
                    else {
                        // Data found
                        document.mainForm.ORDER_ITEM_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
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
                AJAX_Request(target, AJAX_Handle_PAYOR_OFFICE_CATEGORY);
            }
            
            function AJAX_Handle_PAYOR_OFFICE_CATEGORY() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.value = "";
                        document.mainForm.PAYOR_OFFICE_CATEGORY_DESCRIPTION.value = "";
                        
                    }
                    else {
                        // Data found
                        document.mainForm.PAYOR_OFFICE_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "NAME_ENG");
                        
                    }
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=MST_SHIFT_CASE&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>' AND CASE_CODE='" + document.mainForm.CASE_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Verify_Data);
            }
            
            function AJAX_Handle_Verify_Data() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "CASE_CODE");
                    
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
                                if (confirm("<%=labelMap.get(LabelMap.CONFIRM_ADD_NEW_DATA)%>")) {
                                    document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
                                    document.mainForm.submit();
                                }
                            }
                            break;
                    }
                }
            }

            function SAVE_Click() {
            	if(document.mainForm.CAL_TYPE.value == "OVER"){
            		if (!isObjectEmptyString(document.mainForm.CASE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                            !isObjectEmptyString(document.mainForm.CASE_TYPE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                            !isObjectEmptyString(document.mainForm.START_TIME, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                            !isObjectEmptyString(document.mainForm.END_TIME, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')&& 
                            isObjectValidNumber(document.mainForm.MAX_CASE, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                            isObjectValidNumber(document.mainForm.AMOUNT_PER_CASE, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                            !isObjectEmptyString(document.mainForm.MAX_CASE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                            !isObjectEmptyString(document.mainForm.AMOUNT_PER_CASE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')
                            ) {
                            AJAX_VerifyData();
                        }
            	}else{
            		if (!isObjectEmptyString(document.mainForm.CASE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                            !isObjectEmptyString(document.mainForm.CASE_TYPE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                            !isObjectEmptyString(document.mainForm.START_TIME, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                            !isObjectEmptyString(document.mainForm.END_TIME, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')&&  
                            !isObjectEmptyString(document.mainForm.CASE_MAPPING_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') 
                            ) {
                            AJAX_VerifyData();
                        }
            	}
                
            }
          
            function changeType(){
            	if(document.mainForm.CAL_TYPE.value == "OVER"){
            		document.mainForm.MAX_CASE.disabled = false;
            		document.mainForm.AMOUNT_PER_CASE.disabled = false;
            		document.mainForm.CASE_MAPPING_CODE.disabled = true;
            		document.mainForm.CASE_MAPPING_CODE.value = "";
            	}else if (document.mainForm.CAL_TYPE.value == "COMPARE"){
            		document.mainForm.MAX_CASE.disabled = true;
            		document.mainForm.AMOUNT_PER_CASE.disabled = true;
            		document.mainForm.CASE_MAPPING_CODE.disabled = false;
            	}
            }
            $(document).ready(function() {
            	changeType();
            });
            
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
-->
        </style>
</head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="case_shift.jsp">
        	<center>
                <table width="800" border="0">
                    <tr>
                    	<td align="left">
                	    	<b><font color='#003399'><%=Utils.getInfoPage("case_shift.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    	</td>
                    </tr>
				</table>
            </center>
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <table class="form">
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                    </th>
                </tr>
                <tr>
                  <td class="label"><label for="CASE_CODE"><span class="style1">${labelMap.CASE_CODE}*</span></label></td>
                    <td class="input" colspan="3">
                        <input name="CASE_CODE" type="text" class="short" id="CASE_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(methodRec, "CASE_CODE")%>" onkeypress="return CASE_CODE_KeyPress(event);" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=MST_SHIFT_CASE&RETURN_FIELD=CASE_CODE&DISPLAY_FIELD=START_TIME&displayField_second=END_TIME&BEINSIDEHOSPITAL=1&TARGET=CASE_CODE&HANDLE=Refresh_MAPPING_CASE'); return false;" />                   
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="CAL_TYPE"><span class="style1">${labelMap.CAL_TYPE}*</span></label></td>
                    <td colspan="3" class="input">
					<select class="medium" id="CAL_TYPE" name="CAL_TYPE" onchange="changeType()">
						<option value="OVER"<%= DBMgr.getRecordValue(methodRec, "CAL_TYPE").equalsIgnoreCase("OVER") ? " selected=\"selected\"" : "" %>>${labelMap.OVER}</option>
						<option value="COMPARE"<%= DBMgr.getRecordValue(methodRec, "CAL_TYPE").equalsIgnoreCase("COMPARE") ? " selected=\"selected\"" : "" %>>${labelMap.COMPARE}</option>
                    </select>
					</td>
                </tr>
                <tr>
                    <td class="label"><label for="START_TIME"><span class="style1">${labelMap.START_TIME}*</span></label></td>
                    <td colspan="" class="input">
                        <input name="START_TIME" type="text" class="short alignRight" id="START_TIME" maxlength="8" value="<%= JDate.showTime(DBMgr.getRecordValue(methodRec, "START_TIME")) %>" onchange="return checkKeyTime(this);" /> HHMM
                    </td>
                    <td class="label"><label for="END_TIME"><span class="style1">${labelMap.END_TIME}*</span></label></td>
                    <td colspan="" class="input">
                        <input name="END_TIME" type="text" class="short alignRight" id="END_TIME" maxlength="8" value="<%= JDate.showTime(DBMgr.getRecordValue(methodRec, "END_TIME")) %>" onchange="return checkKeyTime(this);" /> HHMM
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="AMOUNT"><span class="style1">${labelMap.AMOUNT}*</span></label></td>
                    <td colspan="3" class="input">
                        <input name="AMOUNT" type="text" class="short alignRight" id="AMOUNT" value="<%= DBMgr.getRecordValue(methodRec, "AMOUNT") %>" maxlength="13"  /> Baht
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="MAX_CASE">${labelMap.MAX_CASE}</label></td>
                    <td colspan="" class="input">
                        <input name="MAX_CASE" type="text" class="short alignRight" id="MAX_CASE" value="<%= DBMgr.getRecordValue(methodRec, "MAX_CASE") %>" maxlength="13"  />
                    </td>
                    <td class="label"><label for="AMOUNT_PER_CASE">${labelMap.AMOUNT_PER_CASE}</label></td>
                    <td colspan="" class="input">
                        <input name="AMOUNT_PER_CASE" type="text" class="short alignRight" id="AMOUNT_PER_CASE" value="<%= DBMgr.getRecordValue(methodRec, "AMOUNT_PER_CASE") %>"  maxlength="13" /> Baht
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="CASE_TYPE"><span class="style1">${labelMap.CASE_TYPE}*</span></label></td>
                    <td colspan="" class="input">
					<select class="medium" id="CASE_TYPE" name="CASE_TYPE">
						<option value="TRANSACTION"<%= DBMgr.getRecordValue(methodRec, "CASE_TYPE").equalsIgnoreCase("TRANSACTION") ? " selected=\"selected\"" : "" %>>${labelMap.TRANSACTION}</option>
						<option value="EPISODE"<%= DBMgr.getRecordValue(methodRec, "CASE_TYPE").equalsIgnoreCase("EPISODE") ? " selected=\"selected\"" : "" %>>${labelMap.EPISODE}</option>
					</select>
					</td>
                    <td class="label"><label for="CASE_MAPPING_CODE"><span class="style1">${labelMap.CASE_MAPPING_CODE}*</span></label></td>
                    <td class="input" colspan="3">
                        <input name="CASE_MAPPING_CODE" type="text" class="short" id="CASE_MAPPING_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(methodRec, "CASE_MAPPING_CODE")%>" onkeypress="return CASE_MAPPING_CODE_KeyPress(event);" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=STP_MAPPING_CASE&RETURN_FIELD=CASE_MAPPING_CODE&DISPLAY_FIELD=CASE_MAPPING_CODE&BEINSIDEHOSPITAL=1&TARGET=CASE_MAPPING_CODE&HANDLE=AJAX_Refresh_CASE_MAPPING'); return false;" />                   
                    </td>
                </tr>
               <%--  <tr>
                    <td class="label"><label for="PAYOR_OFFICE_CATEGORY_CODE"><span class="style1">${labelMap.PAYOR_OFFICE_CATEGORY_CODE}</span></label></td>
                    <td colspan="3" class="input">
                        <input name="PAYOR_OFFICE_CATEGORY_CODE" type="text" class="short" id="PAYOR_OFFICE_CATEGORY_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(payorCateRec, "CODE")%>" onkeypress="return PAYOR_OFFICE_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE_CATEGORY();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE_CATEGORY&DISPLAY_FIELD=NAME_ENG&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PAYOR_OFFICE_CATEGORY_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE_CATEGORY'); return false;" />            
                    	<input name="PAYOR_OFFICE_CATEGORY_DESCRIPTION" type="text" readonly="readonly" class="mediumMax" id="PAYOR_OFFICE_CATEGORY_DESCRIPTION" value="<%= DBMgr.getRecordValue(payorCateRec, "NAME_ENG")%>" maxlength="255" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ORDER_ITEM_CODE"><span class="style1">${labelMap.ORDER_ITEM_CODE}</span></label></td>
                    <td colspan="3" class="input">
                        <input name="ORDER_ITEM_CODE" type="text" class="short" id="ORDER_ITEM_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(orderItemRec, "CODE")%>" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM();" <%= DBMgr.getRecordValue(methodRec, "CASE_TYPE").equalsIgnoreCase("ALL") ? "" : "disabled=\"disabled\"" %>/>
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_ENG&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=ORDER_ITEM_CODE&HANDLE=AJAX_Refresh_ORDER_ITEM'); return false;" />            
                    	<input name="ORDER_ITEM_DESCRIPTION" type="text" readonly="readonly" class="mediumMax" id="ORDER_ITEM_DESCRIPTION" value="<%= DBMgr.getRecordValue(orderItemRec, "DESCRIPTION_ENG")%>" maxlength="255" />
                    </td>
                </tr> --%>
                
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(methodRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(methodRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(methodRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
