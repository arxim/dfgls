/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.report;

/**
 *
 * @author
 */
public class ReportQuery {

    String hospital_code;
    String from_date;
    String to_date;
    String doctor_code;
    String doctor_profile_code;
    String doctor_category;
    String order_item;
    String order_item_category;
    String doctor_department;
    String transaction_type;
    String admission_type;
    String invoice_no;
    String doc_type;
    String transaction_module;
    String isOnward;
    String isPartial;
    String payorOffice;
    String year;
    String month;

    public String getReport(String n){
        String qr = "";
        if(n.equals("DailyChecklist")){
            qr = summaryDaily();
        }
        if(n.equals("ImportTransaction")){
            qr = importTransaction();
        }
        if(n.equals("NoVerifyTransaction")){
        	qr = noVerifyTransaction();
        }
        if(n.equals("GLAccru")){
        	qr = accuDetail();
        }
        if(n.equals("AccruDetails")){
        	qr = BNHAccruDetail();
        }
        if(n.equals("DailyGuaranteeChecklist")){
        	qr = dfTrnGuarantee();
        }
        if(n.equals("DailyGuarantee")){
        	qr = dailyGuarantee();
        }
        if(n.equals("MonthlyGuarantee")){
        	qr = monthlyGuarantee();
        }
        if(n.equals("XrayManagement")){
        	qr = this.xrayManagement();
        }
        if(n.equals("Accrue")){
        	qr = this.ac();        	
        }
        if(n.equals("GL")){
        	qr = this.gl();        	
        }

        return qr;
    }    
    public void setYear(String year){
    	this.year = year;
    }
    public void setMonth(String month){
    	this.month = month;
    }
    public String getIsOnward() {
		return isOnward;
	}
	public void setIsOnward(String isOnward) {
		this.isOnward = isOnward;
	}
	public String getIsPartial() {
		return isOnward;
	}
	public void setIsPartial(String isPartial) {
		this.isPartial = isPartial;
	}
	public void setHospitalCode(String n){
        this.hospital_code = n;
    }
    public void setFromDate(String n){
        this.from_date = n;
    }
    public void setToDate(String n){
        this.to_date = n;
    }
    public void setDoctorProfileCode(String n){
    	this.doctor_profile_code = n;
    }
    public void setDoctorCode(String n){
        this.doctor_code = n;
    }
    public void setDoctorCategory(String n){
        this.doctor_category = n;
    }
    public void setOrderItem(String n){
        this.order_item = n;
    }
    public void setOrderItemCategory(String n){
        this.order_item_category = n;
    }
    public void setDoctorDepartment(String n){
        this.doctor_department = n;
    }
    public void setTransactionType(String n){
        this.transaction_type = n;
    }
    public void setInvoiceNo(String n){
    	this.invoice_no = n;
    }
    public void setAdmissionType(String n){
        this.admission_type = n;
    }
    public void setDocType(String n){
        this.doc_type = n;
    }
    public void setTransactionModule(String n){
        this.transaction_module = n;
    }
    public void setPayorOffice(String n){
    	this.payorOffice = n;
    }
    private String dfTrnGuarantee(){
    	return "SELECT D.DOCTOR_TYPE_CODE, D.DOCTOR_PROFILE_CODE, T.DOCTOR_CODE, D.NAME_THAI, T.INVOICE_NO, T.INVOICE_DATE, " +
    		   "T.VERIFY_DATE, T.VERIFY_TIME, T.HN_NO, T.PATIENT_NAME, T.ADMISSION_TYPE_CODE, T.PATIENT_DEPARTMENT_CODE, " +
    		   "DP.DESCRIPTION, T.ORDER_ITEM_CODE, T.ORDER_ITEM_DESCRIPTION, T.AMOUNT_AFT_DISCOUNT "+
    		   "FROM TRN_DAILY T LEFT OUTER JOIN DOCTOR D ON T.HOSPITAL_CODE = D.HOSPITAL_CODE AND T.DOCTOR_CODE = D.CODE "+
    		   "LEFT OUTER JOIN DEPARTMENT DP ON T.HOSPITAL_CODE = DP.HOSPITAL_CODE AND T.PATIENT_DEPARTMENT_CODE = DP.CODE "+
    		   "WHERE T.HOSPITAL_CODE = '"+this.hospital_code+"' AND (TRANSACTION_DATE BETWEEN '"+this.from_date+"' AND '"+this.to_date+"') "+
    		   "AND (D.DOCTOR_TYPE_CODE = 'IT' OR D.DOCTOR_TYPE_CODE LIKE '%G%') AND D.ACTIVE = '1' "+
    		   "ORDER BY D.DOCTOR_PROFILE_CODE, D.DOCTOR_TYPE_CODE, D.CODE, T.INVOICE_DATE";
    }
    private String noVerifyTransaction(){
    	return "SELECT I.HOSPITAL_CODE, CASE WHEN I.ADMISSION_TYPE_CODE = 'I' THEN 'IPD' ELSE 'OPD' END AS ADMISSION_TYPE_CODE, "+
    	"I.INVOICE_NO, I.INVOICE_DATE, I.LINE_NO, I.RECEIPT_TYPE_CODE, I.HN_NO, I.PATIENT_NAME, I.EPISODE_NO, "+
    	"I.ORDER_ITEM_CODE, OI.DESCRIPTION_THAI, I.DOCTOR_CODE, I.AMOUNT_AFT_DISCOUNT "+
    	"FROM TRN_DAILY I LEFT OUTER JOIN ORDER_ITEM OI ON I.ORDER_ITEM_CODE = OI.CODE AND I.HOSPITAL_CODE = OI.HOSPITAL_CODE "+
    	"WHERE (I.INVOICE_DATE BETWEEN '"+this.from_date+"' AND '"+this.to_date+"') AND "+
    	"I.LINE_NO NOT IN (SELECT SUBSTRING(LINE_NO,1,LEN(LINE_NO)-1) " +
    	"FROM TRN_DAILY WHERE (INVOICE_DATE BETWEEN '"+this.from_date+"' AND '"+this.to_date+"') AND INVOICE_TYPE = 'RESULT') AND "+
    	"I.INVOICE_TYPE = 'ORDER' AND OI.IS_COMPUTE = 'Y' AND "+
    	"I.ADMISSION_TYPE_CODE LIKE '"+this.admission_type+"' AND "+
    	"I.TRANSACTION_TYPE LIKE '"+this.transaction_type+"' AND "+
    	"I.ORDER_ITEM_CODE LIKE '"+this.order_item+"' AND "+
    	"I.ORDER_ITEM_CATEGORY_CODE LIKE '"+this.order_item_category+"' AND "+
    	"I.RECEIPT_DEPARTMENT_CODE LIKE '"+this.doctor_department+"' AND "+
    	"I.HOSPITAL_CODE LIKE '"+this.hospital_code+"' "+
    	"ORDER BY I.ADMISSION_TYPE_CODE, I.INVOICE_NO, I.LINE_NO";
    }
    private String importTransaction(){
        return "SELECT I.HOSPITAL_CODE, CASE WHEN I.ADMISSION_TYPE_CODE = 'I' THEN 'IPD' ELSE 'OPD' END AS ADMISSION_TYPE_CODE, "+
        "I.BILL_NO, I.BILL_DATE, I.LINE_NO, I.RECEIPT_TYPE_CODE, I.HN_NO, I.PATIENT_NAME, I.EPISODE_NO, I.PAYOR_CODE, "+
        "I.PAYOR_NAME, I.ORDER_ITEM_CODE, I.ORDER_ITEM_DESCRIPTION, I.DOCTOR_CODE, I.DOCTOR_NAME, I.AMOUNT_BEF_DISCOUNT, "+
        "I.PATIENT_LOCATION_CODE, I.VERIFIED_DATE, I.VERIFIED_TIME, "+
        "I.AMOUNT_OF_DISCOUNT "+
        "FROM INT_HIS_BILL I LEFT OUTER JOIN ORDER_ITEM OI ON I.ORDER_ITEM_CODE = OI.CODE AND I.HOSPITAL_CODE = OI.HOSPITAL_CODE "+
        "WHERE (I.BILL_DATE BETWEEN '"+this.from_date+"' AND '"+this.to_date+"') AND "+
        "I.DOCTOR_CODE LIKE '"+this.doctor_code+"' AND "+
        "I.ADMISSION_TYPE_CODE LIKE '"+this.admission_type+"' AND "+
        "I.TRANSACTION_TYPE LIKE '"+this.transaction_type+"' AND "+
        "I.ORDER_ITEM_CODE LIKE '"+this.order_item+"' AND "+
        "I.HOSPITAL_CODE LIKE '"+this.hospital_code+"' AND "+
        "I.IS_ONWARD LIKE '"+this.getIsOnward()+"' "+
        "ORDER BY I.ADMISSION_TYPE_CODE, I.BILL_NO, I.LINE_NO";
    }
    private String accuDetail(){
    	return "SELECT DP.CODE AS PROFILE_CODE, DP.NAME_THAI AS PROFILE_NAME, "+
    	"SD.VERIFY_DATE AS 'VERIFY_DATE', SD.DOCTOR_CODE AS 'DOCTOR_CODE', "+
    	"SD.VERIFY_TIME AS 'START_TIME', SD.YYYY AS 'YYYY', SD.MM AS 'MM', "+
    	"DR.NAME_THAI AS 'NAME_THAI', SD.INVOICE_NO AS 'INVOICE_NO', "+
    	"SD.INVOICE_DATE AS 'INVOICE_DATE', SD.PATIENT_NAME AS 'PATIENT_NAME', "+
    	"SD.HN_NO AS 'HN_NO', SD.IS_PAID, SD.ADMISSION_TYPE_CODE AS 'ADMISSION_TYPE_CODE', "+
    	"SD.TRANSACTION_MODULE AS 'TRANSACTION_MODULE', SD.LINE_NO AS 'LINE_NO', "+
    	"SD.ORDER_ITEM_CODE AS 'ORDER_ITEM_CODE', SD.PAYOR_OFFICE_CODE, "+
    	"SD.PAYOR_OFFICE_NAME, SD.DR_AMT AS 'DR_AMT', SD.AMOUNT_AFT_DISCOUNT, "+
    	"SD.AMOUNT_BEF_DISCOUNT - SD.AMOUNT_OF_DISCOUNT AS 'OLD_AMOUNT', "+
    	"SD.GUARANTEE_PAID_AMT, SD.GUARANTEE_NOTE, OI.DESCRIPTION_THAI AS 'DESCRIPTION_THAI', "+
    	"SD.DR_TAX_402+SD.DR_TAX_406+SD.DR_TAX_401 AS 'DR_TAX_AMT', "+
    	"DR.DOCTOR_CATEGORY_CODE AS 'DOCTOR_CATEGORY_CODE' "+
    	
    	"FROM TRN_DAILY SD LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE "+
    	"AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
    	"LEFT OUTER JOIN DOCTOR_PROFILE DP ON (DR.DOCTOR_PROFILE_CODE = DP.CODE "+
    	"AND DR.HOSPITAL_CODE = DP.HOSPITAL_CODE) "+
    	"LEFT OUTER JOIN ORDER_ITEM OI ON (OI.CODE = SD.ORDER_ITEM_CODE "+
    	"AND OI.HOSPITAL_CODE = SD.HOSPITAL_CODE) "+
    	"WHERE (SD.INVOICE_DATE BETWEEN '00000000' AND '99999999') AND "+
    	"DP.CODE LIKE '"+this.doctor_profile_code+"%' AND " +
    	"SD.PATIENT_DEPARTMENT_CODE LIKE '"+this.doctor_department+"%' AND "+
    	"SD.PAYOR_OFFICE_CODE LIKE '"+this.payorOffice+"%' AND "+
    	"SD.ACTIVE = '1' AND DR.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND "+
    	"(SD.BATCH_NO = '' OR SD.BATCH_NO IS NULL) AND "+
    	"SD.INVOICE_TYPE <> 'ORDER' AND SD.IS_PAID <> 'N' AND "+
    	"SD.HOSPITAL_CODE LIKE '"+this.hospital_code+"%' "+
    	"ORDER BY PROFILE_CODE,DOCTOR_CODE,INVOICE_DATE";
    }
    private String summaryDaily(){
        return "SELECT S.HOSPITAL_CODE, CASE WHEN S.ADMISSION_TYPE_CODE = 'I' THEN 'IPD' ELSE 'OPD' END AS ADMISSION_TYPE_CODE, "+
        "S.TRANSACTION_TYPE, S.TRANSACTION_MODULE, S.INVOICE_NO, S.INVOICE_DATE,S.RECEIPT_TYPE_CODE, " +
        "S.HN_NO, S.LINE_NO, S.PATIENT_NAME, S.EPISODE_NO, S.PAYOR_OFFICE_CODE, S.PAYOR_OFFICE_NAME, " +
        "S.ORDER_ITEM_CODE, S.DOCTOR_CATEGORY_CODE, S.ORDER_ITEM_DESCRIPTION, S.DOCTOR_CODE, D.NAME_THAI, " +
        "S.AMOUNT_AFT_DISCOUNT, S.DOCTOR_DEPARTMENT_CODE, S.NOR_ALLOCATE_AMT, S.DR_AMT, S.HP_AMT, " +
        "S.DR_TAX_400+S.DR_TAX_401+S.DR_TAX_402+S.DR_TAX_406 AS DR_TAX_AMT, S.TAX_TYPE_CODE, S.NOR_ALLOCATE_PCT, S.IS_DISCHARGE_SUMMARY "+
        "FROM TRN_DAILY S LEFT OUTER JOIN DOCTOR D ON S.DOCTOR_CODE = D.CODE AND S.HOSPITAL_CODE = D.HOSPITAL_CODE "+
        "LEFT OUTER JOIN ORDER_ITEM OI ON S.ORDER_ITEM_CODE = OI.CODE AND S.HOSPITAL_CODE = OI.HOSPITAL_CODE "+
        "WHERE ((S.INVOICE_DATE BETWEEN '"+from_date+"' AND '"+to_date+"') or (S.RECEIPT_DATE BETWEEN '"+from_date+"' AND '"+to_date+"')) AND "+
        "S.TRANSACTION_MODULE LIKE '"+transaction_module+"' AND "+
        "S.ADMISSION_TYPE_CODE LIKE '"+admission_type+"' AND "+
        "S.TRANSACTION_TYPE LIKE '"+transaction_type+"' AND "+
        "D.DOCTOR_PROFILE_CODE LIKE '"+doctor_profile_code+"' AND "+
        "S.DOCTOR_CODE LIKE '"+doctor_code+"' AND "+
        "D.DEPARTMENT_CODE LIKE '"+doctor_department+"' AND "+
        "D.DOCTOR_CATEGORY_CODE LIKE '"+this.doctor_category+"' AND "+
        "S.ORDER_ITEM_CODE LIKE '"+order_item+"' AND "+
        "OI.ORDER_ITEM_CATEGORY_CODE LIKE '"+order_item_category+"' AND "+
        "S.INVOICE_NO LIKE '"+invoice_no+"' AND "+
        "S.IS_WRITE_OFF LIKE '"+doc_type+"' AND "+
        "S.ACTIVE = '1' AND "+
        "S.IS_ONWARD LIKE '"+this.getIsOnward()+"' AND "+
        "S.IS_PARTIAL LIKE '"+this.getIsPartial()+"' AND "+
        "S.HOSPITAL_CODE LIKE '"+hospital_code+"' "+
        "ORDER BY S.ADMISSION_TYPE_CODE, S.INVOICE_NO, S.LINE_NO";
    }
    private String BNHAccruDetail(){
        return "SELECT DP.CODE AS PROFILE_CODE, DP.NAME_THAI AS PROFILE_NAME, "+
        "SD.DOCTOR_CODE AS 'DOCTOR_CODE', DR.NAME_THAI AS 'NAME_THAI', "+
        "SD.INVOICE_DATE AS 'INVOICE_DATE', SD.INVOICE_NO AS 'INVOICE_NO', "+
        "SD.HN_NO AS 'HN_NO', SD.PATIENT_NAME AS 'PATIENT_NAME', "+
        "SD.PATIENT_DEPARTMENT_CODE, DPT.DESCRIPTION, "+
        "SD.AMOUNT_AFT_DISCOUNT, SD.DR_AMT AS 'DR_AMT' "+
        "FROM TRN_DAILY SD LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE "+
        "AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
        "LEFT OUTER JOIN DOCTOR_PROFILE DP ON (DR.DOCTOR_PROFILE_CODE = DP.CODE "+
        "AND DR.HOSPITAL_CODE = DP.HOSPITAL_CODE) "+
        "LEFT OUTER JOIN ORDER_ITEM OI ON (OI.CODE = SD.ORDER_ITEM_CODE "+
        "AND OI.HOSPITAL_CODE = SD.HOSPITAL_CODE) "+
        "LEFT OUTER JOIN DEPARTMENT DPT ON (DPT.CODE = SD.PATIENT_DEPARTMENT_CODE "+
        "AND DPT.HOSPITAL_CODE = SD.HOSPITAL_CODE) "+
        "WHERE (SD.INVOICE_DATE BETWEEN '00000000' AND '99999999') AND "+
        "DP.CODE LIKE '"+this.doctor_profile_code+"' AND SD.PATIENT_DEPARTMENT_CODE LIKE '"+this.doctor_department+"' AND "+
        "SD.ACTIVE = '1' AND DR.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND "+
        "(SD.BATCH_NO = '' OR SD.BATCH_NO IS NULL) AND "+
        "SD.INVOICE_TYPE <> 'ORDER' AND SD.IS_PAID <> 'N' AND "+
        "SD.HOSPITAL_CODE LIKE '"+this.hospital_code+"' "+
        "ORDER BY PROFILE_CODE, DOCTOR_CODE, INVOICE_DATE";
    }
    private String dailyGuarantee(){
    	return ""+
    	"SELECT Q.HOSPITAL_CODE, D.DOCTOR_PROFILE_CODE, Q.GUARANTEE_DR_CODE, D.NAME_THAI, SUM(GUARANTEE_AMOUNT) AS GUARANTEE_AMOUNT, " +
    	"SUM(AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, SUM(DR_AMT) AS DR_AMT, SUM(ABSORB_AMOUNT) AS ABSORB_AMOUNT "+
    	"FROM ("+
    	"SELECT T.HOSPITAL_CODE, T.GUARANTEE_DR_CODE, 0 AS GUARANTEE_AMOUNT, SUM(AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, SUM(DR_AMT) AS DR_AMT, 0 AS ABSORB_AMOUNT "+
    	"FROM TRN_PAYMENT T "+
    	"WHERE T.HOSPITAL_CODE = '"+this.hospital_code+"' AND T.GUARANTEE_TERM_YYYY <> '' AND T.GUARANTEE_TYPE = 'DLY' "+
    	"AND T.YYYY+MM = '"+this.year+this.month+"' "+
    	"AND T.GUARANTEE_NOTE NOT LIKE '%EXTRA%' "+
    	"GROUP BY T.HOSPITAL_CODE, T.GUARANTEE_DR_CODE "+
    	"UNION "+
    	"SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, SUM(GUARANTEE_AMOUNT) AS GUARANTEE_AMOUNT, 0 AS AMOUNT_AFT_DISCOUNT, 0 AS DR_AMT, SUM(HP402_ABSORB_AMOUNT) AS ABSORB_AMOUNT "+
    	"FROM STP_GUARANTEE WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND ACTIVE = '1' AND GUARANTEE_TYPE_CODE = 'DLY' "+
    	"AND YYYY+MM = '"+this.year+this.month+"' "+
    	"GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE "+
    	") Q LEFT OUTER JOIN DOCTOR D ON Q.GUARANTEE_DR_CODE = D.CODE AND Q.HOSPITAL_CODE = D.HOSPITAL_CODE "+
    	"WHERE Q.HOSPITAL_CODE = '"+this.hospital_code+"' "+
    	"GROUP BY Q.HOSPITAL_CODE, D.DOCTOR_PROFILE_CODE, Q.GUARANTEE_DR_CODE, D.NAME_THAI "+
    	"ORDER BY D.DOCTOR_PROFILE_CODE, Q.GUARANTEE_DR_CODE DESC";    	
    }
    private String monthlyGuarantee(){
    	return ""+
    	"SELECT Q.HOSPITAL_CODE, D.DOCTOR_PROFILE_CODE, Q.GUARANTEE_DR_CODE, D.GUARANTEE_START_DATE, D.GUARANTEE_EXPIRE_DATE, " +
    	"SUM(GUARANTEE_AMOUNT) AS GUARANTEE_AMOUNT, SUM(GUARANTEE_INCLUDE_AMOUNT) AS GUARANTEE_INCLUDE_AMOUNT, D.NAME_THAI, " +
    	"SUM(AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, SUM(DR_AMT) AS DR_AMT, SUM(ABSORB_AMOUNT) AS ABSORB_AMOUNT "+
    	"FROM ("+
    	"SELECT T.HOSPITAL_CODE, T.GUARANTEE_DR_CODE, 0 AS GUARANTEE_AMOUNT, 0 AS GUARANTEE_INCLUDE_AMOUNT, SUM(AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, SUM(DR_AMT) AS DR_AMT, 0 AS ABSORB_AMOUNT "+
    	"FROM TRN_PAYMENT T "+
    	"WHERE T.HOSPITAL_CODE = '"+this.hospital_code+"' AND T.GUARANTEE_TERM_YYYY <> '' AND T.GUARANTEE_TYPE LIKE 'M%' "+
    	"AND T.YYYY+MM = '"+this.year+this.month+"' "+
    	"AND T.GUARANTEE_NOTE NOT LIKE '%EXTRA%' "+
    	"GROUP BY T.HOSPITAL_CODE, T.GUARANTEE_DR_CODE "+
    	"UNION "+
    	"SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, SUM(GUARANTEE_AMOUNT) AS GUARANTEE_AMOUNT, SUM(GUARANTEE_INCLUDE_AMOUNT) AS GUARANTEE_INCLUDE_AMOUNT, 0 AS AMOUNT_AFT_DISCOUNT, 0 AS DR_AMT, " +
    	"SUM(HP402_ABSORB_AMOUNT) AS ABSORB_AMOUNT "+
    	"FROM STP_GUARANTEE WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND ACTIVE = '1' AND GUARANTEE_TYPE_CODE LIKE 'M%' "+
    	"AND YYYY+MM = '"+this.year+this.month+"' "+
    	"GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE "+
    	") Q LEFT OUTER JOIN DOCTOR D ON Q.GUARANTEE_DR_CODE = D.CODE AND Q.HOSPITAL_CODE = D.HOSPITAL_CODE "+
    	"WHERE Q.HOSPITAL_CODE = '"+this.hospital_code+"' "+
    	"GROUP BY Q.HOSPITAL_CODE, D.GUARANTEE_START_DATE, D.GUARANTEE_EXPIRE_DATE, D.DOCTOR_PROFILE_CODE, Q.GUARANTEE_DR_CODE, D.NAME_THAI "+
    	"ORDER BY D.DOCTOR_PROFILE_CODE, Q.GUARANTEE_DR_CODE DESC";
    }
    private String xrayManagement(){
    	return ""+
        "SELECT S.HOSPITAL_CODE, CASE WHEN S.ADMISSION_TYPE_CODE = 'I' THEN 'IPD' ELSE 'OPD' END AS ADMISSION_TYPE_CODE, "+
        "S.TRANSACTION_TYPE, S.TRANSACTION_MODULE, S.INVOICE_NO, S.INVOICE_DATE,S.RECEIPT_TYPE_CODE, " +
        "S.HN_NO, S.LINE_NO, S.PATIENT_NAME, S.EPISODE_NO, S.PAYOR_OFFICE_CODE, S.PAYOR_OFFICE_NAME, " +
        "S.ORDER_ITEM_CODE, S.DOCTOR_CATEGORY_CODE, S.ORDER_ITEM_DESCRIPTION, S.DOCTOR_CODE, D.NAME_THAI, " +
        "OI.ORDER_ITEM_CATEGORY_CODE, "+
        "S.AMOUNT_AFT_DISCOUNT, S.AMOUNT_OF_DISCOUNT, S.DOCTOR_DEPARTMENT_CODE, S.NOR_ALLOCATE_AMT, S.DR_AMT, S.HP_AMT, " +
        "S.DR_TAX_400+S.DR_TAX_401+S.DR_TAX_402+S.DR_TAX_406 AS DR_TAX_AMT, S.TAX_TYPE_CODE, S.NOR_ALLOCATE_PCT "+
        "FROM TRN_DAILY S LEFT OUTER JOIN DOCTOR D ON S.DOCTOR_CODE = D.CODE AND S.HOSPITAL_CODE = D.HOSPITAL_CODE "+
        "LEFT OUTER JOIN ORDER_ITEM OI ON S.ORDER_ITEM_CODE = OI.CODE AND S.HOSPITAL_CODE = OI.HOSPITAL_CODE "+
        "WHERE S.TRANSACTION_DATE LIKE '"+this.year+this.month+"%' AND "+
        "OI.ORDER_ITEM_CATEGORY_CODE LIKE '"+this.order_item_category+"' AND "+
        "S.HOSPITAL_CODE = '"+this.hospital_code+"' AND S.NOTE = 'XM' "+
        "ORDER BY S.ADMISSION_TYPE_CODE, S.INVOICE_NO, S.LINE_NO";
    }
    private String gl(){
    	return ""+
    	"SELECT 'DF' AS MODULE, TM.DOCTOR_CODE, DR.NAME_THAI, TM.HN_NO, TM.PATIENT_NAME, TM.INVOICE_NO, TM.LINE_NO, "+
    	"TM.INVOICE_DATE, TM.AMOUNT_AFT_DISCOUNT, TM.DR_AMT AS 'PAID_AMOUNT', TM.AMOUNT_AFT_DISCOUNT-TM.DR_AMT AS 'SHARING', "+
    	"CASE WHEN DR.DOCTOR_TYPE_CODE LIKE 'G%' THEN '61070003' ELSE OI.ACCOUNT_CODE END AS ACCOUNT_CODE, "+
    	//"OI.ACCOUNT_CODE, "+
    	"TM.PATIENT_DEPARTMENT_CODE, TM.TAX_TYPE_CODE "+
    	"FROM TRN_PAYMENT TM "+
    	"LEFT OUTER JOIN DOCTOR DR ON TM.DOCTOR_CODE = DR.CODE AND TM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN ORDER_ITEM OI ON TM.ORDER_ITEM_CODE = OI.CODE AND TM.HOSPITAL_CODE = OI.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DEPT ON TM.PATIENT_DEPARTMENT_CODE = DEPT.CODE AND TM.HOSPITAL_CODE = DEPT.HOSPITAL_CODE "+
    	"WHERE TM.HOSPITAL_CODE = '"+this.hospital_code+"' "+
    	"AND TM.YYYY+MM = '"+this.year+this.month+"' "+ //GL
    	"AND TM.ACTIVE = '1' "+
    	"AND DR.ACTIVE = '1' AND DR.PAYMENT_MODE_CODE != 'U' "+
    	"AND TM.LINE_NO+'|'+TM.INVOICE_NO IN "+
    	"(SELECT LINE_NO FROM TEMP_GL WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND YYYY+MM = '"+this.year+this.month+"' AND PROCESS = 'GL' AND AMOUNT_SIGN = '+')" +
    	"AND (TM.BATCH_NO = '"+this.year+this.month+"' OR TM.BATCH_NO = '') "+ //AC
    	"UNION ALL "+
    	"SELECT 'ADD' AS MODULE, TX.DOCTOR_CODE, DR.NAME_THAI, '' AS HN_NO, '' AS PATIENT_NAME, '' AS INVOICE_NO, TX.LINE_NO, "+
    	"'' AS INVOICE_DATE, TX.AMOUNT AS AMOUNT_AFT_DISCOUNT, TX.AMOUNT AS 'PAID_AMOUNT', '0' AS 'SHARING', "+
    	"TX.EXPENSE_ACCOUNT_CODE, TX.DEPARTMENT_CODE, TX.TAX_TYPE_CODE "+
    	"FROM TRN_EXPENSE_DETAIL TX "+
    	"LEFT OUTER JOIN DOCTOR DR ON TX.DOCTOR_CODE = DR.CODE AND TX.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DEPT ON TX.DEPARTMENT_CODE = DEPT.CODE AND TX.HOSPITAL_CODE = DEPT.HOSPITAL_CODE "+
    	"WHERE TX.HOSPITAL_CODE = '"+this.hospital_code+"' AND TX.YYYY+TX.MM = '"+this.year+this.month+"' "+
    	"AND DR.ACTIVE = '1' AND DR.PAYMENT_MODE_CODE != 'U' "+
    	"AND TX.EXPENSE_CODE IN (SELECT CODE FROM EXPENSE WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND GL_INTERFACE = 'Y') "+
    	"AND TX.EXPENSE_SIGN = '1' "+
    	"UNION ALL "+
    	"SELECT 'DEDUCT' AS MODULE, TX.DOCTOR_CODE, DR.NAME_THAI, '' AS HN_NO, '' AS PATIENT_NAME, '' AS INVOICE_NO, TX.LINE_NO, "+
    	"'' AS INVOICE_DATE, TX.AMOUNT AS AMOUNT_AFT_DISCOUNT, TX.AMOUNT AS 'PAID_AMOUNT', '0' AS 'SHARING', "+
    	"TX.EXPENSE_ACCOUNT_CODE, TX.DEPARTMENT_CODE, TX.TAX_TYPE_CODE "+
    	"FROM TRN_EXPENSE_DETAIL TX "+
    	"LEFT OUTER JOIN DOCTOR DR ON TX.DOCTOR_CODE = DR.CODE AND TX.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DEPT ON TX.DEPARTMENT_CODE = DEPT.CODE AND TX.HOSPITAL_CODE = DEPT.HOSPITAL_CODE "+
    	"WHERE TX.HOSPITAL_CODE = '"+this.hospital_code+"' AND TX.YYYY+TX.MM = '"+this.year+this.month+"' "+
    	"AND DR.ACTIVE = '1' AND DR.PAYMENT_MODE_CODE != 'U' "+
    	"AND TX.EXPENSE_CODE IN (SELECT CODE FROM EXPENSE WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND GL_INTERFACE = 'Y') "+
    	"AND TX.EXPENSE_SIGN = '-1' ";
    }
    private String ac(){
    	return ""+
    	"SELECT 'DF' AS MODULE, TM.DOCTOR_CODE, DR.NAME_THAI, TM.HN_NO, TM.PATIENT_NAME, TM.INVOICE_NO, TM.LINE_NO, "+
    	"TM.INVOICE_DATE, " +
    	"TM.AMOUNT_AFT_DISCOUNT, " +
        /*
    	"CASE WHEN TM.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' " +
        "THEN " +
        	"CASE WHEN TM.LINE_NO LIKE '%ADV' THEN TM.GUARANTEE_PAID_AMT " +
        	"ELSE TM.AMOUNT_AFT_DISCOUNT - TM.GUARANTEE_PAID_AMT END " +
        "ELSE TM.AMOUNT_AFT_DISCOUNT " +
        "END AS AMOUNT_AFT_DISCOUNT, "+
		*/
    	"TM.DR_AMT AS 'PAID_AMOUNT', TM.AMOUNT_AFT_DISCOUNT-TM.DR_AMT AS 'SHARING', "+
    	"CASE WHEN DR.DOCTOR_TYPE_CODE LIKE 'G%' THEN '61070003' ELSE OI.ACCOUNT_CODE END AS ACCOUNT_CODE, "+
    	"TM.PATIENT_DEPARTMENT_CODE, TM.TAX_TYPE_CODE "+
    	"FROM TRN_DAILY TM "+
    	"LEFT OUTER JOIN DOCTOR DR ON TM.DOCTOR_CODE = DR.CODE AND TM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN ORDER_ITEM OI ON TM.ORDER_ITEM_CODE = OI.CODE AND TM.HOSPITAL_CODE = OI.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DEPT ON TM.PATIENT_DEPARTMENT_CODE = DEPT.CODE AND TM.HOSPITAL_CODE = DEPT.HOSPITAL_CODE "+
    	"WHERE TM.HOSPITAL_CODE = '"+this.hospital_code+"' "+
    	//"AND TM.TRANSACTION_DATE <= '20140331' "+
    	"AND (TM.BATCH_NO = '"+this.year+this.month+"' OR TM.BATCH_NO = '') "+
    	"AND TM.ACTIVE = '1' AND TM.ORDER_ITEM_ACTIVE = '1' AND DR.ACTIVE = '1' "+
    	"AND TM.IS_PAID <> 'N' AND OI.ACCOUNT_CODE NOT LIKE '0%' AND TM.INVOICE_TYPE <> 'ORDER' "+
    	"AND TM.AMOUNT_AFT_DISCOUNT > 0 " +
    	//"AND DR.PAYMENT_MODE_CODE != 'U' " +
    	"AND TM.DOCTOR_CODE NOT LIKE '99999%' "+
    	"UNION ALL "+
    	"SELECT 'ADD' AS MODULE, TX.DOCTOR_CODE, DR.NAME_THAI, '' AS HN_NO, '' AS PATIENT_NAME, '' AS INVOICE_NO, TX.LINE_NO, "+
    	"'' AS INVOICE_DATE, TX.AMOUNT AS AMOUNT_AFT_DISCOUNT, TX.AMOUNT AS 'PAID_AMOUNT', '0' AS 'SHARING', "+
    	"TX.EXPENSE_ACCOUNT_CODE, TX.DEPARTMENT_CODE, TX.TAX_TYPE_CODE "+
    	"FROM TRN_EXPENSE_DETAIL TX "+
    	"LEFT OUTER JOIN DOCTOR DR ON TX.DOCTOR_CODE = DR.CODE AND TX.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DEPT ON TX.DEPARTMENT_CODE = DEPT.CODE AND TX.HOSPITAL_CODE = DEPT.HOSPITAL_CODE "+
    	"WHERE TX.HOSPITAL_CODE = '"+this.hospital_code+"' AND TX.YYYY+TX.MM = '"+this.year+this.month+"' "+
    	"AND DR.ACTIVE = '1' "+
    	"AND TX.EXPENSE_CODE IN (SELECT CODE FROM EXPENSE WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND AC_INTERFACE = 'Y') "+
    	"AND TX.EXPENSE_SIGN = '1' "+
    	"UNION ALL "+
    	"SELECT 'DEDUCT' AS MODULE, TX.DOCTOR_CODE, DR.NAME_THAI, '' AS HN_NO, '' AS PATIENT_NAME, '' AS INVOICE_NO, TX.LINE_NO, "+
    	"'' AS INVOICE_DATE, TX.AMOUNT AS AMOUNT_AFT_DISCOUNT, TX.AMOUNT AS 'PAID_AMOUNT', '0' AS 'SHARING', "+
    	"TX.EXPENSE_ACCOUNT_CODE, TX.DEPARTMENT_CODE, TX.TAX_TYPE_CODE "+
    	"FROM TRN_EXPENSE_DETAIL TX "+
    	"LEFT OUTER JOIN DOCTOR DR ON TX.DOCTOR_CODE = DR.CODE AND TX.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DEPT ON TX.DEPARTMENT_CODE = DEPT.CODE AND TX.HOSPITAL_CODE = DEPT.HOSPITAL_CODE "+
    	"WHERE TX.HOSPITAL_CODE = '"+this.hospital_code+"' AND TX.YYYY+TX.MM = '"+this.year+this.month+"' "+
    	"AND DR.ACTIVE = '1' "+
    	"AND TX.EXPENSE_CODE IN (SELECT CODE FROM EXPENSE WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND AC_INTERFACE = 'Y') "+
    	"AND TX.EXPENSE_SIGN = '-1' ";
    } 
}