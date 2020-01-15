package df.bean.process;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;

public class ProcessPartialPayment {
	private DBConn dbConn;
	private String sql;
	private String hospitalCode;
	private String startDate;
	private String endDate;
	private ArrayList<HashMap<String, String>> arrData = new ArrayList<HashMap<String,String>>();
	
	public boolean processPartial(String hospitalCode, String mm, String yyyy, String startDate, String endDate){
		boolean result = false;
		this.hospitalCode = hospitalCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.arrData = this.getLineNo(startDate, endDate);
		String isPartial = "";
		try {
			this.dbConn = new DBConn(true);
			this.dbConn.setStatement();
			isPartial = this.dbConn.getSingleData("SELECT IS_PARTIAL FROM HOSPITAL WHERE CODE = '"+this.hospitalCode+"'");
			this.dbConn.getStatement().close();
			this.dbConn.closeDB("Close DB from "+this.getClass());
		} catch (SQLException e) {
			System.out.println("Cannot get is partial condition from hospital : "+e);
		}
		
		if(isPartial.equals("Y")){
			System.out.println("\nPartial Payment Processing");
			if(this.backUpData(yyyy,mm)){
				System.out.println("Start Partial Process : "+JDate.getTime());
				System.out.println(this.arrData);
				result = this.culculate(this.arrData,mm,yyyy); //New Version (TRN_PARTIAL)
				//result = this.culculatePartial(this.arrData,mm,yyyy); //Old Version No (TRN_PARTIAL)
				System.out.println("Finish Partial Process : "+JDate.getTime());
			}else{
				result = false;
			}
		}
		return result;
	}	
	private boolean culculate(ArrayList<HashMap<String, String>> arrLineNo ,String mm, String yyyy){
		boolean result = true; 
		String trnDate = "";
		DBConn dbConnNew = new DBConn(false);
		try { dbConnNew.setStatement(); } catch (SQLException e1) {}
		HashMap<String, String > hashData = new HashMap<String, String>();
		for(int i=0;i<arrLineNo.size();i++){
			hashData = arrLineNo.get(i);
			
				this.sql = "INSERT INTO TRN_DAILY (" +
						" HOSPITAL_CODE," +
						" INVOICE_NO," +
						" INVOICE_DATE," +
						" RECEIPT_NO," +
						" RECEIPT_DATE," +
						" TRANSACTION_DATE," +
						" HN_NO," +
						" PATIENT_NAME," +
						" EPISODE_NO," +
						" NATIONALITY_CODE," +
						" NATIONALITY_DESCRIPTION," +
						" PAYOR_OFFICE_CODE," +
						" PAYOR_OFFICE_NAME, " +
						" TRANSACTION_MODULE," +
						" TRANSACTION_TYPE," +
						" PAYOR_OFFICE_CATEGORY_CODE," +
						" PAYOR_OFFICE_CATEGORY_DESCRIPTION," +
						" IS_WRITE_OFF," +
						" LINE_NO," +
						" ADMISSION_TYPE_CODE,"+ 
						" PATIENT_DEPARTMENT_CODE," +
						" PATIENT_LOCATION_CODE," +
						" RECEIPT_DEPARTMENT_CODE," +
						" RECEIPT_LOCATION_CODE," +
						" DOCTOR_DEPARTMENT_CODE," +
						" ORDER_ITEM_CODE," +
						" ORDER_ITEM_DESCRIPTION," +
						" DOCTOR_CODE," +
						" VERIFY_DATE,"+
						" VERIFY_TIME," +
						" DOCTOR_EXECUTE_CODE," +
						" EXECUTE_DATE," +
						" EXECUTE_TIME," +
						" DOCTOR_RESULT_CODE," +
						" OLD_DOCTOR_CODE," +
						" RECEIPT_TYPE_CODE," +
						" AMOUNT_BEF_DISCOUNT," +
						" AMOUNT_OF_DISCOUNT," +
						" AMOUNT_AFT_DISCOUNT," +
						" AMOUNT_BEF_WRITE_OFF," +
						" INV_IS_VOID," +
						" REC_IS_VOID," +
						" UPDATE_DATE," +
						" UPDATE_TIME," +
						" USER_ID," +
						" BATCH_NO," +
						" YYYY," +
						" MM," +
						" DD," +
						" NOR_ALLOCATE_AMT," +
						" NOR_ALLOCATE_PCT," +
						" DR_AMT," +
						" OLD_DR_AMT," +
						" DR_TAX_400," +
						" DR_TAX_401," +
						" DR_TAX_402," +
						" DR_TAX_406," +
						" TAX_TYPE_CODE," +
						" DR_PREMIUM," +
						" GUARANTEE_PAID_AMT," +
						" GUARANTEE_AMT," +
						" GUARANTEE_CODE," +
						" GUARANTEE_DR_CODE," +
						" GUARANTEE_TYPE," +
						" GUARANTEE_DATE_TIME," +
						" GUARANTEE_TERM_MM," +
						" GUARANTEE_TERM_YYYY," +
						" GUARANTEE_NOTE," +
						" IS_GUARANTEE," +
						" HP_AMT," +
						" HP_PREMIUM," +
						" HP_TAX," +
						" COMPUTE_DAILY_DATE," +
						" COMPUTE_DAILY_TIME," +
						" COMPUTE_DAILY_USER_ID," +
						" DOCTOR_CATEGORY_CODE," +
						" EXCLUDE_TREATMENT," +
						" PREMIUM_CHARGE_PCT," +
						" PREMIUM_REC_AMT," +
						" ACTIVE," +
						" INVOICE_TYPE," +
						" TOTAL_BILL_AMOUNT," +
						" TOTAL_DR_REC_AMOUNT," +
						" OLD_AMOUNT," +
						" PAY_BY_CASH," +
						" PAY_BY_AR," +
						" PAY_BY_DOCTOR," +
						" PAY_BY_PAYOR," +
						" PAY_BY_CASH_AR," +
						" IS_PAID," +
						" ORDER_ITEM_ACTIVE," +
						" ORDER_ITEM_CATEGORY_CODE," +
						" WRITE_OFF_BILL_AMT," +
						" WRITE_OFF_RECEIPT_AMT," +
						" OLD_DR_AMT_BEF_WRITE_OFF," +
						" DR_AMT_BEF_WRITE_OFF," +
						" DR_PREMIUM_BEF_WRITE_OFF," +
						" HP_AMT_BEF_WRITE_OFF," +
						" HP_PREMIUM_WRITE_OFF," +
						" OLD_TAX_AMT," +
						" DR_TAX_406_BEF_WRITE_OFF," +
						" TAX_FROM_ALLOCATE," +
						" IS_GUARANTEE_FROM_ALLOC," +
						" IS_PARTIAL) " +
					"SELECT " +
						" T.HOSPITAL_CODE," +
						" INVOICE_NO," +
						" INVOICE_DATE," +
						" '"+hashData.get("RECEIPT_NO")+"'," + 
						" '"+hashData.get("RECEIPT_DATE")+"'," +
						" T.TRANSACTION_DATE," +
						" HN_NO," + 
						" PATIENT_NAME," +
						" EPISODE_NO,"+ 
						" NATIONALITY_CODE," + 
						" NATIONALITY_DESCRIPTION," + 
						" PAYOR_OFFICE_CODE," + 
						" PAYOR_OFFICE_NAME," + 
						" 'AR'," + 
						" TRANSACTION_TYPE," + 
						" PAYOR_OFFICE_CATEGORY_CODE," +
						" PAYOR_OFFICE_CATEGORY_DESCRIPTION," +
						" IS_WRITE_OFF," + 
						" '"+hashData.get("LINE_NO")+"'," + 
						" ADMISSION_TYPE_CODE,"+ 
						" PATIENT_DEPARTMENT_CODE," +
						" PATIENT_LOCATION_CODE," + 
						" RECEIPT_DEPARTMENT_CODE," +
						" RECEIPT_LOCATION_CODE," + 
						" DOCTOR_DEPARTMENT_CODE," +
						" ORDER_ITEM_CODE," + 
						" ORDER_ITEM_DESCRIPTION," + 
						" DOCTOR_CODE," + 
						" VERIFY_DATE," + 
						" VERIFY_TIME," + 
						" DOCTOR_EXECUTE_CODE," + 
						" EXECUTE_DATE," + 
						" EXECUTE_TIME," + 
						" DOCTOR_RESULT_CODE," +
						" OLD_DOCTOR_CODE," + 
						" T.RECEIPT_TYPE_CODE," + 
						" AMOUNT_BEF_DISCOUNT," +
						" AMOUNT_OF_DISCOUNT," +
						//----AMOUNT_AFT_DISCOUNT
						" CASE WHEN (SELECT ((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT)*P.DR_AMT)/100)" +
						" FROM TRN_DAILY T, INT_ERP_AR_RECEIPT I, TRN_PARTIAL P " + 
						" WHERE T.INVOICE_NO = I.BILL_NO AND T.HOSPITAL_CODE = I.HOSPITAL_CODE" +
						" AND T.INVOICE_NO = P.INVOICE_NO AND T.HOSPITAL_CODE = P.HOSPITAL_CODE AND T.LINE_NO = P.LINE_NO AND T.INVOICE_TYPE = P.INVOICE_TYPE"+
						" AND T.LINE_NO = '"+hashData.get("LINE_NO")+"' AND I.IS_LOADED = 'N'" +
						" AND T.INVOICE_TYPE = '"+hashData.get("INVOICE_TYPE")+"'" +
						" AND I.RECEIPT_DATE = '"+hashData.get("RECEIPT_DATE")+"'" +
						" AND T.BATCH_NO = '' AND T.YYYY = '' "+
						" AND I.RECEIPT_NO = '"+hashData.get("RECEIPT_NO")+"' AND T.IS_PARTIAL ='N') > DR_AMT " +
						" THEN AMOUNT_AFT_DISCOUNT" + //IF Partial allocate > amount for payment then use that amount
						" ELSE " +
						" (SELECT ((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT)* P.AMOUNT_AFT_DISCOUNT)/100)" +
						" FROM TRN_DAILY T,INT_ERP_AR_RECEIPT I,TRN_PARTIAL P"+
						" WHERE T.INVOICE_NO = I.BILL_NO AND T.HOSPITAL_CODE = I.HOSPITAL_CODE" +
						" AND T.INVOICE_NO = P.INVOICE_NO AND T.HOSPITAL_CODE = P.HOSPITAL_CODE AND T.LINE_NO = P.LINE_NO AND T.INVOICE_TYPE = P.INVOICE_TYPE"+
						" AND T.LINE_NO = '"+hashData.get("LINE_NO")+"' AND I.IS_LOADED = 'N'" +
						" AND T.INVOICE_TYPE = '"+hashData.get("INVOICE_TYPE")+"'" +
						" AND I.RECEIPT_DATE = '"+hashData.get("RECEIPT_DATE")+"'" +
						" AND T.BATCH_NO = '' AND T.YYYY = '' "+
						" AND I.RECEIPT_NO = '"+hashData.get("RECEIPT_NO")+"' AND T.IS_PARTIAL ='N') END, "+
						//----AMOUNT_AFT_DISCOUNT
						" AMOUNT_BEF_WRITE_OFF," +
						" INV_IS_VOID," +
						" REC_IS_VOID," +
						" T.UPDATE_DATE," +
						" T.UPDATE_TIME," +
						" T.USER_ID," +
						" BATCH_NO," +
						" '"+yyyy+"'," +
						" '"+mm+"'," +
						" DD," +
						" NOR_ALLOCATE_AMT," +
						" NOR_ALLOCATE_PCT," +
						//----DR_AMT
						" CASE WHEN (SELECT ((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT)*P.DR_AMT)/100)" +
						" FROM TRN_DAILY T, INT_ERP_AR_RECEIPT I, TRN_PARTIAL P" + 
						" WHERE T.INVOICE_NO = I.BILL_NO AND T.HOSPITAL_CODE = I.HOSPITAL_CODE" +
						" AND T.INVOICE_NO = P.INVOICE_NO AND T.HOSPITAL_CODE = P.HOSPITAL_CODE AND T.LINE_NO = P.LINE_NO AND T.INVOICE_TYPE = P.INVOICE_TYPE"+
						" AND T.LINE_NO = '"+hashData.get("LINE_NO")+"' AND I.IS_LOADED = 'N'" +
						" AND T.INVOICE_TYPE = '"+hashData.get("INVOICE_TYPE")+"'" +
						" AND I.RECEIPT_DATE = '"+hashData.get("RECEIPT_DATE")+"'" +
						" AND T.BATCH_NO = '' AND T.YYYY = '' "+
						" AND I.RECEIPT_NO = '"+hashData.get("RECEIPT_NO")+"' AND T.IS_PARTIAL ='N') > DR_AMT " +
						" THEN DR_AMT" + //IF Partial allocate > amount for payment then use that amount
						" ELSE (SELECT ((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT)*P.DR_AMT)/100)" +
						" FROM TRN_DAILY T,INT_ERP_AR_RECEIPT I, TRN_PARTIAL P" + 
						" WHERE T.INVOICE_NO = I.BILL_NO AND T.HOSPITAL_CODE = I.HOSPITAL_CODE" +
						" AND T.INVOICE_NO = P.INVOICE_NO AND T.HOSPITAL_CODE = P.HOSPITAL_CODE AND T.LINE_NO = P.LINE_NO AND T.INVOICE_TYPE = P.INVOICE_TYPE"+
						" AND T.LINE_NO = '"+hashData.get("LINE_NO")+"' AND I.IS_LOADED = 'N'" +
						" AND T.INVOICE_TYPE = '"+hashData.get("INVOICE_TYPE")+"'" +
						" AND I.RECEIPT_DATE = '"+hashData.get("RECEIPT_DATE")+"'" +
						" AND T.BATCH_NO = '' AND T.YYYY = '' "+
						" AND I.RECEIPT_NO = '"+hashData.get("RECEIPT_NO")+"' AND T.IS_PARTIAL ='N') END, "+
						//----DR_AMT
						" OLD_DR_AMT," +
						" DR_TAX_400," +
						" DR_TAX_401," +
						" DR_TAX_402," +
						//----DR_TAX_406
						" CASE WHEN (SELECT ((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT)*P.DR_AMT)/100)" +
						" FROM TRN_DAILY T, INT_ERP_AR_RECEIPT I, TRN_PARTIAL P " + 
						" WHERE T.INVOICE_NO = I.BILL_NO AND T.HOSPITAL_CODE = I.HOSPITAL_CODE" +
						" AND T.INVOICE_NO = P.INVOICE_NO AND T.HOSPITAL_CODE = P.HOSPITAL_CODE AND T.LINE_NO = P.LINE_NO AND T.INVOICE_TYPE = P.INVOICE_TYPE"+
						" AND T.LINE_NO = '"+hashData.get("LINE_NO")+"' AND I.IS_LOADED = 'N'" +
						" AND T.INVOICE_TYPE = '"+hashData.get("INVOICE_TYPE")+"'" +
						" AND I.RECEIPT_DATE = '"+hashData.get("RECEIPT_DATE")+"'" +
						" AND T.BATCH_NO = '' AND T.YYYY = '' "+
						" AND I.RECEIPT_NO = '"+hashData.get("RECEIPT_NO")+"' AND T.IS_PARTIAL ='N') > DR_AMT " +
						" THEN DR_TAX_406" + //IF Partial allocate > amount for payment then use that amount
						" ELSE (SELECT ((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT)*P.TAX_AMT)/100)" +
						" FROM TRN_DAILY T, INT_ERP_AR_RECEIPT I, TRN_PARTIAL P "+ 
						" WHERE T.INVOICE_NO = I.BILL_NO AND T.HOSPITAL_CODE = I.HOSPITAL_CODE" +
						" AND T.INVOICE_NO = P.INVOICE_NO AND T.HOSPITAL_CODE = P.HOSPITAL_CODE AND T.LINE_NO = P.LINE_NO AND T.INVOICE_TYPE = P.INVOICE_TYPE"+
						" AND T.LINE_NO = '"+hashData.get("LINE_NO")+"' AND I.IS_LOADED = 'N'" +
						" AND T.INVOICE_TYPE = '"+hashData.get("INVOICE_TYPE")+"'" +
						" AND I.RECEIPT_DATE = '"+hashData.get("RECEIPT_DATE")+"'" +
						" AND T.BATCH_NO = '' AND T.YYYY = '' "+
						" AND I.RECEIPT_NO = '"+hashData.get("RECEIPT_NO")+"' AND T.IS_PARTIAL ='N') END,"+ 
						//----DR_TAX_406
						" TAX_TYPE_CODE," +
						" DR_PREMIUM," +
						" GUARANTEE_PAID_AMT," +
						" GUARANTEE_AMT," +
						" GUARANTEE_CODE," +
						" GUARANTEE_DR_CODE," +
						" GUARANTEE_TYPE," +
						" GUARANTEE_DATE_TIME," +
						" GUARANTEE_TERM_MM," +
						" GUARANTEE_TERM_YYYY," +
						" GUARANTEE_NOTE," +
						" IS_GUARANTEE," +
						" '0', "+
						//" HP_AMT," +
						" HP_PREMIUM," +
						" HP_TAX," +
						" COMPUTE_DAILY_DATE," +
						" COMPUTE_DAILY_TIME," +
						" COMPUTE_DAILY_USER_ID," +
						" DOCTOR_CATEGORY_CODE," +
						" EXCLUDE_TREATMENT," +
						" PREMIUM_CHARGE_PCT," +
						" PREMIUM_REC_AMT," +
						" ACTIVE," +
						" INVOICE_TYPE," +
						" TOTAL_BILL_AMOUNT," +
						" TOTAL_DR_REC_AMOUNT," +
						" OLD_AMOUNT," +
						" PAY_BY_CASH," +
						//" CASE WHEN SUBSTRING(I.RECEIPT_DATE,1,6) = SUBSTRING(T.INVOICE_DATE,1,6) THEN PAY_BY_AR ELSE 'Y' END, " +
						" 'Y'," +
						" PAY_BY_DOCTOR," +
						" PAY_BY_PAYOR," +
						//" CASE WHEN SUBSTRING(I.RECEIPT_DATE,1,6) = SUBSTRING(T.INVOICE_DATE,1,6) THEN 'Y' ELSE PAY_BY_CASH_AR END, " +
						" PAY_BY_CASH_AR," +
						" IS_PAID," +
						" ORDER_ITEM_ACTIVE," +
						" ORDER_ITEM_CATEGORY_CODE," +
						" WRITE_OFF_BILL_AMT," +
						" WRITE_OFF_RECEIPT_AMT," +
						" OLD_DR_AMT_BEF_WRITE_OFF," +
						" DR_AMT_BEF_WRITE_OFF," +
						" DR_PREMIUM_BEF_WRITE_OFF," +
						" HP_AMT_BEF_WRITE_OFF," +
						" HP_PREMIUM_WRITE_OFF," +
						" OLD_TAX_AMT," +
						" DR_TAX_406_BEF_WRITE_OFF," +
						" TAX_FROM_ALLOCATE," +
						" IS_GUARANTEE_FROM_ALLOC," +
						" 'Y'" +
						//" FROM TRN_DAILY "+ 
						//" WHERE LINE_NO = '"+hashData.get("LINE_NO")+"' AND IS_PARTIAL = 'N'" +
						" FROM TRN_DAILY T,INT_ERP_AR_RECEIPT I "+
						" WHERE T.INVOICE_NO = I.BILL_NO "+
						" AND T.HOSPITAL_CODE = I.HOSPITAL_CODE "+
						" AND T.INVOICE_NO = '"+hashData.get("INVOICE_NO")+"'" +
						" AND I.RECEIPT_NO = '"+hashData.get("RECEIPT_NO")+"'"+
						" AND I.RECEIPT_DATE = '"+hashData.get("RECEIPT_DATE")+"'" +
						" AND LINE_NO = '"+hashData.get("LINE_NO")+"' AND IS_PARTIAL = 'N'"+ 
						" AND INVOICE_TYPE ='"+hashData.get("INVOICE_TYPE")+"'" +
						" AND BATCH_NO = '' AND T.YYYY = '' "+
						//" AND DR_AMT > 0" +
						" AND T.HOSPITAL_CODE = '"+this.hospitalCode+"' AND T.ACTIVE = '1'";
						//System.out.println("sql = " + this.sql);
		
				try {
					dbConnNew.insert(this.sql);
					dbConnNew.commitDB();
				} catch (SQLException e) {
					System.out.println("error partial on statement : "+this.sql);
					result = false;
					System.out.println("SQLException in method \"culculatePratial\" By LineNo =\""+hashData.get("LINE_NO")+"\" Error => "+e.getMessage());
				}
				if(result){
					this.sql = "UPDATE TRN_DAILY SET "
					+ "DR_AMT = CASE WHEN ((((100 * I.PAYMENT_AMOUNT)/I.BILL_AMOUNT) * P.DR_AMT) / 100) > T.DR_AMT "
					+ "THEN 0 "
					+ "ELSE T.DR_AMT_BEF_PARTIAL - ((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT) * P.DR_AMT) / 100) END, "
					
					+ "DR_TAX_406 = CASE WHEN ((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT) * P.DR_AMT) / 100) > T.DR_AMT "
					+ "THEN 0 "
					+ "ELSE T.DR_TAX_BEF_PARTIAL-((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT) * P.TAX_AMT) / 100) END, "
					
					+ "AMOUNT_AFT_DISCOUNT = CASE WHEN ((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT) * P.DR_AMT) / 100) > T.DR_AMT "
					+ "THEN 0 "
					+ "ELSE T.AMT_BEF_PARTIAL-((((100*I.PAYMENT_AMOUNT)/I.BILL_AMOUNT) * P.AMOUNT_AFT_DISCOUNT) / 100) END "
					
					+ "FROM TRN_DAILY T, INT_ERP_AR_RECEIPT I, TRN_PARTIAL P "
					+ "WHERE T.INVOICE_NO = I.BILL_NO AND T.HOSPITAL_CODE = I.HOSPITAL_CODE "
					+" AND T.INVOICE_NO = P.INVOICE_NO AND T.HOSPITAL_CODE = P.HOSPITAL_CODE "
					+ "AND T.LINE_NO = P.LINE_NO AND T.INVOICE_TYPE = P.INVOICE_TYPE "
					+ "AND I.DOC_TYPE= 'R' AND I.IS_LAST_RECEIPT ='N' "
					+ "AND I.IS_LOADED = 'N' "
					+ "AND T.IS_PARTIAL = 'N' "
					+ "AND T.YYYY = '' "
					+ "AND T.BATCH_NO = '' "
					+ "AND T.HOSPITAL_CODE = '"+this.hospitalCode+"' "
					//add invoice type
					+ "AND T.INVOICE_TYPE = '"+hashData.get("INVOICE_TYPE")+"' "
					+ "AND T.LINE_NO = '"+hashData.get("LINE_NO")+"'";
					//System.out.println("Test : "+this.sql);
					//System.out.println("UPDATE NONE PARTIAL = " + this.sql);
					try {
						dbConnNew.insert(this.sql);
						dbConnNew.commitDB();
					} catch (SQLException e) {
						result = false;
						System.out.println("Error Update Partial \"TRN_DAILY\" => " + e.getMessage());
					}
				}
			}//end loop for
		
			this.sql = "UPDATE TRN_DAILY SET "
			+ "PAY_BY_CASH_AR = 'Y', PAY_BY_AR = 'N' "
			+ "WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' "
			+ "AND INVOICE_DATE LIKE '"+yyyy+mm+"%' "
			+ "AND IS_PARTIAL = 'Y' ";
			//System.out.println("UPDATE NONE PARTIAL = " + this.sql);
			try {
				dbConnNew.insert(this.sql);
				dbConnNew.commitDB();
			} catch (SQLException e) {
				result = false;
				System.out.println("Error Update Partial in Month \"TRN_DAILY\" => " + e.getMessage());
			}

			this.sql = "UPDATE INT_ERP_AR_RECEIPT SET "
			+ "IS_LOADED = 'Y' "
			+ "WHERE TRANSACTION_DATE LIKE '"+yyyy+mm+"%' AND HOSPITAL_CODE = '"
			+ this.hospitalCode+"' AND IS_LAST_RECEIPT = 'N'";
			try {
				//System.out.println("Update : "+this.sql);
				dbConnNew.insert(this.sql);
				dbConnNew.commitDB();
			} catch (SQLException e) {
				System.out.println("Error Update \"INT_ERP_AR_RECEIPT\" => " + e.getMessage());
				result = false;
			}
			dbConnNew.closeDB("");
		return result;
	}

	private ArrayList<HashMap<String, String>> getLineNo(String startDate, String endDate){
		this.dbConn = new DBConn();
		ArrayList<HashMap<String, String>> arrData = new ArrayList<HashMap<String, String>>();
		
		if(getLineNoCombineBill( this.hospitalCode,startDate,endDate)){
			this.sql = "SELECT DISTINCT T.LINE_NO, T.OLD_DR_AMT, I.RECEIPT_DATE, I.RECEIPT_NO, I.TRANSACTION_DATE, "
					+ "T.INVOICE_NO, T.INVOICE_DATE, T.INVOICE_TYPE "
					+ "FROM TRN_DAILY T ,INT_ERP_AR_RECEIPT I "//,INT_ERP_AR_RECEIPT I
					+ "WHERE T.HOSPITAL_CODE ='"+this.hospitalCode+"' AND T.INVOICE_NO = I.BILL_NO "
					+ "AND T.HOSPITAL_CODE = I.HOSPITAL_CODE "
					+ "AND (I.TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') "
					+ "AND I.DOC_TYPE= 'R' AND I.IS_LAST_RECEIPT ='N' AND T.IS_PARTIAL='N' AND T.BATCH_NO = '' "
					+ "AND T.YYYY = '' "
					+ "AND T.ACTIVE = '1' "
					//+ "AND I.IS_LOADED = 'N' "
					+ "ORDER BY T.INVOICE_NO, T.LINE_NO";
					try {
						this.dbConn.setStatement();
						System.out.println(this.sql);
						arrData = this.dbConn.getMultiData(this.sql);
						this.dbConn.closeDB("");
					} catch (SQLException e) {
						System.out.println("Partial Payment Process Error on statement : "+this.sql);
					}
		}else{}
		return arrData;
	}
	
	//Combine bill
	public boolean getLineNoCombineBill(String hospitalCode,String startDate, String endDate){
		this.dbConn = new DBConn();
		this.hospitalCode = hospitalCode;
		//ArrayList<HashMap<String, String>> arrData = new ArrayList<HashMap<String, String>>();
		String qMessage="";
		boolean result =true;
		//Update Payment Amount if exceed or equal Bill Amount
		String sqlUpdatePartial = "UPDATE INT_ERP_AR_RECEIPT SET IS_LAST_RECEIPT = 'Y', PAYMENT_AMOUNT = BILL_AMOUNT "
			  +	"WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' "
			  +	"AND DOC_TYPE = 'R' AND IS_LAST_RECEIPT = 'N' AND PAYMENT_AMOUNT >= BILL_AMOUNT";
		
		//Insert Combind bill case N/N or N in month
		String sqlCombineBillNew = "INSERT INTO INT_ERP_AR_RECEIPT "
			  +	"SELECT IAR.HOSPITAL_CODE,IAR.BILL_NO, I3.RECEIPT_NO,I3.RECEIPT_DATE,IAR.RECEIPT_TYPE_CODE,"
			  + "IAR.BILL_AMOUNT,IAR.CREDIT_NOTE_AMOUNT, IAR.DEBIT_NOTE_AMOUNT, PAYMENT_AMT, "
			  + "IAR.WRITE_OFF_AMOUNT,IAR.DOC_TYPE,IAR.IS_LAST_RECEIPT,I3.TRANSACTION_DATE, IAR.UPDATE_DATE, "
			  + "IAR.UPDATE_TIME,'COMBINE' AS USER_ID,IAR.IS_LOADED "
			  + "FROM INT_ERP_AR_RECEIPT IAR JOIN ( "
			  + "SELECT I.HOSPITAL_CODE, I.BILL_NO, I2.RECEIPT_DATE, I2.RECEIPT_NO, I2.TRANSACTION_DATE, PAYMENT_AMT "
			  + "FROM INT_ERP_AR_RECEIPT I JOIN ( "
			  + "SELECT INT_ERP_AR_RECEIPT.HOSPITAL_CODE, INT_ERP_AR_RECEIPT.BILL_NO, RECEIPT_NO,RECEIPT_DATE,TRANSACTION_DATE, "
			  + "ROW_NUMBER() OVER(PARTITION BY INT_ERP_AR_RECEIPT.BILL_NO ORDER BY INT_ERP_AR_RECEIPT.BILL_NO ) AS PT_NUM, "
			  + "PAYMENT_AMT FROM INT_ERP_AR_RECEIPT LEFT OUTER JOIN ( "
			  + "SELECT HOSPITAL_CODE, BILL_NO, SUM(PAYMENT_AMOUNT) AS PAYMENT_AMT FROM INT_ERP_AR_RECEIPT "
			  + "WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' AND HOSPITAL_CODE='"+this.hospitalCode+"' "
			  + "AND DOC_TYPE+IS_LAST_RECEIPT='RN' GROUP BY HOSPITAL_CODE, BILL_NO HAVING COUNT(*) > 1) A "
			  + "ON INT_ERP_AR_RECEIPT.HOSPITAL_CODE = A.HOSPITAL_CODE AND INT_ERP_AR_RECEIPT.BILL_NO = A.BILL_NO "
			  + "WHERE A.HOSPITAL_CODE='"+this.hospitalCode+"' AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' ) I2 "
			  + "ON I.HOSPITAL_CODE =I2.HOSPITAL_CODE AND I.BILL_NO = I2.BILL_NO AND I.RECEIPT_NO = I2.RECEIPT_NO WHERE PT_NUM = 1) I3 "
			  + "ON I3.HOSPITAL_CODE = IAR.HOSPITAL_CODE and I3.BILL_NO = IAR.BILL_NO AND I3.RECEIPT_DATE = IAR.RECEIPT_DATE "
			  + "WHERE IAR.TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' AND IS_LAST_RECEIPT = 'N' "
			  + "AND IAR.HOSPITAL_CODE = '"+this.hospitalCode+"'"; 
		/*
		String sqlCombineBill = "INSERT INTO INT_ERP_AR_RECEIPT "
				 +"SELECT IAR.HOSPITAL_CODE,IAR.BILL_NO, I3.RECEIPT_NO,I3.RECEIPT_DATE,IAR.RECEIPT_TYPE_CODE,IAR.BILL_AMOUNT,IAR.CREDIT_NOTE_AMOUNT, " 
				 +"IAR.DEBIT_NOTE_AMOUNT,SUM(IAR.PAYMENT_AMOUNT)AS PAYMENT_AMONT,IAR.WRITE_OFF_AMOUNT,IAR.DOC_TYPE,IAR.IS_LAST_RECEIPT,I3.TRANSACTION_DATE, "
				 + "IAR.UPDATE_DATE,IAR.UPDATE_TIME,'COMBINE' AS USER_ID,IAR.IS_LOADED from INT_ERP_AR_RECEIPT IAR "
				 + "JOIN ( SELECT I.HOSPITAL_CODE, I.BILL_NO, I.RECEIPT_DATE, I.RECEIPT_NO,I.TRANSACTION_DATE FROM INT_ERP_AR_RECEIPT I JOIN( "
				 + "SELECT HOSPITAL_CODE,BILL_NO ,RECEIPT_NO,RECEIPT_DATE,TRANSACTION_DATE, ROW_NUMBER() OVER(PARTITION BY BILL_NO ORDER BY BILL_NO )AS PT_NUM  "
				 + "FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE='"+this.hospitalCode+"'AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  AND BILL_NO IN ( "
				 + "SELECT BILL_NO  FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' AND HOSPITAL_CODE='"+this.hospitalCode+"' "
				 + "AND BILL_NO IN (SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' "
				 + "AND HOSPITAL_CODE='"+this.hospitalCode+"' AND IS_LAST_RECEIPT='N')AND  "
				 + "BILL_NO NOT IN (SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  "
				 + "AND HOSPITAL_CODE='"+this.hospitalCode+"' AND IS_LAST_RECEIPT='Y') )AND BILL_NO IN ( SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE='"+this.hospitalCode+"' "
				 + "AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' GROUP BY BILL_NO HAVING COUNT(*)>1) )I2 ON I.HOSPITAL_CODE =I2.HOSPITAL_CODE "
				 + "AND I.BILL_NO = I2.BILL_NO AND I.RECEIPT_NO = I2.RECEIPT_NO WHERE PT_NUM =1 ) I3 ON I3.HOSPITAL_CODE = IAR.HOSPITAL_CODE and I3.BILL_NO = IAR.BILL_NO  "
				 + "WHERE IAR.TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' AND IS_LAST_RECEIPT = 'N' AND IAR.HOSPITAL_CODE = '"+this.hospitalCode+"' "
				 + "GROUP BY IAR.BILL_NO, I3.RECEIPT_DATE, I3.RECEIPT_NO,IAR.HOSPITAL_CODE,IAR.RECEIPT_TYPE_CODE, "
				 + "IAR.BILL_AMOUNT,IAR.CREDIT_NOTE_AMOUNT,IAR.DEBIT_NOTE_AMOUNT,IAR.WRITE_OFF_AMOUNT,IAR.DOC_TYPE, "
				 + "IAR.IS_LAST_RECEIPT,I3.TRANSACTION_DATE,IAR.UPDATE_DATE,IAR.UPDATE_TIME,IAR.USER_ID,IAR.IS_LOADED";
		*/
		//delete dupicate bill in month N/N
		String sqlDeleteBeforeCombineBill ="DELETE INT_ERP_AR_RECEIPT  WHERE HOSPITAL_CODE='"+this.hospitalCode+"'AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  AND BILL_NO IN ( "
				+ "SELECT BILL_NO   FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' AND HOSPITAL_CODE='"+this.hospitalCode+"' "
				+ "AND BILL_NO IN (SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  "
				+ "AND HOSPITAL_CODE='"+this.hospitalCode+"' AND IS_LAST_RECEIPT='N')AND  "
				+ "BILL_NO NOT IN (SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  "
				+ "AND HOSPITAL_CODE='"+this.hospitalCode+"' AND IS_LAST_RECEIPT='Y') )AND BILL_NO IN ( "
				+ "SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  "
				+ "GROUP BY BILL_NO HAVING COUNT(*)>1) AND ( USER_ID <>'COMBINE' OR USER_ID IS NULL )";
		//delete dupicate bill in month N/Y
		String sqlDeleteDupBill ="DELETE INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE='"+this.hospitalCode+"'AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  "
				+ "AND BILL_NO IN ( SELECT BILL_NO   FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' AND HOSPITAL_CODE='"+this.hospitalCode+"'  "
				+ "AND BILL_NO IN (SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  "
				+ "AND HOSPITAL_CODE='"+this.hospitalCode+"' AND IS_LAST_RECEIPT='N') "
				+ "AND BILL_NO IN (SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  "
				+ "AND HOSPITAL_CODE='"+this.hospitalCode+"' AND IS_LAST_RECEIPT='Y') "
				+ ")AND BILL_NO IN ( SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'  "
				+ "GROUP BY BILL_NO HAVING COUNT(*)>1) AND IS_LAST_RECEIPT='N'";
		
		try { 
			this.dbConn.setStatement(); 
			dbConn.insert(sqlUpdatePartial); 
			qMessage =sqlUpdatePartial;	
			dbConn.insert(sqlCombineBillNew); 
			qMessage =sqlCombineBillNew;	
			dbConn.insert(sqlDeleteBeforeCombineBill);
			qMessage =sqlDeleteBeforeCombineBill;	
			dbConn.insert(sqlDeleteDupBill);
			qMessage =sqlDeleteDupBill;	
			this.dbConn.commitDB();
			result = true; 
		} catch (Exception e1) { 
			dbConn.rollDB(); 
			result = false; 
			System.out.println("SQLException in stetment : "+qMessage); 
		}	
		//dbConn.closeDB("Close Connection from Partial Process"); 
		return result; 
	}
	
	private String backupTransactionMaster(){
		//Old
		String q = "INSERT INTO TRN_PARTIAL "+
				   "SELECT HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, RECEIPT_NO, RECEIPT_DATE, TRANSACTION_DATE, "+
				   "LINE_NO, INVOICE_TYPE, AMOUNT_AFT_DISCOUNT, DR_AMT, DR_TAX_401+DR_TAX_402+DR_TAX_406, "+
				   "'"+this.startDate+"', '"+JDate.getTime()+"' "+
				   "FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND INVOICE_NO IN "+
				   "(SELECT BILL_NO FROM INT_ERP_AR_RECEIPT "+
				   "WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' "+
				   "AND TRANSACTION_DATE BETWEEN '"+this.startDate+"' AND '"+this.endDate+"' "+
				   "AND IS_LAST_RECEIPT = 'N' "+
				   "GROUP BY BILL_NO HAVING COUNT(*) = 1 "+
				   //"AND BILL_NO IN "+
				   //"(SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' "+
				   //"GROUP BY BILL_NO, HOSPITAL_CODE HAVING COUNT(*) = 1 "+
				   //")"+
				   ")"+
				   "AND YYYY+MM = '' AND INVOICE_TYPE != 'ORDER' "+
				   "AND PAY_BY_AR = 'N' AND PAY_BY_CASH = 'N' AND PAY_BY_CASH_AR = 'N' AND PAY_BY_DOCTOR = 'N' AND PAY_BY_PAYOR = 'N' "+
				   "AND HOSPITAL_CODE+LINE_NO NOT IN (SELECT HOSPITAL_CODE+LINE_NO FROM TRN_PARTIAL WHERE HOSPITAL_CODE = '"+this.hospitalCode+"')";

		//New
		String qn = "INSERT INTO TRN_PARTIAL "+
				   "SELECT HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, RECEIPT_NO, RECEIPT_DATE, TRANSACTION_DATE, "+
				   "LINE_NO, INVOICE_TYPE, AMOUNT_AFT_DISCOUNT, DR_AMT, DR_TAX_401+DR_TAX_402+DR_TAX_406, "+
				   "'"+this.startDate+"', '"+JDate.getTime()+"' "+
				   "FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND YYYY+MM = '' AND INVOICE_NO IN "+
				   "("+
				   "SELECT DISTINCT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE BILL_NO IN "+
				   "("+
				   "SELECT BILL_NO FROM INT_ERP_AR_RECEIPT "+
				   "WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND "+
				   "BILL_NO NOT IN (SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND DOC_TYPE = 'R' AND IS_LAST_RECEIPT = 'Y') AND "+
				   "BILL_NO NOT IN (SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' AND DOC_TYPE = 'R' AND IS_LAST_RECEIPT = 'N' AND TRANSACTION_DATE NOT BETWEEN '"+this.startDate+"' AND '"+this.endDate+"'   GROUP BY BILL_NO HAVING COUNT(*)>1) "+
				   ") "+
				   "AND TRANSACTION_DATE BETWEEN '"+this.startDate+"' AND '"+this.endDate+"' "+
				   ") "+
				   "AND INVOICE_NO+LINE_NO NOT IN (SELECT INVOICE_NO+LINE_NO FROM TRN_PARTIAL WHERE HOSPITAL_CODE = '"+this.hospitalCode+"')";
		//System.out.println("TRN_PARTIAL : "+qn );
		return qn;
	}
	private String clearTransactionMaster(){
		return "DELETE TRN_PARTIAL WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND CREATE_DATE BETWEEN '"+this.startDate+"' AND '"+this.endDate+"'";
	}
	private boolean backUpData(String YYYY, String MM){
		//Backup Transaction in TRN_DAILY only match with transaction partial in INT_ERP_AR_RECEIPT
		System.out.println("Start Backup Data for Partial Process : "+JDate.getTime());
		boolean result =true;
		this.dbConn = new DBConn(false);
		this.sql = "UPDATE TRN_DAILY SET "
		+ "AMT_BEF_PARTIAL = AMOUNT_AFT_DISCOUNT, "
		+ "DR_AMT_BEF_PARTIAL = DR_AMT, "
		+ "DR_TAX_BEF_PARTIAL = DR_TAX_406+DR_TAX_402 "
		//+ "OLD_TAX_AMT = CASE WHEN TAX_TYPE_CODE = '406' THEN DR_TAX_406 ELSE '0' END "
		+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' "
		+ "AND INVOICE_NO IN ("
		+ "SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE = '"+hospitalCode+"' "
		+ "AND TRANSACTION_DATE LIKE '"+YYYY+MM+"%' "
		+ "AND DOC_TYPE= 'R' AND IS_LAST_RECEIPT ='N') "
		+ "AND IS_PARTIAL = 'N' AND BATCH_NO = ''";
		System.out.println(this.sql);
		try {
			this.dbConn.setStatement();
			System.out.println("Update TrnDaily");
			this.dbConn.insert(this.sql);
			System.out.println("Clean TrnPartial");
			this.dbConn.insert(this.clearTransactionMaster());
			System.out.println("BackupInMonth TrnPartial : "+this.backupTransactionMaster());
			this.dbConn.insert(this.backupTransactionMaster());
			this.dbConn.commitDB();
		} catch (SQLException e) {
			this.dbConn.rollDB();
			System.out.println("SQLException in method \"backUpData\" "+e.getMessage());
			result = false;
		}
		this.dbConn.closeDB("");
		System.out.println("Finish Backup Data for Partial Process : "+JDate.getTime());
		return result;
	}

	public boolean rollBack(String startDate, String endDate, String hospitalCode){ 
		boolean result = true; 
		this.dbConn = new DBConn(false); 
		String qMessage = ""; 

		String updateTrn = "UPDATE TRN_DAILY SET " 
		+ "AMOUNT_AFT_DISCOUNT = AMT_BEF_PARTIAL, " 
		+ "DR_AMT = DR_AMT_BEF_PARTIAL, " 
		+ "DR_TAX_406 = CASE WHEN TAX_TYPE_CODE = '406' THEN DR_TAX_BEF_PARTIAL ELSE '0' END, " 
		+ "DR_TAX_402 = CASE WHEN TAX_TYPE_CODE = '402' THEN DR_TAX_BEF_PARTIAL ELSE '0' END " 
		+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' " 
		+ "AND INVOICE_NO IN (" 
		+ "SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE = '"+hospitalCode+"' " 
		//+ "AND TRANSACTION_DATE LIKE '"+YYYY+MM+"%' " 
		+ "AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'" 
		+ "AND DOC_TYPE= 'R' AND IS_LAST_RECEIPT ='N') " 
		+ "AND IS_PARTIAL = 'N' AND BATCH_NO = ''"; 

		String updateIntErp = "UPDATE INT_ERP_AR_RECEIPT SET " 
		+ "IS_LOADED = 'N' " 
		+ "FROM INT_ERP_AR_RECEIPT I " 
		+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND I.DOC_TYPE= 'R' " 
		//+ "AND I.IS_LAST_RECEIPT ='N' AND TRANSACTION_DATE LIKE '"+YYYY+MM+"%'"; 
		+ "AND I.IS_LAST_RECEIPT ='N' AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'"; 

		String deleteTrn = "DELETE TRN_DAILY " 
		//OLD
		//+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND RECEIPT_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' "

		//NEW
		+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND RECEIPT_DATE IN "
		+ "(SELECT RECEIPT_DATE FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') " 
		+ "AND BATCH_NO = '' "
		
		+ "AND IS_PARTIAL = 'Y'"; 


		try { 
			this.dbConn.setStatement(); 
			if(this.dbConn.getSingleData("SELECT IS_PARTIAL FROM HOSPITAL WHERE CODE = '"+hospitalCode+"'").equals("Y")){ 
				System.out.println("Rollback Partial"); 
				qMessage = updateTrn; 
				dbConn.insert(updateTrn); 
				qMessage = updateIntErp; 
				dbConn.insert(updateIntErp); 
				qMessage = deleteTrn; 
				dbConn.insert(deleteTrn); 
				dbConn.commitDB();	
			}else{ 
				System.out.println("No Partial Condition"); 
			} 
		} catch (Exception e1) { 
			dbConn.rollDB(); 
			result = false; 
			System.out.println("SQLException in stetment : "+qMessage); 
		}	
		dbConn.closeDB("Close Connection from Partial Process"); 
		return result; 
	}
	
	public boolean rollBackOld(String YYYY, String MM, String hospitalCode){
		boolean result = true;
		this.dbConn = new DBConn(false);
		String qMessage = "";
		
		String updateTrn = "UPDATE TRN_DAILY SET "
			+ "AMOUNT_AFT_DISCOUNT = AMT_BEF_PARTIAL, "
			+ "DR_AMT = DR_AMT_BEF_PARTIAL, "
			+ "DR_TAX_406 = CASE WHEN TAX_TYPE_CODE = '406' THEN DR_TAX_BEF_PARTIAL ELSE '0' END, "
			+ "DR_TAX_402 = CASE WHEN TAX_TYPE_CODE = '402' THEN DR_TAX_BEF_PARTIAL ELSE '0' END "
			+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' "
			+ "AND INVOICE_NO IN ("
			+ "SELECT BILL_NO FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE = '"+hospitalCode+"' "
			+ "AND TRANSACTION_DATE LIKE '"+YYYY+MM+"%' "
			+ "AND DOC_TYPE= 'R' AND IS_LAST_RECEIPT ='N') "
			+ "AND IS_PARTIAL = 'N' AND BATCH_NO = ''";
		
		String updateIntErp = "UPDATE INT_ERP_AR_RECEIPT SET "
			+ "IS_LOADED = 'N' "
			+ "FROM INT_ERP_AR_RECEIPT I "
			+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND I.DOC_TYPE= 'R' "
			+ "AND I.IS_LAST_RECEIPT ='N' AND TRANSACTION_DATE LIKE '"+YYYY+MM+"%'";
		
		String deleteTrn = "DELETE TRN_DAILY "
			+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND YYYY = '"+YYYY+"' "
			+ "AND MM = '"+MM+"' AND IS_PARTIAL = 'Y'";
		
		
		try {
			this.dbConn.setStatement();
			if(this.dbConn.getSingleData("SELECT IS_PARTIAL FROM HOSPITAL WHERE CODE = '"+hospitalCode+"'").equals("Y")){
				System.out.println("Rollback Partial");
				qMessage = updateTrn;
				dbConn.insert(updateTrn);
				qMessage = updateIntErp;
				dbConn.insert(updateIntErp);
				qMessage = deleteTrn;
				dbConn.insert(deleteTrn);
				dbConn.commitDB();				
			}else{
				System.out.println("No Partial Condition");
			}
		} catch (Exception e1) {
			dbConn.rollDB();
			result = false;
			System.out.println("SQLException in stetment : "+qMessage);
		}		
		dbConn.closeDB("Close Connection from Partial Process");
		return result;
	}
}