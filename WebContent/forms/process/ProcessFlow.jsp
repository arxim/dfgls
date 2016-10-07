<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@ include file="../../_global.jsp"%>

<%
	// Test Update Data Show Value.

	//
	// Verify permission
	//

	if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_DEMO)) {
		response.sendRedirect("../message.jsp");
		return;
	}

	//
	// Initial LabelMap
	//

	if (session.getAttribute("LANG_CODE") == null) {
		session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
	}

	//ProcessUtil proUtil = new ProcessUtil();                    
	//Batch batch = new Batch();                                 
	LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "Flow Process", "ระบบ Flow");
	labelMap.add("PAY_DATE", "Pay Date", "วันที่จ่ายเงิน");
	labelMap.add("COL_0", " - ", " - ");
	labelMap.add("COL_1", "Process Name", "ชื่อโปรเซส");
	labelMap.add("COL_2", "Count", "จำนวนโปรเซส");
	labelMap.add("COL_3", " - ", " - ");
	labelMap.add("COL_4", " - ", " - ");
	labelMap.add("INTERFACE", "Interface", "นำเข้ารายการทั่วไป");
	labelMap.add("PROCESS_01", "Interface Bill", "นำเข้ารายการรักษา");
	labelMap.add("PROCESS_02", "Interface Verify", "นำเข้ารายการอ่านผล");
	labelMap.add("PROCESS_03", "Interface AR Receipt","นำเข้ารายการรับชำระ");
	labelMap.add("PROCESS_04", "Import Transaction","นำรายการรักษาเข้าระบบ");
	labelMap.add("PROCESS_05", "Compute daily", "คำนวณรายวัน");
	labelMap.add("PROCESS_06", "Process Receipt","ประมวลผลรายการรับเงิน");
	labelMap.add("PROCESS_06_01", "Process Receipt by Cash","ประมวลผลรายการรับเงิน(เงินสด)");
	labelMap.add("PROCESS_06_02", "Process Receipt by AR","ประมวลผลรายการรับเงิน(รับชำระหนี้)");
	labelMap.add("PROCESS_06_03", "Process Receipt by Payor Office","ประมวลผลรายการรับเงิน(คู่สัญญา)");
	labelMap.add("PROCESS_06_04", "Process Receipt by Doctor","ประมวลผลรายการรับเงิน(แพทย์)");
	labelMap.add("PROCESS_07", "Compute monthly","คำนวณรายการรักษารายเดือน");
	labelMap.add("PROCESS_08", "Payment monthly","คำนวณรายการทำจ่ายรายเดือน");
	labelMap.add("PROCESS_09", "Export bank", "ประมวลผลรายการโอนแบงค์");
	labelMap.add("PROCESS_10", "Compute summary tax 40(6)","คำนวณรายการภาษี 40(6)");
	labelMap.add("PROCESS_11", "Compute payment monthly for Salary","คำนวณรายการจ่ายเงินเดือนแพทย์");
	labelMap.add("VALUEBUTTONPROCESS", "Process", "Process");
	labelMap.add("NAMEBUTTONPROCESS", "Process", "คำนวณ");
	labelMap.add("MM", "Month", "เดือน");
	labelMap.add("YYYY", "Year", "ปี");
	labelMap.add("1", "January", "มกราคม");
	labelMap.add("2", "February", "กุมภาพันธ์");
	labelMap.add("3", "March", "มีนาคม");
	labelMap.add("4", "April", "เมษายน");
	labelMap.add("5", "May", "พฤษภาคม");
	labelMap.add("6", "June", "มิถุนายน");
	labelMap.add("7", "July", "กรกฎาคม");
	labelMap.add("8", "August", "สิงหาคม");
	labelMap.add("9", "September", "กันยายน");
	labelMap.add("10", "October", "ตุลาคม");
	labelMap.add("11", "November", "พฤศจิกายน");
	labelMap.add("12", "December", "ธันวาคม");
	labelMap.add("SELECT", "Display", "แสดงผล");

	request.setAttribute("labelMap", labelMap.getHashMap());
	DBConnection conn = new DBConnection();
	conn.connectToLocal();
	Batch bt = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), conn);

	String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${labelMap.TITLE_MAIN}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="../../javascript/util.js"></script>
<script type="text/javascript" src="../../javascript/ajax.js"></script>

</head>
<body bgcolor="#dde4e8" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

	<form id="mainForm" name="mainForm" method="post" action="ProcessPaymentMonthly.jsp">
		<input type="hidden" id="MM" name="MM" value="<%= bt.getMm() %>" />
		<input type="hidden" id="YY" name="YY" value="<%= bt.getYyyy() %>" />
	
<!-- ImageReady Slices (flowAppNew.psd) -->
	<br/>
	<br/>
	<table id="Table_01" width="700" height="400" border="0" cellpadding="0" cellspacing="0" align="center">
		<tr>
			<td height="58">
				<img src="images/process_01.png" width="700" height="58" alt="">
			</td>
		</tr>
		<tr>
			<td height="15">
				<img src="images/process_02.png" width="700" height="15" alt="">
			</td>
		</tr>
		<tr>
			<td height="64">
				<img src="images/process_03.png" alt="" width="700" height="64" border="0" usemap="#Map">
			</td>
		</tr>
		<tr>
			<td height="55" valign="top" style="background-image:url(images/process_04.png); text-align: right; padding-top: 10px;">
			
				<%
					if(request.getParameter("acIdNewData") != null){ 
						
						ResultSet resultInterface  =  null;
			 			
			 			String  sqlCommandResualt  =  "";
			 			
			 			sqlCommandResualt  = "SELECT DISTINCT" +   
			 											 " ( SELECT COUNT(DISTINCT CODE) FROM ORDER_ITEM  WHERE  ACTIVE  = 2  AND HOSPITAL_CODE = '" + hospital_code + "' ) AS  NEW_ORDER_ITEM  , "+
														 " ( SELECT COUNT(DISTINCT CODE) FROM PAYOR_OFFICE WHERE  ACTIVE = 2 AND HOSPITAL_CODE =  '" + hospital_code + "' )  AS  NEW_PAYOR  , " +
			 					 						 " ( SELECT ( COUNT(DISTINCT DOCTOR_CODE) +  (SELECT COUNT(DISTINCT DOCTOR_CODE) AS INFO " + 
			 											 " FROM INT_HIS_VERIFY WHERE DOCTOR_CODE NOT IN (SELECT CODE FROM DOCTOR WHERE HOSPITAL_CODE LIKE '" + hospital_code + "')"+
			 											 " AND TRANSACTION_DATE BETWEEN '00000000' AND '99999999' AND HOSPITAL_CODE LIKE '" + hospital_code + "')) " + 
			 											 " FROM INT_HIS_BILL WHERE DOCTOR_CODE NOT IN (SELECT CODE FROM DOCTOR WHERE HOSPITAL_CODE LIKE '" + hospital_code + "')"+
			 											 " AND BILL_DATE BETWEEN '00000000' AND '99999999' "  + 
			 											 " AND HOSPITAL_CODE LIKE '" + hospital_code + "'  ) AS  NEW_DOCTOR  "  +
			 											 " FROM DOCTOR DR	 WHERE HOSPITAL_CODE  =  '" + hospital_code + "'";
			 			
			 			// out.print(sqlCommandResualt);
			 			
			 			resultInterface  =  conn.executeQuery(sqlCommandResualt);
			 			
			 			resultInterface.next(); 
			 			
				%>
			
					 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 15px; padding-right: 210px; text-align: right;">
		    	 	 		<%=resultInterface.getString("NEW_DOCTOR")%> 
		    	 	 </div>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 15px; padding-right: 210px; text-align: right;">
		    	 	 	<%=resultInterface.getString("NEW_ORDER_ITEM")%> 
		    	 	 </div>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 15px; padding-right: 210px; text-align: right;">
		    	 	 	<%=resultInterface.getString("NEW_PAYOR")%> 
		    	 	 </div>
		    	
		    	<%
					} else  {
		    	%>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 15px; padding-right: 210px; text-align: right;">
		    	 	 	0- 
		    	 	 </div>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 15px; padding-right: 210px; text-align: right;">
		    	 	 	0-
		    	 	 </div>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 15px; padding-right: 210px; text-align: right;">
		    	 	 	0-
		    	 	 </div>
		    	<% } %>
	    	</td>
  		</tr>
		<tr>
			<td height="10">
				<img src="images/process_05.png" width="700" height="10" alt="">
			</td>
		</tr>
		<tr>
			<td height="14">
				<img src="images/process_06.png" width="700" height="14" alt="">
			</td>
		</tr>
		<tr>
			<td height="16">
				<img src="images/process_07.png" width="700" height="16" alt="">
			</td>
		</tr>
		<tr>
			<td height="61">
				<img src="images/process_08.png" alt="" width="700" height="61" border="0" usemap="#Map2">
			</td>
  		</tr>
		<tr>
			<td height="68" valign="top" background="images/process_09.png" style="padding-top: 9px;">
			
				<%
				if(request.getParameter("acIdReceipt") != null){
					
					
					String  sqlCommandReceipt  = "SELECT " + 
								" DISTINCT ISNULL((SELECT COUNT(*) " +
										" FROM TRN_DAILY " + 
										" WHERE " + 
										" PAY_BY_AR = 'Y' " + 
										" AND HOSPITAL_CODE =  '" + hospital_code + "'"+  
										" AND TRANSACTION_DATE LIKE  '"+bt.getYyyy()+""+bt.getMm()+"%'" +  
										" AND ACTIVE = '1' "  +
									" ) ,0) AS  AR , " +  
									" ISNULL(( SELECT COUNT( *)  " + 
										" FROM TRN_DAILY " + 
										" WHERE " +
										" PAY_BY_PAYOR = 'Y' " +
										" AND HOSPITAL_CODE =  '"+hospital_code+"'" + 
												" AND TRANSACTION_DATE LIKE  '"+bt.getYyyy()+""+bt.getMm()+"%'" +  
										" AND ACTIVE = '1' "  +
									" ) , 0) AS PAYOR , " + 
									" ISNULL(( SELECT COUNT( *)  " +
										" FROM TRN_DAILY " + 
										" WHERE " + 
										" PAY_BY_DOCTOR = 'Y'" + 
										" AND HOSPITAL_CODE =  '" + hospital_code + "'" +  
												" AND TRANSACTION_DATE LIKE  '"+bt.getYyyy()+""+bt.getMm()+"%'" +  
										" AND ACTIVE = '1' "  +
									" ) , 0) AS DOCTOR , " + 
								" COUNT(*)  AS TOTAL " +
								" FROM TRN_DAILY  " + 
								" WHERE HOSPITAL_CODE =  '" + hospital_code + "'" +  
								" AND TRANSACTION_DATE LIKE  '" + bt.getYyyy() +  "" + bt.getMm() +"%'" +  
								" AND ACTIVE = '1' " ;
					
					  System.out.print("Info : "  +  sqlCommandReceipt);
					
					  ResultSet resultReceipt = conn.executeQuery(sqlCommandReceipt);
					
					  // Next  Record
					  resultReceipt.next(); 
					
				%>
			
					<div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 480px; text-align: right;">
		    	 	 	<%=resultReceipt.getString("AR")%> 
		    	 	 </div>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 480px; text-align: right;">
		    	 	 	<%=resultReceipt.getString("PAYOR")%>
		    	 	 </div>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 480px; text-align: right;">
		    	 	 	<%=resultReceipt.getString("DOCTOR")%>
		    	 	 </div>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 500px; text-align: right;">
		    	 	 	 <table style="margin: 0px; padding: 0px; padding-left: 105px;" border='0' cellpadding="0" cellspacing="0" >
		    	 			<tr >
		    	 				<td valign="middle">
		    	 					<img src="images/ok.png" width="16" height="16" alt="" />
		    	 				</td>
		    	 				<td>
		    	 					Success !!!
		    	 				</td>
		    	 			</tr>
		    	 		</table>
		    	 	 </div>
	   			 <%
				} else { 
	   			 %>
	   			     <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 480px; text-align: right;">
		    	 	 	0- 
		    	 	 </div>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 480px; text-align: right;">
		    	 	 	0-
		    	 	 </div>
		    	 	 <div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 480px; text-align: right;">
		    	 	 	0-
		    	 	 </div>
	   			 <%
				}
	   			 %>
	   			 
	   			 
	   			 <%
	   			 	if(request.getParameter("acIdGuarantee") != null){
	   			 		
	   			 	 	String sqlCommandGuarantee = "SELECT COUNT(*) AS GUARANTEE FROM SUMMARY_GUARANTEE  WHERE MM = '" + bt.getMm() + "' AND YYYY = '"  + bt.getYyyy() +"' AND  HOSPITAL_CODE = '" + hospital_code + "' ";

	   			 	 	ResultSet resultGuarantee = conn.executeQuery(sqlCommandGuarantee);
	   			 
		    	 	 		if(resultGuarantee.next() && resultGuarantee.getInt("GUARANTEE") > 0) { 
		    	 	 			%>
		    	 	 			<div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 380px; text-align: right;">
		    	 					 Success !!!
		    	 	 		 	</div>
		    	 	 	
		    	 	 		    <%  } else { %>
		    	 	 			<div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 345px; text-align: right;">
		    	 					 Not Guarantee !!!
		    	 	 		 	</div>
				    	 	 	<% 
				    	 	 		}
	   			 	}  
	   			
	   			 	if(request.getParameter("acIdPayment") != null){
	   			 		
	   			 	    
	   			 		String sqlCommandMonthlyCal = "SELECT COUNT(*) AS MONTHLY_CAL FROM SUMMARY_PAYMENT  WHERE MM = '" + bt.getMm() + "' AND YYYY = '"  + bt.getYyyy() +"' AND  HOSPITAL_CODE = '" + hospital_code + "' ";
	   				 
	   			 		// out.print(sqlCommandMonthlyCal);
	   			 		
	   			 	 	ResultSet resultPayment = conn.executeQuery(sqlCommandMonthlyCal);
	   			 
		    	 	 		if(resultPayment.next() && resultPayment.getInt("MONTHLY_CAL") > 0) { 
		    	 	 			%>
		    	 	 			<div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; text-align: right;">
		    	 					 <table style="margin: 0px; padding: 0px; padding-left: 530px;" border='0' cellpadding="0" cellspacing="0" >
		    	 					 	<tr >
		    	 					 		<td valign="middle">
		    	 					 			<img src="images/ok.png" width="16" height="16" alt="" />
		    	 					 		</td>
		    	 					 		<td valign="middle">
		    	 					 			Success !!!
		    	 					 		</td>
		    	 					 	</tr>
		    	 					 </table>
		    	 	 		 	</div>
		    	 	 	
		    	 	 		    <%  } else { %>
		    	 	 			<div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 100px; text-align: right;">
		    	 					 Not Payment !!!
		    	 	 		 	</div>
				    	 	 	<% 
				    	 	 		}
	   			 	}  
	   			  
	   			if(request.getParameter("acIdTax") != null){
   			 		
   			 	    
   			 		String sqlCommandMonthlyTax = "SELECT COUNT(*) AS TAX FROM SUMMARY_TAX_402  WHERE MM = '" + bt.getMm() + "' AND YYYY = '"  + bt.getYyyy() +"' AND  HOSPITAL_CODE = '" + hospital_code + "' ";
   				 
   			 		//out.print(sqlCommandMonthlyTax);
   			 		
   			 	 	ResultSet resultTax = conn.executeQuery(sqlCommandMonthlyTax);
   			 
	    	 	 		if(resultTax.next() && resultTax.getInt("TAX") > 0) { 
	    	 	 			%>
	    	 	 			<div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; text-align: right;">
	    	 					 <table style="margin: 0px; padding: 0px; padding-left: 400px;" border='0' cellpadding="0" cellspacing="0" >
	    	 					 	<tr >
	    	 					 		<td valign="middle">
	    	 					 			<img src="images/ok.png" width="16" height="16" alt="" />
	    	 					 		</td>
	    	 					 		<td valign="middle">
	    	 					 			Success !!!
	    	 					 		</td>
	    	 					 	</tr>
	    	 					 </table>
	    	 	 		 	</div>
	    	 	 	
	    	 	 		    <%  } else { %>
	    	 	 			<div style="text-align: right; font-size: 12px; color: white; font-weight:bold; margin: 0px; padding: 0px; height: 16px; padding-right: 220px; text-align: right;">
	    	 					 Not TAX !!!
	    	 	 		 	</div>
			    	 	 	<% 
			    	 	 		}
   			 		}  
	   			
	   			 %>
	   			 
	   		</td>
  		</tr>
		<tr>
	  		<td height="20px" style="height:20px;">
				<img src="images/process_10.png" width="700" height="20" alt="">
			</td>
		</tr>
	</table>
	<!-- End ImageReady Slices -->

	<map name="Map">
	  <area shape="circle" coords="162,34,30" href="javascript:void(0)" onclick="OpenWindow('ProcessFlowPopupInterface.jsp');" alt="Interface File data">
	  <area shape="circle" coords="159,40,2"  href="javascript:void(0)" onclick="OpenWindow('ProcessFlowPopupInterface.jsp');" alt="Interface File data">
	  <area shape="circle" coords="299,37,30" href="javascript:void(0)" onclick="OpenWindow('ProcessFlowPopupImportBill.jsp');" alt="Import Data">
	  <area shape="circle" coords="433,32,31" href="?acIdNewData=true" alt="New Data ( Doctor , Order Item , Payor )">
	  <area shape="circle" coords="570,32,30" href="javascript:void(0)" onclick="OpenWindow('ProcessFlowPopupComputedaily.jsp');" alt="BasicDailyCal">
	  <area shape="circle" coords="307,28,6" href="#">
	</map>

	<map name="Map2">
	  <area shape="circle" coords="164,30,29" href="?acIdReceipt=true">
	  <area shape="circle" coords="301,29,28" href="?acIdGuarantee=true" alt="GuaranteeCall">
	  <area shape="circle" coords="443,30,29" href="?acIdTax=true" alt="tax">
	  <area shape="circle" coords="575,29,29" href="?acIdPayment=true" alt="Payment">
	</map>
	
	</form>
	
	<script language="javascript">
		function OpenWindow(url, temp) {
			var mm = document.getElementById("MM");
			var yy = document.getElementById("YY");
			url = url + "?mm=" + mm.value + "&yy=" + yy.value;
			if (temp == '')
				temp = 'temp';
			temp415 = window.open(url,temp,'scrollbars=yes,statusbars=no,toolbar=no,location=no,status=no,menubar=no,resizable=yes,dependent=no');
			temp415.focus();
		}
	</script>
</body>
</html>
