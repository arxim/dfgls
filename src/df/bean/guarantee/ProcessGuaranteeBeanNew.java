package df.bean.guarantee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import df.bean.db.conn.DBConn;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;
import df.bean.obj.util.JNumber;
import df.bean.obj.util.Variables;

public class ProcessGuaranteeBeanNew {
	
    DBConn cdb;
    boolean status_guarantee = true;
    String[][] g_setup = null;
    String result = "";
    String month = "";
    String year = "";
    String hospital_code = "";
    String user_id = "";
    String guarantee_note = "";
    double dr_amt = 0.00;
    double hp_amt = 0.00;
    double tax_amt = 0.00;
    double sum_trn_guarantee_amt = 0.00;
    double sum_trn_guarantee_balance = 0.00;
    double sum_amount_aft_discount = 0.00;
    double trn_guarantee_amt = 0.00;
    double trn_guarantee_paid_amt = 0.00;
    double percent_in_allocate = 0.00;
    double percent_over_allocate = 0.00;
    double amount_allocate = 0.00;
    double guarantee_balance_paid = 0.00;
    double guarantee_balance = 0.00;
    double guarantee_paid = 0.00; //ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚Â´ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Âµ
    double guarantee_amt = 0.00;
    String guarantee_allocate_condition = "";
    
    /*
    DF_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
    HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
    DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
    DF406_HOLD_AMOUNT = ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
    */
    public ProcessGuaranteeBeanNew(DBConn cdb){
        try {
            this.cdb = cdb;
            if (this.cdb.getStatement() == null) {
                this.cdb.setStatement();
            }

        } catch (SQLException ex) {
            this.result = ""+ex;
            System.out.println(ex);
        }
    }
    
    public String getMessage(){
        return this.result;
    }
    
    public boolean prepareProcess(String month, String year, String hospital_code, String process_type, String user_id){
       
    	boolean status = true;
        this.month = month;
        this.year = year;
        this.hospital_code = hospital_code;
        this.user_id = user_id;
        
        //new method
        String q_check = "SELECT * FROM SUMMARY_GUARANTEE WHERE HOSPITAL_CODE = '"+hospital_code+"' " +
        "AND YYYY = '"+year+"' AND MM = '"+month+"'";
        
        status = cdb.countRow(q_check) == 0 ? true : false ;
        
        System.out.print(Variables.IS_TEST ? "Guarantee Process Type : "+process_type+" Status = "+status+"\n" : "");
       
        
        if(status && process_type.equals("Set OrderItem Active")){
        	status = true;
        	status = this.setOrderItemActive();
        }
        
        if(status && process_type.equals("Set OrderItem Guarantee")){
        	status = this.setOrderItemGuarantee();
        }
        
        if(status && process_type.equals("Backup Tax Before Guarantee")){
        	status = this.backupTransactionTax();
        	status = this.setAllocateInGuarantee();
        	status = this.setAllocateOverGuarantee();
        	status = this.setGuaranteeSource();
        }
        
        if(status && process_type.equals("Prorate Process")){
        	status = this.proRate();
        }
        
        if(status && process_type.equals("Adjust Guarantee Amount")){
        	status = this.sumGuaranteeDailyToMonthly();
        }
        
        if(status && process_type.equals("Guarantee Calculate Step")){
        	
        	if(this.setTransactionStep()){
            	status = this.calculateGuaranteeStep();        		
        	}else{
        		status = false;
        	}
        	
        }
        
        if(status && process_type.equals("Set Guarantee Transaction")){
        	status = this.setTransactionGuarantee();
        }
        
        if(status && process_type.equals("Guarantee Calculate")){
        	status = this.calculateGuarantee();
        }
        
        if(status && process_type.equals("Calculate Guarantee Old Absorb")){
        	status = this.calculatePreviousGuarantee();
        }
        
        if(status && process_type.equals("Summary Guarantee Transaction")){
            status = sumAmountGuarantee();
        }
        
        if(status && process_type.equals("Summary Guarantee Tax")){
            status = sumTaxGuarantee();
        }
        
        if(status && process_type.equals("Summary Guarantee Monthly")){
        	// Calculate guarantee monthly to yearly.
        	calculateMonthlyToYear();
        
        	status = sumMonthGuarantee();
        }
        
        if(status && process_type.equals("Export Summary Absorb")){
        	status = insertExpenseGuaranteeHP();
        	System.out.println("Start Export HP "+status);
        	if(status){
                status = insertAbsorbSomeGuarantee();        		
            	System.out.println("Start Export DR "+status);        		
        	}
        	if(status){
            	status = insertExpenseGuaranteeEX();
            	System.out.println("Finish Export EX "+status);        		
        	}
        	if(status){
            	status = insertExpenseGuaranteeFix();
            	System.out.println("Finish Export Fix "+status);        		
        	}
        	if(status){
            	status = insertExpenseGuaranteeInclude();
            	System.out.println("Finish Export Include "+status);        		
        	}
        }
        return status;
    }
    
    private void messageWrite(int i, String[][] guarantee_table, int x, String[][]transaction_table, String message){
    	if(Variables.IS_TEST){
    		System.out.print( message +
    	    		"|"+this.guarantee_amt+
    	    		"|"+this.sum_trn_guarantee_amt+
    	            "|"+this.guarantee_balance+
    	            "|"+this.guarantee_paid+
    	            "|"+this.sum_trn_guarantee_balance+
    	            "|"+this.trn_guarantee_amt+
    	            "|"+transaction_table[x][3]);
    	}
    }
    
    private boolean setOrderItemActive(){ //new method
    	//this method for set transaction by order item is active
    	boolean status = true;
    	String message = "Set Transaction to Order Item is Active";
        String sql_statement = "UPDATE TRN_DAILY SET ORDER_ITEM_ACTIVE = '1' " +
        "WHERE "+
        "TRANSACTION_DATE LIKE '"+year+""+month+"%' AND "+
        "HOSPITAL_CODE = '"+hospital_code+"' AND "+
        //UPDATE (DR_AMT >= 0) FOR CANCEL INVOICE ON 21/11/2010 NOPP
        "DR_AMT >= 0 AND BATCH_NO = '' AND "+
        "(ORDER_ITEM_CODE IN " +
        "(SELECT CODE FROM ORDER_ITEM WHERE ACTIVE = '1' AND HOSPITAL_CODE = '"+hospital_code+"')) ";

        try{
        	if(Variables.IS_TEST){
        		System.out.println("Update Order Item Active Start TIME "+JDate.getTime());
        	}
            
            cdb.insert(sql_statement);
            cdb.commitDB();
            
            if(Variables.IS_TEST){
        		System.out.println("Update Order Item Active Complete TIME "+JDate.getTime());
        	}
        }catch(Exception e){
        	TRN_Error.setUser_name(this.user_id);
        	TRN_Error.setHospital_code(hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
        }
    	return status;
    }
    
    private boolean setOrderItemGuarantee(){ //new method
    	//this method for set transaction by order item is guarantee
    	boolean status = true;
    	String message = "Set Transaction for Order Item is Guarantee Calculate";
        String old_sql_statement = "UPDATE TRN_DAILY SET IS_GUARANTEE = 'Y' " +
        "WHERE TRANSACTION_DATE LIKE '"+year+""+month+"%' AND "+
        "INVOICE_TYPE <> 'ORDER' AND COMPUTE_DAILY_DATE <> '' AND "+
        "HOSPITAL_CODE = '"+hospital_code+"' AND BATCH_NO = '' AND "+
        "ORDER_ITEM_CODE IN (SELECT CODE FROM ORDER_ITEM WHERE IS_GUARANTEE = 'Y' " +
        	"AND ACTIVE = '1' AND HOSPITAL_CODE = '"+hospital_code+"')";
        
    	String sql_statement = "UPDATE TRN_DAILY SET " +
    	"TRN_DAILY.ORDER_ITEM_ACTIVE = ORDER_ITEM.ACTIVE, "+
    	"TRN_DAILY.IS_GUARANTEE = ORDER_ITEM.IS_GUARANTEE "+
    	"FROM " +
    	"TRN_DAILY JOIN ORDER_ITEM ON " +
    	"TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND "+
    	"TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "+
    	"WHERE " +
    	"TRN_DAILY.HOSPITAL_CODE = '"+hospital_code+"' AND "+
        "TRANSACTION_DATE LIKE '"+year+""+month+"%'";

        try{
            System.out.println("Update Order Item is Guarantee Start TIME "+JDate.getTime());
            cdb.insert(sql_statement);
            cdb.commitDB();
            System.out.println("Update Order Item is Guarantee Complete TIME "+JDate.getTime());
        }catch(Exception e){
        	TRN_Error.setUser_name(this.user_id);
        	TRN_Error.setHospital_code(hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
        }
    	return status;
    }
    
    private boolean backupTransactionTax(){ //new method
    	//this method for backup tax transaction
    	boolean status = true;
    	String message = "Backup tax before guarantee";
    	
        String sql_statement = "UPDATE TRN_DAILY SET "+
        //"OLD_TAX_AMT = DR_TAX_406+DR_TAX_402+DR_TAX_401, " +
        "OLD_TAX_AMT = CASE WHEN TAX_TYPE_CODE = '401' THEN DR_TAX_401 ELSE "+
        "CASE WHEN TAX_TYPE_CODE = '402' THEN DR_TAX_402 ELSE "+
        "CASE WHEN TAX_TYPE_CODE = '406' THEN DR_TAX_406 ELSE "+
        "'0' END END END, "+

        "OLD_DR_AMT = DR_AMT, HP_PREMIUM = AMOUNT_AFT_DISCOUNT "+
		"WHERE TRANSACTION_DATE LIKE '"+year+""+month+"%' "+
		"AND HOSPITAL_CODE = '"+hospital_code+"' AND BATCH_NO = '' ";

        String sql_statement1 = "UPDATE TRN_DAILY SET TAX_FROM_ALLOCATE = 'Y' " +
		"WHERE TRANSACTION_DATE LIKE '"+year+""+month+"%' "+
		"AND HOSPITAL_CODE = '"+hospital_code+"' AND BATCH_NO = '' "+
		"AND ORDER_ITEM_CODE IN (SELECT CODE FROM ORDER_ITEM WHERE HOSPITAL_CODE = '"+hospital_code+"' " +
		"AND IS_ALLOC_FULL_TAX = 'N')";
        
        String sql_statement2 = "UPDATE TRN_DAILY SET TAX_FROM_ALLOCATE = 'N' " +
		"WHERE TRANSACTION_DATE LIKE '"+year+""+month+"%' "+
		"AND HOSPITAL_CODE = '"+hospital_code+"' AND BATCH_NO = '' "+
		"AND (TAX_FROM_ALLOCATE <> 'Y' OR TAX_FROM_ALLOCATE IS NULL) "+
		"AND ORDER_ITEM_CODE IN (SELECT CODE FROM ORDER_ITEM WHERE HOSPITAL_CODE = '"+hospital_code+"' " +
		"AND IS_ALLOC_FULL_TAX = 'Y')";

        try{
            System.out.println("Backup Tax Transaction Start TIME "+JDate.getTime());
            cdb.insert(sql_statement);
            message = "Backup Transaction Tax Error";
            cdb.insert(sql_statement1);
            message = "Set Tax from DF Error";
            cdb.insert(sql_statement2);
            message = "Set Tax from Amount Error";
            cdb.commitDB();
            System.out.println("Backup Tax Transaction Complete TIME "+JDate.getTime());
        }catch(Exception e){
        	TRN_Error.setUser_name(this.user_id);
        	TRN_Error.setHospital_code(hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
            cdb.rollDB();
            status = false;
        }
    	return status;
    }
    
    private boolean setAllocateInGuarantee(){
    	//THIS METHOD UPDATE IN_ALLOCATE FROM DOCTOR TO STP_GUARANTEE ONLY
    	String message = "Update percent in allocate to guarantee table";
        boolean status1 = true;
        String sql= "";
        String[][] doctor = null;
        
        try{
            sql = "SELECT CODE, OVER_GUARANTEE_PCT, IN_GUARANTEE_PCT FROM DOCTOR " +
                  "WHERE ACTIVE = '1' AND CODE = GUARANTEE_DR_CODE AND " +
                  "HOSPITAL_CODE = '"+this.hospital_code+"'";
            
            doctor = cdb.query(sql);
            
            for(int i = 0; i<doctor.length; i++){
            	try{
                	if(Integer.parseInt(""+doctor[i][1])<1){
                		doctor[i][1] = "100";
                	}
            	}catch(Exception e){
            		doctor[i][1] = "100";
            	}
            	try{
                	if(Integer.parseInt(""+doctor[i][2])<1){
                		doctor[i][2] = "100";
                	}
            	}catch(Exception e){
            		doctor[i][2] = "100";
            	}
            	
        		sql = "UPDATE STP_GUARANTEE SET GUARANTEE_ALLOCATE_PCT = "+Integer.parseInt(doctor[i][2])+" "+
                "WHERE (GUARANTEE_ALLOCATE_PCT IS NULL OR GUARANTEE_ALLOCATE_PCT < 1) " +
                "AND YYYY = '"+year+"' AND MM = '"+month+"' " +
                "AND GUARANTEE_DR_CODE = '"+doctor[i][0]+"' "+
                "AND GUARANTEE_TYPE_CODE != 'STP' " +
                "AND HOSPITAL_CODE = '"+hospital_code+"' "+
                "AND ACTIVE = '1'";
            	try{
            		cdb.insert(sql);
                    cdb.commitDB();
            	}catch(Exception e){
            		cdb.rollDB();
                	TRN_Error.setUser_name(this.user_id);
                	TRN_Error.setHospital_code(hospital_code);
                    TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql,"");
            		System.out.println("Exception Update In Allocate : "+e);
            		System.out.println("By Query : "+sql);
            	}
            }
            System.out.println("Update In Percent to Setup guarantee Complete");
        }catch(Exception e){
            System.out.println("Excepiton in transfer allocate from doctor : "+e);
        }
        return status1;
    }
    
    private boolean setAllocateOverGuarantee(){
    	
    	//THIS METHOD UPDATE OVER_ALLOCATE FROM DOCTOR TO STP_GUARANTEE ONLY
        boolean status1 = true;
        String sql= "";
        String[][] doctor = null;
    	String message = "Update percent over allocate to guarantee table";

        try{
            sql = "SELECT CODE, OVER_GUARANTEE_PCT, IN_GUARANTEE_PCT " +
            	  "FROM DOCTOR " +
                  "WHERE ACTIVE = '1' AND CODE = GUARANTEE_DR_CODE AND " +
                  "HOSPITAL_CODE = '"+this.hospital_code+"'";
            doctor = cdb.query(sql);
            
            for(int i = 0; i<doctor.length; i++){
            	try{
                	if(Integer.parseInt(""+doctor[i][1])<1){
                		doctor[i][1] = "100";
                	}
            	}catch(Exception e){
            		doctor[i][1] = "100";
            	}
            	try{
                	if(Integer.parseInt(""+doctor[i][2])<1){
                		doctor[i][2] = "100";
                	}
            	}catch(Exception e){
            		doctor[i][2] = "100";
            	}
        		sql = "UPDATE STP_GUARANTEE SET OVER_ALLOCATE_PCT = "+Integer.parseInt(doctor[i][1])+" " +
                "WHERE (OVER_ALLOCATE_PCT IS NULL OR OVER_ALLOCATE_PCT < 2) " +
                "AND YYYY = '"+year+"' AND MM = '"+month+"' " +
                "AND GUARANTEE_DR_CODE = '"+doctor[i][0]+"' "+
                "AND GUARANTEE_TYPE_CODE != 'STP' " +
                "AND HOSPITAL_CODE = '"+hospital_code+"' "+
                "AND ACTIVE = '1'";
            	try{
            		cdb.insert(sql);
                    cdb.commitDB();
            	}catch(Exception e){
            		cdb.rollDB();
                	TRN_Error.setUser_name(this.user_id);
                	TRN_Error.setHospital_code(hospital_code);
                    TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql,"");
            		System.out.println("Exception Update Over Allocate : "+e);
            		System.out.println("By Query : "+sql);
            	}
            }
            System.out.println("Update Over Percent to Setup guarantee Complete");
        }catch(Exception e){
            System.out.println("Excepiton in transfer allocate from doctor : "+e);
        }
        return status1;
    }
    
    private boolean setGuaranteeSource(){
    	//THIS METHOD UPDATE GUARANTEE_SOURCE FROM DOCTOR TO STP_GUARANTEE ONLY
        boolean status1 = true;
        String sql= "";
        String[][] doctor = null;
        String[][] time_table = null;
    	String message = "Update guarantee source/day to guarantee table";
        try{
            sql = "SELECT CODE, CASE WHEN GUARANTEE_SOURCE = 'AF' THEN 'AF' ELSE 'BF' END AS GUARANTEE_SOURCE " +
            	  "FROM DOCTOR " +
                  "WHERE ACTIVE = '1' AND CODE = GUARANTEE_DR_CODE AND " +
                  "HOSPITAL_CODE = '"+this.hospital_code+"'";
            doctor = cdb.query(sql);
            
            for(int i = 0; i<doctor.length; i++){
        		sql = "UPDATE STP_GUARANTEE SET GUARANTEE_SOURCE = '"+doctor[i][1]+"' "+
                "WHERE YYYY = '"+year+"' AND MM = '"+month+"' " +
                "AND (GUARANTEE_SOURCE = '' OR GUARANTEE_SOURCE IS NULL) "+
                "AND GUARANTEE_DR_CODE = '"+doctor[i][0]+"' "+
                "AND GUARANTEE_TYPE_CODE != 'STP' " +
                "AND HOSPITAL_CODE = '"+hospital_code+"' "+
                "AND ACTIVE = '1'";
            	try{
            		//if(Variables.IS_TEST){ System.out.println("Guarantee source update : "+sql); }
            		cdb.insert(sql);
                    cdb.commitDB();
            	}catch(Exception e){
            		cdb.rollDB();
                	TRN_Error.setUser_name(this.user_id);
                	TRN_Error.setHospital_code(hospital_code);
                    TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql,"");
            		if(Variables.IS_TEST){ System.out.println("Exception Update Guarantee Source : "+e); }
            	}
            }
            if(Variables.IS_TEST){
                System.out.println("Update Guarantee Source to Setup guarantee Complete");
                System.out.println("Start Update Guarantee Day to Stp_guarantee Table");            	
            }
            sql = "SELECT CASE WHEN GT.GUARANTEE_DAY = '' THEN " +
            	  "CASE WHEN DR.GUARANTEE_DAY = '' THEN HP.GUARANTEE_DAY ELSE DR.GUARANTEE_DAY END " +
            	  "ELSE GT.GUARANTEE_DAY END GUARANTEE_DAY_COMPLETE, "+
            	  "GT.GUARANTEE_DR_CODE, GT.GUARANTEE_CODE, GT.GUARANTEE_TYPE_CODE, GT.ADMISSION_TYPE_CODE, " +
            	  "GT.MM, GT.YYYY, GT.START_DATE, GT.START_TIME, GT.END_DATE, GT.END_TIME "+
            	  "FROM STP_GUARANTEE GT "+
            	  "LEFT OUTER JOIN DOCTOR DR ON GT.HOSPITAL_CODE = DR.HOSPITAL_CODE AND GT.GUARANTEE_DR_CODE = DR.CODE "+
            	  "LEFT OUTER JOIN HOSPITAL HP ON GT.HOSPITAL_CODE = HP.CODE "+
            	  "WHERE GT.HOSPITAL_CODE = '"+hospital_code+"' AND GT.YYYY = '"+year+"' AND GT.MM = '"+month+"' AND GT.ACTIVE = '1'";
            time_table = cdb.query(sql);
            for(int i = 0; i<time_table.length; i++){
        		sql = "UPDATE STP_GUARANTEE SET GUARANTEE_DAY = '"+time_table[i][0]+"' "+
                "WHERE HOSPITAL_CODE = '"+hospital_code+"' "+
                "AND YYYY = '"+year+"' AND MM = '"+month+"' " +
                "AND (GUARANTEE_DAY = '' OR GUARANTEE_DAY IS NULL) "+
                "AND GUARANTEE_DR_CODE = '"+time_table[i][1]+"' "+
                "AND GUARANTEE_CODE = '"+time_table[i][2]+"' "+
                "AND GUARANTEE_TYPE_CODE = '"+time_table[i][3]+"' "+
                "AND ADMISSION_TYPE_CODE = '"+time_table[i][4]+"' "+
                "AND ACTIVE = '1'";
            	try{
            		cdb.insert(sql);
                    cdb.commitDB();
            	}catch(Exception e){
            		cdb.rollDB();
                	TRN_Error.setUser_name(this.user_id);
                	TRN_Error.setHospital_code(hospital_code);
                    TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql,"");
                    if(Variables.IS_TEST){
                		System.out.println("Exception Update Guarantee Day : "+e);                    	
                    }
            	}
            }
        }catch(Exception e){
            System.out.println("Excepiton in Guarantee Source from doctor : "+e);
        }
        return status1;
    }
    private boolean proRate(){
    	System.out.print(Variables.IS_TEST ? "Prorate Process\n" : "");
    	String list_guarantee = "";
    	String[][] arr_checklist = null;
    	String q_process = "";
    	String message = "Prorate Expire Guarantee";
    	String query_message = "";
    	String guarantee_backup = "UPDATE STP_GUARANTEE SET OLD_ACTIVE = ACTIVE, " +
    	"OLD_START_DATE = START_DATE, OLD_END_DATE = END_DATE, "+
    	"OLD_GUARANTEE_AMOUNT = GUARANTEE_AMOUNT, " +
    	"OLD_GUARANTEE_SOURCE = GUARANTEE_SOURCE, " +
    	"OLD_GUARANTEE_FIX_AMOUNT = GUARANTEE_FIX_AMOUNT, " +
    	"OLD_GUARANTEE_INCLUDE_AMOUNT = GUARANTEE_INCLUDE_AMOUNT, "+
    	"OLD_GUARANTEE_EXCLUDE_AMOUNT = GUARANTEE_EXCLUDE_AMOUNT " +
    	"WHERE HOSPITAL_CODE = '"+hospital_code+"' AND MM = '" + month + "' AND YYYY = '"+year+"'";
    	
    	try {
    		message = "Backup Prorate Process";
    		query_message = guarantee_backup;
			cdb.insert(guarantee_backup);
			cdb.commitDB();
			list_guarantee = "SELECT "+
	    	"CASE "+
	    		"WHEN DR.GUARANTEE_START_DATE <= TT.START_DATE "+
	    		"THEN "+
	    		"CASE "+
	    			"WHEN DR.GUARANTEE_EXPIRE_DATE >= TT.END_DATE "+
	    			"THEN 'NORMAL' "+
	    			"ELSE "+
	    				"CASE "+
	    					"WHEN SUBSTRING(DR.GUARANTEE_EXPIRE_DATE,0,7) >= SUBSTRING(TT.END_DATE,0,7) "+
	    					"AND TT.START_DATE <= DR.GUARANTEE_EXPIRE_DATE "+
	    					"THEN 'PRO-RATE' "+
	    					"ELSE 'EXPIRE' END "+
	    			"END "+
	    		"ELSE "+
	    			"CASE "+
	    				"WHEN SUBSTRING(DR.GUARANTEE_START_DATE,0,7) <= SUBSTRING(TT.START_DATE,0,7) "+
	    				//Case Doctor Guarantee Start date 15/02/2013
	    				//but User key Guarantee Daily 12/02/2013 - 12/02/2013
	    				//"THEN '1st PRO-RATE' "+ comment by nopp 20130305
	    					//start add by nopp 20130305
	    				"THEN "+
	    					"CASE WHEN DR.GUARANTEE_START_DATE > TT.START_DATE AND DR.GUARANTEE_START_DATE > TT.END_DATE "+
	    					"THEN 'EXPIRE' ELSE '1st PRO-RATE' END "+
	    					//end add by nopp 20130305
	    				"ELSE 'EXPIRE' END "+
	    		"END AS PRORATE_TYPE, "+
	    	"TT.HOSPITAL_CODE, DR.GUARANTEE_START_DATE, DR.GUARANTEE_EXPIRE_DATE, "+//1-3
	    	"TT.GUARANTEE_DR_CODE, TT.GUARANTEE_TYPE_CODE, "+//4-5
	    	"TT.START_TIME, TT.END_TIME, TT.GUARANTEE_AMOUNT, TT.GUARANTEE_EXCLUDE_AMOUNT, "+//6-9
	    	"TT.GUARANTEE_FIX_AMOUNT, TT.GUARANTEE_CODE "+//10-11
	    	"FROM STP_GUARANTEE TT LEFT OUTER JOIN DOCTOR DR "+
	    	"ON TT.HOSPITAL_CODE = DR.HOSPITAL_CODE AND TT.GUARANTEE_DR_CODE = DR.CODE "+
	    	"WHERE TT.HOSPITAL_CODE='"+hospital_code+"' AND TT.GUARANTEE_AMOUNT > 0 "+
	    	"AND TT.YYYY = '"+year+"' AND TT.MM = '"+month+"'";
			arr_checklist = cdb.query(list_guarantee);
			if(arr_checklist.length>0){
				try{
				for(int i = 0; i < arr_checklist.length; i++){
					
					if(arr_checklist[i][0].equals("EXPIRE")){//EXPIRE
						q_process = "UPDATE STP_GUARANTEE SET ACTIVE = '0', USER_ID = 'EXPIRE' WHERE HOSPITAL_CODE = '"+hospital_code+"' "+
						"AND YYYY = '"+year+"' AND MM = '"+month+"' AND GUARANTEE_DR_CODE = '"+arr_checklist[i][4]+"' "+
						"AND GUARANTEE_CODE = '"+arr_checklist[i][11]+"'";
						if(Variables.IS_TEST){
							System.out.print(i+":"+arr_checklist[i][0]+":"+q_process);
						}
						cdb.insert(q_process);
					}else if(arr_checklist[i][0].equals("PRO-RATE")){ //EXPIRE BEFORE END MONTH
						double guarantee_amount = 0;
						double guarantee_fix = 0;
						guarantee_amount = (Double.parseDouble(arr_checklist[i][8])*
								(Double.parseDouble(arr_checklist[i][3].substring(6, 8))*100)
								/Double.parseDouble(JDate.getEndMonthDate(year, month))/100);
						guarantee_fix = (Double.parseDouble(arr_checklist[i][10])*
								(Double.parseDouble(arr_checklist[i][3].substring(6, 8))*100)
								/Double.parseDouble(JDate.getEndMonthDate(year, month))/100);

						q_process = "UPDATE STP_GUARANTEE SET GUARANTEE_AMOUNT = '"+guarantee_amount+"', "+
						//if guarantee table change date same expire date release comment line below
						"END_DATE = '"+arr_checklist[i][3]+"', "+ 
						"USER_ID = 'PRORATE', "+
						"GUARANTEE_FIX_AMOUNT = '"+guarantee_fix+"' "+
						"WHERE HOSPITAL_CODE = '"+hospital_code+"' "+
						"AND YYYY = '"+year+"' AND MM = '"+month+"' " +
						"AND GUARANTEE_DR_CODE = '"+arr_checklist[i][4]+"' "+
						"AND GUARANTEE_CODE = '"+arr_checklist[i][11]+"'";
						if(Variables.IS_TEST){
							System.out.print(i+":"+arr_checklist[i][0]+":"+q_process);
						}
						cdb.insert(q_process);
						
					}else if(arr_checklist[i][0].equals("1st PRO-RATE")){ //START AFTER BEGIN MONTH
						double guarantee_amount = 0;
						double guarantee_fix = 0;
						guarantee_amount = (Double.parseDouble(arr_checklist[i][8])*
								(((Double.parseDouble(JDate.getEndMonthDate(year, month)))-((Double.parseDouble(arr_checklist[i][2].substring(6, 8)))-1))*100)
								/Double.parseDouble(JDate.getEndMonthDate(year, month))/100);
						
						guarantee_fix = (Double.parseDouble(arr_checklist[i][10])*
								((Double.parseDouble(JDate.getEndMonthDate(year, month)))-(Double.parseDouble(arr_checklist[i][2].substring(6, 8))-1)*100)
								/Double.parseDouble(JDate.getEndMonthDate(year, month))/100);

						q_process = "UPDATE STP_GUARANTEE SET GUARANTEE_AMOUNT = '"+guarantee_amount+"', "+
						"USER_ID = '1stPRORATE', "+
						//if guarantee table change date same start date release comment line below
						"START_DATE = '"+arr_checklist[i][2]+"', "+ 
						"GUARANTEE_FIX_AMOUNT = '"+guarantee_fix+"' "+
						"WHERE HOSPITAL_CODE = '"+hospital_code+"' "+
						"AND YYYY = '"+year+"' AND MM = '"+month+"' " +
						"AND GUARANTEE_DR_CODE = '"+arr_checklist[i][4]+"' "+
						"AND GUARANTEE_CODE = '"+arr_checklist[i][11]+"'";
						if(Variables.IS_TEST){
							System.out.print(i+":"+arr_checklist[i][0]+":"+q_process);
						}
						cdb.insert(q_process);
						
					}else{//IN GUARANTEE
						if(arr_checklist[i][0].equals("NORMAL")){
						}else{
							System.out.println(i+":"+arr_checklist[i][0]);
						}
						q_process = "";
					}
					try{
						cdb.insert(q_process);
						cdb.commitDB();
					}catch(Exception e){
						message = "Change Date/Guarantee Amount for Prorate Process";
			    		query_message = q_process;
						TRN_Error.setUser_name(this.user_id);
						TRN_Error.setHospital_code(this.hospital_code);
			            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), query_message,"");
					}
					//}
				}
				
				}catch(Exception e){
					TRN_Error.setUser_name(this.user_id);
					TRN_Error.setHospital_code(this.hospital_code);
		            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), query_message,"");
					System.out.print(Variables.IS_TEST ? e+"\n" : "");
				}
			}
			
		} catch (SQLException e) {
			TRN_Error.setUser_name(this.user_id);
			TRN_Error.setHospital_code(this.hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), query_message,"");
			System.out.print(Variables.IS_TEST ? "Process not action : "+e+"\n" : "");
		}
    	return true;
    }
    private boolean sumGuaranteeDailyToMonthly(){
    	//This Process is update amount of guarantee every transaction to once price
    	//for VTN Site (guarantee daily to monthly)
    	boolean status = true;
    	String[][] arr_guarantee = null;
    	String update_guarantee = "";
    	int all_records = 0;
    	int complete_records = 0;
    	String message = "Adjust Guarantee Daily to Monthly Calculate Amount";
    	/*
    	String get_guarantee = "SELECT GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, "+
    	"SUM(GUARANTEE_AMOUNT) FROM STP_GUARANTEE WHERE GUARANTEE_TYPE_CODE LIKE 'M%' "+
    	"AND HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+year+"' AND MM = '"+month+"' "+
    	"AND ACTIVE = '1' AND GUARANTEE_AMOUNT > 0 AND IS_GUARANTEE_DAILY = 'Y' "+
    	"GROUP BY GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE HAVING COUNT(*) > 1";
    	*/
    	String get_guarantee = "SELECT GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, "+
    	"SUM(GUARANTEE_AMOUNT) FROM STP_GUARANTEE WHERE GUARANTEE_TYPE_CODE LIKE 'M%' "+
    	"AND HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+year+"' AND MM = '"+month+"' "+
    	"AND ACTIVE = '1' AND GUARANTEE_AMOUNT > 0 AND IS_GUARANTEE_DAILY = 'Y' "+
    	"GROUP BY GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE HAVING COUNT(*) > 1";
    	
    	arr_guarantee = cdb.query(get_guarantee);
    	if(arr_guarantee.length>0){
    		all_records = arr_guarantee.length;
    		for(int i = 0; i < arr_guarantee.length; i++){
    			update_guarantee = "UPDATE STP_GUARANTEE SET GUARANTEE_AMOUNT = '"+arr_guarantee[i][3]+"' " +
    					"WHERE HOSPITAL_CODE = '"+hospital_code+"' "+
    					"AND YYYY = '"+year+"' AND MM = '"+month+"' AND IS_GUARANTEE_DAILY = 'Y' " +
    					"AND GUARANTEE_DR_CODE = '"+arr_guarantee[i][0]+"' "+
    					"AND GUARANTEE_CODE = '"+arr_guarantee[i][1]+"' "+
    					"AND ADMISSION_TYPE_CODE = '"+arr_guarantee[i][2]+"'";
    			try {
					cdb.insert(update_guarantee);
					complete_records++;
				} catch (SQLException e) {
					status = false;
                	TRN_Error.setUser_name(this.user_id);
                	TRN_Error.setHospital_code(hospital_code);
		            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), update_guarantee,"");
				}
    		}
    		if(status){
    			cdb.commitDB();
    		}else{
    			cdb.rollDB();
    		}
    		//System.out.println("Process update Guarantee Amount of Guarantee Daily to Monthly Complete "+complete_records+" records from "+all_records+" records.");
    	}
    	return status;
    }
    
    private boolean setTransactionStep(){
        boolean status = true;
        String sql_statement = "";
        String guarantee_code = "";
        String admission = "";
        String start_time = "";
        String end_time = "";
        String guarantee_include_extra = "";
        String guarantee_day = "";
        String guarantee_day_con = "";
        int t = 0;
        String day_condition = "";
    	String message = "Set Guarantee Transaction Step Conditions";
        try {
        	//*dUPLICATE DATA
            //get guarantee setup (STP_GUARANTEE Table) to verify and update flag
            //into transaction table(TRN_DAILY)
            sql_statement = "SELECT SG.HOSPITAL_CODE, SG.GUARANTEE_DR_CODE, DR.CODE, SG.GUARANTEE_TYPE_CODE, "+ //0-3
            "SG.ADMISSION_TYPE_CODE, SG.MM, SG.YYYY, SG.START_DATE, SG.START_TIME, SG.EARLY_TIME, " +           //4-9
            "SG.END_DATE, SG.END_TIME, SG.LATE_TIME, SG.GUARANTEE_LOCATION, SG.GUARANTEE_AMOUNT, " +            //10-14
            "SG.GUARANTEE_EXCLUDE_AMOUNT, SG.GUARANTEE_SOURCE, SG.GUARANTEE_FIX_AMOUNT, SG.GUARANTEE_CODE, "+   //15-18
            "HP.GUARANTEE_INCLUDE_EXTRA, HP.GUARANTEE_DAY, DR.GUARANTEE_DAY, SG.GUARANTEE_DAY "+ //19-22
            "from STP_GUARANTEE SG " +
            "LEFT OUTER JOIN DOCTOR DR ON (SG.GUARANTEE_DR_CODE = DR.GUARANTEE_DR_CODE AND SG.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
            "LEFT OUTER JOIN HOSPITAL HP ON SG.HOSPITAL_CODE = HP.CODE "+
            "WHERE SG.ACTIVE = '1' AND DR.ACTIVE = '1' AND SG.MM = '"+month+"' AND SG.YYYY = '"+year+"' AND "+
            "SG.GUARANTEE_TYPE_CODE = 'STP' AND "+
            "SG.HOSPITAL_CODE = '"+hospital_code+"' AND DR.HOSPITAL_CODE = '"+hospital_code+"' "+
            "ORDER BY SG.GUARANTEE_TYPE_CODE, SG.GUARANTEE_DR_CODE";
            g_setup = cdb.query(sql_statement);
            System.out.println("Set Transaction Step : "+sql_statement);
            
        	t = g_setup.length;
        	
            for(int i = 0; i<g_setup.length; i++){ //update flag in trn_daily for calculate guarantee
            	System.out.println("Guarantee Process Step 6 Running to "+i+" Of "+t+" ON TIME "+JDate.getTime());
                String is_paid = "";
                if(Double.parseDouble(g_setup[i][17])== 0){//if != fix guarantee
                    is_paid = "Y";
                }else{
                    is_paid = "N";
                }
                
                guarantee_code = g_setup[i][18];
                
                try{//Check Setup Guarantee Location?
                    if(g_setup[i][13].length()<2){
                        g_setup[i][13] = "%";
                    }
                }catch(Exception e){
                	g_setup[i][13] = "%";
                }
                
                if(g_setup[i][4].equals("U")){//Admission Type
                    admission = "ADMISSION_TYPE_CODE LIKE '%' AND ";
                }else{
                    admission = "ADMISSION_TYPE_CODE LIKE '" + g_setup[i][4] + "' AND ";
                }
                
                try{//Start Time
                    if(g_setup[i][9].equals("000000") || g_setup[i][9].equals("0")){
                        start_time = g_setup[i][8];
                    }else{
                        start_time = g_setup[i][9];
                    }
                }catch(Exception e){
                	start_time = g_setup[i][8];
                    System.out.println("Exception guarantee early time : "+e);
                }
                try{//End Time
                    if(g_setup[i][12].equals("000000") || g_setup[i][12].equals("0")){
                        end_time = g_setup[i][11];
                    }else{
                        end_time = g_setup[i][12];
                    }
                }catch(Exception e){
                	end_time = g_setup[i][11];
                    System.out.println("Exception guarantee late time : "+e);
                }
                guarantee_include_extra = g_setup[i][19].equals("Y")? "GUARANTEE_TERM_MM LIKE '%' AND " : "GUARANTEE_TERM_MM = '' AND ";
                day_condition = g_setup[i][20].equals("VER") ? "VER" : "INV";
                day_condition = g_setup[i][21].equals("VER") ? "VER" : "INV";
                day_condition = g_setup[i][22].equals("VER") ? "VER" : "INV";
                
                if(day_condition.equals("VER")){//Guarantee Day? Verify Date = "VER", Invoice Date = "INV"
                	guarantee_day = "GUARANTEE_DATE_TIME = VERIFY_DATE+''+VERIFY_TIME ";
                }else{
                	guarantee_day = "GUARANTEE_DATE_TIME = TRANSACTION_DATE+''+VERIFY_TIME ";
                }
                
                if(day_condition.equals("VER")){//Guarantee Day? Verify Date = "VER", Invoice Date = "INV"
                	guarantee_day_con = "AND (VERIFY_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND ";
                }else{
                	guarantee_day_con = "AND (TRANSACTION_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND ";
                }

                try{
                    sql_statement = "UPDATE TRN_DAILY SET " +
                    "GUARANTEE_CODE = '"+ guarantee_code + "', ";
                    if(g_setup[i][16].equals("AF")){
                    	//if guarantee from after allocate
                    	//OLD COMMENT 20100920 sql_statement += "GUARANTEE_AMT = CASE WHEN GUARANTEE_AMT = 0 THEN DR_AMT ELSE GUARANTEE_AMT END, ";
                    	sql_statement += "GUARANTEE_AMT = DR_AMT, ";
                    }else{
                    	//if guarantee from before allocate
                    	//OLD COMMENT 20100920 sql_statement +="GUARANTEE_AMT = CASE WHEN GUARANTEE_AMT = 0 THEN AMOUNT_AFT_DISCOUNT ELSE GUARANTEE_AMT END, ";
                    	sql_statement +="GUARANTEE_AMT = AMOUNT_AFT_DISCOUNT, ";
                    }
                    sql_statement += "IS_PAID = '"+ is_paid + "', " +
                    "GUARANTEE_DR_CODE = '" + g_setup[i][1] + "', " +
                    "GUARANTEE_TYPE = '" + g_setup[i][3] + "', " +
                    //"GUARANTEE_TERM_MM = '" + g_setup[i][5] +"', "+
                    "GUARANTEE_TERM_YYYY = '" + g_setup[i][6] +"', "+guarantee_day+
                    "WHERE DOCTOR_CODE = '" + g_setup[i][2] + "' AND " + 
                    admission + //guarantee_include_extra +
                    "COMPUTE_DAILY_DATE <> '' AND "+
                    "PATIENT_LOCATION_CODE LIKE '" + g_setup[i][13] + "' AND " +
                    "(TRANSACTION_DATE BETWEEN '"+g_setup[i][6]+""+g_setup[i][5]+"01' AND '"+g_setup[i][6]+""+g_setup[i][5]+"31') AND "+
                    "VERIFY_DATE IS NOT NULL "+guarantee_day_con+
                    //comment 20100920 "AND (VERIFY_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND "+//add 20100902 nopp
                    "HOSPITAL_CODE = '" + g_setup[i][0] + "' AND " +
                    "ACTIVE = '1' AND " +
                    "DR_AMT > 0 AND "+//Update 20100915(10:31)
                    "ORDER_ITEM_ACTIVE = '1' AND " +
                    "BATCH_NO = '' AND "+
                    "IS_GUARANTEE = 'Y'";

                    if(Variables.IS_TEST){
	                    System.out.println(sql_statement);	                    	
                    }
                    
                    cdb.insert(sql_statement); //comment for skip write database process
                    cdb.commitDB(); //comment for skip write database process
                
                }catch(Exception e){
                	cdb.rollDB();
                	status = false;
                	TRN_Error.setUser_name(this.user_id);
                	TRN_Error.setHospital_code(hospital_code);
		            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
                	System.out.println("Some transaction don't process step 6");
                	System.out.println("Exception update guarantee step 6 : "+e);
                	System.out.println("Command : "+sql_statement+"\n");
                }
            }
        } catch (Exception ex) {
        	System.out.println("Must to Rollback Guarantee Process!!");
            System.out.println("Exception Guarantee Step : "+ex);
            status = false;
        }
        System.out.println("FINISH GUARANTEE STEP 2 "+JDate.getTime());
        return status;
    }
    
    private boolean setTransactionGuarantee(){
        boolean status = true;
        String sql_statement = "";
        String guarantee_code = "";
        String admission = "";
        String start_time = "";
        String end_time = "";
        String guarantee_include_extra = "";
        String guarantee_day = "";
        String guarantee_day_con = "";
        int t = 0;
        String day_condition = "";
        String dischargeCondition = "";
        String onwardCondition = "";
        String guarantee_location = "";
    	String message = "Set Guarantee Transaction Conditions";
        try {
            //get guarantee setup (STP_GUARANTEE Table) to verify and update flag
            //into transaction table(TRN_DAILY)
            sql_statement = "SELECT SG.HOSPITAL_CODE, SG.GUARANTEE_DR_CODE, DR.CODE, SG.GUARANTEE_TYPE_CODE, "+ //0-3
            "SG.ADMISSION_TYPE_CODE, SG.MM, SG.YYYY, SG.START_DATE, SG.START_TIME, SG.EARLY_TIME, " +           //4-9
            "SG.END_DATE, SG.END_TIME, SG.LATE_TIME, SG.GUARANTEE_LOCATION, SG.GUARANTEE_AMOUNT, " +            //10-14
            "SG.GUARANTEE_EXCLUDE_AMOUNT, SG.GUARANTEE_SOURCE, SG.GUARANTEE_FIX_AMOUNT, SG.GUARANTEE_CODE, "+   //15-18
            "HP.GUARANTEE_INCLUDE_EXTRA, HP.GUARANTEE_DAY, DR.GUARANTEE_DAY, SG.GUARANTEE_DAY, SG.IS_INCLUDE_LOCATION, "+ //19-23
            "SG.IS_GUARANTEE_DAILY, HP.IS_DISCHARGE_BASIS, HP.IS_GUARANTEE_ONWARD "+//24-26
            "from STP_GUARANTEE SG " +
            "LEFT OUTER JOIN DOCTOR DR ON (SG.GUARANTEE_DR_CODE = DR.GUARANTEE_DR_CODE AND SG.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
            "LEFT OUTER JOIN HOSPITAL HP ON SG.HOSPITAL_CODE = HP.CODE "+
            "WHERE SG.ACTIVE = '1' AND DR.ACTIVE = '1' AND SG.MM = '"+month+"' AND SG.YYYY = '"+year+"' AND "+
            "SG.GUARANTEE_TYPE_CODE <> 'STP' AND "+
            "SG.HOSPITAL_CODE = '"+hospital_code+"' AND DR.HOSPITAL_CODE = '"+hospital_code+"' "+
            "ORDER BY SG.GUARANTEE_TYPE_CODE, SG.GUARANTEE_DR_CODE";
            g_setup = cdb.query(sql_statement);
            //System.out.println(sql_statement);
            
        	t = g_setup.length;
        	System.out.println("Set Guarantee Transaction Running Start Time "+JDate.getTime());
            for(int i = 0; i<g_setup.length; i++){ //update flag in trn_daily for calculate guarantee
                String is_paid = "";

                //Check Fix Guarantee
                is_paid = Double.parseDouble(g_setup[i][17])== 0 ? "Y" : "N";
                guarantee_code = g_setup[i][18];
                
                //Check Admission Type Condition
                if(g_setup[i][4].equals("U")){
                    admission = "ADMISSION_TYPE_CODE LIKE '%' AND ";
                }else{
                    admission = "ADMISSION_TYPE_CODE LIKE '" + g_setup[i][4] + "' AND ";
                }
                
                try{//Start Time
                    if(g_setup[i][9].equals("000000") || g_setup[i][9].equals("0") || g_setup[i][9].equals("")){
                        start_time = g_setup[i][8];
                    }else{
                        start_time = g_setup[i][9];
                    }
                }catch(Exception e){
                	start_time = g_setup[i][8];
                    System.out.println("Exception guarantee early time : "+e);
                }
                
                try{//End Time
                    if(g_setup[i][12].equals("000000") || g_setup[i][12].equals("0") || g_setup[i][12].equals("")){
                        end_time = g_setup[i][11];
                    }else{
                        end_time = g_setup[i][12];
                    }
                }catch(Exception e){
                	end_time = g_setup[i][11];
                    System.out.println("Exception guarantee late time : "+e);
                }

                //Guarantee Date from Hospital Setup
                try{
	                if(g_setup[i][20].equals("VER")){
	                	day_condition = "VER";
	                }else{
	                	day_condition = "INV";
	                }
                }catch(Exception e){
                	System.out.println("Error Guarantee Setup Day Condition from hospital : "+e);
                }

                //Guarantee Date from Doctor Setup
                try{
	                if(g_setup[i][21].equals("VER")){
	                	day_condition = "VER";
	                }else{
	                	day_condition = "INV";
	                }
                }catch(Exception e){
                	System.out.println("Error Guarantee Setup Day Condition from doctor : "+e);
                }

                //Guarantee Date from Time Table
                try{
	                if(g_setup[i][22].equals("VER")){
	                	day_condition = "VER";
	                }else{
	                	day_condition = "INV";
	                }
	            }catch(Exception e){
	            	System.out.println("Error Guarantee Setup Day Condition from Time Table : "+e);
	            }

	            //Guarantee Include Extra?
	            try{
		            if(g_setup[i][19].equals("Y")){
	                	guarantee_include_extra = "GUARANTEE_TERM_MM LIKE '%' AND ";
	                }else{
	                	guarantee_include_extra = "GUARANTEE_TERM_MM = '' AND ";
	                }
	            }catch(Exception e){
	            	//Default Guarantee Include DF In Extra Time
                	guarantee_include_extra = "GUARANTEE_TERM_MM LIKE '%' AND ";
	            }

	            //Create Statement from Guarantee Date Condition
	            if(day_condition.equals("VER")){//Guarantee Day? Verify Date = "VER", Invoice Date = "INV"
	            	if(g_setup[i][24].equals("N")){
	                	guarantee_day_con = "AND (VERIFY_DATE BETWEEN '"+g_setup[i][7]+"' AND '"+g_setup[i][10]+"') "+
	                	"AND (VERIFY_TIME BETWEEN '"+start_time+"' AND '"+end_time+"') AND ";
	            	}else{
	                	guarantee_day_con = "AND (VERIFY_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND ";
	            	}
                }else{
	            	if(g_setup[i][24].equals("N")){
	                	guarantee_day_con = "AND (TRANSACTION_DATE BETWEEN '"+g_setup[i][7]+"' AND '"+g_setup[i][10]+"') "+
	                	"AND (VERIFY_TIME BETWEEN '"+start_time+"' AND '"+end_time+"') AND ";
	            	}else{
	                	guarantee_day_con = "AND (TRANSACTION_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND ";
	            	}
                }

	            try{
	                if(g_setup[i][13].trim().equals("") || g_setup[i][13]==null){
	                	guarantee_location = "";
	                }else{
	                	if(g_setup[i][23].trim().equals("Y")){
		                	guarantee_location = "PATIENT_DEPARTMENT_CODE = '" + g_setup[i][13] + "' AND ";
	                	}else{
		                	guarantee_location = "PATIENT_DEPARTMENT_CODE != '" + g_setup[i][13] + "' AND ";
	                	}
	                }
	            }catch(Exception e){
	            	guarantee_location = "";
	            }
	            //NEW BY NOPP FOR MONTHLY GUARANTEE MORE THAN 1 LOCATION
            	guarantee_day = "GUARANTEE_DATE_TIME = '"+g_setup[i][7]+""+g_setup[i][8]+"'";

            	try{
            		if(g_setup[i][25].equals("N")){
            			dischargeCondition = " ";
            		}else{
    	            	if(Double.parseDouble(g_setup[i][15]) > 0){
    	            		dischargeCondition = " ";
    	            	}else{
    	            		dischargeCondition = ", " +
    		            		"YYYY = CASE WHEN YYYY = '' THEN '" + g_setup[i][6] + "' ELSE YYYY END, " +
    		    				"MM = CASE WHEN MM = '' THEN '" + g_setup[i][5] + "' ELSE MM END, " +
    		    				"PAY_BY_CASH = CASE WHEN YYYY = '' THEN 'Y' ELSE PAY_BY_CASH END, " +
    		    				"RECEIPT_NO = CASE WHEN RECEIPT_NO = '' THEN 'DISCHARGE' ELSE RECEIPT_NO END, " +
    		    				"RECEIPT_DATE = CASE WHEN RECEIPT_DATE = '' THEN INVOICE_DATE ELSE RECEIPT_DATE END ";
    	            	}            			
            		}
	            }catch(Exception e){
	            	System.out.println("Discharge Con error : "+e);
	            }
            	
            	try{
            		if(g_setup[i][26].equals("Y")){
            			onwardCondition = "";
            		}else{
            			onwardCondition = "IS_ONWARD <> 'Y' AND ";
            		}
            	}catch(Exception e){
            		onwardCondition = "IS_ONWARD <> 'Y' AND ";
            	}
            	
                try{
                    sql_statement = "UPDATE TRN_DAILY SET GUARANTEE_CODE = '"+ guarantee_code + "', ";
                    
                    if(g_setup[i][16].equals("")||g_setup[i][16].equals("AF")){
                    	//if guarantee from after allocate
                    	sql_statement += "GUARANTEE_AMT = CASE WHEN IS_GUARANTEE_FROM_ALLOC = 'N' AND COMPUTE_DAILY_USER_ID NOT LIKE '%Employee%' THEN AMOUNT_AFT_DISCOUNT ELSE DR_AMT END, ";
                    }else{
                    	//if guarantee from before allocate
                    	sql_statement += "GUARANTEE_AMT = CASE WHEN IS_GUARANTEE_FROM_ALLOC = 'Y' OR COMPUTE_DAILY_USER_ID LIKE '%Employee%' THEN DR_AMT ELSE AMOUNT_AFT_DISCOUNT END, ";
                    }
                    
                    sql_statement += "IS_PAID = '"+ is_paid + "', " +
                    "GUARANTEE_DR_CODE = '" + g_setup[i][1] + "', " +
                    "GUARANTEE_TYPE = '" + g_setup[i][3] + "', " +
                    "GUARANTEE_TERM_MM = '" + g_setup[i][5] +"', "+
                    "GUARANTEE_TERM_YYYY = '" + g_setup[i][6] +"', "+
                    guarantee_day+
                    dischargeCondition+" "+
                    "WHERE DOCTOR_CODE = '" + g_setup[i][2] + "' AND " + 
                    admission + guarantee_include_extra +
                    "COMPUTE_DAILY_DATE <> '' AND "+guarantee_location+
                    "(TRANSACTION_DATE BETWEEN '"+g_setup[i][6]+""+g_setup[i][5]+"01' AND '"+g_setup[i][6]+""+g_setup[i][5]+"31') AND "+
                    "VERIFY_DATE IS NOT NULL "+guarantee_day_con+
                    "HOSPITAL_CODE = '" + g_setup[i][0] + "' AND " +
                    "ACTIVE = '1' AND " +
                    "DR_AMT > 0 AND "+//Update 20100915(10:31)
                    "ORDER_ITEM_ACTIVE = '1' AND " +
                    onwardCondition+
                    "BATCH_NO = '' AND "+
                    "IS_GUARANTEE = 'Y'";

                    if(Variables.IS_TEST){
	                    System.out.println(sql_statement);	                    	
                    }
                    cdb.insert(sql_statement); //comment for skip write database process
                    cdb.commitDB(); //comment for skip write database process
                }catch(Exception e){
                	cdb.rollDB();
                	status = false;
                	TRN_Error.setUser_name(this.user_id);
                	TRN_Error.setHospital_code(hospital_code);
		            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
                	System.out.println("Some transaction don't process step 6");
                	System.out.println("Exception update guarantee step 6 : "+e);
                	System.out.println("Command : "+sql_statement+"\n");
                }
            }
            
        	System.out.println("Set Guarantee Transaction Running End Time "+JDate.getTime());
        } catch (Exception ex) {
        	System.out.println("Must to Rollback Guarantee Process!!");
            System.out.println("Exception Step 6 : "+ex);
            status = false;
        }
        
        System.out.println("FINISH GUARANTEE STEP 2 "+JDate.getTime());
        return status;
        
    }
    
    private boolean paidAbsorbByHospital(int index, String[][] g){
    	boolean status = true;
    	double hpPaidAmount = Double.parseDouble(g[index][7])+Double.parseDouble(g[index][8]);
    	String message = "Paid Absorb By Hospital";
    	g[index][3] = g[index][3].equals("U") ? "%" : g[index][3].toString();
        String sql_tmp =  "UPDATE STP_GUARANTEE SET " +
		"HP402_ABSORB_AMOUNT = '"+hpPaidAmount+"', "+//GUARANTEE_AMOUNT+FIX_AMOUNT
		"DF_ABSORB_AMOUNT = '"+Double.parseDouble(g[index][7])+"', "+//GUARANTEE_AMOUNT For Previous Guarantee Process
        "DF402_CASH_AMOUNT = '"+Double.parseDouble(g[index][11])+"' "+//GUARANTEE_EXTRA
        "WHERE HOSPITAL_CODE = '"+g[index][0]+"' "+
        "AND GUARANTEE_DR_CODE = '"+g[index][1]+"' "+
        "AND GUARANTEE_CODE = '"+g[index][2]+"' "+
        "AND IS_INCLUDE_LOCATION = '"+g[index][18]+"' "+
        "AND GUARANTEE_LOCATION LIKE '"+g[index][4].toString()+"' "+
        "AND ADMISSION_TYPE_CODE LIKE '"+ g[index][3] + "' "+
        "AND MM = '"+g[index][5]+"' "+
        "AND YYYY = '"+g[index][6]+"' "+
        "AND ACTIVE = '1' "+
        "AND GUARANTEE_TYPE_CODE = '"+g[index][9]+"'";
		try {
			cdb.insert(sql_tmp);
			cdb.commitDB();
		} catch (SQLException ex) {
        	TRN_Error.setUser_name(this.user_id);
        	TRN_Error.setHospital_code(hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, ex.toString(), sql_tmp,"");
			System.out.println("Error Update Setup Guarantee Hp Absorb");
			System.out.println("Cause Command Error : "+sql_tmp);
			System.out.println("");
			status = false;
		}
		return status;
    }
    private void inGuaranteeAllocate(int g_index, int t_index, String[][] g, String[][] t){
    	double amount = Double.parseDouble(t[t_index][22]);
    	double temp = 0.00;
    	
    	if(this.guarantee_allocate_condition.equals("Y")){
    		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
    			this.dr_amt = this.trn_guarantee_amt * (this.percent_in_allocate/100);
    		}else{
    			this.dr_amt = this.trn_guarantee_amt;
    		}
            
    		this.guarantee_paid = this.guarantee_paid + this.dr_amt;
    		this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance - this.trn_guarantee_amt;
    		this.guarantee_balance = this.guarantee_balance - (this.trn_guarantee_amt+temp) < 0 ? 0 : this.guarantee_balance - this.trn_guarantee_amt;
            this.hp_amt = amount - this.dr_amt < 0 ? 0 : amount - this.dr_amt;
            this.trn_guarantee_paid_amt = 0; //guarantee_paid_amt for Absorb some Guarantee
    	}else if(this.guarantee_allocate_condition.equals("A")){
    		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
    			this.dr_amt = this.trn_guarantee_amt * (this.percent_in_allocate/100);
    		}else{
    			this.dr_amt = this.trn_guarantee_amt;
    		}
    		
    		this.guarantee_paid = this.guarantee_paid + this.dr_amt;
    		this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance - this.dr_amt;
    		this.guarantee_balance = this.guarantee_balance - this.dr_amt < 0 ? 0 : this.guarantee_balance - this.dr_amt;
            this.hp_amt = amount - this.dr_amt < 0 ? 0 : amount - this.dr_amt;
            this.trn_guarantee_paid_amt = 0; //guarantee_paid_amt for Absorb some Guarantee
        }else{
        	if(this.sum_amount_aft_discount <= this.guarantee_amt){
        		this.dr_amt = amount;
        	}else if(this.sum_trn_guarantee_amt > this.guarantee_amt){
        		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
        			this.dr_amt = this.trn_guarantee_amt * (this.percent_in_allocate/100);
        		}else{
        			this.dr_amt = this.trn_guarantee_amt;
        		}
        		double balance_diff = 0;
        		double balance_temp = this.sum_trn_guarantee_balance - this.trn_guarantee_amt+(this.trn_guarantee_amt * (this.percent_in_allocate/100))+this.guarantee_paid;
        		if(balance_temp < this.guarantee_amt){
        			balance_diff = this.guarantee_amt - balance_temp;
        			this.dr_amt = this.trn_guarantee_amt * (this.percent_in_allocate/100)+balance_diff;
        		}
        	}else if(this.sum_trn_guarantee_amt == this.guarantee_amt){
        		this.dr_amt = this.trn_guarantee_amt;
        	}else if(this.sum_trn_guarantee_amt < this.guarantee_amt){
        		temp = amount - this.trn_guarantee_amt;
        		if(this.sum_trn_guarantee_amt+temp > this.guarantee_amt){
        			temp = this.guarantee_amt-this.sum_trn_guarantee_amt;
        		}
        		this.sum_trn_guarantee_amt = this.sum_trn_guarantee_amt+temp;
        		this.dr_amt = this.trn_guarantee_amt+temp;
        	}else{ /*not implement*/ }
        	
    		this.guarantee_paid = this.guarantee_paid + this.dr_amt;
    		this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance - this.trn_guarantee_amt;
    		this.guarantee_balance = this.guarantee_balance - this.dr_amt < 0 ? 0 : this.guarantee_balance - this.dr_amt;
            this.hp_amt = amount - this.dr_amt < 0 ? 0 : amount - this.dr_amt;
            this.trn_guarantee_paid_amt = 0; //guarantee_paid_amt for Absorb some Guarantee
        }
        if(t[t_index][21].toString().equals("Y")){
            this.tax_amt = this.dr_amt;
        }else{
        	this.tax_amt = amount;
        }
    }
    private void inAndOverGuaranteeAllocate(int g_index, int t_index, String[][] g, String[][] t){
    	double amount = Double.parseDouble(t[t_index][22]); //AMOUNT_AFT_DISCOUNT
    	double amountAfterDiscount = Double.parseDouble(t[t_index][22]); //AMOUNT_AFT_DISCOUNT
        double trn_in_guarantee_amount = 0;
        double over_guarantee_amount = 0;
        double temp_amount = 0;
        this.trn_guarantee_paid_amt = 0; //guarantee_paid_amt for Absorb some Guarantee

    	if(this.guarantee_allocate_condition.equals("Y")){
            if(this.guarantee_balance == 0){
        		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
        			this.dr_amt = this.trn_guarantee_amt * (this.percent_over_allocate/100);
        		}else{
        			this.dr_amt = this.trn_guarantee_amt;
        		}
            	if(!t[t_index][5].equals("")){
            		this.guarantee_note = "OVER GUARANTEE "+t[t_index][16]+" to "+this.percent_over_allocate;
            	}else{
            		this.guarantee_note = "";
            	}
            }
            if(this.guarantee_balance > 0 && this.guarantee_balance < this.trn_guarantee_amt){
            	trn_in_guarantee_amount = this.guarantee_balance * (percent_in_allocate /100);
        		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
                    over_guarantee_amount = (this.trn_guarantee_amt - this.guarantee_balance) * (percent_over_allocate/100);
        		}else{
                    over_guarantee_amount = (this.trn_guarantee_amt - this.guarantee_balance);
        		}

            	if(!t[t_index][5].equals("")){
            		//if Receipt transaction
            		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
                        this.dr_amt = trn_in_guarantee_amount+over_guarantee_amount;
            		}else{
            			this.dr_amt = this.trn_guarantee_amt;
            		}

            		this.guarantee_note = "IN/OVER GUARANTEE="+JNumber.getSaveMoney(trn_in_guarantee_amount)+"/"+JNumber.getSaveMoney(over_guarantee_amount);
            	}else{
            		//if Invoice transaction
                	if(this.guarantee_balance <= 0){
                		this.guarantee_note = "";
                	}else{
                		this.guarantee_note = "ABSORB SOME GUARANTEE";
                		this.dr_amt = over_guarantee_amount;
                		this.trn_guarantee_paid_amt = trn_in_guarantee_amount;
                	}
            	}
            }
        	this.guarantee_balance = 0;
    	}else if(this.guarantee_allocate_condition.equals("A")){
                if(this.guarantee_balance == 0){
            		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
            			this.dr_amt = this.trn_guarantee_amt * (this.percent_over_allocate/100);
            		}else{
            			this.dr_amt = this.trn_guarantee_amt;
            		}
                	if(!t[t_index][5].equals("")){
                		this.guarantee_note = "OVER GUARANTEE "+t[t_index][16]+" to "+this.percent_over_allocate;
                	}else{
                		this.guarantee_note = "";
                	}
                }
                if(this.guarantee_balance > 0 && this.guarantee_balance < (this.trn_guarantee_amt * (this.percent_over_allocate/100))){
                	trn_in_guarantee_amount = this.guarantee_balance * (percent_in_allocate /100);
            		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
                        over_guarantee_amount = (this.trn_guarantee_amt - this.guarantee_balance) * (percent_over_allocate/100);
            		}else{
                        over_guarantee_amount = (this.trn_guarantee_amt - this.guarantee_balance);
            		}

                	if(!t[t_index][5].equals("")){ //if Receipt transaction
                		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
                            this.dr_amt = trn_in_guarantee_amount+over_guarantee_amount;
                		}else{
                			this.dr_amt = this.trn_guarantee_amt;
                		}

                		this.guarantee_note = "IN/OVER GUARANTEE="+JNumber.getSaveMoney(trn_in_guarantee_amount)+"/"+JNumber.getSaveMoney(over_guarantee_amount);
                	}else{ //if Invoice transaction
                    	if(this.guarantee_balance <= 0){
                    		this.guarantee_note = "";
                    	}else{
                    		this.guarantee_note = "ABSORB SOME GUARANTEE";
                    		this.dr_amt = over_guarantee_amount;
                    		this.trn_guarantee_paid_amt = trn_in_guarantee_amount;
                    	}
                	}
                }else{//add block 20180220
                	this.dr_amt = this.trn_guarantee_amt * (percent_in_allocate /100);
            		this.guarantee_note = "IN GUARANTEE="+JNumber.getSaveMoney(trn_in_guarantee_amount);
                }
            	//this.guarantee_balance = 0; //comment 20180220
        		this.guarantee_balance = this.guarantee_balance - this.dr_amt < 0 ? 0 : this.guarantee_balance - this.dr_amt; //add 20180220
    	}else{
    		if(t[t_index][26].equals("N") || t[t_index][26].equals("")){
                trn_in_guarantee_amount = this.guarantee_balance * (percent_in_allocate /100);
	            over_guarantee_amount = (this.trn_guarantee_amt - this.guarantee_balance) * (percent_over_allocate/100);
	            this.dr_amt = trn_in_guarantee_amount+over_guarantee_amount;
    		}else{
                trn_in_guarantee_amount = this.guarantee_balance;
	            over_guarantee_amount = (this.trn_guarantee_amt - this.guarantee_balance);
	            this.dr_amt = trn_in_guarantee_amount+over_guarantee_amount;    			
    		}
            temp_amount = this.dr_amt;
        	if(!t[t_index][5].equals("")){
        	//if Receipt transaction
        		this.guarantee_note = "IN/OVER GUARANTEE="+JNumber.getSaveMoney(trn_in_guarantee_amount)+"/"+JNumber.getSaveMoney(over_guarantee_amount);
        	}else{
        	//if Invoice transaction
            	if(this.guarantee_paid+this.dr_amt <= this.guarantee_amt){
            		double balance_diff = 0;
            		double balance_temp = this.sum_trn_guarantee_balance - this.trn_guarantee_amt+(this.trn_guarantee_amt * (this.percent_in_allocate/100))+this.guarantee_paid;
            		//System.out.println(this.guarantee_paid+"<>"+this.dr_amt+"<>"+this.guarantee_balance+"<>"+this.trn_guarantee_amt);
            		if(balance_temp < this.guarantee_amt){
            			balance_diff = this.guarantee_amt - balance_temp;
            			this.dr_amt = this.trn_guarantee_amt * (this.percent_in_allocate/100)+balance_diff;
            			temp_amount = this.dr_amt;
            		}
            		this.guarantee_note = "ABSORB GUARANTEE IN/OVER";
            	}else{
            		this.guarantee_note = "ABSORB SOME GUARANTEE";
            		this.dr_amt = (this.dr_amt - this.guarantee_balance);
            		this.trn_guarantee_paid_amt = this.guarantee_balance;
            		temp_amount = this.guarantee_balance;
            		amount = amount - this.guarantee_balance;
            	}
        	}
            this.guarantee_balance = 0; //add 20180220
    	}
        if(t[t_index][21].toString().equals("Y")){//if tax from after allocate
            this.tax_amt = this.dr_amt;        		
    	}else{//else tax from before allocate
    		//this.tax_amt = amount;
        	this.tax_amt = this.guarantee_note.equals("ABSORB SOME GUARANTEE")? amountAfterDiscount - this.trn_guarantee_paid_amt : amountAfterDiscount;
        }
		this.guarantee_paid = this.guarantee_paid + temp_amount;
        //this.guarantee_balance = 0; //comment 20180220
        //this.tax_amt = this.guarantee_note.equals("ABSORB SOME GUARANTEE")? this.tax_amt - this.trn_guarantee_paid_amt : this.tax_amt;
        this.hp_amt = amount - this.dr_amt < 0 ? 0 : amount - this.dr_amt;
        this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance - this.trn_guarantee_amt;
    }
    private void overGuaranteeAllocate(int g_index, int t_index, String[][] g, String[][] t){
    	double amount = Double.parseDouble(t[t_index][22]); //AMOUNT_AFT_DISCOUNT
    	double amountAfterDiscount = Double.parseDouble(t[t_index][22]); //AMOUNT_AFT_DISCOUNT
        this.trn_guarantee_paid_amt = 0; //guarantee_paid_amt for Absorb some Guarantee
        this.dr_amt = this.trn_guarantee_amt * (this.percent_over_allocate/100);

        //Method ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â´ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â¡ (BNH) ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
		double balance_diff = 0;
		double balance_temp = this.sum_trn_guarantee_balance - this.trn_guarantee_amt+(this.trn_guarantee_amt * (this.percent_over_allocate/100))+this.guarantee_paid;
		if(balance_temp < this.guarantee_amt){
			balance_diff = this.guarantee_amt - balance_temp;
			this.dr_amt = this.trn_guarantee_amt * (this.percent_over_allocate/100)+balance_diff;
			this.guarantee_note = t[t_index][5].equals("") ? "ABSORB GUARANTEE OVER" : "OVER GUARANTEE";
		}else if(this.guarantee_paid+this.dr_amt > this.guarantee_amt && this.guarantee_paid < this.guarantee_amt){
			if(!t[t_index][5].equals("")){//if Receipt transaction
            		this.guarantee_note = "OVER GUARANTEE";
            }else{
	    		this.dr_amt = this.dr_amt - (this.guarantee_amt - this.guarantee_paid);
	    		this.trn_guarantee_paid_amt = this.guarantee_amt - this.guarantee_paid;
	    		this.guarantee_note = "ABSORB SOME GUARANTEE";
            }
    	}else if(this.guarantee_paid+this.dr_amt < this.guarantee_amt && this.guarantee_paid < this.guarantee_amt){
    		this.guarantee_note = t[t_index][5].equals("") ? "ABSORB GUARANTEE OVER" : "OVER GUARANTEE";
    	}else{
    		if(!t[t_index][5].equals("")){//if Receipt transaction
        		this.guarantee_note = "OVER GUARANTEE";
	        }else{
	    		this.guarantee_note = "";
	        }
    	}
        if(t[t_index][21].toString().equals("Y")){//if tax from after allocate
            this.tax_amt = this.dr_amt;        		
    	}else{//else tax from before allocate
    		//this.tax_amt = amount;
        	this.tax_amt = this.guarantee_note.equals("ABSORB SOME GUARANTEE")? amountAfterDiscount - this.trn_guarantee_paid_amt : amountAfterDiscount;
        }
		this.guarantee_paid = this.guarantee_note.equals("ABSORB SOME GUARANTEE")? this.guarantee_paid + this.trn_guarantee_paid_amt : this.guarantee_paid + this.dr_amt;
    	this.guarantee_balance = 0;
        this.hp_amt = amount - this.dr_amt < 0 ? 0 : amount - this.dr_amt;
        this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance - this.trn_guarantee_amt;
    }
    private boolean calculateGuarantee(){
        boolean status = true;
        String[][] guarantee_table = null;
        String[][] transaction_table = null;
        String admission_type = "";
        String department = "";
        String sql_temp = "";
        String message = "Guarantee Calculate";
        String onwardCondition = "";
        String order_by = "";
        double fix_amount = 0;
        int t = 0;
        String sql_new = "SELECT DISTINCT "+
        "SG.HOSPITAL_CODE, SG.GUARANTEE_DR_CODE, SG.GUARANTEE_CODE, SG.ADMISSION_TYPE_CODE, "+ //0-3
        "SG.GUARANTEE_LOCATION, SG.MM, SG.YYYY, SG.GUARANTEE_AMOUNT-SG.GUARANTEE_INCLUDE_AMOUNT, " +//4-7
        "SG.GUARANTEE_FIX_AMOUNT, "+ //8
        "SG.GUARANTEE_TYPE_CODE, SG.OVER_ALLOCATE_PCT, SG.GUARANTEE_EXCLUDE_AMOUNT, "+//9-11
        "SG.HP402_ABSORB_AMOUNT, SG.GUARANTEE_ALLOCATE_PCT, 'N', '0', HP.GUARANTEE_ALL_ALLOC, "+ //12-16
        "HP.GUARANTEE_DAY, SG.IS_INCLUDE_LOCATION, HP.IS_GUARANTEE_ONWARD "+ //17-19
        "FROM STP_GUARANTEE SG LEFT OUTER JOIN HOSPITAL HP ON SG.HOSPITAL_CODE = HP.CODE "+
        "WHERE SG.HOSPITAL_CODE = '"+hospital_code+"' AND SG.MM = '"+month+"' AND " +
        "SG.YYYY = '"+year+"' AND SG.GUARANTEE_TYPE_CODE <> 'STP' AND SG.ACTIVE = '1'";
        guarantee_table = cdb.query(sql_new);
        t = guarantee_table.length;
        
        for(int i = 0; i<guarantee_table.length; i++){
            String stemp = "";
            try{
        	System.out.println("Guarantee Calculate Running Doctor Code "+guarantee_table[i][1]+" by "+guarantee_table[i][2]+" No. "+i+" From "+t+" Start Time "+JDate.getTime()+" ");
        	if(guarantee_table[i][17].equals("VER")){
            	//Guarantee Day? Verify Date = "VER", Invoice Date = "INV"
            	order_by = "ORDER BY YYYY DESC, IS_ONWARD ASC, VERIFY_DATE, VERIFY_TIME, INVOICE_NO, LINE_NO";
            }else{
            	order_by = "ORDER BY YYYY DESC, IS_ONWARD ASC, TRANSACTION_DATE, VERIFY_TIME, INVOICE_NO, LINE_NO";
            }
        	
        	try{
        		if(guarantee_table[i][19].equals("Y")){
        			onwardCondition = "";
        		}else{
        			onwardCondition = "AND IS_ONWARD <> 'Y'";
        		}
        	}catch(Exception e){
        		onwardCondition = "";
        	}
        	
            admission_type = guarantee_table[i][3].equals("U") ? "%" : guarantee_table[i][3].toString();
            guarantee_table[i][4] = guarantee_table[i][4].trim();
            if(guarantee_table[i][18].equals("Y")){
                department = guarantee_table[i][4].equals("") || guarantee_table[i][4] == null ? "" : "AND PATIENT_DEPARTMENT_CODE = '"+guarantee_table[i][4].toString()+"' ";            	
            }else if(guarantee_table[i][18].equals("N")){
                department = guarantee_table[i][4].equals("") || guarantee_table[i][4] == null ? "" : "AND PATIENT_DEPARTMENT_CODE != '"+guarantee_table[i][4].toString()+"' ";            	
            }else{
            	//department = "AND PATIENT_DEPARTMENT_CODE != '' ";
            	// Modify for Department in transaction has no data
            	department = "";
            }
            String s = "SELECT INVOICE_NO, INVOICE_DATE, ORDER_ITEM_CODE, LINE_NO, " + //0-3
            "TRANSACTION_MODULE, YYYY, GUARANTEE_AMT, GUARANTEE_DR_CODE, GUARANTEE_CODE, " + //4-8
            "GUARANTEE_TERM_MM, GUARANTEE_TERM_YYYY, GUARANTEE_PAID_AMT, GUARANTEE_NOTE, " + //9-12
            "IS_PAID, DR_AMT, HP_AMT, NOR_ALLOCATE_PCT, AMOUNT_AFT_DISCOUNT, DR_TAX_406, " + //13-18
            "HP_TAX, CASE WHEN DR_TAX_406+DR_TAX_402 > DR_AMT THEN 'AMT' ELSE 'A' END, " + //19-20
            "TAX_FROM_ALLOCATE, AMOUNT_AFT_DISCOUNT, OLD_TAX_AMT, TRANSACTION_DATE, RECEIPT_DATE "+ //21-25
            ", IS_GUARANTEE_FROM_ALLOC, IS_PARTIAL "+ //26-27 Fix Not Allocate from Guarantee Process
            "FROM TRN_DAILY "+
            "WHERE GUARANTEE_DR_CODE = '"+guarantee_table[i][1]+"' "+
            "AND GUARANTEE_CODE = '"+guarantee_table[i][2]+"' "+
            "AND ADMISSION_TYPE_CODE LIKE '"+ admission_type + "' "+
            department+
            "AND GUARANTEE_TERM_MM = '"+guarantee_table[i][5]+"' "+
            "AND GUARANTEE_TERM_YYYY = '"+guarantee_table[i][6]+"' "+
            "AND GUARANTEE_TYPE = '"+guarantee_table[i][9]+"' "+
            "AND HOSPITAL_CODE = '"+guarantee_table[i][0]+"' "+
            "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' "+
            "AND BATCH_NO = '' "+
            "AND COMPUTE_DAILY_DATE <> '' AND IS_PAID = 'Y' "+
            onwardCondition+
            order_by;
            
            //this statement use index 'gua_select_01'//
            stemp = s;
            transaction_table = cdb.query(s);
            String absHos = "Y";
            
            try{
	            if(transaction_table.length < 1){
	            	absHos = "Y";
	            }else{
	            	absHos = "N";
	            }
            }catch(Exception e){}

            //if(transaction_table.length < 1){
            if(absHos.equals("Y")){
            	paidAbsorbByHospital(i,guarantee_table);
            }else{
            //else more than 1 transaction for guarantee 
            	this.guarantee_amt = Double.parseDouble(guarantee_table[i][7]);
            	this.guarantee_balance = Double.parseDouble(guarantee_table[i][7]);
            	this.guarantee_paid = 0.00;
            	this.sum_trn_guarantee_amt = 0.00;
            	this.sum_amount_aft_discount = 0.00;
            	this.sum_trn_guarantee_balance = 0.00;
                this.percent_in_allocate = Double.parseDouble(guarantee_table[i][13]);
                this.percent_over_allocate = Double.parseDouble(guarantee_table[i][10]);
                this.guarantee_allocate_condition = guarantee_table[i][16].toString();

            	for(int ix = 0; ix<transaction_table.length; ix++){
                	this.sum_trn_guarantee_amt = this.sum_trn_guarantee_amt+Double.parseDouble(transaction_table[ix][6]);
                	this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance+Double.parseDouble(transaction_table[ix][6]);
                	this.sum_amount_aft_discount = this.sum_amount_aft_discount+Double.parseDouble(transaction_table[ix][22]);
                }
            	this.guarantee_balance_paid = 0.00;
            	//System.out.println("Sum Trn Guarantee : "+this.sum_trn_guarantee_amt+" and Sum Amount 100% : "+this.sum_amount_aft_discount+" >"+guarantee_table[i][1]+">"+guarantee_table[i][2]);
            	for(int x = 0; x<transaction_table.length; x++){
                	message = "Invoice No/Line No = "+transaction_table[x][0]+" / "+transaction_table[x][3];
                	//System.out.println("Start Guarantee Process of "+message);
                    transaction_table[x][12] = "";//Clear guarantee note
                    this.trn_guarantee_amt = Double.parseDouble(transaction_table[x][6]);
                	
//==================GUARANTEE MONTHLY/DAILY
                    if(this.guarantee_amt>0){
                    	if(this.guarantee_allocate_condition.equals("Y") || this.guarantee_allocate_condition.equals("A")){
                        //=================== BGH METHOD ===================//
                    		if(this.guarantee_balance >= this.trn_guarantee_amt){//---in guarantee
                            	this.inGuaranteeAllocate(i, x, guarantee_table, transaction_table);
                    			//messageWrite(i,guarantee_table, x, transaction_table,"In Guarantee ");
                            	this.guarantee_note = transaction_table[x][5].equals("") ? 
                            		"ABSORB GUARANTEE" : 
                            		"IN GUARANTEE "+transaction_table[x][16]+" to "+guarantee_table[i][13];
                                transaction_table[x][16] = ""+this.percent_in_allocate; //normal_alloc_pct
                    		}else{//--------------------------------------------------in/over || over guarantee
                            	this.inAndOverGuaranteeAllocate(i, x, guarantee_table, transaction_table);
                    			//messageWrite(i,guarantee_table, x, transaction_table,"In/Over Guarantee ");
                                transaction_table[x][16] = ""+this.percent_over_allocate; //normal_alloc_pct
                    		}
                    		this.guarantee_balance_paid = this.guarantee_balance_paid+this.dr_amt;
                            guarantee_table[i][7] = ""+this.guarantee_balance;
                            double dut = this.guarantee_balance_paid > this.guarantee_amt ? 0.00 : this.guarantee_amt - this.guarantee_balance_paid ;
                            guarantee_table[i][7] = ""+dut; 
                    	}else{
                    	//=================== BNH METHOD ===================//
                    		if(this.guarantee_balance <= 0){
                    			//messageWrite(i,guarantee_table, x, transaction_table,"Over Guarantee ");
                            	this.overGuaranteeAllocate(i, x, guarantee_table, transaction_table);
                                guarantee_table[i][7] = ""+this.guarantee_balance;
                                transaction_table[x][16] = ""+this.percent_over_allocate; //normal_alloc_pct
                    		}else if(this.guarantee_balance > 0 && this.guarantee_balance < this.trn_guarantee_amt){
                    			//messageWrite(i,guarantee_table, x, transaction_table,"In/Over Guarantee ");
                            	this.inAndOverGuaranteeAllocate(i, x, guarantee_table, transaction_table);
                    			guarantee_table[i][7] = ""+this.guarantee_balance;
                                transaction_table[x][16] = ""+this.percent_in_allocate; //normal_alloc_pct
                    		}else if(this.guarantee_balance > 0 && this.guarantee_balance >= this.trn_guarantee_amt ){
                    			//messageWrite(i,guarantee_table, x, transaction_table,"In Guarantee ");
                            	this.inGuaranteeAllocate(i, x, guarantee_table, transaction_table);
                    			this.guarantee_note = transaction_table[x][5].equals("") ? 
                                		"ABSORB GUARANTEE" : 
                                		"IN GUARANTEE "+transaction_table[x][16]+" to "+guarantee_table[i][13];
                                guarantee_table[i][7] = ""+this.guarantee_balance;
                                transaction_table[x][16] = ""+this.percent_in_allocate; //normal_alloc_pct
                    		}else{System.out.println("in Guarantee not implement");/*no implementation by nop*/}
                    	}
                    	//System.out.println(" | dr amt | "+this.dr_amt+" | "+this.guarantee_note);
                        guarantee_table[i][7] = ""+this.guarantee_balance;
                        transaction_table[x][11] = ""+this.trn_guarantee_paid_amt; //guarantee_paid_amt
                        transaction_table[x][12] = this.guarantee_note;
                        transaction_table[x][14] = ""+this.dr_amt; //dr_amt
                        transaction_table[x][15] = ""+this.hp_amt; //hp_amt
                        transaction_table[x][16] = ""+this.percent_in_allocate; //normal_alloc_pct
                        transaction_table[x][18] = ""+this.tax_amt; //dr_tax_406
                        transaction_table[x][19] = "0";	//hp_tax
                    }
                    //end GUARANTEE MONTHLY/DAILY
                    
//==================GUARANTEE EXTRA (PART TIME)
                    if(Double.parseDouble(guarantee_table[i][11]) > 0 ){//GUARANTEE EXTRA
                    	transaction_table[x][12] = "GUARANTEE EXTRA "+transaction_table[x][16]+" ->"+guarantee_table[i][13];
                    	if(Double.parseDouble(guarantee_table[i][11]) >= Double.parseDouble(transaction_table[x][6])){ //IN GUARANTEE
                            transaction_table[x][12] = "GUARANTEE EXTRA "+transaction_table[x][16]+" ->"+guarantee_table[i][13];
                            
                            double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
                            double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]);
                            double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
                            double dr_amt = trn_guarantee_amount * (in_allocate_pct/100);
                            double hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
                            guarantee_amount = guarantee_amount - trn_guarantee_amount;
                            
                            transaction_table[x][14] = ""+dr_amt; //dr_amt
                            if(Double.parseDouble(transaction_table[x][18])<= 0.00 && dr_amt > 0){
                            	transaction_table[x][18] = transaction_table[x][6]; // dr_tax_406 from guarantee amount
                            }
                            guarantee_table[i][7] = ""+guarantee_amount;
                            transaction_table[x][15] = ""+hp_amt;
                            transaction_table[x][16] = ""+in_allocate_pct;
                            transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                        	transaction_table[x][19] = "0";						 	 // hp_tax

                        }else{ //OVER GUARANTEE
                            if(Integer.parseInt(guarantee_table[i][10])>0){
                                transaction_table[x][12] = "GUARANTEE EXTRA "+transaction_table[x][16]+" > "+guarantee_table[i][10];
                                
                                double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
                                double over_allocate_pct = Double.parseDouble(guarantee_table[i][10]);
                                double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]) ;
                                double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
                                double trn_in_guarantee_amount = 0;
                                double over_guarantee_amount = 0;
                                double dr_amt = Double.parseDouble(transaction_table[x][14]);
                                double hp_amt = 7;

                                if(guarantee_amount < 0.2){//if over guarantee (guarantee remain < 0.2)
                                    guarantee_amount = 0;
                                    dr_amt = trn_guarantee_amount * (over_allocate_pct/100);
                                    hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
                                }else{
                                    trn_in_guarantee_amount = guarantee_amount * (in_allocate_pct /100);
                                    over_guarantee_amount = (trn_guarantee_amount - guarantee_amount) * (over_allocate_pct/100);
                                    dr_amt = over_guarantee_amount + trn_in_guarantee_amount;
                                    hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
                                    transaction_table[x][12] = "GUARANTEE EXTRA "+(int)trn_in_guarantee_amount+"/"+(int)over_guarantee_amount;
                                }
                                if(Double.parseDouble(transaction_table[x][18])<= 0.00 && dr_amt > 0){
                                	transaction_table[x][18] = transaction_table[x][6]; // dr_tax_406 from guarantee amount
                                }

                            	transaction_table[x][19] = "0";	
                                transaction_table[x][15] = ""+JNumber.showDouble(hp_amt, 2);
                                transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                                transaction_table[x][14] = ""+JNumber.showDouble(dr_amt, 2); //DR_AMT
                                guarantee_table[i][7] = "0.1";
                            }
                        } //END ELSE OVER GUARANTEE EXTRA
                        transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                    }
                    
//==================GUARANTEE FIX RATE
                    if(Double.parseDouble(guarantee_table[i][8]) > 0){
                        transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                        transaction_table[x][14] = "0"; //DR_AMT
                        transaction_table[x][18] = "0"; //DR_TAX_406
                        transaction_table[x][12] = "FIX GUARANTEE";
                        transaction_table[x][13] = "N"; //NOT PAY 
                    }
                    
                    String ss = "";
                    if(transaction_table[x][12].startsWith("ABSORB GUARANTEE")){
                    	ss = this.getUpdateAdvanceDailyScript(transaction_table, x);
                    	stemp = ss;
                    }else{
                    	ss = "UPDATE TRN_DAILY SET GUARANTEE_PAID_AMT = '"+Double.parseDouble(JNumber.getSaveMoney(transaction_table[x][11]))+"', "+
                        "IS_PAID = '"+transaction_table[x][13]+"', "+
                        "DR_AMT = '"+Double.parseDouble(JNumber.getSaveMoney(transaction_table[x][14]))+"', "+
                
						"DR_TAX_400 = CASE WHEN TAX_TYPE_CODE = '400' THEN '"+Double.parseDouble(JNumber.getSaveMoney(transaction_table[x][18]))+"' ELSE '0.0' END , "+
						"DR_TAX_401 = CASE WHEN TAX_TYPE_CODE = '401' THEN '"+Double.parseDouble(JNumber.getSaveMoney(transaction_table[x][18]))+"' ELSE '0.0' END , "+
 						"DR_TAX_402 = CASE WHEN TAX_TYPE_CODE = '402' THEN '"+Double.parseDouble(JNumber.getSaveMoney(transaction_table[x][18]))+"' ELSE '0.0' END , "+
                        "DR_TAX_406 = CASE WHEN TAX_TYPE_CODE = '406' THEN '"+Double.parseDouble(JNumber.getSaveMoney(transaction_table[x][18]))+"' ELSE '0.0' END , "+

                        "HP_AMT = '"+Double.parseDouble(JNumber.getSaveMoney(transaction_table[x][15]))+"', "+
                        "HP_TAX = '"+Double.parseDouble(JNumber.getSaveMoney(transaction_table[x][19]))+"', "+
                        //"NOR_ALLOCATE_PCT = '"+transaction_table[x][16]+"', "+
                        "GUARANTEE_NOTE = '"+transaction_table[x][12]+"' " +
                        "WHERE INVOICE_NO = '"+transaction_table[x][0]+"' "+
                        "AND INVOICE_DATE = '"+transaction_table[x][1]+"' "+
                        "AND LINE_NO = '"+transaction_table[x][3]+"' "+
                        "AND TRANSACTION_DATE = '"+transaction_table[x][24]+"' "+
                        "AND RECEIPT_DATE = '"+transaction_table[x][25]+"' "+
                        "AND GUARANTEE_DR_CODE = '"+transaction_table[x][7]+"' "+
                        "AND GUARANTEE_CODE = '"+transaction_table[x][8]+"' "+
                        "AND GUARANTEE_TERM_MM = '" +transaction_table[x][9]+"' "+
                        "AND GUARANTEE_TERM_YYYY = '"+transaction_table[x][10]+"' "+
                        "AND HOSPITAL_CODE = '"+hospital_code+"' "+
                        "AND BATCH_NO = '' "+
                        "AND IS_PARTIAL = '"+transaction_table[x][27]+"' "+
                        "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' "+
                        onwardCondition;
                    	stemp = ss;
                    }
                    try {
                        cdb.insert(ss);
                        cdb.commitDB();
                    } catch (SQLException ex) {
                    	TRN_Error.setUser_name(this.user_id);
                    	TRN_Error.setHospital_code(hospital_code);
                        TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, ex.toString(), ss,"");
                        System.out.println("Guarantee Prepare Update Transaction : "+ex);
                        System.out.println("By Statement : "+ss);
                        status = false;
                    }
                }//END FOR OF GUARANTEE MONTHLY/DAILY AND GUARANTEE TURN
                if(Double.parseDouble(guarantee_table[i][7]) < 0.2){ guarantee_table[i][7] = "0"; }
                if(Double.parseDouble(guarantee_table[i][8]) > 1){
                	fix_amount = Double.parseDouble(guarantee_table[i][8]);
                }else{
                	fix_amount = 0;
                }
                try {
        	        /*
        	        Absorb Guarantee Daily
        	        DF_ABSORB_AMOUNT = ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚Â´ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ Absorb ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
        	    	HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
        	    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
        	    	DF406_HOLD_AMOUNT = ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
        	        */
                    if(guarantee_table[i][18].equals("Y")){
                        department = guarantee_table[i][4].equals("") || guarantee_table[i][4] == null ? "" : "AND GUARANTEE_LOCATION = '"+guarantee_table[i][4].toString()+"' ";            	
                    }else{
                        department = guarantee_table[i][4].equals("") || guarantee_table[i][4] == null ? "" : "AND GUARANTEE_LOCATION != '"+guarantee_table[i][4].toString()+"' ";            	
                    }

        			double temp = Double.parseDouble(guarantee_table[i][7])+fix_amount;
                	sql_temp = "UPDATE STP_GUARANTEE SET " +
                	"HP402_ABSORB_AMOUNT = '"+temp+"', "+
         		    "DF_ABSORB_AMOUNT = '"+Double.parseDouble(guarantee_table[i][7])+"', "+
                    "DF402_CASH_AMOUNT = '"+Double.parseDouble(guarantee_table[i][11])+"' "+
                    "WHERE HOSPITAL_CODE = '"+guarantee_table[i][0]+"' "+
                    "AND GUARANTEE_DR_CODE = '"+guarantee_table[i][1]+"' "+
                    "AND GUARANTEE_CODE = '"+guarantee_table[i][2]+"' "+
                    "AND IS_INCLUDE_LOCATION = '"+guarantee_table[i][18]+"' "+
                    "AND GUARANTEE_LOCATION LIKE '"+guarantee_table[i][4].toString()+"' "+
                    "AND ADMISSION_TYPE_CODE LIKE '"+ admission_type + "' "+
                    "AND MM = '"+guarantee_table[i][5]+"' "+
                    "AND YYYY = '"+guarantee_table[i][6]+"' "+
                    "AND ACTIVE = '1' "+
                    "AND GUARANTEE_TYPE_CODE = '"+guarantee_table[i][9]+"'";
                	stemp = sql_temp;
                    cdb.insert(sql_temp);
                    cdb.commitDB();
                } catch (SQLException ex) {
                	TRN_Error.setUser_name(this.user_id);
                	TRN_Error.setHospital_code(hospital_code);
                    TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, ex.toString(), sql_temp,"");
                    System.out.println("Guarantee Prepare Update Guarantee Table : "+ex);
                    System.out.println("Cause Error : "+message);
                    System.out.println("");
                    status = false;
                }
            }//END ELSE OF GUARANTEE MONTHLY/DAILY AND GUARANTEE TURN
        } catch (Exception xx){
        	System.out.println("Guarantee Process Error : "+xx);
        	System.out.println(stemp);
        }
        }
        System.out.println("\nGuarantee Calculate Finish Ending Time "+JDate.getTime());
        return status;
    }
    private String calculateExtraPreviousGuarantee(){ 
    	String sql = "UPDATE TRN_DAILY SET GUARANTEE_NOTE = 'OLD EXTRA' " 
    	+ "FROM TRN_DAILY T " 
    	+ "LEFT OUTER JOIN ( " 
    	+ "SELECT * FROM STP_GUARANTEE " 
    	+ "WHERE HOSPITAL_CODE='"+this.hospital_code+"' AND YYYY+MM='"+JDate.getPreviousBatch(this.month, this.year)+"' AND ACTIVE='1' " 
    	+ "AND GUARANTEE_EXCLUDE_AMOUNT >0 )G ON T.HOSPITAL_CODE = G.HOSPITAL_CODE AND T.DOCTOR_CODE = G.GUARANTEE_DR_CODE " 
    	+ "WHERE T.HOSPITAL_CODE ='"+this.hospital_code+"' AND ( T.TRANSACTION_DATE LIKE '"+this.year+this.month+"%' AND T.VERIFY_DATE < '"+this.year+this.month+"00') " 
    	+ "AND T.VERIFY_DATE+T.VERIFY_TIME BETWEEN G.START_DATE+G.START_TIME AND G.END_DATE+G.END_TIME "; 
    	return sql;
    }
    
    private boolean calculatePreviousGuarantee(){
        boolean status = true;
        boolean admis_status = true;
        String guarantee_status = "";
        String message = "Guarantee Previous";
        String[][] guarantee_table = null;
        String[][] transaction_table = null;
        String guarantee_info = "";
        String is_paid = "";
        int count_line = 0;
        String t = "UPDATE STP_GUARANTEE SET OLD_ABSORB_AMOUNT = DF_ABSORB_AMOUNT WHERE HOSPITAL_CODE = '"+this.hospital_code+"'";
        
        String sql_trn = "SELECT T.INVOICE_NO, T.INVOICE_DATE, T.LINE_NO, T.VERIFY_DATE, " + //0-3
        "T.VERIFY_TIME, T.DOCTOR_CODE, DR.GUARANTEE_DR_CODE, ISNULL(T.DR_AMT,0), ISNULL(T.AMOUNT_AFT_DISCOUNT,0), " +//4-8
        "'' AS GUARANTEE_SOURCE, T.ADMISSION_TYPE_CODE, 'VER' AS GUARANTEE_DAY, ISNULL(T.DR_TAX_406,0), " +//9-12
        "T.TRANSACTION_DATE, T.IS_PARTIAL "+ //13-14
        "FROM TRN_DAILY T "+
        "LEFT OUTER JOIN DOCTOR DR ON T.DOCTOR_CODE = DR.CODE AND T.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
        "WHERE T.TRANSACTION_DATE LIKE '"+this.year+this.month+"%' " +
        "AND T.VERIFY_DATE <= '"+this.year+this.month+"01' AND T.VERIFY_DATE != '' " +
        "AND T.VERIFY_TIME <> '' AND T.HOSPITAL_CODE = '"+this.hospital_code+"' " +
        "AND T.IS_GUARANTEE = 'Y' AND INVOICE_TYPE <> 'ORDER' AND GUARANTEE_NOTE != 'OLD EXTRA' "+
        "AND T.GUARANTEE_NOTE = '' AND IS_ONWARD <> 'Y' AND T.BATCH_NO = '' AND T.ACTIVE = '1' "+
        "ORDER BY YYYY DESC, VERIFY_DATE+VERIFY_TIME ASC";
        
        try {
        	System.out.println("Select Previous Guarantee : "+JDate.getTime());
        	cdb.insert(calculateExtraPreviousGuarantee());
        	cdb.insert(t);
			transaction_table = cdb.query(sql_trn);
        } catch (Exception ex) {
        	TRN_Error.setUser_name(this.user_id);
        	TRN_Error.setHospital_code(hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, ex.toString(), sql_trn,"");
            System.out.println("Select Previous Guarantee Error : "+ex);
            status = false;
        }
        
        count_line = transaction_table.length;

        //---------------------
    	System.out.println("Process Guarantee Previous Daily Running Start Time "+JDate.getTime());
        
    	//DAILY GUARANTEE
        for(int i = 0; i<transaction_table.length; i++){
        	String s1 = 
        	"SELECT S.YYYY, S.MM, S.GUARANTEE_DR_CODE, S.DF_ABSORB_AMOUNT, " +//0-3
        	"S.GUARANTEE_CODE, S.ADMISSION_TYPE_CODE, S.GUARANTEE_SOURCE " +//4-6
        	"FROM STP_GUARANTEE S " +
            "WHERE S.HOSPITAL_CODE = '"+this.hospital_code+"' AND "+
            "S.DF_ABSORB_AMOUNT > 0 AND "+
            "S.GUARANTEE_FIX_AMOUNT = 0 AND "+
            "S.GUARANTEE_TYPE_CODE = 'DLY' AND "+
            "LEN(S.GUARANTEE_CODE) > 6 AND "+
            "S.GUARANTEE_DR_CODE = '"+transaction_table[i][6]+"' AND " +
        	"S.ACTIVE = '1' AND " +
        	"S.GUARANTEE_DAY = 'VER' AND "+        	
        	"('" +transaction_table[i][3]+""+transaction_table[i][4]+"' BETWEEN "+
        	"START_DATE+CASE WHEN S.EARLY_TIME = '000000' THEN S.START_TIME ELSE S.EARLY_TIME END AND S.END_DATE+CASE WHEN "+
        	"LATE_TIME = '000000' THEN S.END_TIME ELSE S.LATE_TIME END)";
        	is_paid = "N";
        	guarantee_info = "ABSORB OLD GUARANTEE";
        	guarantee_table = cdb.query(s1);
        	
        	if(guarantee_table != null){
        		
        		if(guarantee_table.length>0){
        			
        			if(guarantee_table[0][5].equals("U")){
        				admis_status = true;
        			}else{
        				if(transaction_table[i][10].equals(guarantee_table[0][5])){
        					admis_status = true;
        				}else{
        					admis_status = false;
        				}
        			}
        			
        			if(admis_status){
	        			if(guarantee_table[0][6].equals("AF") || guarantee_table[0][6].equals("DF")){
		        			//GUARANTEE BY AFTER ALLOCATE AMOUNT
			        		if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][7])){
		            			guarantee_table[0][3] = ""+(Double.parseDouble(guarantee_table[0][3]) - Double.parseDouble(transaction_table[i][7]));
		            			transaction_table[i][7] = "0";
		            			transaction_table[i][12] = "0";
			        		}else if(Double.parseDouble(guarantee_table[0][3])>0){
		        				transaction_table[i][7] = ""+(Double.parseDouble(transaction_table[i][7])- Double.parseDouble(guarantee_table[0][3]));
		            			transaction_table[i][12] = ""+(Double.parseDouble(transaction_table[i][12])- Double.parseDouble(guarantee_table[0][3]));
		            			//Clear amount of DR_AMT if negative value
		            			if(Double.parseDouble(transaction_table[i][7])<=0){
		            				transaction_table[i][7] = "0";
		            				transaction_table[i][12] = "0";
		            				
		            			}
		            			//Clear amount of DR_TAX_406 if negative value
		            			if(Double.parseDouble(transaction_table[i][12])<=0){
		            				transaction_table[i][12] = "0";
		            			}
		        				guarantee_info = "ABSORB OLD SOME";
		            			is_paid = "Y";
		            			guarantee_table[0][3] = "0";
			        		}else{
			        			is_paid = "Y";
		        				guarantee_info = "";
			        		}
			        		
		        		}else{
		        			//GUARANTEE BY BEFORE ALLOCATE AMOUNT
		        			if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][8])){
		            			guarantee_table[0][3] = ""+(Double.parseDouble(guarantee_table[0][3]) - Double.parseDouble(transaction_table[i][8]));
		            			transaction_table[i][7] = "0";
		            			transaction_table[i][12] = "0";
			        		}else if(Double.parseDouble(guarantee_table[0][3])>0){
			        				transaction_table[i][7] = ""+(Double.parseDouble(transaction_table[i][7])- Double.parseDouble(guarantee_table[0][3]));
			            			transaction_table[i][12] = ""+(Double.parseDouble(transaction_table[i][12])- Double.parseDouble(guarantee_table[0][3]));
			            			//Clear amount of DR_AMT if negative value
			            			if(Double.parseDouble(transaction_table[i][7])<0){
			            				transaction_table[i][7] = "0";
			            			}
			            			//Clear amount of DR_TAX_406 if negative value
			            			if(Double.parseDouble(transaction_table[i][12])<0){
			            				transaction_table[i][12] = "0";
			            			}
			        				guarantee_info = "ABSORB OLD SOME";
			        				is_paid = "Y";
			            			guarantee_table[0][3] = "0";
			        		}else{
			        			is_paid = "Y";
		        				guarantee_info = "";		        			
			        		}
		        		}
	        			
		        		String updateGuarantee = "UPDATE STP_GUARANTEE SET DF_ABSORB_AMOUNT = "+
		        		Double.parseDouble(guarantee_table[0][3])+" WHERE " +
		            	"GUARANTEE_DR_CODE = '"+guarantee_table[0][2]+"' AND " +
		            	"HOSPITAL_CODE = '"+this.hospital_code+"' AND " +
		            	"ACTIVE = '1' AND GUARANTEE_CODE = '"+guarantee_table[0][4]+"'";
		        		
		        		String ss = "UPDATE TRN_DAILY SET "+
		                "DR_AMT = '"+Double.parseDouble(transaction_table[i][7])+"', "+
		                "DR_TAX_406 = '"+Double.parseDouble(transaction_table[i][12])+"', "+
		                "IS_PAID = '"+is_paid+"', "+
		                "GUARANTEE_NOTE = '"+guarantee_info+"' " +
		                "WHERE INVOICE_NO = '"+transaction_table[i][0]+"' "+
		                "AND HOSPITAL_CODE = '"+this.hospital_code+"' "+
		                "AND INVOICE_DATE = '"+transaction_table[i][1]+"' "+
		                "AND BATCH_NO = '' "+
		                "AND TRANSACTION_DATE = '"+transaction_table[i][13]+"' "+
		                "AND IS_PARTIAL = "+transaction_table[i][14]+"' "+
		                "AND LINE_NO = '"+transaction_table[i][2]+"'";
		                
		        		try {
		        			cdb.insert(updateGuarantee);
		                    cdb.insert(ss);
		                    cdb.commitDB();
		                } catch (SQLException ex) {
		                	cdb.rollDB();
		                	TRN_Error.setUser_name(this.user_id);
		                	TRN_Error.setHospital_code(hospital_code);
		                    TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, ex.toString(), sql_trn,"");
		                    System.out.println("Previous Guarantee Error : "+ex);
		        			System.out.println("On Statement : "+updateGuarantee);
		        			System.out.println("or Statement"+ss);
		                    status = false;
		                }
        			}
	        	}
        	}
        }
        
        System.out.println("Process Guarantee Previous Daily to Monthly Running Start Time "+JDate.getTime());
        // DAILY TO MONTHLY GUARANTEE
        // CEO FREEALIFE
        for(int i = 0; i<transaction_table.length; i++){
        	String s1 = 
        	"SELECT S.YYYY, S.MM, S.GUARANTEE_DR_CODE, S.DF_ABSORB_AMOUNT, " +//0-3
        	"S.GUARANTEE_CODE, S.ADMISSION_TYPE_CODE, S.GUARANTEE_SOURCE " +//4-6
        	"FROM STP_GUARANTEE S " +
            "WHERE S.HOSPITAL_CODE = '"+this.hospital_code+"' AND "+
            "S.DF_ABSORB_AMOUNT > 0 AND "+
            "S.GUARANTEE_FIX_AMOUNT = 0 AND "+
            "S.GUARANTEE_TYPE_CODE = 'MLD' AND "+
            "LEN(S.GUARANTEE_CODE) = 6 AND "+
            "S.GUARANTEE_DR_CODE = '"+transaction_table[i][6]+"' AND " +
        	"S.ACTIVE = '1' AND " +
        	"S.GUARANTEE_DAY = 'VER' AND "+
        	"S.GUARANTEE_DAY = 'VER' AND "+        	
        	"('" +transaction_table[i][3]+""+transaction_table[i][4]+"' BETWEEN "+
        	"START_DATE+CASE WHEN S.EARLY_TIME = '000000' THEN S.START_TIME ELSE S.EARLY_TIME END AND S.END_DATE+CASE WHEN "+
        	"LATE_TIME = '000000' THEN S.END_TIME ELSE S.LATE_TIME END)";

        	is_paid = "N";
        	guarantee_info = "ABSORB OLD GUARANTEE";
        	guarantee_table = cdb.query(s1);
        	if(guarantee_table != null){
        		if(guarantee_table.length>0){
        			if(guarantee_table[0][5].equals("U")){
        				admis_status = true;
        			}else{
        				if(transaction_table[i][10].equals(guarantee_table[0][5])){
        					admis_status = true;
        				}else{
        					admis_status = false;
        				}
        			}
        			if(admis_status){
	        			if(guarantee_table[0][6].equals("AF") || guarantee_table[0][6].equals("DF")){
		        			//GUARANTEE BY AFTER ALLOCATE AMOUNT
			        		if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][7])){
		            			guarantee_table[0][3] = ""+(Double.parseDouble(guarantee_table[0][3]) - Double.parseDouble(transaction_table[i][7]));
		            			transaction_table[i][7] = "0";
		            			transaction_table[i][12] = "0";
			        		}else if(Double.parseDouble(guarantee_table[0][3])>0){
		        				transaction_table[i][7] = ""+(Double.parseDouble(transaction_table[i][7])- Double.parseDouble(guarantee_table[0][3]));
		            			transaction_table[i][12] = ""+(Double.parseDouble(transaction_table[i][12])- Double.parseDouble(guarantee_table[0][3]));
		            			
		            			//Clear amount of DR_AMT if negative value
		            			if(Double.parseDouble(transaction_table[i][7])<=0){
		            				transaction_table[i][7] = "0";
		            				transaction_table[i][12] = "0";
		            			}
		            			//Clear amount of DR_TAX_406 if negative value
		            			if(Double.parseDouble(transaction_table[i][12])<=0){
		            				transaction_table[i][12] = "0";
		            			}
		        				guarantee_info = "ABSORB OLD SOME";
		            			is_paid = "Y";
		            			guarantee_table[0][3] = "0";
			        		}else{
			        			is_paid = "Y";
		        				guarantee_info = "";
			        		}
		        		}else{
		        			//GUARANTEE BY BEFORE ALLOCATE AMOUNT
		        			if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][8])){
		            			guarantee_table[0][3] = ""+(Double.parseDouble(guarantee_table[0][3]) - Double.parseDouble(transaction_table[i][8]));
		            			transaction_table[i][7] = "0";
		            			transaction_table[i][12] = "0";
			        		}else if(Double.parseDouble(guarantee_table[0][3])>0){
			        				transaction_table[i][7] = ""+(Double.parseDouble(transaction_table[i][7])- Double.parseDouble(guarantee_table[0][3]));
			            			transaction_table[i][12] = ""+(Double.parseDouble(transaction_table[i][12])- Double.parseDouble(guarantee_table[0][3]));
			            			//Clear amount of DR_AMT if negative value
			            			if(Double.parseDouble(transaction_table[i][7])<0){
			            				transaction_table[i][7] = "0";
			            			}
			            			//Clear amount of DR_TAX_406 if negative value
			            			if(Double.parseDouble(transaction_table[i][12])<0){
			            				transaction_table[i][12] = "0";
			            			}
			        				guarantee_info = "ABSORB OLD SOME";
			        				is_paid = "Y";
			            			guarantee_table[0][3] = "0";
			        		}else{
			        			is_paid = "Y";
		        				guarantee_info = "";		        			
			        		}
		        		}
	        			
		        		String updateGuarantee = "UPDATE STP_GUARANTEE SET DF_ABSORB_AMOUNT = "+
		        		Double.parseDouble(guarantee_table[0][3])+" WHERE " +
		            	"GUARANTEE_DR_CODE = '"+guarantee_table[0][2]+"' AND " +
		            	"HOSPITAL_CODE = '"+this.hospital_code+"' AND " +
		            	"ACTIVE = '1' AND GUARANTEE_CODE = '"+guarantee_table[0][4]+"'";
		        		
		        		String ss = "UPDATE TRN_DAILY SET "+
		                "DR_AMT = '"+Double.parseDouble(transaction_table[i][7])+"', "+
		                "DR_TAX_406 = '"+Double.parseDouble(transaction_table[i][12])+"', "+
		                "IS_PAID = '"+is_paid+"', "+
		                "GUARANTEE_NOTE = '"+guarantee_info+"' " +
		                "WHERE INVOICE_NO = '"+transaction_table[i][0]+"' "+
		                "AND HOSPITAL_CODE = '"+this.hospital_code+"' "+
		                "AND INVOICE_DATE = '"+transaction_table[i][1]+"' "+
		                "AND BATCH_NO = '' "+
		                "AND TRANSACTION_DATE = '"+transaction_table[i][13]+"' "+
		                "AND LINE_NO = '"+transaction_table[i][2]+"'";
		                
		        		try {
		        			cdb.insert(updateGuarantee);
		                    cdb.insert(ss);
		                    cdb.commitDB();
		                } catch (SQLException ex) {
		                	cdb.rollDB();
		                	TRN_Error.setUser_name(this.user_id);
		                	TRN_Error.setHospital_code(hospital_code);
		                    TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, ex.toString(), sql_trn,"");
		                    System.out.println("Previous Guarantee Error : "+ex);
		        			System.out.println("On Statement : "+updateGuarantee);
		        			System.out.println("or Statement"+ss);
		                    status = false;
		                }
        			}
	        	}
        	}
        }

        
        System.out.println("Process Guarantee Previous Monthly Running Start Time "+JDate.getTime());
    	
        // MONTHLY GUARANTEE
        for(int i = 0; i<transaction_table.length; i++){
        	String s1 = 
        	"SELECT S.YYYY, S.MM, S.GUARANTEE_DR_CODE, S.DF_ABSORB_AMOUNT, " +//0-3
        	"S.GUARANTEE_CODE, S.ADMISSION_TYPE_CODE, S.GUARANTEE_SOURCE " +//4-6
        	"FROM STP_GUARANTEE S " +
            "WHERE S.HOSPITAL_CODE = '"+this.hospital_code+"' AND "+
            "S.DF_ABSORB_AMOUNT > 0 AND "+
            "S.GUARANTEE_FIX_AMOUNT = 0 AND "+
            "S.GUARANTEE_TYPE_CODE IN ( 'MLY' , 'MMY' ) AND "+
            "LEN(S.GUARANTEE_CODE) = 6 AND "+
            "S.GUARANTEE_DR_CODE = '"+transaction_table[i][6]+"' AND " +
        	"S.ACTIVE = '1' AND " +
        	"S.GUARANTEE_DAY = 'VER' AND "+
        	"(('"+transaction_table[i][3]+"' BETWEEN S.START_DATE AND S.END_DATE) AND ("+
        	"'"+transaction_table[i][4]+"' BETWEEN CASE WHEN S.EARLY_TIME = '000000' THEN S.START_TIME ELSE S.EARLY_TIME END " +
        	"AND CASE WHEN S.LATE_TIME = '000000' THEN S.END_TIME ELSE S.LATE_TIME END))";

        	is_paid = "N";
        	guarantee_info = "ABSORB OLD GUARANTEE";
        	guarantee_table = cdb.query(s1);
        	if(guarantee_table != null){
        		if(guarantee_table.length>0){
        			if(guarantee_table[0][5].equals("U")){
        				admis_status = true;
        			}else{
        				if(transaction_table[i][10].equals(guarantee_table[0][5])){
        					admis_status = true;
        				}else{
        					admis_status = false;
        				}
        			}
        			if(admis_status){
	        			if(guarantee_table[0][6].equals("AF") || guarantee_table[0][6].equals("DF")){
		        			//GUARANTEE BY AFTER ALLOCATE AMOUNT
			        		if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][7])){
		            			guarantee_table[0][3] = ""+(Double.parseDouble(guarantee_table[0][3]) - Double.parseDouble(transaction_table[i][7]));
		            			transaction_table[i][7] = "0";
		            			transaction_table[i][12] = "0";
			        		}else if(Double.parseDouble(guarantee_table[0][3])>0){
		        				transaction_table[i][7] = ""+(Double.parseDouble(transaction_table[i][7])- Double.parseDouble(guarantee_table[0][3]));
		            			transaction_table[i][12] = ""+(Double.parseDouble(transaction_table[i][12])- Double.parseDouble(guarantee_table[0][3]));
		            			
		            			//Clear amount of DR_AMT if negative value
		            			if(Double.parseDouble(transaction_table[i][7])<=0){
		            				transaction_table[i][7] = "0";
		            				transaction_table[i][12] = "0";
		            			}
		            			//Clear amount of DR_TAX_406 if negative value
		            			if(Double.parseDouble(transaction_table[i][12])<=0){
		            				transaction_table[i][12] = "0";
		            			}
		        				guarantee_info = "ABSORB OLD SOME";
		            			is_paid = "Y";
		            			guarantee_table[0][3] = "0";
			        		}else{
			        			is_paid = "Y";
		        				guarantee_info = "";
			        		}
		        		}else{
		        			//GUARANTEE BY BEFORE ALLOCATE AMOUNT
		        			if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][8])){
		            			guarantee_table[0][3] = ""+(Double.parseDouble(guarantee_table[0][3]) - Double.parseDouble(transaction_table[i][8]));
		            			transaction_table[i][7] = "0";
		            			transaction_table[i][12] = "0";
			        		}else if(Double.parseDouble(guarantee_table[0][3])>0){
			        				transaction_table[i][7] = ""+(Double.parseDouble(transaction_table[i][7])- Double.parseDouble(guarantee_table[0][3]));
			            			transaction_table[i][12] = ""+(Double.parseDouble(transaction_table[i][12])- Double.parseDouble(guarantee_table[0][3]));
			            			//Clear amount of DR_AMT if negative value
			            			if(Double.parseDouble(transaction_table[i][7])<0){
			            				transaction_table[i][7] = "0";
			            			}
			            			//Clear amount of DR_TAX_406 if negative value
			            			if(Double.parseDouble(transaction_table[i][12])<0){
			            				transaction_table[i][12] = "0";
			            			}
			        				guarantee_info = "ABSORB OLD SOME";
			        				is_paid = "Y";
			            			guarantee_table[0][3] = "0";
			        		}else{
			        			is_paid = "Y";
		        				guarantee_info = "";		        			
			        		}
		        		}
	        			
		        		String updateGuarantee = "UPDATE STP_GUARANTEE SET DF_ABSORB_AMOUNT = "+
		        		Double.parseDouble(guarantee_table[0][3])+" WHERE " +
		            	"GUARANTEE_DR_CODE = '"+guarantee_table[0][2]+"' AND " +
		            	"HOSPITAL_CODE = '"+this.hospital_code+"' AND " +
		            	"ACTIVE = '1' AND GUARANTEE_CODE = '"+guarantee_table[0][4]+"'";
		        		
		        		String ss = "UPDATE TRN_DAILY SET "+
		                "DR_AMT = '"+Double.parseDouble(transaction_table[i][7])+"', "+
		                "DR_TAX_406 = '"+Double.parseDouble(transaction_table[i][12])+"', "+
		                "IS_PAID = '"+is_paid+"', "+
		                "GUARANTEE_NOTE = '"+guarantee_info+"' " +
		                "WHERE INVOICE_NO = '"+transaction_table[i][0]+"' "+
		                "AND HOSPITAL_CODE = '"+this.hospital_code+"' "+
		                "AND INVOICE_DATE = '"+transaction_table[i][1]+"' "+
		                "AND BATCH_NO = '' "+
		                "AND TRANSACTION_DATE = '"+transaction_table[i][13]+"' "+
		                "AND LINE_NO = '"+transaction_table[i][2]+"'";
		                
		        		try {
		        			cdb.insert(updateGuarantee);
		                    cdb.insert(ss);
		                    cdb.commitDB();
		                } catch (SQLException ex) {
		                	cdb.rollDB();
		                	TRN_Error.setUser_name(this.user_id);
		                	TRN_Error.setHospital_code(hospital_code);
		                    TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, ex.toString(), sql_trn,"");
		                    System.out.println("Previous Guarantee Error : "+ex);
		        			System.out.println("On Statement : "+updateGuarantee);
		        			System.out.println("or Statement"+ss);
		                    status = false;
		                }
        			}
	        	}
        	}
        }
        System.out.println("Finish Process Previous Absorb Guarantee");
        return status;
    }
    
    /**
     * @author CEO-FreeAlife
     * @return true
     */
    private boolean calculateMonthlyToYear(){
    	// check action process
    	boolean action  = false;
    	
    	// BACK UP ABSORB AMOUNT GUARANTEE , ABSORB REMENT AMOUNT 
    	String UpdateBeforAbsorbGuarantee  =  "UPDATE STP_GUARANTEE SET "+ 
    										  "ABSORB_REMAIN_AMOUNT =  DF_ABSORB_AMOUNT, "+ 
    										  "ABSORB_AMOUNT  =  HP402_ABSORB_AMOUNT "+ 
    										  "WHERE HOSPITAL_CODE = '"+ this.hospital_code+ "' "+
    										  "AND GUARANTEE_TYPE_CODE = 'MMY' "+
    										  "AND YYYY = '" + this.year + "' "+
    										  "AND ACTIVE = '1'";
 
    	try {
    		System.out.println("Set Absorb Remain : " + UpdateBeforAbsorbGuarantee);    		
			cdb.insert(UpdateBeforAbsorbGuarantee);
			action  = true;			
		} catch (Exception ex) {
			action  = false;
    		TRN_Error.setUser_name(this.user_id);
        	TRN_Error.setHospital_code(hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  "", ex.toString(), "","");
            System.out.println("Insert Absorb Monthly to year Error : "+ex);
			System.out.println("On Statement : " + UpdateBeforAbsorbGuarantee);
		}
    	
    	// TRN_DAILY record over guarantee
    	String[][] drOverGuarantee = null;
    	
    	// fine over guarantee amount 
    	String sqlCommand  = " SELECT DISTINCT TD.GUARANTEE_DR_CODE, SUM(DR_AMT) AS SUM_DR, GUARANTEE_AMOUNT, " + 
    						 " CASE WHEN SUM(DR_AMT)-GUARANTEE_AMOUNT <= 0 THEN 0 ELSE SUM(DR_AMT)-GUARANTEE_AMOUNT END AS OVER_DR_AMT "+
    						 //" SUM(DR_AMT) AS OVER_DR_AMT " + 
    						 " FROM TRN_DAILY TD " + 
    						 " INNER JOIN STP_GUARANTEE SG ON TD.GUARANTEE_DR_CODE  = SG.GUARANTEE_DR_CODE " + 
    						 " AND TD.HOSPITAL_CODE = SG.HOSPITAL_CODE" +
    						 " AND SG.YYYY+SG.MM = TD.GUARANTEE_TERM_YYYY+TD.GUARANTEE_TERM_MM " + 
    						 " AND TD.GUARANTEE_TYPE = SG.GUARANTEE_TYPE_CODE AND SG.ACTIVE = 1 " + 
    						 " WHERE TD.HOSPITAL_CODE = '"+this.hospital_code+"' "  + 
    						 " AND TD.GUARANTEE_TYPE  = 'MMY' " + 
    						 " AND TD.GUARANTEE_TERM_YYYY+TD.GUARANTEE_TERM_MM = '" + this.year + this.month + "' " + 
    						 " AND TD.ACTIVE = 1 " + 
    						 " AND TD.YYYY+TD.MM = '" + this.year + this.month + "' " +
    						 //" AND TD.GUARANTEE_NOTE LIKE 'OVER%' "+
    						 " AND TD.GUARANTEE_NOTE != '' "+
    						 " GROUP BY TD.GUARANTEE_DR_CODE, GUARANTEE_AMOUNT "+
    						 " HAVING SUM(TD.DR_AMT)> 0 "+
    						 " ORDER BY TD.GUARANTEE_DR_CODE ";
    	try {
    		System.out.println("SQL DEFINE OVER GUARANTEE AMOUNT : " + sqlCommand);
    		drOverGuarantee = cdb.query(sqlCommand);

    		if(drOverGuarantee.length > 0){        		
        		for(int i = 0; i < drOverGuarantee.length ; i++){
        			// get sum absorb remain for doctor over guarantee in month
        			String ql = 
        				"SELECT SUM(ABSORB_AMOUNT-DEDUCT_ABSORB_AMOUNT) FROM STP_GUARANTEE " +
        				"WHERE HOSPITAL_CODE = '"+this.hospital_code+"' " +
        				"AND GUARANTEE_DR_CODE = '"+drOverGuarantee[i][0]+"' " +
        				"AND YYYY = '"+this.year+"' AND ACTIVE = '1' "+
        				"AND GUARANTEE_TYPE_CODE = 'MMY' "+
        				"HAVING SUM(ABSORB_AMOUNT-DEDUCT_ABSORB_AMOUNT) > 0 ";
        			double sumAbsorbRemain = 0;
        			double deductAbsorbAmount = 0;
        			try{
        				sumAbsorbRemain = Double.parseDouble( cdb.getSingleData(ql) );
        			}catch(Exception e){
        				System.out.println("Cast Deduct Absorb MMY Error : "+e);
            			System.out.println(ql);
        				sumAbsorbRemain = 0;
        			}
        			if(Double.parseDouble(drOverGuarantee[i][3]) > sumAbsorbRemain){
        				deductAbsorbAmount = sumAbsorbRemain;
        			}else if(Double.parseDouble(drOverGuarantee[i][3]) < sumAbsorbRemain){
        				deductAbsorbAmount = Double.parseDouble(drOverGuarantee[i][3]);
        			}else{
        				//not implement
        			}
        			
        			// set over guarantee of monthly
        			String sqlUpdateOverGuarantee = " UPDATE STP_GUARANTEE SET "+ 
        											" OVER_GUARANTEE_AMOUNT = '" + Double.parseDouble(drOverGuarantee[i][3]) +"', "+ // AMOUNT OVER
        											" DEDUCT_ABSORB_AMOUNT = '"+deductAbsorbAmount+"' "+
        											" WHERE HOSPITAL_CODE  = '"+ this.hospital_code+ "' " +
        											" AND YYYY+MM = '"+ this.year+this.month + "'" + 
        											" AND GUARANTEE_DR_CODE = '"+ drOverGuarantee[i][0]+"' " + 
        											" AND GUARANTEE_TYPE_CODE =  'MMY' " +
        											//" AND ( OVER_GUARANTEE_AMOUNT = 0 OR OVER_GUARANTEE_AMOUNT IS NULL )" + 
        											" AND ACTIVE = 1 " ;        			
        			try {
						cdb.insert(sqlUpdateOverGuarantee);
						cdb.commitDB();
						//System.out.println("UPDATE : OVER_GUARANTEE_AMOUNT : " + sqlUpdateOverGuarantee);
						action  = true;
					} catch (Exception ex) {
						action  = false;
			    		TRN_Error.setUser_name(this.user_id);
			        	TRN_Error.setHospital_code(hospital_code);
			            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  "", ex.toString(), "","");
			            System.out.println("Update OVER_GUARANTEE_AMOUNT Error : "+ex);
						System.out.println("On Statement : " + sqlUpdateOverGuarantee);
					}
        		}
        	}
        	
			// Insert expense month to year 
			String SQLExpense = "INSERT INTO TRN_EXPENSE_DETAIL " +
					" SELECT DISTINCT SG.YYYY , SG.MM  , 'ProcessGuarantee'  , SG.GUARANTEE_DR_CODE , SG.HOSPITAL_CODE ,  (START_DATE+START_TIME) " + 
					" , EP.CODE  , EP.SIGN , EP.ACCOUNT_CODE , SG.DEDUCT_ABSORB_AMOUNT ,  SG.DEDUCT_ABSORB_AMOUNT , ''  " +
					" , ''  , '' , '' , '' , SG.YYYY+SG.MM , SG.START_DATE  , 'Absorb Guarantee : Monthly To Year ' + (SG.START_DATE + '' + SG.START_TIME) " + 
					" , EP.TAX_TYPE_CODE , '' , DR.DEPARTMENT_CODE, '', '', '', '2', SG.GUARANTEE_DR_CODE " +
					" FROM STP_GUARANTEE SG " +
					" LEFT OUTER JOIN DOCTOR DR ON SG.GUARANTEE_DR_CODE  =  DR.GUARANTEE_DR_CODE AND  SG.HOSPITAL_CODE  =  DR.HOSPITAL_CODE " +
					" LEFT OUTER JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE  = DR.DEPARTMENT_CODE AND DR.HOSPITAL_CODE = DP.HOSPITAL_CODE " + 
					" LEFT OUTER JOIN EXPENSE EP ON DR.HOSPITAL_CODE = EP.HOSPITAL_CODE " + 
					" WHERE SG.HOSPITAL_CODE = '" + this.hospital_code + "' AND EP.ADJUST_TYPE = 'MY' " + //   -- ABS MONTHLY TO YEAR " + 
					" AND SG.YYYY+SG.MM = '" + (this.year + this.month) + "' " +
					" AND SG.GUARANTEE_TYPE_CODE  = 'MMY' " + 
					" AND SG.DEDUCT_ABSORB_AMOUNT > 0 " +
					" AND SG.ACTIVE = 1 " + 
					" AND DR.ACTIVE = 1 " +
					" ORDER BY SG.GUARANTEE_DR_CODE ";
			try {
				cdb.insert(SQLExpense);
				cdb.commitDB();
				System.out.println("UPDATE : INSERT EXPENSE DEDUCT MONTHLY TO YEAR : " + SQLExpense);
				action  = true;
			} catch (Exception ex) {
				action  = false;
	    		TRN_Error.setUser_name(this.user_id);
	        	TRN_Error.setHospital_code(hospital_code);
	            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  "", ex.toString(), "","");
	            System.out.println("Insert Expense Deduct Monthly to year Error : "+ex);
				System.out.println("On Statement : " + SQLExpense);
			}
		    
    	}catch (Exception ex){ 
    		action  = false;
    		TRN_Error.setUser_name(this.user_id);
        	TRN_Error.setHospital_code(hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  "", ex.toString(), "","");
            System.out.println("Amount over guarantee  Error : "+ex);
			System.out.println("On Statement : " + sqlCommand);
		}
    	
    	return action;
    }    
    private boolean calculateGuaranteeStep(){
        boolean status = true;
        String[][] guarantee_table = null;
        String[][] transaction_table = null;
        String admission_type = "";
        int t = 0;
        String sql_new = "SELECT DISTINCT "+
        "HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, "+ //0-3
        "GUARANTEE_LOCATION, MM, YYYY, GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT, "+ //4-7
        "GUARANTEE_FIX_AMOUNT, GUARANTEE_TYPE_CODE, OVER_ALLOCATE_PCT, GUARANTEE_EXCLUDE_AMOUNT, " +//8-11
        "HP402_ABSORB_AMOUNT, GUARANTEE_ALLOCATE_PCT "+ //12-13
        "FROM STP_GUARANTEE "+
        "WHERE HOSPITAL_CODE = '"+hospital_code+"' AND MM = '"+month+"' AND " +
        "YYYY = '"+year+"' AND GUARANTEE_TYPE_CODE = 'STP' AND ACTIVE = '1'";
        guarantee_table = cdb.query(sql_new);
        t = guarantee_table.length;
        if(guarantee_table.length > 0){
	        for(int i = 0; i<guarantee_table.length; i++){
	        	System.out.println("Step Guarantee Process Step 4 Running to "+i+" Of "+t+" ON TIME "+JDate.getTime());
	            admission_type = guarantee_table[i][3].equals("U") ? "%" : guarantee_table[i][3].toString();
	            String s = "SELECT INVOICE_NO, INVOICE_DATE, ORDER_ITEM_CODE, LINE_NO, " + //0-3
	            "TRANSACTION_MODULE, YYYY, GUARANTEE_AMT, GUARANTEE_DR_CODE, " +           //4-7
	            "GUARANTEE_CODE, GUARANTEE_TERM_MM, GUARANTEE_TERM_YYYY, GUARANTEE_PAID_AMT, " +//8-11
	            "GUARANTEE_NOTE ,IS_PAID, DR_AMT, HP_AMT, NOR_ALLOCATE_PCT, AMOUNT_AFT_DISCOUNT, "+ //12-17
	            "DR_TAX_406, HP_TAX, MM, TAX_FROM_ALLOCATE "+ //18-20
	            "FROM TRN_DAILY "+ 
	            "WHERE HOSPITAL_CODE = '"+guarantee_table[i][0]+"' "+
	            "AND GUARANTEE_DR_CODE = '"+guarantee_table[i][1]+"' "+
	            "AND GUARANTEE_CODE = '"+guarantee_table[i][2]+"' "+
	            "AND ADMISSION_TYPE_CODE LIKE '"+ admission_type + "' "+
	            //"AND GUARANTEE_TYPE = '"+guarantee_table[i][9]+"' "+
	            "AND INVOICE_DATE LIKE '"+year+month+"%' "+
	            "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' AND IS_PAID = 'Y' AND BATCH_NO = '' "+
	            "ORDER BY YYYY DESC, IS_ONWARD ASC, VERIFY_DATE, INVOICE_NO, LINE_NO";
	            transaction_table = cdb.query(s);	
	            if(transaction_table.length < 1){ //HP ABSORB
	            }else{
	                for(int x = 0; x<transaction_table.length; x++){
	                	
	                    transaction_table[x][12] = "";
	                    
	                    //If monthly/daily guarantee or guarantee turn
	                    if(Double.parseDouble(guarantee_table[i][7])>0 && Double.parseDouble(guarantee_table[i][8]) == 0){
	                    	//In Guarantee
	                        if(Double.parseDouble(guarantee_table[i][7]) > Double.parseDouble(transaction_table[x][6])){ //IN GUARANTEE
	                            transaction_table[x][12] = "STEP IN";
	                            double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
	                            double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]);
	                            double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
	                            double dr_amt = trn_guarantee_amount * (in_allocate_pct/100);
	                            double hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
	                            guarantee_amount = guarantee_amount - trn_guarantee_amount;
	                            transaction_table[x][14] = ""+dr_amt;
	                            guarantee_table[i][7] = ""+guarantee_amount;
	                            transaction_table[x][15] = ""+hp_amt;
	                            transaction_table[x][16] = ""+in_allocate_pct;
	                            transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                                if(transaction_table[x][20].toString().equals("Y")){
                                	//if tax from after allocate
                                	transaction_table[x][18] = transaction_table[x][14];
                                	transaction_table[x][19] = "0";
                                }else{
                                	//else tax from before allocate
                                	transaction_table[x][18] = transaction_table[x][17];
                                	transaction_table[x][19] = "0";
                                }
	                        }else{
	                        	//Over Guarantee
                                System.out.println("STEP OVER : "+guarantee_table[i][10]);
	                            if(Integer.parseInt(guarantee_table[i][10])>0){
	                                transaction_table[x][12] = "STEP OVER";
	                                double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
	                                double over_allocate_pct = Double.parseDouble(guarantee_table[i][10]);
	                                double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]) ;
	                                double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
	                                double trn_in_guarantee_amount = 0;
	                                double over_guarantee_amount = 0;
	                                double dr_amt = 0;
	                                double hp_amt = 0;
	                                System.out.println("STEP OVER : "+guarantee_amount);

	                                if(guarantee_amount > 0){
	                                    trn_in_guarantee_amount = guarantee_amount * (in_allocate_pct /100);
	                                    over_guarantee_amount = (trn_guarantee_amount - guarantee_amount) * (over_allocate_pct/100);
	                                    dr_amt = over_guarantee_amount + trn_in_guarantee_amount;
	                                    hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
	                                    transaction_table[x][12] = "STEP IN/OVER";
	                                    guarantee_amount = 0;
	                                }else{
	                                    dr_amt = trn_guarantee_amount * (over_allocate_pct/100);
	                                    hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
	                                    guarantee_amount = 0;
	                                }
	                                transaction_table[x][15] = ""+hp_amt;
	                                transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
	                                transaction_table[x][14] = ""+dr_amt; //DR_AMT
	                                guarantee_table[i][7] = ""+guarantee_amount;
	                                if(transaction_table[x][20].toString().equals("Y")){
	                                	//if tax from after allocate
	                                	transaction_table[x][18] = transaction_table[x][14];
	                                }else{
	                                	//else tax from before allocate
	                                	transaction_table[x][18] = transaction_table[x][17];
	                                }
	                            }
	                        }
	                      //} //End transaction to pay in term
	                    }else{
	                    	if(Integer.parseInt(guarantee_table[i][10])>0){
                                transaction_table[x][12] = "STEP OVER";
                                double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
                                double over_allocate_pct = Double.parseDouble(guarantee_table[i][10]);
                                double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]) ;
                                double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
                                double trn_in_guarantee_amount = 0;
                                double over_guarantee_amount = 0;
                                double dr_amt = 0;
                                double hp_amt = 0;
                                System.out.println("STEP OVER : "+guarantee_amount);

                                if(guarantee_amount > 0){
                                    trn_in_guarantee_amount = guarantee_amount * (in_allocate_pct /100);
                                    over_guarantee_amount = (trn_guarantee_amount - guarantee_amount) * (over_allocate_pct/100);
                                    dr_amt = over_guarantee_amount + trn_in_guarantee_amount;
                                    hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
                                    transaction_table[x][12] = "STEP IN/OVER";
                                    guarantee_amount = 0;
                                }else{
                                    dr_amt = trn_guarantee_amount * (over_allocate_pct/100);
                                    hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
                                    guarantee_amount = 0;
                                }
                                transaction_table[x][15] = ""+hp_amt;
                                transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                                transaction_table[x][14] = ""+dr_amt; //DR_AMT
                                guarantee_table[i][7] = ""+guarantee_amount;
                                if(transaction_table[x][20].toString().equals("Y")){
                                	//if tax from after allocate
                                	transaction_table[x][18] = transaction_table[x][14];
                                }else{
                                	//else tax from before allocate
                                	transaction_table[x][18] = transaction_table[x][17];
                                }
                            }
	                    }
	                    //new code 12/01/2010
	                    String ss = "";
	                    if(transaction_table[x][12].equals("ABSORB GUARANTEE")){
	                    	//ss = this.getUpdateAdvanceDailyScript(transaction_table, x);
	                    }else{
	                    	ss = "UPDATE TRN_DAILY SET " +
	                    	"GUARANTEE_PAID_AMT = '"+Double.parseDouble(transaction_table[x][11])+"', "+
	                        "IS_PAID = '"+transaction_table[x][13]+"', "+
	                        "DR_AMT = '"+Double.parseDouble(transaction_table[x][14])+"', "+
	                        "DR_TAX_406 = '"+Double.parseDouble(transaction_table[x][18])+"', "+
	                        "HP_AMT = '"+Double.parseDouble(transaction_table[x][15])+"', "+
	                        "HP_TAX = '"+Double.parseDouble(transaction_table[x][19])+"', "+
	                        "COMPUTE_DAILY_USER_ID = '"+transaction_table[x][12]+"' " +
	                        "WHERE INVOICE_NO = '"+transaction_table[x][0]+"' "+
	                        "AND HOSPITAL_CODE = '"+hospital_code+"' "+
	                        "AND INVOICE_DATE = '"+transaction_table[x][1]+"' "+
	                        "AND ORDER_ITEM_CODE = '"+transaction_table[x][2]+"' "+
	                        "AND BATCH_NO = '' "+
	                        "AND LINE_NO = '"+transaction_table[x][3]+"' "+
	                        "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' "+
	                        "AND GUARANTEE_DR_CODE = '"+transaction_table[x][7]+"' "+
	                        "AND GUARANTEE_CODE = '"+transaction_table[x][8]+"' "+
	                        //"AND GUARANTEE_TERM_MM = '" +transaction_table[x][9]+"' "+
	                        "AND GUARANTEE_TERM_YYYY = '"+transaction_table[x][10]+"'";
	                    }
	                    try {
	                        cdb.insert(ss);
	                        cdb.commitDB();
	                        //System.out.println(ss);
	                    } catch (SQLException ex) {
	                        System.out.println("Guarantee Step Error : "+ex);
	                        status = false;
	                    }
	                }//END FOR OF GUARANTEE MONTHLY/DAILY AND GUARANTEE TURN
	            }//END ELSE OF GUARANTEE MONTHLY/DAILY AND GUARANTEE TURN
	        }        
        }else{
        	System.out.println("Guarantee Step not exist.");
        }
        System.out.println("FINISH STEP CALCULATE");
        return status;
    }
    private boolean sumAmountGuarantee(){
        /*
        DF_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
    	DF406_HOLD_AMOUNT = ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
        */
        String[][] tmp = null;
        String message = "";
        boolean status = true;
        String stm = "";
            //Select from Transaction and Sum Amount of Guarantee Separate by Tax Type, Cash, Credit and Hold
            stm = "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406') THEN GUARANTEE_PAID_AMT ELSE '0' END) AS DF_406_ABSORB, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402') THEN GUARANTEE_PAID_AMT ELSE '0' END) AS DF_402_ABSORB, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400') THEN GUARANTEE_PAID_AMT ELSE '0' END) AS DF_400_ABSORB "+
                  "FROM TRN_DAILY "+
                  "WHERE GUARANTEE_TERM_YYYY = '"+this.year+"' AND GUARANTEE_TERM_MM = '"+this.month+"' AND " +
                  "GUARANTEE_NOTE IN ('ABSORB SOME GUARANTEE') AND "+
                  "HOSPITAL_CODE = '"+this.hospital_code+"' AND ACTIVE = '1' AND BATCH_NO = '' "+
                  "GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE";
            
        tmp = cdb.query(stm);
        try {
            for(int i = 0; i<tmp.length; i++){
            	message = "Sum Absorb, Absorb Some from Transaction for Doctor ="+tmp[i][1]+
            	"' by Period : "+tmp[i][2];

            	stm = "UPDATE STP_GUARANTEE SET "+
                      "DF406_HOLD_AMOUNT = '"+tmp[i][4]+"', "+
                      "DF402_HOLD_AMOUNT = '"+tmp[i][5]+"', "+
                      "DF400_HOLD_AMOUNT = '"+tmp[i][6]+"' "+
                      "WHERE HOSPITAL_CODE = '"+tmp[i][0]+"' AND "+
                      "GUARANTEE_DR_CODE = '"+tmp[i][1]+"' AND "+
                      "GUARANTEE_CODE = '"+tmp[i][2]+"' AND "+
                      "GUARANTEE_TYPE_CODE = '"+tmp[i][3]+"'";
                cdb.insert(stm);
            }
            cdb.commitDB();
        } catch (Exception ex) {
        	TRN_Error.setUser_name(this.user_id);
        	TRN_Error.setHospital_code(hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, ex.toString(), stm,"");
            status = false;
            cdb.rollDB();
        }
        return status;
    }
    private boolean sumTaxGuarantee(){
    	/*
        DF_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
        */
        String[][] tmp = null;
        String temp = "";
        boolean status = true;

        String stm = "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE_CODE "+
                  "FROM STP_GUARANTEE "+
                  "WHERE YYYY = '"+this.year+"' AND MM = '"+this.month+"' AND " +
                  "HOSPITAL_CODE = '"+this.hospital_code+"' AND ACTIVE = '1' "+
                  "GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE_CODE";
        tmp = cdb.query(stm);

        try{
            for(int i = 0; i<tmp.length; i++){
                temp = "UPDATE STP_GUARANTEE SET "+
                    "SUM_TAX_406 = DF406_HOLD_AMOUNT, "+//absorb  transaction
                    "SUM_TAX_402 = DF402_HOLD_AMOUNT+HP402_ABSORB_AMOUNT+GUARANTEE_EXCLUDE_AMOUNT, "+
                    //absorb  transaction + absorb by hospital + parttime
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
        try {
        	System.out.println(getSumScript());
        	cdb.insert(getSumScript());
            cdb.commitDB();
        } catch (SQLException ex) {
            cdb.rollDB();
            status = false;
            System.out.println("Update Summary Month Guarantee : "+ex);
            System.out.println("On Script : "+getSumScript());
        }
        return status;
    }
    private String getSumScript(){
        /*
        Absorb Guarantee Daily
        DF_ABSORB_AMOUNT = ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚Â´ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ Absorb ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
    	DF406_HOLD_AMOUNT = ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
        */
    	String t = 
        	"INSERT INTO SUMMARY_GUARANTEE " +
            "(HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM, "+
            "GUARANTEE_AMOUNT, GUARANTEE_FIX_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, "+
            "SUM_HP_OVER_AMOUNT, "+
            "SUM_TAX_406, SUM_TAX_402, SUM_TAX_400) "+
            //--select--//
            "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, YYYY, MM, "+
            "SUM(GUARANTEE_AMOUNT), SUM(GUARANTEE_FIX_AMOUNT), SUM(GUARANTEE_EXCLUDE_AMOUNT), "+
        	"SUM(SUM_HP_OVER_AMOUNT), "+
        	//"'0','0','0' "+
        	"SUM(SUM_TAX_406), SUM(SUM_TAX_402),'0' "+
        	"FROM VW_PROCESS_GUARANTEE "+ 
        	"WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND YYYY = '"+this.year+"' " +
        	"AND MM = '"+this.month+"' AND SUM_TAX_406+SUM_TAX_402+SUM_TAX_400 > 0 "+
        	"GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, YYYY, MM ";
    	return t;
    }
    private String getUpdateAdvanceDailyScript(String[][] trn_daily, int data_index){
    	String update_script = "UPDATE TRN_DAILY SET " +
    	"GUARANTEE_PAID_AMT = '"+Double.parseDouble(trn_daily[data_index][11])+"', "+
        "IS_PAID = '"+trn_daily[data_index][13]+"', "+
        "DR_AMT = '"+Double.parseDouble(trn_daily[data_index][14])+"', "+
        "DR_TAX_406 = '"+Double.parseDouble(trn_daily[data_index][18])+"', "+
        "HP_AMT = '"+Double.parseDouble(trn_daily[data_index][15])+"', "+
        "HP_TAX = '"+Double.parseDouble(trn_daily[data_index][19])+"', "+
        "YYYY = '"+year+"', "+
        "MM = '"+month+"', "+
        "BATCH_NO = '', "+
        "PAY_BY_CASH = 'Y', "+
        "RECEIPT_NO = 'ADVANCE', "+
        "RECEIPT_DATE = INVOICE_DATE, "+
        "GUARANTEE_NOTE = '"+trn_daily[data_index][12]+"' " +
        "WHERE INVOICE_NO = '"+trn_daily[data_index][0]+"' "+
        "AND INVOICE_DATE = '"+trn_daily[data_index][1]+"' "+
        "AND TRANSACTION_DATE = '"+trn_daily[data_index][24]+"' "+
        "AND LINE_NO = '"+trn_daily[data_index][3]+"' "+
        "AND YYYY = '' "+
        //"AND BATCH_NO = '' "+
        "AND GUARANTEE_DR_CODE = '"+trn_daily[data_index][7]+"' "+
        "AND GUARANTEE_CODE = '"+trn_daily[data_index][8]+"' "+
        "AND GUARANTEE_TERM_MM = '" +trn_daily[data_index][9]+"' "+
        "AND GUARANTEE_TERM_YYYY = '"+trn_daily[data_index][10]+"' "+
        "AND HOSPITAL_CODE = '"+hospital_code+"' "+
        //comment order item 20100706 nop
        //"AND ORDER_ITEM_CODE = '"+trn_daily[data_index][2]+"' "+
        "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1'";
    	return update_script;
    }
    private boolean insertAbsorbSomeGuarantee(){
    	boolean status = true;
    	DBConn d = new DBConn();
    	DBConn de = new DBConn();
    	HashMap<String,String> hm = new HashMap<String,String>();
        ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
        ArrayList<HashMap<String,String>> up = new ArrayList<HashMap<String,String>>();
        /*
        HP_PREMIUM = backup for AMOUNT_AFT_DISCOUNT rollback
        */
    	try { d.setStatement(); } catch (SQLException e) {}
		al = d.getMultiData("SELECT * FROM TRN_DAILY WHERE "+
		"HOSPITAL_CODE = '"+this.hospital_code+"' AND GUARANTEE_TERM_YYYY = '"+this.year+"' AND " +
		"GUARANTEE_TERM_MM = '"+this.month+"' AND GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' AND " +
		"ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' AND INVOICE_TYPE <> 'ORDER' AND BATCH_NO = '' AND "+
        "COMPUTE_DAILY_DATE is not null AND COMPUTE_DAILY_DATE != '' AND "+
        "IS_PAID = 'Y' AND YYYY+MM = ''");
		if(al.size()>0){
			for(int i = 0; i < al.size(); i++){
			//New Payment Transaction for Absorb Some
				double percentage = (Double.parseDouble(al.get(i).get("GUARANTEE_PAID_AMT"))*100) /Double.parseDouble(al.get(i).get("GUARANTEE_AMT"));
				//System.out.println(al.get(i).get("LINE_NO")+"ADV"+"<>"+percentage+"<>"+al.get(i).get("AMOUNT_AFT_DISCOUNT"));
				al.get(i).put("LINE_NO", al.get(i).get("LINE_NO")+"ADV");
				al.get(i).put("RECEIPT_NO", al.get(i).get("INVOICE_NO"));
				al.get(i).put("RECEIPT_DATE", al.get(i).get("INVOICE_DATE"));
				al.get(i).put("TRANSACTION_MODULE", "AR");
				al.get(i).put("BATCH_NO", "  "); //assign value because system insert null value into row
				al.get(i).put("YYYY", this.year);
				al.get(i).put("MM", this.month);
				al.get(i).put("PAY_BY_CASH_AR", "Y");
				al.get(i).put("AMOUNT_AFT_DISCOUNT", ""+(Double.parseDouble(al.get(i).get("AMOUNT_AFT_DISCOUNT"))*percentage)/100);
				al.get(i).put("DR_AMT", al.get(i).get("GUARANTEE_PAID_AMT"));
				al.get(i).put("DR_TAX_406", ""+(Double.parseDouble(al.get(i).get("OLD_TAX_AMT"))*percentage)/100);
			}
			System.out.println(d.addData(al, "TRN_DAILY"));
			d.closeDB("Close Db Select Absorb Some Guarantee");
		}else{
			System.out.println("Advance Some : "+al.size());
		}
    	try {
    		//Old Credit Transaction for Absorb Some
    		de.setStatement(); 
    		String s = "UPDATE TRN_DAILY SET BATCH_NO = '', AMOUNT_AFT_DISCOUNT=T.AMOUNT_AFT_DISCOUNT-Q.AMOUNT_AFT_DISCOUNT, "+
 				   "DR_TAX_406 = T.OLD_TAX_AMT-Q.DR_TAX_406 "+
 				   "FROM TRN_DAILY AS T INNER JOIN "+
 				   "(SELECT HOSPITAL_CODE, INVOICE_NO, GUARANTEE_TERM_YYYY, GUARANTEE_TERM_MM, LINE_NO, AMOUNT_AFT_DISCOUNT, DR_TAX_406 "+
 				   "FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND GUARANTEE_TERM_YYYY = '"+this.year+"' AND "+
 				   "GUARANTEE_TERM_MM = '"+this.month+"' AND GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' AND "+
 				   "ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' AND INVOICE_TYPE <> 'ORDER' AND ( BATCH_NO = '' OR BATCH_NO IS NULL) AND "+
 				   "COMPUTE_DAILY_DATE is not null AND COMPUTE_DAILY_DATE != '' AND "+
 				   "IS_PAID = 'Y' AND YYYY+MM != '') AS Q "+
 				   "ON T.HOSPITAL_CODE = Q.HOSPITAL_CODE AND T.INVOICE_NO = Q.INVOICE_NO AND T.LINE_NO+'ADV' = Q.LINE_NO "+
 				   "WHERE T.HOSPITAL_CODE = '"+this.hospital_code+"' AND T.GUARANTEE_TERM_YYYY = '"+this.year+"' AND "+
 				   "T.GUARANTEE_TERM_MM = '"+this.month+"' AND GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' AND "+
 				   "ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' AND INVOICE_TYPE <> 'ORDER' AND ( BATCH_NO = '' OR BATCH_NO IS NULL) AND "+
 				   "COMPUTE_DAILY_DATE is not null AND COMPUTE_DAILY_DATE != '' AND "+
 				   "IS_PAID = 'Y' AND YYYY+MM = ''";
    		de.insert(s);
    		de.commitDB();
    	} catch (SQLException e) {
    		System.out.println("Update Absorb some Guarantee Error : "+e);
    	}
		de.closeDB("Close DB Update Absorb some Guarantee");
    	return status;
    }
    private boolean insertExpenseGuaranteeHP(){
    	boolean status = true;
        /*
        Absorb Guarantee Daily
        DF_ABSORB_AMOUNT = ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚Â´ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ Absorb ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
    	DF406_HOLD_AMOUNT = ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
        */

    	//Wait for Edit
    	//Get data from stp_guarantee
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
		"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
		"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
		"YYYY, MM, NOTE, EMPLOYEE_ID, " +
		"DEPARTMENT_CODE, LOCATION_CODE) " +
		
		"SELECT DISTINCT SM.HOSPITAL_CODE, SM.GUARANTEE_DR_CODE, SM.YYYY+SM.MM, " +
		"SM.GUARANTEE_CODE+SM.GUARANTEE_LOCATION+SM.IS_INCLUDE_LOCATION, SM.YYYY+SM.MM+'01', SM.HP402_ABSORB_AMOUNT, SM.HP402_ABSORB_AMOUNT, EX.SIGN, "+
		"EX.ACCOUNT_CODE, EX.CODE, CASE WHEN SM.TAX_TYPE_CODE = '' THEN EX.TAX_TYPE_CODE ELSE SM.TAX_TYPE_CODE END, SM.YYYY, SM.MM, " +
		"'Absorb Guarantee : Month/Year: '+SM.MM+'/'+SM.YYYY, 'ProcessGuarantee', "+
    	"CASE WHEN SM.GL_DEPARTMENT_CODE = '' THEN DR.DEPARTMENT_CODE ELSE SM.GL_DEPARTMENT_CODE END AS DEPARTMENT_CODE, " +
    	"DP.DEFAULT_LOCATION_CODE "+
    	"FROM STP_GUARANTEE SM "+ 
    	"LEFT OUTER JOIN DOCTOR DR ON SM.GUARANTEE_DR_CODE = DR.CODE AND SM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE = DP.CODE AND SM.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND EX.ADJUST_TYPE = 'HP' AND " +
    	"SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"' AND " +
    	"SM.ACTIVE = '1' AND SM.HP402_ABSORB_AMOUNT > 0 AND SM.GUARANTEE_TYPE_CODE LIKE 'M%' "+
    	"UNION "+
		"SELECT SM.HOSPITAL_CODE, SM.GUARANTEE_DR_CODE, SM.YYYY+SM.MM, " +
		"SM.GUARANTEE_CODE, SM.START_DATE, SM.HP402_ABSORB_AMOUNT, SM.HP402_ABSORB_AMOUNT, EX.SIGN, "+
		"EX.ACCOUNT_CODE, EX.CODE, CASE WHEN SM.TAX_TYPE_CODE = '' THEN EX.TAX_TYPE_CODE ELSE SM.TAX_TYPE_CODE END, SM.YYYY, SM.MM, " +
		"'Absorb Guarantee : Start Date/Start Time: '+SM.START_DATE+'/'+SM.START_TIME, 'ProcessGuarantee', "+
    	"CASE WHEN SM.GL_DEPARTMENT_CODE = '' THEN DR.DEPARTMENT_CODE ELSE SM.GL_DEPARTMENT_CODE END AS DEPARTMENT_CODE, "+
    	"DP.DEFAULT_LOCATION_CODE "+
    	"FROM STP_GUARANTEE SM "+ 
    	"LEFT OUTER JOIN DOCTOR DR ON SM.GUARANTEE_DR_CODE = DR.CODE AND SM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE = DP.CODE AND SM.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND EX.ADJUST_TYPE = 'HP' AND " +
    	"SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"' AND " +
    	"SM.ACTIVE = '1' AND SM.HP402_ABSORB_AMOUNT > 0 AND SM.GUARANTEE_TYPE_CODE NOT LIKE 'M%'";
    	try{
    		cdb.insert(s);
    		cdb.commitDB();
    		System.out.println("Do insertExpenseGuaranteeHP : "+s);
    	}catch (Exception e){
    		status = false;
    		System.out.println("Error on insertExpenseGuaranteeHP"+e);
    		System.out.println("By Query : "+s);
    		cdb.rollDB();
    	}
    	return status;
    }
    private boolean insertExpenseGuaranteeEX(){
    	boolean status = true;
        /*
        Absorb Guarantee Daily
        DF_ABSORB_AMOUNT = ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚Â´ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ Absorb ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
    	DF406_HOLD_AMOUNT = ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
        */

    	//Wait for Edit
    	//Get data from stp_guarantee
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
		"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
		"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
		"YYYY, MM, NOTE, EMPLOYEE_ID, " +
		"DEPARTMENT_CODE, LOCATION_CODE) " +
		
		"SELECT SM.HOSPITAL_CODE, SM.GUARANTEE_DR_CODE, SM.YYYY+SM.MM, " +
		"SM.GUARANTEE_CODE, SM.START_DATE, SM.DF402_CASH_AMOUNT, SM.DF402_CASH_AMOUNT, EX.SIGN, "+
		"EX.ACCOUNT_CODE, EX.CODE, CASE WHEN SM.TAX_TYPE_CODE = '' THEN EX.TAX_TYPE_CODE ELSE SM.TAX_TYPE_CODE END, " +
		"SM.YYYY, SM.MM, 'Special Consult Start Date/Start Time: '+SM.START_DATE+'/'+SM.START_TIME, 'ProcessGuarantee', "+
    	"CASE WHEN SM.GL_DEPARTMENT_CODE = '' THEN DR.DEPARTMENT_CODE ELSE SM.GL_DEPARTMENT_CODE END AS DEPARTMENT_CODE, " +
    	"DP.DEFAULT_LOCATION_CODE "+
    	"FROM STP_GUARANTEE SM "+ 
    	"LEFT OUTER JOIN DOCTOR DR ON SM.GUARANTEE_DR_CODE = DR.CODE AND SM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE = DP.CODE AND SM.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND EX.ADJUST_TYPE = 'EX' AND " +
    	"SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"' AND " +
    	/*comment date 2011-02-10
    	 *cause cannot insert duplicate start date from guarantee DLY
    	 *(between extra & guarantee daily)
    	"SM.ACTIVE = '1' AND SM.DF402_CASH_AMOUNT > 0";
    	*/
    	"SM.ACTIVE = '1' AND SM.GUARANTEE_EXCLUDE_AMOUNT > 1";
    	try{
    		cdb.insert(s);
    		cdb.commitDB();
    		System.out.println("Do insertExpenseGuaranteeEX : "+s);
    	}catch (Exception e){
    		status = false;
    		System.out.println("Error on insertExpenseGuaranteeEX"+e);
    		System.out.println("By Query : "+s);
    		cdb.rollDB();
    	}
    	return status;
    }
    private boolean insertExpenseGuaranteeFix(){
    	boolean status = true;
        /*
        Absorb Guarantee Daily
        DF_ABSORB_AMOUNT = ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚Â´ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ Absorb ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
    	DF406_HOLD_AMOUNT = ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
        */

    	//Wait for Edit
    	//Get data from stp_guarantee
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
		"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
		"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
		"YYYY, MM, NOTE, EMPLOYEE_ID, " +
		"DEPARTMENT_CODE, LOCATION_CODE) " +
		
		"SELECT DISTINCT SM.HOSPITAL_CODE, SM.GUARANTEE_DR_CODE, SM.YYYY+SM.MM, " +
		"SM.GUARANTEE_CODE, SM.YYYY+SM.MM+'01', SM.GUARANTEE_FIX_AMOUNT, SM.GUARANTEE_FIX_AMOUNT, EX.SIGN, "+
		"EX.ACCOUNT_CODE, EX.CODE, EX.TAX_TYPE_CODE, "+
		"SM.YYYY, SM.MM, 'Guarantee Fix : Month/Year: '+SM.MM+'/'+SM.YYYY, 'ProcessGuarantee', "+
    	"CASE WHEN SM.GL_DEPARTMENT_CODE = '' THEN DR.DEPARTMENT_CODE ELSE SM.GL_DEPARTMENT_CODE END AS DEPARTMENT_CODE, " +
    	"DP.DEFAULT_LOCATION_CODE "+
    	"FROM STP_GUARANTEE SM "+ 
    	"LEFT OUTER JOIN DOCTOR DR ON SM.GUARANTEE_DR_CODE = DR.CODE AND SM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE = DP.CODE AND SM.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND EX.ADJUST_TYPE = 'FX' AND " +
    	"SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"' AND " +
    	"SM.ACTIVE = '1' AND SM.GUARANTEE_FIX_AMOUNT > 1 AND SM.GUARANTEE_TYPE_CODE LIKE 'M%' "+
    	"UNION "+
		"SELECT SM.HOSPITAL_CODE, SM.GUARANTEE_DR_CODE, SM.YYYY+SM.MM, " +
		"SM.GUARANTEE_CODE, SM.START_DATE, SM.GUARANTEE_FIX_AMOUNT, SM.GUARANTEE_FIX_AMOUNT, EX.SIGN, "+
		"EX.ACCOUNT_CODE, EX.CODE, EX.TAX_TYPE_CODE, "+
		"SM.YYYY, SM.MM, 'Guarantee Fix : Start Date/Start Time: '+SM.START_DATE+'/'+SM.START_TIME, 'ProcessGuarantee', "+
    	"DR.DEPARTMENT_CODE, DP.DEFAULT_LOCATION_CODE "+
    	"FROM STP_GUARANTEE SM "+ 
    	"LEFT OUTER JOIN DOCTOR DR ON SM.GUARANTEE_DR_CODE = DR.CODE AND SM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE = DP.CODE AND SM.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND EX.ADJUST_TYPE = 'FX' AND " +
    	"SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"' AND " +
    	"SM.ACTIVE = '1' AND SM.GUARANTEE_FIX_AMOUNT > 1 AND SM.GUARANTEE_TYPE_CODE NOT LIKE 'M%'";
    	try{
    		cdb.insert(s);
    		cdb.commitDB();
    		System.out.println("Do insertExpenseGuaranteeFix : "+s);
    	}catch (Exception e){
    		status = false;
    		System.out.println("Error on insertExpenseGuaranteeFix"+e);
    		System.out.println("By Query : "+s);
    		cdb.rollDB();
    	}
    	return status;
    }
    
    private boolean insertExpenseGuaranteeInclude(){
    	boolean status = true;
        /*
        Absorb Guarantee Daily
        DF_ABSORB_AMOUNT = ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚Â´ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ Absorb ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
    	DF406_HOLD_AMOUNT = ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
        */

    	//Wait for Edit
    	//Get data from stp_guarantee
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
		"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
		"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
		"YYYY, MM, NOTE, EMPLOYEE_ID, " +
		"DEPARTMENT_CODE, LOCATION_CODE) " +
		
		"SELECT DISTINCT SM.HOSPITAL_CODE, SM.GUARANTEE_DR_CODE, SM.YYYY+SM.MM, " +
		"SM.GUARANTEE_CODE, SM.YYYY+SM.MM+'01', SM.GUARANTEE_INCLUDE_AMOUNT, SM.GUARANTEE_INCLUDE_AMOUNT, EX.SIGN, "+
		"EX.ACCOUNT_CODE, EX.CODE, EX.TAX_TYPE_CODE, "+
		"SM.YYYY, SM.MM, 'Guarantee Include : Month/Year: '+SM.MM+'/'+SM.YYYY, 'ProcessGuarantee', "+
    	"CASE WHEN SM.GL_DEPARTMENT_CODE = '' THEN DR.DEPARTMENT_CODE ELSE SM.GL_DEPARTMENT_CODE END AS DEPARTMENT_CODE, " +
    	"DP.DEFAULT_LOCATION_CODE "+
    	"FROM STP_GUARANTEE SM "+ 
    	"LEFT OUTER JOIN DOCTOR DR ON SM.GUARANTEE_DR_CODE = DR.CODE AND SM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE = DP.CODE AND SM.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND EX.ADJUST_TYPE = 'IC' AND " +
    	"SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"' AND " +
    	"SM.ACTIVE = '1' AND SM.GUARANTEE_INCLUDE_AMOUNT > 0 AND SM.GUARANTEE_TYPE_CODE LIKE 'M%' "+
    	"UNION "+
		"SELECT SM.HOSPITAL_CODE, SM.GUARANTEE_DR_CODE, SM.YYYY+SM.MM, " +
		"SM.GUARANTEE_CODE, SM.START_DATE, SM.GUARANTEE_INCLUDE_AMOUNT, SM.GUARANTEE_INCLUDE_AMOUNT, EX.SIGN, "+
		"EX.ACCOUNT_CODE, EX.CODE, EX.TAX_TYPE_CODE, "+
		"SM.YYYY, SM.MM, 'Guarantee Include : Start Date/Start Time: '+SM.START_DATE+'/'+SM.START_TIME, 'ProcessGuarantee', "+
    	"DR.DEPARTMENT_CODE, DP.DEFAULT_LOCATION_CODE "+
    	"FROM STP_GUARANTEE SM "+ 
    	"LEFT OUTER JOIN DOCTOR DR ON SM.GUARANTEE_DR_CODE = DR.CODE AND SM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE = DP.CODE AND SM.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND EX.ADJUST_TYPE = 'IC' AND " +
    	"SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"' AND " +
    	"SM.ACTIVE = '1' AND SM.GUARANTEE_INCLUDE_AMOUNT > 0 AND SM.GUARANTEE_TYPE_CODE NOT LIKE 'M%'";
    	try{
    		cdb.insert(s);
    		cdb.commitDB();
    		System.out.println("Do insertExpenseGuaranteeInclude : "+s);
    	}catch (Exception e){
    		status = false;
    		System.out.println("Error on insertExpenseGuaranteeInclude"+e);
    		System.out.println("By Query : "+s);
    		cdb.rollDB();
    	}
    	return status;
    }
    private boolean insertExpenseGuaranteeDR(){
    	boolean status = true;
        /*
        Absorb Guarantee Daily
        DF_ABSORB_AMOUNT = ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚Â´ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ Absorb ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¢ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ÃƒÂ Ã‚Â¹Ã†â€™ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â«ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ÂºÃƒÂ Ã‚Â¹Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ Absorb ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¡ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¥ ÃƒÂ Ã‚Â¸Ã¢â‚¬Å“ ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â·ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢ÃƒÂ Ã‚Â¹Ã¢â‚¬Â 
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¢â€šÂ¬ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã‚Â£
    	DF406_HOLD_AMOUNT = ÃƒÂ Ã‚Â¸Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã…Â¾ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¹Ã…â€™ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¹Ã¢â‚¬Å¾ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¹Ã¢â‚¬Â°ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â±ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã…Â ÃƒÂ Ã‚Â¸Ã‚Â³ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¹Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â°ÃƒÂ Ã‚Â¸Ã‚Â¡ÃƒÂ Ã‚Â¸Ã‚ÂµÃƒÂ Ã‚Â¸Ã¯Â¿Â½ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â£ÃƒÂ Ã‚Â¸Ã¢â‚¬â€�ÃƒÂ Ã‚Â¸Ã¢â‚¬ï¿½ÃƒÂ Ã‚Â¸Ã‚Â¥ÃƒÂ Ã‚Â¸Ã‚Â­ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‹â€ ÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã‚Â¢ÃƒÂ Ã‚Â¸Ã…Â¡ÃƒÂ Ã‚Â¸Ã‚Â²ÃƒÂ Ã‚Â¸Ã¢â‚¬Â¡ÃƒÂ Ã‚Â¸Ã‚ÂªÃƒÂ Ã‚Â¹Ã‹â€ ÃƒÂ Ã‚Â¸Ã‚Â§ÃƒÂ Ã‚Â¸Ã¢â€žÂ¢
        */

    	//Wait for Edit
    	//Get data from stp_guarantee
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
		"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
		"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
		"YYYY, MM, NOTE, EMPLOYEE_ID, " +
		"DEPARTMENT_CODE, LOCATION_CODE) " +
		
		"SELECT SM.HOSPITAL_CODE, SM.DOCTOR_CODE, SM.YYYY+SM.MM, " +
		"SM.LINE_NO, SM.INVOICE_DATE, SM.GUARANTEE_PAID_AMT, SM.GUARANTEE_PAID_AMT, EX.SIGN, "+
		"EX.ACCOUNT_CODE, EX.CODE, EX.TAX_TYPE_CODE, "+
		"SM.GUARANTEE_TERM_YYYY, SM.GUARANTEE_TERM_MM, " +
		"' Advance : Start Date+Start Time: '+SM.GUARANTEE_CODE, 'ProcessGuarantee', "+
    	"SM.PATIENT_DEPARTMENT_CODE, '' "+
    	"FROM TRN_DAILY SM LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND EX.ADJUST_TYPE = 'DR' AND "+
    	"SM.GUARANTEE_TERM_YYYY = '"+this.year+"' AND SM.GUARANTEE_TERM_MM = '"+this.month+"' AND " +
    	"SM.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' AND SM.ACTIVE = '1' AND " +
        "SM.ORDER_ITEM_ACTIVE = '1' AND SM.INVOICE_TYPE <> 'ORDER' AND SM.BATCH_NO = '' AND "+
        "SM.COMPUTE_DAILY_DATE is not null AND SM.COMPUTE_DAILY_DATE != '' AND "+
        //"(SM.PAY_BY_CASH='Y' OR SM.PAY_BY_AR='Y' OR SM.PAY_BY_DOCTOR='Y' OR SM.PAY_BY_PAYOR='Y' OR SM.PAY_BY_CASH_AR='Y') AND "+
        "SM.IS_PAID = 'Y'";
    	try{
    		cdb.insert(s);
    		cdb.commitDB();
    		System.out.println("Do insertExpenseGuaranteeDR : "+s);
    	}catch (Exception e){
    		status = false;
    		System.out.println("Error on insertExpenseGuaranteeDR : "+e);
    		System.out.println("By Query : "+s);
    		cdb.rollDB();
    	}
    	return status;
    }
}