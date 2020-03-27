<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>

<%@ include file="../../_global.jsp" %>
<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_RECEIPT)) {
                response.sendRedirect("../message.jsp");
                return;
            }

            //
            // Initial LabelMap
            //

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            String dt = JDate.showDate(JDate.getDate());
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Hold Receipt by Details", "ยกเลิกการรับชำระเงินตามรายการค่าแพทย์");
            labelMap.add("INVOICE_NO", "Invoice No", "เลขที่ใบแจ้งหนี้");
            labelMap.add("INVOICE_DATE", "Invoice Date", "วันที่ใบแจ้งหนี้");
            labelMap.add("START_DATE", "Invoice Start Date", "วันที่ใบแจ้งหนี้เริ่ม");
            labelMap.add("END_DATE", "Invoice End Date", "วันที่ใบแจ้งหนี้สิ้นสุด");
			labelMap.add("RECEIPT_DATE", "Receipt Date", "วันที่รับชำระ");
            labelMap.add("PAYOR_OFFICE_CODE", "Payor Office", "บริษัทคู่สัญญา");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รหัสรายการรักษา");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("HN_NO", "HN", "รหัสผู้ป่วย");
            labelMap.add("PATIENT_NAME", "Patient Name", "ชื่อผู้ป่วย");
            labelMap.add("AMOUNT_AFT_DISCOUNT", "Amount", "ยอดเงิน");
            labelMap.add("M_HOLD", "Hold", "ยกเลิกการรับชำระ");
            labelMap.add("TITLE_DATA", "Data List", "รายการข้อมูล");
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord payorOfficeRec = null, doctorRec = null, hnNoRec = null, orderCode = null;
            String query = "";
            
            if (request.getParameter("PAYOR_OFFICE_CODE") != null && !request.getParameter("PAYOR_OFFICE_CODE").equalsIgnoreCase("")) {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM PAYOR_OFFICE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("PAYOR_OFFICE_CODE") + "'";
                payorOfficeRec = DBMgr.getRecord(query);
            }
            
			if (request.getParameter("DOCTOR_CODE") != null) {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("DOCTOR_CODE") + "'";
                doctorRec = DBMgr.getRecord(query);
            }
			
            if (request.getParameter("HN_NO") != null && !request.getParameter("HN_NO").equalsIgnoreCase("")) {
                query = "SELECT HN_NO, PATIENT_NAME FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND HN_NO = '" + request.getParameter("HN_NO") + "'";
                hnNoRec = DBMgr.getRecord(query);
            }
			
            if (request.getParameter("ORDER_ITEM_CODE") != null && !request.getParameter("ORDER_ITEM_CODE").equalsIgnoreCase("")) {
                query = "SELECT * FROM ORDER_ITEM WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("ORDER_ITEM_CODE") + "'";
                orderCode = DBMgr.getRecord(query);
            }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">
            
            function INVOICE_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.INVOICE_NO.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_INVOICE() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=INVOICE_NO='" + document.mainForm.INVOICE_NO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_INVOICE);
            }
            
            function AJAX_Handle_Refresh_INVOICE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "INVOICE_NO")) {
                        document.mainForm.INVOICE_NO.value = "";
                        return;
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

            function AJAX_Refresh_ORDER_ITEM_CODE() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" + document.mainForm.ORDER_ITEM_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM_CODE);
            }
            
            function AJAX_Handle_Refresh_ORDER_ITEM_CODE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.ORDER_ITEM_CODE.value = "";
                        document.mainForm.ORDER_ITEM_NAME.value = "";
                        return;
                    }
                    // Data found
                    document.mainForm.ORDER_ITEM_NAME.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
            function PAYOR_OFFICE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.PAYOR_OFFICE_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_PAYOR_OFFICE() {
                var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.PAYOR_OFFICE_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE);
            }
            
            function AJAX_Handle_Refresh_PAYOR_OFFICE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.PAYOR_OFFICE_CODE.value = "";
                        document.mainForm.PAYOR_OFFICE_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PAYOR_OFFICE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
            function DOCTOR_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
            function HN_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.HN_NO.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_HN_NO() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=HN_NO='" + document.mainForm.HN_NO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_HN_NO);
            }
            
            function AJAX_Handle_Refresh_HN_NO() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "HN_NO")) {
                        document.mainForm.HN_NO.value = "";
                        document.mainForm.PATIENT_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PATIENT_NAME.value = getXMLNodeValue(xmlDoc, "PATIENT_NAME");
                }
            }

            function updateAllCheckBox() 
            { 
                for (var i = 0; i < document.dataForm.elements.length; i++) { 
                    var x = document.dataForm.elements[i]; 
                    if (x.type == 'checkbox') { 
                        x.checked = document.dataForm.allCheckBox.checked; 
                    } 
                } 
            } 
			
			function hold(){
				document.dataForm.submit();
			}
            
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="hold_receipt_by_detail.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_INSERT%>" />
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("hold_receipt_by_detail.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                <tr>
                    <td class="label">
                        <label for="INVOICE_DATE">${labelMap.START_DATE}</label>
                    </td>
                    <td class="input">
                        <input name="START_DATE" type="text" class="short" id="START_DATE" maxlength="10" value="<%= request.getParameter("START_DATE") != null ? request.getParameter("START_DATE") : "" %>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('START_DATE'); return false;" />
                    </td>
                    <td class="label">
                        <label for="END_DATE">${labelMap.END_DATE}</label>
                    </td>
                    <td class="input">
                        <input name="END_DATE" type="text" class="short" id="END_DATE" maxlength="10" value="<%= request.getParameter("END_DATE") != null ? request.getParameter("END_DATE") : "" %>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('END_DATE'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="INVOICE_NO">${labelMap.INVOICE_NO} *</label></td>
                    <td class="input" colspan="3">
                        <input type="text" id="INVOICE_NO" name="INVOICE_NO" class="short" maxlength="20" value="<%= request.getParameter("INVOICE_NO") != null ? request.getParameter("INVOICE_NO") : "" %>" onkeypress="return INVOICE_NO_KeyPress(event);" onblur="AJAX_Refresh_INVOICE();" />
                        <input id="SEARCH_INVOICE_NO" name="SEARCH_INVOICE_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=TRN_DAILY&RETURN_FIELD=INVOICE_NO&DISPLAY_FIELD=INVOICE_DATE&BEINSIDEHOSPITAL=1&TARGET=INVOICE_NO&HANDLE=AJAX_Refresh_INVOICE'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="ORDER_ITEM_CODE" type="text" class="short" id="ORDER_ITEM_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(orderCode, "CODE") %>" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM_CODE();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_<%= labelMap.getFieldLangSuffix() %>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=ORDER_ITEM_CODE&HANDLE=AJAX_Refresh_ORDER_ITEM_CODE'); return false;" />
                        <input name="ORDER_ITEM_NAME" type="text" class="long" id="ORDER_ITEM_NAME" readonly="readonly" value="<%= DBMgr.getRecordValue(orderCode, "DESCRIPTION_"+labelMap.getFieldLangSuffix()) %>" maxlength="255" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="PAYOR_OFFICE_CODE">${labelMap.PAYOR_OFFICE_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="PAYOR_OFFICE_CODE" type="text" class="short" id="PAYOR_OFFICE_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(payorOfficeRec, "CODE") %>" onkeypress="return PAYOR_OFFICE_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PAYOR_OFFICE_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE'); return false;" />
                        <input name="PAYOR_OFFICE_NAME" type="text" class="long" id="PAYOR_OFFICE_NAME" readonly="readonly" value="<%= DBMgr.getRecordValue(payorOfficeRec, "NAME") %>" maxlength="255" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
                        <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="HN_NO">${labelMap.HN_NO}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="HN_NO" name="HN_NO" class="short" value="<%= DBMgr.getRecordValue(hnNoRec, "HN_NO") %>" onkeypress="return HN_NO_KeyPress(event);" onblur="AJAX_Refresh_HN_NO();" />
                        <input id="SEARCH_HN_NO" name="SEARCH_HN_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=TRN_DAILY&RETURN_FIELD=HN_NO&DISPLAY_FIELD=PATIENT_NAME&BEINSIDEHOSPITAL=1&TARGET=HN_NO&HANDLE=AJAX_Refresh_HN_NO'); return false;" />
                        <input type="text" id="PATIENT_NAME" name="PATIENT_NAME" value="<%= DBMgr.getRecordValue(hnNoRec, "PATIENT_NAME") %>" class="long" readonly="readonly" />
                    </td>
                </tr>
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="submit" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" />
                        <input type="button" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='receipt_by_detail.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
        <hr />
        <form id="dataForm" name="dataForm" method="post" action="hold_receipt_by_detail_update.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= DBMgr.MODE_QUERY %>" />
            <table class="data">
                <tr>
                    <td class="sub_head"><input type="checkbox" id="allCheckBox" name="allCheckBox" onclick="updateAllCheckBox()" /></td>
                    <td class="sub_head">${labelMap.INVOICE_NO}</td>
                    <td class="sub_head">${labelMap.INVOICE_DATE}</td>
                    <td class="sub_head">${labelMap.PAYOR_OFFICE_CODE}</td>
                    <td class="sub_head">${labelMap.DOCTOR_CODE}</td>
                    <td class="sub_head">${labelMap.HN_NO}</td>
                    <td class="sub_head">${labelMap.PATIENT_NAME}</td>
                    <td class="sub_head">${labelMap.AMOUNT_AFT_DISCOUNT}</td>
                </tr>
                <%
                if (request.getParameter("MODE") != null) {

                    String cond = "";
                    if (request.getParameter("INVOICE_NO") != null && !request.getParameter("INVOICE_NO").equalsIgnoreCase("")) {
                        cond += " AND INVOICE_NO = '" + request.getParameter("INVOICE_NO") + "'";
                    }
                    if (request.getParameter("START_DATE") != null && !request.getParameter("START_DATE").equalsIgnoreCase("")) {
                        if(request.getParameter("END_DATE") != null && !request.getParameter("END_DATE").equalsIgnoreCase("")) {
                            cond += " AND (INVOICE_DATE BETWEEN '" + JDate.saveDate(request.getParameter("START_DATE")) + "' AND '"
                            + JDate.saveDate(request.getParameter("END_DATE"))+"')";                        	
                        }else{
                        	cond += " AND (INVOICE_DATE BETWEEN '" + JDate.saveDate(request.getParameter("START_DATE")) + "' AND '"
                            + JDate.saveDate(request.getParameter("START_DATE"))+"')";   
                        }
                    }
                    if (request.getParameter("ORDER_ITEM_CODE") != null && !request.getParameter("ORDER_ITEM_CODE").equalsIgnoreCase("")) {
                        cond += " AND ORDER_ITEM_CODE = '" + request.getParameter("ORDER_ITEM_CODE") + "'";
                    }
                    if (request.getParameter("PAYOR_OFFICE_CODE") != null && !request.getParameter("PAYOR_OFFICE_CODE").equalsIgnoreCase("")) {
                        cond += " AND PAYOR_OFFICE_CODE = '" + request.getParameter("PAYOR_OFFICE_CODE") + "'";
                    }
                    if (request.getParameter("DOCTOR_CODE") != null && !request.getParameter("DOCTOR_CODE").equalsIgnoreCase("")) {
                        cond += " AND DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") + "'";
                    }
                    if (request.getParameter("HN_NO") != null && !request.getParameter("HN_NO").equalsIgnoreCase("")) {
                        cond += " AND HN_NO = '" + request.getParameter("HN_NO") + "'";
                    }
        			DBConnection con = new DBConnection();
                    con.connectToLocal();
                    query = "SELECT INVOICE_NO, INVOICE_DATE, LINE_NO, PAYOR_OFFICE_CODE, DOCTOR_CODE, HN_NO, PATIENT_NAME, AMOUNT_AFT_DISCOUNT FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND IS_ONWARD <> 'Y'  AND ACTIVE = '1' AND (PAY_BY_PAYOR = 'Y' OR PAY_BY_CASH = 'Y' OR PAY_BY_CASH_AR = 'Y' OR PAY_BY_DOCTOR = 'Y' OR PAY_BY_AR = 'Y') AND (BATCH_NO IS NULL OR BATCH_NO = '')" + cond;
                    System.out.println(query);
                    ResultSet rs = con.executeQuery(query);
                    int i = 0;
                    while (rs.next()) {
                %>
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><input type="checkbox" id="INVOICE_NO_<%=rs.getString("INVOICE_NO") + "_" + rs.getString("LINE_NO")%>" name="INVOICE_NO_<%=rs.getString("INVOICE_NO") + "_" + rs.getString("LINE_NO")%>" value="<%=rs.getString("INVOICE_NO") + ":" + rs.getString("LINE_NO")%>" /></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("INVOICE_NO"), true)%></td>
                    <td class="row<%=i % 2%>"><%= JDate.showDate(rs.getString("INVOICE_DATE"))%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("PAYOR_OFFICE_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("DOCTOR_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("HN_NO"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("PATIENT_NAME"), true)%></td>
                    <td class="row<%=i % 2%>" align="right"><%= Util.formatHTMLString(rs.getString("AMOUNT_AFT_DISCOUNT"), true)%></td>
                </tr>
                <%
                        i++;
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    con.Close();
                    //con.freeConnection();
                }
                %>                
                <tr>
                    <th colspan="8" class="buttonBar">                        
                        <input type="button" id="UPDATE" name="UPDATE" class="button" value="${labelMap.M_HOLD}" onclick="hold();"/>
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>