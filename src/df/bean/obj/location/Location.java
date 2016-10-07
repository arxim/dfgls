package df.bean.obj.location;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Department;
import df.bean.db.table.Hospital;

public class Location {
    df.bean.db.table.Location location;
    Department department;
    Hospital hospital;
    
    private String locationCode;
    private String hospitalCode;
    public DBConnection conn = null;
    
    public Location(String locationCode, String hospitalCode, DBConnection conn) {
        this.locationCode = locationCode;
        this.hospitalCode = hospitalCode;
        
        this.conn = conn;
        this.newLocation();
//        this.newdepartment();
//        this.newHospital();
    }
     public void setDBConnection(DBConnection conn) {
         this.conn = conn;
     }
     public DBConnection getDBConnection() {
         return this.conn;
     }
    public df.bean.db.table.Location getLocation() {
        return location;
    }

    public void setLocation(df.bean.db.table.Location location) {
        this.location = location;
    }

    public Department getdepartment() {
        return department;
    }

    public void setdepartment(Department department) {
        this.department = department;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    // new location    
    public void newLocation() {
        this.setLocation(new df.bean.db.table.Location(this.locationCode, this.hospitalCode, this.getDBConnection()));
    }
    // new department group
    public void newdepartment() {
        String Code = getDBConnection().executeQueryString("select Department_Code from LOCATION where CODE='" + 
                        this.locationCode + "' and HOSPITAL_CODE = '" + this.hospitalCode + "'");    
        this.setdepartment(new Department(Code, this.hospitalCode, this.getDBConnection()));
    }
    // new hospital
    public void newHospital() {
        this.hospital = new Hospital(this.hospitalCode, this.getDBConnection());
    }
}
