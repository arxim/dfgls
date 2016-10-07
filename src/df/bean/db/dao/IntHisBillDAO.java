package df.bean.db.dao;

import java.sql.SQLException;
import java.util.HashMap;

import df.bean.db.conn.DBConn;

public class IntHisBillDAO {
	DBConn conn = null;
	
	public IntHisBillDAO(){
		conn = new DBConn();
	}
	
	public void prepareCreateTransaction(){
		String sql = 
		"INSERT INTO INT_HIS_BILL "+
		"(HOSPITAL_CODE, EPISODE_TYPE, BILL_NO, BILL_DATE, RECEIPT_TYPE_CODE, "+
		"TRANSACTION_TYPE, HN_NO, PATIENT_NAME, EPISODE_NO, PAYOR_CODE, PAYOR_NAME, "+
		"PAYOR_CATEGORY_CODE, PAYOR_CATEGORY_DESC, ADMISSION_TYPE_CODE, ORDER_ITEM_CODE, "+
		"ORDER_ITEM_DESCRIPTION, DOCTOR_PROFILE_CODE, DOCTOR_PROFILE_NAME, DOCTOR_CODE, "+
		"DOCTOR_NAME, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, ORDERED_DATE, ORDERED_TIME, "+
		"NATIONALITY_CODE, NATIONALITY_DESCRIPTION, PATIENT_LOCATION_CODE, PATIENT_LOCATION_DESC, "+
		"PATIENT_LOCATION_DEPT_CODE, PATIENT_LOCATION_DEPT_DESC, RECEIVING_LOCATION_CODE, "+
		"RECEIVING_LOCATION_DESC, RECEIVING_LOCATION_DEPT_CODE, RECEIVING_LOCATION_DEPT_DESC, "+
		"LINE_NO, VERIFIED_DATE, VERIFIED_TIME, BILL_TOTAL_AMOUNT, TRANSACTION_DATE, INVOICE_TYPE, "+
		"IS_ONWARD, OLD_AMOUNT, DOCTOR_PRIVATE ) VALUES "+
		"( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
		"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try{
			conn.setPrepareStatement(sql);			
		}catch (Exception e){
			System.out.println(e);
		}
	}
	public void createTransaction(HashMap<String, String> m){
		//System.out.println(m);
		try {
			conn.getPrepareStatement().setString(1, m.get("HOSPITAL_CODE").toString());
			conn.getPrepareStatement().setString(2, m.get("EPISODE_TYPE").toString());
			conn.getPrepareStatement().setString(3, m.get("BILL_NO").toString());
			conn.getPrepareStatement().setString(4, m.get("BILL_DATE").toString());
			conn.getPrepareStatement().setString(5, m.get("RECEIPT_TYPE_CODE").toString());
			conn.getPrepareStatement().setString(6, m.get("TRANSACTION_TYPE").toString());
			conn.getPrepareStatement().setString(7, m.get("HN_NO").toString());
			conn.getPrepareStatement().setString(8, m.get("PATIENT_NAME").trim().toString());
			conn.getPrepareStatement().setString(9, m.get("EPISODE_NO").toString());
			conn.getPrepareStatement().setString(10, m.get("PAYOR_CODE").toString());
			conn.getPrepareStatement().setString(11, m.get("PAYOR_NAME").toString());
			conn.getPrepareStatement().setString(12, m.get("PAYOR_CATEGORY_CODE").toString());
			conn.getPrepareStatement().setString(13, m.get("PAYOR_CATEGORY_DESC").toString());
			conn.getPrepareStatement().setString(14, m.get("ADMISSION_TYPE_CODE").toString());
			conn.getPrepareStatement().setString(15, m.get("ORDER_ITEM_CODE").toString());
			conn.getPrepareStatement().setString(16, m.get("ORDER_ITEM_DESCRIPTION").toString());
			conn.getPrepareStatement().setString(17, m.get("DOCTOR_PROFILE_CODE").toString());
			conn.getPrepareStatement().setString(18, m.get("DOCTOR_PROFILE_NAME").toString());
			conn.getPrepareStatement().setString(19, m.get("DOCTOR_CODE").toString());
			conn.getPrepareStatement().setString(20, m.get("DOCTOR_NAME").toString());
			conn.getPrepareStatement().setString(21, m.get("AMOUNT_BEF_DISCOUNT").toString());
			conn.getPrepareStatement().setString(22, m.get("AMOUNT_OF_DISCOUNT").toString());
			conn.getPrepareStatement().setString(23, m.get("ORDERED_DATE").toString());
			conn.getPrepareStatement().setString(24, m.get("ORDERED_TIME").toString());
			conn.getPrepareStatement().setString(25, m.get("NATIONALITY_CODE").toString());
			conn.getPrepareStatement().setString(26, m.get("NATIONALITY_DESCRIPTION").toString());
			conn.getPrepareStatement().setString(27, m.get("PATIENT_LOCATION_CODE").toString());
			conn.getPrepareStatement().setString(28, m.get("PATIENT_LOCATION_DESC").toString());
			conn.getPrepareStatement().setString(29, m.get("PATIENT_LOCATION_DEPT_CODE").toString());
			conn.getPrepareStatement().setString(30, m.get("PATIENT_LOCATION_DEPT_DESC").toString());
			conn.getPrepareStatement().setString(31, m.get("RECEIVING_LOCATION_CODE").toString());
			conn.getPrepareStatement().setString(32, m.get("RECEIVING_LOCATION_DESC").toString());
			conn.getPrepareStatement().setString(33, m.get("RECEIVING_LOCATION_DEPT_CODE").toString());
			conn.getPrepareStatement().setString(34, m.get("RECEIVING_LOCATION_DEPT_DESC").toString());
			conn.getPrepareStatement().setString(35, m.get("LINE_NO").toString());
			conn.getPrepareStatement().setString(36, m.get("VERIFIED_DATE").toString());
			conn.getPrepareStatement().setString(37, m.get("VERIFIED_TIME").toString());
			conn.getPrepareStatement().setString(38, m.get("BILL_TOTAL_AMOUNT").toString());
			conn.getPrepareStatement().setString(39, m.get("TRANSACTION_DATE").toString());
			conn.getPrepareStatement().setString(40, m.get("INVOICE_TYPE").toString());
			conn.getPrepareStatement().setString(41, m.get("IS_ONWARD").toString());
			conn.getPrepareStatement().setString(42, m.get("OLD_AMOUNT").toString());
			conn.getPrepareStatement().setString(43, m.get("DOCTOR_PRIVATE").toString());
			conn.getPrepareStatement().executeUpdate();
			conn.commitDB();
		} catch (SQLException e) {
			System.out.println("SavePrepareStatement Data Error : "+e);
		}
	}
}