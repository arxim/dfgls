package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;

public class TrnHeader extends ABSTable {

    private String invoiceNo;
    private String invoiceDate;
    private String titleName;
    private String firstName;
    private String lastName;
    private String hospitalNo;
    private String visitNo;
    private String coPayment;
    private String cashierLocationCode;
    private Double amountBefDiscount;
    private Double amountOfDiscount;
    private Double percentOfDiscount;
    private Double amountAftDiscount;
    private String hospitalCode;
    private String admissionTypeCode;
    private String locationCode;
    private String payorOfficeCode;
    private String payorName;
    private String transactionDate;
    private String transactionTime;
    private String userId;
    private String updateDate;
    private String updateTime;
    private String ACTIVE;
    private String statusModify;
    private String batchNo;
    private Integer invIsVoid;
    private String receiptNo;
    private String receiptDate;
    private String receiptModule;
    private String receiptType;
    private Integer recIsVoid;
    private Double recAmountBefDiscount;
    private Double recAmountOfDiscount;
    private String invoiceType;
    
    private String tableName = "TRN_DAILY";
    private Statement stmt = null;
    private ResultSet rs = null;
    
    public TrnHeader() {
        super();
    }
    public TrnHeader(DBConnection conn) {
        super();
        this.setDBConnection(conn);
        this.setStmt(this.getDBConnection().getStatementForInsert());
    }
    
    public String getActive() {
        return this.ACTIVE;
    }

    public String getAdmissionTypeCode() {
        return this.admissionTypeCode;
    }

    public Double getAmountAftDiscount() {
        return this.amountAftDiscount;
    }

    public Double getAmountBefDiscount() {
        return this.amountBefDiscount;
    }

    public Double getAmountOfDiscount() {
        return this.amountOfDiscount;
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public String getCashierLocationCode() {
        return this.cashierLocationCode;
    }

    public String getCoPayment() {
        return this.coPayment;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getHospitalNo() {
        return this.hospitalNo;
    }

    public Integer getInvIsVoid() {
        return this.invIsVoid;
    }

    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public String getInvoiceType() {
        return this.invoiceType;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getLocationCode() {
        return this.locationCode;
    }

    public String getPayorName() {
        return this.payorName;
    }

    public String getPayorOfficeCode() {
        return this.payorOfficeCode;
    }

    public Double getPercentOfDiscount() {
        return this.percentOfDiscount;
    }

    public Double getRecAmountBefDiscount() {
        return this.recAmountBefDiscount;
    }

    public Double getRecAmountOfDiscount() {
        return this.recAmountOfDiscount;
    }

    public Integer getRecIsVoid() {
        return this.recIsVoid;
    }

    public String getReceiptDate() {
        return this.receiptDate;
    }

    public String getReceiptModule() {
        return this.receiptModule;
    }

    public String getReceiptNo() {
        return this.receiptNo;
    }

    public String getReceiptType() {
        return this.receiptType;
    }

    public String getStatusModify() {
        return this.statusModify;
    }

    public String getTitleName() {
        return this.titleName;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public String getTransactionTime() {
        return this.transactionTime;
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

    public String getVisitNo() {
        return this.visitNo;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public void setAdmissionTypeCode(String admissionTypeCode) {
        this.admissionTypeCode = admissionTypeCode;
    }

    public void setAmountAftDiscount(Double amountAftDiscount) {
        this.amountAftDiscount = amountAftDiscount;
    }

    public void setAmountBefDiscount(Double amountBefDiscount) {
        this.amountBefDiscount = amountBefDiscount;
    }

    public void setAmountOfDiscount(Double amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setCashierLocationCode(String cashierLocationCode) {
        this.cashierLocationCode = cashierLocationCode;
    }

    public void setCoPayment(String coPayment) {
        this.coPayment = coPayment;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setHospitalNo(String hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    public void setInvIsVoid(Integer invIsVoid) {
        this.invIsVoid = invIsVoid;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public void setPayorName(String payorName) {
        this.payorName = payorName;
    }

    public void setPayorOfficeCode(String payorOfficeCode) {
        this.payorOfficeCode = payorOfficeCode;
    }

    public void setPercentOfDiscount(Double percentOfDiscount) {
        this.percentOfDiscount = percentOfDiscount;
    }

    public void setRecAmountBefDiscount(Double recAmountBefDiscount) {
        this.recAmountBefDiscount = recAmountBefDiscount;
    }

    public void setRecAmountOfDiscount(Double recAmountOfDiscount) {
        this.recAmountOfDiscount = recAmountOfDiscount;
    }

    public void setRecIsVoid(Integer recIsVoid) {
        this.recIsVoid = recIsVoid;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public void setReceiptModule(String receiptModule) {
        this.receiptModule = receiptModule;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public void setStatusModify(String statusModify) {
        this.statusModify = statusModify;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
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

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }
    
    // update by receipt date
    public boolean rollBackUpdateByReceiptDate(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;        
        String sql1 = "update TRANSACTION_HEADER set RECEIPT_NO = '9999', RECEIPT_DATE = '99999999', RECEIPT_TYPE = ''";
                sql1 = sql1 + ", REC_IS_VOID = '0', REC_AMOUNT_BEF_DISCOUNT = 0";
                sql1 = sql1 + ", REC_AMOUNT_OF_DISCOUNT = 0, UPDATE_DATE = '', UPDATE_TIME = '', RECEIPT_MODULE = 'CASH'";
                sql1 = sql1 + " where RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and RECEIPT_MODULE = 'AR'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
                sql1 = sql1 + " and INVOICE_DATE < '20071100'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    
    
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from TRANSACTION_HEADER ";
                sql1 = sql1 + " where INVOICE_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
                sql1 = sql1 + " and INVOICE_DATE > '20071100'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    
    public boolean rollBackDeleteByReceiptDate(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from TRANSACTION_HEADER ";
                sql1 = sql1 + " where TRANSACTION_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
//                sql1 = sql1 + " where (RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
//                sql1 = sql1 + " or INVOICE_DATE = '" + yyyy.concat(mm).concat(dd) + "')";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
                sql1 = sql1 + " and INVOICE_DATE > '20071100'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    public boolean rollBackUpdateCompute(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update TRANSACTION_HEADER set STATUS_MODIFY = 'R'";
                sql1 = sql1 + " where TRANSACTION_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                // ������ФԴ��ҹ�Ҩ�¡��ԡ����ѹ TRANSACTION_DATE
//                sql1 = sql1 + " where RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
//                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
                sql1 = sql1 + " and INVOICE_DATE > '20071100'"; 
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }

    public int updateAdmissionTypeI(String computeDate) {
        int ret = -1;
        String sql1 = "update TRANSACTION_HEADER set ADMISSION_TYPE_CODE = 'I'";
                sql1 = sql1 + " where TRANSACTION_DATE = '" + computeDate + "'";
                sql1 = sql1 + " and substr(INVOICE_NO,2,1) = 'I'";
                sql1 = sql1 + " and INVOICE_DATE > '20071100'"; 
        ret = conn.executeUpdate(sql1);
        return ret;
    }
    public int updateAdmissionTypeO(String computeDate) {
        int ret = -1;
        String sql1 = "update TRANSACTION_HEADER set ADMISSION_TYPE_CODE = 'O'";
                sql1 = sql1 + " where TRANSACTION_DATE = '" + computeDate + "'";
                sql1 = sql1 + " and substr(INVOICE_NO,2,1) <> 'I'";
                sql1 = sql1 + " and INVOICE_DATE > '20071100'"; 
        ret = conn.executeUpdate(sql1);
        return ret;
    }
    // update Coupon
    public int updateReceiptTypeCoupon(String computeDate) {
        int ret = -1;
        String sql1 = "update TRANSACTION_HEADER set RECEIPT_NO = INVOICE_NO, RECEIPT_DATE = INVOICE_DATE";
                sql1 = sql1 + " where TRANSACTION_DATE = '" + computeDate + "'";
                sql1 = sql1 + " and RECEIPT_NO = '9999'";
                sql1 = sql1 + " and INVOICE_NO like 'R%'";
                sql1 = sql1 + " and INVOICE_DATE > '20071100'";
        ret = conn.executeUpdate(sql1);
        return ret;
    }
}
