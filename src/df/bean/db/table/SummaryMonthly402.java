package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;
import df.bean.obj.util.JNumber;

public class SummaryMonthly402 extends ABSTable {
    
    private String yyyy = "";
    private String mm = "";
    private String transactionDate;
    private Double sumAmt = 0d;
    private Double sumDiscAmt = 0d;
    private Double sumPremiumAmt = 0d;
    private Double sumPatientPaidAmt = 0d;
    private Double drSumAmt = 0d;
    private Double drDiscAmt = 0d;
    private Double drPremiumAmt = 0d;
    private Double drCreditAmt = 0d;
    private Double drDebitAmt = 0d;
    private Double drNetPaidAmt = 0d;
    private Double drTax402 = 0d;
    private Double drTax406 = 0d;
    private Double hpSumAmt = 0d;
    private Double hpDiscAmt = 0d;
    private Double hpPremiumAmt = 0d;
    private Double hpTax = 0d;
    private String hospitalCode = "";
    private String doctorCode = "";
    private Double guarunteeAmt = 0d;
    private Double guarunteeTax = 0d;
    private String statusModify = "";
    private String batchNo = "";
    private String userID = "";
    private String updateDate = "";
    private String updateTime = "";
    private String oldDoctorCode = "";
    private String remark = "";
    private String paymentModeCode = "";
    private String refPaidNo = "";
    private String payDate = "";
    private String paymentTermDate = "";
    private Double drStepAmt = 0d;
    private Double hpStepAmt = 0d;
    private Double sumPremiumRecAmt = 0d;
    static public ResultSet rs = null;
    static public Statement stmt = null;

    public SummaryMonthly402(DBConnection conn) {
        super();
        this.setDBConnection(conn);
        this.setSumAmt(0d);
        this.setSumDiscAmt(0d);
        this.setSumPatientPaidAmt(0d);
        this.setSumPremiumAmt(0d);
        this.setDrSumAmt(0d);
        this.setDrTax402(0d);
        this.setDrTax406(0d);
        this.setDrPremiumAmt(0d);
        this.setHpSumAmt(0d);
        this.setHpPremiumAmt(0d);
        this.setHpTax(0d);
        this.setDrStepAmt(0d);
        this.setHpStepAmt(0d);
        this.setSumPremiumRecAmt(0d);
    }

    public String getDoctorCode() {
        return this.doctorCode;
    }

    public Double getDrCreditAmt() {
        return this.drCreditAmt;
    }

    public Double getDrDebitAmt() {
        return this.drDebitAmt;
    }

    public Double getDrDiscAmt() {
        return this.drDiscAmt;
    }

    public Double getDrNetPaidAmt() {
        this.drNetPaidAmt = JNumber.setFormat(this.drNetPaidAmt, JNumber.FORMAT_MONEY);
        return this.drNetPaidAmt;
    }

    public Double getDrPremiumAmt() {
        return this.drPremiumAmt;
    }

    public Double getDrSumAmt() {
        return this.drSumAmt;
    }

    public Double getDrTax402() {
        return this.drTax402;
    }

    public Double getDrTax406() {
        return this.drTax406;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public Double getHpDiscAmt() {
        return this.hpDiscAmt;
    }

    public Double getHpPremiumAmt() {
        return this.hpPremiumAmt;
    }

    public Double getHpSumAmt() {
        return this.hpSumAmt;
    }

    public Double getHpTax() {
        return this.hpTax;
    }

    public String getMm() {
        return this.mm;
    }

    public Double getSumAmt() {
        return this.sumAmt;
    }

    public Double getSumDiscAmt() {
        return this.sumDiscAmt;
    }

    public Double getSumPatientPaidAmt() {
        return this.sumPatientPaidAmt;
    }

    public Double getSumPremiumAmt() {
        return this.sumPremiumAmt;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public String getYyyy() {
        return this.yyyy;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public void setDrCreditAmt(Double drCreditAmt) {
        this.drCreditAmt = drCreditAmt;
    }

    public void setDrDebitAmt(Double drDebitAmt) {
        this.drDebitAmt = drDebitAmt;
    }

    public void setDrDiscAmt(Double drDiscAmt) {
        this.drDiscAmt = drDiscAmt;
    }

    public void setDrNetPaidAmt(Double drNetPaidAmt) {
        this.drNetPaidAmt = drNetPaidAmt;
    }

    public void setDrPremiumAmt(Double drPremiumAmt) {
        this.drPremiumAmt = drPremiumAmt;
    }

    public void setDrSumAmt(Double drSumAmt) {
        this.drSumAmt = drSumAmt;
    }

    public void setDrTax402(Double drTax402) {
        this.drTax402 = drTax402;
    }

    public void setDrTax406(Double drTax406) {
        this.drTax406 = drTax406;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setHpDiscAmt(Double hpDiscAmt) {
        this.hpDiscAmt = hpDiscAmt;
    }

    public void setHpPremiumAmt(Double hpPremiumAmt) {
        this.hpPremiumAmt = hpPremiumAmt;
    }

    public void setHpSumAmt(Double hpSumAmt) {
        this.hpSumAmt = hpSumAmt;
    }

    public void setHpTax(Double hpTax) {
        this.hpTax = hpTax;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public void setSumAmt(Double sumAmt) {
        this.sumAmt = sumAmt;
    }

    public void setSumDiscAmt(Double sumDiscAmt) {
        this.sumDiscAmt = sumDiscAmt;
    }

    public void setSumPatientPaidAmt(Double sumPatientPaidAmt) {
        this.sumPatientPaidAmt = sumPatientPaidAmt;
    }

    public void setSumPremiumAmt(Double sumPremiumAmt) {
        this.sumPremiumAmt = sumPremiumAmt;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }
    
    
    public Double getGuarunteeAmt() {
        return guarunteeAmt;
    }

    public void setGuarunteeAmt(Double guarunteeAmt) {
        this.guarunteeAmt = guarunteeAmt;
    }

    public Double getGuarunteeTax() {
        return guarunteeTax;
    }

    public void setGuarunteeTax(Double guarunteeTax) {
        this.guarunteeTax = guarunteeTax;
    }

    public String getStatusModify() {
        return statusModify;
    }

    public void setStatusModify(String statusModify) {
        this.statusModify = statusModify;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    
    public String getOldDoctorCode() {
        return oldDoctorCode;
    }

    public void setOldDoctorCode(String oldDoctorCode) {
        this.oldDoctorCode = oldDoctorCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPaymentModeCode() {
        return paymentModeCode;
    }

    public void setPaymentModeCode(String paymentModeCode) {
        this.paymentModeCode = paymentModeCode;
    }

    public String getRefPaidNo() {
        return refPaidNo;
    }

    public void setRefPaidNo(String refPaidNo) {
        this.refPaidNo = refPaidNo;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPaymentTermDate() {
        return paymentTermDate;
    }

    public void setPaymentTermDate(String paymentTermDate) {
        this.paymentTermDate = paymentTermDate;
    }

    public Double getDrStepAmt() {
        return drStepAmt;
    }

    public void setDrStepAmt(Double drStepAmt) {
        this.drStepAmt = drStepAmt;
    }

    public Double getHpStepAmt() {
        return hpStepAmt;
    }

    public void setHpStepAmt(Double hpStepAmt) {
        this.hpStepAmt = hpStepAmt;
    }

    public Double getSumPremiumRecAmt() {
        return sumPremiumRecAmt;
    }

    public void setSumPremiumRecAmt(Double sumPremiumRecAmt) {
        this.sumPremiumRecAmt = sumPremiumRecAmt;
    }
        
    // not used
    public boolean setStatusCalculated() {
        boolean ret = false;
        String sql = "update SUMMARY_MONTHLY set STATUS_MODIFY='" + Status.STATUS_CALCULATED + "'";
        sql = sql + " ,YYYY =''";
        sql = sql + " ,MM =''";
        sql = sql + " ,UPDATE_DATE='" + JDate.getDate() + "'";
        sql = sql + " ,Update_Time='" + JDate.getTime() + "'";
        sql = sql + " where HOSPITAL_CODE='" + this.hospitalCode + "'";
        sql = sql + " and DOCTOR_CODE='" + this.doctorCode + "'";
        sql = sql + " and (BATCH_NO is null or BATCH_NO = '')";
        sql = sql + " and (STATUS_MODIFY <> '" + Status.STATUS_CALCULATED + "' or STATUS_MODIFY is null or STATUS_MODIFY = '')";
        if (this.conn.executeUpdate(sql)>-1) {
            ret = true;
        } 
        return ret;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
    
    public boolean insert() {
        boolean ret = false;
//        ResultSet rs = null;
//        Statement stmt = null;
        try {
            // Create an updatable result set
            if (rs == null) {
                String tableName = "Summary_Monthly_402";
                String[] ss = this.getDBConnection().getColumnNames(tableName);
                String s1 = this.getDBConnection().getColumnNamesLine(ss);
                stmt = this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rs = stmt.executeQuery("SELECT " + s1 + " FROM " + tableName + " where YYYY='0000'");
            }
            // Move cursor to the "insert row"
            rs.moveToInsertRow();
            
            // Set values for the new row.
            rs.updateString("YYYY", this.getYyyy());
            rs.updateString("MM", this.getMm());
            rs.updateString("HOSPITAL_CODE", this.getHospitalCode());
            rs.updateString("DOCTOR_CODE", this.getDoctorCode());
            rs.updateString("Transaction_Date", this.getTransactionDate());
            rs.updateDouble("SUM_AMT", this.getSumAmt());
            rs.updateDouble("Sum_Disc_Amt", this.getSumDiscAmt());
            rs.updateDouble("Sum_Premium_Amt", this.getSumPremiumAmt());
            rs.updateDouble("Sum_Patient_Paid_Amt", this.getSumPatientPaidAmt());
            rs.updateDouble("DR_SUM_AMT", this.getDrSumAmt());
            rs.updateDouble("DR_Disc_Amt", this.getDrDiscAmt());
            rs.updateDouble("DR_Premium_Amt", this.getDrPremiumAmt());
            rs.updateDouble("DR_Credit_Amt", this.getDrCreditAmt());
            rs.updateDouble("DR_Debit_Amt", this.getDrDebitAmt());
            rs.updateDouble("DR_NET_PAID_AMT", this.getDrNetPaidAmt());
            rs.updateDouble("DR_Tax_40_2", this.getDrTax402());
            rs.updateDouble("DR_TAX_40_6", this.getDrTax406());
            rs.updateDouble("HP_SUM_AMT", this.getHpSumAmt());
            rs.updateDouble("HP_Disc_Amt", this.getHpDiscAmt());
            rs.updateDouble("HP_Premium_Amt",this.getHpPremiumAmt());
            rs.updateDouble("HP_TAX", this.getHpTax());
            rs.updateString("UPDATE_DATE", this.getUpdateDate());
            rs.updateString("Update_Time", this.getUpdateTime());
            rs.updateString("USER_ID", this.getUserID());
            rs.updateDouble("Guaruntee_Amt", this.getGuarunteeAmt());
            rs.updateDouble("Guaruntee_Tax", this.getGuarunteeTax());
            rs.updateString("STATUS_MODIFY", this.getStatusModify());
            rs.updateString("BATCH_NO", this.getBatchNo());
            rs.updateString("OLD_DOCTOR_CODE", this.getDoctorCode());
            rs.updateString("Remark", this.getRemark());
            rs.updateString("Payment_Mode_Code", this.getPaymentModeCode());
            rs.updateString("Ref_Paid_No", this.getRefPaidNo());
            rs.updateString("PAY_DATE", this.getPayDate());
            rs.updateString("Payment_Term_Date", this.getPaymentTermDate());
            rs.updateDouble("DR_Step_Amt", this.getDrStepAmt());
            rs.updateDouble("HP_Step_Amt", this.getHpStepAmt());
            rs.updateDouble("Sum_Premium_Rec_Amt", this.getSumPremiumRecAmt());
            
            // Insert the row
            rs.insertRow();
            ret=true;
        } catch (SQLException e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
        }
/*        finally {
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
                        System.out.println("A SQLException error has occured in SummaryMonthly402.insert() \n" + ex.getMessage());
                        ex.printStackTrace();
                      } 
                    } */
        return ret;
    }
    
    public boolean rollBackUpdate(String hospitalCode, String yyyy, String mm) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update summary_monthly_402 set STATUS_MODIFY = 'R', UPDATE_DATE = '', UPDATE_TIME=''";
                sql1 = sql1 + " ,remark = '', PAYMENT_MODE_CODE='', ref_paid_no = '', PAY_DATE = ''";
                sql1 = sql1 + " WHERE YYYY='" + yyyy + "'";
                sql1 = sql1 + " AND MM='"+ mm + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
                
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from summary_monthly_402 WHERE YYYY='" + yyyy + "'";
                sql1 = sql1 + " AND MM = '" + mm + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }

}
