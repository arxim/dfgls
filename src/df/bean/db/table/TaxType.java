package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class TaxType extends ABSTable {

    private String code;
    private String description;
    static public final String  TAX_TYPE_400 = "400";
    static public final String  TAX_TYPE_401 = "401";
    static public final String  TAX_TYPE_402 = "402";
    static public final String  TAX_TYPE_406 = "406";

    public TaxType() {
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

    public TaxType(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from TAX_TYPE where CODE='" + code + "'"));

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
