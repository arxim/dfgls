package df.bean.process;

import java.sql.SQLException;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

public class ProcessSummaryMonthly {
	private Batch b;
	private DBConn cn = new DBConn();
	private DBConnection conn = new DBConnection();
	
    public ProcessSummaryMonthly() {
    	try {
			cn.setStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	conn.connectToLocal();
	}
    
    public boolean processMonthly(String hospitalCode, String user_id){
    	boolean status = true;
    	b = new Batch(hospitalCode,conn);

    	try{
    		String[][] st = cn.query(this.getSQLSum_DAILY(hospitalCode));
    		//conn.executeUpdate(this.getSQLSum_DAILY(hospitalCode));
    		if(Variables.IS_TEST){
            	System.out.println(st.length+":"+this.getSQLSum_DAILY(hospitalCode));    			
    		}
    	}catch(Exception e){
    		System.out.println(e);
    		status = false;
    	}
    	return status;
    }
	public String getSQLSum_DAILY(String hospitalCode) {
        return "INSERT INTO SUMMARY_MONTHLY ("+
        	    "YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, "+
        	    "TRANSACTION_DATE, "+
        	    "SUM_AMT, "+
        	    "SUM_DISC_AMT, "+
        	    "DR_SUM_AMT, "+
        	    "DR_NET_PAID_AMT, "+
        	    "DR_TAX_400, "+
        	    "DR_TAX_401, "+
        	    "DR_TAX_402, "+
        	    "DR_TAX_406, "+
        	    "DR_PREMIUM_AMT, "+
        	    "HP_SUM_AMT, "+
        	    "HP_PREMIUM_AMT, "+
        	    "HP_TAX, "+
        	    "SUM_PREMIUM_REC_AMT, "+
        	    ""+
                ") "+
               "select " +
        		b.getYyyy()+", "+b.getMm()+", "+hospitalCode+", "+
        		"DOCTOR_CODE, " +
        		JDate.getDate()+", "+
        		"SUM(AMOUNT_AFT_DISCOUNT) as sinv_amt, " +
        		"SUM(AMOUNT_OF_DISCOUNT) as sinv_discount_amt, " + 
                "SUM(DR_AMT) as sDr_Amt," +
                "SUM(DR_AMT) as sDr_Netpaid_Amt," +
                "SUM(DR_TAX_400) as sDr_Amt_400, " +
                "SUM(DR_TAX_401) as sDr_Amt_401, " + 
                "SUM(DR_TAX_402) as sDr_Amt_402, " +
                "SUM(DR_TAX_406) as sDr_Amt_406,  " +
                "SUM(DR_PREMIUM) as sDr_Premium,  " + 
                "SUM(HP_AMT) as sHp_Amt, " +
                "SUM(HP_PREMIUM) as sHp_Premium, " +
                "SUM(HP_TAX) as sHp_Tax,  " + 
                //"SUM(0) as sRec_Amt, " +
                //"SUM(0) as sRec_Premium_Amt,  " +
                "SUM(PREMIUM_REC_AMT) as sPremium_Rec_Amt, " + 
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN 0 ELSE DR_AMT END ) AS sDr_Amt_CASH, " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN DR_AMT ELSE 0 END ) AS sDr_Amt_AR, " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN 0 ELSE DR_AMT END) AS sCash_Amt,  " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN 0 ELSE DR_TAX_400 END) AS sCash_400,  " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN 0 ELSE DR_TAX_401 END) AS sCash_401,  " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN 0 ELSE DR_TAX_402 END) AS sCash_402,  " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN 0 ELSE DR_TAX_406 END) AS sCash_406,  " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN DR_AMT ELSE 0 END) AS sAr_Amt,  " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN DR_TAX_400 ELSE 0 END) AS sAr_400,  " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN DR_TAX_401 ELSE 0 END) AS sAr_401,  " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN DR_TAX_402 ELSE 0 END) AS sAr_402,  " +
                "SUM(CASE PAY_BY_AR WHEN 'Y' THEN DR_TAX_406 ELSE 0 END) AS sAr_406 " +
                "FROM TRN_DAILY " +
                "where HOSPITAL_CODE ='" + hospitalCode + "' " + 
                "and (BATCH_NO is null or BATCH_NO = '') " + 
                "and YYYY='" + b.getYyyy() + "' and MM='" + b.getMm() + "' " +
                "and DOCTOR_CODE is not null AND DOCTOR_CODE != '' " +
                "and ACTIVE = '1' " +
                "and ORDER_ITEM_ACTIVE = '1' "+
                "and COMPUTE_DAILY_DATE is not null AND COMPUTE_DAILY_DATE != '' " +
                "and (PAY_BY_CASH='Y' OR  PAY_BY_AR='Y' OR PAY_BY_DOCTOR='Y' OR PAY_BY_PAYOR='Y' OR PAY_BY_CASH_AR='Y' ) " +
                "and IS_PAID = 'Y' " +
                "group By DOCTOR_CODE order by DOCTOR_CODE";
    }
}
