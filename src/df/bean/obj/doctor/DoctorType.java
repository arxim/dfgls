package df.bean.obj.doctor;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.DoctorClass;
import df.bean.db.table.Hospital;

public class DoctorType {
    df.bean.db.table.DoctorType doctorType = null;
    Hospital hospital = null;
    DoctorClass doctorClass = null;
    
    protected String doctorTypeCode = "";
    protected String hospitalCode = "";
    DBConnection conn = null;
    
    public DoctorType(String code, String hospitalCode, DBConnection conn) {
        this.doctorTypeCode = code;
        this.hospitalCode = hospitalCode;
        
        this.conn = conn;
        this.newDoctorType();
        this.newHospital();
        this.newDoctorClass();

    }
    @Override
    protected void finalize() {
        doctorType = null;
        hospital = null;
        doctorClass = null;
    }
    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    public DBConnection getDBConnection() {
        return this.conn;
    }
    public void newDoctorType() {
        String Code = getDBConnection().executeQueryString("select CODE FROM DOCTOR_TYPE where CODE='" + doctorTypeCode + "'");
        setDoctorType(new df.bean.db.table.DoctorType(Code, this.hospitalCode, this.getDBConnection()));    
    }
    
    public void newHospital() {
        String Code = getDBConnection().executeQueryString("select HOSPITAL_CODE from DOCTOR_TYPE where CODE='" + doctorTypeCode + "'");
        setHospital(new Hospital(Code, this.getDBConnection()));     
    }
    
    public void newDoctorClass() {
        String Code = getDBConnection().executeQueryString("select DOCTOR_CLASS_CODE from DOCTOR_TYPE where CODE='" + doctorTypeCode + "'");
        setDoctorClass(new DoctorClass(Code, this.getDBConnection()));      
    }

    public df.bean.db.table.DoctorType getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(df.bean.db.table.DoctorType doctorType) {
        this.doctorType = doctorType;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public DoctorClass getDoctorClass() {
        return doctorClass;
    }

    public void setDoctorClass(DoctorClass doctorClass) {
        this.doctorClass = doctorClass;
    }
}
