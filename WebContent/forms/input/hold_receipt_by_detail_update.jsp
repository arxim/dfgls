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
            Map<String,String> m = (Map)request.getParameterMap();
            //HashMap m = (HashMap)request.getParameterMap();
            //out.println(m.toString());
            Iterator keyValuePairs1 = m.entrySet().iterator();
            String item[];
            //String receipt_date = JDate.saveDate(request.getParameter("RECEIPT_DATE").toString());
            String cmd;
            DBConnection con = new DBConnection();
            con.connectToLocal();
            //con.connectToServer();
            for (int i = 0; i < m.size(); i++) {
                Map.Entry entry = (Map.Entry) keyValuePairs1.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key.toString().contains("INVOICE_NO_")) {
                    /*if(receipt_date.length()<5){
                        receipt_date = JDate.getDate();
                    }*/
                    item = request.getParameter(key.toString()).split(":");
                    cmd = String.format("UPDATE TRN_DAILY SET PAY_BY_CASH_AR = 'N', PAY_BY_AR = 'N', TRANSACTION_MODULE = 'TR', RECEIPT_DATE = '', RECEIPT_NO = '', RECEIPT_TYPE_CODE = 'AR', YYYY = '', MM = '', DD = '' WHERE INVOICE_NO = '%1$s' AND LINE_NO = '%2$s' AND ACTIVE = '1' AND TRANSACTION_TYPE = 'INV' AND INVOICE_TYPE <> 'ORDER' AND (BATCH_NO IS NULL OR BATCH_NO = '')", item[0], item[1]);
                    con.executeUpdate(cmd);
                    //out.println("<br />" + key.toString() + " --> " + request.getParameter(key.toString()));
                }
                m.get(key.toString());
            }
            session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/hold_receipt_by_detail.jsp"));
            con.Close();
            //con.freeConnection();
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
    </body>
</html>
