package df.bean.db.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;

public class TrnDailyDAO {
	DBConn conn = null;
	String seq = "";
	String taxType = "";
	String doctorCategory = "";
	String userId = "";
	String hospital = "";
	String month = "";
	String year = "";
	String taxSource = "";
	String taxSQL = "";
	String startDate = "";
	String endDate = "";
	double taxAmt = 0.00;
	double drAmt = 0.00;
	double hpAmt = 0.00;
	double allocatePct = 0.00;
	double allocateAmt = 0.00;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public String getDoctorCategory() {
		return doctorCategory;
	}
	public void setDoctorCategory(String doctorCategory) {
		this.doctorCategory = doctorCategory;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public double getDrAmt() {
		return drAmt;
	}
	public void setDrAmt(double drAmt) {
		this.drAmt = drAmt;
	}
	public double getHpAmt() {
		return hpAmt;
	}
	public void setHpAmt(double hpAmt) {
		this.hpAmt = hpAmt;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getTaxSource() {
		return taxSource;
	}
	public void setTaxSource(String taxSource) {
		this.taxSource = taxSource;
	}
	public String getTaxSQL() {
		return taxSQL;
	}
	public void setTaxSQL(String taxSQL) {
		this.taxSQL = taxSQL;
	}
	public double getAllocatePct() {
		return allocatePct;
	}
	public void setAllocatePct(double allocatePct) {
		this.allocatePct = allocatePct;
	}
	public double getAllocateAmt() {
		return allocateAmt;
	}
	public void setAllocateAmt(double allocateAmt) {
		this.allocateAmt = allocateAmt;
	}

	public TrnDailyDAO(){
		conn = new DBConn();
		try {
			conn.setStatement();
		} catch (SQLException e) {
		}
	}
	public TrnDailyDAO(boolean b){
		String sql = "UPDATE TRN_DAILY SET DR_AMT = ?, HP_AMT = ?, SEQ_STEP = ?, OLD_DR_AMT = ?, TAX_TYPE_CODE = ?, DR_TAX_406 = ?, "+ //1-6
				     "DR_TAX_402 = ?, DR_TAX_401 = ?, DR_TAX_400 = ?, DOCTOR_CATEGORY_CODE = ?, COMPUTE_DAILY_DATE = ?, "+ //7-11
				     "COMPUTE_DAILY_TIME = ?, NOR_ALLOCATE_PCT = ?, COMPUTE_DAILY_USER_ID = ?, NOR_ALLOCATE_AMT = ? "+ //12-15
				     "WHERE HOSPITAL_CODE = ? AND DOCTOR_CODE = ? AND INVOICE_NO = ? AND INVOICE_DATE = ? "+ //15-18
				     "AND TRANSACTION_DATE = ? AND LINE_NO = ? AND VERIFY_DATE = ? AND VERIFY_TIME = ?"; //19-22
		//System.out.println(sql);
		conn = new DBConn(b);
		conn.setPrepareStatement(sql);
	}

	public List<Map<String,Object>> getTrnDaily(String hospitalCode, String startDate, String endDate){
		String sql = this.getTemplateForBasicAllocate()
		 		+ "WHERE TRN_DAILY.HOSPITAL_CODE = '"+hospitalCode+"' "
		 		+ "AND (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') "
		 		+ "AND AMOUNT_AFT_DISCOUNT > 0 AND INVOICE_TYPE != 'ORDER' "
				+ "AND (BATCH_NO IS NULL OR BATCH_NO = '') "
		 		+ "ORDER BY VERIFY_DATE, VERIFY_TIME ";
		 try {
			 conn.setStatement();
			 return conn.queryList(sql);
		 } catch (Exception e) {
			 System.out.println("Error : "+e);
			 return null;
		 }
	}
	private String getTemplateForBasicAllocate(){
		return 	"SELECT TRN_DAILY.INVOICE_NO, TRN_DAILY.INVOICE_DATE, TRN_DAILY.ADMISSION_TYPE_CODE AS ADMISSION_TYPE, TRN_DAILY.TRANSACTION_DATE, "+
				"TRN_DAILY.RECEIPT_DATE, TRN_DAILY.VERIFY_DATE, TRN_DAILY.VERIFY_TIME, TRN_DAILY.LINE_NO, TRN_DAILY.ORDER_ITEM_CODE AS ORDER_ITEM, "+
				"ORDER_ITEM.ORDER_ITEM_CATEGORY_CODE AS ORDER_ITEM_CATEGORY, ORDER_ITEM.IS_PROCEDURE, "+
				"TRN_DAILY.PAYOR_OFFICE_CODE AS PAYER_OFFICE, PAYOR_OFFICE.PAYOR_OFFICE_CATEGORY_CODE AS PAYOR_CATEGORY, "+
				"TRN_DAILY.DOCTOR_CODE AS DOCTOR, DOCTOR.DOCTOR_CATEGORY_CODE AS DOCTOR_CATEGORY, DOCTOR.DEPARTMENT_CODE AS DOCTOR_DEPARTMENT_CODE, "+
				// "TRN_DAILY.DOCTOR_PRIVATE_CODE, TRN_DAILY.INVOICE_TYPE AS DOCTOR_TREATMENT, TRN_DAILY.AMOUNT_AFT_DISCOUNT, "+
				"TRN_DAILY.DOCTOR_PRIVATE_CODE, CASE WHEN TRN_DAILY.INVOICE_TYPE = 'ORDER' THEN TRN_DAILY.INVOICE_TYPE ELSE 'EXECUTE' END AS DOCTOR_TREATMENT, TRN_DAILY.AMOUNT_AFT_DISCOUNT, "+
				"TRN_DAILY.DR_AMT, TRN_DAILY.HP_AMT, TRN_DAILY.OLD_DR_AMT, TRN_DAILY.TAX_TYPE_CODE, TRN_DAILY.DR_TAX_406, TRN_DAILY.DR_TAX_402, "+
				"TRN_DAILY.DR_TAX_401, TRN_DAILY.TAX_FROM_ALLOCATE, TRN_DAILY.DR_TAX_400, TRN_DAILY.COMPUTE_DAILY_DATE, TRN_DAILY.COMPUTE_DAILY_TIME, "+
				"TRN_DAILY.COMPUTE_DAILY_USER_ID, TRN_DAILY.NOR_ALLOCATE_AMT, TRN_DAILY.NOR_ALLOCATE_PCT, TRN_DAILY.IS_GUARANTEE_FROM_ALLOC, "+
				"TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.SEQ_STEP "+
				"FROM TRN_DAILY LEFT OUTER JOIN DOCTOR "+
				"ON TRN_DAILY.HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE AND TRN_DAILY.DOCTOR_CODE = DOCTOR.CODE "+
				"LEFT OUTER JOIN ORDER_ITEM "+
				"ON TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE AND TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE "+
				"AND ORDER_ITEM.IS_COMPUTE='Y' "+
				"LEFT OUTER JOIN PAYOR_OFFICE "+
				"ON TRN_DAILY.HOSPITAL_CODE = PAYOR_OFFICE.HOSPITAL_CODE AND TRN_DAILY.PAYOR_OFFICE_CODE = PAYOR_OFFICE.CODE ";
	}
	private String getTemplateForRollbackBasicCalculate(){
		return "UPDATE TRN_DAILY SET DR_AMT = 0.00, HP_AMT = 0.00, SEQ_STEP = null, OLD_DR_AMT = 0.00, TAX_TYPE_CODE = '', "+
			   "DR_TAX_400 = 0.00, DR_TAX_401 = 0.00, DR_TAX_402 = 0.00, DR_TAX_406 = 0.00, DOCTOR_CATEGORY_CODE = '', "+
			   "COMPUTE_DAILY_DATE = '', COMPUTE_DAILY_TIME = '', NOR_ALLOCATE_PCT = 0.00, NOR_ALLOCATE_AMT = 0.00, "+
			   "COMPUTE_DAILY_USER_ID = '', IS_GUARANTEE_FROM_ALLOC = '', ORDER_ITEM_CATEGORY_CODE = '', OLD_TAX_AMT = 0.00, TAX_FROM_ALLOCATE = '' ";
	}
	public void prepareSelectCalculate(){
		String sql = this.getTemplateForBasicAllocate()+
					 "WHERE TRN_DAILY.HOSPITAL_CODE = ? AND INVOICE_NO = ? AND (TRANSACTION_DATE BETWEEN ? AND ?) AND LINE_NO = ? "+
					 "AND AMOUNT_AFT_DISCOUNT > 0 AND INVOICE_TYPE != 'ORDER' "+
					 "AND (COMPUTE_DAILY_DATE IS NULL OR COMPUTE_DAILY_DATE = '') "+
					 "AND (BATCH_NO IS NULL OR BATCH_NO = '') "+
					 "AND ORDER_ITEM.IS_COMPUTE='Y' "+
					 "ORDER BY TRANSACTION_DATE, INVOICE_NO, DOCTOR_CODE";
		conn.setPrepareStatement(sql);
	}
	public List<Map<String,Object>> getPsTrnDailyForBasicCalculate(String hospitalCode, String startDate, String endDate, String invoiceNo, String lineNo){
		try {
			conn.getPrepareStatement().setString(1, hospitalCode);
			conn.getPrepareStatement().setString(2, invoiceNo);
			conn.getPrepareStatement().setString(3, JDate.saveDate(startDate));
			conn.getPrepareStatement().setString(4, JDate.saveDate(endDate));
			conn.getPrepareStatement().setString(5, lineNo);
		} catch (SQLException e) {
			System.out.println("Get transaction for basic allocate each line no error : "+e);
		}
		return conn.queryPsList();
	}
	public void prepareUpdateCalculate(){
		String sql = "UPDATE TRN_DAILY SET DR_AMT = ?, HP_AMT = ?, SEQ_STEP = ?, OLD_DR_AMT = ?, TAX_TYPE_CODE = ?, DR_TAX_406 = ?, "+ //1-6
				     "DR_TAX_402 = ?, DR_TAX_401 = ?, DR_TAX_400 = ?, DOCTOR_CATEGORY_CODE = ?, COMPUTE_DAILY_DATE = ?, "+ //7-11
				     "COMPUTE_DAILY_TIME = ?, NOR_ALLOCATE_PCT = ?, COMPUTE_DAILY_USER_ID = ?, NOR_ALLOCATE_AMT = ?, "+ //12-15
				     "IS_GUARANTEE_FROM_ALLOC = ?, ORDER_ITEM_CATEGORY_CODE = ?, OLD_TAX_AMT = ?, TAX_FROM_ALLOCATE = ?, "+ //16-19
				     "DOCTOR_DEPARTMENT_CODE = ? "+ //20
				     "WHERE HOSPITAL_CODE = ? AND INVOICE_NO = ? AND INVOICE_DATE = ? AND RECEIPT_DATE = ? AND VERIFY_DATE = ? "+ //21-25
				     "AND VERIFY_TIME = ? AND TRANSACTION_DATE = ? AND DOCTOR_CODE = ? AND LINE_NO = ?"; //26-29
		conn.setPrepareStatement(sql);
	}
	public void updatePrepareCalculate(Map<String, Object> m){
		String taxAmount = m.get("TAX_FROM_ALLOCATE").toString().equals("BF") ? m.get("AMOUNT_AFT_DISCOUNT").toString() : m.get("DR_AMT").toString();
		try{
			conn.getPrepareStatement().setDouble(1, Double.parseDouble(m.get("DR_AMT").toString()));
			conn.getPrepareStatement().setDouble(2, Double.parseDouble(m.get("HP_AMT").toString()));
			conn.getPrepareStatement().setString(3, m.get("SEQ_STEP").toString());
			conn.getPrepareStatement().setDouble(4, Double.parseDouble(m.get("DR_AMT").toString()));
			conn.getPrepareStatement().setString(5, m.get("TAX_TYPE_CODE").toString());
			conn.getPrepareStatement().setString(6, m.get("TAX_TYPE_CODE").toString().equals("406") ? taxAmount : "0" );
			conn.getPrepareStatement().setString(7, m.get("TAX_TYPE_CODE").toString().equals("402") ? taxAmount : "0" );
			conn.getPrepareStatement().setString(8, m.get("TAX_TYPE_CODE").toString().equals("401") ? taxAmount : "0" );
			conn.getPrepareStatement().setString(9, m.get("TAX_TYPE_CODE").toString().equals("400") ? taxAmount : "0" );
			conn.getPrepareStatement().setString(10, m.get("DOCTOR_CATEGORY").toString());
			conn.getPrepareStatement().setString(11, JDate.getDate());
			conn.getPrepareStatement().setString(12, JDate.getTime());
			conn.getPrepareStatement().setString(13, m.get("NOR_ALLOCATE_PCT").toString());
			conn.getPrepareStatement().setString(14, m.get("COMPUTE_DAILY_USER_ID").toString());
			conn.getPrepareStatement().setString(15, m.get("NOR_ALLOCATE_AMT").toString());
			conn.getPrepareStatement().setString(16, m.get("IS_GUARANTEE_FROM_ALLOC").toString());
			conn.getPrepareStatement().setString(17, m.get("ORDER_ITEM_CATEGORY").toString());
			conn.getPrepareStatement().setString(18, taxAmount);
			conn.getPrepareStatement().setString(19, m.get("TAX_FROM_ALLOCATE").toString().equals("BF") ? "N" : "Y");
			conn.getPrepareStatement().setString(20, m.get("DOCTOR_DEPARTMENT_CODE").toString());
			//WHERE CONDITION BELOW
			conn.getPrepareStatement().setString(21, m.get("HOSPITAL_CODE").toString());
			conn.getPrepareStatement().setString(22, m.get("INVOICE_NO").toString());
			conn.getPrepareStatement().setString(23, m.get("INVOICE_DATE").toString());
			conn.getPrepareStatement().setString(24, m.get("RECEIPT_DATE").toString());
			conn.getPrepareStatement().setString(25, m.get("VERIFY_DATE").toString());
			conn.getPrepareStatement().setString(26, m.get("VERIFY_TIME").toString());
			conn.getPrepareStatement().setString(27, m.get("TRANSACTION_DATE").toString());
			conn.getPrepareStatement().setString(28, m.get("DOCTOR").toString());
			conn.getPrepareStatement().setString(29, m.get("LINE_NO").toString());
			//DOCTOR_DEPARTMENT_CODE			
			conn.getPrepareStatement().executeUpdate();			
			conn.commitDB();
		}catch (Exception e){
			System.out.println("Update Step Allocate = "+e+" by line No "+m.get("LINE_NO").toString());
		}
	}	
	public List<Map<String,Object>> getTrnDailyForUpdateBillNotPrint(String hospitalCode, String startDate, String endDate){
		String sql = 
				"SELECT OW.HOSPITAL_CODE, OW.INVOICE_NO, T.INVOICE_NO AS BILL_NO, "+
				"CASE WHEN OW.YYYY != '' THEN OW.RECEIPT_NO ELSE T.RECEIPT_NO END AS RECEIPT_NO, "+
				"CASE WHEN OW.YYYY != '' THEN OW.RECEIPT_DATE ELSE T.RECEIPT_DATE END AS RECEIPT_DATE, "+
				"CASE WHEN OW.YYYY != '' THEN OW.YYYY ELSE T.YYYY END AS YYYY, "+
				"CASE WHEN OW.YYYY != '' THEN OW.MM ELSE T.MM END AS MM, "+
				"CASE WHEN OW.YYYY != '' THEN OW.RECEIPT_TYPE_CODE ELSE T.RECEIPT_TYPE_CODE END AS RECEIPT_TYPE_CODE, "+
				"T.TRANSACTION_MODULE, OW.LINE_NO, CASE WHEN OW.YYYY = '' THEN 'Y' ELSE OW.PAY_BY_CASH END PAY_BY_CASH, OW.TRANSACTION_DATE "+
				"FROM TRN_DAILY AS T INNER JOIN "+

				"(SELECT HOSPITAL_CODE, HN_NO, EPISODE_NO, LINE_NO, INVOICE_NO, "+
				"RECEIPT_NO, RECEIPT_DATE, RECEIPT_TYPE_CODE, TRANSACTION_DATE, PAY_BY_CASH, YYYY, MM "+
				"FROM TRN_DAILY "+
				"WHERE (INVOICE_NO LIKE HN_NO+EPISODE_NO+'%' OR RECEIPT_NO LIKE HN_NO+EPISODE_NO+'%') AND HOSPITAL_CODE = '"+hospitalCode+"' "+
				"AND INVOICE_DATE != '' AND TRANSACTION_DATE != '' "+
				"AND ACTIVE != '' AND LINE_NO NOT LIKE 'ADD%') AS OW "+

				"ON T.HOSPITAL_CODE = OW.HOSPITAL_CODE "+
				"AND T.HN_NO = OW.HN_NO AND T.EPISODE_NO = OW.EPISODE_NO "+
				"AND (T.LINE_NO = OW.LINE_NO OR T.LINE_NO+'ADV' = OW.LINE_NO) "+
				"WHERE T.HOSPITAL_CODE = '"+hospitalCode+"' AND T.INVOICE_NO NOT LIKE T.HN_NO+T.EPISODE_NO+'%' "+
				"AND T.INVOICE_DATE != '' AND (T.TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') "+
				"AND T.ACTIVE != '' AND T.LINE_NO NOT LIKE 'ADD%'";
		 try {
			 System.out.println(sql);
			 conn.setStatement();
			 return conn.queryList(sql);
		 } catch (Exception e) {
			 System.out.println("Error : "+e);
			 return null;
		 }
	}
	public List<Map<String,Object>> getTrnDailyForStepCalByDoctor(String hospitalCode, String admissionType, String doctorCategory, String doctorCode,String month,String year){
		 String admiss = "";
				 if(admissionType.equals("U")){
					 admiss="";
				 }else{
					 admiss="AND ADMISSION_TYPE_CODE = '"+admissionType+"' ";					 
				 }

		 String sql = "SELECT DOCTOR_CODE, TRN_DAILY.HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, TRANSACTION_DATE, RECEIPT_NO, RECEIPT_DATE, VERIFY_DATE,"
		 		+ "VERIFY_TIME, LINE_NO, AMOUNT_AFT_DISCOUNT, DR_AMT "
		 		+ "FROM TRN_DAILY LEFT OUTER JOIN DOCTOR ON TRN_DAILY.HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE AND TRN_DAILY.DOCTOR_CODE = DOCTOR.CODE "
		 		+ "LEFT OUTER JOIN ORDER_ITEM ON TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "
		 		+ "WHERE (BATCH_NO = '' OR BATCH_NO IS NULL) AND TRN_DAILY.HOSPITAL_CODE = '"+hospitalCode+"' "
		 		+ "AND TRANSACTION_DATE BETWEEN '"+year+month+"01' AND '"+year+month+"31' "
		 		//+ "AND (TRANSACTION_DATE BETWEEN '"+year+month+"01' AND '"+year+month+"31') AND (VERIFY_DATE BETWEEN '"+year+month+"01' AND '"+year+month+"31') "
		 		+ admiss
		 		+ "AND DOCTOR.DOCTOR_CATEGORY_CODE = '"+doctorCategory+"' "
		 		+ "AND DOCTOR.DOCTOR_PROFILE_CODE = '"+doctorCode+"' "
		 		//+ "AND ORDER_ITEM.IS_STEP_COMPUTE = CASE WHEN DOCTOR.DOCTOR_TYPE_CODE LIKE 'G%' THEN ORDER_ITEM.IS_STEP_COMPUTE ELSE 1 END "
		 		+ "AND ORDER_ITEM.IS_STEP_COMPUTE = '1' " //ALL DOCTOR TYPE
		 		+ "AND AMOUNT_AFT_DISCOUNT > 0 AND INVOICE_TYPE != 'ORDER' "
		 		+ "ORDER BY VERIFY_DATE, VERIFY_TIME ";
	
		 System.out.println("sql step sharing allocate="+sql);
		 try {
			 return conn.queryList(sql);
		 } catch (Exception e) {
			 System.out.println("Error : "+e);
			 return null;
		 }

	}
	public void updateBillNotPrint(Map<String, Object> m){
		//System.out.println(m);
		try{
			conn.getPrepareStatement().setString(1, m.get("BILL_NO").toString());
			conn.getPrepareStatement().setString(2, m.get("RECEIPT_NO").toString());
			conn.getPrepareStatement().setString(3, m.get("RECEIPT_DATE").toString());
			conn.getPrepareStatement().setString(4, m.get("TRANSACTION_MODULE").toString());
			conn.getPrepareStatement().setString(5, m.get("YYYY").toString());
			conn.getPrepareStatement().setString(6, m.get("MM").toString());
			conn.getPrepareStatement().setString(7, m.get("PAY_BY_CASH").toString());
			conn.getPrepareStatement().setString(8, m.get("RECEIPT_TYPE_CODE").toString());
			//WHERE CONDITION BELOW
			conn.getPrepareStatement().setString(9, m.get("HOSPITAL_CODE").toString());
			conn.getPrepareStatement().setString(10, m.get("INVOICE_NO").toString());
			conn.getPrepareStatement().setString(11, m.get("TRANSACTION_DATE").toString());
			conn.getPrepareStatement().setString(12, m.get("LINE_NO").toString());
			conn.getPrepareStatement().executeUpdate();			
			conn.commitDB();
		}catch (Exception e){
			System.out.println("Update Bill Not Print = "+e+" by line No "+m.get("LINE_NO").toString());
		}
	}
	public boolean updateInactiveBillNotPrint(String hospitalCode, String startDate, String endDate){
		boolean status = true;		
				
		String sqlMobile = 
				"UPDATE TRN_DAILY SET ACTIVE = '0' "+
				"FROM TRN_DAILY A WHERE HOSPITAL_CODE = '"+hospitalCode+"' "+ 
				"AND (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') "+
				"AND EPISODE_NO+LINE_NO IN (SELECT EPISODE_NO+LINE_NO FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+hospitalCode+"' "+ 
				"AND RECEIPT_NO LIKE HN_NO+EPISODE_NO+'%' AND TRANSACTION_DATE < '"+startDate+"')";
				
		String sqlOnward = 
				"UPDATE TRN_DAILY SET ACTIVE = '0' "+
				"FROM TRN_DAILY A WHERE HOSPITAL_CODE = '"+hospitalCode+"' "+ 
				"AND (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') "+
				"AND EPISODE_NO+LINE_NO IN (SELECT EPISODE_NO+LINE_NO FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+hospitalCode+"' "+ 
				//"AND (INVOICE_NO = A.INVOICE_NO AND IS_ONWARD = 'Y' AND TRANSACTION_DATE < '"+startDate+"') ) "+
				"AND (IS_ONWARD = 'Y' AND TRANSACTION_DATE < '"+startDate+"') ) "+
				"AND (IS_ONWARD != 'Y' OR IS_ONWARD IS NULL)";
		try{
			conn = new DBConn();
			conn.setStatement();
			conn.insert(sqlMobile);
			conn.insert(sqlOnward);
			conn.commitDB();
			status = true;
			System.out.println("Update Inactive Bill Not Print complete "+JDate.getTime());
		}catch(Exception e){
			status = false;
			System.out.println("Update Bill Not Print error = "+e);
		}
		return status;
	}			
	public void updatePrepareStepCalculate(Map<String, Object> m){
		//System.out.println(m);
		String taxAmount = this.getTaxSource().equals("BF") ? m.get("AMOUNT_AFT_DISCOUNT").toString() : m.get("DR_AMT").toString();
		try{
			//System.out.println(sql);
			conn.getPrepareStatement().setDouble(1, this.getDrAmt());
			conn.getPrepareStatement().setDouble(2, this.getHpAmt());
			conn.getPrepareStatement().setString(3, this.getSeq());
			conn.getPrepareStatement().setDouble(4, this.getDrAmt());
			conn.getPrepareStatement().setString(5, this.getTaxType());
			conn.getPrepareStatement().setString(6, this.getTaxType().equals("406") ? taxAmount : "0" );
			conn.getPrepareStatement().setString(7, this.getTaxType().equals("402") ? taxAmount : "0" );
			conn.getPrepareStatement().setString(8, this.getTaxType().equals("401") ? taxAmount : "0" );
			conn.getPrepareStatement().setString(9, this.getTaxType().equals("400") ? taxAmount : "0" );
			conn.getPrepareStatement().setString(10, this.getDoctorCategory());
			conn.getPrepareStatement().setString(11, JDate.getDate());
			conn.getPrepareStatement().setString(12, JDate.getTime());
			conn.getPrepareStatement().setString(13, this.getAllocatePct()+"");
			conn.getPrepareStatement().setString(14, this.getUserId().toString());
			conn.getPrepareStatement().setDouble(15, this.getAllocateAmt());
			conn.getPrepareStatement().setString(16, m.get("HOSPITAL_CODE").toString());
			conn.getPrepareStatement().setString(17, m.get("DOCTOR_CODE").toString());
			conn.getPrepareStatement().setString(18, m.get("INVOICE_NO").toString());
			conn.getPrepareStatement().setString(19, m.get("INVOICE_DATE").toString());
			conn.getPrepareStatement().setString(20, m.get("TRANSACTION_DATE").toString());
			conn.getPrepareStatement().setString(21, m.get("LINE_NO").toString());
			conn.getPrepareStatement().setString(22, m.get("VERIFY_DATE").toString());
			conn.getPrepareStatement().setString(23, m.get("VERIFY_TIME").toString());
			conn.getPrepareStatement().executeUpdate();
			conn.commitDB();
		}catch (Exception e){
			System.out.println(e);
		}
	}
	public void prepareUpdateBillNotPrint(){
		String sql = "UPDATE TRN_DAILY SET INVOICE_NO = ?, RECEIPT_NO = ?, RECEIPT_DATE = ?, "+
					 "TRANSACTION_MODULE = ?, YYYY = ?, MM = ?, PAY_BY_CASH = ?, RECEIPT_TYPE_CODE = ? "+
					 "WHERE HOSPITAL_CODE = ? AND INVOICE_NO = ? "+
					 "AND INVOICE_DATE != '' AND TRANSACTION_DATE = ? "+
					 "AND ACTIVE = '1' AND LINE_NO = ? ";
		conn.setPrepareStatement(sql);
	}
	public boolean rollbackStepCalculate(){
		String sql = "UPDATE TRN_DAILY SET DR_AMT = 0.00, HP_AMT = 0.00, SEQ_STEP = null, OLD_DR_AMT = 0.00, TAX_TYPE_CODE = '', "+
			         "DR_TAX_400 = 0.00, DR_TAX_401 = 0.00, DR_TAX_402 = 0.00, DR_TAX_406 = 0.00, DOCTOR_CATEGORY_CODE = '', "+
			         "COMPUTE_DAILY_DATE = '', COMPUTE_DAILY_TIME = '', COMPUTE_DAILY_USER_ID = '', NOR_ALLOCATE_PCT = 0.00, "+
			         "NOR_ALLOCATE_AMT = 0.00 WHERE HOSPITAL_CODE = ? AND TRANSACTION_DATE BETWEEN ? AND ? AND SEQ_STEP IS NOT NULL";
		try{
			conn = new DBConn(false);
			conn.setPrepareStatement(sql);
			conn.getPrepareStatement().setString(1, this.hospital);
			conn.getPrepareStatement().setString(2, this.year+this.month+"01");
			conn.getPrepareStatement().setString(3, this.year+this.month+"31");
			conn.getPrepareStatement().executeUpdate();
			conn.commitDB();
			System.out.println("rollback step calculate complete ");
		}catch(Exception e){
			System.out.println("rollback step calculate = "+e);
		}
		return true;
	}
	public boolean rollbackBasicCalculate(){
		String sql = getTemplateForRollbackBasicCalculate()+
			         "WHERE HOSPITAL_CODE = '"+this.hospital+"' "+
			         "AND TRANSACTION_DATE BETWEEN '"+this.getStartDate()+"' AND '"+this.getEndDate()+"' "+
			         "AND COMPUTE_DAILY_DATE != ''";
		try{
			conn = new DBConn(true);
			conn.setStatement();
			conn.insert(sql);
			System.out.println("rollback basic calculate complete ");
		}catch(Exception e){
			System.out.println("rollback basic calculate error = "+e);
		}
		return true;
	}
}