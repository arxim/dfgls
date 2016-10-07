package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.DialogBox;

public class BankPaymentMonthly402 extends ABSTable{

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
    private String sendRef;
    private String vendorID;
    private String dateOfOrder;
    private String informationOrder;
    private String otherInformation;
    private String referenceRunningNo;
    private String transType;
    private String filler;
    private String userId;
    private String updateDate;
    private String updateTime;
    private String batchNo;
    private String hospitalCode;
    private String yyyy;
    private String mm;
    
    public final static String FILE_TYPE = "10";
    public final static String RECORD_TYPE = "2";
    public final static String BATCH_NUMBER = "100001";
    public final static String CLEARING_HOUSE_CODE = "00";
    public final static String SERVICE_TYPE_PAYROLL = "01";
    public final static String SERVICE_TYPE_PAYMENT = "04";
    public final static String SEND_REF = "7777777777";
    public final static String DATE_OF_ORDER = "";
    public final static String INFORMATION_ORDER = "";
    public final static String OTHER_INFORMATION = "";
    public final static String TRANS_TYPE_GI = "GI";
    public final static String TRANS_TYPE_IT = "IT";
    public final static String TRANS_TYPE_LW = "LW";
    public final static String FILTER = "";
    
    public BankPaymentMonthly402() {
        super();
    }
    public BankPaymentMonthly402(DBConnection conn) {
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

    public String getDateOfOrder() {
        return this.dateOfOrder;
    }

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public String getFileType() {
        return this.fileType;
    }

    public String getFiller() {
        return this.filler;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getInformationOrder() {
        return this.informationOrder;
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
        return this.receivingAccountNo;
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

    public String getSendRef() {
        return this.sendRef;
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

    public String getTransType() {
        return this.transType;
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

    public void setDateOfOrder(String dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setInformationOrder(String informationOrder) {
        this.informationOrder = informationOrder;
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

    public void setSendRef(String sendRef) {
        this.sendRef = sendRef;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
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

    public void setTransType(String transType) {
        this.transType = transType;
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

    public boolean insert() {
        boolean ret = false;
        ResultSet rs = null;
        Statement stmt = null;
        try {
             // Create an updatable result set
             String tableName = "BANK_PAYMENT_MONTHLY_402";
             String[] ss = this.getDBConnection().getColumnNames(tableName);
             String s1 = this.getDBConnection().getColumnNamesLine(ss);
             stmt = this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             rs = stmt.executeQuery("SELECT " + s1 + " FROM " + tableName + " where Hospital_code=0");
             // Move cursor to the "insert row"
             rs.moveToInsertRow();
         
             // Set values for the new row.
             rs.updateString("yyyy", this.getYyyy());
             rs.updateString("MM", this.getMm());
             rs.updateString("File_Type", this.getFileType());
             rs.updateString("Record_Type", this.getRecordType());
             rs.updateString("Batch_Number", this.getBatchNumber());
             rs.updateString("Receiving_Bank_Code", this.getReceivingBankCode());
             rs.updateString("Receiving_Branch_Code", this.getReceivingBranchCode());
             rs.updateString("Receiving_Account_No", this.getReceivingAccountNo());
             rs.updateString("Sending_Bank_Code", this.getSendingBankCode());
             rs.updateString("Sending_Branch_Code", this.getSendingBranchCode());
             rs.updateString("Sending_Account_No", this.getSendingAccountNo());
             rs.updateString("Effective_Date", this.getEffectiveDate());
             rs.updateString("Service_Type", this.getServiceType());
             rs.updateString("Clearing_House_Code", this.getClearingHouseCode());
             rs.updateString("Transfer_Amount", this.getTransferAmount());
             rs.updateString("Receiver_Information", this.getReceiverInformation());
             rs.updateString("Sender_Information", this.getSenderInformation());
             rs.updateString("Send_Ref", this.getSendRef());
             rs.updateString("Vendor_ID", this.getVendorID());
             rs.updateString("Date_Of_Order", this.getDateOfOrder());
             rs.updateString("INFORMATION_ORDER", this.getInformationOrder());
             rs.updateString("Reference_Running_No", this.getReferenceRunningNo());
             rs.updateString("Trans_Type", this.getTransType());
             rs.updateString("Filler", this.getFiller());
             rs.updateString("OTHER_INFORMATION", this.getOtherInformation());

             rs.updateString("HOSPITAL_CODE", this.getHospitalCode());
             rs.updateString("UPDATE_DATE", this.getUpdateDate());
             rs.updateString("Update_Time", this.getUpdateTime());
             rs.updateString("USER_ID", this.getUserId());
             rs.updateString("BATCH_NO", this.getBatchNo());
             
             // Insert the row
             rs.insertRow();
             ret = true;
         } catch (SQLException e) {
             e.printStackTrace();
             TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
             ret = false;
         }
        finally {
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
                        System.out.println("A SQLException error has occured in BankPaymentMonthly402.insert() \n" + ex.getMessage());
                        ex.printStackTrace();
                        ret = false;
                      }
                    }
         return ret;
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
        
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from bank_payment_monthly_402 WHERE YYYY='" + yyyy + "'";
                sql1 = sql1 + " AND MM = '" + mm + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
        
}
