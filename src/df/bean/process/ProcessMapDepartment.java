package df.bean.process;

import df.bean.db.conn.DBConnection;

public class ProcessMapDepartment implements ProcessMaster {

	private DBConnection objConn = null;
	private String hospitalCode;
	private String dateStart;
	private String dateEnd;
	
	
	public ProcessMapDepartment(String hospitalCode  , String dateStart , String dateEnd) { 
		
		/***
		 *  This is set hospital code for this process
		 */
		this.hospitalCode = hospitalCode;
		
		this.dateStart =  dateStart;
		
		this.dateEnd  = dateEnd;
		
		/**
		 *  Set condition to database.
		 */
		this.objConn = new DBConnection(this.hospitalCode);
		this.objConn.connectToLocal();
		
	}
	
	@Override
	public boolean doProcess() {
		boolean action  = false;
		
		try{ 
		
			if(this.updateTrnPatientDepartment() != 0)
				action = true;
			
			if(this.updateTrnPatientReceipt() != 0)
				action = true;
			
		}catch(Exception  ex){ 
			System.out.println(ex.toString());
		}
		
		return action;
	}

	@Override
	public boolean doRollback() {
		return false;
	}

	@Override
	public boolean doBatchClose() {
		return false;
	}
	
	/**
	 * Update New Code Department.
	 * @return Integer Row
	 */
	private int updateDepartment(){ 

		int actRow =  0;
		
		try {
			
			String sql = "UPDATE DEPARTMENT SET	DEPARTMENT.CODE = TMD.NEW_DEPT_CODE FROM " + 
						 " TB_MAPPING_DEPT TMD , DEPARTMENT " +
						 " WHERE DEPARTMENT.CODE =  TMD.OLD_DEPT_CODE " + 
						 " AND DEPARTMENT.HOSPITAL_CODE = TMD.HOSPITAL_CODE " +
						 " AND DEPARTMENT.HOSPITAL_CODE = '"+ this.hospitalCode +"' "  +
						 " AND TMD.HOSPITAL_CODE = '" + this.hospitalCode + "'";
			
			actRow  = this.objConn.executeUpdate(sql);
			
		}catch(Exception ex){
				System.out.println(ex.toString());
		}
		return actRow;
	}
	
	/**
	 * Update New Code Doctor.
	 * @return
	 */
	private int updateDoctor(){ 
		
		int actRow =  0;
		
		try {
			
			String sql = "UPDATE DOCTOR SET	DOCTOR.DEPARTMENT_CODE = TMD.NEW_DEPT_CODE FROM " + 
						 " TB_MAPPING_DEPT TMD , DOCTOR " +
						 " WHERE DOCTOR.DEPARTMENT_CODE =  TMD.OLD_DEPT_CODE " + 
						 " AND DOCTOR.HOSPITAL_CODE = TMD.HOSPITAL_CODE " +
						 " AND DOCTOR.HOSPITAL_CODE = '"+ this.hospitalCode +"' "  +
						 " AND TMD.HOSPITAL_CODE = '" + this.hospitalCode + "'";
			
			actRow  = this.objConn.executeUpdate(sql);
			
		}catch(Exception ex){
				System.out.println(ex.toString());
		}
		
		return actRow;
		
	}
	
	/**
	 * Update New Code Patient Department.
	 * @return
	 */
	private int updateTrnPatientDepartment(){ 
		
		int actRow =  0;
		
		try {
			
			String sql = "UPDATE TRN_DAILY SET	TRN_DAILY.PATIENT_DEPARTMENT_CODE = TMD.NEW_DEPT_CODE FROM " + 
						 " TB_MAPPING_DEPT TMD , TRN_DAILY " +
						 " WHERE TRN_DAILY.PATIENT_DEPARTMENT_CODE =  TMD.OLD_DEPT_CODE " + 
						 " AND TRN_DAILY.HOSPITAL_CODE = TMD.HOSPITAL_CODE " +
						 " AND TRN_DAILY.HOSPITAL_CODE = '"+ this.hospitalCode +"' "  +
						 " AND TMD.HOSPITAL_CODE = '" + this.hospitalCode + "' " +
						 " AND TRANSACTION_DATE BETWEEN '" + this.dateStart + "' AND '" + this.dateEnd + "' ";
			
			actRow  = this.objConn.executeUpdate(sql);
			
		}catch(Exception ex){
				System.out.println(ex.toString());
		}
		
		return actRow;
		
	}
	
	/**
	 * Update New Code Receipt Department.
	 * @return
	 */
	private int updateTrnPatientReceipt(){ 
		
		int actRow =  0;
		
		try {
			
			String sql = "UPDATE TRN_DAILY SET	TRN_DAILY.RECEIPT_DEPARTMENT_CODE = TMD.NEW_DEPT_CODE FROM " + 
						 " TB_MAPPING_DEPT TMD , TRN_DAILY " +
						 " WHERE TRN_DAILY.RECEIPT_DEPARTMENT_CODE =  TMD.OLD_DEPT_CODE " + 
						 " AND TRN_DAILY.HOSPITAL_CODE = TMD.HOSPITAL_CODE " +
						 " AND TRN_DAILY.HOSPITAL_CODE = '"+ this.hospitalCode +"' "  +
						 " AND TMD.HOSPITAL_CODE = '" + this.hospitalCode + "' " +
						 " AND TRANSACTION_DATE BETWEEN '" + this.dateStart + "' AND '" + this.dateEnd + "' ";
			
			actRow  = this.objConn.executeUpdate(sql);
			
		}catch(Exception ex){
				System.out.println(ex.toString());
		}
		
		return actRow;
		
	}

	
}
