package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;
import java.util.ArrayList;
import java.util.List;

public class Doctor  extends ABSTable{

    private String code;
    private String nameThai;
    private String nameEng;
    private String licenseId;
    private String fromDate;
    private String toDate;
    private String bankAccountNo;
    private String hospitalCode;
    private String doctorProfileCode;
    private String bankBranchCode;
    private String bankCode;
    private String doctorTypeCode;
    private String doctorCategoryCode;
    private String departmentCode;
    private String paymentModeCode;
    private String taxId;
    private String active;
    private String bankAccountName;
    private String isAdvancePayment;
    private String guranteeDoctorCode;
    private String hospitalUnitCode;
    private String isHold = "";
    private Double salary = 0d;
    private Double positionAmt = 0d;
//    ResultSet rs = null;
//    Statement stmt = null;
static private String SQL_DOCTOR = "select distinct DOCTOR.CODE as DOCTOR_CODE, DOCTOR_CLASS.CLASS_NAME as CLASS_NAME " +
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
                " from DOCTOR";

    static public String getSQL_DOCTOR() {
        return SQL_DOCTOR;
    }    
    
    static public String getSQL_DOCTOR(String hospitalCode) {
        return SQL_DOCTOR + " and HOSPITAL_CODE='" + hospitalCode + "' and ACTIVE = '1'";
    }
    public Doctor() {
        super();
    }

    public Doctor(DBConnection dBConnection) {
        this.setDBConnection(dBConnection);
    }
    
    public String getHospitalUnitCode() {
        return hospitalUnitCode;
    }

    public void setHospitalUnitCode(String hospitalUnitCode) {
        this.hospitalUnitCode = hospitalUnitCode;
    }

    public String getBankAccountNo() {
        return this.bankAccountNo;
    }

    public String getBankBranchCode() {
        return this.bankBranchCode;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public String getCode() {
        return this.code;
    }

    public String getDoctorCategoryCode() {
        return this.doctorCategoryCode;
    }

    public String getDoctorTypeCode() {
        return this.doctorTypeCode;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getLicenseId() {
        return this.licenseId;
    }

    public String getNameEng() {
        return this.nameEng;
    }

    public String getNameThai() {
        return this.nameThai;
    }

    public String getToDate() {
        return this.toDate;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public void setBankBranchCode(String bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDoctorCategoryCode(String doctorCategoryCode) {
        this.doctorCategoryCode = doctorCategoryCode;
    }

    public void setDoctorTypeCode(String doctorTypeCode) {
        this.doctorTypeCode = doctorTypeCode;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public void setNameThai(String nameThai) {
        this.nameThai = nameThai;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
    public String getPaymentModeCode() {
        return paymentModeCode;
    }

    public void setPaymentModeCode(String paymentModeCode) {
        this.paymentModeCode = paymentModeCode;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getIsAdvancePayment() {
        return isAdvancePayment;
    }

    public void setIsAdvancePayment(String isAdvancePayment) {
        this.isAdvancePayment = isAdvancePayment;
    }

    public String getGuranteeDoctorCode() {
        return guranteeDoctorCode;
    }

    public void setGuranteeDoctorCode(String guranteeDoctorCode) {
        this.guranteeDoctorCode = guranteeDoctorCode;
    }

    public String getDoctorProfileCode() {
        return doctorProfileCode;
    }

    public void setDoctorProfileCode(String doctorProfileCode) {
        this.doctorProfileCode = doctorProfileCode;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getIsHold() {
        return isHold;
    }

    public void setIsHold(String isHold) {
        this.isHold = isHold;
    }

    public Double getPositionAmt() {
        return positionAmt;
    }

    public void setPositionAmt(Double positionAmt) {
        if (positionAmt == null) positionAmt = 0d;
        this.positionAmt = positionAmt;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        if (salary == null) salary = 0d;
        this.salary = salary;
    }
    
    public Doctor(String code, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        String sql = "select * from DOCTOR where CODE='" + code + "'" +
                            " and HOSPITAL_CODE='" + hospitalCode + "'";
        
        try {
            if (this.getStatement() == null) { this.setStatement(this.conn.getConnection().createStatement()); }
            this.setResultSet(this.getStatement().executeQuery(sql));
            
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.nameThai = this.getResultSet().getString("NAME_THAI");
                this.nameEng = this.getResultSet().getString("Name_Eng");
                this.licenseId = this.getResultSet().getString("License_ID");
                this.fromDate = this.getResultSet().getString("From_Date");
                this.toDate = this.getResultSet().getString("To_Date");
                this.bankAccountNo = this.getResultSet().getString("BANK_ACCOUNT_NO");
                this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
                this.doctorProfileCode = this.getResultSet().getString("Doctor_Profile_Code");
                this.bankBranchCode = this.getResultSet().getString("BANK_BRANCH_CODE");
                this.bankCode = this.getResultSet().getString("BANK_CODE");
                this.doctorTypeCode = this.getResultSet().getString("Doctor_Type_Code");
                this.doctorCategoryCode = this.getResultSet().getString("DOCTOR_CATEGORY_CODE");
                this.departmentCode = this.getResultSet().getString("Department_Code");
                this.paymentModeCode = this.getResultSet().getString("Payment_Mode_Code");
                this.taxId = this.getResultSet().getString("Tax_ID");
                this.active = this.getResultSet().getString("ACTIVE");
                this.bankAccountName = this.getResultSet().getString("Bank_Account_Name");
                this.isAdvancePayment = this.getResultSet().getString("Is_Advance_Payment");
                this.guranteeDoctorCode = this.getResultSet().getString("Guarantee_DR_Code");
                this.guranteeDoctorCode = this.getResultSet().getString("Hospital_Unit_Code");
                this.isHold = this.getResultSet().getString("IS_HOLD");
                this.salary = this.getResultSet().getDouble("SALARY");
                this.positionAmt = this.getResultSet().getDouble("POSITION_AMT");
                
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        } finally {
               //Clean up resources, close the connection.
               if(this.getResultSet() != null) {
                  try {
                     this.getResultSet().close();
                     this.setResultSet(null);
//                     this.getStatement().close();
//                     this.setStatement(null);
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
    }
    
    //update receipt by doctor
    public int updateReceipt(String YYYY, String MM, String hospitalCode, String tableName) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET " + tableName + ".PAY_BY_DOCTOR = 'Y' "
                    + " ,YYYY = '" + YYYY + "'"
                    + " ,MM = '" + MM + "'"
                    + " WHERE " + tableName + ".PAY_BY_DOCTOR = 'N' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND " + tableName + ".DOCTOR_CODE IN (SELECT CODE FROM DOCTOR WHERE ACTIVE = '1' AND IS_ADVANCE_PAYMENT = 'Y') "
                    + " AND (TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
            rows = this.getDBConnection().executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    } 
    
    //update receipt by doctor no handicraft filter
    /*
    public int updateReceipt(String YYYY, String MM, String hospitalCode, String tableName, String doctorCode) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET " + tableName + ".PAY_BY_DOCTOR = 'Y' "
                    + " ,YYYY = '" + YYYY + "'"
                    + " ,MM = '" + MM + "'"
                    + " WHERE " + tableName + ".PAY_BY_DOCTOR = 'N' "
                    + " AND IS_ONWARD != 'Y' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND " + tableName + ".DOCTOR_CODE = '" + doctorCode + "'"
                    + " AND (TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
            rows = this.getDBConnection().executeUpdate(sql);

        } catch (Exception e) {
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                TRN_Error.PROCESS_RECEIPT_BY_DOCTOR, 
                                "Receipt By Doctor is Error.", 
                                e.getMessage(), sql);
                e.printStackTrace();
        }
        return rows;
    }
    */
    
    public int updateReceipt(String YYYY, String MM, String hospitalCode, String tableName, String doctorCode) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET " + tableName + ".PAY_BY_DOCTOR = 'Y' "
                    + " ,YYYY = '" + YYYY + "'"
                    + " ,MM = '" + MM + "'"
                    + " FROM TRN_DAILY "
                    + " INNER JOIN ORDER_ITEM ON TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "
                    + " AND TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE "
                    + " AND (HANDICRAFT <> 1 OR HANDICRAFT IS NULL) "
                    + " WHERE " + tableName + ".PAY_BY_DOCTOR = 'N' "
                    + " AND IS_ONWARD != 'Y' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND " + tableName + ".HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND " + tableName + ".DOCTOR_CODE = '" + doctorCode + "'"
                    + " AND (TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
            rows = this.getDBConnection().executeUpdate(sql);

        } catch (Exception e) {
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                TRN_Error.PROCESS_RECEIPT_BY_DOCTOR, 
                                "Receipt By Doctor is Error.", 
                                e.getMessage(), sql);
                e.printStackTrace();
        }
        return rows;
    }
    
    public boolean rollBackUpdate(String YYYY, String MM, String hospitalCode, String tableName) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "UPDATE " + tableName + " SET YYYY = ''"
                    + " ,MM = ''"
                    + " WHERE " + tableName + ".PAY_BY_DOCTOR = 'Y' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND (PAY_BY_CASH <> 'Y' AND PAY_BY_AR <> 'Y' AND PAY_BY_PAYOR <> 'Y' AND PAY_BY_CASH_AR <> 'Y')"
//                    + " AND " + tableName + ".PAYOR_OFFICE_CODE IN (SELECT CODE FROM PAYOR_OFFICE WHERE ACTIVE = '1' AND IS_ADVANCE_PAYMENT = 'Y') "
                    + " AND(TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
        
        String sql2 = "UPDATE " + tableName + " SET " + tableName + ".PAY_BY_DOCTOR = 'N' "
                    + " WHERE " + tableName + ".PAY_BY_DOCTOR = 'Y' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
//                    + " AND " + tableName + ".PAYOR_OFFICE_CODE IN (SELECT CODE FROM PAYOR_OFFICE WHERE ACTIVE = '1' AND IS_ADVANCE_PAYMENT = 'Y') "
                    + " AND(TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
        
        sqlCommand.add( sql1 );
        sqlCommand.add( sql2 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
}
