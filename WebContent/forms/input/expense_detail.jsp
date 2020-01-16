<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="java.sql.Types"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="org.apache.log4j.Logger"%>

<%    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
	
	final Logger logger = Logger.getLogger("expense_detail.jsp");
    DataRecord trnDailyRec = null;
    String pkTBL = JDate.getYear() + "" + JDate.getMonth() + "" + JDate.getDay() + "" + JDate.getTime();

    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
    labelMap.add("TITLE_MAIN", "Adjust Revenue Details", "รายละเอียดรายการปรับปรุงรายได้");
    labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
    labelMap.add("MM", "Month", "เดือน");
    labelMap.add("YYYY", "Year", "ปี");
    labelMap.add("EXPENSE_CODE", "Adjust Code *", "รายการปรับปรุง *");
    labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร"    ); 
    labelMap.add("DOC_NO", "Document No.", "เลขที่เอกสาร");
    labelMap.add("DOC_DATE", "Document Date", "วันที่เอกสาร");
    labelMap.add("SUPPLIER_CODE", "Supplier Code", "รหัสคู่สัญญา");
    labelMap.add("TAX_TYPE_CODE", "Tax Type", "ประเภทภาษี");
    labelMap.add("COMPUTE_TAX_TYPE", "Tax Condition", "เงื่อนไขการคิดภาษี");
    labelMap.add("AMOUNT", "Amount", "จำนวนเงิน");
	labelMap.add("TAX_AMOUNT","Tax Amount","จำนวนเงินภาษี");
    labelMap.add("EXPENSE_ACCOUT_CODE", "Account Code *", "รหัสบันทึกบัญชี *");
    labelMap.add("NOTE", "Note", "หมายเหตุ");
    labelMap.add("DEPARTMENT_CODE", "Department Code *", "แผนก *");
    labelMap.add("LOCATION_CODE", "Location Code", "สถาน");
    labelMap.add("GUARANTEE_INCLUDE" ,  "Guarantee Include" , "นำไปเทียบการันตี" );
    labelMap.add("GUARANTEE_INCLUDE1" ,  "Yes" , "ใช่ " );
    labelMap.add("GUARANTEE_INCLUDE2" ,  "No" , "ไม่" );

    request.setAttribute("labelMap", labelMap.getHashMap());

    request.setCharacterEncoding("UTF-8");
    byte MODE = DBMgr.MODE_INSERT;
    DataRecord stpExpenseRec = null, doctorRec = null, expenseTypeRec = null, supplierTypeRec = null;
    DataRecord departmentRec= null, locationRec=null, summaryMounthlyRec = null;
    String MM = "", YYYY = "";

    if (request.getParameter("MODE") != null) {
        // Insert or update

        MODE = Byte.parseByte(request.getParameter("MODE"));

        stpExpenseRec = new DataRecord("TRN_EXPENSE_DETAIL");

        stpExpenseRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
        stpExpenseRec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CODE"), true);
        stpExpenseRec.addField("LINE_NO", Types.VARCHAR, request.getParameter("LINE_NO"), true);
        stpExpenseRec.addField("MM", Types.VARCHAR, request.getParameter("MM"), true);
        stpExpenseRec.addField("YYYY", Types.VARCHAR, request.getParameter("YYYY"), true);
        stpExpenseRec.addField("EXPENSE_CODE", Types.VARCHAR, request.getParameter("EXPENSE_CODE"), true);
        stpExpenseRec.addField("EXPENSE_SIGN", Types.NUMERIC, request.getParameter("EXPENSE_SIGN"));
        stpExpenseRec.addField("EXPENSE_ACCOUNT_CODE", Types.VARCHAR, request.getParameter("EXPENSE_ACCOUNT_CODE"));
        stpExpenseRec.addField("AMOUNT", Types.NUMERIC, request.getParameter("AMOUNT"));
		stpExpenseRec.addField("TAX_AMOUNT", Types.NUMERIC, request.getParameter("TAX_AMOUNT"));
        stpExpenseRec.addField("DOC_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("DOC_DATE")));
        stpExpenseRec.addField("SUPPLIER_CODE", Types.VARCHAR, request.getParameter("SUPPLIER_CODE"));
        stpExpenseRec.addField("INVOICE_TYPE_DESCRIPTION", Types.VARCHAR, request.getParameter("INVOICE_TYPE_DESCRIPTION"));
        stpExpenseRec.addField("DOC_NO", Types.VARCHAR, request.getParameter("DOC_NO"));
        stpExpenseRec.addField("NOTE", Types.VARCHAR, request.getParameter("NOTE"));
        stpExpenseRec.addField("TAX_TYPE_CODE", Types.VARCHAR, request.getParameter("TAX_TYPE_CODE"));
        stpExpenseRec.addField("ACTIVE", Types.INTEGER, "1");
        stpExpenseRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
        stpExpenseRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
        stpExpenseRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
        stpExpenseRec.addField("BATCH_NO", Types.VARCHAR, "");
        stpExpenseRec.addField("PAYMENT_DATE", Types.VARCHAR, "");
        stpExpenseRec.addField("PAYMENT_TERM", Types.VARCHAR, "");
        stpExpenseRec.addField("DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("DEPARTMENT_CODE").toString());
        stpExpenseRec.addField("LOCATION_CODE", Types.VARCHAR, request.getParameter("LOCATION_CODE").toString());

        //------
        if (MODE == DBMgr.MODE_INSERT) {

            if (DBMgr.insertRecord(stpExpenseRec)) {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/expense_main.jsp?EXPENSE_DR_CODE=%1$s&MM=%2$s&YYYY=%3$s", stpExpenseRec.getField("DOCTOR_CODE").getValue(), stpExpenseRec.getField("MM").getValue(), stpExpenseRec.getField("YYYY").getValue())));
            } else {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
            }
        } else if (MODE == DBMgr.MODE_UPDATE) {
            if (DBMgr.updateRecord(stpExpenseRec)) {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/expense_main.jsp?EXPENSE_DR_CODE=%1$s&MM=%2$s&YYYY=%3$s", stpExpenseRec.getField("DOCTOR_CODE").getValue(), stpExpenseRec.getField("MM").getValue(), stpExpenseRec.getField("YYYY").getValue())));
            } else {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
            }
        }

        response.sendRedirect("../message.jsp");
        return;
    }  else if (request.getParameter("DOCTOR_CODE") != null && request.getParameter("HOSPITAL_CODE") != null
            && request.getParameter("EXPENSE_CODE") != null && request.getParameter("MM") != null
            && request.getParameter("YYYY") != null ) {
        // Edit
        MODE = DBMgr.MODE_UPDATE;
        String query = String.format("SELECT * FROM TRN_EXPENSE_DETAIL WHERE HOSPITAL_CODE = '%1$s' " +
                "AND DOCTOR_CODE = '%2$s' " +
                "AND EXPENSE_CODE = '%3$s' " +
                "AND MM = '%4$s' " +
				"AND LINE_NO= '%5$s' " +
                "AND YYYY = '%6$s' "
                , session.getAttribute("HOSPITAL_CODE")
                , request.getParameter("DOCTOR_CODE")
                , request.getParameter("EXPENSE_CODE")
                , request.getParameter("MM")
                , request.getParameter("LINE_NO")
				, request.getParameter("YYYY"));
		//System.out.println("Update Mode : "+query);
        stpExpenseRec = DBMgr.getRecord(query);

        doctorRec = DBMgr.getRecord(String.format("SELECT CODE, NAME_%1$s AS NAME, DEPARTMENT_CODE FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '%2$s'", labelMap.getFieldLangSuffix(), DBMgr.getRecordValue(stpExpenseRec, "DOCTOR_CODE")));

        MM = DBMgr.getRecordValue(stpExpenseRec, "MM");
        YYYY = DBMgr.getRecordValue(stpExpenseRec, "YYYY");

        expenseTypeRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION, TAX_TYPE_CODE FROM EXPENSE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '%1$s'", DBMgr.getRecordValue(stpExpenseRec, "EXPENSE_CODE")));

        supplierTypeRec = DBMgr.getRecord(String.format("SELECT CODE, NAME_THAI FROM PAYOR_OFFICE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '%1$s'", DBMgr.getRecordValue(stpExpenseRec, "SUPPLIER_CODE")));

        query = "SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(stpExpenseRec, "DEPARTMENT_CODE") + "'";
        departmentRec = DBMgr.getRecord(query);

        query = "SELECT CODE, DESCRIPTION FROM LOCATION WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(stpExpenseRec, "LOCATION_CODE") + "'";
        locationRec = DBMgr.getRecord(query);
        
        //check summary monthly
        
        query = "SELECT MM FROM SUMMARY_PAYMENT WHERE HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString() +"' AND YYYY='"+ YYYY +"' AND MM='"+ MM +"' "+
        "AND (BATCH_NO = '' AND HOSPITAL_CODE IN (SELECT HOSPITAL_CODE FROM BATCH WHERE HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString() +"' AND CLOSE_DATE = ''))"+
        "GROUP BY MM ";
        
        //query = "SELECT MM FROM SUMMARY_PAYMENT WHERE HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString() +"' AND YYYY='"+ YYYY +"' AND MM='"+ MM +"' GROUP BY MM ";
        summaryMounthlyRec = DBMgr.getRecord(query);
        logger.info(query);
    
    } else if (request.getParameter("DOCTOR_CODE") != null && request.getParameter("MM") != null && request.getParameter("YYYY") != null) {
        // New
        MODE = DBMgr.MODE_INSERT;
        MM = request.getParameter("MM");
        YYYY = request.getParameter("YYYY");
        //doctorRec = DBMgr.getRecord(String.format("SELECT CODE, NAME_%1$s AS NAME, DEPARTMENT_CODE FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '%2$s'", labelMap.getFieldLangSuffix(), DBMgr.getRecordValue(stpExpenseRec, "DOCTOR_CODE")));
        doctorRec = DBMgr.getRecord(String.format("SELECT CODE, NAME_%1$s AS NAME, DEPARTMENT_CODE FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '%2$s'", labelMap.getFieldLangSuffix(), request.getParameter("DOCTOR_CODE")));       
    
    } else {
        response.sendRedirect("../message.jsp");
    }

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">
        
            function EXPENSE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.EXPENSE_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_EXPENSE() {
                var target = "../../RetrieveData?TABLE=EXPENSE&COND=CODE='" + document.mainForm.EXPENSE_CODE.value +"'"
                + " AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                //var target = "../../RetrieveData?TABLE=EXPENSE&COND=CODE='" + document.mainForm.EXPENSE_CODE.value +"'";
                AJAX_Request(target, AJAX_Handle_Refresh_EXPENSE);
            }

            function AJAX_Handle_Refresh_EXPENSE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.EXPENSE_CODE.value = "";
                        document.mainForm.EXPENSE_DESCRIPTION.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.EXPENSE_CODE.value = getXMLNodeValue(xmlDoc, "CODE");
                    document.mainForm.EXPENSE_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                    document.mainForm.EXPENSE_ACCOUNT_CODE.value = getXMLNodeValue(xmlDoc, "ACCOUNT_CODE");
                    document.mainForm.EXPENSE_SIGN.value = getXMLNodeValue(xmlDoc, "SIGN");
                    document.mainForm.TAX_TYPE_CODE.value = getXMLNodeValue(xmlDoc, "TAX_TYPE_CODE");
                    document.mainForm.AMOUNT.value = getXMLNodeValue(xmlDoc, "SIGN");
                }
            }

            function SUPPLIER_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.SUPPLIER_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_SUPPLIER() {
                var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.SUPPLIER_CODE.value +"'";
                AJAX_Request(target, AJAX_Handle_Refresh_SUPPLIER);
            }

            function AJAX_Handle_Refresh_SUPPLIER() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.SUPPLIER_CODE.value = "";
                        document.mainForm.SUPPLIER_DESCRIPTION.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.SUPPLIER_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "NAME_THAI");
                }
            }

            //---------------------
            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=TRN_EXPENSE_DETAIL&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'"
                    + " AND DOCTOR_CODE='" + document.mainForm.DOCTOR_CODE.value + "'"
                    + " AND LINE_NO='" + document.mainForm.LINE_NO.value + "'"
                    + " AND EXPENSE_CODE='" + document.mainForm.EXPENSE_CODE.value + "'"
                    + " AND MM='" + document.mainForm.MM.value + "'"
                    + " AND YYYY='" + document.mainForm.YYYY.value + "'";
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }

            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "HOSPITAL_CODE");
                    switch (document.mainForm.MODE.value) {
                    case "<%=DBMgr.MODE_INSERT%>" :
                            var e = document.mainForm.LINE_NO.value = "<%=pkTBL%>";
                            if (beExist) {
                                //alert("insert true");
                                if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                                    document.mainForm.MODE.value= "<%=DBMgr.MODE_UPDATE%>";
                                    document.mainForm.submit();
                                }
                            }else {
                                //alert("insert false : " + e);
                                document.mainForm.submit();
                            }
                        break;
                    case "<%=DBMgr.MODE_UPDATE%>" :
                        if (beExist) {
                            //alert("update true");
                            document.mainForm.submit();
                        }
                        else {
                            //alert("update false");
                            if (confirm("<%=labelMap.get(LabelMap.CONFIRM_ADD_NEW_DATA)%>")) {
                                alert("insert false - true");
                                document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
                                document.mainForm.submit();
                            }
                        }
                        break;
                    }
                }
            }


            function validateData(){
                var e = document.mainForm;
                if(!isObjectEmptyString(e.DOCTOR_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')
                    && !isObjectEmptyString(e.MM, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')
                    && !isObjectEmptyString(e.YYYY, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')
                    && !isObjectEmptyString(e.EXPENSE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')
                    ){
                        AJAX_VerifyData();
                }
            }

            /*****************************************************************************
             *
             *
             ****************************************************************************/
            function LOCATION_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.LOCATION_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_LOCATION() {
                var target = "../../RetrieveData?TABLE=LOCATION&COND=CODE='" + document.mainForm.LOCATION_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_LOCATION);
            }

            function AJAX_Handle_Refresh_LOCATION() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.LOCATION_CODE.value = "";
                        document.mainForm.LOCATION_DESCRIPTION.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.LOCATION_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            /*****************************************************************************
             *
             *
             ****************************************************************************/
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
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.DEPARTMENT_CODE.value + "'";
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
        </script>
        
<style type="text/css">
	<!--
	.style1 {color: #003399}
	.style2 {color: #333}
	-->
</style>
        
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="expense_detail.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <input type="hidden" id="LINE_NO" name="LINE_NO" value="<%= DBMgr.getRecordValue(stpExpenseRec, "LINE_NO") %>" />
            <input type="hidden" id="EXPENSE_SIGN" name="EXPENSE_SIGN" value="<%= DBMgr.getRecordValue(stpExpenseRec, "EXPENSE_SIGN") %>" />
            <table class="form">
                <tr><th colspan="4">${labelMap.TITLE_MAIN}</th></tr>
                <tr>
                    <td class="label">
                        <label>${labelMap.MM} / ${labelMap.YYYY}</label>                    
                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="MM" name="MM" class="veryShort" value="<%=MM%>" readonly/>
                        &nbsp;/&nbsp;
                        <input type="text" id="YYYY" name="YYYY"  value="<%=YYYY%>" class="veryShort" readonly/>                    
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" />
                        <input type="text" id="DOCTOR_CODE_NAME" name="DOCTOR_CODE_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME") %>" />
					</td>
                </tr>
				<tr>
                    <td class="label">
                        <label for="aText">${labelMap.SUPPLIER_CODE}</label>                    
                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="SUPPLIER_CODE" name="SUPPLIER_CODE" class="short" value="<%= DBMgr.getRecordValue(stpExpenseRec, "SUPPLIER_CODE") %>" onkeypress="return SUPPLIER_CODE_KeyPress(event);" onblur="AJAX_Refresh_SUPPLIER();" />
                        <input id="SEARCH_SUPPLIER_CODE" name="SEARCH_SUPPLIER_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_THAI&TARGET=SUPPLIER_CODE&BEACTIVE=1&HANDLE=AJAX_Refresh_SUPPLIER'); return false;" />
                        <input type="text" id="SUPPLIER_DESCRIPTION" name="SUPPLIER_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(supplierTypeRec, "NAME_THAI") %>" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="EXPENSE_CODE"><span class="style1">${labelMap.EXPENSE_CODE}</span></label></td>
                    <td class="input" colspan="3">
                        <input type="text" id="EXPENSE_CODE" name="EXPENSE_CODE" class="short" value="<%= DBMgr.getRecordValue(stpExpenseRec, "EXPENSE_CODE") %>" onkeypress="return EXPENSE_CODE_KeyPress(event);" onblur="AJAX_Refresh_EXPENSE();" />
                        <input id="SEARCH_EXPENSE_CODE" name="SEARCH_EXPENSE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=EXPENSE&DISPLAY_FIELD=DESCRIPTION&TARGET=EXPENSE_CODE&BEACTIVE=1&HANDLE=AJAX_Refresh_EXPENSE'); return false;" />
                        <input type="text" id="EXPENSE_DESCRIPTION" name="EXPENSE_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(expenseTypeRec, "DESCRIPTION") %>" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="DOCUMENT_TYPE">${labelMap.DOCUMENT_TYPE}</label>                    
                    </td>
                    <td class="input">
                        <select class="short" name="INVOICE_TYPE_DESCRIPTION">
                            <option value="Invoice" <%= (DBMgr.getRecordValue(stpExpenseRec, "DOC_NO")!="Invoice" ? "selected" : "") %>>Invoice</option>
                            <option value="Memo" <%= (DBMgr.getRecordValue(stpExpenseRec, "DOC_NO")!="Memo" ? "selected" : "") %>>Memo</option>
                        </select>				
                    </td>
					<td class="label">
                        <label for="EXPENSE_ACCOUT_CODE"><span class="style1">${labelMap.EXPENSE_ACCOUT_CODE}</span></label>                    
                    </td>
                    <td class="input" >
						<input type="text" id="EXPENSE_ACCOUNT_CODE" name="EXPENSE_ACCOUNT_CODE" value="<%= DBMgr.getRecordValue(stpExpenseRec, "EXPENSE_ACCOUNT_CODE").equalsIgnoreCase("") ? "" : DBMgr.getRecordValue(stpExpenseRec, "EXPENSE_ACCOUNT_CODE") %>" class="short" readonly=readonly/>                    
				    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="aText">${labelMap.DOC_NO}</label>                    
                    </td>
                    <td class="input">
                    	<input type="text" id="DOC_NO" name="DOC_NO" class="medium" value="<%= DBMgr.getRecordValue(stpExpenseRec, "DOC_NO") %>" />
                    </td>
                    <td class="label">
                        <label for="DOC_DATE">${labelMap.DOC_DATE}</label>                    
                    </td>
                    <td class="input">
                        <input type="text" id="DOC_DATE" name="DOC_DATE" class="short" value="<%= JDate.showDate(DBMgr.getRecordValue(stpExpenseRec, "DOC_DATE")) %>" onblur="CheckDate(this.value,'DOC_DATE','')" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('DOC_DATE'); return false;" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label id="DEPARTMENT_LABEL" for="DEPARTMENT_CODE"><span class="style1">${labelMap.DEPARTMENT_CODE}</span></label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="DEPARTMENT_CODE" type="text" class="short" id="DEPARTMENT_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(stpExpenseRec, "DEPARTMENT_CODE").equals("") ? DBMgr.getRecordValue(doctorRec, "DEPARTMENT_CODE") : DBMgr.getRecordValue(stpExpenseRec, "DEPARTMENT_CODE") %>" onkeypress="return DEPARTMENT_CODE_KeyPress(event);" onblur="AJAX_Refresh_DEPARTMENT();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DEPARTMENT_CODE&HANDLE=AJAX_Refresh_DEPARTMENT'); return false;" />
                        <input name="DEPARTMENT_DESCRIPTION" type="text" class="mediumMax" id="DEPARTMENT_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(departmentRec, "DESCRIPTION")%>" maxlength="255" />                    
                     </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="LOCATION_CODE">${labelMap.LOCATION_CODE}</label>                    
                    </td>
                    <td class="input" colspan="3">
                        <input name="LOCATION_CODE" type="text" class="short" id="LOCATION_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(stpExpenseRec, "LOCATION_CODE")%>" onkeypress="return LOCATION_CODE_KeyPress(event);" onblur="AJAX_Refresh_LOCATION();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=LOCATION&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=LOCATION_CODE&HANDLE=AJAX_Refresh_LOCATION'); return false;" />
                        <input name="LOCATION_DESCRIPTION" type="text" class="mediumMax" id="LOCATION_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(locationRec, "DESCRIPTION")%>" maxlength="255" />                    
                     </td>
                </tr>
                <tr>
					<td class="label">
                        <label for="AMOUNT">${labelMap.AMOUNT}</label>
					</td>
                    <td class="input"  colspan="3">
                        <input type="text" id="AMOUNT" name="AMOUNT" class="short" value="<%= DBMgr.getRecordValue(stpExpenseRec, "AMOUNT") %>" />
                        <label for="AMOUNT_SIGN"><%= DBMgr.getRecordValue(stpExpenseRec, "EXPENSE_SIGN").equals("1") ? " Add" : DBMgr.getRecordValue(stpExpenseRec, "EXPENSE_SIGN").equals("-1") ? " Deduct" :"" %></label>
					</td>
                </tr>                
                <tr>
					<td class="label">
                        <label for="TAX_AMOUNT">${labelMap.TAX_AMOUNT}</label>
					</td>
                    <td class="input">
						<input type="text" id="TAX_AMOUNT" name="TAX_AMOUNT" value="<%= DBMgr.getRecordValue(stpExpenseRec, "TAX_AMOUNT") %>" class="short"/>
					</td>
                    <td class="label">
                        <label for="aText">${labelMap.TAX_TYPE_CODE}</label>
                    </td>
                    <td class="input">
                        <select class="short" id="TAX_TYPE_CODE" name="TAX_TYPE_CODE">
                            <option value="400" <%= ("400".equalsIgnoreCase(DBMgr.getRecordValue(stpExpenseRec, "TAX_TYPE_CODE")) ? "selected" : "") %>>-- No Tax --</option>
                            <option value="401" <%= ("401".equalsIgnoreCase(DBMgr.getRecordValue(stpExpenseRec, "TAX_TYPE_CODE")) ? "selected" : "") %>>40(1)</option>
                            <option value="402" <%= ("402".equalsIgnoreCase(DBMgr.getRecordValue(stpExpenseRec, "TAX_TYPE_CODE")) ? "selected" : "") %>>40(2)</option>
                            <option value="406" <%= ("406".equalsIgnoreCase(DBMgr.getRecordValue(stpExpenseRec, "TAX_TYPE_CODE")) ? "selected" : "") %>>40(6)</option>
                        </select>
					</td>
				</tr>
                <tr>
                    <td class="label">
                        <label for="NOTE">${labelMap.NOTE}</label>                    
                    </td>
                    <td class="input" colspan="3">
                        <textarea id="NOTE" name="NOTE" class="long" rows="5"><%= DBMgr.getRecordValue(stpExpenseRec, "NOTE") %></textarea>                    
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE"
					class="button" value="${labelMap.SAVE}"
					<%=!DBMgr.getRecordValue(stpExpenseRec, "HOSPITAL_CODE").equals("00001") && (!DBMgr.getRecordValue(stpExpenseRec, "BATCH_NO").equals("") || !DBMgr.getRecordValue(summaryMounthlyRec, "MM").equals(""))  ? " disabled=\"disabled\"" : ""%>
					onclick="validateData();" /> <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location.href='expense_main.jsp?EXPENSE_DR_CODE=<%=request.getParameter("DOCTOR_CODE")%>&MM=<%=MM%>&YYYY=<%=YYYY%>'" />                    
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>