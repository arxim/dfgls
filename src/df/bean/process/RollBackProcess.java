package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.BankPaymentMonthly;
import df.bean.db.table.InvoiceDetail;
import df.bean.db.table.InvoiceHeader;
import df.bean.db.table.SummaryDaily;
import df.bean.db.table.SummaryMonthlyStep;
import df.bean.obj.util.DialogBox;
import df.bean.db.table.ReceiptDetail;
import df.bean.db.table.ReceiptHeader;

public class RollBackProcess {
    private DBConnection conn = null;
    
    
    public RollBackProcess(DBConnection conn) {
        this.conn = conn;
    }
    // Bank Payment Monthly


    public boolean rollBackSummaryDaily(String hospitalCode, String yyyy, String mm, String dd) {
        boolean result = true;
        SummaryDaily sd = new SummaryDaily(conn);
        InvoiceDetail id = new InvoiceDetail(conn);
        InvoiceHeader ih = new InvoiceHeader(conn);
        
        ReceiptHeader rh = new ReceiptHeader(conn);
        ReceiptDetail rd = new ReceiptDetail(conn);
        
        try {
            conn.beginTrans();
//            if (result) { result = sd.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = id.rollBackUpdate(hospitalCode, yyyy, mm, dd); }
            if (result) { result = ih.rollBackUpdate(hospitalCode, yyyy, mm, dd); }
            if (result) { result = rh.rollBackUpdate(hospitalCode, yyyy, mm, dd); }
            if (result) { result = rd.rollBackUpdate(hospitalCode, yyyy, mm, dd); }
            
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
            
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            sd = null;
            id = null;
            ih = null;
            rh = null;
            rd = null;
        }
        
        return result;    
    }
    
    public boolean rollBackInvoice(String hospitalCode, String yyyy, String mm, String dd) {
        boolean result = true;
        InvoiceDetail id = new InvoiceDetail(conn);
        InvoiceHeader ih = new InvoiceHeader(conn);

        ReceiptHeader rh = new ReceiptHeader(conn);
        ReceiptDetail rd = new ReceiptDetail(conn);

        try {
            conn.beginTrans();
            if (result) { result = id.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = ih.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = rd.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            if (result) { result = rh.rollBackDelete(hospitalCode, yyyy, mm, dd); }
            
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
            
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            id = null;
            ih = null;
            rd = null;
            rh = null;
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
//            if (result) { result = sd.rollBackDeleteByDateAndDoctorCode(hospitalCode, sYYYY, sMM, sDD, eYYYY, eMM, eDD, doctorCode); }
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
    
    
    
}
