package df.bean.process;

import java.sql.SQLException;
import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;

public class ProcessSummaryMonthlyDF implements ProcessMaster{
	String hospitalCode, year, month, payDate, term, batchNo, endDate, userID;
	DBConnection conn;
    Batch batch;
    DBConn cdb;
        
    public ProcessSummaryMonthlyDF(String hospitalCode, String payDate, String year, String month, String user){
    	this.setUserID(user);
    	processSummaryMonthlyDF(hospitalCode, payDate, year, month);
    }
    public ProcessSummaryMonthlyDF(String hospitalCode, String payDate, String year, String month){
    	this.setUserID("");
    	processSummaryMonthlyDF(hospitalCode, payDate, year, month);
    }
    public void processSummaryMonthlyDF(String hospitalCode, String payDate, String year, String month){
		conn = new DBConnection();
		conn.connectToLocal();
        batch = new Batch(hospitalCode, this.conn);
    	this.hospitalCode = hospitalCode;
        this.payDate = payDate.length()> 8 ? JDate.saveDate(payDate) : payDate;
    	this.year = year.equals(null)||year.equals("")? batch.getYyyy() : year;
    	this.month = month.equals(null)||month.equals("")? batch.getMm() : month;
    	System.out.println(this.payDate.substring(0, 6)+"<pay year>"+this.year+this.month);
    	conn.Close();
    	if(this.payDate.substring(0, 6).equals(this.year+this.month)){
    		this.term = "1";
    		this.endDate = "15";
    	}else{
    		this.term = "2";
    		this.endDate = "31";
    	}
    	System.out.println(this.payDate+"<>"+this.term);
    }
    
    public static void main (String arg[]){
    	ProcessSummaryMonthlyDF pm = new ProcessSummaryMonthlyDF("000","20150125","2015","00","test");
    	pm.doBatchClose();
    	//pm.tranformDFData("00000000");
    }
    
    public void setPayDate(String payDate){
    	this.payDate = payDate;
    }
    private String monthlyProcessHalfMonthBAK(String endDate){
    	String sql = "";
    	String trnCon = "";
    	String termCon = "";
        if(this.term.equals("1")){
        	trnCon = "AND TRN_DAILY.GUARANTEE_TERM_YYYY = '' ";
        	termCon = "AND DOCTOR.PAYMENT_TIME = '2' AND ORDER_ITEM.PAYMENT_TIME = '2' ";
        }else{ /*empty value*/ }
    	sql =  "FROM " +
               "DOCTOR LEFT OUTER JOIN TRN_DAILY "+
               "ON DOCTOR.CODE = TRN_DAILY.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = TRN_DAILY.HOSPITAL_CODE "+
               "LEFT OUTER JOIN ORDER_ITEM "+
               "ON TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "+
               "WHERE " +
               "DOCTOR.HOSPITAL_CODE = '"+this.hospitalCode+"' AND (BATCH_NO = '' OR BATCH_NO IS NULL) "+
               //"AND IS_PAID = 'Y' "+
               "AND ((TRN_DAILY.TRANSACTION_DATE BETWEEN '"+this.year+this.month+"01' AND '"+this.year+this.month+endDate+"') OR ("+
               "TRN_DAILY.RECEIPT_DATE BETWEEN '"+this.year+this.month+"01' AND '"+this.year+this.month+endDate+"'))"+
               trnCon+termCon+
               "AND TRN_DAILY.YYYY = '"+this.year+"' AND TRN_DAILY.MM = '"+this.month+"' AND TRN_DAILY.ORDER_ITEM_ACTIVE = '1' AND DOCTOR.ACTIVE = '1'  AND TRN_DAILY.IS_PAID != 'N'";
         return sql;		
	}
	private String summaryMonthlyProcessHalfMonth(String endDate){
    	String sql = "";
    	String trnCon = "";
    	String termCon = "";
        if(this.term.equals("1")){
        	trnCon = "AND TRN_DAILY.GUARANTEE_TERM_YYYY = '' ";
        	termCon = "AND DOCTOR.PAYMENT_TIME = '2' AND ORDER_ITEM.PAYMENT_TIME = '2' ";
        }else{ /*empty value*/ }
    	sql =  "FROM " +
               "DOCTOR LEFT OUTER JOIN TRN_DAILY "+
               "ON DOCTOR.CODE = TRN_DAILY.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = TRN_DAILY.HOSPITAL_CODE "+
               "LEFT OUTER JOIN ORDER_ITEM "+
               "ON TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "+
               "WHERE " +
               "DOCTOR.HOSPITAL_CODE = '"+this.hospitalCode+"' AND (BATCH_NO = '' OR BATCH_NO IS NULL) "+
               //"AND IS_PAID = 'Y' "+
               "AND ((TRN_DAILY.TRANSACTION_DATE BETWEEN '"+this.year+this.month+"01' AND '"+this.year+this.month+endDate+"') OR ("+
               "TRN_DAILY.RECEIPT_DATE BETWEEN '"+this.year+this.month+"01' AND '"+this.year+this.month+endDate+"'))"+
               trnCon+termCon+
               "AND TRN_DAILY.YYYY = '"+this.year+"' AND TRN_DAILY.MM = '"+this.month+"' AND TRN_DAILY.ACTIVE = '1' AND TRN_DAILY.ORDER_ITEM_ACTIVE = '1' AND DOCTOR.ACTIVE = '1' "+
               "AND TRN_DAILY.IS_PAID != 'N'";
         return sql;		
	}
	private String summaryMonthlyProcess(String endDate){
    	String sql = "";
    	String trnCon = "";
    	String termCon = "";
        if(this.term.equals("1")){
        	trnCon = "AND TRN_DAILY.GUARANTEE_TERM_YYYY = '' ";
        	termCon = "AND DOCTOR.PAYMENT_TIME = '2' AND ORDER_ITEM.PAYMENT_TIME = '2' ";
        }else{ /*empty value*/ }
    	sql =  "FROM " +
               "DOCTOR LEFT OUTER JOIN TRN_DAILY "+
               "ON DOCTOR.CODE = TRN_DAILY.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = TRN_DAILY.HOSPITAL_CODE "+
               "LEFT OUTER JOIN ORDER_ITEM "+
               "ON TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "+
               "WHERE " +
               "DOCTOR.HOSPITAL_CODE = '"+this.hospitalCode+"' AND (TRN_DAILY.BATCH_NO = '' OR TRN_DAILY.BATCH_NO IS NULL) "+
               //"AND IS_PAID = 'Y' "+
               "AND ((TRN_DAILY.TRANSACTION_DATE BETWEEN '"+this.year+this.month+"01' AND '"+this.year+this.month+endDate+"') OR ("+
               "TRN_DAILY.RECEIPT_DATE BETWEEN '"+this.year+this.month+"01' AND '"+this.year+this.month+endDate+"') OR (TRN_DAILY.YYYY+TRN_DAILY.MM = '"+this.year+this.month+"'))"+
               trnCon+termCon+
               "AND TRN_DAILY.YYYY = '"+this.year+"' AND TRN_DAILY.MM = '"+this.month+"' AND TRN_DAILY.ACTIVE = '1' AND TRN_DAILY.ORDER_ITEM_ACTIVE = '1' AND DOCTOR.ACTIVE = '1' "+
               "AND TRN_DAILY.IS_PAID != 'N'";
         return sql;		
	}
	private String monthlyProcess(String endDate){
    	String sql = "";
    	String trnCon = "";
    	String termCon = "";
        if(this.term.equals("1")){
        	trnCon = "AND TRN_DAILY.GUARANTEE_TERM_YYYY = '' ";
        	termCon = "AND DOCTOR.PAYMENT_TIME = '2' AND ORDER_ITEM.PAYMENT_TIME = '2' ";
        }else{ /*empty value*/ }
    	sql =  "FROM " +
               "DOCTOR LEFT OUTER JOIN TRN_DAILY "+
               "ON DOCTOR.CODE = TRN_DAILY.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = TRN_DAILY.HOSPITAL_CODE "+
               "LEFT OUTER JOIN ORDER_ITEM "+
               "ON TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "+
               "WHERE " +
               "DOCTOR.HOSPITAL_CODE = '"+this.hospitalCode+"' AND (TRN_DAILY.BATCH_NO = '' OR TRN_DAILY.BATCH_NO IS NULL) "+
               //"AND IS_PAID = 'Y' "+
               "AND ((TRN_DAILY.TRANSACTION_DATE BETWEEN '"+this.year+this.month+"01' AND '"+this.year+this.month+endDate+"') OR ("+
               "TRN_DAILY.RECEIPT_DATE BETWEEN '"+this.year+this.month+"01' AND '"+this.year+this.month+endDate+"') OR (TRN_DAILY.YYYY+TRN_DAILY.MM = '"+this.year+this.month+"'))"+
               trnCon+termCon+
               "AND TRN_DAILY.YYYY = '"+this.year+"' AND TRN_DAILY.MM = '"+this.month+"' AND TRN_DAILY.ORDER_ITEM_ACTIVE = '1' AND DOCTOR.ACTIVE = '1' AND TRN_DAILY.IS_PAID != 'N' ";
         return sql;		
	}
	
	private String tranformDFData(String endDate){
    	String sql = "";
    	sql =  "INSERT INTO TRN_PAYMENT ("
    			+ " HOSPITAL_CODE,"
    			+ " INVOICE_NO,"
    			+ " INVOICE_DATE,"
    			+ " RECEIPT_NO,"
    			+ " RECEIPT_DATE,"
    			+ " TRANSACTION_DATE,"
    			+ " HN_NO,"
    			+ " PATIENT_NAME,"
    			+ " EPISODE_NO,"
    			+ " NATIONALITY_CODE,"
    			+ " NATIONALITY_DESCRIPTION,"
    			+ " PAYOR_OFFICE_CODE,"
    			+ " PAYOR_OFFICE_NAME,"
    			+ " TRANSACTION_MODULE,"
    			+ " TRANSACTION_TYPE,"
    			+ " PAYOR_OFFICE_CATEGORY_CODE,"
    			+ " PAYOR_OFFICE_CATEGORY_DESCRIPTION,"
    			+ " IS_WRITE_OFF,"
    			+ " LINE_NO,"
    			+ " ADMISSION_TYPE_CODE,"
    			+ " PATIENT_DEPARTMENT_CODE,"
    			+ " PATIENT_LOCATION_CODE,"
    			+ " RECEIPT_DEPARTMENT_CODE,"
    			+ " RECEIPT_LOCATION_CODE,"
    			+ " DOCTOR_DEPARTMENT_CODE,"
    			+ " ORDER_ITEM_CODE,"
    			+ " ORDER_ITEM_DESCRIPTION,"
    			+ " DOCTOR_CODE,"
    			+ " VERIFY_DATE,"
    			+ " VERIFY_TIME,"
    			+ " DOCTOR_EXECUTE_CODE,"
    			+ " EXECUTE_DATE,"
    			+ " EXECUTE_TIME,"
    			+ " DOCTOR_RESULT_CODE,"
    			+ " OLD_DOCTOR_CODE,"
    			+ " RECEIPT_TYPE_CODE,"
    			+ " AMOUNT_BEF_DISCOUNT,"
    			+ " AMOUNT_OF_DISCOUNT,"
    			+ " AMOUNT_AFT_DISCOUNT,"
    			+ " AMOUNT_BEF_WRITE_OFF,"
    			+ " INV_IS_VOID,"
    			+ " REC_IS_VOID,"
    			+ " UPDATE_DATE,"
    			+ " UPDATE_TIME,"
    			+ " USER_ID,"
    			+ " BATCH_NO,"
    			+ " YYYY,"
    			+ " MM,"
    			+ " DD,"
    			+ " NOR_ALLOCATE_AMT,"
    			+ " NOR_ALLOCATE_PCT,"
    			+ " DR_AMT,"
    			+ " OLD_DR_AMT,"
    			+ " DR_TAX_400,"
    			+ " DR_TAX_401,"
    			+ " DR_TAX_402,"
    			+ " DR_TAX_406,"
    			+ " TAX_TYPE_CODE,"
    			+ " DR_PREMIUM,"
    			+ " GUARANTEE_PAID_AMT,"
    			+ " GUARANTEE_AMT,"
    			+ " GUARANTEE_CODE,"
    			+ " GUARANTEE_DR_CODE,"
    			+ " GUARANTEE_TYPE,"
    			+ " GUARANTEE_DATE_TIME,"
    			+ " GUARANTEE_TERM_MM,"
    			+ " GUARANTEE_TERM_YYYY,"
    			+ " GUARANTEE_NOTE,"
    			+ " IS_GUARANTEE,"
    			+ " HP_AMT,"
    			+ " HP_PREMIUM,"
    			+ " HP_TAX,"
    			+ " COMPUTE_DAILY_DATE,"
    			+ " COMPUTE_DAILY_TIME,"
    			+ " COMPUTE_DAILY_USER_ID,"
    			+ " DOCTOR_CATEGORY_CODE,"
    			+ " EXCLUDE_TREATMENT,"
    			+ " PREMIUM_CHARGE_PCT,"
    			+ " PREMIUM_REC_AMT,"
    			+ " ACTIVE,"
    			+ " INVOICE_TYPE,"
    			+ " TOTAL_BILL_AMOUNT,"
    			+ " TOTAL_DR_REC_AMOUNT,"
    			+ " OLD_AMOUNT,"
    			+ " PAY_BY_CASH,"
    			+ " PAY_BY_AR,"
    			+ " PAY_BY_DOCTOR,"
    			+ " PAY_BY_PAYOR,"
    			+ " PAY_BY_CASH_AR,"
    			+ " IS_PAID,"
    			+ " ORDER_ITEM_ACTIVE,"
    			+ " ORDER_ITEM_CATEGORY_CODE,"
    			+ " WRITE_OFF_BILL_AMT,"
    			+ " WRITE_OFF_RECEIPT_AMT,"
    			+ " OLD_DR_AMT_BEF_WRITE_OFF,"
    			+ " DR_AMT_BEF_WRITE_OFF,"
    			+ " DR_PREMIUM_BEF_WRITE_OFF,"
    			+ " HP_AMT_BEF_WRITE_OFF,"
    			+ " HP_PREMIUM_WRITE_OFF,"
    			+ " OLD_TAX_AMT,"
    			+ " DR_TAX_406_BEF_WRITE_OFF,"
    			+ " TAX_FROM_ALLOCATE,"
    			+ " IS_GUARANTEE_FROM_ALLOC,"
    			+ " IS_ONWARD,"
    			+ " IS_PARTIAL,"
    			+ " DR_AMT_BEF_PARTIAL,"
    			+ " DR_TAX_BEF_PARTIAL,"
    			+ " IS_DISCHARGE_SUMMARY,"
    			+ " AMT_BEF_PARTIAL,"
    			+ " DOCTOR_PRIVATE_CODE,"
    			+ " NOTE,"
    			+ " SEQ_STEP,"
    			+ " PAYMENT_DATE,"
    			+ " PAYMENT_TERM) "
    			
    			+" SELECT  TRN_DAILY.HOSPITAL_CODE,"
				+" TRN_DAILY.INVOICE_NO,"
				+" TRN_DAILY.INVOICE_DATE,"
				+" TRN_DAILY.RECEIPT_NO,"
				+" TRN_DAILY.RECEIPT_DATE,"
				+" TRN_DAILY.TRANSACTION_DATE,"
				+" TRN_DAILY.HN_NO,"
				+" TRN_DAILY.PATIENT_NAME,"
				+" TRN_DAILY.EPISODE_NO,"
				+" TRN_DAILY.NATIONALITY_CODE,"
				+" TRN_DAILY.NATIONALITY_DESCRIPTION,"
				+" TRN_DAILY.PAYOR_OFFICE_CODE,"
				+" TRN_DAILY.PAYOR_OFFICE_NAME,"
				+" TRN_DAILY.TRANSACTION_MODULE,"
				+" TRN_DAILY.TRANSACTION_TYPE,"
				+" TRN_DAILY.PAYOR_OFFICE_CATEGORY_CODE,"
				+" TRN_DAILY.PAYOR_OFFICE_CATEGORY_DESCRIPTION,"
				+" TRN_DAILY.IS_WRITE_OFF,"
				+" TRN_DAILY.LINE_NO,"
				+" TRN_DAILY.ADMISSION_TYPE_CODE,"
				+" TRN_DAILY.PATIENT_DEPARTMENT_CODE,"
				+" TRN_DAILY.PATIENT_LOCATION_CODE,"
				+" TRN_DAILY.RECEIPT_DEPARTMENT_CODE,"
				+" TRN_DAILY.RECEIPT_LOCATION_CODE,"
				+" TRN_DAILY.DOCTOR_DEPARTMENT_CODE,"
				+" TRN_DAILY.ORDER_ITEM_CODE,"
				+" TRN_DAILY.ORDER_ITEM_DESCRIPTION,"
				+" TRN_DAILY.DOCTOR_CODE,"
				+" TRN_DAILY.VERIFY_DATE,"
				+" TRN_DAILY.VERIFY_TIME,"
				+" TRN_DAILY.DOCTOR_EXECUTE_CODE,"
				+" TRN_DAILY.EXECUTE_DATE,"
				+" TRN_DAILY.EXECUTE_TIME,"
				+" TRN_DAILY.DOCTOR_RESULT_CODE,"
				+" TRN_DAILY.OLD_DOCTOR_CODE,"
				+" TRN_DAILY.RECEIPT_TYPE_CODE,"
				+" TRN_DAILY.AMOUNT_BEF_DISCOUNT,"
				+" TRN_DAILY.AMOUNT_OF_DISCOUNT,"
				+" TRN_DAILY.AMOUNT_AFT_DISCOUNT,"
				+" TRN_DAILY.AMOUNT_BEF_WRITE_OFF,"
				+" TRN_DAILY.INV_IS_VOID,"
				+" TRN_DAILY.REC_IS_VOID,"
				+" TRN_DAILY.UPDATE_DATE,"
				+" TRN_DAILY.UPDATE_TIME,"
				+" TRN_DAILY.USER_ID,"
				+" TRN_DAILY.BATCH_NO,"
				+" TRN_DAILY.YYYY,"
				+" TRN_DAILY.MM,"
				+" TRN_DAILY.DD,"
				+" TRN_DAILY.NOR_ALLOCATE_AMT,"
				+" TRN_DAILY.NOR_ALLOCATE_PCT,"
				+" TRN_DAILY.DR_AMT,"
				+" TRN_DAILY.OLD_DR_AMT,"
				+" TRN_DAILY.DR_TAX_400,"
				+" TRN_DAILY.DR_TAX_401,"
				+" TRN_DAILY.DR_TAX_402,"
				+" TRN_DAILY.DR_TAX_406,"
				+" TRN_DAILY.TAX_TYPE_CODE,"
				+" TRN_DAILY.DR_PREMIUM,"
				+" TRN_DAILY.GUARANTEE_PAID_AMT,"
				+" TRN_DAILY.GUARANTEE_AMT,"
				+" TRN_DAILY.GUARANTEE_CODE,"
				+" TRN_DAILY.GUARANTEE_DR_CODE,"
				+" TRN_DAILY.GUARANTEE_TYPE,"
				+" TRN_DAILY.GUARANTEE_DATE_TIME,"
				+" TRN_DAILY.GUARANTEE_TERM_MM,"
				+" TRN_DAILY.GUARANTEE_TERM_YYYY,"
				+" TRN_DAILY.GUARANTEE_NOTE,"
				+" TRN_DAILY.IS_GUARANTEE,"
				+" TRN_DAILY.HP_AMT,"
				+" TRN_DAILY.HP_PREMIUM,"
				+" TRN_DAILY.HP_TAX,"
				+" TRN_DAILY.COMPUTE_DAILY_DATE,"
				+" TRN_DAILY.COMPUTE_DAILY_TIME,"
				+" TRN_DAILY.COMPUTE_DAILY_USER_ID,"
				+" TRN_DAILY.DOCTOR_CATEGORY_CODE,"
				+" TRN_DAILY.EXCLUDE_TREATMENT,"
				+" TRN_DAILY.PREMIUM_CHARGE_PCT,"
				+" TRN_DAILY.PREMIUM_REC_AMT,"
				+" TRN_DAILY.ACTIVE,"
				+" TRN_DAILY.INVOICE_TYPE,"
				+" TRN_DAILY.TOTAL_BILL_AMOUNT,"
				+" TRN_DAILY.TOTAL_DR_REC_AMOUNT,"
				+" TRN_DAILY.OLD_AMOUNT,"
				+" TRN_DAILY.PAY_BY_CASH,"
				+" TRN_DAILY.PAY_BY_AR,"
				+" TRN_DAILY.PAY_BY_DOCTOR,"
				+" TRN_DAILY.PAY_BY_PAYOR,"
				+" TRN_DAILY.PAY_BY_CASH_AR,"
				+" TRN_DAILY.IS_PAID,"
				+" TRN_DAILY.ORDER_ITEM_ACTIVE,"
				+" TRN_DAILY.ORDER_ITEM_CATEGORY_CODE,"
				+" TRN_DAILY.WRITE_OFF_BILL_AMT,"
				+" TRN_DAILY.WRITE_OFF_RECEIPT_AMT,"
				+" TRN_DAILY.OLD_DR_AMT_BEF_WRITE_OFF,"
				+" TRN_DAILY.DR_AMT_BEF_WRITE_OFF,"
				+" TRN_DAILY.DR_PREMIUM_BEF_WRITE_OFF,"
				+" TRN_DAILY.HP_AMT_BEF_WRITE_OFF,"
				+" TRN_DAILY.HP_PREMIUM_WRITE_OFF,"
				+" TRN_DAILY.OLD_TAX_AMT,"
				+" TRN_DAILY.DR_TAX_406_BEF_WRITE_OFF,"
				+" TRN_DAILY.TAX_FROM_ALLOCATE,"
				+" TRN_DAILY.IS_GUARANTEE_FROM_ALLOC,"
				+" TRN_DAILY.IS_ONWARD,"
				+" TRN_DAILY.IS_PARTIAL,"
				+" TRN_DAILY.DR_AMT_BEF_PARTIAL,"
				+" TRN_DAILY.DR_TAX_BEF_PARTIAL,"
				+" TRN_DAILY.IS_DISCHARGE_SUMMARY,"
				+" TRN_DAILY.AMT_BEF_PARTIAL,"
				+" TRN_DAILY.DOCTOR_PRIVATE_CODE,"
				+" TRN_DAILY.NOTE,"
				+" TRN_DAILY.SEQ_STEP, '"+this.payDate+"', '"+this.term+"' "+ (this.term.equals("1") ? monthlyProcessHalfMonthBAK(endDate) : monthlyProcess(endDate));
    	System.out.println(sql);
        return sql;
    }
	
	private String doTransferDoctorPaymentReport(){
		return	"INSERT INTO REPORT_DF_PAYMENT "+
				"SELECT TD.HOSPITAL_CODE, '"+this.year+this.month+"' AS TERM, TD.DOCTOR_CODE, TD.VERIFY_DATE, "+
				"CASE WHEN TD.IS_ONWARD = 'Y' THEN '' ELSE TD.INVOICE_DATE END AS INVOICE_DATE, "+
				"TD.HN_NO, TD.PATIENT_NAME, TD.ORDER_ITEM_DESCRIPTION, TD.AMOUNT_AFT_DISCOUNT, TD.AMOUNT_OF_DISCOUNT, TD.AMOUNT_AFT_DISCOUNT-TD.DR_AMT AS HOSPITAL_AMT, "+
				"CASE WHEN TD.YYYY+TD.MM = '' OR (TD.YYYY+TD.MM != '' AND IS_DISCHARGE_SUMMARY = 'N') THEN 0 ELSE CASE WHEN TD.ACTIVE = '1' THEN TD.DR_AMT ELSE 0 END END AS DR_AMT, "+
				"CASE WHEN (TD.YYYY+TD.MM != '' AND IS_DISCHARGE_SUMMARY = 'N') THEN '' ELSE TD.YYYY+TD.MM END PAY_TERM, "+
				"CASE WHEN TD.YYYY+TD.MM = '' OR (TD.YYYY+TD.MM != '' AND IS_DISCHARGE_SUMMARY = 'N') THEN CASE WHEN TD.ACTIVE = '1' THEN TD.DR_AMT ELSE 0 END ELSE 0 END AS OUTSTANDING_AMT, "+
				"DATEDIFF(day, TD.VERIFY_DATE, '"+this.payDate+"') AS DiffDate, "+
				"CASE WHEN TD.IS_DISCHARGE_SUMMARY = 'N' THEN 'N' ELSE "+
				"CASE WHEN TD.IS_DISCHARGE_SUMMARY = 'Y' THEN 'Y' ELSE '' END END AS DISCHARGE_SUMMARY, TD.EPISODE_NO, "+
				"CASE WHEN TD.IS_ONWARD = 'Y' THEN '' ELSE TD.INVOICE_NO END AS INVOICE_NO, "+
				"CASE WHEN MTD.NOTE != '' THEN MTD.NOTE ELSE CASE WHEN TD.IS_ONWARD = 'Y' THEN 'ON ADMISSION' ELSE '-' END "+
				"END AS NOTE, TD.LINE_NO, TD.ACTIVE, TD.IS_ONWARD "+
				"FROM TRN_DAILY TD "+
				"LEFT OUTER JOIN MA_TRN_DAILY MTD ON TD.INVOICE_NO = MTD.INVOICE_NO AND TD.LINE_NO = MTD.LINE_NO "+
				"AND MTD.UPDATE_DATE BETWEEN '"+this.year+this.month+"10' AND '"+this.payDate+"' "+
				"WHERE TD.HOSPITAL_CODE = '"+this.hospitalCode+"' "+
				"AND (TD.ACTIVE = '1' OR (TD.ACTIVE = '0' AND MTD.ACTIVE_OLD = '1')) "+
				"AND (TD.YYYY+TD.MM = '"+this.year+this.month+"' OR TD.YYYY+TD.MM = '' OR (TD.YYYY+TD.MM != '' AND IS_DISCHARGE_SUMMARY = 'N')) "+
				"AND TD.DOCTOR_CODE NOT LIKE '9999%' "+
				"AND TD.TRANSACTION_DATE < '"+this.payDate.substring(0, 6)+"00' "+
				"ORDER BY TD.TRANSACTION_DATE";
	}
	
	private String doProcessAdjustPayment(){
		String con = "";
		if(this.term.equals("1")){
	        con = "AND DOCTOR.PAYMENT_TIME = '2' ";
	    }
		return
		"UPDATE TRN_EXPENSE_DETAIL SET "+
		"PAYMENT_DATE = '"+this.payDate+"', PAYMENT_TERM = '"+this.term+"' "+
		"FROM TRN_EXPENSE_DETAIL " +
        "LEFT OUTER JOIN DOCTOR " +
        "ON TRN_EXPENSE_DETAIL.DOCTOR_CODE = DOCTOR.CODE AND TRN_EXPENSE_DETAIL.HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE "+
        "WHERE "+
        "TRN_EXPENSE_DETAIL.HOSPITAL_CODE = '"+this.hospitalCode+"' "+
        "AND TRN_EXPENSE_DETAIL.YYYY = '"+this.year+"' " +
        "AND TRN_EXPENSE_DETAIL.MM = '"+this.month+"' "+ con +
        "AND (BATCH_NO = '' OR BATCH_NO IS NULL)";
	}
	private String doTransactionClose(){
		String test = "UPDATE TRN_DAILY SET TRN_DAILY.BATCH_NO = '" +this.year+this.month+ "' "+
		//"WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND YYYY = '"+this.year+"' AND MM = '"+this.month+"'";
        this.monthlyProcess(this.endDate);
		System.out.println(test);
		return test;
	}
	private String doTransactionPaymentClose(){
		return
		"UPDATE TRN_PAYMENT SET BATCH_NO = '" +this.year+this.month+ "', "+
		"PAYMENT_TERM = '"+this.term+"' "+
		"WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' "+
        "AND YYYY = '"+this.year+"' " +
        "AND MM = '"+this.month+"' "+
        "AND PAYMENT_DATE = '"+this.payDate+"' "+
		"";
	}
	private String doSummaryClose(){
		return
		"UPDATE SUMMARY_PAYMENT SET BATCH_NO = '" +this.year+this.month+ "', "+
		"PAYMENT_TERM = '"+this.term+"' "+
		"FROM SUMMARY_PAYMENT "+
		"WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' "+
        "AND YYYY = '"+this.year+"' " +
        "AND MM = '"+this.month+"' "+
        "AND PAYMENT_DATE = '"+this.payDate+"' "+
		"";
	}
	private String doFirstTermAdjustClose(){
		return
		"UPDATE TRN_EXPENSE_DETAIL SET BATCH_NO = '" +this.year+this.month+ "', "+
		"PAYMENT_DATE = '"+this.payDate+"', PAYMENT_TERM = '"+this.term+"' "+
		"FROM TRN_EXPENSE_DETAIL " +
        "LEFT OUTER JOIN DOCTOR " +
        "ON TRN_EXPENSE_DETAIL.DOCTOR_CODE = DOCTOR.CODE AND TRN_EXPENSE_DETAIL.HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE "+
        "WHERE "+
        "TRN_EXPENSE_DETAIL.HOSPITAL_CODE = '"+this.hospitalCode+"' "+
        "AND TRN_EXPENSE_DETAIL.YYYY = '"+this.year+"' " +
        "AND TRN_EXPENSE_DETAIL.MM = '"+this.month+"' "+
        "AND DOCTOR.PAYMENT_TIME = '2' AND BATCH_NO = ''"+
		"";
	}

    @Override
    public boolean doProcess() {
		boolean status = true;
    	try {
        	cdb = new DBConn();
			cdb.setStatement();
			conn = new DBConnection();
			conn.connectToLocal();
	        batch = new Batch(this.hospitalCode, this.conn);
	        this.batchNo = batch.getBatchNo();
	        if(term.equals("1")){
	        	//First Term Process
	            System.out.println("Set Prepare : "+setPrepareTransactionGuarantee()); //this is process
	            System.out.println("Set Transaction : "+setTransactionGuarantee()); //this is process
	            System.out.println("Transform DF Data Records Result : "+conn.executeUpdate(tranformDFData(this.endDate)));
	            System.out.println("SQL Info SUMMARY_PAYMENT : " +  SummaryHalfMonth());
	            System.out.println("Summary Payment Records Result : "+conn.executeUpdate(SummaryHalfMonth()));
	        
	        }else{
	    		System.out.println("Copy Transaction Records Result : "+conn.executeUpdate(tranformDFData(this.endDate)));
	    		System.out.println("Info Month End : " + SummaryMonthEnd());
	    		System.out.println("Monthly Payment Records Result "+hospitalCode+": "+conn.executeUpdate(SummaryMonthEnd()));
	        }
	        
	        System.out.println("Update Payment Term Adjust : "+conn.executeUpdate(doProcessAdjustPayment()));

    	} catch (SQLException e) {
			System.out.println(e);
			status = false;
		} finally{
			conn.Close();
			cdb.closeDB("");
		}
		return status;
	}
	@Override
    public boolean doBatchClose() {
		boolean status = true;
    	try {
        	cdb = new DBConn();
			cdb.setStatement();
			conn = new DBConnection();
			conn.connectToLocal();
	        batch = new Batch(hospitalCode, conn);
	        this.batchNo = batch.getBatchNo();
	        this.year = batch.getYyyy();
	        this.month = batch.getMm();
	        
	        if(term.equals("1")){
	        	System.out.println("Initial First Term doBatchClose");
	        	//TRN_DAILY CLOSE
	        	//CLEAR GUARANTEE REMARK
	        	//TRN_EXPENSE_DETAIL CLOSE
	        	//SUMMARY_PAYMENT CLOSE
	        	//TRN_PAYMENT CLOSE
	        	
	            conn.executeUpdate(doTransactionClose());
	            conn.executeUpdate(rollbackTrnGuarantee());
	            conn.executeUpdate(doFirstTermAdjustClose());
	            conn.executeUpdate(doSummaryClose());
	            conn.executeUpdate(doTransactionPaymentClose());
	           
	        }else{
	        	System.out.println("Initial Month End doBatchClose");
	        	System.out.println(doTransferDoctorPaymentReport());
	        	
	        	conn.beginTrans();
	        	if(this.hospitalCode.equals("00001")){
		        	if(conn.executeUpdate(this.doTransferDoctorPaymentReport())>0){
			            conn.executeUpdate(doTransactionClose()); //TRN_DAILY CLOSE
			            conn.executeUpdate(doTransactionPaymentClose()); //TRN_PAYMENT CLOSE
			        	conn.executeUpdate(doMonthEndAdjustClose()); //TRN_EXPENSE_DETAIL CLOSE
			        	conn.executeUpdate(doSummaryClose()); //SUMMARY_PAYMENT CLOSE		        		
		        	}
	        	}else{
		            conn.executeUpdate(doTransactionClose()); //TRN_DAILY CLOSE
		            conn.executeUpdate(doTransactionPaymentClose()); //TRN_PAYMENT CLOSE
		        	conn.executeUpdate(doMonthEndAdjustClose()); //TRN_EXPENSE_DETAIL CLOSE
		        	conn.executeUpdate(doSummaryClose()); //SUMMARY_PAYMENT CLOSE
	        	}
	        	//STP_GUARANTEE
	        	try{ //MOVE X-RAY
		        	if(conn.executeUpdate(doMoveXray())>0){
			        	conn.executeUpdate(doDeleteXray());	        		        		
		        	}
	        	}catch(Exception e){
	        		System.out.println("Error while moving X-ray Management to TRN_DAILY_LOG");
	        		System.out.println("Error : "+e);
	        		System.out.println("Statement : "+doMoveXray());
	        	}
	        	
	        	//MOVE ONWARD
	        	try{
		        	if(conn.executeUpdate(doMoveOnward())>0){
			        	conn.executeUpdate(doDeleteOnward());	        		        		
		        	}
	        	}catch(Exception e){
	        		System.out.println("Error while moving Onward to TRN_ONWARD");
	        		System.out.println("Error : " + e);
	        		System.out.println("Statement : "+doMoveOnward());
	        	}
	        	
	        	//MOVE TRANSACTION PAYMENT (EDIT UNPAID AS OF DATE REPORT BEFORE)
	        	//MOVE TRANSACTION IN ACTIVE
	        	//CREATE BATCH
	        	batch.setCloseByUserId(this.userID);
	            if(batch.closeBATCH() && batch.createBATCH()){
	                conn.commitTrans();
	            }else{
	            	status = false;
	            }
	        }

		} catch (SQLException e) {
			status = false;
		} finally{
			conn.Close();
			cdb.closeDB("");
		}
    	return status;
	}
	@Override
	public boolean doRollback() {
		System.out.println("Process Monthly Rollback");
		boolean status = true;
    	try {
        	cdb = new DBConn();
			cdb.setStatement();
			conn = new DBConnection();
			conn.connectToLocal();
	        batch = new Batch(hospitalCode, conn);
	        this.batchNo = batch.getBatchNo();
	    	conn.executeUpdate(rollbackSumMonth());
	    	conn.executeUpdate(rollbackTrnPayment());
	        if(this.term.equals("1")){
		    	conn.executeUpdate(rollbackTrnGuarantee());	        	
	        }else{ /*empty*/}
		} catch (SQLException e) {
			System.out.println("Rollback Monthly Process : "+e);
			status = false;
		} finally{
			conn.Close();
			cdb.closeDB("");
		}
    	return status;
	}

	private String rollbackSumMonth(){
    	return "DELETE FROM SUMMARY_PAYMENT WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND "+
    		   "PAYMENT_DATE = '"+this.payDate+"' AND (BATCH_NO = '' OR BATCH_NO IS NULL)";
    }
    private String rollbackTrnPayment(){
    	return "DELETE FROM TRN_PAYMENT WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND "+
		       "PAYMENT_DATE = '"+this.payDate+"' AND (BATCH_NO = '' OR BATCH_NO IS NULL)";
    }
    private String rollbackTrnGuarantee(){
        return "UPDATE TRN_DAILY SET GUARANTEE_CODE = '', "+
        	   "GUARANTEE_DR_CODE = '', GUARANTEE_TERM_MM = '', GUARANTEE_TERM_YYYY = '' "+
        	   "WHERE (TRANSACTION_DATE BETWEEN '"+this.batchNo+"01' AND '"+this.batchNo+"31') AND "+
        	   "(BATCH_NO = '' OR BATCH_NO IS NULL) AND HOSPITAL_CODE = '" + this.hospitalCode + "'";
    }
    
    /**
     * First Term Method
     * 
     */
	private boolean setPrepareTransactionGuarantee(){
    	boolean status = true;
    	String upOrderItem = 
    	"UPDATE " +
    	"TRN_DAILY "+
    	"SET " +
    	"TRN_DAILY.ORDER_ITEM_ACTIVE = ORDER_ITEM.ACTIVE, "+
    	"TRN_DAILY.IS_GUARANTEE = ORDER_ITEM.IS_GUARANTEE "+
    	"FROM " +
    	"TRN_DAILY JOIN ORDER_ITEM ON " +
    	"TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND "+
    	"TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "+
    	"WHERE " +
    	"TRN_DAILY.HOSPITAL_CODE = '"+this.hospitalCode+"' AND "+
    	"(TRANSACTION_DATE BETWEEN '"+this.year+""+this.month+"01' AND '"+this.year+""+this.month+"15')";
    	
    	String upDay = 
    	"UPDATE STP_GUARANTEE "+
    	"SET GUARANTEE_SOURCE = CASE WHEN DOCTOR.GUARANTEE_SOURCE = 'DF' THEN 'DF' ELSE 'FL' END, "+
    	"GUARANTEE_DAY = CASE WHEN STP_GUARANTEE.GUARANTEE_DAY = '' THEN "+
    	"CASE WHEN DOCTOR.GUARANTEE_DAY = '' THEN HOSPITAL.GUARANTEE_DAY "+
    	"ELSE DOCTOR.GUARANTEE_DAY END ELSE STP_GUARANTEE.GUARANTEE_DAY END "+
    	"FROM STP_GUARANTEE " +
    	"LEFT JOIN DOCTOR ON STP_GUARANTEE.GUARANTEE_DR_CODE = DOCTOR.GUARANTEE_DR_CODE AND "+
    	"STP_GUARANTEE.HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE "+
    	"LEFT JOIN HOSPITAL ON STP_GUARANTEE.HOSPITAL_CODE = HOSPITAL.CODE "+
    	"WHERE YYYY = '"+this.year+"' AND MM = '"+this.month+"' "+
    	"AND (STP_GUARANTEE.GUARANTEE_SOURCE = '' OR STP_GUARANTEE.GUARANTEE_SOURCE IS NULL) "+
    	"AND GUARANTEE_TYPE_CODE != 'STP' "+
    	"AND STP_GUARANTEE.HOSPITAL_CODE = '"+this.hospitalCode+"' "+
    	"AND STP_GUARANTEE.ACTIVE = '1'";
        try{
            cdb.insert(upOrderItem);
            cdb.insert(upDay);
            cdb.commitDB();
            System.out.println("Commit setGuaranteePrepareCondition");
        }catch(Exception e){
        	status = false;
        	//TRN_Error.setUser_name(this.user_id);
        	//TRN_Error.setHospital_code(hospital_code);
            //TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
        }finally{
        	//cdb.closeStatement("");
        }
    	return status;
    }
    private boolean setTransactionGuarantee(){
        boolean status = true;
        String sql_statement = "";
        String start_time = "";
        String end_time = "";
        String guarantee_day_con = "";
        int t = 0;
        String[][] g_setup = null;
        String guarantee_location = "";
    	String message = "Set Guarantee Transaction Conditions";
        try {
            //get guarantee setup (STP_GUARANTEE Table) to verify and update flag
            //into transaction table(TRN_DAILY)
            sql_statement = "SELECT SG.HOSPITAL_CODE, SG.GUARANTEE_DR_CODE, DR.CODE, SG.GUARANTEE_TYPE_CODE, "+ //0-3
            "CASE WHEN SG.ADMISSION_TYPE_CODE = 'U' THEN '%' ELSE SG.ADMISSION_TYPE_CODE END, " + //4
            "SG.MM, SG.YYYY, SG.START_DATE, SG.START_TIME, SG.EARLY_TIME, " +           //5-9
            "SG.END_DATE, SG.END_TIME, SG.LATE_TIME, SG.GUARANTEE_LOCATION, SG.GUARANTEE_AMOUNT, " +            //10-14
            "SG.GUARANTEE_EXCLUDE_AMOUNT, SG.GUARANTEE_SOURCE, SG.GUARANTEE_FIX_AMOUNT, SG.GUARANTEE_CODE, "+   //15-18
            "HP.GUARANTEE_INCLUDE_EXTRA, HP.GUARANTEE_DAY, DR.GUARANTEE_DAY, SG.GUARANTEE_DAY, SG.IS_INCLUDE_LOCATION "+ //19-23
            "from STP_GUARANTEE SG " +
            "LEFT OUTER JOIN DOCTOR DR ON (SG.GUARANTEE_DR_CODE = DR.GUARANTEE_DR_CODE AND SG.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
            "LEFT OUTER JOIN HOSPITAL HP ON SG.HOSPITAL_CODE = HP.CODE "+
            "WHERE SG.ACTIVE = '1' AND DR.ACTIVE = '1' AND SG.MM = '"+month+"' AND SG.YYYY = '"+year+"' AND "+
            "SG.IS_PROCESS = 'N' AND "+
            "SG.HOSPITAL_CODE = '"+this.hospitalCode+"' AND DR.HOSPITAL_CODE = '"+this.hospitalCode+"' "+
            "ORDER BY SG.GUARANTEE_TYPE_CODE, SG.GUARANTEE_DR_CODE";
            g_setup = cdb.query(sql_statement);
            
        	t = g_setup.length;
        	System.out.println("Num Tran : "+t);
            for(int i = 0; i<g_setup.length; i++){ //update flag in trn_daily for calculate guarantee
            	System.out.println("Set Guarantee Transaction Running to "+i+" Of "+t+" ON TIME "+JDate.getTime());                                
                try{//Start Time
                    if(g_setup[i][9].equals("000000") || g_setup[i][9].equals("0") || g_setup[i][9].equals("")){
                        start_time = g_setup[i][8];
                    }else{
                        start_time = g_setup[i][9];
                    }
                }catch(Exception e){
                	start_time = g_setup[i][8];
                    System.out.println("Exception guarantee early time : "+e);
                }
                
                try{//End Time
                    if(g_setup[i][12].equals("000000") || g_setup[i][12].equals("0") || g_setup[i][12].equals("")){
                        end_time = g_setup[i][11];
                    }else{
                        end_time = g_setup[i][12];
                    }
                }catch(Exception e){
                	end_time = g_setup[i][11];
                    System.out.println("Exception guarantee late time : "+e);
                }

	            //Create Statement from Guarantee Date Condition
	            if(g_setup[i][22].equals("VER")){//Guarantee Day? Verify Date = "VER", Invoice Date = "INV"
                	guarantee_day_con = "AND (VERIFY_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND ";
                }else{
                	guarantee_day_con = "AND (TRANSACTION_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND ";
                }
	            
	            try{
	                if(g_setup[i][13].trim().equals("") || g_setup[i][13]==null){
	                	guarantee_location = "";
	                }else{
		                if(g_setup[i][23].trim().equals("Y")){
		                	guarantee_location = "PATIENT_DEPARTMENT_CODE = '" + g_setup[i][13] + "' AND ";
		                }else{
		                	guarantee_location = "PATIENT_DEPARTMENT_CODE != '" + g_setup[i][13] + "' AND ";		                	
		                }
	                }
	            }catch(Exception e){
	            	guarantee_location = "";
	            }

                try{
                    sql_statement = "UPDATE TRN_DAILY SET GUARANTEE_CODE = '"+ g_setup[i][18] + "', "+
                    "GUARANTEE_DR_CODE = '" + g_setup[i][1] + "', " +
                    "GUARANTEE_TERM_MM = '" + month +"', "+
                    "GUARANTEE_TERM_YYYY = '" + year +"' "+
                    "WHERE DOCTOR_CODE = '" + g_setup[i][2] + "' AND " + 
                    "ADMISSION_TYPE_CODE LIKE '"+ g_setup[i][4] + "' AND " +
                    "COMPUTE_DAILY_DATE <> '' AND "+guarantee_location+
                    "(TRANSACTION_DATE BETWEEN '"+g_setup[i][6]+""+g_setup[i][5]+"01' AND '"+g_setup[i][6]+""+g_setup[i][5]+"31') AND "+
                    "VERIFY_DATE IS NOT NULL "+guarantee_day_con+
                    "HOSPITAL_CODE = '" + g_setup[i][0] + "' AND " +
                    "ACTIVE = '1' AND " +
                    "DR_AMT > 0 AND "+//Update 20100915(10:31)
                    "ORDER_ITEM_ACTIVE = '1' AND " +
                    "IS_GUARANTEE = 'Y'";
                    System.out.println("up guarantee : "+sql_statement);
                    cdb.insert(sql_statement); //comment for skip write database process
                    cdb.commitDB(); //comment for skip write database process
                }catch(Exception e){
                    System.out.println("Exception : "+e);
                	cdb.rollDB();
                	status = false;
                	TRN_Error.setUser_name("");
                	TRN_Error.setHospital_code(this.hospitalCode);
		            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
                }
            }
        } catch (Exception ex) {
        	System.out.println("Must to Rollback Guarantee Process!!");
            System.out.println("Exception Step 6 : "+ex);
            status = false;
        }
        System.out.println("FINISH GUARANTEE STEP 2 "+JDate.getTime());
        return status;
    }

    private String SummaryHalfMonth(){
        return  "INSERT INTO SUMMARY_PAYMENT ("+
                "PAYMENT_TERM, YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, PAYMENT_TYPE, " +
                "SUM_AMT, SUM_DISC_AMT, SUM_DR_AMT, HP_SUM_AMT, DR_NET_PAID_AMT, " +
                "SUM_PAY_BY_CASH, SUM_PAY_BY_AR, SUM_PAY_BY_PATIENT, SUM_DR_IN_GUA, SUM_DR_IN_EXT, "+
                "SUM_DF_TAX_400, SUM_DF_TAX_401, SUM_DF_TAX_402, SUM_DF_TAX_406, "+
                "CREATE_DATE, CREATE_TIME, CREATE_USER_ID, PAYMENT_MODE_CODE, " +
                "REF_PAID_NO, PAYMENT_DATE, EXDR_AMT, EXCR_AMT, "+
                "EXDR_400, EXDR_401, EXDR_402, EXDR_406, " +
                "EXCR_400, EXCR_401, EXCR_402, EXCR_406, " +
                "IS_HOLD, SALARY_AMT, POSITION_AMT, GUARANTEE_AMT, ABSORB_AMT, EXTRA_AMT) "+

                "SELECT PAYMENT_TERM, YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, PAYMENT_TYPE, "+
                "SUM(AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, "+
                "SUM(AMOUNT_OF_DISCOUNT) AS AMOUNT_OF_DISCOUNT, "+
                "SUM(DR_AMT) AS DR_AMT, SUM(HP_AMT) AS HP_AMT, "+
                "SUM(DR_NET_PAID_AMT) AS DR_NET_PAID_AMT, "+
                "SUM(SUM_PAY_BY_CASH) AS SUM_PAY_BY_CASH, "+
                "SUM(SUM_PAY_BY_AR) AS SUM_PAY_BY_AR, "+
                "SUM(SUM_PAY_BY_PATIENT) AS SUM_PAY_BY_PATIENT, "+
                "SUM(SUM_DR_IN_GUA) AS SUM_DR_IN_GUA, "+
                "SUM(SUM_DR_IN_EXT) AS SUM_DR_IN_EXT, "+
                "SUM(SUM_TAX_400) AS SUM_TAX_400, "+
                "SUM(SUM_TAX_401) AS SUM_TAX_401, "+
                "SUM(SUM_TAX_402) AS SUM_TAX_402, "+
                "SUM(SUM_TAX_406) AS SUM_TAX_406, "+
                "CREATE_DATE, CREATE_TIME, CREATE_USER_ID, PAYMENT_MODE_CODE, REF_PAID_NO, "+
                "PAYMENT_DATE, "+
                "SUM(EXDR_AMT) AS EXDR_AMT, "+
                "SUM(EXCR_AMT) AS EXCR_AMT, "+
                "SUM(EXDR_400) AS EXDR_400, "+
                "SUM(EXDR_401) AS EXDR_401, "+
                "SUM(EXDR_402) AS EXDR_402, "+
                "SUM(EXDR_406) AS EXDR_406, "+
                "SUM(EXCR_400) AS EXCR_400, "+
                "SUM(EXCR_401) AS EXCR_401, "+
                "SUM(EXCR_402) AS EXCR_402, "+
                "SUM(EXCR_406) AS EXCR_406, "+
                "IS_HOLD, SALARY_AMT, POSITION_AMT,  "+
                "SUM(GUARANTEE_AMOUNT) AS GUARANTEE_AMOUNT, "+
                "SUM(ABSORB_AMT) AS ABSORB_AMT, "+
                "SUM(EXTRA_AMT) AS EXTRA_AMT "+
                "FROM "+
                
                "( "+
                "SELECT '"+this.term+"' AS PAYMENT_TERM, TRN_DAILY.YYYY, TRN_DAILY.MM, TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.DOCTOR_CODE, '04' AS PAYMENT_TYPE, "+
                "SUM(TRN_DAILY.AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, "+
                "SUM(TRN_DAILY.AMOUNT_OF_DISCOUNT) AS AMOUNT_OF_DISCOUNT, "+
                "SUM(TRN_DAILY.DR_AMT) AS DR_AMT, SUM(TRN_DAILY.HP_AMT) AS HP_AMT, "+
                "SUM(TRN_DAILY.DR_AMT) AS DR_NET_PAID_AMT, "+
                "SUM(CASE WHEN TRN_DAILY.PAY_BY_AR != 'Y' THEN TRN_DAILY.DR_AMT ELSE 0 END) AS SUM_PAY_BY_CASH, "+
                "SUM(CASE WHEN TRN_DAILY.PAY_BY_AR = 'Y' AND (TRN_DAILY.RECEIPT_NO NOT LIKE 'AdvanceAR%' AND TRN_DAILY.IS_PARTIAL != 'Y' ) THEN TRN_DAILY.DR_AMT ELSE 0 END) AS SUM_PAY_BY_AR, "+
                "SUM(CASE WHEN TRN_DAILY.PAY_BY_AR = 'Y' AND (TRN_DAILY.RECEIPT_NO LIKE 'AdvanceAR%' OR TRN_DAILY.IS_PARTIAL = 'Y') THEN TRN_DAILY.DR_AMT ELSE 0 END ) AS SUM_PAY_BY_PATIENT, "+
                "0 AS SUM_DR_IN_GUA, "+
                "0 AS SUM_DR_IN_EXT, "+
                "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '400' THEN DR_TAX_400 ELSE 0 END) AS SUM_TAX_400, "+
                "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '401' THEN DR_TAX_401 ELSE 0 END) AS SUM_TAX_401, "+
                "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '402' THEN DR_TAX_402 ELSE 0 END) AS SUM_TAX_402, "+
                "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '406' THEN DR_TAX_406 ELSE 0 END) AS SUM_TAX_406, "+
                "'"+JDate.getDate()+"' AS CREATE_DATE, '"+JDate.getTime()+"' AS CREATE_TIME, '' AS CREATE_USER_ID, " +
                "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO AS REF_PAID_NO, "+
                "'"+this.payDate+"' AS PAYMENT_DATE, "+
                "0 AS EXDR_AMT, 0 AS EXCR_AMT, "+
                "0 AS EXDR_400, 0 AS EXDR_401, 0 AS EXDR_402, 0 AS EXDR_406, "+
                "0 AS EXCR_400, 0 AS EXCR_401, 0 AS EXCR_402, 0 AS EXCR_406, "+
                "DOCTOR.IS_HOLD AS IS_HOLD, 0 AS SALARY_AMT, 0 AS POSITION_AMT, 0 AS GUARANTEE_AMOUNT, "+
                "0 AS ABSORB_AMT, 0 AS EXTRA_AMT "+
                this.summaryMonthlyProcessHalfMonth(this.endDate)+
                "GROUP BY TRN_DAILY.YYYY, TRN_DAILY.MM, TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.DOCTOR_CODE, "+
                "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, "+
                "DOCTOR.IS_HOLD "+
                "UNION "+
                
                "SELECT '"+this.term+"' AS PAYMENT_TERM, AJ.YYYY, AJ.MM, AJ.HOSPITAL_CODE, AJ.DOCTOR_CODE, '04' AS PAYMENT_TYPE, "+
                "0 AS AMOUNT_AFT_DISCOUNT, "+
                "0 AS AMOUNT_OF_DISCOUNT, "+
                "0 AS DR_AMT, "+
                "0 AS HP_AMT, "+
                "SUM(AJ.AMOUNT*AJ.EXPENSE_SIGN) AS DR_NET_PAID_AMT, "+
                "0 AS SUM_PAY_BY_CASH, "+
                "0 AS SUM_PAY_BY_AR, "+
                "0 AS SUM_PAY_BY_PATIENT, "+
                "0 AS SUM_DR_IN_GUA, "+
                "0 AS SUM_DR_IN_EXT, "+
                "0 AS SUM_TAX_400, "+
                "0 AS SUM_TAX_401, "+
                "0 AS SUM_TAX_402, "+
                "0 AS SUM_TAX_406, "+
                "'"+JDate.getDate()+"' AS CREATE_DATE, '"+JDate.getTime()+"' AS CREATE_TIME, '' AS CREATE_USER_ID, DOCTOR.PAYMENT_MODE_CODE, "+
                "DOCTOR.BANK_ACCOUNT_NO AS REF_PAID_NO, '"+this.payDate+"' AS PAYMENT_DATE, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' THEN AJ.AMOUNT ELSE 0 END) AS EXDR_AMT, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' THEN AJ.AMOUNT ELSE 0 END) AS EXCR_AMT, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '400' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_400, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '401' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_401, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '402' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_402, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '406' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_406, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '400' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_400, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '401' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_401, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '402' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_402, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '406' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_406, "+
                "DOCTOR.IS_HOLD AS IS_HOLD, 0 AS SALARY_AMT, 0 AS POSITION_AMT, 0 AS GUARANTEE_AMOUNT, "+
                "SUM(CASE WHEN EX.ADJUST_TYPE = 'HP' THEN AJ.AMOUNT ELSE 0 END) AS ABSORB_AMT, "+
                "SUM(CASE WHEN EX.ADJUST_TYPE = 'EX' THEN AJ.AMOUNT ELSE 0 END) AS EXTRA_AMT "+
                "FROM DOCTOR  "+
                "LEFT OUTER JOIN TRN_EXPENSE_DETAIL AS AJ "+
                "ON DOCTOR.CODE = AJ.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = AJ.HOSPITAL_CODE "+
                "LEFT OUTER JOIN EXPENSE AS EX "+
                "ON AJ.EXPENSE_CODE = EX.CODE AND AJ.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
                "WHERE DOCTOR.HOSPITAL_CODE = '"+this.hospitalCode+"' AND DOCTOR.PAYMENT_TIME = '2' "+ 
                "AND DOCTOR.PAYMENT_MODE_CODE NOT IN ('U','') AND DOCTOR.ACTIVE = '1' "+
                "AND AJ.YYYY = '"+this.year+"' AND AJ.MM = '"+this.month+"' AND AJ.BATCH_NO = '' "+
                "GROUP BY AJ.YYYY, AJ.MM, AJ.HOSPITAL_CODE, AJ.DOCTOR_CODE, "+
                "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, "+
                "DOCTOR.IS_HOLD "+
                ") Q "+
                "GROUP BY PAYMENT_TERM, YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, "+
                "PAYMENT_TYPE, CREATE_DATE, CREATE_TIME , CREATE_USER_ID, "+
                "PAYMENT_MODE_CODE, REF_PAID_NO, "+
                "PAYMENT_DATE, " +
                "IS_HOLD, SALARY_AMT, POSITION_AMT";
    }
		
	/**
	 * Second Term Method
	 */
	private String SummaryMonthEnd(){
        return  "INSERT INTO SUMMARY_PAYMENT ("+
                "PAYMENT_TERM, YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, PAYMENT_TYPE, " +
                "SUM_AMT, SUM_DISC_AMT, SUM_DR_AMT, HP_SUM_AMT, DR_NET_PAID_AMT, " +
                "SUM_PAY_BY_CASH, SUM_PAY_BY_AR, SUM_PAY_BY_PATIENT, "+
                "SUM_DR_IN_GUA, SUM_DR_IN_EXT, "+
                "SUM_DF_TAX_400, SUM_DF_TAX_401, SUM_DF_TAX_402, SUM_DF_TAX_406, "+
                "CREATE_DATE, CREATE_TIME, CREATE_USER_ID, PAYMENT_MODE_CODE, " +
                "REF_PAID_NO, PAYMENT_DATE, EXDR_AMT, EXCR_AMT, "+
                "EXDR_400, EXDR_401, EXDR_402, EXDR_406, " +
                "EXCR_400, EXCR_401, EXCR_402, EXCR_406, " +
                "IS_HOLD, SALARY_AMT, POSITION_AMT, GUARANTEE_AMT, ABSORB_AMT, EXTRA_AMT) "+

                "SELECT PAYMENT_TERM, YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, PAYMENT_TYPE, "+
                "SUM(AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, "+
                "SUM(AMOUNT_OF_DISCOUNT) AS AMOUNT_OF_DISCOUNT, "+
                "SUM(DR_AMT) AS DR_AMT, SUM(HP_AMT) AS HP_AMT, "+
                "SUM(DR_NET_PAID_AMT) AS DR_NET_PAID_AMT, "+
                "SUM(SUM_PAY_BY_CASH) AS SUM_PAY_BY_CASH, "+
                "SUM(SUM_PAY_BY_AR) AS SUM_PAY_BY_AR, "+
                "SUM(SUM_PAY_BY_PATIENT) AS SUM_PAY_BY_PATIENT, "+
                "SUM(SUM_DR_IN_GUA) AS SUM_DR_IN_GUA, "+
                "SUM(SUM_DR_IN_EXT) AS SUM_DR_IN_EXT, "+
                "SUM(SUM_TAX_400) AS SUM_TAX_400, "+
                "SUM(SUM_TAX_401) AS SUM_TAX_401, "+
                "SUM(SUM_TAX_402) AS SUM_TAX_402, "+
                "SUM(SUM_TAX_406) AS SUM_TAX_406, "+
                "CREATE_DATE, CREATE_TIME, CREATE_USER_ID, PAYMENT_MODE_CODE, REF_PAID_NO, "+
                "PAYMENT_DATE, "+
                "SUM(EXDR_AMT) AS EXDR_AMT, "+
                "SUM(EXCR_AMT) AS EXCR_AMT, "+
                "SUM(EXDR_400) AS EXDR_400, "+
                "SUM(EXDR_401) AS EXDR_401, "+
                "SUM(EXDR_402) AS EXDR_402, "+
                "SUM(EXDR_406) AS EXDR_406, "+
                "SUM(EXCR_400) AS EXCR_400, "+
                "SUM(EXCR_401) AS EXCR_401, "+
                "SUM(EXCR_402) AS EXCR_402, "+
                "SUM(EXCR_406) AS EXCR_406, "+
                "IS_HOLD, SALARY_AMT, POSITION_AMT,  "+
                "SUM(GUARANTEE_AMOUNT) AS GUARANTEE_AMOUNT, "+
                "SUM(ABSORB_AMT) AS ABSORB_AMT, "+
                "SUM(EXTRA_AMT) AS EXTRA_AMT "+
                "FROM "+
                
                "( "+
                "SELECT '2' AS PAYMENT_TERM, TRN_DAILY.YYYY, TRN_DAILY.MM, TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.DOCTOR_CODE, '04' AS PAYMENT_TYPE, "+
                "SUM(TRN_DAILY.AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, "+
                "SUM(TRN_DAILY.AMOUNT_OF_DISCOUNT) AS AMOUNT_OF_DISCOUNT, "+
                "SUM(TRN_DAILY.DR_AMT) AS DR_AMT, SUM(TRN_DAILY.HP_AMT) AS HP_AMT, "+
                "SUM(TRN_DAILY.DR_AMT) AS DR_NET_PAID_AMT, "+
                "SUM(CASE WHEN TRN_DAILY.PAY_BY_AR != 'Y' THEN TRN_DAILY.DR_AMT ELSE 0 END) AS SUM_PAY_BY_CASH, "+
                "SUM(CASE WHEN TRN_DAILY.PAY_BY_AR = 'Y' AND (TRN_DAILY.RECEIPT_NO NOT LIKE 'AdvanceAR%' AND TRN_DAILY.IS_PARTIAL != 'Y' ) THEN TRN_DAILY.DR_AMT ELSE 0 END) AS SUM_PAY_BY_AR, "+
                "SUM(CASE WHEN TRN_DAILY.PAY_BY_AR = 'Y' AND (TRN_DAILY.RECEIPT_NO LIKE 'AdvanceAR%' OR TRN_DAILY.IS_PARTIAL = 'Y') THEN TRN_DAILY.DR_AMT ELSE 0 END ) AS SUM_PAY_BY_PATIENT, "+
                "SUM(CASE WHEN TRN_DAILY.GUARANTEE_CODE != '' AND TRN_DAILY.TRANSACTION_DATE LIKE '"+this.year+this.month+"%' AND GUARANTEE_NOTE NOT LIKE '%EXTRA%' THEN DR_AMT ELSE 0 END) AS SUM_DR_IN_GUA, "+
                "SUM(CASE WHEN TRN_DAILY.GUARANTEE_CODE != '' AND TRN_DAILY.TRANSACTION_DATE LIKE '"+this.year+this.month+"%' AND GUARANTEE_NOTE LIKE '%EXTRA%' THEN DR_AMT ELSE 0 END) AS SUM_DR_IN_EXT, "+
                "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '400' THEN DR_TAX_400 ELSE 0 END) AS SUM_TAX_400, "+
                "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '401' THEN DR_TAX_401 ELSE 0 END) AS SUM_TAX_401, "+
                "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '402' THEN DR_TAX_402 ELSE 0 END) AS SUM_TAX_402, "+
                "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '406' THEN DR_TAX_406 ELSE 0 END) AS SUM_TAX_406, "+
                "'"+JDate.getDate()+"' AS CREATE_DATE, '"+JDate.getTime()+"' AS CREATE_TIME, '' AS CREATE_USER_ID, " +
                "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO AS REF_PAID_NO, "+
                "'"+this.payDate+"' AS PAYMENT_DATE, "+
                "0 AS EXDR_AMT, "+
                "0 AS EXCR_AMT, "+
                "0 AS EXDR_400, "+
                "0 AS EXDR_401, "+
                "0 AS EXDR_402, "+
                "0 AS EXDR_406, "+
                "0 AS EXCR_400, "+
                "0 AS EXCR_401, "+
                "0 AS EXCR_402, "+
                "0 AS EXCR_406, "+
                "DOCTOR.IS_HOLD AS IS_HOLD, 0 AS SALARY_AMT, 0 AS POSITION_AMT, 0 AS GUARANTEE_AMOUNT, "+
                "0 AS ABSORB_AMT, "+
                "0 AS EXTRA_AMT "+
                this.summaryMonthlyProcess(this.endDate)+
                "GROUP BY TRN_DAILY.YYYY, TRN_DAILY.MM, TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.DOCTOR_CODE, "+
                "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, "+
                "DOCTOR.IS_HOLD "+
                "UNION "+
                
                "SELECT '2' AS PAYMENT_TERM, AJ.YYYY, AJ.MM, AJ.HOSPITAL_CODE, AJ.DOCTOR_CODE, '04' AS PAYMENT_TYPE, "+
                "0 AS AMOUNT_AFT_DISCOUNT, "+
                "0 AS AMOUNT_OF_DISCOUNT, "+
                "0 AS DR_AMT, "+
                "0 AS HP_AMT, "+
                "SUM(AJ.AMOUNT*AJ.EXPENSE_SIGN) AS DR_NET_PAID_AMT, "+
                "0 AS SUM_PAY_BY_CASH, "+
                "0 AS SUM_PAY_BY_AR, "+
                "0 AS SUM_PAY_BY_PATIENT, "+
                "0 AS SUM_DR_IN_GUA, "+
                "0 AS SUM_DR_IN_EXT, "+
                "0 AS SUM_TAX_400, "+
                "0 AS SUM_TAX_401, "+
                "0 AS SUM_TAX_402, "+
                "0 AS SUM_TAX_406, "+
                "'"+JDate.getDate()+"' AS CREATE_DATE, '"+JDate.getTime()+"' AS CREATE_TIME, '' AS CREATE_USER_ID, DOCTOR.PAYMENT_MODE_CODE, "+
                "DOCTOR.BANK_ACCOUNT_NO AS REF_PAID_NO, '"+this.payDate+"' AS PAYMENT_DATE, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' THEN AJ.AMOUNT ELSE 0 END) AS EXDR_AMT, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' THEN AJ.AMOUNT ELSE 0 END) AS EXCR_AMT, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '400' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_400, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '401' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_401, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '402' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_402, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '406' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_406, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '400' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_400, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '401' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_401, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '402' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_402, "+
                "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '406' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_406, "+
                "DOCTOR.IS_HOLD AS IS_HOLD, 0 AS SALARY_AMT, 0 AS POSITION_AMT, 0 AS GUARANTEE_AMOUNT, "+
                "SUM(CASE WHEN EX.ADJUST_TYPE = 'HP' THEN AJ.AMOUNT ELSE 0 END) AS ABSORB_AMT, "+
                "SUM(CASE WHEN EX.ADJUST_TYPE = 'EX' THEN AJ.AMOUNT ELSE 0 END) AS EXTRA_AMT "+
                "FROM DOCTOR  "+
                "LEFT OUTER JOIN TRN_EXPENSE_DETAIL AS AJ "+
                "ON DOCTOR.CODE = AJ.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = AJ.HOSPITAL_CODE "+
                "LEFT OUTER JOIN EXPENSE AS EX "+
                "ON AJ.EXPENSE_CODE = EX.CODE AND AJ.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
                "WHERE DOCTOR.HOSPITAL_CODE = '"+this.hospitalCode+"' "+ 
                "AND DOCTOR.PAYMENT_MODE_CODE NOT IN ('U','') AND DOCTOR.ACTIVE = '1' "+
                "AND AJ.YYYY = '"+this.year+"' AND AJ.MM = '"+this.month+"' AND ( AJ.BATCH_NO = '' OR AJ.BATCH_NO IS NULL ) "+
                "GROUP BY AJ.YYYY, AJ.MM, AJ.HOSPITAL_CODE, AJ.DOCTOR_CODE, "+
                "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, "+
                "DOCTOR.IS_HOLD "+
                "UNION "+
                
                "SELECT '2' AS PAYMENT_TERM, GT.YYYY, GT.MM, GT.HOSPITAL_CODE, GT.GUARANTEE_DR_CODE, '04' AS PAYMENT_TYPE, "+
                "0 AS AMOUNT_AFT_DISCOUNT, "+
                "0 AS AMOUNT_OF_DISCOUNT, "+
                "0 AS DR_AMT, "+
                "0 AS HP_AMT, "+
                "0 AS DR_NET_PAID_AMT, "+
                "0 AS SUM_PAY_BY_CASH, "+
                "0 AS SUM_PAY_BY_AR, "+
                "0 AS SUM_PAY_BY_PATIENT, "+
                "0 AS SUM_DR_IN_GUA, "+
                "0 AS SUM_DR_IN_EXT, "+
                "0 AS SUM_TAX_400, "+
                "0 AS SUM_TAX_401, "+
                "0 AS SUM_TAX_402, "+
                "0 AS SUM_TAX_406, "+
                "'"+JDate.getDate()+"' AS CREATE_DATE, '"+JDate.getTime()+"' AS CREATE_TIME, '' AS CREATE_USER_ID, DOCTOR.PAYMENT_MODE_CODE, "+
                "DOCTOR.BANK_ACCOUNT_NO AS REF_PAID_NO, '"+this.payDate+"' AS PAYMENT_DATE, "+
                "0 AS EXDR_AMT, "+
                "0 AS EXCR_AMT, "+
                "0 AS EXDR_400, "+
                "0 AS EXDR_401, "+
                "0 AS EXDR_402, "+
                "0 AS EXDR_406, "+
                "0 AS EXCR_400, "+
                "0 AS EXCR_401, "+
                "0 AS EXCR_402, "+
                "0 AS EXCR_406, "+
                "DOCTOR.IS_HOLD AS IS_HOLD, 0 AS SALARY_AMT, 0 AS POSITION_AMT, "+
                "SUM(GT.GUARANTEE_AMOUNT) AS GUARANTEE_AMOUNT, "+
                "0 AS ABSORB_AMT, "+
                "0 AS EXTRA_AMT "+
                "FROM DOCTOR  "+
                "LEFT OUTER JOIN STP_GUARANTEE AS GT "+
                "ON DOCTOR.CODE = GT.GUARANTEE_DR_CODE AND DOCTOR.HOSPITAL_CODE = GT.HOSPITAL_CODE "+
                "WHERE GT.HOSPITAL_CODE = '"+this.hospitalCode+"' "+
                "AND DOCTOR.PAYMENT_MODE_CODE NOT IN ('U','') AND DOCTOR.ACTIVE = '1' "+
                "AND GT.YYYY = '"+this.year+"' AND GT.MM = '"+this.month+"' AND GT.ACTIVE = '1' "+
                "GROUP BY GT.YYYY, GT.MM, GT.HOSPITAL_CODE, GT.GUARANTEE_DR_CODE, "+
                "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, "+
                "DOCTOR.IS_HOLD "+
                ") Q "+
                "GROUP BY PAYMENT_TERM, YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, "+
                "PAYMENT_TYPE, CREATE_DATE, CREATE_TIME , CREATE_USER_ID, "+
                "PAYMENT_MODE_CODE, REF_PAID_NO, "+
                "PAYMENT_DATE, " +
                "IS_HOLD, SALARY_AMT, POSITION_AMT";
    }
	private String doMonthEndAdjustClose(){
		return
		"UPDATE TRN_EXPENSE_DETAIL SET BATCH_NO = '" +this.year+this.month+ "', "+
		"PAYMENT_DATE = '"+this.payDate+"', PAYMENT_TERM = '"+this.term+"' "+
		"FROM TRN_EXPENSE_DETAIL " +
        "LEFT OUTER JOIN DOCTOR " +
        "ON TRN_EXPENSE_DETAIL.DOCTOR_CODE = DOCTOR.CODE AND TRN_EXPENSE_DETAIL.HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE "+
        "WHERE "+
        "TRN_EXPENSE_DETAIL.HOSPITAL_CODE = '"+this.hospitalCode+"' "+
        "AND TRN_EXPENSE_DETAIL.YYYY = '"+this.year+"' " +
        "AND TRN_EXPENSE_DETAIL.MM = '"+this.month+"' "+
        "AND (BATCH_NO = '' OR BATCH_NO IS NULL)"+
		"";
	}
	public void setUserID(String s){
		this.userID = s;
	}
	private String doMoveOnward(){
		//"INSERT INTO TRN_ONWARD SELECT * FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND IS_ONWARD = 'Y'";
		String cmd_onward = 
		"INSERT INTO TRN_ONWARD "+
		"SELECT HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, RECEIPT_NO, RECEIPT_DATE, TRANSACTION_DATE, HN_NO, PATIENT_NAME, EPISODE_NO, "+
        "NATIONALITY_CODE, NATIONALITY_DESCRIPTION, PAYOR_OFFICE_CODE, PAYOR_OFFICE_NAME, TRANSACTION_MODULE, TRANSACTION_TYPE, "+
        "PAYOR_OFFICE_CATEGORY_CODE, PAYOR_OFFICE_CATEGORY_DESCRIPTION, IS_WRITE_OFF, LINE_NO, ADMISSION_TYPE_CODE, "+
        "PATIENT_DEPARTMENT_CODE, PATIENT_LOCATION_CODE, RECEIPT_DEPARTMENT_CODE, RECEIPT_LOCATION_CODE, DOCTOR_DEPARTMENT_CODE, "+
        "ORDER_ITEM_CODE, ORDER_ITEM_DESCRIPTION, DOCTOR_CODE, VERIFY_DATE, VERIFY_TIME, DOCTOR_EXECUTE_CODE, EXECUTE_DATE, EXECUTE_TIME, "+
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
        "IS_DISCHARGE_SUMMARY, AMT_BEF_PARTIAL, DOCTOR_PRIVATE_CODE, NOTE, SEQ_STEP "+
        "FROM TRN_DAILY "+
		//"SELECT * FROM TRN_DAILY "+
		"WHERE IS_ONWARD = 'Y' AND TRANSACTION_DATE LIKE '"+(this.year+this.month)+"%' AND "+
		"HOSPITAL_CODE = '"+this.hospitalCode+"' AND YYYY = '' AND GUARANTEE_PAID_AMT = 0";
		return cmd_onward;		
	}
	private String doDeleteOnward(){
		//"DELETE FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND IS_ONWARD = 'Y'";
		String cmd_del_onward = 
		"DELETE FROM TRN_DAILY "+
		"WHERE IS_ONWARD = 'Y' AND TRANSACTION_DATE LIKE '"+(this.year+this.month)+"%' AND "+
		"HOSPITAL_CODE = '"+this.hospitalCode+"' AND YYYY = '' AND GUARANTEE_PAID_AMT = 0";
		return cmd_del_onward;
	}
	private String doMoveXray(){
		//OLD FOR SVNH STANDARD 
		//return "INSERT INTO TRN_DAILY_LOG SELECT *,'','','','' FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND NOTE = 'XM'";
		return "INSERT INTO LOG_TRN_DAILY SELECT *,'','','' FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND NOTE = 'XM'";
	}
	private String doDeleteXray(){
		return
		"DELETE FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND NOTE = 'XM'"+
		"";
	}
}