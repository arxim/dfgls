<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.DialogBox"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.db.table.TrnDaily"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@ include file="_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_DEMO)) {
                response.sendRedirect("../message.jsp");
                return;
            }

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="css/share.css" media="all" />
        <script type="text/javascript" src="javascript/util.js"></script>
        <script type="text/javascript" src="javascript/ajax.js"></script>
        <link rel="stylesheet" type="text/css" href="css/calendar.css" />
        <script type="text/javascript" src="javascript/calendar.js"></script>

    </head>
    <body>
        <APPLET codebase="classes" code="df.applet.process.TransDailyPcss.class" width="810" height="600">
            <PARAM name="HOSPITAL_CODE" value=<%=session.getAttribute("HOSPITAL_CODE").toString()%>>
            <PARAM name="USER_ID" value=<%=session.getAttribute("USER_ID").toString()%>>
        </APPLET>
        
    </body>
</html>


