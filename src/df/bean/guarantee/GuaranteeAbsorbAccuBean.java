/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.guarantee;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import df.bean.db.conn.DBConn;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author arxim
 */
public class GuaranteeAbsorbAccuBean {
    DBConn cdb;
    String[][] temp_table = null;
    String previous_month = "";
    String previous_year = "";
    String month = "";
    String year = "";
    String hospital = "";
    String result = "";
    
    public GuaranteeAbsorbAccuBean(DBConn cdb){
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
    
    public void copyDataFile() throws Exception {
        String[] dt = null;
        int col_amt = 0;
        String line_data = "";
        Scanner inputStream = new Scanner(new FileInputStream(""));
        while (inputStream.hasNextLine()) {
            dt = line_data.split("[|]");
            String s = "SELECT INVOICE_NO, INVOICE_DATE, ORDER_ITEM_CODE, LINE_NO, " + //0-3
            "TRANSACTION_MODULE, YYYY, GUARANTEE_AMT, GUARANTEE_DR_CODE, " +           //4-7
            "GUARANTEE_CODE, GUARANTEE_TERM_MM, GUARANTEE_TERM_YYYY, GUARANTEE_PAID_AMT, " +//8-11
            "GUARANTEE_NOTE ,IS_PAID, DR_AMT FROM TRN_DAILY "+ //12-15
            "";
            /*
            if (line_data.split("[|]").length > col_amt) {
                col_amt = line_data.split("[|]").length;
            }
            */
        }
        inputStream.close();
        inputStream = null;
    }    
}
