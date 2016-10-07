package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.PayorOffice;
import df.bean.db.table.TRN_Error;

public class ProcessReceiptBean {
	DBConnection conn;
	public ProcessReceiptBean(){
		conn = new DBConnection();
		conn.connectToLocal();
	}
	public void clearPayor(){
		this.conn.Close();
	}
    public boolean CalculateReceiptByPayor(String yyyy, String mm, String hospitalCode, String payorCode) {
        boolean ret = false;
        try {
            ret = updateReceiptByPayor(yyyy, mm, hospitalCode, "TRN_DAILY", payorCode)? true : false;
        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.conn.getConnection(), 
            TRN_Error.PROCESS_RECEIPT_BY_PAYOR,"Receipt By Payor is Error.",e.getMessage());
        }
        return ret;
    }   
    public boolean updateReceiptByPayor(String YYYY, String MM, String hospitalCode, String tableName, String payorCode) {
        boolean ret = false;
        if(this.conn.IsClosed()||this.conn==null){
        	System.out.println("Connection in Pay by Payor Process is null or closed!!!");
        	this.conn.connectToLocal();
        }else{/*not implement*/}
        PayorOffice rec = new PayorOffice(this.conn);
            try {
                ret = rec.updateReceipt(YYYY, MM, hospitalCode, tableName, payorCode) > -1 ? true : false;
            } catch (Exception e) {
                TRN_Error.writeErrorLog(this.conn.getConnection(), 
                TRN_Error.PROCESS_RECEIPT_BY_PAYOR,"Receipt By Payor is Error.",e.getMessage());
            }
            return ret;
    }     
}