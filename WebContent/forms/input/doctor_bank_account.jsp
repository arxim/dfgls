<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
			// Short Page Profile Doctor
			// Verify permission
			//

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_DOCTOR_PROFILE)) {
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
            labelMap.add("TITLE_MAIN", "Doctor Bank Account", "บัญชีแพทย์");
            labelMap.add("TITLE_DETAIL", "Bank Account", "บัญชี");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("NAME_THAI", "Name (Thai)", "ชื่อ (ไทย)");
            labelMap.add("NAME_ENG", "Name (Eng)", "ชื่อ (อังกฤษ)");
            labelMap.add("DOCTOR_NAME","Doctor Name","ชื่อแพทย์");
            labelMap.add("BANK_ACCOUNT_NO", "Bank Account No.", "เลขที่บัญชีธนาคาร");
            labelMap.add("BANK", "Bank", "ธนาคาร");
            labelMap.add("BANK_ACCOUNT_NAME", "Account Name", "ชื่อบัญชี");
            labelMap.add("BANK_CODE", "Bank Code", "รหัสธนาคาร");
            labelMap.add("BANK_BRANCH_CODE", "Branch Code", "รหัสสาขา");
            labelMap.add("BANK_COUNTRY_CODE","Country","ประเทศ");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
			//
			// Process request
			//

            request.setCharacterEncoding("UTF-8");
            DataRecord doctorBankAccountRec = null,doctorBankAccountRecEdit = null,  doctorBankAccountRecOld  = null , doctorBankAccountRecLog = null, bankRec = null, bankBranchRec = null ;
            byte MODE = DBMgr.MODE_INSERT;
            String readonlyCheck = "";

            if (request.getParameter("MODE") != null) {
                MODE = Byte.parseByte(request.getParameter("MODE"));
                doctorBankAccountRec = new DataRecord("DOCTOR_BANK_ACCOUNT");
                
                
                doctorBankAccountRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                doctorBankAccountRec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CODE"), true);
                doctorBankAccountRec.addField("BANK_ACCOUNT_NO", Types.VARCHAR, request.getParameter("BANK_ACCOUNT_NO"), true);
                doctorBankAccountRec.addField("BANK_ACCOUNT_NAME", Types.VARCHAR, request.getParameter("BANK_ACCOUNT_NAME"));
                doctorBankAccountRec.addField("BANK_COUNTRY_CODE", Types.VARCHAR, request.getParameter("BANK_COUNTRY_CODE"));
                doctorBankAccountRec.addField("BANK_CODE", Types.VARCHAR, request.getParameter("BANK_CODE"));
                doctorBankAccountRec.addField("BANK_BRANCH_CODE", Types.VARCHAR, request.getParameter("BANK_BRANCH_CODE"));
                doctorBankAccountRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                doctorBankAccountRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
            	doctorBankAccountRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
            	doctorBankAccountRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if (MODE == DBMgr.MODE_INSERT) {
                    if (DBMgr.insertRecord(doctorBankAccountRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/doctor_bank_account.jsp?DOCTOR_PROFILE_CODE="+ request.getParameter("DOCTOR_PROFILE_CODE")+"&DOCTOR_CODE=" + doctorBankAccountRec.getField("DOCTOR_CODE").getValue()+"&BANK_ACCOUNT_NO="+doctorBankAccountRec.getField("BANK_ACCOUNT_NO").getValue()));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                    
                } else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(doctorBankAccountRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/doctor_bank_account.jsp?DOCTOR_PROFILE_CODE="+ request.getParameter("DOCTOR_PROFILE_CODE")+"&DOCTOR_CODE=" + doctorBankAccountRec.getField("DOCTOR_CODE").getValue()+"&BANK_ACCOUNT_NO="+doctorBankAccountRec.getField("BANK_ACCOUNT_NO").getValue()));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } else if (MODE == DBMgr.MODE_QUERY) {
                	doctorBankAccountRecEdit = DBMgr.getRecord("SELECT * FROM DOCTOR_BANK_ACCOUNT WHERE BANK_ACCOUNT_NO = '"+request.getParameter("BANK_ACCOUNT_NO")+"' AND DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") +  "' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"'");
                	
                }

                response.sendRedirect("../message.jsp");
                return;
            }else if (request.getParameter("DOCTOR_CODE") != null) {
            	doctorBankAccountRec = DBMgr.getRecord("SELECT * FROM DOCTOR_BANK_ACCOUNT WHERE DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") +  "' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"'");
            	
            	MODE = DBMgr.MODE_INSERT;
                
                if(request.getParameter("BANK_ACCOUNT_NO") != null){
                	doctorBankAccountRecEdit = DBMgr.getRecord("SELECT * FROM DOCTOR_BANK_ACCOUNT WHERE BANK_ACCOUNT_NO = '"+request.getParameter("BANK_ACCOUNT_NO")+"' AND DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") +  "' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"'");
                	bankRec = DBMgr.getRecord("SELECT B.CODE, B.DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION, B.COUNTRY_CODE, C.DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS COUNTRY_DESCRIPTION FROM BANK B LEFT JOIN COUNTRY C ON B.COUNTRY_CODE = C.CODE WHERE B.CODE = '" + DBMgr.getRecordValue(doctorBankAccountRecEdit, "BANK_CODE") + "' AND B.COUNTRY_CODE = '" + DBMgr.getRecordValue(doctorBankAccountRecEdit, "BANK_COUNTRY_CODE") + "' ");
                    bankBranchRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION FROM BANK_BRANCH WHERE CODE = '" + DBMgr.getRecordValue(doctorBankAccountRecEdit, "BANK_BRANCH_CODE") + "' AND BANK_CODE='"+DBMgr.getRecordValue(doctorBankAccountRecEdit, "BANK_CODE")+"'  AND BANK_COUNTRY_CODE = '" + DBMgr.getRecordValue(doctorBankAccountRecEdit, "BANK_COUNTRY_CODE") + "' ");
                	MODE = DBMgr.MODE_UPDATE;
                	readonlyCheck = "readonly=\"readonly\"";
                }
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
            
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
        	var code;
            var country_code;
        	if(!(/^\d+$/.test(document.mainForm.BANK_CODE.value))){
        		code = document.mainForm.BANK_CODE.value.substring(2, document.mainForm.BANK_CODE.value.length);
                country_code = document.mainForm.BANK_CODE.value.substring(0, 2);
        	}
        	else{
        		code = document.mainForm.BANK_CODE.value;
                country_code = document.mainForm.BANK_COUNTRY_CODE.value;
        	}
        	document.mainForm.BANK_CODE.value = code;
        	document.mainForm.BANK_COUNTRY_CODE.value
            var target = "../../RetrieveData?TABLE=BANK&COND=CODE='" + code + "' AND COUNTRY_CODE='"+country_code+"' ";
            AJAX_Request(target, AJAX_Handle_Refresh_BANK);
        }
        
        function AJAX_Handle_Refresh_BANK() {
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;

                // Data not found
                if (!isXMLNodeExist(xmlDoc, "CODE")) {
                    //document.mainForm.BANK_CODE.value = "";
                    document.mainForm.BANK_DESCRIPTION.value = "";
                    //document.mainForm.SEARCH_BANK_BRANCH_CODE.disabled = false;
                    return;
                }
                // Data found
                document.mainForm.BANK_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                document.mainForm.BANK_COUNTRY_CODE.value = getXMLNodeValue(xmlDoc, "COUNTRY_CODE");
                //document.mainForm.SEARCH_BANK_BRANCH_CODE.disabled = true;

                if(document.mainForm.BANK_BRANCH_CODE.value != ""){
                    AJAX_Refresh_BANK_BRANCH();
                }
                if(document.mainForm.BANK_COUNTRY_CODE.value != ""){
                    AJAX_Refresh_BANK_COUNTRY_CODE();
                }
            }
        }
        
        function BANK_BRANCH_CODE_KeyPress(e) {
            var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

            if (key == 13) {
                document.mainForm.BANK_BRANCH_CODE.blur();
                return false;
            }
            else {
                return true;
            }
        }
            
            function AJAX_Refresh_BANK_BRANCH() {
                var target = "../../RetrieveData?TABLE=BANK_BRANCH&COND=BANK_CODE='" + document.mainForm.BANK_CODE.value + "' AND CODE='" + document.mainForm.BANK_BRANCH_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_BANK_BRANCH);
            }
            
            function AJAX_Handle_Refresh_BANK_BRANCH() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.BANK_BRANCH_CODE.value = "";
                        document.mainForm.BANK_BRANCH_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.BANK_BRANCH_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                }
            }
            
            function AJAX_Refresh_BANK_COUNTRY_CODE() {
                var target = "../../RetrieveData?TABLE=COUNTRY&COND=CODE='" + document.mainForm.BANK_COUNTRY_CODE.value + "' ";
                AJAX_Request(target, AJAX_Handle_Refresh_BANK_COUNTRY_CODE);
            }
            
            function AJAX_Handle_Refresh_BANK_COUNTRY_CODE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.BANK_COUNTRY_CODE.value = "";
                        document.mainForm.COUNTRY_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.COUNTRY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                }
            }
            
            function checkBankBranchCode(){
		        if(document.mainForm.BANK_DESCRIPTION.value==""){
		            alert("${labelMap.ALERT_BANK}");
		            document.mainForm.BANK_CODE.focus();
		        }else{
		        	//var url = '../search.jsp?TABLE=BANK_BRANCH&BEACTIVE=1&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&COND=[AND (BANK_CODE=\''+ document.mainForm.BANK_CODE.value+'\') AND (BANK_COUNTRY_CODE=\''+ document.mainForm.BANK_COUNTRY_CODE.value +'\')]&TARGET=BANK_BRANCH_CODE&HANDLE=AJAX_Refresh_BANK_BRANCH';
		        	var url = '../search.jsp?TABLE=BANK_BRANCH&BEACTIVE=1&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&COND= AND BANK_CODE=\''+ document.mainForm.BANK_CODE.value+'\' &TARGET=BANK_BRANCH_CODE&HANDLE=AJAX_Refresh_BANK_BRANCH';
		        	openSearchForm(url);
		        }
		        return false;
			}
            
            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=DOCTOR_BANK_ACCOUNT&COND=BANK_ACCOUNT_NO='" + document.mainForm.BANK_ACCOUNT_NO.value + "' AND DOCTOR_CODE='" + document.mainForm.DOCTOR_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }
            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "BANK_ACCOUNT_NO");
                    
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
                if (!isObjectEmptyString(document.mainForm.BANK_ACCOUNT_NO, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.BANK_ACCOUNT_NAME, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.BANK_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.BANK_BRANCH_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                AJAX_VerifyData();
            }
        }
        </script>
        <style type="text/css">
			<!--
			.style1 {color: #003399}
			-->
        </style>
</head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="doctor_bank_account.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE%>" />
            <input type="hidden" id="DOCTOR_PROFILE_CODE" name="DOCTOR_PROFILE_CODE" value="<%= request.getParameter("DOCTOR_PROFILE_CODE")%>" />
            <table class="form">
                <tr>
                <th colspan="4" class="buttonBar">     
                		<div style="float: left;">${labelMap.TITLE_MAIN}</div> 
               	</th>
				</tr>
                <tr>
                  	<td class="label">
                        <label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}*</label>                    
                    </td>
                    <td class="input" colspan="3">
                    	<input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" maxlength="20" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorBankAccountRec, "DOCTOR_CODE").equals("") ? request.getParameter("DOCTOR_CODE") : DBMgr.getRecordValue(doctorBankAccountRec, "DOCTOR_CODE").equals("") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label id="BANK_ACCOUNT_LABEL" for="BANK_ACCOUNT_NO">${labelMap.BANK_ACCOUNT_NO}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="BANK_ACCOUNT_NO" type="text" class="medium" id="BANK_ACCOUNT_NO" <%=readonlyCheck%> value="<%= DBMgr.getRecordValue(doctorBankAccountRecEdit, "BANK_ACCOUNT_NO")%>" maxlength="11"  onkeypress="return isNumberKey(event)"/>                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="BANK_ACCOUNT_NAME">${labelMap.BANK_ACCOUNT_NAME}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="BANK_ACCOUNT_NAME" type="text" class="long" id="BANK_ACCOUNT_NAME" value="<%= DBMgr.getRecordValue(doctorBankAccountRecEdit, "BANK_ACCOUNT_NAME")%>" maxlength="255"  />  </td>
                </tr>
                <tr>
                    <td class="label"><label for="BANK_CODE">${labelMap.BANK_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="BANK_CODE" name="BANK_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(bankRec, "CODE") %>" onkeypress="return BANK_CODE_KeyPress(event);" onblur="AJAX_Refresh_BANK();" />
                        <input id="SEARCH_BANK_CODE" name="SEARCH_BANK_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=BANK&BEACTIVE=1&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&TARGET=BANK_CODE&HANDLE=AJAX_Refresh_BANK'); return false;" />
                        <input type="text" id="BANK_DESCRIPTION" name="BANK_DESCRIPTION" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(bankRec, "DESCRIPTION") %>" /></td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="BANK_BRANCH_CODE">${labelMap.BANK_BRANCH_CODE}</label>                    </td>
                    <td class="input" colspan="3"><input type="text" id="BANK_BRANCH_CODE" name="BANK_BRANCH_CODE" class="short" maxlength="20"value="<%= DBMgr.getRecordValue(bankBranchRec, "CODE") %>" onkeypress="return BANK_BRANCH_CODE_KeyPress(event);" onblur="AJAX_Refresh_BANK_BRANCH();"/>
                      	<input id="SEARCH_BANK_BRANCH_CODE" name="SEARCH_BANK_BRANCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="return checkBankBranchCode()" />
                        <input type="text" id="BANK_BRANCH_DESCRIPTION" name="BANK_BRANCH_DESCRIPTION" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(bankBranchRec, "DESCRIPTION") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="BANK_COUNTRY_CODE">${labelMap.BANK_COUNTRY_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="BANK_COUNTRY_CODE" name="BANK_COUNTRY_CODE" class="short" readonly="readonly" maxlength="20" value="<%= DBMgr.getRecordValue(bankRec, "COUNTRY_CODE") %>" />
                        <input type="text" id="COUNTRY_DESCRIPTION" name="COUNTRY_DESCRIPTION" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(bankRec, "COUNTRY_DESCRIPTION") %>" />                    
                    </td>
                </tr>
                <tr>
                	<td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(doctorBankAccountRecEdit, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(doctorBankAccountRecEdit, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                               <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(doctorBankAccountRecEdit, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                               <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location.href='doctor_bank_account.jsp?DOCTOR_CODE=<%=request.getParameter("DOCTOR_CODE")%>&DOCTOR_PROFILE_CODE=<%=request.getParameter("DOCTOR_PROFILE_CODE")%>'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location.href='doctor_detail.jsp?CODE=<%=request.getParameter("DOCTOR_CODE")%>&DOCTOR_PROFILE_CODE=<%=request.getParameter("DOCTOR_PROFILE_CODE")%>'" />
                    </th>
                </tr>
            </table>
            <hr />
            <table class="data">
                <tr>
                    <th colspan="7" class="alignLeft">${labelMap.TITLE_DETAIL}</th>
                </tr>
                <tr>
                    <td class="sub_head">${labelMap.BANK_ACCOUNT_NO}</td>
                    <td class="sub_head">${labelMap.BANK_ACCOUNT_NAME}</td>
                    <td class="sub_head">${labelMap.BANK_CODE}</td>
                    <td class="sub_head">${labelMap.BANK_BRANCH_CODE}</td>
                    <td class="sub_head">${labelMap.BANK_COUNTRY_CODE}</td>
                    <td class="sub_head">${labelMap.ACTIVE}</td>
                    <td class="sub_head"><%=labelMap.get(LabelMap.EDIT)%></td>
                </tr>
                <%

            DBConnection con = new DBConnection();
            con.connectToLocal();
            ResultSet rs = con.executeQuery("SELECT DOCTOR_CODE,BANK_ACCOUNT_NO, BANK_ACCOUNT_NAME, BANK_CODE, BANK_BRANCH_CODE, BANK_COUNTRY_CODE, ACTIVE  FROM DOCTOR_BANK_ACCOUNT WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND DOCTOR_CODE = '" + DBMgr.getRecordValue(doctorBankAccountRec, "DOCTOR_CODE") + "' ORDER BY ACTIVE DESC");
            int i = 0;
            String activeIcon, linkEdit;
            while (rs.next()) {
                activeIcon = "<img src=\"../../images/" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
                linkEdit = "<a href=\"doctor_bank_account.jsp?BANK_ACCOUNT_NO=" + rs.getString("BANK_ACCOUNT_NO") + "&DOCTOR_CODE=" + rs.getString("DOCTOR_CODE")+"&DOCTOR_PROFILE_CODE="+ request.getParameter("DOCTOR_PROFILE_CODE") + "\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"Edit\" /></a>";
                %>                
                <tr>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("BANK_ACCOUNT_NO"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("BANK_ACCOUNT_NAME"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("BANK_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("BANK_BRANCH_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("BANK_COUNTRY_CODE"), true)%></td>
                    <td class="row<%=i % 2%>" align="center"><%= activeIcon %></td>                    
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
            </table>
        </form>
      </body>
</html>
