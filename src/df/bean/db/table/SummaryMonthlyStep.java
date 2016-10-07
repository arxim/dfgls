package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

public class SummaryMonthlyStep extends ABSTable {

    private String doctorCode;
    private String yyyy;
    private String mm;
    private String stepSharingCode;
    private Double SORDER;
    private Double drPercent;
    private Double hpPercent;
    private Double drAmt;
    private Double hpAmt;
    private String updateDate;
    private String updateTime;
    private String userId;
    private String hospitalCode;
    private String oldDoctorCode;

    public SummaryMonthlyStep(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }

    public String getDoctorCode() {
        return this.doctorCode;
    }

    public Double getDrAmt() {
        return this.drAmt;
    }

    public Double getDrPercent() {
        return this.drPercent;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public Double getHpAmt() {
        return this.hpAmt;
    }

    public Double getHpPercent() {
        return this.hpPercent;
    }

    public String getMm() {
        return this.mm;
    }

    public Double getSorder() {
        return this.SORDER;
    }

    public String getStepSharingCode() {
        return this.stepSharingCode;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getYyyy() {
        return this.yyyy;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public void setDrAmt(Double drAmt) {
        this.drAmt = drAmt;
    }

    public void setDrPercent(Double drPercent) {
        this.drPercent = drPercent;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setHpAmt(Double hpAmt) {
        this.hpAmt = hpAmt;
    }

    public void setHpPercent(Double hpPercent) {
        this.hpPercent = hpPercent;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public void setSorder(Double SORDER) {
        this.SORDER = SORDER;
    }

    public void setStepSharingCode(String stepSharingCode) {
        this.stepSharingCode = stepSharingCode;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }
    
    public boolean insert() {
        boolean ret = false;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            // Create an updatable result set
            String tableName = "SUMMARY_MONTHLY_STEP";
            String[] ss = this.getDBConnection().getColumnNames(tableName);
            String s1 = this.getDBConnection().getColumnNamesLine(ss);
            stmt = this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rs = stmt.executeQuery("SELECT " + s1 + " FROM " + tableName);
            // Move cursor to the "insert row"
            rs.moveToInsertRow();
            
            // Set values for the new row.
            rs.updateString("YYYY", this.getYyyy());
            rs.updateString("MM", this.getMm());
            rs.updateString("HOSPITAL_CODE", this.getHospitalCode());
            rs.updateString("DOCTOR_CODE", this.getDoctorCode());
            rs.updateString("UPDATE_DATE", JDate.getDate());
            rs.updateString("Update_Time", JDate.getTime());
            rs.updateString("USER_ID", Variables.getUserID());
            rs.updateString("Step_Sharing_Code", this.getStepSharingCode());
            rs.updateDouble("SORDER", this.getSorder());
            rs.updateDouble("Dr_Percent", this.getDrPercent());
            rs.updateDouble("Hp_Percent", this.getHpPercent());
            rs.updateDouble("DR_AMT", this.getDrAmt());
            rs.updateDouble("HP_AMT", this.getHpAmt()); 
            rs.updateString("OLD_DOCTOR_CODE", this.getDoctorCode());
            
            // Insert the row
            rs.insertRow();
            ret=true;
        } catch (SQLException e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
        }finally {
              try {
                if (rs != null) { 
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
              }
              catch (SQLException ex)  {
                System.out.println("A SQLException error has occured in SummaryMonthlyStep.insert() \n" + ex.getMessage());
                ex.printStackTrace();
              }
            }
        return ret;
    }

    
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from SUMMARY_MONTHLY_STEP WHERE YYYY='" + yyyy + "'" 
                + " AND MM = '" + mm + "'" 
                + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }

    public String getOldDoctorCode() {
        return oldDoctorCode;
    }

    public void setOldDoctorCode(String oldDoctorCode) {
        this.oldDoctorCode = oldDoctorCode;
    }
}
