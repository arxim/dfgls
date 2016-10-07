package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.Utils;

public class BankTMBPaymentMonthlyH extends ABSTable{

    private String hospitalCode;
    private String yyyy;
    private String mm;
    private String recordType;
    private String sequenceNumber;
    private String bankCode;
    private String companyAccountNo;
    private String companyName;
    private String postDate;
    private String tapeNumber;
    private String spare;
    
    public final String RECORD_TYPE = "H";
    public final String SEQUENCE_NUMBER = "000001";
    public final String BANK_CODE = "011";

    
    public BankTMBPaymentMonthlyH() {
        super();
    }
    public BankTMBPaymentMonthlyH(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }

    public String getBankCode() {
        if (bankCode == null) { bankCode = ""; }
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCompanyAccountNo() {
        if (this.companyAccountNo == null) { this.companyAccountNo = ""; }
        return companyAccountNo;
    }

    public void setCompanyAccountNo(String companyAccountNo) {
        this.companyAccountNo = companyAccountNo;
    }

    public String getCompanyName() {
        if (this.companyName == null) { this.companyName = ""; }
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getPostDate() {
        if (this.postDate == null) { this.postDate = ""; }
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getSequenceNumber() {
        if (sequenceNumber == null) { sequenceNumber = ""; }
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSpare() {
        if (spare == null) { spare = ""; }
        spare = Utils.replAheadWith("0", spare, 10);
        return spare;
    }

    public void setSpare(String spare) {
        this.spare = spare;
    }

    public String getTapeNumber() {
        tapeNumber = "      ";
        return tapeNumber;
    }

    public void setTapeNumber(String tapeNumber) {
        this.tapeNumber = tapeNumber;
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }
    
    public boolean insert() {
        boolean ret = false;
        try {
             // Create an updatable result set
            if (this.getStatement() == null) {
                 String tableName = "BANK_TMB_PAYMENT_MONTHLY_H";
                 String[] ss = this.getDBConnection().getColumnNames(tableName);
                 String s1 = this.getDBConnection().getColumnNamesLine(ss);
                 this.setStatement(this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE));
                 this.setResultSet(this.getStatement().executeQuery("SELECT " + s1 + " FROM " + tableName + " where HOSPITAL_CODE='0'"));
            }
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
             this.getResultSet().updateString("COMPANY_NAME", this.getCompanyName());
             this.getResultSet().updateString("POST_DATE", this.getPostDate());
             this.getResultSet().updateString("TAPE_NUMBER", this.getTapeNumber());
             this.getResultSet().updateString("SPARE", this.getSpare());

             // Insert the row
             this.getResultSet().insertRow();
             ret = true;
         } catch (SQLException e) {
             e.printStackTrace();
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
