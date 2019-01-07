package df.bean.tax;

import java.sql.SQLException;
import java.text.DecimalFormat;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.Utils;

public class ProcessTaxWithHolding {
	 
	//caculate Tax 3,14,15
	 public String CalculateTaxWithHolding(String hospital_code,String Doctor,String methodTax,String yyyy,String mm){
		 DBConn cdb ;
		 cdb = new DBConn();  
		 String sql="";
		 String sql_up="";
		 String sql_accu ="";
		 String msg ="Complete" ;
		 try {
			cdb.setStatement();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		 sql=    "SELECT TURN_OUT_AMT FROM SUMMARY_TAX_402 WHERE YYYY+MM='"+yyyy+mm+"' "+
		 		 "AND DOCTOR_CODE='"+Doctor+"' AND HOSPITAL_CODE='"+hospital_code+"'";
		 
		 String taxAmt =  cdb.getSingleData(sql);
		 String calTaxAmt = new DecimalFormat(".00").format((Double.parseDouble(taxAmt)*Double.parseDouble(methodTax))/100);
		 
		 sql_up="UPDATE SUMMARY_TAX_402 SET NET_TAX_MONTH = CASE WHEN HOSPITAL_CODE = '045' THEN ROUND("+calTaxAmt+",0) ELSE '"+calTaxAmt+"' END, SUM_NORMAL_TAX_AMT=TURN_OUT_AMT " 
				 + ", TEXT_NET_TAX_MONTH = CASE WHEN HOSPITAL_CODE = '045' THEN '"+Utils.toThaiMoney((double)(Math.round(Double.parseDouble(calTaxAmt))))+"' ELSE '"+Utils.toThaiMoney(calTaxAmt)+"' END " 
				 + " WHERE HOSPITAL_CODE='"+hospital_code+"' AND YYYY+MM='"+yyyy+mm+"' AND DOCTOR_CODE='"+Doctor+"' ";

		 sql_accu="UPDATE SUMMARY_TAX_402 SET ACCU_NORMAL_TAX_MONTH = SUM_NORMAL_TAX_AMT, "
		 		+ "NORMAL_TAX_MONTH = NET_TAX_MONTH "
		 		+ "WHERE HOSPITAL_CODE='"+hospital_code+"' AND YYYY+MM='"+yyyy+mm+"' AND DOCTOR_CODE='"+Doctor+"' ";
		 
		if(methodTax != ""){
			try{
				 cdb.insert(sql_up);
				 cdb.commitDB();
				 cdb.insert(sql_accu);
				 cdb.commitDB();
				 msg = "update_complete";
			 }catch(Exception e){
				 System.out.println(e);
				 msg="Error  " + e;
			 }
		} 
		if(msg.equals("update_complete")){
			insertExpenseTax(Doctor, yyyy, mm, hospital_code);
			msg= "Complete";
		}else{
			 msg="Error Calulate Tax ";
		}
		cdb.closeDB(""); 
		return msg;
	 }
	 
	 public boolean insertExpenseTax(String doctor,String year,String month,String hospital){
		 DBConn cdb ;
		 cdb = new DBConn();
		 try {
			cdb.setStatement();
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
			"HOSPITAL_CODE,DOCTOR_CODE,DOC_NO," +
			"LINE_NO, DOC_DATE,AMOUNT,TAX_AMOUNT,EXPENSE_SIGN," +
			"EXPENSE_ACCOUNT_CODE,EXPENSE_CODE,TAX_TYPE_CODE," +
			"YYYY,MM,NOTE,USER_ID) " +
			"SELECT SM.HOSPITAL_CODE, SM.DOCTOR_CODE, " +
			"'TAX 40(2)','40201',SM.YYYY+SM.MM+'01', SM.NET_TAX_MONTH,'0','-1', "+
			"EX.ACCOUNT_CODE, EX.CODE, EX.TAX_TYPE_CODE, SM.YYYY, SM.MM, 'Expense Tax of Month ', 'ProcessTax' "+
			"FROM SUMMARY_TAX_402 SM LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE " +
			"WHERE SM.HOSPITAL_CODE = '"+hospital+"' AND " +
			"EX.ADJUST_TYPE = 'TX' AND "+
			"SM.DOCTOR_CODE = '"+doctor+"' AND "+
	    	"SM.NET_TAX_MONTH > 0 AND SM.YYYY = '"+year+"' AND SM.MM = '"+month+"'";
	    	//System.out.println("INSERT EXP : "+s);
	    	try{
	    		cdb.insert(s);
	    		cdb.commitDB();
	    	}catch (Exception e){
	    		cdb.rollDB();
	    	}
	    	return true;
	    }
}
