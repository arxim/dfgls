package df.bean.obj.util;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Doctor;
import df.bean.db.table.DoctorProfile;
import df.bean.obj.doctor.CareProvider;
import df.bean.obj.doctor.DoctorList;

public class DoctorProfileService extends Doctor{
	
	public ArrayList<Doctor> GetDoctorsByDoctorCode(String doctorCode,String hospitalCode) {
		String SQL_DOCTOR = "select distinct "+
				"  DOCTOR.CODE as DOCTOR_CODE"+
				//" ,DOCTOR_CLASS.CLASS_NAME as CLASS_NAME " +
                " ,DOCTOR.HOSPITAL_CODE as HOSPITAL_CODE " +
                " ,DOCTOR.HOSPITAL_UNIT_CODE as HOSPITAL_UNIT_CODE " +
                " ,DOCTOR.DOCTOR_PROFILE_CODE as DOCTOR_PROFILE_CODE " +
                " ,DOCTOR.GUARANTEE_DR_CODE as GUARANTEE_DR_CODE " +
                " ,DOCTOR.NAME_THAI as NAME_THAI " +
                " ,DOCTOR.NAME_ENG as NAME_ENG " +
                " ,DOCTOR.LICENSE_ID as LICENSE_ID " +
                " ,DOCTOR.FROM_DATE as FROM_DATE " +
                " ,DOCTOR.TO_DATE as TO_DATE " +
                " ,DOCTOR.BANK_ACCOUNT_NO as BANK_ACCOUNT_NO " +
                " ,DOCTOR.BANK_ACCOUNT_NAME as BANK_ACCOUNT_NAME " +
                " ,DOCTOR.BANK_BRANCH_CODE as BANK_BRANCH_CODE " +
                " ,DOCTOR.BANK_CODE as BANK_CODE " +
                " ,DOCTOR.DOCTOR_TYPE_CODE as DOCTOR_TYPE_CODE " +
                " ,DOCTOR.DOCTOR_CATEGORY_CODE as DOCTOR_CATEGORY_CODE " +
                " ,DOCTOR.PAYMENT_MODE_CODE as PAYMENT_MODE_CODE " +
                " ,DOCTOR.DEPARTMENT_CODE as DEPARTMENT_CODE " +
                " ,DOCTOR.TAX_ID as TAX_ID " +
                " ,DOCTOR.IS_ADVANCE_PAYMENT as IS_ADVANCE_PAYMENT " +
                " ,DOCTOR.ACTIVE as ACTIVE " +     
                " ,DOCTOR.IS_HOLD as IS_HOLD " +
                " ,DOCTOR.SALARY as SALARY " +
                " ,DOCTOR.POSITION_AMT as POSITION_AMT " +
                " from DOCTOR"  +
                " where DOCTOR.CODE = '"+doctorCode+"' and DOCTOR.HOSPITAL_CODE = '"+hospitalCode+"'";
		
		return GetDoctors(SQL_DOCTOR);
	        
	}
	 
	 private ArrayList<Doctor> GetDoctors(String sql) {
	        Statement stmt = null;
	        ResultSet rs = null;
	        String className = "", doctorCode = "";
	        ArrayList<Doctor> doctorResultList = new ArrayList<Doctor>();

	        try {
	          
	        	DBConnection objConn  =  new DBConnection();
	        	objConn.connectToLocal();
	            rs = objConn.executeQuery(sql);
	            
	            int i=0;
	            while (rs.next()) {
	                //className = (String)rs.getString("CLASS_NAME");
	                //doctorCode = (String)rs.getString("DOCTOR_CODE");
	               
	                try {
	                   
	                    Doctor doctor = new Doctor();

	                    doctorResultList.add(doctor);
	                                     
	                    //////////////////////////////////////
	                    doctorResultList.get(i).setCode(rs.getString("DOCTOR_CODE"));
	                    doctorResultList.get(i).setNameThai(rs.getString("NAME_THAI"));
	                    doctorResultList.get(i).setNameEng(rs.getString("Name_Eng"));
	                    doctorResultList.get(i).setLicenseId(rs.getString("License_ID"));
	                    doctorResultList.get(i).setFromDate(rs.getString("From_Date"));
	                    doctorResultList.get(i).setToDate(rs.getString("To_Date"));
	                    doctorResultList.get(i).setBankAccountNo(rs.getString("BANK_ACCOUNT_NO"));
	                    doctorResultList.get(i).setHospitalCode(rs.getString("HOSPITAL_CODE"));
	                    doctorResultList.get(i).setDoctorProfileCode(rs.getString("Doctor_Profile_Code"));
	                    doctorResultList.get(i).setBankBranchCode(rs.getString("BANK_BRANCH_CODE"));
	                    doctorResultList.get(i).setBankCode(rs.getString("BANK_CODE"));
	                    doctorResultList.get(i).setDoctorTypeCode(rs.getString("Doctor_Type_Code"));
	                    doctorResultList.get(i).setDoctorCategoryCode(rs.getString("DOCTOR_CATEGORY_CODE"));
	                    doctorResultList.get(i).setDepartmentCode(rs.getString("Department_Code"));
	                    doctorResultList.get(i).setPaymentModeCode(rs.getString("Payment_Mode_Code"));
	                    doctorResultList.get(i).setTaxId(rs.getString("Tax_ID"));
	                    doctorResultList.get(i).setActive(rs.getString("ACTIVE"));
	                    doctorResultList.get(i).setBankAccountName(rs.getString("Bank_Account_Name"));
	                    doctorResultList.get(i).setIsAdvancePayment(rs.getString("Is_Advance_Payment"));
	                    doctorResultList.get(i).setGuranteeDoctorCode(rs.getString("Guarantee_DR_Code"));
	                    doctorResultList.get(i).setHospitalUnitCode(rs.getString("Hospital_Unit_Code"));
	                    doctorResultList.get(i).setIsHold(rs.getString("IS_HOLD"));
	                    
	                    i++;
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
	        
	        return doctorResultList;
	    
	    }
	 
	 public ArrayList<String> GetDoctorCodesByDoctorProflie(String doctorProfile,String exceptDrCode, String hospitalCode){
		 String sql = "select CODE as DOCTOR_CODE "
		 		+ "from DOCTOR "
		 		+ "where DOCTOR_PROFILE_CODE = '"+doctorProfile+"' and CODE != '"+exceptDrCode+"' and HOSPITAL_CODE = '"
		 		+ hospitalCode+"'";
		 
	        Statement stmt = null;
	        ResultSet rs = null;
	        
	        ArrayList<String> doctorCodesResultList = new ArrayList<String>();

	        try {
	          
	        	DBConnection objConn  =  new DBConnection();
	        	objConn.connectToLocal();
	            rs = objConn.executeQuery(sql);
	            
	            while (rs.next()) {
	                //className = (String)rs.getString("CLASS_NAME");
	                //doctorCode = (String)rs.getString("DOCTOR_CODE");
	               
	                try {
	                    doctorCodesResultList.add(rs.getString("DOCTOR_CODE"));
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
	        
	        return doctorCodesResultList;
	    
	    }
	 
	 

}
