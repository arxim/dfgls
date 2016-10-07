/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.obj.doctor;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Doctor;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Constructor;

/**
 *
 * @author admin
 */
public class DoctorList {
    private DBConnection conn = null;
    List <CareProvider> doctorLst = new ArrayList <CareProvider> ();
    String hospitalCode = "";

    public DBConnection getDBConnection() {
        return conn;
    }
    
    public DoctorList(String hospitalCode, DBConnection conn) {
    	System.out.println("New Doctor List");
        this.conn = conn;
        this.hospitalCode = hospitalCode;
    }

    public DBConnection getConn() {
        return conn;
    }

    public List getDoctorLst() {
        return doctorLst;
    }
    
    public void newDoctorForComputeDaily(String startDate, String endDate, String hospitalCode) {
        String sql = Doctor.getSQL_DOCTOR() 
                + " INNER JOIN DOCTOR_CATEGORY on DOCTOR.DOCTOR_CATEGORY_CODE=DOCTOR_CATEGORY.CODE"
                + " INNER JOIN DOCTOR_CLASS on DOCTOR_CATEGORY.DOCTOR_CLASS_CODE=DOCTOR_CLASS.CODE" 
                + " INNER JOIN TRN_DAILY on DOCTOR.CODE=TRN_DAILY.DOCTOR_CODE" 
                + " where DOCTOR.HOSPITAL_CODE='" + hospitalCode + "'" 
                + " AND (TRANSACTION_DATE >='" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "')" 
                + " AND (COMPUTE_DAILY_DATE = '' OR COMPUTE_DAILY_DATE IS NULL)"
                + " AND (BATCH_NO IS NULL OR BATCH_NO = '')"
                + " and DOCTOR.ACTIVE='1'";
        newDoctor(sql);
    }
    
    public void newAllDoctor(String hospitalCode) {
        String sql = Doctor.getSQL_DOCTOR() 
                + " INNER JOIN DOCTOR_CATEGORY on DOCTOR.DOCTOR_CATEGORY_CODE=DOCTOR_CATEGORY.CODE"
                + " INNER JOIN DOCTOR_CLASS on DOCTOR_CATEGORY.DOCTOR_CLASS_CODE=DOCTOR_CLASS.CODE" 
                + " where DOCTOR.HOSPITAL_CODE='" + hospitalCode + "'" 
                + " and DOCTOR.ACTIVE='1'";
        newDoctor(sql);
    }
    
    private void newDoctor(String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        String className = "", doctorCode = "";


        try {
            stmt = this.getConn().getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            this.doctorLst.clear();
            int i=0;
            while (rs.next()) {
                className = (String)rs.getString("CLASS_NAME");
                doctorCode = (String)rs.getString("DOCTOR_CODE");
                Class cls = null;
                Constructor c = null;
                Object obj = null;
                try {
                    cls = Class.forName(className);
                    c = cls.getConstructor();  
                    obj = c.newInstance();

                    this.doctorLst.add((CareProvider) obj);
                    this.doctorLst.get(i).setClassName(className);
                    this.doctorLst.get(i).setDoctorCode(doctorCode);
                    this.doctorLst.get(i).setHospitalCode(hospitalCode);
                    this.doctorLst.get(i).setDBConnection(conn);
                    this.doctorLst.get(i).newDoctor();
                    
                    //////////////////////////////////////
                    this.doctorLst.get(i).getDoctor().setCode(rs.getString("DOCTOR_CODE"));
                    this.doctorLst.get(i).getDoctor().setNameThai(rs.getString("NAME_THAI"));
                    this.doctorLst.get(i).getDoctor().setNameEng(rs.getString("Name_Eng"));
                    this.doctorLst.get(i).getDoctor().setLicenseId(rs.getString("License_ID"));
                    this.doctorLst.get(i).getDoctor().setFromDate(rs.getString("From_Date"));
                    this.doctorLst.get(i).getDoctor().setToDate(rs.getString("To_Date"));
                    this.doctorLst.get(i).getDoctor().setBankAccountNo(rs.getString("BANK_ACCOUNT_NO"));
                    this.doctorLst.get(i).getDoctor().setHospitalCode(rs.getString("HOSPITAL_CODE"));
                    this.doctorLst.get(i).getDoctor().setDoctorProfileCode(rs.getString("Doctor_Profile_Code"));
                    this.doctorLst.get(i).getDoctor().setBankBranchCode(rs.getString("BANK_BRANCH_CODE"));
                    this.doctorLst.get(i).getDoctor().setBankCode(rs.getString("BANK_CODE"));
                    this.doctorLst.get(i).getDoctor().setDoctorTypeCode(rs.getString("Doctor_Type_Code"));
                    this.doctorLst.get(i).getDoctor().setDoctorCategoryCode(rs.getString("DOCTOR_CATEGORY_CODE"));
                    this.doctorLst.get(i).getDoctor().setDepartmentCode(rs.getString("Department_Code"));
                    this.doctorLst.get(i).getDoctor().setPaymentModeCode(rs.getString("Payment_Mode_Code"));
                    this.doctorLst.get(i).getDoctor().setTaxId(rs.getString("Tax_ID"));
                    this.doctorLst.get(i).getDoctor().setActive(rs.getString("ACTIVE"));
                    this.doctorLst.get(i).getDoctor().setBankAccountName(rs.getString("Bank_Account_Name"));
                    this.doctorLst.get(i).getDoctor().setIsAdvancePayment(rs.getString("Is_Advance_Payment"));
                    this.doctorLst.get(i).getDoctor().setGuranteeDoctorCode(rs.getString("Guarantee_DR_Code"));
                    this.doctorLst.get(i).getDoctor().setHospitalUnitCode(rs.getString("Hospital_Unit_Code"));
                    this.doctorLst.get(i).getDoctor().setIsHold(rs.getString("IS_HOLD"));
                    
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
    
    }
    
    public int getDoctorIDX(String doctorCode) {
        int ret = -1; 
        for (int i=0;i<this.doctorLst.size();i++) {
            if (doctorCode.equals(this.doctorLst.get(i).getDoctorCode())) {
                return i;
            }
        }
        return ret;
    }
    public CareProvider getDoctorIDX(int index) {
        return this.doctorLst.get(index); 
    }
    
    public CareProvider getDoctor(String doctorCode) {
        int i = getDoctorIDX(doctorCode);
        if (i >= 0) {  return this.doctorLst.get(i); }
        else { return null; }
    }
}
