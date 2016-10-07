package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class PaymentMode extends ABSTable {
    static public final String PAYMENT_CASH = "C";
    static public final String PAYMENT_CHEQUE = "CQ";
    static public final String PAYMENT_PAYROLL = "PR";
    static public final String PAYMENT_UNPAID = "U";
    static public final String PAYMENT_BANK = "B";
    
    private String code;
    private String description;

    public PaymentMode() {
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

    public PaymentMode(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from PAYMENT_MODE where CODE='" + code + "'"));

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
