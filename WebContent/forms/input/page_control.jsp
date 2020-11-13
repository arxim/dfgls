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
    String year = request.getParameter("YYYY");
    String month = request.getParameter("MM");
    String transactionDate = request.getParameter("TRANSACTION_DATE");
    String targetFile = request.getParameter("target_file");
    String processName = request.getParameter("PROCESS_NAME");
	String linkPage = "";
	System.out.println(request.getParameter("MENU"));
	if(menuRequest.equals("MasterTimeTable")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/masterTimeTablePage";
	}else if(menuRequest.equals("SetupBasicAllocate")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/stpMethodAllocateMainPage";
	}else if(menuRequest.equals("InterfaceAccrual")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8883/exportFileDF";
	}else if(menuRequest.equals("DFTransaction")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/invoiceSearchPage";
	}else if(menuRequest.equals("TimeTable")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/guaranteeTimeTablePage";
	}else if(menuRequest.equals("WithHoldingTax")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8082/tax/initProcessTax";
	}else if(menuRequest.equals("RadiologyShiftFactor")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/radiologyShiftFactorPage";
	}else if(menuRequest.equals("MasterRadiologyTimetable")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/masterRadiologyTimetablePage";
	}else if(menuRequest.equals("RadiologyTimetable")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/radiologyTimetablePage";
	}else if(menuRequest.equals("RadiologyPoolProcess")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/allocatePoolFactorPage";
	}else if(menuRequest.equals("SetupCaseMapping")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/caseMappingMainPage";
	}else if(menuRequest.equals("SetupCaseTimetable")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/caseTimeTablePage";
	}else if(menuRequest.equals("SetupDoctor")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/doctorMainPage";
	}else if(menuRequest.equals("AdjustRevenue")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/adjustRevenueMainPage";
	}else if(menuRequest.equals("ProcessRollback")){
		linkPage = "http://"+rp.getPropertiesData("config.properties", "interface.","ip").get("ip")+":8989/mainApp/processRollbackPage";
	}else{}
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
		<form id="mainForm" name="mainForm" method="post" action="<%= linkPage %>">
            <input type="hidden" id="USER_ID" name="USER_ID" value="<%= userCode %>"/>
            <input type="hidden" id="user_group_code" name="USER_GROUP_CODE" value="<%= userGroupCode %>"/>
            <input type="hidden" id="HOSPITAL_CODE" name="HOSPITAL_CODE" value="<%= hospitalCode %>"/>
            <input type="hidden" id="USER_ID" name="USER_ID" value="<%= userCode %>"/>
            <input type="hidden" id="PROCESS_NAME" name="PROCESS_NAME" value="<%= processName %>"/>
            <input type="hidden" id="YYYY" name="YYYY" value="<%= year %>"/>
            <input type="hidden" id="MM" name="MM" value="<%= month %>"/>
            <input type="hidden" id="target_file" name="target_file" value="<%= targetFile %>"/>
		</form>
    </body>
</html>