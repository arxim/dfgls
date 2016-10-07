<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>

<%@page import="df.jsp.LabelMap"%>
<%@page import="java.io.*"%>

<%
            //
            // Initial LabelMap
            //

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Error", "เกิดความผิดพลาด");
            labelMap.add("MSG", "Please send following error information to developer team to fix this problem.", "กรุณาส่งรายละเอียดข้อผิดพลาดนี้ให้ทีมพัฒนาเพื่อทำการแก้ไขต่อไป");

            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Initial exception
            //
            
            if (exception == null) {
                exception = new Exception();
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/share.css" media="all" />
        <title>${labelMap.TITLE_MAIN}</title>
    </head>
    <body>
        <h1>${labelMap.TITLE_MAIN}</h1>
        <p>${labelMap.MSG}</p>
        <p><%=exception%></p>
        <p><% exception.printStackTrace(new PrintWriter(out));%></p>
    </body>
</html>
