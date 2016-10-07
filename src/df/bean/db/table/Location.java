package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class Location extends ABSTable {

    private String code;
    private String description;
    private String building;
    private String floor;
    private String tel;
    private String locationTypeCode;
    private String fromDate;
    private String toDate;
    private String hospitalCode;
    private String departmentCode;
    private String ACTIVE;

    public Location() {
        super();
    }

    public String getBuilding() {
        return this.building;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFloor() {
        return this.floor;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getTel() {
        return this.tel;
    }

    public String getToDate() {
        return this.toDate;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getLocationTypeCode() {
        return locationTypeCode;
    }

    public void setLocationTypeCode(String locationTypeCode) {
        this.locationTypeCode = locationTypeCode;
    }
    
    public Location(String code, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from LOCATION where CODE='" + code + 
                        "' and HOSPITAL_CODE ='" + hospitalCode + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.description = this.getResultSet().getString("Description");
                this.building = this.getResultSet().getString(("Building"));
                this.floor = this.getResultSet().getString("Floor");
                this.tel = this.getResultSet().getString("Tel");
                this.setLocationTypeCode(this.getResultSet().getString("Location_Type_Code"));
                this.fromDate = this.getResultSet().getString("From_Date");
                this.toDate = this.getResultSet().getString("To_Date");
                this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
                this.departmentCode = this.getResultSet().getString("Department_Code");
                this.ACTIVE = this.getResultSet().getString("ACTIVE");
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

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }


}
