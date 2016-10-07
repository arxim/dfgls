
<%@ page contentType="text/xml; charset=UTF-8" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="java.sql.*"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessReceipt"%>
<%@include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //
            if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_PREPARE_GUARANTEE)) {
                response.sendRedirect("../message.jsp");
                return;
            }

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            
            DBConnection conn;
            conn = new DBConnection();
            conn.connectToLocal();
        	
            ProcessReceipt proWriteoff = new ProcessReceipt(conn);
           
            String start_date = request.getParameter("START_DATE");
            String end_date = request.getParameter("END_DATE");
            String hospital_code = request.getParameter("HOSPITAL_CODE");
            try {
                out.print("<RESULT><SUCCESS>" + 
						proWriteoff.updateReceiptWriteOff(start_date,end_date,hospital_code,"TRN_DAILY")+
				"</SUCCESS></RESULT>");
            }catch (Exception  e) {
                out.print("<RESULT><SUCCESS>false</SUCCESS></RESULT>");
            }finally { 
                out.close();
		conn.Close();
            }
%>
