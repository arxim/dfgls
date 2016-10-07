package df.bean.obj.location;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Hospital;

public class Department {
    df.bean.db.table.Department department;

    Hospital hospital;
    private String hospitalCode;
    private String departmentCode;
    public DBConnection conn = null;
    
    public Department(String hospitalCode, String departmentCode, DBConnection conn) {
        this.setHospitalCode(hospitalCode);
        this.setDepartmentCode(departmentCode);
        
        this.conn = conn;
        this.newDepartment();
//        this.newHospital();
    }
     public void setDBConnection(DBConnection conn) {
         this.conn = conn;
     }
     public DBConnection getDBConnection() {
         return this.conn;
     }
    public void newHospital() {
       setHospital(new Hospital(this.getHospitalCode(), this.getDBConnection()));    
    }
    public void newDepartment() {
        setDepartment(new df.bean.db.table.Department(departmentCode, this.hospitalCode, this.getDBConnection()));  
    }

    public df.bean.db.table.Department getDepartment() {
        return department;
    }

    public void setDepartment(df.bean.db.table.Department department) {
        this.department = department;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
}
