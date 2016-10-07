package df.bean.process;

import java.sql.SQLException;

import df.bean.db.conn.DBConn;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;

public class ProcessXrayManagementBean implements ProcessMaster {

	private String hospitalCode;
	private String year;
	private String month;
    private DBConn cdb;

	public ProcessXrayManagementBean(String hospitalCode, String year, String month){
		this.hospitalCode = hospitalCode;
		this.year = year;
		this.month = month;
	}
	@Override
	public boolean doBatchClose() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean doProcess() {
		boolean status = true;
		try {
			if(this.doRollback()){
		    	cdb = new DBConn();
				cdb.setStatement();
		        this.setTransactionGuarantee();
				System.out.println(this.isPaidProcess());
		        cdb.insert(this.isPaidProcess());
		        cdb.commitDB();
		        this.cleanTransactionGuarantee();
			}
		} catch (SQLException e) {
			System.out.println(e);
			status = false;
		} finally{
			cdb.closeDB("");
		}
		return status;
	}
	@Override
	public boolean doRollback() {
        boolean status = true;
        String sql_statement = "";
    	cdb = new DBConn();
        try{
			cdb.setStatement();
            sql_statement = 
            "UPDATE TRN_DAILY SET "+
            "NOTE = '', "+
            "ACTIVE = '1' "+
            "WHERE HOSPITAL_CODE = '" + hospitalCode + "' AND " +
            "NOTE = 'XM' AND "+
            "TRANSACTION_DATE LIKE '"+year+month+"%'";

            System.out.println("Rollback X-Ray Management : "+sql_statement);
            cdb.insert(sql_statement); //comment for skip write database process
            cdb.commitDB(); //comment for skip write database process
        }catch(Exception e){
            System.out.println("Exception : "+e);
        	cdb.rollDB();
        	status = false;
        }finally{
			cdb.closeDB("");
		}
        System.out.println("Finished Rollback Xray Management "+JDate.getTime());
        return status;
	}
    private boolean setTransactionGuarantee(){
        boolean status = true;
        String sql_statement = "";
        String start_time = "";
        String end_time = "";
        String guarantee_day_con = "";
        int t = 0;
        String[][] g_setup = null;
        String guarantee_location = "";
    	String message = "Set Guarantee Transaction Conditions For X-Ray Management";
    	
    	String upOrderItem = 
        	"UPDATE TRN_DAILY SET " +
        	"TRN_DAILY.IS_GUARANTEE = ORDER_ITEM.IS_GUARANTEE "+
        	"FROM " +
        	"TRN_DAILY JOIN ORDER_ITEM ON " +
        	"TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND "+
        	"TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "+
        	"WHERE " +
        	"TRN_DAILY.HOSPITAL_CODE = '"+this.hospitalCode+"' AND "+
        	"TRN_DAILY.BATCH_NO = '' AND "+
        	"(TRANSACTION_DATE LIKE '"+this.year+""+this.month+"%')";
        try {
			//cdb.insert(upOrderItem);
		} catch (Exception e1) {
		} finally {
	        cdb.commitDB();			
		}

        try {
            //get guarantee setup (STP_GUARANTEE Table) to verify and update flag
            //into transaction table(TRN_DAILY)
            sql_statement = "SELECT SG.HOSPITAL_CODE, SG.GUARANTEE_DR_CODE, DR.CODE, SG.GUARANTEE_TYPE_CODE, "+ //0-3
            "CASE WHEN SG.ADMISSION_TYPE_CODE = 'U' THEN '%' ELSE SG.ADMISSION_TYPE_CODE END, " + //4
            "SG.MM, SG.YYYY, SG.START_DATE, SG.START_TIME, SG.EARLY_TIME, " +           //5-9
            "SG.END_DATE, SG.END_TIME, SG.LATE_TIME, SG.GUARANTEE_LOCATION, SG.GUARANTEE_AMOUNT, " +            //10-14
            "SG.GUARANTEE_EXCLUDE_AMOUNT, SG.GUARANTEE_SOURCE, SG.GUARANTEE_FIX_AMOUNT, SG.GUARANTEE_CODE, "+   //15-18
            "HP.GUARANTEE_INCLUDE_EXTRA, HP.GUARANTEE_DAY, DR.GUARANTEE_DAY, SG.GUARANTEE_DAY, SG.IS_INCLUDE_LOCATION "+ //19-23
            "FROM STP_GUARANTEE SG " +
            "LEFT OUTER JOIN DOCTOR DR ON (SG.GUARANTEE_DR_CODE = DR.GUARANTEE_DR_CODE AND SG.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
            "LEFT OUTER JOIN HOSPITAL HP ON SG.HOSPITAL_CODE = HP.CODE "+
            "WHERE SG.ACTIVE = '1' AND DR.ACTIVE = '1' AND SG.MM = '"+this.month+"' AND SG.YYYY = '"+this.year+"' AND "+
            "DR.DOCTOR_TYPE_CODE = 'XRY' AND SG.HOSPITAL_CODE = '"+this.hospitalCode+"' AND DR.HOSPITAL_CODE = '"+this.hospitalCode+"' "+
            "ORDER BY SG.GUARANTEE_TYPE_CODE, SG.GUARANTEE_DR_CODE";
            g_setup = cdb.query(sql_statement);
            
        	t = g_setup.length;
        	System.out.println("Num Tran : "+t);
            for(int i = 0; i<g_setup.length; i++){ //update flag in trn_daily for calculate guarantee
            	System.out.println("Set Guarantee Transaction Running to "+i+" Of "+t+" ON TIME "+JDate.getTime());                                
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

	            //Create Statement from Guarantee Date Condition
	            if(g_setup[i][22].equals("VER")){//Guarantee Day? Verify Date = "VER", Invoice Date = "INV"
                	guarantee_day_con = "AND (VERIFY_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND ";
                }else{
                	guarantee_day_con = "AND (TRANSACTION_DATE+VERIFY_TIME BETWEEN '"+g_setup[i][7]+start_time+"' AND '"+g_setup[i][10]+end_time+"') AND ";
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

                try{
                    sql_statement = 
                    "UPDATE TRN_DAILY SET "+
                    "GUARANTEE_NOTE = 'No XM', "+
                    "GUARANTEE_TERM_MM = '" + month +"', "+
                    "GUARANTEE_TERM_YYYY = '" + year +"' "+
                    "WHERE DOCTOR_CODE = '" + g_setup[i][2] + "' AND " + 
                    "ADMISSION_TYPE_CODE LIKE '"+ g_setup[i][4] + "' AND " +
                    guarantee_location+
                    "(TRANSACTION_DATE BETWEEN '"+g_setup[i][6]+""+g_setup[i][5]+"01' AND '"+g_setup[i][6]+""+g_setup[i][5]+"31') AND "+
                    "VERIFY_DATE IS NOT NULL "+guarantee_day_con+
                    "HOSPITAL_CODE = '" + g_setup[i][0] + "' AND " +
                    "ACTIVE = '1'";
                    //"ORDER_ITEM_ACTIVE = '1' AND IS_GUARANTEE = 'Y'";

                    System.out.println("up guarantee : "+sql_statement);
                    cdb.insert(sql_statement); //comment for skip write database process
                    cdb.commitDB(); //comment for skip write database process
                }catch(Exception e){
                    System.out.println("Exception : "+e);
                	cdb.rollDB();
                	status = false;
                	TRN_Error.setUser_name("");
                	TRN_Error.setHospital_code(this.hospitalCode);
		            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
                }
            }
        } catch (Exception ex) {
            System.out.println("Set Xray In Guarantee Exception : "+ex);
            status = false;
        }
        System.out.println("Finished Set Xray In Guarantee "+JDate.getTime());
        return status;
    }
    private boolean cleanTransactionGuarantee(){
        boolean status = true;
        String sql_statement = "";
        int t = 0;
    	String message = "Clean Guarantee Transaction Conditions For X-Ray Management";
        try{
            sql_statement = 
            "UPDATE TRN_DAILY SET "+
            "GUARANTEE_NOTE = '', "+
            "GUARANTEE_TERM_MM = '', "+
            "GUARANTEE_TERM_YYYY = '' "+
            "WHERE HOSPITAL_CODE = '" + hospitalCode + "' AND " +
            "GUARANTEE_NOTE = 'No XM' AND "+
            "GUARANTEE_TERM_MM = '"+month+"' AND "+
            "GUARANTEE_TERM_YYYY = '"+year+"' ";

            System.out.println("clean up guarantee : "+sql_statement);
            cdb.insert(sql_statement); //comment for skip write database process
            cdb.commitDB(); //comment for skip write database process
        }catch(Exception e){
            System.out.println("Exception : "+e);
        	cdb.rollDB();
        	status = false;
        	TRN_Error.setUser_name("");
        	TRN_Error.setHospital_code(this.hospitalCode);
            TRN_Error.writeErrorLog(this.cdb.getConnection(), "GuaranteeProcess",  message, e.toString(), sql_statement,"");
        }
        System.out.println("Finished Clean Xray In Guarantee "+JDate.getTime());
        return status;
    }
    private String isPaidProcess(){
    	return "UPDATE TRN_DAILY SET TRN_DAILY.ACTIVE = '0', TRN_DAILY.NOTE = 'XM' "+
    	"FROM TRN_DAILY S LEFT OUTER JOIN DOCTOR D ON S.DOCTOR_CODE = D.CODE AND S.HOSPITAL_CODE = D.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN ORDER_ITEM OI ON S.ORDER_ITEM_CODE = OI.CODE AND S.HOSPITAL_CODE = OI.HOSPITAL_CODE "+
    	"LEFT OUTER JOIN ORDER_ITEM_CATEGORY OIG ON OI.ORDER_ITEM_CATEGORY_CODE = OIG.CODE AND OI.HOSPITAL_CODE = OIG.HOSPITAL_CODE "+
    	"WHERE (TRANSACTION_DATE LIKE '"+this.year+""+this.month+"%') AND "+
    	"S.GUARANTEE_TERM_YYYY = '' AND "+
    	"D.DOCTOR_TYPE_CODE = 'XRY' AND S.IS_ONWARD <> 'Y' AND "+
    	"S.ACTIVE = '1' AND BATCH_NO = '' AND D.ACTIVE = '1' AND "+
    	"OI.ORDER_ITEM_CATEGORY_CODE IN ( 'XRY' , 'XRY-B' , 'XRY-C' , 'XRY-M' , 'XRY-U' ) AND "+
    	"S.HOSPITAL_CODE LIKE '"+this.hospitalCode+"' AND "+
    	"S.INVOICE_TYPE <>  'ORDER'";
    }
}
