<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="error.jsp"%>

<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.jsp.Util"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.process.ProcessFlow"%>

<%
            
            //
            // Initial LabelMap
            //
            
            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            ProcessUtil proUtil = new ProcessUtil();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Search", "ค้นหา");
            request.setAttribute("labelMap", labelMap.getHashMap());   
            request.setCharacterEncoding("UTF-8");

            DBConnection conn = new DBConnection();
            //conn.connectToServer();
			conn.connectToLocal();
            String mm = request.getParameter("mm");
            String yy = request.getParameter("yy");
            String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
            ProcessFlow pf = new ProcessFlow(conn, hospital_code);
            String value = pf.InterfaceHtml(mm, yy);
			conn.Close();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/search.css" media="all" />
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="ProcessPaymentMonthly.jsp">
            <%=value%>
        </form>                
    </body>
</html>