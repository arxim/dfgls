<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.*" %>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="df.bean.db.table.Batch"%>

<%@ include file="../../_global.jsp" %>

<%
//
// Verify permission
//

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_RECEIPT)) {
                response.sendRedirect("../message.jsp");
                return;
            }

//
// Initial LabelMap
//

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());

            labelMap.add("TITLE_MAIN", "Transaction", "ใบแจ้งหนี้/ใบเสร็จรับเงิน");
            labelMap.add("INVOICE_NO", "Invoice No.", "เลขที่ใบแจ้งหนี้");
            labelMap.add("INVOICE_DATE", "Invoice Date", "วันที่ใบแจ้งหนี้");
            labelMap.add("RECEIPT_NO", "Receive No.", "เลขที่ใบเสร็จ");
            labelMap.add("RECEIPT_DATE", "Receive Date", "วันที่ใบเสร็จ");
            //labelMap.add("HN_NO", "HN_NO", "เลขที่ผู้ป่วย");
            //labelMap.add("PATIENT_NAME", "Patient Name", "ชื่อผู้ป่วย");
            labelMap.add("EPISODE_NO", "Episode No", "ลำดับผู้ป่วย");
            labelMap.add("PAYOR_OFFICE_CODE", "Payor Office", "คู่สัญญา");
            labelMap.add("PAYOR_OFFICE_CATEGORY_CODE", "Payor Office Category", "กลุ่มคู่สัญญา");
            labelMap.add("IS_WRITE_OFF", "Write Off", "หักหนี้สูญ");
            labelMap.add("IS_WRITE_OFF_Y", "Yes", "ใช่");
            labelMap.add("IS_WRITE_OFF_N", "No", "ไม่ใช่");
            labelMap.add("TRANSACTION_TYPE", "Transaction Type", "ประเภทรายการ");
            //labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "ประเภทการรับผู้ป่วย");
            labelMap.add("PATIENT_DEPARTMENT_CODE", "Patient Department Code", "แผนกที่รักษาผู้ป่วย");
            labelMap.add("PATIENT_LOCATION_CODE", "Patient Location Code", "สถานที่รักษาผู้ป่วย");
            labelMap.add("RECEIPT_DEPARTMENT_CODE", "Receipt Department Code", "แผนกรับชำระเงิน");
            labelMap.add("RECEIPT_LOCATION_CODE", "Receipt Location Code", "สถานที่รับชำระเงิน");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รายการรักษา");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("RECEIPT_TYPE_CODE", "Receipt Type Code", "ประเภทการรับชำระเงิน");
            labelMap.add("TOTAL_BILL_AMOUNT", "Total Bill Amount", "จำนวนเงิน");

            labelMap.add("TITLE_DETAIL", "Transaction Details", "รายละเอียดใบแจ้งหนี้/ใบเสร็จรับเงิน");
            labelMap.add("LINE_NO", "Line No.", "บรรทัดที่");
            labelMap.add("VERIFY_DATE", "Verify Date", "วันที่อ่านผล");
            labelMap.add("DOCTOR_DEPARTMENT_CODE", "Doctor Department Code", "รหัสแผนกแพทย์");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รหัสการรักษา");
            labelMap.add("VERIFY_DATE", "Verify Date", "วันที่อ่านผล");
            labelMap.add("VERIFY_TIME", "Verify Time", "เวลาที่อ่านผล");
            labelMap.add("DOCTOR_EXECUTE_CODE", "Doctor Execute Code", "แพทย์รักษา");
            labelMap.add("EXECUTE_DATE", "Execute Date", "วันที่รักษา");
            labelMap.add("EXECUTE_TIME", "Execute Time", "เวลาที่รักษา");
            labelMap.add("DOCTOR_RESULT_CODE", "Doctor Result Code", "แพทย์อ่านผล");
            labelMap.add("PAY_BY", "Pay by", "ชำระเงินด้วย");
            labelMap.add("PAY_BY_CASH", "Cash", "เงินสด");
            labelMap.add("PAY_BY_AR", "AR", "รับชำระหนี้");
            labelMap.add("PAY_BY_DOCTOR", "Doctor", "เงื่อนไขแพทย์");
            labelMap.add("PAY_BY_PAYOR", "Payor", "เงื่อนไขคู่สัญญา");
            labelMap.add("PAY_BY_CASH_AR", "Cash AR", "รับชำระในเดือน");

            labelMap.add("CANCEL_INVOICE","Cancel Invoice","ยกเลิก Invoice");
            labelMap.add("CANCEL_0","Yes","Yes");
            labelMap.add("CANCEL_1","No","No");

            labelMap.add("AMOUNT_BEF_DISCOUNT", "Amount Before Discount", "Amount Before Discount");
            labelMap.add("AMOUNT_OF_DISCOUNT", "Amount Of Discount", "Amount Of Discount");
            
            labelMap.add("TITLE_DATA", "Transaction Details", "รายละเอียดใบแจ้งหนี้/ใบเสร็จรับเงิน");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รายการรักษา");
            labelMap.add("DR_AMT", "Doctor Amount", "ค่าแพทย์");
            labelMap.add("AMOUNT_AFT_DISCOUNT", "Amount After Disc.", "จำนวนเงิน");
            labelMap.add("ALERT_INVALID_INVOICE_DATE", "Invalid invoice date (this date must be after the last batch date)", "วันที่อินวอยซ์ไม่ถูกต้อง (วันที่นี้จะต้องมีค่าหลังจากวันที่ปิดยอดครั้งล่าสุด)");

            request.setAttribute("labelMap", labelMap.getHashMap());

//
// Process request
//
            
            final byte MODE_QUERY = 0;
            final byte MODE_QUERY_SUBMIT = 1;
            final byte MODE_INSERT_MASTER_DETAIL = 2;
            final byte MODE_INSERT_MASTER_DETAIL_SUBMIT = 3;
            final byte MODE_UPDATE_MASTER = 4;
            final byte MODE_UPDATE_MASTER_SUBMIT = 5;
            final byte MODE_INSERT_DETAIL = 6;
            final byte MODE_INSERT_DETAIL_SUBMIT = 7;
            final byte MODE_UPDATE_DETAIL = 8;
            final byte MODE_UPDATE_DETAIL_SUBMIT = 9;

            DBConnection con;
            ResultSet rs;
            
            con = new DBConnection();
            con.connectToLocal();
            //con.connectToServer();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
            con.Close();
            //con.freeConnection();
            String BATCH_DATE = b.getYyyy() + b.getMm() + "00";

            request.setCharacterEncoding("UTF-8");
            byte MODE = MODE_QUERY;
            String query = "";
            
            DataRecord trnDailyRec = null, payorOfficeRec = null, payorOfficeCategoryRec = null, patientDepartmentRec = null, patientLocationRec = null, receiptDepartmentRec = null, receiptLocationRec = null, receiptTypeRec = null;
            DataRecord doctorDepartmentRec = null, orderItemRec = null, doctorRec = null, doctorExecuteRec = null, doctorResultRec = null;
            if (request.getParameter("INVOICE_NO") != null && request.getParameter("LINE_NO") != null) {
                query = "SELECT * FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND INVOICE_NO = '" + request.getParameter("INVOICE_NO") + "'" +
                " AND LINE_NO = '" + DBMgr.toSQLString( request.getParameter("LINE_NO")) + "'";
                trnDailyRec = DBMgr.getRecord(query);
                MODE = MODE_UPDATE_DETAIL;
            }
            else if (request.getParameter("INVOICE_NO") != null) {
                query = "SELECT * FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND INVOICE_NO = '" + request.getParameter("INVOICE_NO") + "'";
                trnDailyRec = DBMgr.getRecord(query);
                MODE = MODE_UPDATE_MASTER;
            }
           if (request.getParameter("MODE") != null) {
                MODE = Byte.parseByte(request.getParameter("MODE"));
                if (MODE == MODE_UPDATE_MASTER) {
                    trnDailyRec = new DataRecord("TRN_DAILY");

                    trnDailyRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                    trnDailyRec.addField("INVOICE_NO", Types.VARCHAR, request.getParameter("INVOICE_NO"), true);
                    trnDailyRec.addField("INVOICE_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("INVOICE_DATE")), true);
                    trnDailyRec.addField("TRANSACTION_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("INVOICE_DATE")), true);
                    trnDailyRec.addField("TRANSACTION_MODULE", Types.VARCHAR, "TR");
                    trnDailyRec.addField("INVOICE_TYPE",Types.VARCHAR, "EXECUTE");
                    trnDailyRec.addField("LINE_NO", Types.VARCHAR, request.getParameter("LINE_NO"), true);
                    if(request.getParameter("EXECUTE_TIME").length()<2){
                       trnDailyRec.addField("EXECUTE_TIME", Types.VARCHAR, "000000");
                       trnDailyRec.addField("VERIFY_TIME", Types.VARCHAR, "000000");                            
                    }else{
                       trnDailyRec.addField("EXECUTE_TIME", Types.VARCHAR, JDate.saveTime(request.getParameter("EXECUTE_TIME")));
                       trnDailyRec.addField("VERIFY_TIME", Types.VARCHAR, JDate.saveTime(request.getParameter("EXECUTE_TIME")));                                                        
                    }
                    trnDailyRec.addField("PAY_BY_CASH", Types.VARCHAR, request.getParameter("PAY_BY_CASH") == null ? "N" : "Y");
                    trnDailyRec.addField("EXECUTE_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("EXECUTE_DATE")));
                    trnDailyRec.addField("VERIFY_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("EXECUTE_DATE")));
                    
                    trnDailyRec.addField("RECEIPT_NO", Types.VARCHAR, request.getParameter("RECEIPT_NO"));
                    trnDailyRec.addField("RECEIPT_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("RECEIPT_DATE")));
                    trnDailyRec.addField("HN_NO", Types.VARCHAR, request.getParameter("HN_NO"));
                    trnDailyRec.addField("PATIENT_NAME", Types.VARCHAR, request.getParameter("PATIENT_NAME"));
                    trnDailyRec.addField("EPISODE_NO", Types.VARCHAR, request.getParameter("EPISODE_NO"));
                    trnDailyRec.addField("PAYOR_OFFICE_CODE", Types.VARCHAR, request.getParameter("PAYOR_OFFICE_CODE"));
                    trnDailyRec.addField("PAYOR_OFFICE_NAME", Types.VARCHAR, request.getParameter("PAYOR_OFFICE_NAME"));//*
                    trnDailyRec.addField("PAYOR_OFFICE_CATEGORY_CODE", Types.VARCHAR, request.getParameter("PAYOR_OFFICE_CATEGORY_CODE"));
                    trnDailyRec.addField("IS_WRITE_OFF", Types.VARCHAR, request.getParameter("IS_WRITE_OFF"));//*
                    trnDailyRec.addField("TRANSACTION_TYPE", Types.VARCHAR, request.getParameter("TRANSACTION_TYPE"));//*
                    trnDailyRec.addField("ADMISSION_TYPE_CODE", Types.VARCHAR, request.getParameter("ADMISSION_TYPE_CODE"));//*
                    trnDailyRec.addField("PATIENT_DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("PATIENT_DEPARTMENT_CODE"));
                    trnDailyRec.addField("PATIENT_LOCATION_CODE", Types.VARCHAR, request.getParameter("PATIENT_LOCATION_CODE"));
                    trnDailyRec.addField("RECEIPT_DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("RECEIPT_DEPARTMENT_CODE"));
                    trnDailyRec.addField("RECEIPT_LOCATION_CODE", Types.VARCHAR, request.getParameter("RECEIPT_LOCATION_CODE"));
                    trnDailyRec.addField("DOCTOR_CODE", Types.VARCHAR, (request.getParameter("DOCTOR_RESULT_CODE") == null || request.getParameter("DOCTOR_RESULT_CODE") == "" ? request.getParameter("DOCTOR_EXECUTE_CODE") : request.getParameter("DOCTOR_RESULT_CODE")));
                    trnDailyRec.addField("TOTAL_BILL_AMOUNT", Types.NUMERIC, request.getParameter("TOTAL_BILL_AMOUNT"));
                    
                    trnDailyRec.addField("DOCTOR_DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("DOCTOR_DEPARTMENT_CODE"));
                    trnDailyRec.addField("ORDER_ITEM_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CODE"));
                    trnDailyRec.addField("ORDER_ITEM_DESCRIPTION", Types.VARCHAR, request.getParameter("ORDER_ITEM_DESCRIPTION"));

                    trnDailyRec.addField("DOCTOR_EXECUTE_CODE", Types.VARCHAR, request.getParameter("DOCTOR_EXECUTE_CODE"));
                    trnDailyRec.addField("DOCTOR_RESULT_CODE", Types.VARCHAR, request.getParameter("DOCTOR_RESULT_CODE"));//*
                    trnDailyRec.addField("AMOUNT_AFT_DISCOUNT", Types.NUMERIC, request.getParameter("AMOUNT_AFT_DISCOUNT"));
                    trnDailyRec.addField("DR_AMT", Types.NUMERIC, request.getParameter("DR_AMT"));
                    
                    trnDailyRec.addField("PAY_BY_CASH", Types.VARCHAR, request.getParameter("PAY_BY_CASH") == null ? "N" : "Y");
                    trnDailyRec.addField("PAY_BY_AR", Types.VARCHAR, request.getParameter("PAY_BY_AR") == null ? "N" : "Y");
                    trnDailyRec.addField("PAY_BY_DOCTOR", Types.VARCHAR, request.getParameter("PAY_BY_DOCTOR") == null ? "N" : "Y");
                    trnDailyRec.addField("PAY_BY_PAYOR", Types.VARCHAR, request.getParameter("PAY_BY_PAYOR") == null ? "N" : "Y");
                    trnDailyRec.addField("PAY_BY_CASH_AR", Types.VARCHAR, request.getParameter("PAY_BY_CASH_AR") == null ? "N" : "Y");

                    trnDailyRec.addField("AMOUNT_BEF_DISCOUNT", Types.NUMERIC, request.getParameter("AMOUNT_BEF_DISCOUNT"));
                    trnDailyRec.addField("AMOUNT_OF_DISCOUNT", Types.NUMERIC, request.getParameter("AMOUNT_OF_DISCOUNT"));
                    trnDailyRec.addField("BATCH_NO", Types.VARCHAR, "");

                    if("1".equalsIgnoreCase(request.getParameter("CANCEL_INVOICE"))){
                        trnDailyRec.addField("INV_IS_VOID", Types.VARCHAR, "1");
                        trnDailyRec.addField("REC_IS_VOID", Types.VARCHAR, "1");
                    }else{
                        trnDailyRec.addField("INV_IS_VOID", Types.VARCHAR, "0");
                        trnDailyRec.addField("REC_IS_VOID", Types.VARCHAR, "0");
                    }
                    
                    trnDailyRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                    trnDailyRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                    trnDailyRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                    trnDailyRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                    if(request.getParameter("TRANSACTION_TYPE").equalsIgnoreCase("REV")){
                        trnDailyRec.addField("RECEIPT_TYPE_CODE", Types.VARCHAR, request.getParameter("RECEIPT_TYPE_CODE"));
                        trnDailyRec.addField("YYYY", Types.VARCHAR, b.getYyyy());
                        trnDailyRec.addField("MM", Types.VARCHAR, b.getMm());
                        trnDailyRec.addField("DD", Types.VARCHAR, "");
                    }else{
                        trnDailyRec.addField("RECEIPT_TYPE_CODE", Types.VARCHAR, "AR");
                        trnDailyRec.addField("YYYY", Types.VARCHAR, "");
                        trnDailyRec.addField("MM", Types.VARCHAR, "");
                        trnDailyRec.addField("DD", Types.VARCHAR, "");
                    }
                    trnDailyRec.addField("NOR_ALLOCATE_AMT", Types.NUMERIC, "0");
                    trnDailyRec.addField("NOR_ALLOCATE_PCT", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_AMT", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_TAX_400", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_TAX_401", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_TAX_402", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_TAX_406", Types.NUMERIC, "0");
                    trnDailyRec.addField("TAX_TYPE_CODE", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_PREMIUM", Types.NUMERIC, "0");
                    trnDailyRec.addField("HP_AMT", Types.NUMERIC, "0");
                    trnDailyRec.addField("HP_PREMIUM", Types.NUMERIC, "0");
                    trnDailyRec.addField("HP_TAX", Types.NUMERIC, "0");
                    trnDailyRec.addField("COMPUTE_DAILY_DATE", Types.VARCHAR, "");
                    trnDailyRec.addField("COMPUTE_DAILY_TIME", Types.VARCHAR, "");
                    trnDailyRec.addField("COMPUTE_DAILY_USER_ID", Types.VARCHAR, "");
                    trnDailyRec.addField("DOCTOR_CATEGORY_CODE", Types.VARCHAR, "");
                    trnDailyRec.addField("EXCLUDE_TREATMENT", Types.VARCHAR, "");
                    trnDailyRec.addField("PREMIUM_CHARGE_PCT", Types.NUMERIC, "0");
                    trnDailyRec.addField("PREMIUM_REC_AMT", Types.NUMERIC, "0");
                    trnDailyRec.addField("ORDER_ITEM_CATEGORY_CODE", Types.VARCHAR, "");
                }
            }

            if (DBMgr.getRecordValue(trnDailyRec, "PAYOR_OFFICE_CODE") != "") {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM PAYOR_OFFICE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "PAYOR_OFFICE_CODE") + "'";
                payorOfficeRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "PAYOR_OFFICE_CATEGORY_CODE") != "") {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM PAYOR_OFFICE_CATEGORY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "PAYOR_OFFICE_CATEGORY_CODE") + "'";
                payorOfficeCategoryRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "PATIENT_DEPARTMENT_CODE") != "") {
                query = "SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "PATIENT_DEPARTMENT_CODE") + "'";
                patientDepartmentRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "PATIENT_LOCATION_CODE") != "") {
                query = "SELECT CODE, DESCRIPTION FROM LOCATION WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "PATIENT_LOCATION_CODE") + "'";
                patientLocationRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "RECEIPT_DEPARTMENT_CODE") != "") {
                query = "SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "RECEIPT_DEPARTMENT_CODE") + "'";
                receiptDepartmentRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "RECEIPT_LOCATION_CODE") != "") {
                query = "SELECT CODE, DESCRIPTION FROM LOCATION WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "RECEIPT_LOCATION_CODE") + "'";
                receiptLocationRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "RECEIPT_TYPE_CODE") != "") {
                query = "SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION FROM RECEIPT_TYPE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "RECEIPT_TYPE_CODE") + "'";
                receiptTypeRec = DBMgr.getRecord(query);
            }
            
            if (DBMgr.getRecordValue(trnDailyRec, "DOCTOR_DEPARTMENT_CODE") != "") {
                query = "SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "DOCTOR_DEPARTMENT_CODE") + "'";
                doctorDepartmentRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "ORDER_ITEM_CODE") != "") {
                query = "SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION FROM ORDER_ITEM WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "ORDER_ITEM_CODE") + "'";
                orderItemRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "DOCTOR_CODE") != "") {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "DOCTOR_CODE") + "'";
                doctorRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "DOCTOR_EXECUTE_CODE") != "") {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "DOCTOR_EXECUTE_CODE") + "'";
                doctorExecuteRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "DOCTOR_RESULT_CODE") != "") {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "DOCTOR_RESULT_CODE") + "'";
                doctorResultRec = DBMgr.getRecord(query);
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        
        <style type="text/css">
<!--
.style1 {color: #003399}
.style2 {color: #333}
-->
        </style>
</head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="invoice.jsp">
            
            <table width="500" class="form">
                <tr>
                    <th colspan="2">
			<div style="float: left;">${labelMap.TITLE_MAIN}</div>                    </th>
                </tr>
                <tr>
                    <td class="label"><label for="INVOICE_NO">${labelMap.INVOICE_NO} </label></td>
                    <td class="input">&nbsp;<%= DBMgr.getRecordValue(trnDailyRec, "INVOICE_NO")%><label for="INVOICE_DATE"></label></td>
                </tr>
                 <tr>
                   <td class="label"><label for="INVOICE_DATE">${labelMap.INVOICE_DATE} </label></td>
                   <td class="input">&nbsp;<%=JDate.showDate(DBMgr.getRecordValue(trnDailyRec, "INVOICE_DATE"))%></td>
                 </tr>
                 <tr>
                    <td class="label"><label for="RECEIPT_NO">${labelMap.RECEIPT_NO}</label></td>
                    <td class="input">&nbsp;<%= DBMgr.getRecordValue(trnDailyRec, "RECEIPT_NO")%></td>
                </tr>
                 <tr>
                   <td class="label"><label for="RECEIPT_DATE">${labelMap.RECEIPT_DATE}</label></td>
                   <td class="input">&nbsp;<%=JDate.showDate(DBMgr.getRecordValue(trnDailyRec, "RECEIPT_DATE"))%></td>
                 </tr>
                <tr>
                    <td class="label"><label for="HN_NO">${labelMap.HN_NO}</label></td>
                    <td class="input">&nbsp;<%= DBMgr.getRecordValue(trnDailyRec, "HN_NO")%></td>
                </tr>
                <tr>
                  <td class="label"><label for="EPISODE_NO">${labelMap.EPISODE_NO}</label></td>
                  <td class="input">&nbsp;<%= DBMgr.getRecordValue(trnDailyRec, "EPISODE_NO")%></td>
                </tr>
                <tr>
                    <td class="label"><label for="PATIENT_NAME">${labelMap.PATIENT_NAME}</label></td>
                    <td class="input">&nbsp;<%= DBMgr.getRecordValue(trnDailyRec, "PATIENT_NAME")%></td>
                </tr>
                <tr>
                  <td class="label"><label for="PAYOR_OFFICE_CODE">${labelMap.PAYOR_OFFICE_CODE}</label></td>
                    <td class="input">&nbsp;(<%= DBMgr.getRecordValue(payorOfficeRec, "CODE")%>) <%= DBMgr.getRecordValue(payorOfficeRec, "NAME")%></td>
                </tr>
                <tr>
                    <td class="label"><label for="PAYOR_OFFICE_CATEGORY_CODE">${labelMap.PAYOR_OFFICE_CATEGORY_CODE}</label>                    </td>
                    <td class="input">&nbsp;(<%= DBMgr.getRecordValue(payorOfficeCategoryRec, "CODE")%>) <%= DBMgr.getRecordValue(payorOfficeCategoryRec, "NAME")%></td>
                </tr>
                <tr>
                    <td class="label"><label for="ADMISSION_TYPE_CODE">${labelMap.ADMISSION_TYPE_CODE}</label></td>
					
					<td class="input">&nbsp;<% 
					if(DBMgr.getRecordValue(trnDailyRec, "ADMISSION_TYPE_CODE").equals("I")){out.println("IPD");}
					else if(DBMgr.getRecordValue(trnDailyRec, "ADMISSION_TYPE_CODE").equals("O")){ out.println("OPD");}
					%>
					                   </td>
                </tr>
                <tr>
                  <td class="label"><label for="TRANSACTION_TYPE">${labelMap.TRANSACTION_TYPE}</label></td>
                  <td class="input">&nbsp;<%
                  	if("INV".equals(DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE")))
                  	{
                  		out.println("Invoice");
                  	}
                  	if("REV".equals(DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE")))
                  	{
                  		out.println("Receipt");
                  	}
                  	%></td>
                </tr>
                <tr>
                  <td class="label"><label for="PATIENT_DEPARTMENT_CODE">${labelMap.PATIENT_DEPARTMENT_CODE}</label>                    </td>
                    <td class="input">&nbsp;(<%= DBMgr.getRecordValue(patientDepartmentRec, "CODE")%>) <%= DBMgr.getRecordValue(patientDepartmentRec, "DESCRIPTION")%></td>
                </tr>
                <tr>
                    <td class="label"><label for="PATIENT_LOCATION_CODE">${labelMap.PATIENT_LOCATION_CODE}</label>                    </td>
                    <td class="input">&nbsp;(<%= DBMgr.getRecordValue(patientLocationRec, "CODE")%>) <%= DBMgr.getRecordValue(patientLocationRec, "DESCRIPTION")%></td>
                </tr>
                <tr>
                    <td class="label"><label id="RECEIPT_DEPARTMENT_LABEL" for="RECEIPT_DEPARTMENT_CODE">${labelMap.RECEIPT_DEPARTMENT_CODE}</label>                    </td>
                    <td class="input">&nbsp;(<%= DBMgr.getRecordValue(receiptDepartmentRec, "CODE")%>) <%= DBMgr.getRecordValue(receiptDepartmentRec, "DESCRIPTION")%></td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="RECEIPT_LOCATION_CODE">${labelMap.RECEIPT_LOCATION_CODE}</label>                    </td>
                    <td class="input">&nbsp;(<%= DBMgr.getRecordValue(receiptLocationRec, "CODE")%>) <%= DBMgr.getRecordValue(receiptLocationRec, "DESCRIPTION")%></td>
                </tr>
                <tr>
                    <td class="label"><label for="TOTAL_BILL_AMOUNT">${labelMap.TOTAL_BILL_AMOUNT}</label></td>
                    <td class="input">&nbsp;<%= DBMgr.getRecordValue(trnDailyRec, "TOTAL_BILL_AMOUNT")%></td>
                </tr>
                <tr>
                    <td class="label"><label for="IS_WRITE_OFF">${labelMap.IS_WRITE_OFF}</label>                    </td>
                    <td class="input">&nbsp;
                        <input type="radio" id="IS_WRITE_OFF_Y" name="IS_WRITE_OFF" value="Y"<%= DBMgr.getRecordValue(trnDailyRec, "IS_WRITE_OFF").equalsIgnoreCase("Y") ? " checked=\"checked\"" : "" %> disabled="disabled" />
                               <label for="IS_WRITE_OFF_Y">${labelMap.IS_WRITE_OFF_Y}</label>
                        <input type="radio" id="IS_WRITE_OFF_N" name="IS_WRITE_OFF" value="N"<%= DBMgr.getRecordValue(trnDailyRec, "IS_WRITE_OFF").equalsIgnoreCase("N") || DBMgr.getRecordValue(trnDailyRec, "IS_WRITE_OFF").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> disabled />
                               <label for="IS_WRITE_OFF_N">${labelMap.IS_WRITE_OFF_N}</label>                   </td>
                </tr>
                <tr>
                    <th colspan="2">${labelMap.TITLE_DETAIL}</th>
                </tr>
                <tr>
                    <td class="label"><label for="LINE_NO">${labelMap.LINE_NO} </label></td>
                    <td class="input">&nbsp;<%=DBMgr.getRecordValue(trnDailyRec, "LINE_NO") %></td>
                </tr>
                <tr>
                  <td class="label"><label for="DOCTOR_DEPARTMENT_CODE">${labelMap.DOCTOR_DEPARTMENT_CODE}</label>                    </td>
                    <td class="input">&nbsp;(<%=DBMgr.getRecordValue(doctorDepartmentRec, "CODE") %>) <%=DBMgr.getRecordValue(doctorDepartmentRec, "DESCRIPTION")%></td>
                </tr>
                <tr>
                    <td class="label"><label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label></td>
                    <td class="input">&nbsp;(<%=DBMgr.getRecordValue(orderItemRec, "CODE")%>) <%=DBMgr.getRecordValue(orderItemRec, "DESCRIPTION")%></td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_EXECUTE_CODE">${labelMap.DOCTOR_EXECUTE_CODE}</label></td>
                    <td class="input">&nbsp;(<%=DBMgr.getRecordValue(doctorExecuteRec, "CODE") %>) <%=DBMgr.getRecordValue(doctorExecuteRec, "NAME")%></td>
                </tr>
                <tr>
                    <td class="label"><label for="EXECUTE_DATE">${labelMap.EXECUTE_DATE}</label></td>
                  <td class="input">&nbsp;<%=JDate.showDate(DBMgr.getRecordValue(trnDailyRec, "EXECUTE_DATE")) %>                    </td>
                </tr>
                <tr>
                  <td class="label"><label for="EXECUTE_TIME">${labelMap.EXECUTE_TIME}</label></td>
                  <td class="input">&nbsp;<%=JDate.showTime(DBMgr.getRecordValue(trnDailyRec, "EXECUTE_TIME"))%></td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_RESULT_CODE">${labelMap.DOCTOR_RESULT_CODE}</label></td>
                    <td class="input">&nbsp;(<%= DBMgr.getRecordValue(trnDailyRec, "DOCTOR_RESULT_CODE")%>)<%= DBMgr.getRecordValue(doctorResultRec, "NAME") %></td>
                </tr>
				<tr>
                    <td class="label"><label for="VERIFY_DATE">${labelMap.VERIFY_DATE}</label>                    </td>
                    <td class="input">&nbsp;<%=JDate.showDate(DBMgr.getRecordValue(trnDailyRec, "VERIFY_DATE"))%>                    </td>
                </tr>
                <tr>
                  <td class="label"><label for="VERIFY_TIME">${labelMap.VERIFY_TIME}</label></td>
                  <td class="input">&nbsp;<%=JDate.showTime(DBMgr.getRecordValue(trnDailyRec, "VERIFY_TIME")) %></td>
                </tr>
                <tr>
                    <td class="label"><label for="AMOUNT_BEF_DISCOUNT">${labelMap.AMOUNT_BEF_DISCOUNT}</label>                    </td>
                    <td class="input">&nbsp;<%=JNumber.getShowMoney(Double.parseDouble(DBMgr.getRecordValue(trnDailyRec, "AMOUNT_BEF_DISCOUNT")))%>  บาท                 </td>
                </tr>
				<tr>
				  <td class="label"><label for="AMOUNT_OF_DISCOUNT">${labelMap.AMOUNT_OF_DISCOUNT}</label> </td>
				  <td class="input">&nbsp;<%=JNumber.getShowMoney(Double.parseDouble(DBMgr.getRecordValue(trnDailyRec, "AMOUNT_OF_DISCOUNT")))%>  บาท</td>
			  </tr>
				<tr>
                  <td class="label"><label for="AMOUNT_AFT_DISCOUNT"><span class="style2">${labelMap.AMOUNT_AFT_DISCOUNT}</span></label>                    </td>
                    <td class="input">&nbsp;<%=JNumber.getShowMoney(Double.parseDouble(DBMgr.getRecordValue(trnDailyRec, "AMOUNT_AFT_DISCOUNT")))%>  บาท</td>
                </tr>
                <tr>
                  <td class="label"><label for="DR_AMT">${labelMap.DR_AMT}</label>  </td>
                  <td class="input">&nbsp;<%=JNumber.getShowMoney(Double.parseDouble(DBMgr.getRecordValue(trnDailyRec, "DR_AMT")))%> บาท</td>
                </tr>
                <tr>
                  <td class="label"><label for="RECEIPT_TYPE_CODE">${labelMap.RECEIPT_TYPE_CODE}</label>                    </td>
                    <td class="input">&nbsp;(<%= DBMgr.getRecordValue(receiptTypeRec, "CODE").equalsIgnoreCase("") ? "AR" : DBMgr.getRecordValue(receiptTypeRec, "CODE") %>) <%= DBMgr.getRecordValue(receiptTypeRec, "DESCRIPTION")%></td>
				</tr>
                <tr>
                    <td class="label">
                        <label for="PAY_BY">${labelMap.PAY_BY}</label>                    </td>
                    <td class="input">
                        <input id="PAY_BY_CASH" name="PAY_BY_CASH" type="checkbox" value="1"<%= (DBMgr.getRecordValue(receiptTypeRec, "CODE").equalsIgnoreCase("A")) || DBMgr.getRecordValue(trnDailyRec, "PAY_BY_CASH").equalsIgnoreCase("Y") ? " checked=\"checked\"" : "" %> disabled="disabled" />
                        <label for="PAY_BY_CASH">${labelMap.PAY_BY_CASH}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="PAY_BY_AR" name="PAY_BY_AR" type="checkbox" disabled="disabled" value="1"<%= DBMgr.getRecordValue(trnDailyRec, "PAY_BY_AR").equalsIgnoreCase("Y") ? " checked=\"checked\"" : "" %> />
                        <label for="PAY_BY_AR">${labelMap.PAY_BY_AR}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="PAY_BY_DOCTOR" name="PAY_BY_DOCTOR" type="checkbox" disabled="disabled" value="1"<%= DBMgr.getRecordValue(trnDailyRec, "PAY_BY_DOCTOR").equalsIgnoreCase("Y") ? " checked=\"checked\"" : "" %> />
                        <label for="PAY_BY_DOCTOR">${labelMap.PAY_BY_DOCTOR}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="PAY_BY_PAYOR" name="PAY_BY_PAYOR" type="checkbox" disabled="disabled" value="1"<%= DBMgr.getRecordValue(trnDailyRec, "PAY_BY_PAYOR").equalsIgnoreCase("Y") ? " checked=\"checked\"" : "" %> />
                        <label for="PAY_BY_PAYOR">${labelMap.PAY_BY_PAYOR}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="PAY_BY_CASH_AR" name="PAY_BY_CASH_AR" type="checkbox" disabled="disabled" value="1"<%= DBMgr.getRecordValue(trnDailyRec, "PAY_BY_CASH_AR").equalsIgnoreCase("Y") ? " checked=\"checked\"" : "" %> />
                        <label for="PAY_BY_CASH_AR">${labelMap.PAY_BY_CASH_AR}</label>                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.CANCEL_INVOICE}</label></td>
                    <td class="input">
                        <input type="radio" id="CANCEL_INVOICE_0" name="CANCEL_INVOICE" value="0" <%= DBMgr.getRecordValue(trnDailyRec, "INV_IS_VOID").equalsIgnoreCase("0")? " checked=\"checked\"" : ""%> disabled="disabled" />
                        <label for="ACTIVE_1">${labelMap.CANCEL_0}</label>
                        <input type="radio" id="CANCEL_INVOICE_1" name="CANCEL_INVOICE" value="1" <%= DBMgr.getRecordValue(trnDailyRec, "INV_IS_VOID").equalsIgnoreCase("1") || trnDailyRec == null ? " checked=\"checked\"" : ""%> disabled="disabled"/>
                        <label for="ACTIVE_0">${labelMap.CANCEL_1}</label>                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(trnDailyRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(trnDailyRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%>  disabled="disabled"/>
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(trnDailyRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> disabled="disabled" />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>                    </td>
                </tr>
                <tr>
                    <th colspan="2" class="buttonBar">                       
                    <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.close();" />                    </th>
                </tr>
          </table>
        </form>
    </body>
</html>
