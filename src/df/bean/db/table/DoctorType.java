package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class DoctorType  extends ABSTable {

    private String code;
    private String description;
    private String accountno;
    private String hospitalCode;
    private String doctorClassCode;

    public DoctorType() {
        super();
    }

    public String getAccountno() {
        return this.accountno;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDoctorClassCode() {
        return this.doctorClassCode;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDoctorClassCode(String doctorClassCode) {
        this.doctorClassCode = doctorClassCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }
    
    public DoctorType(String code, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from DOCTOR_TYPE where CODE='" + code + "'" +
                                    " and HOSPITAL_CODE = '" + hospitalCode + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.description = this.getResultSet().getString("Description");
                this.accountno = this.getResultSet().getString("AccountNo");
                this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
                this.doctorClassCode = this.getResultSet().getString("DOCTOR_CLASS_CODE");
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
    