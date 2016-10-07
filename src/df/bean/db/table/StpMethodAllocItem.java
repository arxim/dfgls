package df.bean.db.table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;

public class StpMethodAllocItem extends ABSTable {

    private Double price = 0d;
    private Double normalAllocatePct = 0d;
    private Double normalAllocateAmt = 0d;
    private String remark = "";
    private String hospitalCode = "";
    private String doctorTreatmentCode = "";
    private String admissionTypeCode = "";
    private String doctorCategoryCode = "";
    private String orderItemCode = "";
    private String excludeTreatment = "";
    private String taxTypeCode = "";
    private String doctorCode = "";
    private String guaranteeSource = "";
    static public final String EXCLUDE_YES = "Y";
    static public final String EXCLUDE_NO = "N";
    
    public List priceArry = new ArrayList();
    public List normalAllocatePctArry = new ArrayList();
    public List normalAllocateAmtArry = new ArrayList();
    public List remarkArry = new ArrayList();
    public List hospitalCodeArry = new ArrayList();
    public List doctorTreatmentCodeArry = new ArrayList();
    public List admissionTypeCodeArry = new ArrayList();
    public List doctorCategoryCodeArry = new ArrayList();
    public List orderItemCodeArry = new ArrayList();
    public List excludeTreatmentArry = new ArrayList();
    public List taxTypeCodeArry = new ArrayList();
    public List doctorCodeArry = new ArrayList();
    public List guaranteeSourceArry = new ArrayList();
    
    public StpMethodAllocItem() {
        
    }
    public String getAdmissionTypeCode() {
        return this.admissionTypeCode;
    }

    public String getDoctorCategoryCode() {
        return this.doctorCategoryCode;
    }

    public String getDoctorTreatmentCode() {
        return this.doctorTreatmentCode;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public Double getNormalAllocateAmt() {
        return this.normalAllocateAmt;
    }

    public Double getNormalAllocatePct() {
        return this.normalAllocatePct;
    }

    public String getOrderItemCode() {
        return this.orderItemCode;
    }

    public Double getPrice() {
        return this.price;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setAdmissionTypeCode(String admissionTypeCode) {
        this.admissionTypeCode = admissionTypeCode;
    }

    public void setDoctorCategoryCode(String doctorCategoryCode) {
        this.doctorCategoryCode = doctorCategoryCode;
    }

    public void setDoctorTreatmentCode(String doctorTreatmentCode) {
        this.doctorTreatmentCode = doctorTreatmentCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setNormalAllocateAmt(Double normalAllocateAmt) {
        this.normalAllocateAmt = normalAllocateAmt;
    }

    public void setNormalAllocatePct(Double normalAllocatePct) {
        this.normalAllocatePct = normalAllocatePct;
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean IsFound(String orderItemCode, String doctorTreatmentCode, String hospitalCode, String doctorCategoryCode, String admissionTypeCode) {
        boolean ret = false;
        this.setResultSet(this.getDBConnection().executeQuery("select * from STP_METHOD_ALLOC_ITEM where ORDER_ITEM_CODE='" + orderItemCode + "'" +
                                                " and DOCTOR_TREATMENT_CODE='" + doctorTreatmentCode + "'" +
                                                " and HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " and DOCTOR_CATEGORY_CODE='" + doctorCategoryCode + "'" +
                                                " and ADMISSION_TYPE_CODE='" + admissionTypeCode + "'"));
            try {
                while (this.getResultSet().next()) {
                    ret = true;
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
        return ret;
    }
    
    public StpMethodAllocItem(String orderItemCode, String doctorTreatmentCode, String hospitalCode, String doctorCategoryCode, String admissionTypeCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from STP_METHOD_ALLOC_ITEM where ORDER_ITEM_CODE='" + orderItemCode + "'" +
                                                " and DOCTOR_TREATMENT_CODE='" + doctorTreatmentCode + "'" +
                                                " and HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " and ACTIVE = '1' "+
                                                " and DOCTOR_CATEGORY_CODE='" + doctorCategoryCode + "'" +
                                                " and ADMISSION_TYPE_CODE='" + admissionTypeCode + "'"));
            try {
                while (this.getResultSet().next()) {
                    this.price = this.getResultSet().getDouble("Price");
                    this.normalAllocatePct = this.getResultSet().getDouble("Normal_Allocate_Pct");
                    this.normalAllocateAmt = this.getResultSet().getDouble("Normal_Allocate_Amt");
                    this.remark = this.getResultSet().getString("Remark");
                    this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
                    this.doctorTreatmentCode = this.getResultSet().getString("DOCTOR_TREATMENT_CODE");
                    this.admissionTypeCode = this.getResultSet().getString("ADMISSION_TYPE_CODE");
                    this.doctorCategoryCode = this.getResultSet().getString("DOCTOR_CATEGORY_CODE");
                    this.orderItemCode = this.getResultSet().getString("ORDER_ITEM_CODE"); 
                    this.excludeTreatment = this.getResultSet().getString("EXCLUDE_TREATMENT"); 
                    this.taxTypeCode = this.getResultSet().getString("TAX_TYPE_CODE");
                    this.guaranteeSource = this.getResultSet().getString("GUARANTEE_SOURCE");
                    this.setDoctorCode(this.getResultSet().getString("DOCTOR_CODE"));
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

    public String getExcludeTreatment() {
        return excludeTreatment;
    }

    public void setExcludeTreatment(String excludeTreatment) {
        this.excludeTreatment = excludeTreatment;
    }

    public String getTaxTypeCode() {
        return taxTypeCode;
    }

    public void setTaxTypeCode(String taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }
    
    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getGuaranteeSource() {
        return guaranteeSource;
    }

    public void setGuaranteeSource(String guaranteeSource) {
        this.guaranteeSource = guaranteeSource;
    }
    
    
    // ============================= select all of table method allocation ===============================//
    public void clearAllArry() {
        this.priceArry.clear();
        this.normalAllocatePctArry.clear();
        this.normalAllocateAmtArry.clear();
        this.remarkArry.clear();
        this.hospitalCodeArry.clear();
        this.doctorTreatmentCodeArry.clear();
        this.admissionTypeCodeArry.clear();
        this.doctorCategoryCodeArry.clear();
        this.orderItemCodeArry.clear();
        this.excludeTreatmentArry.clear();
        this.taxTypeCodeArry.clear();
        this.doctorCodeArry.clear();
        this.guaranteeSourceArry.clear();
    }
    
    public StpMethodAllocItem(String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from STP_METHOD_ALLOC_ITEM where HOSPITAL_CODE='" + hospitalCode + "' and ACTIVE = '1'"));
            try {
                this.clearAllArry();
                
                while (this.getResultSet().next()) {
                    this.priceArry.add(this.getResultSet().getDouble("Price"));
                    this.normalAllocatePctArry.add(this.getResultSet().getDouble("Normal_Allocate_Pct"));
                    this.normalAllocateAmtArry.add(this.getResultSet().getDouble("Normal_Allocate_Amt"));
                    
                    if (this.getResultSet().getString("Remark") == null ) { this.remarkArry.add(""); } else { this.remarkArry.add(this.getResultSet().getString("Remark")); }
                    if (this.getResultSet().getString("HOSPITAL_CODE") == null ) { this.hospitalCodeArry.add(""); } else { this.hospitalCodeArry.add(this.getResultSet().getString("HOSPITAL_CODE")); }
                    if (this.getResultSet().getString("DOCTOR_TREATMENT_CODE") == null ) { this.doctorTreatmentCodeArry.add(""); } else { this.doctorTreatmentCodeArry.add(this.getResultSet().getString("DOCTOR_TREATMENT_CODE")); }
                    if (this.getResultSet().getString("ADMISSION_TYPE_CODE") == null ) { this.admissionTypeCodeArry.add(""); } else { this.admissionTypeCodeArry.add(this.getResultSet().getString("ADMISSION_TYPE_CODE")); }
                    if (this.getResultSet().getString("DOCTOR_CATEGORY_CODE") == null ) { this.doctorCategoryCodeArry.add(""); } else { this.doctorCategoryCodeArry.add(this.getResultSet().getString("DOCTOR_CATEGORY_CODE")); }
                    if (this.getResultSet().getString("ORDER_ITEM_CODE") == null ) { this.orderItemCodeArry.add(""); } else { this.orderItemCodeArry.add(this.getResultSet().getString("ORDER_ITEM_CODE")); }
                    if (this.getResultSet().getString("EXCLUDE_TREATMENT") == null ) { this.excludeTreatmentArry.add(""); } else { this.excludeTreatmentArry.add(this.getResultSet().getString("EXCLUDE_TREATMENT")); }
                    if (this.getResultSet().getString("TAX_TYPE_CODE") == null ) { this.taxTypeCodeArry.add(""); } else { this.taxTypeCodeArry.add(this.getResultSet().getString("TAX_TYPE_CODE")); }
                    if (this.getResultSet().getString("DOCTOR_CODE") == null ) { this.doctorCodeArry.add(""); } else { this.doctorCodeArry.add(this.getResultSet().getString("DOCTOR_CODE")); }
                    if (this.getResultSet().getString("GUARANTEE_SOURCE") == null ) { this.guaranteeSourceArry.add(""); } else { this.guaranteeSourceArry.add(this.getResultSet().getString("GUARANTEE_SOURCE")); }

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
    
    public boolean IsFound(String orderItemCode, String doctorTreatmentCode, String doctorCategoryCode, String admissionTypeCode) {
        boolean ret = false;
        
        for (int i=0; i<this.priceArry.size(); i++) {
            if (this.orderItemCodeArry.get(i).toString().equals(orderItemCode) && 
                    this.doctorTreatmentCodeArry.get(i).toString().equals(doctorTreatmentCode) &&
                    this.doctorCategoryCodeArry.get(i).toString().equals(doctorCategoryCode) && 
                    this.admissionTypeCodeArry.get(i).toString().equals(admissionTypeCode)) {
                    
                    this.setPrice( Double.parseDouble(this.priceArry.get(i).toString()) );
                    this.setNormalAllocatePct( Double.parseDouble(this.normalAllocatePctArry.get(i).toString()) );
                    this.setNormalAllocateAmt( Double.parseDouble(this.normalAllocateAmtArry.get(i).toString()) );
                    this.setRemark( this.remarkArry.get(i).toString() );
                    this.setHospitalCode( this.hospitalCodeArry.get(i).toString() );
                    this.setDoctorTreatmentCode( this.doctorTreatmentCodeArry.get(i).toString() );
                    this.setAdmissionTypeCode( this.admissionTypeCodeArry.get(i).toString() );
                    this.setDoctorCategoryCode( this.doctorCategoryCodeArry.get(i).toString() );
                    this.setOrderItemCode( this.orderItemCodeArry.get(i).toString() );
                    this.setExcludeTreatment( this.excludeTreatmentArry.get(i).toString() );
                    this.setTaxTypeCode( this.taxTypeCodeArry.get(i).toString() );
                    this.setDoctorCode( this.doctorCodeArry.get(i).toString() );
                    this.setGuaranteeSource( this.guaranteeSourceArry.get(i).toString() );
                    
                    ret = true;
                break;
            }
        }
        return ret;
    }
    
    public boolean IsFound(String orderItemCode, String doctorTreatmentCode, String doctorCategoryCode, String admissionTypeCode, Double price) {
        boolean ret = false;
        
        for (int i=0; i<this.priceArry.size(); i++) {
            if (this.orderItemCodeArry.get(i).toString().equals(orderItemCode) && 
                    this.doctorTreatmentCodeArry.get(i).toString().equals(doctorTreatmentCode) &&
                    this.doctorCategoryCodeArry.get(i).toString().equals(doctorCategoryCode) && 
                    this.admissionTypeCodeArry.get(i).toString().equals(admissionTypeCode) &&
                    this.priceArry.get(i).toString().equals(price.toString()) ) {
                    
                    this.setPrice( Double.parseDouble(this.priceArry.get(i).toString()) );
                    this.setNormalAllocatePct( Double.parseDouble(this.normalAllocatePctArry.get(i).toString()) );
                    this.setNormalAllocateAmt( Double.parseDouble(this.normalAllocateAmtArry.get(i).toString()) );
                    this.setRemark( this.remarkArry.get(i).toString() );
                    this.setHospitalCode( this.hospitalCodeArry.get(i).toString() );
                    this.setDoctorTreatmentCode( this.doctorTreatmentCodeArry.get(i).toString() );
                    this.setAdmissionTypeCode( this.admissionTypeCodeArry.get(i).toString() );
                    this.setDoctorCategoryCode( this.doctorCategoryCodeArry.get(i).toString() );
                    this.setOrderItemCode( this.orderItemCodeArry.get(i).toString() );
                    this.setExcludeTreatment( this.excludeTreatmentArry.get(i).toString() );
                    this.setTaxTypeCode( this.taxTypeCodeArry.get(i).toString() );
                    this.setDoctorCode( this.doctorCodeArry.get(i).toString() );
                    this.setGuaranteeSource( this.guaranteeSourceArry.get(i).toString() );

                    ret = true;
                break;
            }
        }
        return ret;
    }
}
