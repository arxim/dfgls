<jsp:useBean id="pc" scope="session" class="df.bean.process.ProcessDailyCalculateBean" />
<%
//ProcessBasicAllocateBean
	String msg =pc.processRequest(
			   	request.getParameter("ROW"),
			   	request.getParameter("currentRowID"),
			   	request.getParameter("HOSPITAL_CODE").toString(),
				request.getParameter("START_DATE"), 
			   	request.getParameter("END_DATE"),
			   	request.getParameter("INVOICE_NO"),
			   	request.getParameter("LINE_NO"),
			   	request.getParameter("TRANSACTION_DATE")
	);
	//String [] temp = msg.split("\\|");
	response.setContentType("text/xml; charset=UTF-8");
    out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    out.print("<msg>");
    out.print("<code>"+msg+"</code>");
    out.print("<msg_E>"+msg+"</msg_E>");
    out.print("<msg_T>"+msg+"</msg_T>");
    out.print("<msg_detail>"+msg+"</msg_detail>");
    out.print("</msg>");
%>