package df.bean.process;

import java.util.ArrayList;
import java.util.HashMap;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.AjaxUtils;


public class ProcessAjaxCalculate extends ProcessTransDaily{
	private HashMap<String, String> hashData  = new HashMap<String, String>();
    private DBConnection connect = null;

	public AjaxUtils getDrGuanateeByIsGuanateeProfire(String hospitalCode, String doctorProfileCode,String guaranteeProfile,String columnName){
		AjaxUtils process = new AjaxUtils();
		
		DBConn dbConn = new DBConn();
		DBConnection conn = new DBConnection();
		ArrayList<String> arrData = new ArrayList<String>();
		String sql = "";
        
        if(conn.connectToLocal()){
            this.connect = conn; 
        }
        
        //System.out.println("columnName = " + columnName);
        
        sql = "SELECT CODE FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospitalCode+"'";
        if(guaranteeProfile.equals("Y")){
        	sql += " AND DOCTOR_PROFILE_CODE = '"+doctorProfileCode+"'";
        	arrData = dbConn.getSingleDataIsArrayList(sql,columnName);
            //System.out.println("arrData.size() = " + arrData.size());
        	if(arrData.size()>0){
                process.setCode(arrData.get(1));
        	}else{
        		process.setCode("");
        	}
        }else{
        	arrData = dbConn.getSingleDataIsArrayList(sql, columnName);
            //System.out.println("arrData.size() = " + arrData.size());
           	if(arrData.size()>0){
                process.setCode(arrData.get(1));
        	}else{
        		process.setCode("");
        	}
        }
        //System.out.println("sql = " + sql);
        process.setArrData(arrData);
		return process;
		
	}
}
