package df.bean.report;

import df.bean.db.conn.DBConn;

public class TempDoctorProfileGuarantee {
	private DBConn conn = null;
	private String yyyy = "";
	private String mm = "";
	private String hospital_code = "";
	private String[][] arrDrProfileCode = null;
	
	private String getYyyy() {
		return yyyy;
	}
	private void setYyyy(String yyyy) {
		this.yyyy = yyyy;
	}
	private String getMm() {
		return mm;
	}
	private void setMm(String mm) {
		this.mm = mm;
	}
	private String getHospital_code() {
		return hospital_code;
	}
	private void setHospital_code(String hospital_code) {
		this.hospital_code = hospital_code;
	}
	
	private DBConn getConn(){
		if(conn==null){
			conn = new DBConn();
			try{
				conn.setStatement();
			}catch(Exception err){
				System.out.println(err.getMessage());
			}
		}
		return conn;
	}
	public TempDoctorProfileGuarantee(String h, String y, String m, boolean rollback){
		this.setHospital_code(h);
		this.setYyyy(y);
		this.setMm(m);
		this.setCheckRollback(rollback);
	}
	
	private void InsertInformDoctor(){
		String qInsert = "";
		qInsert ="INSERT TEMP_REPORT_DOCTORPROFLIE_GUARANTEE(HOSPITAL_CODE, YYYY, MM, "+
				 "	DOCTOR_PROFILE_CODE, DOCTOR_CODE, GUARANTEE_DR_CODE,  "+
				 "	NAME_THAI, PROFILE_NAME, HOSPITAL_UNIT_CODE,H_UNIT  "+
				 "	, SUM_AMT,  DR_NET_PAID_AMT "+
				 "	, DR_TAX_400, DR_TAX_401, DR_TAX_402, DR_TAX_406 "+
				 "	, PAYMENT_MODE_CODE, EXDR_AMT, EXCR_AMT "+
				 "	, GDR_402, GDR_406, REF_PAID_NO,DESCRIPTION,DR_SUM_AMT ) "+
				 "SELECT '"+ this.getHospital_code() +"','"+ this.getYyyy() +"','"+ this.getMm() +"' "+
				 "	,DR.DOCTOR_PROFILE_CODE, DR.CODE, DR.GUARANTEE_DR_CODE "+
				 "	,DR.NAME_THAI AS 'NAME_THAI' ,DP.NAME_THAI AS 'PROFILE_NAME'  "+
				 "	,DR.HOSPITAL_UNIT_CODE, HU.DESCRIPTION  "+
				 "	,PM.SUM_AMT, PM.DR_NET_PAID_AMT "+
				 "	,PM.DR_TAX_400,PM.DR_TAX_401,PM.DR_TAX_402,PM.DR_TAX_406 "+
				 "	,PM.PAYMENT_MODE_CODE,PM.EXDR_AMT,PM.EXCR_AMT "+
				 "	,PM.GDR_402,PM.GDR_406,PM.REF_PAID_NO,PM_MODE.DESCRIPTION "+
				 "	,ISNULL((SELECT SUM(DR_AMT) FROM TRN_DAILY " +
							"		WHERE YYYY='"+ this.getYyyy() +"'  " +
							"			AND MM='"+ this.getMm() +"' " +
							"			AND DOCTOR_CODE NOT LIKE '99999%'  " +
							"			AND HOSPITAL_CODE=DR.HOSPITAL_CODE " +
							"			AND GUARANTEE_TERM_MM ='"+ this.getMm() +"'  " +
							"			AND GUARANTEE_TERM_YYYY ='"+ this.getYyyy() +"'  " +
							"			AND GUARANTEE_DR_CODE = DR.CODE  " +
							"		GROUP BY YYYY, MM ),0) " +				 
				 "FROM DOCTOR AS DR  "+
				 "	INNER JOIN DOCTOR_PROFILE AS DP ON DP.CODE = DR.DOCTOR_PROFILE_CODE  "+
				 "	LEFT JOIN HOSPITAL_UNIT AS HU ON HU.CODE = DR.HOSPITAL_UNIT_CODE  "+
				 "	LEFT JOIN PAYMENT_MONTHLY AS PM ON (PM.DOCTOR_CODE=DR.CODE  "+
				 "							AND PM.HOSPITAL_CODE=DR.HOSPITAL_CODE "+
				 "							AND PM.YYYY = '"+ this.getYyyy() +"' "+
				 "							AND PM.MM = '"+ this.getMm() +"') "+
				 "  LEFT OUTER JOIN PAYMENT_MODE PM_MODE ON (PM.PAYMENT_MODE_CODE = PM_MODE.CODE) "+
				 "WHERE DR.CODE NOT LIKE '9999%'  "+
				 "	AND DR.ACTIVE=1  "+
				 "	AND DR.HOSPITAL_CODE='"+ this.getHospital_code() +"'"; 
			try{
				this.getConn().insert(qInsert);
				this.getConn().commitDB();
			}catch(Exception err){
				System.out.println(err.getMessage());
			}
			//System.out.println(qInsert);
	}
	
	private void UpdateStpGuarantee(){
		String qSelect = "";
		qSelect = "	SELECT TP.DOCTOR_PROFILE_CODE " +
			",ISNULL((SELECT SUM(STP.GUARANTEE_AMOUNT+STP.GUARANTEE_FIX_AMOUNT+STP.GUARANTEE_INCLUDE_AMOUNT+STP.GUARANTEE_EXCLUDE_AMOUNT) " +
			"	FROM STP_GUARANTEE AS STP  " +
			"	WHERE STP.GUARANTEE_DR_CODE IN (SELECT PM_T.DOCTOR_CODE  FROM TEMP_REPORT_DOCTORPROFLIE_GUARANTEE AS PM_T WHERE PM_T.DOCTOR_PROFILE_CODE=TP.DOCTOR_PROFILE_CODE)  " + 
			"		AND STP.HOSPITAL_CODE=TP.HOSPITAL_CODE " +
			"		AND STP.YYYY='"+ this.getYyyy() +"'  " +
			"		AND STP.ACTIVE=1 " +
			"		AND STP.MM='"+ this.getMm() +"'),0) AS 'SUM_ABSORB'  " +
			",ISNULL((SELECT SUM(STP.GUARANTEE_EXCLUDE_AMOUNT)  " +
			"	FROM STP_GUARANTEE AS STP  " +
			"	WHERE STP.GUARANTEE_DR_CODE IN (SELECT PM_T.DOCTOR_CODE  FROM TEMP_REPORT_DOCTORPROFLIE_GUARANTEE AS PM_T WHERE PM_T.DOCTOR_PROFILE_CODE=TP.DOCTOR_PROFILE_CODE)  " + 
			"		AND STP.HOSPITAL_CODE=TP.HOSPITAL_CODE  " +
			"		AND STP.YYYY='"+ this.getYyyy() +"'  " +
			"		AND STP.ACTIVE=1 " +
			"		AND STP.MM='"+ this.getMm() +"'),0) AS 'GUARANTEE_EXCLUDE_AMOUNT'  " +
			",ISNULL((SELECT TOP 1 " +
			"				CASE WHEN SUM(STP.GUARANTEE_AMOUNT) > 0 " +
			"				THEN " +
			"					SUBSTRING(STP.GUARANTEE_TYPE_CODE,1,1) " +
			"				ELSE  " +
			"					'PT' " +
			"				END " +
			"		FROM STP_GUARANTEE AS STP " +
			"		WHERE STP.GUARANTEE_DR_CODE IN (SELECT PM_T.DOCTOR_CODE  FROM TEMP_REPORT_DOCTORPROFLIE_GUARANTEE AS PM_T WHERE PM_T.DOCTOR_PROFILE_CODE=TP.DOCTOR_PROFILE_CODE) " +
			"			AND STP.HOSPITAL_CODE=TP.HOSPITAL_CODE " +
			"			AND STP.YYYY='"+ this.getYyyy() +"'  " +
			"			AND STP.ACTIVE=1 " +
			"			AND STP.MM='"+ this.getMm() +"' GROUP BY GUARANTEE_TYPE_CODE),'')" +		
			"FROM TEMP_REPORT_DOCTORPROFLIE_GUARANTEE AS TP " +
			"WHERE TP.HOSPITAL_CODE='"+this.getHospital_code()+"' " +
			"GROUP BY TP.DOCTOR_PROFILE_CODE,TP.HOSPITAL_CODE ";
		//System.out.println(qSelect);
		String[][] arrStp = this.getConn().query(qSelect);
		String qInsert = "";
		String qSubSelect = "";
		String[][] arrSub = null; 
		for(int i = 0; i < arrStp.length ; i++){
			qSubSelect = "SELECT TOP 1 DOCTOR_PROFILE_CODE,DOCTOR_CODE,GUARANTEE_DR_CODE " +
					"FROM  TEMP_REPORT_DOCTORPROFLIE_GUARANTEE " +
					"WHERE HOSPITAL_CODE='"+ this.getHospital_code() +"' AND YYYY='"+ this.getYyyy() +"' AND MM='"+ this.getMm() +"' " +
					"AND DOCTOR_PROFILE_CODE='"+ arrStp[i][0] +"' ";
			arrSub = this.getConn().query(qSubSelect);
			
			//----------------------------------------
			qInsert = "UPDATE TEMP_REPORT_DOCTORPROFLIE_GUARANTEE SET SUM_GUARANTEE="+arrStp[i][1]+", EXCLUDE_GUARANTEE="+arrStp[i][2]+", TYPE_GUARANTEE='"+ arrStp[i][3] +"' " +
					"WHERE HOSPITAL_CODE='"+ this.getHospital_code() +"' " +
							"AND YYYY='"+ this.getYyyy() +"' " +
							"AND MM='"+ this.getMm() +"' " +
							"AND DOCTOR_PROFILE_CODE='"+arrSub[0][0]+"' " +
							"AND DOCTOR_CODE = '"+arrSub[0][1]+"' " +
							"AND GUARANTEE_DR_CODE='"+ arrSub[0][2] +"' ";
			try{
				this.getConn().insert(qInsert);
				this.getConn().commitDB();
			}catch(Exception err){
				System.out.println(err.getMessage());
			}
			arrSub = null;
		}
	}

	private void RollBackTempReport(){
		String qSelect = "DELETE TEMP_REPORT_DOCTORPROFLIE_GUARANTEE WHERE YYYY='"+ this.getYyyy() +"' AND MM='"+ this.getMm() +"' AND HOSPITAL_CODE='"+ this.getHospital_code() +"';";
		try{
			this.getConn().insert(qSelect);
			this.getConn().commitDB();
		}catch(Exception err){
			System.out.println(err.getMessage());
		}		
	}
	
	private boolean checkRollback = false;
	
	private boolean isCheckRollback() {
		return checkRollback;
	}
	private void setCheckRollback(boolean checkRollback) {
		this.checkRollback = checkRollback;
	}
	
	public void ProcessMain(){
		if(this.isCheckRollback()){
			this.RollBackTempReport();
			this.InsertInformDoctor();
			this.UpdateStpGuarantee();			
		}
	}
	
	public static void main(String[] arg){
		System.out.println("Start ->");
		TempDoctorProfileGuarantee tg = new TempDoctorProfileGuarantee("00001","2009","04",true);
		tg.ProcessMain();
		System.out.println("<- Finish");
	}
}
