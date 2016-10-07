package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class AdmissionType extends ABSTable {

    private String code;
    private String description;
    static public final String TYPE_IPD = "I";
    static public final String TYPE_OPD = "O";

    public AdmissionType() {
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


    public AdmissionType(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from ADMISSION_TYPE where CODE='" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("CODE");
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
