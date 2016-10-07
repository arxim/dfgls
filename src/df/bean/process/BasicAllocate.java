package df.bean.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.db.table.Doctor;
import df.bean.db.table.DoctorTreatment;
import df.bean.db.table.OrderItem;
import df.bean.db.table.SummaryTax406;
import df.bean.db.table.TRN_Error;
import df.bean.db.table.TrnDaily;
import df.bean.obj.Item.DrMethodAllocation;
import df.bean.obj.Item.MethodAllocation;
import df.bean.obj.invoice.InvoiceDetail;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Utils;
import df.bean.obj.util.Variables;
import df.bean.obj.variable.SummaryMonthlyVar;


public class BasicAllocate {
    private Doctor doctor = null;
    private ResultSet rs = null;
    private Statement statement = null;
    private TrnDaily trnDaily = null;
    private MethodAllocation methodAllocation = null;
    private Batch b = null;
    InvoiceDetail invoiceDetail = null;
    DBConnection conn = null;  

    private String doctorCode;
    private String hospitalCode;
    private String invoiceNo;
    private String treatmentType;
    private String lineNo;
    private String receiptDate;
    
    public String lYyyy, lMm, lDd, lHospitalCode, lInvNo, lInvDate, lHno, lPatientName, lDoctorDepartmentCode;
    public String lLocationCode, lLineNo, lOrderItemCode, lReceiptModeCode, lReceiptTypeCode, lPaymentModule, lDoctorCode;
    public Double lRecAmt, lInvAmt, lInvDiscountAmt, lTotPremiumAmt, lNorAllocateAmt, lNorAllocatePct, lDrAmt, lDrPremium;
    public Double lDrTax400, lDrTax401, lDrTax402, lDrTax406, lHpAmt, lHpPremium, lHpTax, lPremiumChargePct, lPremiumRecAmt;
    public String lStatusCode, lTaxTypeCode, lAdmissionTypeCode, lDoctorTreatmentCode, lTransactionDate, lOrderDate;
    public String lDoctorCategoryCode, lExcludeTreatment;
    public String lInvIsVoid, lRecIsVoid, lIsGuaranteeFromAlloc;
    public Double lguaranteeAmt;
    public String message = "";
    public String guaranteeMsg = "";
    private String tax_from_allocate = "";
    
    public BasicAllocate() {
    }
    public BasicAllocate(DBConnection conn) {
        this.setDBConnection(conn);
    }
    public BasicAllocate(String doctorCode, String hospitalCode, DBConnection conn) {
        this.setDoctorCode(doctorCode);
        this.setHospitalCode(hospitalCode);
        this.setDBConnection(conn);
    }
    public DBConnection getDBConnection() {
        return this.conn;
    }
    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    
    // คำนวณหาค่า Method Allocation
    protected boolean computeMethodAllocation() {
        try {
            double amountAftDiscount = lInvAmt;
            double normalAllocatePct = methodAllocation.getNormalAllocPct();
            double normalAllocateAmt = methodAllocation.getNormalAllocAmt();
            double normalAllocatePrice = methodAllocation.getPrice();
            double drAmount=0, hpAmount=0, amtAftDisc = amountAftDiscount;
            
            // allocate percent
            if (normalAllocatePct != 0) { drAmount = amtAftDisc * normalAllocatePct /100; }  // percent

            // allocate amount
            if ((normalAllocatePrice == 0) && (normalAllocateAmt != 0)) {
                drAmount = amountAftDiscount;
                // ตรวจสอบว่า ถ้าจำนวนเงินมากกว่าที่ setup ไว้  ให้จ่ายได้สูงสุดเท่ากับ ที่ setup ไว้
                if (amountAftDiscount > normalAllocateAmt) { drAmount = normalAllocateAmt; }   
            }

            // allocate fix price
            if ((normalAllocatePrice != 0) && (normalAllocatePrice == amountAftDiscount)) {
            	drAmount = normalAllocateAmt;
            }

            // กรณี order item ไม่นำไปคำนวณค่าแพทย์ 
            if ( !trnDaily.getIsCompute().equalsIgnoreCase("Y") ) { normalAllocateAmt = 0; normalAllocatePct=0; drAmount = 0;}
                        
            hpAmount = amountAftDiscount - drAmount;
            this.lNorAllocateAmt = normalAllocateAmt;
            this.lNorAllocatePct = normalAllocatePct;
            this.lDrAmt = drAmount;
            this.lHpAmt = hpAmount;

            if (methodAllocation.getGuaranteeSource().equalsIgnoreCase("DF")) {
                this.lguaranteeAmt = 0.00;
                this.lIsGuaranteeFromAlloc = "D";
                this.guaranteeMsg = "ALLOC";
                this.tax_from_allocate = "Y";
            } else if(methodAllocation.getGuaranteeSource().equalsIgnoreCase("FULL")) {
                this.lguaranteeAmt = 0.00;
                this.lIsGuaranteeFromAlloc = "F";
                this.guaranteeMsg = "ALLOC";
                this.tax_from_allocate = "N";
            } else{
                this.guaranteeMsg = "";
                this.lIsGuaranteeFromAlloc = "N";
                this.tax_from_allocate = "N";
                this.lguaranteeAmt = 0.00;
            }

        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_DAILY, "Compute Medthod Allocation is error", 
                    e.getMessage(), "",TRN_Error.ERROR_TYPE_COMPUTE_METHOD_ALLOCATE_ERROR);
            return false;
        }
        return true;
    }
    
    // คำนวณหาค่า Tax
    // Compute all of care provider
    public boolean computeTransDaily() throws SQLException {
    	System.out.println("Compute CareProvider");
        boolean ret = true;
        try {
            if (this.methodAllocation == null) {
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_DAILY, "Method Allocation is null", "", "", TRN_Error.ERROR_TYPE_METHOD_ALLOCATION_IS_NULL);
            }
            // Receipt Detail
            this.lHospitalCode = trnDaily.getHospitalCode();
            this.lInvNo = trnDaily.getInvoiceNo();
            this.lLineNo = trnDaily.getLineNo();
            this.lOrderItemCode = trnDaily.getOrderItemCode();
            this.lDoctorCategoryCode = this.getDoctor().getDoctorCategoryCode();
            this.lDoctorCode = this.getDoctorCode(); 
            this.lInvAmt = trnDaily.getAmountAftDiscount();
            this.lInvDiscountAmt = trnDaily.getAmountOfDiscount();
            this.lAdmissionTypeCode = trnDaily.getAdmissionTypeCode();
            this.lDoctorTreatmentCode = DoctorTreatment.TYPE_CURE; 
            this.lReceiptTypeCode = trnDaily.getReceiptTypeCode();
            this.lRecAmt = lInvAmt + lInvDiscountAmt;
            this.lTransactionDate = trnDaily.getTransactionDate();
            this.lInvIsVoid = trnDaily.getInvIsVoid();
            this.lRecIsVoid = trnDaily.getRecIsVoid();
            this.lInvDate = trnDaily.getInvoiceDate();
            this.lHno = trnDaily.getHnNo();
            // Method Allocation
            if (true){
                ret = false; // by default
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                        TRN_Error.PROCESS_DAILY, "Error : Inv No : " + this.lInvNo + " is not found condition : " +
                                     " Doctor Code : '" + this.lDoctorCode + "'" +
                                     " doctor category code : '" + this.getDoctor().getDoctorCategoryCode() + "'" + 
                                     " 0rder Item Category code : '" + trnDaily.getOrderItemCategoryCode() + "'" + 
                                     " Order Item : '" + this.lOrderItemCode + "'" + 
                                     " Treatment : '" + this.lDoctorTreatmentCode + "'" + 
                                     " Admission Type : '" + this.lAdmissionTypeCode + "'", "", 
                                     TRN_Error.ERROR_TYPE_NOT_SETUP_CONDITION);
                /*
                this.setMessage("Error : Not found condition : " +
                                     " Doctor Code : '" + this.lDoctorCode + "'" +
                                     " doctor category code : '" + this.getDoctor().getDoctorCategoryCode() + "'" + 
                                     " 0rder Item Category code : '" + trnDaily.getOrderItemCategoryCode() + "'" + 
                                     " Order Item : '" + this.lOrderItemCode + "'" + 
                                     " Admission Type : '" + this.lAdmissionTypeCode + "'");
                */
            }

            // Tax Base
            //if (!this.computeTax()) {  ret = false; }

            // this.lDoctorCategoryCode = getDrMethodAllocation().getDoctorCategoryCode();
            this.lExcludeTreatment = methodAllocation.getExcluedTreatment();
            this.lPremiumChargePct = trnDaily.getPremiumChargePct();

            // set variables and update Status to calculated
            if (!this.updateTransDaily()) {  
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                        TRN_Error.PROCESS_DAILY, "", "Update transaction is error."
                        ,"", TRN_Error.ERROR_TYPE_UPDATE_TRNASACTION_ERROR);
                ret = false;  
            }
        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_DAILY," ", 
                    e.getMessage(), "", TRN_Error.ERROR_TYPE_COMPUTE_DAILY_ERROR);
            e.printStackTrace(); ret = false;
        }
        return ret;
    }
    
    private boolean updateTransDaily() {
        boolean ret = false;
        TrnDaily td = this.trnDaily;

        td.setHospitalCode(trnDaily.getHospitalCode());
        td.setInvoiceNo(trnDaily.getInvoiceNo());
        td.setInvoiceDate(trnDaily.getInvoiceDate());
        td.setTransactionDate(trnDaily.getTransactionDate());
        td.setHnNo(trnDaily.getHnNo());
        td.setPatientName(trnDaily.getPatientName());
        td.setEpisodeNo(trnDaily.getEpisodeNo());
        td.setPayorOfficeCode(trnDaily.getPayorOfficeCode());
        td.setPayorOfficeName(trnDaily.getPayorOfficeName());
        td.setTransactionModule(trnDaily.getTransactionModule());
        td.setTransactionType(trnDaily.getTransactionType());
        td.setPayorOfficeCategoryCode(trnDaily.getPayorOfficeCategoryCode());
        td.setPayorOfficeCategoryDescription(trnDaily.getPayorOfficeCategoryDescription());
        td.setIsWriteOff(trnDaily.getIsWriteOff());
        td.setLineNo(trnDaily.getLineNo());
        td.setAdmissionTypeCode(trnDaily.getAdmissionTypeCode());
        td.setNationalityCode(trnDaily.getNationalityCode());
        td.setNationalityDescription(trnDaily.getNationalityDescription());
        td.setPatientDepartmentCode(trnDaily.getPatientDepartmentCode());
        td.setPatientLocationCode(trnDaily.getPatientLocationCode());
        td.setReceiptDepartmentCode(trnDaily.getReceiptDepartmentCode());
        td.setReceiptLocationCode(trnDaily.getReceiptLocationCode());
//        td.setDoctorDepartmentCode(trnDaily.getDoctorDepartmentCode());
        td.setDoctorDepartmentCode(this.getDoctor().getDepartmentCode());
        td.setOrderItemCode(trnDaily.getOrderItemCode());
        td.setOrderItemDescription(trnDaily.getOrderItemDescription());
        td.setDoctorCode(trnDaily.getDoctorCode());
        td.setVerifyDate(trnDaily.getVerifyDate());
        td.setVerifyTime(trnDaily.getVerifyTime());
        td.setDoctorExecuteCode(trnDaily.getDoctorExecuteCode());
        td.setExecuteDate(trnDaily.getExecuteDate());
        td.setExecuteTime(trnDaily.getExecuteTime());
        td.setDoctorResultCode(trnDaily.getDoctorResultCode());
        td.setOldDoctorCode(trnDaily.getDoctorCode());
        td.setReceiptTypeCode(trnDaily.getReceiptTypeCode());
        td.setAmountBefDiscount(trnDaily.getAmountBefDiscount());
        td.setAmountOfDiscount(trnDaily.getAmountOfDiscount());
        td.setAmountAftDiscount(trnDaily.getAmountAftDiscount());
        td.setAmountBefWriteOff(trnDaily.getAmountBefWriteOff());
        td.setInvIsVoid(trnDaily.getInvIsVoid());
        td.setRecIsVoid(trnDaily.getRecIsVoid());
        td.setUpdateDate(JDate.getDate());
        td.setUpdateTime(JDate.getTime());
        td.setUserId(Variables.getUserID());
        td.setInvoiceType(trnDaily.getInvoiceType());
        td.setTotalBillAmount(trnDaily.getTotalBillAmount());
        td.setTotalDrRecAmount(trnDaily.getTotalDrRecAmount());
        td.setOldAmount(trnDaily.getAmountAftDiscount());
        td.setActive(trnDaily.getActive());
        
        td.setTaxTypeCode(this.lTaxTypeCode);
        td.setPremiumRecAmt(this.lTotPremiumAmt);
        td.setNorAllocateAmt(this.lNorAllocateAmt);
        td.setNorAllocatePct(this.lNorAllocatePct);
        td.setDrAmt(this.lDrAmt);
        td.setOldDrAmt(this.lDrAmt);    // keep dr_amt
        td.setDrPremium(this.lDrPremium);
        td.setDrTax400(this.lDrTax400);
        td.setDrTax401(this.lDrTax401);
        td.setDrTax402(this.lDrTax402);
        td.setDrTax406(this.lDrTax406);
        td.setTaxFromAllocate(this.tax_from_allocate);
        td.setHpAmt(this.lHpAmt);
        td.setHpPremium(this.lHpPremium);
        td.setHpTax(this.lHpTax);
        
        td.setPremiumChargePct(this.lPremiumChargePct);
        td.setDoctorCategoryCode(this.lDoctorCategoryCode);
        td.setExcludeTreatment(this.lExcludeTreatment);
        td.setPremiumRecAmt(this.lPremiumRecAmt);
        td.setComputeDailyDate(JDate.getDate());
        td.setComputeDailyTime(JDate.getTime());
        td.setComputeDailyUserID(this.guaranteeMsg+":"+Variables.getUserID());
        
        td.setYyyy(trnDaily.getYyyy());
        td.setMm(trnDaily.getMm());
        td.setReceiptNo(trnDaily.getReceiptNo());
        td.setReceiptDate(trnDaily.getReceiptDate());
        td.setPayByCash(trnDaily.getPayByCash());
        td.setPayByAR(trnDaily.getPayByAR());
        td.setPayByDoctor(trnDaily.getPayByDoctor());
        td.setPayByPayor(trnDaily.getPayByPayor());
        td.setPayByCashAr(trnDaily.getPayByCashAr());
        td.setIsPaid(trnDaily.getIsPaid());
        td.setOrderItemCategoryCode(trnDaily.getOrderItemCategoryCode());
        td.setGuaranteeAmt(this.lguaranteeAmt);
        td.setIsGuaranteeFromAllocate(this.lIsGuaranteeFromAlloc);//UPDATE BY NOP 2011-02-06

        // insert
        ret = true;
        ret = td.update();
        td = null;
        if (!ret) { System.out.println("Error : " + td.getInvoiceNo()); }
        return ret;
    }
	
    public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
}


