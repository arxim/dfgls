package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.obj.util.JDate;

public class ProcessSummaryInMonth {
	String hospitalcode,payDate,yyyy,mm ;
	DBConnection db,connsb;
	Batch batch;
	
//	public static void main (String arg[]){
//		ProcessSummaryInMonth pm = new ProcessSummaryInMonth();
//		try {
//			pm.doProcessSummaryInMonth("054","20190101","","");
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//
//    }
	
	public boolean doProcessSummaryInMonth(String hospitalcode,String payDate,String yyyy,String mm){
		boolean status = true;
        db = new DBConnection();
        db.connectToLocal(); 
        this.hospitalcode = hospitalcode;
        batch = new Batch(this.hospitalcode, this.db);
		try {
			this.payDate = payDate.length()> 8 ? JDate.saveDate(payDate) : payDate;
			this.yyyy = yyyy.equals(null)||yyyy.equals("")? batch.getYyyy() : yyyy;
		    this.mm = mm.equals(null)||mm.equals("")? batch.getMm() : mm;
		} catch (Exception e) {
			System.out.println(e.toString());
		}

    	DeleteSummaryInMonth(this.hospitalcode,this.yyyy,this.mm);
    
    	String yymm = this.yyyy+this.mm+"%";
    	// insert into table Summary In Month
		String sqlqu = " INSERT INTO SUMMARY_IN_MONTH ( " + 
				" HOSPITAL_CODE, " + 
				" DOCTOR_CODE, " + 
				" YYYY, " + 
				" MM," + 
				" SUM_AMOUNT_AFT_DISCOUNT, " + 
				" SUM_DR_AMOUNT  " + 
				" ) " + 
				
				" SELECT  HOSPITAL_CODE,DOCTOR_CODE,"+this.yyyy+","+this.mm+",SUM(AMOUNT_AFT_DISCOUNT) AS SUM_AMOUNT_AFT_DISCOUNT ,SUM(DR_AMT) AS SUM_DR_AMOUNT " +
				" FROM TRN_DAILY " +
				" WHERE HOSPITAL_CODE = '"+this.hospitalcode+"' AND TRANSACTION_DATE LIKE '"+yymm+"' " + 
				" AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1'" + 
				" GROUP BY HOSPITAL_CODE ,DOCTOR_CODE" ;
		
		System.out.println(sqlqu);
		status = db.executeUpdate(sqlqu)<0 ? false : true;
		db.Close();		
		System.out.println("Process Summary in month is Success ? "+status);
		
		return status;
	}
	
	// delete table Summary In Month
	public int DeleteSummaryInMonth(String  hospitalCode , String yyyy, String mm ){ 
		int resuftAction = 0;
	        DBConnection db = new DBConnection();
			db.connectToLocal(); 	
	        if (db != null) {
	            try {
	                String SQLCOMMAND = "DELETE FROM SUMMARY_IN_MONTH "				
	            			+"WHERE "
	            			+"YYYY = '"+yyyy+"' AND "
	            			+"MM = '"+mm+"' AND "
	            			+"HOSPITAL_CODE='"+this.hospitalcode+"' ";
	                System.out.println(">> DELECT SQLCOMMAND : " + SQLCOMMAND);
	                resuftAction = db.executeUpdate(SQLCOMMAND);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
		return resuftAction;
	}
	
}
