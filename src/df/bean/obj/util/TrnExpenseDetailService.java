package df.bean.obj.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Doctor;
import df.bean.db.table.TrnExpenseDetail;

public class TrnExpenseDetailService {
	
	
	public ArrayList<TrnExpenseDetail> GetExpensesDetailByDoctorCode(String doctorCode,String hospitalCode,String YYYY,String MM){
		
		String SQL_EXPENSE = "select distinct "+
				"  TRN_EXPENSE_DETAIL.DOCTOR_CODE as DOCTOR_CODE"+
                " ,TRN_EXPENSE_DETAIL.AMOUNT as AMOUNT " +
				" ,TRN_EXPENSE_DETAIL.LINE_NO as LINE_NO"+
                " ,TRN_EXPENSE_DETAIL.EXPENSE_SIGN as EXPENSE_SIGN"+
                " from TRN_EXPENSE_DETAIL"  +
                " where TRN_EXPENSE_DETAIL.DOCTOR_CODE = '"+doctorCode+"' and TRN_EXPENSE_DETAIL.HOSPITAL_CODE = '"+hospitalCode+"'" +
                " and TRN_EXPENSE_DETAIL.YYYY = '"+YYYY+"' and TRN_EXPENSE_DETAIL.MM = '"+MM+"' "+
                "order by TRN_EXPENSE_DETAIL.AMOUNT";
		
		return GetTrnExpensesDetail(SQL_EXPENSE);
	}
	
	 private ArrayList<TrnExpenseDetail> GetTrnExpensesDetail(String sql) {
	        Statement stmt = null;
	        ResultSet rs = null;
	        String className = "", doctorCode = "";
	        ArrayList<TrnExpenseDetail> resultExpensesDetail = new ArrayList<TrnExpenseDetail>();

	        try {
	        	DBConnection objConn  =  new DBConnection();
	        	objConn.connectToLocal();
	            rs = objConn.executeQuery(sql);
	            while (rs.next()) {
	                try {
	                	resultExpensesDetail.add(new TrnExpenseDetail(rs.getString("DOCTOR_CODE"), Double.parseDouble(rs.getString("AMOUNT"))
	                			,rs.getString("LINE_NO"),Integer.parseInt(rs.getString("EXPENSE_SIGN"))));
	                } catch(Exception ex) {
	                    ex.printStackTrace();
	                }
	                
	            }

	        }catch (SQLException e) {    e.printStackTrace();       }        
	        finally {
	                try {
	                    if (rs != null) { 
	                        rs.close();
	                        rs = null;
	                    }
	                    if (stmt != null) {
	                        stmt.close();
	                        stmt = null;
	                    }
	                } catch (SQLException ex) {      ex.printStackTrace();          }
	        }
	        
	        return resultExpensesDetail;
	    }
	 
	 public void UpdateOLD_DOCTOR_CODE(String YYYY,String MM,String HOSPITAL_CODE){
		 DBConnection objConn  =  new DBConnection();
		 objConn.connectToLocal();
		 int rs = 0;
	     
		 String SQL_UPDATE_OLD_DOCTOR_CODE =
				 "UPDATE "+
				   "TRN_EXPENSE_DETAIL "+
				"SET "+
				    "TRN_EXPENSE_DETAIL.OLD_DOCTOR_CODE = TRN_EXPENSE_DETAIL.DOCTOR_CODE "+
				"WHERE "+
				    "TRN_EXPENSE_DETAIL.YYYY = '"+YYYY+"' and TRN_EXPENSE_DETAIL.MM = '"+MM+"' "+
				    "and TRN_EXPENSE_DETAIL.HOSPITAL_CODE = '"+HOSPITAL_CODE+"' ";
		 
		 System.out.println("Update old_DrCode : "+SQL_UPDATE_OLD_DOCTOR_CODE);

		 try {
	            rs = objConn.executeUpdate(SQL_UPDATE_OLD_DOCTOR_CODE);
	     } catch (Exception e){ 
	    	 System.out.println("Update Exception : "+e); 
	     } finally {
	    	 objConn.Close();
	     }
	 }
	 
	 public void UpdateDoctorCodeOfExpenseDetail(String YYYY,String MM,String doctorCode,String hospitalCode,String lindNumber,String newDoctorCode){
		 String SQL_UPDATE_DOCTOR_CODE =
				"UPDATE "+
				    "TRN_EXPENSE_DETAIL "+
				"SET "+
				    "TRN_EXPENSE_DETAIL.DOCTOR_CODE = '"+newDoctorCode+"' "+
				"WHERE "
				+ "TRN_EXPENSE_DETAIL.OLD_DOCTOR_CODE = '"+doctorCode+"' "
				+ "and TRN_EXPENSE_DETAIL.PAYMENT_TERM != '1' "
				+ "and TRN_EXPENSE_DETAIL.YYYY = '"+YYYY+"' "
				+ "and TRN_EXPENSE_DETAIL.MM = '"+MM+"' "
				+ "and TRN_EXPENSE_DETAIL.HOSPITAL_CODE = '"+hospitalCode+"' "
				+ "and TRN_EXPENSE_DETAIL.LINE_NO = '"+lindNumber+"' "; 
		 try {
	        	DBConnection objConn  =  new DBConnection();
	        	objConn.connectToLocal();
	            objConn.executeUpdate(SQL_UPDATE_DOCTOR_CODE);
	     }catch(Exception e){
	    	 
	     }
		 
	 }
	 
	 public double GetNetOfDoctorCode(ArrayList<HashMap<String,String>> maps,String drCode){
		
		 
		 for(HashMap<String, String> token:maps){//To find Net of docCodeTokenOfDPF by keep all HashMap related with the DrCode
				if(drCode.equals(token.get("DOCTOR_CODE"))){
						return Double.parseDouble(token.get("DR_NET_PAID_AMT"));
				}
			}
		 return 0;
	 }
	 

}
