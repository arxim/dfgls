package df.bean.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.DataFile;

public class ProcessTranferRevenue implements ProcessMaster{
	
	private String userId = null;
	private String hospitalCode = null;
	private String transactionDateForm  = null;
	private String transactionDateTo = null;
	private DataFile dataFile = null;
	private List list = null;
	private DBConnection  dbCondition = null; 
	
	/**
	 *  this config file data for read file from config.
	 */
	private String tableName =  "DF_TRANFER";
	private String[] dataFields = { "HOSPITAL_CODE"  , "DOCTOR_FROM" , "DOCTOR_TO" , "ADMISSION" , "PERCENT" };
	private Map mapCondition = null;

	public ProcessTranferRevenue(String hospitalCode , String userId , String transactionDateForm  , String transactionDateTo) {
		
		/**
		 *  @TODO set default value config
		 */
		
		this.transactionDateForm = transactionDateForm;
		this.transactionDateTo = transactionDateTo;
		this.userId = userId;
		this.hospitalCode = hospitalCode;
		
		this.dbCondition  = new DBConnection(hospitalCode);
		this.dbCondition.connectToLocal();
		
		// this.dataFile =  new DataFile();
		
		// this.list = this.dataFile.listData(tableName, mapCondition);
		
		this.list = this.getDataCondition();
		
	}
	

//	public static void main(String[] arge){
//		
//		ProcessTranferRevenue obj = new ProcessTranferRevenue("011", "emp011" , "20130901" , "20130931");
//		
//	    //obj.doProcess();
//		
//	    obj.doRollback();
//	}
//	
	/**
	 * @TODO Do process  tranfer df revenue.
	 */
	@Override
	public boolean doProcess() {
		
		boolean action  = false;
		
		
		try { 
			
			System.out.println("Process Tranfer revenue DF  ---  Start date : " +  this.transactionDateForm  +  " :  End date : " + this.transactionDateTo );
			
			// Rollback data befor run process
			this.doRollback();
			
			for(int i = 0; i < this.list.size(); i++){
				
				Map<String , String> mapDataField = (Map<String , String>) this.list.get(i);
				
				System.out.println( i +  " :  " + this.getSqlCommand(mapDataField));
				
				action = this.actionProcess(this.getSqlCommand(mapDataField));
				
			}
			
		}catch (Exception ex){ 
			ex.printStackTrace();
			System.out.println("Error: Process Tranfer Revenue " + ex.getMessage().toString());
		}
		
		return action;
	}

	/**
	 * @TODO Process Rollback Process Tranfer data DF.
	 */
	@Override
	public boolean doRollback() {
		boolean action = false;
		try { 
			
			System.out.println(getSqlCommandRollback(this.mapCondition));
			
			if(this.dbCondition.executeUpdate(getSqlCommandRollback(this.mapCondition)) != -1 ){ 
				action = true;
			} else { 
				action = false;
			}
			
		}catch (Exception ex){ 
			ex.printStackTrace();
			System.out.println(ex.getMessage().toString());
		}
		
		return action;
	}

	@Override
	public boolean doBatchClose() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private boolean actionProcess(String sqlCommand){ 
		boolean action = false;
		
		try{ 
			
			if(this.dbCondition != null){
			
				System.out.println(" :  " + sqlCommand);
				
				if(this.dbCondition.executeUpdate(sqlCommand) != -1) { 
					action = true;
				}  else {
					action = false;
				}
				
			} else { 
				System.out.println("Error");
			}
			
		} catch(Exception ex){ 
			ex.printStackTrace();
			System.out.println(ex.getMessage().toString());
		}
	
		// System.out.println("--- Process Tranfer Revenue ---");
		
		return action;
	}
	
	/**
	 * @TODO sql process 
	 * @param fieds
	 * @return
	 */
	private String getSqlCommand(Map<String , String> fieds ){ 
		return "UPDATE TRN_DAILY SET " + 
			   "DOCTOR_CODE = '" + fieds.get("DOCTOR_TO").trim() +  "'  ," +  
			   "USER_ID = USER_ID + ':TrnFerDF' "+ 
			   "WHERE" + 
			   " DOCTOR_CODE  = '"+ fieds.get("DOCTOR_FROM").trim() +"' AND " + 
			   " ( TRANSACTION_DATE BETWEEN  '" + this.transactionDateForm + "' AND '" + this.transactionDateTo + "' ) AND" + 
			   " HOSPITAL_CODE = '" + fieds.get("HOSPITAL_CODE").trim() + "' AND " +
			   " ADMISSION_TYPE_CODE = '"+ fieds.get("ADMISSION_TYPE").trim() +"' AND " + 
			   " COMPUTE_DAILY_DATE = '' AND " + 
			   " COMPUTE_DAILY_TIME = '' AND " +
			   " BATCH_NO = '' AND " +
			   " USER_ID NOT LIKE '%:TrnFerDF%' AND " + 
			   " INVOICE_TYPE <> 'ORDER' ";
	}
	
	/**
	 * @TODO sql rollback process
	 * @param fieds
	 * @return
	 */
	private String getSqlCommandRollback(Map<String , String> fieds){ 
		return "UPDATE TRN_DAILY SET " + 
				   "DOCTOR_CODE = OLD_DOCTOR_CODE  ," +  
				   "USER_ID = REPLACE(USER_ID , ':TrnFerDF' , '')"+ 
				   "WHERE" + 
				   " ( TRANSACTION_DATE BETWEEN  '" + this.transactionDateForm + "' AND '" + this.transactionDateTo + "' ) AND" + 
				   " HOSPITAL_CODE = '" + this.hospitalCode + "' AND " + 
				   " COMPUTE_DAILY_DATE = '' AND " + 
				   " COMPUTE_DAILY_TIME = '' AND " +
				   " BATCH_NO = '' AND " +
				   " USER_ID LIKE '%:TrnFerDF%' AND " + 
				   " INVOICE_TYPE <> 'ORDER' ";
	}
	
	/**
	 * This get data condition data. 
	 * 
	 * @return
	 * @throws SQLException
	 */
	private List getDataCondition() {
		
			DBConnection conn = new DBConnection();
			conn.connectToLocal();
			
			List<Map> dataList = new ArrayList<Map>();
			
			String sqlCommand  = "SELECT * FROM STP_TRANFER_DF WHERE HOSPITAL_CODE  = '" + this.hospitalCode + "' ";
			
			ResultSet rs = conn.executeQuery(sqlCommand);
			
			try {
				while (rs.next()) {
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("DOCTOR_FROM", rs.getString("DOCTOR_FROM"));
					dataMap.put("DOCTOR_TO", rs.getString("DOCTOR_TO"));
					dataMap.put("ADMISSION_TYPE" , rs.getString("ADMISSION_TYPE"));
					dataMap.put("HOSPITAL_CODE" , rs.getString("HOSPITAL_CODE"));
					dataList.add(dataMap);
				}
			} catch (Exception e) {
				System.out.println("Process Tranfer DF : " +  e);
			}
			
			return dataList;
	}
}
