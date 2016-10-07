package df.bean.process;

import java.sql.SQLException;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;
import df.bean.db.table.TRN_Error;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseDisBean {
    DBConn cdb;
    
    String result = "";
    String month = "";
    String year = "";
    String hospital_code = "";
    String expense_code_get = "";
    int expense_sign_get=0;
    String expense_type_get="";
    
    public ExpenseDisBean(DBConn cdb){
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
    
    public boolean prepareProcess(String month, String year, String hospital_code, String process_type){
        boolean status = true,calAmt=true, calTax=true;
        this.month = month;
        this.year = year;
        this.hospital_code = hospital_code;
        System.out.println("START RUN PROCESS Expense Distribute");
          if(process_type.equals("Calculate Expense Distribute"))
	        {
	        	if(DeleteDataSummaryMonthlyExp())
	        	{
	        		if(!(CalculateExpense() && CalculateExpenseTax()))
	        		{
	        			status=false;
	        		}
	        	}
	        }
         return status;
    }

    private boolean DeleteDataSummaryMonthlyExp(){
        //Step 1:ลบข้อมูลออกจากตาราง SUMMARY_MONTHLY ตามเงื่อนไข HOSPITAL_CODE,YYYY,MM, EXPENSE_CODE IN (AD406,DD,406,AD402,DD402)
    	System.out.println("START STEP 1 : DELETE DATA IN TABLE TRN_EXPENSE_DETAIL");
    	String sql_statement1 = "",sql_statement2="";
        String[][] ExpenseArr = null;
        String startMonth="";
        int statusMonth=0;
        boolean status_delete = true;
        int start_month=Integer.parseInt(month)+1;
        int start_year=Integer.parseInt(year)+1;;
        String startYear="";
        if(start_month>12) 
        {
        	startMonth="01";
        	startYear=String.valueOf(start_year);
        	
        }
        else
        {
	        startMonth=String.valueOf(start_month);
	        startYear=year;
	        if(startMonth.length()==1)
	        {
	        	startMonth="0"+startMonth;
	        }
        }
        System.out.println("month="+month);
        System.out.println("startMonth="+startMonth);
        System.out.println("year="+year);
        System.out.println("startYear="+startYear);
        	sql_statement1 = "DELETE FROM TRN_EXPENSE_DETAIL WHERE HOSPITAL_CODE='"+hospital_code+"'"+
            " AND YYYY='"+year+"' AND MM='"+month+"' AND NOTE LIKE 'EXP_DIS%'";
        	System.out.println("DELETE EXP_DIS="+sql_statement1);
        	
        	sql_statement2 = "DELETE FROM TRN_EXPENSE_DETAIL WHERE HOSPITAL_CODE='"+hospital_code+"'"+
        	" AND YYYY='"+startYear+"' AND MM='"+startMonth+"' AND NOTE LIKE 'EXP_ENT%'";
        	System.out.println("DELETE EXP_ENT="+sql_statement2);
        	
        	
        try
        {
            cdb.insert(sql_statement1);
            cdb.insert(sql_statement2);
            System.out.println("Step 1 : Delete TRN_EXPENSE_DETAIL complete");
            cdb.commitDB();
                
         }
       	 catch(Exception e)
       	 {
            System.out.println("Step 1 Delete TRN_EXPENSE_DETAIL EXP_DIS Exclude : "+e+" QUERY="+sql_statement1);
            System.out.println("Step 1 Delete TRN_EXPENSE_DETAIL EXP_ENT Exclude : "+e+" QUERY="+sql_statement2);
            //result = result+"Update order item exclude guarantee is error : "+e;
            status_delete = false;
         }
       	System.out.println("FINISH STEP 1 : DELETE DATA IN TABLE TRN_EXPENSE_DETAIL");
        return status_delete;
    }
    
    
    private String[][] GetRevenue(String DoctorCode, String DoctorProfileCode, String type, String ExpenseStatus){
        //ผลรวมรายได้
    	String sql="";
        String[][] RevenueArr = null;
        //type=1 amount<0
        //type=2 amount>0
        //type=3 amount all
        //ExpStatus=1 is revenue
        //ExpStatus=2 is tax
        sql="SELECT SM.DOCTOR_CODE,(SELECT DOCTOR_PROFILE_CODE FROM DOCTOR WHERE CODE=SM.DOCTOR_CODE)AS DOCTOR_PROFILE_CODE,";
        if(ExpenseStatus.equalsIgnoreCase("1"))//Revenue
        {
        	sql+="CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END AS DR_SUM_AMT,   "
        		+"CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END AS GUARANTEE_AMOUNT,   "
        		+"CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END AS EXPENSE_AMOUNT, "  
        		+"( "
        		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)   "
        		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)  "
        		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END) "
        		+") AS TOTAL_AMOUNT ";
        }
        else if(ExpenseStatus.equalsIgnoreCase("2"))//Tax
        {
        	sql+="CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END AS DR_TAX_406, "
        		+"CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END AS EXPENSE_TAX, "
        		+"( "
        		+"(CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END ) "
        		+"+(CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END ) "
        		+") AS TOTAL_TAX ";
        }
        else//ผลรวมทั้งหมด
        {
        		sql+="CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END AS DR_SUM_AMT,   "
            		+"CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END AS GUARANTEE_AMOUNT,   "
            		+"CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' ) IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' ) END AS EXPENSE_AMOUNT, "  
            		+"( "
            		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)   "
            		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)  "
            		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' ) IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' ) END) "
            		+") AS TOTAL_AMOUNT ";
            
        }
        sql+="FROM SUMMARY_MONTHLY SM,DOCTOR D  "
        +"WHERE SM.HOSPITAL_CODE=D.HOSPITAL_CODE   "
        +" AND SM.DOCTOR_CODE=D.CODE   ";
        if(!DoctorCode.equals("")&& !DoctorProfileCode.equals(""))
        {
        	sql+=" AND D.DOCTOR_PROFILE_CODE='"+DoctorProfileCode+"' ";
        	if(type.equalsIgnoreCase("3"))
        	{
        		sql+=" AND SM.DOCTOR_CODE='"+DoctorCode+"' ";
        	}
        	else
        	{
        		sql+=" AND SM.DOCTOR_CODE<>'"+DoctorCode+"' ";
        	}
        }
        sql+=" AND SM.HOSPITAL_CODE='"+hospital_code+"' "
        +" AND SM.MM='"+month+"'  AND SM.YYYY='"+year+"'   "
        +" GROUP BY SM.DOCTOR_CODE ";
        if(ExpenseStatus.equalsIgnoreCase("1") && type.equalsIgnoreCase("1")) //is revenue and amount<0
        {
        	sql+=" HAVING  "
        		+" ( "
        		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)  " 
        		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)  "
        		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END) "
        		+")<0 ";
        }
        else if(ExpenseStatus.equalsIgnoreCase("1") && type.equalsIgnoreCase("2")) //is revenue and amount>0
        {
        	sql+=" HAVING  "
        		+" ( "
        		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)  " 
        		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)  "
        		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END) "
        		+")>0 ";
        	sql+=" ORDER BY ( "
        		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)   "
        		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)  "
        		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END) "
        		+")DESC ";
        }
        else if(ExpenseStatus.equalsIgnoreCase("2") && type.equalsIgnoreCase("1"))//is tax and amount_tax<0
        {
        	sql+=" HAVING  "
        		+" ( "
                +"(CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END ) "
                +"+(CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END ) "
                +")<0 ";
        	sql+=" ORDER BY SM.DOCTOR_CODE ";
        }
        else if(ExpenseStatus.equalsIgnoreCase("2") && type.equalsIgnoreCase("2"))//is tax and amount_tax>0
        {
        	sql+=" HAVING  "
        		+" ( "
                +"(CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END ) "
                +"+(CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END ) "
                +")>0 ";
        	sql+="( (CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)  "
        		+" +(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END) "
        		+" +(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) "
        		+" FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END) ) DESC ";
				
        }
        
                
      	System.out.println("sql="+sql);
    	try
        {   
    		RevenueArr = cdb.query(sql);
    		
        }
        catch(Exception e)
        {
            System.out.println("Excepiton Query Data Table SUMMARY_MONTHLY : "+e+" query="+sql);
        }
        return RevenueArr;
        
    } 
    
    private String[][] GetExpense(String doctorCode, String type, String ExpStatus)
    {
        String sql="";
        String[][] ExpenseArr = null;
        
        if(ExpStatus.equals("1"))//Revenue
        {
        	sql = "SELECT DOCTOR_CODE,(AMOUNT*EXPENSE_SIGN) AS TOTAL_AMOUNT,  "
           	  +" LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE "
        	  +" FROM TRN_EXPENSE_DETAIL "
        	  +" WHERE HOSPITAL_CODE='"+hospital_code+"' "
        	  +" AND MM='"+month+"' "
        	  +" AND YYYY='"+year+"' "
        	  +" AND DOCTOR_CODE='"+doctorCode+"' ";
        	  //+" AND NOTE NOT LIKE 'EXP_DIS%' ";// OR NOTE NOT LIKE 'EXP_ENT%')
                	  
           	if(type.equals("1"))//ติดลบ
        	{
        		sql+=" AND (AMOUNT*EXPENSE_SIGN)<0 ";
        		sql+=" ORDER BY (AMOUNT*EXPENSE_SIGN) ASC ";
        	}
        	else if(type.equals("2"))//บวก
        	{
        	  sql+=" AND ((AMOUNT*EXPENSE_SIGN)>0 "
        		  +" OR (AMOUNT*EXPENSE_SIGN)<>0) ";
        	  sql+=" ORDER BY (AMOUNT*EXPENSE_SIGN) DESC ";
        	}
        }
        else//Tax
        {
        	sql = "SELECT DOCTOR_CODE,TAX_AMOUNT,  "
          	  +" LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE "
          	  +" FROM TRN_EXPENSE_DETAIL "
          	  +" WHERE HOSPITAL_CODE='"+hospital_code+"' "
          	  +" AND MM='"+month+"' "
          	  +" AND YYYY='"+year+"' "
          	  +" AND DOCTOR_CODE='"+doctorCode+"' "
          	  +" AND EXPENSE_SIGN=-1 "
          	  +" AND ((TAX_AMOUNT*EXPENSE_SIGN)<>0 "
          	  +" AND AMOUNT <> 0)"
          	  +" ORDER BY TAX_AMOUNT DESC ";
        }
        
        	
        System.out.println("Query Expense  : "+sql);
        	
       	try
        {   
       		ExpenseArr = cdb.query(sql);
        }
        catch(Exception e)
        {
            System.out.println("Excepiton Query Data Table TRN_EXPENSE_DETAIL EXPENSE : "+e+" query="+sql);
        }
       	
        return ExpenseArr;
    } 
    
    private boolean RevenueVerifly(String DoctorCode, String DoctorProfileCode, String ExpenseStatus, double reExpAmount){
        //ตรวจสอบรายได้ว่าติดลบหรือไม่
    	String sql="";
    	boolean status=true;
        String[][] RevenueArr = null;
        double total_rs=0;
      //type=1 amount<0
        //type=2 amount>0
        //type=3 amount all
        //ExpStatus=1 is revenue
        //ExpStatus=2 is tax
        sql="SELECT ";
        if(ExpenseStatus.equalsIgnoreCase("1"))//Revenue
        {
        	sql+="( "
        		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)   "
        		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END)  "
        		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END) "
        		+") AS TOTAL_AMOUNT ";
        }
        else//Tax
        {
        	sql+="( "
        		+"(CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"') END ) "
        		+"+(CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='"+month+"' AND YYYY='"+year+"' AND NOTE NOT LIKE 'EXP_DIS%') END ) "
        		+") AS TOTAL_TAX ";
        }
        sql+="FROM SUMMARY_MONTHLY SM,DOCTOR D "
        +"WHERE SM.HOSPITAL_CODE=D.HOSPITAL_CODE "
        +"AND SM.DOCTOR_CODE=D.CODE ";
        if(!DoctorCode.equals("")&& !DoctorProfileCode.equals("")){
        	sql+=" AND D.DOCTOR_PROFILE_CODE='"+DoctorProfileCode+"' ";
        	sql+=" AND SM.DOCTOR_CODE='"+DoctorCode+"' ";
        }
        sql+=" AND SM.HOSPITAL_CODE='"+hospital_code+"' "
        +" AND SM.MM='"+month+"'  AND SM.YYYY='"+year+"'   "
        +" GROUP BY SM.DOCTOR_CODE ";
             
      	System.out.println("sql_verify="+sql);
    	try
        {   
    		RevenueArr = cdb.query(sql);
    		total_rs=Double.parseDouble(RevenueArr[0][0]);
    		if(total_rs >= 0)
    		{
    			status=false;
    		}
    		
        }
        catch(Exception e)
        {
            System.out.println("Excepiton Query Data Table SUMMARY_MONTHLY Verify : "+e+" query="+sql);
        }
        return status;
    } 
    
    
    
    
    private String[][] TaxVerify()
    {
        String sql="";
        String[][] ExpenseArr = null;
        
        sql = "SELECT DOCTOR_CODE,SUM(TAX_AMOUNT*EXPENSE_SIGN) AS TOTAL_TAX_AMOUNT   "
        	  +" FROM TRN_EXPENSE_DETAIL "
        	  +" WHERE HOSPITAL_CODE='"+hospital_code+"' "
        	  +" AND MM='"+month+"' "
        	  +" AND YYYY='"+year+"' "
        	  //+" AND (MM BETWEEN '"+startMonth+"' AND '"+endMonth+"') "
        	  +" GROUP BY DOCTOR_CODE "
        	  +" HAVING SUM(TAX_AMOUNT*EXPENSE_SIGN)<0 ";
        	   
        	System.out.println("Query Verify Tax : "+sql);
        	
       	try
        {   
       		ExpenseArr = cdb.query(sql);
        }
        catch(Exception e)
        {
            System.out.println("Excepiton Query Data Verify TAX Table TRN_EXPENSE_DETAIL Exception : "+e+" query="+sql);
        }
       	
        return ExpenseArr;
    } 
    /*private boolean RevenueVerifly(String doctorCode,String doctorProfile,double reExpAmount){
        //ดึงข้อมูล month เริ่มต้นและสิ้นสุด
    	String sql="";
        String[][] RevenueArr = null;
        boolean status=true;
        
        sql = "SELECT D.DOCTOR_PROFILE_CODE,SM.DOCTOR_CODE,SM.DR_SUM_AMT AS DR_SUM_AMT, "
        	  +" SG.GUARANTEE_AMOUNT AS GUARANTEE_AMOUNT, "
        	  +" SUM(TED.AMOUNT*TED.EXPENSE_SIGN) AS EXPENSE_AMOUNT, ";
        if(reExpAmount!=0)
        {
        	  sql+=" (SM.DR_SUM_AMT+SG.GUARANTEE_AMOUNT+SUM(TED.AMOUNT*TED.EXPENSE_SIGN)+"+reExpAmount+") AS TOTAL_AMOUNT ";
        }
        else
        {
        	  sql+=" (SM.DR_SUM_AMT+SG.GUARANTEE_AMOUNT+SUM(TED.AMOUNT*TED.EXPENSE_SIGN)) AS TOTAL_AMOUNT ";
            
        }     
        sql+=" FROM SUMMARY_MONTHLY SM, SUMMARY_GUARANTEE SG, TRN_EXPENSE_DETAIL TED, DOCTOR D "
        	  +" WHERE SM.HOSPITAL_CODE=SG.HOSPITAL_CODE "
        	  +" AND SG.HOSPITAL_CODE=TED.HOSPITAL_CODE "
        	  +" AND SM.HOSPITAL_CODE=D.HOSPITAL_CODE "
        	  +" AND SM.DOCTOR_CODE=SG.DOCTOR_CODE "
        	  +" AND SM.DOCTOR_CODE=TED.DOCTOR_CODE "
        	  +" AND SM.DOCTOR_CODE=D.CODE "
        	  +" AND SM.MM=TED.MM AND SM.MM=SG.MM "
        	  +" AND SM.YYYY=TED.YYYY AND SM.YYYY=SG.YYYY "
        	  +" AND SM.HOSPITAL_CODE='"+hospital_code+"' "
        	  +" AND SM.MM='"+month+"' "
        	  +" AND SM.YYYY='"+year+"' ";
        	  //+" AND (SM.MM BETWEEN '"+startMonth+"' AND '"+endMonth+"') ";
        if(!doctorCode.equals(""))
        {
        	sql+=" AND SM.DOCTOR_CODE='"+doctorCode+"' ";
        }
        if(!doctorProfile.equals(""))
        {
        	  sql+=" AND D.DOCTOR_PROFILE_CODE='"+doctorProfile+"' ";
        }
        sql+=" GROUP BY D.DOCTOR_PROFILE_CODE,SM.DOCTOR_CODE, SM.DR_SUM_AMT, SG.GUARANTEE_AMOUNT ";
               	
        	System.out.println("Query Revenue Verify : "+sql);
        	
       	try
        {   
       		double total_amount=0;
       		
       		RevenueArr = cdb.query(sql);
       		total_amount=Double.parseDouble(RevenueArr[0][5]);
       		System.out.println("Total_amount="+total_amount);
       		if(total_amount>0)
       		{
       			status=false;
       		}
        }
        catch(Exception e)
        {
            System.out.println("Excepiton Query Data Table Sum Revenue EXPENSE : "+e+" query="+sql);
        }
       	
        return status;
    } */
    private boolean CalculateExpense()
    {
    	System.out.println(" Step 2 : TRN_EXPENSE_DETAIL =Amount");
        boolean status = true;
        String sql= "",startMonth="",endMonth="";
        String[][] RevenueArr = null;
          //query revenue of month/year
	        try
	        {
	        	System.out.println("ค้นหา doctor code ที่มีรายได้ติดลบ");
	        	RevenueArr=this.GetRevenue("","","1","1");//is amount<0 and revenue
	        	//RevenueArr=this.GetRevenue(startMonth,endMonth,"","","3");
	            String doctorCode="",doctorProfile="";
	            String[][] doctorArr=null, doctorArrRe=null;
	            double dr_sum_amt=0,guarantee_amount=0,expenseAmt=0;
	            String expense_code="",expense_account_code="",eatxpense_note="",line_no="",tax_type_code="";
	            int expense_sign=0, runNum=0;
	            boolean status_doctor_ne=true,status_doctor_dis=true;
	            System.out.println("วนลูปรายได้ของ doctor code ที่ติดลบ");
	            System.out.println("RevenueArr.length="+RevenueArr.length);
	            if(RevenueArr.length !=0)
	            {
		            for(int i=0; i<RevenueArr.length;i++)
		            {
		            	doctorCode=RevenueArr[i][0];
		            	doctorProfile=RevenueArr[i][1];
		            	dr_sum_amt=Double.parseDouble(RevenueArr[i][2]);
		            	guarantee_amount=Double.parseDouble(RevenueArr[i][3]);
		            	expenseAmt=Double.parseDouble(RevenueArr[i][4]);
		            	 System.out.println("=============Start No."+i+" ====================");
		            	 System.out.print("doctorProfile="+doctorProfile);
		            	 System.out.print(",doctorCode="+doctorCode);
		            	 System.out.print(",dr_sum_amt="+dr_sum_amt);
		            	 System.out.print(",guarantee_amount="+guarantee_amount);
		            	 System.out.println(",expenseAmt="+expenseAmt);
		            	//ค้นหา Expense code ที่ติดลบของ doctor code ที่รายได้ติดลบ
		            	String[][] expenseArr=null;
		            	double expense_amount=0, result_amount=0;
	            		expenseArr=GetExpense(doctorCode,"1","1");//เป็นลบ ,revenue
	            		boolean status_amount=true;
	            		int runNumNe=0;
	            		if(expenseArr.length!=0)
	            		{
	            			
	            			for(int g=0;g<expenseArr.length;g++)
	            			{
	            				runNumNe++;
	            				
	            				 System.out.println("============exp deduct no : "+g+" ของ doctor ที่มีรายได้ติดลบ===================");
	            				//ตรวจสอบว่ารายได้รวมแล้วเป็นติดลบหรือไม่ ถ้าติดลบจะให้ค่า true ออกมา
	            				status_amount=RevenueVerifly(doctorCode,doctorProfile,"1",0);
	            				 System.out.println("status_amount="+status_amount);
	            				if(status_amount)//รายได้ยังติดลบอยู่
	            				{
		            				//list expense ที่ติดลบที่ละตัว
		            				expense_amount=Double.parseDouble(expenseArr[g][1]);
		            				 System.out.println("Expense Deduct Amount="+expense_amount);
		            				//ค้นหา Doctor Code ที่อยู่ในภาย Doctor Profile ที่มีรายได้เป็นบวก
		            				doctorArr=this.GetRevenue(doctorCode,doctorProfile,"2","1");//is amount>0 and revenue
		            				
		            				String doctorCodeDis="";
		            				//String[][] expenseDisArr=null;
		            				double d_expense_amount=0;
		            				if(doctorArr.length!=0)
		            				{
		            					//get Doctor Code ที่มีรายได้บวกออกมา
		            					//doctorCodeDis=doctorArr[0][0];
		            					//get Expense ที่เป็นบวกออกมา
		            					//expenseDisArr=GetExpense(doctorCodeDis,"2","1");
		            					//if(expenseDisArr.length !=0)
		            					//{
		            						boolean status_exp_ne=true, status_exp_dis=true, status_next_month=true;
		            						int runNumDis=0;
		            						int num=0;
		            						double total_amount_doctorDis=0,exp_amount_old=0;
		            						
		            						for(int k=0;k<doctorArr.length;k++)
		            						{
		            							exp_amount_old=expense_amount;
		            							System.out.println("old="+exp_amount_old);
		            							System.out.println("new="+expense_amount);
		            							if(status_exp_ne)//รายได้ยังติดลบอยู่หรือไม่
		            							{
			            							double rs_exp_ne=0, rs_exp_dis=0;
			            							boolean status_ne=true, status_dis=true;
			            							int sign_dis=0;
			            							num=k+1;
			            							 System.out.println("============exp add no : "+k+" ของ doctor ที่มีรายได้บวกและ exp บวก===================");
			            							 doctorCodeDis=doctorArr[k][0];//รหัสของ docotor code ที่จะเอามาหัก
			            							 total_amount_doctorDis=Double.parseDouble(doctorArr[k][5]);//จำนวนเงินทั้งหมด
			            							 if(expense_amount !=0 && expense_amount<0)
			            							 {
			            								 if(total_amount_doctorDis !=0 && total_amount_doctorDis>0)
				            							 {
			            									    //เอาค่า Expense ที่เป็นลบมาบวกกับที่เป็นบวก Amount ของ Doctor Code ที่จะหักลบ
						            							rs_exp_ne=expense_amount+total_amount_doctorDis;
						            							if(rs_exp_ne<0)
						            							{
						            								//d_expense_amount น้อยกว่า expense_amount ที่จะกระจาย
						            								rs_exp_dis=total_amount_doctorDis;
						            							}
						            							else
						            							{
						            								rs_exp_dis=expense_amount*-1;
						            								
						            							}
						            							expense_amount=expense_amount+rs_exp_dis;
						            							//ตรวจสอบ Doctor Code ที่ติดลบคำนวณแล้วยังติดลบอยู่หรือไม่ (ไม่เป็นไร)
						            							status_exp_ne=RevenueVerifly(doctorCode,doctorProfile,"1",rs_exp_dis);
						            							
						            							String line_no_ne="", note_ne="", sql_insert_ne="", line_no_dis="", note_dis="", sql_insert_dis="";
						            							int expense_sign_ne=0, expense_sign_dis=0;
						            							runNumDis++;
						            							
							            						//insert data expense distribute เพิ่ม
							            						line_no_ne=expenseArr[g][2]+"ED"+runNumDis;
							            						note_ne="EXP_DIS AddFromDR:"+doctorCodeDis+" LineNo="+expenseArr[g][2]+" "+expenseArr[g][7];
							            						expense_sign_ne=1;
							            						sql_insert_ne="INSERT INTO TRN_EXPENSE_DETAIL("
							            						+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
							            						+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
							            						+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
							            						+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"',"
							            						+" '"+line_no_ne+"', '"+expenseArr[g][3]+"', "+expense_sign_ne+", "
							            						+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_ne+"', "+rs_exp_dis+",0,"
							            						+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
							            						System.out.println("insert_expense_ne="+sql_insert_ne);
							            						    
							            						//insert data expense distribute ลด
							            						line_no_dis=expenseArr[g][2]+"EDN"+runNumDis;
							            						note_dis="EXP_DIS DeductFromDR:"+doctorCode+" LineNo="+expenseArr[g][2]+" "+expenseArr[g][7];
							            						expense_sign_dis=-1;
							            						sql_insert_dis="INSERT INTO TRN_EXPENSE_DETAIL("
							            						+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
							            						+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
							            						+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
							            						+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCodeDis+"',"
							            						+" '"+line_no_dis+"', '"+expenseArr[g][3]+"', "+expense_sign_dis+", "
							            						+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_dis+"', "+rs_exp_dis+",0,"
							            						+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
							            						
							            						// sql = "SELECT 0DOCTOR_CODE,1(AMOUNT*EXPENSE_SIGN) AS TOTAL_AMOUNT,  "
							            						//	  +" 2LINE_NO, 3EXPENSE_CODE, 4EXPENSE_SIGN, 5EXPENSE_ACCOUNT_CODE, 6TAX_TYPE_CODE, 7NOTE "
							            						System.out.println("insert_expense_dis="+sql_insert_dis);
							    	                            try
							    	                            {
							    	                            	cdb.insert(sql_insert_dis);
							    	                                cdb.insert(sql_insert_ne);
							    	                                cdb.commitDB();
							    	                             }
							    	                             catch(Exception e)
							    	                        	 {
							    	                                System.out.println("insert_expense DIS Excepiton : "+e+"query="+sql_insert_dis);
							    	                                System.out.println("insert_expense NE Excepiton : "+e+"query="+sql_insert_ne);
							    	                        		cdb.rollDB();
							    	                               	status=false;
							    	                        	}
							            				 }
			            								 else
			            								 {
			            									 System.out.println("Amount ของ Doctor Code dis ไม่สามารถหักได้อีกแล้ว");
			            								 }
			            							 }
			            							 else
			            							 {
			            								 System.out.println("Expense นี้ ไม่สามารถหักลบได้อีกแล้ว");
			            							 }
		            							}
		            							else
		            							{
		            								System.out.println("รายได้เป็นบวกแล้ว");
		            							}
		            						}//for
		            						boolean status_end=false;
		            						status_end=RevenueVerifly(doctorCode,doctorProfile,"1",0);
		            						System.out.println("status_end="+status_end);
		            						if(runNumNe==expenseArr.length)
	            							{
		            							if(status_end)
		            							{
		            								System.out.println("next_mount");
	    	                                   		//ทำการยกยอดไปเดือนถัดไป
	    	                                   		String [][] AmountArr=this.GetRevenue(doctorCode,doctorProfile,"3","3");
	        		            					double re_amount_total=Double.parseDouble(AmountArr[0][5]);
	        		            					double total_re_ex=re_amount_total;
	        		            					double amount_add=0, amount_next=0;
	        		            					String line_no_ne_n="", note_ne_n="",sql_insert_ne_n="";
	        		            					int expense_sign_ne_n=0;
		        		            				//insert data expense distribute เพิ่ม
			            							line_no_ne_n=expenseArr[g][2]+"EDN"+runNumDis;
			            							note_ne_n="EXP_DIS Deduct "+expenseArr[g][7];
			            							expense_sign_ne_n=1;
			            							sql_insert_ne_n="INSERT INTO TRN_EXPENSE_DETAIL("
			            							+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
			            							+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
			            							+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
			            							+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"',"
			            							+" '"+line_no_ne_n+"', '"+expenseArr[g][3]+"', "+expense_sign_ne_n+", "
			            							+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_ne_n+"', "+re_amount_total*-1+",0,"
			            							+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
			            							System.out.println("nsert_expense_ne_end="+sql_insert_ne_n);
			            								
			            							int month_int=Integer.parseInt(month)+1;
			    	    							String month_str=String.valueOf(month_int);
			    	    							int year_int=0;
			    	    							String year_str="";
			    	    							if(month_int>12)
			    	    							{
			    	    								month_str="01";
			    	    								year_int=Integer.parseInt(year)+1;
			    	    								year_str=String.valueOf(year_int);
			    	    							}
			    	    							else
			    	    							{
			    		    							if(month_str.length()==1)
			    		    							{
			    		    								month_str="0"+month_str;
			    		    							}
			    		    							year_str=year;
			    		    								
			    		    							//String month_str=month_int.toString();
			    	    							}
			            							//insert data expense distribute ลด
			    	    							String line_no_dis_n="", note_dis_n="",sql_insert_dis_n="";
	        		            					int expense_sign_dis_n=0;
	        		            					
			            							line_no_dis_n=expenseArr[g][2]+"ED"+runNumDis;
			            							note_dis_n="EXP_ENT Deduct "+expenseArr[g][7];
			            							expense_sign_dis_n=-1;
			            							sql_insert_dis_n="INSERT INTO TRN_EXPENSE_DETAIL("
			            							+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
			            							+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
			            							+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
			            							+" VALUES('"+hospital_code+"', '"+year_str+"', '"+month_str+"', '"+doctorCode+"',"
			            							+" '"+line_no_dis_n+"', '"+expenseArr[g][3]+"', "+expense_sign_dis_n+", "
			            							+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_dis_n+"', "+re_amount_total*-1+",0,"
			            							+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
			            						
			            							// sql = "SELECT 0DOCTOR_CODE,1(AMOUNT*EXPENSE_SIGN) AS TOTAL_AMOUNT,  "
			            						    //	  +" 2LINE_NO, 3EXPENSE_CODE, 4EXPENSE_SIGN, 5EXPENSE_ACCOUNT_CODE, 6TAX_TYPE_CODE, 7NOTE "
			            							System.out.println("insert_expense_dis_end="+sql_insert_dis_n);
			    	                                try
			    	                                {
			    	                                	cdb.insert(sql_insert_ne_n);
			    	                                	cdb.insert(sql_insert_dis_n);
			    	                                   	cdb.commitDB();
			    	                                }
			    	                                catch(Exception e)
			    	                        		{
			    	                                 	 System.out.println("insert_expense End NE Excepiton : "+e+"query="+sql_insert_ne_n);
			    	                                   	 System.out.println("insert_expense End DIS Excepiton : "+e+"query="+sql_insert_dis_n);
			    	                               	 	cdb.rollDB();
			    	                               	 	status=false;
			    	                        		}
	        		            				}//if(status_exp_ne)
		            						}//if(runNumNe==expenseArr.length)
		            				}//if doctorArr
		            				else//ไม่พบ doctor Code ภายใน Doctor Profile เดียวกัน ให้ทำการยกยอดไปเดือนถัดไป
		            				{
		            					//ทำการยกยอดไปเดือนถัดไป
                                   		String [][] AmountArr=this.GetRevenue(doctorCode,doctorProfile,"3","3");
		            					double re_amount_total=Double.parseDouble(AmountArr[0][5]);
		            					double total_re_ex=re_amount_total;
		            					double amount_add=0, amount_next=0;
		            					String line_no_ne_n="", note_ne_n="",sql_insert_ne_n="";
		            					int expense_sign_ne_n=0;
		            					
    		            				//insert data expense distribute เพิ่ม
            							line_no_ne_n=expenseArr[g][2]+"EDN"+runNumNe;
            							note_ne_n="EXP_DIS NoDR "+expenseArr[g][7];
            							expense_sign_ne_n=1;
            							sql_insert_ne_n="INSERT INTO TRN_EXPENSE_DETAIL("
            							+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
            							+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
            							+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
            							+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"',"
            							+" '"+line_no_ne_n+"', '"+expenseArr[g][3]+"', "+expense_sign_ne_n+", "
            							+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_ne_n+"', "+expense_amount*-1+",0,"
            							+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
            							System.out.println("nsert_expense_ne_end="+sql_insert_ne_n);
            								
            							int month_int=Integer.parseInt(month)+1;
    	    							String month_str="";
    	    							month_str=String.valueOf(month_int);
    	    							int year_int=0;
    	    							String year_str="";
    	    							if(month_int>12)
    	    							{
    	    								month_str="01";
    	    								year_int=Integer.parseInt(year)+1;
    	    								year_str=String.valueOf(year_int);
    	    							}
    	    							else
    	    							{
    		    							if(month_str.length()==1)
    		    							{
    		    								month_str="0"+month_str;
    		    							}
    		    							year_str=year;
    		    								
    		    							//String month_str=month_int.toString();
    	    							}
            							//insert data expense distribute ลด
    	    							String line_no_dis_n="", note_dis_n="",sql_insert_dis_n="";
		            					int expense_sign_dis_n=0;
		            					
            							line_no_dis_n=expenseArr[g][2]+"ED"+runNumNe;
            							note_dis_n="EXP_ENT NoDR "+expenseArr[g][7];
            							expense_sign_dis_n=-1;
            							sql_insert_dis_n="INSERT INTO TRN_EXPENSE_DETAIL("
            							+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
            							+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
            							+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
            							+" VALUES('"+hospital_code+"', '"+year_str+"', '"+month_str+"', '"+doctorCode+"',"
            							+" '"+line_no_dis_n+"', '"+expenseArr[g][3]+"', "+expense_sign_dis_n+", "
            							+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_dis_n+"', "+expense_amount*-1+",0,"
            							+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
            						
            							// sql = "SELECT 0DOCTOR_CODE,1(AMOUNT*EXPENSE_SIGN) AS TOTAL_AMOUNT,  "
            						    //	  +" 2LINE_NO, 3EXPENSE_CODE, 4EXPENSE_SIGN, 5EXPENSE_ACCOUNT_CODE, 6TAX_TYPE_CODE, 7NOTE "
            							System.out.println("insert_expense_dis_end="+sql_insert_dis_n);
    	                                try
    	                                {
    	                                	cdb.insert(sql_insert_ne_n);
    	                                	cdb.insert(sql_insert_dis_n);
    	                                   	cdb.commitDB();
    	                                }
    	                                catch(Exception e)
    	                        		{
    	                                 	 System.out.println("insert_expense End NE Excepiton : "+e+"query="+sql_insert_ne_n);
    	                                   	 System.out.println("insert_expense End DIS Excepiton : "+e+"query="+sql_insert_dis_n);
    	                               	 	cdb.rollDB();
    	                               	 	status=false;
    	                        		}
		            				}
	            				}//status_amount
	            				else//ตรวจสอบแล้วรายได้ไม่ติดลบแล้ว
	            				{
	            					System.out.println("รายได้เป็นบวกแล้ว");
	            				}
	            			}//for expenseArr
	            		} //if expenseArr
	            		else
	            		{
	            			System.out.println("ไม่พบ expense ที่ติดลบ");
	            		}
		            } //for RevenueArr
	            } //if RevenueArr
	            else
	            {
	            	System.out.println("ไม่พบรายได้ที่ติดลบ");
	            }
	        } //try
	        catch(Exception e){
	        	 System.out.println("Step 2:1 QUERY Revenue Excepiton : "+e);
	            status=false;
	        }
        
        
        return status;
    }   
    private boolean CalculateExpenseTax()
    {
    	System.out.println(" Step 3 : TRN_EXPENSE_DETAIL=Tax_Amount ");
        boolean status = true, statusExp=true;
        String sql= "",startMonth="",endMonth="";
        String[][] ExpTaxArr = null;
        //ค้นหาเดือนเริ่มต้นและสิ้นสุด
           //query revenue of month/year
	        
	        	//ค้นหา doctor code ที่มีภาษีติดลบ
	        	ExpTaxArr=this.GetRevenue("","","1","2");//is amount<0 and tax
	        	if(ExpTaxArr.length !=0)
	        	{
		        	String doctorCode="", doctorProfileCode="";
		            String[][] taxArr=null;
		            double amount_next=0, DrTax406=0,TotalTax=0, TaxAmt=0, TaxTotal=0;
		            String expense_code="",expense_account_code="",expense_note="",line_no="",tax_type_code="";
		            int expense_sign=0, runNum=0;
		            //วนลูปภาษีของ doctor code ที่ติดลบ
		            for(int i=0; i<ExpTaxArr.length;i++)
		            {
		            	
		            	doctorCode=ExpTaxArr[i][0];
		            	doctorProfileCode=ExpTaxArr[i][1];
		            	DrTax406=Double.parseDouble(ExpTaxArr[i][2]);
		            	TotalTax=Double.parseDouble(ExpTaxArr[i][4]);
		            	//ค้นหาภาษีที่ติดลบของ doctor code ที่มีภาษีติดลบ
		            	taxArr=GetExpense(doctorCode,"1","2");
		            	for(int g=0;g<taxArr.length;g++)
		            	{
		            		TaxAmt=Double.parseDouble(taxArr[g][1]);
		            		TaxTotal=TotalTax+TaxAmt;
		            		statusExp=RevenueVerifly(doctorCode,doctorProfileCode,"2",0);
		            		System.out.println("statusExp="+statusExp);
							if(statusExp)//คำนวณแล้วยังติดลบ
							{
								if(TaxTotal>=0)//เป็นบวก
			            		{
			            			amount_next=TotalTax*-1;
			            		}
			            		else//เป็นลบ
			            		{
			            			amount_next=TaxAmt;
			            		}
							
		            		
		            		runNum++;
		            		int month_int=Integer.parseInt(month)+1;
		            		String month_str=String.valueOf(month_int);
							int year_int=0;
							String year_str="";
							if(month_int>12)
							{
								month_str="01";
								year_int=Integer.parseInt(year)+1;
								year_str=String.valueOf(year_int);
							}
							else
							{
								if(month_str.length()==1)
								{
									month_str="0"+month_str;
								}
								year_str=year;
								
							}
		    					//ยกยอดไปเดือนถัดไป
		    					//insert data expense distribute เพิ่ม
								String line_no_ne=taxArr[g][2]+"TD"+runNum;
								String note_ne="EXP_DIS TAX :"+taxArr[g][7];
								int expense_sign_ne=1;
								
								if(amount_next !=0)
								{
									String sql_insert_ne="INSERT INTO TRN_EXPENSE_DETAIL("
									+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
									+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
									+" AMOUNT, TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
									+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"',"
									+" '"+line_no_ne+"', '"+taxArr[g][3]+"', "+expense_sign_ne+", "
									+" '"+taxArr[g][5]+"', '"+taxArr[g][6]+"', '"+note_ne+"',0, "+amount_next+", "
									+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
									System.out.println("Insert_expense_tax_ne="+sql_insert_ne);
									
									//insert data expense distribute ลด เป็นรายการยกยอดในเืดือนถัดไป
									String line_no_dis=taxArr[g][2]+"TN"+runNum;
									String note_dis="EXP_ENT TAX:"+taxArr[g][7];
									int expense_sign_dis=-1;
									
									String sql_insert_dis="INSERT INTO TRN_EXPENSE_DETAIL("
									+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
									+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
									+" AMOUNT, TAX_AMOUNT,UPDATE_DATE, UPDATE_TIME)"
									+" VALUES('"+hospital_code+"', '"+year_str+"', '"+month_str+"', '"+doctorCode+"',"
									+" '"+line_no_dis+"', '"+taxArr[g][3]+"', "+expense_sign_dis+", "
									+" '"+taxArr[g][5]+"', '"+taxArr[g][6]+"', '"+note_dis+"',0, "+amount_next+","
									+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
									System.out.println("Insert_expense_tax_dis="+sql_insert_dis);
		                           	try
		                           	{
		                           		cdb.insert(sql_insert_ne);
		                           		cdb.insert(sql_insert_dis);
		                           		cdb.commitDB();
		                           	}
		                           	catch(Exception e)
		                			{
		                				System.out.println("insert_expense TAX NE Excepiton : "+e+"query="+sql_insert_ne);
		                				System.out.println("insert_expense TAX DIS Excepiton : "+e+"query="+sql_insert_dis);
		                       		 	cdb.rollDB();
		                       		 	status=false;
		                			}
								}
							}
							else
							{
								System.out.println("ภาษีเป็นบวกแล้ว");
							}
		            	}
		            }
	        	}
	        	else
	        	{
	        		System.out.println("ไม่พบข้อมูลภาษีที่ติดลบในเดือน "+month);
    			}
	        
        
        
        return status;
    }   
}