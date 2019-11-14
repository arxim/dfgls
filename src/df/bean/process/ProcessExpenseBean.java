package df.bean.process;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import df.bean.obj.util.JNumber;
import df.bean.db.table.TRN_Error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import df.bean.db.table.Batch;
import df.bean.process.ProcessUtil;

public class ProcessExpenseBean {
    DBConn cdb;
    
    String result = "";
    String month = "";
    String year = "";
    String hospital_code = "";
    String expense_code_get = "";
    int expense_sign_get=0;
    String expense_type_get="";
    String user="";
    
    public ProcessExpenseBean(DBConn cdb){
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
    public void setUser(String user){
    	this.user = user;
    }
    
    public String getMessage(){
        return this.result;
    }
    
    public boolean prepareProcess(String month, String year, String hospital_code, String process_type){
        boolean status = true,Process1=true, Process2=true;
        this.month = month;
        this.year = year;
        this.hospital_code = hospital_code;
        System.out.println("START RUN PROCESS Expense Distribute");
		 if(process_type.equals("Calculate Expense Period"))
	        {
	        	
	        		if(!(CalculateExpensePeriod()))
	        		{
	        			status=false;
	        		}
	        	
	        }
          if(process_type.equals("Calculate Expense Distribute"))
	        {
	        	if(DeleteDataSummaryMonthlyExp())
	        	{
	        		if(!(CalculateExpense() && CalculateExpenseTax()))
	        		{
	        			status=false;
	        		}
	        	}
        	  //Process2=true;
	        }
			//if(Process1 != Process2)
			//{
				//status=false;
			//}
         return status;
    }
    
    
    public ArrayList<HashMap<String, String>> CalculateExpenseCarryForward(String month,String year,String hospital_code){
    	ArrayList<HashMap<String,String>> listDr = new ArrayList<HashMap<String,String>>();
    	ArrayList<HashMap<String,String>> listEx = new ArrayList<HashMap<String,String>>();
    	ArrayList<HashMap<String,String>> listExCarryForward = new ArrayList<HashMap<String,String>>();
    	NumberFormat formatter = new DecimalFormat("00");  
    	String sql_dr = "SELECT YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, SUM(DR_AMT) AS AMT FROM ( "
    			+ "SELECT TRN_DAILY.YYYY, TRN_DAILY.MM, TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.DOCTOR_CODE, SUM(DR_AMT) AS DR_AMT "
    			+ " FROM TRN_DAILY  "
    			+ " LEFT OUTER JOIN ORDER_ITEM ON ORDER_ITEM_CODE = CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "
    			+ " LEFT OUTER JOIN DOCTOR ON DOCTOR_CODE = DOCTOR.CODE AND TRN_DAILY.HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE "
    			+ " WHERE TRN_DAILY.HOSPITAL_CODE = '"+hospital_code+"' AND (TRN_DAILY.BATCH_NO = '' OR TRN_DAILY.BATCH_NO IS NULL)  "
    			+ " AND TRN_DAILY.YYYY = '"+year+"' AND TRN_DAILY.MM = '"+month+"'  "
    			+ " AND IS_PAID = 'Y' AND TRN_DAILY.ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' AND DOCTOR.ACTIVE = '1' "
    			+ " GROUP BY TRN_DAILY.YYYY, TRN_DAILY.MM, TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.DOCTOR_CODE "
    			+ " UNION ALL "
    			+ " SELECT YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, SUM(AMOUNT*EXPENSE_SIGN) AS DR_AMT "
    			+ " FROM TRN_EXPENSE_DETAIL "
    			+ " WHERE HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+year+"' AND MM = '"+month+"' "
    			+ " GROUP BY YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE "
    			+ " ) AS A "
    			+ " GROUP BY YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE HAVING SUM(DR_AMT)< 0 "
    			+ " ORDER BY A.DOCTOR_CODE ";
    	listDr = cdb.listQueryData(sql_dr);
    	//System.out.println(listDr);
    	for(int i=0;i<listDr.size();i++){
    		
    		   String  sql_ex = "SELECT *,AMOUNT AS OLD_AMOUNT "
    		   		+ " FROM TRN_EXPENSE_DETAIL EXD INNER JOIN EXPENSE EX ON EXD.EXPENSE_CODE = EX.CODE"
    		   		+ " WHERE EXD.DOCTOR_CODE='"+listDr.get(i).get("DOCTOR_CODE")+"' AND EXD.YYYY+EXD.MM='"+year+month+"' AND EXD.HOSPITAL_CODE='"+hospital_code+"'"
    		   		+ " AND EXD.AMOUNT > 0 AND EXD.EXPENSE_SIGN='-1'  ORDER BY DOC_DATE,AMOUNT ASC";
    		   listEx = cdb.listQueryData(sql_ex);
    		   //System.out.println(listEx);
    		   double drAmtValue = Double.parseDouble(listDr.get(i).get("AMT"));
    		   double sumExpAmt=0;
    		   for(int j =0;j< listEx.size();j++){
    			   sumExpAmt += Double.parseDouble(listEx.get(j).get("AMOUNT"));
    		   } 
    		   double tempDrAmt = sumExpAmt+drAmtValue;
    		   System.out.println("tempDrAmt = "+sumExpAmt+"+"+drAmtValue);
    		   for(int k =0;k< listEx.size();k++){
    			   double expAmt = Double.parseDouble(listEx.get(k).get("AMOUNT"));
    			   	  HashMap<String, String> data1 = new HashMap<String, String>();
    			   	  data1.put("YYYY",listEx.get(k).get("YYYY"));
	    			  data1.put("MM", listEx.get(k).get("MM"));
	    			  data1.put("EMPLOYEE_ID", listEx.get(k).get("EMPLOYEE_ID"));
	      			  data1.put("DOCTOR_CODE",listEx.get(k).get("DOCTOR_CODE"));
	      			  data1.put("HOSPITAL_CODE",listEx.get(k).get("HOSPITAL_CODE"));
	      			  data1.put("LINE_NO",listEx.get(k).get("LINE_NO"));
	      			  data1.put("EXP_CODE",listEx.get(k).get("EXPENSE_CODE"));
	      			  data1.put("EXP_DESP", listEx.get(k).get("DESCRIPTION"));
	      			  data1.put("EXPENSE_SIGN",listEx.get(k).get("EXPENSE_SIGN"));
	      			  data1.put("EXPENSE_ACCOUNT_CODE",listEx.get(k).get("EXPENSE_ACCOUNT_CODE"));
	      			  data1.put("AMT", listEx.get(k).get("AMOUNT"));
	    			  data1.put("TAX_AMT",listEx.get(k).get("TAX_AMOUNT"));
	    			  data1.put("UPDATE_DATE",listEx.get(k).get("UPDATE_DATE"));
	      			  data1.put("UPDATE_TIME",listEx.get(k).get("UPDATE_TIME"));
	      			  data1.put("USER_ID", listEx.get(k).get("USER_ID"));
	    			  data1.put("SUPPLIER_CODE",listEx.get(k).get("SUPPLIER_CODE"));
	    			  data1.put("INVOICE_TYPE_DESCRIPTION",listEx.get(k).get("INVOICE_TYPE_DESCRIPTION"));
	      			  data1.put("DOC_NO",listEx.get(k).get("DOC_NO"));
	      			  data1.put("DOC_DATE", listEx.get(k).get("DOC_DATE"));
	    			  data1.put("NOTE",listEx.get(k).get("NOTE"));
	    			  data1.put("TAX_TYPE_CODE",listEx.get(k).get("TAX_TYPE_CODE"));
	      			  data1.put("COMPUTE_TAX_TYPE",listEx.get(k).get("COMPUTE_TAX_TYPE"));
	      			  data1.put("DEPARTMENT_CODE", listEx.get(k).get("DEPARTMENT_CODE"));
	    			  data1.put("LOCATION_CODE",listEx.get(k).get("LOCATION_CODE"));
	    			 // data1.put("TAX_TYPE_CODE",listEx.get(k).get("PAYMENT_DATE"));
	    			  data1.put("TAX_TYPE_CODE",listEx.get(k).get("TAX_TYPE_CODE"));
	      			 // data1.put("COMPUTE_TAX_TYPE",listEx.get(k).get("BATCH_NO"));
	    			  data1.put("COMPUTE_TAX_TYPE",listEx.get(k).get("COMPUTE_TAX_TYPE"));
	      			  data1.put("DEPARTMENT_CODE", listEx.get(k).get("DEPARTMENT_CODE"));
	    			  data1.put("LOCATION_CODE",listEx.get(k).get("LOCATION_CODE"));
	    			  data1.put("ACT", "0");
	    			  data1.put("OLD_AMOUNT",listEx.get(k).get("OLD_AMOUNT"));
	    			  data1.put("PAYMENT_DATE",listEx.get(k).get("PAYMENT_DATE"));
	    			  data1.put("BATCH_NO",listEx.get(k).get("BATCH_NO"));
	    			  data1.put("OLD_DOCTOR",listEx.get(k).get("OLD_DOCTOR"));
    			   if(tempDrAmt > 0){
	    				  if(tempDrAmt >= expAmt){
	 	        			  tempDrAmt= tempDrAmt - expAmt;
	 	        			// System.out.println("ËÑ¡ä´é·Ñé§ËÁ´ : ËÑ¡ä»áÅéÇ"+listEx.get(k).get("AMOUNT")+"  àËÅ×Í·ÕèËÑ¡ä´é  :"+tempDrAmt);
	 	    			  }else {
	 	    				//put original  
	 	    				  double percentTaxExp= (tempDrAmt/Double.parseDouble(listEx.get(k).get("AMOUNT")));
	 	    				  System.out.println("percentTaxExp"+tempDrAmt+"/"+Double.parseDouble(listEx.get(k).get("AMOUNT")));
	 	    				  listExCarryForward.add(data1);
	 		    			  HashMap<String, String> data2 = new HashMap<String, String>();
		        			  data2.put("YYYY",listEx.get(k).get("YYYY"));
			    			  data2.put("MM", listEx.get(k).get("MM"));
			    			  data2.put("EMPLOYEE_ID", listEx.get(k).get("EMPLOYEE_ID"));
			      			  data2.put("DOCTOR_CODE",listEx.get(k).get("DOCTOR_CODE"));
			      			  data2.put("HOSPITAL_CODE",listEx.get(k).get("HOSPITAL_CODE"));
			      			  data2.put("LINE_NO",listEx.get(k).get("LINE_NO"));
			      			  data2.put("EXP_CODE",listEx.get(k).get("EXPENSE_CODE"));
			      			  data2.put("EXP_DESP", listEx.get(k).get("DESCRIPTION"));
			      			  data2.put("EXPENSE_SIGN",listEx.get(k).get("EXPENSE_SIGN"));
			      			  data2.put("EXPENSE_ACCOUNT_CODE",listEx.get(k).get("EXPENSE_ACCOUNT_CODE"));
			      			  data2.put("AMT",""+ JNumber.showDouble(tempDrAmt, 2));
			    			  //data2.put("TAX_AMT",""+tempDrAmt);
			      			  data2.put("TAX_AMT",""+(Double.parseDouble(listEx.get(k).get("TAX_AMOUNT"))==0?0 : percentTaxExp*Double.parseDouble(listEx.get(k).get("TAX_AMOUNT"))));
			    			  data2.put("UPDATE_DATE",listEx.get(k).get("UPDATE_DATE"));
			      			  data2.put("UPDATE_TIME",listEx.get(k).get("UPDATE_TIME"));
			      			  data2.put("USER_ID", listEx.get(k).get("USER_ID"));
			    			  data2.put("SUPPLIER_CODE",listEx.get(k).get("SUPPLIER_CODE"));
			    			  data2.put("INVOICE_TYPE_DESCRIPTION",listEx.get(k).get("INVOICE_TYPE_DESCRIPTION"));
			      			  data2.put("DOC_NO",listEx.get(k).get("DOC_NO"));
			      			  data2.put("DOC_DATE", listEx.get(k).get("DOC_DATE"));
			    			  data2.put("NOTE",listEx.get(k).get("NOTE"));
			    			  data2.put("TAX_TYPE_CODE",listEx.get(k).get("TAX_TYPE_CODE"));
			      			  data2.put("COMPUTE_TAX_TYPE",listEx.get(k).get("COMPUTE_TAX_TYPE"));
			      			  data2.put("DEPARTMENT_CODE", listEx.get(k).get("DEPARTMENT_CODE"));
			    			  data2.put("LOCATION_CODE",listEx.get(k).get("LOCATION_CODE"));
			    			  data2.put("TAX_TYPE_CODE",listEx.get(k).get("TAX_TYPE_CODE"));
			      			  data2.put("COMPUTE_TAX_TYPE",listEx.get(k).get("BATCH_NO"));
			      			  data2.put("DEPARTMENT_CODE", listEx.get(k).get("DEPARTMENT_CODE"));
			    			  data2.put("LOCATION_CODE",listEx.get(k).get("LOCATION_CODE"));
			    			  data2.put("ACT", "1");
			    			  data2.put("EXP_DESP", listEx.get(k).get("DESCRIPTION"));
			    			  data2.put("OLD_AMOUNT",listEx.get(k).get("OLD_AMOUNT"));
			    			  data2.put("PAYMENT_DATE",listEx.get(k).get("PAYMENT_DATE"));
			    			  data2.put("BATCH_NO",listEx.get(k).get("BATCH_NO"));
			    			  data2.put("OLD_DOCTOR",listEx.get(k).get("OLD_DOCTOR"));
		        			  double someExpCarryForword = Double.parseDouble(listEx.get(k).get("AMOUNT"))-tempDrAmt ;
		        			  double percentTaxSomeExp= someExpCarryForword/Double.parseDouble(listEx.get(k).get("AMOUNT"));
		        			  //System.out.println("percentTaxSomeExp : "+someExpCarryForword+"/"+Double.parseDouble(listEx.get(k).get("AMOUNT")));
		        			  HashMap<String, String> data3 = new HashMap<String, String>();
		        			  data3.put("YYYY",""+((Integer.parseInt(listEx.get(k).get("MM"))+1)==13?(Integer.parseInt(listEx.get(k).get("YYYY"))+1):(Integer.parseInt(listEx.get(k).get("YYYY")))));
		        			  data3.put("MM",""+((Integer.parseInt(listEx.get(k).get("MM"))+1)==13 ? "01" : (formatter.format(Integer.parseInt(listEx.get(k).get("MM"))+1))));
		        			  //data3.put("MM",""+formatter.format(Double.parseDouble(listEx.get(k).get("MM"))+1));
			    			  data3.put("EMPLOYEE_ID", listEx.get(k).get("EMPLOYEE_ID"));
			      			  data3.put("DOCTOR_CODE",listEx.get(k).get("DOCTOR_CODE"));
			      			  data3.put("HOSPITAL_CODE",listEx.get(k).get("HOSPITAL_CODE"));
			      			  data3.put("LINE_NO",listEx.get(k).get("LINE_NO"));
			      			  data3.put("EXP_CODE",listEx.get(k).get("EXPENSE_CODE"));
			      			  data3.put("EXP_DESP", listEx.get(k).get("DESCRIPTION"));
			      			  data3.put("EXPENSE_SIGN",listEx.get(k).get("EXPENSE_SIGN"));
			      			  data3.put("EXPENSE_ACCOUNT_CODE",listEx.get(k).get("EXPENSE_ACCOUNT_CODE"));
			      			  data3.put("AMT",""+ JNumber.showDouble(someExpCarryForword, 2));
			      			  //data3.put("TAX_AMT",""+(Double.parseDouble(listEx.get(k).get("TAX_AMOUNT"))- tempDrAmt));
			      			  data3.put("TAX_AMT",""+(Double.parseDouble(listEx.get(k).get("TAX_AMOUNT"))==0?0 :percentTaxSomeExp*Double.parseDouble(listEx.get(k).get("TAX_AMOUNT"))));
			    			  data3.put("UPDATE_DATE",listEx.get(k).get("UPDATE_DATE"));
			      			  data3.put("UPDATE_TIME",listEx.get(k).get("UPDATE_TIME"));
			      			  data3.put("USER_ID", listEx.get(k).get("USER_ID"));
			    			  data3.put("SUPPLIER_CODE",listEx.get(k).get("SUPPLIER_CODE"));
			    			  data3.put("INVOICE_TYPE_DESCRIPTION",listEx.get(k).get("INVOICE_TYPE_DESCRIPTION"));
			      			  data3.put("DOC_NO",listEx.get(k).get("DOC_NO"));
			      			  data3.put("DOC_DATE", listEx.get(k).get("DOC_DATE"));
			    			  data3.put("NOTE",listEx.get(k).get("NOTE"));
			    			  data3.put("TAX_TYPE_CODE",listEx.get(k).get("TAX_TYPE_CODE"));
			      			  data3.put("COMPUTE_TAX_TYPE",listEx.get(k).get("COMPUTE_TAX_TYPE"));
			      			  data3.put("DEPARTMENT_CODE", listEx.get(k).get("DEPARTMENT_CODE"));
			    			  data3.put("LOCATION_CODE",listEx.get(k).get("LOCATION_CODE"));
			    			  data3.put("TAX_TYPE_CODE",listEx.get(k).get("TAX_TYPE_CODE"));
			      			  data3.put("COMPUTE_TAX_TYPE",listEx.get(k).get("COMPUTE_TAX_TYPE"));
			      			  data3.put("DEPARTMENT_CODE", listEx.get(k).get("DEPARTMENT_CODE"));
			    			  data3.put("LOCATION_CODE",listEx.get(k).get("LOCATION_CODE"));
			    			  data3.put("ACT", "1");
			    			  data3.put("OLD_AMOUNT",listEx.get(k).get("OLD_AMOUNT"));
			    			  data3.put("PAYMENT_DATE",listEx.get(k).get("PAYMENT_DATE"));
			    			  data3.put("BATCH_NO",listEx.get(k).get("BATCH_NO"));
			    			  data3.put("OLD_DOCTOR",listEx.get(k).get("OLD_DOCTOR"));
		        			  tempDrAmt= tempDrAmt - expAmt;
		        			  listExCarryForward.add(data2);
		        			  listExCarryForward.add(data3);
	 	    			  }
    			   }else{
    				   		  HashMap<String, String> data4 = new HashMap<String, String>();
		        			  data4.put("YYYY",""+((Integer.parseInt(listEx.get(k).get("MM"))+1)==13?(Integer.parseInt(listEx.get(k).get("YYYY"))+1):(Integer.parseInt(listEx.get(k).get("YYYY")))));
		        			  data4.put("MM",""+((Integer.parseInt(listEx.get(k).get("MM"))+1)==13 ? "01" : (formatter.format(Integer.parseInt(listEx.get(k).get("MM"))+1))));
		        			  //data4.put("MM",""+formatter.format(Double.parseDouble(listEx.get(k).get("MM"))+1));
			    			  data4.put("EMPLOYEE_ID", listEx.get(k).get("EMPLOYEE_ID"));
			      			  data4.put("DOCTOR_CODE",listEx.get(k).get("DOCTOR_CODE"));
			      			  data4.put("HOSPITAL_CODE",listEx.get(k).get("HOSPITAL_CODE"));
			      			  data4.put("LINE_NO",listEx.get(k).get("LINE_NO"));
			      			  data4.put("EXP_CODE",listEx.get(k).get("EXPENSE_CODE"));
			      			  data4.put("EXP_DESP", listEx.get(k).get("DESCRIPTION"));
			      			  data4.put("EXPENSE_SIGN",listEx.get(k).get("EXPENSE_SIGN"));
			      			  data4.put("EXPENSE_ACCOUNT_CODE",listEx.get(k).get("EXPENSE_ACCOUNT_CODE"));
			      			  data4.put("AMT",JNumber.showDouble(Double.parseDouble(listEx.get(k).get("AMOUNT")), 2));
			    			  data4.put("TAX_AMT",listEx.get(k).get("TAX_AMOUNT"));
			    			  data4.put("UPDATE_DATE",listEx.get(k).get("UPDATE_DATE"));
			      			  data4.put("UPDATE_TIME",listEx.get(k).get("UPDATE_TIME"));
			      			  data4.put("USER_ID", listEx.get(k).get("USER_ID"));
			    			  data4.put("SUPPLIER_CODE",listEx.get(k).get("SUPPLIER_CODE"));
			    			  data4.put("INVOICE_TYPE_DESCRIPTION",listEx.get(k).get("INVOICE_TYPE_DESCRIPTION"));
			      			  data4.put("DOC_NO",listEx.get(k).get("DOC_NO"));
			      			  data4.put("DOC_DATE", listEx.get(k).get("DOC_DATE"));
			    			  data4.put("NOTE",listEx.get(k).get("NOTE"));
			    			  data4.put("TAX_TYPE_CODE",listEx.get(k).get("TAX_TYPE_CODE"));
			      			  data4.put("COMPUTE_TAX_TYPE",listEx.get(k).get("COMPUTE_TAX_TYPE"));
			      			  data4.put("DEPARTMENT_CODE", listEx.get(k).get("DEPARTMENT_CODE"));
			    			  data4.put("LOCATION_CODE",listEx.get(k).get("LOCATION_CODE"));
			    			  data4.put("TAX_TYPE_CODE",listEx.get(k).get("PAYMENT_DATE"));
			      			  data4.put("COMPUTE_TAX_TYPE",listEx.get(k).get("COMPUTE_TAX_TYPE"));
			      			  data4.put("DEPARTMENT_CODE", listEx.get(k).get("DEPARTMENT_CODE"));
			    			  data4.put("LOCATION_CODE",listEx.get(k).get("LOCATION_CODE"));
			    			  data4.put("ACT", "1");
			    			  data4.put("OLD_AMOUNT",listEx.get(k).get("OLD_AMOUNT"));
			    			  data4.put("PAYMENT_DATE",listEx.get(k).get("PAYMENT_DATE"));
			    			  data4.put("BATCH_NO",listEx.get(k).get("BATCH_NO"));
			    			  data4.put("OLD_DOCTOR",listEx.get(k).get("OLD_DOCTOR"));
		        			  listExCarryForward.add(data4);
    			   		}
    		   		} 
    		   }
    	System.out.println("Show List : "+listExCarryForward);
    	
    	return listExCarryForward;
    }
    public String updateExpCarryForward(String month,String year,String hospital_code){
        DBConnection con;
        con = new DBConnection();
        con.connectToLocal();
		Batch b = new Batch(hospital_code,con);
		String batch= b.getYyyy() + b.getMm();
		NumberFormat formatter = new DecimalFormat("00");  
		ArrayList<HashMap<String,String>> listExCarryForwardData = CalculateExpenseCarryForward(month,year,hospital_code);
    	String sql_update="";
    	String sql_insert="";
    	for(int i=0;i<listExCarryForwardData.size();i++){
    		 String yyyyMm = listExCarryForwardData.get(i).get("YYYY")+listExCarryForwardData.get(i).get("MM");
    		 if(batch.equals(yyyyMm)){
    			//ã¹à´×Í¹ ºÒ§ÊèÇ¹áÅÐËÑ¡ËÁ´
    			 if(listExCarryForwardData.get(i).get("ACT").equals("0")){
    			 }else{
    				 //In month
    				 sql_update += "UPDATE TRN_EXPENSE_DETAIL SET ";  
    				 sql_update +="AMOUNT = "+listExCarryForwardData.get(i).get("AMT")+" ,";
    				 sql_update +="TAX_AMOUNT = "+listExCarryForwardData.get(i).get("TAX_AMT")+" ,";
    				 sql_update +="UPDATE_DATE = '"+JDate.getDate()+"' ,";
    				 sql_update +="UPDATE_TIME = '"+JDate.getTime()+"' ,";
    				 sql_update +="USER_ID = ':ProcessExpCarry' "; 
    				 sql_update +="WHERE  HOSPITAL_CODE = '"+listExCarryForwardData.get(i).get("HOSPITAL_CODE")+"' ";
    				 sql_update +="AND LINE_NO = '"+listExCarryForwardData.get(i).get("LINE_NO")+"' ";
    				 sql_update +="AND DOCTOR_CODE = '"+listExCarryForwardData.get(i).get("DOCTOR_CODE")+"'";
    				 sql_update +="AND EXPENSE_CODE = '"+listExCarryForwardData.get(i).get("EXP_CODE")+"'";
    				 sql_update +="AND EXPENSE_SIGN = '-1' ;";
    			 }
    		 }else{
    			 if(listExCarryForwardData.get(i).get("AMT").equals(listExCarryForwardData.get(i).get("OLD_AMOUNT"))){
    				 sql_update += "UPDATE TRN_EXPENSE_DETAIL SET ";  
    				 sql_update += "YYYY = '"+listExCarryForwardData.get(i).get("YYYY")+"' ,"; 
    				 sql_update += "MM = '"+listExCarryForwardData.get(i).get("MM")+"' ,";
    				 sql_update +="AMOUNT = "+listExCarryForwardData.get(i).get("AMT")+" ,";
    				 sql_update +="TAX_AMOUNT = "+listExCarryForwardData.get(i).get("TAX_AMT")+" ,";
    				 sql_update +="UPDATE_DATE = '"+JDate.getDate()+"' ,";
    				 sql_update +="UPDATE_TIME = '"+JDate.getTime()+"' ,";
    				 sql_update +="USER_ID = ':ProcessExpCarry' ";
    				 sql_update +="WHERE  HOSPITAL_CODE = '"+listExCarryForwardData.get(i).get("HOSPITAL_CODE")+"' ";
    				 sql_update +="AND LINE_NO = '"+listExCarryForwardData.get(i).get("LINE_NO")+"' ";
    				 sql_update +="AND DOCTOR_CODE = '"+listExCarryForwardData.get(i).get("DOCTOR_CODE")+"'";
    				 sql_update +="AND EXPENSE_CODE = '"+listExCarryForwardData.get(i).get("EXP_CODE")+"'";
    				 sql_update +="AND EXPENSE_SIGN = '-1' ;";
    			 }else{
	 			 	  	sql_insert +="INSERT INTO TRN_EXPENSE_DETAIL ( YYYY, MM,EMPLOYEE_ID,"
				    			+ "DOCTOR_CODE,HOSPITAL_CODE,LINE_NO,EXPENSE_CODE,EXPENSE_SIGN,EXPENSE_ACCOUNT_CODE,"
				    			+ "AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME,USER_ID,SUPPLIER_CODE,INVOICE_TYPE_DESCRIPTION,DOC_NO,"
				    			+ "DOC_DATE,NOTE,TAX_TYPE_CODE,COMPUTE_TAX_TYPE,DEPARTMENT_CODE,LOCATION_CODE,PAYMENT_DATE,"
				    			+ "BATCH_NO,PAYMENT_TERM,OLD_DOCTOR_CODE) "
				    			+ "VALUES ( ";
				 		 sql_insert +="'" +listExCarryForwardData.get(i).get("YYYY")+"' ,";
						 sql_insert +="'" +listExCarryForwardData.get(i).get("MM")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("EMPLOYEE_ID")+"' ,";
						 sql_insert +="'"+ listExCarryForwardData.get(i).get("DOCTOR_CODE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("HOSPITAL_CODE")+"' ,";
						 sql_insert +="'"+JDate.getDate()+JDate.getTime()+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("EXP_CODE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("EXPENSE_SIGN")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("EXPENSE_ACCOUNT_CODE")+"' ,";
						 sql_insert +=listExCarryForwardData.get(i).get("AMT")+" ,";
						 sql_insert +=listExCarryForwardData.get(i).get("TAX_AMT")+" ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("UPDATE_DATE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("UPDATE_TIME")+"' ,";
						 sql_insert +="':ProcessExpCarry' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("SUPPLIER_CODE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("INVOICE_TYPE_DESCRIPTION")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("DOC_NO")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("DOC_DATE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("NOTE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("TAX_TYPE_CODE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("COMPUTE_TAX_TYPE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("DEPARTMENT_CODE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("LOCATION_CODE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("PAYMENT_DATE")+"' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("BATCH_NO")+"' ,";
						 sql_insert +="'2' ,";
						 sql_insert +="'"+listExCarryForwardData.get(i).get("OLD_DOCTOR_CODE")+"')";	 
    			 } 	    
    		 }
    	}
    	System.out.println("INSERT : "+listExCarryForwardData);
    	 try {
			 cdb.setStatement();
			 cdb.insert(sql_update);
			 cdb.insert(sql_insert);
			 cdb.commitDB();
			 cdb.closeDB("");
		} catch (SQLException e) {
			e.printStackTrace();
		}   
    	return "";
    }
    

    private String[][] DeleteDataExpense(String DoctorCode, String ExpenseCode){
        //Step 1:ลบข้อมูลออกจากตาราง SUMMARY_MONTHLY ตามเงื่อนไข HOSPITAL_CODE,YYYY,MM, EXPENSE_CODE IN (AD406,DD,406,AD402,DD402)
    	System.out.println("START STEP 1 : DELETE DATA IN TABLE TRN_EXPENSE_DETAIL");
    	String sql_statement = "",sql_data="";
        String[][] ExpenseArr = null;
        String startMonth="";
        int statusMonth=0;
        boolean status_delete = true;
        System.out.println("month="+month);
        System.out.println("year="+year);
        sql_data="SELECT AMOUNT, TAX_AMOUNT FROM TRN_EXPENSE_DETAIL WHERE HOSPITAL_CODE='"+hospital_code+"'"
        +" AND YYYY='"+year+"' AND MM='"+month+"' AND NOTE LIKE 'EXP_PED%'"
        +" AND DOCTOR_CODE='"+DoctorCode+"' AND EXPENSE_CODE='"+ExpenseCode+"' ";
        System.out.println("sql_data="+sql_data);
        try
        {
            ExpenseArr=cdb.query(sql_data);
            if(ExpenseArr.length !=0)
            {
            	sql_statement = "DELETE FROM TRN_EXPENSE_DETAIL WHERE HOSPITAL_CODE='"+hospital_code+"'"
                +" AND YYYY='"+year+"' AND MM='"+month+"' AND NOTE LIKE 'EXP_PED%'"
                +" AND DOCTOR_CODE='"+DoctorCode+"' AND EXPENSE_CODE='"+ExpenseCode+"' ";
                System.out.println("DELETE EXP_PED="+sql_statement);
                try
                {
                    cdb.insert(sql_statement);
                    System.out.println("Step 1 : Delete TRN_EXPENSE_DETAIL complete");
                    cdb.commitDB();
                        
                 }
               	 catch(Exception e)
               	 {
                    System.out.println("Step 1 Delete TRN_EXPENSE_DETAIL EXP_PED Exclude : "+e+" QUERY="+sql_statement);
                    //result = result+"Update order item exclude guarantee is error : "+e;
                   
                 }
            }
            else
            {
            	System.out.println("ไม่มีข้อมูลที่ต้องทำการลบจากตาราง TRN_EXPENSE_DETAIL");
            }
        }
       	 catch(Exception e)
       	 {
            System.out.println("Step 1 Delete TRN_EXPENSE_DETAIL EXP_PED Exclude : "+e+" QUERY="+sql_statement);
            //result = result+"Update order item exclude guarantee is error : "+e;
            status_delete = false;
         }
        
       	System.out.println("FINISH STEP 1 : DELETE DATA IN TABLE TRN_EXPENSE_DETAIL");
        return ExpenseArr;
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
        int start_year=Integer.parseInt(year)+1;
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
        	sql+="CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END AS DR_SUM_AMT,   "
        		+"CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END AS GUARANTEE_AMOUNT,   "
        		+"CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') END AS EXPENSE_AMOUNT, "  
        		+"( "
        		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END)   "
        		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END)  "
        		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') END) "
        		+") AS TOTAL_AMOUNT ";
        }
        else//Tax
        {
        	sql+="CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END AS DR_TAX_406, "
        		+"CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') END AS EXPENSE_TAX, "
        		+"( "
        		+"(CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END ) "
        		+"+(CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') END ) "
        		+") AS TOTAL_TAX ";
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
        		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END)  " 
        		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END)  "
        		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') END) "
        		+")<0 ";
        }
        else if(ExpenseStatus.equalsIgnoreCase("1") && type.equalsIgnoreCase("2")) //is revenue and amount>0
        {
        	sql+=" HAVING  "
        		+" ( "
        		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END)  " 
        		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END)  "
        		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') END) "
        		+")>0 ";
        }
        else if(ExpenseStatus.equalsIgnoreCase("2") && type.equalsIgnoreCase("1"))//is tax and amount_tax<0
        {
        	sql+=" HAVING  "
        		+" ( "
                +"(CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END ) "
                +"+(CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') END ) "
                +")<0 ";
        }
        else if(ExpenseStatus.equalsIgnoreCase("2") && type.equalsIgnoreCase("2"))//is tax and amount_tax>0
        {
        	sql+=" HAVING  "
        		+" ( "
                +"(CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END ) "
                +"+(CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') END ) "
                +")>0 ";
        }
        sql+=" ORDER BY SM.DOCTOR_CODE ";
                
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
        		+"(CASE WHEN (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_SUM_AMT FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END)   "
        		+"+(CASE WHEN (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT GUARANTEE_AMOUNT FROM SUMMARY_GUARANTEE WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END)  "
        		+"+(CASE WHEN (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END) "
        		+") AS TOTAL_AMOUNT ";
        }
        else//Tax
        {
        	sql+="( "
        		+"(CASE WHEN (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') IS NULL THEN 0.00 ELSE (SELECT DR_TAX_406 FROM SUMMARY_MONTHLY WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010') END ) "
        		+"+(CASE WHEN (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') IS NULL THEN 0.00 ELSE (SELECT SUM(TAX_AMOUNT*EXPENSE_SIGN) FROM TRN_EXPENSE_DETAIL WHERE DOCTOR_CODE=SM.DOCTOR_CODE AND MM='03' AND YYYY='2010' AND NOTE NOT LIKE 'EXP_DIS%') END ) "
        		+") AS TOTAL_TAX ";
        }
        sql+="FROM SUMMARY_MONTHLY SM,DOCTOR D  "
        +"WHERE SM.HOSPITAL_CODE=D.HOSPITAL_CODE   "
        +" AND SM.DOCTOR_CODE=D.CODE   ";
        if(!DoctorCode.equals("")&& !DoctorProfileCode.equals(""))
        {
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
		            				String[][] expenseDisArr=null;
		            				double d_expense_amount=0;
		            				if(doctorArr.length!=0)
		            				{
		            					//get Doctor Code ที่มีรายได้บวกออกมา
		            					doctorCodeDis=doctorArr[0][0];
		            					//get Expense ที่เป็นบวกออกมา
		            					expenseDisArr=GetExpense(doctorCodeDis,"2","1");
		            					if(expenseDisArr.length !=0)
		            					{
		            						boolean status_exp_ne=true, status_exp_dis=true, status_next_month=true;
		            						int runNumDis=0;
		            						int num=0;
		            						for(int k=0;k<expenseDisArr.length;k++)
		            						{
		            							num=k+1;
		            							 System.out.println("============exp add no : "+k+" ของ doctor ที่มีรายได้บวกและ exp บวก===================");
		            							if(status_exp_ne)//รายได้ยังติดลบอยู่หรือไม่
		            							{
			            							double rs_exp_ne=0, rs_exp_dis=0;
			            							boolean status_ne=true, status_dis=true;
			            							int sign_dis=0;
			            							
			            							//ดึงค่าใช้จ่ายที่เป็นบวก
			            							d_expense_amount=Double.parseDouble(expenseDisArr[k][1]);
			            							//เอาค่า Expense ที่เป็นลบมาบวกกับที่เป็นบวก Doctor ติดลบ
			            							rs_exp_ne=expense_amount+d_expense_amount;
			            							if(rs_exp_ne<0)
			            							{
			            								//d_expense_amount น้อยกว่า expense_amount ที่จะกระจาย
			            								rs_exp_dis=d_expense_amount;
			            							}
			            							else
			            							{
			            								rs_exp_dis=expense_amount;
			            								
			            							}
			            							
			            							//ตรวจสอบ Doctor Code ที่ติดลบคำนวณแล้วยังติดลบอยู่หรือไม่ (ไม่เป็นไร)
			            							//เป็นจริงเมื่อ < 0
			            							
			            							status_ne=RevenueVerifly(doctorCode,doctorProfile,"1",rs_exp_dis);
			            							status_dis=RevenueVerifly(doctorCodeDis,doctorProfile,"1",rs_exp_dis*-1);
			            							if(status_ne==true && status_dis==true)
			            							{
			            								//รายได้ DoctorCode ที่ติดลบคำนวณแล้วยังติดลบ และ รายได้ของ DoctorCode ที่กระจายเป็นติดลบ
			            								//เอาจำนวน expense_amount ของ DoctorCode ที่ติดลบมาทำการคำนวณว่าจะยังติดลบอยู่หรือไม่
			            								//เปลี่ยนไปเลือก expense code ตัวอื่น
			            								status_exp_dis=false;
			            								
			            							}
			            							
			            							else if(status_ne==true && status_dis==false)
			            							{
			            								//รายได้ DoctorCode ที่ติดลบคำนวณแล้วยังติดลบ และ รายได้ของ DoctorCode ที่กระจายเป็นบวก
			            								//ให้ทำการกระจาย expense ได้
			            								status_exp_dis=true;
			            								if(num==expenseDisArr.length)
			            								{
			            									status_exp_ne=false;
			            								}
		            								
			            							}
			            							else if(status_ne==false && status_dis==true)
			            							{
			            								//รายได้ DoctorCode ที่ติดลบคำนวณแล้วเป็นบวก และ รายได้ของ DoctorCode ที่กระจายเป็นติดลบ
			            								//คำนวณแล้วยังติดลบอยู่
			            								status_exp_dis=false;
			            								
			            							}
			            							else if(status_ne==false && status_dis==false)
			            							{
			            								//รายได้ DoctorCode ที่ติดลบคำนวณแล้วเป็นบวก และ รายได้ของ DoctorCode ที่กระจายเป็นบวก
		            									status_exp_ne=false;//ไม่คำนวณ expense ของ DoctorCode กระจายอีกแล้ว
			            								status_exp_dis=true;//ทำการกระจาย expense ได้
		            								}
			            							 System.out.println("status_exp_ne="+status_exp_ne);
			            							 System.out.println("status_exp_dis="+status_exp_dis);
			            							String line_no_ne="", note_ne="", sql_insert_ne="", line_no_dis="", note_dis="", sql_insert_dis="";
			            							int expense_sign_ne=0, expense_sign_dis=0;
			            							if(status_exp_dis)
			            							{
			            								runNumDis++;
			            								if(rs_exp_dis !=0)
			            								{
				            								//insert data expense distribute เพิ่ม
				            								line_no_ne=expenseArr[g][2]+"ED"+runNumDis;
				            								note_ne="EXP_DIS AddFromDR:"+doctorCodeDis+" LineNo="+expenseDisArr[k][2]+" "+expenseArr[g][7];
				            								expense_sign_ne=1;
				            								sql_insert_ne="INSERT INTO TRN_EXPENSE_DETAIL("
				            								+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
				            								+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
				            								+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
				            								+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"',"
				            								+" '"+line_no_ne+"', '"+expenseArr[g][3]+"', "+expense_sign_ne+", "
				            								+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_ne+"', "+rs_exp_dis+",0,"
				            								+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
				            								 System.out.println("nsert_expense_ne="+sql_insert_ne);
				            								 System.out.println("insert_expense_dis="+sql_insert_dis);
				    	                                
				            								//insert data expense distribute ลด
				            								line_no_dis=expenseDisArr[k][2]+"EDN"+runNumDis;
				            								note_dis="EXP_DIS DeductFromDR:"+doctorCode+" LineNo="+expenseArr[g][2]+" "+expenseDisArr[k][7];
				            								expense_sign_dis=-1;
				            								sql_insert_dis="INSERT INTO TRN_EXPENSE_DETAIL("
				            								+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
				            								+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
				            								+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
				            								+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCodeDis+"',"
				            								+" '"+line_no_dis+"', '"+expenseDisArr[k][3]+"', "+expense_sign_dis+", "
				            								+" '"+expenseDisArr[k][5]+"', '"+expenseDisArr[k][6]+"', '"+note_dis+"', "+rs_exp_dis+",0,"
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
			    	                                   	if(num==expenseDisArr.length || runNumNe==expenseArr.length)
				            							{
			    	                                   		//ทำการยกยอดไปเดือนถัดไป
			    	                                   		String [][] AmountArr=this.GetRevenue(doctorCode,doctorProfile,"3","1");
			        		            					double re_amount_total=Double.parseDouble(AmountArr[0][5]);
			        		            					double total_re_ex=re_amount_total+rs_exp_dis;
			        		            					double amount_add=0, amount_next=0;
			        		            					if(total_re_ex<0)//คำนวณแล้วมีค่าน้อยกว่า 0
			        		            					{
			        		            						amount_next=rs_exp_dis*-1;
			        		            					}
			        		            					if(amount_next !=0)
			        		            					{
				        		            					//insert data expense distribute เพิ่ม
					            								line_no_ne=expenseArr[g][2]+"EDN"+runNumDis;
					            								note_ne="EXP_DIS "+expenseArr[g][7];
					            								expense_sign_ne=1;
					            								sql_insert_ne="INSERT INTO TRN_EXPENSE_DETAIL("
					            								+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
					            								+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
					            								+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
					            								+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"',"
					            								+" '"+line_no_ne+"', '"+expenseArr[g][3]+"', "+expense_sign_ne+", "
					            								+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_ne+"', "+amount_next+",0,"
					            								+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
					            								 System.out.println("nsert_expense_ne_end="+sql_insert_ne);
					            								
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
					            								line_no_dis=expenseDisArr[k][2]+"ED"+runNumDis;
					            								note_dis="EXP_ENT "+expenseDisArr[k][7];
					            								expense_sign_dis=-1;
					            								sql_insert_dis="INSERT INTO TRN_EXPENSE_DETAIL("
					            								+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
					            								+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
					            								+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
					            								+" VALUES('"+hospital_code+"', '"+year_str+"', '"+month_str+"', '"+doctorCodeDis+"',"
					            								+" '"+line_no_dis+"', '"+expenseDisArr[k][3]+"', "+expense_sign_dis+", "
					            								+" '"+expenseDisArr[k][5]+"', '"+expenseDisArr[k][6]+"', '"+note_dis+"', "+amount_next+",0,"
					            								+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
					            						
					            								// sql = "SELECT 0DOCTOR_CODE,1(AMOUNT*EXPENSE_SIGN) AS TOTAL_AMOUNT,  "
					            						        //	  +" 2LINE_NO, 3EXPENSE_CODE, 4EXPENSE_SIGN, 5EXPENSE_ACCOUNT_CODE, 6TAX_TYPE_CODE, 7NOTE "
					            								 System.out.println("insert_expense_dis_end="+sql_insert_dis);
					    	                                   	try
					    	                                   	{
					    	                                   		cdb.insert(sql_insert_ne);
					    	                                   		cdb.insert(sql_insert_dis);
					    	                                   		cdb.commitDB();
					    	                                   	}
					    	                                   	catch(Exception e)
					    	                        			{
					    	                                   	 System.out.println("insert_expense End NE Excepiton : "+e+"query="+sql_insert_ne);
					    	                                   	 System.out.println("insert_expense End DIS Excepiton : "+e+"query="+sql_insert_dis);
					    	                               		 	cdb.rollDB();
					    	                               		 	status=false;
					    	                        			}
			        		            					}
				            							}
			            							}
		            							}
		            							else
		            							{
		            								 System.out.println("รายได้ ของ Doctor Code : "+doctorCode+" ไม่ติดลบแล้ว");
		            							}
	            						
		            						} //for(int k=0;k<expenseDisArr.length;k++)
		            						
		        							
		            					}
		            					else
		            					{
		            						 System.out.println("ไม่พบข้อมูล expense ของ Doctor Code ภายใน Doctor Profile เดียวกัน ที่จะนำ expense มากระจายให้");
				            					
				            					runNumNe++;
				            					 String [][] AmountArr=null;
				            					 boolean status_dis=true;
				            					
				            					AmountArr=this.GetRevenue(doctorCode,doctorProfile,"3","1");
				            					double re_amount_total=Double.parseDouble(AmountArr[0][5]);
				            					double ex_amount_next=Double.parseDouble(expenseArr[g][1])*-1;
				            					double total_re_ex=re_amount_total+ex_amount_next;
				            					double amount_add=0, amount_next=0;
				            						if(total_re_ex >= 0)//คำนวณแล้วมีค่ามากกว่า 0
					            					{
					            						amount_add=re_amount_total*-1;
					            						amount_next=re_amount_total*-1;
					            						
					            					}
					            					else//ยังเป็นค่าติดลบ
					            					{
					            						amount_add=ex_amount_next;
					            						amount_next=ex_amount_next;
					            					}
					            					if(amount_add !=0)
					            					{
						            					//ยกยอดไปเดือนถัดไป
						            					//insert data expense distribute เพิ่ม
					    								String line_no_ne=expenseArr[g][2]+"ED"+runNumNe;
					    								String note_ne="EXP_DIS DR:"+doctorCode+" DeductAmt:"+re_amount_total+" AmtOld:"+ex_amount_next+" "+expenseArr[g][7];
					    								int expense_sign_ne=1;
					    								
					    								String sql_insert_ne="INSERT INTO TRN_EXPENSE_DETAIL("
					    								+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
					    								+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
					    								+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
					    								+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"',"
					    								+" '"+line_no_ne+"', '"+expenseArr[g][3]+"', "+expense_sign_ne+", "
					    								+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_ne+"', "+amount_add+",0,"
					    								+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
					    								 System.out.println("insert_expense_ne="+sql_insert_ne);
					    								try
					                                   	{
					                                   		cdb.insert(sql_insert_ne);
					                                   		cdb.commitDB();
					                                   	}
					                                   	catch(Exception e)
					                        			{
					                                   	 System.out.println("insert_expense NE Excepiton : "+e+"query="+sql_insert_ne);
					                        				cdb.rollDB();
					                        				status=false;
					                        			}
					            					}
					            					if(amount_next !=0)
					            					{
					    								//insert data expense distribute ลด เป็นรายการยกยอดในเืดือนถัดไป
					    								String line_no_dis=expenseArr[g][2]+"EN"+runNumNe;
					    								String note_dis="EXP_ENT DR:"+doctorCode+" DeductAmt:"+re_amount_total+" AmtOld:"+ex_amount_next+" "+expenseArr[g][7];
					    								int expense_sign_dis=-1;
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
					    								 System.out.println("year_str="+year_str);
					    								
					    								String sql_insert_dis="INSERT INTO TRN_EXPENSE_DETAIL("
					    								+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
					    								+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
					    								+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
					    								+" VALUES('"+hospital_code+"', '"+year_str+"', '"+month_str+"', '"+doctorCode+"',"
					    								+" '"+line_no_dis+"', '"+expenseArr[g][3]+"', "+expense_sign_dis+", "
					    								+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_dis+"', "+amount_next+",0,"
					    								+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
					    								 System.out.println("insert_expense_dis="+sql_insert_dis);
					                                   	try
					                                   	{
					                                   		cdb.insert(sql_insert_dis);
					                                   		cdb.commitDB();
					                                   	}
					                                   	catch(Exception e)
					                        			{
					                                   	 System.out.println("insert_expense DIS Excepiton : "+e+"query="+sql_insert_dis);
					                               		 	cdb.rollDB();
					                               		 	status=false;
					                        			}
					            					}
				            					
				            				 
		            					}///////////////////////////////
		            				}
		            				else
		            				{
		            					/*doctorArrRe=this.GetRevenue(doctorCode,doctorProfile,"3");
		            					double amountTotal=Double.parseDouble(doctorArrRe[5][0]);
		            					double amount_next=Double.parseDouble(expenseArr[g][1])*-1;
		            					double totalAmtDeduct=amountTotal+amount_next;
		            					if(totalAmtDeduct<0)*/
		            					
		            					runNumNe++;
		            					 System.out.println("ไม่พบข้อมูล Doctor Code ภายใน Doctor Profile ที่มีรายได้เป็นบวก");
		            					String [][] AmountArr=null;
		            					AmountArr=this.GetRevenue(doctorCode,doctorProfile,"3","1");
		            					double re_amount_total=Double.parseDouble(AmountArr[0][5]);
		            					double ex_amount_next=Double.parseDouble(expenseArr[g][1])*-1;
		            					double total_re_ex=re_amount_total+ex_amount_next;
		            					double amount_add=0, amount_next=0;
		            					if(total_re_ex>=0)//คำนวณแล้วมีค่ามากกว่า 0
		            					{
		            						amount_add=re_amount_total*-1;
		            						amount_next=re_amount_total*-1;
		            					}
		            					else//ยังเป็นค่าติดลบ
		            					{
		            						amount_add=ex_amount_next;
		            						amount_next=ex_amount_next;
		            					}
		            					if(amount_add !=0)
		            					{
			            					//ยกยอดไปเดือนถัดไป
			            					//insert data expense distribute เพิ่ม
		    								String line_no_ne=expenseArr[g][2]+"ED"+runNumNe;
		    								String note_ne="EXP_DIS DR:"+doctorCode+" DeductAmt:"+re_amount_total+" AmtOld:"+ex_amount_next+" "+expenseArr[g][7];
		    								int expense_sign_ne=1;
		    								
		    								String sql_insert_ne="INSERT INTO TRN_EXPENSE_DETAIL("
		    								+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
		    								+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
		    								+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
		    								+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"',"
		    								+" '"+line_no_ne+"', '"+expenseArr[g][3]+"', "+expense_sign_ne+", "
		    								+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_ne+"', "+amount_add+",0,"
		    								+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
		    								 System.out.println("insert_expense_ne="+sql_insert_ne);
		    								try
		                                   	{
		                                   		cdb.insert(sql_insert_ne);
		                                   		cdb.commitDB();
		                                   	}
		                                   	catch(Exception e)
		                        			{
		                                   	 System.out.println("insert_expense NE Excepiton : "+e+"query="+sql_insert_ne);
		                        				cdb.rollDB();
		                        				status=false;
		                        			}
		            					}
		            					if(amount_next !=0)
		            					{
		    								//insert data expense distribute ลด เป็นรายการยกยอดในเืดือนถัดไป
		    								String line_no_dis=expenseArr[g][2]+"EN"+runNumNe;
		    								String note_dis="EXP_ENT DR:"+doctorCode+" DeductAmt:"+re_amount_total+" AmtOld:"+ex_amount_next+" "+expenseArr[g][7];
		    								int expense_sign_dis=-1;
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
		    								 System.out.println("year_str="+year_str);
		    								
		    								String sql_insert_dis="INSERT INTO TRN_EXPENSE_DETAIL("
		    								+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, LINE_NO, EXPENSE_CODE, EXPENSE_SIGN, "
		    								+" EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
		    								+" AMOUNT,TAX_AMOUNT,UPDATE_DATE,UPDATE_TIME)"
		    								+" VALUES('"+hospital_code+"', '"+year_str+"', '"+month_str+"', '"+doctorCode+"',"
		    								+" '"+line_no_dis+"', '"+expenseArr[g][3]+"', "+expense_sign_dis+", "
		    								+" '"+expenseArr[g][5]+"', '"+expenseArr[g][6]+"', '"+note_dis+"', "+amount_next+",0,"
		    								+" '"+JDate.getDate()+"','"+JDate.getTime()+"')";
		    								 System.out.println("insert_expense_dis="+sql_insert_dis);
		                                   	try
		                                   	{
		                                   		cdb.insert(sql_insert_dis);
		                                   		cdb.commitDB();
		                                   	}
		                                   	catch(Exception e)
		                        			{
		                                   	 System.out.println("insert_expense DIS Excepiton : "+e+"query="+sql_insert_dis);
		                               		 	cdb.rollDB();
		                               		 	status=false;
		                        			}
		            					}
		            				}
	            				} //if(!status_amount)
	            				else
	            				{
	            					 System.out.println("Doctor Code : "+doctorCode+" นี้มีรายได้เป็นบวกแล้ว");
	            				}
	            			
	            				
	            			} //for(int g=0;g<expenseArr.length;g++)
		            		
	            			
	            		}
	            		else
	            		{
	            			 System.out.println("Notfound Data : ค้นหา Expense code ที่ติดลบของ doctor code ที่รายได้ติดลบ");
	            			//เอาค่าที่ิติดลบยกไปเดือนถัดไป
	            			//ตรวจสอบว่าเมื่อยกไปเดือนถัดไปแล้วคำนวณในเดือนนั้นยังติดลบอยู่หรือไม่
	            			//ยังมีรายได้ที่ติดลบอยู่
	            		}
	            		
		            } //for(int i=0; i<RevenueArr.length;i++)
	            }
        		else
        		{
        			 System.out.println("Notfound Data : ไม่พบ doctor code ที่รายได้ติดลบ");
        		}
            }
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
	//////////////////////////////////////////////////////////
	private boolean CalculateExpensePeriod()
    {
    	System.out.println(" Step 2 : TRN_EXPENSE_DETAIL =Period");
        boolean status = true;
        String sql= "",startMonth="",endMonth="";
        String[][] PeriodArr = null;
          //query revenue of month/year
	        try
	        {
	        	System.out.println("ค้นหาเงื่อนไขในการคำนวณแบ่งจ่ายค่าใช้จ่าย");
	        	PeriodArr=this.GetPeriod();
	        	
	            System.out.println("วนลูปค่าใช้จ่ายของแพทย์ที่ต้องการทำ Expense");
	            System.out.println("PeriodArr.length="+PeriodArr.length);
	            if(PeriodArr.length !=0)
	            {
	            	double num=0;
		            for(int i=0; i<PeriodArr.length;i++)
		            {
		            	String doctorCode="", expenseCode="", typeExpense="", taxType="", departmentCode = "";
			        	double getAmount=0, getPayAmount=0, getTotalAmount=0,
			        	getTaxAmount=0, getPayTaxAmount=0, getTotalTaxAmount=0,
			        	rsAmount=0, rsPayAmount=0, rsTotalAmount=0,
			        	rsTaxAmount=0, rsPayTaxAmount=0, rsTotalTaxAmount=0,
			        	getStartMonth=0, getStartYear=0, getEndMonth=0, getEndYear=0,
			        	getMonth=0, getYear=0;
			        	String getTerm="", getNote="";
			        	
		               	doctorCode			=PeriodArr[i][0];
		            	expenseCode			=PeriodArr[i][1];
		            	typeExpense			=PeriodArr[i][2];
		            	taxType				=PeriodArr[i][3];
		            	getAmount			=Double.parseDouble(PeriodArr[i][4]);
		            	getPayAmount		=Double.parseDouble(PeriodArr[i][5]);
		            	getTotalAmount		=Double.parseDouble(PeriodArr[i][6]);
		            	getTaxAmount		=Double.parseDouble(PeriodArr[i][7]);
		            	getPayTaxAmount		=Double.parseDouble(PeriodArr[i][8]);
		            	getTotalTaxAmount	=Double.parseDouble(PeriodArr[i][9]);
		            	getStartMonth		=Double.parseDouble(PeriodArr[i][10]);
		            	getStartYear		=Double.parseDouble(PeriodArr[i][11]);
		            	getEndMonth			=Double.parseDouble(PeriodArr[i][12]);
		            	getEndYear			=Double.parseDouble(PeriodArr[i][13]);
		            	getNote				=PeriodArr[i][14];
		            	departmentCode		=PeriodArr[i][15];
		            	getMonth			=Double.parseDouble(month);
		            	getYear				=Double.parseDouble(year);
		            	getTerm				=PeriodArr[i][10]+"/"+PeriodArr[i][11]+"-"+PeriodArr[i][12]+"/"+PeriodArr[i][13];
		            	/*
		            	System.out.println("getMonth="+getMonth);
		            	System.out.println("getYear="+getYear);
		            	System.out.println("getStartMonth="+getStartMonth);
		            	System.out.println("getEndMonth="+getEndMonth);
		            	System.out.println("getStartYear="+getStartYear);
		            	System.out.println("getEndYear="+getEndYear);
		            	System.out.println("getAmount="+getAmount);
		            	System.out.println("getTaxAmount="+getTaxAmount);
		            	*/
		            	double getExpSign=0, delAmount=0, delTaxAmount=0;
		            	if((Double.parseDouble(PeriodArr[i][11]+PeriodArr[i][10]) <= Double.parseDouble(year+month)) && (Double.parseDouble(PeriodArr[i][13]+PeriodArr[i][12]) >= Double.parseDouble(year+month)))
		            	//if((getStartMonth<=getMonth && getEndMonth>=getMonth) && (getStartYear<=getYear && getEndYear>=getYear))
		            	{
		            		String[][] ExpArr = null, AmountArr=null;
		            		ExpArr=this.GetExp(expenseCode);
		            		AmountArr=this.DeleteDataExpense(doctorCode, expenseCode);
		            		String getExpAccCode="", line_no="", note_save="", sql_insert="", sql_update="";
		            		
		            		if(ExpArr.length !=0)
		    	            {
		            			getExpSign=Double.parseDouble(ExpArr[0][0]);
		            			getExpAccCode=ExpArr[0][1];
		            			System.out.println("getExpSign="+getExpSign);
		    	            }
		            		if(getExpSign !=0)
		            		{
			            		if(typeExpense.equals("1"))//จ่ายเงินเท่ากันทุกๆ เดือน
			            		{
			            			if(AmountArr.length !=0)
			            			{
			            				delAmount=Double.parseDouble(AmountArr[0][0]);
			            				delTaxAmount=Double.parseDouble(AmountArr[0][1]);
			            			}
			            			System.out.println("type expense=1");
			            			num++;
			            			rsAmount=getAmount;
			            			rsPayAmount=rsAmount-delAmount;
			            			rsTaxAmount=getTaxAmount;
			            			rsPayTaxAmount=rsTaxAmount-delTaxAmount;
			            			//insert data expense Period เพิ่ม
			            			line_no=expenseCode+"PD"+num;
							       	note_save="EXP_PED Conditon Type: PayEquals Amount :"+getAmount+" Tax Amount:"+getTaxAmount+" Term :"+getTerm;
							       	
							       	sql_insert="INSERT INTO TRN_EXPENSE_DETAIL("
							       	+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, DEPARTMENT_CODE, LINE_NO, "
							       	+" EXPENSE_CODE, EXPENSE_SIGN, EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
							       	+" AMOUNT, TAX_AMOUNT, USER_ID, UPDATE_DATE, UPDATE_TIME)"
							       	+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"', '"+departmentCode+"', "
							       	+" '"+line_no+"', '"+expenseCode+"', "+getExpSign+", "
							       	+" '"+getExpAccCode+"', '"+taxType+"', '"+getNote+"', "+rsAmount+", "
							       	+rsTaxAmount+", '"+this.user+"', '"+JDate.getDate()+"','"+JDate.getTime()+"')";
							        System.out.println("sql_insert="+sql_insert);
							        
							        try
									{
	                               		cdb.insert(sql_insert);
	                               		cdb.commitDB();
	                               		
	                               		//Update Pay Amount and Total Amount
	                               		sql_update="UPDATE STP_PERIOD_EXPENSE SET "
	                               		+" PAY_AMOUNT="+rsPayAmount+", PAY_TAX_AMOUNT="+rsPayTaxAmount+" "
	                               		+" WHERE HOSPITAL_CODE='"+hospital_code+"' AND DOCTOR_CODE='"+doctorCode+"' "
	                               		+" AND EXPENSE_CODE='"+expenseCode+"' AND TYPE_EXPENSE='1'";
		                               	 try
		 								{
		                                	cdb.insert(sql_update);
		                                	cdb.commitDB();
		                                }
		                                catch(Exception e)
		                     			{
		                                	System.out.println("Update_expense Period Excepiton : "+e+"query="+sql_update);
		                                	cdb.rollDB();
		                                	status=false;
		                     			}
	                               		
	                               	}
	                               	catch(Exception e)
	                    			{
	                               		System.out.println("insert_expense Period Excepiton : "+e+"query="+sql_insert);
	                               		cdb.rollDB();
	                               		status=false;
	                    			}
			            		}
			            		else if(typeExpense.equals("2"))//จำนวนเงินแบ่งจ่ายเป็นรายเดือน
			            		 {
			            			System.out.println("Type Expense: 2");
			            			//if(Double.parseDouble(year+month)>=getStartYear+getStartMonth &&
			            			//   Double.parseDouble(year+month)<=getEndYear+getEndMonth){
			            			if(Double.parseDouble(year+month) >= Double.parseDouble(PeriodArr[i][11]+PeriodArr[i][10])
			            			   && Double.parseDouble(year+month) <= Double.parseDouble(PeriodArr[i][13]+PeriodArr[i][12])) {
				            			num++;
				            			double NumMonth=0, NumPayMonth=0;
				            			NumMonth=JDate.GetDiffMonth(Double.parseDouble(month), Double.parseDouble(year), getEndMonth, getEndYear);
				            			//NumMonth=this.GetNumMonth(getStartMonth,getStartYear,getEndMonth,getEndYear);
				            			if(AmountArr.length !=0){
				            				delAmount=Double.parseDouble(AmountArr[0][0]);
				            				delTaxAmount=Double.parseDouble(AmountArr[0][1]);
				            			}
				            			if(!(NumMonth <= 0)){
				            				if(getAmount !=0){
				            					getPayAmount=getPayAmount-delAmount;
				            					rsAmount=(getAmount-getPayAmount)/NumMonth;
				            					if(getPayAmount==0)
					            				{
				            						rsPayAmount=rsAmount;
					            					rsTotalAmount=getAmount-rsAmount;
					            				}
				            					else
				            					{
				            						rsPayAmount=getPayAmount+rsAmount;
				            						rsTotalAmount=getAmount-rsPayAmount;
				            					}
				            				}
				            				if(getTaxAmount !=0)
				            				{
				            					getPayTaxAmount=getPayTaxAmount-delTaxAmount;
				            					rsTaxAmount=(getTaxAmount-getPayTaxAmount)/NumMonth;
				            					if(getPayTaxAmount==0)
					            				{
					            					rsPayTaxAmount=rsTaxAmount;
					            					rsTotalTaxAmount=getTaxAmount-rsTaxAmount;
					            				}
				            					else
				            					{
				            						rsPayTaxAmount=getPayTaxAmount+rsTaxAmount;
				            						rsTotalTaxAmount=getTaxAmount-rsPayTaxAmount;
				            					}
				            				}
				            				num++;
					            			
					            			//insert data expense Period เพิ่ม
					            			line_no=expenseCode+"PD"+num;
					            			note_save="EXP_PED Conditon Type: PayMonth Amount :"+getAmount+" Tax Amount:"+getTaxAmount+" Term :"+getTerm;
									       	
									       	sql_insert="INSERT INTO TRN_EXPENSE_DETAIL("
									       	+" HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, DEPARTMENT_CODE, LINE_NO, "
									       	+" EXPENSE_CODE, EXPENSE_SIGN, EXPENSE_ACCOUNT_CODE, TAX_TYPE_CODE, NOTE, "
									       	+" AMOUNT, TAX_AMOUNT, USER_ID, UPDATE_DATE, UPDATE_TIME)"
									       	+" VALUES('"+hospital_code+"', '"+year+"', '"+month+"', '"+doctorCode+"', '"+departmentCode+"', "
									       	+" '"+line_no+"', '"+expenseCode+"', "+getExpSign+", "
									       	+" '"+getExpAccCode+"', '"+taxType+"', '"+getNote+"', "+rsAmount+", "
									       	+rsTaxAmount+", '"+this.user+"', '"+JDate.getDate()+"','"+JDate.getTime()+"')";
									        System.out.println("sql_insert="+sql_insert);
									        
									        try
											{
			                               		cdb.insert(sql_insert);
			                               		cdb.commitDB();
			                               		
			                               		//Update Pay Amount and Total Amount
			                               		sql_update="UPDATE STP_PERIOD_EXPENSE SET "
			                               		+" PAY_AMOUNT="+rsPayAmount+", PAY_TAX_AMOUNT="+rsPayTaxAmount+", "
			                               		+" TOTAL_AMOUNT="+rsTotalAmount+", TOTAL_TAX_AMOUNT="+rsTotalTaxAmount+" "
			                               		+" WHERE HOSPITAL_CODE='"+hospital_code+"' AND DOCTOR_CODE='"+doctorCode+"' "
			                               		+" AND EXPENSE_CODE='"+expenseCode+"' AND TYPE_EXPENSE='2'";
				                               	 try
				 								{
				                                	cdb.insert(sql_update);
				                                	cdb.commitDB();
				                                }
				                                catch(Exception e)
				                     			{
				                                	System.out.println("Update_expense Period Excepiton : "+e+"query="+sql_update);
				                                	cdb.rollDB();
				                                	status=false;
				                     			}
			                               		
			                               	}
			                               	catch(Exception e)
			                    			{
			                               		System.out.println("insert_expense Period Excepiton : "+e+"query="+sql_insert);
			                               		cdb.rollDB();
			                               		status=false;
			                    			}
				            			}
				            			else
				            			{
				            				System.out.println("จำนวนเืืดือนที่จะหารไม่มี");
				            			}
			            			}
			            		 }
						     }
		            		else
		            		{
		            			System.out.println("ไม่พบ Sign ของ Expense จึงไม่สามารถบันทึกและปรับปรุงข้อมูลได้");
		            		}
		            		 
		            	}
		            	else
		            	{
		            		System.out.println("ไม่ได้อยู่ในช่วงระยะเวลาที่ต้องการทำจ่าย");
		            	}
		            	
		            } //for(int i=0; i<PeriodArr.length;i++)
	            }
        		else
        		{
        			 System.out.println("Notfound Data : ไม่พบข้อมูลในการทำจ่าย Expense แบบช่วงเวลา");
        		}
            }
	        catch(Exception e){
	        	 System.out.println("Step 2:1 QUERY STP_PERIOD_EXPENSE Excepiton : "+e);
	            status=false;
	        }
        
        
        return status;
    }
	private String[][] GetPeriod()
	{
        //Query data from table STP_PERIOD_EXPENSE
    	String sql="";
        String[][] PeriodArr = null;
        //type=1 amount<0
        //type=2 amount>0
        //type=3 amount all
        //ExpStatus=1 is revenue
        //ExpStatus=2 is tax
        sql="SELECT STP_PERIOD_EXPENSE.DOCTOR_CODE, STP_PERIOD_EXPENSE.EXPENSE_CODE, STP_PERIOD_EXPENSE.TYPE_EXPENSE, "
        +"STP_PERIOD_EXPENSE.TAX_TYPE, STP_PERIOD_EXPENSE.AMOUNT, STP_PERIOD_EXPENSE.PAY_AMOUNT, STP_PERIOD_EXPENSE.TOTAL_AMOUNT, "
    	+"STP_PERIOD_EXPENSE.TAX_AMOUNT, STP_PERIOD_EXPENSE.PAY_TAX_AMOUNT, STP_PERIOD_EXPENSE.TOTAL_TAX_AMOUNT, "
    	+"STP_PERIOD_EXPENSE.START_TERM_MM, STP_PERIOD_EXPENSE.START_TERM_YYYY, STP_PERIOD_EXPENSE.END_TERM_MM, "
    	+"STP_PERIOD_EXPENSE.END_TERM_YYYY, STP_PERIOD_EXPENSE.NOTE, STP_PERIOD_EXPENSE.DEPARTMENT_CODE ";
        sql+="FROM STP_PERIOD_EXPENSE LEFT OUTER JOIN DOCTOR ON STP_PERIOD_EXPENSE.HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE "
        +"AND STP_PERIOD_EXPENSE.DOCTOR_CODE = DOCTOR.CODE  "
        +" WHERE STP_PERIOD_EXPENSE.HOSPITAL_CODE='"+hospital_code+"' AND STP_PERIOD_EXPENSE.ACTIVE=1 "
        +" ORDER BY STP_PERIOD_EXPENSE.DOCTOR_CODE, STP_PERIOD_EXPENSE.EXPENSE_CODE ";
                
      	System.out.println("sql="+sql);
    	try
        {   
    		PeriodArr = cdb.query(sql);
    		
        }
        catch(Exception e)
        {
            System.out.println("Excepiton Query Data Table STP_PERIOD_EXPENSE : "+e+" query="+sql);
        }
        return PeriodArr;
        
    } 
	private String[][] GetExp(String ExpCode){
        //Query data from table EXPENSE
    	String sql="";
        String[][] ExpArr = null;
        sql="SELECT SIGN, ACCOUNT_CODE FROM EXPENSE "
        +" WHERE HOSPITAL_CODE='"+hospital_code+"' AND ACTIVE=1 "
        +" AND CODE='"+ExpCode+"'";
                
      	System.out.println("sql="+sql);
    	try{   
    		ExpArr = cdb.query(sql);
    		
        }catch(Exception e){
            System.out.println("Excepiton Query Data Table EXPENSE : "+e+" query="+sql);
        }
        return ExpArr;
        
    } 
	private double GetNumMonth(double startMonth, double startYear, double endMonth, double endYear){
			double totalMonth=0, totalDeductMonth=0, totalNum=0;
        	if(startYear==endYear){
	        	totalNum=((endMonth-startMonth)+1)-(Double.parseDouble(month)-startMonth);
	        }else if(Double.parseDouble(year)==endYear){
        		totalNum=(endMonth-Double.parseDouble(month))+1;
	        }else if((Double.parseDouble(month)==startMonth) && (Double.parseDouble(year)==startYear)){
	        	totalNum=(endMonth-Double.parseDouble(month))+1;//จำนวนเดือนที่ทำจ่าย
	        }else{
	        	totalDeductMonth=(12-(12-Double.parseDouble(month)+1))+(12-endMonth);//จำนวนเดือนส่วนเกินในปี
	        	/*
	        	 * (12-(12-startMonth+1)) หากจากต้นปีกี่เดือน
	        	 * (12-Double.parseDouble(month)) หากจากปลายปีกี่เดือน
	        	 * นำทั้ง 2 มาบวกกัน ว่ามีจำนวนเดือนต้นและปลายปีกี่เดือน
	        	 */
	        	totalMonth=((endYear-startYear)+1)*12; //จำนวนเดือนทั้งหมดของปีต้นและปลายที่เลือก
	        	//totalMonth=(Double.parseDouble(year)-startYear)*12;//จำนวนเดือนทั้งหมด
	        	totalNum=totalMonth-totalDeductMonth;//จำนวนเดือนที่ทำจ่าย
	        }
        return totalNum;
        
    } 
}