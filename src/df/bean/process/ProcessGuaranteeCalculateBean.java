package df.bean.process;

import java.sql.SQLException;
import df.bean.db.conn.DBConn;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

public class ProcessGuaranteeCalculateBean {
    DBConn cdb;
    String[][] g_setup = null;
    String result = "";
    String month = "";
    String year = "";
    String hospital_code = "";
    String user_id = "";
    /*
    DF_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับตัดรายการ Absorb ส่วนของโรงพยาบาลกรณีเดือนต่อไปมีรายการย้อนหลังของเดือนที่มีการ Absorb
    HP402_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับเป็นรายการ Absorb ส่วนของโรงพยาบาล ณ เดือนนั้นๆ
    DF402_CASH_AMOUNT = Guarantee_exclude_amount ค่าเวร
    DF406_HOLD_AMOUNT = ค่าแพทย์ที่ยังไม่ได้รับชำระและมีการทดลองจ่ายบางส่วน
     */
    public ProcessGuaranteeCalculateBean(DBConn cdb){
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
        if(Variables.IS_TEST){
            System.out.println("Guarantee Process Type : "+process_type);        	
        }
        if(status && process_type.equals("Set OrderItem Active")){
        	status = this.setOrderItemActive();
        }
        if(status && process_type.equals("Set OrderItem Guarantee")){
        	status = this.setOrderItemGuarantee();
        }
        if(status && process_type.equals("Backup Tax Before Guarantee")){
        	status = this.doBackupTransactionTax();
        	status = this.setGuaranteeAllocate();
        }
        if(status && process_type.equals("Prorate Process")){
        	status = this.doProRate();
        }
        if(status && process_type.equals("Adjust Guarantee Amount")){
        	//Adjust guarantee amount from stp_guarantee for guarantee daily to monthly
        	status = this.setAmountGuaranteeDailyToMonthly();
        }
        if(status && process_type.equals("Set Guarantee Transaction")){
        	//Set amount from dr_amt or amount_aft_discount to guarantee process
        	status = this.doTransactionGuarantee();
        }
        if(status && process_type.equals("Guarantee Calculate")){
        	status = this.doGuaranteePaidAmount();
        }
        if(status && process_type.equals("Guarantee Calculate Step")){
        	status = this.calculateGuaranteeStep();
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
            status = sumMonthGuarantee();
        }
        if(status && process_type.equals("Export Summary Absorb")){
            status = sumExpenseGuarantee();
        }
        return status;
    }
    
    private boolean setOrderItemActive(){ //new method
    	//this method for set transaction by order item is active
    	boolean status = true;
    	String message = "Set Transaction to Order Item is Active";
        String sql_statement = "UPDATE TRN_DAILY SET ORDER_ITEM_ACTIVE = '1' " +
        "WHERE "+
        "TRANSACTION_DATE LIKE '"+year+""+month+"%' AND "+
        "HOSPITAL_CODE = '"+hospital_code+"' AND "+
        "(ORDER_ITEM_CODE IN " +
        "(SELECT CODE FROM ORDER_ITEM WHERE ACTIVE = '1' AND HOSPITAL_CODE = '"+hospital_code+"')) ";

        try{
        	System.out.println("Update Order Item Active Start TIME "+JDate.getTime());
            cdb.insert(sql_statement);
            cdb.commitDB();
        	System.out.println("Update Order Item Active Complete TIME "+JDate.getTime());
        }catch(Exception e){
        	TRN_Error.setUser_name(this.user_id);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
        	System.out.println("Can't update Order Item Active from Guarantee Process");
        	status = false;
        }
    	return status;
    }
    private boolean setOrderItemGuarantee(){ //new method
    	//this method for set transaction by order item is guarantee
    	boolean status = true;
    	String message = "Set Transaction by Order Item is Guarantee Calculate";
    	
        String sql_statement = "UPDATE TRN_DAILY SET IS_GUARANTEE = 'Y' " +
        "WHERE "+
        "TRANSACTION_DATE LIKE '"+year+""+month+"%' AND "+
        //"VERIFY_DATE LIKE '"+year+""+month+"%' AND "+
        "INVOICE_TYPE <> 'ORDER' AND COMPUTE_DAILY_DATE <> '' AND "+
        "HOSPITAL_CODE = '"+hospital_code+"' AND "+
        "ORDER_ITEM_CODE IN " +
        	"(SELECT CODE FROM ORDER_ITEM WHERE IS_GUARANTEE = 'Y' " +
        	"AND ACTIVE = '1' AND HOSPITAL_CODE = '"+hospital_code+"')";

        try{
            System.out.println("Update Order Item Guarantee Start TIME "+JDate.getTime());
            cdb.insert(sql_statement);
            cdb.commitDB();
            System.out.println("Update Order Item Guarantee Complete TIME "+JDate.getTime());
        }catch(Exception e){
        	TRN_Error.setUser_name(this.user_id);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
        	System.out.println("Can't update Order Item is Guarantee from Guarantee Process");
            cdb.rollDB();
            status = false;
        }
        
    	return status;
    }
    private boolean doBackupTransactionTax(){ //new method
    	//this method for backup tax transaction
    	boolean status = true;
    	String message = "";
    	
        String sql_statement = "UPDATE TRN_DAILY SET OLD_TAX_AMT = DR_TAX_406+DR_TAX_402, " +
        "OLD_DR_AMT = DR_AMT "+ //Added date 20100320 (nop)
		"WHERE " +
		//"SUBSTRING(INVOICE_DATE,1,6) = '"+year+""+month+"' "+
        "TRANSACTION_DATE LIKE '"+year+""+month+"%' AND "+
		"AND HOSPITAL_CODE = '"+hospital_code+"'";
        message = "Backup Transaction Tax Error";

        try{
            System.out.println("Backup Tax Transaction Start TIME "+JDate.getTime());
            cdb.insert(sql_statement);
            cdb.commitDB();
            System.out.println("Backup Tax Transaction Complete TIME "+JDate.getTime());
        }catch(Exception e){
        	TRN_Error.setUser_name(this.user_id);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
            cdb.rollDB();
            status = false;
        }
    	return status;
    }
    private boolean setGuaranteeAllocate(){
    	//THIS METHOD UPDATE IN/OVER_ALLOCATE FROM DOCTOR TO STP_GUARANTEE ONLY
        boolean status1 = true;
        String sql= "";
        String[][] doctor = null;
        double over_allocate = 0.00;
        double in_allocate = 0.00;
        
        try{
            sql = "SELECT CODE, OVER_GUARANTEE_PCT, IN_GUARANTEE_PCT FROM DOCTOR " +
                  "WHERE ACTIVE = '1' AND CODE = GUARANTEE_DR_CODE AND HOSPITAL_CODE = '"+this.hospital_code+"'";
            doctor = cdb.query(sql);
            
            try{
                for(int i = 0; i<doctor.length; i++){
                	try{
	                	if(Double.parseDouble(""+doctor[i][1])>0 || Double.parseDouble(""+doctor[i][2])>0){
	                		if(Double.parseDouble(""+doctor[i][1])==0){
	                			doctor[i][1] = "100";
	                		}
	                		if(Double.parseDouble(""+doctor[i][2])==0){
	                			doctor[i][2] = "100";
	                		}
	                		sql = "UPDATE STP_GUARANTEE SET OVER_ALLOCATE_PCT = '"+doctor[i][1]+"', " +
	                        "GUARANTEE_ALLOCATE_PCT = '"+doctor[i][2]+"' "+
	                        "WHERE HOSPITAL_CODE = '"+hospital_code+"' AND GUARANTEE_DR_CODE = '"+doctor[i][0]+"' "+
	                        "AND YYYY = '"+year+"' AND MM = '"+month+"' AND ACTIVE = '1'";
	                		//System.out.println(sql);
	                		cdb.insert(sql);
	                	}
                	}catch(Exception e){}
                }
                cdb.commitDB();
                System.out.println("Update Over/In Percent to Setup guarantee Complete");
            }catch(Exception e){
                cdb.rollDB();
            }           
        }catch(Exception e){
            System.out.println("Excepiton in transfer allocate from doctor : "+e);
        }
        return status1;
    }
    private boolean doProRate(){
    	System.out.print(Variables.IS_TEST ? "Prorate Process\n" : "");
    	String list_guarantee = "";
    	String[][] arr_checklist = null;
    	String q_process = "";
    	String message = "";
    	String query_message = "";
    	String guarantee_backup = "UPDATE STP_GUARANTEE SET OLD_ACTIVE = ACTIVE, " +
    	"OLD_START_DATE = START_DATE, OLD_END_DATE = END_DATE, "+
    	"OLD_GUARANTEE_AMOUNT = GUARANTEE_AMOUNT, " +
    	"OLD_GUARANTEE_FIX_AMOUNT = GUARANTEE_FIX_AMOUNT, " +
    	"OLD_GUARANTEE_INCLUDE_AMOUNT = GUARANTEE_INCLUDE_AMOUNT, "+
    	"OLD_GUARANTEE_EXCLUDE_AMOUNT = GUARANTEE_EXCLUDE_AMOUNT " +
    	//"OLD_SPECIAL_AMOUNT = SPECIAL_AMOUNT "+
    	"WHERE HOSPITAL_CODE = '"+hospital_code+"' AND MM = '"+month+"' AND YYYY = '"+year+"'";
    	try {
    		message = "Backup Prorate Process";
    		query_message = guarantee_backup;
			cdb.insert(guarantee_backup);
			cdb.commitDB();
			//GUARANTEE_DR_CODE = '0114695'
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
	    				"THEN '1st PRO-RATE' "+
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
			//System.out.println(list_guarantee+":"+arr_checklist.length);
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
							((Double.parseDouble(JDate.getEndMonthDate(year, month))-Double.parseDouble(arr_checklist[i][2].substring(6, 8)))*100)
							/Double.parseDouble(JDate.getEndMonthDate(year, month))/100);
						
						guarantee_fix = (Double.parseDouble(arr_checklist[i][10])*
								((Double.parseDouble(JDate.getEndMonthDate(year, month))-Double.parseDouble(arr_checklist[i][2].substring(6, 8)))*100)
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
						System.out.println(i+":"+arr_checklist[i][0]);
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
			            System.out.println("Guarantee Process Prorate Update STP_Guarantee error : "+e);
					}
					//}
				}
				
				}catch(Exception e){
					TRN_Error.setUser_name(this.user_id);
					TRN_Error.setHospital_code(this.hospital_code);
		            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), query_message,"");
				}
			}
			
		} catch (SQLException e) {
			TRN_Error.setUser_name(this.user_id);
			TRN_Error.setHospital_code(this.hospital_code);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), query_message,"");
            System.out.println("Guarantee Process Prorate Gen Script error : "+e);
		}
    	return true;
    }
    private boolean setAmountGuaranteeDailyToMonthly(){
    	//This Process is update amount of guarantee every transaction to once price
    	//for VTN Site
    	/*
    	boolean status = true;
    	String[][] arr_guarantee = null;
    	String update_guarantee = "";
    	int all_records = 0;
    	int complete_records = 0;
    	String message = "Adjust Guarantee Daily to Monthly Calculate Amount";
    	String get_guarantee = "SELECT GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, "+
    	"SUM(GUARANTEE_AMOUNT) FROM STP_GUARANTEE WHERE GUARANTEE_TYPE_CODE = 'MLY' "+
    	"AND HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+year+"' AND MM = '"+month+"' "+
    	"AND ACTIVE = '1' AND GUARANTEE_AMOUNT > 0 "+
    	"GROUP BY GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE HAVING COUNT(*) > 1";
    	arr_guarantee = cdb.query(get_guarantee);
    	if(arr_guarantee.length>0){
    		all_records = arr_guarantee.length;
    		for(int i = 0; i < arr_guarantee.length; i++){
    			update_guarantee = "UPDATE STP_GUARANTEE SET GUARANTEE_AMOUNT = '"+arr_guarantee[i][3]+"' " +
    					"WHERE HOSPITAL_CODE = '"+hospital_code+"' "+
    					"AND YYYY = '"+year+"' AND MM = '"+month+"' " +
    					"AND GUARANTEE_DR_CODE = '"+arr_guarantee[i][0]+"' "+
    					"AND GUARANTEE_CODE = '"+arr_guarantee[i][1]+"' "+
    					"AND ADMISSION_TYPE_CODE = '"+arr_guarantee[i][2]+"'";
    			try {
					cdb.insert(update_guarantee);
					complete_records++;
				} catch (SQLException e) {
					status = false;
		            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), update_guarantee,"");
					//System.out.println("Update Guarantee Daily to Monthly Error in Statement :"+update_guarantee);
					//System.out.println("Update Guarantee Daily to Monthly Error Cause : "+e);
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
    	*/
    	return true;
    }
    private boolean doTransactionGuarantee(){
        boolean status = true;
        String sql_statement = "";
        String temp = "";
        String guarantee_code = "";
        String admission = "";
        String start_time = "";
        String end_time = "";
        String is_paid = "";
        String guarantee_extra = "";
        int t = 0;

    	//*dUPLICATE DATA
        //get guarantee setup (STP_GUARANTEE Table) to verify and update flag
        //into transaction table(TRN_DAILY)
        sql_statement = "SELECT SG.HOSPITAL_CODE, SG.GUARANTEE_DR_CODE, DR.CODE, SG.GUARANTEE_TYPE_CODE, "+ //0-3
        "SG.ADMISSION_TYPE_CODE, SG.MM, SG.YYYY, SG.START_DATE, SG.START_TIME, SG.EARLY_TIME, " +           //4-9
        "SG.END_DATE, SG.END_TIME, SG.LATE_TIME, SG.GUARANTEE_LOCATION, SG.GUARANTEE_AMOUNT, " +            //10-14
        "SG.GUARANTEE_EXCLUDE_AMOUNT, DR.GUARANTEE_SOURCE, SG.GUARANTEE_FIX_AMOUNT, SG.GUARANTEE_CODE "+    //15-18
        //", SG.GUARANTEE_SOURCE "+ //19 ADD For Set Guarantee Source From Guarantee Setup Page
        "from STP_GUARANTEE SG LEFT OUTER JOIN DOCTOR DR ON (SG.GUARANTEE_DR_CODE = DR.GUARANTEE_DR_CODE "+
        "AND SG.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
        "WHERE SG.ACTIVE = '1' AND DR.ACTIVE = '1' AND SG.MM = '"+month+"' AND SG.YYYY = '"+year+"' AND "+
        "SG.HOSPITAL_CODE = '"+hospital_code+"' AND DR.HOSPITAL_CODE = '"+hospital_code+"' "+
        "ORDER BY SG.GUARANTEE_TYPE_CODE, SG.GUARANTEE_DR_CODE";
        g_setup = cdb.query(sql_statement);
        
        if(Variables.IS_TEST){
            System.out.println(sql_statement);            	
        }
        
        try{
            //update flag for calculate guarantee to transaction table (TRN_DAILY)
        	t = g_setup.length;
            for(int i = 0; i<g_setup.length; i++){
            	System.out.println("Guarantee Process Step 2 Running to "+i+" Of "+t+" ON TIME "+JDate.getTime());
                //Message Detail Error
                //temp = "Guarantee Doctor Code="+g_setup[i][1]+" And Doctor Code="+
                //        g_setup[i][2]+" AND Transaction Date="+g_setup[i][6]+""+g_setup[i][5];
                
                try{
                	is_paid = (Double.parseDouble(g_setup[i][17])== 0 ? "Y" : "N" );
                }catch(Exception e){
                	is_paid = "Y";
                }
                guarantee_code = g_setup[i][18];
                
                try{
                    if(g_setup[i][13].length()<2){
                        g_setup[i][13] = "NO";
                    }
                }catch(Exception e){
                	g_setup[i][13] = "NO";
                }
                
                if(g_setup[i][4].equals("U")){
                    admission = "ADMISSION_TYPE_CODE LIKE '%' AND ";
                }else{
                	admission = (g_setup[i][4].length()>1 ? 
                				"ADMISSION_TYPE_CODE = '" + g_setup[i][4].charAt(0) + "' AND " : 
                				"ADMISSION_TYPE_CODE = '" + g_setup[i][4] + "' AND " );
                }
                
                try{
                    if(g_setup[i][9].equals("000000") || g_setup[i][9].equals("0")){
                        start_time = g_setup[i][8];
                    }else{
                        start_time = g_setup[i][9];
                    }
                }catch(Exception e){
                	start_time = g_setup[i][8];
                    System.out.println("Early time no value : "+e);
                }
                try{
                    if(g_setup[i][12].equals("000000") || g_setup[i][12].equals("0")){
                        end_time = g_setup[i][11];
                    }else{
                        end_time = g_setup[i][12];
                    }
                }catch(Exception e){
                	end_time = g_setup[i][11];
                    System.out.println("Late time no value : "+e);
                }

                //Check Table Doctor setup where Guarantee Amount get from Before or After Allocate
                if(g_setup[i][16].equals("DF")){
                    sql_statement = "UPDATE TRN_DAILY SET GUARANTEE_AMT = OLD_DR_AMT, "+
                    "OLD_TAX_AMT = DR_TAX_400+DR_TAX_401+DR_TAX_402+DR_TAX_406 "+
                    "WHERE DOCTOR_CODE = '" + g_setup[i][2] + "' AND " + admission +
                    "VERIFY_DATE IS NOT NULL AND "+ //add from old class
                    "(VERIFY_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND "+
                    "PATIENT_LOCATION_CODE <> '" + g_setup[i][13] + "' AND " +
                    "(TRANSACTION_DATE BETWEEN '"+g_setup[i][6]+""+g_setup[i][5]+"01' AND '"+
                    g_setup[i][6]+""+g_setup[i][5]+"31') AND "+
                    "HOSPITAL_CODE = '" + g_setup[i][0] + "' AND " +
                    "ACTIVE = '1' AND " + 
                    "IS_GUARANTEE <> 'N' AND "+
                    "COMPUTE_DAILY_DATE <> '' AND "+
                    "GUARANTEE_AMT = 0";
                }else{
                    sql_statement = "UPDATE TRN_DAILY SET GUARANTEE_AMT = AMOUNT_AFT_DISCOUNT, "+
                    "OLD_TAX_AMT = DR_TAX_400+DR_TAX_401+DR_TAX_402+DR_TAX_406 "+
                    "WHERE DOCTOR_CODE = '" + g_setup[i][2] + "' AND " + admission +
                    "VERIFY_DATE IS NOT NULL AND "+ //add from old class
                    "(VERIFY_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND "+
                    "PATIENT_LOCATION_CODE <> '" + g_setup[i][13] + "' AND " +
                    "(TRANSACTION_DATE BETWEEN '"+g_setup[i][6]+""+g_setup[i][5]+"01' AND '"+
                    g_setup[i][6]+""+g_setup[i][5]+"31') AND "+
                    "HOSPITAL_CODE = '" + g_setup[i][0] + "' AND " +
                    "ACTIVE = '1' AND " + 
                    "IS_GUARANTEE <> 'N' AND "+
                    "COMPUTE_DAILY_DATE <> '' AND "+
                    "GUARANTEE_AMT = 0";
                }
                try{
                    cdb.insert(sql_statement);
                    cdb.commitDB();
                }catch(Exception e){
                	cdb.rollDB();
                	System.out.println("Update Guarantee Amount Exception : "+e+" by statment->"+sql_statement);
                }
                try{
                    if(cdb.getSingleData("SELECT GUARANTEE_INCLUDE_EXTRA FROM HOSPITAL WHERE CODE = '"+g_setup[i][0]+"'").equals("Y")){
                    	guarantee_extra = "GUARANTEE_TERM_MM LIKE '%' AND ";
                    }else{
                    	guarantee_extra = "GUARANTEE_TERM_MM = '' AND ";
                    }
                }catch(Exception e){
                	System.out.println("Exception Select Guarantee Extra Condition "+e);
                }
                sql_statement = "UPDATE TRN_DAILY SET GUARANTEE_CODE = '"+ guarantee_code + "', " + 
                "IS_PAID = '"+ is_paid + "', " +
                "GUARANTEE_DR_CODE = '" + g_setup[i][1] + "', " +
                "GUARANTEE_TYPE = '" + g_setup[i][3] + "', " +
                "GUARANTEE_TERM_MM = '" + g_setup[i][5] +"', "+
                "GUARANTEE_TERM_YYYY = '" + g_setup[i][6] +"', "+
                "GUARANTEE_DATE_TIME = VERIFY_DATE+''+VERIFY_TIME "+ //add from old class
                "WHERE " +
                "DOCTOR_CODE = '" + g_setup[i][2] + "' AND " + admission + guarantee_extra +
                "COMPUTE_DAILY_DATE <> '' AND "+
                "PATIENT_LOCATION_CODE <> '" + g_setup[i][13] + "' AND " +
                "(TRANSACTION_DATE BETWEEN '"+g_setup[i][6]+""+g_setup[i][5]+"01' AND '"+
                g_setup[i][6]+""+g_setup[i][5]+"31') AND "+
                "VERIFY_DATE IS NOT NULL ";
                if(guarantee_code.length()>6){
                	//if guarantee daily
                	sql_statement = sql_statement +"AND (VERIFY_DATE+VERIFY_TIME BETWEEN '"+
                	g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND ";                    	
                }else{
                	//if guarantee monthly
                	sql_statement = sql_statement +
                	"AND (VERIFY_DATE BETWEEN '"+g_setup[i][7]+"' AND '"+g_setup[i][10]+"') "+
                	"AND (VERIFY_TIME BETWEEN '"+start_time+"' AND '"+end_time+"') AND ";
                }
                sql_statement = sql_statement +"HOSPITAL_CODE = '" + g_setup[i][0] + "' AND " +
                "ACTIVE = '1' AND IS_GUARANTEE = 'Y'";

                //System.out.println(sql_statement);
                cdb.insert(sql_statement); //comment for skip write database process
                cdb.commitDB(); //comment for skip write database process
            }
        }catch(Exception e){
            System.out.println("Inner Exception : "+e+" on : "+temp+" sql "+sql_statement);
            result = "Update guarantee data source error : "+e+" on : "+temp+" sql "+sql_statement;
            status = false;
            //TRN_Error.writeErrorLog(cdb.getConnection(),"PrepareGuarantee", "Set Transaction Guarantee Method", result); //Uncomment to write log to db
        }
        System.out.println("FINISH GUARANTEE STEP 2 Result = "+status);
        return status;
    }
    private boolean doGuaranteePaidAmount(){
        boolean status = true;
        String[][] guarantee_table = null;
        String[][] transaction_table = null;
        String admission_type = "";
        String sql_temp = "";
        String sql_tmp = "";
        String error_message = "";
        double fix_amount = 0;
        int t = 0;
        String sql_new = "SELECT DISTINCT "+
        "HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, "+ //0-3
        "GUARANTEE_LOCATION, MM, YYYY, GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT, "+ //4-7
        "GUARANTEE_FIX_AMOUNT, GUARANTEE_TYPE_CODE, OVER_ALLOCATE_PCT, GUARANTEE_EXCLUDE_AMOUNT, " +//8-11
        "HP402_ABSORB_AMOUNT, GUARANTEE_ALLOCATE_PCT, 'N', '0' "+ //12-15
        "FROM STP_GUARANTEE WHERE HOSPITAL_CODE = '"+hospital_code+"' AND MM = '"+month+"' AND " +
        "YYYY = '"+year+"' AND GUARANTEE_TYPE_CODE <> 'STP' AND ACTIVE = '1'";
        guarantee_table = cdb.query(sql_new);
        t = guarantee_table.length;
        for(int i = 0; i<guarantee_table.length; i++){
        	
        	System.out.println("Guarantee Process Step 3 Running to "+i+" Of "+t+" ON TIME "+JDate.getTime());
            admission_type = guarantee_table[i][3].equals("U") ? "%" : guarantee_table[i][3].toString();
            String s = "SELECT INVOICE_NO, INVOICE_DATE, ORDER_ITEM_CODE, LINE_NO, " + //0-3
            "TRANSACTION_MODULE, YYYY, GUARANTEE_AMT, GUARANTEE_DR_CODE, " +           //4-7
            "GUARANTEE_CODE, GUARANTEE_TERM_MM, GUARANTEE_TERM_YYYY, GUARANTEE_PAID_AMT, " +//8-11
            "GUARANTEE_NOTE ,IS_PAID, DR_AMT, HP_AMT, NOR_ALLOCATE_PCT, AMOUNT_AFT_DISCOUNT, "+ //12-17
            "DR_TAX_406, HP_TAX, "+ //18-19
            "CASE WHEN DR_TAX_406+DR_TAX_402 > DR_AMT THEN 'AMT' ELSE 'DFA' END "+ //20
            "FROM TRN_DAILY "+
            "WHERE GUARANTEE_DR_CODE = '"+guarantee_table[i][1]+"' "+
            "AND GUARANTEE_CODE = '"+guarantee_table[i][2]+"' "+
            "AND ADMISSION_TYPE_CODE LIKE '"+ admission_type + "' "+
            "AND GUARANTEE_TERM_MM = '"+guarantee_table[i][5]+"' "+
            "AND GUARANTEE_TERM_YYYY = '"+guarantee_table[i][6]+"' "+
            "AND GUARANTEE_TYPE = '"+guarantee_table[i][9]+"' "+
            "AND HOSPITAL_CODE = '"+guarantee_table[i][0]+"' "+
            "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' "+
            "ORDER BY YYYY DESC, VERIFY_DATE";
            transaction_table = cdb.query(s);
            //System.out.println(s);

            
            if(transaction_table.length < 1){ //HP ABSORB
            	//error_message = "Invoice No/Line No = "+transaction_table[0][0];

                sql_tmp = "UPDATE STP_GUARANTEE SET HP402_ABSORB_AMOUNT = '"+Double.parseDouble(guarantee_table[i][7])+"', "+
                		   "DF_ABSORB_AMOUNT = '"+Double.parseDouble(guarantee_table[i][7])+"', "+
                           "DF402_CASH_AMOUNT = '"+Double.parseDouble(guarantee_table[i][11])+"' "+
                           "WHERE HOSPITAL_CODE = '"+guarantee_table[i][0]+"' "+
                           "AND GUARANTEE_DR_CODE = '"+guarantee_table[i][1]+"' "+
                           "AND GUARANTEE_CODE = '"+guarantee_table[i][2]+"' "+
                           "AND ADMISSION_TYPE_CODE LIKE '"+ admission_type + "' "+
                           "AND MM = '"+guarantee_table[i][5]+"' "+
                           "AND YYYY = '"+guarantee_table[i][6]+"' "+
                           /*
                           "AND (START_DATE BETWEEN '"+guarantee_table[i][6]+guarantee_table[i][5]+"01' " +
                           		"AND '"+guarantee_table[i][6]+guarantee_table[i][5]+"31') " +
                           "AND (END_DATE BETWEEN '"+guarantee_table[i][6]+guarantee_table[i][5]+"01' " +
                           		"AND '"+guarantee_table[i][6]+guarantee_table[i][5]+"31') " +
                           */
                           "AND ACTIVE = '1' "+
                           "AND GUARANTEE_TYPE_CODE = '"+guarantee_table[i][9]+"'";
                try {
                    cdb.insert(sql_tmp);
                    cdb.commitDB();
                } catch (SQLException ex) {
                    System.out.println("Error Update Setup Guarantee Hp Absorb");
                    System.out.println("Cause Command Error : "+sql_tmp);
                    System.out.println("");
                    status = false;
                }
            }else{
                for(int x = 0; x<transaction_table.length; x++){
                	error_message = "Invoice No/Line No = "+transaction_table[x][0]+" / "+transaction_table[x][3];
                    transaction_table[x][12] = "";
                    
                    //If monthly/daily guarantee
                    //and Guarantee Amount > 0 and Guarantee Fix Amount == 0
                    if(Double.parseDouble(guarantee_table[i][7])>0 && Double.parseDouble(guarantee_table[i][8]) == 0){
                        if(transaction_table[x][5].equals("null")||transaction_table[x][5].equals("")){ //DF ABSORB
                            transaction_table[x][12] = "ABSORB GUARANTEE";
                            
                            //if guarantee remain >= transaction guarantee amount (from dr_amt or amount_aft_discount)
                            if(Double.parseDouble(guarantee_table[i][7]) >= Double.parseDouble(transaction_table[x][6])){
                            	//IF GUARANTEE REMAIN > GUARANTEE AMOUNT (FROM TRN_DAILY) 
                            	//* GUARANTEE AMOUNT = dr_amt or amount_aft_discount
                                double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
                                double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]);
                                double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
                                double dr_amt = trn_guarantee_amount * (in_allocate_pct/100);
                                double hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
                                guarantee_amount = guarantee_amount - trn_guarantee_amount;
                                
                                guarantee_table[i][7] = ""+guarantee_amount;
                                transaction_table[x][11] = "0"; //Guarantee_Paid_amt for Absorb some Guarantee
                                transaction_table[x][14] = ""+dr_amt; //Dr_amt
                                transaction_table[x][15] = hp_amt < 0 ? "0" : ""+hp_amt;
                                transaction_table[x][16] = ""+in_allocate_pct;                                
                                /* Default Tax from Method Allocate
                            	transaction_table[x][18] = transaction_table[x][17]; // dr_tax_406 from transaction amount
                            	transaction_table[x][18] = transaction_table[x][14]; // dr_tax_406 from paid amount(dr_amt)
                            	transaction_table[x][18] = transaction_table[x][6]; // dr_tax_406 from guarantee amount
                            	*/
                            
                            }else{ //if guarantee remain < transaction guarantee amount (from dr_amt or amount_aft_discount)
                            	
                            	transaction_table[x][11] = ""+Double.parseDouble(guarantee_table[i][7]); 
                            	//GUARANTEE_PAID_AMT = guarantee remain
                                
                            	if(Double.parseDouble(guarantee_table[i][7]) == 0.1){
                                    transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT
                                    transaction_table[x][12] = "";  //GUARANTEE_NOTE
                                }
                            	
                                //if guarantee_paid_amount > dr_amt
                                if(Double.parseDouble(transaction_table[x][11])>(Double.parseDouble(transaction_table[x][14]))){
                                	guarantee_table[i][7] = "0";
                                	//dr_amt = Guarantee_paid_amt
                                    transaction_table[x][14] = transaction_table[x][11];
                                    transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT
                                    /* Default Tax from Method Allocate
                                	transaction_table[x][18] = transaction_table[x][17]; // dr_tax_406 from transaction amount
                                	transaction_table[x][18] = transaction_table[x][14]; // dr_tax_406 from paid amount(dr_amt)
                                	transaction_table[x][18] = transaction_table[x][6]; // dr_tax_406 from guarantee amount
                                	*/
                                }else{ //if guarantee_paid_amount < dr_amt
                                	transaction_table[x][12] = "ABSORB SOME GUARANTEE";
                                	if(Double.parseDouble(guarantee_table[i][7]) == 0.1){
                                		transaction_table[x][12] = "";
                                	}else{
                                        transaction_table[x][14] = ""+(Double.parseDouble(transaction_table[x][14]) - Double.parseDouble(guarantee_table[i][7]));//DR_AMT
                                        transaction_table[x][18] = ""+(Double.parseDouble(transaction_table[x][18]) - Double.parseDouble(guarantee_table[i][7]));//DR_TAX                        		
                                	}
                                    guarantee_table[i][7] = "0";
                                }
                            }

                        }else{ //NOT ABSORB
                        	//if guarantee remain >= transaction guarantee amount (from dr_amt or amount_aft_discount)
                            if(Double.parseDouble(guarantee_table[i][7]) >= Double.parseDouble(transaction_table[x][6])){ //IN GUARANTEE
                                transaction_table[x][12] = "IN GUARANTEE "+transaction_table[x][16]+" -> "+guarantee_table[i][13];
                                double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
                                double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]);
                                double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
                                double dr_amt = trn_guarantee_amount * (in_allocate_pct/100);
                                double hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
                                guarantee_amount = guarantee_amount - trn_guarantee_amount;
                                
                                transaction_table[x][14] = ""+dr_amt; //dr_amt
                                if(Double.parseDouble(transaction_table[x][18])<= 0.00 && dr_amt > 0){
                                	//transaction_table[x][18] = transaction_table[x][17]; // dr_tax_406 from transaction amount
                                	//transaction_table[x][18] = transaction_table[x][14]; // dr_tax_406 from paid amount(dr_amt)
                                	//if tax <= 0 : tax = guarantee amount (if use other condition consider above this line)
                                	transaction_table[x][18] = transaction_table[x][6]; // dr_tax_406 from guarantee amount
                                	transaction_table[x][19] = "0";						// hp_tax
                                }
                                guarantee_table[i][7] = ""+guarantee_amount;
                                transaction_table[x][15] = hp_amt < 0 ? "0" : ""+hp_amt;
                                transaction_table[x][16] = ""+in_allocate_pct;
                                transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)

                            }else{ //OVER GUARANTEE
                                if(Integer.parseInt(guarantee_table[i][10])>0){
                                    transaction_table[x][12] = "OVER GUARANTEE "+transaction_table[x][16]+" > "+guarantee_table[i][10];

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
                                        transaction_table[x][12] = "IN/OVER GUARANTEE="+(int)trn_in_guarantee_amount+"/"+(int)over_guarantee_amount;
                                    }
                                    if(Double.parseDouble(transaction_table[x][18])<= 0.00 && dr_amt > 0){
                                    	//transaction_table[x][18] = transaction_table[x][17]; // dr_tax_406 from transaction amount
                                    	//transaction_table[x][18] = transaction_table[x][14]; // dr_tax_406 from paid amount(dr_amt)
                                    	//if tax <= 0 : tax = guarantee amount (if use other condition consider above this line)
                                    	transaction_table[x][18] = transaction_table[x][6]; // dr_tax_406 from guarantee amount
                                    	transaction_table[x][19] = "0";						// hp_tax
                                    }

                                    transaction_table[x][15] = hp_amt < 0 ? "0" : ""+hp_amt;
                                    transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                                    transaction_table[x][14] = ""+dr_amt; //DR_AMT
                                    guarantee_table[i][7] = "0.1";
                                }
                            }
                        }                                   
                    }
                    
                    //GUARANTEE EXTRA (PART TIME)
                    if(Double.parseDouble(guarantee_table[i][11]) > 0 ){//GUARANTEE EXTRA
                    	/*
                    	transaction_table[x][12] = "GUARANTEE EXTRA "+transaction_table[x][16]+" ->"+guarantee_table[i][13];
                    	if(Double.parseDouble(guarantee_table[i][11]) >= Double.parseDouble(transaction_table[x][6])){ //IN GUARANTEE
                            transaction_table[x][12] = "IN GUARANTEE EXTRA "+transaction_table[x][16]+" ->"+guarantee_table[i][13];
                            
                            double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
                            double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]);
                            double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
                            double dr_amt = trn_guarantee_amount * (in_allocate_pct/100);
                            double hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
                            guarantee_amount = guarantee_amount - trn_guarantee_amount;
                            
                            transaction_table[x][14] = ""+dr_amt; //dr_amt
                            if(Double.parseDouble(transaction_table[x][18])<= 0.00 && dr_amt > 0){
                            	//transaction_table[x][18] = transaction_table[x][17]; // dr_tax_406 from transaction amount
                            	//transaction_table[x][18] = transaction_table[x][14]; // dr_tax_406 from paid amount(dr_amt)
                            	//if tax <= 0 : tax = guarantee amount (if use other condition consider above this line)
                            	transaction_table[x][18] = transaction_table[x][6]; // dr_tax_406 from guarantee amount
                            }
                            guarantee_table[i][7] = ""+guarantee_amount;
                            transaction_table[x][15] = hp_amt < 0 ? "0" : ""+hp_amt;
                            transaction_table[x][16] = ""+in_allocate_pct;
                            transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                        	transaction_table[x][19] = "0";						 	 // hp_tax

                        }else{ //OVER GUARANTEE
                            if(Integer.parseInt(guarantee_table[i][10])>0){
                                transaction_table[x][12] = "OVER GUARANTEE EXTRA "+transaction_table[x][16]+" > "+guarantee_table[i][10];
                                
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
                                    transaction_table[x][12] = "IN/OVER GUARANTEE="+(int)trn_in_guarantee_amount+"/"+(int)over_guarantee_amount;
                                }
                                if(Double.parseDouble(transaction_table[x][18])<= 0.00 && dr_amt > 0){
                                	//transaction_table[x][18] = transaction_table[x][17]; // dr_tax_406 from transaction amount
                                	//transaction_table[x][18] = transaction_table[x][14]; // dr_tax_406 from paid amount(dr_amt)
                                	//if tax <= 0 : tax = guarantee amount (if use other condition consider above this line)
                                	transaction_table[x][18] = transaction_table[x][6]; // dr_tax_406 from guarantee amount
                                }

                            	transaction_table[x][19] = "0";	
                                transaction_table[x][15] = hp_amt < 0 ? "0" : ""+hp_amt;
                                transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                                transaction_table[x][14] = ""+dr_amt; //DR_AMT
                                guarantee_table[i][7] = "0.1";
                            }
                        } //END ELSE OVER GUARANTEE EXTRA
                    	*/
                    	transaction_table[x][12] = "GUARANTEE EXTRA";
                        transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                    }
                    
                    //GUARANTEE FIX RATE
                    if(Double.parseDouble(guarantee_table[i][8]) > 0){
                        transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
                        transaction_table[x][14] = "0"; //DR_AMT
                        transaction_table[x][18] = "0"; //DR_TAX_406
                        transaction_table[x][12] = "FIX GUARANTEE";
                        transaction_table[x][13] = "N"; //NOT PAY DF
                    }
                    String ss = "";
                    if(transaction_table[x][12].equals("ABSORB GUARANTEE")){
                    	ss = this.getUpdateAbsorbDailyScript(transaction_table, x);
                    //}else if (transaction_table[x][12].equals("ABSORB SOME GUARANTEE")){
                    }else{
                    	ss = "UPDATE TRN_DAILY SET GUARANTEE_PAID_AMT = '"+Double.parseDouble(transaction_table[x][11])+"', "+
                        "IS_PAID = '"+transaction_table[x][13]+"', "+
                        "DR_AMT = '"+Double.parseDouble(transaction_table[x][14])+"', "+
                        "DR_TAX_406 = '"+Double.parseDouble(transaction_table[x][18])+"', "+
                        "HP_AMT = '"+Double.parseDouble(transaction_table[x][15])+"', "+
                        "HP_TAX = '"+Double.parseDouble(transaction_table[x][19])+"', "+
                        //"NOR_ALLOCATE_PCT = '"+transaction_table[x][16]+"', "+
                        "GUARANTEE_NOTE = '"+transaction_table[x][12]+"' " +
                        "WHERE INVOICE_NO = '"+transaction_table[x][0]+"' "+
                        "AND INVOICE_DATE = '"+transaction_table[x][1]+"' "+
                        "AND LINE_NO = '"+transaction_table[x][3]+"' "+
                        "AND GUARANTEE_DR_CODE = '"+transaction_table[x][7]+"' "+
                        "AND GUARANTEE_CODE = '"+transaction_table[x][8]+"' "+
                        "AND GUARANTEE_TERM_MM = '" +transaction_table[x][9]+"' "+
                        "AND GUARANTEE_TERM_YYYY = '"+transaction_table[x][10]+"' "+
                        "AND HOSPITAL_CODE = '"+hospital_code+"' "+
                        //comment order item 20100706 nop
                        //"AND ORDER_ITEM_CODE = '"+transaction_table[x][2]+"' "+
                        "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1'";
                    }
                    try {
                        cdb.insert(ss);
                        cdb.commitDB();
                    } catch (SQLException ex) {
                        System.out.println("Guarantee Prepare Update Transaction : "+ex);
                        System.out.println("Cause Error : "+error_message);
                        System.out.println(ss);
                        status = false;
                    }
                }//END FOR OF GUARANTEE MONTHLY/DAILY AND GUARANTEE TURN
                if(Double.parseDouble(guarantee_table[i][7]) < 0.2){ guarantee_table[i][7] = "0"; }
                if(Double.parseDouble(guarantee_table[i][8]) > 0){
                	fix_amount = Double.parseDouble(guarantee_table[i][8]);
                }else{
                	fix_amount = 0;
                }
                try {
                	//Special Guarantee and DF not over Guarantee
                	//if(guarantee_table[i][14].equals("Y") && Double.parseDouble(guarantee_table[i][7])< Double.parseDouble(guarantee_table[i][15])){
                	if(guarantee_table[i][14].equals("Y")){
                		System.out.println("Special Guarantee");
                		if(Double.parseDouble(guarantee_table[i][7])>0){
                    		//Update Special Guarantee
                    		sql_temp = "UPDATE STP_GUARANTEE SET " +
                    		"HP402_ABSORB_AMOUNT = '"+(Double.parseDouble(guarantee_table[i][15]))+"', "+
    	         		    "DF_ABSORB_AMOUNT = '"+(Double.parseDouble(guarantee_table[i][15]))+"', "+
    	                    "DF402_CASH_AMOUNT = '"+Double.parseDouble(guarantee_table[i][11])+"' "+
    	                    "WHERE HOSPITAL_CODE = '"+guarantee_table[i][0]+"' "+
    	                    "AND GUARANTEE_DR_CODE = '"+guarantee_table[i][1]+"' "+
    	                    "AND GUARANTEE_CODE = '"+guarantee_table[i][2]+"' "+
    	                    "AND ADMISSION_TYPE_CODE LIKE '"+ admission_type + "' "+
    	                    "AND MM = '"+guarantee_table[i][5]+"' "+
    	                    "AND YYYY = '"+guarantee_table[i][6]+"' "+
    	                    "AND ACTIVE = '1' "+
    	                    "AND GUARANTEE_TYPE_CODE = '"+guarantee_table[i][9]+"'";
                    		System.out.println(sql_temp);
                		}else{
    	                	sql_temp = "UPDATE STP_GUARANTEE SET " +
    	                	"HP402_ABSORB_AMOUNT = '"+(Double.parseDouble(guarantee_table[i][7])+fix_amount)+"', "+
    	         		    "DF_ABSORB_AMOUNT = '"+(Double.parseDouble(guarantee_table[i][7])+fix_amount)+"', "+
    	                    "DF402_CASH_AMOUNT = '"+Double.parseDouble(guarantee_table[i][11])+"' "+
    	                    "WHERE HOSPITAL_CODE = '"+guarantee_table[i][0]+"' "+
    	                    "AND GUARANTEE_DR_CODE = '"+guarantee_table[i][1]+"' "+
    	                    "AND GUARANTEE_CODE = '"+guarantee_table[i][2]+"' "+
    	                    "AND ADMISSION_TYPE_CODE LIKE '"+ admission_type + "' "+
    	                    "AND MM = '"+guarantee_table[i][5]+"' "+
    	                    "AND YYYY = '"+guarantee_table[i][6]+"' "+
    	                    "AND ACTIVE = '1' "+
    	                    "AND GUARANTEE_TYPE_CODE = '"+guarantee_table[i][9]+"'";
                		}
                	}else{
                		//Update Non Special Guarantee for HP Absorb Only
	                	sql_temp = "UPDATE STP_GUARANTEE SET " +
	                	"HP402_ABSORB_AMOUNT = '"+(Double.parseDouble(guarantee_table[i][7])+fix_amount)+"', "+
	         		    "DF_ABSORB_AMOUNT = '"+(Double.parseDouble(guarantee_table[i][7])+fix_amount)+"', "+
	                    "DF402_CASH_AMOUNT = '"+Double.parseDouble(guarantee_table[i][11])+"' "+
	                    "WHERE HOSPITAL_CODE = '"+guarantee_table[i][0]+"' "+
	                    "AND GUARANTEE_DR_CODE = '"+guarantee_table[i][1]+"' "+
	                    "AND GUARANTEE_CODE = '"+guarantee_table[i][2]+"' "+
	                    "AND ADMISSION_TYPE_CODE LIKE '"+ admission_type + "' "+
	                    "AND MM = '"+guarantee_table[i][5]+"' "+
	                    "AND YYYY = '"+guarantee_table[i][6]+"' "+
	                    "AND ACTIVE = '1' "+
	                    "AND GUARANTEE_TYPE_CODE = '"+guarantee_table[i][9]+"'";
                	}
                    cdb.insert(sql_temp);
                    cdb.commitDB();
                } catch (SQLException ex) {
                    System.out.println("Guarantee Prepare Update Guarantee Table : "+ex);
                    System.out.println("Cause Error : "+error_message);
                    System.out.println("");
                    status = false;
                }
            }//END ELSE OF GUARANTEE MONTHLY/DAILY AND GUARANTEE TURN
        }        
        System.out.println("FINISH GUARANTEE STEP 3");
        return status;
    }
    private boolean calculatePreviousGuarantee(){
        boolean status = true;
        boolean admis_status = true;
        String[][] guarantee_table = null;
        String[][] transaction_table = null;
        String guarantee_info = "";
        String is_paid = "";
        int count_line = 0;
        String t = "UPDATE STP_GUARANTEE SET OLD_ABSORB_AMOUNT = DF_ABSORB_AMOUNT";
        String sql_trn = "SELECT T.INVOICE_NO, T.INVOICE_DATE, T.LINE_NO, T.VERIFY_DATE, " + //0-3
        "T.VERIFY_TIME, T.DOCTOR_CODE, DR.GUARANTEE_DR_CODE, ISNULL(T.DR_AMT,0), ISNULL(T.AMOUNT_AFT_DISCOUNT,0), " +//4-8
        "DR.GUARANTEE_SOURCE, T.ADMISSION_TYPE_CODE "+ //9-10
        "FROM TRN_DAILY T LEFT OUTER JOIN ORDER_ITEM OI ON T.ORDER_ITEM_CODE = OI.CODE " +
        "AND T.HOSPITAL_CODE = OI.HOSPITAL_CODE "+
        "LEFT OUTER JOIN DOCTOR DR ON T.DOCTOR_CODE = DR.CODE AND T.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
        "WHERE T.TRANSACTION_DATE LIKE '"+this.year+this.month+"%' AND T.VERIFY_DATE < '"+this.year+this.month+"' " +
        "AND T.HOSPITAL_CODE = '"+this.hospital_code+"' "+
        "AND (T.GUARANTEE_NOTE <> 'ABSORB OLD GUARANTEE' OR T.GUARANTEE_NOTE = '' OR T.GUARANTEE_NOTE IS NULL) "+
        "AND OI.IS_GUARANTEE = 'Y' AND (T.VERIFY_DATE <> '' AND T.VERIFY_TIME <> '') AND INVOICE_TYPE <> 'ORDER'";
        
        try {
        	System.out.println("Select Previous Guarantee : "+sql_trn);
        	cdb.insert(t);
			transaction_table = cdb.query(sql_trn);
        } catch (Exception ex) {
            System.out.println("Select Previous Guarantee Error : "+ex);
            status = false;
        }
        count_line = transaction_table.length;
        for(int i = 0; i<transaction_table.length; i++){
        	System.out.println("Guarantee Process Previous Running to "+i+" from "+count_line+" ON TIME "+JDate.getTime());

        	//String s1 = "SELECT DISTINCT YYYY, MM, GUARANTEE_DR_CODE, DF_ABSORB_AMOUNT, GUARANTEE_CODE FROM STP_GUARANTEE "+
        	String s1 = "SELECT YYYY, MM, GUARANTEE_DR_CODE, DF_ABSORB_AMOUNT, " +//0-3
        	"GUARANTEE_CODE, ADMISSION_TYPE_CODE " +//4-5
        	"FROM STP_GUARANTEE "+
            "WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND "+
            "DF_ABSORB_AMOUNT > 0 AND "+
            "GUARANTEE_TYPE_CODE <> 'STP' AND "+
            "GUARANTEE_DR_CODE = '"+transaction_table[i][6]+"' AND " +
        	"ACTIVE = '1' AND " +
        	"('" +transaction_table[i][3]+""+transaction_table[i][4]+"' BETWEEN "+
        	"START_DATE+START_TIME AND END_DATE+END_TIME)";
        	/*
        	"('" +transaction_table[i][3]+"' BETWEEN "+
        	"START_DATE AND END_DATE) AND ('"+transaction_table[i][4]+"' BETWEEN START_TIME AND END_TIME)";
        	*/
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
	        			if(transaction_table[i][9].equals("DF")){
	        				System.out.println("DF : "+Double.parseDouble(guarantee_table[0][3])+">="+Double.parseDouble(transaction_table[i][7]));
			        		if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][7])){
		            			guarantee_table[0][3] = ""+(Double.parseDouble(guarantee_table[0][3]) - Double.parseDouble(transaction_table[i][7]));
		            			transaction_table[i][7] = "0";
		            			transaction_table[i][8] = "0";
			        		}else if(Double.parseDouble(guarantee_table[0][3])>0){
			        			if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][7])){
			        				transaction_table[i][7] = "0";
			            			transaction_table[i][8] = "0";
			        			}else{
			        				guarantee_info = "ABSORB OLD SOME";
			            			is_paid = "Y";
			        				transaction_table[i][7] = ""+(Double.parseDouble(transaction_table[i][7])- Double.parseDouble(guarantee_table[0][3]));
			            			transaction_table[i][8] = ""+(Double.parseDouble(transaction_table[i][8])- Double.parseDouble(guarantee_table[0][3]));
			            			//Clear amount of DR_AMT if negative value
			            			if(Double.parseDouble(transaction_table[i][7])<0){
			            				transaction_table[i][7] = "0";
			            			}
			            			//Clear amount of DR_TAX_406 if negative value
			            			if(Double.parseDouble(transaction_table[i][8])<0){
			            				transaction_table[i][8] = "0";
			            			}
			        			}
		            			guarantee_table[0][3] = "0";
			        		}else{
			        			is_paid = "Y";
		        				guarantee_info = "";
			        		}
		        		}else{
		        			//FULL GUARANTEE AMOUNT
	        				System.out.println("AMT : "+Double.parseDouble(guarantee_table[0][3])+">="+Double.parseDouble(transaction_table[i][8]));
		        			if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][8])){
		            			guarantee_table[0][3] = ""+(Double.parseDouble(guarantee_table[0][3]) - Double.parseDouble(transaction_table[i][8]));
		            			transaction_table[i][7] = "0";
		            			transaction_table[i][8] = "0";
			        		}else if(Double.parseDouble(guarantee_table[0][3])>0){
			        			if(Double.parseDouble(guarantee_table[0][3]) >= Double.parseDouble(transaction_table[i][7])){
			        				transaction_table[i][7] = "0";
			        				transaction_table[i][8] = "0";
			        			}else{
			        				transaction_table[i][7] = ""+(Double.parseDouble(transaction_table[i][7])- Double.parseDouble(guarantee_table[0][3]));
			            			transaction_table[i][8] = ""+(Double.parseDouble(transaction_table[i][8])- Double.parseDouble(guarantee_table[0][3]));
			            			//Clear amount of DR_AMT if negative value
			            			if(Double.parseDouble(transaction_table[i][7])<0){
			            				transaction_table[i][7] = "0";
			            			}
			            			//Clear amount of DR_TAX_406 if negative value
			            			if(Double.parseDouble(transaction_table[i][8])<0){
			            				transaction_table[i][8] = "0";
			            			}
			        				is_paid = "Y";
			        				guarantee_info = "ABSORB OLD SOME";
			        			}
		            			guarantee_table[0][3] = "0";
			        		}else{
			        			is_paid = "Y";
		        				guarantee_info = "";		        			
			        		}
		        		}
		        		
		        		String updateGuarantee = "UPDATE STP_GUARANTEE SET DF_ABSORB_AMOUNT = "+
		            	//"DF_ABSORB_AMOUNT - "
		        		Double.parseDouble(guarantee_table[0][3])+" WHERE " +
		            	"GUARANTEE_DR_CODE = '"+guarantee_table[0][2]+"' AND " +
		            	"HOSPITAL_CODE = '"+this.hospital_code+"' AND " +
		        		//Add Date 16/07/2009 Add for Guarantee daily but calculate Total Monthly
		            	"ACTIVE = '1' AND GUARANTEE_CODE = '"+guarantee_table[0][4]+"'";
		        		//-----------------------------------------------------------------------
		        		
		        		//Comment Date 16/07/2009
		            	//"ACTIVE = '1' AND  ('" +transaction_table[i][3]+"' BETWEEN "+
		            	//"START_DATE AND END_DATE) AND ('"+transaction_table[i][4]+"' BETWEEN START_TIME AND END_TIME)";
		        		//-----------------------
		        		String ss = "UPDATE TRN_DAILY SET "+
		                "DR_AMT = '"+Double.parseDouble(transaction_table[i][7])+"', "+
		                "DR_TAX_406 = '"+Double.parseDouble(transaction_table[i][8])+"', "+
		                "IS_PAID = '"+is_paid+"', "+
		                "GUARANTEE_NOTE = '"+guarantee_info+"' " +
		                "WHERE INVOICE_NO = '"+transaction_table[i][0]+"' "+
		                "AND HOSPITAL_CODE = '"+this.hospital_code+"' "+
		                "AND INVOICE_DATE = '"+transaction_table[i][1]+"' "+
		                "AND LINE_NO = '"+transaction_table[i][2]+"'";
		                
		        		try {
		        			//System.out.println(updateGuarantee);
		        			cdb.insert(updateGuarantee);
		        			//System.out.println(ss);
		                    cdb.insert(ss);
		                    cdb.commitDB();
		                } catch (SQLException ex) {
		                	cdb.rollDB();
		                    System.out.println("Previous Guarantee Error : "+ex);
		        			System.out.println("On Statement : "+updateGuarantee);
		        			System.out.println("or Statement"+ss);
		                    status = false;
		                }
        			}
	        	}
        	//System.out.println("No Previous Absorb Data");
        	}
        }
        System.out.println("Finish Process Previous Absorb Guarantee");
        return status;
    }
    private boolean calculateGuaranteeStep(){
        boolean status = true;
        String[][] guarantee_table = null;
        String[][] transaction_table = null;
        String admission_type = "";
        /*
        String sql_temp = "";
        String sql_tmp = "";
        double over_amount = 0;
        double dr_amount = 0;
        double hp_amount = 0;
        */
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
        if(guarantee_table.length > 1){
	        for(int i = 0; i<guarantee_table.length; i++){
	        	System.out.println("Step Guarantee Process Step 4 Running to "+i+" Of "+t+" ON TIME "+JDate.getTime());
	            admission_type = guarantee_table[i][3].equals("U") ? "%" : guarantee_table[i][3].toString();
	            String s = "SELECT INVOICE_NO, INVOICE_DATE, ORDER_ITEM_CODE, LINE_NO, " + //0-3
	            "TRANSACTION_MODULE, YYYY, GUARANTEE_AMT, GUARANTEE_DR_CODE, " +           //4-7
	            "GUARANTEE_CODE, GUARANTEE_TERM_MM, GUARANTEE_TERM_YYYY, GUARANTEE_PAID_AMT, " +//8-11
	            "GUARANTEE_NOTE ,IS_PAID, DR_AMT, HP_AMT, NOR_ALLOCATE_PCT, AMOUNT_AFT_DISCOUNT, "+ //12-17
	            "DR_TAX_406, HP_TAX, MM "+ //18-19
	            "FROM TRN_DAILY "+ 
	            "WHERE HOSPITAL_CODE = '"+guarantee_table[i][0]+"' "+
	            "AND GUARANTEE_DR_CODE = '"+guarantee_table[i][1]+"' "+
	            "AND GUARANTEE_CODE = '"+guarantee_table[i][2]+"' "+
	            "AND ADMISSION_TYPE_CODE LIKE '"+ admission_type + "' "+
	            //"AND GUARANTEE_TERM_MM = '"+guarantee_table[i][5]+"' "+
	            //"AND GUARANTEE_TERM_YYYY = '"+guarantee_table[i][6]+"' "+
	            "AND GUARANTEE_TYPE = '"+guarantee_table[i][9]+"' "+
	            "AND INVOICE_DATE LIKE '"+year+month+"%' "+
	            //"AND MM = '"+month+"' AND YYYY = '"+year+"' "+
	            "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' AND IS_PAID = 'Y' "+
	            "ORDER BY YYYY DESC, INVOICE_NO, LINE_NO";
	            //"ORDER BY INVOICE_NO DESC, INVOICE_DATE, LINE_NO";
	            transaction_table = cdb.query(s);
	            //System.out.println(s);
	
	            if(transaction_table.length < 1){ //HP ABSORB
	            }else{
	                for(int x = 0; x<transaction_table.length; x++){
	                    transaction_table[x][12] = "";
	                    
	                    //If monthly/daily guarantee or guarantee turn
	                    if(Double.parseDouble(guarantee_table[i][7])>0 && Double.parseDouble(guarantee_table[i][8]) == 0){ 
	                    	
	                        //if(transaction_table[x][5].equals("null")||transaction_table[x][5].equals("")){ //Invoice Transaction
	                        //}else{ //Transaction to pay in term
	                        	
	                    	//In Guarantee
	                        if(Double.parseDouble(guarantee_table[i][7]) > Double.parseDouble(transaction_table[x][6])){ //IN GUARANTEE
	                            transaction_table[x][12] = "STEP IN GUARANTEE "+transaction_table[x][16]+" > "+guarantee_table[i][13];
	                            double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
	                            double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]);
	                            double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
	                            double dr_amt = trn_guarantee_amount * (in_allocate_pct/100);
	                            double hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
	                            guarantee_amount = guarantee_amount - trn_guarantee_amount;
	                            transaction_table[x][14] = ""+dr_amt;
	                            if(Double.parseDouble(transaction_table[x][18])== 0.00 && dr_amt > 0){
	                            	transaction_table[x][18] = transaction_table[x][17]; // dr_tax_406
	                            	transaction_table[x][19] = "0";						 // hp_tax
	                            }
	                            guarantee_table[i][7] = ""+guarantee_amount;
	                            transaction_table[x][15] = ""+hp_amt;
	                            transaction_table[x][16] = ""+in_allocate_pct;
	                            transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
	
	                        }else{
	                        	//Over Guarantee
	                            if(Integer.parseInt(guarantee_table[i][10])>0){
	                                transaction_table[x][12] = "STEP OVER GUARANTEE "+transaction_table[x][16]+" > "+guarantee_table[i][10];
	
	                                double in_allocate_pct = Double.parseDouble(guarantee_table[i][13]);
	                                double over_allocate_pct = Double.parseDouble(guarantee_table[i][10]);
	                                double trn_guarantee_amount = Double.parseDouble(transaction_table[x][6]) ;
	                                double guarantee_amount = Double.parseDouble(guarantee_table[i][7]);
	                                double trn_in_guarantee_amount = 0;
	                                double over_guarantee_amount = 0;
	                                double dr_amt = 7;
	                                double hp_amt = 7;
	
	                                if(guarantee_amount < 0.2){
	                                    guarantee_amount = 0;
	                                    dr_amt = trn_guarantee_amount * (over_allocate_pct/100);
	                                    hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
	                                    //hp_amt = trn_guarantee_amount - dr_amt;
	                                }else{
	                                    trn_in_guarantee_amount = guarantee_amount * (in_allocate_pct /100);
	                                    over_guarantee_amount = (trn_guarantee_amount - guarantee_amount) * (over_allocate_pct/100);
	                                    dr_amt = over_guarantee_amount + trn_in_guarantee_amount;
	                                    hp_amt = Double.parseDouble(transaction_table[x][17]) - dr_amt;
	                                    //hp_amt = trn_guarantee_amount - dr_amt;
	                                    transaction_table[x][12] = "STEP IN/OVER "+(int)trn_in_guarantee_amount+"/"+(int)over_guarantee_amount;
	                                }
	                                if(Double.parseDouble(transaction_table[x][18])== 0.00 && dr_amt > 0){
	                                	transaction_table[x][18] = transaction_table[x][17]; // dr_tax_406
	                                	transaction_table[x][19] = "0";						 // hp_tax
	                                }
	                                transaction_table[x][15] = ""+hp_amt;
	                                transaction_table[x][11] = "0"; //GUARANTEE_PAID_AMT (FOR ABSORB ONLY)
	                                transaction_table[x][14] = ""+dr_amt; //DR_AMT
	                                guarantee_table[i][7] = "0.1";
	                            }
	                        }
	                      //} //End transaction to pay in term
	                    }
	                    //new code 12/01/2010
	                    String ss = "";
	                    if(transaction_table[x][12].equals("ABSORB GUARANTEE")){
	                    	ss = this.getUpdateAbsorbDailyScript(transaction_table, x);
	                    //}else if (transaction_table[x][12].equals("ABSORB SOME GUARANTEE")){
	                    }else{
	                    	ss = "UPDATE TRN_DAILY SET GUARANTEE_PAID_AMT = '"+Double.parseDouble(transaction_table[x][11])+"', "+
	                        "IS_PAID = '"+transaction_table[x][13]+"', "+
	                        "DR_AMT = '"+Double.parseDouble(transaction_table[x][14])+"', "+
	                        "DR_TAX_406 = '"+Double.parseDouble(transaction_table[x][18])+"', "+
	                        "HP_AMT = '"+Double.parseDouble(transaction_table[x][15])+"', "+
	                        "HP_TAX = '"+Double.parseDouble(transaction_table[x][19])+"', "+
	                        //"NOR_ALLOCATE_PCT = '"+transaction_table[x][16]+"', "+
	                        "GUARANTEE_NOTE = '"+transaction_table[x][12]+"' " +
	                        "WHERE INVOICE_NO = '"+transaction_table[x][0]+"' "+
	                        "AND HOSPITAL_CODE = '"+hospital_code+"' "+
	                        "AND INVOICE_DATE = '"+transaction_table[x][1]+"' "+
	                        "AND ORDER_ITEM_CODE = '"+transaction_table[x][2]+"' "+
	                        "AND LINE_NO = '"+transaction_table[x][3]+"' "+
	                        "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' "+
	                        "AND GUARANTEE_DR_CODE = '"+transaction_table[x][7]+"' "+
	                        "AND GUARANTEE_CODE = '"+transaction_table[x][8]+"' "+
	                        "AND GUARANTEE_TERM_MM = '" +transaction_table[x][9]+"' "+
	                        "AND GUARANTEE_TERM_YYYY = '"+transaction_table[x][10]+"'";
	                    }

	                    try {
	                        cdb.insert(ss);
	                        cdb.commitDB();
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
        System.out.println("FINISH STEP GUARANTEE STEP 4");
        return status;
    }
    private boolean sumAmountGuarantee(){
        /*
        DF_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับตัดรายการ Absorb ส่วนของโรงพยาบาลกรณีเดือนต่อไปมีรายการย้อนหลังของเดือนที่มีการ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับเป็นรายการ Absorb ส่วนของโรงพยาบาล ณ เดือนนั้นๆ
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ค่าเวร
    	DF406_HOLD_AMOUNT = ค่าแพทย์ที่ยังไม่ได้รับชำระและมีการทดลองจ่ายบางส่วน
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
                  "HOSPITAL_CODE = '"+this.hospital_code+"' AND ACTIVE = '1' "+
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
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, ex.toString(), stm,"");
            status = false;
            cdb.rollDB();
        }
        return status;
    }
    private boolean sumTaxGuarantee(){
    	/*
        DF_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับตัดรายการ Absorb ส่วนของโรงพยาบาลกรณีเดือนต่อไปมีรายการย้อนหลังของเดือนที่มีการ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับเป็นรายการ Absorb ส่วนของโรงพยาบาล ณ เดือนนั้นๆ
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ค่าเวร
        */
        String[][] tmp = null;
        String temp = "";
        boolean status = true;

        String stm = "";
            stm = "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE_CODE "+
                  "FROM STP_GUARANTEE "+
                  "WHERE YYYY = '"+this.year+"' AND MM = '"+this.month+"' AND " +
                  //"GUARANTEE_NOTE = 'DF ABSORB' AND "+
                  "HOSPITAL_CODE = '"+this.hospital_code+"' AND ACTIVE = '1' "+
                  "GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, GUARANTEE_TYPE_CODE";
            //System.out.println(stm);
        tmp = cdb.query(stm);

        try{
            for(int i = 0; i<tmp.length; i++){
                temp = "UPDATE STP_GUARANTEE SET "+
                    "SUM_TAX_406 = DF406_HOLD_AMOUNT, "+//absorb df transaction
                    "SUM_TAX_402 = DF402_HOLD_AMOUNT+HP402_ABSORB_AMOUNT+GUARANTEE_EXCLUDE_AMOUNT, "+
                    //absorb df transaction + absorb by hospital + parttime

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
        	cdb.insert(getRollbackScript());
        	cdb.commitDB();
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
    private boolean sumExpenseGuarantee(){
    	/*
    	String[][] absorb_condition = null;
    	String get_absorb_type = "SELECT CODE, ACCOUNT_CODE, TAX_TYPE_CODE, ADJUST_TYPE FROM EXPENSE " +
    			"WHERE ADJUST_TYPE <> '' AND HOSPITAL_CODE = '"+this.hospital_code+"'";
    	absorb_condition = cdb.query(get_absorb_type);
    	if(absorb_condition.length>0){
    		for(int i = 0; i < absorb_condition.length; i++){
    			if(absorb_condition[i][3].equals("HP")){
    				//If hospital absorb use tax from setup 
    				insertExpenseGuaranteeHP(absorb_condition[i][0],absorb_condition[i][1],absorb_condition[i][2]);
    			}else if(absorb_condition[i][3].equals("DR")){
    				//If df absorb some use tax from setup 
    				insertExpenseGuaranteeDR(absorb_condition[i][0],absorb_condition[i][1],absorb_condition[i][2]);
    			}else{
    				//no condition
    			}
    		}	
    	}
    	*/
    	return true;
    }
    private boolean insertExpenseGuaranteeDR(String expense_code, String account_code, String tax_type_code){
        /*
        Absorb Guarantee Daily
        DF_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับตัดรายการ Absorb ส่วนของโรงพยาบาลกรณีเดือนต่อไปมีรายการย้อนหลังของเดือนที่มีการ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับเป็นรายการ Absorb ส่วนของโรงพยาบาล ณ เดือนนั้นๆ
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ค่าเวร
    	DF406_HOLD_AMOUNT = ค่าแพทย์ที่ยังไม่ได้รับชำระและมีการทดลองจ่ายบางส่วน
        */
    	/*
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
		"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
		"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
		"YYYY, MM, NOTE, EMPLOYEE_ID, " +
		"DEPARTMENT_CODE, LOCATION_CODE) " +
		
		"SELECT SM.HOSPITAL_CODE, SM.DOCTOR_CODE, 'Absorb Guarantee', " +
		"'001', SM.YYYY+SM.MM+'01', SM.SUM_TAX_406, SM.SUM_TAX_406, '1', "+
		"'"+account_code+"', '"+expense_code+"', '"+tax_type_code+"', " +
		"SM.YYYY, SM.MM, 'Advance Guarantee of Month', 'ProcessGuarantee', "+
    	"DR.DEPARTMENT_CODE, DP.DEFAULT_LOCATION_CODE "+
    	"FROM SUMMARY_GUARANTEE SM "+ 
    	"LEFT OUTER JOIN DOCTOR DR ON SM.DOCTOR_CODE = DR.CODE AND SM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"RIGHT JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE = DP.CODE  AND DR.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND " +
    	"SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"' AND SM.SUM_TAX_406 > 0";
    	//System.out.println(s);
    	try{
    		cdb.insert(s);
    		cdb.commitDB();
    	}catch (Exception e){
    		System.out.println(e);
    		cdb.rollDB();
    	}
    	*/
    	return true;
    }
    private boolean insertExpenseGuaranteeHP(String expense_code, String account_code, String tax_type_code){
    	/*
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
		"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
		"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
		"YYYY, MM, NOTE, EMPLOYEE_ID, " +
		"DEPARTMENT_CODE, LOCATION_CODE) " +
		
		"SELECT SM.HOSPITAL_CODE, SM.DOCTOR_CODE, 'Absorb Guarantee', " +
		"'001', SM.YYYY+SM.MM+'01', SM.SUM_TAX_402, SM.SUM_TAX_402, '1', "+
		"'"+account_code+"', '"+expense_code+"', '"+tax_type_code+"', " +
		"SM.YYYY, SM.MM, 'Absorb Guarantee of Month', 'ProcessGuarantee', "+
    	"DR.DEPARTMENT_CODE, DP.DEFAULT_LOCATION_CODE "+
    	"FROM SUMMARY_GUARANTEE SM "+ 
    	"LEFT OUTER JOIN DOCTOR DR ON SM.DOCTOR_CODE = DR.CODE AND SM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
    	"RIGHT JOIN DEPARTMENT DP ON DR.DEPARTMENT_CODE = DP.CODE  AND DR.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital_code+"' AND " +
    	"SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"' AND SM.SUM_TAX_402 > 0";
    	//System.out.println(s);
    	try{
    		cdb.insert(s);
    		cdb.commitDB();
    	}catch (Exception e){
    		System.out.println(e);
    		cdb.rollDB();
    	}
    	*/
    	return true;
    }
    private boolean insertHospitalAbsorb406Daily(String expense_code, String account_code, String tax_type_code){
        /*
        Absorb Guarantee Daily
        DF_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับตัดรายการ Absorb ส่วนของโรงพยาบาลกรณีเดือนต่อไปมีรายการย้อนหลังของเดือนที่มีการ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับเป็นรายการ Absorb ส่วนของโรงพยาบาล ณ เดือนนั้นๆ
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ค่าเวร
    	DF406_HOLD_AMOUNT = ค่าแพทย์ที่ยังไม่ได้รับชำระและมีการทดลองจ่ายบางส่วน
        */
    	/*
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
		"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
		"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
		"YYYY, MM, NOTE, EMPLOYEE_ID, DEPARTMENT_CODE) " +
    	
		"SELECT S.HOSPITAL_CODE, S.DOCTOR_CODE, S.START_DATE+'-'+S.END_DATE, " +
		"S.GUARANTEE_CODE, S.START_DATE, S.HP402_ABSORB_AMOUNT, S.HP402_ABSORB_AMOUNT, '1', "+
		"'"+account_code+"','"+expense_code+"','"+tax_type_code+"', " +
		"YYYY, MM, 'Absorb Guarantee Term "+this.month+"/"+this.year+"', 'ProcessGuarantee', "+
		"D.DEPARTMENT_CODE "+
		"FROM STP_GUARANTEE S LEFT OUTER JOIN DOCTOR D ON S.GUARANTEE_DR_CODE = D.CODE AND " +
		"S.HOSPITAL_CODE = D.HOSPITAL_CODE "+
		"WHERE S.HOSPITAL_CODE = '"+this.hospital_code+"' AND S.GUARANTEE_TYPE_CODE = 'DLY' AND " +
    	"S.YYYY = '"+this.year+"' AND S.MM = '"+this.month+"' AND S.HP402_ABSORB_AMOUNT > 0";
    	System.out.println(s);
    	try{
    		//cdb.insert(s);
    		//cdb.commitDB();
    	}catch (Exception e){
    		System.out.println(e);
    		cdb.rollDB();
    	}
    	*/
    	return true;
    }
    private boolean insertHospitalAbsorb406Monthly(String expense_code, String account_code, String tax_type_code){
        /*
        Absorb Guarantee Daily
        DF_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับตัดรายการ Absorb ส่วนของโรงพยาบาลกรณีเดือนต่อไปมีรายการย้อนหลังของเดือนที่มีการ Absorb
    	HP402_ABSORB_AMOUNT = Guarantee_amount ใช้สำหรับเป็นรายการ Absorb ส่วนของโรงพยาบาล ณ เดือนนั้นๆ
    	DF402_CASH_AMOUNT = Guarantee_exclude_amount ค่าเวร
    	DF406_HOLD_AMOUNT = ค่าแพทย์ที่ยังไม่ได้รับชำระและมีการทดลองจ่ายบางส่วน
        */
    	/*
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE, DOCTOR_CODE, DOC_NO, " +
		"LINE_NO, DOC_DATE, AMOUNT, TAX_AMOUNT, EXPENSE_SIGN, " +
		"EXPENSE_ACCOUNT_CODE, EXPENSE_CODE, TAX_TYPE_CODE, " +
		"YYYY, MM, NOTE, EMPLOYEE_ID, DEPARTMENT_CODE) " +
    	
		"SELECT DISTINCT S.HOSPITAL_CODE, S.DOCTOR_CODE, S.GUARANTEE_CODE, " +
		"S.GUARANTEE_CODE, S.YYYY+S.MM+'01', S.HP402_ABSORB_AMOUNT, S.HP402_ABSORB_AMOUNT, '1', "+
		"'"+account_code+"','"+expense_code+"','"+tax_type_code+"', " +
		"YYYY, MM, 'Absorb Guarantee Term "+this.month+"/"+this.year+"', 'ProcessGuarantee', "+
		"D.DEPARTMENT_CODE "+
		"FROM STP_GUARANTEE S LEFT OUTER JOIN DOCTOR D ON S.GUARANTEE_DR_CODE = D.CODE AND " +
		"S.HOSPITAL_CODE = D.HOSPITAL_CODE "+
		"WHERE S.HOSPITAL_CODE = '"+this.hospital_code+"' AND S.GUARANTEE_TYPE_CODE = 'MLY' AND " +
    	"S.YYYY = '"+this.year+"' AND S.MM = '"+this.month+"' AND S.HP402_ABSORB_AMOUNT > 0";
    	System.out.println(s);
    	try{
    		//cdb.insert(s);
    		//cdb.commitDB();
    	}catch (Exception e){
    		System.out.println(e);
    		cdb.rollDB();
    	}
    	*/
    	return true;
    }
    private String getUpdateAbsorbDailyScript(String[][] trn_daily, int data_index){
    	String update_script = "UPDATE TRN_DAILY SET " +
    	"GUARANTEE_PAID_AMT = '"+Double.parseDouble(trn_daily[data_index][11])+"', "+
        "IS_PAID = '"+trn_daily[data_index][13]+"', "+
        "DR_AMT = '"+Double.parseDouble(trn_daily[data_index][14])+"', "+
        "DR_TAX_406 = '"+Double.parseDouble(trn_daily[data_index][18])+"', "+
        "HP_AMT = '"+Double.parseDouble(trn_daily[data_index][15])+"', "+
        "HP_TAX = '"+Double.parseDouble(trn_daily[data_index][19])+"', "+
        "YYYY = '"+year+"', "+
        "MM = '"+month+"', "+
        "PAY_BY_CASH = 'Y', "+
        "RECEIPT_NO = 'ADVANCE', "+
        "RECEIPT_DATE = INVOICE_DATE, "+
        "GUARANTEE_NOTE = '"+trn_daily[data_index][12]+"' " +
        "WHERE INVOICE_NO = '"+trn_daily[data_index][0]+"' "+
        "AND INVOICE_DATE = '"+trn_daily[data_index][1]+"' "+
        "AND LINE_NO = '"+trn_daily[data_index][3]+"' "+
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
    private String getSumScript(){
    	String t = 
        	"INSERT INTO SUMMARY_GUARANTEE " +
            "(HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM, "+
            "GUARANTEE_AMOUNT, "+
            "SUM_HP_OVER_AMOUNT, "+
            "SUM_TAX_406, SUM_TAX_402, SUM_TAX_400) "+
            
            "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, YYYY, MM, "+
            "SUM(GUARANTEE_AMOUNT+GUARANTEE_FIX_AMOUNT+GUARANTEE_INCLUDE_AMOUNT+GUARANTEE_EXCLUDE_AMOUNT), "+
        	"SUM(SUM_HP_OVER_AMOUNT), "+
        	"SUM(SUM_TAX_406), SUM(SUM_TAX_402), SUM(SUM_TAX_400) "+
        	"FROM VW_PROCESS_GUARANTEE "+ 
        	"WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND YYYY = '"+this.year+"' " +
        	"AND MM = '"+this.month+"' AND SUM_TAX_406+SUM_TAX_402+SUM_TAX_400 > 0 "+
        	"GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, YYYY, MM ";
    	return t;
    }
    private String getRollbackScript(){
    	String t = 
        	"DELETE SUMMARY_GUARANTEE " +
        	"WHERE HOSPITAL_CODE = '"+this.hospital_code+"' AND YYYY = '"+this.year+"' " +
        	"AND MM = '"+this.month+"'";
    	return t;
    }
}