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

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_EXPENSE)) {
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
            labelMap.add("TITLE_MAIN", "Adjust", "ประเภทรายการปรับปรุง");
            labelMap.add("CODE", "Code", "รหัส");
            labelMap.add("VENDOR", "Vendor Code", "Vendor Code");
            labelMap.add("DESCRIPTION", "Description", "ชื่อ");
            labelMap.add("SIGN", "Adjust Mode", "ประเภทการปรับปรุง");
            labelMap.add("SIGN_SUB", "Deduct", "รายการหัก");
            labelMap.add("SIGN_ADD", "Revenue", "รายการเพิ่ม");
            labelMap.add("GL_INTERFACE","GL Interface","นำส่งข้อมูลการจ่ายเข้าระบบ");
            labelMap.add("GL_INTERFACE1","Yes","Yes");
            labelMap.add("GL_INTERFACE2","No","No");
            labelMap.add("AC_INTERFACE","Accru Interface","นำส่งข้อมูลตั้งหนี้เข้าระบบ");
            labelMap.add("AC_INTERFACE1","Yes","Yes");
            labelMap.add("AC_INTERFACE2","No","No");
            labelMap.add("ACCOUNT_REV","Deduct Advance","หักคืนรายการสำรองจ่าย");
            labelMap.add("ABSORB_TYPE","Adjust Type","ประเภทการปรับปรุง");
            labelMap.add("ABSORB","Absorb Guarantee","ค่าการันตีเพิ่มเติม");
            labelMap.add("ADVANCE","Advance Guarantee","ค่าแพทย์ทดรองจ่าย");
            labelMap.add("EXTRA","Extra Guarantee","ค่าอยู่เวร");
            labelMap.add("INCLUDE","Include Guarantee","ค่าแพทย์รวมการันตี");
            labelMap.add("FIX","FIX Guarantee","ค่าการันตีอัตราคงที่");
            labelMap.add("EXPENSE","C/O Doctor","หักค่าใช้จ่ายญาติแพทย์");
            labelMap.add("TAX402","Tax 402 Reduce","หักค่าแพทย์นำส่งสรรพากร");
            labelMap.add("DF","Distribute Revenue DF","ค่าแพทย์สำหรับกระจายรายได้");
            labelMap.add("ABSORB_D","Distribute Revenue Absorb","ค่าการันตีเพิ่มเติมสำหรับกระจายรายได้");
            labelMap.add("EXPENSE_DIS","Distribute Expense Not afford","ค่าใช้จ่ายที่ติดลบ");
            labelMap.add("EXPENSE_NEXT","Expense Not afford to Next Month","ยกยอดค่าใช้จ่ายที่ติดลบ");
            labelMap.add("MANAGEMENT_FEE","Management Fee","รายการหักสวัสดิการ");
            labelMap.add("EXPENSE_MONTOYEAR","Deduct Monthly to Yearly","หักค่าแพทย์เกินการันตีรายเดือนคิดเป็นรายปี");
            labelMap.add("ACCOUNT_CODE", "Account Code", "รหัสการลงบัญชี");
            labelMap.add("TAX_TYPE_CODE", "Tax Type", "ประเภทภาษี");
            labelMap.add("MAPPING_CASE", "Mapping Case", "Mapping Case");
            labelMap.add("GA_SS", "Social Security Guarantee", "การันตีประกันสังคม");
            
            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            byte MODE = DBMgr.MODE_INSERT;
            DataRecord record = null;
            DataRecord expenseRecLog = null;
            String remark = "";
            if (request.getParameter("MODE") != null) {
            	MODE = Byte.parseByte(request.getParameter("MODE"));
                //out.print(request.getParameterMap().toString());
				
                record = new DataRecord("EXPENSE");
                expenseRecLog = new DataRecord("LOG_EXPENSE");
                
                record.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(),true);
                record.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                record.addField("DESCRIPTION", Types.VARCHAR, request.getParameter("DESCRIPTION"));
                record.addField("ACCOUNT_CODE", Types.VARCHAR, request.getParameter("ACCOUNT_CODE"));
                record.addField("GL_INTERFACE", Types.VARCHAR, request.getParameter("GL_INTERFACE"));
                record.addField("AC_INTERFACE", Types.VARCHAR, request.getParameter("AC_INTERFACE"));
                record.addField("ADJUST_TYPE", Types.VARCHAR, request.getParameter("ADJUST_TYPE"));
                record.addField("TAX_TYPE_CODE", Types.VARCHAR, request.getParameter("TAX_TYPE_CODE"));
                record.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                record.addField("SIGN", Types.VARCHAR, request.getParameter("SIGN"));
                record.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                record.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                record.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                
                //for log
                expenseRecLog.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(),true);
                expenseRecLog.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                expenseRecLog.addField("DESCRIPTION", Types.VARCHAR, request.getParameter("DESCRIPTION"));
                expenseRecLog.addField("ACCOUNT_CODE", Types.VARCHAR, request.getParameter("ACCOUNT_CODE"));
                expenseRecLog.addField("GL_INTERFACE", Types.VARCHAR, request.getParameter("GL_INTERFACE"));
                expenseRecLog.addField("AC_INTERFACE", Types.VARCHAR, request.getParameter("AC_INTERFACE"));
                expenseRecLog.addField("ADJUST_TYPE", Types.VARCHAR, request.getParameter("ADJUST_TYPE"));
                expenseRecLog.addField("TAX_TYPE_CODE", Types.VARCHAR, request.getParameter("TAX_TYPE_CODE"));
                expenseRecLog.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                expenseRecLog.addField("SIGN", Types.VARCHAR, request.getParameter("SIGN"));
                expenseRecLog.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate(), true);
                expenseRecLog.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime(), true);
                expenseRecLog.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_INSERT) {
                	expenseRecLog.addField("REMARK", Types.VARCHAR, remark);
                    if (DBMgr.insertRecord(record) && DBMgr.insertRecord(expenseRecLog)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/expense.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }else if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
                	DataRecord expense = DBMgr.getRecord("SELECT HOSPITAL_CODE, CODE, DESCRIPTION, ACCOUNT_CODE, GL_INTERFACE, AC_INTERFACE, "+
                			"ADJUST_TYPE, TAX_TYPE_CODE, ACTIVE, SIGN, UPDATE_DATE, UPDATE_TIME, USER_ID "+
               				"FROM EXPENSE WHERE CODE = '" + request.getParameter("CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' " );
             		
               		remark = "แก้ไข ";
               		for(int i = 0; i < expense.getSize(); i++){
               			if(!expense.getValueOfIndex(i).getValue().equalsIgnoreCase(record.getValueOfIndex(i).getValue())
               					&& !record.getValueOfIndex(i).getName().equals("USER_ID")
               					&& !record.getValueOfIndex(i).getName().equals("UPDATE_DATE")
               					&& !record.getValueOfIndex(i).getName().equals("UPDATE_TIME")){
               				//System.out.println(dept.getValueOfIndex(i).getValue()+", "+departmentRec.getValueOfIndex(i).getValue());
               				System.out.println("แก้ไข"+record.getValueOfIndex(i).getName());
               				remark += record.getValueOfIndex(i).getName()+", ";
               			}
               		}
               		expenseRecLog.addField("REMARK", Types.VARCHAR, remark.substring(0, remark.length()-2));
                	
                    if (DBMgr.updateRecord(record) && DBMgr.insertRecord(expenseRecLog)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/expense.jsp"));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
			//DBConnection con = new DBConnection();
            //con.connectToLocal();
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

            function AJAX_Refresh_EXPENSE() {
                var target = "../../RetrieveData?TABLE=EXPENSE&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_EXPENSE);
            }
            
            function AJAX_Handle_Refresh_EXPENSE() {
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
                    document.mainForm.DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                    document.mainForm.TAX_TYPE_CODE.value = getXMLNodeValue(xmlDoc, "TAX_TYPE_CODE");
                    document.mainForm.ACCOUNT_CODE.value = getXMLNodeValue(xmlDoc, "ACCOUNT_CODE");
                    document.mainForm.ADJUST_TYPE.value = getXMLNodeValue(xmlDoc, "ADJUST_TYPE");
                    document.mainForm.ACTIVE[0].checked = getXMLNodeValue(xmlDoc, "ACTIVE") == '1' ? true : false;
                    document.mainForm.ACTIVE[1].checked = getXMLNodeValue(xmlDoc, "ACTIVE") == '0' ? true : false;
                    document.mainForm.GL_INTERFACE[0].checked = getXMLNodeValue(xmlDoc, "GL_INTERFACE") == 'Y' ? true : false;
                    document.mainForm.GL_INTERFACE[1].checked = getXMLNodeValue(xmlDoc, "GL_INTERFACE") == 'N' ? true : false;
                    document.mainForm.AC_INTERFACE[0].checked = getXMLNodeValue(xmlDoc, "AC_INTERFACE") == 'Y' ? true : false;
                    document.mainForm.AC_INTERFACE[1].checked = getXMLNodeValue(xmlDoc, "AC_INTERFACE") == 'N' ? true : false;
              
                   // document.mainForm.GUARANTEE_INCLUDE[0].checked = getXMLNodeValue(xmlDoc, "GUARANTEE_INCLUDE") == 'Y' ? true : false;
                   // document.mainForm.GUARANTEE_INCLUDE[1].checked = getXMLNodeValue(xmlDoc, "GUARANTEE_INCLUDE") == 'N' ? true : false;
              
                    document.mainForm.SIGN[0].checked = getXMLNodeValue(xmlDoc, "SIGN") == '-1' ? true : false;
                    document.mainForm.SIGN[1].checked = getXMLNodeValue(xmlDoc, "SIGN") == '1' ? true : false;
                    document.mainForm.ACCOUNT_CODE.value = getXMLNodeValue(xmlDoc, "ACCOUNT_CODE");
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=EXPENSE&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
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
                if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                document.mainForm.reset();
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.readOnly = false;
            }
        </script>
    </head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="expense.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_INSERT%>" />
			<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("expense.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">
				  		<div style="float: left;">${labelMap.TITLE_MAIN}</div>					</th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=EXPENSE&DISPLAY_FIELD=DESCRIPTION&TARGET=CODE&HANDLE=AJAX_Refresh_EXPENSE'); return false;" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION"><span class="style1">${labelMap.DESCRIPTION}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION" name="DESCRIPTION" maxlength="255" class="long" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="SIGN_SUB">${labelMap.SIGN}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="SIGN_SUB" name="SIGN" value="-1" checked="checked" />
                        <label for="SIGN_SUB">${labelMap.SIGN_SUB}</label>
                        <input type="radio" id="SIGN_ADD" name="SIGN" value="1" />
                        <label for="SIGN_ADD">${labelMap.SIGN_ADD}</label>                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACCOUNT_CODE"><span class="style1">${labelMap.ACCOUNT_CODE}*</span></label></td>
                    <td colspan="3" class="input">
						<%= DBMgr.generateDropDownList("ACCOUNT_CODE", "mediumMax", "SELECT CODE, CODE + ' : ' + DESCRIPTION AS DESCRIPTION FROM ACCOUNT ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(record, "ACCOUNT_CODE"))%>
					</td>
                </tr>
                <tr>
                  <td class="label"><label for="GL_INTERFACE">${labelMap.GL_INTERFACE}</label></td>
                  <td colspan="3" class="input">
                        <input type="radio" id="GL_INTERFACE1" name="GL_INTERFACE" value="Y" checked="checked" />
                        <label for="GL_INTERFACE1">${labelMap.GL_INTERFACE1}</label>
                        <input type="radio" id="GL_INTERFACE2" name="GL_INTERFACE" value="N" />
                        <label for="GL_INTERFACE2">${labelMap.GL_INTERFACE2}</label>                    </td>
                </tr>
                <tr>
                  <td class="label"><label for="AC_INTERFACE">${labelMap.AC_INTERFACE}</label></td>
                  <td colspan="3" class="input">
                        <input type="radio" id="AC_INTERFACE1" name="AC_INTERFACE" value="Y" checked="checked" />
                        <label for="AC_INTERFACE1">${labelMap.AC_INTERFACE1}</label>
                        <input type="radio" id="AC_INTERFACE2" name="AC_INTERFACE" value="N" />
                        <label for="AC_INTERFACE2">${labelMap.AC_INTERFACE2}</label>                    </td>
                </tr>
                
                <tr>
                    <td class="label"><label for="ABSORB_TYPE">${labelMap.ABSORB_TYPE}</label></td>
                    <td colspan="" class="input">
						<select class="mediumMax" id="ADJUST_TYPE" name="ADJUST_TYPE">
	                        <option value=""<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>-- Undefine --</option>
							<option value="HP"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("HP") ? " selected=\"selected\"" : "" %>>${labelMap.ABSORB}</option>
							<option value="DR"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("DR") ? " selected=\"selected\"" : "" %>>${labelMap.ADVANCE}</option>
							<option value="EX"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("EX") ? " selected=\"selected\"" : "" %>>${labelMap.EXTRA}</option>
							<option value="FX"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("FX") ? " selected=\"selected\"" : "" %>>${labelMap.FIX}</option>
							<option value="IC"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("IC") ? " selected=\"selected\"" : "" %>>${labelMap.INCLUDE}</option>
							<option value="CO"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("CO") ? " selected=\"selected\"" : "" %>>${labelMap.EXPENSE}</option>
							<option value="AR"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("AR") ? " selected=\"selected\"" : "" %>>${labelMap.ACCOUNT_REV}</option>
		                    <option value="DF"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("DF") ? " selected=\"selected\"" : "" %>>${labelMap.DF}</option>
		                    <option value="AB"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("AB") ? " selected=\"selected\"" : "" %>>${labelMap.ABSORB_D}</option>
		                    <option value="EA"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("EA") ? " selected=\"selected\"" : "" %>>${labelMap.EXPENSE_DIS}</option>
		                    <option value="EN"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("EN") ? " selected=\"selected\"" : "" %>>${labelMap.EXPENSE_NEXT}</option>
		                    <option value="TX"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("TX") ? " selected=\"selected\"" : "" %>>${labelMap.TAX402}</option>
		                    <option value="MF"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("MF") ? " selected=\"selected\"" : "" %>>${labelMap.MANAGEMENT_FEE}</option>
		                    <option value="MY"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("MY") ? " selected=\"selected\"" : "" %>>${labelMap.EXPENSE_MONTOYEAR}</option>
		                    <option value="GS"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("GS") ? " selected=\"selected\"" : "" %>>${labelMap.GA_SS}</option>
		                    <option value="MC"<%= DBMgr.getRecordValue(record, "ADJUST_TYPE").equalsIgnoreCase("MC") ? " selected=\"selected\"" : "" %>>${labelMap.MAPPING_CASE}</option>
		                    </select>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="aText"><span class="style1">${labelMap.TAX_TYPE_CODE}*</span></label></td>
                    <td colspan="3"class="input">
                        <%=DBMgr.generateDropDownList("TAX_TYPE_CODE", "short", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM TAX_TYPE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(record, "TAX_TYPE_CODE"))%>
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
