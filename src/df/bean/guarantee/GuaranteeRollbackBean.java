/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.guarantee;

import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nopphadon
 */
public class GuaranteeRollbackBean {
    DBConn c;
    String err_mesg;
    public GuaranteeRollbackBean(DBConn db){
        //c = db;
        try{
        	c = new DBConn();
            c.setStatement();
        }catch(Exception e){
            System.out.println("Exception Set Statement from GuaranteeRollbackBean Class");
        }
    }
    public String getErrorMessage(){
        return this.err_mesg;
    }
    private String getRollbackSummary(String h, String y, String m){
    	String t = 
        	"DELETE SUMMARY_GUARANTEE " +
        	"WHERE HOSPITAL_CODE = '"+h+"' AND YYYY = '"+y+"' " +
        	"AND MM = '"+m+"'";
    	return t;
    }
    private String getRollbackExpense(String h, String y, String m){
    	String t = 
        	"DELETE TRN_EXPENSE_DETAIL " +
        	"WHERE HOSPITAL_CODE = '"+h+"' AND YYYY = '"+y+"' " +
        	"AND MM = '"+m+"' AND EMPLOYEE_ID = 'ProcessGuarantee'";
    	return t;
    }
    public boolean rollBackSetup(String hospital_code, String year, String month){
        boolean st = true;
        String q_check = "SELECT * FROM SUMMARY_GUARANTEE WHERE HOSPITAL_CODE = '"+hospital_code+"' " +
        "AND YYYY = '"+year+"' AND MM = '"+month+"'";
        
        String sq = "UPDATE STP_GUARANTEE SET DF406_CASH_AMOUNT = 0, DF406_CREDIT_AMOUNT = 0, "+
        "DF406_HOLD_AMOUNT = 0, DF402_CASH_AMOUNT = 0, DF402_CREDIT_AMOUNT = 0, DF402_HOLD_AMOUNT = 0, "+
        "DF400_CASH_AMOUNT = 0, DF400_CREDIT_AMOUNT = 0, DF400_HOLD_AMOUNT = 0, DF_CASH_AMOUNT = 0, "+
        "DF_HOLD_AMOUNT = 0, HP402_ABSORB_AMOUNT = 0, DF406_ABSORB_AMOUNT = 0, DF402_ABSORB_AMOUNT = 0, "+
        "DF400_ABSORB_AMOUNT = 0, OVER_GUARANTEE_AMOUNT = 0, SUM_DR_OVER_AMOUNT = 0, "+
        "SUM_HP_OVER_AMOUNT = 0, GUARANTEE_PAID_AMOUNT = 0, SUM_TAX_406 = 0, SUM_TAX_402 = 0, SUM_TAX_400 = 0, "+
        "START_DATE = OLD_START_DATE, END_DATE = OLD_END_DATE, ACTIVE = OLD_ACTIVE, " +
        "GUARANTEE_AMOUNT = OLD_GUARANTEE_AMOUNT, " +
        "GUARANTEE_FIX_AMOUNT = OLD_GUARANTEE_FIX_AMOUNT, "+
        "GUARANTEE_INCLUDE_AMOUNT = OLD_GUARANTEE_INCLUDE_AMOUNT, "+
    	"GUARANTEE_EXCLUDE_AMOUNT = OLD_GUARANTEE_EXCLUDE_AMOUNT, " +
        "GUARANTEE_SOURCE = OLD_GUARANTEE_SOURCE " +
        "WHERE HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+year+"' AND MM = '"+month+"'";
            
        if(c.countRow(q_check)>0){
            try {
            	System.out.println("Start Rollback Setup Guarantee time : "+JDate.getTime());
            	c.insert(this.getRollbackSummary(hospital_code, year, month));
            	c.insert(getRollbackExpense(hospital_code, year, month));
                c.insert(sq);
                System.out.println("Finish Rollback Setup Guarantee time : "+JDate.getTime());
                System.out.println("Rollback Setup Guarantee ACTIVE = OLD_ACTIVE");
                c.insert("UPDATE STP_GUARANTEE SET DF_ABSORB_AMOUNT = OLD_ABSORB_AMOUNT"+
                " WHERE HOSPITAL_CODE = '"+hospital_code+"'");
                System.out.println("Rollback Setup Guarantee DF_ABSORB_AMOUNT = OLD_ABSORB_AMOUNT");
                c.commitDB();
            } catch (SQLException ex) {
                System.out.println("Error Mes From rollBackSetup : "+ex);
                err_mesg = ""+ex;
                st = false;
            }
        }
        return st;
    }
    
    public boolean rollBackTransaction(String hospital_code, String year, String month){
        boolean st = true;
        String mess = "";
        String q_check = "SELECT * FROM SUMMARY_GUARANTEE WHERE HOSPITAL_CODE = '"+hospital_code+"' " +
        "AND YYYY = '"+year+"' AND MM = '"+month+"'";

        String sq0 = "UPDATE TRN_DAILY SET ORDER_ITEM_ACTIVE = '0'" +
                     " WHERE TRANSACTION_DATE LIKE '"+year+""+month+"%' "+
                     "AND HOSPITAL_CODE = '"+hospital_code+"'";
        /*
        String sq1 = "UPDATE TRN_DAILY SET GUARANTEE_PAID_AMT = 0, GUARANTEE_AMT = 0, "+
        "GUARANTEE_CODE = '', GUARANTEE_DR_CODE = '', GUARANTEE_TYPE = '', " +
        "GUARANTEE_DATE_TIME = '', GUARANTEE_TERM_MM = '', GUARANTEE_TERM_YYYY = '', "+
        "GUARANTEE_NOTE = '', IS_GUARANTEE = '', DR_AMT = OLD_DR_AMT, "+
        "HP_AMT = AMOUNT_AFT_DISCOUNT - OLD_DR_AMT "+
        "WHERE GUARANTEE_TERM_MM = '"+month+"' AND GUARANTEE_TERM_YYYY = '"+year+"' " +
        "AND COMPUTE_DAILY_USER_ID NOT LIKE 'ALLOC%' AND HOSPITAL_CODE = '"+hospital_code+"'";
		*/
        String sq1 = "UPDATE TRN_DAILY SET GUARANTEE_PAID_AMT = 0, "+
        "GUARANTEE_AMT = CASE WHEN COMPUTE_DAILY_USER_ID NOT LIKE 'ALLOC%' THEN 0 ELSE "+
        "GUARANTEE_AMT END, "+
        "GUARANTEE_CODE = '', GUARANTEE_DR_CODE = '', GUARANTEE_TYPE = '', " +
        "GUARANTEE_DATE_TIME = '', GUARANTEE_TERM_MM = '', GUARANTEE_TERM_YYYY = '', "+
        "GUARANTEE_NOTE = '', IS_GUARANTEE = '', IS_PAID = 'Y', DR_AMT = OLD_DR_AMT, "+
        "HP_AMT = AMOUNT_AFT_DISCOUNT - OLD_DR_AMT, "+
        "DR_TAX_400 = CASE WHEN TAX_TYPE_CODE = '400' THEN OLD_TAX_AMT ELSE '0' END, "+
        "DR_TAX_401 = CASE WHEN TAX_TYPE_CODE = '401' THEN OLD_TAX_AMT ELSE '0' END, "+
        "DR_TAX_402 = CASE WHEN TAX_TYPE_CODE = '402' THEN OLD_TAX_AMT ELSE '0' END, "+
        "DR_TAX_406 = CASE WHEN TAX_TYPE_CODE = '406' THEN OLD_TAX_AMT ELSE '0' END "+
        //"WHERE INVOICE_DATE LIKE '"+year+month+"%' " +
        "WHERE TRANSACTION_DATE LIKE '"+year+month+"%' "+//change by nop 20100706
        //"WHERE GUARANTEE_TERM_MM = '"+month+"' AND GUARANTEE_TERM_YYYY = '"+year+"' " +
        "AND HOSPITAL_CODE = '"+hospital_code+"'";

        String sq2 = "UPDATE TRN_DAILY SET " +
        "YYYY = '', "+
        "MM = '', "+
        "PAY_BY_CASH = 'N', "+
        "RECEIPT_NO = '', "+
        "RECEIPT_DATE = '' "+
        "WHERE RECEIPT_NO = 'ADVANCE' " +
        "AND YYYY = '"+year+"' AND MM = '"+month+"' "+
        "AND HOSPITAL_CODE = '"+hospital_code+"'";
        System.out.println("Count Row : "+c.countRow(q_check));
        if(c.countRow(q_check)>0){
            try {
            	mess = sq0;
                c.insert(sq0);
                System.out.println("Rollback Transaction Step 1 Complete");
                mess = sq1;
                c.insert(sq1);
                System.out.println("Rollback Transaction Step 2 Complete");
                mess = sq2;
                c.insert(sq2);
                c.commitDB();
                System.out.println("Rollback All Transaction Guarantee Complete");
            } catch (SQLException ex) {
                System.out.println("Error Mes From rollBackTransaction : "+ex);
                System.out.println(mess);
                err_mesg = ""+ex;
                st = false;
            }
        }
        return st;
    }
}