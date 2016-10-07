package df.bean.obj.doctor;

import df.bean.db.conn.DBConnection;

// ��Ң�鹺ѹ� ��Դ��� premium
public class DrBeauty extends DrFullTime {
    
    public DrBeauty () {
        super();
    }
    public DrBeauty(String doctorCode, String hospitalCode, DBConnection conn) {
        super(doctorCode, hospitalCode, conn);
    }    
    
    // �ӹǳ�Ҥ�� Premium
    @Override
    public boolean computePremium() {
        try {
            // Premium 
            double pctOfCharge = 0, pctOfVat = 0, drPremium = 0, hpPremium = 0, totOfPremium = 0;
            
            // if patient was paid by credit card 
            pctOfCharge = this.lPremiumChargePct / 100;
            totOfPremium = (this.lInvAmt * (pctOfCharge + (pctOfCharge * pctOfVat)));
            drPremium = (this.lDrAmt * (pctOfCharge + (pctOfCharge * pctOfVat)));

    //            this.lPremiumChargePct = receipt.getReceiptType().getReceiptType().getPercentOfCharge();
    //            this.lPremiumVatPct = receipt.getReceiptType().getReceiptType().getPercentOfVat();
            this.lDrPremium = Double.parseDouble((new java.text.DecimalFormat("0.00")).format(drPremium));
            this.lTotPremiumAmt = Double.parseDouble((new java.text.DecimalFormat("0.00")).format(totOfPremium));
            hpPremium = this.lTotPremiumAmt - this.lDrPremium;
            this.lHpPremium = Double.parseDouble((new java.text.DecimalFormat("0.00")).format(hpPremium));
            this.lPremiumRecAmt = this.lRecAmt * this.lPremiumChargePct / 100; 
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
}
