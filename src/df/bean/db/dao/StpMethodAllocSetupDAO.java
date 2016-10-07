package df.bean.db.dao;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import df.bean.db.conn.DBConn;;

public class StpMethodAllocSetupDAO {
	DBConn conn = new DBConn(true);
	
	public List<Map<String,Object>> getStpMethodAllocSetup(String hospitalCode, String doctor){
		List<Map<String, Object>> result = null;
		String sql = "SELECT * FROM STP_METHOD_ALLOC_STEP WHERE HOSPITAL_CODE = '"+hospitalCode+"' "+ 
				 	 "AND STEP_ID IN (SELECT DOCTOR_CATEGORY_CODE FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND DOCTOR_PROFILE_CODE = '"+doctor+"') "+
				     "ORDER BY STEP_SEQ";

		try {
			 conn.setStatement();
			 result = conn.queryList(sql);
		 } catch (SQLException e) { result = null; }
		 System.out.println(sql);
		 System.out.println(result.size());
		 return result;
	}
	
	public List<Map<String,Object>> getDoctorStep(String hospitalCode, String doctorCode){
		String sql = "SELECT * FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND CODE = '"+doctorCode+"'"+
		"AND DOCTOR_CATEGORY_CODE IN (SELECT DISTINCT STEP_ID FROM STP_METHOD_ALLOC_STEP WHERE HOSPITAL_CODE = '"+hospitalCode+"')";

		try {conn.setStatement();
		} catch (SQLException e) {}
		 
		List<Map<String, Object>> result = conn.queryList(sql);
	    if (result != null) {
	    	return result;
	    } else {
	    	return null;
	    }    
	}
	public List<Map<String,Object>> getStepByDoctor(String hospitalCode, String doctorCode){
		String sql = "SELECT DISTINCT HOSPITAL_CODE,STEP_ID, ADMISSION_TYPE_CODE,TAX_TYPE_CODE, TAX_RATE, TAX_SOURCE "+
		"FROM STP_METHOD_ALLOC_STEP WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND STEP_ID IN ("+
		"SELECT DOCTOR_CATEGORY_CODE FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND CODE = '"+doctorCode+"')";

		try {conn.setStatement();
		} catch (SQLException e) {}
		 
		List<Map<String, Object>> result = conn.queryList(sql);
	    if (result != null) {
	    	return result;
	    } else {
	    	return null;
	    }    
	}
}
