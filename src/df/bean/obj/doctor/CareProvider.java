package df.bean.obj.doctor;

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


public class CareProvider {
    private Doctor doctor = null;
    private ResultSet rs = null;
    private Statement statement = null;
    // use for compute daily
    private TrnDaily trnDaily = null;
    private DrMethodAllocation drMethodAllocation = null;
    private Batch b = null;
    InvoiceDetail invoiceDetail = null;
    DBConnection conn = null;
    
    public static SummaryMonthlyVar smVar = null;

    private String doctorCode;
    private String className;
    private String hospitalCode;
    private String invoiceNo;
    private String treatmentType;
    private String lineNo;
    private String receiptDate;
    private String user;
    
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
    
    public CareProvider() {
    }
    
    public CareProvider(DBConnection conn) {
        this.setDBConnection(conn);
//        sd = new SummaryDaily(this.getDBConnection());
    }
    public DBConnection getDBConnection() {
        return this.conn;
    }
    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    
//    @Override
//    protected void finalize() {
//        doctor = null;
//        rs = null;
//        setDrMethodAllocation(null);
//        invoiceDetail = null;
//    }
    
    public CareProvider(String doctorCode, String hospitalCode, DBConnection conn) {
        this.setDoctorCode(doctorCode);
        this.setHospitalCode(hospitalCode);
        
        this.setDBConnection(conn);
        this.newDoctor();
    }

    // new Doctor
    public void newDoctor() {
        //doctor = new Doctor(this.getDoctorCode(), this.getHospitalCode(), this.getDBConnection());      
        doctor = new Doctor();
    }
    
    public void setUser(String a){
    	this.user = a;
    }
    
    public Doctor getDoctor() {
        return this.doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public String getlineNo() {
        return lineNo;
    }

    public void setlineNo(String lineNo) {
        this.lineNo = lineNo;
    }
    
    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }
    
    public void setTrnDaily(TrnDaily trnDaily) {
        this.trnDaily = trnDaily;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public SummaryMonthly getSummaryMonthly() {
//        return summaryMonthly;
//    }
//
//    public void setSummaryMonthly(SummaryMonthly summaryMonthly) {
//        this.summaryMonthly = summaryMonthly;
//    }

    // คำนวณหาค่า Method Allocation
    protected boolean computeMethodAllocation() {
        try {
            double amountAftDiscount = lInvAmt;
            double normalAllocatePct = getDrMethodAllocation().getMethodAllocation().getNormalAllocatePct();
            double normalAllocateAmt = getDrMethodAllocation().getMethodAllocation().getNormalAllocateAmt();
            double normalAllocatePrice = getDrMethodAllocation().getMethodAllocation().getPrice();
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

            if (getDrMethodAllocation().getMethodAllocation().getGuaranteeSource().equalsIgnoreCase("DF")) {
                this.lguaranteeAmt = 0.00;
                this.lIsGuaranteeFromAlloc = "D";
                this.guaranteeMsg = "ALLOC";
                this.tax_from_allocate = "Y";
            } else if(getDrMethodAllocation().getMethodAllocation().getGuaranteeSource().equalsIgnoreCase("FULL")) {
                this.lguaranteeAmt = 0.00;
                this.lIsGuaranteeFromAlloc = "F";
                this.guaranteeMsg = "ALLOC";
                this.tax_from_allocate = "N";
            } else{
                this.guaranteeMsg = "";
                this.lIsGuaranteeFromAlloc = "";
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
    private boolean computeTax() {
        try {
        // วิธีคำนวณแบบมีการแบ่งหัตถการ (แบบที่ 1)
        this.lDrTax400 = 0d;
        this.lDrTax401 = 0d;
        this.lDrTax402 = 0d;
        this.lDrTax406 = 0d;
        this.lHpTax = 0d;
        double taxAmt = 0d;
//        OrderItem oi = new OrderItem(this.lOrderItemCode, this.hospitalCode, this.getDBConnection());
//        if (oi.getCode() == null) {
        if ((this.trnDaily.getOrderItemCode() == null) || (this.trnDaily.getOrderItemCode().equalsIgnoreCase("")))  {
            System.out.print("Error : CareProvider.computetax() : Order Item '"+ this.lOrderItemCode + "' is not found");
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_DAILY, 
                                "Error : CareProvider.computetax() : Order Item '"+ this.lOrderItemCode + "' is not found", "", 
                                TRN_Error.ERROR_TYPE_NO_ORDER_ITEM);
//            DialogBox.ShowError("Order Item Code : " + this.lOrderItemCode + " is not found. ");
            return false;
        }
        
        if ((this.trnDaily.getOrderItemIsAllocFullTax() == null) || (this.trnDaily.getOrderItemIsAllocFullTax().equalsIgnoreCase(""))) { this.trnDaily.setOrderItemIsAllocFullTax(OrderItem.ALLOC_FULL_TAX_YES); }
        if (this.trnDaily.getOrderItemIsAllocFullTax().equals(OrderItem.ALLOC_FULL_TAX_NO)) {       // =N : คิดภาษีจากยอดเต็ม
            taxAmt = this.lDrAmt; // กรณีที่ฐานภาษีคิดจากยอดที่แพทย์ได้จริง
            this.lHpTax = this.lHpAmt;
        } else {                            // =Y : 
            taxAmt = this.lInvAmt;//กรณีที่ฐานภาษีคิดจากยอดหักส่วนลดแล้ว + this.lInvDiscountAmt; // กรณีที่ฐานภาษีคิดจากยอดทั้งหมด
            this.lHpTax = 0d;
        }
//        oi = null;
        // ภาษีจะคำนวณจาก ถ้าเป็น หัตถการ ฐานภาษีจะเท่ากับ ส่วนแบ่งที่หมอได้รับ
        // ถ้าไม่ใช่ หัตถการ ฐานภาษีจะเท่ากับ ยอดทั้งหมด หลังจากหักส่วนลดแล้ว  แต่ยังไม่หักค่า premium
        // if get from method allocation
        this.lTaxTypeCode = getDrMethodAllocation().getMethodAllocation().getTaxTypeCode();
        if (getDrMethodAllocation().getMethodAllocation().getNormalAllocatePct() > 100 ) {
            taxAmt = this.lDrAmt;
            this.lHpTax = 0d;
        }
        if (!this.trnDaily.getIsCompute().equalsIgnoreCase("Y")) { taxAmt = 0; this.lHpTax = this.lInvAmt; }
        if (this.lDrAmt <= 0) { taxAmt = 0; this.lHpTax = this.lInvAmt; } // เพิ่มเพื่อให้ภาษีเป็น 0 เมื่อแพทย์ไม่ได้รับส่วนแบ่ง
        if (this.lTaxTypeCode.equals("400")) { this.lDrTax400 = taxAmt; }            
        if (this.lTaxTypeCode.equals("401")) { this.lDrTax401 = taxAmt; }
        if (this.lTaxTypeCode.equals("402")) { this.lDrTax402 = taxAmt; }
        if (this.lTaxTypeCode.equals("406")) { this.lDrTax406 = taxAmt; }
        
        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_DAILY, "Compute Tax is Error", e.getMessage(), "",
                    TRN_Error.ERROR_TYPE_COMPUTE_TAX_ERROR);
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    // คำนวณหาค่า Premium
    protected boolean computePremium() {
        try {
            // Premium 
            double pctOfCharge = 0, pctOfVat = 0, drPremium = 0, hpPremium = 0, totOfPremium = 0;
            
            // if patient was paid by credit card 
            pctOfCharge = this.lPremiumChargePct / 100;
            totOfPremium = (this.lInvAmt * (pctOfCharge + (pctOfCharge * pctOfVat)));
            drPremium = (this.lDrAmt * (pctOfCharge + (pctOfCharge * pctOfVat)));

            this.lDrPremium = Double.parseDouble((new java.text.DecimalFormat("0.00")).format(drPremium));
            this.lTotPremiumAmt = Double.parseDouble((new java.text.DecimalFormat("0.00")).format(totOfPremium));
            hpPremium = this.lTotPremiumAmt - this.lDrPremium;
            this.lHpPremium = Double.parseDouble((new java.text.DecimalFormat("0.00")).format(hpPremium));
            this.lPremiumRecAmt = this.lRecAmt * this.lPremiumChargePct / 100; 
        } catch (Exception e) {
            e.printStackTrace();
            DialogBox.ShowError(this.lInvNo);
            
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_DAILY, "Compute Premium is Error", e.getMessage(), "", 
                    TRN_Error.ERROR_TYPE_COMPUTE_PREMIUM_ERROR);
            return false;
        }
        return true;
    }
    
    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }
    
    public boolean computeTax406(String dd, String mm, String yyyy) {
        SummaryTax406 st = new SummaryTax406(this.getDBConnection());
        String mm1 = "01";
        if (mm.equals("12")) { mm1 = "07"; }
        
        try {
               String sql = "select DOCTOR_CODE, sum(SUM_AMT) as sAmt, sum(DR_NET_PAID_AMT) as sDrAmt, sum(HP_SUM_AMT) as sHpAmt,";
               sql = sql + " sum(DR_NET_TAX_406_AMT) as sDrTax, sum(HP_TAX) as sHpTax";
               sql = sql + " from PAYMENT_MONTHLY";
               sql = sql + " where (TAX_406_DATE >= '" + yyyy.concat(mm1.concat("00")) + "'";
               sql = sql + " and TAX_406_DATE <= '" + yyyy.concat(mm.concat(dd)) + "')";
               sql = sql + " and DOCTOR_CODE = '" + this.getDoctorCode() + "'";
               sql = sql + " group by DOCTOR_CODE";

                if (this.getStatement() == null) { this.setStatement(this.getDBConnection().getConnection().createStatement()); }
                rs = this.getStatement().executeQuery(sql);
                
                while (rs.next()) {
//                        values.clear();
                    // call for compute summary monthly
                    

                    // insert into SUMMARY_MONTHLY table
                    st.setHospitalCode(this.getHospitalCode());
                    st.setDoctorCode(this.getDoctorCode());
                    st.setDd(dd);
                    st.setMm(mm);
                    st.setYyyy(yyyy);
                    st.setSumAmt(rs.getString("sAmt"));
                    st.setSumDrAmt(rs.getString("sDrAmt"));
                    st.setSumHpAmt(rs.getDouble("sHpAmt"));
                    st.setSumTaxDrAmt(rs.getString("sDrTax"));
                    st.setSumTaxHpAmt(rs.getDouble("sHpTax"));
                    st.setTextSumDrAmt(Utils.toThaiMoney(rs.getDouble("sDrAmt")));
                    st.setTextSumTaxDrAmt(Utils.toThaiMoney(rs.getDouble("sDrTax")));
                    st.setPrintDate(yyyy+mm+dd);
                    st.setUpdateDate(JDate.getDate());
                    st.setUpdateTime(JDate.getTime());
                    st.setUserId(Variables.getUserID());

                    if (!st.insert()) { st = null;   return false;  }

                }
        } catch (Exception e) {
            e.printStackTrace();
            this.setMessage("Calculate tax 4(6) is error.");
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_DAILY, "Calculate tax 4(6) is error.", e.getMessage());
//            DialogBox.ShowError("Error in CareProvider.computeSummaryTax406() \n" + e.getMessage());
            return false;
        } finally {
               //Clean up resources, close the connection.
            st = null;
                try {
                    if(rs != null) {
                        rs.close();
                        rs = null;
                    }
//                    if (stmt != null) {
//                        stmt.close();
//                        stmt = null;
//                    }
                } catch (Exception ignored) { ignored.printStackTrace();   }
            }

        return true;
    }

    public boolean computeTax406(String startDate, String endDate, String MM, String YYYY, String hospital_code) {
        SummaryTax406 st = new SummaryTax406(this.getDBConnection());
        String yyyy = YYYY; //endDate.substring(0, 4);
        String mm = MM; //endDate.substring(4, 6);
        String dd = endDate.substring(6, 8);
        
        try {
               String sql = "select DR.DOCTOR_TAX_CODE, sum(SUM_AMT) as sAmt, sum(DR_NET_PAID_AMT) as sDrAmt, sum(HP_SUM_AMT) as sHpAmt,";
               sql = sql + " sum((SUM_DF_TAX_406+EXDR_406)-EXCR_406) as sDrTax, '0' as sHpTax";
               sql = sql + " from SUMMARY_PAYMENT PM LEFT OUTER JOIN DOCTOR DR ON PM.DOCTOR_CODE = DR.CODE AND PM.HOSPITAL_CODE = DR.HOSPITAL_CODE";
               sql = sql + " where (PAYMENT_DATE >= '" + startDate + "'";
               sql = sql + " and PAYMENT_DATE <= '" + endDate + "')";
               sql = sql + " and PM.HOSPITAL_CODE = '" + hospital_code + "'";
               //sql = sql + " and DR.DOCTOR_TAX_CODE = '" + this.getDoctorCode() + "'";
               sql = sql + " and EXISTS (SELECT DOCTOR_TAX_CODE FROM DOCTOR WHERE HOSPITAL_CODE = '"+ hospital_code +"' AND CODE = '" + this.getDoctorCode() +
            		       "' AND DR.DOCTOR_TAX_CODE = DOCTOR_TAX_CODE)";
               sql = sql + " group by DR.DOCTOR_TAX_CODE";

                if (this.getStatement() == null) { this.setStatement(this.getDBConnection().getConnection().createStatement()); }
                rs = this.getStatement().executeQuery(sql);
                
                while (rs.next()) {
                	// values.clear();
                    // call for compute summary monthly
                    // insert into SUMMARY_MONTHLY table
                    st.setHospitalCode(this.getHospitalCode());
                    st.setDoctorCode(this.getDoctorCode());
                    st.setDd(dd);
                    st.setMm(mm);
                    st.setYyyy(yyyy);
                    st.setSumAmt(rs.getString("sAmt"));
                    st.setSumDrAmt(rs.getString("sDrAmt"));
                    st.setSumHpAmt(rs.getDouble("sHpAmt"));
                    st.setSumTaxDrAmt(rs.getString("sDrTax"));
                    st.setSumTaxHpAmt(rs.getDouble("sHpTax"));
/*
                    st.setSumAmt(rs.getDouble("sAmt"));
                    st.setSumDrAmt(rs.getDouble("sDrAmt"));
                    st.setSumHpAmt(rs.getDouble("sHpAmt"));
                    st.setSumTaxDrAmt(rs.getDouble("sDrTax"));
                    st.setSumTaxHpAmt(rs.getDouble("sHpTax"));
*/
                    st.setTextSumDrAmt(Utils.toThaiMoney(rs.getString("sDrAmt")));
                    st.setTextSumTaxDrAmt(Utils.toThaiMoney(rs.getString("sDrTax")));
                    st.setPrintDate("");
                    st.setUpdateDate(JDate.getDate());
                    st.setUpdateTime(JDate.getTime());
                    st.setUserId("");
                    //st.setUserId(Variables.getUserID());
                    if (!st.insert()) { st = null;   return false;  }
                }
        } catch (Exception e) {
            e.printStackTrace();
            this.setMessage("Calculate tax 4(6) is error.");
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_DAILY, "Calculate tax 4(6) is error.", e.getMessage());
//            DialogBox.ShowError("Error in CareProvider.computeSummaryTax406() \n" + e.getMessage());
            return false;
        } finally {
               //Clean up resources, close the connection.
            st = null;
                try {
                    if(rs != null) {
                        rs.close();
                        rs = null;
                    }
//                    if (stmt != null) {
//                        stmt.close();
//                        stmt = null;
//                    }
                } catch (Exception ignored) { ignored.printStackTrace();   }
            }

        return true;
    }

    public DrMethodAllocation getDrMethodAllocation() {
        return drMethodAllocation;
    }

    public void setDrMethodAllocation(DrMethodAllocation drMethodAllocation) {
        this.drMethodAllocation = drMethodAllocation;
    }
    
    // Compute all of care provider
    public boolean computeTransDaily() throws SQLException {
    	//System.out.println("Compute CareProvider");
        boolean ret = true;
        try {
            if (this.getDrMethodAllocation() == null) {
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
                // find on table Personal
                this.getDrMethodAllocation().useMethodAllocationPersonal();
                if (getDrMethodAllocation().getMethodAllocation().IsFound(this.lOrderItemCode, this.lDoctorTreatmentCode, this.lDoctorCode, this.lAdmissionTypeCode, this.lInvAmt)) { } else {
                    if (getDrMethodAllocation().getMethodAllocation().IsFound(this.lOrderItemCode, this.lDoctorTreatmentCode, this.lDoctorCode, this.lAdmissionTypeCode, 0d)) { } else {
                        
                        // find on table item
                        this.getDrMethodAllocation().useMethodAllocationNormal();
                        // check method allocation is found
                        if (getDrMethodAllocation().getMethodAllocation().IsFound(this.lOrderItemCode, this.lDoctorTreatmentCode, this.lDoctorCategoryCode, this.lAdmissionTypeCode, this.lInvAmt)) { } else {
                            if (getDrMethodAllocation().getMethodAllocation().IsFound(this.lOrderItemCode, this.lDoctorTreatmentCode, this.lDoctorCategoryCode, this.lAdmissionTypeCode, 0d)) { } else {
                                
                                // find on table item category
                                this.getDrMethodAllocation().useMethodAllocItemCategory();
                                if (getDrMethodAllocation().getMethodAllocation().IsFound(trnDaily.getOrderItemCategoryCode(), this.lDoctorTreatmentCode, this.lDoctorCategoryCode, this.lAdmissionTypeCode, this.lInvAmt)) { } else {
                                    if (getDrMethodAllocation().getMethodAllocation().IsFound(trnDaily.getOrderItemCategoryCode(), this.lDoctorTreatmentCode, this.lDoctorCategoryCode, this.lAdmissionTypeCode, 0d)) { } else {
                                
                                        // find on table Master
                                        this.getDrMethodAllocation().useMethodAllocationMaster();
                                        if (!getDrMethodAllocation().getMethodAllocation().IsFound(this.lOrderItemCode, this.lDoctorTreatmentCode, this.lDoctorCategoryCode, this.lAdmissionTypeCode)) {
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
                                            this.setMessage("Error : Not found condition : " +
                                                                 " Doctor Code : '" + this.lDoctorCode + "'" +
                                                                 " doctor category code : '" + this.getDoctor().getDoctorCategoryCode() + "'" + 
                                                                 " 0rder Item Category code : '" + trnDaily.getOrderItemCategoryCode() + "'" + 
                                                                 " Order Item : '" + this.lOrderItemCode + "'" + 
                                                                 " Admission Type : '" + this.lAdmissionTypeCode + "'");
                                            ////break;
                                        }
                                    } // ตรวจสอบการคำนวณด้วยราคา 0 (หมายถึงไม่ระบุราคา) (Item Category)
                                }   // ตรวจสอบการคำนวณด้วยราคา (Item Category)
                                
                            }   // ตรวจสอบการคำนวณด้วยราคา 0 (หมายถึงไม่ระบุราคา)
                        }   // ตรวจสอบการคำนวณด้วยราคา
                    }   // ตรวจสอบการคำนวณด้วยราคา 0 (หมายถึงไม่ระบุราคา)
                }   // ตรวจสอบการคำนวณด้วยราคา

                // Method Allocation
                this.computeMethodAllocation();

                // Tax Base
                if (!this.computeTax()) {  ret = false; }

                // this.lDoctorCategoryCode = getDrMethodAllocation().getDoctorCategoryCode();
                this.lExcludeTreatment = getDrMethodAllocation().getMethodAllocation().getExcludeTreatment();
                this.lPremiumChargePct = trnDaily.getPremiumChargePct();
                this.computePremium();

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
        td.setUserId(this.user);
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
        td.setComputeDailyUserID(this.guaranteeMsg+":"+this.user);
        
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
}


