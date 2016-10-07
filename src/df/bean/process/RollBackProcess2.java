package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.BankPaymentMonthly;
import df.bean.db.table.SummaryMonthlyStep;
import df.bean.obj.util.DialogBox;
import df.bean.db.table.TrnHeader;

public class RollBackProcess2 {
    private DBConnection conn = null;
    
    public RollBackProcess2(DBConnection conn) {
        this.conn = conn;
    }
    // Bank Payment Monthly

    /*
    public boolean rollBackInvoice(String hospitalCode, String yyyy, String mm, String dd) {
        boolean result = true;
        InvoiceDetail id = new InvoiceDetail(conn);
        InvoiceHeader ih = new InvoiceHeader(conn);
        TcInvoiceHeader tcih = new TcInvoiceHeader(conn);
        TcInvoiceDetail tcid = new TcInvoiceDetail(conn);

        ReceiptHeader rh = new ReceiptHeader(conn);
        ReceiptDetail rd = new ReceiptDetail(conn);

        try {
            conn.beginTrans();
            if (result) { result = id.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = ih.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = rd.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = rh.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = tcih.rollBackUpdate(hospitalCode, yyyy, mm, dd); }
            if (result) { result = tcid.rollBackUpdate(hospitalCode, yyyy, mm, dd); }
            
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
            
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            id = null;
            ih = null;
            tcih = null;
            tcid = null;
            rd = null;
            rh = null;
        }
        
        return result;    
    }
    
    public boolean rollBackTCInvoice(String hospitalCode, String yyyy, String mm, String dd) {
        boolean result = true;
//        Receipt rec = new Receipt(conn);
        TcInvoiceHeader tcih = new TcInvoiceHeader(conn);
        TcInvoiceDetail tcid = new TcInvoiceDetail(conn);
        ImpInvoiceMedtrak imt = new ImpInvoiceMedtrak(conn);
        ImpInvoiceDetail iid = new ImpInvoiceDetail(conn);
        ImpInvoiceHead iih = new ImpInvoiceHead(conn);

        ReceiptHeader rh = new ReceiptHeader(conn);
        ReceiptDetail rd = new ReceiptDetail(conn);
        
        try {
            conn.beginTrans();
//            if (result) { result = rec.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = rh.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = rd.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = tcih.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = tcid.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = imt.rollBackUpdate(hospitalCode, yyyy, mm, dd); }
            if (result) { result = iid.rollBackUpdate(hospitalCode, yyyy, mm, dd); }
            if (result) { result = iih.rollBackUpdate(hospitalCode, yyyy, mm, dd); }
            
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
            
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
//            rec = null;
            tcih = null;
            tcid = null;
            imt = null;
            iid = null;
            iih = null;
            rh = null;
            rd = null;
        }
        
        return result;    
    }
    
    
    public boolean rollBackSummaryDailyByDateAndDoctorCode(String hospitalCode, String sYYYY, String sMM, String sDD, String eYYYY, String eMM, String eDD, String doctorCode) {
        boolean result = true;
        SummaryDaily sd = new SummaryDaily(conn);
        InvoiceDetail id = new InvoiceDetail(conn);
        InvoiceHeader ih = new InvoiceHeader(conn);

        ReceiptHeader rh = new ReceiptHeader(conn);
        ReceiptDetail rd = new ReceiptDetail(conn);
        
        try {
            conn.beginTrans();
            if (result) { result = sd.rollBackDeleteByDateAndDoctorCode(hospitalCode, sYYYY, sMM, sDD, eYYYY, eMM, eDD, doctorCode); }
            if (result) { result = id.rollBackUpdateByDateAndDoctorCode(hospitalCode, sYYYY, sMM, sDD, eYYYY, eMM, eDD, doctorCode); }
            if (result) { result = ih.rollBackUpdateByDateAndDoctorCode(hospitalCode, sYYYY, sMM, sDD, eYYYY, eMM, eDD); }
            if (result) { result = rd.rollBackUpdateByDateAndDoctorCode(hospitalCode, sYYYY, sMM, sDD, eYYYY, eMM, eDD, doctorCode); }
            if (result) { result = rh.rollBackUpdateByDateAndDoctorCode(hospitalCode, sYYYY, sMM, sDD, eYYYY, eMM, eDD); }
            
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
            
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            sd = null;
            id = null;
            ih = null;
            rd = null;
            rh = null;
        }
        
        return result;    
    }
    */
}
