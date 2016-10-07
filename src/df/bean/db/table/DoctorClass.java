package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class DoctorClass  extends ABSTable{

    private String code;
    private String description;
    private String className;
    private String ACTIVE;

    public DoctorClass() {
        super();
    }

    public String getClassName() {
        return this.className;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public DoctorClass(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from DOCTOR_CLASS where CODE='" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.description = this.getResultSet().getString("Description");
                this.className = this.getResultSet().getString("Class_Name");
            }
            this.getResultSet().close();
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

    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }
    
}
