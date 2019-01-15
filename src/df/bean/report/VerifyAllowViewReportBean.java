package df.bean.report;

import java.sql.ResultSet;
import java.sql.Statement;
import df.bean.db.conn.DBConn;

public class VerifyAllowViewReportBean {
	Statement stmt1;
    ResultSet rs;
    DBConn cdb;
    
    public boolean getReportPermit(String hospital,String paymentTerm,String month,String year){
        cdb = new DBConn();
        String q="";
        boolean status=true;
        if(paymentTerm != null){
        	if(paymentTerm=="2" || paymentTerm.equals("2")){
        		
        		 q = "SELECT YYYY+MM+PAYMENT_TERM FROM SUMMARY_PAYMENT WHERE HOSPITAL_CODE = '"+hospital+"' AND BATCH_NO = '"+year+month+"' AND PAYMENT_TERM='2' GROUP BY YYYY, MM, PAYMENT_TERM HAVING COUNT(*)>1";
        	}else{
        		
        		 q = "SELECT YYYY+MM+PAYMENT_TERM FROM SUMMARY_PAYMENT WHERE HOSPITAL_CODE = '"+hospital+"' AND BATCH_NO = '"+year+month+"' AND PAYMENT_TERM='1' GROUP BY YYYY, MM, PAYMENT_TERM HAVING COUNT(*)>1";
        	}
        }
        System.out.println(q);
        try{
        	cdb.setStatement();   
        	status = cdb.getSingleData(q).length()>0? true : false;
        	//status = status.length()>0 ? status : "00000000" ;
        	System.out.println("status from verifyPermit : "+status);
        }catch(Exception e){
        	System.out.println(e);
        }
        cdb.closeDB("");
    	return status;
    }

}