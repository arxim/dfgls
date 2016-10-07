package df.bean.obj.receipt;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.ReceiptMode;

public class ReceiptType {
    ReceiptMode receiptMode;
    df.bean.db.table.ReceiptType receiptType;
    
    private String hospitalCode;
    private String receiptTypeCode;
    public DBConnection conn = null;
    
    public ReceiptType(String hospitalCode, String receiptTypeCode, DBConnection conn) {
        this.hospitalCode = hospitalCode;
        this.receiptTypeCode = receiptTypeCode;
        
        this.setDBConnection(conn);
        this.newReceiptType();
        this.newReceiptMode();
    }
    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    public DBConnection getDBConnection() {
        return this.conn;
    }
    public void newReceiptType() {
        this.setReceiptType(new df.bean.db.table.ReceiptType(this.hospitalCode, this.receiptTypeCode, this.getDBConnection()));
    }
    public void newReceiptMode() {
        String code = this.getDBConnection().executeQueryString("select RECEIPT_MODE_CODE from RECEIPT_TYPE where code='" + this.receiptTypeCode + "'" +
                        " and HOSPITAL_CODE = '" + this.hospitalCode + "'");        
        if (code != null) this.setReceiptMode(new ReceiptMode(code, this.getDBConnection()));
    }

    public ReceiptMode getReceiptMode() {
        return receiptMode;
    }

    public void setReceiptMode(ReceiptMode receiptMode) {
        this.receiptMode = receiptMode;
    }

    public df.bean.db.table.ReceiptType getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(df.bean.db.table.ReceiptType receiptType) {
        this.receiptType = receiptType;
    }
}
