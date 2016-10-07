package df.bean.process;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;

public class ProcessSummaryDoctorRevenue {
	private DBConnection conn = null;
	private String hospital_code, yyyy , mm;
	public ProcessSummaryDoctorRevenue(DBConnection conn,String hos,String yy, String mm) {
	        this.conn = conn;
	        this.hospital_code=hos;
	        this.yyyy=yy;
	        this.mm=mm;
	        this.rollBackRevenue();
	}
	
	public boolean insMaster(){
		boolean result = true;
		String sql = "INSERT INTO SUMMARY_REVENUE (" 
		+" SUMMARY_REVENUE.HOSPITAL_CODE,"
		+" SUMMARY_REVENUE.DR_CODE,"
		+" SUMMARY_REVENUE.MM,"
		+" SUMMARY_REVENUE.YYYY,"
		+" SUMMARY_REVENUE.GUARANTEE_TYPE_CODE"
		+")"
		+" SELECT DISTINCT PM.HOSPITAL_CODE"
		+", PM.DOCTOR_CODE"
		+", PM.MM"
		+", PM.YYYY"
		+", SG.GUARANTEE_TYPE_CODE"
		+" FROM PAYMENT_MONTHLY PM left outer join STP_GUARANTEE SG on " 
		+" PM.DOCTOR_CODE = SG.GUARANTEE_DR_CODE"
		+" WHERE PM.HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND PM.YYYY = '"+ this.yyyy +"'"
		+" AND PM.MM = '"+ this.mm +"'";
		try{
			if (conn.executeUpdate(sql) >= 0) { result = true; } else { result = false; }
		}catch (Exception ex){
			System.out.println("Insert Revenue Error :" + ex+" On Statement " + sql);   
		}
		return result;
	}
	
	public boolean updateSGAD(){
		boolean result = true;	
		String sql = "UPDATE SUMMARY_REVENUE SET SUMMARY_REVENUE.SUM_GUARANTEE_AMT = "
		+"(SELECT(sum(SG.GUARANTEE_AMOUNT)+sum(SG.GUARANTEE_FIX_AMOUNT)+sum(SG.GUARANTEE_INCLUDE_AMOUNT))" 
		+" FROM STP_GUARANTEE SG "
		+" WHERE SG.HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND SG.GUARANTEE_DR_CODE = SUMMARY_REVENUE.DR_CODE"
		+" AND SG.YYYY = '"+ this.yyyy +"'"
		+" AND SG.MM = '" + this.mm + "'"
		+" AND SG.ACTIVE = '1'"
		+" AND SG.GUARANTEE_TYPE_CODE = 'DLY'"
		+" GROUP BY SG.GUARANTEE_DR_CODE "
		+")"	
		+" WHERE SUMMARY_REVENUE.GUARANTEE_TYPE_CODE = 'DLY'";
		try{
			if (conn.executeUpdate(sql) >= 0){
				result = true;
			}else{
				result = false;
			}
		}catch (Exception ex){
			System.out.println("UPDate SUM_GUARANTEE_AMT<>DLY : " + sql);  
		}
		return result;
	}
	
	public boolean updateDGAM(){
		boolean result = true;	
		String sql = "UPDATE SUMMARY_REVENUE SET SUMMARY_REVENUE.SUM_GUARANTEE_AMT = "
		+"(SELECT DISTINCT((SG.GUARANTEE_AMOUNT)+(SG.GUARANTEE_FIX_AMOUNT)+(SG.GUARANTEE_INCLUDE_AMOUNT))" 
		+" FROM STP_GUARANTEE SG"
		+" WHERE SG.HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND SG.GUARANTEE_DR_CODE = SUMMARY_REVENUE.DOCTOR_REV_CODE "
		+" AND SG.YYYY = '"+ this.yyyy +"'"
		+" AND SG.MM = '" + this.mm + "'"
		+" AND SG.ACTIVE = '1'"
		+" AND SG.GUARANTEE_TYPE_CODE = 'MLY'"
		+")"
		+" WHERE SUMMARY_REVENUE.GUARANTEE_TYPE_CODE = 'MLY'";
		try{
			if (conn.executeUpdate(sql) > 0){
				result = true;
			}else{
				result = false;
			}
		}catch (Exception ex){
			System.out.println("UPDate SUM_GUARANTEE_AMT<>MLY : " + sql);  
		}
		return result;
	}

	public boolean updateRevenueSTP(){
		boolean result = true;	
		String sql = "UPDATE SUMMARY_REVENUE SET SUM_ABSORB402 = "
		+"(SELECT sum(SG.HP402_ABSORB_AMOUNT)" 
		+" FROM STP_GUARANTEE SG"
		+" WHERE SG.HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND SG.GUARANTEE_DR_CODE = SUMMARY_REVENUE.DOCTOR_REV_CODE "
		+" AND SG.YYYY = '"+ this.yyyy +"'"
		+" AND SG.MM = '" + this.mm + "'"
		+" AND SG.ACTIVE = '1'" 
		+" GROUP BY SG.GUARANTEE_DR_CODE"  
		+")"
		+", SUM_PARTTIME ="
		+"(SELECT sum(SG.GUARANTEE_EXCLUDE_AMOUNT)" 
		+" FROM STP_GUARANTEE SG"
		+" WHERE SG.HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND SG.GUARANTEE_DR_CODE = SUMMARY_REVENUE.DOCTOR_REV_CODE "
		+" AND SG.YYYY = '"+ this.yyyy +"'"
		+" AND SG.MM = '" + this.mm + "'"
		+" AND SG.ACTIVE = '1'" 
		+" GROUP BY SG.GUARANTEE_DR_CODE"  
		+")";
		try{
			if (conn.executeUpdate(sql) > 0){
				result = true;
			}else{
				result = false;
			}
		}catch (Exception ex){
			System.out.println("UPDate UpdateRevenueSTP : " + sql);  
		}
		return result;	
	}

	public boolean updateRevenueTRN(){
		boolean result = true;	
		String sql = "UPDATE SUMMARY_REVENUE SET SUM_VDF_INV = "
		+"(SELECT sum(TD.DR_AMT)" 
		+" FROM TRN_DAILY TD,DOCTOR DT"
		+" WHERE TD.HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND TD.DOCTOR_CODE = SUMMARY_REVENUE.DOCTOR_REV_CODE " 
		+" AND TD.DOCTOR_CODE = DT.CODE"
		+" AND TD.VERIFY_DATE BETWEEN '"+this.yyyy+this.mm+"01' AND '"+this.yyyy+this.mm+"31'"
		+" GROUP BY DT.DOCTOR_PROFILE_CODE"  
		+")"
		+", SUM_VDF_INV_PAID = "
		+"(SELECT sum(TD.DR_AMT)" 
		+" FROM TRN_DAILY TD"
		+" WHERE TD.HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND TD.DOCTOR_CODE = SUMMARY_REVENUE.DOCTOR_REV_CODE "
		+" AND TD.YYYY = '"+ this.yyyy +"'"
		+" AND TD.MM = '" + this.mm + "'"
		+" AND TD.PAY_BY_AR = 'Y'"
		+" GROUP BY TD.DOCTOR_CODE"  
		+")"
		+", SUM_VDF_RECEIPT = "
		+"(SELECT sum(TD.AMOUNT_AFT_DISCOUNT)" 
		+" FROM TRN_DAILY TD"
		+" WHERE TD.HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND TD.DOCTOR_CODE = SUMMARY_REVENUE.DOCTOR_REV_CODE "
		+" AND TD.YYYY = '"+ this.yyyy +"'"
		+" AND TD.MM = '" + this.mm + "'"
		+" AND TD.TRANSACTION_TYPE = 'REV'"
		+" GROUP BY TD.DOCTOR_CODE"  
		+")"
		+", SUM_VDF_GUA_PAID = "
		+"(SELECT DISTINCT sum(TD.GUARANTEE_AMT)" 
		+" FROM TRN_DAILY TD"
		+" WHERE TD.HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND TD.GUARANTEE_TERM_YYYY = '"+ this.yyyy +"'"
		+" AND TD.GUARANTEE_TERM_MM = '" + this.mm + "'"
		+" AND TD.GUARANTEE_NOTE NOT LIKE '%TURN%'"
		+" AND TD.GUARANTEE_DR_CODE = SUMMARY_REVENUE.DOCTOR_REV_CODE "
		+" GROUP BY TD.GUARANTEE_DR_CODE"  
		+")";	
		try{
			if (conn.executeUpdate(sql) > 0){
				result = true;
			}else{
				result = false;
			}
		}catch (Exception ex){
			System.out.println("UPDate UpdateRevenueTRN : " + sql);  
		}
		return result;	
		}
	
	public boolean rollBackRevenue(){
		boolean result = true;
		String sql = "DELETE FROM SUMMARY_REVENUE WHERE HOSPITAL_CODE = '" + this.hospital_code + "'"
		+" AND YYYY = '"+ this.yyyy +"'"
		+" AND MM = '"+ this.mm +"'";
		try{
			if (conn.executeUpdate(sql) >= 0) { result = true; } else { result = false; }
		}catch (Exception ex){
			System.out.println("Insert Revenue Error :" + ex+" On Statement " + sql);   
		}
		return result;
	}
}
