<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="java.sql.Types"%>
<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_DEPARTMENT)) {
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
            labelMap.add("TITLE_MAIN", "User", "ผู้ใช้งาน");
            labelMap.add("HOSPITAL_CODE", "Hospital Code", "รหัสโรงพยาบาล");
            labelMap.add("LABEL_LOGIN_NAME", "Login name", "ชื่อเข้าใช้ระบบ");
            labelMap.add("PASSWORD", "Password", "รหัสเข้าใช้ระบบ");
            labelMap.add("NAME", "Full Name", "ชื่อ - นามสกุล");
            labelMap.add("USER_GROUP_CODE", "User Group Code", "กลุ่มผู้ใช้งาน");
            labelMap.add("ACTIVE", "Active", "สถานะ");
            labelMap.add("LOGIN_DATE", "Login Date", "วันที่เข้าใช้งานล่าสุด");
            labelMap.add("LOGIN_TIME", "Login Time", "เวลาที่เข้าใช้งานล่าสุด");
            labelMap.add("IPADDRESS", "IP Address", "เข้าใช้งานจาก IP");
            labelMap.add("EMAIL", "E-mail", "อีเมล์");
            labelMap.add("LANG_CODE", "Lang Code", "ภาษา");
            labelMap.add("CONFIRM_PASSWORD", "Congfirm Password", "ยืนยันรหัสผ่าน");
            labelMap.add("SQL_HOSPITAL", "SELECT CODE, (CODE + ' : ' + DESCRIPTION_ENG) as DESCRIPTION FROM HOSPITAL ORDER BY CODE;", "SELECT CODE, (CODE + ' : ' + DESCRIPTION_THAI) as DESCRIPTION FROM HOSPITAL ORDER BY CODE;");
            labelMap.add("langThai", "Thai", "ไทย");
            labelMap.add("langEng", "English", "อังกฤษ");
            labelMap.add("CHANGE_PASS","Change Password","เปลี่ยนรหัสผ่าน");
            request.setAttribute("labelMap", labelMap.getHashMap());


            String[] langValue = {labelMap.get("langThai"),labelMap.get("langEng")};
            String[] langName = {"T","E"};

            /*
            Type of Doctor
            1	System Admin 	-> All

            2	Manager		->
                    - มีหน้าที่ตรวจสอบข้อมูลและทำการ Active รหัสแพทย์นั้นๆ
                    - ไม่สามารถแก้ไขข้อมูลทั้งหมดได้ยกเว้น Active
            3	Organize Doctor	->
                    - สามารถดูข้อมูลและเพิ่มเติมเปลี่ยนแปลงแก้ไขได้ทั้งหมด
                    - ไม่สามารถ Active รหัสแพทย์นั้นได้ (ในกรณีเพิ่มหรือแก้ไข ให้ Set เป็น InActive)
            4	DF User(IT)	->
                    - สามารถดูข้อมูลได้ทั้งหมดแต่ไม่สามารถทำการเปลี่ยนแปลงแก้ไข

            5	User(Doctor)	->
            */
            String LOGIN_NAME = "";
            String disabledManager = "";
            String readonlyManager = "";
            request.setCharacterEncoding("UTF-8");
            DataRecord usersRec = null, hospitalUnitRec = null;
            String userGroup = "";
            byte MODE = DBMgr.MODE_INSERT;
            
            if("1".equalsIgnoreCase(session.getAttribute("USER_GROUP_CODE").toString()) ||
               "3".equalsIgnoreCase(session.getAttribute("USER_GROUP_CODE").toString()) ){
                //readonlyManager = "readonly=\"readonly\"";
                LOGIN_NAME = "";
            }else{
            	readonlyManager = "readonly=\"readonly\"";
                if (request.getParameter("LOGIN_NAME") == null){
                    LOGIN_NAME = session.getAttribute("USER_ID").toString();
                }
            }
            
            if (request.getParameter("MODE") != null) {

                //out.print(request.getParameterMap().toString());

                usersRec = new DataRecord("USERS");

                usersRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString() , true);
                usersRec.addField("LOGIN_NAME", Types.VARCHAR, request.getParameter("LOGIN_NAME"), true);
                usersRec.addField("PASSWORD", Types.VARCHAR, request.getParameter("PASSWORD"));
                usersRec.addField("NAME", Types.VARCHAR, request.getParameter("NAME"));
                usersRec.addField("USER_GROUP_CODE", Types.NUMERIC, (request.getParameter("USER_GROUP_CODE") == null?session.getAttribute("USER_GROUP_CODE").toString():request.getParameter("USER_GROUP_CODE")));
                usersRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                usersRec.addField("LOGIN_DATE", Types.VARCHAR, request.getParameter("LOGIN_DATE"));
                usersRec.addField("LOGIN_TIME", Types.VARCHAR, request.getParameter("LOGIN_TIME"));
                usersRec.addField("IPADDRESS", Types.VARCHAR, request.getParameter("IPADDRESS"));
                usersRec.addField("EMAIL", Types.VARCHAR, request.getParameter("EMAIL"));
                usersRec.addField("LANG_CODE", Types.VARCHAR, request.getParameter("LANG_CODE"));
				System.out.print("User Group : "+(request.getParameter("LANG_CODE")));
                //usersRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                //usersRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                //usersRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                
                if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(usersRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/users_main.jsp"));
                    }
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }else if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
                	System.out.println("Update Mode : "+ usersRec);
                    if (DBMgr.updateRecord(usersRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/users_main.jsp"));
                    }else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }else if (request.getParameter("LOGIN_NAME")!=null || LOGIN_NAME != "") {
                if(request.getParameter("LOGIN_NAME")==null){
                    usersRec = DBMgr.getRecord("SELECT * FROM USERS WHERE LOGIN_NAME = '" + LOGIN_NAME + "' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"'");
                }else{
                    usersRec = DBMgr.getRecord("SELECT * FROM USERS WHERE LOGIN_NAME = '" + request.getParameter("LOGIN_NAME") + "' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"'");
                }
                if (usersRec == null) {
                    MODE = DBMgr.MODE_INSERT;
                }
                else {
                    MODE = DBMgr.MODE_UPDATE;
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
        <script type="text/javascript" src="../../javascript/md5.js"></script>
        <script type="text/javascript">

            function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    Refresh_USERS();
                    return false;
                }
                else {
                    return true;
                }
            }

            function Refresh_USERS() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?LOGIN_NAME=' + document.mainForm.LOGIN_NAME.value;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?LOGIN_NAME=' + document.mainForm.LOGIN_NAME.value;
                }
            }

            function USERS_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.LOGIN_NAME.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_USERS() {
                var target = "../../RetrieveData?TABLE=USERS&COND=LOGIN_NAME='" + document.mainForm.LOGIN_NAME.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_USERS);
            }

            function AJAX_Handle_Refresh_USERS() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "LOGIN_NAME")) {
                        document.mainForm.LOGIN_NAME.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.LOGIN_NAME.value = getXMLNodeValue(xmlDoc, "LOGIN_NAME");
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=USERS&COND=HOSPITAL_CODE='" + document.mainForm.HOSPITAL_CODE.value + "' AND LOGIN_NAME='" + document.mainForm.LOGIN_NAME.value + "'";
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }

            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "LOGIN_NAME");             
                    
                    switch (document.mainForm.MODE.value) {
                        case "<%=DBMgr.MODE_INSERT%>" :
                            if (beExist) {
                                if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                                    document.mainForm.MODE.value= "<%=DBMgr.MODE_UPDATE%>";                                    
                                    document.mainForm.submit();
                                }
                            }
                            else {
                                document.mainForm.PASSWORD.value = hex_md5(document.mainForm.LOGIN_NAME.value);
                                document.mainForm.submit();
                            }
                            break;
                        case "<%=DBMgr.MODE_UPDATE%>" :
                            if(document.mainForm.MODE_RESERT_PASS.value=='true'){
                                document.mainForm.PASSWORD.value = hex_md5(document.mainForm.LOGIN_NAME.value);
                            }
                            if (beExist) {
                                document.mainForm.submit();
                            }
                            else {
                                alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                                document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
                                document.mainForm.submit();
                            }
                            break;
                    }
                }
            }

            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.LOGIN_NAME, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                    !isObjectEmptyString(document.mainForm.NAME, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                    !isObjectEmptyString(document.mainForm.HOSPITAL_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                    !isObjectEmptyString(document.mainForm.LANG_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }

            function RESET_Click() {
                window.location.href = "users_main.jsp";
                return false;
            }
        </script>
    </head>
    <body>
            <form id="mainForm" name="mainForm" method="post" action="users_main.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
            <input type="hidden" id="MODE_RESERT_PASS" name="MODE_RESERT_PASS" value="false" />
           	<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("users_main.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  	<div style="float: right;" id="Language" name="Language"></div>
				  	</th>
                </tr>
                <%
                if(LOGIN_NAME==""){
                %>
                    <tr>
                        <td class="label"><label for="CODE">${labelMap.LABEL_LOGIN_NAME} *</label></td>
                        <td colspan="3" class="input">
                            <input type="text" id="LOGIN_NAME" name="LOGIN_NAME" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(usersRec, "LOGIN_NAME") %>" onkeypress="return CODE_KeyPress(event);" />
                            <input id="SEARCH_LOGIN_NAME" name="SEARCH_LOGIN_NAME" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=USERS&RETURN_FIELD=LOGIN_NAME&DISPLAY_FIELD=NAME&TARGET=LOGIN_NAME&HANDLE=Refresh_USERS'); return false;" />
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><label for="PASSWORD">${labelMap.PASSWORD}</label></td>
                        <td colspan="3" class="input">
                            <input type="password" id="PASSWORD" name="PASSWORD" class="short" maxlength="255" value="<%= DBMgr.getRecordValue(usersRec, "PASSWORD")%>" <%= session.getAttribute("USER_ID") == DBMgr.getRecordValue(usersRec, "LOGIN_NAME").toString() ? "" : " readonly=\"readonly\"" %>/>
                            <%
                            if(MODE==2){
                                out.println("<input type='button' name='resetpassword' value='Reset Password' onclick='FuncResetPass();'>");
                            }
                            %>
                        </td>
                    </tr>
                <%}else{%>
                    <tr>
                        <td class="label"><label for="CODE">${labelMap.LABEL_LOGIN_NAME}</label></td>
                        <td colspan="3" class="input">
                            <input type="text" id="LOGIN_NAME" name="LOGIN_NAME" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(usersRec, "LOGIN_NAME") %>" onkeypress="return CODE_KeyPress(event);" readonly/>
                            <input type="hidden" id="PASSWORD" name="PASSWORD" class="short" maxlength="255" value="<%= DBMgr.getRecordValue(usersRec, "PASSWORD")%>"/>
                        </td>
                    </tr>
                <%}%>
                <tr>
                    <td class="label"><label for="NAME">${labelMap.NAME}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="NAME" name="NAME" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(usersRec, "NAME") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="USER_GROUP_CODE">${labelMap.USER_GROUP_CODE}</label></td>
                    <td colspan="3" class="input">
                        <%=DBMgr.generateDropDownList("USER_GROUP_CODE", "medium", "SELECT USER_GROUP, ACTION_TYPE FROM USER_GROUP WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"' ORDER BY USER_GROUP", "ACTION_TYPE", "USER_GROUP", DBMgr.getRecordValue(usersRec, "USER_GROUP_CODE"))%>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="HOSPITAL_CODE">${labelMap.HOSPITAL_CODE}</label></td>
                    <td colspan="3" class="input">
                        <%=DBMgr.generateDropDownList("HOSPITAL_CODE", "medium", labelMap.get("SQL_HOSPITAL"), "DESCRIPTION", "CODE", DBMgr.getRecordValue(usersRec, "HOSPITAL_CODE"))%>
                        
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="LANG_CODE">${labelMap.LANG_CODE}</label></td>
                    <td colspan="3" class="input">
                        <%=DBMgr.generateDropDownList("LANG_CODE", "medium", langName, langValue, DBMgr.getRecordValue(usersRec, "LANG_CODE")) %>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="EMAIL">${labelMap.EMAIL}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="EMAIL" name="EMAIL" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(usersRec, "EMAIL") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(usersRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(usersRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(usersRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="CHANGE_PASSWORD" name="CHANGE_PASSWORD"  value="${labelMap.CHANGE_PASS}" onclick="FuncPassword();" 
                                <%= "1".equalsIgnoreCase(session.getAttribute("USER_GROUP_CODE").toString())&& 
                                !DBMgr.getRecordValue(usersRec, "LOGIN_NAME").equalsIgnoreCase(session.getAttribute("USER_ID").toString()) ? "disabled=\"disabled\"" : "" %>/>
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick=<%= session.getAttribute("USER_GROUP_CODE").toString().equals("3") ? "window.location='../../AppMain.jsp'" : "window.location='../process/ProcessFlow.jsp'" %> />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<script language="javascript">
    function FuncResetPass(){
        if(confirm('ยืนยันการรีเซตรหัสผ่าน')){
            document.mainForm.MODE_RESERT_PASS.value = 'true';
            document.mainForm.PASSWORD.value = document.mainForm.LOGIN_NAME.value;
            document.mainForm.resetpassword.disabled = true;
        }
    }
    
    function FuncPassword(){
        window.location.href = 'user_changepass.jsp?HOSPITAL_CODE=<%=DBMgr.getRecordValue(usersRec, "HOSPITAL_CODE") %>&LOGIN_NAME=<%=DBMgr.getRecordValue(usersRec, "LOGIN_NAME") %>';
    }
    
    if('1'=='<%=session.getAttribute("USER_GROUP_CODE").toString()%>'){
        //document.mainForm.USER_GROUP_CODE.disabled = true;
    }else{
        document.mainForm.USER_GROUP_CODE.disabled = true;
        document.mainForm.HOSPITAL_CODE.disabled = true;
        document.mainForm.USER_GROUP_CODE.value = '<%=DBMgr.getRecordValue(usersRec, "USER_GROUP_CODE") %>';
    }
</script>