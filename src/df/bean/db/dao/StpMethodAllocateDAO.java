package df.bean.db.dao;



import java.util.List;
import java.util.Map;
import df.bean.db.conn.DBConn;;

public class StpMethodAllocateDAO {
	DBConn conn = null;

	public StpMethodAllocateDAO(){
		conn = new DBConn();
		conn.setDualStatement("");
	}
	
	public List<Map<String,Object>> getBasicAllocateMethod(String hospitalCode){
		List<Map<String,Object>> listData = null;
		String sql = 
				"SELECT HOSPITAL_CODE, METHOD_SEQUENCE, "
				+ "DOCTOR_CATEGORY_CODE AS DOCTOR_CATEGORY, "
				+ "PAYOR_CATEGORY_CODE AS PAYOR_CATEGORY, "
				+ "PAYOR_CODE AS PAYOR, "
				+ "DOCTOR_CODE AS DOCTOR, "
				+ "DOCTOR_TREATMENT_CODE AS DOCTOR_TREATMENT, "
				+ "PRIVATE_DOCTOR, "
				+ "ADMISSION_TYPE_CODE AS ADMISSION_TYPE, "
				+ "IS_PROCEDURE, "
				+ "ORDER_ITEM_CATEGORY_CODE AS ORDER_ITEM_CATEGORY, "
				+ "ORDER_ITEM_CODE AS ORDER_ITEM, "
				+ "TIME_START, TIME_END, "
				+ "AMOUNT_START, AMOUNT_END, NORMAL_ALLOCATE_AMT, NORMAL_ALLOCATE_PCT, GUARANTEE_SOURCE, TAX_TYPE_CODE, TAX_RATE, TAX_SOURCE "
				+ "FROM STP_METHOD_ALLOCATE "
				+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND ACTIVE = '1' "
				+ "ORDER BY METHOD_SEQUENCE DESC, PAYOR_CATEGORY_CODE DESC, ORDER_ITEM_CODE DESC, ORDER_ITEM_CATEGORY_CODE DESC, IS_PROCEDURE DESC";
		try{
			listData = conn.queryList(sql);
		}catch (Exception e){
			System.out.println("Error from stpmethodallocateDAO "+e);
			listData = getBasicAllocateMethodWithOutTime(hospitalCode);
		}
	    return listData;
	}

	public List<Map<String,Object>> getBasicAllocateMethodWithOutTime(String hospitalCode){
		String sql = 
				"SELECT HOSPITAL_CODE, METHOD_SEQUENCE, "
				+ "DOCTOR_CATEGORY_CODE AS DOCTOR_CATEGORY, "
				+ "PAYOR_CATEGORY_CODE AS PAYOR_CATEGORY, "
				+ "PAYOR_CODE AS PAYOR, "
				+ "DOCTOR_CODE AS DOCTOR, "
				+ "DOCTOR_TREATMENT_CODE AS DOCTOR_TREATMENT, "
				+ "PRIVATE_DOCTOR, "
				+ "ADMISSION_TYPE_CODE AS ADMISSION_TYPE, "
				+ "IS_PROCEDURE, "
				+ "ORDER_ITEM_CATEGORY_CODE AS ORDER_ITEM_CATEGORY, "
				+ "ORDER_ITEM_CODE AS ORDER_ITEM, "
				+ "AMOUNT_START, AMOUNT_END, NORMAL_ALLOCATE_AMT, NORMAL_ALLOCATE_PCT, GUARANTEE_SOURCE, TAX_TYPE_CODE, TAX_RATE, TAX_SOURCE "
				+ "FROM STP_METHOD_ALLOCATE "
				+ "WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND ACTIVE = '1' "
				+ "ORDER BY METHOD_SEQUENCE DESC, PAYOR_CATEGORY_CODE DESC, ORDER_ITEM_CODE DESC, ORDER_ITEM_CATEGORY_CODE DESC, IS_PROCEDURE DESC";
	    return conn.queryList(sql);
	}

	public List<Map<String,Object>> getStepAllocate(String hospitalCode, String doctor){
		List<Map<String, Object>> result = null;
		String sql = "SELECT * FROM STP_METHOD_ALLOC_STEP WHERE HOSPITAL_CODE = '"+hospitalCode+"' "+ 
		"AND STEP_ID IN (SELECT DOCTOR_CATEGORY_CODE FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND DOCTOR_PROFILE_CODE = '"+doctor+"') "+
		"ORDER BY STEP_SEQ";
		return conn.queryList(sql);
	}

	public List<Map<String,Object>> getDoctorForStepAllocate(String hospitalCode, String doctorCode){
		String sql = "SELECT * FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND CODE = '"+doctorCode+"'"+
		"AND DOCTOR_CATEGORY_CODE IN (SELECT DISTINCT STEP_ID FROM STP_METHOD_ALLOC_STEP WHERE HOSPITAL_CODE = '"+hospitalCode+"')";
	    return conn.queryList(sql);
	}

	public List<Map<String,Object>> getStepMethodByDoctor(String hospitalCode, String doctorCode){
		String sql = "SELECT DISTINCT HOSPITAL_CODE,STEP_ID, ADMISSION_TYPE_CODE,TAX_TYPE_CODE, TAX_RATE, TAX_SOURCE "+
		"FROM STP_METHOD_ALLOC_STEP WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND STEP_ID IN ("+
		"SELECT DOCTOR_CATEGORY_CODE FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND CODE = '"+doctorCode+"')";
	    return conn.queryList(sql);
	}
}