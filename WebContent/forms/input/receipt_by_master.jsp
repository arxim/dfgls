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
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Receipt by Invoice", "รับชำระเงิน Invoice ทั้งใบ");
            labelMap.add("INVOICE_NO", "Invoice No", "เลขที่ใบแจ้งหนี้");
            labelMap.add("INVOICE_DATE", "Invoice Date", "วันที่ตั้งหนี้");
			labelMap.add("RECEIPT_DATE", "Receipt Date", "วันที่รับชำระ");
            labelMap.add("PAYOR_OFFICE_CODE", "Payor Office", "บริษัทคู่สัญญา");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("HN_NO", "HN No", "ผู้ป่วย");
            
            labelMap.add("PATIENT_NAME", "Patient Name", "ชื่อผู้ป่วย");
            labelMap.add("TOTAL_BILL_AMOUNT", "Amount", "Amount");
            
            labelMap.add("TITLE_DATA", "Data List", "รายการข้อมูล");
            labelMap.add("M_RECEIPT", "Receipt", "รับชำระ");
            labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "Admission Type");
            
            labelMap.add("INVOICE_START_DATE", "Invoice start date", "Invoice start date");
            labelMap.add("INVOICE_END_DATE", "Invoice end date", "Invoice end date");
            
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord payorOfficeRec = null, hnNoRec = null;
            String query = "";
            
            if (request.getParameter("PAYOR_OFFICE_CODE") != null && !request.getParameter("PAYOR_OFFICE_CODE").equalsIgnoreCase("")) {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM PAYOR_OFFICE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("PAYOR_OFFICE_CODE") + "'";
                payorOfficeRec = DBMgr.getRecord(query);
            }
            if (request.getParameter("HN_NO") != null && !request.getParameter("HN_NO").equalsIgnoreCase("")) {
                query = "SELECT HN_NO, PATIENT_NAME FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND HN_NO = '" + request.getParameter("HN_NO") + "'";
                hnNoRec = DBMgr.getRecord(query);
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
                    
                    // Data found
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
            function receipt(){
				if(document.dataForm.RECEIPT_DATE.value == ""){
					alert("Please Select Receipt Date");
					document.dataForm.RECEIPT_DATE.focus();
				}else{
                    document.dataForm.submit();
				}
			}
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="receipt_by_master.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_INSERT%>" />
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("receipt_by_master.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
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
            		 <td class="label">
                        <label for="INVOICE_START_DATE">${labelMap.INVOICE_START_DATE}</label>
                    </td>
                    <td class="input">
                        <input name="INVOICE_START_DATE" type="text" class="short" id="INVOICE_START_DATE" maxlength="10" value="<%= request.getParameter("INVOICE_START_DATE") != null ? request.getParameter("INVOICE_START_DATE") : "" %>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('INVOICE_START_DATE'); return false;" />
                    </td>
                    
                     <td class="label">
                        <label for="INVOICE_END_DATE">${labelMap.INVOICE_END_DATE}</label>
                    </td>
                    <td class="input">
                        <input name="INVOICE_END_DATE" type="text" class="short" id="INVOICE_END_DATE" maxlength="10" value="<%= request.getParameter("INVOICE_END_DATE") != null ? request.getParameter("INVOICE_END_DATE") : "" %>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('INVOICE_END_DATE'); return false;" />
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
                        <label for="PAYOR_OFFICE_CODE">${labelMap.PAYOR_OFFICE_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="PAYOR_OFFICE_CODE" type="text" class="short" id="PAYOR_OFFICE_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(payorOfficeRec, "CODE") %>" onkeypress="return PAYOR_OFFICE_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PAYOR_OFFICE_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE'); return false;" />
                        <input name="PAYOR_OFFICE_NAME" type="text" class="long" id="PAYOR_OFFICE_NAME" readonly="readonly" value="<%= DBMgr.getRecordValue(payorOfficeRec, "NAME") %>" maxlength="255" />                    
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
                <td class="label"><label for="ADMISSION_TYPE_CODE"><span class="style1">${labelMap.ADMISSION_TYPE_CODE}</span></label></td>
	                <td class="input" colspan="3">
	                        <select id="ADMISSION_TYPE_CODE" name="ADMISSION_TYPE_CODE" class="medium">
	                         	<option value="ALL" > ALL </option>
	                            <%= DBMgr.generateOptionList("inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM ADMISSION_TYPE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", request.getParameter("ADMISSION_TYPE_CODE") == null ? "" : request.getParameter("ADMISSION_TYPE_CODE") ,true)%>
	                        </select>
	                 </td>
                </tr>    
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="submit" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" />
                        <input type="button" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='receipt_by_master.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
        <hr/>
        <form id="dataForm" name="dataForm" method="post" action="receipt_by_master_update.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= DBMgr.MODE_QUERY %>" />
			<!--<input type="hidden" id="RECEIPT_DATE" name="RECEIPT_DATE" value="20081201" />-->
            <table class="data">
                <tr>
					<th colspan="8">
				  	<!--<div style="float: left;">${labelMap.TITLE_DATA}</div>-->
					<div style="float: right;" id="Receipt" name="Receipt">
				  		<label for="RECEIPT_DATE">${labelMap.RECEIPT_DATE}</label>
						<input type="text" name="RECEIPT_DATE" id="RECEIPT_DATE" class="short" maxlength="10" value="<%= request.getParameter("RECEIPT_DATE") != null ? request.getParameter("RECEIPT_DATE") : "" %>" />
            			<input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('RECEIPT_DATE'); return false;" />
					</div></th>
                </tr>
                <tr>
                    <td class="sub_head"><input type="checkbox" id="allCheckBox" name="allCheckBox" onclick="updateAllCheckBox()" /></td>
                    <td class="sub_head">${labelMap.ADMISSION_TYPE_CODE}</td>
                    <td class="sub_head">${labelMap.INVOICE_NO}</td>
                    <td class="sub_head">${labelMap.INVOICE_DATE}</td>
                    <td class="sub_head">${labelMap.PAYOR_OFFICE_CODE}</td>
                    <td class="sub_head">${labelMap.HN_NO}</td>
                    <td class="sub_head">${labelMap.PATIENT_NAME}</td>
                    <td class="sub_head">${labelMap.TOTAL_BILL_AMOUNT}</td>
                </tr>
                <%
                if (request.getParameter("MODE") != null) {

                    String cond = "";
                    if (request.getParameter("INVOICE_NO") != null && !request.getParameter("INVOICE_NO").equalsIgnoreCase("")) {
                        cond += " AND INVOICE_NO = '" + request.getParameter("INVOICE_NO") + "'";
                    }
                    
                    if (request.getParameter("INVOICE_START_DATE") != null && !request.getParameter("INVOICE_START_DATE").equalsIgnoreCase("") && request.getParameter("INVOICE_END_DATE") != null && !request.getParameter("INVOICE_END_DATE").equalsIgnoreCase("")) {
                        cond += " AND ( INVOICE_DATE >= '" + JDate.saveDate(request.getParameter("INVOICE_START_DATE")) + "' AND INVOICE_DATE <= '" + JDate.saveDate(request.getParameter("INVOICE_END_DATE")) + "' ) ";
                    }
                    
                    if (request.getParameter("PAYOR_OFFICE_CODE") != null && !request.getParameter("PAYOR_OFFICE_CODE").equalsIgnoreCase("")) {
                        cond += " AND PAYOR_OFFICE_CODE = '" + request.getParameter("PAYOR_OFFICE_CODE") + "'";
                    }
                    
                    if (request.getParameter("HN_NO") != null && !request.getParameter("HN_NO").equalsIgnoreCase("")) {
                        cond += " AND HN_NO = '" + request.getParameter("HN_NO") + "'";
                    }
                    
                    if (request.getParameter("ADMISSION_TYPE_CODE") != null && !request.getParameter("ADMISSION_TYPE_CODE").equalsIgnoreCase("")) {
                    	
                    	if (!request.getParameter("ADMISSION_TYPE_CODE").equals("ALL") ) { 
                    		cond += " AND ADMISSION_TYPE_CODE = '" + request.getParameter("ADMISSION_TYPE_CODE") + "'";
                    	}
                    	
                    } else { 
                    	 cond += "";
                    }
                    
        			DBConnection con = new DBConnection();
                    con.connectToLocal();
                    query = "SELECT DISTINCT ADMISSION_TYPE_CODE ,  INVOICE_NO, INVOICE_DATE, PAYOR_OFFICE_CODE, HN_NO, PATIENT_NAME, TOTAL_BILL_AMOUNT FROM TRN_DAILY WHERE TRANSACTION_TYPE = 'INV' AND INVOICE_TYPE <> 'ORDER' AND HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND YYYY = '' AND ACTIVE = '1' AND (RECEIPT_NO IS NULL OR RECEIPT_NO = '') AND (BATCH_NO IS NULL OR BATCH_NO = '')" + cond;
                   
                    System.out.println("info Query" + query);
                    
                    ResultSet rs = con.executeQuery(query);
                    int i = 0;
                    while (rs.next()) {
                %>                
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><input type="checkbox" id="INVOICE_NO_<%=rs.getString("INVOICE_NO")%>" name="INVOICE_NO_<%=rs.getString("INVOICE_NO")%>" value="<%=rs.getString("INVOICE_NO")%>" /></td>
                    <td class="row<%=i % 2%>" style="text-align: center;"><%= Util.formatHTMLString(rs.getString("ADMISSION_TYPE_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("INVOICE_NO"), true)%></td>
                    <td class="row<%=i % 2%>"><%= JDate.showDate(rs.getString("INVOICE_DATE"))%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("PAYOR_OFFICE_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("HN_NO"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("PATIENT_NAME"), true)%></td>
                    <td class="row<%=i % 2%>" align="right"><%= Util.formatHTMLString(rs.getString("TOTAL_BILL_AMOUNT"), true)%></td>
                </tr>
                <%
                        i++;
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    con.Close();
                }
                %>                
                <tr>
                    <th colspan="8" class="buttonBar">                        
                        <input type="button" id="UPDATE" name="UPDATE" class="button" value="${labelMap.M_RECEIPT}" onclick="receipt();"/>
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
