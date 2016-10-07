/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.guarantee;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import df.bean.db.conn.DBConn;

/**
 *
 * @author arxim
 */
public class GuaranteeNewAbsorbAccuBean {
    DBConn cdb;
    String[][] temp_table = null;
    String previous_month = "";
    String previous_year = "";
    String month = "";
    String year = "";
    String hospital = "";
    String result = "";
    
    public GuaranteeNewAbsorbAccuBean(DBConn cdb){
        this.cdb = cdb;
    }
    
    public String getMessage(){
        return this.result;
    }
    
    public boolean summaryAbsorbAccu(String month, String year, String hospital){
        boolean status = true;
        try {
            //cdb.setStatement();
            this.month = month;
            this.year = year;
            this.hospital = hospital;
            if(this.month.equals("01")){
                previous_month = "12";
                previous_year = ""+(Integer.parseInt(this.year)-1); 
            }else{
                previous_month = ""+(Integer.parseInt(this.month)-1);
                if(previous_month.length()==1){
                    previous_month = "0"+previous_month;
                }
                previous_year = this.year;
            }
            duplicatePreviousAccu();
        } catch (Exception ex) {
            result = "Set Previous Month and Year Error\n"+
                     "Cause :"+ex;
            previous_month = "";
            previous_year = "";
            status = false;
            
        }
        return status;
    }
    
    private boolean duplicatePreviousAccu(){
        boolean status = true;
        String stm = "INSERT INTO SUMMARY_ABSORB (HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM, "+
                     "MONTHLY_ABSORB_TAX_400, MONTHLY_ABSORB_TAX_402, MONTHLY_ABSORB_TAX_406, "+
                     "ACCU_ABSORB_TAX_400, ACCU_ABSORB_TAX_402, ACCU_ABSORB_TAX_406) " +
                     "SELECT '"+hospital+"', SA.DOCTOR_CODE, '"+year+"', '"+month+"', 0, 0, 0, " +
                     "SA.ACCU_ABSORB_TAX_400, SA.ACCU_ABSORB_TAX_402, SA.ACCU_ABSORB_TAX_406 " +
                     "FROM SUMMARY_ABSORB SA "+
                     "WHERE SA.DOCTOR_CODE NOT IN (SELECT DOCTOR_CODE FROM SUMMARY_GUARANTEE " +
                     "WHERE YYYY = '"+year+"' AND MM = '"+month+"' AND SA.DOCTOR_CODE = DOCTOR_CODE) "+
                     "AND SA.YYYY = '"+previous_year+"' AND SA.MM = '"+previous_month+"'";
        //System.out.print(stm+"\n");
        try {
            cdb.insert(stm);
        }catch (SQLException ex) {
            result = "Duplicate Accu Previous Error\n"+
                     "Cause :"+ex;
            cdb.rollDB();
            status = false;
            System.out.println("Duplicate Summary Accu Code : "+ex);
        }
        return status;
    }
}
