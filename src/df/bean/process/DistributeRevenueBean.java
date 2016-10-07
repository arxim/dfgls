package df.bean.process;

import java.sql.SQLException;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;
import df.bean.db.table.TRN_Error;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DistributeRevenueBean {
    DBConn cdb;
    
    String result = "";
    String month = "";
    String year = "";
    String hospital_code = "";
    String rollback_type = "";
    String expense_code_get = "";
    int expense_sign_get=0;
    String expense_type_get="";
    
    public DistributeRevenueBean(DBConn cdb){
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
    
    public boolean prepareProcess(String month, String year, String hospital_code, String process_type, String rollback_type){
        boolean status = true;
        this.month = month;
        this.year = year;
        this.hospital_code = hospital_code;
        this.rollback_type = rollback_type;
        System.out.println("START RUN PROCESS Calculate Distribut Revenue");
        if(process_type.equals("Calculate Distribute Revenue"))
        {
        	if(rollback_type.equals("1"))
        	{
        		status=DeleteDataSummaryMonthly();
        	}
        	else
        	{
	            if(DeleteDataSummaryMonthly() )
	            {
	            	//DeleteDataDf();
	            	//System.out.println("DELETE & INSERT DATA COMPLETE");
	            	//status=true;
	                //status = calculatePreviousGuarantee();
	            	if(CalculateExpenseDF() && CalculateExpenseAbsorb())
	            	{
	            		status=true;
	            	}
	            }
        	}
            
        }
         return status;
    }

    private boolean DeleteDataSummaryMonthly(){
        //Step 1:ลบข้อมูลออกจากตาราง SUMMARY_MONTHLY ตามเงื่อนไข HOSPITAL_CODE,YYYY,MM, EXPENSE_CODE IN (AD406,DD,406,AD402,DD402)
    	System.out.println("START STEP 1 : DELETE DATA IN TABLE SUMMARY_MONTHLY");
    	String sql_statement = "",sql_expense="", inExpense="";
        String[][] ExpenseArr = null;
        
        boolean status = true;
        try
        {
        	sql_expense = "SELECT CODE FROM EXPENSE WHERE ADJUST_TYPE IN('DF','AB') AND ACTIVE='1' ";
        	//System.out.println("Query Step 2:1 : "+sql);
        	ExpenseArr = cdb.query(sql_expense);
            
            try
            {
            	inExpense="IN(";
                for(int i = 0; i<ExpenseArr.length; i++)
                {
                	if(i==0){ inExpense+="'"+ExpenseArr[i][0]+"'"; }
                	else {inExpense+=",'"+ExpenseArr[i][0]+"'";}
                }
                inExpense+=")";
             }
             catch(Exception e)
             {
            	 System.out.println("Step 1 List Data Table EXPENSE Excepiton : "+e);
             }           
        }
        catch(Exception e)
        {
            System.out.println("Step 1 Excepiton Query Data Table EXPENSE : "+e);
        }
       	try
       	{
        	sql_statement = "DELETE FROM TRN_EXPENSE_DETAIL WHERE HOSPITAL_CODE='"+hospital_code+"'"+
            " AND YYYY='"+year+"' AND MM='"+month+"' AND EXPENSE_CODE "+inExpense;
            cdb.insert(sql_statement);
            System.out.println("Step 1 : Delete Summary Monthly complete");
            cdb.commitDB();
                
         }
       	 catch(Exception e)
       	 {
            System.out.println("Step 1 Delete SUMMARY_MONTHLY Exclude : "+e);
            //result = result+"Update order item exclude guarantee is error : "+e;
            status = false;
         }
       	System.out.println("FINISH STEP 1 : DELETE DATA IN TABLE SUMMARY_MONTHLY");
        return status;
    }
    private String[][] GetDataExpense(int expense_sign_get, String expense_type_get){
        //Step 1:ลบข้อมูลออกจากตาราง SUMMARY_MONTHLY ตามเงื่อนไข HOSPITAL_CODE,YYYY,MM, EXPENSE_CODE IN (AD406,DD,406,AD402,DD402)
    	
    	String sql_expense="";
        String[][] ExpenseArr = null;
        this.expense_sign_get = expense_sign_get;
        this.expense_type_get = expense_type_get;
        
        	sql_expense = "SELECT CODE,ACCOUNT_CODE,DESCRIPTION FROM EXPENSE WHERE ACTIVE='1' ";
        	/*if(!expense_code_get.equals(""))
        	{
        		sql_expense+=" AND CODE='"+expense_code_get+"'";
        	}*/
        	if(expense_sign_get !=0)
        	{
        		sql_expense+=" AND SIGN="+expense_sign_get;
        	}
        	if(!expense_type_get.equals(""))
        	{
        		sql_expense+=" AND ADJUST_TYPE='"+expense_type_get+"'";
        	}
        	System.out.println("Query Expense  : "+sql_expense);
        	
       	try
        {   
       		ExpenseArr = cdb.query(sql_expense);
        }
        catch(Exception e)
        {
            System.out.println("Excepiton Query Data Table EXPENSE : "+e);
        }
       	
        return ExpenseArr;
    } 
    private boolean CalculateExpenseDF()
    {
		// Step 2 : Calculate ค่า DF และ Insert into TRN_EXPENSE_DETAIL
        boolean status = true;
        String sql= "";
        String[][] DoctorProfileArr = null;
      //LIST DOCTOR PROFILE
        //sql = "SELECT DOCTOR_PROFILE_CODE FROM STP_DISTRIBUTE_REVENUE GROUP BY DOCTOR_PROFILE_CODE ";
        sql="SELECT SDR.DOCTOR_PROFILE_CODE, SUM(SM.DR_SUM_AMT) AS TOTAL, SUM(SM.DR_TAX_406) AS TOTAL_TAX "
        +" FROM SUMMARY_MONTHLY SM, STP_DISTRIBUTE_REVENUE SDR "
        +" WHERE SM.DOCTOR_CODE=SDR.DOCTOR_CODE "
        +" AND SM.HOSPITAL_CODE=SDR.HOSPITAL_CODE "
        +" AND SM.HOSPITAL_CODE='"+hospital_code+"' "
        +" AND SM.YYYY='"+year+"' "
        +" AND SM.MM='"+month+"' "
        +" AND SDR.REVENUE_TYPE='1' "
        +" AND SDR.ACTIVE='1' "
        +" GROUP BY SDR.DOCTOR_PROFILE_CODE ";	
    	System.out.println("DF=>Step 2:1 Query DF : "+sql);
        try
        {
            DoctorProfileArr = cdb.query(sql);
          
               	System.out.println("==================START DF============================");
                for(int i = 0; i<DoctorProfileArr.length; i++)
                {
                	System.out.println("DF=>Step 2:2 : List Doctor Profile DF Running to "+1+" Of "+DoctorProfileArr.length+" ON TIME "+JDate.getTime());
  			       
                	double totalDRAmt=0, totalTaxAmt=0, sum_amt_dfc=0, sum_tax_dfc=0;
                	String doctorProfileCode=DoctorProfileArr[i][0];
                	System.out.println("doctorProfileCode="+doctorProfileCode);
                	//===========Total DR_Amount and TAX==============
                	totalDRAmt=Double.parseDouble(DoctorProfileArr[i][1]);
                	totalTaxAmt=Double.parseDouble(DoctorProfileArr[i][2]);
                	
                	System.out.println("totalDRAmt="+totalDRAmt);
                	System.out.println("totalTaxAmt="+totalTaxAmt);
                	
                	String[][] doctorArr=null, doctorSumArr=null, expenseArr=null, expenseArrTax=null;
                	int num=0,num_sum=0;
                	//LIST DOCTOR CODE
                	
                	String sql_doctor_sum="SELECT DOCTOR_CODE, DISTRIBUTE_TYPE, REVENUE_TYPE, "
                	+" DISTRIBUTE_PERCENT,DISTRIBUTE_AMOUNT "
                	+" FROM STP_DISTRIBUTE_REVENUE "
                	+" WHERE HOSPITAL_CODE='"+hospital_code+"' "
                	+" AND REVENUE_TYPE='1' " //DF
                	+" AND ACTIVE='1' "
                	+" AND DOCTOR_PROFILE_CODE='"+doctorProfileCode+"'"
                	+" ORDER BY DOCTOR_CODE ";
                	System.out.println("DF=>STEP 2:3 :sql_doctor_sum="+sql_doctor_sum);
                    	try
                    	{
                    		doctorSumArr = cdb.query(sql_doctor_sum);
                    		sum_amt_dfc=totalDRAmt;
                    		sum_tax_dfc=totalTaxAmt;
                    		
                    		for(int g=0 ; g < doctorSumArr.length ; g++)
                    		{
                    			double dis_percent=0,dis_amount=0,dis_amount_tax=0;
                    			String doctorCode=doctorSumArr[g][0];
                    			String distribute_type=doctorSumArr[g][1];
                        		String revenue_type=doctorSumArr[g][2];
                        		dis_percent=Double.parseDouble(doctorSumArr[g][3]);
                        		dis_amount=Double.parseDouble(doctorSumArr[g][4]);
                        		
                        		num_sum=doctorSumArr.length;
                        		num++;
                        		System.out.println("doctorCode="+doctorCode);
                        		//System.out.println("dr_sum_amt="+dr_sum_amt);
                        		System.out.println("distribute_type="+distribute_type);
                        		System.out.println("revenue_type="+revenue_type);
                        		System.out.println("dis_percent="+dis_percent);
                        		System.out.println("dis_amount="+dis_amount);
                        		System.out.println("num_sum="+num_sum);
                        		System.out.println("before num="+num);
                        		System.out.println("after num="+num);
                        		
                        		String sql_doctor = "SELECT DR_SUM_AMT,DR_TAX_406 FROM SUMMARY_MONTHLY "
        	                    +" WHERE HOSPITAL_CODE='"+hospital_code+"' "
        	                    +" AND YYYY='"+year+"' "
        	                    +" AND MM='"+month+"' "
        	                    +" AND DOCTOR_CODE='"+doctorCode+"' "; 
        	                    System.out.println("DF=>Step 2:4 sql_doctor="+sql_doctor);
                    			
        	                    try
                    			{
                    			    doctorArr = cdb.query(sql_doctor);
        	                        System.out.println("doctorArr="+doctorArr.length);
                                        
        	                    	double dr_sum_amt=0,total_dis=0,result_amt=0, bef_sum_amt_dfc=0;
        	                    	double tax_sum_amt=0, result_tax=0, bef_sum_tax_dfc=0, total_dis_tax=0;
        	                    	int expense_sign=0,expense_sign_tax=0;
        	                    	String expense_code="",expense_code_tax="";
        	                    	String expense_account_code="", expense_note="";
                            		String line_no="",line_no_df="";
        	                    	
        	                    	//System.out.println("sum_amt_dfc="+sum_amt_dfc);
                            		//ตรวจสอบว่ามี Doctor code นี้หรือไม่
        	                    	if(doctorArr.length==0)
        	                    	{
        	                    		dr_sum_amt=0;
        	                    		tax_sum_amt=0;
        	                    	}
        	                    	else
        	                    	{
        	                    		dr_sum_amt=Double.parseDouble(doctorArr[0][0]);
        	                    		tax_sum_amt=Double.parseDouble(doctorArr[0][1]);
        	                    		//System.out.println("dr_sum_amt="+dr_sum_amt);
        	                    	}
        	                    	System.out.println("dr_sum_amt="+dr_sum_amt); 
        	                    	System.out.println("dr_tax_amt="+tax_sum_amt);
        	                    	String name_type="";
        	                    	double value_type=0;
        	                    	//ตรวจสอบว่าเลือกการกระจายแบบไหน (%,amount)
                                	if(distribute_type.equals("1"))//%
                                	{
                                		total_dis=(totalDRAmt*dis_percent)/100;
                                		total_dis_tax=(totalTaxAmt*dis_percent)/100;
                                		name_type="%";
                                		value_type=dis_percent;
                                	}
                                	else//amount
                                	{
                                		name_type="Amount";
                                		value_type=dis_amount;
                                		bef_sum_amt_dfc=sum_amt_dfc;
                                		bef_sum_tax_dfc=sum_tax_dfc;
                                		if(num == num_sum)//doctor คนสุดท้าย ซึ่งรายได้ทั้งหมดที่เหลือจะใส่ไว้ที่ doctor code นี้ทั้่งหมด
                                		{
                                			if(sum_amt_dfc<0 || sum_amt_dfc==0)	
                                			{	total_dis=0;    			}
                                			else   
                                			{	total_dis=bef_sum_amt_dfc;  }
                                			if(sum_tax_dfc<0 || sum_tax_dfc==0)	
                                			{   total_dis_tax=0;   			}
                                			else   
                                			{	total_dis_tax=bef_sum_tax_dfc; 	}
                                			
                            			}
                                		else//ไม่ใช่คนสุดท้าย
                                		{
                                			if(sum_amt_dfc<0 || sum_amt_dfc==0)	//จำนวนรวมของ amount น้อยกว่าหรือเท่ากับ 0
                                			{	total_dis=0; }//จำนวนที่จะ insert ใหม่จะมีค่าเป็น 0
                                			else   
                                			{
                                				sum_amt_dfc=sum_amt_dfc-dis_amount;
                                				//amount เมื่อลบกับ dis_amount แล้วมีค่าน้อยกว่า 0 หรือเท่ากับ 0
                                    			if(sum_amt_dfc<0 || sum_amt_dfc==0)
                                    			{
                                    				total_dis=bef_sum_amt_dfc;//ให้ได้ค่าเท่ากับตัวเดิมก่อนการลบ
                                    			}
                                    			else
                                    			{
                                    				total_dis=dis_amount;//ให้เท่ากับตัวที่ลบแล้ว
                                    			}
                                			}
                                			if(sum_tax_dfc<0 || sum_tax_dfc==0)	
                                			{	total_dis_tax=0; }
                                			else   
                                			{
                                				sum_tax_dfc=sum_tax_dfc-dis_amount;
                                			
	                                			//tax เมื่อลบกับ dis_amount แล้วมีค่าน้อยกว่า 0 หรือเท่ากับ 0
	                                			if(sum_tax_dfc<0 || sum_tax_dfc==0)
	                                			{
	                                				total_dis_tax=bef_sum_tax_dfc;//ให้ได้ค่าเท่ากับตัวเดิมก่อนการลบ
	                                			}
	                                			else
	                                			{
	                                				total_dis_tax=dis_amount;//ให้เท่ากับตัวที่ลบแล้ว
	                                			}
                                			}
                                			
                                		}
                                		
                               		}
                                	if(dr_sum_amt!=0 || tax_sum_amt!=0)
                                	{
	                                	//===============insert data ให้ติดลบกับจำนวนเดิมก่อน=====================
	                            		expense_sign=-1;
	                        			expenseArr=this.GetDataExpense(expense_sign, "DF");
	                            		expense_code=expenseArr[0][0];//"DD406";//ลดรายได้query from table expense
	                            		expense_account_code=expenseArr[0][1];
	                            		//expense_note=expenseArr[0][2]+" DR_AMT="+totalDRAmt+" TAX_AMT="+totalTaxAmt+" Dis Type="+name_type+"("+value_type+")";
	                            		expense_note=expenseArr[0][2]+" Total_Amt="+totalDRAmt+" Total_Tax="+totalTaxAmt;
	                            		line_no=expense_code+distribute_type+g;
	                                	
	                            		String insert_expense="INSERT INTO TRN_EXPENSE_DETAIL (YYYY, MM, DOCTOR_CODE, HOSPITAL_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, AMOUNT, TAX_AMOUNT, TAX_TYPE_CODE, EXPENSE_ACCOUNT_CODE, NOTE, UPDATE_DATE, UPDATE_TIME) "
	                                   	+" VALUES('"+year+"','"+month+"','"+doctorCode+"','"+hospital_code+"'"
	                                   	+",'"+line_no+"','"+expense_code+"','"+expense_sign+"',"+dr_sum_amt+","+tax_sum_amt+",'406','"+expense_account_code+"'"
	                                   	+",'"+expense_note+"','"+JDate.getDate()+"','"+JDate.getTime()+"')";
	                                   	System.out.println("DF=>Step 2:5 insert_expense="+insert_expense);
	                                   	try
	                                   	{
	                                   		cdb.insert(insert_expense);
	                                   		cdb.commitDB();
	                                   	}
	                                   	catch(Exception e)
	                        			{
	                        				System.out.println("DF=>Step 2:5 insert_expense Excepiton : "+e+"query="+insert_expense);
	                               		 	cdb.rollDB();
	                               		 	status=false;
	                        			}
                                	}
                                	if(total_dis!=0 || total_dis_tax!=0)
                                	{
                                   		//===============insert data เพิ่มเข้าไปในระบบ=====================
                                       	expense_sign=1;
                            			expenseArr=this.GetDataExpense(expense_sign, "DF");
                                		expense_code=expenseArr[0][0];//"DD406";//ลดรายได้query from table expense
                                		expense_account_code=expenseArr[0][1];
                                		expense_note=expenseArr[0][2]+" Total_Amt="+totalDRAmt+" Total_Tax="+totalTaxAmt+" DR_AMT="+dr_sum_amt+" TAX_AMT="+tax_sum_amt+" Dis Type="+name_type+"("+value_type+")";
                                		if(num == num_sum)//doctor คนสุดท้าย ซึ่งรายได้ทั้งหมดที่เหลือจะใส่ไว้ที่ doctor code นี้ทั้่งหมด
                                		{
                                			expense_note+="(End Record)";
                                		}
                                		line_no_df="DF"+expense_code+distribute_type+g;
                                    	
                                       	String insert_ex_df="INSERT INTO TRN_EXPENSE_DETAIL (YYYY, MM, DOCTOR_CODE, HOSPITAL_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, AMOUNT, TAX_AMOUNT, TAX_TYPE_CODE, EXPENSE_ACCOUNT_CODE, NOTE, UPDATE_DATE, UPDATE_TIME) "
                                        +" VALUES('"+year+"','"+month+"','"+doctorCode+"','"+hospital_code+"'"
                                        +",'"+line_no_df+"','"+expense_code+"','"+expense_sign+"',"+total_dis+","+total_dis_tax+",'406','"+expense_account_code+"'"
                                        +",'"+expense_note+"','"+JDate.getDate()+"','"+JDate.getTime()+"')";
                                        System.out.println("DF=>Step 2:6 insert_ex_df="+insert_ex_df);
                                        
                                        try
	                                   	{
                                        	cdb.insert(insert_ex_df);
                                            cdb.commitDB();
	                                   	}
	                                   	catch(Exception e)
	                        			{
	                        				System.out.println("DF=>Step 2:6 insert_ex_df Excepiton : "+e+"query="+insert_ex_df);
	                               		 	cdb.rollDB();
	                               		 	status=false;
	                        			}
                                	}
                               		
                           		}
                               	catch(Exception e)
                    			{
                    				System.out.println("DF=>Step 2:4 sql_doctor Excepiton : "+e+"query="+sql_doctor);
                           		 	cdb.rollDB();
                           		 	status=false;
                    			}
                    		}
                    	}
                    	catch(Exception e)
                    	{	
                    		System.out.println("DF=>STEP 2:3 :sql_doctor_sum Excepiton : "+e+"query="+sql_doctor_sum);
                    		status=false;
                    	}     
                    	   
                }
                //System.out.println("Run Process Group Step 2 : INSERT REVENUE_SHARE DF Complete");
            
                      
        }
        catch(Exception e){
            System.out.println("DF=>Step 2:1 QUERY DATA BY DOCTOR PROFILE CODE Excepiton : "+e+"query="+sql);
            status=false;
        }
        
        return status;
    }   
    private boolean CalculateExpenseAbsorb()
    {
		// Step 2 : Calculate ค่า Absorb และ Insert into TRN_EXPENSE_DETAIL
    	boolean status = true;
        String sql= "";
        String[][] DoctorProfileArr = null;
      //LIST DOCTOR PROFILE
        //sql = "SELECT DOCTOR_PROFILE_CODE FROM STP_DISTRIBUTE_REVENUE GROUP BY DOCTOR_PROFILE_CODE ";
        sql="SELECT SDR.DOCTOR_PROFILE_CODE, SUM(SM.SUM_TAX_402) AS TOTAL "
        	+" FROM SUMMARY_GUARANTEE SM, STP_DISTRIBUTE_REVENUE SDR "
        	+" WHERE SM.DOCTOR_CODE=SDR.DOCTOR_CODE "
        	+" AND SM.HOSPITAL_CODE=SDR.HOSPITAL_CODE "
        	+" AND SM.HOSPITAL_CODE='"+hospital_code+"' "
        	+" AND SM.YYYY='"+year+"' "
        	+" AND SM.MM='"+month+"' "
        	+" AND SDR.REVENUE_TYPE='2' "//ABSORB
        	+" AND SDR.ACTIVE='1' "
        	+" GROUP BY SDR.DOCTOR_PROFILE_CODE ";	
    	System.out.println("ABSORB=>Query Step 2:1 : "+sql);
    	// Step 2 : Calculate ค่า DF และ Insert into TRN_EXPENSE_DETAIL
        
        try
        {
            DoctorProfileArr = cdb.query(sql);
          
               	System.out.println("==================START ABSORB============================");
                for(int i = 0; i<DoctorProfileArr.length; i++)
                {
                	System.out.println("ABSORB=>Step 2:2 : List Doctor Profile ABSORB Running to "+1+" Of "+DoctorProfileArr.length+" ON TIME "+JDate.getTime());
  			       
                	double totalDRAmt=0, totalTaxAmt=0, sum_amt_dfc=0, sum_tax_dfc=0;
                	String doctorProfileCode=DoctorProfileArr[i][0];
                	System.out.println("doctorProfileCode="+doctorProfileCode);
                	//===========Total SUM_TAX_402==============
                	totalDRAmt=Double.parseDouble(DoctorProfileArr[i][1]);
                	System.out.println("totalDRAmt="+totalDRAmt);
                	
                	String[][] doctorArr=null, doctorSumArr=null, expenseArr=null, expenseArrTax=null;
                	int num=0,num_sum=0;
                	//LIST DOCTOR CODE
                	
                	String sql_doctor_sum="SELECT DOCTOR_CODE, DISTRIBUTE_TYPE, REVENUE_TYPE, "
                	+" DISTRIBUTE_PERCENT,DISTRIBUTE_AMOUNT "
                	+" FROM STP_DISTRIBUTE_REVENUE "
                	+" WHERE HOSPITAL_CODE='"+hospital_code+"' "
                	+" AND REVENUE_TYPE='2' " //ABSORB
                	+" AND ACTIVE='1' "
                	+" AND DOCTOR_PROFILE_CODE='"+doctorProfileCode+"'"
                	+" ORDER BY DOCTOR_CODE ";
                	System.out.println("ABSORB=>STEP 2:3 :sql_doctor_sum="+sql_doctor_sum);
                    	try
                    	{
                    		doctorSumArr = cdb.query(sql_doctor_sum);
                    		sum_amt_dfc=totalDRAmt;
                    		
                    		for(int g=0 ; g < doctorSumArr.length ; g++)
                    		{
                    			double dis_percent=0,dis_amount=0,dis_amount_tax=0;
                    			String doctorCode=doctorSumArr[g][0];
                    			String distribute_type=doctorSumArr[g][1];
                        		String revenue_type=doctorSumArr[g][2];
                        		dis_percent=Double.parseDouble(doctorSumArr[g][3]);
                        		dis_amount=Double.parseDouble(doctorSumArr[g][4]);
                        		
                        		num_sum=doctorSumArr.length;
                        		num++;
                        		System.out.println("doctorCode="+doctorCode);
                        		//System.out.println("dr_sum_amt="+dr_sum_amt);
                        		System.out.println("distribute_type="+distribute_type);
                        		System.out.println("revenue_type="+revenue_type);
                        		System.out.println("dis_percent="+dis_percent);
                        		System.out.println("dis_amount="+dis_amount);
                        		System.out.println("num_sum="+num_sum);
                        		System.out.println("before num="+num);
                        		System.out.println("after num="+num);
                        		
                        		String sql_doctor = "SELECT SUM_TAX_402 FROM SUMMARY_GUARANTEE "
        	                    +" WHERE HOSPITAL_CODE='"+hospital_code+"' "
        	                    +" AND YYYY='"+year+"' "
        	                    +" AND MM='"+month+"' "
        	                    +" AND DOCTOR_CODE='"+doctorCode+"' "; 
        	                    System.out.println("ABSORB=>Step 2:4 sql_doctor="+sql_doctor);
                    			
        	                    try
                    			{
                    			    doctorArr = cdb.query(sql_doctor);
        	                        System.out.println("doctorArr="+doctorArr.length);
                                        
        	                    	double dr_sum_amt=0,total_dis=0,result_amt=0, bef_sum_amt_dfc=0;
        	                    	double tax_sum_amt=0, result_tax=0, bef_sum_tax_dfc=0, total_dis_tax=0;
        	                    	int expense_sign=0,expense_sign_tax=0;
        	                    	String expense_code="",expense_code_tax="";
        	                    	String expense_account_code="", expense_note="";
                            		String line_no="",line_no_absorb="";
        	                    	
        	                    	//System.out.println("sum_amt_dfc="+sum_amt_dfc);
                            		//ตรวจสอบว่ามี Doctor code นี้หรือไม่
        	                    	if(doctorArr.length==0)
        	                    	{
        	                    		dr_sum_amt=0;
        	                    	}
        	                    	else
        	                    	{
        	                    		dr_sum_amt=Double.parseDouble(doctorArr[0][0]);
        	                    	}
        	                    	System.out.println("dr_sum_amt="+dr_sum_amt); 
        	                    	//ตรวจสอบว่าเลือกการกระจายแบบไหน (%,amount)
        	                    	String name_type="";
        	                    	double value_type=0;
                                	if(distribute_type.equals("1"))//%
                                	{
                                		total_dis=(totalDRAmt*dis_percent)/100;
                                		name_type="%";
                                		value_type=dis_percent;
                                	}
                                	else//amount
                                	{
                                		name_type="Amount";
                                		value_type=dis_amount;
                                		bef_sum_amt_dfc=sum_amt_dfc;
                                		if(num == num_sum)//doctor คนสุดท้าย ซึ่งรายได้ทั้งหมดที่เหลือจะใส่ไว้ที่ doctor code นี้ทั้่งหมด
                                		{
                                			if(sum_amt_dfc<0 || sum_amt_dfc==0)	
                                			{	total_dis=0;    			}
                                			else   
                                			{	total_dis=bef_sum_amt_dfc;  }
                                		}
                                		else//ไม่ใช่คนสุดท้าย
                                		{
                                			if(sum_amt_dfc<0 || sum_amt_dfc==0)	//จำนวนรวมของ amount น้อยกว่าหรือเท่ากับ 0
                                			{	total_dis=0; }//จำนวนที่จะ insert ใหม่จะมีค่าเป็น 0
                                			else   
                                			{
                                				sum_amt_dfc=sum_amt_dfc-dis_amount;
                                				//amount เมื่อลบกับ dis_amount แล้วมีค่าน้อยกว่า 0 หรือเท่ากับ 0
                                    			if(sum_amt_dfc<0 || sum_amt_dfc==0)
                                    			{
                                    				total_dis=bef_sum_amt_dfc;//ให้ได้ค่าเท่ากับตัวเดิมก่อนการลบ
                                    			}
                                    			else
                                    			{
                                    				total_dis=dis_amount;//ให้เท่ากับตัวที่ลบแล้ว
                                    			}
                                			}
                                		}
                                	}
                                	if(dr_sum_amt!=0)
                                	{
	                                	//===============insert data ให้ติดลบกับจำนวนเดิมก่อน=====================
	                            		expense_sign=-1;
	                        			expenseArr=this.GetDataExpense(expense_sign, "AB");
	                            		expense_code=expenseArr[0][0];//"DD402";//ลดรายได้query from table expense
	                            		expense_account_code=expenseArr[0][1];
	                            		expense_note=expenseArr[0][2];
	                            		//expense_note=expenseArr[0][2]+" DR_AMT="+totalDRAmt+" TAX_AMT="+totalTaxAmt+" Dis Type="+name_type+"("+value_type+")";
	                            		line_no=expense_code+distribute_type+g;
	                                	
	                            		String insert_expense="INSERT INTO TRN_EXPENSE_DETAIL (YYYY, MM, DOCTOR_CODE, HOSPITAL_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, AMOUNT, TAX_AMOUNT, TAX_TYPE_CODE, EXPENSE_ACCOUNT_CODE, NOTE, UPDATE_DATE, UPDATE_TIME) "
	                                   	+" VALUES('"+year+"','"+month+"','"+doctorCode+"','"+hospital_code+"'"
	                                   	+",'"+line_no+"','"+expense_code+"','"+expense_sign+"',"+dr_sum_amt+","+dr_sum_amt+",'402','"+expense_account_code+"'"
	                                   	+",'"+expense_note+"','"+JDate.getDate()+"','"+JDate.getTime()+"')";
	                                   	System.out.println("ABSORB=>Step 2:5 insert_expense="+insert_expense);
	                                   	try
	                                   	{
	                                   		cdb.insert(insert_expense);
	                                   		cdb.commitDB();
	                                   	}
	                                   	catch(Exception e)
	                        			{
	                        				System.out.println("DF=>Step 2:5 insert_expense Excepiton : "+e+"query="+insert_expense);
	                               		 	cdb.rollDB();
	                               		 	status=false;
	                        			}
                                	}
                                	if(total_dis!=0)
                                	{
                                   		//===============insert data เพิ่มเข้าไปในระบบ=====================
                                       	expense_sign=1;
                            			expenseArr=this.GetDataExpense(expense_sign, "AB");
                                		expense_code=expenseArr[0][0];//"AD402";//เพิ่มรายได้query from table expense
                                		expense_account_code=expenseArr[0][1];
                                		expense_note=expenseArr[0][2]+" DR_AMT="+totalDRAmt+" Dis Type="+name_type+"("+value_type+")";
                                		if(num == num_sum)//doctor คนสุดท้าย ซึ่งรายได้ทั้งหมดที่เหลือจะใส่ไว้ที่ doctor code นี้ทั้่งหมด
                                		{
                                			expense_note+="(End Record)";
                                		}
                                		line_no_absorb="AB"+expense_code+distribute_type+g;
                                    	
                                       	String insert_ex_absorb="INSERT INTO TRN_EXPENSE_DETAIL (YYYY, MM, DOCTOR_CODE, HOSPITAL_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, AMOUNT, TAX_AMOUNT, TAX_TYPE_CODE, EXPENSE_ACCOUNT_CODE, NOTE, UPDATE_DATE, UPDATE_TIME) "
                                        +" VALUES('"+year+"','"+month+"','"+doctorCode+"','"+hospital_code+"'"
                                        +",'"+line_no_absorb+"','"+expense_code+"','"+expense_sign+"',"+total_dis+","+total_dis+",'402','"+expense_account_code+"'"
                                        +",'"+expense_note+"','"+JDate.getDate()+"','"+JDate.getTime()+"')";
                                        System.out.println("ABSORB=>Step 2:6 insert_ex_absorb="+insert_ex_absorb);
                                        
                                        try
	                                   	{
                                        	cdb.insert(insert_ex_absorb);
                                            cdb.commitDB();
	                                   	}
	                                   	catch(Exception e)
	                        			{
	                        				System.out.println("ABSORB=>Step 2:6 insert_ex_absorb Excepiton : "+e+"query="+insert_ex_absorb);
	                               		 	cdb.rollDB();
	                               		 	status=false;
	                        			}
                                	}
                               		
                           		}
                               	catch(Exception e)
                    			{
                    				System.out.println("ABSORB=>Step 2:4 sql_doctor Excepiton : "+e+"query="+sql_doctor);
                           		 	cdb.rollDB();
                           		 	status=false;
                    			}
                    		}
                    	}
                    	catch(Exception e)
                    	{	
                    		System.out.println("ABSORB=>STEP 2:3 :sql_doctor_sum Excepiton : "+e+"query="+sql_doctor_sum);
                    		status=false;
                    	}     
                    	   
                }
                //System.out.println("Run Process Group Step 2 : INSERT REVENUE_SHARE DF Complete");
            
                      
        }
        catch(Exception e){
            System.out.println("DF=>Step 2:1 QUERY DATA BY DOCTOR PROFILE CODE Excepiton : "+e+"query="+sql);
            status=false;
        }
        
        return status;
    }   
}