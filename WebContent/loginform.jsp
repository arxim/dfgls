<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="forms/error.jsp"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.obj.util.Variables"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>:: Doctor Fee Version 4.2 ::</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
       <!--   <link rel="stylesheet" type="text/css" href="css/share.css" media="all" /> -->
        <link rel="stylesheet" type="text/css" href="css/login.css" media="all" />
<!--[if lt IE 7]>
        <style type="text/css">
            div a, div input, div label, a img {
                /* Enable object located in div with transparent background on IE */
                position: relative; 
                z-index: 1;
            }
            form {
                background: none;
                filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='images/LoginScreen.png', enable='true', sizingMethod='crop');
            }
        </style>
<![endif]-->
        <script type="text/javascript" src="javascript/md5.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript">
<%
			//System.out.println(JDate.getEndMonthDate("2010","02"));
            long l = Long.MAX_VALUE;
            if (request.getParameter("LOGIN_NAME") != null && request.getParameter("PASSWORD") != null && request.getParameter("HOSPITAL_CODE") != null) {
                String LOGIN_NAME = request.getParameter("LOGIN_NAME");
                String PASSWORD = request.getParameter("PASSWORD");
                String HOSPITAL_CODE = request.getParameter("HOSPITAL_CODE");                
                
                String query = String.format("SELECT USERS.LOGIN_NAME, USERS.HOSPITAL_CODE, USERS.LANG_CODE, USERS.USER_GROUP_CODE, USER_GROUP.PERMISSION FROM USERS INNER JOIN USER_GROUP ON USERS.USER_GROUP_CODE = USER_GROUP.USER_GROUP AND USERS.HOSPITAL_CODE = USER_GROUP.HOSPITAL_CODE  WHERE USERS.ACTIVE = 1 AND LOGIN_NAME = '%1$s' AND PASSWORD = '%2$s' AND USERS.HOSPITAL_CODE = '%3$s'",
                        LOGIN_NAME, PASSWORD, HOSPITAL_CODE);
                
                 
//                  String query = String.format("SELECT USERS.LOGIN_NAME, USERS.HOSPITAL_CODE AS HOSPITAL_CODE , USERS.LANG_CODE, USERS.USER_GROUP_CODE, USER_GROUP.PERMISSION FROM USERS INNER JOIN USER_GROUP ON USERS.USER_GROUP_CODE = USER_GROUP.USER_GROUP WHERE USERS.ACTIVE = 1 AND LOGIN_NAME = '%1$s' AND PASSWORD = '%2$s' AND HOSPITAL_CODE = '%3$s'",
//                          LOGIN_NAME, PASSWORD, HOSPITAL_CODE);
                
                DataRecord record = DBMgr.getRecord(query);
                if (record != null) {
                    String cmd = String.format("UPDATE USERS SET LOGIN_DATE = '%1$s', LOGIN_TIME = '%2$s', IPADDRESS = '%3$s' WHERE LOGIN_NAME = '%4$s' AND PASSWORD = '%5$s' AND HOSPITAL_CODE = '%6$s'", JDate.getDate(), JDate.getTime(), request.getRemoteAddr(), LOGIN_NAME, PASSWORD, record.getField("HOSPITAL_CODE").getValue());
                    DBMgr.executeUpdate(cmd);
                    Variables.setHospitalCode(record.getField("HOSPITAL_CODE").getValue());
                    Variables.setUserID(LOGIN_NAME);
                    session.setAttribute("USER_ID", LOGIN_NAME);
                    session.setAttribute("PERMISSION", record.getField("PERMISSION").getValue());
                    session.setAttribute("HOSPITAL_CODE", record.getField("HOSPITAL_CODE").getValue().toString());
                    session.setAttribute("LANG_CODE", record.getField("LANG_CODE").getValue());
                    session.setAttribute("USER_GROUP_CODE", record.getField("USER_GROUP_CODE").getValue());
                    response.sendRedirect("main.jsp");
                    return;
                } else {
                    out.print("alert(\"Invalid username or password\")");
                }
            }
%>
    
    function LOGIN_Click() {
        if (document.mainForm.LOGIN_NAME.value.length <= 0 || document.mainForm.PASSWORD.value.length <= 0) {
            alert("Please fill username and password");
        }else {
            document.mainForm.PASSWORD.value = hex_md5(document.mainForm.PASSWORD.value);
            document.mainForm.submit();
        }
    }
        </script>
    </head>
    <body onload="document.mainForm.LOGIN_NAME.focus()" style="vertical-align: middle;">
        <form id="mainForm" name="mainForm" method="post" action="loginform.jsp">
            <div id="input">
                <div class="row">
                    <label for="username">User ID</label>
                    <input id="LOGIN_NAME" name="LOGIN_NAME" type="text" class="text" maxlength="50"/>
                </div>
                <div class="row">
                    <label for="username">Password</label>
                    <input id="PASSWORD" name="PASSWORD" type="password" class="text" maxlength="50" />
                </div>
                <div class="row">
                    <label for="username">Hospital</label>
                    <%=DBMgr.generateDropDownList("HOSPITAL_CODE", "", "SELECT CODE, DESCRIPTION_ENG FROM HOSPITAL WHERE ACTIVE = 1", "DESCRIPTION_ENG", "CODE", null)%>
                </div>
            </div>
            <div id="submit">
                <input id="LOGIN" name="LOGIN" type="image" class="image" src="images/login_button.png" alt="Log In" onclick="LOGIN_Click()"/>
            </div> <!--  
            <div id="intro">
                <p>Please fill your username and password to login to Doctor Fee Application.</p>
                <p>If you do not have an account, please contact administrator.</p>
            </div> -->
        </form>
    </body>
</html>