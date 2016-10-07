package df.bean.db.table;

import java.sql.SQLException;

import df.bean.db.conn.DBConnection;

public class StpMethodAllocItemCategory extends StpMethodAllocItem {

    public StpMethodAllocItemCategory() {
    }

    @Override
    public boolean IsFound(String orderItemCategoryCode, String doctorTreatmentCode, String hospitalCode, String doctorCategoryCode, String admissionTypeCode) {
        boolean ret = false;
        this.setResultSet(this.getDBConnection().executeQuery("select * from STP_METHOD_ALLOC_ITEM_CATEGORY where ORDER_ITEM_CODE='" + orderItemCategoryCode + "'" +
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
    
    public StpMethodAllocItemCategory(String orderItemCategoryCode, String doctorTreatmentCode, String hospitalCode, String doctorCategoryCode, String admissionTypeCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from STP_METHOD_ALLOC_ITEM_CATEGORY where ORDER_ITEM_CODE='" + orderItemCategoryCode + "'" +
                                                " and DOCTOR_TREATMENT_CODE='" + doctorTreatmentCode + "'" +
                                                " and HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " and DOCTOR_CATEGORY_CODE='" + doctorCategoryCode + "'" +
                                                " and ADMISSION_TYPE_CODE='" + admissionTypeCode + "'"));
            try {
                while (this.getResultSet().next()) {
                    this.setPrice(this.getResultSet().getDouble("Price"));
                    this.setNormalAllocatePct(this.getResultSet().getDouble("Normal_Allocate_Pct"));
                    this.setNormalAllocateAmt(this.getResultSet().getDouble("Normal_Allocate_Amt"));
                    this.setRemark(this.getResultSet().getString("Remark"));
                    this.setHospitalCode( this.getResultSet().getString("HOSPITAL_CODE"));
                    this.setDoctorTreatmentCode(this.getResultSet().getString("DOCTOR_TREATMENT_CODE"));
                    this.setAdmissionTypeCode(this.getResultSet().getString("ADMISSION_TYPE_CODE"));
                    this.setDoctorCategoryCode(this.getResultSet().getString("DOCTOR_CATEGORY_CODE"));
                    this.setOrderItemCode(this.getResultSet().getString("ORDER_ITEM_CODE")); 
                    this.setExcludeTreatment(this.getResultSet().getString("EXCLUDE_TREATMENT")); 
                    this.setTaxTypeCode(this.getResultSet().getString("TAX_TYPE_CODE"));
                    this.setDoctorCode(this.getResultSet().getString("DOCTOR_CODE"));
                    this.setGuaranteeSource(this.getResultSet().getString("GUARANTEE_SOURCE"));
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

    
    // ============================= select all of table method allocation ===============================//
    @Override
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
    
    public StpMethodAllocItemCategory(String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from STP_METHOD_ALLOC_ITEM_CATEGORY where HOSPITAL_CODE='" + hospitalCode + "' AND ACTIVE = '1'"));
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
    
    @Override
    public boolean IsFound(String orderItemCategoryCode, String doctorTreatmentCode, String doctorCategoryCode, String admissionTypeCode) {
        boolean ret = false;
        
        for (int i=0; i<this.priceArry.size(); i++) {
            if (this.orderItemCodeArry.get(i).toString().equals(orderItemCategoryCode) && 
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
    
    // ������¡�äӹǳ�ҡ �Ҥ�
    @Override
    public boolean IsFound(String orderItemCategoryCode, String doctorTreatmentCode, String doctorCategoryCode, String admissionTypeCode, Double price) {
        boolean ret = false;
        
        for (int i=0; i<this.priceArry.size(); i++) {
            if (this.orderItemCodeArry.get(i).toString().equals(orderItemCategoryCode) && 
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
