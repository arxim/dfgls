package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class TrnExpenseHeader extends ABSTable{

    private String yyyy;
    private String mm;
    private String doctorCode;
    private String hospitalCode;
    private Double debitAmount=0d;
    private Double creditAmount=0d;
    private String batchNo;

    public TrnExpenseHeader() {
        super();
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public Double getCreditAmount() {
        return this.creditAmount;
    }

    public Double getDebitAmount() {
        return this.debitAmount;
    }

    public String getDoctorCode() {
        return this.doctorCode;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getMm() {
        return this.mm;
    }

    public String getYyyy() {
        return this.yyyy;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }
    
    public TrnExpenseHeader(String yyyy, String mm, String hospitalCode, String doctorCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from TRN_EXPENSE_HEADER where YYYY='" + yyyy + "'" + 
                                                            " and MM ='" + mm + "'" +
                                                            " and DOCTOR_CODE = '" + doctorCode + "'" +
                                                            " and HOSPITAL_CODE='" + hospitalCode + "'"));

        try {
            while (this.getResultSet().next()) {
                this.setYyyy(this.getResultSet().getString("Yyyy"));
                this.setMm(this.getResultSet().getString("MM"));
                this.setDoctorCode(this.getResultSet().getString("DOCTOR_CODE"));
                this.setHospitalCode(this.getResultSet().getString("HOSPITAL_CODE"));
                this.setCreditAmount(this.getResultSet().getDouble("Credit_Amount"));
                this.setDebitAmount(this.getResultSet().getDouble("Debit_Amount"));
                this.setBatchNo(this.getResultSet().getString("BATCH_NO"));
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
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
    }

}
