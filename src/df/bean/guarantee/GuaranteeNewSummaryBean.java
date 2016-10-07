package df.bean.guarantee;

import java.sql.SQLException;
import df.bean.db.conn.DBConn;

public class GuaranteeNewSummaryBean {
    DBConn cdb;
    String[][] temp_table = null;
    String month = "";
    String year = "";
    String hospital = "";
    String result = "";

    public GuaranteeNewSummaryBean(DBConn cdb){
        try {
            this.cdb = cdb;
            if (this.cdb.getStatement() == null) {
                this.cdb.setStatement();
            }
        } catch (SQLException ex) {
            this.result = ""+ex;
        }
    }
    
    public String getMessage(){
        return this.result;
    }
    
    public boolean summaryProcess(String month, String year, String hospital, String process_type){
        boolean status = true;
        try {
            //cdb = new ConnectionDB();
            //cdb.setStatement();
            this.month = month;
            this.year = year;
            this.hospital = hospital;
            
            if(process_type.equals("Summary Guarantee Transaction")){
                status = sumAmountGuarantee();
            }
            if(process_type.equals("Summary Guarantee Tax")){
                status = sumTaxGuarantee();
            }
            if(process_type.equals("Summary Guarantee Monthly")){
                status = sumMonthGuarantee();
            }
        
        } catch (Exception ex) {
            status = false;
        }
        return status;
    }
    
    public boolean sumAmountGuarantee(){
        String[][] tmp = null;
        String temp = "";
        boolean status = true;
        String stm = "";
            //Select from Transaction and Sum Amount of Guarantee Seperate by Tax Type, Cash, Credit and Hold
            stm = "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406') THEN GUARANTEE_PAID_AMT ELSE '0' END) AS DF_406_ABSORB, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402') THEN GUARANTEE_PAID_AMT ELSE '0' END) AS DF_402_ABSORB, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400') THEN GUARANTEE_PAID_AMT ELSE '0' END) AS DF_400_ABSORB "+
                  
                  "FROM TRN_DAILY "+
                  "WHERE GUARANTEE_TERM_YYYY = '"+this.year+"' AND GUARANTEE_TERM_MM = '"+this.month+"' AND " +
                  "GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' AND "+ 
                  //comment line below for change calculate absorb 406 to absorb some guarantee only 18/01/2010
                  //"GUARANTEE_NOTE IN ('ABSORB GUARANTEE','ABSORB SOME GUARANTEE') AND "+
                  "HOSPITAL_CODE = '"+this.hospital+"' AND ACTIVE = '1' "+
                  "GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE";
            System.out.println(stm);
        tmp = cdb.query(stm);
        try {
            for(int i = 0; i<tmp.length; i++){
                //Return Message
                temp = "Update STP_GUARANTEE by GUARANTEE_DR_CODE="+tmp[i][1]+"' AND "+
                       "GUARANTEE_CODE="+tmp[i][2];
                //When Select Guarantee Amount finish
                //Update Guarantee Amount in to Guarantee Setup Table
                stm = "UPDATE STP_GUARANTEE SET "+
                      "DF406_HOLD_AMOUNT = '"+tmp[i][4]+"', "+
                      "DF402_HOLD_AMOUNT = '"+tmp[i][5]+"', "+
                      "DF400_HOLD_AMOUNT = '"+tmp[i][6]+"' "+
                      "WHERE HOSPITAL_CODE = '"+tmp[i][0]+"' AND "+
                      "GUARANTEE_DR_CODE = '"+tmp[i][1]+"' AND "+
                      "GUARANTEE_CODE = '"+tmp[i][2]+"' AND "+
                      "GUARANTEE_TYPE_CODE = '"+tmp[i][3]+"'";
                System.out.println(stm);
                cdb.insert(stm);
            }
            cdb.commitDB();
        } catch (Exception ex) {
            status = false;
            result = "Update calculate guarantee amount error : \n"+ex+
                     "\nCause "+temp;
            cdb.rollDB();
        }
        return status;
    }

    public boolean sumTaxGuarantee(){
        String[][] tmp = null;
        String temp = "";
        boolean status = true;

        String stm = "";
            stm = "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE_CODE "+
                  "FROM STP_GUARANTEE "+
                  "WHERE YYYY = '"+this.year+"' AND MM = '"+this.month+"' AND " +
                  //"GUARANTEE_NOTE = 'DF ABSORB' AND "+
                  "HOSPITAL_CODE = '"+this.hospital+"' AND ACTIVE = '1' "+
                  "GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE_CODE";
            //System.out.println(stm);
        tmp = cdb.query(stm);

        try{
            for(int i = 0; i<tmp.length; i++){
                temp = "UPDATE STP_GUARANTEE SET "+
                    "SUM_TAX_406 = DF406_HOLD_AMOUNT, "+
                    "SUM_TAX_402 = DF402_HOLD_AMOUNT+HP402_ABSORB_AMOUNT+GUARANTEE_EXCLUDE_AMOUNT, "+
                    "SUM_TAX_400 = DF400_HOLD_AMOUNT "+
                    "WHERE HOSPITAL_CODE = '"+tmp[i][0]+"' AND "+
                    "GUARANTEE_DR_CODE = '"+tmp[i][1]+"' AND "+
                    "GUARANTEE_CODE = '"+tmp[i][2]+"' AND "+
                    "GUARANTEE_TYPE_CODE = '"+tmp[i][3]+"'";        
                cdb.insert(temp);
            }
            cdb.commitDB();
        } catch (Exception ex) {
            status = false;
            result = "Update calculate guarantee amount error : \n"+ex+
                     "\nCause "+temp;
            cdb.rollDB();
        }
        return status;
    }
    private boolean sumMonthGuarantee(){
        boolean status = true;
        /*
        String stm ="INSERT INTO SUMMARY_GUARANTEE " +
                    "(HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM, "+
                    //"ADMISSION_TYPE_CODE, GUARANTEE_LOCATION, GUARANTEE_AMOUNT, "+
                    //"GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, "+
                    //"DF406_CASH_AMOUNT, DF406_CREDIT_AMOUNT, DF406_HOLD_AMOUNT, "+
                    //"DF402_CASH_AMOUNT, DF402_CREDIT_AMOUNT, DF402_HOLD_AMOUNT, "+
                    //"DF400_CASH_AMOUNT, DF400_CREDIT_AMOUNT, DF400_HOLD_AMOUNT, "+
                    //"DF_CASH_AMOUNT, DF_HOLD_AMOUNT, "+
                    //"DF406_ABSORB_AMOUNT, DF402_ABSORB_AMOUNT, DF400_ABSORB_AMOUNT, " +
                    //"DF_ABSORB_AMOUNT, HP402_ABSORB_AMOUNT," +
                    "SUM_HP_OVER_AMOUNT, "+//GUARANTEE_PAID_AMOUNT, "+
                    "SUM_TAX_406, SUM_TAX_402, SUM_TAX_400) "+
                     
                    "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, YYYY, MM, "+
                    //"SUM(DF406_CASH_AMOUNT), SUM(DF406_CREDIT_AMOUNT), SUM(DF406_HOLD_AMOUNT), "+
                    //"SUM(DF402_CASH_AMOUNT), SUM(DF402_CREDIT_AMOUNT), SUM(DF402_HOLD_AMOUNT), "+
                    //"SUM(DF400_CASH_AMOUNT), SUM(DF400_CREDIT_AMOUNT), SUM(DF400_HOLD_AMOUNT), "+
                    //"SUM(DF_CASH_AMOUNT), SUM(DF_HOLD_AMOUNT), " +
                    //"SUM(DF406_ABSORB_AMOUNT), SUM(DF402_ABSORB_AMOUNT), SUM(DF400_ABSORB_AMOUNT), " +
                    //"SUM(DF_ABSORB_AMOUNT), SUM(HP402_ABSORB_AMOUNT)," +
                    "SUM(SUM_HP_OVER_AMOUNT), "+ //SUM(GUARANTEE_PAID_AMOUNT), "+
                    "SUM(SUM_TAX_406), SUM(SUM_TAX_402), SUM(SUM_TAX_400) " +
                    "FROM STP_GUARANTEE "+
                    "WHERE HOSPITAL_CODE = '"+this.hospital+"' AND YYYY = '"+this.year+"' AND MM = '"+this.month+"' "+
                    "GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, YYYY, MM";
             System.out.println(stm);
        */

        try {
            cdb.insert("DELETE FROM SUMMARY_GUARANTEE WHERE HOSPITAL_CODE = '"
                    +this.hospital+"' AND YYYY = '"+this.year+"' AND MM = '"+this.month+"'");
            cdb.commitDB();
        } catch (SQLException ex) {
            cdb.rollDB();
            status = false;
            System.out.println("Delete Summary Month Guarantee : "+ex);
        }
        try {
        	cdb.insert(getSumScript());
            cdb.commitDB();
        } catch (SQLException ex) {
            cdb.rollDB();
            status = false;
            System.out.println("Update Summary Month Guarantee : "+ex);
        }
        return status;
    }

    private String getSumScript(){
    	String t = 
		"INSERT INTO SUMMARY_GUARANTEE " +
        "(HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM, "+
        "GUARANTEE_AMOUNT, GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, "+
        "GUARANTEE_EXCLUDE_AMOUNT, SUM_HP_OVER_AMOUNT, "+
        "SUM_TAX_406, SUM_TAX_402, SUM_TAX_400) "+
        
        "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, YYYY, MM, "+
        "GUARANTEE_AMOUNT, GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, "+
        "GUARANTEE_EXCLUDE_AMOUNT, SUM_HP_OVER_AMOUNT, "+
    	"SUM_TAX_406, SUM_TAX_402, SUM_TAX_400 "+
    	"FROM VW_SUM_GUARANTEE "+ 
    	"WHERE HOSPITAL_CODE = '"+this.hospital+"' AND YYYY = '"+this.year+"' AND MM = '"+this.month+"'"+
    	"ORDER BY GUARANTEE_DR_CODE, YYYY, MM ";
    	return t;
    }
    
    public void updateGuaranteeDate(){
    	String u = "";
    }
}

