package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class ResidentType extends ABSTable {

    private String code;
    protected String description;
    private String ACTIVE;
    private String HOSPITAL_CODE;

    public ResidentType() {
        super();
    }
    
    public String getHospitalCode() {
        return HOSPITAL_CODE;
    }

    public void setHospitalCode(String hospital_code) {
        this.HOSPITAL_CODE = hospital_code;
    }
    
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public ResidentType(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("SELECT * FROM RESIDENT_TYPE WHERE CODE='" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("CODE");
                this.setDescription(this.getResultSet().getString("DESCRIPTION"));
                this.setActive(this.getResultSet().getString("ACTIVE"));
                this.setHospitalCode(this.getResultSet().getString("HOSPITAL_CODE"));
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
