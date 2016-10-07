package df.bean.process;

import df.bean.db.conn.DBConnection;

public class WelfareDFPayment {
	private	String startDate ="";
	private String endDate ="";
	private String hospitalCode="";
	private DBConnection conn = null;		
	
	public String getStartDate() {
		return startDate;
	}

	public DBConnection getConn() {
		return conn;
	}

	public void setConn(DBConnection conn) {
		this.conn = conn;
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


	public String getHospitalCode() {
		return hospitalCode;
	}


	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public boolean doProcess(String hospitalCode,String startDate, String endDate){
		conn = new DBConnection();
		conn.connectToLocal();
		boolean result = true;
			String sql ="UPDATE TRN_DAILY SET	"
					+ "TRN_DAILY.ACTIVE = CASE WHEN DR.DOCTOR_GROUP_CODE = 'FT' THEN '0' ELSE '1' END ,	"
					+ "TRN_DAILY.DR_AMT = CASE WHEN DR.DOCTOR_GROUP_CODE = 'FT' THEN 0.00 ELSE T.AMOUNT_AFT_DISCOUNT*50/100 END,	"
					+ "TRN_DAILY.NOR_ALLOCATE_PCT = CASE WHEN DR.DOCTOR_GROUP_CODE = 'FT' THEN 0.00 ELSE 50.00 END,	"
					+ "TRN_DAILY.DR_TAX_406 = CASE WHEN DR.DOCTOR_GROUP_CODE = 'FT' THEN 0.00 ELSE CASE WHEN T.TAX_TYPE_CODE = '406' THEN T.AMOUNT_AFT_DISCOUNT*50/100 ELSE 0.00 END END,	"
					+ "TRN_DAILY.DR_TAX_402 = CASE WHEN DR.DOCTOR_GROUP_CODE = 'FT' THEN 0.00 ELSE CASE WHEN T.TAX_TYPE_CODE = '402' THEN T.AMOUNT_AFT_DISCOUNT*50/100 ELSE 0.00 END END,	"
					+ "TRN_DAILY.IS_GUARANTEE_FROM_ALLOC = CASE WHEN DR.DOCTOR_GROUP_CODE = 'FT' THEN '' ELSE 'D' END, "
					+ "TRN_DAILY.NOTE ='Employee'"
					+ "FROM TRN_DAILY T	"
					+ "LEFT OUTER JOIN DOCTOR DR ON T.HOSPITAL_CODE = DR.HOSPITAL_CODE AND T.DOCTOR_CODE = DR.CODE	"
					+ "LEFT OUTER JOIN PAYOR_OFFICE PF ON T.HOSPITAL_CODE = PF.HOSPITAL_CODE AND T.PAYOR_OFFICE_CODE = PF.CODE	"
					+ "WHERE T.HOSPITAL_CODE = '"+hospitalCode+"'"	
					+ "AND T.TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'"
					+ "AND DR.DOCTOR_GROUP_CODE IN ('FT','PT')" 
					+ "AND PF.PAYOR_OFFICE_CATEGORY_CODE = 'T007'"
					+ "AND T.ORDER_ITEM_CODE != '19226'";
			//System.out.println("Welfare :"+ sql);
			try {
				doRollBack(hospitalCode,startDate,endDate);
				conn.executeQueryString(sql);
			}catch(Exception ex){
				System.out.println("Welfare Method : "+ex);
				result=false;
			}
			conn.Close();
		return result;
		
	}
	
	public boolean doRollBack(String hospitalCode,String startDate, String endDate){
		conn = new DBConnection();
		conn.connectToLocal();
		boolean result = true;
			String sqlRollback ="UPDATE TRN_DAILY SET	"
					+ "TRN_DAILY.ACTIVE = '1' ,"
					+ "TRN_DAILY.NOTE =''"
					+ "FROM TRN_DAILY T	"
					+ "LEFT OUTER JOIN DOCTOR DR ON T.HOSPITAL_CODE = DR.HOSPITAL_CODE AND T.DOCTOR_CODE = DR.CODE	"
					+ "LEFT OUTER JOIN PAYOR_OFFICE PF ON T.HOSPITAL_CODE = PF.HOSPITAL_CODE AND T.PAYOR_OFFICE_CODE = PF.CODE	"
					+ "WHERE T.HOSPITAL_CODE = '"+hospitalCode+"'"	
					+ "AND T.TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'"
					+ "AND PF.PAYOR_OFFICE_CATEGORY_CODE = 'T007'"
					+ "AND T.ORDER_ITEM_CODE != '19226'"
					+ "AND T.NOTE = 'Employee'";
			try {
				System.out.println("doRollBack : "+sqlRollback);
				conn.executeQueryString(sqlRollback);
			}catch(Exception ex){
				System.out.println("Rollback Method : "+ex);
				result=false;
			}
			//conn.Close();
		return result;
	}
}
