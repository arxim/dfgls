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
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            HashMap m = (HashMap)request.getParameterMap();
            //out.println(m.toString());
            Iterator keyValuePairs1 = m.entrySet().iterator();
            String list = "(";
            String receipt_date = JDate.saveDate(request.getParameter("RECEIPT_DATE").toString());

            for (int i = 0; i < m.size(); i++) {
                Map.Entry entry = (Map.Entry) keyValuePairs1.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                System.out.println(i+" --> " + request.getParameter(key.toString()));
                if (key.toString().contains("INVOICE_NO_")) {
                    if (!list.equalsIgnoreCase("(")) {
                        list += ",";
                    }
                    list += "'" + request.getParameter(key.toString()) + "'";
                }
                m.get(key.toString());
            }
            list += ")";
            if(receipt_date.length()<5){
                receipt_date = JDate.getDate();
            }
            String cmd = String.format("UPDATE TRN_DAILY SET PAY_BY_CASH_AR = CASE WHEN SUBSTRING(INVOICE_DATE,1,6) = '"+receipt_date.substring(0, 6)+"' THEN 'Y' ELSE 'N' END, PAY_BY_AR = CASE WHEN SUBSTRING(INVOICE_DATE,1,6) != '"+receipt_date.substring(0, 6)+"' THEN 'Y' ELSE 'N' END, TRANSACTION_MODULE = 'AR', RECEIPT_DATE = '%1$s', RECEIPT_NO = INVOICE_NO, RECEIPT_TYPE_CODE = 'AR', YYYY = '%2$s', MM = '%3$s', DD = '%4$s' WHERE INVOICE_NO IN %5$s AND ACTIVE = '1' AND TRANSACTION_TYPE = 'INV' AND INVOICE_TYPE <> 'ORDER' AND (YYYY IS NULL OR YYYY = '')", receipt_date, receipt_date.substring(0, 4), receipt_date.substring(4, 6), receipt_date.substring(6, 8), list);
            DBConnection con = new DBConnection();
            con.connectToLocal();
            con.executeUpdate(cmd);
            session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/receipt_by_master.jsp"));
            con.Close();
            response.sendRedirect("../message.jsp");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
    </head>
    <body>
        <%=list%>
    </body>
</html>
