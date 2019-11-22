<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="df.bean.db.table.Batch"%>
<%@ include file="../../_global.jsp" %>

<%
// Transaction Can't edit by condition
// 1. Invoice Date < Batch
// 2. Invoice Type = Order
// 3. Batch No != ''
// 4. Guarantee Note != ''
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

            labelMap.add("TITLE_MAIN", "Add/Edit Invoice", "ใบแจ้งหนี้/ใบเสร็จรับเงิน");
            labelMap.add("INVOICE_NO", "Invoice No.", "เลขที่ใบแจ้งหนี้");
            labelMap.add("INVOICE_DATE", "Invoice Date", "วันที่ใบแจ้งหนี้");
            labelMap.add("RECEIPT_NO", "Receive No.", "เลขที่ใบเสร็จ");
            labelMap.add("RECEIPT_DATE", "Receive Date", "วันที่ใบเสร็จ");
            labelMap.add("EPISODE_NO", "Episode No", "ลำดับผู้ป่วย");
            labelMap.add("PAYOR_OFFICE_CODE", "Payor Office", "คู่สัญญา");
            labelMap.add("PAYOR_OFFICE_CATEGORY_CODE", "Payor Office Category", "กลุ่มคู่สัญญา");
            labelMap.add("IS_WRITE_OFF", "Write Off", "หักหนี้สูญ");
            labelMap.add("IS_WRITE_OFF_Y", "Yes", "ใช่");
            labelMap.add("IS_WRITE_OFF_N", "No", "ไม่ใช่");
            labelMap.add("TRANSACTION_TYPE", "Transaction Type", "ประเภทรายการ");
            labelMap.add("INVOICE_TYPE", "Invoice Type", "ประเภทรายการรักษา");
            labelMap.add("PATIENT_DEPARTMENT_CODE", "Patient Department Code", "แผนกที่รักษาผู้ป่วย");
            labelMap.add("PATIENT_LOCATION_CODE", "Patient Location Code", "สถานที่รักษาผู้ป่วย");
            labelMap.add("RECEIPT_DEPARTMENT_CODE", "Receipt Department Code", "แผนกรับชำระเงิน");
            labelMap.add("RECEIPT_LOCATION_CODE", "Receipt Location Code", "สถานที่รับชำระเงิน");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รายการรักษา");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("RECEIPT_TYPE_CODE", "Receipt Type Code", "ประเภทการรับชำระเงิน");
            labelMap.add("TOTAL_BILL_AMOUNT", "Total Bill Amount", "จำนวนเงิน");

            labelMap.add("TITLE_DETAIL", "Invoice Details", "รายละเอียดใบแจ้งหนี้/ใบเสร็จรับเงิน");
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
            labelMap.add("DOCTOR_PRIVATE_CODE", "Private Doctor", "แพทย์เจ้าของไข้");
            labelMap.add("PAY_BY", "Payment Condition", "ชำระเงินด้วยเงื่อนไข");
            labelMap.add("PAY_BY_CASH", "Cash", "เงินสด");
            labelMap.add("PAY_BY_AR", "AR Receipt", "รับชำระหนี้");
            labelMap.add("PAY_BY_DOCTOR", "Doctor", "เงื่อนไขแพทย์");
            labelMap.add("PAY_BY_PAYOR", "Payor", "เงื่อนไขคู่สัญญา");
            labelMap.add("PAY_BY_CASH_AR", "Receipt In Month", "รับชำระในเดือน");

            labelMap.add("CANCEL_INVOICE","Cancel Invoice","ยกเลิกใบแจ้งหนี้");
            labelMap.add("CANCEL_0","Yes","Yes");
            labelMap.add("CANCEL_1","No","No");

            labelMap.add("AMOUNT_BEF_DISCOUNT", "Amount Before Discount", "จำนวนเงินก่อนหักส่วนลด");
            labelMap.add("AMOUNT_OF_DISCOUNT", "Amount Of Discount", "ส่วนลดค่าแพทย์");
            
            labelMap.add("TITLE_DATA", "Transaction Details", "รายละเอียดใบแจ้งหนี้/ใบเสร็จรับเงิน");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รายการรักษา");
            labelMap.add("DR_AMT", "Doctor Amount", "ค่าแพทย์");
            labelMap.add("AMOUNT_AFT_DISCOUNT", "Amount After Disc.", "จำนวนเงิน");
            labelMap.add("ALERT_INVALID_INVOICE_DATE", "Invalid invoice date (this date must be after the last batch date)", "วันที่อินวอยซ์ไม่ถูกต้อง (วันที่นี้จะต้องมีค่าหลังจากวันที่ปิดยอดครั้งล่าสุด)");
            labelMap.add("COPY", "Copy", "คัดลอก");
            labelMap.add("NOTE", "Note", "หมายเหตุ");

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
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
            con.Close();
            String BATCH_DATE = b.getYyyy() + b.getMm() + "00";

            request.setCharacterEncoding("UTF-8");
            byte MODE = MODE_QUERY;
            String query = "";
            
            DataRecord trnDailyRec = null, trnDailyLogRec = null, oldTrnDailyRec = null, payorOfficeRec = null, payorOfficeCategoryRec = null, patientDepartmentRec = null, patientLocationRec = null, receiptDepartmentRec = null, receiptLocationRec = null, receiptTypeRec = null;
            DataRecord doctorDepartmentRec = null, orderItemRec = null, doctorRec = null, doctorExecuteRec = null, doctorPrivateRec=null, doctorResultRec = null;
            
            if (request.getParameter("INVOICE_NO") != null && request.getParameter("LINE_NO") != null ) {
                query = "SELECT * FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + 
                		"' AND INVOICE_NO = '" + request.getParameter("INVOICE_NO") +
                		"' AND TRANSACTION_DATE = '" + request.getParameter("TRANSACTION_DATE") + 
                		"' AND INVOICE_TYPE = '" + request.getParameter("INVOICE_TYPE") + 
                    	"' AND LINE_NO = '" + DBMgr.toSQLString( request.getParameter("LINE_NO")) +
                    	"' AND RECEIPT_NO = '" + request.getParameter("RECEIPT_NO") +
               		    "' AND RECEIPT_DATE = '" + request.getParameter("RECEIPT_DATE") +  "'";
                System.out.print(" test : "+query);
                trnDailyRec = DBMgr.getRecord(query);
                MODE = MODE_UPDATE_DETAIL;
            }else if (request.getParameter("INVOICE_NO") != null) {
                query = "SELECT * FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + 
                		"' AND INVOICE_NO = '" + request.getParameter("INVOICE_NO") + "'";
                trnDailyRec = DBMgr.getRecord(query);
                MODE = MODE_UPDATE_MASTER;
            }else{
            	//System.out.println("Invoice = null");
            }
           	if (request.getParameter("MODE") != null) {
                MODE = Byte.parseByte(request.getParameter("MODE"));
                if (MODE == MODE_QUERY_SUBMIT) {
                    con = new DBConnection();
                    con.connectToLocal();
                    query = "SELECT COUNT(*) AS NUMROW FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND INVOICE_NO = '" + Util.formatHTMLString(request.getParameter("INVOICE_NO"), false) + "'";
                    rs = con.executeQuery(query);
                    if (rs != null && rs.next() && rs.getInt("NUMROW") > 0) {
                        MODE = MODE_UPDATE_MASTER;
                    }else {
                        MODE = MODE_QUERY;
                    }
                    con.Close();
                }else if (MODE == MODE_UPDATE_MASTER_SUBMIT) {
                    String cmd = "UPDATE TRN_DAILY SET" +
                        " INVOICE_DATE = '" + DBMgr.toSQLString(JDate.saveDate(request.getParameter("INVOICE_DATE"))) + "'" +
                        ", RECEIPT_NO = '" + DBMgr.toSQLString(request.getParameter("RECEIPT_NO")) + "'" +
                        ", RECEIPT_DATE = '" + DBMgr.toSQLString(JDate.saveDate(request.getParameter("RECEIPT_DATE"))) + "'" +
                        ", HN_NO = '" + DBMgr.toSQLString(request.getParameter("HN_NO")) + "'" +
                        ", PATIENT_NAME = '" + DBMgr.toSQLString(request.getParameter("PATIENT_NAME")) + "'" +
                        ", EPISODE_NO = '" + DBMgr.toSQLString(request.getParameter("EPISODE_NO")) + "'" +
                        ", PAYOR_OFFICE_CODE = '" + DBMgr.toSQLString(request.getParameter("PAYOR_OFFICE_CODE")) + "'" +
                        ", PAYOR_OFFICE_NAME = '" + DBMgr.toSQLString(request.getParameter("PAYOR_OFFICE_NAME")) + "'" +
                        ", IS_WRITE_OFF = '" + DBMgr.toSQLString(request.getParameter("IS_WRITE_OFF")) + "'" +
                        ", ADMISSION_TYPE_CODE = '" + DBMgr.toSQLString(request.getParameter("ADMISSION_TYPE_CODE")) + "'" +
                        ", TRANSACTION_TYPE = '" + DBMgr.toSQLString(request.getParameter("TRANSACTION_TYPE")) + "'" +
                        ", PATIENT_DEPARTMENT_CODE = '" + DBMgr.toSQLString(request.getParameter("PATIENT_DEPARTMENT_CODE")) + "'" +
                        ", PATIENT_LOCATION_CODE = '" + DBMgr.toSQLString(request.getParameter("PATIENT_LOCATION_CODE")) + "'" +
                        ", RECEIPT_DEPARTMENT_CODE = '" + DBMgr.toSQLString(request.getParameter("RECEIPT_DEPARTMENT_CODE")) + "'" +
                        ", RECEIPT_LOCATION_CODE = '" + DBMgr.toSQLString(request.getParameter("RECEIPT_LOCATION_CODE")) + "'" +
                        ", TOTAL_BILL_AMOUNT = " + DBMgr.toSQLString(request.getParameter("TOTAL_BILL_AMOUNT")) +
                        ", UPDATE_DATE = '" + DBMgr.toSQLString(JDate.getDate()) + "'" +
                        ", UPDATE_TIME = '" + DBMgr.toSQLString(JDate.getTime()) + "'" +
                        ", USER_ID = '" + DBMgr.toSQLString(session.getAttribute("USER_ID").toString()) + "'" +
                        ", RECEIPT_TYPE_CODE = '" + (request.getParameter("TRANSACTION_TYPE").equalsIgnoreCase("REV")? "A" : "AR") + "'" +
                        ", PAY_BY_CASH = '" + (request.getParameter("TRANSACTION_TYPE").equalsIgnoreCase("REV")? "Y" : "N") + "'" +
                        ", NOR_ALLOCATE_AMT = '0'"+
                        ", NOR_ALLOCATE_PCT = '0'"+
                        ", YYYY = '" + (request.getParameter("TRANSACTION_TYPE").equalsIgnoreCase("REV")? b.getYyyy() : "") + "'" +
                        ", MM = '" + (request.getParameter("TRANSACTION_TYPE").equalsIgnoreCase("REV")? b.getMm() : "") + "'" +
                        ", DR_AMT = '0'"+
                        ", DR_TAX_400 = '0'"+
                        ", DR_TAX_401 = '0'"+
                        ", DR_TAX_402 = '0'"+
                        ", DR_TAX_406 = '0'"+
                        ", TAX_TYPE_CODE = ''"+
                        ", DR_PREMIUM = '0'"+
                        ", HP_AMT = '0'"+
                        ", HP_PREMIUM = '0'"+
                        ", HP_TAX = '0'"+
                        ", COMPUTE_DAILY_DATE = ''"+
                        ", COMPUTE_DAILY_TIME = ''"+
                        ", COMPUTE_DAILY_USER_ID = ''"+
                        ", DOCTOR_CATEGORY_CODE = ''"+
                        ", EXCLUDE_TREATMENT = ''"+
                        ", PREMIUM_CHARGE_PCT = '0'"+
                        ", PREMIUM_REC_AMT = '0'"+
                        ", ORDER_ITEM_CATEGORY_CODE = ''"+
                        " WHERE INVOICE_NO = '" + DBMgr.toSQLString(request.getParameter("INVOICE_NO")) + "'" +
                        " AND HOSPITAL_CODE = '" + DBMgr.toSQLString(session.getAttribute("HOSPITAL_CODE").toString()) + "'";

                    con = new DBConnection();
                    con.connectToLocal();
                    System.out.println("cmd : "+cmd);
                    if (con.executeUpdate(cmd) > 0) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/invoice.jsp?INVOICE_NO=" + request.getParameter("INVOICE_NO")));// + "&INVOICE_DATE=" + request.getParameter("INVOICE_DATE")));
                    }else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                    con.Close();
                    
                    response.sendRedirect("../message.jsp");
                    return;
                }else if (MODE == MODE_INSERT_MASTER_DETAIL_SUBMIT || MODE == MODE_INSERT_DETAIL_SUBMIT || MODE == MODE_UPDATE_DETAIL_SUBMIT) {
                	trnDailyRec = new DataRecord("TRN_DAILY");
                    oldTrnDailyRec = new DataRecord("TRN_DAILY");
        			trnDailyLogRec = new DataRecord("LOG_TRN_DAILY");

                    trnDailyRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                    trnDailyRec.addField("INVOICE_NO", Types.VARCHAR, request.getParameter("INVOICE_NO"), true);
                    trnDailyRec.addField("INVOICE_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("INVOICE_DATE")), true);
                    trnDailyRec.addField("INVOICE_TYPE",Types.VARCHAR, request.getParameter("INVOICE_TYPE"));
                	
                    if (MODE != MODE_UPDATE_DETAIL_SUBMIT) {
                    	
                    	
                    	// NEW Invoice & New TRANSACTION_DATE BY INVOICE_DATE
                    	if("".equals(request.getParameter("TRANSACTION_DATE")) || request.getParameter("TRANSACTION_DATE") == null){
                    		trnDailyRec.addField("TRANSACTION_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("INVOICE_DATE")), true);
                    	} else  { 
                    		trnDailyRec.addField("TRANSACTION_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("TRANSACTION_DATE")), true);
                    	}
                    	
                        trnDailyRec.addField("TRANSACTION_MODULE", Types.VARCHAR, "TR");
                       	trnDailyRec.addField("OLD_DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_EXECUTE_CODE"));
                        trnDailyRec.addField("OLD_AMOUNT",Types.NUMERIC, request.getParameter("AMOUNT_AFT_DISCOUNT"));
                        trnDailyRec.addField("LINE_NO", Types.VARCHAR, request.getParameter("LINE_NO"), true);
                        if(request.getParameter("EXECUTE_TIME").length()<2){
                            trnDailyRec.addField("EXECUTE_TIME", Types.VARCHAR, "000000");
                            trnDailyRec.addField("VERIFY_TIME", Types.VARCHAR, "000000");                            
                        }else{
                            trnDailyRec.addField("EXECUTE_TIME", Types.VARCHAR, JDate.saveTime(request.getParameter("EXECUTE_TIME")));
                            trnDailyRec.addField("VERIFY_TIME", Types.VARCHAR, JDate.saveTime(request.getParameter("EXECUTE_TIME")));   
                            
                        }
                        trnDailyRec.addField("EXECUTE_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("EXECUTE_DATE")));
                        trnDailyRec.addField("VERIFY_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("EXECUTE_DATE")));
                       
                    }else{
                    	trnDailyRec.addField("TRANSACTION_DATE", Types.VARCHAR, request.getParameter("HI_TRANSACTION_DATE"),true);
                        trnDailyRec.addField("LINE_NO", Types.VARCHAR, request.getParameter("LINE_NO"), true);
                        trnDailyRec.addField("EXECUTE_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("EXECUTE_DATE")));
                        trnDailyRec.addField("EXECUTE_TIME", Types.VARCHAR, JDate.saveTime(request.getParameter("EXECUTE_TIME")));
                        trnDailyRec.addField("VERIFY_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("EXECUTE_DATE")));
                        trnDailyRec.addField("VERIFY_TIME", Types.VARCHAR, JDate.saveTime(request.getParameter("EXECUTE_TIME")));
                    }
                    trnDailyRec.addField("RECEIPT_NO", Types.VARCHAR, request.getParameter("RECEIPT_NO"),true);
                    trnDailyRec.addField("RECEIPT_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("RECEIPT_DATE")),true);
                    trnDailyRec.addField("HN_NO", Types.VARCHAR, request.getParameter("HN_NO"));
                    trnDailyRec.addField("PATIENT_NAME", Types.VARCHAR, request.getParameter("PATIENT_NAME"));
                    trnDailyRec.addField("EPISODE_NO", Types.VARCHAR, request.getParameter("EPISODE_NO"));
                    trnDailyRec.addField("PAYOR_OFFICE_CODE", Types.VARCHAR, request.getParameter("PAYOR_OFFICE_CODE"));
                    trnDailyRec.addField("PAYOR_OFFICE_NAME", Types.VARCHAR, request.getParameter("PAYOR_OFFICE_NAME"));//*
                    trnDailyRec.addField("IS_WRITE_OFF", Types.VARCHAR, request.getParameter("IS_WRITE_OFF"));//*
                    trnDailyRec.addField("TRANSACTION_TYPE", Types.VARCHAR, request.getParameter("TRANSACTION_TYPE"));//*
                    trnDailyRec.addField("ADMISSION_TYPE_CODE", Types.VARCHAR, request.getParameter("ADMISSION_TYPE_CODE"));//*
                    trnDailyRec.addField("PATIENT_DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("PATIENT_DEPARTMENT_CODE"));
                    trnDailyRec.addField("PATIENT_LOCATION_CODE", Types.VARCHAR, request.getParameter("PATIENT_LOCATION_CODE"));
                    trnDailyRec.addField("RECEIPT_DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("RECEIPT_DEPARTMENT_CODE"));
                    trnDailyRec.addField("RECEIPT_LOCATION_CODE", Types.VARCHAR, request.getParameter("RECEIPT_LOCATION_CODE"));
                    trnDailyRec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_EXECUTE_CODE"));
                    trnDailyRec.addField("TOTAL_BILL_AMOUNT", Types.NUMERIC, request.getParameter("TOTAL_BILL_AMOUNT"));
                    trnDailyRec.addField("DOCTOR_DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("DOCTOR_DEPARTMENT_CODE"));
                    trnDailyRec.addField("ORDER_ITEM_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CODE"));
                    trnDailyRec.addField("ORDER_ITEM_DESCRIPTION", Types.VARCHAR, request.getParameter("ORDER_ITEM_DESCRIPTION"));
                    trnDailyRec.addField("DOCTOR_EXECUTE_CODE", Types.VARCHAR, request.getParameter("DOCTOR_EXECUTE_CODE"));
	                trnDailyRec.addField("DOCTOR_PRIVATE_CODE", Types.VARCHAR, request.getParameter("DOCTOR_PRIVATE_CODE"));
                    trnDailyRec.addField("AMOUNT_AFT_DISCOUNT", Types.NUMERIC, request.getParameter("AMOUNT_AFT_DISCOUNT"));
                    trnDailyRec.addField("DR_AMT", Types.NUMERIC, request.getParameter("DR_AMT"));
                    trnDailyRec.addField("PAY_BY_CASH", Types.VARCHAR, request.getParameter("PAY_BY_CASH")!= null && request.getParameter("PAY_BY_CASH").equals("Y")?"Y":"N");
                    trnDailyRec.addField("PAY_BY_AR", Types.VARCHAR, request.getParameter("PAY_BY_AR")!= null && request.getParameter("PAY_BY_AR").equals("Y")?"Y":"N");
                    trnDailyRec.addField("PAY_BY_DOCTOR", Types.VARCHAR, request.getParameter("PAY_BY_DOCTOR")!= null && request.getParameter("PAY_BY_DOCTOR").equals("Y")?"Y":"N");
                    trnDailyRec.addField("PAY_BY_PAYOR", Types.VARCHAR, request.getParameter("PAY_BY_PAYOR")!= null && request.getParameter("PAY_BY_PAYOR").equals("Y")?"Y":"N");
                    trnDailyRec.addField("PAY_BY_CASH_AR", Types.VARCHAR, request.getParameter("PAY_BY_CASH_AR")!= null && request.getParameter("PAY_BY_CASH_AR").equals("Y")?"Y":"N");
                    trnDailyRec.addField("AMOUNT_BEF_DISCOUNT", Types.NUMERIC, request.getParameter("AMOUNT_BEF_DISCOUNT"));
                    trnDailyRec.addField("AMOUNT_OF_DISCOUNT", Types.NUMERIC, request.getParameter("AMOUNT_OF_DISCOUNT"));
                    trnDailyRec.addField("BATCH_NO", Types.VARCHAR, "");
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
                        trnDailyRec.addField("RECEIPT_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("RECEIPT_DATE")));
                        trnDailyRec.addField("RECEIPT_NO", Types.VARCHAR, request.getParameter("RECEIPT_NO"));
                        trnDailyRec.addField("YYYY", Types.VARCHAR, request.getParameter("RECEIPT_NO").equals("") ? "" : b.getYyyy());
                        trnDailyRec.addField("MM", Types.VARCHAR, request.getParameter("RECEIPT_NO").equals("") ? "" : b.getMm());
                        trnDailyRec.addField("DD", Types.VARCHAR, "");
                    }
                    trnDailyRec.addField("NOR_ALLOCATE_AMT", Types.NUMERIC, "0");
                    trnDailyRec.addField("NOR_ALLOCATE_PCT", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_AMT", Types.NUMERIC, "0");
                    trnDailyRec.addField("OLD_DR_AMT", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_TAX_400", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_TAX_401", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_TAX_402", Types.NUMERIC, "0");
                    trnDailyRec.addField("DR_TAX_406", Types.NUMERIC, "0");
                    trnDailyRec.addField("TAX_TYPE_CODE", Types.VARCHAR, "");
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
                    trnDailyRec.addField("NOTE", Types.VARCHAR, request.getParameter("NOTE"));

					//write original transaction to log
					if (MODE == MODE_UPDATE_DETAIL_SUBMIT) {
		                query = "INSERT INTO LOG_TRN_DAILY SELECT HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, RECEIPT_NO, RECEIPT_DATE, TRANSACTION_DATE, HN_NO, PATIENT_NAME, EPISODE_NO,"+ 
		                		"NATIONALITY_CODE, NATIONALITY_DESCRIPTION, PAYOR_OFFICE_CODE, PAYOR_OFFICE_NAME, TRANSACTION_MODULE, TRANSACTION_TYPE, "+
		                        "PAYOR_OFFICE_CATEGORY_CODE, PAYOR_OFFICE_CATEGORY_DESCRIPTION, IS_WRITE_OFF, LINE_NO, ADMISSION_TYPE_CODE, "+
		                        "PATIENT_DEPARTMENT_CODE, PATIENT_LOCATION_CODE, RECEIPT_DEPARTMENT_CODE, RECEIPT_LOCATION_CODE, DOCTOR_DEPARTMENT_CODE, "+
		                        "ORDER_ITEM_CODE, ORDER_ITEM_DESCRIPTION, DOCTOR_CODE, VERIFY_DATE, VERIFY_TIME, DOCTOR_EXECUTE_CODE, EXECUTE_DATE, EXECUTE_TIME,"+ 
		                        "DOCTOR_RESULT_CODE, OLD_DOCTOR_CODE, RECEIPT_TYPE_CODE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, AMOUNT_AFT_DISCOUNT, "+
		                        "AMOUNT_BEF_WRITE_OFF, INV_IS_VOID, REC_IS_VOID, UPDATE_DATE, UPDATE_TIME, USER_ID, BATCH_NO, YYYY, MM, DD, NOR_ALLOCATE_AMT, "+
		                        "NOR_ALLOCATE_PCT, DR_AMT, OLD_DR_AMT, DR_TAX_400, DR_TAX_401, DR_TAX_402, DR_TAX_406, TAX_TYPE_CODE, DR_PREMIUM, GUARANTEE_PAID_AMT, "+
		                        "GUARANTEE_AMT, GUARANTEE_CODE, GUARANTEE_DR_CODE, GUARANTEE_TYPE, GUARANTEE_DATE_TIME, GUARANTEE_TERM_MM, "+
		                        "GUARANTEE_TERM_YYYY, GUARANTEE_NOTE, IS_GUARANTEE, HP_AMT, HP_PREMIUM, HP_TAX, COMPUTE_DAILY_DATE, COMPUTE_DAILY_TIME, "+
		                        "COMPUTE_DAILY_USER_ID, DOCTOR_CATEGORY_CODE, EXCLUDE_TREATMENT, PREMIUM_CHARGE_PCT, PREMIUM_REC_AMT, ACTIVE, INVOICE_TYPE, "+
		                        "TOTAL_BILL_AMOUNT, TOTAL_DR_REC_AMOUNT, OLD_AMOUNT, PAY_BY_CASH, PAY_BY_AR, PAY_BY_DOCTOR, PAY_BY_PAYOR, PAY_BY_CASH_AR, IS_PAID, "+
		                        "ORDER_ITEM_ACTIVE, ORDER_ITEM_CATEGORY_CODE, WRITE_OFF_BILL_AMT, WRITE_OFF_RECEIPT_AMT, OLD_DR_AMT_BEF_WRITE_OFF, "+
		                        "DR_AMT_BEF_WRITE_OFF, DR_PREMIUM_BEF_WRITE_OFF, HP_AMT_BEF_WRITE_OFF, HP_PREMIUM_WRITE_OFF, OLD_TAX_AMT, "+
		                        "DR_TAX_406_BEF_WRITE_OFF, TAX_FROM_ALLOCATE, IS_GUARANTEE_FROM_ALLOC, IS_ONWARD, IS_PARTIAL, DR_AMT_BEF_PARTIAL, DR_TAX_BEF_PARTIAL, "+
		                        "AMT_BEF_PARTIAL,IS_DISCHARGE_SUMMARY,DOCTOR_PRIVATE_CODE, NOTE, SEQ_STEP,'','','"+JDate.getDate()+"' FROM TRN_DAILY  "+ 
		               		    "WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + 
                		        "' AND INVOICE_NO = '" + request.getParameter("INVOICE_NO") +
                		        "' AND INVOICE_TYPE ='"+ request.getParameter("HI_INVOICE_TYPE")+
                				"' AND RECEIPT_DATE = '" +JDate.saveDate(request.getParameter("RECEIPT_DATE")) + 
                    			"' AND LINE_NO = '" + DBMgr.toSQLString( request.getParameter("LINE_NO")) + "'";
		                System.out.println("Update Invoice : "+query);
		                DBConnection conUpdate;
		                conUpdate = new DBConnection();
		                conUpdate.connectToLocal();
		                conUpdate.executeUpdate(query);
		                conUpdate.Close();
					}
                    
                    if (MODE == MODE_INSERT_MASTER_DETAIL_SUBMIT || MODE == MODE_INSERT_DETAIL_SUBMIT) {
                        if (DBMgr.insertRecord(trnDailyRec)) {
                            session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/invoice.jsp?INVOICE_NO=%1$s", trnDailyRec.getField("INVOICE_NO").getValue())));
                        }else {
                            session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                        }
                    }else {
                    	System.out.println("eiei");
                        if ( DBMgr.updateRecord(trnDailyRec) ) {
                            session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/invoice.jsp?INVOICE_NO=%1$s", trnDailyRec.getField("INVOICE_NO").getValue())));
                        }else {
                            session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                        }
                    } 

                    response.sendRedirect("../message.jsp");
                    return;
                }
            }

            if (DBMgr.getRecordValue(trnDailyRec, "PAYOR_OFFICE_CODE") != "") {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM PAYOR_OFFICE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "PAYOR_OFFICE_CODE") + "'";
                payorOfficeRec = DBMgr.getRecord(query);
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
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "DOCTOR_CODE") + "'";
                doctorExecuteRec = DBMgr.getRecord(query);
            }
            if (DBMgr.getRecordValue(trnDailyRec, "DOCTOR_PRIVATE_CODE") != "") {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + DBMgr.getRecordValue(trnDailyRec, "DOCTOR_PRIVATE_CODE") + "'";
                doctorPrivateRec = DBMgr.getRecord(query);
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
        <script type="text/javascript">
            function INVOICE_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
                if (key == 13) {
                    AJAX_Refresh_INVOICE();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_INVOICE() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=INVOICE_NO='" + document.mainForm.INVOICE_NO.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_INVOICE);
            }
            
            function AJAX_Handle_Refresh_INVOICE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "INVOICE_NO")) {
                        document.mainForm.INVOICE_NO.value = "";
                        document.mainForm.INVOICE_DATE.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.INVOICE_DATE.value = toShowDate(getXMLNodeValue(xmlDoc, "INVOICE_DATE"));
                }
            }
            
            function HN_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    AJAX_Refresh_HN_NO();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_HN_NO() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=HN_NO='" + document.mainForm.HN_NO.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_HN_NO);
            }
            
            function AJAX_Handle_Refresh_HN_NO() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "HN_NO")) {
                        document.mainForm.HN_NO.value = "";
                        document.mainForm.PATIENT_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PATIENT_NAME.value = getXMLNodeValue(xmlDoc, "PATIENT_NAME");
                }
            }
            
            function PAYOR_OFFICE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.PAYOR_OFFICE_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_PAYOR_OFFICE() {
                var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.PAYOR_OFFICE_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE);
            }
            
            function AJAX_Handle_Refresh_PAYOR_OFFICE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.PAYOR_OFFICE_CODE.value = "";
                        document.mainForm.PAYOR_OFFICE_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PAYOR_OFFICE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_<%= labelMap.getFieldLangSuffix()%>");
                }
            }

            function PATIENT_DEPARTMENT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.PATIENT_DEPARTMENT_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_PATIENT_DEPARTMENT() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.PATIENT_DEPARTMENT_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_PATIENT_DEPARTMENT);
            }
            
            function AJAX_Handle_Refresh_PATIENT_DEPARTMENT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.PATIENT_DEPARTMENT_CODE.value = "";
                        document.mainForm.PATIENT_DEPARTMENT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PATIENT_DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function PATIENT_LOCATION_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.PATIENT_LOCATION_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_PATIENT_LOCATION() {
                var target = "../../RetrieveData?TABLE=LOCATION&COND=CODE='" + document.mainForm.PATIENT_LOCATION_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_PATIENT_LOCATION);
            }
            
            function AJAX_Handle_Refresh_PATIENT_LOCATION() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.PATIENT_LOCATION_CODE.value = "";
                        document.mainForm.PATIENT_LOCATION_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PATIENT_LOCATION_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function RECEIPT_DEPARTMENT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.RECEIPT_DEPARTMENT_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_RECEIPT_DEPARTMENT() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.RECEIPT_DEPARTMENT_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_RECEIPT_DEPARTMENT);
            }
            
            function AJAX_Handle_Refresh_RECEIPT_DEPARTMENT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.RECEIPT_DEPARTMENT_CODE.value = "";
                        document.mainForm.RECEIPT_DEPARTMENT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.RECEIPT_DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function DOCTOR_DEPARTMENT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_DEPARTMENT_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_DEPARTMENT() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.DOCTOR_DEPARTMENT_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_DEPARTMENT);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_DEPARTMENT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_DEPARTMENT_CODE.value = "";
                        document.mainForm.DOCTOR_DEPARTMENT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function RECEIPT_LOCATION_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.RECEIPT_LOCATION_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_RECEIPT_LOCATION() {
                var target = "../../RetrieveData?TABLE=LOCATION&COND=CODE='" + document.mainForm.RECEIPT_LOCATION_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_RECEIPT_LOCATION);
            }
            
            function AJAX_Handle_Refresh_RECEIPT_LOCATION() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.RECEIPT_LOCATION_CODE.value = "";
                        document.mainForm.RECEIPT_LOCATION_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.RECEIPT_LOCATION_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function RECEIPT_TYPE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.RECEIPT_TYPE_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_RECEIPT_TYPE() {
                var target = "../../RetrieveData?TABLE=RECEIPT_TYPE&COND=CODE='" + document.mainForm.RECEIPT_TYPE_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_RECEIPT_TYPE);
            }
            
            function AJAX_Handle_Refresh_RECEIPT_TYPE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.RECEIPT_TYPE_CODE.value = "";
                        document.mainForm.RECEIPT_TYPE_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.RECEIPT_TYPE_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%= labelMap.getFieldLangSuffix()%>");
                }
            }
            
            function ORDER_ITEM_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ORDER_ITEM_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_ORDER_ITEM() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" + document.mainForm.ORDER_ITEM_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_ORDER_ITEM);
            }
            
            function AJAX_Handle_ORDER_ITEM() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        //document.mainForm.ORDER_ITEM_CODE.value = "";
                        document.mainForm.ORDER_ITEM_DESCRIPTION.value = "Unknown Order Item";
                    }
                    else {
                        // Data found
                        document.mainForm.ORDER_ITEM_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                    }
                }
            }
            
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

            function AJAX_Refresh_DOCTOR() {
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "Unknown";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
            function DOCTOR_PRIVATE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_PRIVATE_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_PRIVATE_DOCTOR() {
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_PRIVATE_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_PRIVATE_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_PRIVATE_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_PRIVATE_CODE.value = "";
                        document.mainForm.DOCTOR_PRIVATE_NAME.value = "Unknown Doctor";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PRIVATE_DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
            function DOCTOR_EXECUTE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_EXECUTE_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_EXECUTE() {
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_EXECUTE_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_EXECUTE);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_EXECUTE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_EXECUTE_CODE.value = "";
                        document.mainForm.DOCTOR_EXECUTE_NAME.value = "Unknown Doctor";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_EXECUTE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }

            // Check data on table SUMMARY_MONTHLY : 2009-07-01 By Nop
            function AJAX_Verify_Check_Guarantee() {
                var date_input = document.mainForm.INVOICE_DATE.value;
                var target = "../../CheckSummaryGuaranteeSrvl?DATE_INPUT=" + date_input+"&FORM=invoice";
                AJAX_Request(target, AJAX_Handle_Verify_Check_Guarantee);
            }  

            function AJAX_Handle_Verify_Check_Guarantee(){
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                   // alert(getXMLNodeValue(xmlDoc, "STATUS"));
                    if (getXMLNodeValue(xmlDoc, "STATUS")=='YES') {
                    	<%
                        if (MODE == MODE_UPDATE_MASTER ||  MODE == MODE_INSERT_MASTER_DETAIL) {
			            %>                
                        document.mainForm.SAVE_MASTER.disabled = true;
                        <%} else if(MODE == MODE_INSERT_DETAIL || MODE == MODE_UPDATE_DETAIL) { %>
                        document.mainForm.SAVE_DETAIL.disabled = true;
                        <%} else {} %>
                    }else{
                    	<%
                        if (MODE == MODE_UPDATE_MASTER ||  MODE == MODE_INSERT_MASTER_DETAIL) {
			            %>                
                        document.mainForm.SAVE_MASTER.disabled = false;
                        <%} else if(MODE == MODE_INSERT_DETAIL || MODE == MODE_UPDATE_DETAIL) { %>
                        document.mainForm.SAVE_DETAIL.disabled = false;
                        <%} else {}%>
                    }
                }				
           	}
            
        
            function SELECT_Click() {
                if (!isObjectEmptyString(document.mainForm.INVOICE_NO, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    document.mainForm.MODE.value = <%=MODE_QUERY_SUBMIT%>;
                    document.mainForm.submit();
                }
            }
        
            function NEW_DETAIL_Click() {
            	document.mainForm.MODE.value = '<%= MODE_INSERT_DETAIL %>';
                document.mainForm.submit();
            }
        
            function NEW_MASTER_DETAIL_Click() {
                document.mainForm.MODE.value = '<%= MODE_INSERT_MASTER_DETAIL %>';
                document.mainForm.INVOICE_NO.value = '';
                document.mainForm.INVOICE_DATE.value = '';
                document.mainForm.submit();
            }
            
            function EDIT_DETAIL_Click(lineNo, transactionDate, invoiceType,receiptNo,receiptDate) {
                window.location = 'invoice.jsp?INVOICE_NO=' + document.mainForm.INVOICE_NO.value + '&LINE_NO=' + lineNo + '&TRANSACTION_DATE=' +transactionDate +'&INVOICE_TYPE=' +invoiceType+ '&RECEIPT_NO='+receiptNo+'&RECEIPT_DATE='+receiptDate;
            }
            
            function SAVE_MASTER_CLICK() {
                if (!isObjectEmptyString(document.mainForm.INVOICE_NO, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.INVOICE_DATE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    isObjectValidDate(document.mainForm.INVOICE_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
                    isObjectValidNumber(document.mainForm.TOTAL_BILL_AMOUNT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>')) {
                    document.mainForm.MODE.value = '<%= MODE_UPDATE_MASTER_SUBMIT %>';
                    document.mainForm.submit();
                }
            }
            
            function SAVE_DETAIL_CLICK() {
            	var transaction_type = document.mainForm.TRANSACTION_TYPE;
                var e_text = document.getElementById("RECEIPT_DEPARTMENT_LABEL");
                if(transaction_type.value=='REV'){}
                if (document.mainForm.MODE.value == <%=MODE_INSERT_DETAIL%>) {
                    document.mainForm.MODE.value = <%=MODE_INSERT_DETAIL_SUBMIT%>;
                }
                else if (document.mainForm.MODE.value == <%=MODE_UPDATE_DETAIL%>) {
                    document.mainForm.MODE.value = <%=MODE_UPDATE_DETAIL_SUBMIT%>;
                }
                else if (document.mainForm.MODE.value == <%=MODE_INSERT_MASTER_DETAIL%>) {
                    document.mainForm.MODE.value = <%=MODE_INSERT_MASTER_DETAIL_SUBMIT%>;
                }
                
                if (!isObjectEmptyString(document.mainForm.INVOICE_NO, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.RECEIPT_TYPE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.INVOICE_DATE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    isObjectValidDate(document.mainForm.INVOICE_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
                    isObjectValidNumber(document.mainForm.TOTAL_BILL_AMOUNT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') &&
                    !isObjectEmptyString(document.mainForm.LINE_NO, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    isObjectValidDate(document.mainForm.EXECUTE_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
                    isObjectValidTime(document.mainForm.EXECUTE_TIME, '<%=labelMap.get(LabelMap.ALERT_INVALID_TIME)%>') && 
                    isObjectValidNumber(document.mainForm.AMOUNT_AFT_DISCOUNT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') &&
                    isObjectValidNumber(document.mainForm.DR_AMT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>')) {

                	if(document.getElementById("TRANSACTION_TYPE").value == 'REV' && 
                            (document.mainForm.PAY_BY_CASH.value == 'N' &&
                             document.mainForm.PAY_BY_CASH_AR.value == 'N' &&
                             document.mainForm.PAY_BY_PAYOR.value == 'N' &&
                             document.mainForm.PAY_BY_AR.value == 'N' &&
                             document.mainForm.PAY_BY_DOCTOR.value == 'N')){
                        alert("Please select pay method!!");
                        return false;
                    }
                    if (document.mainForm.MODE.value == <%=MODE_INSERT_MASTER_DETAIL_SUBMIT%>) {
                        if (toSaveDate(document.mainForm.INVOICE_DATE.value) < document.mainForm.BATCH_DATE.value) {
                            alert('${labelMap.ALERT_INVALID_INVOICE_DATE}');
                        }
                        else {
                            AJAX_Verify_MASTER_DETAIL_Data();
                        }
                    }
                    else if (document.mainForm.MODE.value == <%=MODE_INSERT_DETAIL_SUBMIT%>) {
                        AJAX_Verify_DETAIL_Data();
                    }
                    else if (document.mainForm.MODE.value == <%=MODE_UPDATE_DETAIL_SUBMIT%>) {
                        document.mainForm.submit();
                    }
                }

            }
            
            function AJAX_Verify_MASTER_DETAIL_Data() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=INVOICE_NO='" + document.mainForm.INVOICE_NO.value+"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
               
                AJAX_Request(target, AJAX_Handle_Verify_MASTER_DETAIL_Data);
            }
            
            function AJAX_Handle_Verify_MASTER_DETAIL_Data() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "INVOICE_NO");
                    
                    switch (document.mainForm.MODE.value) {
                        case "<%=MODE_INSERT_MASTER_DETAIL_SUBMIT%>" :
                            if (beExist) {
                                alert('<%= labelMap.get(LabelMap.ALERT_DUPLICATE_DATA) %>');
                            }
                            else {
                                document.mainForm.submit();
                            }
                            break;
                    }
                }
            }
            
            function AJAX_Verify_DETAIL_Data() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=INVOICE_NO='" + document.mainForm.INVOICE_NO.value + "' AND LINE_NO='" + document.mainForm.LINE_NO.value + "'";
                AJAX_Request(target, AJAX_Handle_Verify_DETAIL_Data);
            }
            function AmountBlur(){
                var money_amount = document.mainForm.AMOUNT_BEF_DISCOUNT.value - document.mainForm.AMOUNT_OF_DISCOUNT.value;
				if(document.mainForm.AMOUNT_OF_DISCOUNT.value == ''){
					document.mainForm.AMOUNT_OF_DISCOUNT.value = 0;
				}
				document.mainForm.AMOUNT_AFT_DISCOUNT.value = money_amount.toFixed(2);
				//document.mainForm.AMOUNT_AFT_DISCOUNT.value = document.mainForm.AMOUNT_BEF_DISCOUNT.value - document.mainForm.AMOUNT_OF_DISCOUNT.value;
			}
			function DiscountBlur(){
                var money_amount = document.mainForm.AMOUNT_BEF_DISCOUNT.value - document.mainForm.AMOUNT_OF_DISCOUNT.value;
				document.mainForm.AMOUNT_AFT_DISCOUNT.value = money_amount.toFixed(2);
				//document.mainForm.AMOUNT_AFT_DISCOUNT.value = document.mainForm.AMOUNT_BEF_DISCOUNT.value - document.mainForm.AMOUNT_OF_DISCOUNT.value;
			}
            function AJAX_Handle_Verify_DETAIL_Data() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "INVOICE_NO");
                    
                    switch (document.mainForm.MODE.value) {
                        case "<%=MODE_INSERT_DETAIL_SUBMIT%>" :
                            if (beExist) {
                                if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                                    document.mainForm.MODE.value= "<%=MODE_UPDATE_DETAIL_SUBMIT%>";
                                    document.mainForm.submit();
                                }
                            }
                            else {
                                document.mainForm.submit();
                            }
                            break;
                    }
                }
            }
            function COPY_DATA(){
				document.getElementById("copyData").disabled = true;
				document.getElementById("LINE_NO").readOnly = false;
				document.getElementById("LINE_NO").value = document.getElementById("LINE_NO").value+"C";
				document.getElementById("MODE").value = 7;
            }
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
.style2 {color: #333}
-->
        </style>
</head>
    <body onload="AJAX_Verify_Check_Guarantee();" >
        <form id="mainForm" name="mainForm" method="post" action="invoice.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE%>" />
            <input type="hidden" id="BATCH_DATE" name="BATCH_DATE" value="<%= BATCH_DATE %>" />
            <input type="hidden" id="HI_INVOICE_TYPE" name="HI_INVOICE_TYPE" value="<%= DBMgr.getRecordValue(trnDailyRec, "INVOICE_TYPE")%>" />
            <input type="hidden" id="HI_TRANSACTION_DATE" name="HI_TRANSACTION_DATE" value="<%= DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_DATE")%>" />
           
            <center>
				<table width="800" border="0">
					<tr><td align="left">
						<b><font color='#003399'><%=Utils.getInfoPage("invoice.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
					</td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">
			<div style="float: left;">${labelMap.TITLE_MAIN}</div>                    </th>
                </tr>
                <tr>
                    <td class="label"><label for="INVOICE_NO"><span class="style1">${labelMap.INVOICE_NO} *</span></label></td>
                    <td class="input">
                        <input type="text" id="INVOICE_NO" name="INVOICE_NO" class="medium" maxlength="50" value="<%= DBMgr.getRecordValue(trnDailyRec, "INVOICE_NO")%>"<%= MODE == MODE_UPDATE_MASTER || MODE == MODE_INSERT_DETAIL || MODE == MODE_UPDATE_DETAIL ? " readonly=\"readonly\"" : ""%><%= MODE == MODE_UPDATE_MASTER ? " readonly=\"readonly\"" : ""%> onkeypress="return INVOICE_NO_KeyPress(event);" />
                        <input id="SEARCH_INVOICE_NO" name="SEARCH_INVOICE_NO" type="image" class="image_button" <%= MODE == MODE_UPDATE_DETAIL || MODE == MODE_INSERT_DETAIL ? " disabled=\"disabled\"" : ""%>
						src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search_invoice.jsp?TABLE=TRN_DAILY&RETURN_FIELD=INVOICE_NO&DISPLAY_FIELD=INVOICE_DATE&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=INVOICE_NO&HANDLE=AJAX_Refresh_INVOICE'); return false;" />                    </td>
                    <td class="label">
                        <label for="INVOICE_DATE"><span class="style1">${labelMap.INVOICE_DATE} *</span></label>                    </td>
                  <td class="input">
                    <input name="INVOICE_DATE" type="text" class="short" id="INVOICE_DATE" maxlength="10" value="<%=JDate.showDate(DBMgr.getRecordValue(trnDailyRec, "INVOICE_DATE"))%>"<%= MODE != MODE_INSERT_MASTER_DETAIL ? " readonly=\"readonly\"" : ""%> />
                    <input name="image" type="image" class="image_button" onclick="displayDatePicker('INVOICE_DATE'); return false;" src="../../images/calendar_button.png" alt=""
					<%= MODE == MODE_UPDATE_DETAIL || MODE == MODE_INSERT_DETAIL ? " disabled=\"disabled\"" : ""%> /></td>
                </tr>
<%
            if (MODE == MODE_QUERY || MODE == MODE_UPDATE_MASTER) {
%>                
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="SELECT_Click()" />
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="NEW_MASTER_DETAIL_Click()" />                    
						<input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='invoice.jsp'; return false;" />
					</th>
                </tr>
<%
            }
%>
                <tr>
                    <td class="label"><label for="RECEIPT_NO">${labelMap.RECEIPT_NO}</label></td>
                    <td class="input">
                        <input type="text" id="RECEIPT_NO" name="RECEIPT_NO" class="short" maxlength="20" readonly="readonly" value="<%= DBMgr.getRecordValue(trnDailyRec, "RECEIPT_NO")%>" />                    </td>
                    <td class="label">
                        <label for="RECEIPT_DATE">${labelMap.RECEIPT_DATE}</label>                    </td>
                    <td class="input">
                        <input name="RECEIPT_DATE" type="text" class="short" id="RECEIPT_DATE" maxlength="10" readonly="readonly" value="<%=JDate.showDate(DBMgr.getRecordValue(trnDailyRec, "RECEIPT_DATE"))%>" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="HN_NO">${labelMap.HN_NO}</label></td>
                    <td class="input">
                        <input name="HN_NO" type="text" class="short" id="HN_NO" onkeypress="return HN_NO_KeyPress(event);" value="<%= DBMgr.getRecordValue(trnDailyRec, "HN_NO")%>" maxlength="20"<%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " readonly=\"readonly\"" : ""%> />                    </td>
                    <td class="label"><label for="EPISODE_NO">${labelMap.EPISODE_NO}</label></td>
                    <td class="input">
                        <input name="EPISODE_NO" type="text" class="short" id="EPISODE_NO" value="<%= DBMgr.getRecordValue(trnDailyRec, "EPISODE_NO")%>"<%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " readonly=\"readonly\"" : ""%> maxlength="20" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="PATIENT_NAME">${labelMap.PATIENT_NAME}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="PATIENT_NAME" name="PATIENT_NAME" value="<%= DBMgr.getRecordValue(trnDailyRec, "PATIENT_NAME")%>" class="long"<%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " readonly=\"readonly\"" : ""%> />                    </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="PAYOR_OFFICE_CODE"><span class="style1">${labelMap.PAYOR_OFFICE_CODE} *</span></label>                    </td>
                    <td class="input" colspan="3">
                        <input name="PAYOR_OFFICE_CODE" type="text" class="short" id="PAYOR_OFFICE_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(payorOfficeRec, "CODE")%>" <%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " readonly=\"readonly\"" : ""%> onkeypress="return PAYOR_OFFICE_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE();" />
                        <input type="image" class="image_button" <%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " disabled=\"disabled\"" : ""%>
						src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PAYOR_OFFICE_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE'); return false;" />
                        <input name="PAYOR_OFFICE_NAME" type="text" class="mediumMax" id="PAYOR_OFFICE_NAME" readonly="readonly" value="<%= DBMgr.getRecordValue(payorOfficeRec, "NAME")%>" maxlength="255" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ADMISSION_TYPE_CODE"><span class="style1">${labelMap.ADMISSION_TYPE_CODE} *</span></label></td>
					
					<td class="input">
                        <select id="ADMISSION_TYPE_CODE" name="ADMISSION_TYPE_CODE" class="medium"<%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " disabled=\"disabled\"" : ""%>>
                            <%= DBMgr.generateOptionList("inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM ADMISSION_TYPE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(trnDailyRec, "ADMISSION_TYPE_CODE"),true)%>
                        </select>
                        <%
                            if(MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ){
                                //" disabled=\"disabled\"";
                                out.println("<input type='hidden' value='"+ DBMgr.getRecordValue(trnDailyRec, "ADMISSION_TYPE_CODE") +"' name='ADMISSION_TYPE_CODE'>");
                            }else{
                                //"";
                            }
                        %>                    </td>
                    <td class="label"><label for="TRANSACTION_TYPE"><span class="style1">${labelMap.TRANSACTION_TYPE} *</span></label></td>
                    <td class="input">
                        <select id="TRANSACTION_TYPE" name="TRANSACTION_TYPE"<%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " disabled=\"disabled\"" : ""%> onchange="changeTransactionType()">
                            <option value="INV" <% if("INV".equals(DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE"))){ out.println("selected");} %>>Invoice</option>
                            <option value="REV" <% if("REV".equals(DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE"))){ out.println("selected");} %>>Receipt</option>
                        </select>
                        <%
                            if(MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ){
                                //" disabled=\"disabled\"";
                                out.println("<input type='hidden' value='"+ DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE") +"' name='TRANSACTION_TYPE'>");
                            }else{
                                //"";
                            }
                        %>
                 	</td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="PATIENT_DEPARTMENT_CODE"><span class="style1">${labelMap.PATIENT_DEPARTMENT_CODE} *</span></label>                    </td>
                    <td class="input" colspan="3">
                        <input name="PATIENT_DEPARTMENT_CODE" type="text" class="short" id="PATIENT_DEPARTMENT_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(patientDepartmentRec, "CODE")%>" onkeypress="return PATIENT_DEPARTMENT_CODE_KeyPress(event);" onblur="AJAX_Refresh_PATIENT_DEPARTMENT();" />
                        <input type="image" class="image_button"
						src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PATIENT_DEPARTMENT_CODE&HANDLE=AJAX_Refresh_PATIENT_DEPARTMENT'); return false;" />
                        <input name="PATIENT_DEPARTMENT_DESCRIPTION" type="text" class="mediumMax" id="PATIENT_DEPARTMENT_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(patientDepartmentRec, "DESCRIPTION")%>" maxlength="255" />                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="PATIENT_LOCATION_CODE">${labelMap.PATIENT_LOCATION_CODE}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="PATIENT_LOCATION_CODE" type="text" class="short" id="PATIENT_LOCATION_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(patientLocationRec, "CODE")%>" onkeypress="return PATIENT_LOCATION_CODE_KeyPress(event);" onblur="AJAX_Refresh_PATIENT_LOCATION();" />
                        <input type="image" class="image_button"
						src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=LOCATION&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PATIENT_LOCATION_CODE&HANDLE=AJAX_Refresh_PATIENT_LOCATION'); return false;" />
                        <input name="PATIENT_LOCATION_DESCRIPTION" type="text" class="mediumMax" id="PATIENT_LOCATION_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(patientLocationRec, "DESCRIPTION")%>" maxlength="255" />                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label id="RECEIPT_DEPARTMENT_LABEL" for="RECEIPT_DEPARTMENT_CODE">${labelMap.RECEIPT_DEPARTMENT_CODE}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="RECEIPT_DEPARTMENT_CODE" type="text" class="short" id="RECEIPT_DEPARTMENT_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(receiptDepartmentRec, "CODE")%>"<%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " readonly=\"readonly\"" : ""%> onkeypress="return RECEIPT_DEPARTMENT_CODE_KeyPress(event);" onblur="AJAX_Refresh_RECEIPT_DEPARTMENT();" />
                        <input type="image" class="image_button" <%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " disabled=\"disabled\"" : ""%>
						src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=RECEIPT_DEPARTMENT_CODE&HANDLE=AJAX_Refresh_RECEIPT_DEPARTMENT'); return false;" />
                        <input name="RECEIPT_DEPARTMENT_DESCRIPTION" type="text" class="mediumMax" id="RECEIPT_DEPARTMENT_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(receiptDepartmentRec, "DESCRIPTION")%>" maxlength="255" />                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="RECEIPT_LOCATION_CODE">${labelMap.RECEIPT_LOCATION_CODE}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="RECEIPT_LOCATION_CODE" type="text" class="short" id="RECEIPT_LOCATION_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(receiptLocationRec, "CODE")%>"<%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " readonly=\"readonly\"" : ""%> onkeypress="return RECEIPT_LOCATION_CODE_KeyPress(event);" onblur="AJAX_Refresh_RECEIPT_LOCATION();" />
                        <input type="image" class="image_button" <%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " disabled=\"disabled\"" : ""%>
						src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=LOCATION&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=RECEIPT_LOCATION_CODE&HANDLE=AJAX_Refresh_RECEIPT_LOCATION'); return false;" />
                        <input name="RECEIPT_LOCATION_DESCRIPTION" type="text" class="mediumMax" id="RECEIPT_LOCATION_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(receiptLocationRec, "DESCRIPTION")%>" maxlength="255" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="TOTAL_BILL_AMOUNT">${labelMap.TOTAL_BILL_AMOUNT}</label></td>
                    <td class="input">
                        <input type="text" id="TOTAL_BILL_AMOUNT" name="TOTAL_BILL_AMOUNT" class="short alignRight" maxlength="20" value="<%= DBMgr.getRecordValue(trnDailyRec, "TOTAL_BILL_AMOUNT")%>"<%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " readonly=\"readonly\"" : ""%> />                    </td>
                    <td class="label">
                        <label for="IS_WRITE_OFF">${labelMap.IS_WRITE_OFF}</label>                    </td>
                    <td class="input">
                        <input type="radio" id="IS_WRITE_OFF_Y" name="IS_WRITE_OFF" value="Y"<%= DBMgr.getRecordValue(trnDailyRec, "IS_WRITE_OFF").equalsIgnoreCase("Y") ? " checked=\"checked\"" : "" %><%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " disabled=\"disabled\"" : ""%> />
                               <label for="IS_WRITE_OFF_Y">${labelMap.IS_WRITE_OFF_Y}</label>
                        <input type="radio" id="IS_WRITE_OFF_N" name="IS_WRITE_OFF" value="N"<%= DBMgr.getRecordValue(trnDailyRec, "IS_WRITE_OFF").equalsIgnoreCase("N") || DBMgr.getRecordValue(trnDailyRec, "IS_WRITE_OFF").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %><%= MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ? " disabled=\"disabled\"" : ""%> />
                               <label for="IS_WRITE_OFF_N">${labelMap.IS_WRITE_OFF_N}</label>
                        <%
                            if(MODE != MODE_INSERT_MASTER_DETAIL && MODE != MODE_UPDATE_MASTER ){
                                //" disabled=\"disabled\"";
                                out.println("<input type='hidden' value='"+ DBMgr.getRecordValue(trnDailyRec, "IS_WRITE_OFF") +"' name='IS_WRITE_OFF'>");
                            }else{
                                //"";
                            }
                        %>
                    </td>
                </tr>
<%
            if (MODE == MODE_UPDATE_MASTER) {
%>                
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SAVE_MASTER" name="SAVE_MASTER" class="button" value="${labelMap.SAVE}" onclick="SAVE_MASTER_CLICK(); return false;" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
                </tr>
<%
            }
            else if (MODE == MODE_INSERT_MASTER_DETAIL || MODE == MODE_INSERT_DETAIL || MODE == MODE_UPDATE_DETAIL) {
%>
                <tr>
                    <th colspan="4">${labelMap.TITLE_DETAIL}</th>
                </tr>
                <tr>
                    <td class="label"><label for="LINE_NO"><span class="style1">${labelMap.LINE_NO} *</span></label></td>
                    <td class="input">
                        <input type="text" id="LINE_NO" name="LINE_NO" class="medium" maxlength="50" value="<%= MODE == MODE_INSERT_DETAIL ? session.getAttribute("HOSPITAL_CODE").toString()+JDate.getTimeInMillis() : DBMgr.getRecordValue(trnDailyRec, "LINE_NO")%>"<%= MODE == MODE_UPDATE_DETAIL ? "readonly=\"readonly\"" : ""%> />
                    </td>
                    <td class="label"><label for="INVOICE_TYPE"><span class="style1">${labelMap.INVOICE_TYPE} *</span></label></td>
                    <td class="input">
                        <select id="INVOICE_TYPE" name="INVOICE_TYPE">
                            <option value="EXECUTE" <% if("EXECUTE".equals(DBMgr.getRecordValue(trnDailyRec, "INVOICE_TYPE"))){ out.println("selected");} %>>Execute</option>
                            <option value="RESULT" <% if("RESULT".equals(DBMgr.getRecordValue(trnDailyRec, "INVOICE_TYPE"))){ out.println("selected");} %>>Result</option>
                            <option value="ORDER" <% if("ORDER".equals(DBMgr.getRecordValue(trnDailyRec, "INVOICE_TYPE"))){ out.println("selected");} %>>Order</option>
                        </select>
                 	</td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="DOCTOR_DEPARTMENT_CODE">${labelMap.DOCTOR_DEPARTMENT_CODE}</label></td>
                    <td class="input" colspan="3">
                        <input name="DOCTOR_DEPARTMENT_CODE" type="text" class="short" id="DOCTOR_DEPARTMENT_CODE" maxlength="20" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(doctorDepartmentRec, "CODE")%>" onkeypress="return DOCTOR_DEPARTMENT_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_DEPARTMENT();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DOCTOR_DEPARTMENT_CODE&HANDLE=AJAX_Refresh_DOCTOR_DEPARTMENT'); return false;" />
                        <input name="DOCTOR_DEPARTMENT_DESCRIPTION" type="text" class="mediumMax" id="DOCTOR_DEPARTMENT_DESCRIPTION" readonly="readonly" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(doctorDepartmentRec, "DESCRIPTION")%>" maxlength="255" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ORDER_ITEM_CODE"><span class="style1">${labelMap.ORDER_ITEM_CODE} *</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="ORDER_ITEM_CODE" name="ORDER_ITEM_CODE" class="short" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(orderItemRec, "CODE") %>" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM();" />
                        <input id="SEARCH_ORDER_ITEM_CODE" name="SEARCH_ORDER_ITEM_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_<%= labelMap.getFieldLangSuffix() %>&TARGET=ORDER_ITEM_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_ORDER_ITEM'); return false;" />
                        <input type="text" id="ORDER_ITEM_DESCRIPTION" name="ORDER_ITEM_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(orderItemRec, "DESCRIPTION") %>" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_EXECUTE_CODE"><span class="style1">${labelMap.DOCTOR_EXECUTE_CODE} *</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_EXECUTE_CODE" name="DOCTOR_EXECUTE_CODE" class="short" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(trnDailyRec, "DOCTOR_CODE") %>" onkeypress="return DOCTOR_EXECUTE_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_EXECUTE();" />
                        <input id="SEARCH_DOCTOR_EXECUTE_CODE" name="SEARCH_DOCTOR_EXECUTE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_EXECUTE_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_EXECUTE'); return false;" />
                        <input type="text" id="DOCTOR_EXECUTE_NAME" name="DOCTOR_EXECUTE_NAME" class="mediumMax" readonly="readonly" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(doctorExecuteRec, "NAME") %>" />                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="EXECUTE_DATE"><span class="style1">${labelMap.EXECUTE_DATE} *</span></label>
                    </td>
                  	<td class="input">
                        <input name="EXECUTE_DATE" type="text" class="short" id="EXECUTE_DATE" maxlength="10" value="<%= MODE == MODE_INSERT_DETAIL ? "" : JDate.showDate(DBMgr.getRecordValue(trnDailyRec, "VERIFY_DATE"))%>" onblur="CheckDate(this.value,'EXECUTE_DATE','')"/>
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('EXECUTE_DATE'); return false;" />
                        DD/MM/YYYY
                    </td>
                    <td class="label">
                        <label for="EXECUTE_TIME"><span class="style1">${labelMap.EXECUTE_TIME} *</span></label>
                    </td>
                    <td class="input">
					    <input name="EXECUTE_TIME" type="text" class="short" id="EXECUTE_TIME" maxlength="8" value="<%= MODE == MODE_INSERT_DETAIL ? "" : JDate.showTime(DBMgr.getRecordValue(trnDailyRec, "VERIFY_TIME")) %>" onchange="return checkKeyTime(this);" />HHMM
					</td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_PRIVATE_CODE"><span class="">${labelMap.DOCTOR_PRIVATE_CODE}</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_PRIVATE_CODE" name="DOCTOR_PRIVATE_CODE" class="short" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(doctorPrivateRec, "CODE") %>" onkeypress="return DOCTOR_PRIVATE_CODE_KeyPress(event);" onblur="AJAX_Refresh_PRIVATE_DOCTOR();" />
                        <input id="SEARCH_DOCTOR_PRIVATE_CODE" name="SEARCH_DOCTOR_PRIVATE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_PRIVATE_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_PRIVATE_DOCTOR'); return false;" />
                        <input type="text" id="PRIVATE_DOCTOR_NAME" name="PRIVATE_DOCTOR_NAME" class="mediumMax" readonly="readonly" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(doctorPrivateRec, "NAME") %>" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="AMOUNT_BEF_DISCOUNT"><span class="style1">${labelMap.AMOUNT_BEF_DISCOUNT} *</span></label>                    </td>
                    <td class="input">
                        <input name="AMOUNT_BEF_DISCOUNT" type="text" class="short alignRight" id="AMOUNT_BEF_DISCOUNT" maxlength="13" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(trnDailyRec, "AMOUNT_BEF_DISCOUNT") %>" onblur="AmountBlur()" />                    </td>
                    <td class="label">
                        <label for="AMOUNT_OF_DISCOUNT"><span class="style1">${labelMap.AMOUNT_OF_DISCOUNT} *</span></label>                    </td>
                    <td class="input">
                        <input name="AMOUNT_OF_DISCOUNT" type="text" class="short alignRight" id="AMOUNT_OF_DISCOUNT" maxlength="13" value="<%= MODE == MODE_INSERT_DETAIL ? "0" : DBMgr.getRecordValue(trnDailyRec, "AMOUNT_OF_DISCOUNT") %>"  onblur="DiscountBlur()" />                    </td>
                </tr>
				<tr>
                  <td class="label">
                    <label for="AMOUNT_AFT_DISCOUNT"><span class="style2">${labelMap.AMOUNT_AFT_DISCOUNT}</span></label>                    </td>
                    <td class="input">
                        <input name="AMOUNT_AFT_DISCOUNT" type="text" class="short alignRight" id="AMOUNT_AFT_DISCOUNT" maxlength="13" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(trnDailyRec, "AMOUNT_AFT_DISCOUNT") %>" readonly="readonly" />                    </td>
                    <td class="label">
                        <label for="DR_AMT">${labelMap.DR_AMT}</label>                    </td>
                    <td class="input">
                        <input name="DR_AMT" type="text" class="short alignRight" id="DR_AMT" maxlength="13" value="<%= MODE == MODE_INSERT_DETAIL ? "" : DBMgr.getRecordValue(trnDailyRec, "DR_AMT") %>" readonly="readonly"  />                    </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="RECEIPT_TYPE_CODE"><span class="style1">${labelMap.RECEIPT_TYPE_CODE} *</span></label>                    </td>
                    <td class="input" colspan="3">
                        <input name="RECEIPT_TYPE_CODE" type="text" class="short" id="RECEIPT_TYPE_CODE" maxlength="20" 
						value="<%= ((MODE == MODE_INSERT_DETAIL) || (MODE == MODE_INSERT_MASTER_DETAIL)) && DBMgr.getRecordValue(receiptTypeRec, "CODE").equalsIgnoreCase("") ? "AR" : DBMgr.getRecordValue(receiptTypeRec, "CODE") %>" 
						onkeypress="return RECEIPT_TYPE_CODE_KeyPress(event);" onblur="AJAX_Refresh_RECEIPT_TYPE();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=RECEIPT_TYPE&DISPLAY_FIELD=DESCRIPTION_<%= labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=RECEIPT_TYPE_CODE&HANDLE=AJAX_Refresh_RECEIPT_TYPE'); return false;" />
                        <input name="RECEIPT_TYPE_DESCRIPTION" type="text" class="mediumMax" id="RECEIPT_TYPE_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(receiptTypeRec, "DESCRIPTION")%>" maxlength="255" />                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="PAY_BY">${labelMap.PAY_BY}</label>                    </td>
                    <td class="input" colspan="3">
                        <input id="PAY_BY_CASH" name="PAY_BY_CASH" type="checkbox" onclick="pay_cash_click();"
                        <%= DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE").equalsIgnoreCase("INV") ? "disabled " : "" %>
                        <%= DBMgr.getRecordValue(trnDailyRec, "PAY_BY_CASH").equalsIgnoreCase("Y") ? "value=\"Y\" checked=\"checked\"" : "value=\"N\"" %> />
                        <label for="PAY_BY_CASH">${labelMap.PAY_BY_CASH}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="PAY_BY_AR" name="PAY_BY_AR" type="checkbox" onclick="pay_ar_click();"
                        <%= DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE").equalsIgnoreCase("INV") ? "disabled " : "" %> 
                        <%= DBMgr.getRecordValue(trnDailyRec, "PAY_BY_AR").equalsIgnoreCase("Y") ? "value=\"Y\" checked=\"checked\"" : "value=\"N\"" %> />
                        <label for="PAY_BY_AR">${labelMap.PAY_BY_AR}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="PAY_BY_DOCTOR" name="PAY_BY_DOCTOR" type="checkbox" onclick="pay_doctor_click();"
                        <%= DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE").equalsIgnoreCase("INV") ? "disabled " : "" %>
                        <%= DBMgr.getRecordValue(trnDailyRec, "PAY_BY_DOCTOR").equalsIgnoreCase("Y") ? "value=\"Y\" checked=\"checked\"" : "value=\"N\"" %> />
                        <label for="PAY_BY_DOCTOR">${labelMap.PAY_BY_DOCTOR}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="PAY_BY_PAYOR" name="PAY_BY_PAYOR" type="checkbox" onclick="pay_payor_click();"
                        <%= DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE").equalsIgnoreCase("INV") ? "disabled " : "" %>
                        <%= DBMgr.getRecordValue(trnDailyRec, "PAY_BY_PAYOR").equalsIgnoreCase("Y") ? "value=\"Y\" checked=\"checked\"" : "value=\"N\"" %> />
                        <label for="PAY_BY_PAYOR">${labelMap.PAY_BY_PAYOR}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="PAY_BY_CASH_AR" name="PAY_BY_CASH_AR" type="checkbox" onclick="pay_cash_ar_click();"
                        <%= DBMgr.getRecordValue(trnDailyRec, "TRANSACTION_TYPE").equalsIgnoreCase("INV") ? "disabled " : "" %>
                        <%= DBMgr.getRecordValue(trnDailyRec, "PAY_BY_CASH_AR").equalsIgnoreCase("Y") ? "value=\"Y\" checked=\"checked\"" : "value=\"N\"" %> />
                        <label for="PAY_BY_CASH_AR">${labelMap.PAY_BY_CASH_AR}</label>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= MODE == MODE_INSERT_DETAIL || MODE == MODE_INSERT_MASTER_DETAIL || DBMgr.getRecordValue(trnDailyRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(trnDailyRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(trnDailyRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="Note">${labelMap.Note}</label>                    
                    </td>
                    <td class="input" colspan="3">
                    	<textarea  name="NOTE" id="NOTE"  rows="2"  cols="50"><%= DBMgr.getRecordValue(trnDailyRec, "NOTE")%></textarea>
                    </td>
                </tr>
                
                <tr>
                    <th colspan="4" class="buttonBar">                        
                    	<%if(MODE == MODE_UPDATE_DETAIL){ %>
						<input type="button" id="copyData" name="copyData" class="button" value="${labelMap.COPY}" onclick="COPY_DATA();"/>
                        <%} %>
                        <input type="button" id="SAVE_DETAIL" name="SAVE_DETAIL" class="button" value="${labelMap.SAVE}" onclick="SAVE_DETAIL_CLICK(); return false;" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='invoice.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location.href='invoice.jsp?INVOICE_NO=<%=request.getParameter("INVOICE_NO")%>'" />                    </th>
                </tr>
<%
            } else {
%>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='invoice.jsp'; return false;" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
                </tr>
<%
            }
%>
            </table>
<%
            if (MODE == MODE_UPDATE_MASTER) {
%>                
            <hr />
            <table class="data">
                <tr>
                    <th colspan="10" class="alignLeft">${labelMap.TITLE_DATA}</th>
                </tr>
                <tr>
                	<td class="sub_head">${labelMap.EXECUTE_DATE}</td>
                    <td class="sub_head">${labelMap.LINE_NO}</td>
                    <td class="sub_head">${labelMap.ORDER_ITEM_CODE}</td>
                    <td class="sub_head">${labelMap.DOCTOR_CODE}</td>
                    <td class="sub_head">${labelMap.AMOUNT_AFT_DISCOUNT}</td>
                    <td class="sub_head">${labelMap.DR_AMT}</td>
                    <td class="sub_head">${labelMap.RECEIPT_DATE}</td>
                    <td class="sub_head">${labelMap.ACTIVE}</td>
                    <td class="sub_head">${labelMap.EDIT}</td>
                </tr>
                <%
            con = new DBConnection();
            //con.connectToServer();
            con.connectToLocal();
            query = "SELECT a.LINE_NO, SUBSTRING(a.VERIFY_DATE,7,2)+'/'+SUBSTRING(a.VERIFY_DATE,5,2)+'/'+SUBSTRING(a.VERIFY_DATE,1,4) as EXECUTE_DATE, "+
            "a.ORDER_ITEM_CODE, a.DOCTOR_CODE,a.AMOUNT_AFT_DISCOUNT, a.DR_AMT, a.ACTIVE, a.ORDER_ITEM_DESCRIPTION, b.NAME_THAI, a.TRANSACTION_DATE, "+
            "a.INVOICE_TYPE,a.RECEIPT_NO, a.RECEIPT_DATE, " +
            "CASE WHEN a.RECEIPT_DATE = '' THEN '' ELSE SUBSTRING(a.RECEIPT_DATE,7,2)+'/'+SUBSTRING(a.RECEIPT_DATE,5,2)+'/'+SUBSTRING(a.RECEIPT_DATE,1,4) END as DATE_RECEIPT "+
            "FROM TRN_DAILY as a "+
            "LEFT JOIN DOCTOR as b on b.CODE=a.DOCTOR_CODE AND b.HOSPITAL_CODE = a.HOSPITAL_CODE WHERE a.HOSPITAL_CODE = '"
            + session.getAttribute("HOSPITAL_CODE").toString() + "' AND a.INVOICE_NO = '" + Util.formatHTMLString(request.getParameter("INVOICE_NO"), false) + "' ORDER BY a.LINE_NO, a.ACTIVE DESC, A.RECEIPT_DATE ASC ";
            rs = con.executeQuery(query);
            int i = 0;
            String activeIcon, linkEdit;
            while (rs != null && rs.next()) {
                activeIcon = "<img src=\"../../images/" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
                linkEdit = "<a href=\"javascript:EDIT_DETAIL_Click('" + rs.getString("LINE_NO") + "','" +rs.getString("TRANSACTION_DATE") +"','"+rs.getString("INVOICE_TYPE")+"','"+rs.getString("RECEIPT_NO")+"','"+rs.getString("RECEIPT_DATE")+"')\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"" + labelMap.get(LabelMap.EDIT) + "\" /></a>";
                %>                
                <tr>
                	<td class="row<%=i % 2%> alignLeft"><%= Util.formatHTMLString(rs.getString("EXECUTE_DATE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("LINE_NO"), true)%></td>
                    <td class="row<%=i % 2%> alignLeft"><%= Util.formatHTMLString(rs.getString("ORDER_ITEM_CODE"), true)%> : <%= Util.formatHTMLString(rs.getString("ORDER_ITEM_DESCRIPTION"), true)%></td>
                    <td class="row<%=i % 2%> alignLeft"><%= Util.formatHTMLString(rs.getString("DOCTOR_CODE"), true)%> : <%= Util.formatHTMLString(rs.getString("NAME_THAI"), true)%></td>
                    <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("AMOUNT_AFT_DISCOUNT"), true)%></td>
                    <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("DR_AMT"), true)%></td>
                    <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("DATE_RECEIPT"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= activeIcon%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= linkEdit%></td>
                </tr>
                <%
                i++;
            }
            if (rs != null) {
                rs.close();
            }
            con.Close();
                %>                
                <tr>
                    <th colspan="10" class="buttonBar">                        
                        <input type="button" id="NEW_DETAIL" name="NEW_DETAIL" class="button" value="${labelMap.NEW}" onclick="NEW_DETAIL_Click()" />
                    </th>
                </tr>
            </table>
<%
            }
/*
CREATE PROCEDURE procedure1  (IN parameter1 INTEGER)
BEGIN
  DECLARE variable1 CHAR(10);
  IF parameter1 = 17 THEN
    SET variable1 = 'birds';
  ELSE
    SET variable1 = 'beasts';
	END IF;
	INSERT INTO employees(name) VALUES (variable1);
END;
*/
%>                
        </form>
    </body>
</html>
<script language="javascript">
function pay_cash_click(){
	if(document.getElementById("PAY_BY_CASH").checked){
		document.mainForm.PAY_BY_CASH.value = 'Y';
	}else{
		document.mainForm.PAY_BY_CASH.value = 'N';
	}
}
function pay_cash_ar_click(){
	if(document.getElementById("PAY_BY_CASH_AR").checked){
		document.mainForm.PAY_BY_CASH_AR.value = 'Y';
	}else{
		document.mainForm.PAY_BY_CASH_AR.value = 'N';
	}
}
function pay_ar_click(){
	if(document.getElementById("PAY_BY_AR").checked){
		document.mainForm.PAY_BY_AR.value = 'Y';
	}else{
		document.mainForm.PAY_BY_AR.value = 'N';
	}
}
function pay_payor_click(){
	if(document.getElementById("PAY_BY_PAYOR").checked){
		document.mainForm.PAY_BY_PAYOR.value = 'Y';
	}else{
		document.mainForm.PAY_BY_PAYOR.value = 'N';
	}
}
function pay_doctor_click(){
	if(document.getElementById("PAY_BY_DOCTOR").checked){
		document.mainForm.PAY_BY_DOCTOR.value = 'Y';
	}else{
		document.mainForm.PAY_BY_DOCTOR.value = 'N';
	}
}
function changeTransactionType(){
    var e_select = document.mainForm.TRANSACTION_TYPE;
    var e_text = document.getElementById("RECEIPT_DEPARTMENT_LABEL");
    if(e_select.value=='REV'){
        e_text.innerHTML = e_text.innerHTML + '*';
        e_text.style.color = '#003399';
		document.getElementById("RECEIPT_NO").value = document.getElementById("INVOICE_NO").value;
		document.getElementById("RECEIPT_DATE").value = document.getElementById("INVOICE_DATE").value;
		document.getElementById("RECEIPT_TYPE_CODE").value = 'A';
		document.getElementById("PAY_BY_CASH").disabled = false;
		document.getElementById("PAY_BY_AR").disabled = false;
		document.getElementById("PAY_BY_DOCTOR").disabled = false;
		document.getElementById("PAY_BY_CASH_AR").disabled = false;
		document.getElementById("PAY_BY_PAYOR").disabled = false;
		document.getElementById("PAY_BY_CASH").checked = true;
		document.mainForm.PAY_BY_CASH.value = 'Y';
    }else{
        var str = e_text.innerHTML;
        e_text.innerHTML = str.replace('*','');
        e_text.style.color = '#333';
		document.getElementById("RECEIPT_NO").value = '';
		document.getElementById("RECEIPT_DATE").value = '';
		document.getElementById("RECEIPT_TYPE_CODE").value = 'AR';
		document.getElementById("RECEIPT_DEPARTMENT_CODE").value = '';
		document.getElementById("PAY_BY_CASH").disabled = true;
		document.getElementById("PAY_BY_AR").disabled = true;
		document.getElementById("PAY_BY_DOCTOR").disabled = true;
		document.getElementById("PAY_BY_CASH_AR").disabled = true;
		document.getElementById("PAY_BY_PAYOR").disabled = true;
		document.getElementById("PAY_BY_CASH").checked = false;
		document.mainForm.PAY_BY_CASH.value = 'N';
    }
}
</script>