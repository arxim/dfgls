package df.bean.db.table;

import java.sql.SQLException;
import java.sql.Timestamp;
import df.bean.db.conn.DBConnection;

public class DoctorProfile  extends ABSTable{

    private String code;
    private String nameThai;
    private String nameEng;
    private String address1;
    private String address2;
    private String address3;
    private String zip;
    private String placeOfWork;
    private String birthDate;
    private String nationId;
    private String fromDate;
    private String toDate;
    private Timestamp updateDatetime;
    private String hospitalCode;
    private String position ;
    private String branch;
    private String hpRefName01;
    private String hpRefBranch01;
    private String hpRefYear01;
    private String hpRefName02;
    private String hpRefBranch02;
    private String hpRefYear02;

    public DoctorProfile() {
        super();
    }

    public String getAddress1() {
        return this.address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public String getAddress3() {
        return this.address3;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public String getCode() {
        return this.code;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getNameEng() {
        return this.nameEng;
    }

    public String getNameThai() {
        return this.nameThai;
    }

    public String getNationId() {
        return this.nationId;
    }

    public String getPlaceOfWork() {
        return this.placeOfWork;
    }

    public String getToDate() {
        return this.toDate;
    }

    public Timestamp getUpdateDatetime() {
        return this.updateDatetime;
    }

    public String getZip() {
        return this.zip;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public void setNameThai(String nameThai) {
        this.nameThai = nameThai;
    }

    public void setNationId(String nationId) {
        this.nationId = nationId;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public void setUpdateDatetime(Timestamp updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public DoctorProfile(String code, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from DOCTOR_GROUP where CODE='" + code + "'" +
                                                " and HOSPITAL_CODE = '" + hospitalCode + "'"));
        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.nameThai = this.getResultSet().getString("NAME_THAI");
                this.nameEng = this.getResultSet().getString("Name_Eng");
                this.address1 = this.getResultSet().getString("Address1");
                this.address2 = this.getResultSet().getString("Address2");
                this.address3 = this.getResultSet().getString("Address3");
                this.zip = this.getResultSet().getString("Zip");
                this.placeOfWork = this.getResultSet().getString("Place_Of_Work");
                this.birthDate = this.getResultSet().getString("Birth_Date");
                this.nationId = this.getResultSet().getString("Nation_ID");
                this.fromDate = this.getResultSet().getString("From_Date");
                this.toDate = this.getResultSet().getString("To_Date");
                this.updateDatetime = this.getResultSet().getTimestamp("Update_DateTime");
                this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
                this.position = this.getResultSet().getString("Position");
                this.branch = this.getResultSet().getString("Branch");
                this.hpRefName01 = this.getResultSet().getString("HP_Ref_Name_01");
                this.hpRefBranch01 = this.getResultSet().getString("HP_Ref_Branch_01");
                this.hpRefYear01 = this.getResultSet().getString("HP_Ref_Year_01");
                this.hpRefName02 = this.getResultSet().getString("HP_Ref_Name_02");
                this.hpRefBranch02 = this.getResultSet().getString("HP_Ref_Branch_02");
                this.hpRefYear02 = this.getResultSet().getString("HP_Ref_Year_02");
            }
            this.getResultSet().close();
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
        this.setResultSet(null);
    }

    public String getHpRefName01() {
        return hpRefName01;
    }

    public void setHpRefName01(String hpRefName01) {
        this.hpRefName01 = hpRefName01;
    }

    public String getHpRefBranch01() {
        return hpRefBranch01;
    }

    public void setHpRefBranch01(String hpRefBranch01) {
        this.hpRefBranch01 = hpRefBranch01;
    }

    public String getHpRefYear01() {
        return hpRefYear01;
    }

    public void setHpRefYear01(String hpRefYear01) {
        this.hpRefYear01 = hpRefYear01;
    }

    public String getHpRefName02() {
        return hpRefName02;
    }

    public void setHpRefName02(String hpRefName02) {
        this.hpRefName02 = hpRefName02;
    }

    public String getHpRefBranch02() {
        return hpRefBranch02;
    }

    public void setHpRefBranch02(String hpRefBranch02) {
        this.hpRefBranch02 = hpRefBranch02;
    }

    public String getHpRefYear02() {
        return hpRefYear02;
    }

    public void setHpRefYear02(String hpRefYear02) {
        this.hpRefYear02 = hpRefYear02;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
