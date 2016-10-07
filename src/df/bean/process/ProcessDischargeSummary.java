package df.bean.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.obj.util.JDate;

public class ProcessDischargeSummary{
	String hospitalcode = "";
	String yyyy = "";
	String mm = "";
	DBConnection db,connsb;
	Batch batch;

	public boolean doProcessDischange(String hospitalcode,String yyyy,String mm){
		boolean status = true;
        db = new DBConnection();
        db.connectToLocal();
        batch = new Batch(hospitalcode,db);
        db.Close();
		this.hospitalcode=hospitalcode;
		this.yyyy=yyyy;
		this.mm=mm;
		try{
			if(SetBackupDischange()){
				SQLCommit();
				System.out.println("ProcessDischange Complete");			
			}else{
				System.out.println("ProcessDischarge Is Not Complete");
				status = false;
			}
		}catch(Exception e){
			System.out.println("ProcessDischarge Is Not Complete : "+e);
			status = false;
		}
		return status;
	}
	private boolean SetBackupDischange(){
		boolean status = true;
		connsb=new DBConnection();
		String sqlqu="INSERT INTO HIS_TRN_DAILY "				
			+"SELECT "+this.getColumnName()+",'N' AS INCLUDE, "
			+"'DISCHARGE' AS TAG "
			+"FROM TRN_DAILY WITH (index (discharge_index)) "
			+"WHERE "
			+"LINE_NO IN(SELECT LINE_NO FROM INT_HIS_DISCHARGE WHERE HOSPITAL_CODE='"+this.hospitalcode+"' AND YYYY='"+batch.getYyyy()+"' AND MM='"+batch.getMm()+"') AND "
			+"HOSPITAL_CODE='"+this.hospitalcode+"' AND "
			+"IS_DISCHARGE_SUMMARY != 'Y' AND "
			+"ACTIVE = '1' ";
		System.out.println(sqlqu);
		connsb.connectToLocal(); 	
		status = connsb.executeUpdate(sqlqu)<0 ? false : true;
		connsb.Close();		
		System.out.println("Process Backup is Success ? "+status);
		return status;
	}
	public void SQLCommit() throws SQLException {
        DBConnection db = new DBConnection();
        db.connectToLocal();
        Batch batch = new Batch(hospitalcode,db);
        db.Close();
		DBConnection connsb=new DBConnection();
		connsb.connectToLocal(); 	
		connsb.beginTrans();
		DBConnection connihd=new DBConnection();
		connihd.connectToLocal(); 	
		String sql="SELECT * FROM INT_HIS_DISCHARGE WHERE HOSPITAL_CODE='"+this.hospitalcode+"' AND YYYY='"+batch.getYyyy()+"' AND MM='"+batch.getMm()+"'";
		ResultSet rsforind=connihd.executeQuery(sql);
		while(rsforind.next()){
			//System.out.println(rsforind.getString("PAYMENT_STATUS"));
			if(rsforind.getString("PAYMENT_STATUS").equals("N")){
                String SQLCOMMAND = 
                	"UPDATE TRN_DAILY SET " +
                	"MM='', YYYY='', " +
                	"RECEIPT_DATE='', BATCH_NO = '', IS_PAID='N', " +
                	"IS_DISCHARGE_SUMMARY = 'N' "+
                	"WHERE " +
                	"TRN_DAILY.LINE_NO='"+rsforind.getString("LINE_NO")+"' AND "+
                	"TRN_DAILY.HOSPITAL_CODE='"+this.hospitalcode+"' AND "+
                	"TRN_DAILY.TRANSACTION_DATE LIKE '"+batch.getYyyy()+batch.getMm()+"%' AND "+
              		"(TRN_DAILY.IS_DISCHARGE_SUMMARY='' OR TRN_DAILY.IS_DISCHARGE_SUMMARY='D') AND " +
              		"TRN_DAILY.BATCH_NO = ''";
                //System.out.println("Discharge Hold : "+SQLCOMMAND);
                connsb.executeUpdate(SQLCOMMAND);
			}else{
                String SQLCOMMAND = "UPDATE TRN_DAILY SET " +
      		    "RECEIPT_DATE = CASE WHEN RECEIPT_NO != '' THEN '"+rsforind.getString("COMPLETE_DATE")+"' ELSE '' END, " +
      		    "MM = CASE WHEN RECEIPT_NO != '' THEN '"+batch.getMm()+"' ELSE '' END, " +
            	"YYYY = CASE WHEN RECEIPT_NO != '' THEN '"+batch.getYyyy()+"' ELSE '' END, " +
            	"BATCH_NO = '', IS_PAID = 'Y', "+
            	"IS_DISCHARGE_SUMMARY='Y' "+
            	"WHERE "+
            	"LINE_NO='"+rsforind.getString("LINE_NO")+"' AND "+
            	"HOSPITAL_CODE='"+this.hospitalcode+"' AND "+
            	//"INVOICE_NO = '"+rsforind.getString("INVOICE_NO")+"' AND "+
          		"IS_DISCHARGE_SUMMARY='N'";
                //System.out.println("Discharge Payment : "+SQLCOMMAND);
                connsb.executeUpdate(SQLCOMMAND);
			}
		}
		
		if (connsb != null) {
	    	connsb.commitTrans();
	    }
	    connsb.Close();
	}
    public int rollBackDischarge(String  hospitalCode , String mm , String yyyy){ 
        int resuftAction = 0;		
        DBConnection connsb=new DBConnection();
		connsb.connectToLocal(); 	
        if (connsb != null) {
            try {
                String SQLCOMMAND = "DELETE FROM INT_HIS_DISCHARGE WHERE MM = '" + mm +"' AND YYYY = '" + yyyy + "' AND HOSPITAL_CODE = '" + hospitalCode + "'";
                resuftAction = connsb.executeUpdate(SQLCOMMAND);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resuftAction;
    }
	public void dataRollBack(String hospitalcode,String yyyy,String mm){
		 this.hospitalcode=hospitalcode;
		 this.yyyy=yyyy;
		 this.mm=mm;
		DBConnection con2 =new DBConnection();
		con2.connectToLocal();
		con2.beginTrans();
		String sql="UPDATE TRN_DAILY SET" +
    		    " TRN_DAILY.RECEIPT_DATE = HIS_TRN_DAILY.RECEIPT_DATE," +
    		    " TRN_DAILY.MM = HIS_TRN_DAILY.MM," +
          		" TRN_DAILY.YYYY = HIS_TRN_DAILY.YYYY," +
          		" TRN_DAILY.IS_PAID = HIS_TRN_DAILY.IS_PAID," +
        		" TRN_DAILY.IS_DISCHARGE_SUMMARY = HIS_TRN_DAILY.IS_DISCHARGE_SUMMARY"+
          		" FROM"+
				" TRN_DAILY INNER JOIN HIS_TRN_DAILY"+
				" ON TRN_DAILY.LINE_NO = HIS_TRN_DAILY.LINE_NO" +
				" AND TRN_DAILY.HOSPITAL_CODE = HIS_TRN_DAILY.HOSPITAL_CODE" +
				" AND TRN_DAILY.HOSPITAL_CODE='"+this.hospitalcode+"' " +
				" AND HIS_TRN_DAILY.TAG='DISCHARGE'"+
				" AND HIS_TRN_DAILY.TRANSACTION_DATE LIKE '"+yyyy+mm+"%'";
     	System.out.println("Rollback Discharge Transaction : "+sql);
		con2.executeUpdate(sql);
		sql="DELETE FROM HIS_TRN_DAILY WHERE TAG='DISCHARGE' AND HOSPITAL_CODE='"+this.hospitalcode+"' AND TRANSACTION_DATE LIKE '"+yyyy+mm+"%'";
		System.out.println("Rollback Discharge Transaction Completed");
		con2.executeUpdate(sql);
		con2.commitTrans();
		con2.Close();
	}
	private String getColumnName(){
		DBConnection conncol=new DBConnection();
		conncol.connectToLocal(); 	
		ResultSet rscol=conncol.executeQuery("SELECT * FROM TRN_DAILY WHERE 1 != 1");
		String datacol="";
		try {
			String [] colname=new String[rscol.getMetaData().getColumnCount()];
			for(int i=1;i<=rscol.getMetaData().getColumnCount();i++){
				if(rscol.getMetaData().getColumnName(i).equals("TRANSACTION_DATE")){
					colname[i-1]=rscol.getMetaData().getColumnName(i)+"='"+batch.getYyyy()+batch.getMm()+JDate.getEndMonthDate(batch.getYyyy(),batch.getMm())+"'";
				}else{
					colname[i-1]=rscol.getMetaData().getColumnName(i);	
				}
			}
			datacol=join(colname, ",");
			rscol.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datacol;
	}
	public String join(final StringBuffer buff, final Object array[], final String delim){
		boolean haveDelim = (delim != null);
		for (int i = 0; i < array.length; i++){
			buff.append(array[i]);
			// if this is the last element then don't append delim
			if (haveDelim && (i + 1) < array.length){
				buff.append(delim);
			}
		}
		return buff.toString();
	}
	public String join(final Object array[], final String delim){
		return join(new StringBuffer(), array, delim);
	}
}