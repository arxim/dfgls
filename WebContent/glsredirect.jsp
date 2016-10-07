<%@page import="df.jsp.Guard" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<HEAD>
<title></title>
<!--<meta HTTP-EQUIV='Refresh' CONTENT='1; URL=http://10.105.15.1:8080/nurapp/faces/Login.jsp'>-->
<!--<meta HTTP-EQUIV='Refresh' CONTENT='1; URL=http://172.18.10.44:8080/df/loginform.jsp'>-->

<script type="text/javascript">
	function runpgm() {
		
		//window.location = 'http://dc-df01-t:8080/df/menu.jsp';
 	//window.location = '/dfglswlg/main.jsp';
	
	}
<%
			String LOGIN_NAME = null;
                String HOSPITAL_CODE = null;
            if (request.getParameter("LOGIN_NAME") != null  && request.getParameter("HOSPITAL_CODE") != null) {
               LOGIN_NAME = request.getParameter("LOGIN_NAME");

               HOSPITAL_CODE = request.getParameter("HOSPITAL_CODE");
              }
              else {
              	
 				LOGIN_NAME = "sys";

               HOSPITAL_CODE = "00005";
              }         
                    session.setAttribute("USER_ID", LOGIN_NAME);
                    //session.setAttribute("PERMISSION", record.getField("PERMISSION").getValue());
                    session.setAttribute("HOSPITAL_CODE", HOSPITAL_CODE);
                    session.setAttribute("LANG_CODE", "ENG");
                    session.setAttribute("USER_GROUP_CODE", "5");
                    session.setAttribute("PERMISSION", Guard.PERMISSION_ALL);
                    String xurl = "/dfglswlg/main.jsp";
                   if (request.getParameter("url") != null) {
                   	xurl = request.getParameter("url");
                   	}
                    response.sendRedirect(xurl);
               
//<body onload="runpgm()";>glsredirect
             
            
%>
	
</script>
</HEAD>
<body onload="runpgm()";>glsredirect
</body>
</html>
