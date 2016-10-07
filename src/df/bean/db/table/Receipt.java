package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class Receipt extends ABSTable {

    private String hospitalCode;
    private String receiptNo;
    private String invoiceNo;
    private String receiptDate;
    private String cashierLocationCode;
    private Double amountBefDiscount;
    private Double amountOfDiscount;
    private Double withHoldingTax;
    private String receiptFrom;
    private String receiptTypeCode;
    private String paymentModule;
    private String updateDate;
    private String updateTime;
    private String userId;
    private String lineNo;
    private String ACTIVE;
    private double sumAmountBefDiscount = 0d, sumAmountOfDiscount = 0d, sumWithHoldingTax = 0d;

    public Receipt() {
        super();
    }
    public Receipt(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }
    public Double getAmountBefDiscount() {
        return this.amountBefDiscount;
    }

    public Double getAmountOfDiscount() {
        return this.amountOfDiscount;
    }

    public String getCashierLocationCode() {
        return this.cashierLocationCode;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public String getReceiptDate() {
        return this.receiptDate;
    }

    public String getReceiptFrom() {
        return this.receiptFrom;
    }

    public String getReceiptNo() {
        return this.receiptNo;
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

    public Double getWithHoldingTax() {
        return this.withHoldingTax;
    }

    public void setAmountBefDiscount(Double amountBefDiscount) {
        this.amountBefDiscount = amountBefDiscount;
    }

    public void setAmountOfDiscount(Double amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

    public void setCashierLocationCode(String cashierLocationCode) {
        this.cashierLocationCode = cashierLocationCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public void setReceiptFrom(String receiptFrom) {
        this.receiptFrom = receiptFrom;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
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

    public void setWithHoldingTax(Double withHoldingTax) {
        this.withHoldingTax = withHoldingTax;
    }

    public double getSumAmountBefDiscount() {
        return sumAmountBefDiscount;
    }

    public void setSumAmountBefDiscount(double sumAmountBefDiscount) {
        this.sumAmountBefDiscount = sumAmountBefDiscount;
    }

    public double getSumAmountOfDiscount() {
        return sumAmountOfDiscount;
    }

    public void setSumAmountOfDiscount(double sumAmountOfDiscount) {
        this.sumAmountOfDiscount = sumAmountOfDiscount;
    }

    public double getSumWithHoldingTax() {
        return sumWithHoldingTax;
    }

    public void setSumWithHoldingTax(double sumWithHoldingTax) {
        this.sumWithHoldingTax = sumWithHoldingTax;
    }
    
    public Receipt(String hospitalCode, String invoiceNo, String receiptTypeCode, DBConnection conn) {
        this.setDBConnection(conn);
        ResultSet rs = this.getDBConnection().executeQuery("select * from Receipt where HOSPITAL_CODE='" + hospitalCode + "'" +
                                                    " and INVOICE_NO = '" + invoiceNo + "'" + 
                                                    " and RECEIPT_TYPE_CODE='" + receiptTypeCode + "'");

        try {
            while (rs.next()) {
                this.hospitalCode = rs.getString("HOSPITAL_CODE");
                this.receiptNo = rs.getString("RECEIPT_NO");
                this.invoiceNo = rs.getString("INVOICE_NO");
                this.receiptDate = rs.getString("RECEIPT_DATE");
                this.cashierLocationCode = rs.getString("Cashier_Location_Code");
                this.amountBefDiscount = rs.getDouble("AMOUNT_BEF_DISCOUNT");
                this.amountOfDiscount = rs.getDouble("AMOUNT_OF_DISCOUNT");
                this.withHoldingTax = rs.getDouble("With_Holding_Tax");
                this.setReceiptTypeCode(rs.getString("Receipt_Type_Code"));
                this.setReceiptFrom(rs.getString("Receipt_From"));
                this.setPaymentModule(rs.getString("Payment_Module"));
/*                this.updateDate = rs.getString("UPDATE_DATE");
                this.updateTime = rs.getString("Update_Time");
                this.userId = rs.getString("USER_ID"); 
                this.ACTIVE = rs.getString("ACTIVE"); */
                
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        } finally {
               //Clean up resources, close the connection.
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        getSummaryReceipt(this.getHospitalCode(), this.getInvoiceNo(), this.getDBConnection());
    }
    
    public Receipt(String hospitalCode, String invoiceNo, String lineNo, String receiptTypeCode, DBConnection conn) {
        this.setDBConnection(conn);
        ResultSet rs = this.getDBConnection().executeQuery("select * from Receipt where HOSPITAL_CODE='" + hospitalCode + "'" +
                                                    " and INVOICE_NO = '" + invoiceNo + "'" + 
                                                    " and line_No = '" + lineNo + "'"); 
                                                    //" and RECEIPT_TYPE_CODE='" + receiptTypeCode + "'");

        try {
            while (rs.next()) {
                this.hospitalCode = rs.getString("HOSPITAL_CODE");
                this.receiptNo = rs.getString("RECEIPT_NO");
                this.invoiceNo = rs.getString("INVOICE_NO");
                this.receiptDate = rs.getString("RECEIPT_DATE");
                this.cashierLocationCode = rs.getString("Cashier_Location_Code");
                this.amountBefDiscount = rs.getDouble("AMOUNT_BEF_DISCOUNT");
                this.amountOfDiscount = rs.getDouble("AMOUNT_OF_DISCOUNT");
                this.withHoldingTax = rs.getDouble("With_Holding_Tax");
                this.setReceiptTypeCode(rs.getString("Receipt_Type_Code"));
                this.setReceiptFrom(rs.getString("Receipt_From"));
                this.setPaymentModule(rs.getString("Payment_Module"));
                this.setLineNo(rs.getString("line_No"));
    /*                this.updateDate = rs.getString("UPDATE_DATE");
                this.updateTime = rs.getString("Update_Time");
                this.userId = rs.getString("USER_ID"); 
                this.ACTIVE = rs.getString("ACTIVE"); */
                
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        } finally {
               //Clean up resources, close the connection.
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        getSummaryReceipt(this.getHospitalCode(), this.getInvoiceNo(), this.getDBConnection());
    }
    public String getPaymentModule() {
        if (this.paymentModule == null) {
            this.paymentModule = "";
        }
        return paymentModule;
    }

    public void setPaymentModule(String paymentModule) {
        this.paymentModule = paymentModule;
    }
    
    public void getSummaryReceipt(String hospitalCode, String invoiceNo, DBConnection conn) {
        this.setDBConnection(conn);
        String sql = "";
        sql = "select sum(AMOUNT_BEF_DISCOUNT) as sAmount_Bef_Discount, ";
        sql = sql + " sum(AMOUNT_OF_DISCOUNT) as sAmount_of_Discount, ";
        sql = sql + " sum(With_Holding_Tax) as sWith_Holding_Tax, ";
        sql = sql + " HOSPITAL_CODE, INVOICE_NO ";
        sql = sql + " from receipt ";
        sql = sql + " where HOSPITAL_CODE='" + hospitalCode + "'";
        sql = sql + " and INVOICE_NO = '" + invoiceNo + "'";
        sql = sql + " and ACTIVE='1'";
        sql = sql + " group by HOSPITAL_CODE, INVOICE_NO";
        
        ResultSet rs = this.getDBConnection().executeQuery(sql);
        try {
            while (rs.next()) {
                this.hospitalCode = rs.getString("HOSPITAL_CODE");
                this.invoiceNo = rs.getString("INVOICE_NO");
                this.setSumAmountBefDiscount(rs.getDouble("sAmount_Bef_Discount"));
                this.setSumAmountOfDiscount(rs.getDouble("sAmount_of_Discount"));
                this.setSumWithHoldingTax(rs.getDouble("sWith_Holding_Tax"));
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        } finally {
               //Clean up resources, close the connection.
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
    }

    public String getReceiptTypeCode() {
        return receiptTypeCode;
    }

    public void setReceiptTypeCode(String receiptTypeCode) {
        this.receiptTypeCode = receiptTypeCode;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }
    
    /*
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from receipt ";
                sql1 = sql1 + " where RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    } */
    
}
