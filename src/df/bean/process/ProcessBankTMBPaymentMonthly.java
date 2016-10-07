package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.obj.bank.BankTMB;

public class ProcessBankTMBPaymentMonthly extends Process {
    String startDate = "";
    String endDate = "";
    
    public ProcessBankTMBPaymentMonthly(DBConnection conn) {
        super(conn);
        this.setDBConnection(conn);
    }
    
    public ProcessBankTMBPaymentMonthly() {
        super();
    }
    
    @Override
    protected void finalize() {
    }

    public boolean run(String YYYY, String MM, String payDate, String paymentType, String hospitalCode) {
        if (this.getDBConnection().IsClosed()) {  
            if (!this.getDBConnection().Connect()) { return false;}
        }
        
        this.getDBConnection().beginTrans();
        
        result = this.Calculate(YYYY, MM, payDate, paymentType, hospitalCode);
        if (this.result) {
            this.getDBConnection().commitTrans();
        } else {
            this.getDBConnection().rollBackTrans();
        }         
        
        return result;
    }
    
    public boolean Calculate(String YYYY, String MM, String payDate, String paymentType, String hospitalCode) {
        boolean ret = false;
        
        BankTMB b = new BankTMB(hospitalCode, this.getDBConnection());
        ret = b.CalculateMediaClearing(YYYY, MM, payDate, paymentType, hospitalCode);
        b = null;
        
        return ret;
    } 
    //////////////////////// By doctor /////////////////////
    public boolean run(String YYYY, String MM, String payDate, String paymentType, String hospitalCode, String doctorCode) {
        if (this.getDBConnection().IsClosed()) {  
            if (!this.getDBConnection().Connect()) { return false;}
        }
        
        this.getDBConnection().beginTrans();
        
        result = this.Calculate(YYYY, MM, payDate, paymentType, hospitalCode, doctorCode);
        if (this.result) {
            this.getDBConnection().commitTrans();
        } else {
            this.getDBConnection().rollBackTrans();
        }         
        
        return result;
    }
    
    public boolean Calculate(String YYYY, String MM, String payDate, String paymentType, String hospitalCode, String doctorCode) {
        boolean ret = false;
        
        BankTMB b = new BankTMB(hospitalCode, this.getDBConnection());
        ret = b.CalculateMediaClearing(YYYY, MM, payDate, paymentType, hospitalCode);
        b = null;
        
        return ret;
    } 
}
