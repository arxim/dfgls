package df.bean.process;

import df.bean.db.conn.DBConnection;

public class ProcessAllocateManagementFeeBean implements ProcessMaster{
	
	
	public static void main(String[] args){ 
		
		ProcessMaster  objProcess =  new  ProcessAllocateManagementFeeBean("09" , "2013" , "011" , "01");
		
		objProcess.doProcess();
		
	}

	private DBConnection objConn;
	
	private String hospitalCode;
	
	private double amount;
	
	private String mm;
	
	private String yyyy;
	
	private String term;
	
	private String s = "";
	
	/**
	 * this you <pre> Ojb.setAmount() </pre> befor call doProcess();
	 * @param mm
	 * @param yyyy
	 * @param hospitalCode
	 */
	public ProcessAllocateManagementFeeBean(String mm , String yyyy , String hospitalCode , String term){ 
		
		this.objConn = new DBConnection();
		this.objConn.connectToLocal();
		this.mm = mm;
		this.yyyy = yyyy;
		this.hospitalCode = hospitalCode;
		this.term  =  term;
		
		if(term.equals("1")){
			
			s = " AND DOCTOR.PAYMENT_TIME = '2' AND ORDER_ITEM.PAYMENT_TIME = '2' "+
				" AND ((TRANSACTION_DATE BETWEEN '" + this.yyyy + this.mm + "01' AND '" + this.yyyy + this.mm + "15') "+
				" OR (RECEIPT_DATE BETWEEN '" + this.yyyy + this.mm + "01' AND '" + this.yyyy + this.mm + "15')) ";
		
		} else {
			
			s = " AND BATCH_NO = '' ";
			
		}

	}
	
	@Override
	public boolean doProcess() {
		boolean action  = false;
		
		try { 
			
			if(this.objConn == null){
				return false;
			}
				
			if(this.hospitalCode == null){
				return false;
			} 
				
			if(this.mm == null) {
				return false;
			}
			
			if(this.yyyy == null){
				return false;
			}
			
			this.doRollback();
			
			System.out.println("get excute :  " + getSQL());
			
			 if (this.objConn.executeUpdate(getSQL()) !=  -1 ){

				 System.out.println(getSQL());
				 action = true;
			 
			 } else { 
				 action  = false;
			 }
			 
		}catch (Exception ex){ 
			System.out.println("Process name [Management Fee] : " + ex.toString());
		}
		
		return action;
	}

	@Override
	public boolean doRollback() {
		if(this.objConn.executeUpdate(this.getDeleteTrnExpenseOnRunProcess()) != -1)   { 
			
			System.out.println("get delete : " + getDeleteTrnExpenseOnRunProcess());
			return true;
			
		} else  {
			
			return false;
		
		}
	}

	@Override
	public boolean doBatchClose() {
		return false;
	}

	public DBConnection getObjConn() {
		return objConn;
	}

	public void setObjConn(DBConnection objConn) {
		this.objConn = objConn;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	public String getYyyy() {
		return yyyy;
	}

	public void setYyyy(String yyyy) {
		this.yyyy = yyyy;
	}

	/**
	 * 
	 * @return sql command get value df amount
	 */
	private String getSQL(){
		return  " INSERT INTO TRN_EXPENSE_DETAIL (" +
				"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
				"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
				"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
				"YYYY, MM, NOTE, EMPLOYEE_ID, " +
			    "DEPARTMENT_CODE, LOCATION_CODE)" + 
				
			   " SELECT DISTINCT EX.HOSPITAL_CODE AS HOSPITAL_CODE  , DOCTOR_CODE  , '' AS DOC_NO  , " + 
			   " 'MF' + '" + this.yyyy + "' + '" + this.mm + "/' + CONVERT(varchar, (ROW_NUMBER() OVER (ORDER BY DOCTOR_CODE))) AS LINE_NO  , " + 
			   " '' AS DOC_DATE , ( SUM(DR_NET_PAID_AMT) * " + this.amount + " ) / 100  AS AMOUNT  , " + 
			   " ( SUM(DR_NET_PAID_AMT) * " + this.amount + " ) / 100  AS TAX_AMOUNT  , EX.SIGN AS EXPENSE_SIGN , "  +
			   " EX.ACCOUNT_CODE AS EXPENSE_ACCOUNT_CODE  , EX.CODE AS EXPENSE_CODE  , " +
			   " EX.TAX_TYPE_CODE AS TAX_TYPE_CODE , '" + this.yyyy + "' , '" + this.mm + "' , 'ManagementFee : "+ this.yyyy +" / " + this.mm + "' AS NOTE , " +
			   " 'ManagementFee' AS EMPLOYEE_ID, Q.DEPARTMENT_CODE AS DEPARTMENT_CODE , '' AS LOCATION_CODE " + 
			   " FROM  ( " + 
			   
					 " SELECT   TRN_DAILY.DOCTOR_CODE AS DOCTOR_CODE , DOCTOR.DEPARTMENT_CODE  ,  SUM(TRN_DAILY.DR_AMT) AS DR_NET_PAID_AMT " +             
					 " FROM DOCTOR " +
					 " LEFT OUTER JOIN TRN_DAILY ON DOCTOR.CODE = TRN_DAILY.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = TRN_DAILY.HOSPITAL_CODE " +
					 " LEFT OUTER JOIN ORDER_ITEM ON TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "+
					 " WHERE DOCTOR.HOSPITAL_CODE = '" + this.hospitalCode + "' " +
					 " AND TRN_DAILY.YYYY = '" + this.yyyy + "' AND TRN_DAILY.MM = '"+ this.mm +"'" + 
					 " AND ORDER_ITEM.ACTIVE = '1' AND TRN_DAILY.ACTIVE = '1' "  + s +
					 " GROUP BY TRN_DAILY.YYYY, TRN_DAILY.MM, TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.DOCTOR_CODE, DOCTOR.DEPARTMENT_CODE , " + 
					 " DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, " + 
					 " DOCTOR.IS_HOLD " +  
					 
					 " UNION " + 
                
					 " SELECT AJ.DOCTOR_CODE AS DOCTOR_CODE , DOCTOR.DEPARTMENT_CODE , " +
					 " CASE WHEN SUM(AJ.AMOUNT * AJ.EXPENSE_SIGN) < 0 THEN 0 ELSE SUM(AJ.AMOUNT * AJ.EXPENSE_SIGN) END  AS DR_NET_PAID_AMT " +  // AD CEOFREEALIFE 11/12/2013 
					 //SUM(AJ.AMOUNT * AJ.EXPENSE_SIGN) AS DR_NET_PAID_AMT " + 
					 " FROM DOCTOR " +  
					 " LEFT OUTER JOIN TRN_EXPENSE_DETAIL AS AJ " +  
					 " ON DOCTOR.CODE = AJ.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = AJ.HOSPITAL_CODE " +  
					 " LEFT OUTER JOIN EXPENSE AS EX " +
					 " ON AJ.EXPENSE_CODE = EX.CODE AND AJ.HOSPITAL_CODE = EX.HOSPITAL_CODE " +
					 " WHERE DOCTOR.HOSPITAL_CODE = '" + this.hospitalCode +"' " +
					 " AND AJ.YYYY = '" + this.yyyy + "' AND AJ.MM = '" + this.mm + "' AND ( AJ.BATCH_NO = '' OR AJ.BATCH_NO IS NULL ) " +
					 (this.term.equals("1") ?  " AND DOCTOR.PAYMENT_TIME = '2' "  :  " AND DOCTOR.PAYMENT_TIME != '' " ) + 
					 " AND EX.ADJUST_TYPE != 'EX'"+
					 " AND LINE_NO NOT LIKE 'MF" +  this.yyyy  + this.mm + "'" +
					 " GROUP BY AJ.YYYY, AJ.MM, AJ.HOSPITAL_CODE, AJ.DOCTOR_CODE , DOCTOR.DEPARTMENT_CODE , " + 
					 " DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, " + 
					 " DOCTOR.IS_HOLD " +  
				 " ) Q " +  
				 " INNER JOIN EXPENSE EX ON EX.HOSPITAL_CODE = '" + this.hospitalCode + "'  AND EX.ADJUST_TYPE = 'MF'" + 
			     " GROUP BY EX.HOSPITAL_CODE , DOCTOR_CODE  , DEPARTMENT_CODE  , EX.CODE , EX.TAX_TYPE_CODE , EX.SIGN ,  EX.ACCOUNT_CODE , EX.ADJUST_TYPE " + 
			     " HAVING ( SUM(DR_NET_PAID_AMT) * " + this.amount + " ) / 100 >  0 " + // ADD  11 / 12 / 2013
			     " ORDER BY DOCTOR_CODE ";
	}

	/**
	 * Query delete befor run Expens
	 * @return sql commnad
	 */
	private String getDeleteTrnExpenseOnRunProcess(){ 
		return  "DELETE FROM TRN_EXPENSE_DETAIL  WHERE HOSPITAL_CODE = '" 
				+ this.hospitalCode + "' AND MM = '" + this.mm + "' AND YYYY = '" 
				+ this.yyyy + "' AND LINE_NO LIKE 'MF" + this.yyyy + this.mm + "%'"
				+ " AND  BATCH_NO = '' ";
	}
}
