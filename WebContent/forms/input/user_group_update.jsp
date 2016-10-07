<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_USER_GROUP)) {
                response.sendRedirect("../message.jsp");
                return;
            }
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            HashMap m = (HashMap)request.getParameterMap();
            //out.println(m.toString());
            Iterator keyValuePairs1 = m.entrySet().iterator();

            for (int i = 0; i < m.size(); i++) {
                Map.Entry entry = (Map.Entry) keyValuePairs1.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key.toString().contains("_CHECKBOX")) {
                    out.println("<br />" + key.toString() + " --> " + request.getParameter(key.toString()));
                }
                m.get(key.toString());
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
    </head>
    <body>
    </body>
</html>
