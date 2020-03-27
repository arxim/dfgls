package df.bean.process;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;

public class ProcessTimeTableCase {
	
	final static Logger logger = Logger.getLogger(ProcessTimeTableCase.class);
	
	private DBConnection conn;
	private String hospitalCode;
	private String userId;
	String sqlCommand="";
	
	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public ProcessTimeTableCase(){
		this.conn = new DBConnection();
		try {
			conn.connectToLocal();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		}
    }
	
	public void initProcessMappingCase(String hospitalCode, String userId) {
		this.hospitalCode=hospitalCode;
		this.userId=userId;
	}
	
	public boolean runTimeTableCase(String yyyy, String mm) {
		boolean status = false;
		if(doRollback(yyyy, mm)) {
			status = updateGuaranteeNote(yyyy, mm);
			if(status) {
				status = updatePackageCase(yyyy, mm);
			}
			if(status) {
				status = updateOverCase(yyyy, mm);
			}
			if(status) {
				status = updateCompareCase(yyyy, mm);
			}
			if(status) {
				status = updateCalculateAmount(yyyy, mm);
			}
			if(status) {
				status = insertMappingCaseExpenseDetail(yyyy, mm);
			}
		}
		return status;
	}
	
	public boolean updateGuaranteeNote(String yyyy, String mm) {
//		DBConnection conn = new DBConnection();
		boolean status = false;
		String sqlUpdate ="";
		try {
//			conn.connectToLocal();
			sqlUpdate = "UPDATE TD SET TD.GUARANTEE_NOTE='MAPPING_CASE'\r\n" + 
					"FROM STP_TIME_TABLE_CASE STTC\r\n" + 
					"LEFT JOIN STP_PACKAGE_ITEM_MAPPING SPIM ON STTC.HOSPITAL_CODE=SPIM.HOSPITAL_CODE AND STTC.PACKAGE_CODE=SPIM.PACKAGE_CODE\r\n" + 
					"LEFT JOIN TRN_DAILY TD ON STTC.HOSPITAL_CODE=TD.HOSPITAL_CODE AND STTC.DOCTOR_CODE=TD.DOCTOR_CODE\r\n" + 
					"LEFT JOIN MST_PACKAGE MP ON STTC.HOSPITAL_CODE=MP.HOSPITAL_CODE AND STTC.PACKAGE_CODE=MP.PACKAGE_CODE\r\n" + 
					"LEFT JOIN MST_SHIFT_CASE MSC ON STTC.HOSPITAL_CODE=MSC.HOSPITAL_CODE AND STTC.CASE_CODE=MSC.CASE_CODE\r\n" + 
					"WHERE STTC.HOSPITAL_CODE='"+this.hospitalCode+"' \r\n" + 
					"AND STTC.YYYY+STTC.MM='"+yyyy+mm+"'\r\n" + 
					"AND MSC.CAL_TYPE='OVER'\r\n" + 
					"AND TD.GUARANTEE_NOTE = ''\r\n" + 
					"AND TD.ORDER_ITEM_CODE=SPIM.ORDER_ITEM_CODE\r\n" + 
					"AND TD.VERIFY_DATE BETWEEN STTC.START_DATE AND STTC.END_DATE\r\n" + 
					"AND TD.VERIFY_TIME BETWEEN STTC.START_TIME AND STTC.END_TIME\r\n"+
					"AND SPIM.ACTIVE='1' AND MP.ACTIVE='1' AND MSC.ACTIVE='1'";
			conn.executeUpdate(sqlUpdate);
			
			sqlUpdate = "UPDATE TD SET TD.GUARANTEE_NOTE='MAPPING_CASE'\r\n" + 
					"FROM  STP_TIME_TABLE_CASE STTC\r\n" + 
					"LEFT JOIN TRN_DAILY TD ON TD.HOSPITAL_CODE=STTC.HOSPITAL_CODE AND TD.DOCTOR_CODE=STTC.DOCTOR_CODE\r\n" + 
					"LEFT JOIN MST_SHIFT_CASE MSC ON STTC.HOSPITAL_CODE=MSC.HOSPITAL_CODE AND STTC.CASE_CODE=MSC.CASE_CODE\r\n" + 
					"LEFT JOIN MST_PACKAGE MP ON STTC.HOSPITAL_CODE=MP.HOSPITAL_CODE AND STTC.PACKAGE_CODE=MP.PACKAGE_CODE\r\n" + 
					"WHERE TD.HOSPITAL_CODE='"+this.hospitalCode+"'\r\n" + 
					"AND STTC.YYYY+STTC.MM='"+yyyy+mm+"'\r\n" + 
					"AND MSC.CAL_TYPE='OVER'\r\n" + 
					"AND TD.GUARANTEE_NOTE = ''\r\n" + 
					"AND TD.VERIFY_DATE BETWEEN STTC.START_DATE AND STTC.END_DATE\r\n" + 
					"AND TD.VERIFY_TIME BETWEEN STTC.START_TIME AND STTC.END_TIME\r\n" + 
					"AND TD.ORDER_ITEM_CODE NOT IN (SELECT DISTINCT ORDER_ITEM_CODE FROM STP_PACKAGE_ITEM_MAPPING WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND ACTIVE='1')\r\n"+
					"AND MP.ACTIVE='1' AND MSC.ACTIVE='1'";
			conn.executeUpdate(sqlUpdate);
			
			sqlUpdate = "UPDATE TD SET TD.GUARANTEE_NOTE='MAPPING_CASE'  \r\n" + 
					"FROM  STP_TIME_TABLE_CASE STTC\r\n" + 
					"LEFT JOIN TRN_DAILY TD ON TD.HOSPITAL_CODE=STTC.HOSPITAL_CODE AND TD.DOCTOR_CODE=STTC.DOCTOR_CODE\r\n" + 
					"LEFT JOIN MST_SHIFT_CASE MSC ON STTC.HOSPITAL_CODE=MSC.HOSPITAL_CODE AND STTC.CASE_CODE=MSC.CASE_CODE\r\n" + 
					"LEFT JOIN STP_MAPPING_CASE SMC ON MSC.HOSPITAL_CODE=SMC.HOSPITAL_CODE AND MSC.CASE_MAPPING_CODE=SMC.CASE_MAPPING_CODE\r\n" + 
					"LEFT JOIN ORDER_ITEM OI ON TD.HOSPITAL_CODE=OI.HOSPITAL_CODE AND TD.ORDER_ITEM_CODE=OI.CODE\r\n" + 
					"WHERE TD.HOSPITAL_CODE='"+this.hospitalCode+"'\r\n" + 
					"AND STTC.YYYY+STTC.MM='"+yyyy+mm+"'\r\n" + 
					"AND MSC.CAL_TYPE='COMPARE'\r\n" + 
					"AND TD.GUARANTEE_NOTE = ''\r\n" + 
					"AND ( (TD.VERIFY_DATE=STTC.START_DATE AND TD.VERIFY_TIME BETWEEN STTC.START_TIME AND '235959') OR ( TD.VERIFY_DATE=STTC.END_DATE AND TD.VERIFY_TIME BETWEEN '000000' AND STTC.END_TIME ) )\r\n" + 
					"AND TD.ORDER_ITEM_CODE NOT IN (\r\n" + 
					"SELECT DISTINCT ORDER_ITEM_CODE FROM STP_PACKAGE_ITEM_MAPPING WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND ACTIVE='1'\r\n" + 
					")\r\n" + 
					"AND TD.ORDER_ITEM_CODE=SMC.ORDER_ITEM_CODE\r\n" + 
					"--AND TD.PAYOR_OFFICE_CATEGORY_CODE=SMC.PAYOR_OFFICE_CATEGORY_CODE\r\n" + 
					"AND TD.ADMISSION_TYPE_CODE=SMC.ADMISSION_TYPE_CODE\r\n"+
					"AND MSC.ACTIVE='1' AND SMC.ACTIVE='1' AND OI.ACTIVE='1'";
			conn.executeUpdate(sqlUpdate);
			status = true;
		}catch (Exception e) {
			// TODO: handle exception
			status = false;
			TRN_Error.setHospital_code(this.hospitalCode);
			TRN_Error.setUser_name(this.userId);
			TRN_Error.writeErrorLog(conn.getConnection(), "Run Time Table Case Process",  "Update GUARANTEE_NOTE in TRN_DAILY Error", e.getMessage(), sqlUpdate,"");
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean updatePackageCase(String yyyy,String mm) {
//		DBConnection conn = new DBConnection();
		boolean status = false;
		String sqlSelect = "";
		String sqlUpdate = "";
		try {
//			conn.connectToLocal();
			sqlSelect = "SELECT STTC.START_DATE,STTC.DOCTOR_CODE,STTC.CASE_CODE,STTC.PACKAGE_CODE,MP.NUM_CASE,MP.PAY_CASE, COUNT(*) COUNT_CASE\r\n" + 
					"FROM STP_TIME_TABLE_CASE STTC\r\n" + 
					"LEFT JOIN STP_PACKAGE_ITEM_MAPPING SPIM ON STTC.HOSPITAL_CODE=SPIM.HOSPITAL_CODE AND STTC.PACKAGE_CODE=SPIM.PACKAGE_CODE\r\n" + 
					"LEFT JOIN TRN_DAILY TD ON STTC.HOSPITAL_CODE=TD.HOSPITAL_CODE AND STTC.DOCTOR_CODE=TD.DOCTOR_CODE\r\n" + 
					"LEFT JOIN MST_PACKAGE MP ON STTC.HOSPITAL_CODE=MP.HOSPITAL_CODE AND STTC.PACKAGE_CODE=MP.PACKAGE_CODE\r\n" + 
					"LEFT JOIN MST_SHIFT_CASE MSC ON STTC.HOSPITAL_CODE=MSC.HOSPITAL_CODE AND STTC.CASE_CODE=MSC.CASE_CODE\r\n" + 
					"WHERE STTC.HOSPITAL_CODE='"+this.hospitalCode+"' \r\n" + 
					"AND STTC.YYYY+STTC.MM='"+yyyy+mm+"'\r\n" + 
					"AND MSC.CAL_TYPE='OVER'\r\n" + 
					"AND TD.GUARANTEE_NOTE='MAPPING_CASE'\r\n" + 
					"AND TD.ORDER_ITEM_CODE=SPIM.ORDER_ITEM_CODE\r\n" + 
					"AND TD.VERIFY_DATE BETWEEN STTC.START_DATE AND STTC.END_DATE\r\n" + 
					"AND TD.VERIFY_TIME BETWEEN STTC.START_TIME AND STTC.END_TIME\r\n" + 
					"AND SPIM.ACTIVE='1' AND MP.ACTIVE='1' AND MSC.ACTIVE='1'\r\n" + 
					"GROUP BY STTC.START_DATE,STTC.DOCTOR_CODE,STTC.CASE_CODE,STTC.PACKAGE_CODE,MP.NUM_CASE,MP.PAY_CASE";
			
			ResultSet rs = conn.executeQuery(sqlSelect);
			ArrayList<HashMap<String, String>> mapList = new ArrayList<>();
			while(rs.next()) {
				HashMap<String, String> hm = new HashMap<>();
				hm.put("START_DATE", rs.getString("START_DATE"));
				hm.put("DOCTOR_CODE", rs.getString("DOCTOR_CODE"));
				hm.put("CASE_CODE", rs.getString("CASE_CODE"));
				hm.put("PACKAGE_CODE", rs.getString("PACKAGE_CODE"));
				hm.put("NUM_CASE", rs.getString("NUM_CASE"));
				hm.put("PAY_CASE", rs.getString("PAY_CASE"));
				hm.put("COUNT_CASE", rs.getString("COUNT_CASE"));
				mapList.add(hm);
			}
			
			int numPackageCase = 0;
			int mod = 0;
			
			for(int i=0;i<mapList.size();i++) {
//				System.out.println("COUNT_CASE = "+ mapList.get(i).get("COUNT_CASE"));
//				System.out.println("mod >> "+Integer.parseInt(mapList.get(i).get("COUNT_CASE")) %  Integer.parseInt(mapList.get(i).get("NUM_CASE")));
				mod = Integer.parseInt(mapList.get(i).get("COUNT_CASE")) %  Integer.parseInt(mapList.get(i).get("NUM_CASE"));
				
				if(mod == 0) {
					//(count_case/num_case)*pay_case
					numPackageCase =  ((Integer.parseInt(mapList.get(i).get("COUNT_CASE"))/Integer.parseInt(mapList.get(i).get("NUM_CASE")))*Integer.parseInt(mapList.get(i).get("PAY_CASE")));
				}else {
					//((count_case/num_case)*pay_case) +  (mod-1) // เพิ่มขึ้นครั้งละ1
					numPackageCase =  ((Integer.parseInt(mapList.get(i).get("COUNT_CASE"))/Integer.parseInt(mapList.get(i).get("NUM_CASE")))*Integer.parseInt(mapList.get(i).get("PAY_CASE"))) + (mod - 1);
				}
//				System.out.println(numPackageCase);
				mapList.get(i).put("NUM_PACKAGE_CASE", String.valueOf(numPackageCase));
				
				sqlUpdate = "UPDATE STP_TIME_TABLE_CASE SET NUM_PACKAGE_CASE='"+numPackageCase+"', UPDATE_DATE='"+JDate.getDate()+"', UPDATE_TIME='"+JDate.getTime()+"', USER_ID='"+this.userId+"' \r\n" + 
						"WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND YYYY+MM='"+yyyy+mm+"' AND START_DATE='"+mapList.get(i).get("START_DATE")+"' AND DOCTOR_CODE='"+mapList.get(i).get("DOCTOR_CODE")+"' AND CASE_CODE='"+mapList.get(i).get("CASE_CODE")+"' AND PACKAGE_CODE='"+mapList.get(i).get("PACKAGE_CODE")+"'";
				conn.executeUpdate(sqlUpdate);
			}
			status = true;
		}catch (Exception e) {
			// TODO: handle exception
			status = false;
			TRN_Error.setHospital_code(this.hospitalCode);
			TRN_Error.setUser_name(this.userId);
			TRN_Error.writeErrorLog(conn.getConnection(), "Run Time Table Case Process",  "Update calculate NUM_PACKAGE_CASE in STP_TIME_TABLE_CASE Error", e.getMessage(), sqlUpdate,"");
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean updateOverCase(String yyyy,String mm) {
//		DBConnection conn = new DBConnection();
		boolean status = false;
		String sqlSelect = "";
		String sqlUpdate = "";
		try {
//			conn.connectToLocal();
			// OVER CASE
			sqlSelect = "SELECT  STTC.START_DATE,TD.DOCTOR_CODE,STTC.CASE_CODE,STTC.PACKAGE_CODE\r\n" + 
					",MSC.MAX_CASE,MSC.AMOUNT,MSC.AMOUNT_PER_CASE, COUNT(*) COUNT_OVER_CASE\r\n" + 
					"FROM  STP_TIME_TABLE_CASE STTC\r\n" + 
					"LEFT JOIN TRN_DAILY TD ON TD.HOSPITAL_CODE=STTC.HOSPITAL_CODE AND TD.DOCTOR_CODE=STTC.DOCTOR_CODE\r\n" + 
					"LEFT JOIN MST_SHIFT_CASE MSC ON STTC.HOSPITAL_CODE=MSC.HOSPITAL_CODE AND STTC.CASE_CODE=MSC.CASE_CODE\r\n" + 
					"LEFT JOIN MST_PACKAGE MP ON STTC.HOSPITAL_CODE=MP.HOSPITAL_CODE AND STTC.PACKAGE_CODE=MP.PACKAGE_CODE\r\n" + 
					"WHERE TD.HOSPITAL_CODE='"+this.hospitalCode+"'\r\n" + 
					"AND STTC.YYYY+STTC.MM='"+yyyy+mm+"'\r\n" + 
					"AND TD.GUARANTEE_NOTE='MAPPING_CASE'\r\n" + 
					"AND MSC.CAL_TYPE='OVER'\r\n" + 
					"AND MSC.CASE_TYPE='TRANSACTION'\r\n" + 
					"AND TD.VERIFY_DATE BETWEEN STTC.START_DATE AND STTC.END_DATE\r\n" + 
					"AND TD.VERIFY_TIME BETWEEN STTC.START_TIME AND STTC.END_TIME\r\n" + 
					"AND TD.ORDER_ITEM_CODE NOT IN (SELECT DISTINCT ORDER_ITEM_CODE FROM STP_PACKAGE_ITEM_MAPPING WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND ACTIVE='1')\r\n" + 
					"AND MSC.ACTIVE='1' AND MP.ACTIVE='1'\r\n" + 
					"GROUP BY  STTC.START_DATE,TD.DOCTOR_CODE,STTC.CASE_CODE,STTC.PACKAGE_CODE,MSC.MAX_CASE,MSC.AMOUNT,MSC.AMOUNT_PER_CASE\r\n"+
					"UNION ALL\r\n" + 
					"SELECT START_DATE,DOCTOR_CODE,CASE_CODE,PACKAGE_CODE\r\n" + 
					",MAX_CASE,AMOUNT,AMOUNT_PER_CASE, COUNT(*) COUNT_OVER_CASE FROM (\r\n" + 
					"SELECT DISTINCT STTC.START_DATE,TD.DOCTOR_CODE,STTC.CASE_CODE,STTC.PACKAGE_CODE\r\n" + 
					",MSC.MAX_CASE,MSC.AMOUNT,MSC.AMOUNT_PER_CASE,TD.EPISODE_NO\r\n" + 
					"FROM  STP_TIME_TABLE_CASE STTC\r\n" + 
					"LEFT JOIN TRN_DAILY TD ON TD.HOSPITAL_CODE=STTC.HOSPITAL_CODE AND TD.DOCTOR_CODE=STTC.DOCTOR_CODE\r\n" + 
					"LEFT JOIN MST_SHIFT_CASE MSC ON STTC.HOSPITAL_CODE=MSC.HOSPITAL_CODE AND STTC.CASE_CODE=MSC.CASE_CODE\r\n" + 
					"LEFT JOIN MST_PACKAGE MP ON STTC.HOSPITAL_CODE=MP.HOSPITAL_CODE AND STTC.PACKAGE_CODE=MP.PACKAGE_CODE\r\n" + 
					"WHERE TD.HOSPITAL_CODE='"+this.hospitalCode+"'\r\n" + 
					"AND STTC.YYYY+STTC.MM='"+yyyy+mm+"'\r\n" + 
					"AND TD.GUARANTEE_NOTE='MAPPING_CASE'\r\n" + 
					"AND MSC.CAL_TYPE='OVER'\r\n" + 
					"AND MSC.CASE_TYPE='EPISODE'\r\n" + 
					"AND TD.VERIFY_DATE BETWEEN STTC.START_DATE AND STTC.END_DATE\r\n" + 
					"AND TD.VERIFY_TIME BETWEEN STTC.START_TIME AND STTC.END_TIME\r\n" + 
					"AND TD.ORDER_ITEM_CODE NOT IN (SELECT DISTINCT ORDER_ITEM_CODE FROM STP_PACKAGE_ITEM_MAPPING WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND ACTIVE='1')\r\n" + 
					"AND MSC.ACTIVE='1' AND MP.ACTIVE='1'\r\n" + 
					") T GROUP BY START_DATE,DOCTOR_CODE,CASE_CODE,PACKAGE_CODE,MAX_CASE,AMOUNT,AMOUNT_PER_CASE";
			
			ResultSet rs = conn.executeQuery(sqlSelect);
			ArrayList<HashMap<String, String>> mapList = new ArrayList<>();
			while(rs.next()) {
				HashMap<String, String> hm = new HashMap<>();
				hm.put("START_DATE", rs.getString("START_DATE"));
				hm.put("DOCTOR_CODE", rs.getString("DOCTOR_CODE"));
				hm.put("CASE_CODE", rs.getString("CASE_CODE"));
				hm.put("PACKAGE_CODE", rs.getString("PACKAGE_CODE"));
				hm.put("MAX_CASE", rs.getString("MAX_CASE"));
				hm.put("AMOUNT", rs.getString("AMOUNT"));
				hm.put("AMOUNT_PER_CASE", rs.getString("AMOUNT_PER_CASE"));
				hm.put("COUNT_OVER_CASE", rs.getString("COUNT_OVER_CASE"));
				mapList.add(hm);
			}
			
			for(int i=0;i<mapList.size();i++) {
				sqlUpdate = "UPDATE STP_TIME_TABLE_CASE SET NUM_CASE='"+mapList.get(i).get("COUNT_OVER_CASE")+"', UPDATE_DATE='"+JDate.getDate()+"', UPDATE_TIME='"+JDate.getTime()+"', USER_ID='"+this.userId+"' \r\n" + 
						"WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND YYYY+MM='"+yyyy+mm+"' AND START_DATE='"+mapList.get(i).get("START_DATE")+"' AND DOCTOR_CODE='"+mapList.get(i).get("DOCTOR_CODE")+"' AND CASE_CODE='"+mapList.get(i).get("CASE_CODE")+"' AND PACKAGE_CODE='"+mapList.get(i).get("PACKAGE_CODE")+"'";
				conn.executeUpdate(sqlUpdate);
			}
			status = true;
		}catch (Exception e) {
			// TODO: handle exception
			status = false;
			TRN_Error.setHospital_code(this.hospitalCode);
			TRN_Error.setUser_name(this.userId);
			TRN_Error.writeErrorLog(conn.getConnection(), "Run Time Table Case Process",  "Update calculate NUM_PACKAGE_CASE in STP_TIME_TABLE_CASE Error", e.getMessage(), sqlUpdate,"");
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean updateCompareCase(String yyyy,String mm) {
//		DBConnection conn = new DBConnection();
		boolean status = false;
		String sqlSelect = "";
		String sqlUpdate = "";
		try {
//			conn.connectToLocal();
			 sqlSelect = "SELECT START_DATE,DOCTOR_CODE\r\n" + 
		        		",CASE_CODE,PACKAGE_CODE\r\n" + 
		        		",CAL_TYPE,CASE_TYPE,AMOUNT\r\n" + 
		        		",SUM(AMOUNT_PER_ITEM) + SUM(AMOUNT_OF_PERCENT) AS SUM_AMOUNT\r\n" + 
		        		"FROM (\r\n" + 
		        		"SELECT  STTC.START_DATE,TD.VERIFY_DATE,TD.VERIFY_TIME,TD.DOCTOR_CODE,TD.EPISODE_NO,TD.PAYOR_OFFICE_CATEGORY_CODE,TD.ORDER_ITEM_CODE,TD.AMOUNT_AFT_DISCOUNT\r\n" + 
		        		",STTC.CASE_CODE,STTC.PACKAGE_CODE,STTC.START_TIME,STTC.END_TIME\r\n" + 
		        		",MSC.CAL_TYPE,MSC.CASE_TYPE,MSC.CASE_MAPPING_CODE,MSC.MAX_CASE,MSC.AMOUNT,MSC.AMOUNT_PER_CASE\r\n" + 
		        		",SMC.AMOUNT_PER_ITEM,SMC.PERCENT_PER_ITEM\r\n" + 
		        		",(PERCENT_PER_ITEM/100)*AMOUNT_AFT_DISCOUNT AS AMOUNT_OF_PERCENT\r\n" + 
		        		"FROM  STP_TIME_TABLE_CASE STTC\r\n" + 
		        		"LEFT JOIN TRN_DAILY TD ON TD.HOSPITAL_CODE=STTC.HOSPITAL_CODE AND TD.DOCTOR_CODE=STTC.DOCTOR_CODE\r\n" + 
		        		"LEFT JOIN MST_SHIFT_CASE MSC ON STTC.HOSPITAL_CODE=MSC.HOSPITAL_CODE AND STTC.CASE_CODE=MSC.CASE_CODE\r\n" + 
		        		"LEFT JOIN STP_MAPPING_CASE SMC ON MSC.HOSPITAL_CODE=SMC.HOSPITAL_CODE AND MSC.CASE_MAPPING_CODE=SMC.CASE_MAPPING_CODE\r\n" + 
		        		"LEFT JOIN ORDER_ITEM OI ON TD.HOSPITAL_CODE=OI.HOSPITAL_CODE AND TD.ORDER_ITEM_CODE=OI.CODE\r\n" + 
		        		"WHERE TD.HOSPITAL_CODE='"+this.hospitalCode+"'\r\n" + 
		        		"AND STTC.YYYY+STTC.MM='"+yyyy+mm+"'\r\n" + 
		        		"AND TD.GUARANTEE_NOTE='MAPPING_CASE'\r\n" + 
		        		"AND MSC.CAL_TYPE='COMPARE'\r\n" + 
		        		"AND ( (TD.VERIFY_DATE=STTC.START_DATE AND TD.VERIFY_TIME BETWEEN STTC.START_TIME AND '235959') OR ( TD.VERIFY_DATE=STTC.END_DATE AND TD.VERIFY_TIME BETWEEN '000000' AND STTC.END_TIME ) )\r\n" + 
		        		"AND TD.ORDER_ITEM_CODE NOT IN (SELECT DISTINCT ORDER_ITEM_CODE FROM STP_PACKAGE_ITEM_MAPPING WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND ACTIVE='1')\r\n" + 
		        		"AND TD.ORDER_ITEM_CODE=SMC.ORDER_ITEM_CODE\r\n" + 
		        		"--AND TD.PAYOR_OFFICE_CATEGORY_CODE=SMC.PAYOR_OFFICE_CATEGORY_CODE\r\n" + 
		        		"AND TD.ADMISSION_TYPE_CODE=SMC.ADMISSION_TYPE_CODE\r\n" + 
		        		"AND MSC.ACTIVE='1' AND SMC.ACTIVE='1' AND OI.ACTIVE='1'\r\n" + 
		        		") T GROUP BY START_DATE,DOCTOR_CODE,CASE_CODE,PACKAGE_CODE,CAL_TYPE,CASE_TYPE,AMOUNT,AMOUNT_PER_CASE\r\n";
				
			 	ResultSet rs = conn.executeQuery(sqlSelect);
		        ArrayList<HashMap<String, String>> mapList = new ArrayList<>();
		        while(rs.next()) {
					HashMap<String, String> hm = new HashMap<>();
					hm.put("START_DATE", rs.getString("START_DATE"));
					hm.put("DOCTOR_CODE", rs.getString("DOCTOR_CODE"));
					hm.put("CASE_CODE", rs.getString("CASE_CODE"));
					hm.put("PACKAGE_CODE", rs.getString("PACKAGE_CODE"));
					hm.put("CAL_TYPE", rs.getString("CAL_TYPE"));
					hm.put("CASE_TYPE", rs.getString("CASE_TYPE"));
					hm.put("AMOUNT", rs.getString("AMOUNT"));
					hm.put("SUM_AMOUNT", rs.getString("SUM_AMOUNT"));
					mapList.add(hm);
				}
		        
		        double amountPerCase = 0.0;
		        double sumAmount = 0.0;
		        double amount = 0.0;
		        
		        for(int i=0;i<mapList.size();i++) {
		        	
		        	amountPerCase = Double.parseDouble(mapList.get(i).get("AMOUNT"));
		        	sumAmount = Double.parseDouble(mapList.get(i).get("SUM_AMOUNT"));
		        	
		        	
		        	if( amountPerCase > sumAmount) {
		        		amount = amountPerCase;
		        	}else {
		        		amount = sumAmount;
		        	}
		        	
		        	sqlUpdate = "UPDATE STP_TIME_TABLE_CASE SET AMOUNT='"+amount+"', UPDATE_DATE='"+JDate.getDate()+"', UPDATE_TIME='"+JDate.getTime()+"', USER_ID='"+this.userId+"' \r\n" + 
							"WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND YYYY+MM='"+yyyy+mm+"' AND START_DATE='"+mapList.get(i).get("START_DATE")+"' AND DOCTOR_CODE='"+mapList.get(i).get("DOCTOR_CODE")+"' AND CASE_CODE='"+mapList.get(i).get("CASE_CODE")+"' AND PACKAGE_CODE='"+mapList.get(i).get("PACKAGE_CODE")+"'";
					conn.executeUpdate(sqlUpdate);
		        }
		        status = true;
		}catch (Exception e) {
			// TODO: handle exception
			status = false;
			TRN_Error.setHospital_code(this.hospitalCode);
			TRN_Error.setUser_name(this.userId);
			TRN_Error.writeErrorLog(conn.getConnection(), "Run Time Table Case Process",  "Update calculate NUM_PACKAGE_CASE in STP_TIME_TABLE_CASE Error", e.getMessage(), sqlUpdate,"");
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean updateCalculateAmount(String yyyy,String mm) {
//		DBConnection conn = new DBConnection();
		boolean status = false;
		String sqlSelect = "";
		String sqlUpdate = "";
		try {
//			conn.connectToLocal();
			sqlSelect = "SELECT STTC.START_DATE,STTC.DOCTOR_CODE,STTC.CASE_CODE,STTC.PACKAGE_CODE\r\n" + 
	        		",MSC.START_TIME MST_START_TIME,MSC.END_TIME MST_END_TIME,STTC.START_TIME,STTC.END_TIME\r\n" + 
	        		",MSC.AMOUNT,MSC.MAX_CASE,AMOUNT_PER_CASE,NUM_CASE,NUM_PACKAGE_CASE\r\n" + 
	        		",STTC.NUM_CASE + STTC.NUM_PACKAGE_CASE  AS ALL_CASE \r\n" + 
	        		"FROM STP_TIME_TABLE_CASE STTC\r\n" + 
	        		"LEFT JOIN MST_SHIFT_CASE MSC ON STTC.HOSPITAL_CODE=MSC.HOSPITAL_CODE AND STTC.CASE_CODE=MSC.CASE_CODE\r\n" + 
	        		"WHERE STTC.HOSPITAL_CODE='"+this.hospitalCode+"' AND STTC.YYYY+STTC.MM='"+yyyy+mm+"'\r\n" + 
	        		"AND MSC.CAL_TYPE='OVER'  AND MSC.ACTIVE='1'";
			ResultSet rs = conn.executeQuery(sqlSelect);
	        ArrayList<HashMap<String, String>> mapList = new ArrayList<>();
	        while(rs.next()) {
				HashMap<String, String> hm = new HashMap<>();
				hm.put("START_DATE", rs.getString("START_DATE"));
				hm.put("DOCTOR_CODE", rs.getString("DOCTOR_CODE"));
				hm.put("CASE_CODE", rs.getString("CASE_CODE"));
				hm.put("PACKAGE_CODE", rs.getString("PACKAGE_CODE"));
				hm.put("MST_START_TIME", rs.getString("MST_START_TIME"));
				hm.put("MST_END_TIME", rs.getString("MST_END_TIME"));
				hm.put("START_TIME", rs.getString("START_TIME"));
				hm.put("END_TIME", rs.getString("END_TIME"));
				hm.put("AMOUNT", rs.getString("AMOUNT"));
				hm.put("MAX_CASE", rs.getString("MAX_CASE"));
				hm.put("AMOUNT_PER_CASE", rs.getString("AMOUNT_PER_CASE"));
				hm.put("NUM_CASE", rs.getString("NUM_CASE"));
				hm.put("NUM_PACKAGE_CASE", rs.getString("NUM_PACKAGE_CASE"));
				hm.put("ALL_CASE", rs.getString("ALL_CASE"));
				mapList.add(hm);
			}
	        
	        String mstStartTime = "";
	        String mstEndTime = "";
	        String stpStartTime = "";
	        String stpEndTime = "";
	        double mstAmount = 0.0;
	        double mstMaxCase = 0.0;
	        double mstAmountPerCase = 0.0;
	        double allCase = 0.0;
	        double stpMaxCase = 0.0;
	        double stpAmount = 0.0;
	        double mstHour = 0.0;
	        double stpHour = 0.0;
	        double overCase = 0.0;
	        
	        for(int i=0; i< mapList.size(); i++) {
	        	
	        	mstStartTime = mapList.get(i).get("MST_START_TIME");
		        mstEndTime = mapList.get(i).get("MST_END_TIME");
		        stpStartTime = mapList.get(i).get("START_TIME");
		        stpEndTime = mapList.get(i).get("END_TIME");
		        mstAmount = Double.parseDouble(mapList.get(i).get("AMOUNT"));
		        mstMaxCase = Double.parseDouble(mapList.get(i).get("MAX_CASE"));
		        mstAmountPerCase = Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
		        allCase = Double.parseDouble(mapList.get(i).get("ALL_CASE"));
		        
				mstHour = JDate.getDiffTimes(mapList.get(i).get("START_DATE"), mapList.get(i).get("START_DATE"), mstStartTime, mstEndTime);
				stpHour = JDate.getDiffTimes(mapList.get(i).get("START_DATE"), mapList.get(i).get("START_DATE"), stpStartTime, stpEndTime);
				stpMaxCase = (int) ((stpHour * mstMaxCase ) / mstHour);
//				System.out.println("STP Max Case >> " + stpMaxCase) ;
				
				//check over case for pay
				if(stpMaxCase < allCase ) {
					overCase = allCase - stpMaxCase;
					stpAmount = mstAmount + (overCase * mstAmountPerCase);
				}else {
					stpAmount = mstAmount;
				}
				System.out.println("STP Amount >> " + stpAmount) ;
				sqlUpdate = "UPDATE STP_TIME_TABLE_CASE SET AMOUNT='"+stpAmount+"', UPDATE_DATE='"+JDate.getDate()+"', UPDATE_TIME='"+JDate.getTime()+"', USER_ID='"+this.userId+"' \r\n" + 
						"WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND YYYY+MM='"+yyyy+mm+"' AND START_DATE='"+mapList.get(i).get("START_DATE")+"' AND DOCTOR_CODE='"+mapList.get(i).get("DOCTOR_CODE")+"' AND CASE_CODE='"+mapList.get(i).get("CASE_CODE")+"' AND PACKAGE_CODE='"+mapList.get(i).get("PACKAGE_CODE")+"'";
				conn.executeUpdate(sqlUpdate);
	        }
	        status = true;
		}catch (Exception e) {
			// TODO: handle exception
			status = false;
			TRN_Error.setHospital_code(this.hospitalCode);
			TRN_Error.setUser_name(this.userId);
			TRN_Error.writeErrorLog(conn.getConnection(), "Run Time Table Case Process",  "Update calculate amount in STP_TIME_TABLE_CASE Error", e.getMessage(), sqlUpdate,"");
			e.printStackTrace();
		}
		return status;
	}
	

	public boolean insertMappingCaseExpenseDetail(String yyyy,String mm) {
//		DBConnection conn = new DBConnection();
		String sqlDelete = "";
		String sqlInsert = "";
		boolean status = false;
		try {
//			conn.connectToLocal();
			sqlDelete = "DELETE TRN_EXPENSE_DETAIL WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND YYYY+MM='"+yyyy+mm+"' AND EXPENSE_CODE='ADD_CASE' AND ( BATCH_NO = '' OR BATCH_NO IS NULL )";
			conn.executeUpdate(sqlDelete);
			System.out.println("Delete time table case to STP_EXPENSE_DETAIL complete");
			
			
			sqlInsert = "INSERT INTO TRN_EXPENSE_DETAIL (HOSPITAL_CODE,YYYY,MM,DOCTOR_CODE,LINE_NO,EXPENSE_CODE,EXPENSE_SIGN,EXPENSE_ACCOUNT_CODE,AMOUNT,TAX_AMOUNT,TAX_TYPE_CODE,UPDATE_DATE,UPDATE_TIME,USER_ID) \r\n" + 
					"SELECT TTC.HOSPITAL_CODE,TTC.YYYY,TTC.MM, TTC.DOCTOR_CODE,'"+JDate.getDate()+JDate.getTime()+JDate.getSeconds()+"', E.CODE AS EXPENSE_CODE, E.SIGN AS EXPENSE_SIGN, E.ACCOUNT_CODE, SUM(TTC.AMOUNT) AS AMOUNT, SUM(TTC.AMOUNT) AS TAX_AMOUNT, E.TAX_TYPE_CODE ,'"+JDate.getDate()+"','"+JDate.getTime()+"','"+this.userId+"' \r\n" + 
					"FROM STP_TIME_TABLE_CASE TTC\r\n" + 
					"JOIN EXPENSE E ON  TTC.HOSPITAL_CODE=E.HOSPITAL_CODE\r\n" + 
					"WHERE TTC.HOSPITAL_CODE='"+this.hospitalCode+"' AND E.ADJUST_TYPE ='MC' AND TTC.YYYY+TTC.MM='"+yyyy+mm+"'\r\n"+
					"GROUP BY TTC.HOSPITAL_CODE,TTC.YYYY,TTC.MM, TTC.DOCTOR_CODE, E.CODE, E.SIGN , E.ACCOUNT_CODE, E.TAX_TYPE_CODE\r\n"+
					"HAVING SUM(TTC.AMOUNT) > 0";
			
			conn.executeUpdate(sqlInsert);
			System.out.println("Insert time table case to TRN_EXPENSE_DETAIL complete");
			status = true;
		}catch (Exception e) {
			// TODO: handle exception
			status = false;
			TRN_Error.setHospital_code(this.hospitalCode);
			TRN_Error.setUser_name(this.userId);
			TRN_Error.writeErrorLog(conn.getConnection(), "Run Time Table Case Process",  "Insert TRN_EXPENSE_DETAIL Error", e.getMessage(), sqlInsert,"");
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean doRollback(String yyyy,String mm) {
//		DBConnection conn = new DBConnection();
		boolean status = false;
		String sqlUpdate = "";
		try {
//			conn.connectToLocal();
		sqlUpdate = "UPDATE TRN_DAILY SET GUARANTEE_NOTE='' " + 
				"WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND GUARANTEE_NOTE = 'MAPPING_CASE'  " + 
				"AND SUBSTRING(VERIFY_DATE,1,6) ='"+yyyy+mm+"' AND ( BATCH_NO = '' OR BATCH_NO IS NULL)";
		status = conn.executeUpdate(sqlUpdate) >= 0 ? true : false;
		logger.info("Run Time Table Case Process Method:Rollback");
		
		}catch (Exception e) {
			// TODO: handle exception
			status = false;
			TRN_Error.setHospital_code(this.hospitalCode);
			TRN_Error.setUser_name(this.userId);
			TRN_Error.writeErrorLog(conn.getConnection(), "Run Time Table Case Process",  "Rollback process Error", e.getMessage(), sqlUpdate,"");
			e.printStackTrace();
		}
		return status;
	}
	
}
