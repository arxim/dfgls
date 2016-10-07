
package df.bean.process;

import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Status;
import df.bean.db.table.SummaryMonthly402;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

public class ProcessSummaryMonthly402 extends Process{
    private String mm = "";
    private String yyyy = "";
    private String dd = "";
    
    public ProcessSummaryMonthly402(DBConnection conn) {
        super(conn);
        this.setDBConnection(conn);
    }
    protected void finalize() {
//        calculatorFrm = null;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }    
    
    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }
    
    public void run() {
        if (this.getDBConnection().IsClosed()) {  
            if (!this.getDBConnection().Connect()) { DialogBox.ShowError(" Connection Fail!  "); return ;}
        }
        /*
        List values = new ArrayList();
        values.add("---- Start ----");
        values.add(JDate.getDate());
        values.add(JDate.getTime());
//        this.calculatorFrm.setTableValue(values);
        
        this.getDBConnection().beginTrans();
        
        super.run();    // call super 
        if (this.calculatorFrm.getLstDoctorGroupCode().getModel().getSize() == this.calculatorFrm.getLstDoctorGroupCode().getSelectedIndex()+1) {
            if (result) {
                this.getDBConnection().commitTrans();
                if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
                DialogBox.ShowInfo("      ��÷ӧҹ�����z�ó�         ");
            } else {
                this.getDBConnection().rollBackTrans();
                if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
                DialogBox.ShowError("Error in ProcessTax406.Calculate !!!! \nTransactions are rollback.");
            } 
        } else { 
            this.getDBConnection().rollBackTrans();
            if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
            DialogBox.ShowWarning("��÷ӧҹ�ѧ���ú��hӹǹ      \nTransactions are rollback.");
        }
        this.calculatorFrm.stopProcess();        
        values.clear();
        values.add("==== Stop ====");
        values.add(JDate.getDate());
        values.add(JDate.getTime());
        this.calculatorFrm.setTableValue(values);
        values = null; */
    }
    
    public boolean Calculate(String doctorCode, String hospitalCode) {
        boolean ret = false;
       
        List values = new ArrayList();
        values.add(this.getYyyy());
        values.add(this.getMm());
        values.add( doctorCode );

        String sql = "";
        ResultSet rs = null;
        Statement stmt = null;

        try {
            sql = "select yyyy, mm, doctorcode, position_amt, turn_in_amt, turn_out_amt, guarantee_amt, other_amt, ";
            sql = sql + " SUM_NORMAL_TAX_AMT, SUM_TURN_TAX_AMT, NORMAL_TAX_MONTH, ACCU_NORMAL_TAX_MONTH, HOSPITAL_TAX_MONTH,";
            sql = sql + " ACCU_HOSPITAL_TAX_MONTH, NET_TAX_MONTH, TEXT_NET_TAX_MONTH, BANK_ACCOUNT_NO, PAYMENT_MODE_CODE";
            sql = sql + " from SUMMARY_TAX_402, doctor";
            sql = sql + " where SUMMARY_TAX_402.DOCTOR_CODE=DOCTOR.CODE";
            sql = sql + " and yyyy='" + this.getYyyy() + "'";
            sql = sql + " AND MM='" + this.getMm() + "'";
            sql = sql + " and DOCTOR_CODE = '" + doctorCode + "'";
            
            stmt = this.getDBConnection().getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                SummaryMonthly402 sm402 = new SummaryMonthly402(this.getDBConnection());
                sm402.setYyyy(this.getYyyy());
                sm402.setMm(this.getMm());
                sm402.setTransactionDate(JDate.getDate());
                sm402.setSumAmt(rs.getDouble("SUM_NORMAL_TAX_AMT)")+rs.getDouble("SUM_TURN_TAX_AMT")-rs.getDouble("NET_TAX_MONTH") );
                sm402.setSumDiscAmt(0d);
                sm402.setSumPremiumAmt(0d);
                sm402.setSumPatientPaidAmt(sm402.getSumAmt());
                sm402.setDrSumAmt(sm402.getSumAmt());
                sm402.setDrDiscAmt(0d);
                sm402.setDrPremiumAmt(0d);
                sm402.setDrCreditAmt(0d);
                sm402.setDrDebitAmt(0d);
                sm402.setDrNetPaidAmt(sm402.getSumAmt());
                sm402.setDrTax402(rs.getDouble("NET_TAX_MONTH"));
                sm402.setDrTax406(0d);
                sm402.setHpSumAmt(0d);
                sm402.setHpDiscAmt(0d);
                sm402.setHpPremiumAmt(0d);
                sm402.setHpTax(rs.getDouble("HOSPITAL_TAX_MONTH"));
                sm402.setGuarunteeAmt(rs.getDouble("guarantee_amt"));
                sm402.setGuarunteeTax(0d);
                sm402.setHospitalCode(hospitalCode);
                sm402.setDoctorCode(rs.getString("DOCTOR_CODE"));
                sm402.setUpdateDate(JDate.getDate());
                sm402.setUpdateTime(JDate.getTime());
                sm402.setUserID(Variables.getUserID());
                sm402.setStatusModify(Status.STATUS_EDIT);
                sm402.setBatchNo("");
                sm402.setOldDoctorCode(rs.getString("DOCTOR_CODE"));
                sm402.setRemark(rs.getString("TEXT_NET_TAX_MONTH"));
                sm402.setPaymentModeCode(rs.getString("PAYMENT_MODE_CODE"));
                sm402.setRefPaidNo(rs.getString("BANK_ACCOUNT_NO"));
                sm402.setPayDate("");
                sm402.setPaymentTermDate(this.yyyy.concat(this.mm.concat(this.dd)));
                sm402.setDrStepAmt(0d);
                sm402.setHpStepAmt(0d);
                sm402.setSumPremiumRecAmt(0d);
                
                ret = sm402.insert();
                if (!ret) {  break;  }
            }            
        } catch (Exception e) {
            ret = false;
            System.out.print("Error in ProcessSummaryMonthly402.Calculate !!!!!! \n Doctor Code : " + doctorCode);
            DialogBox.ShowError("Error in ProcessSummaryMonthly402.Calculate !!!!!! \n Doctor Code : " + doctorCode );
        } finally {
               //Clean up resources, close the connection.
                try {
                    if(rs != null) {
                        rs.close();
                        rs = null;
                    }
                    if (stmt != null) {
                        stmt.close();
                        stmt = null;
                    }
                } catch (Exception ignored) { 
                    ignored.printStackTrace();   
                    ret = false;
            }
        }
        
        if (result) { result = ret; }  
        
//        this.calculatorFrm.setTableValue(values);
        return ret;
    }

}
