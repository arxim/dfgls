<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%
            session.invalidate();
            response.sendRedirect("loginform.jsp");
            return;
%>