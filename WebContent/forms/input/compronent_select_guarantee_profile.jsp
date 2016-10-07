<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="df.bean.db.DBMgr"%>
<%
	 
	String  is_guarantee_profile =  request.getParameter("is_guarantee_profile");
	String  doctorProfileCode  =  request.getParameter("doctor_profile_code");
	String hospital_code =  request.getParameter("hospital_code");
	
	 if("Y".equals(is_guarantee_profile)) { 
		out.print(DBMgr.generateDropDownList("GUARANTEE_DR_CODE", "medium", "SELECT CODE FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospital_code+"' AND DOCTOR_PROFILE_CODE = '"+doctorProfileCode+"' ORDER BY CODE", "CODE", "CODE" , "GUARANTEE_DR_CODE"));
    } else { 
    	out.print(DBMgr.generateDropDownList("GUARANTEE_DR_CODE", "medium", "SELECT CODE FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospital_code+"' ORDER BY CODE", "CODE", "CODE", "GUARANTEE_DR_CODE"));
    }
%>