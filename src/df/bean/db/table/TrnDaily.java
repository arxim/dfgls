package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrnDaily extends ABSTable {
    private String hospitalCode = "";
    private String invoiceNo = "";
    private String invoiceDate = "";
    private String transactionDate = "";
    private String hnNo = "";
    private String patientName = "";
    private String episodeNo = "";
    private String nationalityCode = "";
    private String nationalityDescription = "";
    private String payorOfficeCode = "";
    private String payorOfficeName = "";
    private String payorOfficeCategoryCode = "";
    private String payorOfficeCategoryDescription = "";
    private String transactionModule = "";
    private String isWriteOff = "";
    private String lineNo = "";
    private String admissionTypeCode = "";
    private String transactionType = "";
    private String patientDepartmentCode = "";
    private String patientLocationCode = "";
    private String receiptDepartmentCode = "";
    private String receiptLocationCode = "";
    private String doctorDepartmentCode = "";
    private String orderItemCode = "";
    private String orderItemDescription = "";
    private String doctorCode = "";
    private String verifyDate = "";
    private String verifyTime = "";
    private String doctorExecuteCode = "";
    private String executeDate = "";
    private String executeTime = "";
    private String doctorResultCode = "";
    private String oldDoctorCode = "";
    private String receiptTypeCode = "";
    private Double amountBefDiscount = 0d;
    private Double amountOfDiscount = 0d;
    private Double amountAftDiscount = 0d;
    private Double amountBefWriteOff = 0d;
    private String invIsVoid = "";
    private String recIsVoid = "";
    private String updateDate = "";
    private String updateTime = "";
    private String userId = "";
    private String batchNo = "";
    private String active = "";
    private String computeDailyDate;
    private String computeDailyTime;
    private String computeDailyUserID;
    private String invoiceType = "";
    
    // receipt
    private String yyyy = "";
    private String mm = "";
    private String payByCash = "";
    private String payByAR = "";
    private String payByDoctor = "";
    private String payByPayor = "";
    private String receiptNo = "";
    private String receiptDate = "";
    private String payByCashAr = "";
    private String isPaid = "";
    
    private String dd = "";
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
    private String doctorCategoryCode;
    private Double premiumRecAmt=0d;
    private Double oldAmount = 0d;
    
    private String excludeTreatment;
    private Double premiumChargePct;
    private Double totalBillAmount = 0d;
    private Double totalDrRecAmount = 0d;
    private String orderItemCategoryCode = "";
    private Double oldDrAmt = 0d;
    private String isCompute = "";
    private String taxFromAllocate = "N";
    private String tableName = "TRN_DAILY";
    private String isGuaranteeFromAllocate = "";
	private String orderItemIsAllocFullTax = "";
//    static public CallableStatement proc_stmt = null;
    static final public String TRANSACTION_MODULE_TR = "TR";
    static final public String TRANSACTION_MODULE_AR = "AR";
    static final public String INVOICE_TYPE_EXECUTE = "EXECUTE";
    static final public String INVOICE_TYPE_ORDER = "ORDER";
    static final public String INVOICE_TYPE_RESULT = "RESULT";

    
    public static String getSQL_TRN_DAILY(String startDate, String endDate, String hospitalCode) {
        return "SELECT TD.HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, TRANSACTION_DATE, HN_NO, PATIENT_NAME, EPISODE_NO, PAYOR_OFFICE_CODE, PAYOR_OFFICE_NAME, "
        + " PAYOR_OFFICE_CATEGORY_CODE, PAYOR_OFFICE_CATEGORY_DESCRIPTION, TRANSACTION_MODULE, IS_WRITE_OFF, LINE_NO, ADMISSION_TYPE_CODE, " 
        + " TRANSACTION_TYPE, NATIONALITY_CODE, NATIONALITY_DESCRIPTION, "
        + " PATIENT_DEPARTMENT_CODE, PATIENT_LOCATION_CODE, RECEIPT_DEPARTMENT_CODE, RECEIPT_LOCATION_CODE, " 
        + " DOCTOR_DEPARTMENT_CODE, ORDER_ITEM_CODE, ORDER_ITEM_DESCRIPTION, DOCTOR_CODE, VERIFY_DATE, VERIFY_TIME, DOCTOR_EXECUTE_CODE, " 
        + " EXECUTE_DATE, EXECUTE_TIME, DOCTOR_RESULT_CODE, RECEIPT_TYPE_CODE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, " 
        + " AMOUNT_AFT_DISCOUNT, AMOUNT_BEF_WRITE_OFF, " 
        + " NOR_ALLOCATE_AMT, NOR_ALLOCATE_PCT, " 
        + " IS_GUARANTEE_FROM_ALLOC, "//NEW BY NOP 2011-02-06
        + " COMPUTE_DAILY_DATE, COMPUTE_DAILY_TIME, COMPUTE_DAILY_USER_ID, TAX_FROM_ALLOCATE, " 
        + " TD.ACTIVE as ACTIVE, INVOICE_TYPE, TOTAL_BILL_AMOUNT, TOTAL_DR_REC_AMOUNT, " 
        + " INV_IS_VOID, REC_IS_VOID, " 
        + " YYYY, MM, RECEIPT_NO, RECEIPT_DATE, PAY_BY_CASH, PAY_BY_AR, PAY_BY_DOCTOR, PAY_BY_PAYOR, PAY_BY_CASH_AR, TD.IS_PAID AS IS_PAID, "  // FOR RECEIPT 
        + " RT.PERCENT_OF_CHARGE AS PREMIUM_CHARGE_PCT, "
        + " OI.IS_ALLOC_FULL_TAX AS IS_ALLOC_FULL_TAX, OI.EXCLUDE_TREATMENT AS EXCLUDE_TREATMENT, OI.ORDER_ITEM_CATEGORY_CODE AS ORDER_ITEM_CATEGORY_CODE, "
        + " OI.IS_COMPUTE AS IS_COMPUTE, GUARANTEE_AMT "
        + " FROM TRN_DAILY TD INNER JOIN RECEIPT_TYPE RT ON TD.RECEIPT_TYPE_CODE = RT.CODE AND TD.HOSPITAL_CODE = RT.HOSPITAL_CODE "
        + " LEFT JOIN ORDER_ITEM OI ON TD.ORDER_ITEM_CODE = OI.CODE AND TD.HOSPITAL_CODE = OI.HOSPITAL_CODE"
        + " WHERE (TRANSACTION_DATE >='" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "')"
        + " AND TD.HOSPITAL_CODE = '" + hospitalCode + "'"
        + " AND (COMPUTE_DAILY_DATE = '' OR COMPUTE_DAILY_DATE IS NULL)"
        + " AND (BATCH_NO IS NULL OR BATCH_NO = '')"
        + " AND (INVOICE_TYPE = '" + INVOICE_TYPE_EXECUTE + "' OR INVOICE_TYPE = '" + INVOICE_TYPE_RESULT + "')"  
        + " order by INVOICE_NO, LINE_NO ";
    }
    public static String getSQL_TRN_DAILY(String startDate, String endDate, String hospitalCode, String invoiceNo) {
        return "SELECT TD.HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, TRANSACTION_DATE, HN_NO, PATIENT_NAME, EPISODE_NO, PAYOR_OFFICE_CODE, PAYOR_OFFICE_NAME, "
        + " PAYOR_OFFICE_CATEGORY_CODE, PAYOR_OFFICE_CATEGORY_DESCRIPTION, TRANSACTION_MODULE, IS_WRITE_OFF, LINE_NO, ADMISSION_TYPE_CODE, " 
        + " TRANSACTION_TYPE, NATIONALITY_CODE, NATIONALITY_DESCRIPTION, "
        + " PATIENT_DEPARTMENT_CODE, PATIENT_LOCATION_CODE, RECEIPT_DEPARTMENT_CODE, RECEIPT_LOCATION_CODE, " 
        + " DOCTOR_DEPARTMENT_CODE, ORDER_ITEM_CODE, ORDER_ITEM_DESCRIPTION, DOCTOR_CODE, VERIFY_DATE, VERIFY_TIME, DOCTOR_EXECUTE_CODE, " 
        + " EXECUTE_DATE, EXECUTE_TIME, DOCTOR_RESULT_CODE, RECEIPT_TYPE_CODE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, " 
        + " AMOUNT_AFT_DISCOUNT, AMOUNT_BEF_WRITE_OFF, " 
        + " NOR_ALLOCATE_AMT, NOR_ALLOCATE_PCT, " 
        + " IS_GUARANTEE_FROM_ALLOC, "//NEW BY NOP 2011-02-06
        + " COMPUTE_DAILY_DATE, COMPUTE_DAILY_TIME, COMPUTE_DAILY_USER_ID, TAX_FROM_ALLOCATE, " 
        + " TD.ACTIVE as ACTIVE, INVOICE_TYPE, TOTAL_BILL_AMOUNT, TOTAL_DR_REC_AMOUNT, " 
        + " INV_IS_VOID, REC_IS_VOID, " 
        + " YYYY, MM, RECEIPT_NO, RECEIPT_DATE, PAY_BY_CASH, PAY_BY_AR, PAY_BY_DOCTOR, PAY_BY_PAYOR, PAY_BY_CASH_AR, TD.IS_PAID AS IS_PAID, "  // FOR RECEIPT 
        + " RT.PERCENT_OF_CHARGE AS PREMIUM_CHARGE_PCT, "
        + " OI.IS_ALLOC_FULL_TAX AS IS_ALLOC_FULL_TAX, OI.EXCLUDE_TREATMENT AS EXCLUDE_TREATMENT, OI.ORDER_ITEM_CATEGORY_CODE AS ORDER_ITEM_CATEGORY_CODE, "
        + " OI.IS_COMPUTE AS IS_COMPUTE, GUARANTEE_AMT "
        + " FROM TRN_DAILY TD INNER JOIN RECEIPT_TYPE RT ON TD.RECEIPT_TYPE_CODE = RT.CODE AND TD.HOSPITAL_CODE = RT.HOSPITAL_CODE "
        + " LEFT JOIN ORDER_ITEM OI ON TD.ORDER_ITEM_CODE = OI.CODE AND TD.HOSPITAL_CODE = OI.HOSPITAL_CODE"
        + " WHERE (TRANSACTION_DATE >='" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "')"
        + " AND TD.HOSPITAL_CODE = '" + hospitalCode + "'"
        + " AND (COMPUTE_DAILY_DATE = '' OR COMPUTE_DAILY_DATE IS NULL)"
        + " AND (BATCH_NO IS NULL OR BATCH_NO = '')"
        + " AND (INVOICE_TYPE = '" + INVOICE_TYPE_EXECUTE + "' OR INVOICE_TYPE = '" + INVOICE_TYPE_RESULT + "')"
        + " AND TD.INVOICE_NO = '" + invoiceNo + "'"
        + " order by INVOICE_NO, LINE_NO ";
    }
    public static String getSQL_TRN_DAILY(String startDate, String endDate, String hospitalCode, String invoiceNo, String lineNo) {
        return "SELECT TD.HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, TRANSACTION_DATE, HN_NO, PATIENT_NAME, EPISODE_NO, PAYOR_OFFICE_CODE, PAYOR_OFFICE_NAME, "
        + " PAYOR_OFFICE_CATEGORY_CODE, PAYOR_OFFICE_CATEGORY_DESCRIPTION, TRANSACTION_MODULE, IS_WRITE_OFF, LINE_NO, ADMISSION_TYPE_CODE, " 
        + " TRANSACTION_TYPE, NATIONALITY_CODE, NATIONALITY_DESCRIPTION, "
        + " PATIENT_DEPARTMENT_CODE, PATIENT_LOCATION_CODE, RECEIPT_DEPARTMENT_CODE, RECEIPT_LOCATION_CODE, " 
        + " DOCTOR_DEPARTMENT_CODE, ORDER_ITEM_CODE, ORDER_ITEM_DESCRIPTION, DOCTOR_CODE, VERIFY_DATE, VERIFY_TIME, DOCTOR_EXECUTE_CODE, " 
        + " EXECUTE_DATE, EXECUTE_TIME, DOCTOR_RESULT_CODE, RECEIPT_TYPE_CODE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, " 
        + " AMOUNT_AFT_DISCOUNT, AMOUNT_BEF_WRITE_OFF, " 
        + " NOR_ALLOCATE_AMT, NOR_ALLOCATE_PCT, " 
        + " IS_GUARANTEE_FROM_ALLOC, "//NEW BY NOP 2011-02-06
        + " COMPUTE_DAILY_DATE, COMPUTE_DAILY_TIME, COMPUTE_DAILY_USER_ID, "
        + " CASE WHEN (DR.TAX_402_METHOD <> 'STP' AND HP.IS_DF_ALLOC_TAX_402 = 'Y') THEN 'Y' ELSE TAX_FROM_ALLOCATE END AS TAX_FROM_ALLOCATE, "
        //+ " TAX_FROM_ALLOCATE, " 
        + " TD.ACTIVE as ACTIVE, INVOICE_TYPE, TOTAL_BILL_AMOUNT, TOTAL_DR_REC_AMOUNT, " 
        + " INV_IS_VOID, REC_IS_VOID, " 
        + " YYYY, MM, RECEIPT_NO, RECEIPT_DATE, PAY_BY_CASH, PAY_BY_AR, PAY_BY_DOCTOR, PAY_BY_PAYOR, PAY_BY_CASH_AR, TD.IS_PAID AS IS_PAID, "  // FOR RECEIPT 
        + " RT.PERCENT_OF_CHARGE AS PREMIUM_CHARGE_PCT, "
        + " CASE WHEN (DR.TAX_402_METHOD <> 'STP' AND HP.IS_DF_ALLOC_TAX_402 = 'Y') THEN 'N' ELSE OI.IS_ALLOC_FULL_TAX END AS IS_ALLOC_FULL_TAX, "
        //+ " OI.IS_ALLOC_FULL_TAX AS IS_ALLOC_FULL_TAX, "
        //+ " OI.EXCLUDE_TREATMENT AS EXCLUDE_TREATMENT, "
        + " 'N' AS EXCLUDE_TREATMENT, "
        + " OI.ORDER_ITEM_CATEGORY_CODE AS ORDER_ITEM_CATEGORY_CODE, "
        + " OI.IS_COMPUTE AS IS_COMPUTE, GUARANTEE_AMT "
        + " FROM TRN_DAILY TD INNER JOIN RECEIPT_TYPE RT ON TD.RECEIPT_TYPE_CODE = RT.CODE AND TD.HOSPITAL_CODE = RT.HOSPITAL_CODE "
        + " LEFT JOIN DOCTOR DR ON TD.HOSPITAL_CODE= DR.HOSPITAL_CODE AND TD.DOCTOR_CODE = DR.CODE "
        + " LEFT JOIN ORDER_ITEM OI ON TD.ORDER_ITEM_CODE = OI.CODE AND TD.HOSPITAL_CODE = OI.HOSPITAL_CODE"
        + " LEFT JOIN HOSPITAL HP ON TD.HOSPITAL_CODE = HP.CODE"
        + " WHERE TD.LINE_NO = '" + lineNo + "'"
        + " AND TD.HOSPITAL_CODE = '" + hospitalCode + "'"
        + " AND TD.INVOICE_NO = '" + invoiceNo + "'"
        + " AND (TRANSACTION_DATE >='" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "')"
        + " AND (COMPUTE_DAILY_DATE = '' OR COMPUTE_DAILY_DATE IS NULL)"
        + " AND (BATCH_NO IS NULL OR BATCH_NO = '')"
        + " AND (INVOICE_TYPE = '" + INVOICE_TYPE_EXECUTE + "' OR INVOICE_TYPE = '" + INVOICE_TYPE_RESULT + "')"
        + " order by INVOICE_NO, LINE_NO ";
        //+ " FROM TRN_DAILY TD INNER JOIN RECEIPT_TYPE RT ON TD.RECEIPT_TYPE_CODE = RT.CODE AND TD.HOSPITAL_CODE = RT.HOSPITAL_CODE "
        //+ " LEFT JOIN ORDER_ITEM OI ON TD.ORDER_ITEM_CODE = OI.CODE AND TD.HOSPITAL_CODE = OI.HOSPITAL_CODE"
        //+ " WHERE (TRANSACTION_DATE >='" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "')"
        //+ " AND TD.HOSPITAL_CODE = '" + hospitalCode + "'"
        //+ " AND (COMPUTE_DAILY_DATE = '' OR COMPUTE_DAILY_DATE IS NULL)"
        //+ " AND (BATCH_NO IS NULL OR BATCH_NO = '')"
        //+ " AND (INVOICE_TYPE = '" + INVOICE_TYPE_EXECUTE + "' OR INVOICE_TYPE = '" + INVOICE_TYPE_RESULT + "')"
        //+ " AND TD.INVOICE_NO = '" + invoiceNo + "' AND LINE_NO = '" + lineNo + "'"
        //+ " order by INVOICE_NO, LINE_NO ";
    }
    
    
    public TrnDaily() {
        this.setTableName("TRN_DAILY");
    }
    public TrnDaily(DBConnection conn) {
        super();
        this.setDBConnection(conn);
        this.setTableName("TRN_DAILY");
        //System.out.println("New Tran Daily");
    }
    public String getIsGuaranteeFromAllocate() {
		return isGuaranteeFromAllocate;
	}
	public void setIsGuaranteeFromAllocate(String isGuaranteeFromAllocate) {
		this.isGuaranteeFromAllocate = isGuaranteeFromAllocate;
	}

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAdmissionTypeCode() {
        return admissionTypeCode;
    }

    public void setAdmissionTypeCode(String admissionTypeCode) {
        this.admissionTypeCode = admissionTypeCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getComputeDailyDate() {
        return computeDailyDate;
    }

    public void setComputeDailyDate(String computeDailyDate) {
        this.computeDailyDate = computeDailyDate;
    }

    public String getComputeDailyTime() {
        return computeDailyTime;
    }

    public void setComputeDailyTime(String computeDailyTime) {
        this.computeDailyTime = computeDailyTime;
    }

    public String getComputeDailyUserID() {
        return computeDailyUserID;
    }

    public void setComputeDailyUserID(String computeDailyUserID) {
        this.computeDailyUserID = computeDailyUserID;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getDoctorDepartmentCode() {
        return doctorDepartmentCode;
    }

    public void setDoctorDepartmentCode(String doctorDepartmentCode) {
        this.doctorDepartmentCode = doctorDepartmentCode;
    }

    public String getDoctorExecuteCode() {
        return doctorExecuteCode;
    }

    public void setDoctorExecuteCode(String doctorExecuteCode) {
        this.doctorExecuteCode = doctorExecuteCode;
    }

    public String getDoctorResultCode() {
        return doctorResultCode;
    }

    public void setDoctorResultCode(String doctorResultCode) {
        this.doctorResultCode = doctorResultCode;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getHnNo() {
        return hnNo;
    }

    public void setHnNo(String hnNo) {
        this.hnNo = hnNo;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getInvIsVoid() {
        return invIsVoid;
    }

    public void setInvIsVoid(String invIsVoid) {
        this.invIsVoid = invIsVoid;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getOrderItemCode() {
        return orderItemCode;
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }

    public String getOrderItemDescription() {
        return orderItemDescription;
    }

    public void setOrderItemDescription(String orderItemDescription) {
        this.orderItemDescription = orderItemDescription;
    }

    public String getPatientDepartmentCode() {
        return patientDepartmentCode;
    }

    public void setPatientDepartmentCode(String patientDepartmentCode) {
        this.patientDepartmentCode = patientDepartmentCode;
    }

    public String getPatientLocationCode() {
        return patientLocationCode;
    }

    public void setPatientLocationCode(String patientLocationCode) {
        this.patientLocationCode = patientLocationCode;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPayorOfficeCategoryCode() {
        return payorOfficeCategoryCode;
    }

    public void setPayorOfficeCategoryCode(String payorOfficeCategoryCode) {
        this.payorOfficeCategoryCode = payorOfficeCategoryCode;
    }

    public String getPayorOfficeCategoryDescription() {
        return payorOfficeCategoryDescription;
    }

    public void setPayorOfficeCategoryDescription(String payorOfficeCategoryDescription) {
        this.payorOfficeCategoryDescription = payorOfficeCategoryDescription;
    }

    public String getPayorOfficeCode() {
        return payorOfficeCode;
    }

    public void setPayorOfficeCode(String payorOfficeCode) {
        this.payorOfficeCode = payorOfficeCode;
    }

    public String getPayorOfficeName() {
        return payorOfficeName;
    }

    public void setPayorOfficeName(String payorOfficeName) {
        this.payorOfficeName = payorOfficeName;
    }

    public String getRecIsVoid() {
        return recIsVoid;
    }

    public void setRecIsVoid(String recIsVoid) {
        this.recIsVoid = recIsVoid;
    }

    public String getReceiptDepartmentCode() {
        return receiptDepartmentCode;
    }

    public void setReceiptDepartmentCode(String receiptDepartmentCode) {
        this.receiptDepartmentCode = receiptDepartmentCode;
    }

    public String getReceiptLocationCode() {
        return receiptLocationCode;
    }

    public void setReceiptLocationCode(String receiptLocationCode) {
        this.receiptLocationCode = receiptLocationCode;
    }

    public String getReceiptTypeCode() {
        return receiptTypeCode;
    }

    public void setReceiptTypeCode(String receiptTypeCode) {
        this.receiptTypeCode = receiptTypeCode;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Double getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(Double totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public Double getTotalDrRecAmount() {
        return totalDrRecAmount;
    }

    public void setTotalDrRecAmount(Double totalDrRecAmount) {
        this.totalDrRecAmount = totalDrRecAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionModule() {
        return transactionModule;
    }

    public void setTransactionModule(String transactionModule) {
        this.transactionModule = transactionModule;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(String verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Double getAmountAftDiscount() {
        return amountAftDiscount;
    }

    public void setAmountAftDiscount(Double amountAftDiscount) {
        this.amountAftDiscount = amountAftDiscount;
    }

    public Double getAmountBefDiscount() {
        return amountBefDiscount;
    }

    public void setAmountBefDiscount(Double amountBefDiscount) {
        this.amountBefDiscount = amountBefDiscount;
    }

    public Double getAmountBefWriteOff() {
        return amountBefWriteOff;
    }

    public void setAmountBefWriteOff(Double amountBefWriteOff) {
        this.amountBefWriteOff = amountBefWriteOff;
    }

    public Double getAmountOfDiscount() {
        return amountOfDiscount;
    }

    public void setAmountOfDiscount(Double amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getPayByAR() {
        return payByAR;
    }

    public void setPayByAR(String payByAR) {
        this.payByAR = payByAR;
    }

    public String getPayByCash() {
        return payByCash;
    }

    public void setPayByCash(String payByCash) {
        this.payByCash = payByCash;
    }

    public String getPayByDoctor() {
        return payByDoctor;
    }

    public void setPayByDoctor(String payByDoctor) {
        this.payByDoctor = payByDoctor;
    }

    public String getPayByPayor() {
        return payByPayor;
    }

    public void setPayByPayor(String payByPayor) {
        this.payByPayor = payByPayor;
    }

    public String getPayByCashAr() {
        return payByCashAr;
    }

    public void setPayByCashAr(String payByCashAr) {
        this.payByCashAr = payByCashAr;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getNationalityDescription() {
        return nationalityDescription;
    }

    public void setNationalityDescription(String nationalityDescription) {
        this.nationalityDescription = nationalityDescription;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getIsCompute() {
        return isCompute;
    }

    public void setIsCompute(String isCompute) {
        this.isCompute = isCompute;
        if (this.isCompute == null) { this.isCompute = "N"; }
    }
    
    public boolean OpenResultSet(String sql) {
        boolean ret = false;
        
        try {
            if (this.getStatement() == null) { this.setStatement(this.getDBConnection().getConnection().createStatement()); }
            this.setResultSet(this.getStatement().executeQuery(sql));
            ret = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return ret;
    }

    public String getExcludeTreatment() {
        return excludeTreatment;
    }

    public void setExcludeTreatment(String excludeTreatment) {
        this.excludeTreatment = excludeTreatment;
    }

    public String getOrderItemIsAllocFullTax() {
        return orderItemIsAllocFullTax;
    }

    public void setOrderItemIsAllocFullTax(String orderItemIsAllocFullTax) {
        this.orderItemIsAllocFullTax = orderItemIsAllocFullTax;
    }

    public Double getPremiumChargePct() {
        return premiumChargePct;
    }

    public void setPremiumChargePct(Double premiumChargePct) {
        this.premiumChargePct = premiumChargePct;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
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
        if (premiumRecAmt == null) { premiumRecAmt = 0d; }
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

    public String getOrderItemCategoryCode() {
        return orderItemCategoryCode;
    }

    public void setOrderItemCategoryCode(String orderItemCategoryCode) {
        this.orderItemCategoryCode = orderItemCategoryCode;
    }
    public Double getOldDrAmt() {
        return oldDrAmt;
    }

    public void setOldDrAmt(Double oldDrAmt) {
        this.oldDrAmt = oldDrAmt;
    }
    public String getTaxFromAllocate(){
    	return this.taxFromAllocate;
    }
    public void setTaxFromAllocate(String t){
    	this.taxFromAllocate = t;
    }
    public boolean MoveNext() {
        boolean ret = false;
        try {
            ret = this.getResultSet().next();
            if (!ret) { return false; }
            this.setHospitalCode(this.getResultSet().getString("Hospital_Code"));
            this.setInvoiceNo(this.getResultSet().getString("Invoice_No"));
            this.setInvoiceDate(this.getResultSet().getString("Invoice_Date"));
            this.setTransactionDate(this.getResultSet().getString("Transaction_Date"));
            this.setHnNo(this.getResultSet().getString("Hn_No"));
            this.setPatientName(this.getResultSet().getString("patient_Name"));
            this.setEpisodeNo(this.getResultSet().getString("episode_No"));
            this.setNationalityCode(this.getResultSet().getString("NATIONALITY_CODE"));
            this.setNationalityCode(this.getResultSet().getString("NATIONALITY_DESCRIPTION"));
            this.setPayorOfficeCode(this.getResultSet().getString("payor_Office_Code"));
            this.setPayorOfficeName(this.getResultSet().getString("payor_Office_Name"));
            this.setPayorOfficeCategoryCode(this.getResultSet().getString("payor_Office_Category_Code"));
            this.setPayorOfficeCategoryDescription(this.getResultSet().getString("payor_Office_Category_Description"));
            this.setTransactionModule(this.getResultSet().getString("transaction_Module"));
            this.setIsWriteOff(this.getResultSet().getString("is_Write_Off"));
            this.setLineNo(this.getResultSet().getString("Line_No"));
            this.setAdmissionTypeCode(this.getResultSet().getString("admission_Type_Code"));
            this.setTransactionType(this.getResultSet().getString("transaction_Type"));
            this.setPatientDepartmentCode(this.getResultSet().getString("patient_Department_Code"));
            this.setPatientLocationCode(this.getResultSet().getString("patient_Location_Code"));
            this.setReceiptDepartmentCode(this.getResultSet().getString("receipt_Department_Code"));
            this.setReceiptLocationCode(this.getResultSet().getString("receipt_Location_Code"));
            this.setDoctorDepartmentCode(this.getResultSet().getString("doctor_Department_Code"));
            this.setOrderItemCode(this.getResultSet().getString("order_Item_Code"));
            this.setOrderItemDescription(this.getResultSet().getString("order_Item_Description"));
            this.setDoctorCode(this.getResultSet().getString("doctor_Code"));
            this.setReceiptTypeCode(this.getResultSet().getString("receipt_Type_Code"));
            this.setInvIsVoid(this.getResultSet().getString("INV_IS_VOID"));
            this.setRecIsVoid(this.getResultSet().getString("REC_IS_VOID"));
            this.setActive(this.getResultSet().getString("ACTIVE"));
            this.setVerifyDate(this.getResultSet().getString("VERIFY_DATE"));
            this.setVerifyTime(this.getResultSet().getString("VERIFY_TIME"));
            this.setDoctorExecuteCode(this.getResultSet().getString("DOCTOR_EXECUTE_CODE"));
            this.setExecuteDate(this.getResultSet().getString("EXECUTE_DATE"));
            this.setExecuteTime(this.getResultSet().getString("EXECUTE_TIME"));
            this.setDoctorResultCode(this.getResultSet().getString("DOCTOR_RESULT_CODE"));
            //UPDATE BY NOP 2011-02-06
            this.setIsGuaranteeFromAllocate(this.getResultSet().getString("IS_GUARANTEE_FROM_ALLOC"));
//            this.setUpdateDate(this.getResultSet().getString("UPDATE_DATE"));
//            this.setUpdateTime(this.getResultSet().getString("UPDATE_TIME"));
//            this.setUserId(this.getResultSet().getString("USER_ID"));
            this.setInvoiceType(this.getResultSet().getString("INVOICE_TYPE"));
            this.setTotalBillAmount(this.getResultSet().getDouble("TOTAL_BILL_AMOUNT"));
            this.setTotalDrRecAmount(this.getResultSet().getDouble("TOTAL_DR_REC_AMOUNT"));
            this.setAmountBefDiscount(this.getResultSet().getDouble("amount_BEF_Discount"));
            this.setAmountOfDiscount(this.getResultSet().getDouble("AMOUNT_OF_DISCOUNT"));
            this.setAmountAftDiscount(this.getResultSet().getDouble("AMOUNT_AFT_DISCOUNT"));
            this.setAmountBefWriteOff(this.getResultSet().getDouble("AMOUNT_BEF_WRITE_OFF"));
            this.setPremiumChargePct(this.getResultSet().getDouble("PREMIUM_CHARGE_PCT"));
            this.setExcludeTreatment(this.getResultSet().getString("EXCLUDE_TREATMENT"));
            this.setOrderItemIsAllocFullTax(this.getResultSet().getString("IS_ALLOC_FULL_TAX"));
            
            // receipt 
            this.setReceiptNo(this.getResultSet().getString("RECEIPT_NO"));
            this.setReceiptDate(this.getResultSet().getString("RECEIPT_DATE"));
            this.setYyyy(this.getResultSet().getString("YYYY"));
            this.setMm(this.getResultSet().getString("MM"));
            this.setPayByCash(this.getResultSet().getString("PAY_BY_CASH"));
            this.setPayByAR(this.getResultSet().getString("PAY_BY_AR"));
            this.setPayByDoctor(this.getResultSet().getString("PAY_BY_DOCTOR"));
            this.setPayByPayor(this.getResultSet().getString("PAY_BY_PAYOR"));
            this.setPayByCashAr(this.getResultSet().getString("PAY_BY_CASH_AR"));
            this.setIsPaid(this.getResultSet().getString("IS_PAID"));
            this.setOrderItemCategoryCode(this.getResultSet().getString("ORDER_ITEM_CATEGORY_CODE"));
            this.setIsCompute(this.getResultSet().getString("IS_COMPUTE"));
            this.setGuaranteeAmt(this.getResultSet().getDouble("GUARANTEE_AMT"));
            
        } catch (SQLException ex) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_DAILY, "Update transaction fail.", ex.getMessage());
            ret = false;
        }
        return ret;
    }
    
    public boolean updateCompute(String startDate, String endDate, String hospitalCode) {
        boolean ret = false;
        String sql = "UPDATE TRN_DAILY SET COMPUTE_DAILY_DATE = '" + JDate.getDate() + "'"
                    + ", COMPUTE_DAILY_TIME = '" + JDate.getTime() + "'"
                    + ", COMPUTE_DAILY_USER_ID = '" + this.getDBConnection().getUserID() + "'"
                    + " WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'"
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '')"
                    + " AND (COMPUTE_DAILY_DATE = '' OR COMPUTE_DAILY_DATE IS NULL)"
                    + " AND EXISTS (SELECT CODE FROM DOCTOR WHERE ACTIVE = '1' AND TRN_DAILY.DOCTOR_CODE = DOCTOR.CODE AND DOCTOR.HOSPITAL_CODE='" + hospitalCode + "')"
                    + " AND EXISTS (SELECT CODE FROM DOCTOR_CATEGORY WHERE ACTIVE = '1' AND TRN_DAILY.DOCTOR_CATEGORY_CODE = DOCTOR_CATEGORY.CODE AND DOCTOR_CATEGORY.HOSPITAL_CODE='" + hospitalCode + "')";
        try {
            if (this.getDBConnection().executeUpdate(sql)> -1)             ret = true;
        } catch (Exception ex) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_DAILY, "Update transaction fail.", ex.getMessage(), sql);
            ex.printStackTrace();
        }
        
        return ret;
    }
    
    public boolean updateCompute(String startDate, String endDate, String hospitalCode, String invoiceNo, String lineNo) {
        boolean ret = false;
        String sql = "UPDATE TRN_DAILY SET COMPUTE_DAILY_DATE = '" + JDate.getDate() + "'"
                    + ", COMPUTE_DAILY_TIME = '" + JDate.getTime() + "'"
                    + ", COMPUTE_DAILY_USER_ID = '" + this.getDBConnection().getUserID() + "'"
                    + " WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'"
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '')"
                    + " AND (COMPUTE_DAILY_DATE = '' OR COMPUTE_DAILY_DATE IS NULL)"
//                    + " AND EXISTS (SELECT CODE FROM DOCTOR WHERE ACTIVE = '1' AND TRN_DAILY.DOCTOR_CODE = DOCTOR.CODE)"
//                    + " AND EXISTS (SELECT CODE FROM DOCTOR_CATEGORY WHERE ACTIVE = '1' AND TRN_DAILY.DOCTOR_CATEGORY_CODE = DOCTOR_CATEGORY.CODE)"
                    + " AND INVOICE_NO = '" + invoiceNo + "' AND LINE_NO = '" + lineNo + "'";
        try {
            if (this.getDBConnection().executeUpdate(sql)> -1)             ret = true;
        } catch (Exception ex) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_DAILY, "Update transaction fail.", ex.getMessage(), sql);
            ex.printStackTrace();
        }
        
        return ret;
    }
        
    public boolean update() {
    boolean ret = false;
    ResultSet rs = null;
    Statement stmt = null;
        String sql = "";
            sql = "SELECT HOSPITAL_CODE,INVOICE_NO,INVOICE_DATE,LINE_NO,ORDER_ITEM_CODE,DOCTOR_CODE, DOCTOR_DEPARTMENT_CODE, " 
                    + " AMOUNT_BEF_DISCOUNT,AMOUNT_OF_DISCOUNT, "
                    + " AMOUNT_AFT_DISCOUNT,BATCH_NO,UPDATE_DATE,UPDATE_TIME,USER_ID,OLD_DOCTOR_CODE,"
                    + " INV_IS_VOID,YYYY,MM,DD,RECEIPT_NO,RECEIPT_DATE,RECEIPT_TYPE_CODE,"
                    + " REC_IS_VOID,NOR_ALLOCATE_AMT, NOR_ALLOCATE_PCT, DR_AMT, OLD_DR_AMT, DR_PREMIUM,"
                    + " HP_AMT,HP_PREMIUM,HP_TAX,TAX_TYPE_CODE,DR_TAX_400,DR_TAX_401,DR_TAX_402,DR_TAX_406,"
                    + " COMPUTE_DAILY_DATE,COMPUTE_DAILY_TIME,"
                    + " COMPUTE_DAILY_USER_ID, TAX_FROM_ALLOCATE, PREMIUM_CHARGE_PCT,TRANSACTION_DATE,DOCTOR_CATEGORY_CODE,"
                    + " EXCLUDE_TREATMENT, PREMIUM_REC_AMT,ACTIVE, ORDER_ITEM_CATEGORY_CODE, GUARANTEE_AMT,"
                    + " IS_GUARANTEE_FROM_ALLOC "//UPDATE BY NOP 2011-01-06
            + " FROM " + this.getTableName(); 
            sql = sql + " where HOSPITAL_CODE='" + this.getHospitalCode() + "'";
            sql = sql + " and INVOICE_TYPE <> 'ORDER' ";
            sql = sql + " and INVOICE_NO='" + this.getInvoiceNo() + "'";
            sql = sql + " and TRANSACTION_DATE='" + this.getTransactionDate() + "'";
            sql = sql + " and INVOICE_DATE = '" + this.getInvoiceDate() + "'";
            sql = sql + " and RECEIPT_DATE = '" + this.getReceiptDate() + "'";
            sql = sql + " and LINE_NO = '" + this.getLineNo() + "'";
    
    try {
        if (rs == null) {
            stmt = this.getDBConnection().getStatementForInsert();
            rs = stmt.executeQuery(sql);
        } 
            rs.absolute(1);
            rs.updateDouble("AMOUNT_BEF_DISCOUNT", this.getAmountBefDiscount());
            rs.updateDouble("AMOUNT_OF_DISCOUNT", this.getAmountOfDiscount());
            rs.updateDouble("AMOUNT_AFT_DISCOUNT", this.getAmountAftDiscount());
            rs.updateDouble("PREMIUM_REC_AMT", this.getPremiumRecAmt());
            rs.updateDouble("NOR_ALLOCATE_AMT", this.getNorAllocateAmt());
            rs.updateDouble("NOR_ALLOCATE_PCT", this.getNorAllocatePct());
            rs.updateDouble("DR_AMT", this.getDrAmt());
            rs.updateDouble("OLD_DR_AMT", this.getOldDrAmt());
            rs.updateDouble("DR_PREMIUM", this.getDrPremium());
            rs.updateDouble("HP_AMT", this.getHpAmt());
            rs.updateDouble("HP_PREMIUM", this.getHpPremium());
            rs.updateDouble("HP_TAX", this.getHpTax());
            rs.updateString("TAX_TYPE_CODE", this.getTaxTypeCode());
            rs.updateDouble("DR_TAX_400", this.getDrTax400());
            rs.updateDouble("DR_TAX_401", this.getDrTax401());
            rs.updateDouble("DR_TAX_402", this.getDrTax402());
            rs.updateDouble("DR_TAX_406", this.getDrTax406());
            rs.updateDouble("PREMIUM_CHARGE_PCT", this.getPremiumChargePct());
            rs.updateString("DOCTOR_CATEGORY_CODE", this.getDoctorCategoryCode());
            rs.updateString("EXCLUDE_TREATMENT", this.getExcludeTreatment());
            rs.updateDouble("PREMIUM_REC_AMT", this.getPremiumRecAmt());
            
//             update when invoice have RECEIPT_NO
            rs.updateString("yyyy", this.getYyyy());
            rs.updateString("mm", this.getMm());
            rs.updateString("dd", this.getDd()); 
            rs.updateString("COMPUTE_DAILY_DATE", this.getComputeDailyDate());
            rs.updateString("COMPUTE_DAILY_TIME", this.getComputeDailyTime());
            rs.updateString("COMPUTE_DAILY_USER_ID", this.getComputeDailyUserID());
            rs.updateString("TAX_FROM_ALLOCATE", this.getTaxFromAllocate());
            rs.updateString("ORDER_ITEM_CATEGORY_CODE", this.getOrderItemCategoryCode());
            rs.updateString("DOCTOR_DEPARTMENT_CODE", this.getDoctorDepartmentCode());
            rs.updateDouble("GUARANTEE_AMT", this.getGuaranteeAmt());
            //UPDATE BY NOP 2011-02-06
            rs.updateString("IS_GUARANTEE_FROM_ALLOC",this.getIsGuaranteeFromAllocate());

            // update when compute monthly            
            // rs.updateString("ACTIVE", this.getActive());
            
            // Insert the row
            rs.updateRow();
            ret = true;
        } catch (SQLException e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_DAILY, "Update transaction fail.", e.getMessage(), sql);
            System.out.println("A SQLException error has occured in " + this.getClass().getName() + ".update() \n" + sql);
            e.printStackTrace();
            ret = false;
        }
        
        finally {
            try {
                rs.close();
                rs = null;
                stmt = null;
            }
            catch (Exception ex)  {
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_DAILY, "", ex.getMessage(), sql);
                System.out.println("A SQLException error has occured in " + this.getClass().getName() + ".update() \n" + ex.getMessage());
                ex.printStackTrace();
                ret = false;
            }
        }
    
    return ret;
    } 
    
    //// Roll back transaction ////
    public boolean rollBackUpdate(String hospitalCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql = "UPDATE " + this.getTableName() + " SET "
//                    + "  YYYY = ''"
//                    + ", MM = ''"
//                    + ", DD = ''"
                    + " NOR_ALLOCATE_AMT = 0"
                    + ", NOR_ALLOCATE_PCT = 0"
                    + ", DR_AMT = 0"
                    + ", OLD_DR_AMT = 0"
                    + ", DR_TAX_400 = 0"
                    + ", DR_TAX_401 = 0"
                    + ", DR_TAX_402 = 0"
                    + ", DR_TAX_406 = 0"
                    + ", TAX_TYPE_CODE = ''"
                    + ", DR_PREMIUM = 0"
                    + ", HP_AMT = 0"
                    + ", HP_PREMIUM = 0"
                    + ", HP_TAX = 0"
                    + ", COMPUTE_DAILY_DATE = ''"
                    + ", COMPUTE_DAILY_TIME = ''"
                    + ", COMPUTE_DAILY_USER_ID = ''"
                    + ", TAX_FROM_ALLOCATE = 'N'"
                    + ", DOCTOR_CATEGORY_CODE = ''"
                    + ", EXCLUDE_TREATMENT = ''"
                    + ", PREMIUM_CHARGE_PCT = 0"
                    + ", PREMIUM_REC_AMT = 0"
                    + ", ORDER_ITEM_CATEGORY_CODE = ''"
                    + ", GUARANTEE_AMT = 0"
                    + ", IS_GUARANTEE_FROM_ALLOC = ''" //UPDATE BY NOP 2011-02-06
                    + " WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'"
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '')";
        sqlCommand.add( sql );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
    
    //// Roll back transaction by doctor_code ////// 20/06/2009
    public boolean rollBackUpdate(String hospitalCode,String doctorCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql = "UPDATE " + this.getTableName() + " SET "
//                    + "  YYYY = ''"
//                    + ", MM = ''"
//                    + ", DD = ''"
                    + " NOR_ALLOCATE_AMT = 0"
                    + ", NOR_ALLOCATE_PCT = 0"
                    + ", DR_AMT = 0"
                    + ", OLD_DR_AMT = 0"
                    + ", DR_TAX_400 = 0"
                    + ", DR_TAX_401 = 0"
                    + ", DR_TAX_402 = 0"
                    + ", DR_TAX_406 = 0"
                    + ", TAX_TYPE_CODE = ''"
                    + ", DR_PREMIUM = 0"
                    + ", HP_AMT = 0"
                    + ", HP_PREMIUM = 0"
                    + ", HP_TAX = 0"
                    + ", COMPUTE_DAILY_DATE = ''"
                    + ", COMPUTE_DAILY_TIME = ''"
                    + ", COMPUTE_DAILY_USER_ID = ''"
                    + ", TAX_FROM_ALLOCATE = 'N'"
                    + ", DOCTOR_CATEGORY_CODE = ''"
                    + ", EXCLUDE_TREATMENT = ''"
                    + ", PREMIUM_CHARGE_PCT = 0"
                    + ", PREMIUM_REC_AMT = 0"
                    + ", ORDER_ITEM_CATEGORY_CODE = ''"
                    + ", GUARANTEE_AMT = 0"
                    + ", IS_GUARANTEE_FROM_ALLOC = ''" //UPDATE BY NOP 2011-02-06
                    + " WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'"
                    + " AND DOCTOR_CODE = '" + doctorCode + "'" 	
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '')";
        sqlCommand.add( sql );
        ret = super.rollBack(sqlCommand);
        return ret;
    }   
    public boolean rollBackDelete(String hospitalCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql = "DELETE FROM " + this.getTableName()
                    + " WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'"
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '')";
        sqlCommand.add( sql );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
    public boolean rollBackImportBillDelete(String hospitalCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql = "DELETE FROM " + this.getTableName()
                    + " WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'"
                    + " AND (INVOICE_TYPE = '" + TrnDaily.INVOICE_TYPE_EXECUTE + "' OR INVOICE_TYPE = '" + TrnDaily.INVOICE_TYPE_ORDER + "')"
                    //+ " AND (BATCH_NO IS NULL OR BATCH_NO = '') AND LINE_NO NOT LIKE 'ADD%'";
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') AND INVOICE_NO+LINE_NO IN (SELECT BILL_NO+LINE_NO FROM INT_HIS_BILL"
                    + " WHERE HOSPITAL_CODE ='" + hospitalCode + "' AND (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"'))";
        sqlCommand.add( sql );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    public boolean rollBackImportBillUpdate(String hospitalCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        //String sql = "UPDATE INT_HIS_BILL SET AMOUNT_BEF_DISCOUNT = OLD_AMOUNT"
        String sql = "UPDATE INT_HIS_BILL SET AMOUNT_BEF_DISCOUNT = "
        			+"CASE WHEN H.IS_JOIN_BILL = 'Y' THEN  OLD_AMOUNT ELSE AMOUNT_BEF_DISCOUNT END "
        			+"FROM INT_HIS_BILL LEFT OUTER JOIN HOSPITAL H ON INT_HIS_BILL.HOSPITAL_CODE = H.CODE "
                    +"WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "' "
                    +"AND HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql );
        ret = super.rollBack(sqlCommand);
        return ret;
    } 
    public boolean rollBackImportVerifyDelete(String hospitalCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql = "DELETE FROM " + this.getTableName()
                    + " WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'"
                    + " AND (INVOICE_TYPE = '" + TrnDaily.INVOICE_TYPE_RESULT + "')"
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '')";
        sqlCommand.add( sql );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
}
