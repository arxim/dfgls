package df.bean.db.table;

import java.sql.SQLException;

import df.bean.db.conn.DBConnection;

public class Hospital extends ABSTable {

    private String code;
    private String descriptionThai;
    private String descriptionEng;
    private String address1;
    private String address2;
    private String address3;
    private String zip;
    private String tel;
    private String fax;
    private String email;
    private String taxno;
    private String contractNameThai;
    private String contractNameEng;
    private String contractPosition;
    private String contractTel;
    private String contractEmail;
    private String updateDate;
    private String updateTime;
    private String bankCode;
    private String bankBranchCode;
    private String accountNo;

    public Hospital() {
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

    public String getCode() {
        return this.code;
    }

    public String getContractEmail() {
        return this.contractEmail;
    }

    public String getContractNameEng() {
        return this.contractNameEng;
    }

    public String getContractNameThai() {
        return this.contractNameThai;
    }

    public String getContractPosition() {
        return this.contractPosition;
    }

    public String getContractTel() {
        return this.contractTel;
    }

    public String getDescriptionEng() {
        return this.descriptionEng;
    }

    public String getDescriptionThai() {
        return this.descriptionThai;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFax() {
        return this.fax;
    }

    public String getTaxno() {
        return this.taxno;
    }

    public String getTel() {
        return this.tel;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public String getUpdateTime() {
        return this.updateTime;
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

    public void setCode(String code) {
        this.code = code;
    }

    public void setContractEmail(String contractEmail) {
        this.contractEmail = contractEmail;
    }

    public void setContractNameEng(String contractNameEng) {
        this.contractNameEng = contractNameEng;
    }

    public void setContractNameThai(String contractNameThai) {
        this.contractNameThai = contractNameThai;
    }

    public void setContractPosition(String contractPosition) {
        this.contractPosition = contractPosition;
    }

    public void setContractTel(String contractTel) {
        this.contractTel = contractTel;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
    }

    public void setDescriptionThai(String descriptionThai) {
        this.descriptionThai = descriptionThai;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setTaxno(String taxno) {
        this.taxno = taxno;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public Hospital(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from HOSPITAL where CODE='" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.descriptionThai = this.getResultSet().getString("Description_Thai");
                this.descriptionEng = this.getResultSet().getString("Description_Eng");
                this.address1 = this.getResultSet().getString("Address1");
                this.address2 = this.getResultSet().getString("Address2");
                this.address3 = this.getResultSet().getString("Address3");
                this.zip = this.getResultSet().getString("Zip");
                this.tel = this.getResultSet().getString("Tel");
                this.fax = this.getResultSet().getString("Fax");
                this.email = this.getResultSet().getString("Email");
                this.taxno = this.getResultSet().getString("TaxNo");
                this.contractNameThai = this.getResultSet().getString("Contract_Name_Thai");
                this.contractNameEng = this.getResultSet().getString("Contract_Name_Eng");
                this.contractPosition = this.getResultSet().getString("Contract_Position");
                this.contractTel = this.getResultSet().getString("Contract_Tel");
                this.contractEmail = this.getResultSet().getString("Email");
                this.bankCode = this.getResultSet().getString("BANK_CODE");
                this.bankBranchCode = this.getResultSet().getString("BANK_BRANCH_CODE");
                this.accountNo = this.getResultSet().getString("Account_No");
                this.updateDate = this.getResultSet().getString("UPDATE_DATE");
                this.updateTime = this.getResultSet().getString("Update_Time");
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


    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankBranchCode(String bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
}
