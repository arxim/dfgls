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
            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_BANK)) {
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
            labelMap.add("TITLE_MAIN", "Bank", "ธนาคาร");
            labelMap.add("CODE", "Code", "รหัส");
            labelMap.add("DESCRIPTION_THAI", "Description (Thai)", "ชื่อ (ไทย)");
            labelMap.add("DESCRIPTION_ENG", "Description (Eng)", "ชื่อ (อังกฤษ)");
            labelMap.add("SWIFT_CODE", "Swift Code", "Swift Code");
            labelMap.add("COUNTRY_CODE", "Country", "ประเทศ");
            //labelMap.add("ACTIVE", "Status", "สถานะ");
            //labelMap.add("ACTIVE_0", "Inactive", "ไม่ใช้งาน");
            //labelMap.add("ACTIVE_1", "Active", "ใช้งาน");
            labelMap.add("TITLE_DATA", "Bank Branch", "สาขาธนาคาร");

            labelMap.add("ALERT_REQUIRED_CODE", "Please fill bank code", "กรุณาป้อนรหัสธนาคาร");
            labelMap.add("ALERT_REQUIRED_DESCRIPTION_THAI", "Please fill description (Thai)", "กรุณาป้อนชื่อ (ไทย)");
            labelMap.add("ALERT_REQUIRED_DESCRIPTION_ENG", "Please fill description (Eng)", "กรุณาป้อนชื่อ (อังกฤษ)");

            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord bankRec = null, countryRec = null;
            byte MODE = DBMgr.MODE_INSERT;
			String getcode = "";
			String codescript = "";
            if (request.getParameter("MODE") != null) {

                //out.print(request.getParameterMap().toString());

                DataRecord record = new DataRecord("BANK");

                record.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                record.addField("DESCRIPTION_THAI", Types.VARCHAR, request.getParameter("DESCRIPTION_THAI"));
                record.addField("DESCRIPTION_ENG", Types.VARCHAR, request.getParameter("DESCRIPTION_ENG"));
                record.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                record.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                record.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                record.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
               	record.addField("COUNTRY_CODE", Types.VARCHAR, request.getParameter("COUNTRY_CODE").toString(), true);
                record.addField("SWIFT_CODE", Types.VARCHAR, request.getParameter("SWIFT_CODE").toString());

                if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(record)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/bank.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(record)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/bank.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
            else if (request.getParameter("CODE") != null) {
            	bankRec = DBMgr.getRecord("SELECT * FROM BANK WHERE CODE = '" + request.getParameter("CODE") + "' AND COUNTRY_CODE ='"+ request.getParameter("COUNTRY_CODE") + "' ");
                if (bankRec == null) {
                    MODE = DBMgr.MODE_INSERT;
					getcode = request.getParameter("CODE");
					codescript = "<script language=\"javascript\">";
					codescript+= "	alert('Data Not Found');";
					codescript+= "</script>";
                }
                else {
                    MODE = DBMgr.MODE_UPDATE;
					getcode = DBMgr.getRecordValue(bankRec, "CODE");
                }
            }
            DBConnection con = new DBConnection();
            con.connectToLocal();	
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
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
            
            function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    Refresh_BANK();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function Refresh_BANK() {
                var to = document.location.pathname.lastIndexOf('?');
                var code;
                var country_code;
                if (to < 0) {
                	if(!(/^\d+$/.test(document.mainForm.CODE.value))){
                		code = document.mainForm.CODE.value.substring(2, document.mainForm.CODE.value.length);
                        country_code = document.mainForm.CODE.value.substring(0, 2);
                	}
                	else{
                		code = document.mainForm.CODE.value;
                        country_code = document.mainForm.COUNTRY_CODE.value;
                	}
                    window.location = document.location.pathname + '?CODE=' +code +'&COUNTRY_CODE='+ country_code;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?CODE=' + document.mainForm.CODE.value+'&COUNTRY_CODE='+ document.mainForm.COUNTRY_CODE.value;
                }
            }
            
            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=BANK&COND=CODE='" + document.mainForm.CODE.value + "' AND COUNTRY_CODE='" + document.mainForm.COUNTRY_CODE.value+ "' ";
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
            	//alert(document.mainForm.COUNTRY_CODE.value);
                if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_ENG, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.COUNTRY_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                removeAllDataRow();
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.value = '';
                document.mainForm.CODE.readOnly = false;
                document.mainForm.DESCRIPTION_THAI.value = '';
                document.mainForm.DESCRIPTION_ENG.value = '';
                document.mainForm.ACTIVE_1.checked = true;
                document.mainForm.COUNTRY_CODE.value = '';
                document.mainForm.SWIFT_CODE.value = '';
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
        <form id="mainForm" name="mainForm" method="post" action="bank.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
            <center>
			<table width="800" border="0">
			<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("bank.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
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
                	<td class="labelRequest"><label for="COUNTRY_CODE">${labelMap.COUNTRY_CODE} *</label></td>
					<td colspan="" class="input">
                        <%= DBMgr.generateDropDownList("COUNTRY_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION_ENG FROM COUNTRY WHERE ACTIVE = '1' ORDER BY DESCRIPTION_ENG", "DESCRIPTION_ENG", "CODE", DBMgr.getRecordValue(bankRec, "COUNTRY_CODE")) %>
					</td>
                </tr>
                <tr>
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.CODE} *</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" value="<%= getcode %>"<%= MODE == DBMgr.MODE_UPDATE ? " readonly=\"readonly\"" : "" %> onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=BANK&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&TARGET=CODE&HANDLE=Refresh_BANK'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_ENG"><span class="style1">${labelMap.DESCRIPTION_ENG} *</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DESCRIPTION_ENG" name="DESCRIPTION_ENG" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(bankRec, "DESCRIPTION_ENG") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_THAI"><span class="style1">${labelMap.DESCRIPTION_THAI} *</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DESCRIPTION_THAI" name="DESCRIPTION_THAI" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(bankRec, "DESCRIPTION_THAI") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="SWIFT_CODE">${labelMap.SWIFT_CODE} </label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="SWIFT_CODE" name="SWIFT_CODE" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(bankRec, "SWIFT_CODE") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(bankRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(bankRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(bankRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
            <hr />
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="4" class="alignLeft">${labelMap.TITLE_DATA}</th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("DESCRIPTION_" + labelMap.getFieldLangSuffix())%></td>
                    <td class="sub_head"><%=labelMap.get("ACTIVE")%></td>
                    <td class="sub_head"><%=labelMap.get(LabelMap.EDIT)%></td>
                </tr>
                <%

            
            ResultSet rs = con.executeQuery("SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + ", ACTIVE FROM BANK_BRANCH WHERE BANK_CODE = '" + DBMgr.getRecordValue(bankRec, "CODE") + "' AND BANK_COUNTRY_CODE = '"+ DBMgr.getRecordValue(bankRec, "COUNTRY_CODE") +"' ORDER BY ACTIVE DESC");
            int i = 0;
            String linkEdit;
            while (rs.next()) {
                linkEdit = "<a href=\"bank_branch.jsp?BANK_CODE=" + DBMgr.getRecordValue(bankRec, "CODE") + "&CODE=" + rs.getString("CODE") + "&BANK_COUNTRY_CODE=" + DBMgr.getRecordValue(bankRec, "COUNTRY_CODE") + "\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"" + labelMap.get(LabelMap.EDIT) + "\" /></a>";
                String status = "";
                if("1".equals(Util.formatHTMLString(rs.getString("ACTIVE"), true))){
                    status = "<input type='checkbox' name='' value='' checked disabled>";
                }
                %>                
                <tr>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("DESCRIPTION_" + labelMap.getFieldLangSuffix()), true)%></td>
                    <td class="row<%=i % 2%>" align="center"><%=status %></td>
                    <td class="row<%=i % 2%>" align="center"><%= linkEdit%></td>
                </tr>
                <%
                i++;
            }
            if (rs != null) {
                rs.close();
            }
            con.Close();
                %>                
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}"<%= MODE == DBMgr.MODE_UPDATE ? "" : " disabled=\"disabled\""%> onclick="window.location = 'bank_branch.jsp?BANK_CODE=' + document.mainForm.CODE.value+'&BANK_COUNTRY_CODE='+document.mainForm.COUNTRY_CODE.value;" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<%=codescript%>