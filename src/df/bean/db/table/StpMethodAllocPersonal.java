package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import df.bean.db.conn.DBConnection;

public class StpMethodAllocPersonal extends StpMethodAllocItem {

    @Override
    public boolean IsFound(String orderItemCode, String doctorTreatmentCode, String hospitalCode, String doctorCode, String admissionTypeCode) {
        boolean ret = false;
        this.setResultSet(this.getDBConnection().executeQuery("select * from STP_METHOD_ALLOC_ITEM where ORDER_ITEM_CODE='" + orderItemCode + "'" +
                                                " and DOCTOR_TREATMENT_CODE='" + doctorTreatmentCode + "'" +
                                                " and HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " and DOCTOR_CODE='" + doctorCode + "'" +
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
    
    public StpMethodAllocPersonal(DBConnection conn, String orderItemCode, String doctorTreatmentCode, String hospitalCode, String doctorCode, String admissionTypeCode) {
//        super("", "", "", "", "", conn);
        super();
        this.setDBConnection(conn);
        ResultSet rs = this.getDBConnection().executeQuery("select * from STP_MEDTHOD_ALLOC_PERSONAL where ORDER_ITEM_CODE='" + orderItemCode + "'" +
                                                " and DOCTOR_TREATMENT_CODE='" + doctorTreatmentCode + "'" +
                                                " and HOSPITAL_CODE='" + hospitalCode + "'" +
                                                " and DOCTOR_CODE='" + doctorCode + "'" +
                                                " and ADMISSION_TYPE_CODE='" + admissionTypeCode + "'");
            try {
                while (rs.next()) {
                    this.setPrice( rs.getDouble("Price") );
                    this.setNormalAllocatePct(rs.getDouble("Normal_Allocate_Pct"));
                    this.setNormalAllocateAmt (rs.getDouble("Normal_Allocate_Amt"));
                    this.setRemark (rs.getString("Remark"));
                    this.setHospitalCode (rs.getString("HOSPITAL_CODE"));
                    this.setDoctorTreatmentCode (rs.getString("DOCTOR_TREATMENT_CODE"));
                    this.setAdmissionTypeCode (rs.getString("ADMISSION_TYPE_CODE"));
                    this.setDoctorCategoryCode (rs.getString("DOCTOR_CATEGORY_CODE"));
                    this.setOrderItemCode (rs.getString("ORDER_ITEM_CODE")); 
                    this.setExcludeTreatment (rs.getString("EXCLUDE_TREATMENT")); 
                    this.setTaxTypeCode (rs.getString("TAX_TYPE_CODE"));
                    this.setDoctorCode(rs.getString("DOCTOR_CODE"));
                    this.setGuaranteeSource(rs.getString("GUARANTEE_SOURCE"));
                }
            } catch (SQLException e) {
                // TODO
                e.printStackTrace();
            } finally {
               //Clean up resources, close the connection.
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
    }
    
    //=========================== select alll method allocation personal =============================//
    public StpMethodAllocPersonal(String hospitalCode, DBConnection conn) {
        super();
        this.setDBConnection(conn);
        ResultSet rs = this.getDBConnection().executeQuery("select * from STP_METHOD_ALLOC_PERSONAL where HOSPITAL_CODE='" + hospitalCode + "' AND ACTIVE = '1'");
            try {
                this.clearAllArry();
                
                while (rs.next()) {
                    this.priceArry.add(rs.getDouble("Price"));
                    this.normalAllocatePctArry.add(rs.getDouble("Normal_Allocate_Pct"));
                    this.normalAllocateAmtArry.add(rs.getDouble("Normal_Allocate_Amt"));
                    
                    if (rs.getString("Remark") == null ) { this.remarkArry.add(""); } else { this.remarkArry.add(rs.getString("Remark")); }
                    if (rs.getString("HOSPITAL_CODE") == null ) { this.hospitalCodeArry.add(""); } else { this.hospitalCodeArry.add(rs.getString("HOSPITAL_CODE")); }
                    if (rs.getString("DOCTOR_TREATMENT_CODE") == null ) { this.doctorTreatmentCodeArry.add(""); } else { this.doctorTreatmentCodeArry.add(rs.getString("DOCTOR_TREATMENT_CODE")); }
                    if (rs.getString("ADMISSION_TYPE_CODE") == null ) { this.admissionTypeCodeArry.add(""); } else { this.admissionTypeCodeArry.add(rs.getString("ADMISSION_TYPE_CODE")); }
                    if (rs.getString("DOCTOR_CATEGORY_CODE") == null ) { this.doctorCategoryCodeArry.add(""); } else { this.doctorCategoryCodeArry.add(rs.getString("DOCTOR_CATEGORY_CODE")); }
                    if (rs.getString("ORDER_ITEM_CODE") == null ) { this.orderItemCodeArry.add(""); } else { this.orderItemCodeArry.add(rs.getString("ORDER_ITEM_CODE")); }
                    if (rs.getString("EXCLUDE_TREATMENT") == null ) { this.excludeTreatmentArry.add(""); } else { this.excludeTreatmentArry.add(rs.getString("EXCLUDE_TREATMENT")); }
                    if (rs.getString("TAX_TYPE_CODE") == null ) { this.taxTypeCodeArry.add(""); } else { this.taxTypeCodeArry.add(rs.getString("TAX_TYPE_CODE")); }
                    if (rs.getString("DOCTOR_CODE") == null ) { this.doctorCodeArry.add(""); } else { this.doctorCodeArry.add(rs.getString("DOCTOR_CODE")); }
                    if (rs.getString("GUARANTEE_SOURCE") == null ) { this.guaranteeSourceArry.add(""); } else { this.guaranteeSourceArry.add(rs.getString("GUARANTEE_SOURCE")); }

                }
            } catch (SQLException e) {
                // TODO
                e.printStackTrace();
            } finally {
               //Clean up resources, close the connection.
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
    }
    public boolean IsFound(String orderItemCode, String doctorTreatmentCode, String doctorCode, String admissionTypeCode) {
        boolean ret = false;
        
        for (int i=0; i<this.priceArry.size(); i++) {
            if (this.orderItemCodeArry.get(i).toString().equals(orderItemCode) && 
                    this.doctorTreatmentCodeArry.get(i).toString().equals(doctorTreatmentCode) &&
                    this.doctorCodeArry.get(i).toString().equals(doctorCode) && 
                    this.admissionTypeCodeArry.get(i).toString().equals(admissionTypeCode) ) {
                    
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
    
    // �������ͤӹǳ���� �Ҥ�
    public boolean IsFound(String orderItemCode, String doctorTreatmentCode, String doctorCode, String admissionTypeCode, Double price) {
        boolean ret = false;
        
        for (int i=0; i<this.priceArry.size(); i++) {
            if (this.orderItemCodeArry.get(i).toString().equals(orderItemCode) && 
                    this.doctorTreatmentCodeArry.get(i).toString().equals(doctorTreatmentCode) &&
                    this.doctorCodeArry.get(i).toString().equals(doctorCode) && 
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
