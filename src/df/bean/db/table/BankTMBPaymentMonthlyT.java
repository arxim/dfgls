package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.Utils;

public class BankTMBPaymentMonthlyT extends ABSTable{

    private String hospitalCode;
    private String yyyy;
    private String mm;
    private String recordType;
    private String sequenceNumber;
    private String bankCode;
    private String companyAccountNo;
    private String noOfDrTransaction;
    private String totalDrAmount;
    private String noOfCrTransaction;
    private String totalCrAmount;
    private String noOfRejectDrTrans;
    private String totalRejectDrAmount;
    private String noOfRejectCrTrans;
    private String totalRejectCrAmount;
    private String spare;
    
    public final String RECORD_TYPE = "T";
    public final String BANK_CODE = "011";

    
    public BankTMBPaymentMonthlyT() {
        super();
        this.setRecordType(RECORD_TYPE);
        this.setBankCode(BANK_CODE);
    }
    public BankTMBPaymentMonthlyT(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCompanyAccountNo() {
        return companyAccountNo;
    }

    public void setCompanyAccountNo(String companyAccountNo) {
        this.companyAccountNo = companyAccountNo;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getNoOfCrTransaction() {
        if (this.noOfCrTransaction == null) { noOfCrTransaction = ""; }
        noOfCrTransaction = Utils.replAheadWith("0", noOfCrTransaction, 7);
        return noOfCrTransaction;
    }

    public void setNoOfCrTransaction(String noOfCrTransaction) {
        this.noOfCrTransaction = noOfCrTransaction;
    }

    public String getNoOfDrTransaction() {
        if (this.noOfDrTransaction == null) { noOfDrTransaction = ""; }
        noOfDrTransaction = Utils.replAheadWith("0", noOfDrTransaction, 7);
        return noOfDrTransaction;
    }

    public void setNoOfDrTransaction(String noOfDrTransaction) {
        this.noOfDrTransaction = noOfDrTransaction;
    }

    public String getNoOfRejectCrTrans() {
        if (noOfRejectCrTrans == null) { noOfRejectCrTrans = ""; }
        noOfRejectCrTrans = Utils.replAheadWith(" ", noOfRejectCrTrans, 7);
        return noOfRejectCrTrans;
    }

    public void setNoOfRejectCrTrans(String noOfRejectCrTrans) {
        this.noOfRejectCrTrans = noOfRejectCrTrans;
    }

    public String getNoOfRejectDrTrans() {
        if (noOfRejectDrTrans == null) { noOfRejectDrTrans = ""; }
        noOfRejectDrTrans = Utils.replAheadWith(" ", noOfRejectDrTrans, 7);
        return noOfRejectDrTrans;
    }

    public void setNoOfRejectDrTrans(String noOfRejectDrTrans) {
        this.noOfRejectDrTrans = noOfRejectDrTrans;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getSequenceNumber() {
        if (sequenceNumber == null) { sequenceNumber = ""; }
        sequenceNumber = Utils.replAheadWith("0", sequenceNumber, 6);
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSpare() {
        if (spare == null) { spare = ""; }
        spare = Utils.replAheadWith("0", spare, 28);
        return spare;
    }

    public void setSpare(String spare) {
        this.spare = spare;
    }

    public String getTotalCrAmount() {
        if (this.totalCrAmount == null) { this.totalCrAmount = "0"; }
        this.totalCrAmount = Utils.removeString(".", this.totalCrAmount);
        this.totalCrAmount = Utils.replAheadWith("0", this.totalCrAmount, 13);
        return totalCrAmount;
    }

    public void setTotalCrAmount(String totalCrAmount) {
        this.totalCrAmount = totalCrAmount;
    }

    public String getTotalDrAmount() {
        if (this.totalDrAmount == null) { this.totalDrAmount = "0.0"; }
        this.totalDrAmount = Utils.removeString(".", this.totalDrAmount);
        this.totalDrAmount = Utils.replAheadWith("0", this.totalDrAmount, 13);
        return totalDrAmount;
    }

    public void setTotalDrAmount(String totalDrAmount) {
        this.totalDrAmount = totalDrAmount;
    }

    public String getTotalRejectCrAmount() {
        if (this.totalRejectCrAmount == null) { this.totalRejectCrAmount = ""; }
//        this.totalRejectCrAmount = Utils.removeString(".", this.totalRejectCrAmount);
        this.totalRejectCrAmount = Utils.replAheadWith(" ", this.totalRejectCrAmount, 13);
        return totalRejectCrAmount;
    }

    public void setTotalRejectCrAmount(String totalRejectCrAmount) {
        this.totalRejectCrAmount = totalRejectCrAmount;
    }

    public String getTotalRejectDrAmount() {
        if (this.totalRejectDrAmount == null) { this.totalRejectDrAmount = ""; }
//        this.totalRejectDrAmount = Utils.removeString(".", this.totalRejectDrAmount);
        this.totalRejectDrAmount = Utils.replAheadWith(" ", this.totalRejectDrAmount, 13);
        return totalRejectDrAmount;
    }

    public void setTotalRejectDrAmount(String totalRejectDrAmount) {
        this.totalRejectDrAmount = totalRejectDrAmount;
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    
    public boolean insert() {
        boolean ret = false;
        String tableName = "BANK_TMB_PAYMENT_MONTHLY_T";
        try {
             // Create an updatable result set
            if (this.getStatement() == null) {
                 String[] ss = this.getDBConnection().getColumnNames(tableName);
                 String s1 = this.getDBConnection().getColumnNamesLine(ss);
                 this.setStatement(this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE));
            }
             this.setResultSet(this.getStatement().executeQuery("SELECT * FROM " + tableName + " where HOSPITAL_CODE='0'"));
             // Move cursor to the "insert row"
             this.getResultSet().moveToInsertRow();
         
             // Set values for the new row.
             this.getResultSet().updateString("yyyy", this.getYyyy());
             this.getResultSet().updateString("MM", this.getMm());
             this.getResultSet().updateString("HOSPITAL_CODE", this.getHospitalCode());
             this.getResultSet().updateString("RECORD_TYPE", this.getRecordType());
             this.getResultSet().updateString("SEQUENCE_NUMBER", this.getSequenceNumber());
             this.getResultSet().updateString("BANK_CODE", this.getBankCode());
             this.getResultSet().updateString("COMPANY_ACCOUNT_NO", this.getCompanyAccountNo());
             this.getResultSet().updateString("NO_OF_DR_TRANSACTION", this.getNoOfDrTransaction());
             this.getResultSet().updateString("TOTAL_DR_AMOUNT", this.getTotalDrAmount());
             this.getResultSet().updateString("NO_OF_CR_TRANSACTION", this.getNoOfCrTransaction());
             this.getResultSet().updateString("TOTAL_CR_AMOUNT", this.getTotalCrAmount());
             this.getResultSet().updateString("NO_OF_REJECT_DR_TRANS", this.getNoOfRejectDrTrans());
             this.getResultSet().updateString("TOTAL_REJECT_DR_AMOUNT", this.getTotalRejectDrAmount());
             this.getResultSet().updateString("NO_OF_REJECT_CR_TRANS", this.getNoOfRejectCrTrans());
             this.getResultSet().updateString("TOTAL_REJECT_CR_AMOUNT", this.getTotalRejectCrAmount());
             this.getResultSet().updateString("SPARE", this.getSpare());             
             
             // Insert the row
             this.getResultSet().insertRow();
             ret = true;
         } catch (SQLException e) {
             e.printStackTrace();
             TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
             ret = false;
         }
        finally {
                    }
         return ret;
    }
        
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from BANK_TMB_PAYMENT_MONTHLY_H WHERE YYYY='" + yyyy + "'";
                sql1 = sql1 + " AND MM = '" + mm + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
        
}
