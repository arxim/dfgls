<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="forms/error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>

<% 
        String lang = request.getParameter("lang");
        if (lang == null)
            lang = LabelMap.LANG_EN;
        if (lang.equals(LabelMap.LANG_EN) || lang.equals(LabelMap.LANG_TH))
            session.setAttribute("LANG_CODE", lang);
        response.sendRedirect(request.getHeader("Referer"));
//        return;
%>
