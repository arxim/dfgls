package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class ReceiptType extends ABSTable {

    private String code;
    private String descriptionThi;
    private String descriptionEng;
    private Integer isCharge;
    private Double percentOfCharge;
    private String ACTIVE;
    private String updateDate;
    private String updateTime;
    private String userId;
    private String bankCode;
    private String hospitalCode;
    private String ReceiptModeCode;

    public ReceiptType() {
        super();
    }

    public String getActive() {
        return this.ACTIVE;
    }
    
    public String getBankCode() {
        return this.bankCode;
    }

    public String getCode() {
        if (this.code == null) {
            this.code = "";
        }
        return this.code;
    }

    public String getDescriptionEng() {
        return this.descriptionEng;
    }

    public String getDescriptionThi() {
        return this.descriptionThi;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public Integer getIsCharge() {
        return this.isCharge;
    }

    public Double getPercentOfCharge() {
        return this.percentOfCharge;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
    }

    public void setDescriptionThi(String descriptionThi) {
        this.descriptionThi = descriptionThi;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setIsCharge(Integer isCharge) {
        this.isCharge = isCharge;
    }

    public void setPercentOfCharge(Double percentOfCharge) {
        this.percentOfCharge = percentOfCharge;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiptModeCode() {
        return ReceiptModeCode;
    }

    public void setReceiptModeCode(String ReceiptModeCode) {
        this.ReceiptModeCode = ReceiptModeCode;
    }
    
    public ReceiptType(String hospitalCode, String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from RECEIPT_TYPE where HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " and CODE = '" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.descriptionThi = this.getResultSet().getString("Description_Thi");
                this.descriptionEng = this.getResultSet().getString("Description_Eng");
                this.isCharge = this.getResultSet().getInt("Is_Charge");
                this.percentOfCharge = this.getResultSet().getDouble("PERCENT_OF_CHARGE");
                this.ACTIVE = this.getResultSet().getString("ACTIVE");
                this.updateDate = this.getResultSet().getString("UPDATE_DATE");
                this.updateTime = this.getResultSet().getString("Update_Time");
                this.userId = this.getResultSet().getString("USER_ID");
                this.bankCode = this.getResultSet().getString("BANK_CODE");
                this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
                this.ReceiptModeCode = this.getResultSet().getString("RECEIPT_MODE_CODE");
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
