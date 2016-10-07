<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
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
            labelMap.add("TITLE_MAIN", "CHANGE PASSWORD", "เปลี่ยนรหัสผ่าน");
            labelMap.add("HOSPITAL_CODE", "Hospital Code", "รหัสโรงพยาบาล");
            labelMap.add("LABEL_LOGIN_NAME", "Login name", "ชื่อเข้าใช้ระบบ");
            labelMap.add("PASSWORD", "New Password", "รหัสผ่านใหม่");
            labelMap.add("OLD_PASSWORD", "Old Password", "รหัสผ่านเดิม");
            labelMap.add("CONFIRM_PASSWORD", "Congfirm Password", "ยืนยันรหัสผ่าน");

            request.setAttribute("labelMap", labelMap.getHashMap());
            String[] langValue = {labelMap.get("langThai"),labelMap.get("langEng")};
            String[] langName = {"T","E"};

            request.setCharacterEncoding("UTF-8");
            DataRecord usersRec = null;
            byte MODE = DBMgr.MODE_UPDATE;

            if (request.getParameter("LOGIN_NAME") != null && request.getParameter("PASSWORD") != null) {
                String LOGIN_NAME = request.getParameter("LOGIN_NAME");
                String OLD_PASSWORD = request.getParameter("OLD_PASSWORD");
                String PASSWORD = request.getParameter("PASSWORD");

                String query = String.format("SELECT USERS.LOGIN_NAME, USERS.HOSPITAL_CODE, USERS.LANG_CODE, USERS.USER_GROUP_CODE, USER_GROUP.PERMISSION FROM USERS INNER JOIN USER_GROUP ON USERS.USER_GROUP_CODE = USER_GROUP.USER_GROUP AND USERS.HOSPITAL_CODE = USER_GROUP.HOSPITAL_CODE WHERE USERS.ACTIVE = 1 AND LOGIN_NAME = '%1$s' AND PASSWORD = '%2$s' AND USERS.HOSPITAL_CODE = '%3$s'",
                        LOGIN_NAME, OLD_PASSWORD, session.getAttribute("HOSPITAL_CODE").toString());
                out.println(query);
                
                DataRecord record = DBMgr.getRecord(query);
                if (record != null) {

                    usersRec = new DataRecord("USERS");
                    usersRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                    usersRec.addField("LOGIN_NAME", Types.VARCHAR, LOGIN_NAME, true);
                    usersRec.addField("PASSWORD", Types.VARCHAR, PASSWORD);
                    out.println(LOGIN_NAME + "<br>");
                    out.println(PASSWORD);
                    if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
                        if (DBMgr.updateRecord(usersRec)) {
                            session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/users_main.jsp"));
                        }
                        else {
                            session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                        }
                    }
                    response.sendRedirect("users_main.jsp");
                    return;
                } else {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert(\"Invalid password\")");
                    out.println("</script>");
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
                    //document.mainForm.submit();
                    SAVE_Click();
                    return false;
                }
                else {
                    return true;
                }
            }
            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.OLD_PASSWORD, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                    !isObjectEmptyString(document.mainForm.PASSWORD, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                    !isObjectEmptyString(document.mainForm.CONFIRM_PASSWORD, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                    (document.mainForm.PASSWORD.value==document.mainForm.CONFIRM_PASSWORD.value)) {
                        document.mainForm.PASSWORD.value = hex_md5(document.mainForm.PASSWORD.value);
                        document.mainForm.OLD_PASSWORD.value = hex_md5(document.mainForm.OLD_PASSWORD.value);
                        document.mainForm.CONFIRM_PASSWORD.value = hex_md5(document.mainForm.CONFIRM_PASSWORD.value);
                        document.mainForm.submit();
                }else{
                    alert('Confrim password invalid');
                    document.mainForm.CONFIRM_PASSWORD.fucus();
                }
            }
        </script>
    </head>
    <body>
            <form id="mainForm" name="mainForm" method="post">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
            <input type="hidden" name="LOGIN_NAME" value="<%= request.getParameter("LOGIN_NAME") %>"/>
            <table class="form">
                <tr>
                    <th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  	<div style="float: right;" id="Language" name="Language"></div>
				  	</th>
                </tr>
                <tr>
                    <td class="label"><label for="NAME">${labelMap.OLD_PASSWORD}</label></td>
                    <td colspan="3" class="input">
                        <input type="password" id="OLD_PASSWORD" name="OLD_PASSWORD" class="short" maxlength="255" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="EMAIL">${labelMap.PASSWORD}</label></td>
                    <td colspan="3" class="input">
                        <input type="password" id="PASSWORD" name="PASSWORD" class="short" maxlength="255" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="EMAIL">${labelMap.CONFIRM_PASSWORD}</label></td>
                    <td colspan="3" class="input">
                        <input type="password" id="CONFIRM_PASSWORD" name="CONFIRM_PASSWORD" class="short" maxlength="255"  onkeypress="return CODE_KeyPress(event);"/>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>