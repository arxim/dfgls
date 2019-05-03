<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.Types"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.obj.util.Variables"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.table.Batch"%>

<%@ include file="../../_global.jsp" %>
<%
//
// Verify permission
//
//out.println("session="+session);
//out.println("page="+Guard.PAGE_INPUT_GROUP_DOCTOR_CATEGORY);
            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_EXPENSE_PERIOD)) {
                response.sendRedirect("../message.jsp");
                return;
            }
            DBConnection con;
			con = new DBConnection();
			con.connectToLocal();
			
            ProcessUtil proUtil = new ProcessUtil();
			Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
			//con.Close();
			//con.freeConnection();
			String batch_year = b.getYyyy();
			String batch_month = b.getMm();
			
// Initial LabelMap
//

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Setup Advance Adjust Details", "รายละเอียดการตั้งค่ารายการปรับปรุงล่วงหน้า");
            labelMap.add("LABEL_DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("LABEL_EXPENSE_CODE", "Adjust Code", "รหัสรายการปรับปรุง");
            labelMap.add("LABEL_TYPE_EXPENSE", "Adjust Type", "ประเภทรายการปรับปรุง");
            labelMap.add("LABEL_TAX_TYPE", "Tax Type", "ประเภทภาษี");
            labelMap.add("LABEL_NOTE", "Note", "ข้อความ");
            labelMap.add("LABEL_AMOUNT", "Amount", "จำนวนเงิน");
            labelMap.add("LABEL_TAX_AMOUNT", "Tax Amount", "จำนวนเงินภาษี");
            labelMap.add("LABEL_PAY_AMOUNT", "Pay Amount", "จำนวนเงินที่จ่าย");
            labelMap.add("LABEL_TOTAL_AMOUNT", "Total Amount", "จำนวนเงินคงเหลือ");
            labelMap.add("LABEL_PAY_TAX_AMOUNT", "Pay Tax Amount", "จำนวนเงินภาษีที่จ่าย");
            labelMap.add("LABEL_TOTAL_TAX_AMOUNT", "Total Tax Amount", "จำนวนเงินภาษีคงเหลือ");
            labelMap.add("LABEL_START_TERM", "Start Term", "ช่วงที่เริ่มต้น");
            labelMap.add("LABEL_END_TERM", "End Term", "ช่วงที่สิ้นสุด");
            labelMap.add("LABEL_EDIT", "Edit", "แก้ไข");
            labelMap.add("ALERT_AMOUNT_VALUE","Please, fill Amount","กรุณากรอกจำนวนเงิน");
            labelMap.add("DEPARTMENT_CODE","Department Code","รหัสแผนก");

            request.setAttribute("labelMap", labelMap.getHashMap());
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DBConn conData = new DBConn();
            conData.setStatement();
            
            byte MODE = DBMgr.MODE_INSERT;
            DataRecord periodRec = null, departmentRec= null, expenseRec=null, doctorRec=null;
            DataRecord stpDelete = null;
            String MM = "", YYYY = "";
            String select_type="", select_tax="";
            String pay_amount="", total_amount="", pay_tax_amount="", total_tax_amount="";
            String get_amount="", get_tax_amount="", get_pay_amount="", get_total_amount="", get_pay_tax_amount="", get_total_tax_amount="";
            String get_start_mm="", get_start_year="", get_end_mm="", get_end_year="";
            String db_get_start_mm="", db_get_start_year="", db_get_end_mm="", db_get_end_year="";
            String db_get_old_start_mm="", db_get_old_start_year="", db_get_old_end_mm="", db_get_old_end_year="";
            double db_get_amount=0, db_get_tax_amount=0, db_get_old_amount=0, db_get_old_tax_amount=0;
            String master_start_mm =JDate.getMonth();
			String master_start_year =JDate.getYear();    
            
            if (request.getParameter("MODE") != null) {
                // Insert or update
                MODE = Byte.parseByte(request.getParameter("MODE"));
				periodRec = new DataRecord("STP_PERIOD_EXPENSE");
                if (MODE == DBMgr.MODE_UPDATE){
                	if(Double.parseDouble(request.getParameter("AMOUNT"))!=Double.parseDouble(request.getParameter("OLD_AMOUNT"))){
	                	pay_amount=request.getParameter("OLD_PAY_AMOUNT");
	                	if(request.getParameter("OLD_SEL_TYPE").equals("2")){
	                		if(Double.parseDouble(request.getParameter("OLD_PAY_AMOUNT"))==0){
	                			total_amount=request.getParameter("AMOUNT");
	                		}else{
	                			total_amount=String.valueOf(Double.parseDouble(request.getParameter("AMOUNT"))-Double.parseDouble(request.getParameter("OLD_PAY_AMOUNT")));
	                		}
	                	}else{
	                		total_amount="0";
	                	}
		            }else{
	                	pay_amount=request.getParameter("OLD_PAY_AMOUNT");
	                	total_amount=request.getParameter("OLD_TOTAL_AMOUNT");
	                }
                	if(Double.parseDouble(request.getParameter("TAX_AMOUNT"))!=Double.parseDouble(request.getParameter("OLD_TAX_AMOUNT"))){
	                	pay_tax_amount=request.getParameter("OLD_PAY_TAX_AMOUNT");
	                	if(request.getParameter("OLD_SEL_TYPE").equals("2")){
	                		if(Double.parseDouble(request.getParameter("OLD_PAY_TAX_AMOUNT"))==0){
	                			total_tax_amount=request.getParameter("TAX_AMOUNT");
	                		}else{
	                			total_tax_amount=String.valueOf(Double.parseDouble(request.getParameter("TAX_AMOUNT"))-Double.parseDouble(request.getParameter("OLD_PAY_TAX_AMOUNT")));
	                		}
	                	}else{
	                		total_tax_amount="0";
	                	}
	                }else{
	                	pay_tax_amount=request.getParameter("OLD_PAY_TAX_AMOUNT");
	                	total_tax_amount=request.getParameter("OLD_TOTAL_TAX_AMOUNT");
	                }
                	/*
                	if(Variables.IS_TEST){
	                	System.out.println("doctor_code="+request.getParameter("OLD_DOCTOR_CODE"));
	                	System.out.println("expense_code="+request.getParameter("OLD_EXPENSE_CODE"));
	                	System.out.println("sel_type="+request.getParameter("OLD_SEL_TYPE"));
	                	System.out.println("amount="+request.getParameter("AMOUNT"));
	                	System.out.println("pay_amount="+pay_amount);
	                	System.out.println("total_amount="+total_amount);
	                	System.out.println("tax_type="+request.getParameter("TAX_TYPE"));
	                	System.out.println("start_mm="+request.getParameter("OLD_START_MM"));
	                	System.out.println("start_year="+request.getParameter("OLD_START_YYYY"));
	                	System.out.println("end_mm="+request.getParameter("END_TERM_MM"));
	                	System.out.println("end_year="+request.getParameter("END_TERM_YYYY"));
	                	System.out.println("note="+request.getParameter("NOTE"));
	                	System.out.println("active="+request.getParameter("ACTIVE"));
                	}
					*/
                }else{
                	System.out.println("insert");
                	pay_amount="0";
                	pay_tax_amount="0";
                	if(request.getParameter("SEL_TYPE").equals("2")){
                		if(!request.getParameter("AMOUNT").equals("") && !request.getParameter("AMOUNT").equals("0")){
                			total_amount=request.getParameter("AMOUNT");
                		}else{
                			total_amount="0";
                		}if(!request.getParameter("TAX_AMOUNT").equals("") && !request.getParameter("TAX_AMOUNT").equals("0")){
                			total_tax_amount=request.getParameter("TAX_AMOUNT");
                		}else{
                			total_tax_amount="0";
                		}
                		
                	}else{
                		total_amount="0";
                		total_tax_amount="0";
                	}
                	
                	if(true){
	                	System.out.println("doctor_code="+request.getParameter("OLD_DOCTOR_CODE"));
	                	System.out.println("department_code="+request.getParameter("DEPARTMENT_CODE"));
	                	System.out.println("expense_code="+request.getParameter("OLD_EXPENSE_CODE"));
	                	System.out.println("sel_type="+request.getParameter("OLD_SEL_TYPE"));
	                	System.out.println("amount="+request.getParameter("AMOUNT"));
	                	System.out.println("pay_amount="+pay_amount);
	                	System.out.println("total_amount="+total_amount);
	                	System.out.println("tax_type="+request.getParameter("TAX_TYPE"));
	                	System.out.println("start_mm="+request.getParameter("OLD_START_MM"));
	                	System.out.println("start_year="+request.getParameter("OLD_START_YYYY"));
	                	System.out.println("end_mm="+request.getParameter("END_TERM_MM"));
	                	System.out.println("end_year="+request.getParameter("END_TERM_YYYY"));
	                	System.out.println("note="+request.getParameter("NOTE"));
	                	System.out.println("active="+request.getParameter("ACTIVE"));
                	}
                }
               	if(MODE == DBMgr.MODE_INSERT){
	                periodRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
	                periodRec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CODE"), true);
	                periodRec.addField("EXPENSE_CODE", Types.VARCHAR, request.getParameter("EXPENSE_CODE"), true);
	                periodRec.addField("TYPE_EXPENSE", Types.VARCHAR, request.getParameter("SEL_TYPE"), true);
	                periodRec.addField("START_TERM_MM", Types.VARCHAR, request.getParameter("START_TERM_MM"), true);
	                periodRec.addField("START_TERM_YYYY", Types.VARCHAR, request.getParameter("START_TERM_YYYY"), true);
               	}else{
               	    periodRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
	                periodRec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("OLD_DOCTOR_CODE"), true);
	                periodRec.addField("EXPENSE_CODE", Types.VARCHAR, request.getParameter("OLD_EXPENSE_CODE"), true);
	                periodRec.addField("TYPE_EXPENSE", Types.VARCHAR, request.getParameter("OLD_SEL_TYPE"), true);
	                periodRec.addField("START_TERM_MM", Types.VARCHAR, request.getParameter("OLD_START_MM"), true);
	                periodRec.addField("START_TERM_YYYY", Types.VARCHAR, request.getParameter("OLD_START_YYYY"), true);
               	}
                periodRec.addField("AMOUNT", Types.NUMERIC, request.getParameter("AMOUNT"));  
                periodRec.addField("TAX_AMOUNT", Types.NUMERIC, request.getParameter("TAX_AMOUNT"));
                periodRec.addField("PAY_AMOUNT", Types.NUMERIC, pay_amount);
                periodRec.addField("TOTAL_AMOUNT", Types.NUMERIC, total_amount);
                periodRec.addField("PAY_TAX_AMOUNT", Types.NUMERIC, pay_tax_amount);
                periodRec.addField("TOTAL_TAX_AMOUNT", Types.NUMERIC, total_tax_amount);
                periodRec.addField("TAX_TYPE", Types.VARCHAR, request.getParameter("TAX_TYPE"));
                periodRec.addField("END_TERM_MM", Types.VARCHAR, request.getParameter("END_TERM_MM"));
                periodRec.addField("END_TERM_YYYY", Types.VARCHAR, request.getParameter("END_TERM_YYYY"));
                periodRec.addField("NOTE", Types.VARCHAR, request.getParameter("NOTE"));
                periodRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                periodRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                periodRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                periodRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                periodRec.addField("DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("DEPARTMENT_CODE"));

                if(1==1){
                	out.println(request.getParameter("DOCTOR_CODE"));
                	//return;
                }
                if (MODE == DBMgr.MODE_INSERT) {
                    if (DBMgr.insertRecord(periodRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/expense_period_main.jsp?DOCTOR_CODE=%1$s&SEL_TYPE=%2$S", periodRec.getField("DOCTOR_CODE").getValue(),periodRec.getField("SEL_TYPE").getValue())));
                    }else{
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(periodRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/expense_period_main.jsp?DOCTOR_CODE=%1$s&SEL_TYPE=%2$S", periodRec.getField("DOCTOR_CODE").getValue(),periodRec.getField("SEL_TYPE").getValue())));
                    }else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }
                response.sendRedirect("../message.jsp");
                return;
            }else if (request.getParameter("DOCTOR_CODE") != null && request.getParameter("EXPENSE_CODE") != null) {
                MODE = DBMgr.MODE_UPDATE;
                periodRec 			=DBMgr.getRecord(String.format("SELECT * FROM STP_PERIOD_EXPENSE WHERE DOCTOR_CODE = '%1$s' AND EXPENSE_CODE='%2$s' AND HOSPITAL_CODE='%3$s' AND ACTIVE='%4$s' AND START_TERM_YYYY='%5$s' AND START_TERM_MM='%6$s' ", request.getParameter("DOCTOR_CODE"),request.getParameter("EXPENSE_CODE"),session.getAttribute("HOSPITAL_CODE").toString(),request.getParameter("ACTIVE"),request.getParameter("START_TERM_YYYY"),request.getParameter("START_TERM_MM")));
                expenseRec 			=DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM EXPENSE WHERE CODE = '%1$s' AND HOSPITAL_CODE='%2$s'", request.getParameter("EXPENSE_CODE"), session.getAttribute("HOSPITAL_CODE").toString()));
                doctorRec 			=DBMgr.getRecord(String.format("SELECT CODE, NAME_%1$s, DEPARTMENT_CODE FROM DOCTOR WHERE CODE = '%2$s' AND HOSPITAL_CODE='%3$s'", labelMap.getFieldLangSuffix(), request.getParameter( "DOCTOR_CODE"), session.getAttribute("HOSPITAL_CODE").toString()));
		        departmentRec 		=DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE CODE = '%1$s' AND HOSPITAL_CODE='%2$s'", DBMgr.getRecordValue(periodRec, "DEPARTMENT_CODE"), session.getAttribute("HOSPITAL_CODE").toString()));

				get_amount			=DBMgr.getRecordValue(periodRec, "AMOUNT");
				get_pay_amount		=DBMgr.getRecordValue(periodRec, "PAY_AMOUNT");
				get_tax_amount		=DBMgr.getRecordValue(periodRec, "TAX_AMOUNT");
				get_pay_tax_amount	=DBMgr.getRecordValue(periodRec, "PAY_TAX_AMOUNT");
				get_total_tax_amount=DBMgr.getRecordValue(periodRec, "TOTAL_TAX_AMOUNT");
				get_start_mm		=DBMgr.getRecordValue(periodRec, "START_TERM_MM");
				get_start_year		=DBMgr.getRecordValue(periodRec, "START_TERM_YYYY");
				get_end_mm			=DBMgr.getRecordValue(periodRec, "END_TERM_MM");
				get_end_year		=DBMgr.getRecordValue(periodRec, "END_TERM_YYYY");				
            }else if (request.getParameter("DOCTOR_CODE") != null) {
                // New
                MODE = DBMgr.MODE_INSERT;
                //MM = request.getParameter("MM");
                //YYYY = request.getParameter("YYYY");
               
                get_start_mm = JDate.getMonth();
				get_start_year = JDate.getYear();
                doctorRec = DBMgr.getRecord(String.format("SELECT CODE, NAME_%1$s, DEPARTMENT_CODE FROM DOCTOR WHERE CODE = '%2$s' AND HOSPITAL_CODE='%3$s'", labelMap.getFieldLangSuffix(), request.getParameter( "DOCTOR_CODE"), session.getAttribute("HOSPITAL_CODE").toString()));
		        departmentRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE CODE = '%1$s' AND HOSPITAL_CODE='%2$s'", DBMgr.getRecordValue(doctorRec, "DEPARTMENT_CODE"), session.getAttribute("HOSPITAL_CODE").toString()));
				select_type = request.getParameter("SEL_TYPE");
			}else {
                response.sendRedirect("../message.jsp");
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">
             
             function DOCTOR_CODE_KeyPress(e) {
                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_CODE() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CODE);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CODE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "";
                        return;
                    }
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_"+"<%=labelMap.getFieldLangSuffix()%>");
                }
            }     
            function EXPENSE_CODE_KeyPress(e) {

                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.EXPENSE_CODE.blur();
                    return false;
                }else {
                    return true;
                }
            }

            function AJAX_Refresh_EXPENSE_CODE() {
                var target = "../../RetrieveData?TABLE=EXPENSE&COND=CODE='" + document.mainForm.EXPENSE_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_EXPENSE_CODE);
            }
            
            function AJAX_Handle_Refresh_EXPENSE_CODE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.EXPENSE_CODE.value = "";
                        document.mainForm.EXPENSE_NAME.value = "";
                        return;
                    }
                    document.mainForm.EXPENSE_NAME.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            function DEPARTMENT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DEPARTMENT_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            function AJAX_Refresh_DEPARTMENT() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.DEPARTMENT_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DEPARTMENT);
            }
            function AJAX_Handle_Refresh_DEPARTMENT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DEPARTMENT_CODE.value = "";
                        document.mainForm.DEPARTMENT_DESCRIPTION.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function AJAX_VerifyData() {
	            if(document.mainForm.MODE.value=="<%=DBMgr.MODE_INSERT%>"){
	                var target = "../../RetrieveData?TABLE=STP_PERIOD_EXPENSE&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'"
	                    + " AND DOCTOR_CODE='" + document.mainForm.DOCTOR_CODE.value + "'"
	                    + " AND EXPENSE_CODE='" + document.mainForm.EXPENSE_CODE.value + "'"
	                    + " AND START_TERM_MM='" + document.mainForm.START_TERM_MM.value + "'"
	                    + " AND START_TERM_YYYY='" + document.mainForm.START_TERM_YYYY.value + "'"
	                    + " AND TYPE_EXPENSE='" + document.mainForm.SEL_TYPE.value + "'"
	            }else{
	             	var target = "../../RetrieveData?TABLE=STP_PERIOD_EXPENSE&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'"
	                    + " AND DOCTOR_CODE='" + document.mainForm.OLD_DOCTOR_CODE.value + "'"
	                    + " AND EXPENSE_CODE='" + document.mainForm.OLD_EXPENSE_CODE.value + "'"
	                    + " AND START_TERM_MM='" + document.mainForm.OLD_START_MM.value + "'"
	                    + " AND START_TERM_YYYY='" + document.mainForm.OLD_START_YYYY.value + "'"
	                    + " AND TYPE_EXPENSE='" + document.mainForm.OLD_SEL_TYPE.value + "'"
	            }
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist1 = isXMLNodeExist(xmlDoc, "HOSPITAL_CODE");
                    var beExist2 = isXMLNodeExist(xmlDoc, "DOCOTR_CODE");
                    var beExist3 = isXMLNodeExist(xmlDoc, "EXPENSE_CODE");
					var beExist4 = isXMLNodeExist(xmlDoc, "TYPE_EXPENSE");
					var beExist5 = isXMLNodeExist(xmlDoc, "START_TERM_MM");					
					var beExist6 = isXMLNodeExist(xmlDoc, "START_TERM_YYYY");					
					if(document.mainForm.MODE.value=="<%=DBMgr.MODE_INSERT%>"){
                       if (beExist1 && beExist2 && beExist3 && beExist4 && beExist5 && beExist6) {
                          alert('This data is in the system ');
                       }else{
                          document.mainForm.submit();
                       }
                    }else{
                   	 //alert("update ok");
                   		document.mainForm.submit();
                   }
                }
            }
            function SAVE_Click() {
            		if(mainForm.END_TERM_MM.value=="")
                    {
                    	alert("Please, Select End Month Term");
                    	mainForm.END_TERM_MM.focus();
                    	return false;
                    }
                    if(mainForm.END_TERM_YYYY.value=="")
                    {
                    	alert("Please, Select End Year Term");
                    	mainForm.END_TERM_YYYY.focus();
                    	return false;
                    }
                    if(mainForm.TAX_TYPE.value=="")
                    	{
                    		alert("Please, Select TAX Type");
                    		mainForm.TAX_TYPE.focus();
                    		return false;
                    	}
                    	if(mainForm.AMOUNT.value=="")
                    	{
                    		alert("Please, fill Amount");
                    		mainForm.AMOUNT.focus();
                    		return false;
                    	}
                    	else if(mainForm.TAX_AMOUNT.value=="")
                    	{
                    		alert("Please, fill Tax Amount");
                    		mainForm.TAX_AMOUNT.focus();
                    		return false;
                    	}
                    	else if(parseFloat(mainForm.AMOUNT.value)<0)
                    	{
                    		alert("Please, fill Amount more than 0");
                    		mainForm.AMOUNT.focus();
                    		return false;
                    	}
                    	else if(parseFloat(mainForm.TAX_AMOUNT.value)<0)
                    	{
                    		alert("Please, fill Tax Amount more than 0");
                    		mainForm.TAX_AMOUNT.focus();
                    		return false;
                    	}
		            if(document.mainForm.MODE.value=="<%=DBMgr.MODE_INSERT%>")
		            {
                    	if(mainForm.DOCTOR_CODE.value=="")
                    	{
                    		alert("Please, Select Doctor Code");
                    		mainForm.TAX_TYPE.focus();
                    		return false;
                    	}
                    	if(mainForm.EXPENSE_CODE.value=="")
                    	{
                    		alert("Please, Select Expense Code");
                    		mainForm.TAX_TYPE.focus();
                    		return false;
                    	}
                    	if(mainForm.SEL_TYPE.value=="")
                    	{
                    		alert("Please, Select Type");
                    		mainForm.SEL_TYPE.focus();
                    		return false;
                    	}
                    	if(mainForm.START_TERM_MM.value=="")
                    	{
                    		alert("Please, Select Start Month Term");
                    		mainForm.START_TERM_MM.focus();
                    		return false;
                    	}
     	              	else if(mainForm.START_TERM_YYYY.value=="")
                    	{
                    		alert("Please, Select Start Year Term");
                    		mainForm.START_TERM_YYYY.focus();
                    		return false;
                    	}
                    	else if(parseFloat(mainForm.START_TERM_YYYY.value)==parseFloat(mainForm.END_TERM_YYYY.value))
                    	{
                    		if(parseFloat(mainForm.START_TERM_MM.value)> parseFloat(mainForm.END_TERM_MM.value))
                    		{
                    			alert("Start Month more than End Month");
                    			return false;
                    		}
                    	}
                    	else if(parseFloat(mainForm.START_TERM_YYYY.value)!= parseFloat(mainForm.MASTER_YYYY.value))
                    	{
                    		alert("Please, select start term year more than current year or start term year equals current year");
                    		return false;
                    	}
                    	else if(parseFloat(mainForm.START_TERM_YYYY.value)>parseFloat(mainForm.END_TERM_YYYY.value)){
                    		alert("Please, select end term year more than start term year or end term year equals start term year");
                    		return false;
                    	}
                    }else if(document.mainForm.MODE.value=="<%=DBMgr.MODE_UPDATE%>"){
                    	//alert("ok");
                    	//alert("old_start_term_yyyy="+mainForm.OLD_START_YYYY.value);
                    	if(parseFloat(mainForm.OLD_START_YYYY.value)==parseFloat(mainForm.END_TERM_YYYY.value)){
                    		if(parseFloat(mainForm.OLD_START_MM.value)> parseFloat(mainForm.END_TERM_MM.value)){
                    			alert("Please, select end term month more than start term month or end term month equals start term month");
                    			return false;
                    		}
                    	}else if(parseFloat(mainForm.OLD_START_YYYY.value)>parseFloat(mainForm.END_TERM_YYYY.value)){
                    		alert("Please, select end term year more than start term year or end term year equals start term year");
                    		return false;
                    	}
                    	if((parseFloat(mainForm.OLD_PAY_AMOUNT.value) !=0) && (parseFloat(mainForm.OLD_AMOUNT.value) != parseFloat(mainForm.AMOUNT.value)) && (parseFloat(mainForm.OLD_AMOUNT.value) > parseFloat(mainForm.AMOUNT.value))){
                    		alert("Please, fill amount more than old amount or amount equals old amount");
                    		return false;
                    	}
                    	if((parseFloat(mainForm.OLD_PAY_TAX_AMOUNT.value) !=0 ) && (parseFloat(mainForm.OLD_TAX_AMOUNT.value) != parseFloat(mainForm.TAX_AMOUNT.value)) && (parseFloat(mainForm.OLD_TAX_AMOUNT.value) > parseFloat(mainForm.TAX_AMOUNT.value))){
                    		//alert("pay_tax_amount="+mainForm.PAY_TAX_AMOUNT.value);
                    		alert("Please, fill tax amount more than old tax amount or tax amount equals old tax amount");
                    		return false;
                    	}
                    }
                   	if(true){
						AJAX_VerifyData();
					}
            }

            
           	function checkTax(obj) //เมื่อมีการเลือก tax_type_code 
            {
            //alert("OK");
            	//alert("obj="+obj.value);
            	if(mainForm.AMOUNT.value=="") //check amount value
            	{
            		alert("<%=labelMap.get("ALERT_AMOUNT_VALUE")%>");
            		//alert("กรุณาใส่ค่า amount");
            		mainForm.AMOUNT.focus();
            	}
            	
            }
                     //---- ให้คีย์ได้แต่ตัวเลข
	function IsNumericKeyPress()
	{
		if(event.keyCode ==46)
		{
			event.returnValue = true;
		}
		else if (((event.keyCode < 48) || (event.keyCode > 57)))
			{
				event.returnValue = false;
			}
	}
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
-->
        </style>
</head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="expense_period_detail.jsp">
           <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" ></input>
           <input type="hidden" id="OLD_AMOUNT" name="OLD_AMOUNT" value="<%=get_amount%>"></input>
           <input type="hidden" id="OLD_PAY_AMOUNT" name="OLD_PAY_AMOUNT" value="<%=get_pay_amount%>"></input>
           <input type="hidden" id="OLD_TOTAL_AMOUNT" name="OLD_TOTAL_AMOUNT" value="<%=get_total_amount%>"></input>
           <input type="hidden" id="OLD_TAX_AMOUNT" name="OLD_TAX_AMOUNT" value="<%=get_tax_amount%>"></input>
           <input type="hidden" id="OLD_PAY_TAX_AMOUNT" name="OLD_PAY_TAX_AMOUNT" value="<%=get_pay_tax_amount%>"></input>
           <input type="hidden" id="OLD_TOTAL_TAX_AMOUNT" name="OLD_TOTAL_TAX_AMOUNT" value="<%=get_total_tax_amount%>"></input>
           <input type="hidden" id="OLD_START_MM" name="OLD_START_MM" value="<%=get_start_mm%>"></input>
           <input type="hidden" id="OLD_START_YYYY" name="OLD_START_YYYY" value="<%=get_start_year%>"></input>
           <input type="hidden" id="OLD_END_MM" name="OLD_END_MM" value="<%=get_end_mm%>"></input>
           <input type="hidden" id="OLD_END_YYYY" name="OLD_END_YYYY" value="<%=get_end_year%>"></input>
           <input type="hidden" id="MASTER_MM" name="MASTER_MM" value="<%=batch_month%>"></input>
           <input type="hidden" id="MASTER_YYYY" name="MASTER_YYYY" value="<%=batch_year%>"></input>
           <input type="hidden" id="OLD_DOCTOR_CODE" name="OLD_DOCTOR_CODE" value="<%=DBMgr.getRecordValue(doctorRec, "CODE") %>"></input>
           <input type="hidden" id="OLD_EXPENSE_CODE" name="OLD_EXPENSE_CODE" value="<%=DBMgr.getRecordValue(expenseRec, "CODE") %>"></input>
           <input type="hidden" id="OLD_SEL_TYPE" name="OLD_SEL_TYPE" value="<%=DBMgr.getRecordValue(periodRec, "TYPE_EXPENSE") %>"></input>
            <table class="form">
                <tr><th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				</th></tr>
                <tr>
                  <td class="label"><label for="LABEL_DOCTOR_CODE"><span class="style1">${labelMap.LABEL_DOCTOR_CODE} *</span></label></td>
                  <td colspan="3" class="input">
                   <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_CODE();" <% if(MODE==DBMgr.MODE_UPDATE){ out.println("readonly");} %>/>
                   <%if(MODE==DBMgr.MODE_INSERT){%>   
                   <!-- <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorOnSearch(1); return false;" /> --><%} %>
                   <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_" + labelMap.getFieldLangSuffix()) %>" />                  </td>
                </tr>
                <tr>
                    <td class="label">
                        <label id="DEPARTMENT_LABEL" for="DEPARTMENT_CODE"><span class="style1">${labelMap.DEPARTMENT_CODE}</span></label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="DEPARTMENT_CODE" type="text" class="short" id="DEPARTMENT_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(periodRec, "DEPARTMENT_CODE").equals("") ? DBMgr.getRecordValue(doctorRec, "DEPARTMENT_CODE") : DBMgr.getRecordValue(periodRec, "DEPARTMENT_CODE") %>" onkeypress="return DEPARTMENT_CODE_KeyPress(event);" onblur="AJAX_Refresh_DEPARTMENT();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DEPARTMENT_CODE&HANDLE=AJAX_Refresh_DEPARTMENT'); return false;" />
                        <input type="text" id="DEPARTMENT_DESCRIPTION" name="DEPARTMENT_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(departmentRec, "DESCRIPTION") %>" />
                        
                     </td>
                </tr>
               
                <tr>
                  <td width="226" class="label"><label for="LABEL_EXPENSE_CODE"><span class="style1">${labelMap.LABEL_EXPENSE_CODE} *</span></label>                    </td>
                    <td colspan="3" class="input">
                        <input name="EXPENSE_CODE" type="text" class="short" id="EXPENSE_CODE" onblur="AJAX_Refresh_EXPENSE_CODE();" onkeypress="return EXPENSE_CODE_KeyPress(event);" value="<%= DBMgr.getRecordValue(expenseRec, "CODE") %>" maxlength="20" <%if(MODE==DBMgr.MODE_UPDATE){ out.println("readonly");} %>/>
                       <%if(MODE==DBMgr.MODE_INSERT){%> <input id="SEARCH_EXPENSE_CODE" name="SEARCH_EXPENSE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorOnSearch(2); return false;" /><%} %>
                        <input type="text" id="EXPENSE_NAME" name="EXPENSE_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(expenseRec, "DESCRIPTION") %>" />                    </td>
                </tr>
                <tr>
                  <td class="label"><label for="LABEL_TAX_TYPE"><span class="style1">${labelMap.LABEL_TAX_TYPE} *</span></label></td>
                  <td colspan="3" class="input">
                    <select class="short" id="TAX_TYPE" name="TAX_TYPE" onchange="checkTax(this);">
                            <option value="400" <%= ("400".equalsIgnoreCase(DBMgr.getRecordValue(periodRec, "TAX_TYPE"))? "selected" : "") %>>-- No Tax --</option>
                            <option value="401" <%= ("401".equalsIgnoreCase(DBMgr.getRecordValue(periodRec, "TAX_TYPE"))? "selected" : "") %>>40(1)</option>
                            <option value="402" <%= ("402".equalsIgnoreCase(DBMgr.getRecordValue(periodRec, "TAX_TYPE"))? "selected" : "") %>>40(2)</option>
                            <option value="406" <%= ("406".equalsIgnoreCase(DBMgr.getRecordValue(periodRec, "TAX_TYPE"))? "selected" : "") %>>40(6)</option>
                	</select>
                  </td>
                </tr>
                
                <tr>
                  <td class="label"><label for="LABEL_DOCTOR_CODE"><span class="style1">${labelMap.LABEL_TYPE_EXPENSE} *</span></label></td>
                    <td colspan="3" class="input">
                    <%if(MODE==DBMgr.MODE_UPDATE) {
                    	if("1".equalsIgnoreCase(DBMgr.getRecordValue(periodRec, "TYPE_EXPENSE"))){out.println("Fix Money");}
                    	else if("2".equalsIgnoreCase(DBMgr.getRecordValue(periodRec, "TYPE_EXPENSE"))){out.println("Fix Month");}
                    	} else { %>
                    <select name="SEL_TYPE" id="SEL_TYPE">
                      <option value='' <%= ("1".equalsIgnoreCase(DBMgr.getRecordValue(periodRec, "TYPE_EXPENSE")) || select_type.equals("") ? "selected" : "") %>>-- Select --</option>
                      <option value='1' <%= ("1".equalsIgnoreCase(DBMgr.getRecordValue(periodRec, "TYPE_EXPENSE")) || select_type.equals("1") ? "selected" : "") %>>Fix Money</option>
                      <option value='2' <%= ("2".equalsIgnoreCase(DBMgr.getRecordValue(periodRec, "TYPE_EXPENSE")) || select_type.equals("2") ? "selected" : "") %>>Fix Month</option>
                                        </select><%} %></td>
                </tr>
                <tr>
                  <td class="label"><label for="LABEL_START_TERM"><span class="style1">${labelMap.LABEL_START_TERM}  *</span></label></td>
                  <td colspan="3" class="input"><%if(MODE==DBMgr.MODE_INSERT) {%><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "START_TERM_MM",get_start_mm)%>
/ <%=proUtil.selectYY("START_TERM_YYYY", JDate.getYear())%>
                    <%} else { out.println(get_start_mm+"/"+get_start_year);} %></td>
                </tr>
                <tr>
                  <td class="label"><label for="LABEL_END_TERM"><span class="style1">${labelMap.LABEL_END_TERM} *</span></label></td>
                  <td colspan="3" class="input"><%=MODE==DBMgr.MODE_INSERT ?
                  proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "END_TERM_MM",JDate.getMonth())+"/"+proUtil.selectYY("END_TERM_YYYY", JDate.getYear()) : 
				  proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "END_TERM_MM",get_end_mm)+"/"+proUtil.selectYY("END_TERM_YYYY", get_end_year)%></td>
                </tr>
                <tr>
                  <td class="label"><label for="LABEL_AMOUNT"><span class="style1">${labelMap.LABEL_AMOUNT} *</span></label></td>
                  <td width="196" class="input"><input type="text" name="AMOUNT" value="<%=DBMgr.getRecordValue(periodRec, "AMOUNT")%>" onkeypress="IsNumericKeyPress();"/></td>
                  <td width="173" class="label"><label for="LABEL_TAX_AMOUNT"><span class="style1">${labelMap.LABEL_TAX_AMOUNT} *</span></label></td>
                  <td width="185" class="input"><input type="text" name="TAX_AMOUNT" value="<%=DBMgr.getRecordValue(periodRec, "TAX_AMOUNT")%>" onkeypress="IsNumericKeyPress();"/></td>
                </tr>
                <tr>
                  <td class="label"><label for="LABEL_PAY_AMOUNT">${labelMap.LABEL_PAY_AMOUNT} </label></td>
                  <td class="input"><input type="text" name="PAY_AMOUNT" value="<%=DBMgr.getRecordValue(periodRec, "PAY_AMOUNT")%>" readonly="readonly" /></td>
                  <td class="label"><label for="LABEL_PAY_TAX_AMOUNT">${labelMap.LABEL_PAY_TAX_AMOUNT} </label></td>
                  <td class="input"><input type="text" name="PAY_TAX_AMOUNT" value="<%=DBMgr.getRecordValue(periodRec, "PAY_TAX_AMOUNT")%>" readonly="readonly" /></td>
                </tr>
                <tr>
                  <td class="label"><label for="LABEL_TOTAL_AMOUN">${labelMap.LABEL_TOTAL_AMOUNT} </label></td>
                  <td class="input"><input type="text" name="TOTAL_AMOUNT" value="<%=DBMgr.getRecordValue(periodRec, "TOTAL_AMOUNT")%>" readonly="readonly"/></td>
                  <td class="label"><label for="LABEL_TOTAL_TAX_AMOUN">${labelMap.LABEL_TOTAL_TAX_AMOUNT} </label></td>
                  <td class="input"><input type="text" name="TOTAL_TAX_AMOUNT" value="<%=DBMgr.getRecordValue(periodRec, "TOTAL_TAX_AMOUNT")%>" readonly="readonly"/></td>
                </tr>
                <tr>
                  <td class="label"><label for="LABEL_NOTE">${labelMap.LABEL_NOTE} </label></td>
                  <td colspan="3" class="input"><textarea name="NOTE" cols="30" rows="3"><%=DBMgr.getRecordValue(periodRec, "NOTE")%></textarea></td>
                </tr>
                <tr>
               	  <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td> 
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(periodRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(periodRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(periodRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.history.back()" />                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>

<script language="javascript">
	function doctorOnSearch(id){
		if(id==1)//DOCTOR
		{
			var url='../search.jsp?TABLE=DOCTOR&&TARGET=DOCTOR_CODE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&HANDLE=AJAX_Refresh_DOCTOR_CODE';
			openSearchForm(url);
		}
		else if(id==2)//EXPENSE
		{
			openSearchForm('../search.jsp?TABLE=EXPENSE&DISPLAY_FIELD=DESCRIPTION&TARGET=EXPENSE_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_EXPENSE_CODE');	
		}
	}
</script>