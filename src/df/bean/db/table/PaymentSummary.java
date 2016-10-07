package df.bean.db.table;

import java.util.Date;

public class PaymentSummary extends ABSTable {

    private String yy;
    private String mm;
    private Date transactionDate;
    private Double sumAmt;
    private Double sumDiscAmt;
    private Double sumPremiumAmt;
    private Double sumPatientPaidAmt;
    private Double drSumAmt;
    private Double drDiscAmt;
    private Double drPremiumAmt;
    private Double drCreditAmt;
    private Double drDebitAmt;
    private Double drNetPaidAmt;
    private Double drTax402;
    private Double drTax406;
    private Double hpSumAmt;
    private Double hpDiscAmt;
    private Double hpPremiumAmt;
    private Double hpTax;
    private String drTextMoney;
    private String accountNo;
    private Double payDate;
    private String payBy;
    private Integer isPaid;
    private String remark;
    private String hospitalCode;
    private String doctorCode;
    private String paymentModeCode;

    public PaymentSummary() {
        super();
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public String getDoctorCode() {
        return this.doctorCode;
    }

    public Double getDrCreditAmt() {
        return this.drCreditAmt;
    }

    public Double getDrDebitAmt() {
        return this.drDebitAmt;
    }

    public Double getDrDiscAmt() {
        return this.drDiscAmt;
    }

    public Double getDrNetPaidAmt() {
        return this.drNetPaidAmt;
    }

    public Double getDrPremiumAmt() {
        return this.drPremiumAmt;
    }

    public Double getDrSumAmt() {
        return this.drSumAmt;
    }

    public Double getDrTax402() {
        return this.drTax402;
    }

    public Double getDrTax406() {
        return this.drTax406;
    }

    public String getDrTextMoney() {
        return this.drTextMoney;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public Double getHpDiscAmt() {
        return this.hpDiscAmt;
    }

    public Double getHpPremiumAmt() {
        return this.hpPremiumAmt;
    }

    public Double getHpSumAmt() {
        return this.hpSumAmt;
    }

    public Double getHpTax() {
        return this.hpTax;
    }

    public Integer getIsPaid() {
        return this.isPaid;
    }

    public String getMm() {
        return this.mm;
    }

    public String getPayBy() {
        return this.payBy;
    }

    public Double getPayDate() {
        return this.payDate;
    }

    public String getPaymentModeCode() {
        return this.paymentModeCode;
    }

    public String getRemark() {
        return this.remark;
    }

    public Double getSumAmt() {
        return this.sumAmt;
    }

    public Double getSumDiscAmt() {
        return this.sumDiscAmt;
    }

    public Double getSumPatientPaidAmt() {
        return this.sumPatientPaidAmt;
    }

    public Double getSumPremiumAmt() {
        return this.sumPremiumAmt;
    }

    public Date getTransactionDate() {
        return this.transactionDate;
    }

    public String getYy() {
        return this.yy;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public void setDrCreditAmt(Double drCreditAmt) {
        this.drCreditAmt = drCreditAmt;
    }

    public void setDrDebitAmt(Double drDebitAmt) {
        this.drDebitAmt = drDebitAmt;
    }

    public void setDrDiscAmt(Double drDiscAmt) {
        this.drDiscAmt = drDiscAmt;
    }

    public void setDrNetPaidAmt(Double drNetPaidAmt) {
        this.drNetPaidAmt = drNetPaidAmt;
    }

    public void setDrPremiumAmt(Double drPremiumAmt) {
        this.drPremiumAmt = drPremiumAmt;
    }

    public void setDrSumAmt(Double drSumAmt) {
        this.drSumAmt = drSumAmt;
    }

    public void setDrTax402(Double drTax402) {
        this.drTax402 = drTax402;
    }

    public void setDrTax406(Double drTax406) {
        this.drTax406 = drTax406;
    }

    public void setDrTextMoney(String drTextMoney) {
        this.drTextMoney = drTextMoney;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setHpDiscAmt(Double hpDiscAmt) {
        this.hpDiscAmt = hpDiscAmt;
    }

    public void setHpPremiumAmt(Double hpPremiumAmt) {
        this.hpPremiumAmt = hpPremiumAmt;
    }

    public void setHpSumAmt(Double hpSumAmt) {
        this.hpSumAmt = hpSumAmt;
    }

    public void setHpTax(Double hpTax) {
        this.hpTax = hpTax;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public void setPayBy(String payBy) {
        this.payBy = payBy;
    }

    public void setPayDate(Double payDate) {
        this.payDate = payDate;
    }

    public void setPaymentModeCode(String paymentModeCode) {
        this.paymentModeCode = paymentModeCode;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSumAmt(Double sumAmt) {
        this.sumAmt = sumAmt;
    }

    public void setSumDiscAmt(Double sumDiscAmt) {
        this.sumDiscAmt = sumDiscAmt;
    }

    public void setSumPatientPaidAmt(Double sumPatientPaidAmt) {
        this.sumPatientPaidAmt = sumPatientPaidAmt;
    }

    public void setSumPremiumAmt(Double sumPremiumAmt) {
        this.sumPremiumAmt = sumPremiumAmt;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

}
