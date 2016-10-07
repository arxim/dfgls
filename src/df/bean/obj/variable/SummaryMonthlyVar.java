package df.bean.obj.variable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.DialogBox;

public class SummaryMonthlyVar {
    private String doctorCode = "";
    private Double sumAmountAftDiscount = 0d; // sinv_amt
    private Double sumAmountOfDiscount = 0d; // as sinv_discount_amt
    private Double sumDrAmt = 0d;  // as sDr_Amt
    private Double sumDrAmt_400 = 0d;  // as sDr_Amt_400
    private Double sumDrAmt_401 = 0d;  // as sDr_Amt_401
    private Double sumDrAmt_402 = 0d;  // as sDr_Amt_402
    private Double sumDrAmt_406 = 0d;  // as sDr_Amt_406
    private Double sumDrPremium = 0d;  // as sDr_Premium
    private Double sumHpAmt = 0d;  // as sHp_Amt
    private Double sumHpPremium = 0d;   // as sHp_Premium
    private Double sumHpTax = 0d;  // as sHp_Tax
    private Double sumRecAmountAftDiscount = 0d;    // sum(REC_AMOUNT_BEF_DISCOUNT-REC_AMOUNT_OF_DISCOUNT) as sRec_Amt
    private Double sumRecPremiumAmt = 0d;   // as sRec_Premium_Amt
    private Double sumPremiumRecAmt = 0d;   // sum(PREMIUM_REC_AMT) as sPremium_Rec_Amt (premium of total receipt amount
    private Double sumPayByCash = 0d;
    private Double sumPayByAR = 0d;
    private Double cashAmt = 0d;
    private Double cash400 = 0d;
    private Double cash401 = 0d;
    private Double cash402 = 0d;
    private Double cash406 = 0d;
    private Double arAmt = 0d;
    private Double ar400 = 0d;
    private Double ar401 = 0d;
    private Double ar402 = 0d;
    private Double ar406 = 0d;
    
    private String []doctorCodeArry;
    private Double []sumAmountAftDiscountArry; 
    private Double []sumAmountOfDiscountArry; 
    private Double []sumDrAmtArry;  
    private Double []sumDrAmt_400Arry; 
    private Double []sumDrAmt_401Arry; 
    private Double []sumDrAmt_402Arry; 
    private Double []sumDrAmt_406Arry; 
    private Double []sumDrPremiumArry; 
    private Double []sumHpAmtArry;  
    private Double []sumHpPremiumArry; 
    private Double []sumHpTaxArry;  
    private Double []sumRecAmountAftDiscountArry;
    private Double []sumRecPremiumAmtArry;
    private Double []sumPremiumRecAmtArry;
    private Double []sumPayByCashArry;
    private Double []sumPayByARArry;
    private Double []cashAmtArry;
    private Double []cash400Arry;
    private Double []cash401Arry;
    private Double []cash402Arry;
    private Double []cash406Arry;
    private Double []arAmtArry;
    private Double []ar400Arry;
    private Double []ar401Arry;
    private Double []ar402Arry;
    private Double []ar406Arry;    
    
    private int size = 0;
    
    DBConnection conn = null;

    public SummaryMonthlyVar(DBConnection conn) {
        this.conn = conn;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public Double getSumAmountAftDiscount() {
        return sumAmountAftDiscount;
    }

    public void setSumAmountAftDiscount(Double sumAmountAftDiscount) {
        this.sumAmountAftDiscount = sumAmountAftDiscount;
    }

    public Double getSumAmountOfDiscount() {
        return sumAmountOfDiscount;
    }

    public void setSumAmountOfDiscount(Double sumAmountOfDiscount) {
        this.sumAmountOfDiscount = sumAmountOfDiscount;
    }

    public Double getSumDrAmt() {
        return sumDrAmt;
    }

    public void setSumDrAmt(Double sumDrAmt) {
        this.sumDrAmt = sumDrAmt;
    }

    public Double getSumDrAmt_402() {
        return sumDrAmt_402;
    }

    public void setSumDrAmt_402(Double sumDrAmt_402) {
        this.sumDrAmt_402 = sumDrAmt_402;
    }

    public Double getSumDrAmt_406() {
        return sumDrAmt_406;
    }

    public void setSumDrAmt_406(Double sumDrAmt_406) {
        this.sumDrAmt_406 = sumDrAmt_406;
    }

    public Double getSumDrPremium() {
        return sumDrPremium;
    }

    public void setSumDrPremium(Double sumDrPremium) {
        this.sumDrPremium = sumDrPremium;
    }

    public Double getSumHpAmt() {
        return sumHpAmt;
    }

    public void setSumHpAmt(Double sumHpAmt) {
        this.sumHpAmt = sumHpAmt;
    }

    public Double getSumHpPremium() {
        return sumHpPremium;
    }

    public void setSumHpPremium(Double sumHpPremium) {
        this.sumHpPremium = sumHpPremium;
    }

    public Double getSumHpTax() {
        return sumHpTax;
    }

    public void setSumHpTax(Double sumHpTax) {
        this.sumHpTax = sumHpTax;
    }

    public Double getSumRecAmountAftDiscount() {
        return sumRecAmountAftDiscount;
    }

    public void setSumRecAmountAftDiscount(Double sumRecAmountAftDiscount) {
        this.sumRecAmountAftDiscount = sumRecAmountAftDiscount;
    }

    public Double getSumRecPremiumAmt() {
        return sumRecPremiumAmt;
    }

    public void setSumRecPremiumAmt(Double sumRecPremiumAmt) {
        this.sumRecPremiumAmt = sumRecPremiumAmt;
    }

    public Double getSumPremiumRecAmt() {
        return sumPremiumRecAmt;
    }

    public void setSumPremiumRecAmt(Double sumPremiumRecAmt) {
        this.sumPremiumRecAmt = sumPremiumRecAmt;
    }

    public Double getSumPayByAR() {
        return sumPayByAR;
    }

    public void setSumPayByAR(Double sumPayByAR) {
        this.sumPayByAR = sumPayByAR;
    }

    public Double getSumPayByCash() {
        return sumPayByCash;
    }

    public void setSumPayByCash(Double sumPayByCash) {
        this.sumPayByCash = sumPayByCash;
    }

    public Double getAr400() {
        return ar400;
    }

    public void setAr400(Double ar400) {
        this.ar400 = ar400;
    }

    public Double getAr401() {
        return ar401;
    }

    public void setAr401(Double ar401) {
        this.ar401 = ar401;
    }

    public Double getAr402() {
        return ar402;
    }

    public void setAr402(Double ar402) {
        this.ar402 = ar402;
    }

    public Double getAr406() {
        return ar406;
    }

    public void setAr406(Double ar406) {
        this.ar406 = ar406;
    }

    public Double getArAmt() {
        return arAmt;
    }

    public void setArAmt(Double arAmt) {
        this.arAmt = arAmt;
    }

    public Double getCash400() {
        return cash400;
    }

    public void setCash400(Double cash400) {
        this.cash400 = cash400;
    }

    public Double getCash401() {
        return cash401;
    }

    public void setCash401(Double cash401) {
        this.cash401 = cash401;
    }

    public Double getCash402() {
        return cash402;
    }

    public void setCash402(Double cash402) {
        this.cash402 = cash402;
    }

    public Double getCash406() {
        return cash406;
    }

    public void setCash406(Double cash406) {
        this.cash406 = cash406;
    }

    public Double getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(Double cashAmt) {
        this.cashAmt = cashAmt;
    }
    
    public boolean getDataByQuery(String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = this.conn.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.last();
            this.size = rs.getRow();
            rs.beforeFirst();

            this.doctorCodeArry = new String[size];
            this.sumAmountAftDiscountArry = new Double[size];
            this.sumAmountOfDiscountArry = new Double[size];
            this.sumDrAmtArry = new Double[size];
            this.sumDrAmt_400Arry = new Double[size];
            this.sumDrAmt_401Arry = new Double[size];
            this.sumDrAmt_402Arry = new Double[size];
            this.sumDrAmt_406Arry = new Double[size];
            this.sumDrPremiumArry = new Double[size];
            this.sumHpAmtArry = new Double[size];
            this.sumHpPremiumArry = new Double[size];
            this.sumHpTaxArry = new Double[size];
            this.sumRecAmountAftDiscountArry = new Double[size];
            this.sumRecPremiumAmtArry = new Double[size];
            this.sumPremiumRecAmtArry = new Double[size];
            this.sumPayByCashArry = new Double[size];
            this.sumPayByARArry = new Double[size];
            this.cashAmtArry = new Double[size];
            this.cash400Arry = new Double[size];
            this.cash401Arry = new Double[size];
            this.cash402Arry = new Double[size];
            this.cash406Arry = new Double[size];
            this.arAmtArry = new Double[size];
            this.ar400Arry = new Double[size];
            this.ar401Arry = new Double[size];
            this.ar402Arry = new Double[size];
            this.ar406Arry = new Double[size];
            
            int i=0;
            while (rs.next()) {               
                
                this.doctorCodeArry[i] = rs.getString("DOCTOR_CODE");
                this.sumAmountAftDiscountArry[i] = rs.getDouble(2); 
                this.sumAmountOfDiscountArry[i] = rs.getDouble(3); 
                this.sumDrAmtArry[i] = rs.getDouble(4);
                this.sumDrAmt_400Arry[i] = rs.getDouble(5);
                this.sumDrAmt_401Arry[i] = rs.getDouble(6);
                this.sumDrAmt_402Arry[i] = rs.getDouble(7);
                this.sumDrAmt_406Arry[i] = rs.getDouble(8);
                this.sumDrPremiumArry[i] = rs.getDouble(9);
                this.sumHpAmtArry[i] = rs.getDouble(10);
                this.sumHpPremiumArry[i] = rs.getDouble(11);
                this.sumHpTaxArry[i] = rs.getDouble(12);
                this.sumRecAmountAftDiscountArry[i] = rs.getDouble(13);
                this.sumRecPremiumAmtArry[i] = rs.getDouble(14);
                this.sumPremiumRecAmtArry[i] = rs.getDouble(15);
                this.sumPayByCashArry[i] = rs.getDouble(16);
                this.sumPayByARArry[i] = rs.getDouble(17);
                
                this.cashAmtArry[i] = rs.getDouble(18);
                this.cash400Arry[i] = rs.getDouble(19);
                this.cash401Arry[i] = rs.getDouble(20);
                this.cash402Arry[i] = rs.getDouble(21);
                this.cash406Arry[i] = rs.getDouble(22);
                this.arAmtArry[i] = rs.getDouble(23);
                this.ar400Arry[i] = rs.getDouble(24);
                this.ar401Arry[i] = rs.getDouble(25);
                this.ar402Arry[i] = rs.getDouble(26);
                this.ar406Arry[i] = rs.getDouble(27);
                
                i ++;
            }       

        } catch (SQLException e) {
            System.out.println("Error in SummaryMonthlyVar.getDataByQuery() \n" + e.getMessage());
            return false;
        } finally {
               //Clean up resources, close the connection.
                try {
                    if(rs != null) {  rs.close(); rs = null;  }
                    if (stmt != null) {  stmt.close();  stmt = null;   }
                } catch (Exception ignored) { ignored.printStackTrace();   }
        }    
        return true;
    }
    
    public boolean IsFoundAndSetData(String doctorCode) {
        boolean ret = false;
        for (int i=0; i<size; i++) {
            if (this.doctorCodeArry[i].equals(doctorCode)) {
                this.setDoctorCode(this.doctorCodeArry[i]);
                this.setSumAmountAftDiscount(this.sumAmountAftDiscountArry[i]); 
                this.setSumAmountOfDiscount(this.sumAmountOfDiscountArry[i]); 
                this.setSumDrAmt(this.sumDrAmtArry[i]);
                this.setSumDrAmt_400(this.sumDrAmt_400Arry[i]); 
                this.setSumDrAmt_401(this.sumDrAmt_401Arry[i]); 
                this.setSumDrAmt_402(this.sumDrAmt_402Arry[i]); 
                this.setSumDrAmt_406(this.sumDrAmt_406Arry[i]); 
                this.setSumDrPremium(this.sumDrPremiumArry[i]); 
                this.setSumHpAmt(this.sumHpAmtArry[i]);
                this.setSumHpPremium(this.sumHpPremiumArry[i]); 
                this.setSumHpTax(this.sumHpTaxArry[i]);  
                this.setSumRecAmountAftDiscount(this.sumRecAmountAftDiscountArry[i]);
                this.setSumRecPremiumAmt(this.sumRecPremiumAmtArry[i]);
                this.setSumPremiumRecAmt(this.sumPremiumRecAmtArry[i]);
                this.setSumPayByCash(this.sumPayByCashArry[i]);
                this.setSumPayByAR(this.sumPayByARArry[i]);
                
                this.setCashAmt(this.cashAmtArry[i]);
                this.setCash400(this.cash400Arry[i]);
                this.setCash401(this.cash401Arry[i]);
                this.setCash402(this.cash402Arry[i]);
                this.setCash406(this.cash406Arry[i]);
                this.setArAmt(this.arAmtArry[i]);
                this.setAr400(this.ar400Arry[i]);
                this.setAr401(this.ar401Arry[i]);
                this.setAr402(this.ar402Arry[i]);
                this.setAr406(this.ar406Arry[i]);
                
                ret = true;
                break;
            }
        }
        return ret;
    }

    public Double getSumDrAmt_400() {
        return sumDrAmt_400;
    }

    public void setSumDrAmt_400(Double sumDrAmt_400) {
        this.sumDrAmt_400 = sumDrAmt_400;
    }

    public Double getSumDrAmt_401() {
        return sumDrAmt_401;
    }

    public void setSumDrAmt_401(Double sumDrAmt_401) {
        this.sumDrAmt_401 = sumDrAmt_401;
    }
    
}
