package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import df.bean.db.conn.DBConnection;
import java.util.ArrayList;
import java.util.List;

public class SummaryTax406 extends ABSTable{

    private String yyyy;
    private String mm;
    private String dd;
    private String hospitalCode;
    private String doctorCode;
    private String sumAmt;
    private String sumDrAmt;
    private Double sumHpAmt;
    private String sumTaxDrAmt;
    private Double sumTaxHpAmt;
    private String textSumDrAmt;
    private String textSumTaxDrAmt;
    private String printDate;
    private String updateDate;
    private String updateTime;
    private String userId;

    public SummaryTax406(DBConnection conn) {
        super();
        this.setDBConnection(conn);
        this.setSumAmt("");
        this.setSumDrAmt("");
        this.setSumHpAmt(0d);
        this.setSumTaxDrAmt("");
        this.setSumTaxHpAmt(0d);
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

    public String getSumAmt() {
        return this.sumAmt;
    }

    public String getSumDrAmt() {
        return this.sumDrAmt;
    }

    public Double getSumHpAmt() {
        return this.sumHpAmt;
    }

    public String getSumTaxDrAmt() {
        return this.sumTaxDrAmt;
    }

    public Double getSumTaxHpAmt() {
        return this.sumTaxHpAmt;
    }

    public String getTextSumDrAmt() {
        return this.textSumDrAmt;
    }

    public String getTextSumTaxDrAmt() {
        return this.textSumTaxDrAmt;
    }

    public String getYyyy() {
        return this.yyyy;
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

    public void setSumAmt(String sumAmt) {
        this.sumAmt = sumAmt;
    }

    public void setSumDrAmt(String sumDrAmt) {
        this.sumDrAmt = sumDrAmt;
    }

    public void setSumHpAmt(Double sumHpAmt) {
        this.sumHpAmt = sumHpAmt;
    }

    public void setSumTaxDrAmt(String sumTaxDrAmt) {
        this.sumTaxDrAmt = sumTaxDrAmt;
    }

    public void setSumTaxHpAmt(Double sumTaxHpAmt) {
        this.sumTaxHpAmt = sumTaxHpAmt;
    }

    public void setTextSumDrAmt(String textSumDrAmt) {
        this.textSumDrAmt = textSumDrAmt;
    }

    public void setTextSumTaxDrAmt(String textSumTaxDrAmt) {
        this.textSumTaxDrAmt = textSumTaxDrAmt;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }    

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }
    
    public boolean insert() {
        boolean ret = false;
//        ResultSet rs = null;
        try {
            // Create an updatable result set
            String tableName = "SUMMARY_TAX_406";
//            String[] ss;
//            String s1;
            if (this.getStatement()==null) {  
//                ss = this.getDBConnection().getColumnNames(tableName);
//                s1 = this.getDBConnection().getColumnNamesLine(ss);
                this.setStatement(this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)); 
            }
            
            if (this.getResultSet()==null) { this.setResultSet(this.getStatement().executeQuery("SELECT * FROM " + tableName + " where YYYY='0000'")); }
//            rs = this.getResultSet();
            // Move cursor to the "insert row"
            this.getResultSet().moveToInsertRow();
            
            // Set values for the new row.
            this.getResultSet().updateString("YYYY", this.getYyyy());
            this.getResultSet().updateString("MM", this.getMm());
            this.getResultSet().updateString("DD", this.getDd());
            this.getResultSet().updateString("HOSPITAL_CODE", this.getHospitalCode());
            this.getResultSet().updateString("DOCTOR_CODE", this.getDoctorCode());
            this.getResultSet().updateString("SUM_AMT", this.getSumAmt());
            this.getResultSet().updateString("Sum_Dr_Amt", this.getSumDrAmt());
            this.getResultSet().updateDouble("Sum_Hp_Amt", this.getSumHpAmt());
            this.getResultSet().updateString("Sum_Tax_Dr_Amt", this.getSumTaxDrAmt());
            this.getResultSet().updateDouble("Sum_Tax_Hp_Amt", this.getSumTaxHpAmt());
            this.getResultSet().updateString("Text_Sum_Dr_Amt", this.getTextSumDrAmt());
            this.getResultSet().updateString("Text_Sum_Tax_Dr_Amt", this.getTextSumTaxDrAmt());
            this.getResultSet().updateString("Print_Date", this.getPrintDate());
            this.getResultSet().updateString("UPDATE_DATE", this.getUpdateDate());
            this.getResultSet().updateString("Update_Time", this.getUpdateTime());
            this.getResultSet().updateString("USER_ID", this.getUserId());
            // Insert the row
            this.getResultSet().insertRow();
            ret=true;
        } catch (SQLException e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    TRN_Error.PROCESS_COMPUTE_TAX_406, "Duplicate data.", e.getMessage());
        }
        finally {
//                      try {
//                        if (rs != null) { 
//                            rs.close();
//                            rs = null;
//                        }
//                        if (stmt != null) {
//                            stmt.close();
//                            stmt = null;
//                        }
//                      }
//                      catch (SQLException ex)  {
//                        System.out.println("A SQLException error has occured in summarytax406.insert() \n" + ex.getMessage());
//                        ex.printStackTrace();
//                      }
                    }
        return ret;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    // roll back
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from SUMMARY_TAX_406 WHERE YYYY='" + yyyy + "'"
                + " AND MM = '" + mm + "'"
                + " and HOSPITAL_CODE = '" + hospitalCode + "'";
                //+ " and (BATCH_NO IS NULL OR BATCH_NO = '')";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
}
