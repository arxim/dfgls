package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import df.bean.db.conn.DBConnection;

public class BankTMBMediaClearing extends ABSTable{

    private String fileType;
    private String recordType;
    private String batchNumber;
    private String receivingBankCode;
    private String receivingBranchCode;
    private String receivingAccountNo;
    private String sendingBankCode;
    private String sendingBranchCode;
    private String sendingAccountNo;
    private String effectiveDate;
    private String serviceType;
    private String clearingHouseCode;
    private String transferAmount;
    private String receiverInformation;
    private String senderInformation;
    private String otherInformation;
    private String referenceRunningNo;
    private String space = "";
    private String companyCode = "";
    private String sequenceNumber = "";
    private String userId = "";
    private String updateDate = "";
    private String updateTime = "";
    private String batchNo = "";
    private String hospitalCode = "";
    private String yyyy = "";
    private String mm = "";
    private String doctorCode;
//    private DBConnection conn = null;
    
    public final static String FILE_TYPE = "10";
    public final static String RECORD_TYPE = "2";
    public final static String BATCH_NUMBER = "100001";
    public final static String SENDING_BANK_CODE = "011";
    public final static String CLEARING_HOUSE_CODE = "00";
    public final static String SERVICE_TYPE_PAYROLL = "01";
    public final static String SERVICE_TYPE_PAYMENT = "04";
    public final static String DATE_OF_ORDER = "";
    public final static String INFORMATION_ORDER = "";
    public final static String OTHER_INFORMATION = "";
    public final static String FILTER = "";
    
    public final static String COMPANY_CODE = "0515";
    
    
    public BankTMBMediaClearing() {
        super();
    }
    public BankTMBMediaClearing(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }
    public String getBatchNo() {
        return this.batchNo;
    }

    public String getBatchNumber() {
        return this.batchNumber;
    }

    public String getClearingHouseCode() {
        return this.clearingHouseCode;
    }

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public String getFileType() {
        return this.fileType;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getOtherInformation() {
        return this.otherInformation;
    }

    public String getReceiverInformation() {
        if (this.receiverInformation.length() > 59) {
            this.receiverInformation = this.receiverInformation.substring(0,58);
        }
        return this.receiverInformation;
    }

    public String getReceivingAccountNo() {
        return this.receivingAccountNo.substring(0,11);
    }

    public String getReceivingBankCode() {
        return this.receivingBankCode;
    }

    public String getReceivingBranchCode() {
        return this.receivingBranchCode;
    }

    public String getRecordType() {
        return this.recordType;
    }

    public String getReferenceRunningNo() {
        return this.referenceRunningNo;
    }

    public String getSenderInformation() {
        return this.senderInformation;
    }

    public String getSendingAccountNo() {
        return this.sendingAccountNo;
    }

    public String getSendingBankCode() {
        return this.sendingBankCode;
    }

    public String getSendingBranchCode() {
        return this.sendingBranchCode;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public String getTransferAmount() {
        return this.transferAmount;
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

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public void setClearingHouseCode(String clearingHouseCode) {
        this.clearingHouseCode = clearingHouseCode;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setOtherInformation(String otherInformation) {
        this.otherInformation = otherInformation;
    }

    public void setReceiverInformation(String receiverInformation) {
        this.receiverInformation = receiverInformation;
    }

    public void setReceivingAccountNo(String receivingAccountNo) {
        this.receivingAccountNo = receivingAccountNo;
    }

    public void setReceivingBankCode(String receivingBankCode) {
        this.receivingBankCode = receivingBankCode;
    }

    public void setReceivingBranchCode(String receivingBranchCode) {
        this.receivingBranchCode = receivingBranchCode;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public void setReferenceRunningNo(String referenceRunningNo) {
        this.referenceRunningNo = referenceRunningNo;
    }

    public void setSenderInformation(String senderInformation) {
        this.senderInformation = senderInformation;
    }

    public void setSendingAccountNo(String sendingAccountNo) {
        this.sendingAccountNo = sendingAccountNo;
    }

    public void setSendingBankCode(String sendingBankCode) {
        this.sendingBankCode = sendingBankCode;
    }

    public void setSendingBranchCode(String sendingBranchCode) {
        this.sendingBranchCode = sendingBranchCode;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }
    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }
        
    public boolean insert() {
        boolean ret = false;
        try {
             // Create an updatable result set
            if (this.getStatement() == null) {
                String tableName = "BANK_TMB_MEDIA_CLEARING";
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
            this.getResultSet().updateString("File_Type", this.getFileType());
            this.getResultSet().updateString("Record_Type", this.getRecordType());
            this.getResultSet().updateString("Batch_Number", this.getBatchNumber());
            this.getResultSet().updateString("Receiving_Bank_Code", this.getReceivingBankCode());
            this.getResultSet().updateString("Receiving_Branch_Code", this.getReceivingBranchCode());
            this.getResultSet().updateString("Receiving_Account_No", this.getReceivingAccountNo());
            this.getResultSet().updateString("Sending_Bank_Code", this.getSendingBankCode());
            this.getResultSet().updateString("Sending_Branch_Code", this.getSendingBranchCode());
            this.getResultSet().updateString("Sending_Account_No", this.getSendingAccountNo());
            this.getResultSet().updateString("Effective_Date", this.getEffectiveDate());
            this.getResultSet().updateString("Service_Type", this.getServiceType());
            this.getResultSet().updateString("Clearing_House_Code", this.getClearingHouseCode());
            this.getResultSet().updateString("Transfer_Amount", this.getTransferAmount());
            this.getResultSet().updateString("Receiver_Information", this.getReceiverInformation());
            this.getResultSet().updateString("Sender_Information", this.getSenderInformation());
            this.getResultSet().updateString("OTHER_INFORMATION", this.getOtherInformation());
            this.getResultSet().updateString("Reference_Running_No", this.getReferenceRunningNo());
            this.getResultSet().updateString("SPACE", this.getSpace());
            this.getResultSet().updateString("COMPANY_CODE", this.getCompanyCode());
            this.getResultSet().updateString("SEQUENCE_NUMBER", this.getSequenceNumber());
            this.getResultSet().updateString("HOSPITAL_CODE", this.getHospitalCode());
            this.getResultSet().updateString("UPDATE_DATE", this.getUpdateDate());
            this.getResultSet().updateString("Update_Time", this.getUpdateTime());
            this.getResultSet().updateString("USER_ID", this.getUserId());
            this.getResultSet().updateString("BATCH_NO", this.getBatchNo());
            this.getResultSet().updateString("DOCTOR_CODE", this.getDoctorCode());

            // Insert the row
            this.getResultSet().insertRow();
            ret = true;
        } catch (SQLException e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
            ret = false;
        }
        finally {}
        return ret;
    }

    // roll back
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm, String serviceType) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from BANK_TMB_MEDIA_CLEARING WHERE YYYY='" + yyyy + "'"
                + " AND MM = '" + mm + "'"
                + " and HOSPITAL_CODE = '" + hospitalCode + "'"
                + " and SERVICE_TYPE = '" + serviceType + "'"
                + " and (BATCH_NO IS NULL OR BATCH_NO = '')";
        System.out.println("Rollback Bank : "+sql1);
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
}
