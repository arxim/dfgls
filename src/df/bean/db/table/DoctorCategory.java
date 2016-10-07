package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class DoctorCategory  extends ABSTable{

    private String code = "";
    private String description = "";
    private String hospitalCode = "";
    private String ACTIVE = "";
    private String doctorClassCode = "";

    public DoctorCategory() {
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
    
    public DoctorCategory(String code, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from DOCTOR_CATEGORY where CODE='" + code + "' and HOSPITAL_CODE='" + hospitalCode + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.setDescription(this.getResultSet().getString("Description"));
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

    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getDoctorClassCode() {
        return doctorClassCode;
    }

    public void setDoctorClassCode(String doctorClassCode) {
        this.doctorClassCode = doctorClassCode;
    }

}
