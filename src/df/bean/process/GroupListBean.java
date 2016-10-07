package df.bean.process;

import java.sql.SQLException;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;
import df.bean.db.table.TRN_Error;
import java.util.logging.Level;
import java.util.logging.Logger;
import df.bean.obj.util.*;



public class GroupListBean {
    DBConn cdb;
    
    String result = "";
    String month = "";
    String year = "";
    String hospital_code = "";
    String group_code = "";
    
    public GroupListBean(DBConn cdb){
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
    
    public boolean prepareProcess(String month, String year, String hospital_code, String process_type, String group_code){
        boolean status = true;
        this.month = month;
        this.year = year;
        this.hospital_code = hospital_code;
        this.group_code = group_code;
        System.out.println("START RUN PROCESS REVENUE SHARE");
        if(process_type.equals("List Doctor Category"))
        {
        	System.out.println("START PROCESS 1 : List Doctor Category");
            if(DeleteGroup() && InsertDataFromTrnDaily())
            {
            	if(CalculateDFPercent() && DeleteDataDf())
            	{
            		status=true;
            	}
            	//System.out.println("DELETE & INSERT DATA COMPLETE");
            	//status=true;
                //status = calculatePreviousGuarantee();
            }
        }
        
        if(process_type.equals("Calculate Revenue")){
            status = CalculateRevenueGroup();
        }
        
        if(process_type.equals("Revenue Share Prorate Doctor")){
        	//status = true;
            status = ProrateDoctorByGroup();
            //status = calculateGuaranteeStep();
        }
        System.out.println("FINISH RUN PROCESS REVENUE SHARE");
        return status;
    }

    private boolean DeleteGroup(){
        //PROCESS 1 =>Step 1:à¸¥à¸šà¸‚à¹‰à¸­à¸¡à¸¹à¸¥à¸­à¸­à¸�à¸ˆà¸²à¸�à¸•à¸²à¸£à¸²à¸‡ REVENUE_SHARE à¸•à¸²à¸¡à¹€à¸‡à¸·à¹ˆà¸­à¸™à¹„à¸‚
    	System.out.println("START PROCESS 1 STEP 1: Delete Data Group From REVENUE_SHARE");
        String sql_statement = "";
        String temp = "";
        boolean status = true;
        
        sql_statement = "DELETE FROM REVENUE_SHARE WHERE HOSPITAL_CODE='"+hospital_code+"'"+
        " AND YYYY='"+year+"' AND MM='"+month+"' AND GROUP_CODE='"+group_code+"' ";
        
                
        try{
            //get order item code is not include guarantee
            //System.out.println("Query Delete Step 1 : "+sql_statement);
            cdb.insert(sql_statement);
            System.out.println("PROCESS 1 Step 1 : Delete Group complete");
            cdb.commitDB();
            
        }catch(Exception e){
            System.out.println("PROCESS 1 Step 1 : Delete Group Error Exclude : "+e);
            //result = result+"Update order item exclude guarantee is error : "+e;
            status = false;
        }
        System.out.println("FINISH PROCESS 1 : List Doctor Category");
        return status;
    }
    
    private boolean InsertDataFromTrnDaily(){
		// PROCESS 1 =>Step 2 : insert data à¸ˆà¸²à¸�à¸•à¸²à¸£à¸²à¸‡ TRN_DAILY à¹„à¸›à¸—à¸µà¹ˆà¸•à¸²à¸£à¸²à¸‡ REVENUE_SHARE
    	System.out.println("START PROCESS 1 STEP 2: Insert Data Group to REVENUE_SHARE");
        boolean status = true;
        String sql= "",sql_insert="";
        String[][] AmountArr = null;
        int total_rd=0;
        
            sql = "SELECT DISTINCT TD.HOSPITAL_CODE, TD.YYYY, TD.MM,  SG.CODE AS GROUP_CODE, "
            +" D.DOCTOR_PROFILE_CODE, TD.DOCTOR_CODE, SGIC.DOCTOR_CATEGORY_CODE, "
            +" SGIC.ORDER_ITEM_CATEGORY_CODE, TD.AMOUNT_AFT_DISCOUNT, TD.DR_AMT, "
            +" ((TD.DR_AMT*100)/TD.AMOUNT_AFT_DISCOUNT) AS DF_PERCENT,  "
            +" SGDC.DF AS PERCENT_DF, SGDC.POOL AS PERCENT_POOL, "
            +" SGDC.CK AS PERCENT_CK, TD.LINE_NO, TD.INVOICE_NO  "
            +" FROM TRN_DAILY TD,STP_GROUP SG, STP_GROUP_DOCTOR_CATEGORY SGDC,"
            +" dbo.STP_GROUP_ITEM_CATEGORY SGIC, DOCTOR D"
            +" WHERE SG.CODE=SGDC.GROUP_CODE "
            +" AND TD.HOSPITAL_CODE=SGDC.HOSPITAL_CODE "
            +" AND SGIC.HOSPITAL_CODE=D.HOSPITAL_CODE "
            +" AND SG.HOSPITAL_CODE=TD.HOSPITAL_CODE "
            +" AND SGDC.DOCTOR_CATEGORY_CODE=SGIC.DOCTOR_CATEGORY_CODE "
            +" AND TD.DOCTOR_CATEGORY_CODE=SGDC.DOCTOR_CATEGORY_CODE "
            +" AND TD.ORDER_ITEM_CATEGORY_CODE=SGIC.ORDER_ITEM_CATEGORY_CODE"
            +" AND D.CODE=TD.DOCTOR_CODE"
            +" AND D.DOCTOR_CATEGORY_CODE=SGIC.DOCTOR_CATEGORY_CODE "
            +" AND TD.HOSPITAL_CODE='"+hospital_code+"'  "
            +" AND SG.CODE='"+group_code+"'  "
            +" AND TD.YYYY='"+year+"'  "
            +" AND TD.MM='"+month+"' "
            +" AND TD.DR_AMT <> 0 "
            +" AND TD.AMOUNT_AFT_DISCOUNT <> 0 "
            +" ORDER BY D.DOCTOR_PROFILE_CODE, TD.DOCTOR_CODE, SGIC.DOCTOR_CATEGORY_CODE,SGIC.ORDER_ITEM_CATEGORY_CODE ";
            System.out.println("PROCESS 1 Step 2:1 : "+sql);
            try{
            	AmountArr = cdb.query(sql);
            	total_rd=AmountArr.length;
            	double expense_amt=0, expense_amount=0, expense_sum=0;
            	String[][] ExpenseArr = null;
                int total_ex=0, expense_sign=0;
                for(int i = 0; i<total_rd; i++)
                {
                	String sql_expense="SELECT EXPENSE_SIGN, AMOUNT "
                	+" FROM TRN_EXPENSE_DETAIL "
                	+" WHERE DOCTOR_CODE='"+AmountArr[i][5]+"'"
                	+" AND EXPENSE_CODE='602102' AND HOSPITAL_CODE='"+hospital_code+"' "
                	+" AND YYYY='"+year+"'  "
                	+" AND MM='"+month+"' ";
                	ExpenseArr = cdb.query(sql_expense);
                	total_ex=ExpenseArr.length;
                	for(int t = 0; t<total_ex; t++)
                	{
                		expense_sign=Integer.parseInt(ExpenseArr[t][0]);
                		expense_amount=Double.parseDouble(ExpenseArr[t][1]);
                		expense_sum=expense_sign*expense_amount;
                		expense_amt=expense_amt+expense_sum;
                	}
                	sql_insert = "INSERT INTO REVENUE_SHARE(HOSPITAL_CODE, YYYY, MM, GROUP_CODE, DOCTOR_PROFILE_CODE, "
                    	+" DOCTOR_CODE, DOCTOR_CATEGORY_CODE, ORDER_ITEM_CATEGORY_CODE, AMOUNT_AFT_DISCOUNT, DR_AMT, "
                    	+" DF_PERCENT, PERCENT_DF, PERCENT_POOL, PERCENT_CK, LINE_NO, INVOICE_NO, EXPENSE_AMT, CREATE_DATE, "
                    	+" CREATE_TIME ) VALUES("
                    	+" '"+AmountArr[i][0]+"'" //HOSPITAL_CODE
                    	+" ,'"+AmountArr[i][1]+"'" //YYYY
                    	+" ,'"+AmountArr[i][2]+"'" //MM
                    	+" ,'"+AmountArr[i][3]+"'" //GROUP_CODE
                    	+" ,'"+AmountArr[i][4]+"'" //DOCTOR_PROFILE_CODE
                    	+" ,'"+AmountArr[i][5]+"'" //DOCTOR_CODE
                    	+" ,'"+AmountArr[i][6]+"'" //DOCTOR_CATEGORY_CODE
                    	+" ,'"+AmountArr[i][7]+"'" //ORDER_ITEM_CATEGORY_CODE
                    	+" ,"+AmountArr[i][8] //AMOUNT_AFT_DISCOUNT
                    	+" ,"+AmountArr[i][9] //DR_AMT
                    	+" ,"+AmountArr[i][10] //DF_PERCENT
                    	+" ,"+AmountArr[i][11] //PERCENT_DF
                    	+" ,"+AmountArr[i][12] //PERCENT_POOL
                    	+" ,"+AmountArr[i][13] //PERCENT_CK
                    	+" ,'"+AmountArr[i][14]+"'" //LINE_NO
                    	+" ,'"+AmountArr[i][15]+"'" //INVOICE_NO    
                    	+" ,"+expense_amt //EXPENSE_AMT
                    	+" ,'"+JDate.getDate()+"','"+JDate.getTime()+"') ";
                    	
                    //System.out.println("Query Step 2:2 : "+sql_insert);
                	try
                	{
                		cdb.insert(sql_insert);
                		//System.out.println("PROCESS 1 Step 2:2 Insert Data to REVENUE_SHARE Running to "+i+" Of "+total_rd+" ON TIME "+JDate.getTime());
                		cdb.commitDB();
                	}
                	catch(Exception e)
                	{
                		System.out.println("PROCESS 1 Step 2:2 Insert Data to REVENUE_SHARE Exception : "+e+" query="+sql_insert);
                		cdb.rollDB();
                	} 
                }
            }
            catch(Exception e)
            {
            	System.out.println("PROCESS 1 Step 2:1 Query Data Excepiton : "+e+" query="+sql);
            }
            System.out.println("FINISH PROCESS 1 Step 2 : Insert Data Group to REVENUE_SHARE");
            return status;
    }   
    private boolean CalculateDFPercent(){
        //PROCESS 1 =>Step 3 : à¸¥à¸šà¸‚à¹‰à¸­à¸¡à¸¹à¸¥à¸ˆà¸²à¸�à¸•à¸²à¸£à¸²à¸‡ REVENUE_SHARE à¸•à¸²à¸¡à¹€à¸‡à¸·à¹ˆà¸­à¸™à¹„à¸‚ DF_PERCENT<>PERCENT_DF
		System.out.println("START PROCESS 1 STEP 3: Calculate Percent 40.5 From REVENUE_SHARE ");
        String sql_statement = "",sql_revenue="",note="", invoice_no="",line_no="";
        String[][] AmountArr = null;
        boolean status = true;
        int total_rd=0;
        sql_revenue = "SELECT DOCTOR_CODE,AMOUNT_AFT_DISCOUNT,INVOICE_NO,LINE_NO FROM REVENUE_SHARE "
        	+" WHERE HOSPITAL_CODE='"+hospital_code+"' AND YYYY='"+year+"' "
        	+" AND MM='"+month+"' AND GROUP_CODE='"+group_code+"'"
        	+" AND DF_PERCENT = 40.5 AND PERCENT_DF = 45 ";
        try{
        	 AmountArr = cdb.query(sql_revenue);
        	 total_rd=AmountArr.length;
        	 double AmtAftDis=0, AmtBefDis=0,bd_befDis=0;
        	 String PerdoctorCode="";
             for(int i = 0; i<total_rd; i++)
             {
            	 PerdoctorCode=AmountArr[i][0];
            	 System.out.println("DoctorCode="+PerdoctorCode);
            	 AmtAftDis=Double.parseDouble(AmountArr[i][1]);
            	 invoice_no=AmountArr[i][2];
            	 line_no=AmountArr[i][3];
            	 AmtBefDis=(AmtAftDis*90)/100;
            	 
            	 bd_befDis=Double.parseDouble(JNumber.getSaveMoney(AmtBefDis));
                 
            	 note="Amount After Discount="+AmtAftDis+" and DF Percent=40.5";
            	 sql_statement="UPDATE REVENUE_SHARE SET "
            	 +" AMOUNT_AFT_DISCOUNT="+bd_befDis+", "
            	 +" DF_PERCENT=45, NOTE='"+note+"'"
            	 +" WHERE DOCTOR_CODE='"+PerdoctorCode+"' "
            	 +" AND HOSPITAL_CODE='"+hospital_code+"' "
            	 +" AND YYYY='"+year+"' "
            	 +" AND MM='"+month+"' "
            	 +" AND GROUP_CODE='"+group_code+"'"
            	 +" AND INVOICE_NO='"+invoice_no+"'"
            	 +" AND LINE_NO='"+line_no+"'"
            	 +" AND DF_PERCENT = 40.5 "
            	 +" AND PERCENT_DF = 45 ";
            	 try{
		            //get order item code is not include guarantee
		            //System.out.println("Query Delete Step 3 : "+sql_statement);
		            cdb.insert(sql_statement);
		            System.out.println("PROCESS 1 Step 3 : UPDATE Data to REVENUE_SHARE Running to "+i+" Of "+total_rd+" ON TIME "+JDate.getTime());
		            cdb.commitDB();
	            }catch(Exception e){
		            System.out.println("PROCESS 1 Step 3 : UPDATE Data to REVENUE_SHARE Exclude : "+e+" QUERY="+sql_statement);
		            cdb.rollDB();
		            status = false;
	            }
             }
        }
        catch(Exception e)
        {
        	System.out.println("PROCESS 1 Step 3 : Select DF_PERCENT=40.5 AND PERCENT_DF=45 Exclude : "+e+" QUERY="+sql_revenue);
        	status = false;
        }
        return status;
    }
	private boolean DeleteDataDf(){
        //PROCESS 1 =>Step 4 : à¸¥à¸šà¸‚à¹‰à¸­à¸¡à¸¹à¸¥à¸ˆà¸²à¸�à¸•à¸²à¸£à¸²à¸‡ REVENUE_SHARE à¸•à¸²à¸¡à¹€à¸‡à¸·à¹ˆà¸­à¸™à¹„à¸‚ DF_PERCENT<>PERCENT_DF
		System.out.println("START PROCESS 1 STEP 4: Delete Data Group From REVENUE_SHARE CONDITION=> DF_PERCENT<>PERCENT_DF");
        String sql_statement = "";
        boolean status = true;
        
        sql_statement = "DELETE FROM REVENUE_SHARE WHERE HOSPITAL_CODE='"+hospital_code+"'"+
        " AND YYYY='"+year+"' AND MM='"+month+"' AND GROUP_CODE='"+group_code+"' AND DF_PERCENT <> PERCENT_DF   ";//AND DF_PERCENT<> 40.5
         try{
            //get order item code is not include guarantee
            //System.out.println("Query Delete Step 3 : "+sql_statement);
            cdb.insert(sql_statement);
            System.out.println("PROCESS 1 Step 4 : Delete DF_PERCENT<>PERCENT_DF complete");
            cdb.commitDB();
            
        }catch(Exception e){
            System.out.println("PROCESS 1 Step 4 : Delete DF_PERCENT<>PERCENT_DF Exclude : "+e+" query="+sql_statement);
            cdb.rollDB();
            status = false;
        }
        System.out.println("FINISH PROCESS 1 STEP 4: Delete Data Group From REVENUE_SHARE CONDITION=> DF_PERCENT<>PERCENT_DF");
        return status;
    }

    private boolean CalculateRevenueGroup()
    {
    	//PROCESS 2 =>à¸„à¸³à¸™à¸§à¸“à¸„à¹ˆà¸² POOL ,CK à¸•à¸²à¸¡ % à¸—à¸µà¹ˆà¸�à¸³à¸«à¸™à¸”à¹„à¸§à¹‰
    	System.out.println("START PROCESS 2 : Calculate Revenue");
        
        boolean status = true;
        String[][] listRevenue = null;
        String sql_statement = "";
        String sql_update = "";
        
        int t = 0;
        int total_revenue=0;
    
           sql_statement = "SELECT DOCTOR_CATEGORY_CODE, ORDER_ITEM_CATEGORY_CODE "
           +" ,DOCTOR_PROFILE_CODE, DOCTOR_CODE, LINE_NO, AMOUNT_AFT_DISCOUNT "
           +" ,PERCENT_DF, PERCENT_POOL, PERCENT_CK "
           +" FROM REVENUE_SHARE "
           +" WHERE HOSPITAL_CODE='"+hospital_code+"' AND YYYY='"+year+"' "
           +" AND MM='"+month+"' AND GROUP_CODE='"+group_code+"'";
           System.out.println("sql_statement="+sql_statement);
           try
           {
            	listRevenue = cdb.query(sql_statement);
                total_revenue = listRevenue.length;
                for(int i = 0; i<total_revenue; i++)
                {
                	String doctor_category_code = listRevenue[i][0];
                	String order_item_category_code = listRevenue[i][1];
                	String doctor_profile_code = listRevenue[i][2];
                    String doctor_code = listRevenue[i][3];
                    String line_no = listRevenue[i][4];
                    double amount_aft_dis = Double.parseDouble(listRevenue[i][5]);
                    double p_df = Double.parseDouble(listRevenue[i][6]);
                    double p_pool = Double.parseDouble(listRevenue[i][7]);
                    double p_ck = Double.parseDouble(listRevenue[i][8]);
                    
                    double total_df=0, total_pool=0, total_ck=0, bd_df=0, bd_pool=0, bd_ck=0;
                    
                    total_df=(amount_aft_dis*p_df)/100;
                    total_pool=(amount_aft_dis*p_pool)/100;
                    total_ck=(amount_aft_dis*p_ck)/100;
                    System.out.println("==================================");
                    System.out.println("amount_aft_dis="+amount_aft_dis+", p_df="+p_df+", p_pool="+p_pool+", p_ck="+p_ck);
                    System.out.println("Before total df="+total_df+", pool="+total_pool+", ck="+total_ck);
                    
                    bd_df=Double.parseDouble(JNumber.getSaveMoney(total_df));
                    bd_pool=Double.parseDouble(JNumber.getSaveMoney(total_pool));
                    bd_ck=Double.parseDouble(JNumber.getSaveMoney(total_ck));
                    
                    System.out.println("After total df="+bd_df+", pool="+bd_pool+", ck="+bd_ck);
                    
                    sql_update = "UPDATE REVENUE_SHARE SET "
                    	+" AMOUNT_DF="+bd_df
                    	+" ,AMOUNT_POOL="+bd_pool
                    	+" ,AMOUNT_CK="+bd_ck
                    	+" WHERE HOSPITAL_CODE = '" + hospital_code + "' " 
                        +" AND YYYY='"+year+"'"
                        +" AND MM='"+month+"'"
                        +" AND DOCTOR_CATEGORY_CODE='"+doctor_category_code+"'"
                        +" AND ORDER_ITEM_CATEGORY_CODE='"+order_item_category_code+"'"
                        +" AND DOCTOR_PROFILE_CODE='"+doctor_profile_code+"'"
                        +" AND DOCTOR_CODE='"+doctor_code+"'"
                        +" AND LINE_NO='"+line_no+"'";
                        
                    //System.out.println("sql_update="+sql_update);
                    try
                    {
                    	//System.out.println("PROCESS 2 : Calculate Revenue POOL, CK Running to "+i+" Of "+total_revenue+" ON TIME "+JDate.getTime());
                    	cdb.insert(sql_update);
                    	cdb.commitDB();
                    }
                    catch(Exception e)
                    {
                        System.out.println("PROCESS 2 : Calculate Revenue POOL, CK : "+e+" query="+sql_update);
                        cdb.rollDB();
                        status = false;
                    }
                } 
           } 
           catch (Exception ex) 
           {
        	   System.out.println("PROCESS 2 : QUERY DATA REVENUE_SHARE Error Exception : "+ex+" query="+sql_statement);
        	   status = false;
           }
           System.out.println("FINISH PROCESS 2 : Calculate Revenue");
           return status;
    }
    
    private boolean ProrateDoctorByGroup()
    {
    	System.out.println("START PROCESS 3 : Revenue Share Prorate Doctor");
    	
    	//PROCESS 3 =>à¹�à¸šà¹ˆà¸‡à¸£à¸²à¸¢à¹„à¸”à¹‰à¸•à¸²à¸¡ Doctor Code à¸—à¸µà¹ˆ set à¹€à¸­à¸²à¹„à¸§à¹‰
    	
        boolean status = true;
        String[][] sumArr = null;
        String[][] doctorPoolArr = null;
        String[][] doctorCKArr = null;
        String sql_sum = "",sql_doctor="",sql_doctor_pool="",sql_doctor_ck="";
        double sum_pool = 0;
        double sum_ck = 0;
        double amount_doctor = 0;
        int num_pool = 0, num_ck=0;
        //Delete data from REVENUE_SHARE_PRORATE_DOCTOR
        String delete_prorate = "DELETE FROM REVENUE_SHARE_PRORATE_DOCTOR "
        +" WHERE HOSPITAL_CODE='"+hospital_code+"' AND YYYY='"+year+"' "
        +" AND MM='"+month+"' AND GROUP_CODE='"+group_code+"'";
        System.out.println("delete_prorate="+delete_prorate);
        
        try 
        {
        	cdb.insert(delete_prorate);
            cdb.commitDB();
        }
        catch (Exception ex) 
        {
            System.out.println("PROCESS 3 : DELETE REVENUE_SHARE_PRORATE_DOCTOR => Exception : "+ex+" query="+delete_prorate);
            cdb.rollDB();
            status = false;
        }
        //à¸«à¸²à¸ˆà¸³à¸™à¸§à¸™ à¸£à¸§à¸¡ POOL, CK
        sql_sum = "SELECT SUM((AMOUNT_AFT_DISCOUNT*PERCENT_POOL)/100) AS TOTAL_POOL, SUM((AMOUNT_AFT_DISCOUNT*PERCENT_CK)/100) AS TOTAL_CK FROM REVENUE_SHARE "
        +" WHERE HOSPITAL_CODE='"+hospital_code+"'"
        +" AND YYYY='"+year+"' "
        +" AND MM='"+month+"' "
        +" AND GROUP_CODE='"+group_code+"'";
        System.out.println("sql_sum="+sql_sum);
        
        try 
        {
        	sumArr = cdb.query(sql_sum);
	        sum_pool=Double.parseDouble(sumArr[0][0]);
	        sum_ck=Double.parseDouble(sumArr[0][1]);
	        System.out.println("POOL="+sum_pool);
	        System.out.println("CK="+sum_ck);
	        //à¸«à¸² Doctor Code
	        sql_doctor = "SELECT DOCTOR_CODE FROM STP_PRORATE_DOCTOR "
	        	+" WHERE HOSPITAL_CODE='"+hospital_code+"'"
	        	+" AND GROUP_CODE='"+group_code+"'";
	        
	        //POOL
	        sql_doctor_pool=sql_doctor+" AND TYPE='POOL'";
	        try
	        {
	        	doctorPoolArr = cdb.query(sql_doctor_pool);
		        num_pool = doctorPoolArr.length;
		        amount_doctor=Double.parseDouble(JNumber.getSaveMoney(sum_pool/num_pool));
		        
		        for(int i = 0; i<doctorPoolArr.length; i++)
		        {
			       String doctor_code = doctorPoolArr[i][0];
			       String insert_doctor_pool = "INSERT INTO REVENUE_SHARE_PRORATE_DOCTOR "
	               +" ( HOSPITAL_CODE, YYYY, MM, GROUP_CODE, DOCTOR_CODE, TYPE, PRORATE_AMOUNT, CREATE_DATE, CREATE_TIME)"
	               +" VALUES('"+hospital_code+"','"+year+"','"+month+"','"+group_code+"' "
	               +" ,'"+doctor_code+"','POOL',"+amount_doctor
	               +" ,'"+JDate.getDate()+"','"+JDate.getTime()+"') ";
	               //System.out.println("insert_doctor_pool="+insert_doctor_pool);
			       try
			       {
			    	   cdb.insert(insert_doctor_pool);
			    	   cdb.commitDB();
			    	   System.out.println("PROCESS 3 : Insert POOL Running to "+i+" Of "+num_pool+" ON TIME "+JDate.getTime());
			       }
			       catch(Exception e)
			       {
			    	   System.out.println("PROCESS 3 : Insert POOL of group code ="+group_code+"=> Exception : "+e+" query="+insert_doctor_pool);
			           cdb.rollDB();
			           status = false;
			       }
				       
			    }   
		        
	        }
	        catch(Exception e)
		    {
		    	System.out.println("PROCESS 3 : Query POOL of group code ="+group_code+"=> Exception : "+e+" query="+sql_doctor_pool);
		        status = false;
		    }
	      //CK
	        sql_doctor_ck=sql_doctor+" AND TYPE='CK'";
	        try
	        {
	        	doctorCKArr = cdb.query(sql_doctor_ck);
		        num_ck = doctorCKArr.length;
		        amount_doctor=Double.parseDouble(JNumber.getSaveMoney(sum_ck/num_ck));
		        for(int i = 0; i<doctorCKArr.length; i++)
		        {
			       String doctor_code = doctorCKArr[i][0];
			       String insert_doctor_ck = "INSERT INTO REVENUE_SHARE_PRORATE_DOCTOR "
	               +" ( HOSPITAL_CODE, YYYY, MM, GROUP_CODE, DOCTOR_CODE, TYPE, PRORATE_AMOUNT, CREATE_DATE, CREATE_TIME)"
	               +" VALUES('"+hospital_code+"','"+year+"','"+month+"','"+group_code+"' "
	               +" ,'"+doctor_code+"','CK',"+amount_doctor
	               +" ,'"+JDate.getDate()+"','"+JDate.getTime()+"') ";
	               //System.out.println("insert_doctor_ck="+insert_doctor_ck);
			       try
			       {
			    	   cdb.insert(insert_doctor_ck);
			    	   cdb.commitDB();
			    	   System.out.println("PROCESS 3 : Insert CK Running to "+i+" Of "+num_ck+" ON TIME "+JDate.getTime());
			       }
			       catch(Exception e)
			       {
			    	   System.out.println("PROCESS 3 : Insert POOL of group code ="+group_code+"=> Exception : "+e+" query="+insert_doctor_ck);
			           cdb.rollDB();
			           status = false;
			       }
			    }   
		    }
	        catch (Exception ex) 
	        {
	            System.out.println("PROCESS 3 : Query CK of group code ="+group_code+"=> Exception : "+ex+" query="+sql_doctor_ck);
	            status = false;
	        }
        }
        catch (Exception ex) 
        {
            System.out.println("PROCESS 3 : Query Sum POOL&CK of group code= "+group_code+" => Exception : "+ex+" query="+sql_sum);
            status = false;
        }
	        System.out.println("FINISH PROCESS 3 : Revenue Share Prorate Doctor");
	        return status;
    
    }
   
}