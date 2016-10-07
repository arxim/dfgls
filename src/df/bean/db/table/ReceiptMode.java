package df.bean.db.table;

import java.sql.SQLException;

import df.bean.db.conn.DBConnection;

public class ReceiptMode extends ABSTable {

    private String code;
    private String description;
    private String type;
    private String ACTIVE;
    static public String TYPE_CASH = "CASH";
    static public String TYPE_CREDIT = "CREDIT";
    static public String MODE_AR = "AR";
    static public String MODE_CREDIT_CARD = "CC";
    static public String MODE_CHEQUE = "CQ";
    static public String MODE_CASH = "CS";
    static public String MODE_DIRECT_PAYMENT = "DP";
    public ReceiptMode() {
        super();
    }

    public String getCode() {
        if (this.code == null) {
            this.code = "";
        }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public ReceiptMode(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from RECEIPT_MODE where CODE='" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.description = this.getResultSet().getString("Description");
                this.setType(this.getResultSet().getString("Type"));
                this.setActive(this.getResultSet().getString("ACTIVE"));
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

    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

}
