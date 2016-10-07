/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;

/**
 *
 * @author T.
 */
public class ProcessBatchBean {
    private Batch bt;
    private DBConnection conn;
    private String MM = "";
    private String YYYY = "";
    private String batchNo = "";
    private Boolean statusBatch = false;
    private String hosp = "";
    private String user = "";
    private String payment_term = "";
    
    public ProcessBatchBean(String hospitalCode){
        conn = new DBConnection();
        conn.connectToLocal();
        bt = new Batch(hospitalCode, conn);
        this.setBatchNo(bt.getBatchNo());
        this.setMM(bt.getBatchNo().substring(4, 6));
        this.setYYYY(bt.getBatchNo().substring(0, 4));
        this.setHosp(hospitalCode);
    }
	public void setPayment_term(String paymentTerm) {
		payment_term = paymentTerm;
	}
	public void setUser(String user){
    	this.user = user;
    }
    private String getHosp() {
        return hosp;
    }
    public void setHosp(String hosp) {
        this.hosp = hosp;
    }
    private String getMM() {
        return MM;
    }
    private String getYYYY() {
        return YYYY;
    }
    private String getBatchNo() {
        return batchNo;
    }
    private void setMM(String MM) {
        this.MM = MM;
    }
    private void setYYYY(String YYYY) {
        this.YYYY = YYYY;
    }
    private void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    private void setStatusBatch(Boolean statusBatch) {
        this.statusBatch = statusBatch;
    }
    private Boolean getStatusBatch() {
        return statusBatch;
    }
    private void processBatch(){
        String cmd_trn_daily = "";
        String cmd_trn_expense_detail = "";
        String cmd_stp_guarantee = "";
        String cmd_onward = "";
        String cmd_del_onward = "";

        conn.beginTrans();
        try{
            cmd_stp_guarantee = "UPDATE STP_GUARANTEE SET BATCH_NO='"+ this.getBatchNo() +
            "' WHERE  HOSPITAL_CODE='"+ this.getHosp() +"' AND MM='"+this.getMM()+"' AND YYYY='"+this.getYYYY()+"'";

            cmd_trn_expense_detail = "UPDATE TRN_EXPENSE_DETAIL SET BATCH_NO='"+ this.getBatchNo() +
            "' WHERE  HOSPITAL_CODE='"+ this.getHosp() +"' AND MM='"+this.getMM()+"' AND YYYY='"+this.getYYYY()+"'";

            cmd_trn_daily = "UPDATE TRN_PAYMENT SET BATCH_NO='"+ this.getBatchNo() +
            "' WHERE  HOSPITAL_CODE='"+ this.getHosp() +"' AND MM='"+this.getMM()+"' AND YYYY='"+this.getYYYY()+"'";
            
    		cmd_onward = "INSERT INTO TRN_ONWARD SELECT * FROM TRN_DAILY "+
    		"WHERE IS_ONWARD = 'Y' AND TRANSACTION_DATE LIKE '"+(this.getYYYY()+this.getMM())+
    		"%' AND HOSPITAL_CODE = '"+this.getHosp()+"' AND YYYY = '' AND GUARANTEE_PAID_AMT = 0";
    		
    		cmd_del_onward = "DELETE FROM TRN_DAILY "+
    		"WHERE IS_ONWARD = 'Y' AND TRANSACTION_DATE LIKE '"+(this.getYYYY()+this.getMM())+
    		"%' AND HOSPITAL_CODE = '"+this.getHosp()+"' AND YYYY = '' AND GUARANTEE_PAID_AMT = 0";

            //System.out.println("Insert Onward to temp : "+conn.executeUpdate(cmd_onward));			
            //System.out.println("Delete Onward : "+conn.executeUpdate(cmd_del_onward));			
            //System.out.println("Setup Guarantee Close Batch : "+ conn.executeUpdate(cmd_stp_guarantee));
            //System.out.println("Expenese Detail Close Batch : "+conn.executeUpdate(cmd_trn_expense_detail));
    		//System.out.println("Transaction Close Batch : "+ conn.executeUpdate(cmd_trn_daily));

    		bt.setCreateByUserId(this.user);
            if(bt.closeBATCH() && bt.createBATCH()){
                this.setStatusBatch(true);
                conn.commitTrans();
            }else{
               conn.rollBackTrans();
               this.setStatusBatch(false);
            }
        }catch(Exception err){
            conn.rollBackTrans();
            this.setStatusBatch(false);
            System.out.println(err.getMessage());
        }
    }
    public Boolean statusBatch(){
        try{
        	if(this.payment_term.equals("1")){
        		System.out.println("Payment Term : "+this.payment_term);
                this.setStatusBatch(true);
        	}else{
        		System.out.println("Payment Term : "+this.payment_term);
                this.setStatusBatch(true);
        		//this.processBatch();
        	}
        }catch(Exception err){
            this.setStatusBatch(false);
            System.out.println(err.getMessage());
        }
        return this.getStatusBatch();
    }
}
