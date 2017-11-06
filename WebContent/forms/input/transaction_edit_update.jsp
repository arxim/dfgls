<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.*"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>

<%@ include file="../../_global.jsp"%>

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

	LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE")
			.toString());

	request.setAttribute("labelMap", labelMap.getHashMap());

	DBConn conData = new DBConn();
	conData.setStatement();

	//
	// Process request
	//

	request.setCharacterEncoding("UTF-8");

	String item[];

	String cmd;
	DBConnection con = new DBConnection();
	con.connectToLocal();

	//Start Insert Data
	//Process

	String MaType = "", MaType_inactive = "", MaType_discount = "", MaType_change = "", TypeDisCount = "", InvoiceNumber = "", orderItem = "";

	//Data Transaction
	String arrDis[] = null, arrLineNo[] = null, arrDoctorCode[] = null, arrNote[] = null, arrDisAmount[] = null, arrInvoiceNumber[] = null, arrOrderItem[] = null, arrActiveTrn[] = null;
	String arrReceiptDate[] = null;
	
	if (request.getParameterValues("INVOICE_NUMBER[]") != null) {
		arrInvoiceNumber = request.getParameterValues("INVOICE_NUMBER[]");
	} 

	if (request.getParameterValues("DIS[]") != null) {
		arrDis = request.getParameterValues("DIS[]");
	}

	if (request.getParameterValues("DIS[]") != null) {
		arrLineNo = request.getParameterValues("LINE_NO_ARR[]");
	}

	if (request.getParameterValues("DOCTOR_CODE_ARR[]") != null) {
		arrDoctorCode = request.getParameterValues("DOCTOR_CODE_ARR[]");
	}

	if (request.getParameterValues("ORDER_ITEM_CODE[]") != null) {
		arrOrderItem = request.getParameterValues("ORDER_ITEM_CODE[]");
	}

	if (request.getParameterValues("NOTE_ARR[]") != null) {
		arrNote = request.getParameterValues("NOTE_ARR[]");
	}

	if (request.getParameterValues("ACTIVE_ARR[]") != null) {
		arrActiveTrn = request.getParameterValues("ACTIVE_ARR[]");
	}

	if (request.getParameterValues("AMOUNT_AFT_DISCOUNT_ARR[]") != null) {
		arrDisAmount = request.getParameterValues("AMOUNT_AFT_DISCOUNT_ARR[]");
	}
	
	if (request.getParameterValues("RECEIPT_DATE[]") != null) {
		arrReceiptDate = request.getParameterValues("RECEIPT_DATE[]");
	}
	
	String insertData = "", sqlData = "";

	String getLineNo = "", getNoteSave = "", getDis = "", getNote = "", getDoctorCode = "", getInvoiceNumber = "", getOrderItem = "" , getAtive = "";
	String getReceipt = "";

	double getOfAmount = 0;

	int dd = 0;

	System.out.println("arrLineNo.length= " + arrLineNo.length);
	
	for (int nd = 0; nd < arrLineNo.length; nd++) {
		
		dd = nd;
		dd++;
		getLineNo = arrLineNo[nd];
		getOrderItem = arrOrderItem[nd];
		getInvoiceNumber = arrInvoiceNumber[nd];
		getDoctorCode = arrDoctorCode[nd];
		getAtive =  arrActiveTrn[nd];
		getNote = arrNote[nd];
		getReceipt = arrReceiptDate[nd];
		
// 		System.out.println("getLineNo=" + getLineNo);		
// 		System.out.println("getInvoiceNumber=" + getInvoiceNumber);
// 		System.out.println("===============getLineNo=" + getLineNo + "=====================");
				
 				 System.out.println( nd + " < " +  getLineNo + " <> " + getAtive);
 				
					// arrDoctorCode = request.getParameterValues("DOCTOR_CODE_ARR["+ nd + "]");
					getDoctorCode = arrDoctorCode[nd];
					getOfAmount = Double.parseDouble(arrDisAmount[nd]);
					getNote = arrNote[nd];
					
					//======================================================================================	
					//Delete Data from table MA_TRN_DAILY

					String deleteData = "DELETE FROM MA_TRN_DAILY WHERE "
							+ " HOSPITAL_CODE='"
							+ session.getAttribute("HOSPITAL_CODE").toString()
							+ "' "
							+ " AND LINE_NO='"
							+ getLineNo
							+ "'"
							+ " AND INVOICE_NO='" + getInvoiceNumber+ "'";

					try {
						conData.insert(deleteData);
						conData.commitDB();
					} catch (Exception e) {
						System.out.println("DELETE Transaction inactive, discount, change Excepiton : " + e + "query=" + deleteData);
						conData.rollDB();
					}

					//QUERY DATA FROM TRN_DAILY
					sqlData = "SELECT INVOICE_NO, INVOICE_DATE , HN_NO, PAYOR_OFFICE_CODE, TRANSACTION_DATE, "
							+ " ORDER_ITEM_CODE, INVOICE_TYPE, DOCTOR_CODE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, "
							+ " AMOUNT_AFT_DISCOUNT, YYYY, MM, ACTIVE, LINE_NO, "
							+ " PATIENT_NAME, OLD_AMOUNT, DR_AMT, OLD_DR_AMT, DR_TAX_406, "
							+ " OLD_TAX_AMT, AMOUNT_BEF_WRITE_OFF, OLD_DR_AMT_BEF_WRITE_OFF, DR_AMT_BEF_WRITE_OFF, DR_TAX_406_BEF_WRITE_OFF  , RECEIPT_DATE"
							+ " FROM TRN_DAILY "
							+ " WHERE LINE_NO='"
							+ getLineNo
							+ "'"
							+ " AND INVOICE_NO='"
							+ getInvoiceNumber
							+ "'"
							+ " AND RECEIPT_DATE = '" + getReceipt + "'"
							+ " AND HOSPITAL_CODE='"
							+ session.getAttribute("HOSPITAL_CODE").toString() + "' ";

					
					String[][] arrData = conData.query(sqlData);

					double amount_bef_discount = 0, amount_of_discount = 0, amount_aft_discount = 0;
					double oldAmount = 0, drAmt = 0, oldDrAmt = 0, drTax406 = 0, oldTaxAmt = 0, amountBefWriteOff = 0, oldDrAmtBefWriteOff = 0, drAmtBefWriteOff = 0, drTax406BefWriteOff = 0;
					double oldAmountNew = 0, drAmtNew = 0, oldDrAmtNew = 0, drTax406New = 0, oldTaxAmtNew = 0, amountBefWriteOffNew = 0, oldDrAmtBefWriteOffNew = 0, drAmtBefWriteOffNew = 0, drTax406BefWriteOffNew = 0;
					String updateData = "", updateData1 = "";
					double getAftAmountBef = 0, percentDis = 0, percent_bef = 0, percent_total = 0, TotalAftAmount = 0;
					double getAftAmount = 0;
					String getActiveNew = "";
					boolean statusDis = false;
					String invoice_date = "";
					String receipt_date = "";

					if (arrData.length > 0) {

						String invoice_no = arrData[0][0];
						invoice_date = arrData[0][1];
						String hn_no = arrData[0][2];
						String payor_office_code = arrData[0][3];
						String transaction_date = arrData[0][4];
						String order_item_code = arrData[0][5];
						String invoice_type = arrData[0][6];
						String doctor_code = arrData[0][7];
						receipt_date = arrData[0][25];
						
						if (arrData[0][8] != null) {
							amount_bef_discount = Double.parseDouble(arrData[0][8]);
						}

						if (arrData[0][9] != null) {
							amount_of_discount = Double.parseDouble(arrData[0][9]);
						}

						if (arrData[0][10] != null) {
							amount_aft_discount = Double.parseDouble(arrData[0][10]);
						}

						String getYear = arrData[0][11];
						String getMonth = arrData[0][12];
						String getActive = arrData[0][13];
						String PatientName = arrData[0][15];

						if (arrData[0][16] != null) {
							oldAmount = Double.parseDouble(arrData[0][16]);
						}

						if (arrData[0][17] != null) {
							drAmt = Double.parseDouble(arrData[0][17]);
						}

						if (arrData[0][18] != null) {
							oldDrAmt = Double.parseDouble(arrData[0][18]);
						}

						if (arrData[0][19] != null) {
							drTax406 = Double.parseDouble(arrData[0][19]);
						}

						if (arrData[0][20] != null) {
							oldTaxAmt = Double.parseDouble(arrData[0][20]);
						}

						if (arrData[0][21] != null) {
							amountBefWriteOff = Double.parseDouble(arrData[0][21]);
						}

						if (arrData[0][22] != null) {
							oldDrAmtBefWriteOff = Double.parseDouble(arrData[0][22]);
						}

						if (arrData[0][23] != null) {
							drAmtBefWriteOff = Double.parseDouble(arrData[0][23]);
						}

						if (arrData[0][24] != null) {
							drTax406BefWriteOff = Double.parseDouble(arrData[0][24]);
						}

							//discount amount, change doctor code
							//หา % ที่ทำการลด
							//(amount_aft_discount*100)/amount_bef_discount

						
							getAftAmountBef = Double.parseDouble(JNumber.getSaveMoney(amount_bef_discount - getOfAmount));
							percentDis = Double.parseDouble(JNumber.getSaveMoney((getOfAmount * 100)/ amount_bef_discount));						
							
								insertData = "INSERT INTO MA_TRN_DAILY "
										+ " (HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, "
										+ " HN_NO, PAYOR_OFFICE_CODE, LINE_NO, "
										+ " TRANSACTION_DATE, ORDER_ITEM_CODE, INVOICE_TYPE, "
										+ " DOCTOR_CODE_OLD, DOCTOR_CODE_NEW, MA_TYPE, "
										+ " DIS_TYPE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT_OLD,  "
										+ " AMOUNT_OF_DISCOUNT_NEW, AMOUNT_AFT_DISCOUNT_OLD, AMOUNT_AFT_DISCOUNT_NEW, "
										+ " UPDATE_DATE, UPDATE_TIME, USER_ID, "
										+ " YYYY, MM, ACTIVE_OLD, "
										+ " ACTIVE_NEW, NOTE, DIS_PERCENT, "
										+ " PATIENT_NAME, AMOUNT_BEF_WRITE_OFF_OLD, AMOUNT_BEF_WRITE_OFF_NEW, "
										+ " DR_AMT_OLD, DR_AMT_NEW, OLD_DR_AMT_OLD, OLD_DR_AMT_NEW, "
										+ " DR_TAX_406_OLD, DR_TAX_406_NEW, OLD_AMOUNT_OLD, OLD_AMOUNT_NEW, "
										+ " OLD_DR_AMT_BEF_WRITE_OFF_OLD, OLD_DR_AMT_BEF_WRITE_OFF_NEW, "
										+ " DR_AMT_BEF_WRITE_OFF_OLD, DR_AMT_BEF_WRITE_OFF_NEW, "
										+ " OLD_TAX_AMT_OLD, OLD_TAX_AMT_NEW, "
										+ " DR_TAX_406_BEF_WRITE_OFF_OLD, DR_TAX_406_BEF_WRITE_OFF_NEW) "
										+ " VALUES('"
										+ session.getAttribute("HOSPITAL_CODE").toString()
										+ "', '"
										+ invoice_no
										+ "','"
										+ invoice_date
										+ "', "
										+ "'"
										+ hn_no
										+ "', '"
										+ payor_office_code
										+ "', '"
										+ getLineNo
										+ "', "
										+ "'"
										+ transaction_date
										+ "', '"
										+ order_item_code
										+ "', '"
										+ invoice_type
										+ "', "
										+ "'"
										+ doctor_code
										+ "', '"
										+ getDoctorCode
										+ "', '"
										+ "$$$"
										+ "', "
										+ "'', "
										+ amount_bef_discount
										+ ", "
										+ amount_of_discount
										+ ", "
										+ getOfAmount
										+ ", "
										+ amount_aft_discount
										+ ", "
										+ TotalAftAmount
										+ ", "
										+ "'"
										+ JDate.getDate()
										+ "', '"
										+ JDate.getTime()
										+ "', '"
										+ session.getAttribute("USER_ID").toString()
										+ "', "
										+ "'"
										+ getYear
										+ "', '"
										+ getMonth
										+ "', '"
										+ getActive
										+ "', "
										+ "'"
										+ getActiveNew
										+ "', '"
										+ getNoteSave
										+ "',"
										+ 0
										+ ", "
										+ "'"
										+ PatientName
										+ "', "
										+ amountBefWriteOff
										+ ", "
										+ amountBefWriteOffNew
										+ ", "
										+ drAmt
										+ ", "
										+ drAmtNew
										+ ", "
										+ oldDrAmt
										+ ", "
										+ oldDrAmtNew
										+ ", "
										+ drTax406
										+ ", "
										+ drTax406New
										+ ", "
										+ oldAmount
										+ ", "
										+ oldAmountNew
										+ ", "
										+ oldDrAmtBefWriteOff
										+ ", "
										+ oldDrAmtBefWriteOffNew
										+ ", "
										+ drAmtBefWriteOff
										+ ", "
										+ drAmtBefWriteOffNew
										+ ", "
										+ oldTaxAmt
										+ ", "
										+ oldTaxAmtNew
										+ ", "
										+ drTax406BefWriteOff
										+ ", "
										+ drTax406BefWriteOffNew + ")";

								try {
									
									conData.insert(insertData);
									conData.commitDB();
									
									String noteCondition = "";
								
									if(!arrNote[nd].equals("")){ 
										noteCondition  =  " , NOTE = '" + arrNote[nd] + "' ";
									}
									
									//UPDATE DATA TABLE TRN_DAILY
									updateData = "UPDATE TRN_DAILY SET"
						                       +" NOR_ALLOCATE_AMT = '0' "
						                       +", NOR_ALLOCATE_PCT = '0'"
						                       +", DR_AMT = '0'"
						                       +", DR_TAX_400 = '0'"
						                       +", DR_TAX_401 = '0'"
						                       +", DR_TAX_402 = '0'"
						                       +", DR_TAX_406 = '0'"
						                       +", TAX_TYPE_CODE = ''"
						                       +", DR_PREMIUM = '0'"
						                       +", HP_AMT = '0'"
						                       +", HP_PREMIUM = '0'"
						                       +", HP_TAX = '0'"
						                       +", COMPUTE_DAILY_DATE = ''"
						                       +", COMPUTE_DAILY_TIME = ''"
						                       +", COMPUTE_DAILY_USER_ID = ''"
						                       +", DOCTOR_CATEGORY_CODE = ''"
						                       +", EXCLUDE_TREATMENT = ''"
						                       +", PREMIUM_CHARGE_PCT = '0'"
						                       +", PREMIUM_REC_AMT = '0'"
						                       +", ORDER_ITEM_CATEGORY_CODE = ''"
											   +", DOCTOR_CODE='"+ arrDoctorCode[nd] + "' "
											   +" , ORDER_ITEM_CODE = '" + arrOrderItem[nd] + "'"
											   +" , AMOUNT_AFT_DISCOUNT  = "+ arrDisAmount[nd]
											   +" , ACTIVE = '" + arrActiveTrn[nd] + "'"
											   + noteCondition + " WHERE HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE").toString()
											   + "' AND INVOICE_NO='"
											   + arrInvoiceNumber[nd]
											   + "' AND LINE_NO='"
											   + arrLineNo[nd] + "' "
											   + " AND RECEIPT_DATE = '" + arrReceiptDate[nd] + "'"
									           + " AND TRANSACTION_DATE = '" + transaction_date + "' ";
									noteCondition = "";

									try {
										
										System.out.println("SQL UPDATE DATA : " +  updateData);
										conData.insert(updateData);
										conData.commitDB();
									
									} catch (Exception e) {
										System.out.println("Update Transaction inactive, discount, change Update TRN_DAILY Excepiton : "+ e + "query=" + updateData);
										conData.rollDB();
									}
									
								} catch (Exception e) {
									System.out.println("insert Transaction inactive, discount, change Insert MA_TRN_DAILY Excepiton : " + e + "query="+ insertData);
									conData.rollDB();
								}
					}
		}
	
	con.Close();
	session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/transaction_edit.jsp"));
	response.sendRedirect("../message.jsp");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
</head>
<body>
</body>
</html>