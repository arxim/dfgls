<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="df.bean.obj.util.ReadProperties"%>
<%@page import="org.apache.log4j.Logger"%>
<%
	Logger logger = Logger.getLogger("page_control.jsp");
	ReadProperties rp = new ReadProperties();
    String hospitalCode = session.getAttribute("HOSPITAL_CODE").toString();
    String userCode = session.getAttribute("USER_ID").toString();
    String userGroupCode = session.getAttribute("USER_GROUP_CODE").toString();
    String menuRequest = request.getParameter("MENU");
	String linkPage = "";
	if(menuRequest.equals("MasterTimeTable")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/masterTimeTablePage";
	}else if(menuRequest.equals("SetupBasicAllocate")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/stpMethodAllocateMainPage";
	}else if(menuRequest.equals("InterfaceAccrual")){
	}else{
	}
	logger.info(linkPage);
%>
<html>
	<head>
	<script type="text/javascript">
		function load(){
            document.mainForm.submit();
		}
	</script>
	</head>
    <body onload="load();">
		<form id="mainForm" name="mainForm" method="get" action="<%= linkPage %>">
            <input type="hidden" id="USER_ID" name="USER_ID" value="<%= userCode %>"/>
            <input type="hidden" id="user_group_code" name="USER_GROUP_CODE" value="<%= userGroupCode %>"/>
            <input type="hidden" id="HOSPITAL_CODE" name="HOSPITAL_CODE" value="<%= hospitalCode %>"/>
		</form>
    </body>
</html>