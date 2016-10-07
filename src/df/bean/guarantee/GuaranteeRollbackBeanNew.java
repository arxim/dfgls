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
public class GuaranteeRollbackBeanNew {
    DBConn c;
    String err_mesg;
    public GuaranteeRollbackBeanNew(){
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
    private String getRollbackExpenseDetail(String h, String y, String m){
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
        "ABSORB_REMAIN_AMOUNT = 0, ABSORB_AMOUNT = 0, DEDUCT_ABSORB_AMOUNT = 0, "+
        "START_DATE = OLD_START_DATE, END_DATE = OLD_END_DATE, ACTIVE = OLD_ACTIVE, " +
        "GUARANTEE_AMOUNT = OLD_GUARANTEE_AMOUNT, " +
        "GUARANTEE_FIX_AMOUNT = OLD_GUARANTEE_FIX_AMOUNT, "+
        "GUARANTEE_INCLUDE_AMOUNT = OLD_GUARANTEE_INCLUDE_AMOUNT, "+
    	"GUARANTEE_EXCLUDE_AMOUNT = OLD_GUARANTEE_EXCLUDE_AMOUNT, " +
        "GUARANTEE_SOURCE = OLD_GUARANTEE_SOURCE " +
        "WHERE HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+year+"' AND MM = '"+month+"' ";
        //"AND PROCESS_STATUS = 'Y'";
            
        if(c.countRow(q_check)>0){
        //if(true){
            try {
            	System.out.println("Start Rollback Setup Guarantee time : "+JDate.getTime());
            	c.insert(this.getRollbackSummary(hospital_code, year, month));
            	c.insert(this.getRollbackExpenseDetail(hospital_code, year, month));
                c.insert(sq);
                c.insert("UPDATE STP_GUARANTEE SET DF_ABSORB_AMOUNT = OLD_ABSORB_AMOUNT"+
                " WHERE HOSPITAL_CODE = '"+hospital_code+"' AND DF_ABSORB_AMOUNT != OLD_ABSORB_AMOUNT");
                c.commitDB();
                System.out.println("Rollback Setup Finish time : "+JDate.getTime());
                System.out.println("Rollback Guarantee Process Finished");
            } catch (SQLException ex) {
                System.out.println("Error Mes From rollBackSetup : "+ex);
                err_mesg = ""+ex;
                st = false;
            }
        }else{
        	System.out.println("Can't Rollback Guarantee Setup : Guarantee is no process");
        }
        return st;
    }
    
    public boolean rollBackTransaction(String hospital_code, String year, String month){
        boolean st = true;
        String mess = "";
        String q_check = "SELECT * FROM SUMMARY_GUARANTEE WHERE HOSPITAL_CODE = '"+hospital_code+"' " +
        "AND YYYY = '"+year+"' AND MM = '"+month+"'";

        String sq1 = 
        "UPDATE TRN_DAILY SET "+
        "GUARANTEE_CODE = '', GUARANTEE_DR_CODE = '', GUARANTEE_TYPE = '', " +
        "GUARANTEE_DATE_TIME = '', GUARANTEE_TERM_MM = '', GUARANTEE_TERM_YYYY = '', "+
        "GUARANTEE_NOTE = '', IS_GUARANTEE = '', "+
        "IS_PAID = 'Y', ORDER_ITEM_ACTIVE = '0', "+
        "GUARANTEE_PAID_AMT = 0, "+
        "GUARANTEE_AMT = CASE WHEN COMPUTE_DAILY_USER_ID NOT LIKE 'ALLOC%' THEN 0 ELSE GUARANTEE_AMT END, "+
        "DR_AMT = OLD_DR_AMT, "+
        "HP_AMT = HP_PREMIUM - OLD_DR_AMT, "+
        "AMOUNT_AFT_DISCOUNT = HP_PREMIUM, "+
        "DR_TAX_401 = CASE WHEN TAX_TYPE_CODE = '401' THEN OLD_TAX_AMT ELSE '0' END, "+
        "DR_TAX_402 = CASE WHEN TAX_TYPE_CODE = '402' THEN OLD_TAX_AMT ELSE '0' END, "+
        "DR_TAX_406 = CASE WHEN TAX_TYPE_CODE = '406' THEN OLD_TAX_AMT ELSE '0' END "+
        "WHERE TRANSACTION_DATE LIKE '"+year+month+"%' "+//change by nop 20100706
        "AND HOSPITAL_CODE = '"+hospital_code+"' AND BATCH_NO = ''";

		String sq4 = "UPDATE TRN_DAILY SET  YYYY = '', MM = '', PAY_BY_CASH = 'N', RECEIPT_NO = '', RECEIPT_DATE = '' "
		+ "WHERE  TRANSACTION_DATE LIKE '"+year+""+month+"%' "
		+ "AND (RECEIPT_NO = 'DISCHARGE' OR RECEIPT_NO = 'ADVANCE') "
		+ "AND GUARANTEE_TERM_MM = '" + month + "' "
		+ "AND GUARANTEE_TERM_YYYY = '" +  year + "' "
		+ "AND GUARANTEE_NOTE NOT LIKE '%EXTRA%' "
		+ "AND HOSPITAL_CODE = '" + hospital_code + "' "
		+ "AND MM = '" +  month + "'  AND YYYY = '" +  year + "' "
		//+ "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' " 
		+ "AND (BATCH_NO IS NULL OR BATCH_NO = '') ";
		
		//CANCEL THIS SCRIPT TO BE USE sq4 SCRIPT
        //String sq2 = "UPDATE TRN_DAILY SET YYYY = '', MM = '', PAY_BY_CASH = 'N', RECEIPT_NO = '', RECEIPT_DATE = '' "+
        //"WHERE RECEIPT_NO = 'ADVANCE' AND YYYY = '"+year+"' AND MM = '"+month+"' AND HOSPITAL_CODE = '"+hospital_code+"'";
        
        String sq3 = "DELETE TRN_DAILY WHERE LINE_NO LIKE '%ADV' AND TRANSACTION_DATE LIKE '"+year+month+"%' "+
        "AND HOSPITAL_CODE = '"+hospital_code+"'";

        if(c.countRow(q_check)>0){
        //if(true){
	        try {
	            mess = sq4;
	            c.insert(sq4);
	            System.out.println("Rollback Transaction Step 2 Discharge & Advance Complete");
	            mess = sq1;
	            c.insert(sq1);
	            System.out.println("Rollback Transaction Step 3 Guarantee&Tax Complete");
	            mess = sq3;
	            c.insert(sq3);
	            System.out.println("Rollback Transaction Step 4 Delete Advance Complete");
	            c.commitDB();
	            System.out.println("Rollback All Transaction Guarantee Complete");
	        } catch (SQLException ex) {
	            System.out.println("Error Mes From rollBackTransaction : "+ex);
	            System.out.println(mess);
	            err_mesg = ""+ex;
	            st = false;
	        }
        }else{
        	System.out.println("Can't Rollback Guarantee Transaction : Guarantee is no process");
        }
        return st;
    }
}