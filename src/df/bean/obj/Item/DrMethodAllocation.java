package df.bean.obj.Item;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.StpMethodAllocMaster;
import df.bean.db.table.StpMethodAllocPersonal;
import df.bean.db.table.StpMethodAllocItem;
import df.bean.db.table.StpMethodAllocItemCategory;


public class DrMethodAllocation {
    StpMethodAllocItem methodAllocation = null;
    StpMethodAllocItem methodAllocationNormal = null;
    StpMethodAllocItemCategory methodAllocItemCategory = null;
    StpMethodAllocMaster methodAllocationMaster = null;
    StpMethodAllocPersonal methodAllocationPersonal = null;

    private String orderItemCode;
    private String orderItemCategoryCode;
    private String doctorTreatmentCode;
    private String hospitalCode;
    private String admissionTypeCode;
    private String doctorCategoryCode;
    private String doctorCode;
    
    public DBConnection conn = null;
    // Item
    public DrMethodAllocation(String orderItemCode, String doctorTreatmentCode, String hospitalCode, String doctorCategoryCode, String admissionTypeCode, DBConnection conn) {
        this.setOrderItemCode(orderItemCode);
        this.setDoctorTreatmentCode(doctorTreatmentCode);
        this.setHospitalCode(hospitalCode);
        this.setAdmissionTypeCode(admissionTypeCode);
        this.setOrderItemCode(orderItemCode);
        this.setDoctorCategoryCode(doctorCategoryCode);
        
        this.conn = conn;
        this.newMethodAllocation();
    }
    // Item Category
    public DrMethodAllocation(String orderItemCategoryCode, String orderItemCode, String doctorTreatmentCode, String hospitalCode, String doctorCategoryCode, String admissionTypeCode, DBConnection conn) {
        this.setOrderItemCode(orderItemCode);
        this.setDoctorTreatmentCode(doctorTreatmentCode);
        this.setHospitalCode(hospitalCode);
        this.setAdmissionTypeCode(admissionTypeCode);
        this.setOrderItemCode(orderItemCode);
        this.setDoctorCategoryCode(doctorCategoryCode);
        this.setOrderItemCategoryCode(orderItemCategoryCode);
        
        this.conn = conn;
        this.newMethodAllocation();
    }
    // Master
    public DrMethodAllocation(String doctorTreatmentCode, String hospitalCode, String doctorCategoryCode, String admissionTypeCode, DBConnection conn) {
        this.setOrderItemCode(orderItemCode);
        this.setDoctorTreatmentCode(doctorTreatmentCode);
        this.setHospitalCode(hospitalCode);
        this.setAdmissionTypeCode(admissionTypeCode);
        this.setOrderItemCode(orderItemCode);
        this.setDoctorCategoryCode(doctorCategoryCode);
        
        this.conn = conn;
        this.newMethodAllocItemCategory();                
    }
    // Personal
     public DrMethodAllocation(DBConnection conn, String orderItemCode, String doctorTreatmentCode, String hospitalCode, String doctorCode, String admissionTypeCode) {
         this.setOrderItemCode(orderItemCode);
         this.setDoctorTreatmentCode(doctorTreatmentCode);
         this.setHospitalCode(hospitalCode);
         this.setAdmissionTypeCode(admissionTypeCode);
         this.setOrderItemCode(orderItemCode);
         this.setDoctorCode(doctorCode);
         
         this.conn = conn;
         this.newMethodAllocPersonal();
     }
     
    public DrMethodAllocation(DBConnection conn, String hospitalCode) {
    	//System.out.println("New Method");
        this.setHospitalCode(hospitalCode);
        this.conn = conn;
        this.newMethodAllocation();
        this.newMethodAllocMaster();
        this.newMethodAllocPersonal();
        this.newMethodAllocItemCategory();
    }

    @Override
    public void finalize() {
        methodAllocation = null;
    }
    
    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    public DBConnection getDBConnection() {
        return this.conn;
    }    
    public void newMethodAllocation() {
//        setMethodAllocation(new SetMethodAllocation(this.getOrderItemCode(), this.getDoctorTreatmentCode(), this.getHospitalCode(), this.getDoctorCategoryCode(), this.getAdmissionTypeCode(), this.getDBConnection()));
        methodAllocationNormal = new StpMethodAllocItem(this.getHospitalCode(), this.getDBConnection());
    }    
    public void newMethodAllocItemCategory() {
        methodAllocItemCategory = new StpMethodAllocItemCategory(this.getHospitalCode(), this.getDBConnection());
    }
    public void newMethodAllocMaster() {
//        setMethodAllocation(new StpMethodAllocMaster(this.getDoctorTreatmentCode(), this.getHospitalCode(), this.getDoctorCategoryCode(), this.getAdmissionTypeCode(), this.getDBConnection()));
        methodAllocationMaster = new StpMethodAllocMaster(this.getHospitalCode(), this.getDBConnection());
    }
    public void newMethodAllocPersonal() {
//        setMethodAllocation(new StpMethodAllocPersonal(this.getDBConnection(), this.getOrderItemCode(), this.getDoctorTreatmentCode(), this.getHospitalCode(), this.getDoctorCode(), this.getAdmissionTypeCode()));
        methodAllocationPersonal = new StpMethodAllocPersonal(this.getHospitalCode(), this.getDBConnection());
    }
    
    public void useMethodAllocationNormal() {
        setMethodAllocation(methodAllocationNormal);
    }
    public void useMethodAllocItemCategory() {
        setMethodAllocation(methodAllocItemCategory);
    }
    public void useMethodAllocationMaster() {
        setMethodAllocation(methodAllocationMaster);
    }
    public void useMethodAllocationPersonal() {
        setMethodAllocation(methodAllocationPersonal);
    }
    
    public StpMethodAllocItem getMethodAllocation() {
        return methodAllocation;
    }

    public void setMethodAllocation(StpMethodAllocItem methodAllocation) {
        this.methodAllocation = methodAllocation;
    }
    
    public String getOrderItemCode() {
        return orderItemCode;
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }

    public String getDoctorTreatmentCode() {
        return doctorTreatmentCode;
    }

    public void setDoctorTreatmentCode(String doctorTreatmentCode) {
        this.doctorTreatmentCode = doctorTreatmentCode;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getAdmissionTypeCode() {
        return admissionTypeCode;
    }

    public void setAdmissionTypeCode(String admissionTypeCode) {
        this.admissionTypeCode = admissionTypeCode;
    }

    public boolean IsFound() {
        return this.getMethodAllocation().IsFound(this.getOrderItemCode(), this.getDoctorTreatmentCode(), this.getHospitalCode(), this.getDoctorCategoryCode(), this.getAdmissionTypeCode());
    }

    public String getDoctorCategoryCode() {
        return doctorCategoryCode;
    }

    public void setDoctorCategoryCode(String doctorCategoryCode) {
        this.doctorCategoryCode = doctorCategoryCode;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }
    
    public String getOrderItemCategoryCode() {
        return orderItemCategoryCode;
    }

    public void setOrderItemCategoryCode(String orderItemCategoryCode) {
        this.orderItemCategoryCode = orderItemCategoryCode;
    }
}
