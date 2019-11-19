package df.bean.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.TRN_Error;
import df.bean.interfacefile.ImportTimeTableCaseBean;
import df.bean.obj.util.FindDate;
import df.bean.obj.util.JDate;

public class ProcessTimeTableCase {
	
	private DBConn cn = new DBConn();
//	private DBConnection conn = new DBConnection();
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
	
	public void initProcessMappingCase(String hospitalCode, String userId) {
		this.hospitalCode=hospitalCode;
		this.userId=userId;
	}

	public boolean runTimeTableCase(String yyyy, String mm) {
		DBConnection conn = new DBConnection();
		boolean status = false;
		try {
			conn.connectToLocal();
	        String sqlSelect = "SELECT * FROM TRN_TIME_TABLE_CASE\r\n" + 
	        		"WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND YYYY = '"+yyyy+"' AND MM = '"+mm+"'";
	
	        ResultSet rs = conn.executeQuery(sqlSelect);
	        ArrayList<HashMap<String, String>> mapList = new ArrayList<>();
	        while(rs.next()) {
				HashMap<String, String> hm = new HashMap<>();
				hm.put("HOSPITAL_CODE", rs.getString("HOSPITAL_CODE"));
				hm.put("DOCTOR_CODE", rs.getString("DOCTOR_CODE"));
				hm.put("CASE_CODE", rs.getString("CASE_CODE"));
				hm.put("TYPE", rs.getString("TYPE"));
				hm.put("START_TIME", rs.getString("START_TIME"));
				hm.put("END_TIME", rs.getString("END_TIME"));
				hm.put("START_DATE", rs.getString("START_DATE"));
				hm.put("END_DATE", rs.getString("END_DATE"));
				mapList.add(hm);
			}
			
			for(int i=0;i<mapList.size();i++) {
				double amount = calculateMappingCase(mapList.get(i).get("DOCTOR_CODE"), mapList.get(i).get("CASE_CODE"), mapList.get(i).get("START_DATE"), mapList.get(i).get("END_DATE"), mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"), mapList.get(i).get("TYPE"));
				String sqlUpdate = "";
				try {
					sqlUpdate = "UPDATE TRN_TIME_TABLE_CASE SET AMOUNT = '"+amount+"', UPDATE_DATE = '"+JDate.getDate()+"', UPDATE_TIME='"+JDate.getTime()+"', USER_ID='"+this.userId+"'\r\n" + 
							"WHERE HOSPITAL_CODE = '"+mapList.get(i).get("HOSPITAL_CODE")+"' \r\n" + 
							"AND DOCTOR_CODE='"+mapList.get(i).get("DOCTOR_CODE")+"' \r\n" + 
							"AND CASE_CODE='"+mapList.get(i).get("CASE_CODE")+"' \r\n" + 
							"AND START_DATE='"+mapList.get(i).get("START_DATE")+"'\r\n" + 
							"AND END_DATE='"+mapList.get(i).get("END_DATE")+"'\r\n" + 
							"AND START_TIME='"+mapList.get(i).get("START_TIME")+"'\r\n" + 
							"AND END_TIME='"+mapList.get(i).get("END_TIME")+"'\r\n" + 
							"AND TYPE='"+mapList.get(i).get("TYPE")+"'";
					conn.executeUpdate(sqlUpdate);
				}catch (Exception e) {
					// TODO: handle exception
					TRN_Error.setHospital_code(this.hospitalCode);
					TRN_Error.setUser_name(this.userId);
					TRN_Error.writeErrorLog(conn.getConnection(), "Run Time Table Case Process",  "Update TRN_TIME_TABLE_CASE Error row : "+i, e.getMessage(), sqlUpdate,"");
					e.printStackTrace();
				}
				
			}
			
			
			String sqlDelete = "";
			String sqlInsert = "";
			
			try {
				sqlDelete = "DELETE TRN_EXPENSE_DETAIL WHERE HOSPITAL_CODE='"+this.hospitalCode+"' AND YYYY+MM='"+yyyy+mm+"' AND EXPENSE_CODE='ADD_CASE' AND BATCH_NO = ''";
				conn.executeUpdate(sqlDelete);
				System.out.println("Delete time table case to TRN_EXPENSE_DETAIL complete");
				
				
				sqlInsert = "INSERT INTO TRN_EXPENSE_DETAIL (HOSPITAL_CODE,YYYY,MM,DOCTOR_CODE,LINE_NO,EXPENSE_CODE,EXPENSE_SIGN,EXPENSE_ACCOUNT_CODE,AMOUNT,TAX_AMOUNT,TAX_TYPE_CODE,UPDATE_DATE,UPDATE_TIME,USER_ID) \r\n" + 
						"SELECT TTC.HOSPITAL_CODE,TTC.YYYY,TTC.MM, TTC.DOCTOR_CODE,'"+JDate.getDate()+JDate.getTime()+JDate.getSeconds()+"', E.CODE AS EXPENSE_CODE, E.SIGN AS EXPENSE_SIGN, E.ACCOUNT_CODE, SUM(TTC.AMOUNT) AS AMOUNT, SUM(TTC.AMOUNT) AS TAX_AMOUNT, E.TAX_TYPE_CODE ,'"+JDate.getDate()+"','"+JDate.getTime()+"','"+this.userId+"' \r\n" + 
						"FROM TRN_TIME_TABLE_CASE TTC\r\n" + 
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
			
		} catch (Exception e) {
			// TODO: handle exception
			status = false;
			TRN_Error.setHospital_code(this.hospitalCode);
			TRN_Error.setUser_name(this.userId);
			TRN_Error.writeErrorLog(conn.getConnection(), "Run Time Table Case Process",  "Process Error", e.getMessage(), "","");
			e.printStackTrace();
		}
		return status;
	}
	
	public double calculateMappingCase(String doctorCode, String caseCode,String startDate,String endDate, String startTime,String endTime,String type) {

		DBConnection conn = new DBConnection();	
		conn.connectToLocal();
		double total_case = 0.0;
		double stp_amount_per_time = 0.0;
		double trn_amount_per_time = 0.0;
		double max_case = 0.0;
		double over_case = 0.0;
		double amount = 0.0;
		String sqlMapping = "SELECT TOTAL_CASE.CASE_CODE,TOTAL_CASE.CASE_TYPE,TOTAL_CASE.PAYOR_OFFICE_CATEGORY_CODE,TOTAL_CASE.ORDER_ITEM_CODE,TOTAL_CASE.START_TIME,TOTAL_CASE.END_TIME,TOTAL_CASE.AMOUNT_PER_CASE, TOTAL_CASE.MAX_CASE , COUNT(*) AS SUM_CASE FROM (\r\n" + 
				"SELECT DISTINCT TD.HOSPITAL_CODE\r\n" + 
				"      ,TD.TRANSACTION_DATE\r\n" + 
				"      ,TD.HN_NO\r\n" + 
				"      ,TD.EPISODE_NO\r\n" + 
				"	   ,TD.DOCTOR_CODE\r\n" + 
				"	   ,SMC.CASE_CODE\r\n" + 
				"	   ,SMC.START_TIME\r\n" + 
				"      ,SMC.END_TIME\r\n" + 
				"      ,SMC.MAX_CASE\r\n" + 
				"      ,SMC.AMOUNT_PER_CASE\r\n" + 
				"      ,SMC.PAYOR_OFFICE_CATEGORY_CODE\r\n" + 
				"      ,SMC.ORDER_ITEM_CODE\r\n" + 
				"      ,SMC.CASE_TYPE\r\n" + 
				"  FROM TRN_DAILY TD\r\n" + 
				"  LEFT OUTER JOIN STP_MAPPING_CASE SMC ON SMC.HOSPITAL_CODE=TD.HOSPITAL_CODE\r\n" + 
				"  WHERE TD.HOSPITAL_CODE='054' AND DOCTOR_CODE='"+doctorCode+"' AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"' \r\n" + 
				"  AND CASE_CODE='"+caseCode+"'\r\n" + 
				"  AND SMC.ACTIVE = '1' \r\n" + 
				"  AND TD.ORDER_ITEM_CODE LIKE (\r\n" + 
				"	CASE WHEN SMC.ORDER_ITEM_CODE <> '' THEN SMC.ORDER_ITEM_CODE \r\n" + 
				"	ELSE '%' END )\r\n" + 
				"  AND TD.PAYOR_OFFICE_CATEGORY_CODE LIKE (\r\n" + 
				"	CASE WHEN SMC.PAYOR_OFFICE_CATEGORY_CODE <> '' THEN SMC.PAYOR_OFFICE_CATEGORY_CODE \r\n" + 
				"	ELSE '%' END ) \r\n" +
				"  ) TOTAL_CASE\r\n" + 
				"  GROUP BY TOTAL_CASE.CASE_CODE,TOTAL_CASE.CASE_TYPE,TOTAL_CASE.PAYOR_OFFICE_CATEGORY_CODE,TOTAL_CASE.ORDER_ITEM_CODE,TOTAL_CASE.START_TIME,TOTAL_CASE.END_TIME,TOTAL_CASE.AMOUNT_PER_CASE,TOTAL_CASE.MAX_CASE";
		System.out.println(sqlMapping);
		ResultSet rs = conn.executeQuery(sqlMapping);
		ArrayList<HashMap<String, String>> mapList = new ArrayList<>();
		try {
			while(rs.next()) {
				HashMap<String, String> hm = new HashMap<>();
				hm.put("CASE_CODE", rs.getString("CASE_CODE"));
				hm.put("CASE_TYPE", rs.getString("CASE_TYPE"));
				hm.put("PAYOR_OFFICE_CATEGORY_CODE", rs.getString("PAYOR_OFFICE_CATEGORY_CODE"));
				hm.put("ORDER_ITEM_CODE", rs.getString("ORDER_ITEM_CODE"));
				hm.put("START_TIME", rs.getString("START_TIME"));
				hm.put("END_TIME", rs.getString("END_TIME"));
				hm.put("AMOUNT_PER_CASE", rs.getString("AMOUNT_PER_CASE"));
				hm.put("MAX_CASE", rs.getString("MAX_CASE"));
				hm.put("SUM_CASE", rs.getString("SUM_CASE"));
				mapList.add(hm);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println("SQL >> "+sqlMapping);
		System.out.println("Total Case >> "+mapList.size());
		
		if(type.equals("DLY")) {
			for(int i=0; i<mapList.size();i++) {
				
				stp_amount_per_time = JDate.getDiffTimes(startDate, endDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
				trn_amount_per_time = JDate.getDiffTimes(startDate, endDate, startTime, endTime);
				max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time);
				System.out.println("Max Case >> " + max_case) ;
				//check over case for pay
				if(max_case < Double.parseDouble( mapList.get(i).get("SUM_CASE") )) {
					over_case = Double.parseDouble( mapList.get(i).get("SUM_CASE") ) - max_case;
					amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
				}
				
				
//				if(mapList.get(i).get("CASE_TYPE").equals("ALL") && mapList.get(i).get("ORDER_ITEM_CODE").equals("")) {
//					stp_amount_per_time = JDate.getDiffTimes(startDate, endDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
//					trn_amount_per_time = JDate.getDiffTimes(startDate, endDate, startTime, endTime);
//					max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time);
//					System.out.println("Max Case >> " + max_case) ;
//					//check over case for pay
//					if(max_case < Double.parseDouble( mapList.get(i).get("SUM_CASE") )) {
//						over_case = Double.parseDouble( mapList.get(i).get("SUM_CASE") ) - max_case;
//						amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
//					}
//					
//				}else if(mapList.get(i).get("CASE_TYPE").equals("ALL") && mapList.get(i).get("ORDER_ITEM_CODE") != ""){
//					stp_amount_per_time = JDate.getDiffTimes(startDate, endDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
//					trn_amount_per_time = JDate.getDiffTimes(startDate, endDate, startTime, endTime);
//					max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time);
//					System.out.println("Max Case >> " + max_case);
//					//check over case for pay
//					if(max_case < Double.parseDouble( mapList.get(i).get("SUM_CASE") )) {
//						over_case = Double.parseDouble( mapList.get(i).get("SUM_CASE") ) - max_case;
//						amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
//					}
//				}else if(mapList.get(i).get("CASE_TYPE").equals("EPISODE") && mapList.get(i).get("PAYOR_OFFICE_CATEGORY_CODE").equals("")) {
//					stp_amount_per_time = JDate.getDiffTimes(startDate, endDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
//					trn_amount_per_time = JDate.getDiffTimes(startDate, endDate, startTime, endTime);
//					max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time);
//					System.out.println("Max Case >> " + max_case) ;
//					//check over case for pay
//					if(max_case < Double.parseDouble( mapList.get(i).get("SUM_CASE") )) {
//						over_case = Double.parseDouble( mapList.get(i).get("SUM_CASE") ) - max_case;
//						amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
//					}
//				}else if(mapList.get(i).get("CASE_TYPE").equals("EPISODE") && mapList.get(i).get("PAYOR_OFFICE_CATEGORY_CODE") != ""){
//					stp_amount_per_time = JDate.getDiffTimes(startDate, endDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
//					trn_amount_per_time = JDate.getDiffTimes(startDate, endDate, startTime, endTime);
//					max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time);
//					System.out.println("Max Case >> " + max_case);
//					//check over case for pay
//					if(max_case < Double.parseDouble( mapList.get(i).get("SUM_CASE") )) {
//						over_case = Double.parseDouble( mapList.get(i).get("SUM_CASE") ) - max_case;
//						amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
//					}
//				}
			}
		}else if (type.equals("MLY")) {
			
			String year_from = startDate.substring(0, 4);
	    	String year_to = endDate.substring(0, 4);;
	    	String month_from = startDate.substring(4, 6);
	    	String month_to = endDate.substring(4, 6);
	    	String date_from = startDate.substring(6, 8);
	    	String date_to = endDate.substring(6, 8);
			Date day_from = new GregorianCalendar(Integer.parseInt(year_from), Integer.parseInt(month_from), Integer.parseInt(date_from),00,00).getTime();
		    Date day_to = new GregorianCalendar(Integer.parseInt(year_to), Integer.parseInt(month_to), Integer.parseInt(date_to),24,00).getTime();
		    //Date today = new Date();

		    long diff = day_to.getTime() - day_from.getTime();
		    long day_num = (diff / (1000 * 60 * 60 * 24));
		    
		    for(int i=0; i<mapList.size();i++) {	
		    	
		    	stp_amount_per_time = JDate.getDiffTimes(startDate, startDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
				trn_amount_per_time = JDate.getDiffTimes(startDate, startDate, startTime, endTime);
				max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time) * day_num;
				System.out.println("Max Case >> " + max_case) ;
				total_case = Double.parseDouble(mapList.get(i).get("SUM_CASE"));
				//check over case for pay
				if(max_case < total_case) {
					over_case = total_case - max_case;
					amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
				}
		    	
//				if(mapList.get(i).get("CASE_TYPE").equals("ALL") && mapList.get(i).get("ORDER_ITEM_CODE").equals("")) {
//					stp_amount_per_time = JDate.getDiffTimes(startDate, startDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
//					trn_amount_per_time = JDate.getDiffTimes(startDate, startDate, startTime, endTime);
//					max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time) * day_num;
//					System.out.println("Max Case >> " + max_case) ;
//					total_case = Double.parseDouble(mapList.get(i).get("SUM_CASE"));
//					//check over case for pay
//					if(max_case < total_case) {
//						over_case = total_case - max_case;
//						amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
//					}
//					
//				}else if(mapList.get(i).get("CASE_TYPE").equals("ALL") && mapList.get(i).get("ORDER_ITEM_CODE") != ""){
//					stp_amount_per_time = JDate.getDiffTimes(startDate, startDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
//					trn_amount_per_time = JDate.getDiffTimes(startDate, startDate, startTime, endTime);
//					max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time) * day_num;
//					System.out.println("Max Case >> " + max_case);
//					total_case = Double.parseDouble(mapList.get(i).get("SUM_CASE"));
//					//check over case for pay
//					if(max_case < total_case) {
//						over_case = total_case - max_case;
//						amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
//					}
//				}else if(mapList.get(i).get("CASE_TYPE").equals("EPISODE") && mapList.get(i).get("PAYOR_OFFICE_CATEGORY_CODE").equals("")) {
//					stp_amount_per_time = JDate.getDiffTimes(startDate, startDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
//					trn_amount_per_time = JDate.getDiffTimes(startDate, startDate, startTime, endTime);
//					max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time) * day_num;
//					System.out.println("Max Case >> " + max_case) ;
//					total_case = Double.parseDouble(mapList.get(i).get("SUM_CASE")) ;
//					//check over case for pay
//					if(max_case < total_case) {
//						over_case = total_case - max_case;
//						amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
//					}
//				}else if(mapList.get(i).get("CASE_TYPE").equals("EPISODE") && mapList.get(i).get("PAYOR_OFFICE_CATEGORY_CODE") != ""){
//					stp_amount_per_time = JDate.getDiffTimes(startDate, startDate, mapList.get(i).get("START_TIME"), mapList.get(i).get("END_TIME"));
//					trn_amount_per_time = JDate.getDiffTimes(startDate, startDate, startTime, endTime);
//					max_case = (int) ((trn_amount_per_time*Double.parseDouble(mapList.get(i).get("MAX_CASE")) ) / stp_amount_per_time) * day_num;
//					System.out.println("Max Case >> " + max_case);
//					total_case = Double.parseDouble(mapList.get(i).get("SUM_CASE"));
//					//check over case for pay
//					if(max_case < total_case) {
//						over_case = total_case - max_case;
//						amount += over_case * Double.parseDouble(mapList.get(i).get("AMOUNT_PER_CASE"));
//					}
//				}
			}
		}
		
		
		
		System.out.println("amount >> "+ amount);
		
		return amount;
	}

}
