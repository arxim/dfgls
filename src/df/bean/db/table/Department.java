package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class Department  extends ABSTable{

    private String code;
    private String description;
    private String hospitalCode;
    private String ACTIVE;

    public Department() {
        super();
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }
    
    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }
    
    public Department(String code, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from DEPARTMENT where CODE='" + code + "'" +
                                                " and HOSPITAL_CODE='" + hospitalCode + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.description = this.getResultSet().getString("Description");
                this.ACTIVE = this.getResultSet().getString("ACTIVE");
                this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
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
