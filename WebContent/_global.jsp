<%@page import="df.jsp.Guard" %>

<%
            //final boolean DEBUG = true;
            //BYPASS LOGIN = false(login) true(bypass login)
            final boolean BYPASS_LOGINFORM = false;
            
            if (BYPASS_LOGINFORM) {
                session.setAttribute("USER_ID", "sys");
                session.setAttribute("PERMISSION", Guard.PERMISSION_ALL);
                session.setAttribute("HOSPITAL_CODE", "00001");
            }
%>