package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;
//import java.sql.CallableStatement;
import java.sql.ResultSet;

public class SummaryDaily extends TrnDaily {
//    private String hospitalCode = "";
//    private String invoiceNo = "";
//    private String invoiceDate = "";
//    private String transactionDate = "";
//    private String hnNo = "";
//    private String patientName = "";
//    private String episodeNo = "";
//    private String payorOfficeCode = "";
//    private String payorOfficeName = "";
//    private String payorOfficeCategoryCode = "";
//    private String payorOfficeCategoryDescription = "";
//    private String transactionModule = "";
//    private String isWriteOff = "";
//    private String lineNo = "";
//    private String admissionType = "";
//    private String transactionType = "";
//    private String patientDepartmentCode = "";
//    private String patientLocationCode = "";
//    private String receiptDepartmentCode = "";
//    private String receiptLocationCode = "";
//    private String doctorDepartmentCode = "";
//    private String orderItemCode = "";
//    private String orderItemDescription = "";
//    private String doctorCode = "";
//    private String verifyDate = "";
//    private String verifyTime = "";
//    private String doctorExecuteCode = "";
//    private String executeDate = "";
//    private String executeTime = "";
//    private String doctorResultCode = "";
    private String oldDoctorCode = "";
//    private String receiptTypeCode = "";
////    private Double amountBefDiscount = 0d;
////    private Double amountOfDiscount = 0d;
////    private Double amountAftDiscount = 0d;
////    private Double amountBefWriteOff = 0d;
//    private String invIsVoid = "";
//    private String recIsVoid = "";
//    private String updateDate = "";
//    private String updateTime = "";
//    private String userId = "";
//    private String batchNo = "";
//    private String yyyy = "";
//    private String mm = "";
//    private String dd = "";
    private Double norAllocateAmt;
    private Double norAllocatePct;
    private Double drAmt;
    private Double drTax400;
    private Double drTax401;
    private Double drTax402;
    private Double drTax406;
    private String taxTypeCode;
    private Double drPremium;
    private Double guaranteeAmt;
    private String guaranteeCode;
    private String guaranteeDrCode = "";
    private String guaranteeType = "";
    private String isGuarantee = "";
    private Double hpAmt;
    private Double hpPremium;
    private Double hpTax;
    private String receiptModule = "";
    private String receiptType = "";
    private Double recAmountBefDiscount = 0d;
    private Double recAmountOfDiscount = 0d;
    private Double recPremiumAmt = 0d;
    private String receiptModeCode;
//    private String computeDailyDate;
//    private String computeDailyTime;
//    private String computeDailyUserID;
    private String doctorCategoryCode;
//    private String excludeTreatment;
//    private Double premiumChargePct;
    private Double premiumRecAmt=0d;
//    private String active = "";
//    private String invoiceType = "";
//    private Double totalBillAmount = 0d;
//    private Double totalDrRecAmount = 0d;
    private Double oldAmount = 0d;
//    private String payByCash = "";
//    private String payByAR = "";
//    private String payByDoctor = "";
//    private String payByPayor = "";
//    private String payByCashAr = "";
    
////    private String orderItemIsAllocFullTax = "";
//    static public CallableStatement proc_stmt = null;
//    static public ResultSet resultSet = null;
    
    private String tableName = "SUMMARY_DAILY";
    
    public SummaryDaily() {
        this.setTableName("SUMMARY_DAILY");
    }
    public SummaryDaily(DBConnection conn) {
        super();
        this.setDBConnection(conn);
        this.setTableName("SUMMARY_DAILY");
        // Create an updatable result set
//        String sql = "select * from " + tableName + " where YYYY='0000'";
//        
//        try {
//            if (this.getStatement() == null) { this.setStatement(this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)); }
//            if (this.getResultSet() == null) {this.setResultSet(this.getStatement().executeQuery(sql));}
//        } catch (SQLException ex) {
//            Logger.getLogger(SummaryDaily.class.getName()).log(Level.SEVERE, null, ex);
//        }

        this.setStatement(null);
        this.setResultSet(null);
//        this.setStatement(this.getDBConnection().getStatementForInsert());
    }
    
    public String getDoctorCategoryCode() {
        return doctorCategoryCode;
    }

    public void setDoctorCategoryCode(String doctorCategoryCode) {
        this.doctorCategoryCode = doctorCategoryCode;
    }

    public Double getDrAmt() {
        return drAmt;
    }

    public void setDrAmt(Double drAmt) {
        this.drAmt = drAmt;
    }

    public Double getDrPremium() {
        return drPremium;
    }

    public void setDrPremium(Double drPremium) {
        this.drPremium = drPremium;
    }

    public Double getDrTax400() {
        return drTax400;
    }

    public void setDrTax400(Double drTax400) {
        this.drTax400 = drTax400;
    }

    public Double getDrTax401() {
        return drTax401;
    }

    public void setDrTax401(Double drTax401) {
        this.drTax401 = drTax401;
    }

    public Double getDrTax402() {
        return drTax402;
    }

    public void setDrTax402(Double drTax402) {
        this.drTax402 = drTax402;
    }

    public Double getDrTax406() {
        return drTax406;
    }

    public void setDrTax406(Double drTax406) {
        this.drTax406 = drTax406;
    }

    public Double getGuaranteeAmt() {
        return guaranteeAmt;
    }

    public void setGuaranteeAmt(Double guaranteeAmt) {
        this.guaranteeAmt = guaranteeAmt;
    }

    public String getGuaranteeCode() {
        return guaranteeCode;
    }

    public void setGuaranteeCode(String guaranteeCode) {
        this.guaranteeCode = guaranteeCode;
    }

    public String getGuaranteeDrCode() {
        return guaranteeDrCode;
    }

    public void setGuaranteeDrCode(String guaranteeDrCode) {
        this.guaranteeDrCode = guaranteeDrCode;
    }

    public String getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    public Double getHpAmt() {
        return hpAmt;
    }

    public void setHpAmt(Double hpAmt) {
        this.hpAmt = hpAmt;
    }

    public Double getHpPremium() {
        return hpPremium;
    }

    public void setHpPremium(Double hpPremium) {
        this.hpPremium = hpPremium;
    }

    public Double getHpTax() {
        if (hpTax == null) { hpTax = 0d; }
        return hpTax;
    }

    public void setHpTax(Double hpTax) {
        this.hpTax = hpTax;
    }

    public String getIsGuarantee() {
        return isGuarantee;
    }

    public void setIsGuarantee(String isGuarantee) {
        this.isGuarantee = isGuarantee;
    }
    
    public Double getNorAllocateAmt() {
        return norAllocateAmt;
    }

    public void setNorAllocateAmt(Double norAllocateAmt) {
        this.norAllocateAmt = norAllocateAmt;
    }

    public Double getNorAllocatePct() {
        return norAllocatePct;
    }

    public void setNorAllocatePct(Double norAllocatePct) {
        this.norAllocatePct = norAllocatePct;
    }

    public Double getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(Double oldAmount) {
        this.oldAmount = oldAmount;
    }

    public String getOldDoctorCode() {
        return oldDoctorCode;
    }

    public void setOldDoctorCode(String oldDoctorCode) {
        this.oldDoctorCode = oldDoctorCode;
    }

    public Double getPremiumRecAmt() {
        return premiumRecAmt;
    }

    public void setPremiumRecAmt(Double premiumRecAmt) {
        this.premiumRecAmt = premiumRecAmt;
    }

    public Double getRecAmountBefDiscount() {
        return recAmountBefDiscount;
    }

    public void setRecAmountBefDiscount(Double recAmountBefDiscount) {
        this.recAmountBefDiscount = recAmountBefDiscount;
    }

    public Double getRecAmountOfDiscount() {
        return recAmountOfDiscount;
    }

    public void setRecAmountOfDiscount(Double recAmountOfDiscount) {
        this.recAmountOfDiscount = recAmountOfDiscount;
    }

    public Double getRecPremiumAmt() {
        return recPremiumAmt;
    }

    public void setRecPremiumAmt(Double recPremiumAmt) {
        this.recPremiumAmt = recPremiumAmt;
    }

    public String getReceiptModeCode() {
        return receiptModeCode;
    }

    public void setReceiptModeCode(String receiptModeCode) {
        this.receiptModeCode = receiptModeCode;
    }

    public String getReceiptModule() {
        return receiptModule;
    }

    public void setReceiptModule(String receiptModule) {
        this.receiptModule = receiptModule;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getTaxTypeCode() {
        return taxTypeCode;
    }

    public void setTaxTypeCode(String taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }
    
    public boolean insert() {
        boolean ret = false;
        String sql = "select * from " + tableName + " where YYYY='0000'";
        try {
            // Create an updatable result set
            if (this.getStatement() == null) { this.setStatement(this.getDBConnection().getStatementForInsert()); } //this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)); }
            if (this.getResultSet() == null) {this.setResultSet(this.getStatement().executeQuery(sql));}

            // Move cursor to the "insert row"
            this.getResultSet().moveToInsertRow();
            
            this.getResultSet().updateString("HOSPITAL_CODE", this.getHospitalCode());
            this.getResultSet().updateString("INVOICE_NO", this.getInvoiceNo());
            this.getResultSet().updateString("INVOICE_DATE", this.getInvoiceDate());
            this.getResultSet().updateString("TRANSACTION_DATE", this.getTransactionDate());
            this.getResultSet().updateString("HN_NO", this.getHnNo());
            this.getResultSet().updateString("PATIENT_NAME", this.getPatientName());
            this.getResultSet().updateString("EPISODE_NO", this.getEpisodeNo());
            this.getResultSet().updateString("PAYOR_OFFICE_CODE", this.getPayorOfficeCode());
            this.getResultSet().updateString("PAYOR_OFFICE_NAME", this.getPayorOfficeName());
            this.getResultSet().updateString("TRANSACTION_MODULE", this.getTransactionModule());
            this.getResultSet().updateString("TRANSACTION_TYPE", this.getTransactionType());
            this.getResultSet().updateString("PAYOR_OFFICE_CATEGORY_CODE", this.getPayorOfficeCategoryCode());
            this.getResultSet().updateString("PAYOR_OFFICE_CATEGORY_DESCRIPTION", this.getPayorOfficeCategoryDescription());
            this.getResultSet().updateString("IS_WRITE_OFF", this.getIsWriteOff());
            this.getResultSet().updateString("LINE_NO", this.getLineNo());
            this.getResultSet().updateString("ADMISSION_TYPE_CODE", this.getAdmissionTypeCode());
            this.getResultSet().updateString("NATIONALITY_CODE", this.getNationalityCode());
            this.getResultSet().updateString("NATIONALITY_DESCRIPTION", this.getNationalityDescription());
            this.getResultSet().updateString("PATIENT_DEPARTMENT_CODE", this.getPatientDepartmentCode());
            this.getResultSet().updateString("PATIENT_LOCATION_CODE", this.getPatientLocationCode());
            this.getResultSet().updateString("RECEIPT_DEPARTMENT_CODE", this.getReceiptDepartmentCode());
            this.getResultSet().updateString("RECEIPT_LOCATION_CODE", this.getReceiptLocationCode());
            this.getResultSet().updateString("DOCTOR_DEPARTMENT_CODE", this.getAdmissionTypeCode());
            this.getResultSet().updateString("ORDER_ITEM_CODE", this.getOrderItemCode());
            this.getResultSet().updateString("ORDER_ITEM_DESCRIPTION", this.getOrderItemDescription());
            this.getResultSet().updateString("DOCTOR_CODE", this.getDoctorCode());
            this.getResultSet().updateString("VERIFY_DATE", this.getVerifyDate());
            this.getResultSet().updateString("VERIFY_TIME", this.getVerifyTime());
            this.getResultSet().updateString("DOCTOR_EXECUTE_CODE", this.getDoctorExecuteCode());
            this.getResultSet().updateString("EXECUTE_DATE", this.getVerifyDate());
            this.getResultSet().updateString("EXECUTE_TIME", this.getVerifyTime());
            this.getResultSet().updateString("DOCTOR_RESULT_CODE", this.getDoctorResultCode());
            this.getResultSet().updateString("OLD_DOCTOR_CODE", this.getOldDoctorCode());
            this.getResultSet().updateString("RECEIPT_TYPE_CODE", this.getReceiptTypeCode());
            this.getResultSet().updateDouble("AMOUNT_BEF_DISCOUNT", this.getAmountBefDiscount());
            this.getResultSet().updateDouble("AMOUNT_OF_DISCOUNT", this.getAmountOfDiscount());
            this.getResultSet().updateDouble("AMOUNT_AFT_DISCOUNT", this.getAmountAftDiscount());
            this.getResultSet().updateDouble("AMOUNT_BEF_WRITE_OFF", this.getAmountBefWriteOff());
            this.getResultSet().updateString("INV_IS_VOID", this.getInvIsVoid());
            this.getResultSet().updateString("REC_IS_VOID", this.getRecIsVoid());
            this.getResultSet().updateString("INVOICE_TYPE", this.getInvoiceType());
            this.getResultSet().updateDouble("TOTAL_BILL_AMOUNT", this.getTotalBillAmount());
            this.getResultSet().updateDouble("TOTAL_DR_REC_AMOUNT", this.getTotalDrRecAmount());
            this.getResultSet().updateDouble("OLD_AMOUNT", this.getOldAmount());
            
            this.getResultSet().updateString("UPDATE_DATE", this.getUpdateDate());
            this.getResultSet().updateString("UPDATE_TIME", this.getUpdateTime());
            this.getResultSet().updateString("USER_ID", this.getUserId());
            this.getResultSet().updateString("BATCH_NO", this.getBatchNo());
            
            
            
            this.getResultSet().updateDouble("NOR_ALLOCATE_AMT", this.getNorAllocateAmt());
            this.getResultSet().updateDouble("NOR_ALLOCATE_PCT", this.getNorAllocatePct());
            this.getResultSet().updateDouble("DR_AMT", this.getDrAmt());
            this.getResultSet().updateDouble("DR_TAX_400", this.getDrTax400());
            this.getResultSet().updateDouble("DR_TAX_401", this.getDrTax401());
            this.getResultSet().updateDouble("DR_TAX_402", this.getDrTax402());
            this.getResultSet().updateDouble("DR_TAX_406", this.getDrTax406());
            this.getResultSet().updateDouble("DR_PREMIUM", this.getDrPremium());
            this.getResultSet().updateDouble("HP_AMT", this.getHpAmt());
            this.getResultSet().updateDouble("HP_PREMIUM", this.getHpPremium());
            this.getResultSet().updateDouble("HP_TAX", this.getHpTax());
            this.getResultSet().updateString("TAX_TYPE_CODE", this.getTaxTypeCode());
            
            
            this.getResultSet().updateDouble("PREMIUM_REC_AMT", this.getPremiumRecAmt());
            this.getResultSet().updateDouble("PREMIUM_CHARGE_PCT", this.getPremiumChargePct());
            this.getResultSet().updateString("DOCTOR_CATEGORY_CODE", this.getDoctorCategoryCode());
            this.getResultSet().updateString("EXCLUDE_TREATMENT", this.getExcludeTreatment());
            this.getResultSet().updateDouble("PREMIUM_REC_AMT", this.getPremiumRecAmt());
            
            // update when invoice have RECEIPT_NO
            this.getResultSet().updateString("YYYY", this.getYyyy());
            this.getResultSet().updateString("MM", this.getMm());
            this.getResultSet().updateString("RECEIPT_NO", this.getReceiptNo());
            this.getResultSet().updateString("RECEIPT_DATE", this.getReceiptDate());
            this.getResultSet().updateString("PAY_BY_CASH", this.getPayByCash());
            this.getResultSet().updateString("PAY_BY_AR", this.getPayByAR());
            this.getResultSet().updateString("PAY_BY_DOCTOR", this.getPayByDoctor());
            this.getResultSet().updateString("PAY_BY_PAYOR", this.getPayByPayor());
            this.getResultSet().updateString("PAY_BY_CASH_AR", this.getPayByCashAr());

            this.getResultSet().updateString("COMPUTE_DAILY_DATE", this.getComputeDailyDate());
            this.getResultSet().updateString("COMPUTE_DAILY_TIME", this.getComputeDailyTime());
            this.getResultSet().updateString("COMPUTE_DAILY_USER_ID", this.getComputeDailyUserID());
            
            // update when compute monthly            
            this.getResultSet().updateString("ACTIVE", this.getActive());
            
            // Insert the row
            this.getResultSet().insertRow();
            ret=true;
        } catch (SQLException e) {
            e.printStackTrace();
            ret = false;TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
        }
        finally {

                    }
        return ret;
    }

    
}
