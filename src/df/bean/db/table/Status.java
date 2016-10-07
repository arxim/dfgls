package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class Status extends ABSTable {
    static public String STATUS_CALCULATED = "C";
    static public String STATUS_PAID = "P";
    static public String STATUS_IMPORT = "I";
    static public String STATUS_MANUAL = "M";
    static public String STATUS_EDIT = "E";
    static public String STATUS_ROLLBACK = "R";
    
    static public String RECEIPT_TYPE_WRITEOFF = "W";
    static public String RECEIPT_TYPE_RECEIPT = "R";
    static public String INVOICE_TYPE_IS_INVOICE = "V";
    static public String INVOICE_TYPE_IS_ACCU = "A";
    static public String INVOICE_TYPE_IS_RESULT = "R";
    
    static public String INVOICE_IS_VOID = "-1";
    static public String INVOICE_IS_NOT_VOID = "1";
    static public String RECEIPT_IS_VOID = "-1";
    static public String RECEIPT_IS_NOT_VOID = "1";
    
    static public String RECEIPT_NO_DEFAULT = "9999";
    static public String RECEIPT_DATE_DEFAULT = "99999999";
    
    static public String DOCTOR_CODE_UNREAD = "*";
    static public String TRANSACTION_TYPE_RECEIPT = "REV";
    static public String TRANSACTION_TYPE_INVOICE = "INV";
    
    
    
    private String code;
    private String description;

    public Status() {
        super();
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from INVOICESTATUS where CODE='" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.description = this.getResultSet().getString("Description");
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        } finally {
               //Clean up resources, close the connection.
               if(this.getResultSet() != null) {
                  try {
                     this.getResultSet().close();
                     this.setResultSet(null);
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
    }
}
