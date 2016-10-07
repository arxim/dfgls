package df.bean.db.table;

import df.bean.db.conn.DBConnection;
import java.util.ArrayList;
import java.util.List;

public class IntErpArReceiptOld extends ABSTable {

    private String hospitalCode;
    private String billNo;
    private String receiptNo;
    private String receiptDate;
    private String receiptTypeCode;
    private Double billAmount;
    private Double creditNoteAmount;
    private Double debitNoteAmount;
    private Double paymentAmount;
    private String docType;
    private String isLastReceipt;    
    private String transactionDate;
    private String updateDate;
    private String updateTime;
    private String userId;
    private String isLoaded;


    public IntErpArReceiptOld(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Double getCreditNoteAmount() {
        return creditNoteAmount;
    }

    public void setCreditNoteAmount(Double creditNoteAmount) {
        this.creditNoteAmount = creditNoteAmount;
    }

    public Double getDebitNoteAmount() {
        return debitNoteAmount;
    }

    public void setDebitNoteAmount(Double debitNoteAmount) {
        this.debitNoteAmount = debitNoteAmount;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getIsLoaded() {
        return isLoaded;
    }

    public void setIsLoaded(String isLoaded) {
        this.isLoaded = isLoaded;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getReceiptTypeCode() {
        return receiptTypeCode;
    }

    public void setReceiptTypeCode(String receiptTypeCode) {
        this.receiptTypeCode = receiptTypeCode;
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

    public String getIsLastReceipt() {
        return isLastReceipt;
    }

    public void setIsLastReceipt(String isLastReceipt) {
        this.isLastReceipt = isLastReceipt;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

        
    public int updateReceiptOverMonth(String startDate, String endDate, String hospitalCode, String tableName) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET " + tableName + ".PAY_BY_AR = 'Y' " 
                + ", TRANSACTION_MODULE = 'AR'"
//                + ", " + tableName + ".YYYY = '" + endDate.substring(0, 4) + "'"
//                + ", " + tableName + ".MM = '" + endDate.substring(5, 7) + "'"
                + ", " + tableName + ".RECEIPT_NO = (SELECT DISTINCT TOP 1 E.RECEIPT_NO " +
                                    " FROM INT_ERP_AR_RECEIPT E WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                    " AND E.IS_LAST_RECEIPT = 'Y' " +
                                    " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                    " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                    " AND E.IS_LOADED = 'N') "
                + ", " + tableName + ".RECEIPT_DATE = (SELECT DISTINCT TOP 1 E.RECEIPT_DATE " +
                                    " FROM INT_ERP_AR_RECEIPT E WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                    " AND E.IS_LAST_RECEIPT = 'Y' " +
                                    " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                    " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                    " AND E.IS_LOADED = 'N') "
                + " WHERE " + tableName + ".PAY_BY_AR = 'N'"
                + " AND " + tableName + ".HOSPITAL_CODE='" + hospitalCode + "'"
                + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                + " AND " + tableName + ".INVOICE_DATE NOT BETWEEN '" + startDate + "' AND '" + endDate + "'" 
                + " AND " + tableName + ".INVOICE_NO = (SELECT DISTINCT E.BILL_NO " +
                                                " FROM INT_ERP_AR_RECEIPT E " +
                                                " WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                                " AND E.IS_LAST_RECEIPT = 'Y' " +
                                                " AND (E.TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"')"+
                                                //" AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                                " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " AND E.IS_LOADED = 'N') ";
            rows = this.getDBConnection().executeUpdate(sql);

        } catch (Exception e) {
        	System.out.println("Receipt by AR : "+e);
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_RECEIPT_BY_AR, "Receipt by AR is fail.", e.getMessage() ,sql);
            e.printStackTrace();
        }
        return rows;
    } 
            
    public int updateReceiptInMonth(String startDate, String endDate, String hospitalCode, String tableName) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET " + tableName + ".PAY_BY_CASH_AR = 'Y' " 
                + ", TRANSACTION_MODULE = 'AR'"
//                + ", " + tableName + ".YYYY = '" + endDate.substring(0, 4) + "'"
//                + ", " + tableName + ".MM = '" + endDate.substring(5, 7) + "'"
                + ", " + tableName + ".RECEIPT_NO = (SELECT DISTINCT TOP 1 E.RECEIPT_NO " +
                                    " FROM INT_ERP_AR_RECEIPT E WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                    " AND E.IS_LAST_RECEIPT = 'Y' " +
                                    " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                    " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                    " AND E.IS_LOADED = 'N') "
                + ", " + tableName + ".RECEIPT_DATE = (SELECT DISTINCT TOP 1 E.RECEIPT_DATE " +
                                    " FROM INT_ERP_AR_RECEIPT E WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                    " AND E.IS_LAST_RECEIPT = 'Y' " +
                                    " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                    " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                    " AND E.IS_LOADED = 'N') "
                + " WHERE " + tableName + ".PAY_BY_CASH_AR = 'N'"
                + " AND " + tableName + ".HOSPITAL_CODE='" + hospitalCode + "'"
                + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                + " AND " + tableName + ".INVOICE_DATE BETWEEN '" + startDate + "' AND '" + endDate + "'" 
                //+ " AND " + tableName + ".INVOICE_DATE LIKE '" + Batch.getYyyy().concat(Batch.getMm()) + "%'"
                + " AND " + tableName + ".INVOICE_NO = (SELECT DISTINCT E.BILL_NO " +
                                                " FROM INT_ERP_AR_RECEIPT E " +
                                                " WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                                " AND E.IS_LAST_RECEIPT = 'Y' " +
                                                " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                                " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " AND E.IS_LOADED = 'N') ";
            rows = this.getDBConnection().executeUpdate(sql);

        } catch (Exception e) {
        	System.out.println("Receipt by AR : "+e);
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_RECEIPT_BY_AR, "Receipt by AR is fail.", e.getMessage() ,sql);
            e.printStackTrace();
        }
        return rows;
    } 
    
    public int updateWriteOff(String startDate, String endDate, String hospitalCode, String tableName) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET " + tableName + ".IS_WRITE_OFF = 'Y' " 
                + ", AMOUNT_BEF_WRITE_OFF = AMOUNT_AFT_DISCOUNT"
                + ", WRITE_OFF_BILL_AMT = (SELECT DISTINCT E.BILL_AMOUNT-E.CREDIT_NOTE_AMOUNT+E.DEBIT_NOTE_AMOUNT " +
                                                " FROM INT_ERP_AR_RECEIPT E " +
                                                " WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                                " AND E.IS_LAST_RECEIPT = 'Y' " +
                                                " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                                " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " AND E.DOC_TYPE='" + Status.RECEIPT_TYPE_WRITEOFF + "') " 
//                                                " AND E.IS_LOADED = 'N') "
                + ", WRITE_OFF_RECEIPT_AMT = (SELECT DISTINCT E.PAYMENT_AMOUNT " +
                                                " FROM INT_ERP_AR_RECEIPT E " +
                                                " WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                                " AND E.IS_LAST_RECEIPT = 'Y' " +
                                                " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                                " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " AND E.DOC_TYPE='" + Status.RECEIPT_TYPE_WRITEOFF + "') " 
///                                                " AND E.IS_LOADED = 'N') "
                                                + ", DR_AMT_BEF_WRITE_OFF = DR_AMT"
                                                + ", OLD_DR_AMT_BEF_WRITE_OFF = OLD_DR_AMT"
                                                + ", DR_PREMIUM_BEF_WRITE_OFF = DR_PREMIUM"
                                                + ", HP_AMT_BEF_WRITE_OFF = HP_AMT"
                                                + ", HP_PREMIUM_WRITE_OFF = HP_PREMIUM"                                                
                + " WHERE " + tableName + ".HOSPITAL_CODE='" + hospitalCode + "'"
                + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                + " AND (TRN_DAILY.IS_WRITE_OFF = 'N') "
//                + " AND " + tableName + ".INVOICE_DATE BETWEEN '" + startDate + "' AND '" + endDate + "'" 
                + " AND " + tableName + ".INVOICE_NO = (SELECT DISTINCT E.BILL_NO " +
                                                " FROM INT_ERP_AR_RECEIPT E " +
                                                " WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                                " AND E.IS_LAST_RECEIPT = 'Y' " +
                                                " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                                " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " AND E.DOC_TYPE='" + Status.RECEIPT_TYPE_WRITEOFF + "')";
//                                                " AND E.IS_LOADED = 'N') ";
            rows = this.getDBConnection().executeUpdate(sql);
            System.out.println(sql);
        } catch (Exception e) {
        	System.out.println(sql);
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_RECEIPT_BY_AR, " write off fail.", e.getMessage() ,sql);
            e.printStackTrace();
        }

        try {
            sql = "UPDATE " + tableName + " SET " + tableName + ".IS_WRITE_OFF = 'Y'"              
			// เพิ่มเพื่อ ปรับค่าการคำนวณให้ลดลงตามอัตราส่วน
			+" ,TRN_DAILY.AMOUNT_AFT_DISCOUNT = CASE WHEN TRN_DAILY.WRITE_OFF_BILL_AMT > 0 THEN TRN_DAILY.AMOUNT_AFT_DISCOUNT * (1 - (TRN_DAILY.WRITE_OFF_BILL_AMT - TRN_DAILY.WRITE_OFF_RECEIPT_AMT) / TRN_DAILY.WRITE_OFF_BILL_AMT) ELSE 0 END "
			//+" ,TRN_DAILY.DR_AMT = CASE WHEN TRN_DAILY.WRITE_OFF_BILL_AMT > 0 THEN TRN_DAILY.DR_AMT * (1 - (TRN_DAILY.WRITE_OFF_BILL_AMT - TRN_DAILY.WRITE_OFF_RECEIPT_AMT) / TRN_DAILY.WRITE_OFF_BILL_AMT) ELSE 0 END "
			+" ,TRN_DAILY.DR_AMT = CASE WHEN TRN_DAILY.WRITE_OFF_BILL_AMT > 0 THEN (TRN_DAILY.DR_AMT * (100 - (TRN_DAILY.WRITE_OFF_RECEIPT_AMT * 100 / TRN_DAILY.WRITE_OFF_BILL_AMT))) / 100 ELSE 9 END "
			//+" ,TRN_DAILY.OLD_DR_AMT_BEF_WRITE_OFF = CASE WHEN TRN_DAILY.WRITE_OFF_BILL_AMT > 0 THEN TRN_DAILY.OLD_DR_AMT * (1 - (TRN_DAILY.WRITE_OFF_BILL_AMT - TRN_DAILY.WRITE_OFF_RECEIPT_AMT) / TRN_DAILY.WRITE_OFF_BILL_AMT) ELSE 0 END "
			+" ,TRN_DAILY.DR_PREMIUM = CASE WHEN TRN_DAILY.WRITE_OFF_BILL_AMT > 0 THEN TRN_DAILY.DR_PREMIUM * (1 - (TRN_DAILY.WRITE_OFF_BILL_AMT - TRN_DAILY.WRITE_OFF_RECEIPT_AMT) / TRN_DAILY.WRITE_OFF_BILL_AMT) ELSE 0 END "
			+" ,TRN_DAILY.HP_AMT = CASE WHEN TRN_DAILY.WRITE_OFF_BILL_AMT > 0 THEN TRN_DAILY.HP_AMT * (1 - (TRN_DAILY.WRITE_OFF_BILL_AMT - TRN_DAILY.WRITE_OFF_RECEIPT_AMT) / TRN_DAILY.WRITE_OFF_BILL_AMT) ELSE 0 END "
			+" ,TRN_DAILY.HP_PREMIUM = CASE WHEN TRN_DAILY.WRITE_OFF_BILL_AMT > 0 THEN TRN_DAILY.HP_PREMIUM * (1 - (TRN_DAILY.WRITE_OFF_BILL_AMT - TRN_DAILY.WRITE_OFF_RECEIPT_AMT) / TRN_DAILY.WRITE_OFF_BILL_AMT) ELSE 0 END "                    
			// จบ การปรับค่าการคำนวณให้ลดลงตามอัตราส่วน                                              
                                                
                + " WHERE " + tableName + ".HOSPITAL_CODE='" + hospitalCode + "'"
                + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                + " AND (TRN_DAILY.IS_WRITE_OFF = 'Y') "
                //+ " AND " + tableName + ".INVOICE_DATE BETWEEN '" + startDate + "' AND '" + endDate + "'" 
                + " AND " + tableName + ".INVOICE_NO = (SELECT DISTINCT E.BILL_NO " +
                                                " FROM INT_ERP_AR_RECEIPT E " +
                                                " WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                                " AND E.IS_LAST_RECEIPT = 'Y' " +
                                                " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                                " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " AND E.DOC_TYPE='" + Status.RECEIPT_TYPE_WRITEOFF + "')";
//                                                " AND E.IS_LOADED = 'N') ";
            rows = this.getDBConnection().executeUpdate(sql);
            System.out.println(sql);
        } catch (Exception e) {
        	System.out.println(sql);
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_RECEIPT_BY_AR, " write off fail.", e.getMessage() ,sql);
            e.printStackTrace();
        }

        return rows;
    }
    
    public int updateYyyyMm(String YYYY, String MM, String hospitalCode, String tableName) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET YYYY = '" + YYYY + "'"
                    + " ,MM = '" + MM + "'"
                    + " WHERE (PAY_BY_CASH = 'Y' OR PAY_BY_AR = 'Y' OR PAY_BY_DOCTOR = 'Y' OR PAY_BY_PAYOR = 'Y' OR PAY_BY_CASH_AR = 'Y') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND (YYYY IS NULL OR YYYY = '') ";
//                    + " AND(TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
            rows = this.getDBConnection().executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }    
    public int updateYyyyMm(String YYYY, String MM, String startDate, String endDate, String hospitalCode, String tableName) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET YYYY = '" + YYYY + "'"
                    + " ,MM = '" + MM + "'"
                    + " WHERE (PAY_BY_CASH = 'Y' OR PAY_BY_AR = 'Y' OR PAY_BY_DOCTOR = 'Y' OR PAY_BY_PAYOR = 'Y' OR PAY_BY_CASH_AR = 'Y') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'"
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND (YYYY IS NULL OR YYYY = '') ";
//                    + " AND(TRANSACTION_DATE BETWEEN '" + startDate + "' AND '" + endDate + "')";
            rows = this.getDBConnection().executeUpdate(sql);
        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_RECEIPT_BY_AR, "Update yyyy/mm is fail.", e.getMessage() ,sql);
            e.printStackTrace();
        }
        return rows;
    }    
    public int updateIsLoaded(String startDate, String endDate, String hospitalCode) {
        int rows = -1;
        try {
            rows = this.getDBConnection().executeUpdate("UPDATE INT_ERP_AR_RECEIPT SET IS_LOADED = 'Y' " 
                            + " WHERE (TRANSACTION_DATE >='" + startDate + "' AND TRANSACTION_DATE <='" + endDate + "')"
                            + " AND HOSPITAL_CODE='" + hospitalCode + "'"
                            + " AND IS_LAST_RECEIPT = 'Y'"
                            + " AND IS_LOADED = 'N'" );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }    
    
    
    public boolean rollBackUpdate(String hospitalCode, String YYYY, String MM, String tableName) {
        List sqlCommand = new ArrayList();
        
        String startDate = YYYY + MM + "00", endDate = YYYY + MM + "31";
        boolean ret = false;
        String sql1 = "UPDATE INT_ERP_AR_RECEIPT SET IS_LOADED = 'N' " 
                            + " WHERE (TRANSACTION_DATE >='" + startDate + "' AND TRANSACTION_DATE <='" + endDate + "')"
                            + " AND HOSPITAL_CODE='" + hospitalCode + "'"
                            + " AND IS_LAST_RECEIPT = 'Y'"
                            + " AND IS_LOADED = 'Y'";
        
        String sql2 = "UPDATE " + tableName + " SET PAY_BY_AR = 'N', PAY_BY_CASH_AR = 'N' " +
                    " ,YYYY = ''"
                    + " ,MM = ''"
                    + ", TRANSACTION_MODULE = 'TR'"
                    + ", " + tableName + ".RECEIPT_NO = '' "
                    + ", " + tableName + ".RECEIPT_DATE = '' "                    
                    + " WHERE (PAY_BY_CASH <> 'Y' AND PAY_BY_PAYOR <> 'Y' AND PAY_BY_DOCTOR <> 'Y' AND (PAY_BY_AR = 'Y' OR PAY_BY_CASH_AR = 'Y')) "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND (YYYY = '" + YYYY + "') "
                    + " AND(RECEIPT_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')"
                    + " AND " + tableName + ".INVOICE_NO = (SELECT DISTINCT E.BILL_NO " +
                                                " FROM INT_ERP_AR_RECEIPT E " +
                                                " WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                                " AND E.IS_LAST_RECEIPT = 'Y' " +
                                                " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                                " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " AND E.IS_LOADED = 'N') ";
        /*
        String sql3 = "UPDATE " + tableName + " SET PAY_BY_AR = 'N', PAY_BY_CASH_AR = 'N' " +
                    " ,YYYY = ''"
                    + " ,MM = ''"
                    + ", TRANSACTION_MODULE = 'AR'"
                    + ", " + tableName + ".RECEIPT_NO = '' "
                    + ", " + tableName + ".RECEIPT_DATE = '' "                    
                    + " WHERE (PAY_BY_CASH <> 'Y' AND PAY_BY_PAYOR <> 'Y' AND PAY_BY_DOCTOR <> 'Y' AND (PAY_BY_AR = 'Y' OR PAY_BY_CASH_AR = 'Y')) "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND (YYYY = '" + YYYY + "') "
                    + " AND(TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')"
                    + " AND " + tableName + ".INVOICE_NO = (SELECT DISTINCT E.BILL_NO " +
                                                " FROM INT_ERP_AR_RECEIPT E " +
                                                " WHERE E.BILL_NO = " + tableName + ".INVOICE_NO " +
                                                " AND E.IS_LAST_RECEIPT = 'Y' " +
                                                " AND(E.TRANSACTION_DATE >='" + startDate + "' AND E.TRANSACTION_DATE <='" + endDate + "')" +
                                                " AND E.HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " AND E.IS_LOADED = 'N') ";
        */
        System.out.println(sql1);
        System.out.println(sql2);
        
        sqlCommand.add( sql1 );
        sqlCommand.add( sql2 );
        //sqlCommand.add( sql3 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    
    public boolean rollBackDelete(String hospitalCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql = "DELETE FROM INT_ERP_AR_RECEIPT " 
                    + " WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
        
    public boolean rollBackWriteOffDelete(String hospitalCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql = "UPDATE TRN_DAILY SET AMOUNT_AFT_DISCOUNT = AMOUNT_BEF_WRITE_OFF, "
                      + " DR_AMT = DR_AMT_BEF_WRITE_OFF, "
                      + " OLD_DR_AMT = OLD_DR_AMT_BEF_WRITE_OFF, "
                      + " DR_PREMIUM = DR_PREMIUM_BEF_WRITE_OFF, "
                      + " HP_AMT = HP_AMT_BEF_WRITE_OFF, "
                      + " HP_PREMIUM = HP_PREMIUM_WRITE_OFF, "
                      + " AMOUNT_BEF_WRITE_OFF = 0, DR_AMT_BEF_WRITE_OFF=0, "
                      + " OLD_DR_AMT_BEF_WRITE_OFF=0, DR_PREMIUM_BEF_WRITE_OFF=0, "
                      + " HP_AMT_BEF_WRITE_OFF=0, HP_PREMIUM_WRITE_OFF=0, "
                      + " WRITE_OFF_BILL_AMT=0, WRITE_OFF_RECEIPT_AMT=0, "
                      + " TRN_DAILY.IS_WRITE_OFF = 'N' "

                      + " WHERE TRN_DAILY.HOSPITAL_CODE='"+hospitalCode+"' "
                      + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                      + " AND (TRN_DAILY.IS_WRITE_OFF = 'Y') "
                      + " AND TRN_DAILY.INVOICE_NO = (SELECT DISTINCT E.BILL_NO  FROM INT_ERP_AR_RECEIPT E  "
                          + " WHERE E.BILL_NO = TRN_DAILY.INVOICE_NO  AND E.IS_LAST_RECEIPT = 'Y'  AND (E.TRANSACTION_DATE >='" + startDate + "'"
                          + " AND E.TRANSACTION_DATE <='" + endDate + "') AND E.HOSPITAL_CODE='" + hospitalCode + "' AND E.DOC_TYPE='W') ";
        System.out.println(sql);
        sqlCommand.add( sql );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
    
}



/*
UPDATE TRN_DAILY SET TRN_DAILY.PAY_BY_AR = 'Y'
,YYYY = (SELECT DISTINCT SUBSTRING(E.RECEIPT_DATE, 1,4)
						FROM INT_ERP_AR_RECEIPT E 
						WHERE E.BILL_NO = TRN_DAILY.INVOICE_NO  
						AND E.IS_LAST_RECEIPT = 'Y'
						AND(E.TRANSACTION_DATE >='20080201' AND E.TRANSACTION_DATE <='20080201') 
						AND E.HOSPITAL_CODE='201' AND E.IS_LOADED = 'N')
,MM = (SELECT DISTINCT SUBSTRING(E.RECEIPT_DATE, 1,4)
						FROM INT_ERP_AR_RECEIPT E 
						WHERE E.BILL_NO = TRN_DAILY.INVOICE_NO  
						AND E.IS_LAST_RECEIPT = 'Y'
						AND(E.TRANSACTION_DATE >='20080201' AND E.TRANSACTION_DATE <='20080201') 
						AND E.HOSPITAL_CODE='201' AND E.IS_LOADED = 'N')
,TRN_DAILY.RECEIPT_NO = (SELECT DISTINCT E.RECEIPT_DATE
						FROM INT_ERP_AR_RECEIPT E 
						WHERE E.BILL_NO = TRN_DAILY.INVOICE_NO  
						AND E.IS_LAST_RECEIPT = 'Y'
						AND(E.TRANSACTION_DATE >='20080201' AND E.TRANSACTION_DATE <='20080201') 
						AND E.HOSPITAL_CODE='201' AND E.IS_LOADED = 'N')  
,TRN_DAILY.RECEIPT_DATE = (SELECT DISTINCT E.RECEIPT_DATE
						FROM INT_ERP_AR_RECEIPT E 
						WHERE E.BILL_NO = TRN_DAILY.INVOICE_NO  
						AND E.IS_LAST_RECEIPT = 'Y'
						AND(E.TRANSACTION_DATE >='20080201' AND E.TRANSACTION_DATE <='20080201') 
						AND E.HOSPITAL_CODE='201' AND E.IS_LOADED = 'N')  

WHERE TRN_DAILY.PAY_BY_AR = 'N' 
AND TRN_DAILY.HOSPITAL_CODE='201' 
AND TRN_DAILY.INVOICE_NO = (SELECT DISTINCT E.BILL_NO  
							FROM INT_ERP_AR_RECEIPT E 
							WHERE E.BILL_NO = TRN_DAILY.INVOICE_NO  
							AND E.IS_LAST_RECEIPT = 'Y'
							AND(E.TRANSACTION_DATE >='20080201' AND E.TRANSACTION_DATE <='20080201') 
							AND E.HOSPITAL_CODE='201' AND E.IS_LOADED = 'N') */

// roll back //
/* UPDATE TRN_DAILY SET PAY_BY_AR = 'N', RECEIPT_NO = '', RECEIPT_DATE = '', YYYY='', MM='' WHERE TRANSACTION_DATE = '20080201' and INVOICE_NO = 'BI2080000729' */
/* UPDATE INT_ERP_AR_RECEIPT SET IS_LOADED = 'N' " 
                            + " WHERE (TRANSACTION_DATE >='" + startDate + "' AND TRANSACTION_DATE <='" + endDate + "')"
                            + " AND HOSPITAL_CODE='" + hospitalCode + "'"
                            + " AND IS_LAST_RECEIPT = 'Y'"
                            + " AND IS_LOADED = 'Y'" ); */


