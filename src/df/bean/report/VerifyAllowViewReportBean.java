package df.bean.report;

import java.sql.ResultSet;
import java.sql.Statement;
import df.bean.db.conn.DBConn;

public class VerifyAllowViewReportBean {
	Statement stmt1;
    ResultSet rs;
    DBConn cdb;
    
    public String getReportPermit(String hospital){
        cdb = new DBConn();
        String status = "";
        String q = "SELECT YYYY+MM+PAYMENT_TERM FROM SUMMARY_PAYMENT WHERE HOSPITAL_CODE = '"+hospital+"' AND BATCH_NO = '' GROUP BY YYYY, MM, PAYMENT_TERM HAVING COUNT(*)>1";
        try{
        	cdb.setStatement();
        	status = cdb.getSingleData(q);
        	System.out.println(status);
        	status = status.length()>0 ? status : "00000000" ;
        }catch(Exception e){
        	System.out.println(e);
        }
    	return status;
    }

}
