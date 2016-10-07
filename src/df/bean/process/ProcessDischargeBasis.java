package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.process.ProcessMaster;

/**
 * 
 * @author MR.sarunyoo Keawsopa
 * @Module Discharge Basic
 */
public class ProcessDischargeBasis implements ProcessMaster {
	private DBConnection db;
	private String hospitalCode;
	private Batch batch;
	private String isProcessDischarge = "";
	/**
	 * @param hospitalCode
	 */
	public ProcessDischargeBasis(String hospitalCode) {
		this.hospitalCode = hospitalCode;
		this.db = new DBConnection();
		this.db.connectToLocal();
		this.batch = new Batch(this.hospitalCode, this.db);
		this.isProcessDischarge = db.executeQueryString("SELECT IS_DISCHARGE_BASIS FROM HOSPITAL WHERE HOSPITAL_CODE = '"+hospitalCode+"'");
	}
	@Override
	public boolean doProcess() {
		boolean action = false;
		try{
			 System.out.println("Out : "+this.queryProcess());
			 if(this.isProcessDischarge.equals("Y")){
				 System.out.println("Discharge Basis");				 
				 if(this.db.executeUpdate(this.queryProcess()) > 0){ 
					 System.out.println("In : "+this.queryProcess());
					 action = true;
				 } else {
					 action = false;
				 }				 
			 }else{
				 action = true;
				 System.out.println("No Discharge Basis");
			 }
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage().toString());
		}
		return action;
	}
	@Override
	public boolean doRollback() {
		boolean action = false;
		try {
			if(this.db.executeUpdate(this.queryRollback()) > 0){
				 action  =  true;
			} else {
				action = false;
			}
			
		} catch(Exception ex){
			ex.printStackTrace();
			System.out.println(ex.getMessage().toString());
		}
		
		return action;
	}
	@Override
	public boolean doBatchClose() {
		return false;
	}
	/**
	 * @return query command GuaranteeDischargeBasic
	 * @Description transaction in guarantee is paid all. But not equal Extra
	 */
	private String queryProcess() {
		return "UPDATE TRN_DAILY SET " + "YYYY = '" + this.batch.getYyyy() + "', " 
				+ "MM = '" + this.batch.getMm() + "', " 
				+ "PAY_BY_CASH = 'Y', "
				+ "RECEIPT_NO = 'DISCHARGE', " + "RECEIPT_DATE = INVOICE_DATE "
				+ "WHERE  TRANSACTION_DATE LIKE '" + this.batch.getYyyy() + this.batch.getMm() + "%'"
				+ "AND GUARANTEE_TERM_MM = '" + this.batch.getMm() + "' "
				+ "AND GUARANTEE_TERM_YYYY = '" + this.batch.getYyyy() + "' "
				//+ "AND GUARANTEE_TYPE_CODE = 'MLY' "
				+ "AND GUARANTEE_NOTE NOT LIKE '%EXTRA%' "
				+ "AND HOSPITAL_CODE = '" + this.hospitalCode + "' "
				+ "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1'"
				+ "AND (MM IS NULL OR MM = '') "
				+ "AND (YYYY IS NULL OR YYYY = '') "
				+ "AND (BATCH_NO IS NULL OR BATCH_NO = '')";
	}
	/**
	 * @return query command roll back
	 */
	private String queryRollback() {
		return "UPDATE TRN_DAILY SET  YYYY = '', MM = '', PAY_BY_CASH = 'N', RECEIPT_NO = '', RECEIPT_DATE = '' "
				+ "WHERE  TRANSACTION_DATE LIKE '" + this.batch.getYyyy() + this.batch.getMm()+ "%' "
				+ "AND RECEIPT_NO = 'DISCHARGE'"
				+ "AND GUARANTEE_TERM_MM = '" + this.batch.getMm() + "' "
				+ "AND GUARANTEE_TERM_YYYY = '" +  this.batch.getYyyy() + "' "
				+ "AND GUARANTEE_NOTE NOT LIKE '%EXTRA%' "
				+ "AND HOSPITAL_CODE = '" + this.hospitalCode + "' "
				+ "AND MM = '" +  this.batch.getMm() + "'  AND YYYY = '" +  this.batch.getYyyy() + "' "
				+ "AND ACTIVE = '1' AND ORDER_ITEM_ACTIVE = '1' " 
				+ "AND (BATCH_NO IS NULL OR BATCH_NO = '') ";
	}
}
