package df.bean.process;

import df.bean.db.conn.DBConnection;

public class ProcessUpdatePreviousOnWard {

    private DBConnection conn = new DBConnection();
    private DBConnection conn1 = new DBConnection();
    private DBConnection conn2 = new DBConnection();
    private String user_id = "";
    
    public ProcessUpdatePreviousOnWard(){
    	this.conn.connectToLocal();
    	this.conn1.connectToLocal();
    	this.conn2.connectToLocal();
    }
    public void setUserId(String userId){
    	user_id = userId;
    }
    public boolean importOnward(String hospitalCode, String startDate, String endDate) {
		boolean result = true;
		int numAffRec = 0;
		String upOWD = "UPDATE TRN_DAILY SET INVOICE_NO = Q.INVOICE_NO, "+
					   "RECEIPT_TYPE_CODE = Q.RECEIPT_TYPE_CODE, "+
					   "YYYY = CASE WHEN THEN Q.TRANSACTION_TYPE = 'REV' ? 'AR' ELSE 'TR' END "+
					   "TRANSACTION_MODULE = CASE WHEN THEN Q.TRANSACTION_TYPE = 'REV' ? 'AR' ELSE 'TR' END "+
					   "FROM TRN_DAILY T INNER JOIN "+
					   "(SELECT HOSPITAL_CODE, INVOICE_NO, RECEIPT_TYPE_CODE, LINE_NO FROM TRN_DAILY "+
					   "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND IS_ONWARD = 'N' AND (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"')) AS Q "+
					   "ON T.HOSPITAL_CODE = Q.HOSPITAL_CODE AND T.LINE_NO = Q.LINE_NO AND T. "+
					   "WHERE T.INVOICE_NO LIKE T.HN_NO+T.EPISODE_NO+'%' AND T.HOSPITAL_CODE = '"+hospitalCode+"' AND IS_ONWARD = 'Y'";

		String inOWD = "INSERT INTO TRN_ONWARD "+
					   "SELECT T.* FROM TRN_DAILY T INNER JOIN "+
					   "(SELECT HOSPITAL_CODE, INVOICE_NO, RECEIPT_TYPE_CODE, LINE_NO FROM TRN_DAILY "+
					   "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND IS_ONWARD = 'Y') AS Q "+
					   "ON T.HOSPITAL_CODE = Q.HOSPITAL_CODE AND T.LINE_NO = Q.LINE_NO "+
					   "WHERE T.HOSPITAL_CODE = '"+hospitalCode+"' AND "+
					   "IS_ONWARD = 'N' AND "+
					   "(TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"')";

		String delOWD = "DELETE FROM TRN_DAILY WHERE LINE_NO IN ( "+
						"SELECT LINE_NO FROM TRN_ONWARD WHERE (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') "+
						"AND HOSPITAL_CODE = '"+hospitalCode+"' AND IS_ONWARD = 'N' "+
						") AND IS_ONWARD = 'N'";
		try{
			System.out.println(upOWD);
			if(conn.executeUpdate(upOWD) > 0){
				System.out.println(inOWD);
				if(conn1.executeUpdate(inOWD) > 0){
					System.out.println(delOWD);
					conn2.executeUpdate(delOWD);
				}
			}
			result = true;
		}catch(Exception e){
			System.out.println("Onward Error : "+e);
			result = false;
		}finally{
			if(this.conn != null){
				this.conn.Close();
			}
			if(this.conn1 != null){
				this.conn1.Close();
			}
			if(this.conn2 != null){
				this.conn2.Close();
			}
		}
    	return result;
	}
    public boolean importOnward(String hospitalCode, String startDate, String endDate, String backUp) {
		boolean result = true;
		if(isUpdate(hospitalCode, startDate, endDate)){
			if(isBackup(hospitalCode, startDate, endDate)){
				result = this.isDelete(hospitalCode, startDate, endDate)? true : false ;				
			}else{
				result = false;
			}
		}else{
			result = false;
		}
		conn.Close();
    	return result;
	}
    private boolean isUpdate(String hospitalCode, String startDate, String endDate) {
    	boolean result = false;
    	int numAffRec = 0;
		String sqlComand = "";
		sqlComand =	"UPDATE TRN_DAILY SET INVOICE_NO = Q.INVOICE_NO, RECEIPT_TYPE_CODE = Q.RECEIPT_TYPE_CODE "+
					"FROM TRN_DAILY T INNER JOIN "+
					"(SELECT HOSPITAL_CODE, INVOICE_NO, RECEIPT_TYPE_CODE, LINE_NO FROM TRN_DAILY "+
					"WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND IS_ONWARD = 'N' AND (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"')) AS Q "+
					"ON T.HOSPITAL_CODE = Q.HOSPITAL_CODE AND T.LINE_NO = Q.LINE_NO "+
					"WHERE T.INVOICE_NO LIKE T.HN_NO+T.EPISODE_NO+'%' AND T.HOSPITAL_CODE = '"+hospitalCode+"' AND IS_ONWARD = 'Y'";

		numAffRec = conn.executeUpdate(sqlComand);
		if(numAffRec > 0){
			result = true;
		}
		return result;
	}
    private boolean isBackup(String hospitalCode, String startDate, String endDate) {
    	boolean result = false;
    	int numAffRec = 0;
		String sqlCommand = "";
		sqlCommand = "INSERT INTO TRN_ONWARD "+
					 "SELECT T.* FROM TRN_DAILY T INNER JOIN "+
					 "(SELECT HOSPITAL_CODE, INVOICE_NO, RECEIPT_TYPE_CODE, LINE_NO FROM TRN_DAILY "+
					 "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND IS_ONWARD = 'Y') AS Q "+
					 "ON T.HOSPITAL_CODE = Q.HOSPITAL_CODE AND T.LINE_NO = Q.LINE_NO "+
					 "WHERE T.HOSPITAL_CODE = '"+hospitalCode+"' AND "+
					 "IS_ONWARD = 'N' AND "+
					 "(TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"')";		
		numAffRec = conn.executeUpdate(sqlCommand);
		if(numAffRec > 0){
			result = true;
		}
		return result;
	}
    private boolean isDelete(String hospitalCode, String startDate, String endDate) {
    	boolean result = false;
    	int numAffRec = 0;
		String sqlCommand = "";
		sqlCommand = "DELETE FROM TRN_DAILY WHERE LINE_NO+'|'+INVOICE_NO IN ("+
					 "SELECT LINE_NO+'|'+INVOICE_NO FROM TRN_ONWARD WHERE TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' "+
					 "AND HOSPITAL_CODE = '"+hospitalCode+"' AND IS_ONWARD = 'N' "+
					 ") AND IS_ONWARD = 'N'";
					
		numAffRec = conn.executeUpdate(sqlCommand);
		if(numAffRec > 0){
			result = true;
		}
		return result;
	}
}
