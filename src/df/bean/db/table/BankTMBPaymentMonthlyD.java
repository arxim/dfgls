package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.Utils;

public class BankTMBPaymentMonthlyD extends ABSTable{

    private String hospitalCode;
    private String yyyy;
    private String mm;
    private String recordType;
    private String sequenceNumber;
    private String bankCode;
    private String accountNumber;
    private String transactionCode;
    private String amount;
    private String serviceType;
    private String status;
    private String referenceArea1;
    private String inserviceDate;
    private String companyCode;
    private String homeBranch;
    private String referenceArea2;
    private String tmbFlag;
    private String spare;
    private String accountName;

    
    public final String RECORD_TYPE = "D";
    public final String BANK_CODE = "011";
    public final String TRANSACTION_CODE = "C";
    public final String SERVICE_TYPE = "08";
    public final String STATUS = "9";
    public final String COMPANY_CODE = "2222";
    public final String HOME_BRANCH = "001";
    public final String REFERENCE_AREA_2 = "Ref 2               ";
    public final String TMB_FLAG = "000002";

    
    public BankTMBPaymentMonthlyD() {
        super();
    }
    public BankTMBPaymentMonthlyD(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }

    public String getAccountName() {
        if (this.accountName == null) { this.accountName = ""; }
        this.accountName = Utils.replAheadWith(" ", this.accountName, 35);
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        if (this.accountNumber == null) { this.accountNumber = ""; }
        this.accountNumber = Utils.replAheadWith("0", this.accountNumber, 10);
        return this.accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAmount() {
        if (this.amount == null) { this.amount = "0"; }
        this.amount = Utils.removeString(".", this.amount);
        this.amount = Utils.replAheadWith("0", this.amount, 10);
        return amount;
    }

    public void setAmount(String amount) {
        if (this.amount == null) { this.amount = ""; }
        this.amount = amount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        if (this.companyCode == null) { this.companyCode = ""; }
        this.companyCode = companyCode;
    }

    public String getHomeBranch() {
        if (this.homeBranch == null) { this.homeBranch = ""; }
        return homeBranch;
    }

    public void setHomeBranch(String homeBranch) {
        this.homeBranch = homeBranch;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getInserviceDate() {
        if (inserviceDate == null) { inserviceDate = ""; }
        inserviceDate = Utils.replFollowWith(" ", inserviceDate, 6);
        return inserviceDate;
    }

    public void setInserviceDate(String inserviceDate) {
        this.inserviceDate = inserviceDate;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getReferenceArea1() {
        return referenceArea1;
    }

    public void setReferenceArea1(String referenceArea1) {
        if (this.referenceArea1 == null) { this.referenceArea1 = ""; }
        this.referenceArea1 = Utils.replFollowWith(" ", this.referenceArea1, 10);
        this.referenceArea1 = referenceArea1;
    }

    public String getReferenceArea2() {
        if (this.referenceArea2 == null) { this.referenceArea2 = ""; }
        this.referenceArea2 = Utils.replAheadWith(" ", this.referenceArea2, 20);
        return referenceArea2;
    }

    public void setReferenceArea2(String referenceArea2) {
        this.referenceArea2 = referenceArea2;
    }

    public String getSequenceNumber() {
        if (sequenceNumber == null) { sequenceNumber = ""; }
        sequenceNumber = Utils.replAheadWith("0", sequenceNumber, 6);
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSpare() {
        if (this.spare == null) { this.spare = ""; }
        this.spare = Utils.replAheadWith("0", spare, 10);
        return spare;
    }

    public void setSpare(String spare) {
        this.spare = spare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTmbFlag() {
        return tmbFlag;
    }

    public void setTmbFlag(String tmbFlag) {
        this.tmbFlag = tmbFlag;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    
    public boolean insert() {
        boolean ret = false;
        String tableName = "BANK_TMB_PAYMENT_MONTHLY_D";
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
             this.getResultSet().updateString("ACCOUNT_NUMBER", this.getAccountNumber());
             this.getResultSet().updateString("TRANSACTION_CODE", this.getTransactionCode());
             this.getResultSet().updateString("AMOUNT", this.getAmount());
             this.getResultSet().updateString("SERVICE_TYPE", this.getServiceType());
             this.getResultSet().updateString("STATUS", this.getStatus());
             this.getResultSet().updateString("REFERENCE_AREA_1", this.getReferenceArea1());
             this.getResultSet().updateString("INSERVICE_DATE", this.getInserviceDate());
             this.getResultSet().updateString("COMPANY_CODE", this.getCompanyCode());
             this.getResultSet().updateString("HOME_BRANCH", this.getHomeBranch());
             this.getResultSet().updateString("REFERENCE_AREA_2", this.getReferenceArea2());
             this.getResultSet().updateString("TMB_FLAG", this.getTmbFlag());
             this.getResultSet().updateString("SPARE", this.getSpare());
             this.getResultSet().updateString("ACCOUNT_NAME", this.getAccountName());

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
        String sql1 = "delete from BANK_TMB_PAYMENT_MONTHLY_D WHERE YYYY='" + yyyy + "'";
                sql1 = sql1 + " AND MM = '" + mm + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
        
}
