package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import df.bean.db.conn.DBConnection;

public class DoctorTreatment  extends ABSTable{

    private String code;
    private String description;
    static public final String TYPE_ORDER="O";
    static public final String TYPE_RESULT="R";
    static public final String TYPE_CURE="C";

    public DoctorTreatment() {
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

    public DoctorTreatment(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from DOCTOR_TREATMENT where CODE='" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.description = this.getResultSet().getString("Description");
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        finally {
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
