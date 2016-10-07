package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class OrderItem extends ABSTable {

    private String code;
    private String descriptionThai;
    private String descriptionEng;
    private String hospitalCode;
    private Integer handicraft;
    private String taxTypeCode;
    private String orderItemCategoryCode;
    private String accountCode;
    private String isCompute;
    private String isAllocFullTax;
    private String active;
    static final public String ALLOC_FULL_TAX_YES = "Y";
    static final public String ALLOC_FULL_TAX_NO = "N";
    public OrderItem() {
        super();
    }

    public String getCode() {
        return this.code;
    }

    public String getDescriptionEng() {
        return this.descriptionEng;
    }

    public String getDescriptionThai() {
        return this.descriptionThai;
    }

    public Integer getHandicraft() {
        return this.handicraft;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
    }

    public void setDescriptionThai(String descriptionThai) {
        this.descriptionThai = descriptionThai;
    }

    public void setHandicraft(Integer handicraft) {
        this.handicraft = handicraft;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }
    
    public boolean IsHandicraft() {
        boolean ret = false;
            if (this.getHandicraft() == 1) {
                ret = true;
            }
        return ret;
    }
    
    public OrderItem(String code, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from ORDER_ITEM where CODE='" + code + "'" +
                                                " and HOSPITAL_CODE='" + hospitalCode + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.descriptionThai = this.getResultSet().getString("Description_Thai");
                this.descriptionEng = this.getResultSet().getString("Description_Eng");
                this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
                this.handicraft = this.getResultSet().getInt("Handicraft");
                this.taxTypeCode = this.getResultSet().getString("TAX_TYPE_CODE");
                this.isAllocFullTax = this.getResultSet().getString("IS_ALLOC_FULL_TAX");
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

    public String getTaxTypeCode() {
        return taxTypeCode;
    }

    public void setTaxTypeCode(String taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }

    public String getOrderItemCategoryCode() {
        return orderItemCategoryCode;
    }

    public void setOrderItemCategoryCode(String orderItemCategoryCode) {
        this.orderItemCategoryCode = orderItemCategoryCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getIsCompute() {
        return isCompute;
    }

    public void setIsCompute(String isCompute) {
        this.isCompute = isCompute;
    }

    public String getIsAllocFullTax() {
        return isAllocFullTax;
    }

    public void setIsAllocFullTax(String isAllocFullTax) {
        this.isAllocFullTax = isAllocFullTax;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String ACTIVE) {
        this.active = ACTIVE;
    }
}
